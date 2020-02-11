/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.vid.services;

import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.model.CategoryParameterOption;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Primary
public class CategoryParameterServiceImpl implements CategoryParameterService {

    public static final String OPTION_ALREADY_EXIST_FOR_CATEGORY = "Option %s already exist for category %s";
    @Autowired
    private DataAccessService dataAccessService;

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(CategoryParameterServiceImpl.class);

    public static class UnfoundedCategoryException extends RuntimeException {

        public UnfoundedCategoryException(String message) {
            super(message);
        }
    }

    public static class UnfoundedCategoryOptionException extends RuntimeException {

        public UnfoundedCategoryOptionException(String message) {
            super(message);
        }
    }

    public static class AlreadyExistOptionNameException extends RuntimeException {

        public AlreadyExistOptionNameException(String message) {
            super(message);
        }
    }

    @Override
    public CategoryParametersResponse getCategoryParameters(Family familyName) {
        List<CategoryParameter> categoryParameters = dataAccessService.getList(CategoryParameter.class, String.format(" where family = '%s' ",familyName), null, null);
        return convertToCategoryParametersResponse(categoryParameters);
    }

    private CategoryParametersResponse convertToCategoryParametersResponse(List<CategoryParameter> categoryParameters) {
        Comparator<CategoryParameterOptionRep> comparator = Comparator.comparing(CategoryParameterOptionRep::getName, String.CASE_INSENSITIVE_ORDER);
        Map<String, List<CategoryParameterOptionRep>> categoryParametersMap = categoryParameters.stream().collect(Collectors.toMap(
                CategoryParameter::getName,
                x ->  x.getOptions().stream().map(opt -> new CategoryParameterOptionRep(opt.getAppId(), opt.getName())).sorted(comparator).collect(Collectors.toList())));
        return new CategoryParametersResponse(categoryParametersMap);
    }

    @Override
    public AddCategoryOptionResponse createCategoryParameterOptions(String categoryName, AddCategoryOptionsRequest optionsRequest) {

        AddCategoryOptionResponse response = new AddCategoryOptionResponse(new ArrayList<>());
        CategoryParameter categoryParameter = getCategoryParameter(categoryName);
        Set<String> categoryOptions = categoryParameter.getOptions().stream().map(CategoryParameterOption::getName).collect(Collectors.toSet());
        for (String optionName : optionsRequest.options) {
            if (categoryOptions.contains(optionName)) {
                response.getErrors().add(String.format(OPTION_ALREADY_EXIST_FOR_CATEGORY, optionName, categoryName));
                continue;
            }
            String appId = categoryParameter.isIdSupported() ? UUID.randomUUID().toString() : optionName;
            CategoryParameterOption categoryParameterOption = new CategoryParameterOption(appId, optionName, categoryParameter);
            dataAccessService.saveDomainObject(categoryParameterOption, null);
        }

        return response;
    }

    private CategoryParameter getCategoryParameter( String categoryName) {
        List<CategoryParameter> categoryParameters = dataAccessService.getList(CategoryParameter.class, String.format(" where name = '%s' ", categoryName), null, null);
        if (categoryParameters.size() != 1) {
            String msg = "There is no category parameter with name " + categoryName;
            LOG.debug(msg);
            throw new UnfoundedCategoryException(msg);
        }


        return categoryParameters.get(0);
    }

    @Override
    public AddCategoryOptionResponse updateCategoryParameterOption(String categoryName, CategoryParameterOptionRep option) {
        AddCategoryOptionResponse response = new AddCategoryOptionResponse(new ArrayList<>());
        CategoryParameter categoryParameter = getCategoryParameter(categoryName);
        if (!categoryParameter.isIdSupported()) {
            String msg = "Updating option name for category: " + categoryName + ", is not allowed";
            LOG.debug(msg);
            throw new ForbiddenException(msg);
        }
        Optional<CategoryParameterOption> categoryParameterOptionOptional = categoryParameter.getOptions().stream().filter(x->x.getAppId().equals(option.getId())).findFirst();
        if (!categoryParameterOptionOptional.isPresent()) {
            String msg = "There is no option with id "+option.getId() + " for category " + categoryName;
            LOG.debug(msg);
            throw new UnfoundedCategoryOptionException(msg);
        }
        CategoryParameterOption categoryParameterOption = categoryParameterOptionOptional.get();
        Optional<CategoryParameterOption> alreadyExistOptionWithName = categoryParameter.getOptions().stream().filter(x->x.getName().equals(option.getName())).findFirst();
        if (alreadyExistOptionWithName.isPresent() && !alreadyExistOptionWithName.get().getAppId().equals(categoryParameterOption.getAppId())) {
            String msg = "Option with name "+option.getName() + " already exist for category " + categoryName;
            LOG.debug(msg);
            throw new AlreadyExistOptionNameException(msg);
        }

        categoryParameterOption.setName(option.getName());
        dataAccessService.saveDomainObject(categoryParameterOption, null);

        return response;
    }

    @Override
    public void deleteCategoryOption(String categoryName, CategoryParameterOption option) {
        List<CategoryParameter> categoryParameters = dataAccessService.getList(CategoryParameter.class, String.format(" where name = '%s'", categoryName), null, null);
        if (categoryParameters.size() != 1) {
            String msg = "There is no category parameter with name " + categoryName;
            LOG.debug(msg);
            throw new UnfoundedCategoryException(msg);
        }
        CategoryParameter categoryParameter = categoryParameters.get(0);
        Set<CategoryParameterOption> categoryOptions = categoryParameter.getOptions();
        for (CategoryParameterOption categoryOption: categoryOptions) {
            if(categoryOption.getName().equals(option.getName()))
            {
                dataAccessService.deleteDomainObject(categoryOption, null);
            }
        }
    }

}


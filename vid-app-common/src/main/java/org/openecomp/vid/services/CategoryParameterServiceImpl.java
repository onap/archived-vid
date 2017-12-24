package org.openecomp.vid.services;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.vid.category.AddCategoryOptionResponse;
import org.openecomp.vid.category.CategoryParameterOptionRep;
import org.openecomp.vid.model.CategoryParameter;
import org.openecomp.vid.model.CategoryParameterOption;
import org.openecomp.vid.category.AddCategoryOptionsRequest;
import org.openecomp.vid.category.CategoryParametersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;

@Service
public class CategoryParameterServiceImpl implements CategoryParameterService {

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
    public CategoryParametersResponse getCategoryParameters() throws IOException {
        List<CategoryParameter> categoryParameters = dataAccessService.getList(CategoryParameter.class, null);
        return convertToCategoryParametersResponse(categoryParameters);
    }

    private CategoryParametersResponse convertToCategoryParametersResponse(List<CategoryParameter> categoryParameters) {
        Map<String, List<CategoryParameterOptionRep>> categoryParametersMap = categoryParameters.stream().collect(Collectors.toMap(
                CategoryParameter::getName,
                x -> x.getOptions().stream().map(opt -> new CategoryParameterOptionRep(opt.getAppId(), opt.getName())).collect(Collectors.toList())));
        return new CategoryParametersResponse(categoryParametersMap);
    }

    @Override
    public AddCategoryOptionResponse createCategoryParameterOptions(String categoryName, AddCategoryOptionsRequest optionsRequest) throws IOException, UnfoundedCategoryException {

        AddCategoryOptionResponse response = new AddCategoryOptionResponse(new ArrayList<>());
        CategoryParameter categoryParameter = getCategoryParameter(categoryName);
        Set<String> categoryOptions = categoryParameter.getOptions().stream().map(CategoryParameterOption::getName).collect(Collectors.toSet());
        for (String optionName : optionsRequest.options) {
            if (categoryOptions.contains(optionName)) {
                response.getErrors().add(String.format("Option %s already exist for category %s", optionName, categoryName));
                continue;
            }
            String appId = categoryParameter.isIdSupported() ? UUID.randomUUID().toString() : optionName;
            CategoryParameterOption categoryParameterOption = new CategoryParameterOption(appId, optionName, categoryParameter);
            dataAccessService.saveDomainObject(categoryParameterOption, null);
        }

        return response;
    }

    private CategoryParameter getCategoryParameter(String categoryName) {
          List<CategoryParameter> categoryParameters = dataAccessService.getList(CategoryParameter.class, String.format(" where name = '%s'", categoryName), null, null);
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
    public void deleteCategoryOption(String categoryName, CategoryParameterOption option) throws IOException {
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


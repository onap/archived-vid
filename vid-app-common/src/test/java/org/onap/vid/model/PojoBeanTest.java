/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.SetUtils;
import org.onap.vid.testUtils.TestUtils;
import org.springframework.util.ClassUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PojoBeanTest {

    @Test(dataProvider = "modelClasses")
    public void givenPojoWithNoArgsConstructor_hasValidGettersAndSetters(String name, Class<?> clazz,
        Collection<String> propertiesToIgnore) {
        assertThat(name, clazz, hasValidGettersAndSettersExcluding(propertiesToIgnore.toArray(new String[]{})));
    }

    @DataProvider
    public static Object[][] modelClasses() throws IOException {
        ClassPath classPath = ClassPath.from(PojoBeanTest.class.getClassLoader());
        ImmutableSet<ClassInfo> classes1 = classPath.getTopLevelClassesRecursive("org.onap.vid.aai.model");
        ImmutableSet<ClassInfo> classes2 = classPath.getTopLevelClassesRecursive("org.onap.vid.model");

        return SetUtils.union(classes1, classes2).stream()
            .filter(it -> classNotIgnored(it))
            .filter(it -> packageNotIgnored(it))
            .filter(it -> notTestClass(it))
            .map(it -> it.load())
            .filter(it -> notAbstractClass(it))
            .filter(it -> hasNoArgsConstructor(it))
            .map(it -> array(it.getName(), it, propertiesToIgnore(it)))
            .toArray(Object[][]::new);
    }

    @BeforeClass
    public void beforeClass() {
        TestUtils.registerVidNotionsValueGenerator();
    }

    private static final ImmutableSet<String> classesToSkip = ImmutableSet.of(
        /*
         * Reasons vary; some are using Kotlin overrides, others are with setJson setters,
         * and others are just not POJOs per-se.
         */
        "org.onap.vid.model.ProxyResponse",
        "org.onap.vid.model.PombaInstance.ServiceInstance",
        "org.onap.vid.model.SOWorkflowList",
        "org.onap.vid.model.SOWorkflows",
        "org.onap.vid.model.SOWorkflowParameterDefinitions",
        "org.onap.vid.model.LocalWorkflowParameterDefinitions",
        "org.onap.vid.model.aaiTree.*",
        "org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse",
        "org.onap.vid.aai.model.OwningEntityResponse",
        "org.onap.vid.aai.model.PnfProperties",
        "org.onap.vid.aai.model.PnfResult",
        "org.onap.vid.aai.model.VnfResult",
        "org.onap.vid.aai.model.SimpleResult"
    );

    private static final ImmutableSetMultimap<String, String> propertiesToIgnore = ImmutableSetMultimap.<String, String>builder()
        .putAll("org.onap.vid.model.ResourceInfo", "errorMessageRaw")
        .putAll("org.onap.vid.model.JobAuditStatus", "createdDate", "additionalProperties")
        .putAll("org.onap.vid.aai.model.Properties", "isPortMirrored", "portMirrored", "additionalProperties")
        .putAll("org.onap.vid.model.NewServiceModel", "serviceProxies", "configurations")
        .putAll("org.onap.vid.model.CategoryParameter", "options")
        .build();

    private static Object propertiesToIgnore(Class<?> it) {
        List<String> defaultPropertiesToIgnore = list("additionalProperties");

        return propertiesToIgnore.containsKey(it.getName())
            ? propertiesToIgnore.get(it.getName())
            : defaultPropertiesToIgnore;
    }

    private static boolean hasNoArgsConstructor(Class<?> it) {
        return ClassUtils.hasConstructor(it);
    }

    private static boolean notAbstractClass(Class<?> it) {
        return !Modifier.isAbstract(it.getModifiers());
    }

    private static boolean notTestClass(ClassInfo it) {
        return !it.getName().endsWith("Test");
    }

    private static boolean packageNotIgnored(ClassInfo classInfo) {
        return !classesToSkip.contains(classInfo.getPackageName() + ".*");
    }

    private static boolean classNotIgnored(ClassInfo classInfo) {
        return !classesToSkip.contains(classInfo.getName());
    }


}
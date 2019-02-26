/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.model.aaiTree;

import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.factory.javassist.JavassistProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.exceptions.GenericUncheckedException;

public class FailureAAITreeNode extends AAITreeNode {

    private static final Class[] classes = {AAITreeNode.class};
    private static final JavassistProxyFactory proxyFactory = new JavassistProxyFactory(); // can proxy a class without an interface

    private FailureAAITreeNode() {
        // don't instantiate this class
    }

    /*
    Returns a new AAITreeNode instance that throws an exception when
    invoking any of its methods
     */
    public static AAITreeNode of(Exception failureException) {
        return (AAITreeNode) proxyFactory.createInvokerProxy(exceptionThrower(failureException), classes);
    }

    @NotNull
    private static Invoker exceptionThrower(Exception exception) {
        return (self, thisMethod, args) -> {
            throw new GenericUncheckedException("AAI node fetching failed.", exception);
        };
    }
}

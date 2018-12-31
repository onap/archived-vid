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
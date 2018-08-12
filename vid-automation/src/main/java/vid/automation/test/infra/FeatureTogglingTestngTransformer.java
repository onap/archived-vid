package vid.automation.test.infra;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.togglz.core.context.StaticFeatureManagerProvider;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/*
In order to skip test classes regards the state of feature flag we add this listener to our testng configuration
There are 2 ways to annotate that tests required featureFlags to be active :
In method level - with @FeatureTogglingTest on the test method and list of Required Feature flags on
In Class level - with @FeatureTogglingTest on the test class and list of Required Feature flags on
For each test annotation of method level, we check if the test shall whole class shall run regards the features flag test.
Pay attention that this listener shall be configured in the testng.xml (or command line)
It can't be used as Listener annotation of base class
*/
public class FeatureTogglingTestngTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

            if (testMethod!=null) {
                try {

                    if (!annotation.getEnabled()) {
                        return;
                    }

                    if (isIgnoreTest(testMethod)) {
                        disableTest(annotation, testMethod.getDeclaringClass().getName());
                        return;
                    }

                    if (isIgnoreTest(testMethod.getDeclaringClass())) {
                        disableTest(annotation, testMethod.getDeclaringClass().getName());
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private boolean isIgnoreTest(AnnotatedElement annotatedElement) {

        return (annotatedElement.isAnnotationPresent(FeatureTogglingTest.class) &&
            shallDisableTest(annotatedElement.getAnnotation(FeatureTogglingTest.class)));

    }

    private boolean shallDisableTest(FeatureTogglingTest featureTogglingTest) {
        if (featureTogglingTest.value().length==0) {
            return false;
        }
        if (new StaticFeatureManagerProvider().getFeatureManager()==null) {
            FeaturesTogglingConfiguration.initializeFeatureManager();
        }
        for (Features feature : featureTogglingTest.value()) {
            if (!(feature.isActive()==featureTogglingTest.flagActive())) {
                return true;
            }
        }
        return false;
    }

    private void disableTest(ITestAnnotation annotation, String name) {
        System.out.println("Ignore "+ name+" due to feature flags configuration");
        annotation.setEnabled(false);
    }

}


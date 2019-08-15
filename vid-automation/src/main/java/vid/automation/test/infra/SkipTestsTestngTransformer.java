package vid.automation.test.infra;

import java.time.LocalDate;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.togglz.core.context.StaticFeatureManagerProvider;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/*
This transformer skip test we want to ignore during running VID tests.
Pay attention that this listener shall be configured in the testng.xml (or command line)
It can't be used as Listener annotation of base class

FeatureTogglingTest:
There are 2 ways to annotate that tests required featureFlags to be active :
In method level - with @FeatureTogglingTest on the test method and list of Required Feature flags on
In Class level - with @FeatureTogglingTest on the test class and list of Required Feature flags on
For each test annotation of method level, we check if the test shall whole class shall run regards the features flag test.

SkipTestUntil:
If test annotated with SkipTestUntil the transformer check if the due date has already pass

*/
public class SkipTestsTestngTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

            if (testMethod!=null) {
                try {

                    if (!annotation.getEnabled()) {
                        return;
                    }

                    if (isIgnoreFeatureToggledTest(testMethod)) {
                        disableTest(annotation, testMethod.getName());
                        return;
                    }

                    if (isIgnoreFeatureToggledTest(testMethod.getDeclaringClass())) {
                        disableTest(annotation, testMethod.getDeclaringClass().getName());
                        return;
                    }

                    if (isIgnoreSkipUntilTest(testMethod)) {
                        disableTest(annotation, testMethod.getName());
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private boolean isIgnoreFeatureToggledTest(AnnotatedElement annotatedElement) {

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
        System.out.println("Ignore "+ name+" due to annotation");
        annotation.setEnabled(false);
    }

    private boolean isIgnoreSkipUntilTest(AnnotatedElement annotatedElement) {
        if (!annotatedElement.isAnnotationPresent(SkipTestUntil.class)) {
            return false;
        }

        String dateAsStr = annotatedElement.getAnnotation(SkipTestUntil.class).value();
        try {
            return LocalDate.now().isBefore(LocalDate.parse(dateAsStr));
        }
        catch (RuntimeException exception) {
            System.out.println("Failure during processing of SkipTestUntil annotation value is " + dateAsStr);
            exception.printStackTrace();
            return false;
        }
    }
}


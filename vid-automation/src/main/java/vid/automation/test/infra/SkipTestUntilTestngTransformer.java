package vid.automation.test.infra;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/*
TestNg listener that skip tests that are annotated with SkipTestUntil annotation
Pay attention that this listener shall be configured in the testng.xml (or command line)
*/
public class SkipTestUntilTestngTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

            if (testMethod!=null) {
                try {

                    if (!annotation.getEnabled()) {
                        return;
                    }

                    if (!testMethod.isAnnotationPresent(SkipTestUntil.class)) {
                        return;
                    }

                    String dateAsStr = testMethod.getAnnotation(SkipTestUntil.class).value();
                    if (shallDisableTest(dateAsStr)) {
                        disableTest(annotation, testMethod.getDeclaringClass().getName(), dateAsStr);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private boolean shallDisableTest(String dateAsStr) {
        try {
            return LocalDate.now().isBefore(LocalDate.parse(dateAsStr));
        }
        catch (RuntimeException exception) {
            System.out.println("Failure during processing of SkipTestUntil annotation value is " + dateAsStr);
            exception.printStackTrace();
            return false;
        }
    }

    private void disableTest(ITestAnnotation annotation, String name, String dateAsStr) {
        System.out.println("Ignore "+ name+" till "+dateAsStr);
        annotation.setEnabled(false);
    }

}


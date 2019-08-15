package vid.automation.test.infra;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Skip test until date (AKA TimeBomb)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface SkipTestUntil {

    /**
     * Date in the form of "2007-12-20"
     */
    String value();

}

package vid.automation.test.infra;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/*
In order to skip test classes regards the state of feature flag please use this annotation
There are 2 ways to annotate that tests required featureFlags to be active :
In method level - with @FeatureTogglingTest on the test method and list of Required Feature flags on
In Class level - with @FeatureTogglingTest on the test class and list of Required Feature flags on
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE})
public @interface FeatureTogglingTest {

    /**
     * @return list of feature flags relevant to the test
     */
    Features[] value();

    /**
     * @return if all features shall be active.
     * If true test would run if all features are active.
     * If false test would run if all features are not active.
     */
    boolean flagActive() default true;
}

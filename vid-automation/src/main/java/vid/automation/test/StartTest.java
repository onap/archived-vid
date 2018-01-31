package vid.automation.test;

import org.testng.TestNG;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by itzikliderman on 21/06/2017.
 */
public class StartTest {
    public static boolean debug = false;

    public static AtomicBoolean loggerInitialized = new AtomicBoolean(false);

    protected static Logger logger = null;

    public static void main (String[] args) throws IOException {
        String debugEnabled = System.getProperty("debug");
        if (debugEnabled != null && debugEnabled.equalsIgnoreCase("true")) {
            debug = true;
        }
        System.out.println("Debug mode is " + (debug ? "enabled" : "disabled"));

        enableLogger();

        TestNG testng = new TestNG();

        List<String> suites = new ArrayList<String>();
        suites.add(args[0]);
        testng.setTestSuites(suites);
        testng.setUseDefaultListeners(true);
        testng.setOutputDirectory("target/");

        testng.run();
    }

    public StartTest() {
        logger = Logger.getLogger(StartTest.class.getName());
    }

    public static void enableLogger() {

        if (false == loggerInitialized.get()) {

            loggerInitialized.set(true);

            String log4jPropsFile = System.getProperty("log4j.configuration");
//            if (System.getProperty("os.name").contains("Windows")) {
                String logProps = "src/main/resources/ci/conf/log4j.properties";
                if (log4jPropsFile == null) {
                    System.setProperty("targetlog", "target/");
                    log4jPropsFile = logProps;
                }

//            }
            PropertyConfigurator.configureAndWatch(log4jPropsFile);

        }
    }

}

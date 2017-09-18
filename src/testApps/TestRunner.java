package testApps;

import java.util.Arrays;
import org.testng.TestNG;
import org.testng.xml.*;
/**
 * Created by ygt on 16/08/16.
 */
public class TestRunner {
    static TestNG testng;

    public static void main(String[] args) {

        try {
            testng = new TestNG();
            testng.setPreserveOrder(true);
            testng.setVerbose(2);

            // create a suite programmatically
            XmlSuite suite = new XmlSuite();
            suite.setName("Programmatic XmlSuite");

            // create a test case for the suite
            XmlTest xmltest = new XmlTest(suite);
            xmltest.setName("Test 1");
            xmltest.setXmlClasses(Arrays.asList(new XmlClass("testApps.DriverApp")));

            // add a suite-file to the suite
//            suite.setSuiteFiles(Arrays.asList("./Suite1.xml"));

            // 1. To run with testng.xml file, uncomment this one, comment 2
            // testng.setTestSuites(Arrays.asList("testng.xml"));

            // 2. to run with XmlSuite, uncomment this one, comment 1
            testng.setXmlSuites(Arrays.asList(suite));

            testng.run();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}

package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class RegressionTest {

    @Test
    public void runRegressionTests() {
        // Create TestNG instance

        // ================= START MESSAGE =================
        System.out.println("================+ REGRESSION TESTING START +================" );

        TestNG testng = new TestNG();

        // Add classes in the specific order
        List<Class<?>> classes = new ArrayList<>();
        classes.add(HomepageTest.class);
        classes.add(CookiesTest.class);
        classes.add(SitemapTest.class);

        testng.setTestClasses(classes.toArray(new Class[0]));
        testng.run();

        // ================= END MESSAGE =================
        System.out.println("================+ REGRESSION TESTING END +================" );
    }
}

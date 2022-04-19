package TestPlanKnownFailures;

import com.amazon.dtk.configuration.DTKProperties;
import org.junit.platform.launcher.TestPlan;
import org.testng.annotations.Test;
import pages.TestPlanModel;
import pages.TestrailPageModel;
import utils.Constants;
import utils.EmailReporter;
import utils.TestBase;

import javax.mail.MessagingException;

public class TestPlanKnownFailures extends TestBase {

    @Test
    public void knownFailuresTest() throws InterruptedException, MessagingException {
        System.out.println("Test Starting");
        TestPlanModel testPlanModel = new TestPlanModel(driver);
        testPlanModel.initializeSignIn();
        loadTestPlanUrl();
        testPlanModel.knownFailuresCount();
    }
}
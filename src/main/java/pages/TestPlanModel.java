package pages;

import com.amazon.dtk.element.PageElement;
import com.amazon.dtk.locator.LocatorDataType;
import com.amazon.dtk.page.Locator;
import com.amazon.dtk.page.PageName;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import utils.Constants;
import utils.PageElementCreator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.TestBase.testPlanURLConstructor;


@PageName(value = "Testplan.json", type = LocatorDataType.JSON)
public class TestPlanModel extends TestrailPageModel {

    @Locator("TESTRAIL_LOGO")
    protected PageElement testrailLogo;

    @Locator("TESTRAIL_LOGIN_EMAIL")
    protected PageElement loginMailId;

    @Locator("TESTRAIL_LOGIN_PASSWORD")
    protected PageElement loginPassword;

    @Locator("TESTRAIL_LOGIN_SUBMIT")
    protected PageElement loginSubmit;

    @Locator("TESTRUN_COUNT")
    protected PageElement testRunCount;

    @Locator("TESTCASES_IN_TESTRUN")
    protected PageElement testCasesCount;

    @Locator("TESTRUN_NEXT_PAGE")
    protected PageElement testRunNextPage;

    @Locator("KNOWN_FAILURE_STATUS_COUNT")
    protected PageElement knownFailureSimIdCount;

    @Locator("KNOWN_FAILURE_SIMID")
    protected PageElement knownFailureSimId;

    @Locator("TESTCASE_COUNT")
    protected PageElement testCaseIDCount;

    /**
     * Constructor for Add address Page
     *
     * @param driver An instance of {@link WebDriver}
     */

    Set<String> issueID = new HashSet<>();

    public TestPlanModel(@NonNull final WebDriver driver) {
        super(driver);
    }

    public boolean isValidPage() {
        return loginMailId.waitIsPresent();
    }


    public void testRunCount() throws InterruptedException {
        List<PageElement> testRunList = testRunCount.getElements();
        int totalRun = testRunList.size();
        System.out.println(totalRun + "check1");
        for (int count = 1; count <= totalRun; count++) {
            String testPlanTestCaseCount = PageElementCreator.createPageElement(driver,testCasesCount,
                    String.valueOf(count)).getValue("href");
            System.out.println(testPlanTestCaseCount + "check2");
            this.driver.navigate().to(testPlanTestCaseCount);
            waitForPageToLoad();
            knownFailureStatus();
            String testPlanPageUrl = testPlanURLConstructor();
            this.driver.navigate().to(testPlanPageUrl);
        }
    }

    public void knownFailuresCount() throws InterruptedException {
        for (int count = 2; count <= 10; count++) {
            testRunCount();
            PageElement NextPageTestRun= PageElementCreator.createPageElement(driver,testRunNextPage,
                    String.valueOf(count));
            NextPageTestRun.click();
            waitForPageToLoad();
            //estRunCount();
        }
    }

    public void knownFailureStatus() throws InterruptedException {
        Thread.sleep(2000);
        List<PageElement> IDCount =testCaseIDCount.getElements();
        int TestCaseIdCount = IDCount.size();
        System.out.println(TestCaseIdCount+ "kfcheck1");
        if(knownFailureSimIdCount.isPresent()) {
            for (int count = 1; count < TestCaseIdCount; count++) {
                String getKnownFailuresIssueId = PageElementCreator.createPageElement(driver,knownFailureSimId,
                        String.valueOf(count)).getText();
                System.out.println(getKnownFailuresIssueId + "kfcheck3");
                if(!getKnownFailuresIssueId.equalsIgnoreCase("")) {
                    issueID.add(getKnownFailuresIssueId);
                }
            }
        }else{
            String testPlanPageUrl = testPlanURLConstructor();
            this.driver.navigate().to(testPlanPageUrl);
        }
        boolean isEmpty = issueID. isEmpty();
        if(!isEmpty) {
            List<String> distinctElements = issueID.stream().distinct().collect(Collectors.toList());
            System.out.println(distinctElements + "kfcheck4");
            UrlIDLoader(distinctElements);
        }
    }

    public void UrlIDLoader(List distinctElements){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < distinctElements.size() - 1) {
            sb.append(distinctElements.get(i));
            sb.append(Constants.OR);
            i++;
        }
        sb.append(distinctElements.get(i));
        String res = sb.toString();
        System.out.println(res + "result");
        String SimPageUrl = String.format("%s%s%s%s%s",Constants.SIM_PAGE_URL,Constants.SIM_PAGE_URL_WITH_ID_SEARCH,Constants.OPEN_BRACKET,res,Constants.CLOSED_BRACKET);
        System.out.println(SimPageUrl + "qwerty");
    }
}
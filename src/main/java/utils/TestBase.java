package utils;


//import com.amazon.ate.integration.testdata.TestDataDeserializer;

import com.amazon.dtk.configuration.DTKProperties;
import com.amazon.dtk.test.WebUITest;
import lombok.NonNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.lang.reflect.Method;

public class TestBase extends WebUITest {

    public static String password;
    public static String country;
    public static String hostUrl;
    //public static TestDataDeserializer toolsTestDataDeserializer;

   /*(revoke) @BeforeSuite(alwaysRun = true)
    public void initializeSuite() {
        toolsTestDataDeserializer = new toolsTestDataDeserializer(new GsonBuilder());
    }*/

    @BeforeClass(alwaysRun = true)
    public void beforeTestClassSetup()  {
        deleteCSVFiles();
    }

    public static String constructUrl() {
        String simFolderName = DTKProperties.get(Constants.SIM_FOLDER_NAME).get();
        String simHostUrl = DTKProperties.get(Constants.HOSTURL_CONFIG).get();
        System.out.println("Launching SIM page with url " + simHostUrl);
        String simpageUrl = String.format("%s%s%s%s%s", simHostUrl, Constants.SIM_PAGE_URL_WITH_SEARCH,
                Constants.OPEN_BRACKET, simFolderName, Constants.CLOSED_BRACKET);
        System.out.println("SIM page URL constructed successfully and waiting to load");
        System.out.println("Constructed URL is :" + simpageUrl);
        return simpageUrl;
    }

    public static String testrailURLConstructor(int flag){
        String milestoneUrl;
        if(flag==0){
            milestoneUrl = DTKProperties.get(Constants.FIRST_MILESTONE_NUMBER).get();
        } else {
            milestoneUrl = DTKProperties.get(Constants.SECOND_MILESTONE_NUMBER).get();
        }
        String testrailHostUrl = DTKProperties.get(Constants.HOSTURL_CONFIG).get();
        System.out.println("Launching Testrail page with url " + testrailHostUrl);
        String testrailPageUrl = String.format("%s%s%s", testrailHostUrl, Constants.MILESTONE_VIEW_URL,
                milestoneUrl);
        System.out.println("Testrail page URL constructed successfully and waiting to load");
        System.out.println("Constructed URL is :" + testrailPageUrl);
        return testrailPageUrl;
    }

    public static  String testPlanURLConstructor(){
        String testPlanHostUrl = DTKProperties.get(Constants.HOSTURL_CONFIG).get();
        String testPlanNumber = DTKProperties.get(Constants.TESTPLAN_NUMBER).get();
        System.out.println("Launching Testplan page with url " + testPlanHostUrl);
        String testPlanPageUrl = String.format("%s%s%s", testPlanHostUrl,Constants.TESTRAIL_TESTPLAN_URL,testPlanNumber);
        System.out.println("Testrail page URL constructed successfully and waiting to load");
        System.out.println("Constructed URL is :" + testPlanPageUrl);
        return testPlanPageUrl;
    }


    public void loadMilestoneUrl(int flag){
        String testrailPageUrl = testrailURLConstructor(flag);
        this.driver.navigate().to(testrailPageUrl);
    }

    public void deleteCSVFiles() {
        String home = System.getProperty(Constants.HOME_DIRECTORY);
        File directory = new File(home + downloadsPathConstructor());
        for (File file : directory.listFiles()) {
            if (file.getName().contains(".csv")) {
                file.delete();
            }
        }
        System.out.println("CSV Files deleted");
    }

    public static String downloadsPathConstructor() {
        String osName = System.getProperty(Constants.OS_FINDER);
        String downloadDirectory;
        switch (osName.toLowerCase()) {
            case "mac os x":
                downloadDirectory = Constants.SLASH+Constants.DOWNLOADS_DIRECTORY;
                break;
            default:
                downloadDirectory = Constants.BACK_SLASH+Constants.DOWNLOADS_DIRECTORY;
        }
        return downloadDirectory;
    }

    public void loadTestPlanUrl(){
        String testPlanPageUrl = testPlanURLConstructor();
        this.driver.navigate().to(testPlanPageUrl);
    }

}





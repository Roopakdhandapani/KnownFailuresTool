package pages;

import com.amazon.dtk.configuration.DTKProperties;
import com.amazon.dtk.element.PageElement;
import com.amazon.dtk.locator.LocatorDataType;
import com.amazon.dtk.page.Locator;
import com.amazon.dtk.page.PageModel;
import com.amazon.dtk.page.PageName;
import com.opencsv.CSVReader;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import utils.Constants;
import utils.EmailReporter;
import utils.PageElementCreator;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static tables.TestExecutionCountTable.formTable;
import static tables.TestExecutionCountTable.htmlMsg;
import static utils.TestBase.downloadsPathConstructor;
import static utils.TestBase.testrailURLConstructor;


@PageName(value = "Testrail.json", type = LocatorDataType.JSON)
public class TestrailPageModel extends PageModel {

    @Locator("TESTRAIL_LOGIN_EMAIL")
    protected PageElement loginMailId;

    @Locator("TESTRAIL_LOGIN_PASSWORD")
    protected PageElement loginPassword;

    @Locator("TESTRAIL_LOGIN_SUBMIT")
    protected PageElement loginSubmit;

    @Locator("TESTRUN_COUNT")
    protected PageElement testRunCount;

    @Locator("TESTRUNS_IN_MILESTONE")
    protected PageElement testRuninMilestone;

    @Locator("EXPORT_DROPDOWN")
    protected PageElement exportDropdown;

    @Locator("EXPORT_TO_CSV")
    protected PageElement exportToCSV;

    @Locator("SELECT_NONE")
    protected PageElement selectNone;

    @Locator("SELECT_STATUS_CHECKBOX")
    protected PageElement selectStatusCheckbox;

    @Locator("SELECT_TESTEDBY_CHECKBOX")
    protected PageElement selectTestedByCheckbox;

    @Locator("SELECT_TESTEDON_CHECKBOX")
    protected PageElement selectTestedOnCheckbox;

    @Locator("ONE_LINE_RESULT")
    protected PageElement oneLineResult;

    @Locator("EXPORT_BUTTON")
    protected PageElement exportButton;

    public static Integer passed[] = new Integer[100000];
    public static Integer failed[] = new Integer[100000];
    public static Integer knownFailure[] = new Integer[100000];
    public static Integer blck[] = new Integer[100000];
    public static String testedon[] = new String[100000];
    public static String name1[] = new String[100000];
    public static String status[] = new String[100000];
    public static Integer flag[] = new Integer[100000];
    static List<String> list = new ArrayList<String>();
    static ArrayList<Integer> list1 = new ArrayList<Integer>();

    /**
     * Constructor for Add address Page
     *
     * @param driver An instance of {@link WebDriver}
     */
    public TestrailPageModel(@NonNull final WebDriver driver) { super(driver); }

    public boolean isValidPage() { return loginMailId.waitIsPresent(); }


    public void initializeSignIn(){
        isValidPage();
        try {
            loginMailId.setText(Constants.TESTRAIL_LOGIN_MAIL);
            loginPassword.waitIsPresent();
            loginPassword.setText(Constants.TESTRAIL_LOGIN_PASSWORD);
            loginSubmit.click();
            Thread.sleep(5000);
        } catch (Exception e){
            System.out.println("Login not Successfull");
        }
    }
    public void testRunCount(int flag)  {
        List<PageElement> testRunList = testRunCount.getElements();
        int totalRun = testRunList.size();
        for(int count=1;count<=totalRun;count++) {
            String milestoneTestRun =  PageElementCreator.createPageElement(driver,testRuninMilestone,
                    String.valueOf(count)).getValue("href");
            System.out.println(milestoneTestRun);
            this.driver.navigate().to(milestoneTestRun);
            waitForPageToLoad();
            exportCSV();
            String testrailPageUrl = testrailURLConstructor(flag);
            this.driver.navigate().to(testrailPageUrl);
        }
    }

    public void exportCSV() {
        try {
            exportDropdown.click();
            exportToCSV.click();
            selectNone.click();
            selectStatusCheckbox.click();
            selectTestedByCheckbox.click();
            selectTestedOnCheckbox.click();
            oneLineResult.click();
            exportButton.click();
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Unable to Export CSV");
        }
    }

    public void getData(String timeRange) {
        for (int i = 0; i < 100000; i++) {
            name1[i] = status[i] = null;
            blck[i] = passed[i] = failed[i] = flag[i] =  knownFailure[i] =0;
        }
        String home = System.getProperty(Constants.HOME_DIRECTORY);
        File directory = new File(home + downloadsPathConstructor());
        int fl = 0,count=0;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (File file : directory.listFiles()) {
            if (file.getName().contains(".csv")) {
                String s = file.getName();
                System.out.println(s);
                try {
                    FileReader fReader = new FileReader(file);
                    BufferedReader fileBuff = new BufferedReader(fReader);
                    CSVReader csvReader = new CSVReader(fileBuff);
                    String[] nextRecord;
                    while ((nextRecord = csvReader.readNext())!= null) {
                        String temp = nextRecord[0];
                        String tem = nextRecord[1];
                        String tempr = nextRecord[2];
                        Boolean temp1 = temp.contains("Untested");
                        Boolean temp2 = tem.equals("Tested By");
                        if(timeRange.equalsIgnoreCase(Constants.COUNT_TYPE_MONTHLY)) {
                            for (int date = 1; date <= 30; date++) {
                                String monthNumber = DTKProperties.get(Constants.MONTH_NUMBER).get();
                                Boolean getMonth = tempr.contains(monthNumber + "/" + date + "/2021");
                                if (!temp1 && !temp2) {
                                    if (getMonth) {
                                        if (fl > 1) {
                                            name1[count] = tem;
                                            status[count] = temp;
                                            list.add(tem);
                                            count++;
                                        } else {
                                            fl++;
                                        }
                                    }
                                }
                            }
                        }
                        if (timeRange.equalsIgnoreCase(Constants.COUNT_TYPE_DAILY)) {
                            String monthNumber = DTKProperties.get(Constants.MONTH_NUMBER).get();
                            String dateNumber = DTKProperties.get(Constants.DATE_NUMBER).get();
                            Boolean getMonth = tempr.contains(monthNumber + "/" + dateNumber + "/2021");
                            if (!temp1 && !temp2) {
                                if (getMonth) {
                                    if (fl > 1) {
                                        name1[count] = tem;
                                        status[count] = temp;
                                        list.add(tem);
                                        count++;
                                    } else {
                                        fl++;
                                    }
                                }
                            }
                        }
                    }
                }catch(IOException e){
                    System.out.println("Unable to read CSV");
                }
            }
        }
        collateData(count);
    }

    public void collateData(int count) {
        List<String> distinctElements = list.stream().distinct().collect(Collectors.toList());
        System.out.println(distinctElements);
        Collections.sort(distinctElements);
        int len = distinctElements.size();
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            int no = 0;
            for (int j = 0; j < count; j++) {
                if (name1[j].equals(distinctElements.get(i))) {
                    if (flag[j] == 0) {
                        flag[j] = 1;
                        if (status[j].equals("Passed")) {
                            passed[i]++;
                            no++;
                        } else if (status[j].contains("Failed")) {
                            failed[i]++;
                            no++;
                        } else if (status[j].contains("Blocked")) {
                            no++;
                            blck[i]++;
                        } else if (status[j].contains("Known_Failures")) {
                            knownFailure[i]++;
                            no++;
                        }
                    }
                }
            }
            list1.add(no);
        }
        try {
            formTable(list1,len,distinctElements,passed,failed,knownFailure,blck);
            EmailReporter.sendMail(htmlMsg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
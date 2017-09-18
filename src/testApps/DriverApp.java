package testApps;


import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import testReports.TestReports;
import util.TestUtil;
import datatable.Xlfile_Reader;


public class DriverApp{

	public static Properties CONFIG;
	public static Properties Objects;
	public static Properties APPTEXT;
	public static Xlfile_Reader Core;
	public static Xlfile_Reader testData=null;
	public static Xlfile_Reader DBresults=null;
	public static Random randomGenerator = new Random(); // Random Port Number generation 
	public static String currentTest;
	public static String keyword;
	
	public static WebDriver dr=null;
	public static EventFiringWebDriver driver=null;
	public static String object;
	public static String currentTSID;
	public static String stepDescription;
	public static String proceedOnFail;
	public static String testStatus;
	public static String data_column_name;
	public static int  testRepeat;
	public static int nSelPort;
	public static String sSelPort;
	public static Calendar cal = new GregorianCalendar();
	public static  int month = cal.get(Calendar.MONTH);
	public static int year = cal.get(Calendar.YEAR);
	public static  int sec =cal.get(Calendar.SECOND);
	public static  int min =cal.get(Calendar.MINUTE);
	public static  int date = cal.get(Calendar.DATE);
	public static  int day =cal.get(Calendar.HOUR_OF_DAY);
	public static String inputdata;
	public static String strDate;
	public static String result = "Not start";
	public static String mailresult=" - Script successfully executed - no errors found";
	public static String mailscreenshotpath;
	public static final Logger SELENIUM_LOGS = Logger.getRootLogger();
	public static final Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
	

	//Get the current system time - used for generated unique file ids (ex: Screenshots, Reports etc on every test run)
	public static String getCurrentTimeStamp()
    { 
          SimpleDateFormat CurrentDate = new SimpleDateFormat("MM-dd-yyyy"+"_"+"HH-mm-ss");
          Date now = new Date(); 
         String CDate = CurrentDate.format(now); 
          return CDate; 
    }

	
	
   	@BeforeSuite
	public void startTesting() throws Exception{
   		
   		// Code to Generate random numbers
   		
		 nSelPort = randomGenerator.nextInt(40000);
		 strDate=getCurrentTimeStamp();
     	System.out.println("date time stamp :"+strDate);
		 
		 // Start testing method will start generating the Test Reports from the beginning       
		TestReports.startTesting(System.getProperty("user.dir")+"//TestReport//index"+strDate+".html",
		TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), 
        "10.0.80.4",
        "D4-2");
		
		
       //Loading Config File
		CONFIG = new Properties();
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"/src/config/config.properties");
		CONFIG.load(fs);
		
		
		// LOAD Objects properties File
		Objects = new Properties();
		fs = new FileInputStream(System.getProperty("user.dir")+"/src/config/Objects.properties");
		Objects.load(fs);
		
	
		//Load datatable
		Core= new Xlfile_Reader(System.getProperty("user.dir")+"/src/config/Core.xlsx");
		testData  =  new Xlfile_Reader(System.getProperty("user.dir")+"/src/config/TestData.xlsx");
		DBresults = new Xlfile_Reader(System.getProperty("user.dir")+"/src/config/db_data.xlsx");
		
		
		
		//Initializing Webdriver
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver");

				dr = new ChromeDriver();
				driver = new EventFiringWebDriver(dr);	
				
				//maximize window
				driver.manage().window().maximize();
				
				
				//wait for 30 seconds and then fail
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
				
		
	}
	
	@Test
	public void testApp() {
		String startTime=null;
		TestReports.startSuite("Suite 1");
		
		for(int tcid=2 ; tcid<=Core.getRowCount("Suite1");tcid++){
			currentTest = Core.getCellData("Suite1", "TCID", tcid);
			
			// initilize start time of test
			if(Core.getCellData("Suite1", "Runmode", tcid).equals("Y")){
				// executed the keywords
				// loop again - rows in test data
				System.out.println(currentTest);
				int totalSets=Core.getRowCount(currentTest); // holds total rows in test data sheet. IF sheet does not exist then 2 by default
				if(totalSets<=1){
					totalSets=2; // run atleast once
				}
//				for( testRepeat=2; testRepeat<=totalSets;testRepeat++){
					startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");

				APPLICATION_LOGS.debug("Executing the test "+ currentTest);
				// implemented keywords file
				try{
				for(int tsid=2;tsid<=Core.getRowCount(currentTest);tsid++){
					// Get values from xls file
					keyword=Core.getCellData(currentTest, "Keyword", tsid);
					object=Core.getCellData(currentTest, "Object", tsid);
					currentTSID=Core.getCellData(currentTest, "TSID", tsid);
					stepDescription=Core.getCellData(currentTest, "Decription", tsid);
					proceedOnFail=Core.getCellData(currentTest, "ProceedOnFail", tsid);
//					data_column_name=Core.getCellData(currentTest, "Data_Column_Name", tsid);
					inputdata=(Core.getCellData(currentTest, "Data_Column_Name", tsid));
					System.out.println(keyword);
					Method method= KeywordsApp.class.getMethod(keyword);
					result = (String)method.invoke(method);
					APPLICATION_LOGS.debug("***Result of execution -- "+result);
					// take screenshot - every keyword
					//String fileName="Suite1_TC"+(tcid-1)+"_TS"+tsid+"_"+keyword+testRepeat+".jpg";
					if(result.startsWith("Pass")){
						testStatus=result;
						//Uncomment this one to capture screenshots for passed cases as well
						//TestUtil.captureScreenshot(CONFIG.getProperty("screenshotPath")+TestUtil.imageName+".jpeg");
						TestReports.addKeyword(stepDescription, keyword, result, System.getProperty("user.dir")+"/TestReport/Screenshots/"+TestUtil.imageNameIP+".jpeg");
					}
					else if(result.startsWith("Fail")){
							testStatus=result;
							// take screenshot - only on error
							TestUtil.captureScreenshot(CONFIG.getProperty("screenshotPath")+TestUtil.imageName+".jpeg");
							//changed to make the screenshot path generic
							TestReports.addKeyword(stepDescription, keyword, result, System.getProperty("user.dir")+"/TestReport/Screenshots/"+TestUtil.imageNameIP+".jpeg");
							mailscreenshotpath = TestUtil.imageName+".jpeg";
							System.out.println("your screenshot path :: "+ mailscreenshotpath);
							mailresult=" - Script failed ";
							if(proceedOnFail.equalsIgnoreCase("N")){
								break;
							}
						break;
						}
					}

				}
				catch(Throwable t){
					APPLICATION_LOGS.debug("Error");

				}

				// report pass or fail in HTML Report

				if(testStatus == null){
					testStatus="Pass";
				}
				APPLICATION_LOGS.debug("######################"+currentTest+" --- " +testStatus);
				TestReports.addTestCase(currentTest,
										startTime,
										TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
										testStatus );

//				if(result.startsWith("Fail")){
//
//					break;
//	                  }

//				}// test data

			}else{
				APPLICATION_LOGS.debug("Skipping the test "+ currentTest);
				testStatus="Skip";

				// report skipped
				APPLICATION_LOGS.debug("#######################"+currentTest+" --- " +testStatus);
				TestReports.addTestCase(currentTest, 
										TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), 
										TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
										testStatus );
			}
			testStatus=null;
//			if(result.startsWith("Fail")){
//                break;
//                }
		}
		TestReports.endSuite();
	}

	
	@AfterSuite
	public static void endScript() throws Exception{
		// Once the test is completed update the end time in HTML report
		TestReports.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
		// Sending Mail when script fails
		if(result.startsWith("Fail")){
		driver.quit();
		}
	}
}

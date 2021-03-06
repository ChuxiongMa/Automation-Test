package testApps;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import util.ErrorCollectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import datatable.Xlfile_Reader;

public class KeywordsApp extends DriverApp {
	
	   public static Random randomGenerator = new Random();
	   public static Calendar cal = new GregorianCalendar();  //used for adding current date in variable and then used in paths
	   public static int date = cal.get(Calendar.DATE);  //used for adding current date in variable and then used in paths
	   public static int month = cal.get(Calendar.MONTH);  //used for adding current date in variable and then used in paths
	   public static int year = cal.get(Calendar.YEAR);  //used for adding current date in variable and then used in paths
	   public static int sec =cal.get(Calendar.SECOND);  //used for adding current date in variable and then used in paths
	   public static int day =cal.get(Calendar.HOUR_OF_DAY);  //used for adding current date in variable and then used in paths
	   public static int hour=cal.get(Calendar.HOUR);  //used for adding current date in variable and then used in paths
	   public static int min=cal.get(Calendar.MINUTE);  //used for adding current date in variable and then used in paths
	   public static String sMin = new Integer(randomGenerator.nextInt(60)).toString(); //Converted Integer value to String and then used in paths
	   public static String sSec=new Integer(randomGenerator.nextInt(60)).toString(); //Converted Integer value to String and then used in paths
	   public static String sHour=new Integer(randomGenerator.nextInt(24)).toString();  //Converted Integer value to String and then used in paths
	   public static String sDate=new Integer(date).toString();  //Converted Integer value to String and then used in paths
	   
	   
	   public static String call_id ; //Used in GetText() and DBQuerycheck() to store the call id to be used for Eval UI
	   public static String sUser=null;
	   public static String sUser_Name;
	   public static Xlfile_Reader datareader=null;
	   public static Xlfile_Reader datawriter=null;
	   public static float round;
	   public static float round1;

	   

	   public static String script_error=null;
	   public static int globalwait;
	   
	//Navigate to the current URL
	public static String navigate() throws Throwable{
		APPLICATION_LOGS.debug("Executing Navigate");
		try{
            Thread.sleep(700);
			driver.get(CONFIG.getProperty(object));
		}
		catch(Throwable t){
			// Report error in Application logs
			APPLICATION_LOGS.debug("Error while navigating -"+ object + t.getMessage());
		}
		return "Pass";
	}
	
	//Clicking on a link or an Object
	public static String clickLink() throws AddressException, MessagingException{
		APPLICATION_LOGS.debug("Executing clickLink");
		try{
            Thread.sleep(700);
			driver.findElement(By.xpath(Objects.getProperty(object))).click();
		}
		catch(Throwable t){
			// Report error in Application logs
			APPLICATION_LOGS.debug("Error while clicking on an Object -"+ object + t.getMessage());
			script_error = "Timed out after "+globalwait+" miliseconds";
			return "Fail - Link Not Found";
		}
		return "Pass";
	}
	
	//Input data Keyword
	public static String input() throws Exception{
		APPLICATION_LOGS.debug("Executing input Keyword");
		// extract the test data
		//String message = "pass";
//		String data =testData.getCellData(currentTest, data_column_name , testRepeat);
		try{
			System.out.println("Input keyword data :"+inputdata);
            Thread.sleep(700);
			driver.findElement(By.xpath(Objects.getProperty(object))).sendKeys(inputdata);
		}
		catch(Exception t){
			// report error
			APPLICATION_LOGS.debug("Error while wrinting into input -"+ object + t.getMessage());
			script_error = "Timed out after "+globalwait+" miliseconds";
			return "Fail - "+t.getMessage();
			//return "Fail - "+t.getMessage();
		}
		return "Pass";
	}

	public static String softAssertTrue() throws Exception{
		APPLICATION_LOGS.debug("Executing input Keyword");
		// extract the test data
		//String message = "pass";
		String data =testData.getCellData(currentTest, data_column_name , testRepeat);
		try{
            Thread.sleep(700);
			System.out.println("Assert keyword data :"+inputdata);
			System.out.println(driver.findElement(By.xpath(Objects.getProperty(object))).getText());
			ErrorCollectors.verifyEquals(driver.findElement(By.xpath(Objects.getProperty(object))).getText(), inputdata);
			System.out.println("Data matches expected was : "+driver.findElement(By.xpath(Objects.getProperty(object))).getText());
		}
		catch(Exception t){
			// report error
			System.out.println("Inside catch");
			APPLICATION_LOGS.debug("Error while wrinting into input -"+ object + t.getMessage());
			script_error = "Timed out after "+globalwait+" miliseconds";
			return "Fail - "+t.getMessage();
		}
		return "Pass";
	}

	//Verifying text presence
	public static String verifyText(){
		 APPLICATION_LOGS.debug("Executing verifyText");
		 String expected=APPTEXT.getProperty(object);
		 String actual=driver.findElement(By.xpath(Objects.getProperty(object))).getText();
		 APPLICATION_LOGS.debug(expected);
		 APPLICATION_LOGS.debug(actual);
		 try{
             Thread.sleep(700);
			 Assert.assertEquals(actual.trim() , expected.trim());
		 }
		 catch(Throwable t){
			 // error
			 APPLICATION_LOGS.debug("Error in text - "+object);
			 APPLICATION_LOGS.debug("Actual - "+actual);
			 APPLICATION_LOGS.debug("Expected -"+ expected);
			 return "Fail -"+ t.getMessage();
		 }
		 return "Pass";
	}
		
	//verifyTextOnThePage
	public void verifyTextOnThePage (String expected) throws InterruptedException{
		final WebDriverException exception =new WebDriverException();
		try{
            Thread.sleep(700);
			if(driver.findElement(By.xpath(Objects.getProperty(object))).getText().contains(expected)){
				System.out.println(expected +" text is on this page");
			}
			else{
				System.out.println(expected +" text is NOT on this page");
				throw new WebDriverException(exception.getMessage());
			}
		}
		catch (WebDriverException e) {
			throw new WebDriverException(e.getMessage());
		}
	}
		

	public static String clickButton(){
		APPLICATION_LOGS.debug("Executing clickButton Keyword");
		try{
            Thread.sleep(700);
			driver.findElement(By.xpath(Objects.getProperty(object))).click();
		}
		catch(Throwable t){
			// report error
			APPLICATION_LOGS.debug("Error while clicking on Button -"+ object + t.getMessage());
			return "Fail - "+t.getMessage();
		}
		return "Pass";
	}
		
		
	public static String select(){
		APPLICATION_LOGS.debug("Executing select Keyword");
		// extract the test data
		String data =testData.getCellData(currentTest, data_column_name , testRepeat);
		try{
            Thread.sleep(700);
			driver.findElement(By.xpath(Objects.getProperty(object))).sendKeys(inputdata);
		}
		catch(Throwable t){
			// report error
			APPLICATION_LOGS.debug("Error while Selecting from droplist -"+ object + t.getMessage());
			return "Fail - "+t.getMessage();
		}
		return "Pass";
	}


	public static String verifyInput(){
		APPLICATION_LOGS.debug("Executing verifyText");
		String expected=APPTEXT.getProperty(object);
		String actual=driver.findElement(By.xpath(Objects.getProperty(object))).getAttribute("value");
		APPLICATION_LOGS.debug(expected);
		APPLICATION_LOGS.debug(actual);
		try{
            Thread.sleep(700);
			Assert.assertEquals(actual.trim() , expected.trim());
		}
		catch(Throwable t)
		{
			// error
			APPLICATION_LOGS.debug("Error in text - "+object);
			APPLICATION_LOGS.debug("Actual - "+actual);
			APPLICATION_LOGS.debug("Expected -"+ expected);
			return "Fail -"+ t.getMessage();
		}
		return "Pass";
	}

	public static String clearinput(){
		APPLICATION_LOGS.debug("Executing clear input");
		try{
            Thread.sleep(700);
			driver.findElement(By.xpath(Objects.getProperty(object))).clear();
		}
		catch(Throwable t){
			// Report error in Application logs
			APPLICATION_LOGS.debug("Error while clearing a input field -"+ object + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

    public static String upload() throws Exception{
        APPLICATION_LOGS.debug("Executing input Keyword");
        // extract the test data
        //String message = "pass";
//		String data =testData.getCellData(currentTest, data_column_name , testRepeat);
        try{
            System.out.println("Input keyword data :"+inputdata);
            Thread.sleep(700);
            driver.findElement(By.xpath(Objects.getProperty(object))).sendKeys(inputdata);
            Thread.sleep(10000);
        }
        catch(Exception t){
            // report error
            APPLICATION_LOGS.debug("Error while wrinting into input -"+ object + t.getMessage());
            script_error = "Timed out after "+globalwait+" miliseconds";
            return "Fail - "+t.getMessage();
            //return "Fail - "+t.getMessage();
        }
        return "Pass";
    }
	
}

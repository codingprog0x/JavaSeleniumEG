import java.io.File;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ScrapePropForm {
	TheDriver theDriver;
	
	//private WebDriver driver;
	//private WebDriverWait wait;
	
	private String url;
	
	private String submitterName = "";
	private String submitterEmail = "";
	private String submitterCompany = "";
	private String submitterWebsite = "";
	
	private int textFieldNum = 9;
	private int radioNum1 = 11;
	private int matrixNum1 = 12; 
	private int buildingNum = 0;
	
	private String address = "";
	private String units = "";
	private String acctType = "";
	private String useThisAcct = "";
	private String acctNum = "";
	private String routeNum = "";
	private String acctName = "";
	private String tID = "";
	
	private String acctTypeNew = "This is a new account.";
	private String acctTypeExisting = "Use existing account:";
	private String acctTypeNone = "New or existing account was not specified.";
	
	//use a multidimensional ArrayList to store property information
	private ArrayList<String> propInfo;
	
	ScrapePropForm(String url) {
		theDriver = new TheDriver();
		
		this.url = url;
		
		//this.driver = theDriver.getDriver();
		//this.wait = theDriver.getWait();
	}
	
	public void go() {
		System.out.println("Scraping property submission form...\n");
		
		openBrowser();
	}

	private void openBrowser( ) {
		theDriver.getURL(url);
		
		//WebElement pwField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
		WebElement pwField = theDriver.getWaitID("Password");
		pwField.sendKeys("fakepassword");
		
		//WebElement submitButt = driver.findElement(By.name("Submit"));
		WebElement submitButt = theDriver.getFindName("Submit");
		submitButt.click();
		
		callingAllMethods();
	}
	
	private void callingAllMethods() {
		WebElement isAddressEmpty;
		String addressField;
		
		scrapeSubmitter();
		
		//maximum property submission is 15
		for (int x = 0; x < 15; x++) {
			//do not increment textFieldNum, radioNum1, and matrixNum1 on first run
			//check to see whether address field is empty; if it isn't, scrape; otherwise, stop
			if (x < 1) {
				//isAddressEmpty = driver.findElement(By.id("RESULT_TextField-9"));
				isAddressEmpty = theDriver.getFindID("RESULT_TextField-9");
				
				if (!isAddressEmpty.getAttribute("value").equals("")) {
					scrapeBuildingInfo();
				} else {
					break;
				}
			} else if (x < 3) {
				//check to see whether address field is empty; if it isn't, scrape; otherwise, stop
				textFieldNum += 4;
				radioNum1 += 8;
				matrixNum1 += 8;
				
				addressField = String.format("RESULT_TextField-%s", textFieldNum);
				//isAddressEmpty = driver.findElement(By.id(addressField));
				isAddressEmpty = theDriver.getFindID(addressField);
				
				//System.out.println("Textfield = " + addressField);
				
				if (!isAddressEmpty.getAttribute("value").equals("")) {
					scrapeBuildingInfo();
				} else {
					break;
				}
			} else {
				//check to see whether address field is empty; if it isn't, scrape; otherwise, stop
				textFieldNum += 3;
				radioNum1 += 7;
				matrixNum1 += 7;
				
				addressField = String.format("RESULT_TextField-%s", textFieldNum);
				//isAddressEmpty = driver.findElement(By.id(addressField));
				isAddressEmpty = theDriver.getFindID(addressField);
				
				//System.out.println("Textfield = " + addressField);
				
				if (!isAddressEmpty.getAttribute("value").equals("")) {
					scrapeBuildingInfo();
				} else {
					break;
				}
			}
		}
		//end of script
		allDone();
	}
	
	private void scrapeSubmitter() {
		//System.out.println("Running scrapeSubmitter");
		//WebElement sName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("RESULT_TextField-2")));
		WebElement sName = theDriver.getWaitID("RESULT_TextField-2");
		submitterName = sName.getAttribute("value");
		
		//WebElement sEmail = driver.findElement(By.id("RESULT_TextField-3"));
		WebElement sEmail = theDriver.getFindID("RESULT_TextField-3");
		submitterEmail = sEmail.getAttribute("value");
		
		//WebElement cName = driver.findElement(By.id("RESULT_TextField-4"));
		WebElement cName = theDriver.getFindID("RESULT_TextField-4");
		submitterCompany = cName.getAttribute("value");
		
		//WebElement wURL = driver.findElement(By.id("RESULT_TextField-5"));
		WebElement wURL = theDriver.getFindID("RESULT_TextField-5");
		submitterWebsite = wURL.getAttribute("value");
	}
	
	private void scrapeBuildingInfo() {
		//System.out.println("Running scrapeBuildingInfo");
		int radioNum2 = 0;
		int matrixNum2 = 0;
		
		//set variables to contain element ID names
		String tFieldString = String.format("RESULT_TextField-%s", textFieldNum);
		String newAcctString = String.format("RESULT_RadioButton-%s_%s", radioNum1, radioNum2);
		String acctInfoString = String.format("RESULT_MatrixTextField-%s-%s", matrixNum1, matrixNum2);
		
		//collect and store address
		//WebElement addressField = driver.findElement(By.id(tFieldString));
		WebElement addressField = theDriver.getFindID(tFieldString);
		address = addressField.getAttribute("value");
		//System.out.println(addressField.getAttribute("value"));
		
		//increment textFieldNum for next step
		textFieldNum++;
		
		//collect and store units
		tFieldString = String.format("RESULT_TextField-%s", textFieldNum);
		//WebElement unitsField = driver.findElement(By.id(tFieldString));
		WebElement unitsField = theDriver.getFindID(tFieldString);
		units = unitsField.getAttribute("value");
		
		//check whether to use new account
		//WebElement newAcct = driver.findElement(By.id(newAcctString));
		WebElement newAcct = theDriver.getFindID(newAcctString);
		//System.out.println(newAcct.getAttribute("checked"));
		//System.out.println(newAcct.getAttribute("checked").getClass().getSimpleName());
		
		//increment second part of radio element ID for next step
		//and then create new variable
		radioNum2++;
		String existingAcctString = String.format("RESULT_RadioButton-%s_%s", radioNum1, radioNum2);
		
		//check whether to use existing account
		//WebElement existingAcct = driver.findElement(By.id(existingAcctString));
		WebElement existingAcct = theDriver.getFindID(existingAcctString);
		
		//increment textFieldNum for when/if existing account is specified
		textFieldNum += 3;
		
		if (Boolean.parseBoolean(newAcct.getAttribute("checked"))) {
			//System.out.println("New account checkboxed");
			acctType = acctTypeNew;
			
			//collect and store account number
			//WebElement acctInfo = driver.findElement(By.id(acctInfoString));
			WebElement acctInfo = theDriver.getFindID(acctInfoString);
			acctNum = acctInfo.getAttribute("value");
			
			//increment matrixNum2 for next step
			//and update acctInfoString variable
			matrixNum2++;
			acctInfoString = String.format("RESULT_MatrixTextField-%s-%s", matrixNum1, matrixNum2);
			
			//collect and store route number
			//WebElement routeInfo = driver.findElement(By.id(acctInfoString));
			WebElement routeInfo = theDriver.getFindID(acctInfoString);
			routeNum = routeInfo.getAttribute("value");
			
			//increment matrixNum2 for next step
			//and update acctInfoString variable
			matrixNum2++;
			acctInfoString = String.format("RESULT_MatrixTextField-%s-%s", matrixNum1, matrixNum2);
			
			//collect and store account name
			//WebElement nameOfAcct = driver.findElement(By.id(acctInfoString));
			WebElement nameOfAcct = theDriver.getFindID(acctInfoString);
			acctName = nameOfAcct.getAttribute("value");
			
			//increment matrixNum2 for next step
			//and update acctInfoString variable
			matrixNum2++;
			acctInfoString = String.format("RESULT_MatrixTextField-%s-%s", matrixNum1, matrixNum2);
			
			//collect and store tID
			//WebElement theTID = driver.findElement(By.id(acctInfoString));
			WebElement theTID = theDriver.getFindID(acctInfoString);
			tID = theTID.getAttribute("value").replaceAll("[-]", "");
		} else if (Boolean.parseBoolean(existingAcct.getAttribute("checked"))) {
			//System.out.println("Existing account checkboxed");
			acctType = acctTypeExisting;
						
			//update variable with new textFieldNum
			tFieldString = String.format("RESULT_TextField-%s", textFieldNum);
			
			//collect and store specified account to use
			//WebElement existingName = driver.findElement(By.id(tFieldString));
			WebElement existingName = theDriver.getFindID(tFieldString);
			useThisAcct = existingName.getAttribute("value");
		} else {
			acctType = acctTypeNone;
		}
		
		//keep track of how many buildings submitted
		buildingNum++;
		
		/*
		 * Need to write information to .csv file
		 */
		System.out.println("Building " + buildingNum);
		System.out.println("Address: " + address);
		System.out.println("Units: " + units);
		System.out.println(acctType + " " + useThisAcct);
		System.out.println("Account: " + acctNum);
		System.out.println("Routing: " + routeNum);
		System.out.println("Account Name: " + acctName);
		System.out.println("T ID: " + tID + "\n");
	}
	
	private void allDone() {
		System.out.println("All done. Total buildings submitted: " + buildingNum);
	}
}

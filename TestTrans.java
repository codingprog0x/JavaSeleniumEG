import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.InvalidCoordinatesException;
import org.openqa.selenium.WebElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestTrans {
	File theFile;
	
	TheDriver theDriver;
	TheDriver theDriver2;
	
	private String[] linesSplit;
	private float testAmt;
	
	TestTrans(File theFile, String testAmt) {
		this.theFile = theFile;
		this.testAmt = Float.parseFloat(testAmt);
		
		theDriver = new TheDriver();
		theDriver2 = new TheDriver();
	}
	
	public void go() {
		System.out.println("Performing test transaction(s)");
		
		openBrowsers();
	}
	
	public void openBrowsers() {
		Thread t = new Thread() {
			public void run() {
				//driver2.get("https://fillerwebsiteagain.com");
				theDriver2.getURL("https://fillerwebsiteagain.com");
				
				/*
				WebElement inputField2 = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id
						("ctl00_ContentPlaceHolder1_ucLogin_login_UserName")));
				*/
				WebElement inputField2 = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_ucLogin_login_UserName");
				inputField2.sendKeys("fakelogin");
				
				//inputField2 = driver2.findElement(By.id("ctl00_ContentPlaceHolder1_ucLogin_login_Password"));
				inputField2 = theDriver2.getFindID("ctl00_ContentPlaceHolder1_ucLogin_login_Password");
				inputField2.sendKeys("fakepassword");
				
				//inputField2 = driver2.findElement(By.id("LoginButton"));
				inputField2 = theDriver2.getFindID("LoginButton");
				inputField2.click();
				
				//wait for page to load before loading this next page
				try {
					Thread.sleep(1000);
					//driver2.get("https://fillerwebsiteagain.com");
					theDriver2.getURL("https://fillerwebsiteagain.com");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		t.start();
		
		//open main browser for test trans
		theDriver.getURL("http://fillerwebsite.com");
		
		//WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserName")));
		WebElement inputField = theDriver.getWaitID("UserName");
		inputField.sendKeys("fakelogin2");
		
		//inputField = driver.findElement(By.id("Password"));
		inputField = theDriver.getFindID("Password");
		inputField.sendKeys("fakepassword2");
		
		//inputField = driver.findElement(By.className("submitButton"));
		inputField = theDriver.getFindClassName("submitButton");
		inputField.click();
		
		readFileLines();
	}
	
	public void readFileLines() {
		//FileReader readFile;
		try {
			FileReader readTheFile = new FileReader(theFile);
			BufferedReader br = new BufferedReader(readTheFile);
			String lines = null;
			
			//gather info from each line and then use it to perform test trans
			try {
				while ((lines = br.readLine()) != null) {
					//shove info from each .csv row cell into an array index
					linesSplit = lines.split(",");
					
					System.out.println("Performing the following test:");
					System.out.println(Arrays.toString(linesSplit));			
					System.out.println(testAmt);
					System.out.println();
					
					callingAllMethods();
					testAmt += .01;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		mercID("4garble4");
		
		//all done; close browsers and notify user
		System.out.println("No more tests to perform.");
		JOptionPane.showMessageDialog(Main.frame,
				"No more tests to perform.");

		theDriver.getDriver().close();
		theDriver2.getDriver().close();
	}
	
	private void callingAllMethods() {
		try {
			Thread.sleep(2222);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//method calls are consolidated here so each finished one can pop off the stack
		Thread t2 = new Thread() {
			public void run() {
				mercID(linesSplit[0]);
			}
		};
		t2.start();
		navigateAcctSet();
		removeAccounts();
		addBank();
		addAcct();
		try {
			Thread.sleep(1111);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		performTest();
	}
	
	public void navigateAcctSet() {
		//navigate to correct page on test trans browser (driver)
		//WebElement acctSettings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hlTenantSettings")));
		WebElement acctSettings = theDriver.getWaitID("hlTenantSettings");
		acctSettings.click();
	}
	
	public void mercID(String theID) {
		//System.out.println("mercID()");
		//WebElement editButt = driver2.findElement(By.id("ctl00_ContentPlaceHolder1_hlEdit"));
		WebElement mercField = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_hlEdit");
		mercField.click();
		
		/*
		WebElement mercIDField = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtTelecheckMerchantID")));
		*/
		mercField = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtTelecheckMerchantID");
		mercField.clear();
		mercField.sendKeys(theID);
		
		/*
		WebElement emptySpace = driver2.findElement(By.xpath
				("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]"));
		*/
		mercField = theDriver2.getFindXpath("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]");
		mercField.click();
		
		//wait for .js to run
		try {
			Thread.sleep(4000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//WebElement saveButt = driver2.findElement(By.id("ctl00_ContentPlaceHolder1_btnSave"));
		mercField = theDriver2.getFindID("ctl00_ContentPlaceHolder1_btnSave");
		mercField.click();
	}
	
	public void removeAccounts() {
		//wait for table containing existing accounts to load
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("history-table")));
		theDriver.getWaitClassName("history-table");
		
		//count the number of existing accounts
		int x = 1;
		ArrayList<Integer> deleteThese = new ArrayList<Integer>();
		for (x = 1; x < 15; x++) {
			try {
				/*
				driver.findElement(By.xpath
						(String.format("/html/body/div[2]/div/div/div[3]/div[3]/div[2]/"
								+ "div/div[1]/div[3]/div[2]/div[1]/table/tbody/tr[%d]", x)));
				*/
				theDriver.getFindXpath(String.format("/html/body/div[2]/div/div/div[3]/div[3]/div[2]/"
								+ "div/div[1]/div[3]/div[2]/div[1]/table/tbody/tr[%d]", x));
				deleteThese.add(x);
			} catch (Exception ex) {
				break;
			}
		}

		//System.out.println(String.format("%d existing account(s)", x - 1));
		
		//delete all existing accounts
		for (int y = 0; y < deleteThese.size(); y++) {
			//next account to delete is always at same a[2] location after page reloads
			/*
			WebElement deleteThis = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
					("//*[@id='badiv']/div[3]/div[2]/div[1]/table/tbody/tr[1]/td[5]/a[2]")));
			*/
			WebElement deleteThis = theDriver.getWaitXpath("//*[@id='badiv']/div[3]/div[2]/div[1]/table/tbody/tr[1]/td[5]/a[2]");
			deleteThis.click();
			
			//click confirm button on popup confirmation window
			/*
			WebElement deleteConfirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
					("//*[@id='confirm']/div/center/table/tbody/tr/td[1]/div/span")));
			deleteConfirm.click();
			*/
			deleteThis = theDriver.getWaitXpath("//*[@id='confirm']/div/center/table/tbody/tr/td[1]/div/span");
			deleteThis.click();
			
			//System.out.println("An account was deleted.");
			
			//reload page so .js doesn't interfere with next deletion
			//driver.navigate().refresh();
			theDriver.getRefresh();
			
			//wait a little for page to reload before deleting next existing account
			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//System.out.println("All existing accounts have been deleted");
	}
	
	public void addBank() {
		//click button
		//WebElement addAcctButt = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hlAddBankAccount")));
		WebElement addAcctButt = theDriver.getWaitID("hlAddBankAccount");
		addAcctButt.click();
		
		//select account type
		//WebElement selectBank = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("_idBank_Account")));
		WebElement selectBank = theDriver.getWaitID("_idBank_Account");
		selectBank.click();
		
		//WebElement okButt = driver.findElement(By.id("hlAddPaymentAccount"));
		WebElement okButt = theDriver.getFindID("hlAddPaymentAccount");
		okButt.click();
		
		//wait for page to load and then refresh it so .js portion of website is fully loaded
		try {
			Thread.sleep(1500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addAcct() {
		//reload page so .js doesn't interfere
		//driver.navigate().refresh();
		theDriver.getRefresh();
		
		//WebElement nickField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtNickname")));
		WebElement inputField = theDriver.getWaitID("txtNickname");
		inputField.sendKeys(linesSplit[7] + "-" + linesSplit[2]);
		
		//WebElement acctField = driver.findElement(By.id("txtAccountNumber"));
		inputField = theDriver.getFindID("txtAccountNumber");
		inputField.sendKeys(linesSplit[4]);
		
		//WebElement acctConfirmField = driver.findElement(By.id("txtAccountNumberConfirm"));
		inputField = theDriver.getFindID("txtAccountNumberConfirm");
		inputField.sendKeys(linesSplit[4]);
		
		//WebElement routeField = driver.findElement(By.id("txtRoutingNumber"));
		inputField = theDriver.getFindID("txtRoutingNumber");
		
		//check whether route number is 9 digits
		//if not, prefix a 0
		if (linesSplit[5].length() < 9) {
			//System.out.println("Prefixed a 0 to route number");
			inputField.sendKeys("0" + linesSplit[5]);
		} else {
			inputField.sendKeys(linesSplit[5]);
		}
		
		/*
		WebElement acctType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
				("//*[@id='ddlAccountType']/option[2]")));
		*/
		inputField = theDriver.getWaitXpath("//*[@id='ddlAccountType']/option[2]");
		inputField.click();
		
		//WebElement corpField = driver.findElement(By.id("CorporateName"));
		inputField = theDriver.getFindID("CorporateName");
		inputField.sendKeys(linesSplit[2]);
		
		//uncomment if BofA account
		//bOfA();
		
		//sometimes button is not located, in which case
		//refresh page and retry
		//WebElement saveButt = driver.findElement(By.id("btnAddTenantBankAccount"));
		WebElement saveButt = theDriver.getFindID("btnAddTenantBankAccount");
		try {
			saveButt.click();
		} catch (WebDriverException ex) {
			//WebDriverException
			System.out.println(ex);
			System.out.println("addAcct() save button unsuccessfully clicked");
			System.out.println("Retrying to add account information.");
			addAcct();
		}
	}
	
	public void bOfA() {
		try { 
			//WebElement ba = theDriver.getFindID("ddlStates");
			
			System.out.println("BofA account. Select state and then press any key to continue.");
			Scanner reader = new Scanner(System.in);
			//String s = reader.next();
			String s = reader.nextLine();
		} catch (WebDriverException ex) {
			System.out.println("Not a BofA account.");
		}
	}
	
	public void performTest() {
		//WebElement makeTest = driver.findElement(By.id("hlAddPayment"));
		WebElement makeTest = theDriver.getFindID("hlAddPayment");
		makeTest.click();
		
		//wait for page to load to guarantee script won't error out
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//WebElement amtField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtAmount")));
		makeTest = theDriver.getWaitID("txtAmount");
		makeTest.click();
		makeTest.clear();
		
		try {
			Thread.sleep(555);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		
		makeTest.sendKeys(Float.toString(testAmt));
		
		//WebElement emptySpace = driver.findElement(By.xpath("//*[@id='paymentBlock']/div"));
		makeTest = theDriver.getFindXpath("//*[@id='paymentBlock']/div");
		makeTest.click();
		
		//wait for .js to run on page
		try {
			Thread.sleep(1111);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		
		//WebElement nextButt = driver.findElement(By.id("lbNext"));
		makeTest = theDriver.getFindID("lbNext");
		makeTest.click();
		
		//WebElement agreeToS = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cbAgree")));
		makeTest = theDriver.getWaitID("cbAgree");
		makeTest.click();
		
		//WebElement noteField = driver.findElement(By.id("txtNote"));
		makeTest = theDriver.getFindID("txtNote");
		makeTest.sendKeys(linesSplit[7] + "-" + linesSplit[2]);
		
		//WebElement saveButt = driver.findElement(By.id("btnSubmin"));
		makeTest = theDriver.getFindID("btnSubmin");
		makeTest.click();
		
		try {
			Thread.sleep(4444);
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}
}

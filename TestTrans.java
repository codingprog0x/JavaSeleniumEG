import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class TestTrans {
	private File theFile;
	
	private TheDriver theDriver;
	private TheDriver theDriver2;
	
	private PerformPayment performPayment;
	private AddAccount addAccount;
	
	private String[] linesSplit;
	private String merchantID;
	private String llcName;
	private String acctNum;
	private String routeNum;
	private String pmcName;
	
	private float testAmt;
	private String note;
	
	TestTrans(File theFile, String testAmt) {
		this.theFile = theFile;
		this.testAmt = Float.parseFloat(testAmt);
		
		this.theDriver = new TheDriver();
		this.theDriver2 = new TheDriver();
		
		this.performPayment = new PerformPayment();
		this.addAccount = new AddAccount();
		
		this.note = " test transaction";
	}
	
	public void go() {
		System.out.println("Performing test transaction(s)");
		
		openBrowsers();
	}
	
	public void openBrowsers() {
		//using 2 browsers; create a thread for concurrency
		Thread t = new Thread() {
			public void run() {
				theDriver2.setURL("https://aloginpage.com");
				
				WebElement inputField2 = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_ucLogin_login_UserName");
				inputField2.sendKeys("ausername");

				inputField2 = theDriver2.getFindID("ctl00_ContentPlaceHolder1_ucLogin_login_Password");
				inputField2.sendKeys("apassword");

				inputField2 = theDriver2.getFindID("LoginButton");
				inputField2.click();
				
				//wait for page to load before loading this next page
				try {
					Thread.sleep(1000);
					theDriver2.setURL("https://aspecificpage.com");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		t.start();
		
		//open main browser for test trans
		theDriver.setURL("http://www.aspecificpage.com");

		WebElement inputField = theDriver.getWaitID("UserName");
		inputField.sendKeys("ausername");
	
		inputField = theDriver.getFindID("Password");
		inputField.sendKeys("apassword");

		inputField = theDriver.getFindClassName("submitButton");
		inputField.click();
		
		readFileLines();
	}
	
	public void readFileLines() {
		try {
			FileReader readTheFile = new FileReader(theFile);
			BufferedReader br = new BufferedReader(readTheFile);
			String lines = null;
			
			//gather info from each line and then use it to perform test trans
			try {
				while ((lines = br.readLine()) != null) {
					//shove info from each .csv row cell into an array index
					linesSplit = lines.split(",");
					
					merchantID = linesSplit[0];
					llcName = linesSplit[2];
					acctNum = linesSplit[4];
					routeNum = linesSplit[5];
					pmcName = linesSplit[7];
					
					System.out.println("\nPerforming the following test for:");
					System.out.println(Arrays.toString(linesSplit));			
					System.out.println(testAmt);
					System.out.println();
					
					callingAllMethods();
					
					//after test is done, increment test amount
					testAmt += .01;
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//end test session
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
		//method calls are consolidated here
		Thread t2 = new Thread() {
			public void run() {
				mercID(merchantID);
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
		//secondPage();
		logThis();
		PauseMe.sleepHere(1000);
	}
	
	public void navigateAcctSet() {
		PauseMe.sleepHere(1000);
		
		//navigate to correct page on test trans browser (driver)
		WebElement acctSettings = theDriver.getWaitID("hlTenantSettings");
		acctSettings.click();
	}
	
	public void mercID(String theID) {
		WebElement mercField = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_hlEdit");
		mercField.click();
		
		mercField = theDriver2.getWaitID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtTelecheckMerchantID");
		mercField.clear();
		mercField.sendKeys(theID);
		
		mercField = theDriver2.getFindXpath("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]");
		mercField.click();
		
		//wait for .js to run
		PauseMe.sleepHere(4000);
		
		mercField = theDriver2.getFindID("ctl00_ContentPlaceHolder1_btnSave");
		mercField.click();
	}
	
	public void removeAccounts() {
		//wait for table containing existing accounts to load
		try {
			PauseMe.sleepHere(333);
			theDriver.getWaitClassName("history-table");	
		} catch (Exception e) {
			e.printStackTrace();
			removeAccounts();
		}
		
		addAccount.removeAcct(theDriver);
	}
	
	//add an account--a bank account
	public void addBank() {
		addAccount.addBank(theDriver);
		
		
		//wait for page to load and then refresh it so .js portion of website is fully loaded
		PauseMe.sleepHere(1500);
	}
	
	//add the account information
	public void addAcct() {
		//reload page so .js doesn't interfere
		theDriver.getRefresh();
		
		boolean isSuccessful = addAccount.addAcctInfo(
				theDriver,
				pmcName,
				acctNum,
				routeNum,
				llcName
				);
		
		/***
		 * 
		***/
		//uncomment if BofA account
		//bOfA();
		/***
		 * 
		***/
		
		//sometimes button is not located, in which case
		//refresh page and retry
		if (isSuccessful) {
			// do nothing/move on
		} else {
			addAcct();
		}
	}
	
	public void bOfA() {
		try { 			
			System.out.println("BofA account. Select state and then press any key to continue.");
			Scanner reader = new Scanner(System.in);
			String s = reader.nextLine();
		} catch (WebDriverException ex) {
			System.out.println("Not a BofA account.");
		}
	}
	
	public void performTest() {
		Boolean isSuccessful = performPayment.paymentFirstPage(theDriver, testAmt, true);
		
		//if unsuccessful for whatever reason, try again
		//else move onto second page
		if (isSuccessful) {
			System.out.println("Filling out payment's first page successful.");
			secondPage();
		} else {
			System.out.println("Filling out payment's first page failed. Retrying...");
			performTest();
		}
	}
	
	private void secondPage() {
		Boolean isSuccessful = performPayment.paymentSecondPage(theDriver, pmcName, llcName, note);
		
		//if unsuccessful for whatever reason, try again
		if (!isSuccessful) {
			secondPage();
		}
	}
	
	//log test information into a file
	private void logThis() {
		TestLog.logTestTrans(getClass().getName(),
				"logThis",
				String.format("%.2f for %s-%s [[ID: %s]] [[A: %s]] [[R: %s]]",
						testAmt,
						pmcName,
						llcName,
						merchantID,
						acctNum,
						routeNum)
				);
	}
}

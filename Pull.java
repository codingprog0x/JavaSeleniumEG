import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Pull {
	TheDriver theDriver;
	
	private String pmc = "somethinghere";
	private String theAcct = "somethinghere";
	private String acctNum = "somethinghere";
	private String routeNum = "somethinghere";
	
	private String acctName = theAcct;
	private String nickname = pmc + "-" + theAcct;
	private String note = nickname + " <<the reason>>";
	
	Pull() {
		this.theDriver = new TheDriver();
	}
	
	public void go() {
		openBrowser();
		navigateAcctSet();
		removeAccounts();
		addBank();
		addAcct();
		performTest();
	}
	
	private void openBrowser() {
		theDriver.getURL("http://fillerwebsite.com");
		
		WebElement emailField = theDriver.getWaitID("UserName");
		emailField.sendKeys("fakelogin");
		
		WebElement passField = theDriver.getFindID("Password");
		passField.sendKeys("fakepassword");
		
		WebElement logIn = theDriver.getFindClassName("submitButton");
		logIn.click();
	}
	
	public void navigateAcctSet() {
		//navigate to correct page on test trans browser (driver)
		WebElement acctSettings = theDriver.getWaitID("hlTenantSettings");
		acctSettings.click();
	}
	
	public void removeAccounts() {
		//wait for table containing existing accounts to load
		theDriver.getWaitClassName("history-table");
		
		//count the number of existing accounts
		int x = 1;
		ArrayList<Integer> deleteThese = new ArrayList<Integer>();
		for (x = 1; x < 15; x++) {
			try {
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
			WebElement deleteThis = theDriver.getWaitXpath("//*[@id='badiv']/div[3]/div[2]/div[1]/table/tbody/tr[1]/td[5]/a[2]");
			deleteThis.click();
			
			//click confirm button on popup confirmation window
			WebElement deleteConfirm = theDriver.getWaitXpath("//*[@id='confirm']/div/center/table/tbody/tr/td[1]/div/span");
			deleteConfirm.click();
			
			//System.out.println("An account was deleted.");
			
			//reload page so .js doesn't interfere with next deletion
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
		WebElement addAcctButt = theDriver.getWaitID("hlAddBankAccount");
		addAcctButt.click();
		
		//select account type
		WebElement selectBank = theDriver.getWaitID("_idBank_Account");
		selectBank.click();
		
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
		theDriver.getRefresh();
		
		WebElement nickField = theDriver.getWaitID("txtNickname");
		nickField.sendKeys(nickname);
		
		WebElement acctField = theDriver.getFindID("txtAccountNumber");
		acctField.sendKeys(acctNum);
		
		WebElement acctConfirmField = theDriver.getFindID("txtAccountNumberConfirm");
		acctConfirmField.sendKeys(acctNum);
		
		WebElement routeField = theDriver.getFindID("txtRoutingNumber");
		routeField.sendKeys(routeNum);
		
		WebElement acctType = theDriver.getWaitXpath("//*[@id='ddlAccountType']/option[2]");
		acctType.click();
		
		WebElement corpField = theDriver.getFindID("CorporateName");
		corpField.sendKeys(acctName);
		
		//uncomment if BofA account
		//bOfA();
		
		//sometimes button is not located, in which case
		//refresh page and retry
		WebElement saveButt = theDriver.getFindID("btnAddTenantBankAccount");
		try {
			saveButt.click();
		} catch (WebDriverException ex) {
			System.out.println(ex);
			System.out.println("addAcct() save button unsuccessfully clicked");
			System.out.println("Retrying to add account information.");
			addAcct();
		}
	}
	
	public void performTest() {
		try {
			WebElement makeTest = theDriver.getFindID("hlAddPayment");
			makeTest.click();
		} catch (Exception ex) {
			ex.printStackTrace();
			theDriver.getRefresh();
			try {
				Thread.sleep(2111);
				performTest();
			} catch (Exception ex5) {
				ex5.printStackTrace();
			}
		}

		WebElement chooseAcct = theDriver.getWaitXpath("//*[@id='ddlAddress']/option[3]");
		chooseAcct.click();
		
		System.out.println("Input amount and then press any key");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();

		WebElement emptySpace = theDriver.getFindXpath("//*[@id='paymentBlock']/div");
		emptySpace.click();
		
		//wait for .js to run on page
		try {
			Thread.sleep(1111);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		WebElement nextButt = theDriver.getFindID("lbNext");
		nextButt.click();

		WebElement agreeToS = theDriver.getWaitID("cbAgree");
		agreeToS.click();
		
		WebElement noteField = theDriver.getFindID("txtNote");
		noteField.sendKeys(note);
	}
}

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
	Accounts account;
	AddAccount addAccount;
	
	private PerformPayment performPayment;
	
	Pull() {
		this.theDriver = new TheDriver();
		this.account = new Accounts();
		this.performPayment = new PerformPayment();
		this.addAccount = new AddAccount();
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
		theDriver.setURL("http://www.aspecificwebsite.com");
		
		WebElement emailField = theDriver.getWaitID("UserName");
		emailField.sendKeys("ausername");
		
		WebElement passField = theDriver.getFindID("Password");
		passField.sendKeys("apassword");
		
		WebElement logIn = theDriver.getFindClassName("submitButton");
		logIn.click();
	}
	
	public void navigateAcctSet() {
		PauseMe.sleepHere(1000);
		
		//navigate to correct page on test trans browser (driver)
		WebElement acctSettings = theDriver.getWaitID("hlTenantSettings");
		acctSettings.click();
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
		
		//remove any existing accounts
		addAccount.removeAcct(theDriver);
	}
	
	public void addBank() {
		addAccount.addBank(theDriver);
		
		//wait for page to load and then refresh it so .js portion of website is fully loaded
		PauseMe.sleepHere(1500);
	}
	
	public void addAcct() {
		//reload page so .js doesn't interfere
		theDriver.getRefresh();
		
		boolean isSuccessful = addAccount.addAcctInfo(
				theDriver,
				account.getPMC(),
				account.getAcctNum(),
				account.getRouteNum(),
				account.getAcctName()
				);
		
		//uncomment if BofA account
		//bOfA();
		
		//sometimes button is not located, in which case
		//refresh page and retry
		if (isSuccessful) {
			// do nothing/move on
		} else {
			addAcct();
		}
	}
	
	public void performTest() {
		Boolean isSuccessful = performPayment.paymentFirstPage(theDriver, (float) account.getAmt(), false);
		
		if (isSuccessful) {
			System.out.println("Filling out payment's first page successful.");
			secondPage();
		} else {
			System.out.println("Filling out payment's first page failed. Retrying...");
			performTest();
		}
	}
	
	private void secondPage() {
		Boolean isSuccessful = performPayment.paymentSecondPage(theDriver, account.getPMC(), account.getAcctName(), account.getNote());
		
		if (!isSuccessful) {
			System.out.println("Filling out second page unsuccessful.");
			secondPage();
		} else {
			System.out.println("Filling out second page successfull.");
		}
	}
}

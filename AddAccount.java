import java.util.ArrayList;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class AddAccount {
	
	public void removeAcct(TheDriver theDriver) {
		//count the number of existing accounts
		ArrayList<Integer> deleteThese = new ArrayList<Integer>();
		for (int x = 1; x < 15; x++) {
			try {
				theDriver.getFindXpath(String.format("/html/body/div[2]/div/div/div[3]/div[3]/div[2]/"
								+ "div/div[1]/div[3]/div[2]/div[1]/table/tbody/tr[%d]", x));
				deleteThese.add(x);
			} catch (NoSuchElementException nse) {
				System.out.println("Last account to delete. (NoSuchElement)");
				break;
			} catch (Exception ex) {
				System.out.println("Something went wrong while deleting accounts. (Exception)");
				ex.printStackTrace();
				break;
			}
		}
		
		//delete all existing accounts
		for (int y = 0; y < deleteThese.size(); y++) {
			//next account to delete is always at same a[2] location after page reloads
			WebElement deleteThis = theDriver.getWaitXpath("//*[@id='badiv']/div[3]/div[2]/div[1]/table/tbody/tr[1]/td[5]/a[2]");
			deleteThis.click();
			
			//click confirm button on popup confirmation window
			deleteThis = theDriver.getWaitXpath("//*[@id='confirm']/div/center/table/tbody/tr/td[1]/div/span");
			deleteThis.click();
			
			//reload page so .js doesn't interfere with next deletion
			theDriver.getRefresh();
			
			//wait a little for page to reload before deleting next existing account
			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}		
	}
	
	public void addBank(TheDriver theDriver) {
		//click button
		WebElement addAcctButt = theDriver.getWaitID("hlAddBankAccount");
		addAcctButt.click();
		
		//select account type
		WebElement selectBank = theDriver.getWaitID("_idBank_Account");
		selectBank.click();
		
		WebElement okButt = theDriver.getFindID("hlAddPaymentAccount");
		okButt.click();
	}
	
	public boolean addAcctInfo(TheDriver theDriver, String pmcName, String acctNum, String routeNum, String llcName) {
		WebElement nickField = theDriver.getWaitID("txtNickname");
		nickField.sendKeys(pmcName + "-" + llcName);
		
		WebElement acctField = theDriver.getFindID("txtAccountNumber");
		acctField.sendKeys(acctNum);
		
		WebElement acctConfirmField = theDriver.getFindID("txtAccountNumberConfirm");
		acctConfirmField.sendKeys(acctNum);
		
		WebElement routeField = theDriver.getFindID("txtRoutingNumber");
		routeField.sendKeys(routeNum);
		
		WebElement acctType = theDriver.getWaitXpath("//*[@id='ddlAccountType']/option[2]");
		acctType.click();
		
		WebElement corpField = theDriver.getFindID("CorporateName");
		corpField.sendKeys(llcName);
		
		WebElement saveButt = theDriver.getFindID("btnAddTenantBankAccount");
		try {
			saveButt.click();
			return true;
		} catch (WebDriverException ex) {
			System.out.println(ex);
			System.out.println("addAcct() save button unsuccessfully clicked");
			System.out.println("Retrying to add account information.");
			return false;
		}
	}
	
	public void mercID() {
		//for future implementation
	}
}

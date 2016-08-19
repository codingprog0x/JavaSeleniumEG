import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewProperty {
	TheDriver theDriver;
	
	File theFile;
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	private String[] rowSplit;
	private String pmcIs;
	
	private boolean isNew;
	private boolean isGoldmont;
	
	NewProperty(File theFile, boolean isNew, boolean isGoldmont) {
		this.theFile = theFile;
		
		theDriver = new TheDriver();
		//this.driver = theDriver.getDriver();
		//this.wait = theDriver.getWait();
		
		this.isNew = isNew;
		this.isGoldmont = isGoldmont;
	}
	
	public void go() {
		System.out.println("Create fresh new property");
		
		openBrowser();
	}
	
	private void openBrowser() {
		//open browser
		//driver.get("https://fillerwebsite.com");
		theDriver.getURL("https://fillerwebsite.com");;
		
		/*
		WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_ucLogin_login_UserName")));
		*/
		WebElement inputField = theDriver.getWaitID("ctl00_ContentPlaceHolder1_ucLogin_login_UserName");
		inputField.sendKeys("fakelogin");
		
		//inputField = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucLogin_login_Password"));
		inputField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucLogin_login_Password");
		inputField.sendKeys("fakepassword");
		
		//inputField = driver.findElement(By.id("LoginButton"));
		inputField = theDriver.getFindID("LoginButton");
		inputField.click();
		
		assignPMC();
		findPMC();
	}
	
	private void assignPMC() {
		//FileReader readFile;
		try {
			FileReader readFile = new FileReader(theFile);
			BufferedReader br = new BufferedReader(readFile);
			String lines = null;
			
			try {
				lines = br.readLine();
				
				String[] linesSplit = lines.split(",");
				
				pmcIs = linesSplit[0];
				//System.out.println(pmcIs);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void findPMC() {
		boolean pmcFound = false;
		System.out.println("Searching for PMC...");
		
		//wait for page to load
		try {
			Thread.sleep(777);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//driver.get("https://fillerwebsite.com/specificpage");
		theDriver.getURL("https://fillerwebsite.com/specificpage");
		
		//display 100 results per page
		/*
		WebElement increaseResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
				("//*[@id='ctl00_ContentPlaceHolder1_ucCompanies_pagingChanger']/option[4]")));
		*/
		WebElement increaseResult = theDriver.getWaitXpath("//*[@id='ctl00_ContentPlaceHolder1_ucCompanies_pagingChanger']/option[4]");
		increaseResult.click();
		
		try {
			Thread.sleep(1999);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//cycle through all 100 results on page until PMC is found
		WebElement selectPMC;
		for (int x = 2; x < 106; x++) {
			if (x < 10) {
				/*
				selectPMC = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
						(String.format("ctl00_ContentPlaceHolder1_ucCompanies_gvCompanies_ctl0%s_hlCompany",
								String.valueOf(x)))));
				*/
				selectPMC = theDriver.getWaitID(String.format("ctl00_ContentPlaceHolder1_ucCompanies_gvCompanies_ctl0%s_hlCompany",
						String.valueOf(x)));
				//System.out.println(selectPMC.getText());
				if (selectPMC.getText().toLowerCase().equals(pmcIs.toLowerCase())) {
					System.out.println("PMC found");
					selectPMC.click();
					pmcFound = true;
					break;
				}
			} else {
				//no element id has 102 in it, so click next page or exit
				if (x == 102) {
					try {
						/*
						WebElement nextPage = driver.findElement(By.xpath
								("//*[@id='mainContent']/div[2]/div/span/div/a[2]/span"));
						*/
						WebElement nextPage = theDriver.getFindXpath("//*[@id='mainContent']/div[2]/div/span/div/a[2]/span");
						//System.out.println(nextPage.getText());
						if (nextPage.getText().equals(">")) {
							//reset x for next page
							x -= 101;
							
							nextPage.click();
						}
					} catch (Exception ex) {
						System.out.println("No next page");
					}
				} else {
					try {
						/*
						selectPMC = driver.findElement(By.id
								(String.format("ctl00_ContentPlaceHolder1_ucCompanies_gvCompanies_ctl%s_hlCompany",
										String.valueOf(x))));
						*/
						selectPMC = theDriver.getFindID(String.format("ctl00_ContentPlaceHolder1_ucCompanies_gvCompanies_ctl%s_hlCompany",
								String.valueOf(x)));
						//System.out.println(selectPMC.getText());
						
						if (selectPMC.getText().toLowerCase().equals(pmcIs.toLowerCase())) {
							System.out.println("PMC found");
							selectPMC.click();
							pmcFound = true;
							break;
						}
					} catch (NoSuchElementException ex) {
						break;
					}
				}
			}
		}
		
		//navigate to property page
		if (pmcFound) {
			System.out.println("PMC found");
			/*
			WebElement propTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
					("ctl00_ContentPlaceHolder1_hlBuildings")));
			*/
			WebElement propTab = theDriver.getWaitID("ctl00_ContentPlaceHolder1_hlBuildings");
			propTab.click();
			fileReader();
		} else {
			System.out.println("Couldn't find PMC. Please check your spelling in the .csv file");
			JOptionPane.showMessageDialog(Main.frame,
					"Couldn't find PMC. Please check your spelling in the .csv file");
			driver.close();
		}
	}
	
	private void fileReader() {
		//FileReader readFile;
		try {
			//readFile = new FileReader(Main.theFile);
			//BufferedReader br = new BufferedReader(readFile);
			FileReader readTheFile = new FileReader(theFile);
			BufferedReader br = new BufferedReader(readTheFile);
			String lines = null;
			
			try {
				while ((lines = br.readLine()) != null) {
					rowSplit = lines.split(",");
					System.out.println("Adding a new property...");
					callingAllMethods();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//the end of script
		System.out.println("No more properties to create");
		JOptionPane.showMessageDialog(Main.frame,
				"No more properties to create");
		driver.close();
	}
	
	private void callingAllMethods() {
		//method calls are consolidated here
		addProcess();
		addUnits();
		propInfoTab();
		addAcctInfo();
		
		/*
		//System.out.println("Goldmont property (y/n?)");
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer;
		try {
			answer = br.readLine();
			if (answer.toLowerCase().startsWith("y")) {
				System.out.println("A Goldmont property");
				goldmont();
			} else {
				System.out.println("Not a Goldmont property");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		if (isGoldmont) {
			goldmont();
		}
		
		selectCC();
	}
	
	private void addProcess() {
		/*
		WebElement addButt = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_btnActivateAddNewBuilding")));
		*/
		WebElement addButt = theDriver.getWaitID("ctl00_ContentPlaceHolder1_btnActivateAddNewBuilding");
		addButt.click();
		
		/*
		WebElement nField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_txtName")));
		*/
		WebElement nField = theDriver.getWaitID("ctl00_ContentPlaceHolder1_ucBuilding_txtName");
		nField.sendKeys(rowSplit[1]);
		
		//WebElement stField = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucBuilding_txtStreet"));
		WebElement stField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_txtStreet");
		stField.sendKeys(rowSplit[2]);
		
		//WebElement cField = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucBuilding_txtCity"));
		WebElement cField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_txtCity");
		cField.sendKeys(rowSplit[3]);
		
		selectState();
		
		//WebElement zField = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucBuilding_txtZip"));
		WebElement zField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_txtZip");
		zField.sendKeys(rowSplit[5]);
		
		//WebElement saveButt = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnSave"));
		WebElement saveButt = theDriver.getFindID("ctl00_ContentPlaceHolder1_btnSave");
		saveButt.click();
	}
	
	private void selectState() {
		System.out.println("Searching for state...");
		
		WebElement stChoices;
		for (int x = 2; x < 60; x++) {
			/*
			stChoices = driver.findElement(By.xpath
					(String.format
							("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ddlStates']/option[%d]", x)));
			*/
			stChoices = theDriver.getFindXpath(String.format
					("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ddlStates']/option[%d]", x));
			//System.out.println(stChoices.getText());
			
			if (stChoices.getText().toLowerCase().equals(rowSplit[4].toLowerCase())) {
				System.out.println("State found: " + stChoices.getText());
				stChoices.click();
				break;
			}
		}
	}
	
	private void addUnits() {
		//grab and split the multiple units; if null is in cell space, then skip
		if (!rowSplit[9].equals("null")) {
			System.out.println("Adding multiple units...");
			String mUnits = rowSplit[9];
			String[] mUnitContainer = mUnits.split(" ");
			
			for (int x = 0; x < mUnitContainer.length; x++) {
				/*
				WebElement pField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
						("ctl00_ContentPlaceHolder1_txtUnitCreationPattern")));
				*/
				WebElement pField = theDriver.getWaitID("ctl00_ContentPlaceHolder1_txtUnitCreationPattern");
				pField.clear();
				pField.sendKeys(mUnitContainer[x]);
				
				//WebElement pSaveButt = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnCreateUnits"));
				WebElement pSaveButt = theDriver.getFindID("ctl00_ContentPlaceHolder1_btnCreateUnits");
				pSaveButt.click();
				
			}
		} else {
			System.out.println("Multiple units field is empty");
		}

		//grab and split single units; if null is in cell area, then skip
		if (!rowSplit[10].equals("null")) {
			System.out.println("Adding single unit(s)...");
			String sUnits = rowSplit[10].replaceAll("[\"]", "");
			String[] sUnitContainer = sUnits.split(" ");
			int sUnitNum = sUnitContainer.length;
			
			/*
			WebElement sField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
					("ctl00_ContentPlaceHolder1_txtUnitsNumber")));
			*/
			WebElement sField = theDriver.getWaitID("ctl00_ContentPlaceHolder1_txtUnitsNumber");
			sField.sendKeys(String.valueOf(sUnitNum));
			
			//WebElement sSaveButt = driver.findElement(By.id("ctl00_ContentPlaceHolder1_lbAddUnitManual"));
			WebElement sSaveButt = theDriver.getFindID("ctl00_ContentPlaceHolder1_lbAddUnitManual");
			sSaveButt.click();
			
			//input single units into each input field
			WebElement inputFields;
			for (int y = 0; y < sUnitNum; y++) {
				/*
				inputFields = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
						(String.format
								("ctl00_ContentPlaceHolder1_addUnitPopup_ucUnitManual%d_txtApartment", y))));
				*/
				inputFields = theDriver.getWaitID(String.format
						("ctl00_ContentPlaceHolder1_addUnitPopup_ucUnitManual%d_txtApartment", y));
				inputFields.sendKeys(sUnitContainer[y]);
			}
			
			//sSaveButt = driver.findElement(By.id("ctl00_ContentPlaceHolder1_addUnitPopup_btnSaveUnitControls"));
			sSaveButt = theDriver.getFindID("ctl00_ContentPlaceHolder1_addUnitPopup_btnSaveUnitControls");
			sSaveButt.click();
		} else {
			System.out.println("No single unit(s) to add"); 
		}
	}
	
	private void propInfoTab() {
		try {
			Thread.sleep(2111);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//WebElement propTab = driver.findElement(By.id("ctl00_ContentPlaceHolder1_hlBuildingInfo"));
		WebElement propTab = theDriver.getFindID("ctl00_ContentPlaceHolder1_hlBuildingInfo");
		propTab.click();
		
		/*
		WebElement editButt = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_hlEdit")));
		*/
		WebElement editButt = theDriver.getWaitID("ctl00_ContentPlaceHolder1_hlEdit");
		editButt.click();
	}
	
	private void addAcctInfo() {
		System.out.println("Adding account information...");
		/*
		WebElement enabledCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_cbxEnabled")));
		*/
		WebElement enabledCheckbox = theDriver.getWaitID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_cbxEnabled");
		enabledCheckbox.click();
		
		//wait for .js to run
		try {
			Thread.sleep(3333);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		
		//input merc ID
		/*
		WebElement mercField = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtTelecheckMerchantID"));
		*/
		WebElement mercField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtTelecheckMerchantID");
		mercField.sendKeys(rowSplit[6]);
		
		//click somewhere so .js can run on mercField
		/*
		WebElement background = driver.findElement(By.xpath
				("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]"));
		*/
		WebElement background = theDriver.getFindXpath("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]");
		background.click();
		
		//wait for .js to run
		try {
			Thread.sleep(3987);
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
		
		if (isNew) {
			//input route number
			/*
			WebElement routeField = driver.findElement(By.id
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtRoutingNumber"));
			*/
			WebElement routeField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtRoutingNumber");
			routeField.clear();
			//route numbers are 9 digits in length
			if (rowSplit[8].length() < 9) {
				routeField.sendKeys("0" + rowSplit[8]);
			} else {
				routeField.sendKeys(rowSplit[8]);
			}
			
			//click somewhere so .js can run on mercField
			/*
			background = driver.findElement(By.xpath
					("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]"));
			*/
			background = theDriver.getFindXpath("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_pnlGroupSetup']/div[3]");
			background.click();
			
			//wait for .js to run
			try {
				Thread.sleep(3333);
			} catch (Exception ex3) {
				ex3.printStackTrace();
			}
			
			//input account number
			/*
			WebElement acctField = driver.findElement(By.id
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtAccountNumber"));
			*/
			WebElement acctField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtAccountNumber");
			acctField.clear();

			acctField.sendKeys(rowSplit[7]);
			
			//select account type
			/*
			WebElement acctType = driver.findElement(By.xpath
					("//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_ddlAccountType']/option[2]"));
			*/
			WebElement acctType = theDriver.getFindXpath(
					"//*[@id='ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_ddlAccountType']/option[2]");
			acctType.click();
			
			//input account name
			/*
			WebElement nameField = driver.findElement(By.id
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtName"));
			*/
			WebElement nameField = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_ucBankAccount_txtName");
			nameField.clear();
			nameField.sendKeys(rowSplit[1]);
		}
		
		//declare fee amount for one party
		/*
		WebElement tFeeField = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtConvenienceFee"));
		*/
		WebElement tFeeField = theDriver.getFindID(
				"ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtConvenienceFee");
		tFeeField.sendKeys("2.00");
		
		//declare fee amount for other party
		/*
		WebElement pFeeField = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtCompanyFee"));
		*/
		WebElement pFeeField = theDriver.getFindID(
				"ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtCompanyFee");
		pFeeField.sendKeys("0.00");
		
		//declare minimum payment amount
		/*
		WebElement minField = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtMinAmount"));
		*/
		WebElement minField = theDriver.getFindID(
				"ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtMinAmount");
		minField.sendKeys("5");
		
		//declare maximum payment amount
		/*
		WebElement maxField = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtMaxAmount"));
		*/
		WebElement maxField = theDriver.getFindID(
				"ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_repSettings_ctl00_txtMaxAmount");
		maxField.sendKeys("7500");
	}
	
	private void goldmont() {
		//building code
		//WebElement we = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucBuilding_txtPropertyCode"));
		WebElement we = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_txtPropertyCode");
		we.clear();
		we.sendKeys(rowSplit[11]);
		
		//bank code
		//we = driver.findElement(By.id("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtCompanyCode"));
		we = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl00_txtCompanyCode");
		we.clear();
		we.sendKeys(rowSplit[12]);
	}
	
	private void selectCC() {
		System.out.println("Adding CC information...");
		
		//if CC info already supplied, then skip to saveButt
		//otherwise, add CC info
		try {
			/*
			WebElement useDefault = driver.findElement(By.id
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxGroupSetup"));
			*/
			WebElement useDefault = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxGroupSetup");
			//System.out.println("useDefault.getText() = " + useDefault.getText());
			//System.out.println("useDefault.getAttribute(checked) = " + useDefault.getAttribute("checked"));
			//System.out.println("useDefault.getAttribute(value) = " + useDefault.getAttribute("value"));
			
			if (Boolean.parseBoolean(useDefault.getAttribute("checked"))) {
				System.out.println("Company setup for CC already checkboxed");
			} else {
				System.out.println("Company setup for CC not yet checkboxed");
				useDefault.click();
			}
		
			//wait for .js to run
			try {
				Thread.sleep(5555);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (NoSuchElementException noE) {
			/*
			WebElement useNew = driver.findElement(By.id
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxEnabled"));
			*/
			WebElement useNew = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxEnabled");
			useNew.click();
			
			//wait for .js to run
			try {
				Thread.sleep(5555);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
			
			addCCInfo();
		}
		
		//click save button
		//WebElement saveButt = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnSave"));
		WebElement saveButt = theDriver.getFindID("ctl00_ContentPlaceHolder1_btnSave");
		saveButt.click();
		
		//wait until page loads
		try {
			Thread.sleep(2222);
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
		
		driver.navigate().refresh();
		try {
			Thread.sleep(1111);
		} catch (Exception ex3) {
			ex3.printStackTrace();
		}
		
		//navigate back to property page
		/*
		WebElement backToProp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
				("//*[@id='ctl00_hdrSiteMapPath']/a[2]")));
		*/
		WebElement backToProp = theDriver.getWaitXpath("//*[@id='ctl00_hdrSiteMapPath']/a[2]");
		backToProp.click();
	}
	
	private void addCCInfo() {
		//turn on payment for type of group
		/*
		WebElement enableBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxEnabledForTenant")));
		*/
		WebElement enableBox = theDriver.getWaitID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxEnabledForTenant");
		enableBox.click();
		
		//wait for .js to run
		try {
			Thread.sleep(2500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//use this account
		/*
		enableBox = driver.findElement(By.id
				("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxUseMasterAccount"));
		*/
		enableBox = theDriver.getFindID("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_cbxUseMasterAccount");
		enableBox.click();
		
		//wait for .js to run
		try {
			Thread.sleep(2500);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		
		//enable CC types
		WebElement boxes;
		for (int x = 0; x < 4; x++) {
			/*
			boxes = driver.findElement(By.id
					(String.format
							("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_cbxEnabled", x)));
			*/
			boxes = theDriver.getFindID(String.format
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_cbxEnabled", x));
			boxes.click();
			try {
				Thread.sleep(3000);
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
		
		//turn on %-based fee for each type of CC
		WebElement feeBoxes;
		for (int x = 0; x < 4; x++) {
			/*
			feeBoxes = driver.findElement(By.id
					(String.format("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_rblFeeType_2", x)));
			*/
			feeBoxes = theDriver.getFindID(
					String.format("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_rblFeeType_2", x));
			feeBoxes.click();
		}
		
		//input fee amount for each type of CC
		WebElement feeFields;
		for (int x = 0; x < 4; x++) {
			/*
			feeFields = driver.findElement(By.id
					(String.format
							("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_txtFeeAmount", x)));
			*/
			feeFields = theDriver.getFindID(String.format
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_txtFeeAmount", x));
			if (x == 2) {
				feeFields.sendKeys("3.95");
			} else {
				feeFields.sendKeys("2.95");
			}
		}
		
		//set max amount for each type of CC
		WebElement maxAmt;
		for (int x = 0; x < 4; x++) {
			/*
			maxAmt = driver.findElement(By.id
					(String.format
							("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_txtMaxAmount", x)));
			*/
			maxAmt = theDriver.getFindID(String.format
					("ctl00_ContentPlaceHolder1_ucBuilding_ucBuildingPaymentGatewaySettings_repGroups_ctl01_repSettings_ctl0%d_txtMaxAmount", x));
			maxAmt.sendKeys("7500");
		}
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TinCheck {
	TheDriver theDriver;
	File theFile;
	//private String fileConverted;
	
	//private WebDriver driver;
	//private WebDriverWait wait;
	
	private HashMap<String, String> theInfo = new HashMap<String, String>();
	
	TinCheck(File theFile) {
		theDriver = new TheDriver();
		
		//this.driver = theDriver.getDriver();
		//this.wait = theDriver.getWait();
		
		this.theFile = theFile;
	}
	
	public void go() {
		//open browser and go to page
		driver.get("https://fillerwebsite.com");
		WebElement fillField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("eservices")));
		fillField.click();
		
		fillField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Username")));
		fillField.sendKeys("fakelogin");
		
		fillField = driver.findElement(By.id("Password"));
		fillField.sendKeys("fakepassword");
		
		fillField = driver.findElement(By.name("LOGIN"));
		fillField.click();
		
		allMethods();
	}
	
	private void allMethods() {
		//readFile();
		Runnable newJob = new ReadingFile();
		Thread theJob = new Thread(newJob);
		theJob.run();
		
		navTo();
		inputInfo();
	}
	
	private void navTo() {
		//WebElement we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='toaAcceptId']/img")));
		WebElement we = theDriver.getWaitXpath("//*[@id='toaAcceptId']/img");
		we.click();
		
		//we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='aId']/img")));
		we = theDriver.getWaitXpath("//*[@id='aId']/img");
		we.click();
		
		//we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='tinTypeId']/option[2]")));
		we = theDriver.getWaitXpath("//*[@id='tinTypeId']/option[2]");
		we.click();
	}
	
	private void inputInfo() {
		
	}
	
	class ReadingFile implements Runnable {
		public void run() {
			readFile();
		}
		
		private void readFile() {
			String[] linesSplit;
			
			try {
				FileReader readTheFile = new FileReader(theFile);
				BufferedReader br = new BufferedReader(readTheFile);
				String lines = null;
				
				try {
					while ((lines = br.readLine()) != null) {
						//System.out.println(lines);
						linesSplit = lines.split(",");
						theInfo.put(linesSplit[0], linesSplit[1]);
					}
				} catch (Exception ex) {
					System.err.println(ex);
				}
				System.out.println(theInfo.keySet());
				System.out.println(theInfo.values());
				System.out.println("No more in file.");
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
}

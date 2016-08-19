import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TheDriver {
	WebDriver driver;
	WebDriverWait wait;
	
	TheDriver() {
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public WebDriverWait getWait() {
		return wait;
	}
	
	public void getURL(String s) {
		driver.get(s);
	}
	
	public void getRefresh() {
		driver.navigate().refresh();
	}
	
	public WebElement getFindID(String s) {
		return driver.findElement(By.id(s));
	}
	
	public WebElement getFindXpath(String s) {
		return driver.findElement(By.xpath(s));
	}
	
	public WebElement getFindClassName(String s) {
		return driver.findElement(By.className(s));
	}
	
	public WebElement getFindName(String s) {
		return driver.findElement(By.name(s));
	}
	
	public WebElement getWaitID(String s) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(s)));
	}
	
	public WebElement getWaitXpath(String s) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));
	}
	
	public WebElement getWaitClassName(String s) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(s)));
	}
	
}

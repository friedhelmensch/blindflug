package de.kapuzenholunder.germanwings.view;
import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.kapuzenholunder.germanwings.data.location.Departure;
import de.kapuzenholunder.germanwings.helper.INavigator;
import de.kapuzenholunder.germanwings.helper.StringOperations;

public class GermanWingsNavigator implements INavigator
{
	private WebDriver driver;
	JavascriptExecutor executor;
	public GermanWingsNavigator()
	{
		this.driver = new FirefoxDriver();
		Dimension dimension = new Dimension(1024, 768);
		this.driver.manage().window().setSize(dimension);
		this.executor = ((JavascriptExecutor)driver);
	}

	@Override
	public void selectDeparture(Departure departure, String categoryName) 
	{
		String urlToCall = "https://www.germanwings.com/skysales/BlindBooking.aspx?culture=de-DE";
		this.driver.get(urlToCall);
		String departureAirportCode = departure.getAirPortCode();
		WebElement departureSelection = this.driver.findElement(By.xpath("//div[@data-this-id='"+ departureAirportCode +"']"));
		
		this.executor.executeScript("arguments[0].click();", departureSelection);
		
		String departureName = departure.toString();
		
		WebElement categorySelection = this.driver.findElement(By.xpath("//div[@data-this-name='" + categoryName +"' and @data-this-DepartureName='"+ departureName +"']"));
		
		this.executor.executeScript("arguments[0].click();", categorySelection);
		
		List<WebElement> submits = this.driver.findElements(By.xpath("(//button[@type='submit'])"));

		for(WebElement submit : submits)
		{
			String text = submit.getText();
			if(text.contains(categoryName))
			{
				this.executor.executeScript("arguments[0].click();",submit);
				break;
			}
		}
	}
	
	@Override
	public void selectOptions(DateTime from, DateTime to, int adults, int children, int infants)
	{
		String fromYear = from.getYear() + "";
        String fromMonth = StringOperations.reformatTime(from.getMonthOfYear());
        String fromDay = StringOperations.reformatTime(from.getDayOfMonth());

        String toYear = to.getYear() + "";
        String toMonth = StringOperations.reformatTime(to.getMonthOfYear());
        String toDay = StringOperations.reformatTime(to.getDayOfMonth());

        String fromDate = fromYear + "-" + fromMonth + "-" + fromDay;
        String toDate = toYear + "-" + toMonth + "-" + toDay;
		
		String newUrlToCall = "https://www.germanwings.com/skysales/BlindBookingSearch.aspx?"
		        + "__EVENTTARGET=BlindBookingSearchViewSearchControl%24ButtonSubmit"
		        + "&__EVENTARGUMENT="
		        + "&BlindBookingSearchViewSearchControl%24fromdate=" + fromDate
		        + "&BlindBookingSearchViewSearchControl%24todate=" + toDate
		        + "&showDaysValue="
		        + "&passengersText=100"
		        + "&BlindBookingSearchViewSearchControl%24passengersADT=" + adults
		        + "&BlindBookingSearchViewSearchControl%24passengersCHD=" + children
		        + "&BlindBookingSearchViewSearchControl%24passengersINFANT=" + infants;
		this.driver.get(newUrlToCall);
		
		WebElement submitButton = this.driver.findElement(By.id("mainSubmitButton"));
		submitButton.click();
	}
	
	@Override
	public String getSourceCode()
	{
		String sourceCode = driver.getPageSource();
		if (sourceCode.isEmpty())
		{
			try
			{
				synchronized (this.driver)
				{
					this.driver.wait(300);
				}
			} catch (InterruptedException ex) {	System.out.print("impossibru");}
		sourceCode = driver.getPageSource();
		}
		return sourceCode;
	}

	public void quit()
	{
		this.driver.quit();
	}
}


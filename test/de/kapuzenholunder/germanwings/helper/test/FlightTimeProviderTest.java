package de.kapuzenholunder.germanwings.helper.test;

import org.joda.time.DateTime;
import org.junit.Test;
import org.testng.Assert;

import de.kapuzenholunder.germanwings.data.time.Time;
import de.kapuzenholunder.germanwings.helper.FlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.WingsLogger;

public abstract class FlightTimeProviderTest
{

	@Test
	public void testGetSourceCodeForFlightNumber8007() 
	{
		String flightNumer = "8007";
		DateTime date = new DateTime(2014,03,17,0,0);
		Time expectedStartTime = new Time(20,30);
		Time expectedArrivalTime = new Time(21,45);
		performTestGetSourceCodeForFlightNumber(flightNumer, date, false, expectedStartTime, expectedArrivalTime);
	}
	
	@Test
	public void testGetSourceCodeForFlightNumber2007() 
	{
		String flightNumer = "2007";
		DateTime date = new DateTime(2014,03,17,0,0);
		Time expectedStartTime = new Time(20,35);
		Time expectedArrivalTime = new Time(21,45);
		performTestGetSourceCodeForFlightNumber(flightNumer, date, true, expectedStartTime, expectedArrivalTime);
	}

	protected void performTestGetSourceCodeForFlightNumber(String flightNumber, DateTime date, boolean exceptedResult, Time expectedStartTime, Time expectedArrivalTime) 
	{
		WingsLogger logger = new WingsLogger(null);
		FlightTimeProvider provider = this.getFlightTimeProvider(flightNumber, date, logger);
		Time startTime = provider.getStartTime();
		Time arrivalTime = provider.getArrivalTime();
		
		Assert.assertEquals(startTime, expectedStartTime);
		Assert.assertEquals(arrivalTime, expectedArrivalTime);
	}

	protected abstract FlightTimeProvider getFlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger);
}

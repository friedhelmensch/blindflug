package de.kapuzenholunder.germanwings.data.time.test;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.testng.Assert;

import de.kapuzenholunder.germanwings.data.time.DateInformation;

public class DateInformationTest
{
	@Test
	public void testDateInformationConstructorReturnDate46DaysfromNow()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC), DateTime.now(DateTimeZone.UTC).plusDays(46), null, true);
	}
	
	@Test
	public void testDateInformationConstructorOutward46DaysfromNow()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC).plusDays(46), DateTime.now(DateTimeZone.UTC).plusDays(49), null, true);
	}
	
	@Test
	public void testDateInformationConstructorOutwardNowNowParameterInFuture()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC), DateTime.now(DateTimeZone.UTC).plusDays(4), DateTime.now(DateTimeZone.UTC).plusDays(1), true);
	}

	@Test
	public void testDateInformationConstructorOutwardInPastButNowParameterEarlier()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC).minusDays(1), DateTime.now(DateTimeZone.UTC).plusDays(4), DateTime.now(DateTimeZone.UTC).minusDays(2), false);
	}
	
	@Test
	public void testDateInformationConstructorOutwardInPastNowParamterIsow()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC), DateTime.now(DateTimeZone.UTC).minusDays(1), DateTime.now(DateTimeZone.UTC), true);
	}
	
	@Test
	public void testDateInformationConstructorOutwardNowReturnIn4Days()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC), DateTime.now(DateTimeZone.UTC).plusDays(4), null, false);
	}
	
	@Test
	public void testDateInformationConstructorOutwardInPastNoNowParamter()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC).minusDays(1), DateTime.now(DateTimeZone.UTC).plusDays(4), null, true);
	}
	
	@Test
	public void testDateInformationConstructorReturnBeforeOutward()
	{
		this.performConstructorTest(DateTime.now(DateTimeZone.UTC), DateTime.now(DateTimeZone.UTC).minusDays(1), null, true);
	}
	
	@Test
	public void testDateInformationConstructorOutward140310Return130310()
	{
		DateTime outward = new DateTime(2014,03,07,0,0);
		DateTime returnDate = new DateTime(2014,03,10,0,0);
		DateTime now = new DateTime(2014,01,28,0,0);
		this.performConstructorTest(outward, returnDate, now, false);
	}
	
	private void performConstructorTest(DateTime outWard, DateTime returnDate, DateTime now, boolean exceptionExpected)
	{
		boolean exceptionThrown = false;
		try
		{
			new DateInformation(outWard, returnDate, now);
		} catch (Exception e)
		{
			exceptionThrown = true;
		}

		if (exceptionExpected)
		{
			Assert.assertTrue(exceptionThrown);
		} else
		{
			Assert.assertFalse(exceptionThrown);
		}

	}

}

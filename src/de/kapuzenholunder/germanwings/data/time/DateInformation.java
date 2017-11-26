package de.kapuzenholunder.germanwings.data.time;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateInformation
{
	public final static int MaxAdvancedDays = 45;
	
	private DateTime outwardDate;
	private DateTime returnDate;
	
	public DateInformation(DateTime outwardDate, DateTime returnDate)
	{
		this(outwardDate, returnDate, null);
	}
	
	public DateInformation(DateTime outwardDate, DateTime returnDate, DateTime now)
	{
		if (outwardDate == null || returnDate == null)
			throw new IllegalArgumentException("outwardDate or returnDate was null");
		if (outwardDate.compareTo(returnDate) >= 0)
			throw new IllegalArgumentException("outWardDate must be before returnDate" + "outward: " + outwardDate + " return: " + returnDate);

		if (now == null)
		{
			now = new DateTime();
		}

		DateTime nowTimeMidnight = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0,0);
		DateTime outwardMidnight = new DateTime(outwardDate.getYear(), outwardDate.getMonthOfYear(), outwardDate.getDayOfMonth(), 0,0);
		
		DateTime returnMidnight = new DateTime(returnDate.getYear(), returnDate.getMonthOfYear(), returnDate.getDayOfMonth(), 0,0);
		
		int advancedOutwardDays = Days.daysBetween(nowTimeMidnight, outwardMidnight).getDays();
		
		int advancedReturnDays = Days.daysBetween(nowTimeMidnight, returnMidnight).getDays();
		
		if (advancedOutwardDays < 0)
		{
			throw new IllegalArgumentException("outward date is in the past");
		}

		if (advancedOutwardDays > DateInformation.MaxAdvancedDays || advancedReturnDays > DateInformation.MaxAdvancedDays)
		{
			throw new IllegalArgumentException("maximum of 45 days in advance allowed");
		}
		
		this.outwardDate = outwardDate;
		this.returnDate = returnDate;
	}
	
	public DateTime getReturnDate()
	{
		return this.returnDate;
	}
	
	public DateTime getOutwardDate()
	{
		return this.outwardDate;
	}

}

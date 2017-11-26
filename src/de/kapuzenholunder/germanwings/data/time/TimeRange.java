package de.kapuzenholunder.germanwings.data.time;

public class TimeRange 
{
	Time minTimeInfo;
	Time maxTimeInfo;
	
	public TimeRange(Time minTime, Time maxTime)
	{
		if(minTime == null || maxTime == null) throw new IllegalArgumentException("fromTime or toTime was null");
		if(maxTime.isSmaller(minTime)) throw new IllegalArgumentException("from must be smaller than to");
		
		this.minTimeInfo = minTime;
		this.maxTimeInfo = maxTime;
	}
	
	public Time getMinTime()
	{
		return this.minTimeInfo;
	}
	
	public Time getMaxTime()
	{
		return this.maxTimeInfo;
	}
}

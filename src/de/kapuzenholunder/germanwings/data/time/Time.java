package de.kapuzenholunder.germanwings.data.time;

public class Time
{
	private int hour;
	private int minute;

	public Time(int hour, int minute)
	{
		if (hour > 24)
			throw new IllegalArgumentException("hour max 24 but is: " + hour);
		if (minute > 60)
			throw new IllegalArgumentException("hour max 60 but is: " + minute);
		this.hour = hour;
		this.minute = minute;
	}

	public int getHour()
	{
		return this.hour;
	}

	public int getMinute()
	{
		return this.minute;
	}

	public boolean isBetween(Time from, Time to)
	{
		if (this.isSmaller(from))
		{
			return false;
		}

		if (to.isSmaller(this))
		{
			return false;
		}

		return true;
	}

	public boolean isSmaller(Time other)
	{
		int otherHour = other.getHour();
		int thisHour = this.getHour();

		if (thisHour > otherHour)
		{
			return false;
		}

		if (thisHour == otherHour)
		{
			int otherMinute = other.getMinute();
			int thisMinute = this.getMinute();

			if (thisMinute >= otherMinute)
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Time other = (Time) obj;
		if (this.getHour() == other.getHour())
		{
			if (this.getMinute() == other.getMinute())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return this.getHour()+":"+this.getMinute();
	}
}
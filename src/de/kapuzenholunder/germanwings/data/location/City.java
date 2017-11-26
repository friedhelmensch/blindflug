package de.kapuzenholunder.germanwings.data.location;

public class City implements Comparable<City>
{
	private String cityName;
	private String airPortCode;

	public City(String cityName, String airPortCode)
	{
		this.cityName = cityName;
		this.airPortCode = airPortCode;
	}

	public String toString()
	{
		return this.cityName;
	}

	public String getAirportCode()
	{
		return this.airPortCode;
	}

	@Override
	public int compareTo(City compareCity)
	{
		return this.toString().compareTo(compareCity.toString());
	}
}

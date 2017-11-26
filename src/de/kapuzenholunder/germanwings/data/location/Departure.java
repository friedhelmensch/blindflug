package de.kapuzenholunder.germanwings.data.location;

import java.util.List;

public class Departure
{
	public Departure(City city, List<CategoryInfo> categories) throws Exception
	{
		if (categories == null)
			throw new Exception("categories is null");
		this.categories = categories;
		this.departingCity = city;
	}

	public List<CategoryInfo> getCategoryInfos()
	{
		return this.categories;
	}

	public String getAirPortCode()
	{
		return this.departingCity.getAirportCode();
	}

	public String toString()
	{
		return this.departingCity.toString();
	}

	private List<CategoryInfo> categories;
	private City departingCity;
}

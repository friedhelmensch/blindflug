package de.kapuzenholunder.germanwings.data.location;

import java.util.ArrayList;

public class CategoryInfo
{
	private ArrayList<City> cities;
	private Category category;
		
	public CategoryInfo(ArrayList<City> categoryCities, Category category) throws Exception
	{
		if (categoryCities == null)
			throw new Exception("cities is null");
		this.category = category;

		this.cities = new ArrayList<City>();

		for (City categoryCity : categoryCities)
		{
			for (City localCity : this.cities)
			{
				if (localCity == categoryCity || localCity.getAirportCode() == categoryCity.getAirportCode())
				{
					throw new Exception("Found duplicate city definition: " + localCity.getAirportCode() + " in catgeory: " + this.getName());
				}
			}
			this.cities.add(categoryCity);
		}
	}

	public ArrayList<City> getCities()
	{
		return this.cities;
	}

	public String getName() {
		return this.category.getName();
	}
	
	public String getRegionIdentifierString() {
		return this.category.getIdentifier();
	}
	
	public String toString()
	{
		return this.category.getName();
	}
	
}

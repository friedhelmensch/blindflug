package de.kapuzenholunder.germanwings.config;
import java.util.ArrayList;
import java.util.List;

import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;

public class Config
{
	public Config(ArrayList<City> masterDataCities, ArrayList<Departure> configuredDepartures) throws Exception
	{

		for (City masterDataCity : masterDataCities)
		{
			for (City localCity : this.cities)
			{
				if (localCity == masterDataCity || localCity.getAirportCode() == masterDataCity.getAirportCode())
				{
					throw new Exception("Found duplicate city definition: " + localCity.getAirportCode());
				}
			}
			this.cities.add(masterDataCity);
		}

		for (Departure configuredDeparture : configuredDepartures)
		{
			for (Departure localDeparture : this.departures)
			{
				if (localDeparture == configuredDeparture || localDeparture.getAirPortCode() == configuredDeparture.getAirPortCode())
				{
					throw new Exception("Found duplicate departure definition: " + localDeparture.getAirPortCode());
				}
			}
			departures.add(configuredDeparture);
		}
	}

	private List<City> cities = new ArrayList<City>();
	private ArrayList<Departure> departures = new ArrayList<Departure>();

	public ArrayList<Departure> getDepartures()
	{
		return this.departures;
	}
}

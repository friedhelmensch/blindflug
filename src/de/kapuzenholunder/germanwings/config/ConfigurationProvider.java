package de.kapuzenholunder.germanwings.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.kapuzenholunder.germanwings.data.location.CategoryInfo;
import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;
import de.kapuzenholunder.germanwings.helper.IOperationFinishedCallBack;
import de.kapuzenholunder.germanwings.helper.WingsLogger;

public class ConfigurationProvider {
	
	private static HashSet<City> flatListofCities;
	private static ArrayList<Departure> departures;

	public static void reInitByGrabbingConfig(final WingsLogger logger, final IOperationFinishedCallBack callback) {
		
		Runnable task = new Runnable() {

			@Override
			public void run() {
				boolean sucess = true;
				try {
					Config config = ConfigReader.grabNewestConfigFromWebsite();
					fillCities(config);
					
				} catch (Exception e) {
					logger.attachLog("Fehler bei der Konfigermittlung");
					logger.attachLog(e.getMessage());
					sucess = false;
				}
				callback.operationFinished(sucess);
			}
		};
		Thread asyncGrab = new Thread(task, "configGrabber");
		asyncGrab.start();
	}

	private static void fillCities(Config config) throws Exception {
		departures = config.getDepartures();
		flatListofCities = new HashSet<City>();

		for (Departure departure : departures) {
			for (CategoryInfo categoryInfo : departure.getCategoryInfos()) {
				for (City currentCity : categoryInfo.getCities()) {
					flatListofCities.add(currentCity);
				}
			}
		}
	}

	public static ArrayList<CategoryInfo> getCategoryInfosForCity(Departure departure, City city) 
	{
		ArrayList<CategoryInfo> returnInfos = new ArrayList<CategoryInfo>();

		List<CategoryInfo> categoriesOfDeparture = departure.getCategoryInfos();
		
		for (CategoryInfo categoryInfo : categoriesOfDeparture) 
		{
			ArrayList<City> cities = categoryInfo.getCities();
			if (cities.contains(city)) 
			{
				returnInfos.add(categoryInfo);
			}
		}
		return returnInfos;
	}

	public static City getCityForAirportCode(String airportCode)
			throws Exception {
		ArrayList<City> currentCities = new ArrayList<City>();
		for (City city : ConfigurationProvider.flatListofCities) {
			if (city.getAirportCode().equals(airportCode)) {
				if (currentCities.contains(city)) {
					throw new Exception("city with airPortCode: " + airportCode
							+ " definded twice");
				}
				currentCities.add(city);
			}

		}
		if (currentCities.isEmpty()) {
			throw new Exception("city with airPortCode: " + airportCode
					+ " not found in ConfigurationProvider.cities");
		}
		City returnCity = currentCities.get(0);
		return returnCity;
	}

	public static ArrayList<Departure> getAllDepartures() {
		return ConfigurationProvider.departures;
	}
}

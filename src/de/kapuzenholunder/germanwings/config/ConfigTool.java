package de.kapuzenholunder.germanwings.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import de.kapuzenholunder.germanwings.data.location.Category;
import de.kapuzenholunder.germanwings.data.location.CategoryInfo;
import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;

class ConfigTool
{
	public static Config grabConfig() throws Exception
	{
		ArrayList<City> masterDataCities = new ArrayList<City>();
		 
		TagNode rootNode = new HtmlCleaner().clean(new URL("https://www.germanwings.com/skysales/BlindBooking.aspx?culture=de-DE"));
		
		TagNode[] carousels = rootNode.getElementsByAttValue("class", "carousel", true, true);
		
		TagNode carousel = carousels[0];
		
		TagNode[] departureRows = carousel.getElementsByAttValue("class", "row", true, true);
		
		ArrayList<City> departureCities = new ArrayList<City>();
		
		for (TagNode row : departureRows)
		{
			TagNode[] columns = row.getElementsByAttValue("class", "col", true, true);
			
			for (TagNode col : columns)
			{
				TagNode[] departureLabels = col.getElementsByAttValue("class", "primary", true, true);
				TagNode departureLabel = departureLabels[0];
				CharSequence departureName = departureLabel.getText();
				
				TagNode[] airportCodeElements = col.getElementsByAttValue("class", "input-radio ibe-dept-station-radio", true, true);
				TagNode airportCodeElement = airportCodeElements[0];
				String departureAirPortCode = airportCodeElement.getAttributeByName("data-this-id");
				
				City departureCity = new City(departureName.toString(), departureAirPortCode);
				departureCities.add(departureCity);
				
				masterDataCities.add(departureCity);
			}
		}
		
		HashMap<City, ArrayList<Category>> departureMap = new HashMap<City, ArrayList<Category>>();
		
		ArrayList<Category> allCategories = new ArrayList<Category>();
		
		for (City departure : departureCities)
		{
			TagNode[] categoryNodes = rootNode.getElementsByAttValue("data-this-DepartureName", departure.toString(), true, true);
			
			ArrayList<Category> departureSpecificCategories = new ArrayList<Category>();
			
			for (TagNode categoryNode : categoryNodes)
			{
				String categoryId = categoryNode.getAttributeByName("data-this-id");
				String categoryName = categoryNode.getAttributeByName("data-this-name");
				
				Category category = new Category(categoryName, categoryId);
				allCategories.add(category);
				departureSpecificCategories.add(category);
			}
			departureMap.put(departure, departureSpecificCategories);
		}
		
		ArrayList<CategoryInfo> allCategoryInfos = new ArrayList<CategoryInfo>();
		
		for (Category category : allCategories)
		{
			String idString = "cluster_selected_" + category.getIdentifier();
			TagNode[] categoryNodes = rootNode.getElementsByAttValue("id", idString, true, true);
			TagNode categoryNode = categoryNodes[0];
			
			TagNode[] rows = categoryNode.getElementsByAttValue("class", "row", true, true);
			TagNode rowWithCitiesNode = rows[0];
			
			TagNode[] cityNodes = rowWithCitiesNode.getElementsByAttValue("class", "ibe-blindbooking-station-list ibe-blindbooking-station-list-pointer", true, true);
		
			ArrayList<City> cities = new ArrayList<City>();
			for (TagNode cityNode : cityNodes)
			{
				String airportCode = cityNode.getAttributeByName("data-station");
				TagNode[] nameNodes = cityNode.getElementsByAttValue("class", "station-name", true, true);
				TagNode nameNode = nameNodes[0];
				String longCityName = nameNode.getText().toString();
				
				TagNode[] regionNodes = cityNode.getElementsByAttValue("class", "station-region", true, true);
				TagNode regionNode = regionNodes[0];
				
				String regionName = regionNode.getText().toString();
				String cityName = longCityName.replace(regionName, "").trim();

				City city = new City(cityName, airportCode);
				
				City cityToAdd = getEffectiveCity(city, masterDataCities);
				
				if(!masterDataCities.contains(cityToAdd))
				{
					masterDataCities.add(cityToAdd);
				}
				
				cities.add(cityToAdd);
			}
			
			CategoryInfo categoryInfo = new CategoryInfo(cities, category);
			allCategoryInfos.add(categoryInfo);
		}
		
		ArrayList<Departure> departures = new ArrayList<Departure>();
		
		for (City departureCity : departureCities)
		{
			ArrayList<Category> categoriesForDeparture = departureMap.get(departureCity);
			ArrayList<CategoryInfo> categoryInfosForDeparture = new ArrayList<CategoryInfo>();
			
			for (CategoryInfo categoryInfo : allCategoryInfos)
			{
				for (Category categoryOfDeparture : categoriesForDeparture)
				{
					if(categoryInfo.getRegionIdentifierString().equals(categoryOfDeparture.getIdentifier()))
					{
						categoryInfosForDeparture.add(categoryInfo);
					}
				}
			}
			
			Departure departure = new Departure(departureCity, categoryInfosForDeparture);
			departures.add(departure);
		}
		
		Config config = new Config(masterDataCities, departures);

		return config;
	}

	private static City getEffectiveCity(City cityToCompare, ArrayList<City> existingCities)
	{
		City effectiveCity = cityToCompare;
		for (City existingCity : existingCities)
		{
			if (existingCity.getAirportCode().equals(cityToCompare.getAirportCode()))
			{
				effectiveCity = existingCity;
			}
		}
		return effectiveCity;
	}

}

package de.kapuzenholunder.germanwings.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.NoSuchElementException;


import de.kapuzenholunder.germanwings.config.ConfigurationProvider;
import de.kapuzenholunder.germanwings.data.location.CategoryInfo;
import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;
import de.kapuzenholunder.germanwings.data.time.DateInformation;
import de.kapuzenholunder.germanwings.data.time.TimeRange;
import de.kapuzenholunder.germanwings.helper.INavigator;
import de.kapuzenholunder.germanwings.helper.StringOperations;
import de.kapuzenholunder.germanwings.helper.TrySplitter;
import de.kapuzenholunder.germanwings.helper.WingsLogger;
import de.kapuzenholunder.germanwings.view.events.ISearchListener;
import de.kapuzenholunder.germanwings.view.events.SearchResult;
import de.kapuzenholunder.germanwings.view.events.SearcherEvent;

public class Searcher
{
	public Searcher(INavigator navigator)
	{
		this.browser = navigator;
	}
	
	private List<ISearchListener> listeners = new ArrayList<ISearchListener>();
	private final INavigator browser;
	
	public synchronized void addEventListener(ISearchListener listener)
	{
		this.listeners.add(listener);
	}

	public synchronized void removeEventListener(ISearchListener listener)
	{
		this.listeners.remove(listener);
	}

	private synchronized void fireEvent(SearchResult searchResult)
	{
		SearcherEvent event = new SearcherEvent(this, searchResult);
		for (ISearchListener listener : this.listeners)
		{
			listener.onSearchEvent(event);
		}
	}

	private SearchResult internalSearch(
			int maxTrys,
			int adultCount,
			int childrenCount,
			int infantCount,
			Departure departure,
			City destinationCity,
			DateInformation dateInformation,
			TimeRange outwardTimeRange,
			TimeRange returnTimeRange,
			WingsLogger logger) throws Exception
	{
		int totalCount = adultCount + childrenCount + infantCount;
		if(totalCount > 9) throw new Exception("Not more than 9 people in total");
		
		DateTime outwardDate = dateInformation.getOutwardDate();
		DateTime returnDate = dateInformation.getReturnDate();

		ArrayList<CategoryInfo> categoryInfos = ConfigurationProvider.getCategoryInfosForCity(departure, destinationCity);
		
		TrySplitter<CategoryInfo> splitter = new TrySplitter<CategoryInfo>();
		
		HashMap<CategoryInfo, Integer> categories2Tries = splitter.GetCountPerItem(maxTrys, categoryInfos);
		
		for (CategoryInfo categoryInfo : categories2Tries.keySet())
		{
			int triesForCatgeory = categories2Tries.get(categoryInfo);
			for (int i = 0; i < triesForCatgeory; i++)
	        {
			   this.browser.selectDeparture(departure, categoryInfo.getName());
			   
			   try
			   {
				   this.browser.selectOptions(
						   dateInformation.getOutwardDate(),
						   dateInformation.getReturnDate(),
						   adultCount,
						   childrenCount,
						   infantCount
						   );
			   }
			   
			   catch(NoSuchElementException e)
			   {
				   logger.attachLog("Kein Ergebnis");
				   continue;
			   }
			     
			   String sourceCode = this.browser.getSourceCode();
	
	            if (SourceCodeAnalyzer.analyzeSourceCode2(sourceCode, departure, destinationCity, outwardTimeRange, returnTimeRange, outwardDate, returnDate, logger))
	            {
	            	return SearchResult.Sucess;
	            }
	        }
		}
		return SearchResult.NotFound;
	}

	public void search(
			final int maxTrys,
			final int adultCount,
			final int childrenCount,
			final int infantCount,
			final Departure departure,
			final City city,
			final DateInformation dateInformation,
			final TimeRange outwardTimeRange,
			final TimeRange returnTimeRange,
			final WingsLogger logger) throws Exception
	{
		int totalCount = adultCount + childrenCount + infantCount;
		
		if(totalCount > 9) throw new Exception("Not more than 9 people in total");
		
		Runnable task = new Runnable() {

			@Override
			public void run()
			{
				try
				{
					String triesSingularOrPlural = " Versuche";
					if (maxTrys == 1)
					{
						triesSingularOrPlural = " Versuch";
					}

					String maximumTryLog = maxTrys + triesSingularOrPlural;
					logger.attachLog(maximumTryLog);

					logger.attachLog("Rückflug zwischen: " + StringOperations.reformatTime(returnTimeRange.getMinTime().getHour()) + ":" + StringOperations.reformatTime(returnTimeRange.getMinTime().getMinute()) + " und "
							+ StringOperations.reformatTime(returnTimeRange.getMaxTime().getHour()) + ":" + StringOperations.reformatTime(returnTimeRange.getMaxTime().getMinute()) + " Uhr");

					logger.attachLog("Hinflug zwischen: " + StringOperations.reformatTime(outwardTimeRange.getMinTime().getHour()) + ":" + StringOperations.reformatTime(outwardTimeRange.getMinTime().getMinute()) + " und "
							+ StringOperations.reformatTime(outwardTimeRange.getMaxTime().getHour()) + ":" + StringOperations.reformatTime(outwardTimeRange.getMaxTime().getMinute()) + " Uhr");

					SearchResult result = internalSearch(
							maxTrys,
							adultCount,
							childrenCount,
							infantCount,
							departure,
							city,
							dateInformation,
							outwardTimeRange,
							returnTimeRange,
							logger);
					fireEvent(result);
				} catch (Exception ex)
				{
					logger.attachLog(ex.getMessage());
					fireEvent(SearchResult.Error);
				}
			}
		};
		Thread asyncSearch = new Thread(task, "Searcher");
		asyncSearch.start();
	}
	
	
}

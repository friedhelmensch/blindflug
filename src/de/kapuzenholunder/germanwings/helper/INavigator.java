package de.kapuzenholunder.germanwings.helper;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.data.location.Departure;

public interface INavigator
{
	public String getSourceCode();

	public void selectDeparture(Departure departure, String categoryName);
	
	public void selectOptions(DateTime from, DateTime to, int adults, int children, int infants);
}
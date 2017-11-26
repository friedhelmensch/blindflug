package de.kapuzenholunder.germanwings.helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.data.time.Time;

public class FlightMapperFlightTimeProvider extends FlightTimeProvider
{
    
	public FlightMapperFlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger)
	{
		super(flightNumber, date, logger);
	}

	@Override
	protected Time extractTimeInfo(String completeSource, String flightNumber, boolean departure, WingsLogger logger)
	{
		int indexInExtractedStringArray = 3;
		if (departure)
		{
			indexInExtractedStringArray = 1;
		}
		
		String flightInfoString = this.extractByStringOperations(completeSource);
		
		String[] splitted = flightInfoString.split("\\r");
		
		String timeString = splitted[indexInExtractedStringArray].trim();

		String[] hourAndMinute = timeString.split(":");

		int hour = Integer.parseInt(hourAndMinute[0]);
		int minute = Integer.parseInt(hourAndMinute[1]);

		return new Time(hour, minute);
	}
	

	@Override
	protected String extractDestinationAirportString(String completeSource, String flightNumber)
    {
        int indexInExtractedStringArray = 4;
        
        String flightInfoString = this.extractByStringOperations(completeSource);
        
        String[] splitted = flightInfoString.split("\\r");
        
        String destinationString = splitted[indexInExtractedStringArray].trim();

        destinationString = StringOperations.extractString(destinationString, "(", ")").trim();

        return destinationString;
    }

	private String extractByStringOperations(String completeSource)
	{
		String startString = "<td style=\"padding: 4px 4px 4px 4px\">";
		String endString = "</td>";
		String extracted = StringOperations.extractString(completeSource, startString, endString).trim();

		return extracted;
	}

	@Override
	protected String getDateString(int year, int month, int day)
	{
		return "date=" + year + "-" + month + "-" + day;
	}

	@Override
	protected String getUrlString(String flightNumber)
	{
		return "http://info.flightmapper.net/de/flight/Germanwings_" + flightNumber;
	}

	@Override
	protected HttpURLConnection getConnection(String targetURL, String urlParameters) throws IOException
	{
		String flightMapperUrl = targetURL + "?" + urlParameters;
		URL url = new URL(flightMapperUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		return connection;
	}

}

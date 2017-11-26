package de.kapuzenholunder.germanwings.helper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.data.time.Time;

public class AvialabilityFlightTimeProvider extends FlightTimeProvider
{
	public AvialabilityFlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger)
	{
		super(flightNumber, date, logger);
	}

	@Override
	protected Time extractTimeInfo(String completeSource, String flightNumber, boolean departure, WingsLogger logger)
	{
		Time timeInfo;

		try
		{
			String startString = this.getStartStringForTimeExtraction(departure, flightNumber);
			System.out.println(startString);
			if (completeSource.contains(startString))
			{
				String endString = "</div>";
				String extracted = StringOperations.extractString(completeSource, startString, endString).trim();

				String[] firstSplit = extracted.split(">");
				String[] secondSplit = firstSplit[2].split("<");
				String timeString = secondSplit[0];
				
				String[] hourAndMinute = timeString.split(":");

				int hour = Integer.parseInt(hourAndMinute[0]);
				int minute = Integer.parseInt(hourAndMinute[1]);

				timeInfo = new Time(hour, minute);
			} else
			{
				//something went wrong. Probably flightNumber not found for specified date. Invalid data of flightTimeProvider
				timeInfo = null;
			}
		} catch (Exception e)
		{
			logger.attachLog("Error when trying to get flightTimes");
			logger.attachLog(e.getMessage());
			timeInfo = null;
		}

		return timeInfo;
	}

    @Override
    protected String extractDestinationAirportString(String completeSource, String flightNumber) 
    {
		String startString = "<div class=\"adp\" title=\"Departure";
		String endString = "<div class=\"adp\"></div>";
		String firstExtract = StringOperations.extractString(completeSource, startString, endString).trim();

		startString = "<div class=\"apn\">";
		endString = "</div>";
		String secondExtract = StringOperations.extractString(firstExtract, startString, endString);
			
		String[] firstSplit = secondExtract.split("\\(");
		String[] secondSplit = firstSplit[1].split("\\)");				
		String airportCode = secondSplit[0];
	
		return airportCode;
    }
    
	private String getStartStringForTimeExtraction(boolean departure, String flightNumber)
	{
		String whichWayString;
		String divClass;
		if (departure)
		{
			whichWayString = "Departure";
			divClass = "adp";
		} else
		{
			whichWayString = "Arrival";
			divClass = "aar";
		}
		
		return "<div class=\"" + divClass + "\" title=\"" +whichWayString;
	}

	@Override
	protected String getUrlString(String flightNumber)
	{
		return "http://aviability.com/flight-number/flight-" + flightNumber + "-germanwings";
	}

	@Override
	protected String getDateString(int year, int month, int day)
	{
		return "_date=" + year + "-" + month + "-" + day;
	}

	@Override
	protected HttpURLConnection getConnection(String targetURL, String urlParameters) throws IOException
	{
		URL url = new URL(targetURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");
		
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		return connection;
	}
}

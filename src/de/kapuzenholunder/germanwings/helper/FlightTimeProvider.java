package de.kapuzenholunder.germanwings.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.data.time.Time;

public abstract class FlightTimeProvider
{
	private Time startTime;
	private Time arrivalTime;
	private String outwardDestinationString;
	
	public FlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger)
	{
			String completeSource = this.getSourceCodeForFlightNumber(flightNumber, date);
			this.startTime = extractTimeInfo(completeSource, flightNumber, true, logger);
			this.arrivalTime = extractTimeInfo(completeSource, flightNumber, false, logger);
			this.outwardDestinationString = extractDestinationAirportString(completeSource, flightNumber);
	}

    public String getOutwardDestination() 
    {
        return outwardDestinationString;
    }
    
	public Time getStartTime()
	{
		return startTime;
	}

	public Time getArrivalTime()
	{
		return arrivalTime;
	}
	
	protected abstract String extractDestinationAirportString(String completeSource, String flightNumber);
	
	protected abstract Time extractTimeInfo(String completeSource, String flightNumber, boolean departure, WingsLogger logger);

	protected abstract String getDateString(int year, int month, int day);

	protected abstract String getUrlString(String flightNumber);
	
	protected abstract HttpURLConnection getConnection(String targetURL, String urlParameters) throws IOException;

	private String getSourceCodeForFlightNumber(String flightNumber, DateTime date)
	{
		String urlString = this.getUrlString(flightNumber); 
		int year = date.getYear();
		int month = date.getMonthOfYear();
		int day = date.getDayOfMonth();
		String dateString = this.getDateString(year, month, day);
		String result = executeWebrequest(urlString, dateString);
		System.out.println(result);
		return result;
	}

	private String executeWebrequest(String targetURL, String urlParameters)
	{
		HttpURLConnection connection = null;
		try
		{
			connection = this.getConnection(targetURL, urlParameters);
			
			// Get Response
			InputStream is = (InputStream) connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
	
		} catch (Exception e)
		{
	
			e.printStackTrace();
			return null;
	
		} finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
	}

}
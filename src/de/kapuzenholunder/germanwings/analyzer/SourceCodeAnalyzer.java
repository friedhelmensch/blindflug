package de.kapuzenholunder.germanwings.analyzer;

import java.util.HashMap;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.config.ConfigurationProvider;
import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;
import de.kapuzenholunder.germanwings.data.time.Time;
import de.kapuzenholunder.germanwings.data.time.TimeRange;
import de.kapuzenholunder.germanwings.helper.AvialabilityFlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.FlightMapperFlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.FlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.StringOperations;
import de.kapuzenholunder.germanwings.helper.WingsLogger;

class SourceCodeAnalyzer
{
	private static HashMap<String, String> failures = new HashMap<String, String>();
	public static boolean analyzeSourceCode2(
	        String sourceCode,
	        Departure departure,
	        City destination,
	        TimeRange outwardTimeRange,
	        TimeRange returnTimeRange,
	        DateTime outwardDate,
            DateTime returnDate,
            WingsLogger logger) throws Exception
    {
        String destinationString = destination.getAirportCode();
 
        String startString = "writeSiteCatalystPixel (";
        String endString = ");";

        try
        {
	        String seperatedStrings = StringOperations.extractString(sourceCode, startString, endString);
	
	        String[] splitted = seperatedStrings.split(",");
	                
	        String outWardFlightNumber = splitted[8].trim().replace("'", "");
	        String returnFlightNumber = splitted[9].trim().replace("'", "");
	        
	        
	        if(failures.containsKey(outWardFlightNumber))
	        {
	        	return logFailure(failures.get(outWardFlightNumber), logger);
	        }
	        
	        FlightTimeProvider flightTimeProvider;
	        try
	        {
	        	flightTimeProvider = new FlightMapperFlightTimeProvider(outWardFlightNumber, outwardDate, logger);
	        } catch (StringIndexOutOfBoundsException outOfBoundsException)
	        {
	        	flightTimeProvider = new AvialabilityFlightTimeProvider(outWardFlightNumber, outwardDate, logger);
	        }
	        String outWardFlightDestination = flightTimeProvider.getOutwardDestination();
        
            if (outWardFlightDestination.equals(destinationString))
            {
                SearchTimeResult outwardResult = isFlightTimeOfFlightNumberWithinExpectedTimeRange(flightTimeProvider, outWardFlightNumber, outwardTimeRange, outwardDate, logger);
                boolean isOutWardFlightTimeOk = outwardResult.isFlightTimeOk();
                String outwardFlightTimeString = outwardResult.getFlightTimeString();

                SearchTimeResult returnResult = isFlightTimeOfFlightNumberWithinExpectedTimeRange(flightTimeProvider, returnFlightNumber, returnTimeRange, returnDate, logger);
                boolean isReturnFlightTimeOk = returnResult.isFlightTimeOk();
                String returnFlightTimeString = returnResult.getFlightTimeString();
                boolean flightTimeOk = isOutWardFlightTimeOk && isReturnFlightTimeOk;
                String log;
                if (flightTimeOk)
                {
                    log = destination + " gefunden";

                } else
                {
                    log = destination + " zur falschen Zeit";
                }
                logger.attachLog("Rueckflug: " + returnFlightTimeString);
                logger.attachLog("Hinflug: " + outwardFlightTimeString);
                logger.attachLog(log);
                System.out.println(log);
                return flightTimeOk;

            } //else
            {
            	failures.put(outWardFlightNumber, outWardFlightDestination);
            	return logFailure(outWardFlightDestination, logger);
            }
        } catch (StringIndexOutOfBoundsException outOfBoundsException)
        {
            logger.attachLog(outOfBoundsException.getMessage());
            logger.attachLog(sourceCode);
            return false;
        }
    }
	
	private static boolean logFailure(String outWardFlightDestination, WingsLogger logger) throws Exception
	{
		City wrongCity = ConfigurationProvider.getCityForAirportCode(outWardFlightDestination);
        String notFoundLog = "Leider nur nach: " + wrongCity;
        logger.attachLog(notFoundLog);
        return false;
	}
	
	private static SearchTimeResult isFlightTimeOfFlightNumberWithinExpectedTimeRange(FlightTimeProvider provider, String flightNumber, TimeRange expectedTimeRange, DateTime date, WingsLogger logger) throws Exception
	{
		Time startTime = provider.getStartTime();

		String flighTimeString;
		SearchTimeMatch result;

		if (startTime != null)
		{
			Time min = expectedTimeRange.getMinTime();
			Time max = expectedTimeRange.getMaxTime();
			boolean isBetween = startTime.isBetween(min, max);
			if (isBetween)
			{
				result = SearchTimeMatch.Match;
			} else
			{
				result = SearchTimeMatch.NoMatch;
			}
			Time arrivalTime = provider.getArrivalTime();

			String startingHour = StringOperations.reformatTime(startTime.getHour());
			String startingMinute = StringOperations.reformatTime(startTime.getMinute());

			String arrivalHour = StringOperations.reformatTime(arrivalTime.getHour());
			String arrivalMinute = StringOperations.reformatTime(arrivalTime.getMinute());

			flighTimeString = startingHour + ":" + startingMinute + " - " + arrivalHour + ":" + arrivalMinute;
		} else
		{
			result = SearchTimeMatch.NoTimesAvailableForFlightNumber;
			flighTimeString = "Flugzeit für Flug: 4U" + flightNumber + " konnte nicht ermittelt werden";
		}
		return new SearchTimeResult(result, flighTimeString);
	}
}

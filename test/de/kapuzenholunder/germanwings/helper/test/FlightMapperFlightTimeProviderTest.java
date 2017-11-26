package de.kapuzenholunder.germanwings.helper.test;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.helper.FlightMapperFlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.FlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.WingsLogger;

public class FlightMapperFlightTimeProviderTest extends FlightTimeProviderTest
{
	@Override
	protected FlightTimeProvider getFlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger)
	{
		return new FlightMapperFlightTimeProvider(flightNumber, date, logger);
	}

}

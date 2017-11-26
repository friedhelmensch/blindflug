package de.kapuzenholunder.germanwings.helper.test;

import org.joda.time.DateTime;

import de.kapuzenholunder.germanwings.helper.AvialabilityFlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.FlightTimeProvider;
import de.kapuzenholunder.germanwings.helper.WingsLogger;

public class AvialabilityFlightTimeProviderTest extends FlightTimeProviderTest{

	@Override
	protected FlightTimeProvider getFlightTimeProvider(String flightNumber, DateTime date, WingsLogger logger)
	{
		return new AvialabilityFlightTimeProvider(flightNumber, date, logger);
	}
	
}

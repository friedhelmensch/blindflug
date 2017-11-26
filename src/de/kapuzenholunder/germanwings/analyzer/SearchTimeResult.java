package de.kapuzenholunder.germanwings.analyzer;

class SearchTimeResult
{
	private SearchTimeMatch result;
	private String flightTimeString;

	public SearchTimeResult(SearchTimeMatch result, String flightTimeString)
	{
		this.result = result;
		this.flightTimeString = flightTimeString;
	}

	public boolean isFlightTimeOk()
	{
		return this.result == SearchTimeMatch.Match || this.result == SearchTimeMatch.NoTimesAvailableForFlightNumber;
	}
	
	public String getFlightTimeString()
	{
		return this.flightTimeString;
	}

}
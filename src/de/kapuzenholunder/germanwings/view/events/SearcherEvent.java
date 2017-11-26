package de.kapuzenholunder.germanwings.view.events;


public class SearcherEvent extends java.util.EventObject
{
	private static final long serialVersionUID = 1L;

	private SearchResult result;

	public SearcherEvent(Object source, SearchResult searchResult)
	{
		super(source);
		this.result = searchResult;
	}

	public SearchResult getResult()
	{
		return result;
	}

}

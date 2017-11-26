package de.kapuzenholunder.germanwings.data.location;

public class Category {

	private String name;
	private String identifier;
	
	public Category(String name, String identifier)
	{
		this.name = name;
		this.identifier = identifier;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
}

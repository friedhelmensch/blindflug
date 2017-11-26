package de.kapuzenholunder.germanwings.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TrySplitter<T> {
	
	public HashMap<T, Integer> GetCountPerItem(int max, ArrayList<T> items)
	{
		int itemCount = items.size();
		int tries = max / itemCount;
		int rest = max % itemCount;
	
		LinkedHashMap<T, Integer> itemTryCounts = new LinkedHashMap<T, Integer>();
		
		for (T item : items)
		{
			itemTryCounts.put(item, tries);
		}
		
		List<T> keys = new ArrayList<T>(itemTryCounts.keySet());
		for(int i = 0; i < rest; i++)
		{
			T item = keys.get(i);
			int newCount = itemTryCounts.get(item) + 1;
			itemTryCounts.put(item, newCount);
		}
		
		return itemTryCounts;
	}
}

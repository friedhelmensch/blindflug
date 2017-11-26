package de.kapuzenholunder.germanwings.helper.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.testng.Assert;

import de.kapuzenholunder.germanwings.helper.TrySplitter;

public class TrySplitterTest {

	@Test
	public void GetCountPerItem_Max_5_Items_3() {
		TrySplitter<Object> splitter = new TrySplitter<Object>();
		
		int max = 5;
		
		Object item1 = new Object();
		Object item2 = new Object();
		Object item3 = new Object();
		
		ArrayList<Object> items = new ArrayList<Object>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		HashMap<Object, Integer> counts = splitter.GetCountPerItem(max, items);
		
		int count1 = counts.get(item1);
		int count2 = counts.get(item2);
		int count3 = counts.get(item3);
		
		Assert.assertEquals(count1, 2);
		Assert.assertEquals(count2, 2);
		Assert.assertEquals(count3, 1);
	}

	@Test
	public void GetCountPerItem_Max_1_Items_3() {
		TrySplitter<Object> splitter = new TrySplitter<Object>();
		
		int max = 1;
		
		Object item1 = new Object();
		Object item2 = new Object();
		Object item3 = new Object();
		
		ArrayList<Object> items = new ArrayList<Object>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		HashMap<Object, Integer> counts = splitter.GetCountPerItem(max, items);
		
		int count1 = counts.get(item1);
		int count2 = counts.get(item2);
		int count3 = counts.get(item3);
		
		Assert.assertEquals(count1, 1);
		Assert.assertEquals(count2, 0);
		Assert.assertEquals(count3, 0);
	}
	
	@Test
	public void GetCountPerItem_Max_5_Items_6() {
		TrySplitter<Object> splitter = new TrySplitter<Object>();
		
		int max = 5;
		
		Object item1 = new Object();
		Object item2 = new Object();
		Object item3 = new Object();
		Object item4 = new Object();
		Object item5 = new Object();
		Object item6 = new Object();
		
		ArrayList<Object> items = new ArrayList<Object>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		items.add(item6);
		
		HashMap<Object, Integer> counts = splitter.GetCountPerItem(max, items);
		
		int count1 = counts.get(item1);
		int count2 = counts.get(item2);
		int count3 = counts.get(item3);
		int count4 = counts.get(item4);
		int count5 = counts.get(item5);
		int count6 = counts.get(item6);
		
		Assert.assertEquals(count1, 1);
		Assert.assertEquals(count2, 1);
		Assert.assertEquals(count3, 1);
		Assert.assertEquals(count4, 1);
		Assert.assertEquals(count5, 1);
		Assert.assertEquals(count6, 0);
	}
	
	@Test
	public void GetCountPerItem_Max_10_Items_6() {
		TrySplitter<Object> splitter = new TrySplitter<Object>();
		
		int max = 10;
		
		Object item1 = new Object();
		Object item2 = new Object();
		Object item3 = new Object();
		Object item4 = new Object();
		Object item5 = new Object();
		Object item6 = new Object();
		
		ArrayList<Object> items = new ArrayList<Object>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		items.add(item6);
		
		HashMap<Object, Integer> counts = splitter.GetCountPerItem(max, items);
		
		int count1 = counts.get(item1);
		int count2 = counts.get(item2);
		int count3 = counts.get(item3);
		int count4 = counts.get(item4);
		int count5 = counts.get(item5);
		int count6 = counts.get(item6);
		
		Assert.assertEquals(count1, 2);
		Assert.assertEquals(count2, 2);
		Assert.assertEquals(count3, 2);
		Assert.assertEquals(count4, 2);
		Assert.assertEquals(count5, 1);
		Assert.assertEquals(count6, 1);
	}
}

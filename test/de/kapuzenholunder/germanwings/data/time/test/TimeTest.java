package de.kapuzenholunder.germanwings.data.time.test;

import org.junit.Test;
import org.testng.Assert;

import de.kapuzenholunder.germanwings.data.time.Time;

public class TimeTest {

	@Test
	public void testIsSmaller10001200() 
	{
		Time from = new Time(10, 00);
		Time to = new Time(12, 00);
		
		boolean isSmaller = from.isSmaller(to);
		
		Assert.assertTrue(isSmaller);
	}
	
	@Test
	public void testIsSmaller00000000() 
	{
		Time from = new Time(00, 00);
		Time to = new Time(00, 00);
		
		boolean isSmaller = from.isSmaller(to);
		
		Assert.assertFalse(isSmaller);
	}
	
	@Test
	public void testIsSmaller12001000() 
	{
		Time from = new Time(12, 00);
		Time to = new Time(10, 00);
		
		boolean isSmaller = from.isSmaller(to);
		
		Assert.assertFalse(isSmaller);
	}
	
	@Test
	public void testIsSmaller12001210() 
	{
		Time from = new Time(12, 00);
		Time to = new Time(12, 10);
		
		boolean isSmaller = from.isSmaller(to);
		
		Assert.assertTrue(isSmaller);
	}
	
	@Test
	public void testIsSmaller12011200() 
	{
		Time from = new Time(12, 01);
		Time to = new Time(12, 00);
		
		boolean isSmaller = from.isSmaller(to);
		
		Assert.assertFalse(isSmaller);
	}
	
	@Test
	public void testIsBetween101211() 
	{
		Time from = new Time(10, 00);
		Time to = new Time(12, 00);
		
		Time between = new Time(11,00);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertTrue(isBetween);
	}
	
	@Test
	public void testIsBetween101213() 
	{
		Time from = new Time(10, 00);
		Time to = new Time(12, 00);
		
		Time between = new Time(13,00);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertFalse(isBetween);
	}
	
	@Test
	public void testIsBetween101010121011() 
	{
		Time from = new Time(10, 10);
		Time to = new Time(10, 12);
		
		Time between = new Time(10,11);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertTrue(isBetween);
	}
	
	@Test
	public void testIsBetween101010121013() 
	{
		Time from = new Time(10, 10);
		Time to = new Time(10, 12);
		
		Time between = new Time(10,13);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertFalse(isBetween);
	}
	
	@Test
	public void testIsBetween101010121010() 
	{
		Time from = new Time(10, 10);
		Time to = new Time(10, 12);
		
		Time between = new Time(10,10);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertTrue(isBetween);
	}
	
	@Test
	public void testIsBetween101010121012() 
	{
		Time from = new Time(10, 10);
		Time to = new Time(10, 12);
		
		Time between = new Time(10,12);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertTrue(isBetween);
	}
	
	@Test
	public void testIsBetween6001600650()
	{
		Time from = new Time(6, 00);
		Time to = new Time(16, 00);
		
		Time between = new Time(6,50);
		
		boolean isBetween = between.isBetween(from, to);
		
		Assert.assertTrue(isBetween);
	}



}

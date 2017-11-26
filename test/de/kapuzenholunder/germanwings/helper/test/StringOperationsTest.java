package de.kapuzenholunder.germanwings.helper.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.kapuzenholunder.germanwings.helper.StringOperations;

public class StringOperationsTest {

	@Test
	public void TestGetUnEscaped() {
		String dues = StringOperations.convertToUtf8("D\\u00fcsseldorf, North Rhine-Westphalia, Germany");
		//String dues2 = StringOperations.convertToUtf8(dues);
		assertTrue(dues.contains("Düsseldorf"));
	}

}

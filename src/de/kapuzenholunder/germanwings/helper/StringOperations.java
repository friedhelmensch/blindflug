package de.kapuzenholunder.germanwings.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringOperations
{
	public static String extractString(String source, String startString, String endString)
	{
		int startIndex = source.indexOf(startString, 0) + startString.length();
		int endIndex = source.indexOf(endString, startIndex);

		String singleResultLine = source.substring(startIndex, endIndex);

		return singleResultLine;
	}
	
	public static String reformatTime(int input)
	{
		String output = "" + input;
		if (input < 10)
		{
			output = "0" + input;
		}
		return output;
	}
	
	public static String convertToUtf8(String input)
	{
		Matcher matcher = Pattern.compile("(?i)\\\\u([\\da-f]{4})").matcher(input);
		if (matcher.find()) {
			String match = matcher.group();
			String firstGroup = matcher.group(1);
		    String umlaut = String.valueOf((char) Integer.parseInt(firstGroup, 16));
			input = input.replace(match, umlaut);
		}
		return input;
	}
}

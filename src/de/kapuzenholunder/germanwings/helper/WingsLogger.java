package de.kapuzenholunder.germanwings.helper;

import javax.swing.text.JTextComponent;

public class WingsLogger
{
	private String log = "";
	private JTextComponent textVisualizier;

	final static String lineSeparator = System.getProperty("line.separator");

	public WingsLogger(JTextComponent textComponent)
	{
		this.textVisualizier = textComponent;
	}

	public void attachLog(String log)
	{
		this.log = log + lineSeparator + this.log;
		textVisualizier.setText(this.log);
	}

	public void reset()
	{
		this.log = "";
		this.textVisualizier.setText(log);

	}
}

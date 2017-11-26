package de.kapuzenholunder.germanwings.config;

class ConfigReader
{
	public static Config grabNewestConfigFromWebsite() throws Exception
	{
		Config config = ConfigTool.grabConfig();
		return config;
	}
}
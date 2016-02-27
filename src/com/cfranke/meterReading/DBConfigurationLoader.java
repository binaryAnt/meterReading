package com.cfranke.meterReading;

public abstract class DBConfigurationLoader {

	public abstract ConfigurationDatabase getConfigurationDatabase() throws DBConfigurationLoaderException;
	
}

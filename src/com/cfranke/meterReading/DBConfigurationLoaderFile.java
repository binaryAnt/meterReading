package com.cfranke.meterReading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class DBConfigurationLoaderFile extends DBConfigurationLoader {

	private File confFile;

	public DBConfigurationLoaderFile() {
		confFile = new File("db.conf");
	}

	@Override
	public ConfigurationDatabase getConfigurationDatabase() throws DBConfigurationLoaderException {
		Gson gson = new Gson();
		String dbConfigurationAsJson;

		try {
			dbConfigurationAsJson = getDBConfigurationAsJsonFromFile();
		} catch (IOException e) {
			throw new DBConfigurationLoaderException(e.getMessage());
		}

		return gson.fromJson(dbConfigurationAsJson, ConfigurationDatabase.class);
	}

	private String getDBConfigurationAsJsonFromFile() throws IOException {
		String configurationAsJson = "";
		String line;

		InputStream in = new FileInputStream(confFile);

		InputStreamReader reader = new InputStreamReader(in);

		BufferedReader bufReader = new BufferedReader(reader);
		while ((line = bufReader.readLine()) != null) {
			configurationAsJson += line;
		}

		bufReader.close();
		return configurationAsJson;
	}

}

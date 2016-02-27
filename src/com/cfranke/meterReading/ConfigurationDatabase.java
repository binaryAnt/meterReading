package com.cfranke.meterReading;

public class ConfigurationDatabase {
	private String dbDriver;
	private String dbURL;
	private String dbName;
	private String dbUsername;
	private String dbPassword;

	public ConfigurationDatabase(String dbDriver, String dbURL, String dbName, String dbUsername, String dbPassword) {
		// TODO : Passwort nicht so offen speichern!

		this.dbDriver = dbDriver;
		this.dbURL = dbURL;
		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public String getDbURL() {
		return dbURL;
	}

	public String getDbName() {
		return dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}
}

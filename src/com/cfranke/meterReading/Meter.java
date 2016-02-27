package com.cfranke.meterReading;

public class Meter {

	public final int id;
	public final int meterTypeId;
	public final String name;
	public final int residenceId;

	public Meter(int id, int meterTypeId, String name, int residenceId) {
		this.id = id;
		this.meterTypeId = meterTypeId;
		this.name = name;
		this.residenceId = residenceId;
	}

}

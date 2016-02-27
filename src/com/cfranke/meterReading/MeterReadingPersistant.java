package com.cfranke.meterReading;

import java.util.List;

public abstract class MeterReadingPersistant {
	// Meter Reading Data
	public abstract List<MeterReadingData> getAllMeterReadingData() throws MeterReadingPersistantException;

	public abstract List<MeterReadingData> getMeterReadingDataByMeterId(int meterId)
			throws MeterReadingPersistantException;

	public abstract List<MeterReadingData> getMeterReadingDataByMeterTypeId(int meterTypeId)
			throws MeterReadingPersistantException;

	public abstract int insertMeterReading(MeterReadingData meterData) throws MeterReadingPersistantException;

	public abstract void deleteMeterReading(int meterReadingId) throws MeterReadingPersistantException;

	// Meters
	public abstract List<Meter> getMetersByMeterTypeId(int meterTypeId)
			throws MeterReadingPersistantException;

	// Meter Types
	public abstract List<MeterType> getAllMeterTypes() throws MeterReadingPersistantException;
}

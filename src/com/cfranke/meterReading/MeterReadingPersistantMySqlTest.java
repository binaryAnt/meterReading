package com.cfranke.meterReading;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MeterReadingPersistantMySqlTest {

	@Test
	public void getAllMeterReadingData_defaultCall_atLeastOneRow()
			throws DBConfigurationLoaderException, MeterReadingPersistantException {

		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();
		List<MeterReadingData> meterData = mrPersistant.getAllMeterReadingData();
		boolean atLeastOneRow = false;

		System.out.println("");
		System.out.println("getAllMeterReadingData()");

		for (int i = 0; i < meterData.size(); i++) {
			printMeterLine(meterData.get(i));
			atLeastOneRow = true;
		}

		Assert.assertTrue(atLeastOneRow);
	}

	@Test
	public void getMeterReadingDataByMeterId_withId23_DataOfJustMeter23Received()
			throws MeterReadingPersistantException, DBConfigurationLoaderException {

		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();

		List<MeterReadingData> meterData = mrPersistant.getMeterReadingDataByMeterId(23);

		System.out.println("");
		System.out.println("getMeterReadingDataByMeterId(23)");

		for (int i = 0; i < meterData.size(); i++) {
			printMeterLine(meterData.get(i));
		}
	}

	@Test
	public void getMeterReadingDataByMeterTypeId_withId1_DataRetrieved()
			throws DBConfigurationLoaderException, MeterReadingPersistantException {
		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();

		List<MeterReadingData> meterReadingData;
		meterReadingData = mrPersistant.getMeterReadingDataByMeterTypeId(1);

		System.out.println("");
		System.out.println("getMeterReadingDataByMeterTypeId(1)");
		for (int i = 0; i < meterReadingData.size(); i++) {
			printMeterLine(meterReadingData.get(i));
		}
	}

	@Test
	public void insertMeterReading_normalRow_NoExc()
			throws DBConfigurationLoaderException, MeterReadingPersistantException {
		final int meterId = 77777;

		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();

		Date date = new Date();
		date = removeTime(date);

		BigDecimal value = new BigDecimal(134.567);
		// value hat sonst ewig viele Nachkommastellen
		BigDecimal truncated = value.setScale(3, BigDecimal.ROUND_DOWN);

		MeterReadingData meterData = new MeterReadingData(meterId, date, truncated);

		int keyOfInsertedRow = mrPersistant.insertMeterReading(meterData);
		System.out.println();
		System.out.println("insertMeterReading(meterData)");
		System.out.println("KeyOfInsertedRow: " + keyOfInsertedRow);

		MeterReadingData insertedMeterData = mrPersistant.getMeterReadingDataByMeterId(meterId).get(0);

		// wieder initialisieren
		mrPersistant.deleteMeterReading(keyOfInsertedRow);

		Assert.assertTrue(meterData.equals(insertedMeterData));
	}

	@Test
	public void deleteMeterReading_nonExistantRow_Exc() throws DBConfigurationLoaderException {
		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();

		MeterReadingPersistantException exc = null;

		try {
			mrPersistant.deleteMeterReading(98765432);
		} catch (MeterReadingPersistantException e) {
			exc = e;
		}

		Assert.assertNotNull(exc);
	}

	@Test
	public void getAllMeterTypes_defaultCall_atLeastOneRow()
			throws DBConfigurationLoaderException, MeterReadingPersistantException {

		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();
		List<MeterType> allMeterTypes;
		boolean atLeastOneRow = false;

		allMeterTypes = mrPersistant.getAllMeterTypes();

		System.out.println();
		System.out.println("getAllMeterTypes()");

		for (int i = 0; i < allMeterTypes.size(); i++) {
			MeterType meterType = allMeterTypes.get(i);
			System.out.println("Id: " + meterType.id + " Name: " + meterType.name);
			atLeastOneRow = true;
		}

		Assert.assertTrue(atLeastOneRow);
	}

	@Test
	public void getMetersByMeterTypeId_Id1_noExc()
			throws DBConfigurationLoaderException, MeterReadingPersistantException {
		MeterReadingPersistant mrPersistant = getMeterReadingPersistant();

		List<Meter> meters;

		meters = mrPersistant.getMetersByMeterTypeId(1);

		System.out.println();
		System.out.println("getMetersByMeterTypeId(1)");
		for (int i = 0; i < meters.size(); i++) {
			Meter meter = meters.get(i);
			System.out.println("Id: " + meter.id + " meterTypeId: " + meter.meterTypeId + " residenceId: "
					+ meter.residenceId + " name: " + meter.name);
		}
	}

	private MeterReadingPersistant getMeterReadingPersistant() throws DBConfigurationLoaderException {
		DBConfigurationLoader confLoader = new DBConfigurationLoaderFile();
		ConfigurationDatabase dbConf = confLoader.getConfigurationDatabase();

		return new MeterReadingPersistantMySql(dbConf);
	}

	private void printMeterLine(MeterReadingData meterData) {
		System.out.println("Id: " + meterData.meterId + " Datum: " + meterData.date + " Zählerstand: "
				+ meterData.value.toString());
	}

	private static Date removeTime(Date date) {
		// brauchen wir, denn in der DB gibt es die Zeit nicht und da ist es
		// immer 000000
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}

package com.cfranke.meterReading;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MeterReadingPersistantMySql extends MeterReadingPersistant {

	private ConfigurationDatabase dbConf;
	private Connection conn;

	public MeterReadingPersistantMySql(ConfigurationDatabase dbConf) {
		this.dbConf = dbConf;
	}

	@Override
	public List<MeterReadingData> getAllMeterReadingData() throws MeterReadingPersistantException {
		return getMeterDataWithoutWhereClause();
	}

	@Override
	public List<MeterReadingData> getMeterReadingDataByMeterId(int meterId) throws MeterReadingPersistantException {
		String whereClause = "WHERE meterId=" + Integer.toString(meterId);
		return getMeterDataByWhereClause(whereClause);
	}

	@Override
	public List<MeterReadingData> getMeterReadingDataByMeterTypeId(int meterTypeId)
			throws MeterReadingPersistantException {

		String sqlStatementAsText;
		Connection connection;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		List<MeterReadingData> meterReadingData = new ArrayList<MeterReadingData>();

		sqlStatementAsText = "select meterReadings.* from meterReadings " + "INNER JOIN meters "
				+ "on meters.id = meterReadings.meterId " + "where meters.meterTypeId=" + meterTypeId;

		connection = getConnection();

		try {
			preparedStatement = connection.prepareStatement(sqlStatementAsText);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				MeterReadingData meterData = convertResultSetToMeterReadingData(resultSet);
				meterReadingData.add(meterData);
			}

		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}

		return meterReadingData;
	}

	@Override
	public int insertMeterReading(MeterReadingData meterData) throws MeterReadingPersistantMySqlException {
		String sqlStatement = "INSERT INTO meterReadings (meterId, date, reading) VALUES (?, ?, ?)";
		ResultSet resultSet;
		int numberOfRowsInserted;
		Connection connection;

		try {
			java.sql.Date sqlDate = new java.sql.Date(meterData.date.getTime());

			connection = getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, meterData.meterId);
			preparedStatement.setDate(2, sqlDate);
			preparedStatement.setBigDecimal(3, meterData.value);

			numberOfRowsInserted = preparedStatement.executeUpdate();
			if (numberOfRowsInserted > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.first())
					return resultSet.getInt(1);
			}
			throw new MeterReadingPersistantMySqlException("Insert failed");
		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}
	}

	@Override
	public List<Meter> getMetersByMeterTypeId(int meterTypeId) throws MeterReadingPersistantMySqlException {
		List<Meter> meters = new ArrayList<Meter>();
		Connection connection;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String sqlAsText = "SELECT * FROM meters WHERE meters.meterTypeId=" + meterTypeId;

		connection = getConnection();
		try {
			preparedStatement = connection.prepareStatement(sqlAsText);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Meter meter = convertResultSetToMeter(resultSet);
				meters.add(meter);
			}
		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}

		return meters;
	}

	@Override
	public List<MeterType> getAllMeterTypes() throws MeterReadingPersistantMySqlException {
		List<MeterType> allMeterTypes = new ArrayList<MeterType>();
		String sqlStatementAsText = "SELECT * FROM meterTypes";

		PreparedStatement preparedStatement;

		try {
			preparedStatement = getConnection().prepareStatement(sqlStatementAsText);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				MeterType meterType = new MeterType(resultSet.getInt("id"), resultSet.getString("name"));
				allMeterTypes.add(meterType);
			}

		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}

		return allMeterTypes;
	}

	private Connection getConnection() throws MeterReadingPersistantMySqlException {
		if (conn == null) {
			try {

				Class.forName(dbConf.getDbDriver()).newInstance();
				conn = DriverManager.getConnection(dbConf.getDbURL() + dbConf.getDbName() + "?" + "user="
						+ dbConf.getDbUsername() + "&password=" + dbConf.getDbPassword());

			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new MeterReadingPersistantMySqlException("Connection not retrieved", e);
			}
		}

		return conn;
	}

	private List<MeterReadingData> getMeterDataByWhereClause(String whereClause)
			throws MeterReadingPersistantException {
		List<MeterReadingData> meterDataList = new ArrayList<MeterReadingData>();
		String statementText = "SELECT * FROM meterReadings ";

		if (whereClause != null) {
			statementText += whereClause;
		}

		PreparedStatement preparedStatement;
		try {
			preparedStatement = getConnection().prepareStatement(statementText);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				MeterReadingData meterData = convertResultSetToMeterReadingData(resultSet);
				meterDataList.add(meterData);
			}
		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}

		return meterDataList;
	}

	@Override
	public void deleteMeterReading(int meterReadingId) throws MeterReadingPersistantException {

		String statementText = "DELETE from meterReadings WHERE id=" + meterReadingId;

		try {
			PreparedStatement preparedStatement = getConnection().prepareStatement(statementText);
			int countOfDeletedRows = preparedStatement.executeUpdate();
			if (countOfDeletedRows == 0) {
				throw new MeterReadingPersistantMySqlException("no rows deleted");
			}
		} catch (SQLException e) {
			throw new MeterReadingPersistantMySqlException("SQL Error", e);
		}

	}

	private List<MeterReadingData> getMeterDataWithoutWhereClause() throws MeterReadingPersistantException {
		return getMeterDataByWhereClause(null);
	}

	private MeterReadingData convertResultSetToMeterReadingData(ResultSet resultSet) throws SQLException {
		MeterReadingData meterData = new MeterReadingData(resultSet.getInt("meterId"), resultSet.getDate("date"),
				resultSet.getBigDecimal("reading"));
		return meterData;
	}

	private Meter convertResultSetToMeter(ResultSet resultSet) throws SQLException {
		return new Meter(resultSet.getInt("id"), resultSet.getInt("meterTypeId"), resultSet.getString("name"),
				resultSet.getInt("residenceId"));
	}

}

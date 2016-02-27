package com.cfranke.meterReading;

public abstract class MeterReadingPersistantException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187794546875134062L;
	
	public MeterReadingPersistantException(String message){
		super(message);
	}
	
	public MeterReadingPersistantException(String message, Throwable throwable){
		super(message, throwable);
	}
}

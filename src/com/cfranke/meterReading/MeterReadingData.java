package com.cfranke.meterReading;

import java.math.BigDecimal;
import java.util.Date;

public class MeterReadingData {
	public final int meterId;
	public final Date date;
	public final BigDecimal value;

	public MeterReadingData(int meterId, Date date, BigDecimal value) {
		this.meterId = meterId;
		this.date = date;
		this.value = value;
	}

	public boolean equals(MeterReadingData meterData) {
		if (meterData.meterId == this.meterId)
			if (meterData.date.equals(this.date))
				if (meterData.value.equals(this.value))
					return true;
				else
					return false;
			else
				return false;
		else
			return false;
	}

}

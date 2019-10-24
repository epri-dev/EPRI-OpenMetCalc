package com.epri.metric_calculator.actions.snapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * The period of collecting data
 * 
 * @author JoWookJae
 *
 */
public class SnapshotPeriod {

	public static final String YEARLY_TYPE = "y";
	public static final String MONTHLY_TYPE = "M";
	public static final String WEEKLY_TYPE = "w";
	public static final String DAILY_TYPE = "d";
	public static final String HOURLY_TYPE = "h";
	public static final String MINUTELY_TYPE = "m";
	public static final String SECONDLY_TYPE = "s";

	public static final Map<String, Long> MILLIS;

	static {
		MILLIS = new HashMap<>();

		long value = 1;
		MILLIS.put(SECONDLY_TYPE, value *= 1000);
		MILLIS.put(MINUTELY_TYPE, value *= 60);
		MILLIS.put(HOURLY_TYPE, value *= 60);
		MILLIS.put(DAILY_TYPE, value *= 24);
		MILLIS.put(WEEKLY_TYPE, value *= 7);
		MILLIS.put(MONTHLY_TYPE, value *= 30);
		MILLIS.put(YEARLY_TYPE, value *= 12);
	}

	private String period;

	/**
	 * Constructor
	 * 
	 * @param period
	 */
	public SnapshotPeriod(String period) {
		this.period = period;
	}

	/**
	 * Return type
	 * 
	 * @return
	 */
	public String getType() {
		return period.substring(period.length() - 1, period.length());
	}

	/**
	 * Return times
	 * 
	 * @return
	 */
	public int getTimes() {
		return Integer.parseInt(period.substring(0, period.length() - 1));
	}

	/**
	 * Convert period to milli-seconds
	 * 
	 * @return
	 */
	public long convertToMillis() {
		return MILLIS.get(getType()) * getTimes();
	}
}
package com.sst.sstat.metric;

/**
 * Metric type
 * 
 * @author JoWookJae
 *
 */
public enum MetricType {

	STRATEGIC(0),

	TACTICAL(1),

	OPERATIONAL(2);

	public static final String ID_SUFFIX_STRATEGIC = "S";
	public static final String ID_SUFFIX_TACTICAL = "T";
	public static final String ID_SUFFIX_OPERATIONAL = "O";
	
	private static final String STR_STRATEGIC = "STRATEGIC"; //$NON-NLS-1$
	private static final String STR_TACTICAL = "TACTICAL"; //$NON-NLS-1$
	private static final String STR_OPERATIONAL = "OPERATIONAL"; //$NON-NLS-1$

	private int value;

	private MetricType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String str = null;

		switch (this) {
		case STRATEGIC:
			str = STR_STRATEGIC;
		case TACTICAL:
			str = STR_TACTICAL;
		case OPERATIONAL:
			str = STR_OPERATIONAL;
		}

		return str;
	}
}

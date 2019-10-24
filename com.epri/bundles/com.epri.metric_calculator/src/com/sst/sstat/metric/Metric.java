package com.sst.sstat.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class of metric
 * 
 * @author JoWookJae
 *
 */
public class Metric implements Serializable {

	private static final long serialVersionUID = -1353318127554611980L;

	public static final float RESULT_ERROR = -2.0F;
	public static final float RESULT_NONE = -1.0F;

	public static final String RESULT_ERROR_STR = "ERROR";
	public static final String RESULT_NONE_STR = "NONE";

	/**
	 * The name of metric
	 */
	private String name;

	/**
	 * The description of metric
	 */
	private String description;

	private String id;

	/**
	 * The value of metric
	 */
	private float value;

	private float referenceValue = 8;

	private float targetValue = 7;

	/**
	 * Factor list of metric
	 */
	private List<Factor> factors;

	public Metric() {
		factors = new ArrayList<>();
		value = RESULT_NONE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addFactor(Factor factor) {
		factors.add(factor);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Factor> getFactors() {
		return factors;
	}

	public String getFormulaName() {
		return id.replace('-', '_');
	}

	public float getValue() {
		return value;
	}

	public float getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(float referenceValue) {
		this.referenceValue = referenceValue;
	}

	public float getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(float targetValue) {
		this.targetValue = targetValue;
	}

	public String getValueString() {
		if (Float.compare(value, RESULT_NONE) == 0) {
			return RESULT_NONE_STR;
		} else if (Float.compare(value, RESULT_ERROR) == 0) {
			return RESULT_ERROR_STR;
		}

		return Float.toString(value);
	}

	public void setValue(float value) {
		this.value = (float) (Math.round(value * 100d) / 100d);
	}

	public void setValue(String valueStr) throws MetricDefinitionInvalidException {
		if (valueStr.isEmpty() || valueStr.equals(RESULT_NONE_STR)) {
			value = RESULT_NONE;
		} else if (valueStr.equals(RESULT_ERROR_STR)) {
			value = RESULT_ERROR;
		} else {
			try {
				value = Float.valueOf(valueStr);
			} catch (NumberFormatException e) {
				throw new MetricDefinitionInvalidException(e.getMessage());
			}
		}
	}

	public boolean isError() {
		if (Float.compare(value, RESULT_ERROR) == 0) {
			return true;
		}

		return false;
	}

	public boolean isOutOfRange() {
		if (value == RESULT_NONE) {
			return false;
		}

		return value < referenceValue;
	}

	public boolean isNoneValue() {
		return Float.compare(value, RESULT_NONE) == 0;
	}
}

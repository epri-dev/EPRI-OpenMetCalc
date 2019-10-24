package com.epri.metric_calculator.event;

/**
 * MetCalc event class
 * 
 * @author JoWookJae
 *
 */
public class MetCalcEvent {

	/**
	 * Event type
	 */
	private MetCalcEventType type;

	public MetCalcEvent(MetCalcEventType type) {
		this.type = type;
	}

	public MetCalcEventType getType() {
		return type;
	}

	public void setType(MetCalcEventType type) {
		this.type = type;
	}
}

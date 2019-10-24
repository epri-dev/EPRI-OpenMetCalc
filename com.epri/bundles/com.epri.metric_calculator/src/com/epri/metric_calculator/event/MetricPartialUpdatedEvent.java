package com.epri.metric_calculator.event;

public class MetricPartialUpdatedEvent extends MetCalcEvent {

	private String id;

	public MetricPartialUpdatedEvent() {
		super(MetCalcEventType.METRIC_UPDATED);
	}
	
	public MetricPartialUpdatedEvent(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}
}

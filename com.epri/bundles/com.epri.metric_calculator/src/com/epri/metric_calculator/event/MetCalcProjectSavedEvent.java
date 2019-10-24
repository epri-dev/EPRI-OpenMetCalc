package com.epri.metric_calculator.event;

import com.epri.metric_calculator.MetCalcProject;

public class MetCalcProjectSavedEvent extends MetCalcEvent {

	private MetCalcProject project;

	public MetCalcProjectSavedEvent(MetCalcProject project) {
		super(MetCalcEventType.PROJECT_SAVED);
		this.project = project;
	}

	public MetCalcProject getProject() {
		return project;
	}
}

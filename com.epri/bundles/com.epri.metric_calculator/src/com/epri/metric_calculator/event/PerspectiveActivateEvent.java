package com.epri.metric_calculator.event;

import org.eclipse.ui.IPerspectiveDescriptor;

/**
 * Perspective activate event.
 * 
 * @author JoWookJae
 *
 */
public class PerspectiveActivateEvent extends MetCalcEvent {

	private IPerspectiveDescriptor perspectiveDscriptor;

	public PerspectiveActivateEvent(IPerspectiveDescriptor perspective) {
		super(MetCalcEventType.PERSPECTIVE_ACTIVATED);
		this.perspectiveDscriptor = perspective;
	}

	public IPerspectiveDescriptor getPerspectiveDscriptor() {
		return perspectiveDscriptor;
	}

	public void setPerspectiveDscriptor(IPerspectiveDescriptor perspectiveDscriptor) {
		this.perspectiveDscriptor = perspectiveDscriptor;
	}
}

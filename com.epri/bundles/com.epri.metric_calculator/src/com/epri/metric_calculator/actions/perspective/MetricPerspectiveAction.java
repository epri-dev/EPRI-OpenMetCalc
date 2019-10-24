package com.epri.metric_calculator.actions.perspective;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.perspectives.MetricPerspective;

public class MetricPerspectiveAction extends AbstractAction {
	private Action[] perspectiveActions;

	public MetricPerspectiveAction() {
		super(Messages.MetricPerspectiveAction_METRIC_PERSPECTIVE_ACTION_TEXT, IconFactory.COMMON_METRIC, SWT.TOGGLE);
	}

	public void setPerspectiveActions(Action[] perspectiveActions) {
		this.perspectiveActions = perspectiveActions;
	}

	@Override
	public void run() {
		if (!isChecked()) {
			setChecked(true);
			return;
		}

		if (PlatformUI.getWorkbench() != null) {
			IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry()
					.findPerspectiveWithId(MetricPerspective.ID);

			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
		}

		for (Action action : perspectiveActions) {
			if (!(action == this) && action.isChecked()) {
				action.setChecked(false);
			}
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.perspectives.MetricPerspective";
	}
}

package com.epri.metric_calculator.actions.perspective;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.perspectives.DataPointPerspective;

/**
 * @author JoWookJae
 *
 */
public class DataPointPerspectiveAction extends AbstractAction {

	private Action[] perspectiveActions;

	public DataPointPerspectiveAction() {
		super(Messages.DataPointPerspectiveAction_DATA_POINT_PERSPECTIVE_ACTION_TEXT, IconFactory.COMMON_DATA_POINT,
				SWT.TOGGLE);
	}

	/**
	 * 
	 * 
	 * @param perspectiveActions
	 */
	public void setPerspectiveActions(Action[] perspectiveActions) {
		this.perspectiveActions = perspectiveActions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epri.metric_calculator.actions.AbstractAction#run()
	 */
	@Override
	public void run() {
		if (!isChecked()) {
			setChecked(true);
			return;
		}

		if (PlatformUI.getWorkbench() != null) {
			IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry()
					.findPerspectiveWithId(DataPointPerspective.ID);

			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
		}

		for (Action action : perspectiveActions) {
			if (action != this && action.isChecked()) {
				action.setChecked(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epri.metric_calculator.actions.AbstractAction#getId()
	 */
	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.DataPointPerspectiveAction";
	}
}

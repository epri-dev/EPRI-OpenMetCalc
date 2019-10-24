package com.epri.metric_calculator.actions.snapshot;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * Abstract class for snapshot action
 * 
 * @author JoWookJae
 *
 */
public abstract class AbSnapshotAction extends AbstractAction {

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public AbSnapshotAction(int type) {
		super(Messages.AbSnapshotAction_Name, IconFactory.METRIC_COLLECT_DATA, type);
	}

	@Override
	public abstract void run();

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.SnapshotAction"; //$NON-NLS-1$
	}
}

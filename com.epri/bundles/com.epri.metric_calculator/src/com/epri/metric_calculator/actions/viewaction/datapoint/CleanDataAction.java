package com.epri.metric_calculator.actions.viewaction.datapoint;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.viewaction.AbsConsoleViewAction;
import com.epri.metric_calculator.views.ConsoleView;
import com.epri.metric_calculator.views.datapoint.DataPointScopeView;
import com.epri.metric_calculator.views.datapoint.DataPointTableView;

/**
 * Action to clean table data
 * 
 * @author JoWookJae
 *
 */
public class CleanDataAction extends AbsConsoleViewAction {

	public CleanDataAction() {
		super(Messages.ImportConsoleView_CLEAN_DATA_ACTION_TEXT, IconFactory.DATA_POINT_CLEAN_DATA,
				Messages.CleanDataAction_0, "com.epri.metric_calculator.perspectives.DataPointPerspective");
	}

	@Override
	protected void run(ConsoleView consoleView) {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If you do not have a current project, you can create a project
		// without performing an action.
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// No selection in data point scope view
		if (DataPointScopeView.getDefault().getSelection() == null) {
			return;
		}

		// Open question dialog.
		String dpName = DataPointScopeView.getDefault().getSelection().getSimpleName();
		boolean answer = MessageDialog.openQuestion(Display.getDefault().getActiveShell(), Messages.Question,
				NLS.bind(Messages.CleanDataAction_2, dpName));
		if (!answer) {
			return;
		}

		// Clean data from the table
		DataPointTableView.getDefault().cleanData();
		consoleView.info(NLS.bind(Messages.CleanDataAction_1, dpName));

		// Clean data from the current project.
		Class<?> selection = DataPointScopeView.getDefault().getSelection();
		if (currentProject.get(selection).size() != 0) {
			currentProject.get(selection).clear();
			currentProject.setDirty(true);
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.viewaction.datapoint.CleanDataAction"; //$NON-NLS-1$
	}
}
package com.epri.metric_calculator.actions.file;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.sst.sstat.metric.MetricDefinitionInvalidException;

/**
 * Action performed [File] > [Reset metric]
 * 
 * Load metric file(.xml)
 * 
 * @author JoWookJae
 *
 */
public class ResetMetricAction extends AbstractAction {

	public ResetMetricAction() {
		super("Reset Metric", IconFactory.COMMON_RESET_METRIC); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Shell shell = Display.getDefault().getActiveShell();

		MetCalcProject project = MetCalcProject.getCurrent();

		// If you do not have a current project, you can create a project
		// without performing an action.
		if (project == null) {
			NoProjectMessageDialog.open();
			return;
		}

		try {
			project.reloadMetricConfig();
		} catch (IOException e) {
			MessageDialog.openError(shell, Messages.ResetMetricAction_ERR_TITLE_0,
					Messages.ResetMetricAction_ERR_MSG_0);
		} catch (MetricDefinitionInvalidException e) {
			// TODO : 에러 핸들링
			e.printStackTrace();
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ResetMetric"; //$NON-NLS-1$
	}
}

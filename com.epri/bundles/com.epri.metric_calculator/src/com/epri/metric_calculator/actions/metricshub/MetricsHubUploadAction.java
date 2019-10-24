package com.epri.metric_calculator.actions.metricshub;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;

public class MetricsHubUploadAction extends AbstractAction {

	public MetricsHubUploadAction() {
		super(Messages.RecalculationAction_0, IconFactory.COMMON_UPLOAD_TO_CLOUD);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If you do not have a current project, you can create a project
		// without performing an action.
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// Ask if you want to save any unsaved data in the current project
		if (currentProject.isDirty()) {
			boolean willSave = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
					Messages.ExportDataAction_SAVE_DIALOG_TITLE, Messages.ExportDataAction_SAVE_DIALOG_MESSAGE);

			if (willSave) {
				try {
					currentProject.save();
				} catch (Throwable e) {
					notifyFail(e.getMessage());
					return;
				}
			}
		}
		
		MetricsHubUploadWizardDialog uploadDialog = new MetricsHubUploadWizardDialog();
		uploadDialog.open();
	}
	
	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.metricshub.MetricsHubUploadAction";
	}
}

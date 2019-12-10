package com.epri.metric_calculator.actions.recalculation;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * 
 * 
 * @author JoWookJae
 **/
public class RecalculationAction extends AbstractAction {

	public RecalculationAction() {
		super("Re-Calculate", IconFactory.METRIC_RECALCULATE);
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
//		if (currentProject.isDirty()) {
//			boolean willSave = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
//					Messages.ExportDataAction_SAVE_DIALOG_TITLE, Messages.ExportDataAction_SAVE_DIALOG_MESSAGE);
//
//			if (willSave) {
//				try {
//					currentProject.save();
//				} catch (Throwable e) {
//					notifyFail(e.getMessage());
//					return;
//				}
//			}
//		}
		
		RecalculationWizardDialog recalculationDialog = new RecalculationWizardDialog();
		recalculationDialog.open();
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.recalculation.RecalculationAction";
	}
}
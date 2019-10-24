package com.epri.metric_calculator.actions.file;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.ProjectFileSaveFailException;
import com.sst.sstat.metric.MetricSaveFailException;

/**
 * A dialog for showing messages to the user when the current project is not
 * saved and do something else
 * 
 * @author JoWookJae
 *
 */
public class UnsavedWarningDialog {

	/**
	 * Open unsave warning dialog
	 * 
	 * @return
	 */
	public static int open() {
		MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), "Project unsaved", null,
				"There is unsaved data. Any unsaved data will be lost. Do you want to save it?", MessageDialog.WARNING,
				new String[] { "Yes", "No", "Cancel" }, 0) {

			@Override
			protected void buttonPressed(int buttonId) {
				super.buttonPressed(buttonId);

				switch (buttonId) {
				case 0:
					// When [OK] pressed, save project
					try {
						MetCalcProject.getCurrent().save();
					} catch (ProjectFileSaveFailException e) {
						MessageDialog.openError(Display.getDefault().getActiveShell(), "Project save failure",
								e.getMessage());
					} catch (MetricSaveFailException e) {
						// TODO 에러 핸들링
						e.printStackTrace();
					}
					break;
				}
			}
		};

		return dlg.open();
	}
}

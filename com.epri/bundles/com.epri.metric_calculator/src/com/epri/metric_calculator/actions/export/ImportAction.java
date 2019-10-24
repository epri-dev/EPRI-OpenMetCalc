package com.epri.metric_calculator.actions.export;

import java.io.File;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.actions.file.UnsavedWarningDialog;

/**
 * Action performed [Import] > [Import data] (Only analyst).
 * 
 * Import encrypted project.
 * 
 * @author JoWookJae
 *
 */
public class ImportAction extends AbstractAction {

	public ImportAction() {
		super(Messages.ImportAction_IMPORT_ACTION_TEXT, IconFactory.COMMON_IMPORT_DATA);
	}

	@Override
	public void run() {
		Shell shell = Display.getDefault().getActiveShell();

		// if the project has been modified, ask will save
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if (currentProject != null && currentProject.isDirty()) {
			int ret = UnsavedWarningDialog.open();
			// Cancel or ESC
			if (ret == 2 || ret == -1) {
				return;
			}
		}

		// Select file(.security)
		FileDialog dlg = new FileDialog(shell);
		dlg.setFilterExtensions(new String[] { "*." + Const.METCALC_SECURED_PROJECT_EXTENSION }); //$NON-NLS-1$
		String selection = dlg.open();

		// Selected file is null, will return
		if (selection == null) {
			return;
		}

		// Set current project to imported project
		try {
			if (MetCalcProject.importProject(new File(selection))) {
				notifySuccess();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ImportData"; //$NON-NLS-1$
	}
}

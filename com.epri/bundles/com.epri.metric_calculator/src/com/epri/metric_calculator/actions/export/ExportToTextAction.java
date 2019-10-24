package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.ProjectFileSaveFailException;
import com.epri.metric_calculator.actions.AbstractAction;
import com.sst.sstat.metric.MetricSaveFailException;

/**
 * Export > Export to text
 * 
 * Write project to text file(.txt)
 * 
 * @author JoWookJae
 *
 */
public class ExportToTextAction extends AbstractAction {

	public ExportToTextAction() {
		super(Messages.ExportToTextAction_EXPORT_TO_TEXT_ACTION_TEXT, IconFactory.COMMON_EXPORT_DATA_TO_TEXT);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// if current project is null, will return
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// if the project has been modified, ask will save
		if (currentProject.isDirty()) {
			boolean willSave = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
					Messages.ExportDataAction_SAVE_DIALOG_TITLE, Messages.ExportDataAction_SAVE_DIALOG_MESSAGE);

			if (willSave) {
				try {
					currentProject.save();
				} catch (ProjectFileSaveFailException e) {
					MessageDialog.openError(Display.getDefault().getActiveShell(),
							Messages.ERROR_TITLE, e.getMessage());
					e.printStackTrace();
				} catch (MetricSaveFailException e) {
					// TODO error handling
					e.printStackTrace();
				}
			}
		}

		// Select destination
		DirectoryDialog dlg = new DirectoryDialog(Display.getCurrent().getActiveShell());
		dlg.setFilterPath(currentProject.getProjectDir());
		String selection = dlg.open();
		if (selection == null)
			return;

		// Get only name
		String name = currentProject.getFile().getName();
		int pos = name.lastIndexOf("."); //$NON-NLS-1$
		if (pos > 0) {
			name = name.substring(0, pos);
		}

		File dest = new File(new File(selection), name + "." + MetCalcProject.TEXT_EXTENSION); //$NON-NLS-1$

		try {
			currentProject.exportToText(dest.getAbsolutePath());
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.ERROR_TITLE,
					Messages.ExportToTextAction_ERROR_MSG_0 + dest.getAbsolutePath());
			e.printStackTrace();
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ExportToText"; //$NON-NLS-1$
	}
}

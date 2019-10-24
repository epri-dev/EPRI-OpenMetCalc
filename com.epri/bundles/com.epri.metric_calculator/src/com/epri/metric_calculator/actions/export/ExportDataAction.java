package com.epri.metric_calculator.actions.export;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * Action performed [Export] > [Export data]
 * 
 * Encrypt the project and save to file(.security).
 * 
 * @author JoWookJae
 *
 */
public class ExportDataAction extends AbstractAction {

	public ExportDataAction() {
		super(Messages.ExportDataAction_EXPORT_DATA_ACTION_TEXT, IconFactory.COMMON_EXPORT_DATA);
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

		// 저 Select storage path
		DirectoryDialog dlg = new DirectoryDialog(Display.getCurrent().getActiveShell());
		dlg.setFilterPath(currentProject.getProjectDir());
		String selection = dlg.open();
		if (selection == null)
			return;

		// 저장할 파일 이름과 경로를 생성함.
		String name = currentProject.getFile().getName();
		int pos = name.lastIndexOf("."); //$NON-NLS-1$
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		File dest = new File(new File(selection), name + "." + Const.METCALC_SECURED_PROJECT_EXTENSION); //$NON-NLS-1$

		// export 수행
		try {
			if (currentProject.exportProject(dest.getAbsolutePath())) {
				notifySuccess();
			} else {
				notifyFail();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ExportData"; //$NON-NLS-1$
	}
}

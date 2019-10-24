package com.epri.metric_calculator.actions.file;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * Action performed [File] > [Open]
 * 
 * Load project file(.project) and open
 * 
 * @author JoWookJae
 *
 */
public class OpenAction extends AbstractAction {

	private static final String[] FILE_FILTER = new String[] {
			"*." + Const.METCALC_PROJECT_EXTENSION + ";*." + Const.METCALC_SECURED_PROJECT_EXTENSION }; //$NON-NLS-2$

	/**
	 * Constructor
	 */
	public OpenAction() {
		super(Messages.OpenAction_OPEN_ACTION_TEXT, IconFactory.COMMON_OPEN);
	}

	@Override
	public void run() {
		// 현재 프로젝트 저장 유무 검사
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if (currentProject != null && currentProject.isDirty()) {
			int ret = UnsavedWarningDialog.open();
			// Cancel or ESC
			if (ret == 2 || ret == -1) {
				return;
			}
		}

		FileDialog dlg = new FileDialog(Display.getDefault().getActiveShell());
		dlg.setFilterExtensions(FILE_FILTER);
		String src = dlg.open();
		if (src == null)
			return;

		File fileSrc = new File(src);
		if (!fileSrc.exists()) {
			return;
		}

		try {
			if (src.endsWith(Const.METCALC_PROJECT_EXTENSION)) {
				MetCalcProject.open(fileSrc);
			} else if (src.endsWith(Const.METCALC_SECURED_PROJECT_EXTENSION)) {
				MetCalcProject.importProject(fileSrc);
			} else {
				notifyFail("Unknown file.");
				return;
			}

			notifySuccess();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			notifyFail(Messages.OpenAction_ERROR_MSG_0 + fileSrc.getAbsolutePath());
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.Open"; //$NON-NLS-1$
	}
}

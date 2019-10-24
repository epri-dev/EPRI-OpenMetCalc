package com.epri.metric_calculator.actions.file;

import java.io.File;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * File > Save as
 * 
 * @author JoWookJae
 *
 */
public class SaveAsAction extends AbstractAction {

	public SaveAsAction() {
		super(Messages.SaveAsAction_SAVE_AS_ACTION_TEXT, IconFactory.COMMON_SAVE_AS);
	}

	@Override
	public void run() {
		final MetCalcProject currentProject = MetCalcProject.getCurrent();

		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// 현재 프로젝트 저장 유무 검사
		if (currentProject.isDirty()) {
			int ret = UnsavedWarningDialog.open();
			// Cancel 버튼 또는 Ecs에 의한 종료
			if (ret == 2 || ret == -1) {
				return;
			}
		}

		SaveAsWizard wizard = new SaveAsWizard();
		WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);

		int close = wizardDialog.open();
		if (close == 0) {
			File result = wizard.getResult();
			try {
				currentProject.saveAs(result.getAbsolutePath());
				notifySuccess();
			} catch (Throwable e) {
				e.printStackTrace();
				notifyFail(e.getMessage());
			}
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.SaveAs"; //$NON-NLS-1$
	}
}

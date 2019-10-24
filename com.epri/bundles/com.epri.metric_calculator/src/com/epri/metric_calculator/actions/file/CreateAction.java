
package com.epri.metric_calculator.actions.file;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.actions.AbstractAction;
import com.sst.sstat.metric.MetricDefinitionInvalidException;
import com.sst.sstat.metric.MetricSaveFailException;

/**
 * Action performed [File] > [Create]
 * 
 * Create a new project
 * 
 * @author JoWookJae
 *
 */
public class CreateAction extends AbstractAction {

	public CreateAction() {
		super(Messages.CreateAction_CREATE_ACTION_TEXT, IconFactory.COMMON_CREATE);
	}

	@Override
	public void run() {
		// if the project has been modified, ask will save
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if (currentProject != null && currentProject.isDirty()) {
			int ret = UnsavedWarningDialog.open();
			// Cancel or ESC
			if (ret == 2 || ret == -1) {
				return;
			}
		}

		// Open CreateProjectWizard
		CreateProjectWizard wizard = new CreateProjectWizard();
		WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);

		if (wizardDialog.open() == WizardDialog.CANCEL) {
			return;
		}

		File projectDir = wizard.getResult();
		if (!projectDir.exists()) {
			projectDir.mkdirs();
		}

		// Create new MetCalc project
		MetCalcProject newProject = null;
		File fileCreated = null;
		try {
			fileCreated = new File(projectDir, projectDir.getName() + "." + Const.METCALC_PROJECT_EXTENSION);
			fileCreated.createNewFile();

			// 프로젝트 생성
			newProject = new MetCalcProject(fileCreated);
		} catch (IOException | MetricDefinitionInvalidException e) {
			e.printStackTrace();
			MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.CreateAction_ERROR_TITLE,
					e.getMessage());
			return;
		}

		try {
			newProject.save(true);
			MetCalcProject.open(newProject);
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.CreateAction_ERROR_TITLE,
					"Cannot save file : " + fileCreated);
			e.printStackTrace();
		} catch (MetricSaveFailException e) {
			// TODO 에러 핸들링
			e.printStackTrace();
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.Create"; //$NON-NLS-1$
	}
}

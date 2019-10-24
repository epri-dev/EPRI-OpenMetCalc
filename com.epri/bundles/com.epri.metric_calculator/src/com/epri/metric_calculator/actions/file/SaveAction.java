package com.epri.metric_calculator.actions.file;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.ProjectFileSaveFailException;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcProjectSavedEvent;
import com.sst.sstat.metric.MetricSaveFailException;

/**
 * Action performed [File] > [Save]
 * 
 * Save project to project file(.project)
 * 
 * @author JoWookJae
 *
 */
public class SaveAction extends AbstractAction {

	public SaveAction() {
		super(Messages.SaveAction_SAVE_ACTION_TEXT, IconFactory.COMMON_SAVE);
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

		try {
			// Save
			if (currentProject.save()) {
				MetCalcEventManager.getInstance().fire(new MetCalcProjectSavedEvent(currentProject));
			}
		} catch (ProjectFileSaveFailException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.SaveAction_ERR_TITLE,
					e.getMessage());
			e.printStackTrace();
		} catch (MetricSaveFailException e) {
			// TODO 에러 핸들링
			e.printStackTrace();
		}

	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.Save"; //$NON-NLS-1$
	}
}

package com.epri.metric_calculator.actions.export;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;

/**
 * [Import/Export] > [Import metric]
 * 
 * Import custom metric groups
 * 
 * @author JoWookJae
 *
 */
public class ImportConfOfMetricAction extends AbstractAction {

	public ImportConfOfMetricAction() {
		super(Messages.ImportMetricAction_0, IconFactory.COMMON_IMPORT_METRIC);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		Shell shell = Display.getDefault().getActiveShell();
		FileDialog fileDlg = new FileDialog(shell);
		fileDlg.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
		String filePath = fileDlg.open();
		if (filePath == null)
			return;

		try {
			currentProject.importMetric(filePath);
			notifySuccess();
		} catch (Exception e) {
			notifyFail(e.getMessage());
			return;
		}

		MetCalcEventManager.getInstance().fire(new MetCalcEvent(MetCalcEventType.METRIC_UPDATED));
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ImportMetric"; //$NON-NLS-1$
	}

}

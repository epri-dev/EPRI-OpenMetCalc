package com.epri.metric_calculator.actions.export;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.sst.sstat.metric.MetricManager;

/**
 * Import reference value from excel.
 * 
 * @author JoWookJae
 *
 */
public class ImportReferenceValueAction extends AbstractAction {

	/**
	 * Constructor
	 */
	public ImportReferenceValueAction() {
		super(Messages.ImportReferenceValueAction_0, IconFactory.IMPORT_REFERENCE_VALUE);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If no project, stop action.
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		Shell shell = Display.getDefault().getActiveShell();
		FileDialog fileDlg = new FileDialog(shell);
		fileDlg.setFilterExtensions(new String[] { "*" + Const.EXCEL_FILE_EXTENSION }); //$NON-NLS-1$

		String filePath = fileDlg.open();
		if (filePath == null) {
			// Do nothing and finish
			return;
		}

		MetricManager metricManager = currentProject.getMetricManager();
		try {
			ImportReferenceJob job = new ImportReferenceJob(metricManager, new File(filePath));
			job.schedule();

			job.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					super.done(event);

					IStatus result = event.getResult();
					if (result.isOK()) {
						MetCalcEventManager.getInstance().fire(new MetCalcEvent(MetCalcEventType.METRIC_UPDATED));
						notifySuccess();
					} else {
						notifyFail(result.getMessage());
					}
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.export.ImportReferenceValueAction";
	}
}

package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.util.WindowsExplorerUtil;
import com.sst.sstat.metric.MetricSaveFailException;

/**
 * Action performed [Export] > [Export metric]
 * 
 * Export metric to file(.metric).
 * 
 * @author JoWookJae
 *
 */
public class ExportConfOfMetricAction extends AbstractAction {

	public ExportConfOfMetricAction() {
		super(Messages.ExportMetricAction_0, IconFactory.COMMON_EXPORT_METRIC);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If current project is null, will return
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// Select destination
		DirectoryDialog dirDlg = new DirectoryDialog(Display.getDefault().getActiveShell());
		dirDlg.setFilterPath(currentProject.getProjectDir());
		String dir = dirDlg.open();
		if (dir == null)
			return;

		File dest = new File(dir,
				currentProject.getProjectName() + Messages.ExportMetricAction_METRIC_FILE_SUFFIX + ".xml");

		// Do "Export metric"
		try {
			currentProject.exportMetric(dest);
			notifySuccess();
			WindowsExplorerUtil.select(dest.getAbsolutePath());
		} catch (MetricSaveFailException e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		} catch (IOException e) {
			// Do nothing...
			e.printStackTrace();
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ExportMetric"; //$NON-NLS-1$
	}

}

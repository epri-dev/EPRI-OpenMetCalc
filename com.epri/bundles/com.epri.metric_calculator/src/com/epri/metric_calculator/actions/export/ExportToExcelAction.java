package com.epri.metric_calculator.actions.export;

import java.io.File;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.util.WindowsExplorerUtil;

public class ExportToExcelAction extends AbstractAction {

	public ExportToExcelAction() {
		super("Export Metric to Excel", IconFactory.COMMON_EXPORT_TO_EXCEL);
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
		if (dir == null) {
			return;
		}

		File dest = new File(dir, currentProject.getProjectName() + Messages.ExportMetricAction_METRIC_FILE_SUFFIX
				+ Const.EXCEL_FILE_EXTENSION);

		MetricExcelExporter excelExporter = new MetricExcelExporter();
		try {
			excelExporter.export(dest.getAbsolutePath());
			notifySuccess();
			WindowsExplorerUtil.select(dest.getAbsolutePath());
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.command.ExportToExcel";
	}
}

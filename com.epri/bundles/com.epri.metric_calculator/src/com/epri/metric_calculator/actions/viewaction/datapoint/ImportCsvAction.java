package com.epri.metric_calculator.actions.viewaction.datapoint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.viewaction.AbsConsoleViewAction;
import com.epri.metric_calculator.views.ConsoleView;

/**
 * Action to Import csv files
 * 
 * @author JoWookJae
 *
 */
public class ImportCsvAction extends AbsConsoleViewAction {

	/**
	 * Set text and image of action
	 */
	public ImportCsvAction() {
		super("Import CSV", IconFactory.DATA_POINT_IMPORT, "com.epri.metric_calculator.views.datapoint.ImportConsoleView",
				"com.epri.metric_calculator.perspectives.DataPointPerspective");
	}

	@Override
	protected void run(ConsoleView consoleView) {
		// If you do not have a current project, you can create a project
		// without performing an action.
		if (MetCalcProject.getCurrent() == null) {
			NoProjectMessageDialog.open();
			return;
		}

		// Select csv files to import
		FileDialog dlg = new FileDialog(Display.getDefault().getActiveShell(), SWT.MULTI);
		dlg.setFilterExtensions(new String[] { "*.csv" }); //$NON-NLS-1$
		String file = dlg.open();

		// If no selection then return
		if (file == null)
			return;

		// Create a list of file paths
		File dir = new File(file).getParentFile();
		String[] filenames = dlg.getFileNames();

		if (filenames.length != 0) {
			List<String> files = new ArrayList<String>();

			for (String filename : filenames) {
				files.add(new File(dir, filename).getAbsolutePath());
			}

			ImportDataPointJob job = new ImportDataPointJob(files.toArray(new String[files.size()]));
			job.setUser(true);
			job.schedule();
			
			MetCalcProject.getCurrent().setDirty(true);
		}

	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.viewaction.datapoint.ImportCsvAction";
	}
}
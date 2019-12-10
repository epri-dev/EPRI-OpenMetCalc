package com.epri.metric_calculator.actions.viewaction.datapoint;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.supercsv.exception.SuperCsvReflectionException;

import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.views.ConsoleView;
import com.epri.metric_calculator.views.datapoint.DataPointScopeView;
import com.sst.sstat.CsvImpoter;
import com.sst.sstat.model.IDataPointScope;

/**
 * Job to import
 * 
 * @author JoWookJae
 *
 */
public class ImportDataPointJob extends Job {
	private String[] files;
	private ConsoleView consoleView;

	public ImportDataPointJob(String[] files) {
		super(Messages.ImportConsoleView_IMPORT_JOB_TEXT);
		this.files = files;
		this.consoleView = (ConsoleView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("com.epri.metric_calculator.views.datapoint.ImportConsoleView");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// Notify files to import, set total work to length of files
		monitor.beginTask("Import " + files.length + " files.", files.length);

		// Clear console
		Display.getDefault().syncExec(() -> {
			consoleView.clear();
		});

		Class<?> itemClass = null;
		for (int i = 0; i < files.length; i++) {
			String filePath = files[i];

			// Import contents of file "filePath"
			monitor.subTask("Importing \"" + filePath + "\"(" + i + 1 + "/" + files.length + ")");
			List<Object> contents = null;

			// Set print stream to console
			try {
				// Read file
				contents = CsvImpoter.readWithCsvBeanReader(filePath, consoleView.getPrintStream());

				if (contents != null && contents.size() != 0) {
					itemClass = contents.get(0).getClass();

					// Add into current project
					int containIndex;
					for (Object o : contents) {
						MetCalcProject prj = MetCalcProject.getCurrent();
						IDataPointScope dps = (IDataPointScope) o;
						containIndex = prj.contains(itemClass, dps);
						if (containIndex > -1) {
							// It contains data of the same id.
							prj.replace(itemClass, containIndex, o);
//							consoleView.warning(String.format("It contains data of the same id. Overwritten with new values. ID = %s", dps.getId()));
						} else {
							// It does not contain data of the same id.
							prj.append(itemClass, o);
						}
					}

				}
			} catch (SuperCsvReflectionException e) {
				// Reflection failure
				consoleView.error("Fail to import " + filePath + "(It may contain unsupported type).");
				if (e.getMessage() != null && !e.getMessage().isEmpty()) {
					consoleView.error(e.getMessage());
				}
			} catch (Exception e) {
				consoleView.error("Unknown error while importing " + filePath + ".");
				if (e.getMessage() != null && !e.getMessage().isEmpty()) {
					consoleView.error(e.getMessage());
				}

			}

			monitor.worked(1);
		}

		// Set selection of data point scope view to last item
		if (itemClass != null) {
			final Class<?> fItemClass = itemClass;
			Display.getDefault().asyncExec(() -> {
				DataPointScopeView.getDefault().setSelection(fItemClass);
			});
		}

		return Status.OK_STATUS;
	}
}
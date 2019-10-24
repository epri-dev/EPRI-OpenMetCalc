package com.epri.metric_calculator.views.dashboard;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.event.AbMetCalcEventHandler;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.epri.metric_calculator.util.LazyWorker;

/**
 * Dash-board View
 * 
 * @author JoWookJae
 *
 */
public class DashboardView extends ViewPart {
	/**
	 * View id
	 */
	public static final String ID = "com.epri.metric_calculator.views.dashboard.DashboardView";

	/**
	 * HTML browser
	 */
	private Browser browser;

	/**
	 * Lazy worker for refresh job.
	 */
	private LazyWorker refreshWorker = new LazyWorker();

	/**
	 * Refresh browser contents.
	 */
	private void refresh() {
		// if current project is null, return
		if (MetCalcProject.getCurrent() == null) {
			return;
		}

		FileWriter writer = null;
		try {
			File userChartDir = Const.getUserResource(Const.PATH_CHART);

			// Copy resource from bundle to installation path.
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					// If the file is directory, do copy.
					if (file.isDirectory()) {
						return true;
					}

					try {
						// Get relative path for file.
						String relativePath = Activator.getFile(Const.PATH_CHART).toURI().relativize(file.toURI())
								.getPath();

						// If the file exists, return false
						if (new File(userChartDir, relativePath).exists()) {
							return false;
						} else {
							return true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					return false;
				}
			};
			FileUtils.copyDirectory(Activator.getFile(Const.PRODUCT_NAME + File.separator + Const.PATH_CHART),
					userChartDir, filter);

			// Generate dash-board source.
			DashboardSourceGenerator gen = new DashboardSourceGenerator();
			String contents = gen.createIndexPageContents();

			// Write at index.html
			File indexFile = Const.getUserResource(Const.PATH_INDEX_HTML);
			writer = new FileWriter(indexFile);
			writer.write(contents);

			// Set browser url to index.html
			browser.setUrl(indexFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close writer.
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void refreshLazily() {
		refreshWorker.run(new Runnable() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
			}
		}, 500);
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(1, true));

		browser = new Browser(composite, SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				browser.refresh();
			}
		});

		registerEventHandlers();
		refresh();
	}

	private void registerEventHandlers() {
		MetCalcEventManager eventManager = MetCalcEventManager.getInstance();

		// Register event handler. when project open, refresh this view.
		eventManager.register(MetCalcEventType.PROJECT_OPEN, new AbMetCalcEventHandler(getClass()) {
			
			@Override
			public void handle(MetCalcEvent event) {
				refreshLazily();
			}
		});

		// Register event handler. when metric calcurated, refresh data.
		eventManager.register(MetCalcEventType.CALCURATION_FINISHED, new AbMetCalcEventHandler(getClass()) {
			
			@Override
			public void handle(MetCalcEvent event) {
				refreshLazily();
			}
		});

		eventManager.register(MetCalcEventType.PROJECT_SAVED, new AbMetCalcEventHandler(getClass()) {
			
			@Override
			public void handle(MetCalcEvent event) {
				refreshLazily();
			}
		});

		eventManager.register(MetCalcEventType.METRIC_UPDATED, new AbMetCalcEventHandler(getClass()) {
			
			@Override
			public void handle(MetCalcEvent event) {
				refreshLazily();
			}

		});
	}

	@Override
	public void setFocus() {
	}
}
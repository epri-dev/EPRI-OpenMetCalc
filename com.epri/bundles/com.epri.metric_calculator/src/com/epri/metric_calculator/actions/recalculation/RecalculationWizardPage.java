package com.epri.metric_calculator.actions.recalculation;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.actions.recalculation.request.RecalculationDataPointPost;
import com.epri.metric_calculator.actions.recalculation.request.RecalculationPost;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.epri.metric_calculator.event.MetricPartialUpdatedEvent;
import com.epri.metric_calculator.recalculation.RecalculationManager;
import com.epri.metric_calculator.util.ResultantAsyncTask;
import com.sst.sstat.ModelProvider;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricDefinitionInvalidException;
import com.sst.sstat.metric.MetricManager;

public class RecalculationWizardPage extends WizardPage {
	private Text textServerUrl;
	private Table table;
	private boolean canFinish;

	public RecalculationWizardPage() {
		super("RecalculationWizardPage"); //$NON-NLS-1$
		setTitle(Messages.RecalculationWizardPage_1);
		setDescription(Messages.RecalculationWizardPage_2);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblServerUrl = new Label(container, SWT.NONE);
		lblServerUrl.setText(Messages.RecalculationWizardPage_3);

		textServerUrl = new Text(container, SWT.BORDER);
		textServerUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String metricsHubURL = ToolSetting.getInstance().getMetricsHubURL();
		if ( metricsHubURL != null && !metricsHubURL.isEmpty() ) {
			textServerUrl.setText(metricsHubURL);
		}
		textServerUrl.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				pageChanged();
			}
		});

		Group grpDataModel = new Group(container, SWT.NONE);
		grpDataModel.setLayout(new GridLayout(2, false));
		GridData gd_grpDataModel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		grpDataModel.setLayoutData(gd_grpDataModel);
		grpDataModel.setText(Messages.RecalculationWizardPage_7);

		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(grpDataModel,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gdTable.heightHint = 230;
		table.setLayoutData(gdTable);

		Button btnSelectAll = new Button(grpDataModel, SWT.NONE);
		btnSelectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnSelectAll.setText(Messages.RecalculationWizardPage_8);
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(true);
			}
		});

		Button btnDeselectAll = new Button(grpDataModel, SWT.NONE);
		btnDeselectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDeselectAll.setText(Messages.RecalculationWizardPage_9);
		btnDeselectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(false);
			}
		});

		tableViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				Class<?> clazz = (Class<?>) element;
				return clazz.getSimpleName();
			};
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(ModelProvider.getModelClasses());
		tableViewer.setAllChecked(true);
	}

	/**
	 * Start uploading data to metrics Hub
	 * 
	 * @return
	 */
	public boolean uploadData() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				private String serverUrl;
				private List<Class<?>> selectedModelClasses;

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// Initialize progress monitor
					int total = 2;
					monitor.beginTask(getTitle(), total);

					// Initialze variables
					Display.getDefault().syncExec(() -> {
						serverUrl = textServerUrl.getText();
						selectedModelClasses = new ArrayList<>();
						for (TableItem item : table.getItems()) {
							if (item.getChecked()) {
								selectedModelClasses.add((Class<?>) item.getData());
							}
						}
					});

					// Start Uploading
					monitor.subTask(Messages.RecalculationWizardPage_12);

					// Create MetricsHubUploadTask
					RecalculationUploadTask uploadTask = new RecalculationUploadTask(serverUrl, selectedModelClasses);
					monitor.worked(15);
					
					monitor.subTask(Messages.RecalculationWizardPage_16);
					// Start MetricsHubUploadTask
					uploadTask.start();
					
					// Wait for MetricsHubUploadTask
					waitFor(monitor, uploadTask);

					// Confirm response and complete
					monitor.worked(30);
					monitor.subTask(Messages.RecalculationWizardPage_17);
					JSONObject recalculationResult = uploadTask.getResult();
					try {
						if ( !recalculationResult.getString(Const.METRICS_HUB_RESKEY_RESULT_CODE).equals(Const.METRICS_HUB_RESVALUE_OK) ) {
							openErrorDlg(RecalculationTask.NAME, recalculationResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
						}
					} catch (JSONException e) {
						openErrorDlg(RecalculationTask.NAME, Messages.RecalculationWizardPage_11);
						e.printStackTrace();
						return;
					}
					
					monitor.subTask(Messages.RecalculationWizardPage_19);
					try {
						MetricManager metricMgr = MetCalcProject.getCurrent().getMetricManager();
						Map<String, Metric> metrics = metricMgr.getMetricsMap();
						
						JSONArray resultData = (JSONArray)recalculationResult.get(Const.METRICS_HUB_RESKEY_RESULT_DATA);
						for ( int i = 0; i < resultData.length(); ++i ) {
							JSONObject obj = (JSONObject)resultData.get(i);
							Object id = obj.get(Const.CALCULATION_RESULT_TREEDISPLAYNAME);
							Metric metric = metrics.get(id);
							metric.setValue(obj.get(Const.CALCULATION_RESULT_VALUE).toString());
							MetCalcEventManager.getInstance().fire(new MetricPartialUpdatedEvent(metric.getId()));
							monitor.worked(20 + i);
						}
						
						// Fire METRIC_CALCURATED event.
						MetCalcEventManager.getInstance().fire(new MetCalcEvent(MetCalcEventType.CALCURATION_FINISHED));
						// Set dirty flag true.
						MetCalcProject.getCurrent().setDirty(true);
						openInfoDlg(Messages.RecalculationWizardPage_21);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (MetricDefinitionInvalidException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Finish progress
					monitor.done();
					canFinish = true;
				}

				private void waitFor(IProgressMonitor monitor, ResultantAsyncTask<?> task) throws InterruptedException {
					while (!(task.isStopped() || monitor.isCanceled())) {
						Thread.sleep(100);
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean canFinish() {
		return canFinish;
	}

	/**
	 * It will be called on page changed
	 */
	private void pageChanged() {
		String errorMsg = null;
		if (textServerUrl.getText().isEmpty()) {
			errorMsg = Messages.RecalculationWizardPage_14;
		} else if (!UrlValidator.getInstance().isValid(textServerUrl.getText())) {
			errorMsg = Messages.RecalculationWizardPage_15;
		}

		updateStatus(errorMsg);
	}

	/**
	 * Update the status of wizard
	 * 
	 * @param errorMsg
	 */
	private void updateStatus(String errorMsg) {
		setErrorMessage(errorMsg);
		setPageComplete(errorMsg == null);
	}

	/**
	 * Open error dialog
	 * 
	 * @param title
	 * @param message
	 */
	private void openErrorDlg(String taskName, String message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), getTitle(), message);
			}
		});
	}

	private void openInfoDlg(String message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), getTitle(), message);
			}
		});
	}
}

/**
 * Metrics Hub authentication task
 * 
 * @author JoWookJae
 *
 */
class RecalculationTask extends ResultantAsyncTask<JSONObject> {

	public static final String NAME = Messages.RecalculationWizardPage_18; // $NON-NLS-N$

	private String serverUrl;

	/**
	 * @param serverUrl
	 */
	public RecalculationTask(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@Override
	protected JSONObject runSync() {
		JSONObject result = null;
		try {
			// Create a request
			RecalculationPost post = new RecalculationPost(new URL(serverUrl));

			// Create a http client
			HttpClient client = HttpClientBuilder.create().build();

			// Try authentication
			HttpResponse response = client.execute(post);

			try {
				result = new JSONObject(EntityUtils.toString(response.getEntity()));
			} catch (JSONException e) {
				// If the response does not contains JSON, create empty JSON object
				result = new JSONObject();
			}

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				RecalculationManager recalculationManager = RecalculationManager.getInstance();
				recalculationManager.setServerUrl(serverUrl);
			} else {
				if (!result.has(Const.METRICS_HUB_RESKEY_RESULT_MSG)) {
					result.put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
					result.put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
							NLS.bind(Messages.RecalculationWizardPage_0, response.getStatusLine().getStatusCode(),
									EnglishReasonPhraseCatalog.INSTANCE
											.getReason(response.getStatusLine().getStatusCode(), Locale.getDefault())));
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			result = new JsonObjectForThrowable(e);
		}

		return result;
	}
}

/**
 * Metrics Hub upload task
 * 
 * @author JoWookJae
 *
 */
class RecalculationUploadTask extends ResultantAsyncTask<JSONObject> {

	public static final String NAME = Messages.RecalculationWizardPage_20; // $NON-NLS-N$

	private String serverUrl;
	private List<Class<?>> selectedModelClasses;

	public RecalculationUploadTask(String serverUrl, List<Class<?>> selectedModelClasses) {
		this.serverUrl = serverUrl;
		this.selectedModelClasses = selectedModelClasses;
	}

	@Override
	protected JSONObject runSync() {
		JSONObject result = null;

		try {
			RecalculationDataPointPost post = new RecalculationDataPointPost(new URL(serverUrl), selectedModelClasses);
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(post);
			
			try {
				result = new JSONObject(EntityUtils.toString(response.getEntity()));
			} catch (JSONException e) {
				// If the response does not contains JSON, create empty JSON object
				result = new JSONObject();
			}
			
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				if (!result.has(Const.METRICS_HUB_RESKEY_RESULT_MSG)) {
					result.put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
					result.put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
							NLS.bind(Messages.RecalculationWizardPage_0, response.getStatusLine().getStatusCode(),
									EnglishReasonPhraseCatalog.INSTANCE
											.getReason(response.getStatusLine().getStatusCode(), Locale.getDefault())));
				}
			}
		} catch (Throwable e) {
			result = new JsonObjectForThrowable(e);
		}

		return result;
	}
}

/**
 * This class is json object that be converted from throwable
 * 
 * @author JoWookJae
 *
 */
class JsonObjectForThrowable extends JSONObject {
	public JsonObjectForThrowable(Throwable e) {
		put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
		put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
				e.getMessage() != null ? e.getMessage() : Messages.RecalculationWizardPage_11);
	}
}

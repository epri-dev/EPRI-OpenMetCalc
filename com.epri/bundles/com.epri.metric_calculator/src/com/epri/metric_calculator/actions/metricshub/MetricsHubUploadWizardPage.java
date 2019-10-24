package com.epri.metric_calculator.actions.metricshub;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.metricshub.MetricsHubAuthenticationManager;
import com.epri.metric_calculator.metricshub.request.MetricsHubAuthPost;
import com.epri.metric_calculator.metricshub.request.MetricsHubDataPointPost;
import com.epri.metric_calculator.util.ResultantAsyncTask;
import com.sst.sstat.ModelProvider;

/**
 * This class is a wizard page for uploading data to Metrics Hub
 * 
 * @author JoWookJae
 *
 */
public class MetricsHubUploadWizardPage extends WizardPage {

	private Text textServerUrl;
	private Text textUserName;
	private Text textPassword;
	private Table table;
	private boolean canFinish;

	public MetricsHubUploadWizardPage() {
		super("MetricsHubUploadWizardPage"); //$NON-NLS-1$
		setTitle(Messages.MetricsHubUploadWizardPage_1);
		setDescription(Messages.MetricsHubUploadWizardPage_2);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblServerUrl = new Label(container, SWT.NONE);
		lblServerUrl.setText(Messages.MetricsHubUploadWizardPage_3);

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

		Group grpAuthentication = new Group(container, SWT.NONE);
		grpAuthentication.setLayout(new GridLayout(2, false));
		grpAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpAuthentication.setText(Messages.MetricsHubUploadWizardPage_4);

		Label lblUserName = new Label(grpAuthentication, SWT.NONE);
		lblUserName.setText(Messages.MetricsHubUploadWizardPage_5);

		textUserName = new Text(grpAuthentication, SWT.BORDER);
		textUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUserName.setText(MetricsHubAuthenticationManager.getInstance().getUserName());
		textUserName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				pageChanged();
			}
		});

		Label lblPassword = new Label(grpAuthentication, SWT.NONE);
		lblPassword.setText(Messages.MetricsHubUploadWizardPage_6);

		textPassword = new Text(grpAuthentication, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setText(MetricsHubAuthenticationManager.getInstance().getPassword());
		textPassword.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				pageChanged();
			}
		});

		Group grpDataModel = new Group(container, SWT.NONE);
		grpDataModel.setLayout(new GridLayout(2, false));
		GridData gd_grpDataModel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		// gd_grpDataModel.heightHint = 260;
		grpDataModel.setLayoutData(gd_grpDataModel);
		grpDataModel.setText(Messages.MetricsHubUploadWizardPage_7);

		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(grpDataModel,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gdTable.heightHint = 150;
		table.setLayoutData(gdTable);

		Button btnSelectAll = new Button(grpDataModel, SWT.NONE);
		btnSelectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnSelectAll.setText(Messages.MetricsHubUploadWizardPage_8);
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(true);
			}
		});

		Button btnDeselectAll = new Button(grpDataModel, SWT.NONE);
		btnDeselectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDeselectAll.setText(Messages.MetricsHubUploadWizardPage_9);
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
				private String userName;
				private String password;
				private List<Class<?>> selectedModelClasses;

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// Initialize progress monitor
					int total = 2;
					monitor.beginTask(getTitle(), total);

					// Initialze variables
					Display.getDefault().syncExec(() -> {
						serverUrl = textServerUrl.getText();
						userName = textUserName.getText();
						password = textPassword.getText();

						selectedModelClasses = new ArrayList<>();
						for (TableItem item : table.getItems()) {
							if (item.getChecked()) {
								selectedModelClasses.add((Class<?>) item.getData());
							}
						}
					});

					// Start Authentication
					monitor.subTask(Messages.MetricsHubUploadWizardPage_10);

					// Create MetricsHubAuthTask
					MetricsHubAuthTask authTask = new MetricsHubAuthTask(serverUrl, userName, password);

					// Start MetricsHubAuthTask
					authTask.start();

					// Wait for MetricsHubAuthTask
					waitFor(monitor, authTask);

					// Increase progress
					monitor.worked(1);

					// Confirm response
					JSONObject authResult = authTask.getResult();
					try {
						if (!authResult.has(Const.METRICS_HUB_RESKEY_ACCESS_TOKEN)) {
							openErrorDlg(MetricsHubAuthTask.NAME, authResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
							return;
						}
					} catch (JSONException e) {
						openErrorDlg(MetricsHubAuthTask.NAME, Messages.MetricsHubUploadWizardPage_11);
						return;
					}

					// This is access token
					String accessToken = authResult.getString(Const.METRICS_HUB_RESKEY_ACCESS_TOKEN);

					// Start Uploading
					monitor.subTask(Messages.MetricsHubUploadWizardPage_12);

					// Create MetricsHubUploadTask
					MetricsHubUploadTask uploadTask = new MetricsHubUploadTask(serverUrl, accessToken, selectedModelClasses);

					// Start MetricsHubUploadTask
					uploadTask.start();

					// Wait for MetricsHubUploadTask
					waitFor(monitor, uploadTask);

					// Finish progress
					monitor.done();

					// Confirm response and complete
					JSONObject uploadResult = uploadTask.getResult();
					try {
						if (uploadResult.getString(Const.METRICS_HUB_RESKEY_RESULT_CODE).equals(Const.METRICS_HUB_RESVALUE_OK)) {
//							openInfoDlg(uploadResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
							openInfoDlg("Uploading Data Points has been completed.");
							canFinish = true;
						} else {
							openErrorDlg(MetricsHubUploadTask.NAME, uploadResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
						}
					} catch (JSONException e) {
						openErrorDlg(MetricsHubAuthTask.NAME, Messages.MetricsHubUploadWizardPage_11);
						e.printStackTrace();
						return;
					}
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
			errorMsg = Messages.MetricsHubUploadWizardPage_14;
		} else if (!UrlValidator.getInstance().isValid(textServerUrl.getText())) {
			errorMsg = Messages.MetricsHubUploadWizardPage_15;
		} else if (textUserName.getText().isEmpty()) {
			errorMsg = Messages.MetricsHubUploadWizardPage_16;
		} else if (textPassword.getText().isEmpty()) {
			errorMsg = Messages.MetricsHubUploadWizardPage_17;
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
class MetricsHubAuthTask extends ResultantAsyncTask<JSONObject> {

	public static final String NAME = Messages.MetricsHubUploadWizardPage_18; // $NON-NLS-N$

	private String serverUrl;
	private String userName;
	private String password;

	/**
	 * @param serverUrl
	 * @param userName
	 * @param password
	 */
	public MetricsHubAuthTask(String serverUrl, String userName, String password) {
		this.serverUrl = serverUrl;
		this.userName = userName;
		this.password = password;
	}

	@Override
	protected JSONObject runSync() {
		JSONObject result = null;
		try {
			// Create a request
			MetricsHubAuthPost post = new MetricsHubAuthPost(new URL(serverUrl), userName, password);

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
				MetricsHubAuthenticationManager authManager = MetricsHubAuthenticationManager.getInstance();
				authManager.setServerUrl(serverUrl);
				authManager.setUserName(userName);
				authManager.setPassword(password);
			} else {
				if (!result.has(Const.METRICS_HUB_RESKEY_RESULT_MSG)) {
					result.put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
					result.put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
							NLS.bind(Messages.MetricsHubUploadWizardPage_0, response.getStatusLine().getStatusCode(),
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
class MetricsHubUploadTask extends ResultantAsyncTask<JSONObject> {

	public static final String NAME = Messages.MetricsHubUploadWizardPage_20; // $NON-NLS-N$

	private String serverUrl;
	private String accessToken;
	private List<Class<?>> selectedModelClasses;

	public MetricsHubUploadTask(String serverUrl, String accessToken, List<Class<?>> selectedModelClasses) {
		this.serverUrl = serverUrl;
		this.accessToken = accessToken;
		this.selectedModelClasses = selectedModelClasses;
	}

	@Override
	protected JSONObject runSync() {
		JSONObject result = null;

		try {
			// Create a request
			MetricsHubDataPointPost post = new MetricsHubDataPointPost(new URL(serverUrl), accessToken, selectedModelClasses);

			// Create a http client
			HttpClient client = HttpClientBuilder.create().build();

			// Request post
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
							NLS.bind(Messages.MetricsHubUploadWizardPage_0, response.getStatusLine().getStatusCode(),
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
 * This class is json object that be converted from throwable
 * 
 * @author JoWookJae
 *
 */
class JsonObjectForThrowable extends JSONObject {
	public JsonObjectForThrowable(Throwable e) {
		put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
		put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
				e.getMessage() != null ? e.getMessage() : Messages.MetricsHubUploadWizardPage_11);
	}
}
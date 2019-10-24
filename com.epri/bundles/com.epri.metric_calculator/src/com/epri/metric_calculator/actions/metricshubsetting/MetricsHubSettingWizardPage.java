package com.epri.metric_calculator.actions.metricshubsetting;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;
import org.json.JSONObject;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.checkurl.request.CheckURLGet;
import com.epri.metric_calculator.util.ResultantAsyncTask;

/**
 * This class is a wizard page for uploading data to Metrics Hub
 * 
 * @author JoWookJae
 *
 */
public class MetricsHubSettingWizardPage extends WizardPage {

	private Text textServerUrl;
	private boolean canFinish;
	private boolean isURLCorrect;

	public MetricsHubSettingWizardPage() {
		super("MetricsHubURLWizardPage"); //$NON-NLS-1$
		setTitle(Messages.MetricsHubSettingWizardPage_1);
		setDescription(Messages.MetricsHubSettingWizardPage_2);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setSize(100, 50);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblServerUrl = new Label(container, SWT.NONE);
		lblServerUrl.setText(Messages.MetricsHubSettingWizardPage_3);

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
	}

	/**
	 * @return
	 */
	public void saveMetricsHubURL() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				private String serverUrl;

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// Initialize progress monitor
					int total = 2;
					monitor.beginTask(getTitle(), total);

					// Initialze variables
					Display.getDefault().syncExec(() -> {
						serverUrl = textServerUrl.getText();
					});
					
					ToolSetting.getInstance().setMetricsHubURL(serverUrl);
					try {
						ToolSetting.getInstance().saveMetricsHubSetting();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void checkURL() {		
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				private String serverUrl;

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// Initialize progress monitor
					int total = 2;
					monitor.beginTask(getTitle(), total);

					// Initialze variables
					Display.getDefault().syncExec(() -> {
						serverUrl = textServerUrl.getText();
					});
					
					// Create & Start MetricModelVersionRequestTask
					CheckURLRequestTask checkURLRequestTask = new CheckURLRequestTask(serverUrl);
					checkURLRequestTask.start();
					while ( !checkURLRequestTask.isStopped() ); // FIXME THREAD 대기 부분 수정 필요
					
					JSONObject checkURLRequestResult = checkURLRequestTask.getResult();
					try {
						if (checkURLRequestResult.getString(Const.METRICS_HUB_RESKEY_RESULT_CODE).equals(Const.METRICS_HUB_RESVALUE_OK)) {
							isURLCorrect = true;
						} else {
							isURLCorrect = false;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						isURLCorrect = false;
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			isURLCorrect = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			isURLCorrect = false;
		}
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
			errorMsg = Messages.MetricsHubSettingWizardPage_4;
		} else if (!UrlValidator.getInstance().isValid(textServerUrl.getText())) {
			errorMsg = Messages.MetricsHubSettingWizardPage_5;
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

	public boolean isURLCorrect() {
		return isURLCorrect;
	}
}

class CheckURLRequestTask extends ResultantAsyncTask<JSONObject> {
	private String serverUrl;
	
	public CheckURLRequestTask(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	@Override
	protected JSONObject runSync() {
		JSONObject result = null;

		try {
			// Create a request & http client
			CheckURLGet request = new CheckURLGet(new URL(serverUrl));
			HttpClient client = HttpClientBuilder.create().build();

			// Request post
			HttpResponse response = client.execute(request);

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
			e.printStackTrace();
			result = new JsonObjectForThrowable(e);
		}

		return result;
	}
}

class JsonObjectForThrowable extends JSONObject {
	public JsonObjectForThrowable(Throwable e) {
		put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
		put("Connection Error", e.getMessage() != null ? e.getMessage() : "[ERROR] Check URL");
	}
}
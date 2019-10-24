package com.epri.metric_calculator.metricmodel.synchronization;

import java.net.URL;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.json.JSONException;
import org.json.JSONObject;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.metricmodel.request.MetricModelGet;
import com.epri.metric_calculator.metricmodel.request.MetricModelVersionGet;
import com.epri.metric_calculator.util.ResultantAsyncTask;
import com.epri.metric_calculator.util.XMLBuilder;

public class MetricModelSynchronizer {
	
	public String getMerticVersion() {
		
		String metricVersion = new String();
		try {
		// Create & Start MetricModelVersionRequestTask
		String metricsHubURL = ToolSetting.getInstance().getMetricsHubURL();
		MetricModelVersionRequestTask metricModelVersionTask = new MetricModelVersionRequestTask(metricsHubURL);
		metricModelVersionTask.start();
		
		while ( !metricModelVersionTask.isStopped() ); // FIXME THREAD 대기 부분 수정 필요
		
		// Confirm response and complete
		JSONObject metricVersionRequestResult = metricModelVersionTask.getResult();
		
			try {
				if (metricVersionRequestResult.getString(Const.METRICS_HUB_RESKEY_RESULT_CODE).equals(Const.METRICS_HUB_RESVALUE_OK)) {

					// FIXME 후처리
					JSONObject resultData = metricVersionRequestResult.getJSONObject(Const.METRICS_HUB_RESKEY_RESULT_DATA);
					metricVersion = resultData.getString(Const.METRICS_HUB_RESKEY_VERSIONID);
				} else {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed get metric model version from Metrics Hub", metricVersionRequestResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return metricVersion;
	}
	
	public void getMetricModel() {
		
		// Create & Start MetricModelRequestTask
		String metricsHubURL = ToolSetting.getInstance().getMetricsHubURL();
		MetricModelRequestTask metricModelTask = new MetricModelRequestTask(metricsHubURL);
		String metricVersionRequestResult = metricModelTask.getMetricModel();

		try {
			if (metricVersionRequestResult != null) {
				XMLBuilder.buildXML(Const.METRIC_CONFIG_FILE, metricVersionRequestResult);
			} else {// FIXME
//				MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed get metric model version from Metrics Hub", metricVersionRequestResult.getString(Const.METRICS_HUB_RESKEY_RESULT_MSG));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
	}
}

/**
 * Metric model version Request Task
 * 
 * @author Ben Hong
 *
 */

class MetricModelVersionRequestTask extends ResultantAsyncTask<JSONObject> {
	
	private String serverUrl;
	
	public MetricModelVersionRequestTask(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	@Override
	protected JSONObject runSync() {
		JSONObject result = null;

		try {
			// Create a request & http client
			MetricModelVersionGet request = new MetricModelVersionGet(new URL(serverUrl));
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

class MetricModelRequestTask {

	private String serverUrl;
	
	public MetricModelRequestTask(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public String getMetricModel() {
		String result = "";
		try {
			// Create a request & http client
			MetricModelGet request = new MetricModelGet(new URL(serverUrl));
			HttpClient client = HttpClientBuilder.create().build();

			// Request get
			HttpResponse response = client.execute(request);
			result = EntityUtils.toString(response.getEntity());
			
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				
//				if (!result.has(Const.METRICS_HUB_RESKEY_RESULT_MSG)) {
//					result.put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
//					result.put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
//							NLS.bind(Messages.RecalculationWizardPage_0, response.getStatusLine().getStatusCode(),
//									EnglishReasonPhraseCatalog.INSTANCE
//											.getReason(response.getStatusLine().getStatusCode(), Locale.getDefault())));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}

		return result;
	}
}

/**
 * This class is json object that be converted from throwable
 * 
 * @author Ben Hong
 *
 */
class JsonObjectForThrowable extends JSONObject {
	public JsonObjectForThrowable(Throwable e) {
		put(Const.METRICS_HUB_RESKEY_RESULT_CODE, Const.METRICS_HUB_RESVALUE_NOK);
		put(Const.METRICS_HUB_RESKEY_RESULT_MSG,
				e.getMessage() != null ? e.getMessage() : "[ERROR] Get VERSION CHECK");
	}
}
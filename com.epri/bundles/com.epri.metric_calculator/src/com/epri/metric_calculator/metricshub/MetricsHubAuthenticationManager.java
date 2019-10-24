package com.epri.metric_calculator.metricshub;

import org.eclipse.jface.preference.IPreferenceStore;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;

/**
 * Manager for Metrics Hub authentication
 * 
 * @author JoWookJae
 *
 */
public class MetricsHubAuthenticationManager {

	/**
	 * Single instance
	 */
	private static MetricsHubAuthenticationManager INSTANCE;

	/**
	 * Return single instance
	 * 
	 * @return
	 */
	public static MetricsHubAuthenticationManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MetricsHubAuthenticationManager();
		}

		return INSTANCE;
	}

	/**
	 * Constructor
	 */
	private MetricsHubAuthenticationManager() {
		// Do nothing...
	}

	public void setServerUrl(String url) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		prefStore.putValue(Const.PREFERENCE_METRICS_HUB_URL, url);
	}

	public void setUserName(String userName) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		prefStore.putValue(Const.PREFERENCE_METRICS_HUB_AUTH_USERNAME, userName);
	}

	public void setPassword(String pw) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		prefStore.putValue(Const.PREFERENCE_METRICS_HUB_AUTH_PW, pw);
	}

	public String getServerUrl() {
		return Activator.getDefault().getPreferenceStore().getString(Const.PREFERENCE_METRICS_HUB_URL);
	}

	public String getUserName() {
		return Activator.getDefault().getPreferenceStore().getString(Const.PREFERENCE_METRICS_HUB_AUTH_USERNAME);
	}

	public String getPassword() {
		return Activator.getDefault().getPreferenceStore().getString(Const.PREFERENCE_METRICS_HUB_AUTH_PW);
	}
}

package com.epri.metric_calculator.recalculation;

import org.eclipse.jface.preference.IPreferenceStore;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;

public class RecalculationManager {
	/**
	 * Single instance
	 */
	private static RecalculationManager INSTANCE;

	/**
	 * Return single instance
	 * 
	 * @return
	 */
	public static RecalculationManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RecalculationManager();
		}

		return INSTANCE;
	}

	/**
	 * Constructor
	 */
	private RecalculationManager() {
		// Do nothing...
	}

	public void setServerUrl(String url) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		prefStore.putValue(Const.PREFERENCE_METRICS_HUB_URL, url);
	}

	public String getServerUrl() {
		return Activator.getDefault().getPreferenceStore().getString(Const.PREFERENCE_METRICS_HUB_URL);
	}
}

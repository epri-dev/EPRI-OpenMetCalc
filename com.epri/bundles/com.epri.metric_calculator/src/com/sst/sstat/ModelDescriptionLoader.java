package com.sst.sstat;

import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.epri.metric_calculator.Activator;

public class ModelDescriptionLoader {
	private static final String PROPERTIES_PATH = "/res/messages.properties";

	private static ModelDescriptionLoader INSTANCE;

	private Properties properties;

	public static ModelDescriptionLoader getInstance() throws ModelDescriptionLoadFailException {
		if (INSTANCE == null) {
			INSTANCE = new ModelDescriptionLoader();
		}

		return INSTANCE;
	}

	public String get(String name) {
		return properties.getProperty(name);
	}

	private ModelDescriptionLoader() throws ModelDescriptionLoadFailException {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL url = bundle.getResource(PROPERTIES_PATH);
		properties = new Properties();

		if (url == null) {
			throw new ModelDescriptionLoadFailException(
					"Model description file is not found. MetCalc seems to be damaged.");
		}

		try {
			properties.load(url.openConnection().getInputStream());
		} catch (Exception e) {
			if (e.getMessage() != null)
				throw new ModelDescriptionLoadFailException(e.getMessage());

			throw new ModelDescriptionLoadFailException();
		}

		if (properties == null) {
			throw new ModelDescriptionLoadFailException();
		}
	}
}

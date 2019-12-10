package com.epri.metric_calculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.epri.metric_calculator.actions.snapshot.SnapshotPeriod;

/**
 * The class that manages the configuration of the tool
 * 
 * @author JoWookJae
 *
 */

public class ToolSetting {

	public static final String PROP_VERSION 					= "VERSION"; //$NON-NLS-1$
	public static final String PROP_USER_TYPE 					= "USER_TYPE"; //$NON-NLS-1$
	public static final String PROP_DECRYPTION_KEY_PATH 		= "DECRYPTION_KEY_PATH"; //$NON-NLS-1$
	public static final String PROP_PERIOD_OF_COLLECTING_DATA 	= "PERIOD_OF_COLLECTING_DATA"; //$NON-NLS-1$
	public static final String PROP_HISTORIC_CHART_DATE_FORMAT	= "HISTORIC_CHART_DATE_FORMAT"; //$NON-NLS-1$
	public static final String PROP_ENCRYPTION_KEY_SIZE 		= "ENCRYPTION_KEY_SIZE"; //$NON-NLS-1$
	public static final String PROP_METRICMODEL_VERSION 		= "METRICMODEL_VERSION"; //$NON-NLS-1$
	public static final String PROP_METRICS_HUB_URL 			= "METRICS_HUB_URL"; //$NON-NLS-1$
	public static final String PROP_SHOW_DESCRIPTION_IN_METRIC_VIEW = "SHOW_DESCRIPTION_IN_METRIC_VIEW";
	
	// User type : Normal user
	public static final int USER_TYPE_NORMAL_USER = 0;
	// User type : Analyst
	public static final int USER_TYPE_ANALYST = 1;

	// Single instance
	private static ToolSetting INSTANCE;

	private String version;
	private SnapshotPeriod collectionPeriod;
	private String historicChartDateFormat = "yy.MM.dd kk:mm.ss"; //$NON-NLS-1$
	private int encryptionKeySize = 1024;
	private String metricModelVersion;
	private String metricsHubURL;

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 * @throws VersionNotFoundException
	 */
	private ToolSetting() throws VersionNotFoundException, IOException {
		load();
		
		FileSynchronizer.getInstance().put(getUserSettingFile(), new Runnable() {

			@Override
			public void run() {
				// setting file modified, reload the settings.
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						boolean restart = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
								Messages.ToolSetting_0, Messages.ToolSetting_1);
						if (restart) {
							PlatformUI.getWorkbench().restart();
						}
					}
				});
			}
		});
	}

	public static ToolSetting newInstance() throws VersionNotFoundException, IOException {
		INSTANCE = new ToolSetting();
		return INSTANCE;
	}

	/**
	 * Return single instance
	 * 
	 * @return
	 */
	public static ToolSetting getInstance() {
		return INSTANCE;
	}

	public String getHistoricChartDateFormat() {
		return historicChartDateFormat;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public SnapshotPeriod getCollectionPeriod() {
		return collectionPeriod;
	}

	public int getEncryptionKeySize() {
		return encryptionKeySize;
	}
	
	public String getMetricModelVersion() {
		return metricModelVersion;
	}
		
	public String getMetricsHubURL() {
		return metricsHubURL;
	}
	
	public void setMetricModelVersion(String metricModelVersion) {
		this.metricModelVersion = metricModelVersion;
	}
		
	public void setMetricsHubURL(String metricsHubURL) {
		this.metricsHubURL = metricsHubURL;
	}
	
	/**
	 * Return a value of the key string.
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String get(String key) throws IOException {
		File file = getUserSettingFile();
		// Load properties
		Properties properties = new Properties();
		properties.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))); //$NON-NLS-1$
		return properties.getProperty(key);
	}

	public File getUserSettingFile() {
		return Const.getUserResource(Const.PATH_SETTING_FILE);
	}
	
	public File getMetricsHubSettingFile() {
		return Const.getUserResource(Const.PATH_METRICS_HUB_SETTING_FILE);
	}
	
	/**
	 * Return integer value of key. Default value is 0.
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		try {
			String value = get(key);
			return Integer.parseInt(value);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Return boolean value of key. Default value is false.
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		try {
			String value = get(key);
			return Boolean.parseBoolean(value);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Load tool setting
	 * 
	 * @throws VersionNotFoundException
	 * @throws IOException
	 */
	private void load() throws VersionNotFoundException, IOException {
		loadMetCalcSettings();
		loadMetricsHubSettings();
	}
	
	private void loadMetCalcSettings() throws FileNotFoundException, IOException, VersionNotFoundException {
		File file = Const.getUserResource(Const.PATH_SETTING_FILE);

		// If tool setting file does not exist in installation path, copy from internal
		if (!file.exists()) {
			if (!new File(Const.USER_DATA_DIR).exists()) {
				new File(Const.USER_DATA_DIR).mkdirs();
			}

			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = Activator.openResourceStream(Const.PRODUCT_NAME + File.separator + Const.PATH_SETTING_FILE);
				fos = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Load properties
		Properties properties = new Properties();
		properties.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))); //$NON-NLS-1$

		// Load version
		version = load(properties, PROP_VERSION);
		if (version == null) {
			throw new VersionNotFoundException();
		}

		// Load period of collecting data
		String period = load(properties, PROP_PERIOD_OF_COLLECTING_DATA);
		if (period == null) {
			collectionPeriod = new SnapshotPeriod("1s"); //$NON-NLS-1$
		} else if (period.equals(Const.HISTORY_COLLECT_NONPERIOD)) {
			collectionPeriod = null;
		} else {
			collectionPeriod = new SnapshotPeriod(period);
		}

		String format = load(properties, PROP_HISTORIC_CHART_DATE_FORMAT);
		if (format != null) {
			historicChartDateFormat = format;
		}

		String strEncryptionKeySize = load(properties, PROP_ENCRYPTION_KEY_SIZE);
		if (strEncryptionKeySize != null) {
			encryptionKeySize = Integer.parseInt(strEncryptionKeySize);
		}
	}
	
	private void loadMetricsHubSettings() throws FileNotFoundException, IOException {
		File file = Const.getUserResource(Const.PATH_METRICS_HUB_SETTING_FILE);

		// If tool setting file does not exist in installation path, copy from internal
		if (!file.exists()) {
			if (!new File(Const.USER_DATA_DIR).exists()) {
				new File(Const.USER_DATA_DIR).mkdirs();
			}

			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = Activator.openResourceStream(Const.PRODUCT_NAME + File.separator + Const.PATH_METRICS_HUB_SETTING_FILE);
				fos = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Load properties
		Properties properties = new Properties();
		properties.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))); //$NON-NLS-1$

		// Load version
		metricModelVersion = load(properties, PROP_METRICMODEL_VERSION);
		if (metricModelVersion == null) {
			metricModelVersion = "";
		}

		// Load Metrics Hub URL
		metricsHubURL = load(properties, PROP_METRICS_HUB_URL);
	}

	private String load(Properties properties, String key) {
		String result = properties.getProperty(key);

		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return result.trim();
		}
	}
	
	public void saveMetricsHubSetting() throws IOException {
		File file = getMetricsHubSettingFile();

		FileOutputStream out = new FileOutputStream(file);
		FileInputStream in = new FileInputStream(file);
		try {
		   Properties properties = new Properties();
	        properties.load(in);
	        properties.setProperty(ToolSetting.PROP_METRICS_HUB_URL, metricsHubURL);
	        properties.setProperty(ToolSetting.PROP_METRICMODEL_VERSION, metricModelVersion);
	        properties.store(out, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
		}
	}
}

package com.epri.metric_calculator;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.swt.graphics.Color;

public class Const {
	public static final String PRODUCT_NAME = "EPRI OpenMetCalc";

	// Preference store key
	public static final String PREFERENCE_STORE_PRESELECTED_PROJECT_DIRECTORY = "PRESELECTED_PROJECT_DIRECTORY";
	public static final String PREFERENCE_STORE_LAST_USED_PROJECT = "LAST_USED_PROJECT";
	public static final String PREFERENCE_AUTO_SNAPSHOT = "PREFERENCE_AUTO_COLLECTING";
	public static final String PREFERENCE_METRICS_HUB_URL = "PREFERENCE_METRICS_HUB_AUTH_URL";
	public static final String PREFERENCE_METRICS_HUB_AUTH_USERNAME = "PREFERENCE_METRICS_HUB_AUTH_USERNAME";
	public static final String PREFERENCE_METRICS_HUB_AUTH_PW = "PREFERENCE_METRICS_HUB_AUTH_PW";
	public static final String PREFERENCE_METRICS_HUB_AUTH_ACCESSTOKEN = "PREFERENCE_METRICS_HUB_AUTH_ACCESSTOKEN";
	public static final String PREFERENCE_METRICS_HUB_LAST_UPLOAD_SET = "PREFERENCE_METRICS_HUB_LAST_UPLOAD_SET";
	
	// path
	public static final String USER_DATA_DIR = System.getenv("APPDATA") + File.separator + PRODUCT_NAME;
	public static final String PATH_SETTING_FILE 				= "MetCalc_setting.properties";
	public static final String PATH_METRICS_HUB_SETTING_FILE	= "MetricsHub_setting.properties";
	public static final String PATH_CHART = "chart";
	public static final String PATH_INDEX_HTML = PATH_CHART + File.separator + "index.html";
	public static final String METRIC_SETTING_ATTR_WEIGHT = "weight";
	public static final String METRIC_CONFIG_FILENAME = "MetCalc_metric.xml";
	public static final String METRIC_CONFIG_FILE = USER_DATA_DIR + File.separator + METRIC_CONFIG_FILENAME;
	public static final String HISTORIC_DATA_STORE_DIRNAME = "history";

	public static final String DATA_POINT_LATEST_DIR = "data_point";
	public static final String DATA_POINT_LATEST_EXTENSION = "csv";
	public static final String SECURED_DATA_POINT_DIR_SUFFIX = "_CSV";
	public static final String SECURED_DATA_POINT_EXTENSION = "security.csv";
	
	public static final String HISTORIC_DATA_EXTENSION = "snap";
	public static final String SCRIPT_DIR = "script";
	public static final String GROOVY_SCRIPT_TEMPLATE = SCRIPT_DIR + File.separator + "ScriptTemplate.groovy";
	public static final String GROOVY_PREPROCESSOR = SCRIPT_DIR + File.separator + "Preprocessor.groovy";
	public static final String GROOVY_FORMULA_SCRIPT = SCRIPT_DIR + File.separator + "Formula.groovy";
	public static final String METCALC_PROJECT_EXTENSION = "project";
	public static final String METCALC_SECURED_PROJECT_EXTENSION = "security";

	public static final String PREFIX_INFO = "[INFO] ";
	public static final String PREFIX_WARNING = "[WARNING] ";
	public static final String PREFIX_ERROR = "[ERROR] ";

	public static final String COMMA_STR = ",";

	public static final String HISTORY_COLLECT_NONPERIOD = "MANUAL";
	public static final DateFormat HISTORY_FILE_NAME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat HISTORY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final String PRIVATE_FILENAME = "MetCalc_private.key";
	public static final String PUBLIC_FILENAME = "MetCalc_public.key";

	public static final String COLOR_HEX_BRIGHT_GREEN = "#ecffec";
	public static final String COLOR_HEX_BRIGHT_RED = "#ffe6dc";
	
	public static final String PROJECT_ENCRYTION_ALGORITHM = "RSA";
	public static final String EXCEL_FILE_EXTENSION = ".xlsx";

	/**
	 * Color of table header
	 */
	public static final Color COLOR_TABLE_HEADER_BACKGROUND = new Color(null, 120, 120, 120);
	public static final Color COLOR_TABLE_HEADER_FOREGROUND = new Color(null, 255, 255, 255);

	/**
	 * Color of success/fail/none
	 */
	public static final Color COLOR_FAIL = new Color(null, 255, 230, 220);
	public static final Color COLOR_NONE = new Color(null, 255, 255, 255);
	public static final Color COLOR_SUCCESS = new Color(null, 236, 255, 236);

	// METRICS_HUB Response
	public static final String METRICS_HUB_RESKEY_RESULT_CODE	= "result_code";
	public static final String METRICS_HUB_RESKEY_RESULT_MSG	= "result_msg";
	public static final String METRICS_HUB_RESKEY_RESULT_DATA	= "result_data";
	public static final String METRICS_HUB_RESKEY_ACCESS_TOKEN	= "access_token";
	public static final String METRICS_HUB_RESKEY_VERSIONID		= "versionId";
	
	public static final String METRICS_HUB_RESVALUE_OK = "OK";
	public static final String METRICS_HUB_RESVALUE_NOK = "NOK";
	
	// METRIC CALCULTATION RESULT
	public static final String CALCULATION_RESULT_TREEDISPLAYNAME	= "treeDisplayName";
	public static final String CALCULATION_RESULT_VALUE				= "value";
	
	/**
	 * Return resource from user directory
	 * 
	 * @param path
	 * @return
	 */
	public static final File getUserResource(String... path) {
		File result = new File(USER_DATA_DIR);
		for (String pathname : path) {
			result = new File(result, pathname);
		}

		return result;
	}

	public static final String getUserResourcePath(String path) {
		return USER_DATA_DIR + File.separator + path;
	}
}

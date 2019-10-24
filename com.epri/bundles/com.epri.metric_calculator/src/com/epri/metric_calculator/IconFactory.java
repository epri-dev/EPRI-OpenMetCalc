package com.epri.metric_calculator;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author JoWookJae
 *
 */
public class IconFactory {

	// Splash
	public static final Image SPLASH_EPRI = createImageFromFileInBundle("icons/splash_epri.jpg");

	public static final Image DATA_POINT_FOLDER = createImageFromFileInBundle("icons/folder_20.png");
	public static final Image DATA_POINT_FOLDER_OPEN = createImageFromFileInBundle("icons/folder_selected_20.png");
	public static final Image DATA_POINT_IMPORT = createImageFromFileInBundle("icons/import_csv_16.png");
	public static final Image DATA_POINT_CLEAN_DATA = createImageFromFileInBundle("icons/Trash_16px.png");

	public static final Image METRIC_RECALCULATE = createImageFromFileInBundle("icons/calculator_16.png");

	// Metric tree
	public static final Image METRIC_TREE_OVERVIEW = createImageFromFileInBundle(
			"icons/metric_tree/metric_overview.png");
	public static final Image METRIC_TREE_STRATEGIC = createImageFromFileInBundle(
			"icons/metric_tree/metric_strategic.png");
	public static final Image METRIC_TREE_TACTICAL = createImageFromFileInBundle(
			"icons/metric_tree/metric_tactical.png");
	public static final Image METRIC_TREE_OPERATIONAL = createImageFromFileInBundle(
			"icons/metric_tree/metric_operational.png");

	public static final Image METRIC_TREE_NODE = createImageFromFileInBundle("icons/Node_16px.png");
	public static final Image METRIC_COLLECT_DATA = createImageFromFileInBundle("icons/SaveHistory_16px.png");
	
	// 공통
	public static final Image COMMON_CREATE = createImageFromFileInBundle("icons/create_16.png");
	public static final Image COMMON_OPEN = createImageFromFileInBundle("icons/open_16.png");
	public static final Image COMMON_SAVE = createImageFromFileInBundle("icons/save_16.png");
	public static final Image COMMON_SAVE_AS = createImageFromFileInBundle("icons/save_as_16.png");
	public static final Image COMMON_EXIT = createImageFromFileInBundle("icons/exit_16.png");

	public static final Image COMMON_IMPORT_DATA = createImageFromFileInBundle("icons/unlock_16.png");
	public static final Image COMMON_GENERATE_KEY = createImageFromFileInBundle("icons/Key_16px.png");
	public static final Image COMMON_IMPORT_METRIC = createImageFromFileInBundle("icons/import_metric_16.png");
	public static final Image COMMON_EXPORT_METRIC = createImageFromFileInBundle("icons/export_metric_16.png");
	public static final Image COMMON_RESET_METRIC = createImageFromFileInBundle("icons/delete_metric_16.png");
	public static final Image COMMON_EXPORT_DATA = createImageFromFileInBundle("icons/lock_16.png");
	public static final Image COMMON_EXPORT_DATA_TO_TEXT = createImageFromFileInBundle("icons/export_to_text_16.png");
	public static final Image COMMON_EXPORT_TO_EXCEL = createImageFromFileInBundle("icons/export_to_excel_16.png");
	public static final Image COMMON_ENCRYPT_CSV = createImageFromFileInBundle("icons/lock_csv.png");
	public static final Image COMMON_DECRYPT_CSV = createImageFromFileInBundle("icons/unlock_csv.png");
	
	public static final Image COMMON_ABOUT = createImageFromFileInBundle("icons/about_16.png");
	public static final Image COMMON_SETTINGS = createImageFromFileInBundle("icons/settings_16.png");
	
	public static final Image COMMON_UPLOAD_TO_CLOUD = createImageFromFileInBundle("icons/upload_to_cloud.png");
	
	public static final Image IMPORT_REFERENCE_VALUE = createImageFromFileInBundle("icons/import_reference_value.png");

	// 퍼스펙티브
	public static final Image COMMON_DATA_POINT = createImageFromFileInBundle("icons/data_point_16.png");
	public static final Image COMMON_METRIC = createImageFromFileInBundle("icons/metric_16.png");

	/**
	 * Create and return image. If created yet, do not create image.
	 * 
	 * @param path
	 * @return
	 */
	private static Image createImageFromFileInBundle(String path) {
		URL url = Activator.getResource(path);
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		return desc.createImage();
	}
}
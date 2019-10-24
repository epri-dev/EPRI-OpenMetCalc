package com.epri.metric_calculator.util;

import java.io.IOException;

/**
 * Use this class when you want to handle windows explorer.
 * 
 * @author JoWookJae
 *
 */
public class WindowsExplorerUtil {

	public static Process select(String path) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		return runtime.exec("explorer.exe /select," + path); //$NON-NLS-1$
	}
}

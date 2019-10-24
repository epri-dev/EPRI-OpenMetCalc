package com.epri.metric_calculator.actions.snapshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.ToolSetting;
import com.sst.sstat.metric.Metric;

/**
 * It can be used to collect historical data
 * 
 * @author JoWookJae
 *
 */
public class HistoricalDataSnapthot {

	/**
	 * Target project for collecting historical data
	 */
	private MetCalcProject project;

	/**
	 * Constructor
	 * 
	 * @param project
	 */
	public HistoricalDataSnapthot(MetCalcProject project) {
		this.project = project;
	}

	/**
	 * Delete all history data
	 * 
	 * @throws IOException
	 */
	public void deleteAllData() throws IOException {
		FileUtils.deleteDirectory(getStoreDir());
	}

	/**
	 * Remove previous data in the period
	 */
	public void deletePrevDataInPeriod() {
		SnapshotPeriod period = ToolSetting.getInstance().getCollectionPeriod();
		long periodMillis = period.convertToMillis();
		long currentMillis = System.currentTimeMillis();

		if (getStoreDir().listFiles() != null) {
			for (File file : getStoreDir().listFiles()) {
				String name = file.getName();
				try {
					// Date the historic data was created
					Date date = Const.HISTORY_FILE_NAME_FORMAT.parse(name);

					// Difference between current time and the time historic data was created.
					long diff = currentMillis - date.getTime();

					// If Difference is less than the period, delete file.
					if (diff < periodMillis) {
						file.delete();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Write file with current time as file's name for collecting metric values in
	 * project history data
	 * 
	 * @param metrics
	 * @throws IOException
	 */
	public void snapshot(List<Metric> metrics) throws IOException {
		String label = Const.HISTORY_FILE_NAME_FORMAT.format(new Date()) + "." + Const.HISTORIC_DATA_EXTENSION;
		collect(label, metrics);
	}

	/**
	 * Collect metric values in project history data
	 * 
	 * @param label
	 *            it equals with file name and chart x-axis label
	 * @param metrics
	 * @throws IOException
	 */
	private void collect(String label, List<Metric> metrics) throws IOException {
		File dir = getStoreDir();

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(dir, label);
		write(new FileWriter(file), metrics);
	}

	/**
	 * Return the directory of histories
	 * 
	 * @return
	 */
	public File getStoreDir() {
		StringBuilder result = new StringBuilder();
		result.append(project.getProjectDir());
		result.append(File.separator);
		result.append(Const.HISTORIC_DATA_STORE_DIRNAME);
		return new File(result.toString());
	}

	/**
	 * Write metric values
	 * 
	 * @param writer
	 * @param metrics
	 * @throws IOException
	 */
	private void write(Writer writer, List<Metric> metrics) throws IOException {
		Map<String, String> data = new HashMap<>();

		for (Metric metric : metrics) {
			data.put(metric.getId(), Float.toString(metric.getValue()));
		}

		write(writer, data);
	}

	/**
	 * Write data
	 * 
	 * @param writer
	 * @param data
	 * @throws IOException
	 */
	private void write(Writer writer, Map<String, String> data) throws IOException {
		ICsvListWriter csvWriter = null;
		try {
			csvWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);
			List<String> headers = new ArrayList<>();
			List<String> values = new ArrayList<>();

			for (Entry<String, String> entry : data.entrySet()) {
				headers.add(entry.getKey());
				values.add(entry.getValue());
			}

			csvWriter.writeHeader(headers.toArray(new String[headers.size()]));
			csvWriter.write(values);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
		}
	}
}
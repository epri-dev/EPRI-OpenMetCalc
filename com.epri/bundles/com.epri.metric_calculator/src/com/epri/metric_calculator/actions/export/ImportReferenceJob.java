package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Messages;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricManager;

/**
 * Job for importing reference values
 * 
 * @author JoWookJae
 *
 */
public class ImportReferenceJob extends Job {

	/**
	 * Metric manager to perform import reference values
	 */
	private MetricManager metricManager;

	/**
	 * Target excel file.
	 */
	private File targetFile;

	/**
	 * Constant that mean invalid index
	 */
	private static final int INVALID_INDEX = -1;

	/**
	 * Constructor
	 * 
	 * @param metricManager
	 * @param targetFile
	 */
	public ImportReferenceJob(MetricManager metricManager, File targetFile) {
		super(Messages.ImportReferenceJob_0);
		this.metricManager = metricManager;
		this.targetFile = targetFile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus result = new Status(IStatus.OK, Activator.PLUGIN_ID, null);
		try {
			// read excel file.
			doWork(monitor);
		} catch (Throwable e) {
			e.printStackTrace();
			result = new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		}

		return result;
	}

	/**
	 * Read excel
	 * 
	 * @param monitor
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private IStatus doWork(IProgressMonitor monitor)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		Workbook workbook = WorkbookFactory.create(targetFile);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIter = sheet.rowIterator();

		// Read header row
		Row row = rowIter.next();
		Iterator<Cell> cellIter = row.cellIterator();

		List<Integer> metricIdColumnIndexes = new ArrayList<>();
		int referenceValueColumnIndex = INVALID_INDEX;

		// Find indexes of "Metric" column and the index of "Reference Value" column.
		while (cellIter.hasNext()) {
			Cell cell = cellIter.next();
			String header = cell.getStringCellValue();
			if (header.equals(MetricExcelExporter.HEADER_METRIC)) {
				metricIdColumnIndexes.add(cell.getColumnIndex());
			} else if (header.equals(MetricExcelExporter.HEADER_REFERENCE_VALUE)) {
				referenceValueColumnIndex = cell.getColumnIndex();
			}
		}

		// Find all column index of metric id.
		metricIdColumnIndexes = listConsecutiveNumbers(metricIdColumnIndexes);

		// If the required column header does not exist, return result.
		if (metricIdColumnIndexes.isEmpty()) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					NLS.bind(Messages.ImportReferenceJob_3, MetricExcelExporter.HEADER_METRIC));
		} else if (referenceValueColumnIndex == INVALID_INDEX) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					NLS.bind(Messages.ImportReferenceJob_3, MetricExcelExporter.HEADER_REFERENCE_VALUE));
		}

		// For each row,
		while (rowIter.hasNext()) {
			// cells of a row
			row = rowIter.next();

			String metricId = null;
			String cellValue = null;

			// Find metric id
			for (int metricIdIndex : metricIdColumnIndexes) {
				Cell metricIdCell = row.getCell(metricIdIndex);

				// If cell is empty, continue
				if (metricIdCell == null) {
					continue;
				}

				cellValue = metricIdCell.getStringCellValue();
				if (cellValue != null && !cellValue.isEmpty()) {
					metricId = cellValue;
					break;
				}
			}

			// If metric id is null, then continue.
			if (metricId == null) {
				continue;
			}

			if (metricId.equals("O-A-MAC") || metricId.equals("O-A-MAP")) {
				System.out.println();
			}

			Metric metric = metricManager.getMetric(metricId);
			// If metric is null, then continue.
			if (metric == null) {
				continue;
			}

			// Get reference value from excel.
			DataFormatter formatter = new DataFormatter();
			String strRefValue = formatter.formatCellValue(row.getCell(referenceValueColumnIndex));
			float referenceValue = 0;
			try {
				referenceValue = Float.parseFloat(strRefValue);
			} catch (NumberFormatException e) {
				// Do nothing...
			}

			metric.setReferenceValue(referenceValue);
		}

		workbook.close();
		return new Status(IStatus.OK, Activator.PLUGIN_ID, null);
	}

	/**
	 * Listing numbers consecutively.
	 * 
	 * @param list
	 * @return
	 */
	private List<Integer> listConsecutiveNumbers(List<Integer> list) {
		List<Integer> result = new ArrayList<>();

		int min = 0;
		int max = 0;

		for (int num : list) {
			if (num < min) {
				min = num;
			} else if (num > max) {
				max = num;
			}
		}

		for (int i = min; i <= max; i++) {
			result.add(i);
		}

		return result;
	}
}
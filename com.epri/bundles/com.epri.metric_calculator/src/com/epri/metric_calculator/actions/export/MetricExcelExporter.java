package com.epri.metric_calculator.actions.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.epri.metric_calculator.MetCalcProject;
import com.sst.sstat.metric.AbMetricTraceRunnable;
import com.sst.sstat.metric.Factor;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricManager;
import com.sst.sstat.metric.MetricRelationshipModel;

/**
 * Use this class when exporting metric information in Excel format.
 * 
 * @author JoWookJae
 *
 */
public class MetricExcelExporter {

	public static final String HEADER_METRIC = "Metric";
	public static final String HEADER_REFERENCE_VALUE = "Reference Value";

	private static final int COLUMN_WIDTH_MAX = 480;

	private static final String SHEET_NAME = "Metrics";

	private static final int NUM_OF_METRIC_NAME_COLUMNS = 3;

	private CellStyle cellStyleHeader;

	private CellStyle cellStyleValue;

	private XSSFWorkbook workbook;

	private int indexOfRow;
	private int indexOfColumn;

	/**
	 * Constructor 
	 */
	public MetricExcelExporter() {
		workbook = new XSSFWorkbook();

		// Cell style of headers
		cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
		Font headerFont = workbook.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		cellStyleHeader.setFont(headerFont);

		// Cell style of values
		cellStyleValue = workbook.createCellStyle();
		cellStyleValue.setFillForegroundColor(IndexedColors.WHITE.getIndex());
	}

	/**
	 * Do export
	 * 
	 * @param destPath
	 * @throws IOException
	 */
	public void export(String destPath) throws IOException {
		createMetricSheet();
		workbook.write(new FileOutputStream(destPath));
		workbook.close();
	}

	/**
	 * Create sheet
	 * 
	 * @return
	 */
	public XSSFSheet createMetricSheet() {
		indexOfRow = 0;
		indexOfColumn = NUM_OF_METRIC_NAME_COLUMNS - 1;

		XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

		List<IXssfColumnCellProvider<MetricRelationshipModel>> metricColumnCellProviders = new ArrayList<>();
		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>(HEADER_METRIC) {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return model.getMetric().getId();
			}
		});

		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>("Name") {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return model.getMetric().getName();
			}

			@Override
			public int getIndexOfColumn() {
				return NUM_OF_METRIC_NAME_COLUMNS;
			}
		});
		
		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>("Weight") {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return Float.toString(model.getWeight());
			}
		});

		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>("Value") {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return model.getMetric().getValueString();
			}
		});

		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>(HEADER_REFERENCE_VALUE) {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return Float.toString(model.getMetric().getReferenceValue());
			}
		});

		metricColumnCellProviders.add(new AbMetricColumnCellProvider<MetricRelationshipModel>("Target Value") {
			@Override
			protected String getValue(MetricRelationshipModel model) {
				return Float.toString(model.getMetric().getTargetValue());
			}
		});

		List<IXssfColumnCellProvider<Factor>> factorColumnCellProviders = new ArrayList<>();
		factorColumnCellProviders.add(new AbMetricColumnCellProvider<Factor>("Factor") {
			@Override
			protected String getValue(Factor model) {
				return model.getName();
			}
		});

//		factorColumnCellProviders.add(new AbMetricColumnCellProvider<Factor>("Description") {
//			@Override
//			protected String getValue(Factor model) {
//				return model.getDescription();
//			}
//		});

		factorColumnCellProviders.add(new AbMetricColumnCellProvider<Factor>("Value") {
			@Override
			protected String getValue(Factor model) {
				return Float.toString(model.getValue());
			}
		});

		// Create header cells
		XSSFRow headerRow = sheet.createRow(indexOfRow);

		for (IXssfColumnCellProvider<?> cellProvider : metricColumnCellProviders) {
			cellProvider.createHeaderCell(headerRow, indexOfColumn, cellStyleHeader);
			indexOfColumn++;
		}

		int maxNumOfFactor = 0;
		for (Metric metric : MetCalcProject.getCurrent().getMetricManager().getMetrics()) {
			int numOfFactor = metric.getFactors().size();
			if (numOfFactor > maxNumOfFactor) {
				maxNumOfFactor = numOfFactor;
			}
		}

		for (int i = 0; i < maxNumOfFactor; i++) {
			for (IXssfColumnCellProvider<?> cellProvider : factorColumnCellProviders) {
				cellProvider.createHeaderCell(headerRow, indexOfColumn, cellStyleHeader);
				indexOfColumn++;
			}
		}

		indexOfRow++;

		MetricManager metricMgr = MetCalcProject.getCurrent().getMetricManager();

		Map<Integer, Integer> rowGouprMap = new HashMap<>();
		Map<Integer, Short> rowColorMap = new HashMap<>();

		// Create value cells
		metricMgr.trace(new AbMetricTraceRunnable() {

			private int lengthOfGroup;

			@Override
			public boolean run(MetricRelationshipModel model) {
				// Put row color
				short fontColorIndex = 0;
				if (model.getMetric().isNoneValue()) {
					fontColorIndex = IndexedColors.GREY_50_PERCENT.getIndex();
				} else if (model.getMetric().isError() || model.getMetric().isOutOfRange()) {
					fontColorIndex = IndexedColors.RED.getIndex();
				} else {
					fontColorIndex = IndexedColors.BLACK.getIndex();
				}

				// Put row color
				rowColorMap.put(indexOfRow, fontColorIndex);

				// Create cell style
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.cloneStyleFrom(cellStyleValue);
				Font font = workbook.createFont();
				font.setColor(fontColorIndex);
				cellStyle.setFont(font);

				indexOfColumn = getTraceLevel();
				XSSFRow valueRow = sheet.createRow(indexOfRow);
				for (IXssfColumnCellProvider<MetricRelationshipModel> columnCellProvider : metricColumnCellProviders) {
					if (columnCellProvider.getIndexOfColumn() != IXssfColumnCellProvider.COLUMN_INDEX_UNSPECIFIED) {
						indexOfColumn = columnCellProvider.getIndexOfColumn();
					}

					columnCellProvider.createValueCell(valueRow, indexOfColumn, cellStyle, model);
					indexOfColumn++;
				}

				int indexOfFactorColumnStart = indexOfColumn;
				for (Factor factor : model.getMetric().getFactors()) {
					for (IXssfColumnCellProvider<Factor> columnCellProvider : factorColumnCellProviders) {
						columnCellProvider.createValueCell(valueRow, indexOfColumn, cellStyle, factor);
						indexOfColumn++;
					}
				}

				// Put row group
				if (!model.getChildren().isEmpty()) {
					lengthOfGroup = 0;

					// Calcurate the length of group
					metricMgr.trace(model, new AbMetricTraceRunnable() {
						@Override
						public boolean run(MetricRelationshipModel model) {
							lengthOfGroup++;
							return true;
						}
					});

					rowGouprMap.put(indexOfRow + 1, indexOfRow + lengthOfGroup - 1);
				}

				indexOfColumn = indexOfFactorColumnStart;
				indexOfRow++;
				return true;
			}
		});

		// Group by metric relationships.
		for (Entry<Integer, Integer> groupEntry : rowGouprMap.entrySet()) {
			sheet.groupRow(groupEntry.getKey(), groupEntry.getValue());
		}

		// Optimize width of columns with cell contents
		optimizewidthOfColumns(sheet);

		// Apply style to headers
		headerRow.setRowStyle(cellStyleHeader);

		// Merge (0, 0) to (0, 3)
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, NUM_OF_METRIC_NAME_COLUMNS - 1));

		// Re-create (0, 0)
		XSSFCell cell0to0 = sheet.getRow(0).createCell(0);
		cell0to0.setCellValue("Metric");
		cell0to0.setCellStyle(cellStyleHeader);

		return sheet;
	}

	private void optimizewidthOfColumns(XSSFSheet sheet) {
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			sheet.autoSizeColumn(i);
			int width = poiWidthToPixels(sheet.getColumnWidth(i));
			if (width > COLUMN_WIDTH_MAX) {
				sheet.setColumnWidth(i, COLUMN_WIDTH_MAX * 256 / 9);
			}
		}
	}

	private int poiWidthToPixels(double width) {
		if (width <= 256) {
			return (int) Math.round((width / 28));
		} else {
			return (int) (Math.round(width * 9 / 256));
		}
	}
}

/**
 * @author JoWookJae
 *
 * @param <T>
 */
abstract class AbMetricColumnCellProvider<T> implements IXssfColumnCellProvider<T> {

	private String columnName;

	public AbMetricColumnCellProvider(String columnName) {
		this.columnName = columnName;
	}

	protected abstract String getValue(T model);

	@Override
	public XSSFCell createHeaderCell(XSSFRow row, int indexOfColumn, CellStyle style) {
		XSSFCell cell = row.createCell(indexOfColumn);
		cell.setCellValue(columnName);
		cell.setCellStyle(style);
		return cell;
	}

	@Override
	public XSSFCell createValueCell(XSSFRow row, int indexOfColumn, CellStyle style, T model) {
		XSSFCell cell = row.createCell(indexOfColumn);
		cell.setCellValue(getValue(model));
		cell.setCellStyle(style);
		return cell;
	}

	@Override
	public int getIndexOfColumn() {
		return COLUMN_INDEX_UNSPECIFIED;
	}
}

/**
 * 
 * 
 * @author JoWookJae
 *
 */
interface IXssfColumnCellProvider<T> {

	public static final int COLUMN_INDEX_UNSPECIFIED = -1;

	/**
	 * Return a i'th cell of header
	 * 
	 * @param row
	 * @param indexOfColumn
	 *            Index of column(i)
	 * @param style
	 * @return
	 */
	public XSSFCell createHeaderCell(XSSFRow row, int indexOfColumn, CellStyle style);

	/**
	 * Retrun a i'th cell of model
	 * 
	 * @param row
	 * @param style
	 * @param model
	 * @param indexOfColumn
	 *            Index of column(i)
	 * @return
	 */
	public XSSFCell createValueCell(XSSFRow row, int indexOfColumn, CellStyle style, T model);

	public int getIndexOfColumn();
}

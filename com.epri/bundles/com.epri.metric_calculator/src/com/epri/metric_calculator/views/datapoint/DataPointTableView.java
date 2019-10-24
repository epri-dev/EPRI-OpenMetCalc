package com.epri.metric_calculator.views.datapoint;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.util.ReflectionUtil;
import com.sst.sstat.ModelDescriptionLoadFailException;
import com.sst.sstat.ModelDescriptionLoader;

/**
 * View for set of data point contents
 * 
 * @author JoWookJae
 *
 */
public class DataPointTableView extends ViewPart {
	public static final String ID = "com.epri.metric_calculator.views.datapoint.DataPointTableView";
	public static final String TABLE_ITEM_EMPTY_VALUE = "";

	private Table table;
	private List<TableItemTextFormatter<?>> formatters;

	public static DataPointTableView getDefault() {
		return (DataPointTableView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	@Override
	public void createPartControl(Composite parent) {
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setHeaderBackground(Const.COLOR_TABLE_HEADER_BACKGROUND);
		table.setHeaderForeground(Const.COLOR_TABLE_HEADER_FOREGROUND);

		formatters = new ArrayList<TableItemTextFormatter<?>>();
		formatters.add(new DateFormatter());
		// formatters.add(new EnumFormatter());
	}

	/**
	 * @param clazz
	 * @param contents
	 */
	public void setModel(Class<?> clazz, List<Object> contents) {

		// Clean all items of table
		cleanData();

		// Re-configurate table columns
		setColumns(clazz);

		// Add contents
		addItems(contents);

		// Optimize column size
		optimizeAllColumnWidths();
	}

	/**
	 * Re-configurate table columns
	 * 
	 * @param clazz
	 *            Model class
	 */
	private void setColumns(Class<?> clazz) {

		// Dispose all columns
		for (TableColumn clmn : table.getColumns()) {
			clmn.dispose();
		}

		// Load model description
		ModelDescriptionLoader descLoader = null;
		try {
			descLoader = ModelDescriptionLoader.getInstance();
		} catch (ModelDescriptionLoadFailException e) {
			MessageDialog.openError(table.getShell(), "Model description load failure", e.getMessage());
		}

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// Craete a column.
			TableColumn clmn = new TableColumn(table, SWT.NONE);

			// Set column name to field name
			clmn.setText(field.getName());

			if (descLoader != null) {
				// Set tooltip text as model description
				clmn.setToolTipText(descLoader.get(field.getName()));
			}
		}
	}

	/**
	 * Add items into table
	 * 
	 * @param items
	 */
	public void addItems(List<Object> items) {
		if (items == null || items.size() == 0)
			return;

		for (Object item : items) {
			addItem(item);
		}
	}

	/**
	 * Add a item into table
	 * 
	 * @param item
	 */
	private void addItem(Object item) {
		// Get fields of item class
		Field[] fields = item.getClass().getDeclaredFields();

		String[] itemTexts = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String itemText = null;
			// Get field value
			Object retValue = ReflectionUtil.invokeGetter(item, field.getType(), field.getName());
			if (retValue == null) {
				itemText = TABLE_ITEM_EMPTY_VALUE;
			} else {

				// Run table item text formatters
				for (TableItemTextFormatter<?> formatter : formatters) {
					itemText = formatter.run(retValue);

					if (itemText != null)
						break;
				}

				// If no formatter for this type
				if (itemText == null)
					itemText = retValue.toString();
			}

			itemTexts[i] = itemText;
		}

		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(itemTexts);
	}

	/**
	 * Optimize all column widths
	 */
	private void optimizeAllColumnWidths() {
		final int MARGIN = 15;

		GC gc = new GC(Display.getCurrent());
		int max[] = new int[table.getColumnCount()];
		// Set initial max width of column to width of column header text
		for (int i = 0; i < max.length; i++) {
			max[i] = gc.textExtent(table.getColumn(i).getText()).x;
		}

		// Set max[i] to The longest text width of i-th column
		for (TableItem item : table.getItems()) {
			for (int i = 0; i < table.getColumnCount(); i++) {
				String text = item.getText(i);
				int width = gc.textExtent(text).x;

				if (max[i] < width)
					max[i] = width;
			}
		}

		// Optimize all table columns
		int lastIndex = 0;
		int lastMaxWidth = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).setWidth(max[i] + MARGIN);

			lastIndex = i;
			lastMaxWidth = max[i] + MARGIN - 1;
		}
		// Set width of last column to max[i-1] - 1
		// This will make the table better
		table.getColumn(lastIndex).setWidth(lastMaxWidth);
	}

	/**
	 * Clean all table items
	 */
	public void cleanData() {
		table.removeAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}
}

/**
 * Formatter for Date
 * 
 * @author JoWookJae
 *
 */
class DateFormatter extends TableItemTextFormatter<Date> {

	protected DateFormatter() {
		super(Date.class);
	}

	@Override
	public String format(Date item) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
		return df.format(item);
	}
}

@SuppressWarnings("rawtypes")
class EnumFormatter extends TableItemTextFormatter<Enum> {

	protected EnumFormatter() {
		super(Enum.class);
	}

	@Override
	protected boolean checkType(Object item) {
		if (item instanceof Enum) {
			return true;
		}

		return false;
	}

	@Override
	public String format(Enum item) {
		return item.name() + "(" + item.ordinal() + ")";
	}
}

/**
 * The class for data formatting
 * 
 * @author JoWookJae
 *
 * @param <T>
 *            Template type for Target data
 */
abstract class TableItemTextFormatter<T> {

	Class<?> type;

	/**
	 * Class of template T
	 * 
	 * @param type
	 */
	protected TableItemTextFormatter(Class<?> type) {
		this.type = type;
	}

	/**
	 * @param item
	 * @return
	 */
	protected boolean checkType(Object item) {
		if (item.getClass().equals(type))
			return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	public String run(Object item) {
		if (checkType(item))
			return format((T) item);

		return null;
	}

	/**
	 * Get text of item
	 * 
	 * @param item
	 *            Input item
	 * @return Text of item
	 */
	public abstract String format(T item);
}

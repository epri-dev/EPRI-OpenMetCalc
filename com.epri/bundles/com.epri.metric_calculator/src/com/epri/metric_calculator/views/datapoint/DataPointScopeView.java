package com.epri.metric_calculator.views.datapoint;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.event.IMetCalcEventHandler;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;

/**
 * View for set of data points
 * 
 * @author JoWookJae
 *
 */
public class DataPointScopeView extends ViewPart {

	public static final String ID = "com.epri.metric_calculator.views.datapoint.DataPointScopeView";

	private Class<?> selection;
	private TableViewer tableViewer;

	/**
	 * Get single instance of data point scope view
	 * 
	 * @return
	 */
	public static DataPointScopeView getDefault() {
		return (DataPointScopeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	/**
	 * Set selection of view
	 * 
	 * @param newSelection
	 *            Model class of selection
	 */
	public void setSelection(Class<?> newSelection) {
		Class<?> oldSelection = selection;

		// Change selected item of table
		tableViewer.setSelection(new StructuredSelection(newSelection));

		selection = (Class<?>) newSelection;

		// If old selection is not null, refresh old selection.
		if (oldSelection != null) {
			tableViewer.update(oldSelection, null);
		}
		
		if(oldSelection != selection) {
			// Refresh new selection
			tableViewer.update(selection, null);
		}
	}

	/**
	 * Get selection of view
	 * 
	 * @return
	 */
	public Class<?> getSelection() {
		return selection;
	}

	/**
	 * Refresh the table and refresh data point table view
	 */
	private void refresh() {
		// Refresh table viewer
		tableViewer.refresh();

		if (selection != null && DataPointTableView.getDefault() != null) {
			// If selection is not null, refresh data point table view
			DataPointTableView.getDefault().setModel(selection, MetCalcProject.getCurrent().get(selection));
		}
	}

	@Override
	public void createPartControl(Composite parent) {

		// Init table viewer
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER) {

			@Override
			public void update(Object element, String[] properties) {
				super.update(element, properties);

				// If the element to update is the selection
				if (element == selection) {
					Class<?> clazz = (Class<?>) element;
					DataPointTableView.getDefault().setModel(clazz, MetCalcProject.getCurrent().get(clazz));
				}
			}
		};

		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(getViewSite());

		Table table = tableViewer.getTable();

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selection == e.item.getData())
					return;

				setSelection((Class<?>) e.item.getData());
			}
		});

		// Register event handler. when project open, refresh this view.
		MetCalcEventManager.getInstance().register(MetCalcEventType.PROJECT_OPEN, new IMetCalcEventHandler() {
			@Override
			public void handle(MetCalcEvent event) {
				refresh();
			}
		});
	}

	/**
	 * Content provider for table viewer of data point scope view
	 * 
	 * @author JoWookJae
	 *
	 */
	class TableContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			MetCalcProject currentProject = MetCalcProject.getCurrent();

			// If no project then no table elements.
			if (MetCalcProject.getCurrent() == null)
				return new Object[0];

			// Table elements : Model classes
			Set<Class<?>> keyset = currentProject.keySet();
			return keyset.toArray(new Class<?>[keyset.size()]);
		}
	}

	class TableLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element == selection) {
				return IconFactory.DATA_POINT_FOLDER_OPEN;
			} else {
				return IconFactory.DATA_POINT_FOLDER;
			}
		}

		@Override
		public String getText(Object element) {
			// Element name : Data point model class name
			Class<?> clazz = (Class<?>) element;
			return clazz.getSimpleName();
		}
	}

	@Override
	public void setFocus() {
	}
}
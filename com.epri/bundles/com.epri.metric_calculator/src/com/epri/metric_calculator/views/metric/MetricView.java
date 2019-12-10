package com.epri.metric_calculator.views.metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.event.AbMetCalcEventHandler;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.epri.metric_calculator.event.MetricPartialUpdatedEvent;
import com.epri.metric_calculator.event.PerspectiveActivateEvent;
import com.epri.metric_calculator.perspectives.MetricPerspective;
import com.epri.metric_calculator.views.metric.provider.FactorLabelProvider;
import com.epri.metric_calculator.views.metric.provider.MetricTreeImagedLabelProvider;
import com.epri.metric_calculator.views.metric.provider.MetricTreeLabelProvider;
import com.sst.sstat.metric.Factor;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricManager;
import com.sst.sstat.metric.MetricRelationshipModel;

/**
 * The class for view metric groups
 * 
 * @author JoWookJae
 *
 */
public class MetricView extends ViewPart {

	/**
	 * View id
	 */
	public static final String ID = "com.epri.metric_calculator.views.metric.MetricView"; //$NON-NLS-1$

	/**
	 * Right margin of first column
	 */
	private static final int COLUMN_RIGHT_MARGIN = 10;

	private static final int COLUMN_MAX_WIDTH = 400;

	public static final float CELL_BACKGROUND_DARK_RATIO = 0.987F;

	/**
	 * Metric tree-viewer
	 */
	private TreeViewer treeViewer;

	/**
	 * whether metric view has been created
	 */
	private boolean isCreated;

	private static final int TOOLTIP_TEXT_MAX_LENGTH = 50;

	/**
	 * Refresh metric view
	 */
	private void refresh(boolean extendAll) {
		for (TreeColumn column : treeViewer.getTree().getColumns()) {
			column.dispose();
		}

		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if (currentProject == null || currentProject.getMetricManager() == null) {
			return;
		}

		MetricManager metricManager = currentProject.getMetricManager();

		// Compute max number of factor
		int maxNumOfFactor = 5;
		for (Metric metric : metricManager.getMetrics()) {
//			int numOfFactor = metric.getFactors().size();
			int numOfFactor = metric.getPureFactors().size();
			if (maxNumOfFactor < numOfFactor) {
				maxNumOfFactor = numOfFactor;
			}
		}

		// Create columns
		initializeColumns(maxNumOfFactor);

		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.setInput(metricManager);

		if (extendAll) {
			treeViewer.expandAll();
		}

		optimizeColumnWidth(0, COLUMN_RIGHT_MARGIN);
		optimizeColumnWidth(1, COLUMN_RIGHT_MARGIN);

		for (int i = 2; i < treeViewer.getTree().getColumnCount(); i++) {
			optimizeColumnWidth(i, 2);
		}

		isCreated = true;
	}

	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION) {

			@Override
			public void refresh() {
				if (treeViewer == null || treeViewer.getControl().isDisposed()) {
					refresh(true);
				}

				super.refresh();

				// Set selection to null
				setSelection((ISelection) null);
			}
		};

		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setHeaderBackground(Const.COLOR_TABLE_HEADER_BACKGROUND);
		tree.setHeaderForeground(Const.COLOR_TABLE_HEADER_FOREGROUND);

		treeViewer.setContentProvider(new MetricTreeContentProvider());
		treeViewer.addTreeListener(new ITreeViewerListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						optimizeColumnWidth(0, COLUMN_RIGHT_MARGIN);
						optimizeColumnWidth(1, COLUMN_RIGHT_MARGIN);
					}
				});
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				optimizeColumnWidth(0, COLUMN_RIGHT_MARGIN);
				optimizeColumnWidth(1, COLUMN_RIGHT_MARGIN);
			}
		});

		registerEventHandlers();
		refresh(true);
	}

	private void registerEventHandlers() {
		MetCalcEventManager eventManager = MetCalcEventManager.getInstance();

		// Register event handler. when project open, refresh this view.
		eventManager.register(MetCalcEventType.PROJECT_OPEN, new AbMetCalcEventHandler(getClass()) {
			@Override
			public void handle(MetCalcEvent event) {
				refresh(true);
			}
		});

		// Register event handler. when metric calculated, refresh data.
		eventManager.register(MetCalcEventType.CALCURATION_FINISHED, new AbMetCalcEventHandler(getClass()) {
			@Override
			public void handle(MetCalcEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						treeViewer.refresh();
					}
				});
			}
		});

		// Register event handler. when dashboard perspective is activated, refresh this
		// view.
		eventManager.register(MetCalcEventType.PERSPECTIVE_ACTIVATED, new AbMetCalcEventHandler(getClass()) {
			@Override
			public void handle(MetCalcEvent event) {
				PerspectiveActivateEvent perspectiveActivateEvent = (PerspectiveActivateEvent) event;
				if (perspectiveActivateEvent.getPerspectiveDscriptor().getId().equals(MetricPerspective.ID)
						&& !isCreated) {
					refresh(true);
				}
			}
		});

		// Register event handler. when metric is updated, refresh view item.
		eventManager.register(MetCalcEventType.METRIC_UPDATED, new AbMetCalcEventHandler(getClass()) {

			@Override
			public void handle(MetCalcEvent event) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						// Refresh update metric from view.
						if (event instanceof MetricPartialUpdatedEvent) {
							String id = ((MetricPartialUpdatedEvent) event).getId();

							if (id == null) {
								return;
							}

							for (TreeItem item : getAllItems()) {
								MetricRelationshipModel model = (MetricRelationshipModel) item.getData();
								if (id.equals(model.getMetric().getId())) {
									treeViewer.refresh(model, true);
								}
							}
						} else {
							refresh(true);
						}
					}

					/**
					 * Return all items of tree viewer.
					 * 
					 * @return
					 */
					private List<TreeItem> getAllItems() {
						List<TreeItem> result = new ArrayList<>();
						for (TreeItem item : treeViewer.getTree().getItems()) {
							result.add(item);
							result.addAll(getItems(item));
						}
						return result;
					}

					/**
					 * Retru all items of tree item.
					 * 
					 * @param item
					 * @return
					 */
					private List<TreeItem> getItems(TreeItem item) {
						List<TreeItem> result = new ArrayList<>();
						for (TreeItem child : item.getItems()) {
							result.add(child);
							result.addAll(getItems(child));
						}
						return result;
					}
				});
			}
		});
	}

	/**
	 * Optimize a width of tree columns
	 * 
	 * @param index
	 */
	private void optimizeColumnWidth(int index, int rightMargin) {
		TreeColumn column = treeViewer.getTree().getColumn(index);
		column.pack();

		int width = column.getWidth() + rightMargin;
		column.setWidth(width > COLUMN_MAX_WIDTH ? COLUMN_MAX_WIDTH : width);
	}

	/**
	 * Create columns
	 * 
	 * @param maxNumOfFactor
	 */
	private void initializeColumns(int maxNumOfFactor) {
		TreeViewerColumn column = null;

		// Column "Metric"
		column = createColumn("ID");
		column.setLabelProvider(new MetricTreeImagedLabelProvider() {
			@Override
			protected String getText(Metric metric) {
				return metric.getId();
			}

			@Override
			protected String getToolTipText(Metric metric) {
				return metric.getName();
			}
		});

		column = createColumn("Name");
		column.setLabelProvider(new MetricTreeLabelProvider() {
			@Override
			protected String getText(Metric metric) {
				return metric.getName();
			}

			@Override
			protected String getToolTipText(Metric metric) {
				return metric.getName();
			}
		});

		if (showDescription()) {
			// Column "Description"
			column = createColumn(Messages.MetricView_COLUMNHEADER_Description);
			column.setLabelProvider(new MetricTreeLabelProvider() {
				@Override
				protected String getText(Metric metric) {
					return metric.getDescription();
				}

				@Override
				protected String getToolTipText(Metric metric) {
					String desc = metric.getDescription();
					StringBuilder result = new StringBuilder();
					for (int i = 0; i * TOOLTIP_TEXT_MAX_LENGTH < desc.length(); i++) {
						int end = desc.length() > TOOLTIP_TEXT_MAX_LENGTH * (i + 1) ? TOOLTIP_TEXT_MAX_LENGTH * (i + 1)
								: desc.length();

						String line = desc.substring(TOOLTIP_TEXT_MAX_LENGTH * i, end);
						result.append(line);
						result.append("\n");
					}

					return metric.getDescription();
				}
			});

			// Set editable "Description" column for only analyst
			TreeTextEditor.makeEditable(column, new AbMetricTreeModifyListener() {
				@Override
				public boolean canModify(TreeItem item, int index) {
					return super.canModify(item, index);
				}

				@Override
				public String modified(MetricRelationshipModel metric, String text) {
					metric.getMetric().setDescription(text);
					return text;
				}
			});
		}

		// Create "Weight"
		column = createColumn(Messages.MetricView_COLUMNHEADER_Weight);
		column.setLabelProvider(new MetricTreeLabelProvider() {
			@Override
			public String getText(Object element) {
				MetricRelationshipModel content = (MetricRelationshipModel) element;
				float weight = content.getWeight();
				if (Float.compare(MetricRelationshipModel.NO_WEIGHT_VALUE, weight) == 0) {
					return "-"; //$NON-NLS-1$
				}

				return Float.toString(weight);
			}
		});
		// Set column "Weight" editable
		TreeTextEditor.makeEditable(column, new AbMetricTreeModifyListener() {
			@Override
			public String modified(MetricRelationshipModel model, String text) {
				try {
					model.setWeight(text);
					return Float.toString(model.getWeight());
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});

		// Column "Value"
		column = createColumn(Messages.MetricView_COLUMNHEADER_Value);
		column.setLabelProvider(new MetricTreeLabelProvider() {
			@Override
			protected String getText(Metric metric) {
				return metric.getValueString();
			}
		});

		// Column "Reference value"
		column = createColumn(Messages.MetricView_COLUMNHEADER_REFERENCE_VALUE);
		column.setLabelProvider(new MetricTreeLabelProvider() {
			@Override
			protected String getText(Metric metric) {
				return Float.toString(metric.getReferenceValue());
			}
		});
		TreeTextEditor.makeEditable(column, new AbMetricTreeModifyListener() {
			@Override
			public String modified(MetricRelationshipModel model, String text) {
				String result = null;
				try {
					float referenceValue = Float.parseFloat(text);
					model.getMetric().setReferenceValue(referenceValue);
					result = Float.toString(referenceValue);
				} catch (NumberFormatException e) {
					// Do nothing
				}
				return result;
			}
		});

		// Column "Target value"
		column = createColumn("Target value");
		column.setLabelProvider(new MetricTreeLabelProvider() {
			@Override
			protected String getText(Metric metric) {
				return Float.toString(metric.getTargetValue());
			}
		});

		TreeTextEditor.makeEditable(column, new AbMetricTreeModifyListener() {
			@Override
			public String modified(MetricRelationshipModel model, String text) {
				String result = null;
				try {
					float targetValue = Float.parseFloat(text);
					model.getMetric().setTargetValue(targetValue);
					result = Float.toString(targetValue);
				} catch (NumberFormatException e) {
					// Do nothing
				}
				return result;
			}
		});

		for (int i = 0; i < maxNumOfFactor; i++) {
			createFactorColumns(i);
		}
	}

	/**
	 * Create columns for a factor
	 * 
	 * @param indexOfFactor
	 */
	private void createFactorColumns(int indexOfFactor) {
		TreeViewerColumn column = null;

		// Column "Factor"
		column = createColumn(Messages.MetricView_COLUMNHEADER_Factor);
		column.setLabelProvider(new FactorLabelProvider(indexOfFactor) {
			@Override
			protected String getText(Factor factor) {
				return factor.getName();
			}

			@Override
			protected String getToolTipText(Metric metric) {
				List<Factor> pureFactor = metric.getPureFactors();
				
				if (indexOfFactor < pureFactor.size()) {
					return pureFactor.get(indexOfFactor).getDescription();
				}

				return super.getToolTipText(metric);
			}
		});

		// Set column "Factor" editable
		TreeTextEditor.makeEditable(column, new AbMetricTreeFactorModifyListener(indexOfFactor) {
			@Override
			public boolean canModify(TreeItem item, int index) {
				return super.canModify(item, index);
			}

			@Override
			public String modified(Factor factor, String text) {
				factor.setName(text);
				return text;
			}
		});

		if (showDescription()) {
			// Create column "Description"
			column = createColumn(Messages.MetricView_COLUMNHEADER_Description);
			column.setLabelProvider(new FactorLabelProvider(indexOfFactor) {
				@Override
				protected String getText(Factor factor) {
					return factor.getDescription();
				}
			});

			// Set column "Description" editable
			TreeTextEditor.makeEditable(column, new AbMetricTreeFactorModifyListener(indexOfFactor) {
				@Override
				public boolean canModify(TreeItem item, int columnIndex) {
					return super.canModify(item, columnIndex);
				}

				@Override
				public String modified(Factor factor, String text) {
					factor.setDescription(text);
					return text;
				}
			});
		}

		// Create column "Value"
		column = createColumn(Messages.MetricView_COLUMNHEADER_Value);
		column.setLabelProvider(new FactorLabelProvider(indexOfFactor) {
			@Override
			protected String getText(Factor factor) {
				return Float.toString(factor.getValue());
			}
		});

		// Set column "Value" editable
		TreeTextEditor.makeEditable(column, new AbMetricTreeFactorModifyListener(indexOfFactor) {
			@Override
			public String modified(Factor factor, String text) {
				try {
					factor.setValue(text);
					return Float.toString(factor.getValue());
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});
	}

	/**
	 * Create a single column
	 * 
	 * @param headerName
	 * @return
	 */
	private TreeViewerColumn createColumn(String headerName) {
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn column = treeViewerColumn.getColumn();
		column.setText(headerName);
		column.pack();

		return treeViewerColumn;
	}

	private boolean showDescription() {
		try {
			return ToolSetting.getInstance().get(ToolSetting.PROP_SHOW_DESCRIPTION_IN_METRIC_VIEW)
					.compareToIgnoreCase("true") == 0;
		} catch (IOException | NullPointerException e) {
			// Do nothing...
		}
		return false;
	}

	@Override
	public void setFocus() {
	}

	/**
	 * Metric item modify listener
	 * 
	 * @author JoWookJae
	 *
	 */
	private abstract class AbMetricTreeModifyListener extends AbModifyListener {

		/**
		 * Callback method be called when cell text changed.
		 * 
		 * @param metric
		 * @param text
		 * @return
		 */
		public abstract String modified(MetricRelationshipModel metric, String text);

		@Override
		public String modified(TreeItem item, int index, String text) {
			MetricRelationshipModel content = (MetricRelationshipModel) item.getData();
			String result = modified(content, text);
			if (result == null) {
				// result is null, invoke super's method.
				return super.modified(item, index, text);
			} else {
				// result is not null -> the metric model will chanage.
				// Change dirty flag.
				MetCalcProject.getCurrent().setDirty(true);

				// Refresh the item in tree-viewer.
				// treeViewer.refresh(item, true);
				treeViewer.refresh();

				// Fire update event
				MetCalcEventManager.getInstance().fire(new MetricPartialUpdatedEvent());

				return result;
			}
		}
	}

	/**
	 * Metric item modify listener
	 * 
	 * @author JoWookJae
	 *
	 */
	private abstract class AbMetricTreeFactorModifyListener extends AbMetricTreeModifyListener {

		private int index;

		public AbMetricTreeFactorModifyListener(int index) {
			this.index = index;
		}

		public abstract String modified(Factor factor, String text);

		@Override
		public boolean canModify(TreeItem item, int columnIndex) {
			MetricRelationshipModel model = (MetricRelationshipModel) item.getData();
			Metric metric = model.getMetric();
			if (this.index < metric.getPureFactors().size()) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String modified(MetricRelationshipModel model, String text) {
			return modified(model.getMetric().getFactors().get(index), text);
		}
	}
}
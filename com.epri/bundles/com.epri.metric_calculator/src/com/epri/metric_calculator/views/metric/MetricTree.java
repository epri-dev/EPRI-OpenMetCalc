//package com.epri.metric_calculator.views.metric;
//
//import java.util.List;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeColumn;
//import org.eclipse.swt.widgets.TreeItem;
//
//import com.epri.metric_calculator.IconFactory;
//import com.epri.metric_calculator.MetCalcProject;
//import com.epri.metric_calculator.ToolSetting;
//import com.sst.sstat.metric.Factor;
//import com.sst.sstat.metric.Metric;
//import com.sst.sstat.metric.MetricGroup;
//
///**
// * The class of Metric tree in Metric perspective
// * 
// * @author JoWookJae
// *
// */
//public class MetricTree extends Tree {
//
//	public MetricTree(Composite parent) {
//		super(parent, SWT.BORDER | SWT.FULL_SELECTION);
//
//		Display.getCurrent().asyncExec(() -> {
//			setLinesVisible(true);
//			setHeaderVisible(true);
//
//			initColumn();
//			refresh();
//
//			int userType = ToolSetting.getInstance().getUserType();
//			switch (userType) {
//			case ToolSetting.USER_TYPE_NORMAL_USER:
//				EditableTree.setEditable(this, new int[] { 3, 5, 7, 9, 11, 13, 15, 17, 19, 21 });
//				break;
//			case ToolSetting.USER_TYPE_ANALYST:
//				EditableTree.setEditable(this,
//						new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 });
//				break;
//			}
//
//			addListener(SWT.Expand, (e) -> {
//				if (e.item instanceof MetricGroupItem) {
//					MetricGroupItem mgi = (MetricGroupItem) e.item;
//					mgi.setImage(IconFactory.METRIC_CATEGORY_EXPANDED);
//				}
//			});
//
//			addListener(SWT.Collapse, (e) -> {
//				if (e.item instanceof MetricGroupItem) {
//					MetricGroupItem mgi = (MetricGroupItem) e.item;
//					mgi.setImage(IconFactory.METRIC_CATEGORY_COLLAPSED);
//				}
//			});
//		});
//	}
//
//	/**
//	 * Refresh item visual
//	 */
//	public void refresh() {
//		removeAll();
//
//		MetCalcProject currentProject = MetCalcProject.getCurrent();
//		if (currentProject == null)
//			return;
//
//		List<MetricGroup> mgl = MetCalcProject.getCurrent().getMetricGroups();
//		for (MetricGroup mg : mgl) {
//			MetricGroupItem mgi = new MetricGroupItem(this, mg);
//			for (Metric m : mg.getMetricList()) {
//				new MetricItem(mgi, m);
//			}
//		}
//
//		optimizeTree();
//	}
//
//	/**
//	 * Init tree columns
//	 */
//	private void initColumn() {
//		new TreeColumn(this, SWT.NONE).setText("Metric");
//		new TreeColumn(this, SWT.NONE).setText("Description");
//		new TreeColumn(this, SWT.NONE).setText("Factor #1");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #2");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #3");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #4");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #5");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #6");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #7");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #8");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #9");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//		new TreeColumn(this, SWT.NONE).setText("Factor #10");
//		new TreeColumn(this, SWT.NONE).setText("Value");
//
//	}
//
//	/**
//	 * Expand all items, resize column width
//	 */
//	private void optimizeTree() {
//		// expand all nodes
//		expandAll();
//
//		// Optimize width of all columns
//		for (TreeColumn clmn : getColumns())
//			clmn.pack();
//	}
//
//	/**
//	 * Expand all itemss
//	 */
//	private void expandAll() {
//		for (TreeItem item : getItems()) {
//			item.setExpanded(true);
//		}
//	}
//
//	@Override
//	protected void checkSubclass() {
//	}
//}
//
///**
// * Tree item for metric group
// * 
// * @author JoWookJae
// *
// */
//class MetricGroupItem extends TreeItem {
//
//	MetricGroup mg;
//
//	public MetricGroupItem(Tree tree, MetricGroup mg) {
//		super(tree, SWT.NONE);
//		this.mg = mg;
//		setText(new String[] { mg.getName(), mg.getDescription() });
//		setImage(IconFactory.METRIC_CATEGORY_EXPANDED);
//	}
//
//	@Override
//	public void setText(int index, String text) {
//		boolean isDirty = true;
//
//		// MetricGroupItem is modified
//		try {
//			switch (index) {
//			case 0:
//				mg.setName(text);
//				break;
//			case 1:
//				mg.setDescription(text);
//				break;
//			default:
//				isDirty = false;
//				break;
//			}
//		} catch (NumberFormatException e) {
//			return;
//		}
//
//		if (isDirty) {
//			super.setText(index, text);
//			MetCalcProject.getCurrent().setDirty(true);
//		}
//	}
//
//	@Override
//	protected void checkSubclass() {
//	}
//}
//
//class MetricItem extends TreeItem {
//
//	private static final Color COLOR_FAIL = new Color(null, 255, 215, 215);
//	private static final Color COLOR_NONE = new Color(null, 255, 255, 255);
//	private static final Color COLOR_SUCCESS = new Color(null, 215, 255, 215);
//	/**
//	 * data of tree item
//	 */
//	Metric m;
//
//	public MetricItem(MetricGroupItem parent, Metric metric) {
//		super(parent, SWT.NONE);
//		setImage(IconFactory.METRIC_NODE);
//		initText(metric);
//	}
//
//	public void initText(Metric m) {
//		this.m = m;
//
//		String[] texts = new String[22];
//		texts[0] = m.getName() + " : " + m.getScore();
//		texts[1] = m.getDescription();
//
//		Factor[] fs = m.getFactorList();
//		for (int i = 0; i < fs.length; i++) {
//			texts[(i + 1) * 2] = fs[i].getName();
//			texts[(i + 1) * 2 + 1] = fs[i].getValue().toString();
//		}
//
//		setText(texts);
//	}
//
//	@Override
//	public void setText(int index, String text) {
//		boolean isDirty = true;
//
//		// MetricItem is modified
//		try {
//			switch (index) {
//			case 0:
//				// Metric name
//				break;
//			case 1:
//				// Description
//				m.setDescription(text);
//				break;
//			case 2:
//				// Factor #1 name
//				m.getFactorList()[0].setName(text);
//				break;
//			case 3:
//				// Factor #1 value
//				m.getFactorList()[0].setValue(Float.parseFloat(text));
//				break;
//			case 4:
//				// Factor #2 name
//				m.getFactorList()[1].setName(text);
//				break;
//			case 5:
//				// Factor #2 value
//				m.getFactorList()[1].setValue(Float.parseFloat(text));
//				break;
//			case 6:
//				// Factor #3 name
//				m.getFactorList()[2].setName(text);
//				break;
//			case 7:
//				// Factor #3 value
//				m.getFactorList()[2].setValue(Float.parseFloat(text));
//				break;
//			case 8:
//				// Factor #4 name
//				m.getFactorList()[3].setName(text);
//				break;
//			case 9:
//				// Factor #4 value
//				m.getFactorList()[3].setValue(Float.parseFloat(text));
//				break;
//			case 10:
//				// Factor #5 name
//				m.getFactorList()[4].setName(text);
//				break;
//			case 11:
//				// Factor #5 value
//				m.getFactorList()[4].setValue(Float.parseFloat(text));
//				break;
//			case 12:
//				// Factor #6 name
//				m.getFactorList()[5].setName(text);
//				break;
//			case 13:
//				// Factor #6 value
//				m.getFactorList()[5].setValue(Float.parseFloat(text));
//				break;
//			case 14:
//				// Factor #7 name
//				m.getFactorList()[6].setName(text);
//				break;
//			case 15:
//				// Factor #7 value
//				m.getFactorList()[6].setValue(Float.parseFloat(text));
//				break;
//			case 16:
//				// Factor #8 name
//				m.getFactorList()[7].setName(text);
//				break;
//			case 17:
//				// Factor #8 value
//				m.getFactorList()[7].setValue(Float.parseFloat(text));
//				break;
//			case 18:
//				// Factor #9 name
//				m.getFactorList()[8].setName(text);
//				break;
//			case 19:
//				// Factor #9 value
//				m.getFactorList()[8].setValue(Float.parseFloat(text));
//				break;
//			case 20:
//				// Factor #5 name
//				m.getFactorList()[9].setName(text);
//				break;
//			case 21:
//				// Factor #5 value
//				m.getFactorList()[9].setValue(Float.parseFloat(text));
//				break;
//			default:
//				isDirty = false;
//				break;
//			}
//		} catch (NumberFormatException e) {
//			return;
//		}
//
//		if (isDirty) {
//			super.setText(index, text);
//			MetCalcProject.getCurrent().setDirty(true);
//
//			if (m.getScore() == Metric.RESULT_FAIL) {
//				setBackground(COLOR_FAIL);
//			} else if (m.getScore() == Metric.RESULT_NONE) {
//				setBackground(COLOR_NONE);
//			} else {
//				setBackground(COLOR_SUCCESS);
//			}
//		}
//	}
//
//	@Override
//	protected void checkSubclass() {
//	}
//}
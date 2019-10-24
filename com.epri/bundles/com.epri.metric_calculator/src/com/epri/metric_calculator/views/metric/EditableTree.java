//package com.epri.metric_calculator.views.metric;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.TreeEditor;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeColumn;
//import org.eclipse.swt.widgets.TreeItem;
//
//import com.epri.metric_calculator.views.metric.EditableTree.IModifyListener;
//
//public abstract class EditableTree extends Tree {
//
//	public EditableTree(Composite parent, int style, IModifyListener listener, int... columnIndexes) {
//		super(parent, style);
//		new TreeTextEditor(this, columnIndexes, listener);
//	}
//
//	public static TreeTextEditor setEditable(Tree tree, IModifyListener listener) {
//		int[] indexes = new int[tree.getColumnCount()];
//		for (int i = 0; i < tree.getColumnCount(); i++) {
//			indexes[i] = i;
//		}
//
//		return setEditable(tree, listener, indexes);
//	}
//
//	public static TreeTextEditor setEditable(Tree tree, IModifyListener listener, int... columnIndexes) {
//		return new TreeTextEditor(tree, columnIndexes, listener);
//	}
//
//	@Override
//	protected void checkSubclass() {
//	}
//
//	interface IModifyListener {
//		public void modified(TreeItem item, int indexOfClmn, String text);
//	}
//}
//
//class TreeTextEditor {
//	Tree tree;
//	TreeEditor editor;
//	Rectangle clientArea;
//	int[] columnIndexes;
//	IModifyListener modifyListener;
//
//	public TreeTextEditor(final Tree tree, int[] columnIndexes, IModifyListener modifyListener) {
//		this.tree = tree;
//		this.columnIndexes = columnIndexes;
//		this.modifyListener = modifyListener;
//
//		editor = new TreeEditor(tree);
//		editor.horizontalAlignment = SWT.LEFT;
//		editor.grabHorizontal = true;
//
//		clientArea = tree.getClientArea();
//
//		tree.addListener(SWT.MouseDown, (e) -> {
//			traversal(tree, e);
//		});
//	}
//
//	/**
//	 * Preorder traversal
//	 * 
//	 * @param tree
//	 *            tree to traversal
//	 * 
//	 * @param event
//	 */
//	private void traversal(Tree tree, Event event) {
//		for (TreeItem child : tree.getItems()) {
//			process(child, event);
//			traversal(child, event);
//		}
//	}
//
//	/**
//	 * Preorder traversal
//	 * 
//	 * @param item
//	 * @param event
//	 */
//	private void traversal(TreeItem item, Event event) {
//		for (TreeItem child : item.getItems()) {
//			process(child, event);
//			traversal(child, event);
//		}
//	}
//
//	private void process(TreeItem item, Event e) {
//		boolean visible = false;
//
//		for (int i = 0; i < columnIndexes.length; i++) {
//			Rectangle bound = item.getBounds(columnIndexes[i]);
//			if (bound.contains(e.x, e.y)) {
//				final Text text = new Text(tree, SWT.BORDER);
//
//				Listener textListener = new TextListener(item, columnIndexes[i], text);
//				text.addListener(SWT.FocusOut, textListener);
//				text.addListener(SWT.Traverse, textListener);
//				text.addListener(SWT.Modify, textListener);
//				text.addListener(SWT.KeyDown, textListener);
//				editor.setEditor(text, item, columnIndexes[i]);
//				text.setText(item.getText(columnIndexes[i]));
//				text.selectAll();
//				text.setFocus();
//			}
//
//			if (!visible && bound.intersects(clientArea))
//				visible = true;
//
//			if (!visible)
//				continue;
//		}
//	}
//
//	class TextListener implements Listener {
//		private TreeItem item;
//		private int indexOfColumn;
//		private Text text;
//
//		public TextListener(TreeItem item, int indexOfColumn, Text text) {
//			this.item = item;
//			this.indexOfColumn = indexOfColumn;
//			this.text = text;
//		}
//
//		public void handleEvent(final Event e) {
//			switch (e.type) {
//			case SWT.KeyDown:
//				// If pressed enter key, do "case SWT.FocusOut"
//				if (e.character != '\r') {
//					break;
//				}
//			case SWT.FocusOut:
//				if (!item.isDisposed() && !item.getText(indexOfColumn).equals(text.getText())) {
//					item.setText(indexOfColumn, text.getText());
//
//					if (modifyListener != null) {
//						modifyListener.modified(item, indexOfColumn, text.getText());
//					}
//
//					for (TreeColumn clmn : tree.getColumns()) {
//						clmn.setResizable(true);
//					}
//				}
//
//				text.dispose();
//				break;
//			case SWT.Traverse:
//				if (e.detail == SWT.TRAVERSE_ESCAPE) {
//					text.dispose();
//					e.doit = false;
//				}
//
//				break;
//			}
//		}
//	}
//}

//package com.epri.metric_calculator.views.metric;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.TreeEditor;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeColumn;
//import org.eclipse.swt.widgets.TreeItem;
//
//public class EditableTreeColumn {
//}
//
//interface IModifyListener {
//	public void modified(TreeItem item, String text);
//}
//
//class TreeTextEditor {
//	Tree tree;
//	TreeEditor editor;
//	Rectangle clientArea;
//	int indexOfColumn;
//	IModifyListener modifyListener;
//
//	public TreeTextEditor(TreeColumn column, IModifyListener modifyListener) {
//		this.tree = column.getParent();
//		this.indexOfColumn = tree.indexOf(column);
//		this.modifyListener = modifyListener;
//
//		editor = new TreeEditor(tree);
//		editor.horizontalAlignment = SWT.LEFT;
//		editor.grabHorizontal = true;
//
//		clientArea = tree.getClientArea();
//	}
//
//	public void makeEditable() {
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
//	/**
//	 * Process on item.
//	 * 
//	 * @param item
//	 * @param e
//	 */
//	private void process(TreeItem item, Event e) {
//		Rectangle bound = item.getBounds(indexOfColumn);
//		if (bound.contains(e.x, e.y)) {
//			final Text text = new Text(tree, SWT.BORDER);
//
//			Listener textListener = new TextListener(item, indexOfColumn, text);
//			text.addListener(SWT.FocusOut, textListener);
//			text.addListener(SWT.Traverse, textListener);
//			text.addListener(SWT.Modify, textListener);
//			text.addListener(SWT.KeyDown, textListener);
//			editor.setEditor(text, item, indexOfColumn);
//			text.setText(item.getText(indexOfColumn));
//			text.selectAll();
//			text.setFocus();
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
//						modifyListener.modified(item, text.getText());
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

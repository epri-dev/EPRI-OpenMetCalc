package com.epri.metric_calculator.views.metric;

import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class TreeTextEditor {
	private Tree tree;
	private TreeEditor editor;
	private int indexOfColumn;
	private IModifyListener modifyListener;

	public static void makeEditable(TreeViewerColumn column, IModifyListener listener) {
		new TreeTextEditor(column.getColumn(), listener).makeEditable();
	}

	public static void makeEditable(TreeColumn column, IModifyListener listener) {
		new TreeTextEditor(column, listener).makeEditable();
	}

	public TreeTextEditor(TreeColumn column, IModifyListener modifyListener) {
		this.tree = column.getParent();
		this.indexOfColumn = tree.indexOf(column);
		this.modifyListener = modifyListener;

		editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}

	private void makeEditable() {
		tree.addListener(SWT.MouseDown, (e) -> {
			traversal(tree, e);
		});
	}

	/**
	 * Preorder traversal
	 * 
	 * @param tree
	 *            tree to traversal
	 * 
	 * @param event
	 */
	private void traversal(Tree tree, Event event) {
		for (TreeItem child : tree.getItems()) {
			process(child, event);
			traversal(child, event);
		}
	}

	/**
	 * Preorder traversal
	 * 
	 * @param item
	 * @param event
	 */
	private void traversal(TreeItem item, Event event) {
		for (TreeItem child : item.getItems()) {
			process(child, event);
			traversal(child, event);
		}
	}

	/**
	 * Process on item.
	 * 
	 * @param item
	 * @param e
	 */
	private void process(TreeItem item, Event e) {
		Rectangle bound = item.getBounds(indexOfColumn);
		if (bound.contains(e.x, e.y) && modifyListener.canModify(item, indexOfColumn)) {
			final Text text = new Text(tree, SWT.BORDER);

			Listener textListener = new TextListener(item, text);
			text.addListener(SWT.FocusOut, textListener);
			text.addListener(SWT.Traverse, textListener);
			text.addListener(SWT.Modify, textListener);
			text.addListener(SWT.KeyDown, textListener);
			editor.setEditor(text, item, indexOfColumn);
			text.setText(item.getText(indexOfColumn));
			text.selectAll();
			text.setFocus();
		}
	}

	class TextListener implements Listener {
		private TreeItem item;
		private Text text;

		public TextListener(TreeItem item, Text text) {
			this.item = item;
			this.text = text;
		}

		public void handleEvent(final Event e) {
			switch (e.type) {
			case SWT.KeyDown:
				// If pressed enter key, do "case SWT.FocusOut"
				if (e.character != '\r') {
					break;
				}
			case SWT.FocusOut:
				if (!item.isDisposed() && !item.getText(indexOfColumn).equals(text.getText())) {
					if (modifyListener != null) {
						String result = modifyListener.modified(item, indexOfColumn, text.getText());
						item.setText(indexOfColumn, result);
					}

					for (TreeColumn clmn : tree.getColumns()) {
						clmn.setResizable(true);
					}
				}

				text.dispose();
				break;
			case SWT.Traverse:
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					text.dispose();
					e.doit = false;
				}

				break;
			}
		}
	}
}

interface IModifyListener {

	public boolean canModify(TreeItem item, int index);

	/**
	 * Callback method be invoked when cell value changed. Return the text to be
	 * displayed in the cell.
	 * 
	 * @param item
	 * @param index
	 * @param text
	 * @return The string you want to apply to text
	 */
	public String modified(TreeItem item, int index, String text);
}

abstract class AbModifyListener implements IModifyListener {

	@Override
	public boolean canModify(TreeItem item, int index) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epri.metric_calculator.views.metric.IModifyListener#modified(org.eclipse.
	 * swt.widgets.TreeItem, java.lang.String)
	 */
	@Override
	public String modified(TreeItem item, int index, String text) {
		// Returns the original text
		return item.getText(index);
	}
}
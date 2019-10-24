
package com.epri.metric_calculator.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.epri.metric_calculator.Messages;

/**
 * Abstract class for Action will extends the other action.
 * 
 * @author JoWookJae
 *
 */
public abstract class AbstractAction extends Action {

	/**
	 * @param name
	 *            Action text
	 * @param image
	 *            Action image
	 */
	protected AbstractAction(String name, Image image) {
		super(name, ImageDescriptor.createFromImage(image));
		setActionDefinitionId(getId());
	}

	/**
	 * @param name
	 *            Action name
	 * @param image
	 *            Action image
	 * @param style
	 *            Action style
	 */
	protected AbstractAction(String name, Image image, int style) {
		super(name, style);
		setImageDescriptor(ImageDescriptor.createFromImage(image));
		setActionDefinitionId(getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// If this method not override, Open dialog "Not supported yet."
		Shell shell = Display.getDefault().getActiveShell();
		String message = Messages.AbstractAction_0;
		int imageType = 0;
		String[] btnLabels = { Messages.AbstractAction_1 };
		int defaultIndex = 0;

		MessageDialog dlg = new MessageDialog(shell, getText(), null, message, imageType, btnLabels, defaultIndex);
		dlg.open();
	}

	protected void notifySuccess() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.AbstractAction_2,
						NLS.bind(Messages.AbstractAction_3, getText()));
			}
		});

		notifyResult(true);
	}

	protected void notifyFail() {
		notifyFail(null);
	}

	protected void notifyFail(final String message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.ERROR_TITLE,
						NLS.bind(Messages.AbstractAction_4 + message == null ? "" : message, getText())); // $NON-NLS-2$
			}
		});

		notifyResult(false);
	}

	@Override
	public abstract String getId();
}

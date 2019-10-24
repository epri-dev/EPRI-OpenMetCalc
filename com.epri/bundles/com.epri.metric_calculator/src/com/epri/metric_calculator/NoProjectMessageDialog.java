package com.epri.metric_calculator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.actions.file.CreateAction;

public class NoProjectMessageDialog {

	/**
	 * Open no project message dialog.
	 * 
	 * You can also create a new project through this dialog.
	 */
	public static void open() {
		MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(),
				Messages.Question, null, Messages.NoProjectMessageDialog_MESSAGE,
				MessageDialog.QUESTION,
				new String[] { Messages.NoProjectMessageDialog_BTN_OK, Messages.NoProjectMessageDialog_BTN_CANCEL },
				0) {

			@Override
			protected void buttonPressed(int buttonId) {
				super.buttonPressed(buttonId);

				if (buttonId == 0) {
					new CreateAction().run();
				}
			}
		};

		dlg.open();
	}
}

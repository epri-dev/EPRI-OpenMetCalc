package com.epri.metric_calculator.actions.recalculation;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class RecalculationWizardDialog extends WizardDialog  {

	public RecalculationWizardDialog() {
		super(Display.getDefault().getActiveShell(), new RecalculationWizard());
		this.setPageSize(150, 320);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    super.createButtonsForButtonBar(parent);
	    Button finish = getButton(IDialogConstants.FINISH_ID);
	    finish.setText("Calculate");
	}
}

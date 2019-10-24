package com.epri.metric_calculator.actions.metricshubsetting;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class MetricsHubSettingWizardDialog extends WizardDialog {

	public MetricsHubSettingWizardDialog() {
		super(Display.getDefault().getActiveShell(), new MetricsHubSettingWizard());
		this.setPageSize(100, 100);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    super.createButtonsForButtonBar(parent);
	    Button finish = getButton(IDialogConstants.FINISH_ID);
	    finish.setText("OK");
	}
}

package com.epri.metric_calculator.actions.metricshub;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

public class MetricsHubUploadWizardDialog extends WizardDialog {

	public MetricsHubUploadWizardDialog() {
		super(Display.getDefault().getActiveShell(), new MetricsHubUploadWizard());
	}
}

package com.epri.metric_calculator.actions.metricshubsetting;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

public class MetricsHubSettingWizard extends Wizard {

	private MetricsHubSettingWizardPage settingPage;
	
	public MetricsHubSettingWizard() {
		setWindowTitle("Metrics Hub URL");
	}

	@Override
	public void addPages() {
		settingPage = new MetricsHubSettingWizardPage();
		addPage(settingPage);
	}
	
	@Override
	public boolean performFinish() {
		settingPage.checkURL();
		if (settingPage.isURLCorrect()) {
			settingPage.saveMetricsHubURL();
			return true;	
		} else {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Connection Error", "URL is invalid. Please check URL of Metrics Hub and retry.");
			return false;
		}
	}
}
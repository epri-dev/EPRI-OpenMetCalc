package com.epri.metric_calculator.actions.metricshub;

import org.eclipse.jface.wizard.Wizard;

public class MetricsHubUploadWizard extends Wizard {

	private MetricsHubUploadWizardPage uploadPage;
	
	public MetricsHubUploadWizard() {
		setWindowTitle("Metrics Hub Upload");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		uploadPage = new MetricsHubUploadWizardPage();
		addPage(uploadPage);
	}

	@Override
	public boolean performFinish() {
		uploadPage.uploadData();
		return uploadPage.canFinish();
	}
}
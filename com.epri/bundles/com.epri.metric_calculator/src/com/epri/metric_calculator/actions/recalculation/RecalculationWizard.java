package com.epri.metric_calculator.actions.recalculation;

import org.eclipse.jface.wizard.Wizard;

public class RecalculationWizard extends Wizard {
	
	private RecalculationWizardPage recalculationPage;
	
	public RecalculationWizard() {
		setWindowTitle("Recalculation");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		recalculationPage = new RecalculationWizardPage();
		addPage(recalculationPage);
	}

	@Override
	public boolean performFinish() {
		recalculationPage.uploadData();
		return recalculationPage.canFinish();
	}
}

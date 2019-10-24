package com.epri.metric_calculator.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.epri.metric_calculator.views.metric.MetricView;
import com.epri.metric_calculator.views.metric.CalculationConsoleView;

public class MetricPerspective implements IPerspectiveFactory {

	public static final String ID = "com.epri.metric_calculator.perspectives.MetricPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(MetricView.ID, false, IPageLayout.TOP, 0.7f, editorArea);
		layout.addStandaloneView(CalculationConsoleView.ID, true, IPageLayout.BOTTOM, 0.3f, editorArea);

		layout.getViewLayout(CalculationConsoleView.ID).setCloseable(false);
	}
}

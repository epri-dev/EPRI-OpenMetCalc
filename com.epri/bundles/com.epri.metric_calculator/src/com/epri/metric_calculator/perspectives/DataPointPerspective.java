package com.epri.metric_calculator.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.epri.metric_calculator.views.datapoint.DataPointScopeView;
import com.epri.metric_calculator.views.datapoint.DataPointTableView;
import com.epri.metric_calculator.views.datapoint.ImportConsoleView;

public class DataPointPerspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.epri.metric_calculator.perspectives.DataPointPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(DataPointScopeView.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addStandaloneView(DataPointTableView.ID, false, IPageLayout.TOP, 0.7f, editorArea);
		layout.addStandaloneView(ImportConsoleView.ID, true, IPageLayout.BOTTOM, 0.3f, editorArea);

		layout.getViewLayout(ImportConsoleView.ID).setCloseable(false);
	}
}

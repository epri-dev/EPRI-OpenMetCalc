package com.epri.metric_calculator.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.epri.metric_calculator.views.dashboard.DashboardView;

/**
 * @author JoWookJae
 *
 */
public class DashboardPerspective implements IPerspectiveFactory {

	public static final String ID = "com.epri.metric_calculator.perspectives.DashboardPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);


		layout.addStandaloneView(DashboardView.ID, false, IPageLayout.TOP, 1.0f, editorArea);
	}

}

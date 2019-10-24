package com.epri.metric_calculator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.epri.metric_calculator.perspectives.DashboardPerspective;
import com.epri.metric_calculator.perspectives.DataPointPerspective;
import com.epri.metric_calculator.perspectives.MetricPerspective;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * @param configurer
	 */
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);

		// 전체 배경 color 변경
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setBackground(new Color(null, 255, 255, 255));

		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(
	 * org.eclipse.ui.application.IActionBarConfigurer)
	 */
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		configurer.setInitialSize(new Point(screenSize.width, screenSize.height));

		IPersistentPreferenceStore prefStore = (IPersistentPreferenceStore) PlatformUI.getPreferenceStore();

		StringBuilder perspectives = new StringBuilder();
		perspectives.append(DataPointPerspective.ID);
		perspectives.append(Const.COMMA_STR);
		perspectives.append(MetricPerspective.ID);
		perspectives.append(Const.COMMA_STR);
		perspectives.append(DashboardPerspective.ID);

		prefStore.setValue(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, perspectives.toString());
		try {
			prefStore.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

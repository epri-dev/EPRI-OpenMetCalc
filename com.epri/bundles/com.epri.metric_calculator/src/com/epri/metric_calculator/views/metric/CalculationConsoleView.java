package com.epri.metric_calculator.views.metric;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

import com.epri.metric_calculator.actions.recalculation.RecalculationAction;
import com.epri.metric_calculator.views.ConsoleView;

/**
 * The class of view for the log during calculating metrics
 * 
 * @author JoWookJae
 *
 */
public class CalculationConsoleView extends ConsoleView {

	public static final String ID = "com.epri.metric_calculator.views.metric.CalculationConsoleView"; //$NON-NLS-1$

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		// Init tool bar
		RecalculationAction reCalculateAction = new RecalculationAction();
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();

		ActionContributionItem recalcurateActionContributionItem = new ActionContributionItem(reCalculateAction);
		recalcurateActionContributionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(recalcurateActionContributionItem);
	}
}

package com.epri.metric_calculator.views.datapoint;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.epri.metric_calculator.actions.viewaction.datapoint.CleanDataAction;
import com.epri.metric_calculator.actions.viewaction.datapoint.ImportCsvAction;
import com.epri.metric_calculator.views.ConsoleView;

/**
 * View that display log during import
 * 
 * @author JoWookJae
 *
 */
public class ImportConsoleView extends ConsoleView {

	public static final String ID = "com.epri.metric_calculator.views.datapoint.ImportConsoleView"; //$NON-NLS-1$

	/**
	 * Get single instance of import console
	 * 
	 * @return
	 */
	public static ImportConsoleView getDefault() {
		return (ImportConsoleView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		// Initialize tool bar
		ImportCsvAction importAction = new ImportCsvAction();
		CleanDataAction cleanDataAction = new CleanDataAction();

		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();

		ActionContributionItem importItem = new ActionContributionItem(importAction);
		importItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(importItem);

		ActionContributionItem clearDataItem = new ActionContributionItem(cleanDataAction);
		clearDataItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(clearDataItem);
	}
}

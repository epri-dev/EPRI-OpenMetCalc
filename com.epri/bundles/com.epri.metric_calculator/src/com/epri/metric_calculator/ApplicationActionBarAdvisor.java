package com.epri.metric_calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.epri.metric_calculator.actions.export.ExportConfOfMetricAction;
import com.epri.metric_calculator.actions.export.ExportToExcelAction;
import com.epri.metric_calculator.actions.export.ImportConfOfMetricAction;
import com.epri.metric_calculator.actions.export.ImportReferenceValueAction;
import com.epri.metric_calculator.actions.file.CreateAction;
import com.epri.metric_calculator.actions.file.OpenAction;
import com.epri.metric_calculator.actions.file.SaveAction;
import com.epri.metric_calculator.actions.file.SaveAsAction;
import com.epri.metric_calculator.actions.metricshubsetting.MetricsHubSettingAction;
import com.epri.metric_calculator.actions.recalculation.RecalculationAction;
import com.epri.metric_calculator.actions.snapshot.AutoSnapshotAction;
import com.epri.metric_calculator.actions.snapshot.ManualSnapshotAction;
import com.epri.metric_calculator.actions.viewaction.datapoint.ImportCsvAction;

/**
 * @author JoWookJae
 *
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private static Separator SEPARATOR = new Separator();

	private Map<String, List<Object>> menuModel;

	private Map<Class<?>, IAction> actionMap;

	/**
	 * @param configurer
	 */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		actionMap = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		menuModel = new TreeMap<String, List<Object>>();
		menuModel = new LinkedHashMap<String, List<Object>>();

		// File category
		String catFile = Messages.ApplicationActionBarAdvisor_MENU_FILE;
		register(catFile, new CreateAction());
		register(catFile, new OpenAction());
		register(catFile, SEPARATOR);
		register(catFile, new SaveAction());
		register(catFile, new SaveAsAction());
		register(catFile, SEPARATOR);
		register(catFile, new MetricsHubSettingAction());
//		register(catFile, new SettingAction());
		IAction exitAction = ActionFactory.QUIT.create(window);
		exitAction.setImageDescriptor(ImageDescriptor.createFromImage(IconFactory.COMMON_EXIT));

		register(catFile, SEPARATOR);
		register(catFile, exitAction);

		// import/export category
		String catExport = Messages.ApplicationActionBarAdvisor_MENU_IMPORT_EXPORT;
		register(catExport, new ImportConfOfMetricAction());
		register(catExport, new ExportConfOfMetricAction());
		register(catExport, SEPARATOR);
		register(catExport, new ImportReferenceValueAction());

//		if (UserTypeChecker.isExpert()) {
//			register(catExport, SEPARATOR);
//			register(catExport, new GenerateKeyAction());
//			register(catExport, new ImportAction());
//			register(catExport, new ExportDataAction());
//			register(catExport, new DecryptExportedCsvAction());
//			register(catExport, new ExportEncryptedCsvAction());
//		}

		register(catExport, SEPARATOR);
		register(catExport, new ExportToExcelAction());

		// Run category
		String catRun = Messages.ApplicationActionBarAdvisor_MENU_RUN;
		register(catRun, new ImportCsvAction());
		register(catRun, new RecalculationAction());
		IAction collectAction = null;
		try {
			if (ToolSetting.getInstance().get(ToolSetting.PROP_PERIOD_OF_COLLECTING_DATA).isEmpty()) {
				// period of collecting data is null in tool setting, data collection operate
				// manually.
				collectAction = new ManualSnapshotAction();
			} else {
				collectAction = new AutoSnapshotAction();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		register(catRun, collectAction);

		// Metrics Hub
//		String catMetricsHub = Messages.ApplicationActionBarAdvisor_MENU_METRICS_HUB;
//		register(catMetricsHub, new MetricsHubUploadAction());
//		register(catMetricsHub, SEPARATOR);
		
		// about category
		String catAbout = Messages.ApplicationActionBarAdvisor_MENU_ABOUT;
		IAction aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setImageDescriptor(ImageDescriptor.createFromImage(IconFactory.COMMON_ABOUT));
		register(catAbout, aboutAction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface
	 * .action.IMenuManager)
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		for (Entry<String, List<Object>> entry : menuModel.entrySet()) {
			List<Object> items = entry.getValue();
			MenuManager menu = new MenuManager(entry.getKey());
			menuBar.add(menu);
			menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			for (Object item : items) {
				if (item instanceof IContributionItem) {
					menu.add((IContributionItem) item);
				} else if (item instanceof IAction) {
					menu.add((IAction) item);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface
	 * .action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		ToolBarManager toolbarManager = new ToolBarManager();
		for (Entry<String, List<Object>> entry : menuModel.entrySet()) {
			for (Object item : entry.getValue()) {
				if (item instanceof IAction) {
					toolbarManager.add((IAction) item);
				}
			}
			toolbarManager.add(SEPARATOR);
		}
		coolBar.add(new ToolBarContributionItem(toolbarManager, "main")); //$NON-NLS-1$
	}

	public IAction getAction(Class<?> clazz) {
		return actionMap.get(clazz);
	}
	
	/**
	 * Register action or separator into menu model.
	 * 
	 * @param groupName
	 * @param obj
	 */
	private void register(String groupName, Object obj) {
		if (obj instanceof IAction) {
			register((IAction) obj);
			actionMap.put(obj.getClass(), (IAction) obj);
		}

		List<Object> actions = menuModel.get(groupName);
		if (actions == null) {
			actions = new ArrayList<>();
			menuModel.put(groupName, actions);
		}

		actions.add(obj);
	}
}

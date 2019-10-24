package com.epri.metric_calculator;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.epri.metric_calculator.actions.file.UnsavedWarningDialog;
import com.epri.metric_calculator.actions.metricshubsetting.MetricsHubSettingWizardDialog;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.PerspectiveActivateEvent;
import com.epri.metric_calculator.metricmodel.synchronization.MetricModelSynchronizer;
import com.epri.metric_calculator.perspectives.DataPointPerspective;
import com.sst.sstat.metric.MetricDefinitionInvalidException;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 * 
 * @author JoWookJae
 *
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(
	 * org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId ()
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return DataPointPerspective.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#preStartup()
	 */
	@Override
	public void preStartup() {
		// Initialize setting.
		String settingFailMsg = null;
		try {
			ToolSetting.newInstance();
		} catch (VersionNotFoundException e) {
			settingFailMsg = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			settingFailMsg = "The setting file does not exist or can not be loaded.";
			e.printStackTrace();
		}

		if (settingFailMsg != null) {
			settingFailMsg += "\nThe program will terminate. Check the setting file and run it again.";
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Setting failure", settingFailMsg);
			Display.getDefault().asyncExec(() -> {
				System.exit(0);
			});
			return;
		}
		
		String metricsHubURL = ToolSetting.getInstance().getMetricsHubURL();
		if ( metricsHubURL == null || metricsHubURL.isEmpty() ) {
			// MetricsHubSettingAction
			MetricsHubSettingWizardDialog settingDialog = new MetricsHubSettingWizardDialog();
			settingDialog.open();
		}
		
		metricsHubURL = ToolSetting.getInstance().getMetricsHubURL();
		if ( metricsHubURL != null && !metricsHubURL.isEmpty() ) {
			String localModelVersion = ToolSetting.getInstance().getMetricModelVersion();
			MetricModelSynchronizer synchronizer = new MetricModelSynchronizer();
			String serverModelVersion = synchronizer.getMerticVersion();
			if ( !localModelVersion.equalsIgnoreCase(serverModelVersion) ) {
				synchronizer.getMetricModel();
				ToolSetting.getInstance().setMetricModelVersion(serverModelVersion);
				try {
					ToolSetting.getInstance().saveMetricsHubSetting();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Start file synchronizer.
		FileSynchronizer.getInstance().start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
	 */
	@Override
	public void postStartup() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String lastUsedProject = prefStore.getString(Const.PREFERENCE_STORE_LAST_USED_PROJECT);
		if (lastUsedProject != null && !lastUsedProject.isEmpty() && new File(lastUsedProject).exists()) {

			String title = "Question";
			String message = NLS.bind("Would you like to open last used project.({0})", lastUsedProject);

			boolean result = MessageDialog.openQuestion(Display.getDefault().getActiveShell(), title, message);
			if (result) {
				try {
					MetCalcProject.open(new File(lastUsedProject));
				} catch (ClassNotFoundException e1) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.OpenAction_ERROR_TITLE,
							Messages.OpenAction_ERROR_MSG_0 + lastUsedProject);
				} catch (IOException e1) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.OpenAction_ERROR_TITLE,
							Messages.OpenAction_ERROR_MSG_0 + lastUsedProject);
				} catch (VersionDifferentException e) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.OpenAction_ERROR_TITLE,
							e.getMessage());
				} catch (MetricDefinitionInvalidException e) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.OpenAction_ERROR_TITLE,
							e.getMessage());
				}
			}
		} else {
			NoProjectMessageDialog.open();
		}

		// 퍼스펙티브 변경 시 뷰를 refresh 할 수 있도록 리스너 추가
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			window.addPerspectiveListener(new PerspectiveAdapter() {
				@Override
				public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
					MetCalcEventManager.getInstance().fire(new PerspectiveActivateEvent(perspective));
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#preShutdown()
	 */
	@Override
	public boolean preShutdown() {
		// 현재 프로젝트 저장 유무 검사
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if (currentProject != null && currentProject.isDirty()) {
			int ret = UnsavedWarningDialog.open();
			// Cancel 버튼 또는 Ecs에 의한 종료
			if (ret == 2 || ret == -1) {
				return false;
			}
		}
		return true;
	}
}

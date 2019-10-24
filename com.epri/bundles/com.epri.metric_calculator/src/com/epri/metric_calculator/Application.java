package com.epri.metric_calculator;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 */
	public Object start(IApplicationContext context) {
		// Add bouncy castle to jdk security.
		Security.addProvider(new BouncyCastleProvider());

		Display display = PlatformUI.createDisplay();
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.KEY_CONFIGURATION_ID,
				"com.epri.metric_calculator.scheme");

		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}

			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	public static void setTitle(String title) {
		Display.getDefault().asyncExec(() -> {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText(title);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}

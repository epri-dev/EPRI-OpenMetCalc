package com.epri.metric_calculator.actions.file;

import java.io.File;
import java.io.IOException;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.actions.AbstractAction;

/**
 * Setting Action
 * 
 * @author JoWookJae
 *
 */
public class SettingAction extends AbstractAction {

	public SettingAction() {
		super("Setting", IconFactory.COMMON_SETTINGS); //$NON-NLS-1$
	}

	@Override
	public void run() {
		File settingFile = Const.getUserResource(Const.PATH_SETTING_FILE);
		try {
			// Displays the location of the settings file in the file explorer.
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("explorer.exe /select," + settingFile.getAbsolutePath()); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.file.SettingAction"; //$NON-NLS-1$
	}
}

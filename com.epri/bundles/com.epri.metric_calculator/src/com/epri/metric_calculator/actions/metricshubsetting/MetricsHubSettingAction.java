package com.epri.metric_calculator.actions.metricshubsetting;

import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.actions.AbstractAction;

public class MetricsHubSettingAction extends AbstractAction {

	public MetricsHubSettingAction() {
		super(Messages.MetricsHubSettingAction_0, IconFactory.COMMON_SETTINGS);
	}

	@Override
	public void run() {
		MetricsHubSettingWizardDialog settingDialog = new MetricsHubSettingWizardDialog();
		settingDialog.open();
	}
	
	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.metricshubsetting.MetricsHubSettingAction";
	}
}

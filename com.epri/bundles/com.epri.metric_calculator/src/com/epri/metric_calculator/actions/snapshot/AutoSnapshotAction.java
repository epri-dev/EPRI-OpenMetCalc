package com.epri.metric_calculator.actions.snapshot;

import java.io.IOException;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.event.AbMetCalcEventHandler;
import com.epri.metric_calculator.event.IMetCalcEventHandler;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.sst.sstat.metric.MetricType;

/**
 * Auto-Snapshot Action. It has On/Off Status.
 * 
 * @author JoWookJae
 *
 */
public class AutoSnapshotAction extends AbSnapshotAction {

	public AutoSnapshotAction() {
		// Check box style button
		super(AS_CHECK_BOX);

		// Create handler
		IMetCalcEventHandler eventHandler = new AbMetCalcEventHandler(getClass()) {

			@Override
			public void handle(MetCalcEvent event) {
				HistoricalDataSnapthot snapshot = new HistoricalDataSnapthot(MetCalcProject.getCurrent());
				try {
					// If auto-snapshot is not active, do not snapshot
					if (!isChecked()) {
						return;
					}

					// Remove previous data in the period
					snapshot.deletePrevDataInPeriod();
					// Snapshot strategic metrics
					snapshot.snapshot(
							MetCalcProject.getCurrent().getMetricManager().getMetricsByType(MetricType.STRATEGIC));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		// When project is saved, event handler works
		MetCalcEventManager.getInstance().register(MetCalcEventType.PROJECT_SAVED, eventHandler);

		// Set on/off
		if(Activator.getDefault().getPreferenceStore().contains(Const.PREFERENCE_AUTO_SNAPSHOT)) {
			setChecked(Activator.getDefault().getPreferenceStore().getBoolean(Const.PREFERENCE_AUTO_SNAPSHOT));
		} else {
			setChecked(true);
		}
	}

	@Override
	public void run() {
		// Save on/off status
		Activator.getDefault().getPreferenceStore().setValue(Const.PREFERENCE_AUTO_SNAPSHOT, isChecked());
	}
}
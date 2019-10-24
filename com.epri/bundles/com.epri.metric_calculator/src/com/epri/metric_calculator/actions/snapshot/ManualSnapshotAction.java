package com.epri.metric_calculator.actions.snapshot;

import java.io.IOException;

import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.sst.sstat.metric.MetricType;

/**
 * Action for snapshotting data manually
 * 
 * @author JoWookJae
 *
 */
public class ManualSnapshotAction extends AbSnapshotAction {

	public ManualSnapshotAction() {
		super(AS_PUSH_BUTTON);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		if(currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}
		
		HistoricalDataSnapthot snapshot = new HistoricalDataSnapthot(MetCalcProject.getCurrent());
		try {
			snapshot.snapshot(currentProject.getMetricManager().getMetricsByType(MetricType.STRATEGIC));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

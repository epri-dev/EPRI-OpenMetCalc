package com.epri.metric_calculator.views.metric;

import org.eclipse.jface.viewers.TreeNodeContentProvider;

import com.epri.metric_calculator.MetCalcProject;
import com.sst.sstat.metric.MetricRelationshipModel;

public class MetricTreeContentProvider extends TreeNodeContentProvider {

	public MetricTreeContentProvider() {
	}

	@Override
	public Object[] getElements(Object inputObject) {
		return MetCalcProject.getCurrent().getMetricManager().getTopLevelMetricRelationshipModel().toArray();
	}

	@Override
	public Object[] getChildren(Object parentObject) {
		MetricRelationshipModel parentModel = (MetricRelationshipModel) parentObject;
		return parentModel.getChildren().toArray();
	}

	@Override
	public Object getParent(Object object) {
		MetricRelationshipModel model = (MetricRelationshipModel) object;
		return model.getParent();
	}

	@Override
	public boolean hasChildren(Object object) {
		MetricRelationshipModel parentModel = (MetricRelationshipModel) object;
		return !parentModel.getChildren().isEmpty();
	}
}

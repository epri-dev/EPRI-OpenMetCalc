package com.epri.metric_calculator.views.metric.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

import com.epri.metric_calculator.Const;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricRelationshipModel;

public abstract class MetricTreeLabelProvider extends ColumnLabelProvider {

	protected String getText(Metric metric) {
		return "-";
	}

	protected String getToolTipText(Metric metric) {
		return null;
	}

	@Override
	public String getText(Object element) {
		MetricRelationshipModel model = (MetricRelationshipModel) element;

		String result = getText(model.getMetric());
		if (result == null || result.isEmpty()) {
			return "-";
		}

		return result;
	}

	@Override
	public Color getBackground(Object element) {
		MetricRelationshipModel model = (MetricRelationshipModel) element;
		Metric metric = model.getMetric();

		Color color = null;
		if(metric.isError()) {
			color = Const.COLOR_FAIL;
		} else {
			color = Const.COLOR_NONE;
		}

		return color;
	}

	@Override
	public String getToolTipText(Object element) {
		MetricRelationshipModel model = (MetricRelationshipModel) element;
		return getToolTipText(model.getMetric());
	}

	@Override
	public Color getToolTipBackgroundColor(Object object) {
		return new Color(null, 0, 0, 0);
	}

	@Override
	public Color getToolTipForegroundColor(Object object) {
		return new Color(null, 255, 255, 255);
	}
}
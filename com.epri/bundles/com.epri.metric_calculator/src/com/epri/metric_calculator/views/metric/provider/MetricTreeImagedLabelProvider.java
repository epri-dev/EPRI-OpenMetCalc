package com.epri.metric_calculator.views.metric.provider;

import org.eclipse.swt.graphics.Image;

import com.epri.metric_calculator.IconFactory;

public abstract class MetricTreeImagedLabelProvider extends MetricTreeLabelProvider {

	@Override	
	public Image getImage(Object element) {
		return IconFactory.METRIC_TREE_NODE;
	}
}
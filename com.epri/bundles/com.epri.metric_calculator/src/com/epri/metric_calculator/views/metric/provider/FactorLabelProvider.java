package com.epri.metric_calculator.views.metric.provider;

import java.util.List;

import org.eclipse.swt.graphics.Color;

import com.epri.metric_calculator.views.metric.MetricView;
import com.sst.sstat.metric.Factor;
import com.sst.sstat.metric.MetricRelationshipModel;

public abstract class FactorLabelProvider extends MetricTreeLabelProvider {

	/**
	 * The index of factor
	 */
	private int indexOfFactor;

	/**
	 * Constructor
	 * 
	 * @param indexOfFactor
	 */
	public FactorLabelProvider(int indexOfFactor) {
		this.indexOfFactor = indexOfFactor;
	}

	/**
	 * Return text for factor
	 * 
	 * @param factor
	 * @return
	 */
	protected abstract String getText(Factor factor);

	@Override
	public String getText(Object element) {
		MetricRelationshipModel model = (MetricRelationshipModel) element;
		List<Factor> factors = model.getMetric().getFactors();

		if (indexOfFactor + 1 > factors.size()) {
			return null;
		}

		return getText(factors.get(indexOfFactor));
	}

	@Override
	public Color getBackground(Object element) {
		Color color = super.getBackground(element);
		if (indexOfFactor % 2 == 0) {
			color = darken(color);
		}

		return color;
	}

	private Color darken(Color color) {
		float ratio = MetricView.CELL_BACKGROUND_DARK_RATIO;
		return new Color(null, (int) (color.getRed() * ratio), (int) (color.getGreen() * ratio),
				(int) (color.getBlue() * ratio));
	}
}
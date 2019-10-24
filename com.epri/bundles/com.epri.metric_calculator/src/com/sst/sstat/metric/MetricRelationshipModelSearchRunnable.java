package com.sst.sstat.metric;

/**
 * This can be used to find a specific metric.
 * 
 * @author JoWookJae
 *
 */
class MetricRelationshipModelSearchRunnable extends AbMetricTraceRunnable {
	/**
	 * Search result
	 */
	private MetricRelationshipModel result;

	private Metric targetMetric;

	public MetricRelationshipModelSearchRunnable(Metric targetMetric) {
		this.targetMetric = targetMetric;
	}

	@Override
	public boolean run(MetricRelationshipModel model) {
		if (model.getMetric() == targetMetric) {
			result = model;
			return false;
		}

		return true;
	}

	public MetricRelationshipModel getResult() {
		return result;
	}
}
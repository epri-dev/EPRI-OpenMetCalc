package com.sst.sstat.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epri.metric_calculator.MetCalcProject;
import com.sst.sstat.metric.Factor;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricRelationshipModel;

public class DataProvider implements IDataProvider {
	private Metric metric;

	private Map<String, List<Object>> dataPointMap = null;
	private Map<String, Metric> metrics = null;
	private List<Factor> factors = null;

	public DataProvider(Metric metric) {
		this.metric = metric;

		// initialize metric's factor list
		factors = metric.getFactors();

		// initialize metric value map
		metrics = new HashMap<>();
		for (Metric m : MetCalcProject.getCurrent().getMetricManager().getMetrics()) {
			metrics.put(m.getId(), m);
		}

		// initialize data point map
		Map<Class<?>, List<Object>> contents = MetCalcProject.getCurrent().getContents();
		dataPointMap = new HashMap<>();
		for (Class<?> datapointType : contents.keySet()) {
			List<Object> objectList = contents.get(datapointType);
			dataPointMap.put(datapointType.getSimpleName(), objectList);
		}
	}

	@Override
	public List<Object> getDataPointList(String datapointName) {
		return dataPointMap.get(datapointName);
	}

	@Override
	public Float getFactorValue(String factorName) {
		for (Factor factor : factors) {
			if (factor.getName().equals(factorName)) {
				return factor.getValue();
			}
		}

		return new Float(0);
	}

	@Override
	public Float getFactorValue(Integer index) {
		return factors.get(index).getValue();
	}

	@Override
	public Float getMetricValue(String metricId) {
		return metrics.get(metricId).getValue();
	}

	@Override
	public Float getWeight(String metricId) {
		MetricRelationshipModel metricRelationshipModel = MetCalcProject.getCurrent().getMetricManager()
				.getMetricRelationshipModel(this.metric);

		for (MetricRelationshipModel childModel : metricRelationshipModel.getChildren()) {
			if (childModel.getMetric().getId().equals(metricId)) {
				return childModel.getWeight();
			}
		}

		return 0F;
	}

	@Override
	public Float getReference() {
		return metric.getReferenceValue();
	}

	@Override
	public Float getTarget() {
		return metric.getTargetValue();
	}

	@Override
	public Float getReference(String metricId) {
		return metrics.get(metricId).getReferenceValue();
	}

	@Override
	public Float getTarget(String metricId) {
		return metrics.get(metricId).getTargetValue();
	}
}

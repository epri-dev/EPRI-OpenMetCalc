package com.sst.sstat.script;

import java.util.List;

public interface IDataProvider {

	public List<Object> getDataPointList(String datapointName);

	public Float getFactorValue(String factorName);

	public Float getFactorValue(Integer factorIndex);

	public Float getMetricValue(String metricId);

	public Float getWeight(String metricId);

	public Float getReference();

	public Float getTarget();

	public Float getReference(String metricId);

	public Float getTarget(String metricId);
}

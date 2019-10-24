package com.sst.sstat.metric;

import java.util.ArrayList;
import java.util.List;

public class MetricRelationshipModel {

	public static final float NO_WEIGHT_VALUE = Float.NaN;

	private Metric metric;
	private MetricRelationshipModel parent;
	private List<MetricRelationshipModel> children;
	private float weight;

	public MetricRelationshipModel(MetricRelationshipModel parent, Metric metric) {
		this(parent, metric, NO_WEIGHT_VALUE);
	}

	public MetricRelationshipModel(MetricRelationshipModel parent, Metric metric, float weight) {
		this.parent = parent;
		this.metric = metric;
		this.weight = weight;

		children = new ArrayList<>();
	}

	public MetricRelationshipModel(MetricRelationshipModel parent, Metric metric, String strWeight)
			throws NumberFormatException {
		this.parent = parent;
		this.metric = metric;
		setWeight(strWeight);

		children = new ArrayList<>();
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public MetricRelationshipModel getParentModel() {
		return parent;
	}

	public void setParentMetric(MetricRelationshipModel parentMetric) {
		this.parent = parentMetric;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setWeight(String strWeight) throws NumberFormatException {
		if (strWeight == null || strWeight.isEmpty()) {
			weight = NO_WEIGHT_VALUE;
		} else {
			try {
				weight = Float.valueOf(strWeight);
			} catch (NumberFormatException e) {
				throw e;
			}
		}
	}

	public MetricRelationshipModel getParent() {
		return parent;
	}

	public void setParent(MetricRelationshipModel parent) {
		this.parent = parent;
	}

	public List<MetricRelationshipModel> getChildren() {
		return children;
	}

	public void addChild(MetricRelationshipModel child) {
		children.add(child);
	}
	
	public void setChildren(List<MetricRelationshipModel> children) {
		this.children = children;
	}
}

package com.sst.sstat.script;

// Preprocessor.groovy
$PREPROCESSORS

class ScriptTemplate {

	def RESULT_FAIL = -2.0F;
	def RESULT_NONE = -1.0F;

	def dataProvider;

	def setDataProvider(IDataProvider _dataProvider){
		dataProvider = _dataProvider;
	}

	def getDataPointList(String datapointName) {
		return dataProvider.getDataPointList(datapointName);
	}

	def getFactorValue(String factorName) {
		return dataProvider.getFactorValue(factorName);
	}
	
	def getMetricValue(String metricId) {
		return dataProvider.getMetricValue(metricId);
	}

	def getFactorValue(int factorIndex) {
		return dataProvider.getFactorValue(factorIndex);
	}

	def getWeight(String metricId) {
		return dataProvider.getWeight(metricId);
	}

	def getReference(String metricId) {
		return dataProvider.getReference(metricId);
	}

	def getReference() {
		return dataProvider.getReference();
	}

	def getTarget() {
		return dataProvider.getTarget();
	}

	def getTarget(String metricId) {
		return dataProvider.getTarget(metricId);
	}

	// Formula.groovy
	$FORMULA_FUNCTIONS

}
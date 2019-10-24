package com.epri.metric_calculator.event;

public abstract class AbMetCalcEventHandler implements IMetCalcEventHandler {

	private Class<?> clazz;

	public AbMetCalcEventHandler(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public abstract void handle(MetCalcEvent event);

	public Class<?> getClazz() {
		return clazz;
	}
}

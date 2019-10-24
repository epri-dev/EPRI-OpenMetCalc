package com.epri.metric_calculator.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MetCalc Event Manager
 * 
 * @author JoWookJae
 *
 */
public class MetCalcEventManager {

	/**
	 * Single instance
	 */
	private static MetCalcEventManager INSTANCE;

	/**
	 * Event handlers map
	 */
	private Map<MetCalcEventType, List<IMetCalcEventHandler>> eventHandlerMap;

	/**
	 * Get single instance.
	 * 
	 * @return
	 */
	public static MetCalcEventManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MetCalcEventManager();
		}

		return INSTANCE;
	}

	private MetCalcEventManager() {
		eventHandlerMap = new HashMap<>();
	}

	/**
	 * Register event handler
	 * 
	 * @param type
	 * @param eventHandler
	 */
	public void register(MetCalcEventType type, IMetCalcEventHandler eventHandler) {
		if (eventHandlerMap.get(type) == null) {
			eventHandlerMap.put(type, new ArrayList<>());
		}

		eventHandlerMap.get(type).add(eventHandler);
	}

	/**
	 * Fire event
	 * 
	 * @param event
	 */
	public void fire(MetCalcEvent event) {
		List<IMetCalcEventHandler> handlers = eventHandlerMap.get(event.getType());

		// Handles events through all handlers.
		if (handlers != null) {
			for (IMetCalcEventHandler handler : handlers) {
				handler.handle(event);
			}
		}
	}
}

package com.epri.metric_calculator.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * It works lazily
 * 
 * @author JoWookJae
 *
 */
public class LazyWorker {
	/**
	 * running flag
	 */
	private boolean isRunning;

	/**
	 * 
	 */
	private Timer timer;

	/**
	 * The constructor.
	 * 
	 * @param runnable
	 */
	public LazyWorker() {
		timer = new Timer();
	}

	/**
	 * @param delay
	 */
	public void run(Runnable runnable, int delay) {
		if (!isRunning) {
			// When not running,
			isRunning = true;

			// Delay and change running flag.
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					runnable.run();
					isRunning = false;
				}
			}, delay);
		}
	}
}

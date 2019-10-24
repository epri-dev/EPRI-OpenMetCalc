package com.epri.metric_calculator.util;

/**
 * This class can create an asynchronous operation that can specify the result.
 * 
 * @author JoWookJae
 *
 * @param <T>
 */
public abstract class ResultantAsyncTask<T> {

	private Thread thread;

	/**
	 * The result of task
	 */
	private T result;

	/**
	 * Constructor
	 * 
	 * @param runnable
	 */
	public ResultantAsyncTask() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				result = runSync();
			}
		});
	}

	/**
	 * Start this task 
	 */
	public void start() {
		thread.start();
	}
	
	/**
	 * Tests if this task is stooped
	 * 
	 * @return
	 */
	public boolean isStopped() {
		return !thread.isAlive();
	}
	
	/**
	 * Return the result of task
	 * 
	 * @return
	 */
	public T getResult() {
		return result;
	}

	protected abstract T runSync();
}

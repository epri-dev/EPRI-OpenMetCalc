package com.epri.metric_calculator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

/**
 * File Synchronizer
 * 
 * @author JoWookJae
 *
 */
public class FileSynchronizer {

	/**
	 * Period of file checking.
	 */
	private static final int CHECK_PERIOD = 1000;

	/**
	 * Single instance
	 */
	private static FileSynchronizer INSTANCE;

	/**
	 * file handlers
	 */
	private Map<File, Runnable> fileHandlerMap;

	/**
	 * file revisions
	 */
	private Map<File, Long> fileRevisionMap;

	/**
	 * Return single instance.
	 * 
	 * @return
	 */
	public static FileSynchronizer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FileSynchronizer();
		}

		return INSTANCE;
	}

	/**
	 * Constructor
	 */
	private FileSynchronizer() {
		fileHandlerMap = new HashMap<>();
		fileRevisionMap = new HashMap<>();
	}

	/**
	 * Put file for checking.
	 * 
	 * @param file
	 * @param runnable
	 *            When file is modified, run this runnable.
	 */
	public void put(File file, Runnable runnable) {
		fileHandlerMap.put(file, runnable);
		fileRevisionMap.put(file, file.lastModified());
	}

	/**
	 * Start checking for files.
	 */
	public void start() {
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				while (true) {
					try {
						for (Entry<File, Runnable> entry : fileHandlerMap.entrySet()) {
							File file = entry.getKey();
							Runnable handler = entry.getValue();

							boolean isModified = Long.compare(file.lastModified(), fileRevisionMap.get(file)) != 0;
							if (isModified) {
								fileRevisionMap.put(file, file.lastModified());
								handler.run();
							}
						}

						Thread.sleep(CHECK_PERIOD);
					} catch (Throwable e) {
						// Do nothing...
					}
				}
			}
		}, 0);
	}

}

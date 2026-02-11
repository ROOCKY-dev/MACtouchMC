package com.roockydev.mactouchmc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger {

	public static void log(Level level, String message) {
		LogManager.getLogger().log(level, "[MCTouchBar] " + message);
	}
	public static void log(Level level, Object message) {
		LogManager.getLogger().log(level, "[MCTouchBar] " + message);
	}
}

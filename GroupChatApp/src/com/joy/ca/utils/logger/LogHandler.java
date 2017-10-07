package com.joy.ca.utils.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHandler {

	Logger _logger;

	public LogHandler(Class<?> clazz) {
		_logger = LogManager.getLogger(clazz);
	}

	public void trace() {
	}

	public void error(Throwable t) {
		_logger.error("Error : " + t.getMessage(), t);
	}

	public void info(String message) {
		_logger.info("Message : " + message);
	}

	public void debug(String message) {
		if (_logger.isDebugEnabled())
			_logger.debug("Message : " + message);
	}
}

package com.sst.sstat.parser;

import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseBoolExt implements CellProcessor {
	private static final String[] DEFAULT_TRUE_VALUES = new String[] { "1", "true", "t", "y", "yes" };
	private static final String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n", "no", "na", "n/a" };
	private ParseBool parseBool = null;;

	public ParseBoolExt() {
		parseBool = new ParseBool(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, true);
	}

	@SuppressWarnings("unchecked")
	public <T> T execute(Object value, CsvContext context) {
		return (T) parseBool.execute(value, context);
	}
}

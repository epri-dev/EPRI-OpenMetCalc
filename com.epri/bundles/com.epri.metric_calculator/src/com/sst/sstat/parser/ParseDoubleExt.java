package com.sst.sstat.parser;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseDoubleExt implements CellProcessor {
	private ParseDouble parseDouble = null;
	
	public ParseDoubleExt() {
		parseDouble = new ParseDouble();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T execute(Object value, CsvContext context) {
		if ( value == null ) {
			value = new String("-9999");
		}
		
		return (T)parseDouble.execute(value, context);
	}
}

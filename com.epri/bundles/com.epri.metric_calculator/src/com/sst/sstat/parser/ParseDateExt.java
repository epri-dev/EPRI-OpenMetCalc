package com.sst.sstat.parser;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseDateExt implements CellProcessor {
	private ParseDate parseDate = null;
	
	public ParseDateExt() {
		parseDate = new ParseDate("yyyy-MM-dd");
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T execute(Object value, CsvContext context) {
		if ( value == null ) {
			value = new String("1111-01-01");
		}
		
		return (T)parseDate.execute(value, context);
	}
}

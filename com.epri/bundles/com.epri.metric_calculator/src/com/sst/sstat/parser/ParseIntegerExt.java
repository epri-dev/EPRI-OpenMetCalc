package com.sst.sstat.parser;

import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class ParseIntegerExt implements CellProcessor {
	private ParseInt parseInt = null;;

	public  ParseIntegerExt(){
		parseInt = new ParseInt();
	}

	protected void validateInputNotNull(final Object value, final CsvContext context) {
		if( value == null ) {
			throw new SuperCsvCellProcessorException(
				"this processor does not accept null input - if the column is optional then chain an Optional() processor before this one",
				context, this);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);
		if( value instanceof String ) {
			String sValue = (String) value;
			if( sValue.equalsIgnoreCase("infinity") ){
				return (T) parseInt.execute("-2", context);
			}else if(sValue.equalsIgnoreCase("NA")){
				return (T) parseInt.execute("-1", context);
			}else if(sValue.equalsIgnoreCase("N/A")){
				return (T) parseInt.execute("-1", context);
			}
		}
		return (T) parseInt.execute(value, context);
	}
}

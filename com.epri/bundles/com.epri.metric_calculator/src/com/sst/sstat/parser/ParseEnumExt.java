package com.sst.sstat.parser;

import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class ParseEnumExt implements CellProcessor {
	private final Class<? extends Enum<?>> enumClass;

	private ParseEnum parseEnum = null;;

	public <T extends Enum<?>> ParseEnumExt(final Class<T> enumClass) {
		parseEnum = new ParseEnum(enumClass, true);
		this.enumClass = enumClass;
	}

	protected void validateInputNotNull(final Object value, final CsvContext context) {
		if (value == null) {
			throw new SuperCsvCellProcessorException(
					"this processor does not accept null input - if the column is optional then chain an Optional() processor before this one",
					context, this);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object execute(Object value, CsvContext context) {
//		validateInputNotNull(value, context);
		if ( value == null ) {
			value = new String("Null");
		}
		
		Character c = value.toString().toCharArray()[0];
		String inputString;
		if (Character.isDigit(c)) {
			inputString = "_"
					+ value.toString().trim().replace("/", "_").replace(".", "_").replace(" ", "_").replace("-", "_");
		} else {
			inputString = value.toString().trim().replace("/", "_").replace(".", "_").replace(" ", "_").replace("-", "_");
		}

		for (final Enum<?> enumConstant : enumClass.getEnumConstants()) {
			String constantName = enumConstant.name().toLowerCase();
			if (constantName.startsWith(inputString.toLowerCase())) {
				return enumConstant;
			}
		}
		// return enumClass.getEnumConstants()[0];
		return parseEnum.execute(value, context);
	}

}

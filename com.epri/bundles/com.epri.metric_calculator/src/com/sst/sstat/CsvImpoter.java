package com.sst.sstat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.epri.metric_calculator.Const;
import com.sst.sstat.parser.ParseBoolExt;
import com.sst.sstat.parser.ParseDateExt;
import com.sst.sstat.parser.ParseDoubleExt;
import com.sst.sstat.parser.ParseEnumExt;
import com.sst.sstat.parser.ParseIntegerExt;

public class CsvImpoter {

	/**
	 * An example of reading using CsvBeanReader.
	 */
	public static List<Object> readWithCsvBeanReader(String filename, PrintStream ps) throws Exception {

		ICsvBeanReader beanReader = null;
		List<Object> itemList = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);
			phase(ps, "Importing csv file: " + filename);
			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = beanReader.getHeader(true);
			if (header == null || header.length == 0) {
				error(ps, "Fail to read csv header row.");
				return null;
			}
			Class<?> clazz = null;
			clazz = getCVSMappedClassType(header, ps);
			if (clazz == null) {
				error(ps, "Fail to find mapped Datapoint");
				return null;
			}
			final CellProcessor[] processors = getProcessors(clazz, ps);
			if (processors == null) {
				error(ps, "Fail to build cell processor");
				return null;
			}
			itemList = new ArrayList<>();
			Object e = null;
			while ((e = beanReader.read(clazz, header, processors)) != null) {
				itemList.add(e);
			}

			phase(ps, itemList.size() + " row imported.");
			return itemList;
		} catch (SuperCsvCellProcessorException e) {
			error(ps, e.getLocalizedMessage());
			error(ps, e.getCsvContext().toString());
		} catch (FileNotFoundException e1) {
			error(ps, e1.getLocalizedMessage());
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		error(ps, "Fail to import csv file.");
		return null;
	}

	private static Class<?> getCVSMappedClassType(String[] header, PrintStream ps) {
		final Class<?>[] modelClasses = ModelProvider.getModelClasses();
		for (Class<?> ct : modelClasses) {
			Field[] fileds = ct.getDeclaredFields();
			if (header.length != fileds.length) {
				// field count not match
				continue;
			}
			if (!header[0].equals(fileds[0].getName())) {
				// field name is not match
				fileds[0].getAnnotations();
				// ps.println(Fileds[0].getName() + " not match");
				continue;
			}
			int i = 1;
			for (; i < header.length; i++) {
				if (!header[i].equals(fileds[i].getName())) {
					// field name is not match
					error(ps, header[i] + " not match with data point model " + fileds[i].getName());
					break;
				}
			}
			if (i == header.length) {
				return ct;
			}
		}
		// not found mapped class
		return null;
	}

	/**
	 * Sets up the processors used for the model
	 *
	 * @return the cell processors
	 */
	@SuppressWarnings("unchecked")
	private static CellProcessor[] getProcessors(Class<?> clazz, PrintStream ps1) {
		Field[] fields = clazz.getDeclaredFields();

		List<CellProcessor> processorList = new ArrayList<>();
		for (int i = 0; i < fields.length; i++) {
			Class<?> fType = fields[i].getType();

			if (Enum.class.isAssignableFrom(fType)) {
				@SuppressWarnings("rawtypes")
				Class enumClazz = null;
				try {
					enumClazz = Class.forName(fType.getTypeName());
				} catch (ClassNotFoundException e) {
					error(ps1, e.getLocalizedMessage());
					return null;
				}
				processorList.add(new ParseEnumExt(enumClazz));
			} else if (fType.getTypeName().contains("int")) {
				processorList.add(new ParseIntegerExt());
			} else if (fType.getTypeName().contains("long")) {
				processorList.add(new ParseLong());
			} else if (fType.getTypeName().contains("boolean")) {
				processorList.add(new ParseBoolExt());
			} else if (fType.getTypeName().contains("double")) {
				processorList.add(new ParseDoubleExt());
			} else if (fType.getTypeName().contains("Date")) {
				processorList.add(new ParseDateExt());
			} else {
				processorList.add(new NotNull());
			}
		}

		return processorList.toArray(new CellProcessor[fields.length]);
	}

	private static void phase(PrintStream ps, String message) {
		ps.println(Const.PREFIX_INFO + message);
	}

	private static void error(PrintStream ps, String message) {
		ps.println(Const.PREFIX_ERROR + message);
	}
}

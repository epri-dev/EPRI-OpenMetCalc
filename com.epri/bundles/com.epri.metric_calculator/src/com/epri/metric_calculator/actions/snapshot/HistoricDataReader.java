package com.epri.metric_calculator.actions.snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.epri.metric_calculator.Const;

/**
 * It can be used to read history data file.
 * 
 * @author JoWookJae
 *
 */
public class HistoricDataReader {

	private ICsvListReader reader;

	private File file;

	private String[] headers;

	private List<List<String>> rows;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	public HistoricDataReader(String filePath) throws FileNotFoundException {
		this(new File(filePath));
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public HistoricDataReader(File file) throws FileNotFoundException {
		this.file = file;
		reader = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
		rows = new ArrayList<>();
	}

	/**
	 * Return date be created
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Date getDate() throws ParseException {
		return Const.HISTORY_FILE_NAME_FORMAT.parse(file.getName());
	}

	/**
	 * Read csv file
	 * 
	 * @throws IOException
	 */
	public void read() throws IOException {
		try {
			headers = reader.getHeader(true);
			List<String> values = null;
			while ((values = reader.read()) != null) {
				rows.add(values);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * Return values by column index
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getValues(int index) {
		List<String> result = new ArrayList<>();
		for (List<String> row : rows) {
			result.add(row.get(index));
		}
		return result;
	}

	/**
	 * Return values by column name
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getValues(String name) {
		List<String> result = null;
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].equals(name)) {
				result = getValues(i);
			}
		}

		return result;
	}
}

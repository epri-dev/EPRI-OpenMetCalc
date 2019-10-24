package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.sst.sstat.model.Asset;
import com.sst.sstat.model.BoolEx;
import com.sst.sstat.model.BusinessUnit;
import com.sst.sstat.model.Database;
import com.sst.sstat.model.EmailServerFilter;
import com.sst.sstat.model.EndUserDevice;
import com.sst.sstat.model.Event;
import com.sst.sstat.model.Incident;
import com.sst.sstat.model.NetworkAccessPoint;
import com.sst.sstat.model.Personnel;
import com.sst.sstat.model.ThreadWarningAlert;
import com.sst.sstat.model.Vulnerability;
import com.sst.sstat.model.WebProxy;

public class DataPointSampleGenerator {

	private static final String PATH = "E:/metcalc_work/TD";

	public static void main(String[] args) throws IOException {
		generate(Asset.class, "AID", 100);
		generate(BusinessUnit.class, "BID", 100);
		generate(Database.class, "DID", 100);
		generate(EmailServerFilter.class, "MID", 100);
		generate(EndUserDevice.class, "UID", 100);
		generate(Event.class, "EID", 100);
		generate(Incident.class, "IID", 100);
		generate(NetworkAccessPoint.class, "NID", 100);
		generate(Personnel.class, "PID", 100);
		generate(ThreadWarningAlert.class, "TID", 100);
		generate(Vulnerability.class, "VID", 100);
		generate(WebProxy.class, "NWID", 100);
	}

	public static void generate(Class<?> modelClass, String idName, int countOfRaws) throws IOException {
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		ICsvListWriter writer = new CsvListWriter(new FileWriter(new File(dir, modelClass.getSimpleName() + ".csv")),
				CsvPreference.STANDARD_PREFERENCE);
		writer.writeHeader(getHeaders(modelClass));
		writer.writeHeader(getHeaders(modelClass));
		for (List<String> raw : getRaws(modelClass, idName, countOfRaws)) {
			writer.write(raw);
		}
		writer.close();
	}

	private static String[] getHeaders(Class<?> modelClass) {
		List<String> headers = new ArrayList<>();
		for (Field field : modelClass.getDeclaredFields()) {
			headers.add(field.getName());
		}
		return headers.toArray(new String[headers.size()]);
	}

	private static List<List<String>> getRaws(Class<?> modelClass, String idName, int count) {
		List<List<String>> raws = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			List<String> raw = new ArrayList<>();
			for (Field field : modelClass.getDeclaredFields()) {
				if (field.getName().equals(idName)) {
					raw.add(Integer.toString(i));
				} else {
					try {
						raw.add(generateRandomValue(field.getType()).toString());
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}

			}
			raws.add(raw);
		}
		return raws;
	}

	private static Object generateRandomValue(Class<?> type) {
		Random random = new Random();
		Object result = null;
		if (equal(type, int.class, Integer.class)) {
			// int, Integer
			result = random.nextInt(1000);
		} else if (equal(type, float.class, Float.class)) {
			// float, Float
			result = random.nextFloat();
		} else if (equal(type, boolean.class, Boolean.class)) {
			// boolean, Boolean
			result = random.nextBoolean();
		} else if (equal(type, BoolEx.class)) {
			// BoolEx
			int randomInt = random.nextInt(2);
			if (randomInt % 3 == 0) {
				result = BoolEx.Yes;
			} else if (randomInt % 3 == 1) {
				result = BoolEx.No;
			} else {
				result = BoolEx.Null;
			}
		} else if (equal(type, double.class, Double.class)) {
			// double, Double
			result = random.nextDouble();
		} else if (equal(type, long.class, Long.class)) {
			// long, Long
			result = random.nextLong();
		} else if (equal(type, String.class)) {
			// String
			result = randomString(random.nextInt(5) + 5);
		} else if (equal(type, Date.class)) {
			result = String.format("%s/%s/2018", random.nextInt(3) + 1, random.nextInt(27) + 1);
		} else {
			if (type.isEnum()) {
				int index = random.nextInt(2);
				try {
					result = type.getEnumConstants()[index];
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}

		}

		return result;
	}

	private static String randomString(int length) {
		Random random = new Random();
		StringBuilder randomString = new StringBuilder();
		for (int i = 0; i < length;) {
			int randomInt = random.nextInt(122);
			boolean isNumber = randomInt >= 48 && randomInt <= 57;
			boolean isLetter = randomInt >= 97 && randomInt <= 122;

			if (isNumber || isLetter) {
				randomString.append((char) randomInt);
				i++;
			}
		}
		return randomString.toString();
	}

	private static boolean equal(Class<?> type, Class<?>... classes) {
		for (Class<?> clazz : classes) {
			if (type.equals(clazz)) {
				return true;
			}
		}
		return false;
	}
}

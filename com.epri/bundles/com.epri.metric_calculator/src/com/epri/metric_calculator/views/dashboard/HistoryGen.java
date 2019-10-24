//package com.epri.metric_calculator.views.dashboard;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.supercsv.io.CsvListWriter;
//import org.supercsv.io.ICsvListWriter;
//import org.supercsv.prefs.CsvPreference;
//
//public class HistoryGen {
//
//	static float sps = randomGen(80);
//	static float sds = randomGen(120);
//	static float srs = randomGen(100);
//
//	public static void main(String[] args) throws IOException {
//		write("20180312(3) 000000");
//		write("20180313(3) 000000");
//		write("20180314(3) 000000");
//		write("20180315(3) 000000");
//		write("20180316(3) 000000");
//		
//		write("20180319(4) 000000");
//		write("20180320(4) 000000");
//		write("20180321(4) 000000");
//		write("20180322(4) 000000");
//		write("20180323(4) 000000");
//		
//		write("20180326(5) 000000");
//		write("20180327(5) 000000");
//		write("20180328(5) 000000");
//		write("20180329(5) 000000");
//		write("20180330(5) 000000");
//
//		write("20180402(1) 000000");
//		write("20180403(1) 000000");
//		write("20180404(1) 000000");
//		write("20180405(1) 000000");
//		write("20180406(1) 000000");
//
//		write("20180409(2) 000000");
//		write("20180410(2) 000000");
//		write("20180411(2) 000000");
//		write("20180412(2) 000000");
//		write("20180413(2) 000000");
//	}
//
//	private static void write(String label) throws IOException {
//		change();
//
//		Map<String, String> data = new HashMap<>();
//		data.put("S-PS", Float.toString(sps));
//		data.put("S-DS", Float.toString(sds));
//		data.put("S-RS", Float.toString(srs));
//		write(new FileWriter(label), data);
//	}
//
//	private static void change() {
//		sps = randomGen(sps);
//		sds = randomGen(sds);
//		srs = randomGen(srs);
//	}
//
//	private static float randomGen(float value) {
//		int sign = 1;
//
//		if (((int) (Math.random() * 10)) % 2 == 0) {
//			sign = -1;
//		}
//
//		return (float) (value + Math.random() * value * 0.3 * sign);
//	}
//
//	private static void write(Writer writer, Map<String, String> data) throws IOException {
//		ICsvListWriter csvWriter = null;
//		try {
//			csvWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);
//			List<String> headers = new ArrayList<>();
//			List<String> values = new ArrayList<>();
//
//			for (Entry<String, String> entry : data.entrySet()) {
//				headers.add(entry.getKey());
//				values.add(entry.getValue());
//			}
//
//			csvWriter.writeHeader(headers.toArray(new String[headers.size()]));
//			csvWriter.write(values);
//		} finally {
//			if (csvWriter != null) {
//				csvWriter.close();
//			}
//		}
//	}
//}

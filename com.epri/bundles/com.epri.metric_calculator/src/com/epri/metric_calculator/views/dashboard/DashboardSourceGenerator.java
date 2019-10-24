package com.epri.metric_calculator.views.dashboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.actions.snapshot.HistoricDataReader;
import com.google.common.io.CharStreams;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricManager;
import com.sst.sstat.metric.MetricRelationshipModel;
import com.sst.sstat.metric.MetricType;

import j2html.TagCreator;
import static j2html.TagCreator.*;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

/**
 * HTML Source Generator for Dashboard
 * 
 * @author JoWookJae
 *
 */
public class DashboardSourceGenerator {

	private static final String HTML_OPERATIONAL_METRICS_TABLE_ID = "oper_metric_table";

	private static final String HTML_IDENTIFIER_OPERATIONAL_METRICS = "$OPERATIONAL_METRICS";
	private static final String HTML_IDENTIFIER_S_PS_GAUGE = "$S_PS_GAUGE";
	private static final String HTML_IDENTIFIER_S_DS_GAUGE = "$S_DS_GAUGE";
	private static final String HTML_IDENTIFIER_S_RS_GAUGE = "$S_RS_GAUGE";
	private static final String HTML_IDENTIFIER_STRATEGIC_HISTORY = "$STRATEGIC_HISTORY";
	private static final String HTML_IDENTIFIER_TACTICAL_METRICS = "$TACTICAL_METRICS";

	private static final String INDEX_TEMPLATE_PATH = "index-template.html";

	private static final String FORMAT_GAUGE_CHART_PARAM = "name: %s, id: %s, value: %s, referenceValue: %s, targetValue: %s";
	private static final String FORMAT_LINE_CHART_PARAM = "labels:%s, ids:%s, datasets:%s";
	private static final String FORMAT_TACTICAL_METRICS = "ids : %s, names : %s, value : %s, referenceValueLines : %s, targetValueLines : %s, referenceValues : %s, targetValues : %s";

	private static final String[] COLORS = { "#f2643c", "#37f637", "#6e82b7" };

	private static final String ID_COMPARE_VALUE_HEADER_TEXT = "compareValueText";

	private static final String CLASS_NAME_REFER = "referenceValue";
	private static final String CLASS_NAME_TARGET = "targetValue";

	/**
	 * Return index page contents. (index.html)
	 * 
	 * @return
	 */
	public String createIndexPageContents() {
		String indexPageContents = null;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(Activator.openResourceStream(
					Const.PRODUCT_NAME + File.separator + Const.PATH_CHART + File.separator + INDEX_TEMPLATE_PATH));
			indexPageContents = CharStreams.toString(inputStreamReader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MetricManager metricMgr = MetCalcProject.getCurrent().getMetricManager();

		// Replace $OPERATIONAL_METRICS
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_OPERATIONAL_METRICS,
				createOperationalMetrics(metricMgr));

		// Replace $S_PS_GAUGE
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_S_PS_GAUGE,
				createGaugeChartParam(metricMgr.getMetric("S-PS")));

		// Replace $S_DS_GAUGE
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_S_DS_GAUGE,
				createGaugeChartParam(metricMgr.getMetric("S-DS")));

		// Replace $S_RS_GAUGE
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_S_RS_GAUGE,
				createGaugeChartParam(metricMgr.getMetric("S-RS")));

		// Replace $STRATEGIC_HISTORY
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_STRATEGIC_HISTORY,
				createStrategicHistoryChartParam(metricMgr));

		// Replace $TACTICAL_METRICS
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_TACTICAL_METRICS,
				createTacticalMetricsChartParam(metricMgr));

		// Replace $OPERATIONAL_METRICS
		indexPageContents = indexPageContents.replace(HTML_IDENTIFIER_OPERATIONAL_METRICS,
				createOperationalMetrics(metricMgr));

		return indexPageContents;
	}

	/**
	 * Create operational metrics chart.
	 * 
	 * @param metricMgr
	 * @return
	 */
	private String createOperationalMetrics(MetricManager metricMgr) {
		// All tr
		List<DomContent> trList = new ArrayList<>();

		List<DomContent> thList = new ArrayList<>();
		thList.add(td().withClass("header"));
		thList.add(td("Name").withClass("header"));
		thList.add(td("Value").withClass("header"));

		DomContent[] title = { TagCreator.span(TagCreator.text("Reference Value")).withId(ID_COMPARE_VALUE_HEADER_TEXT),
				TagCreator.br(),
				TagCreator.label().withClass("switch")
						.with(TagCreator.input().withType("checkbox").attr("onclick", "onClickHandler(this)"))
						.with(TagCreator.span().withClass("slider")) };
		thList.add(td(title).withClass("header").withId("compareValueTitle").withStyle("min-width: 75px;"));

		trList.add(tr(thList.toArray(new DomContent[thList.size()])));

		List<Metric> tacticalMetrics = metricMgr.getMetricsByType(MetricType.TACTICAL);
		for (Metric tacticalMetric : tacticalMetrics) {
			List<MetricRelationshipModel> operationalMetricModels = metricMgr.getMetricRelationshipModel(tacticalMetric)
					.getChildren();
			List<DomContent> tdList = new ArrayList<>();

			ContainerTag firstTd = td(tacticalMetric.getId()).withClass("header");
			firstTd.attr("rowspan", Integer.toString(operationalMetricModels.size()));
			firstTd.withStyle("text-align:center;");
			tdList.add(firstTd);
			for (MetricRelationshipModel operationalMetricModel : operationalMetricModels) {
				Metric operationalMetric = operationalMetricModel.getMetric();

				DomContent[] tooltipContents = {
						text(operationalMetric.getId() + "(" + operationalMetric.getName() + ")"), br() };

				int lineLength = 100;
				List<String> descriptionLines = new ArrayList<>();
				for (int i = 0; i * lineLength < operationalMetric.getDescription().length(); i++) {
					if (operationalMetric.getDescription().length() - (i + 1) * lineLength > 0) {
						descriptionLines.add(
								operationalMetric.getDescription().substring(i * lineLength, (i + 1) * lineLength));
					} else {
						descriptionLines.add(operationalMetric.getDescription().substring(i * lineLength,
								operationalMetric.getDescription().length() - 1));
					}
				}
				DomContent[] description = multiLineText(descriptionLines.toArray(new String[descriptionLines.size()]));
				tooltipContents = appendContents(tooltipContents, description);

				tdList.add(td(TagCreator.text(operationalMetric.getName()),
						TagCreator.span(tooltipContents).withClass("tooltiptext")));

				DomContent td = td(TagCreator.text(operationalMetric.getValueString())).attr("style",
						"text-align:center");
				tdList.add(td);

				// compare value
				td = createCompareValueTdTag(operationalMetric.getValue(), operationalMetric.getReferenceValue(),
						CLASS_NAME_REFER);
				tdList.add(td);
				td = createCompareValueTdTag(operationalMetric.getValue(), operationalMetric.getTargetValue(),
						CLASS_NAME_TARGET);
				tdList.add(td);

				trList.add(tr(tdList.toArray(new DomContent[tdList.size()])));
				tdList.clear();
			}
		}

		return table(trList.toArray(new DomContent[trList.size()])).withId(HTML_OPERATIONAL_METRICS_TABLE_ID).render();
	}

	private DomContent createCompareValueTdTag(float metricValue, float compareValue, String className) {
		ContainerTag result = td();

		DomContent value = TagCreator.text(Float.toString(compareValue));
		result.with(value);
		if (metricValue > compareValue) {
			result.with(TagCreator.img().withSrc("icons\\up_triangle.png"));
		} else if (metricValue < compareValue) {
			result.with(TagCreator.img().withSrc("icons\\down_triangle.png"));
		}

		result.withStyle("text-align:center");
		result.withClass(className);

		return result;
	}

	private String createGaugeChartParam(Metric metric) {
		String[] split = metric.getName().split(" ");
		StringBuilder name = new StringBuilder();
		name.append(split[0]);
		for (int i = 1; i < split.length; i++) {
			name.append("\\n");
			name.append(split[i]);
		}

		return String.format(FORMAT_GAUGE_CHART_PARAM, withQuotes(name.toString()), withQuotes(metric.getId()),
				metric.getValue(), metric.getReferenceValue(), metric.getTargetValue());
	}

	private String createStrategicHistoryChartParam(MetricManager metricMgr) {
		File historyDir = new File(MetCalcProject.getCurrent().getProjectDir(), Const.HISTORIC_DATA_STORE_DIRNAME);
		JSArray labels = new JSArray();
		List<File> requiredFiles = new ArrayList<>();

		if (historyDir.exists()) {
			for (File file : historyDir.listFiles()) {
				try {
					Date date = Const.HISTORY_FILE_NAME_FORMAT.parse(file.getName());
					labels.addString(
							new SimpleDateFormat(ToolSetting.getInstance().getHistoricChartDateFormat()).format(date));
					requiredFiles.add(file);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		boolean isDirty = MetCalcProject.getCurrent().isDirty();
		// If there are unsaved changes in the project, display the current value.
		if (isDirty) {
			labels.addString("Now");
		}

		Iterator<String> iterColors = Arrays.asList(COLORS).iterator();
		JSArray datasets = new JSArray();
		JSArray ids = new JSArray();
		for (Metric metric : metricMgr.getMetricsByType(MetricType.STRATEGIC)) {
			// Add id
			ids.addString(metric.getId());

			JSMap dataset = new JSMap();
			// Add labels
			dataset.put("label", metric.getName(), true);

			// Add colors
			String color = iterColors.next();
			dataset.put("backgroundColor", color, true);
			dataset.put("borderColor", color, true);
			dataset.put("fill", false);

			JSArray data = new JSArray();
			// Add data per file.
			for (File file : requiredFiles) {
				HistoricDataReader reader = null;
				try {
					reader = new HistoricDataReader(file);
					reader.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				data.addAll(reader.getValues(metric.getId()));
			}

			// If there are unsaved changes in the project, display the current value.
			if (isDirty) {
				data.add(metric.getValue());
			}

			dataset.put("data", data);
			datasets.add(dataset);
		}

		return createLineChartParam(labels, ids, datasets);
	}

	private String createLineChartParam(JSArray labels, JSArray ids, JSArray datasets) {
		return String.format(FORMAT_LINE_CHART_PARAM, labels, ids, datasets);
	}

	/**
	 * Create tactical metrics chart parameter
	 * 
	 * @param metricMgr
	 * @return
	 */
	private String createTacticalMetricsChartParam(MetricManager metricMgr) {
		JSArray ids = new JSArray();
		JSArray names = new JSArray();
		JSArray value = new JSArray();

		JSArray refValueLines = new JSArray();
		JSArray refValues = new JSArray();
		JSArray targetValueLines = new JSArray();
		JSArray targetValues = new JSArray();

		int index = 0;
		for (Metric metric : metricMgr.getMetricsByType(MetricType.TACTICAL)) {
			ids.addString(metric.getId());
			names.addString(metric.getName());
			value.add(metric.getValue());

			JSMap refValue1 = new JSMap();
			refValue1.put("xAxis", index - 0.5);
			refValue1.put("yAxis", metric.getReferenceValue());

			JSMap refValue2 = new JSMap();
			refValue2.put("xAxis", index + 0.5);
			refValue2.put("yAxis", metric.getReferenceValue());

			JSArray refValueSet = new JSArray();
			refValueSet.add(refValue1);
			refValueSet.add(refValue2);

			refValues.add(metric.getReferenceValue());
			refValueLines.add(refValueSet);

			JSMap targetValue1 = new JSMap();
			targetValue1.put("xAxis", index - 0.5);
			targetValue1.put("yAxis", metric.getTargetValue());

			JSMap targetValue2 = new JSMap();
			targetValue2.put("xAxis", index + 0.5);
			targetValue2.put("yAxis", metric.getTargetValue());

			JSArray targetValueSet = new JSArray();
			targetValueSet.add(targetValue1);
			targetValueSet.add(targetValue2);

			targetValues.add(metric.getTargetValue());
			targetValueLines.add(targetValueSet);
			index++;
		}

		return String.format(FORMAT_TACTICAL_METRICS, ids, names, value.toString(), refValueLines, targetValueLines,
				refValues, targetValues);
	}

	private DomContent[] multiLineText(String... lines) {
		List<DomContent> result = new ArrayList<>();

		result.add(TagCreator.text(lines[0]));
		for (int i = 1; i < lines.length; i++) {
			result.add(TagCreator.br());
			result.add(TagCreator.text(lines[i]));
		}

		return result.toArray(new DomContent[lines.length]);
	}

	private DomContent[] appendContents(DomContent[] contents1, DomContent... contents2) {
		DomContent[] result = new DomContent[contents1.length + contents2.length];
		for (int i = 0; i < contents1.length; i++) {
			result[i] = contents1[i];
		}
		for (int i = 0; i < contents2.length; i++) {
			result[contents1.length + i] = contents2[i];
		}
		return result;
	}

	private String withQuotes(String text) {
		StringBuilder result = new StringBuilder();
		result.append("\"");
		result.append(text);
		result.append("\"");
		return result.toString();
	}
}

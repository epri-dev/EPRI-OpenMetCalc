package com.sst.sstat.metric;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epri.metric_calculator.IconFactory;

/**
 * Metric Manager
 * 
 * @author JoWookJae
 *
 */
public class MetricManager {

	/**
	 * constants for xml tag name
	 */
	private static final String METRIC_CONFIG_ELENAME_METRIC_CONFIG = "metric_config";
	private static final String METRIC_CONFIG_ELENAME_METRIC_RELATIONSHIP = "metric_relationship";
	private static final String METRIC_CONFIG_ELENAME_METRICS = "metrics";
	private static final String METRIC_CONFIG_ELENAME_METRIC = "metric";
	private static final String METRIC_CONFIG_ELENAME_FACTOR = "factor";

	private static final String METRIC_CONFIG_ATTRNAME_ID = "id";
	private static final String METRIC_CONFIG_ATTRNAME_NAME = "name";
	private static final String METRIC_CONFIG_ATTRNAME_DESCRIPTION = "description";
	private static final String METRIC_CONFIG_ATTRNAME_VALUE = "value";
	private static final String METRIC_CONFIG_ATTRNAME_WEIGHT = "weight";
	private static final String METRIC_CONFIG_ATTRNAME_REFERENCE_VALUE = "reference_value";
	private static final String METRIC_CONFIG_ATTRNAME_TARGET_VALUE = "target_value";

	/**
	 * Top level relationship models
	 */
	private List<MetricRelationshipModel> topLevelRelationshipModels;

	/**
	 * All metrics
	 */
	private List<Metric> metrics;

	/**
	 * Metric configuration file.
	 */
	private File metricConfigFile;

	/**
	 * Dirty flag
	 */
	private boolean isDirty;

	/**
	 * Create a new instance
	 * 
	 * @param filePath
	 * @return
	 * @throws MetricDefinitionInvalidException
	 */
	public static MetricManager newInstance(String filePath) throws MetricDefinitionInvalidException {
		return new MetricManager(filePath);
	}

	/**
	 * Constructor. It load metric structures from the configuration file.
	 * 
	 * @param filePath
	 * @throws MetricDefinitionInvalidException
	 */
	private MetricManager(String filePath) throws MetricDefinitionInvalidException {
		metricConfigFile = new File(filePath);
		metrics = new ArrayList<>();

		loadMetricConfig();
	}

	/**
	 * Reload metric manager
	 * 
	 * @return
	 * @throws MetricDefinitionInvalidException
	 */
	private void loadMetricConfig() throws MetricDefinitionInvalidException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(getMetricConfigFile());

			Element eleRoot = doc.getDocumentElement();

			loadMetrics(getChildElement(eleRoot, METRIC_CONFIG_ELENAME_METRICS));
			topLevelRelationshipModels = loadRelationships(
					getChildElement(eleRoot, METRIC_CONFIG_ELENAME_METRIC_RELATIONSHIP), null);

		} catch (Throwable e) {
			String message = e.getMessage();

			if (message == null) {
				message = "Cannot load metric configuration file.";
			}

			throw new MetricDefinitionInvalidException(message);
		}
	}

	/**
	 * Load metrics from
	 * 
	 * @param eleMetrics
	 * @throws MetricDefinitionInvalidException
	 */
	private void loadMetrics(Element eleMetrics) throws MetricDefinitionInvalidException {
		for (Element element : getChildrenElements(eleMetrics, METRIC_CONFIG_ELENAME_METRIC)) {
			Metric metric = new Metric();
			metric.setId(element.getAttribute(METRIC_CONFIG_ATTRNAME_ID));
			metric.setName(element.getAttribute(METRIC_CONFIG_ATTRNAME_NAME));
			metric.setDescription(element.getAttribute(METRIC_CONFIG_ATTRNAME_DESCRIPTION));
			metric.setValue(element.getAttribute(METRIC_CONFIG_ATTRNAME_VALUE));
			metric.setReferenceValue(toFloat(element.getAttribute(METRIC_CONFIG_ATTRNAME_REFERENCE_VALUE)));
			metric.setTargetValue(toFloat(element.getAttribute(METRIC_CONFIG_ATTRNAME_TARGET_VALUE)));

			for (Element eleFactor : getChildrenElements(element, METRIC_CONFIG_ELENAME_FACTOR)) {
				Factor factor = new Factor();
				factor.setName(eleFactor.getAttribute(METRIC_CONFIG_ATTRNAME_NAME));
				factor.setDescription(eleFactor.getAttribute(METRIC_CONFIG_ATTRNAME_DESCRIPTION));
				factor.setValue(eleFactor.getAttribute(METRIC_CONFIG_ATTRNAME_VALUE));
				if (factor.getName().contains("-T-") || factor.getName().contains("-O-") ) {
					factor.setMetric(true);
				} else {
					factor.setMetric(false);
				}
				metric.addFactor(factor);
			}

			metrics.add(metric);
		}
	}

	private float toFloat(String str) {
		float result= 0;
		
		try {
			result = Float.parseFloat(str);
		} catch(NumberFormatException e) {
			// do nothing...
		}
		
		return result;
	}
	
	/**
	 * Load relationships among metrics
	 * 
	 * @param eleRelationship
	 * @param parentModel
	 * @return
	 * @throws MetricDefinitionInvalidException
	 */
	private List<MetricRelationshipModel> loadRelationships(Element eleRelationship,
			MetricRelationshipModel parentModel) throws MetricDefinitionInvalidException {
		List<MetricRelationshipModel> models = new ArrayList<>();

		// For each child element
		for (Element element : getChildrenElements(eleRelationship)) {
			Metric metric = getMetric(element.getTagName());

			// If metric is null, throw a exception
			if (metric == null) {
				throw new MetricDefinitionInvalidException(
						NLS.bind("There is no definition for {0}.", element.getTagName()));
			}

			// Create metric relationship model
			String weight = element.getAttribute(METRIC_CONFIG_ATTRNAME_WEIGHT);
			MetricRelationshipModel model = new MetricRelationshipModel(parentModel, metric, weight);
			models.add(model);

			// load relationships with sub-metrics
			loadRelationships(element, model);
		}

		if (parentModel != null) {
			parentModel.setChildren(models);
		}

		return models;
	}

	/**
	 * 
	 * @throws MetricSaveFailException
	 * @throws MetricDefinitionInvalidException
	 */
	public void save() throws MetricSaveFailException {
		saveAs(getMetricConfigFile());
	}

	public void save(boolean force) throws MetricSaveFailException {
		if (!force && !isDirty)
			return;

		save();
	}

	/**
	 * 
	 * @param file
	 * @throws MetricDefinitionInvalidException
	 */
	public void saveAs(File file) throws MetricSaveFailException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element eleMetricConfig = doc.createElement(METRIC_CONFIG_ELENAME_METRIC_CONFIG);
			doc.appendChild(eleMetricConfig);

			eleMetricConfig.appendChild(createMetricsElement(doc));
			eleMetricConfig.appendChild(createMetricRelationshipElement(doc));

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();

			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			trans.transform(source, result);
		} catch (ParserConfigurationException | TransformerException e) {
			throw new MetricSaveFailException(NLS.bind("Could not save metric.({0})", file.getAbsolutePath()), e);
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private Element createMetricsElement(Document doc) {
		Element eleMetrics = doc.createElement(METRIC_CONFIG_ELENAME_METRICS);

		for (Metric metric : metrics) {
			Element eleMetric = doc.createElement(METRIC_CONFIG_ELENAME_METRIC);

			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_ID, metric.getId());
			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_NAME, metric.getName());
			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_DESCRIPTION, metric.getDescription());
			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_VALUE, metric.getValueString());
			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_REFERENCE_VALUE, Float.toString(metric.getReferenceValue()));
			eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_TARGET_VALUE, Float.toString(metric.getTargetValue()));
			eleMetrics.appendChild(eleMetric);

			for (Factor factor : metric.getFactors()) {
				Element eleFactor = doc.createElement(METRIC_CONFIG_ELENAME_FACTOR);
				eleFactor.setAttribute(METRIC_CONFIG_ATTRNAME_NAME, factor.getName());
				eleFactor.setAttribute(METRIC_CONFIG_ATTRNAME_DESCRIPTION, factor.getDescription());
				eleFactor.setAttribute(METRIC_CONFIG_ATTRNAME_VALUE, factor.getValue().toString());
				eleMetric.appendChild(eleFactor);
			}
		}

		return eleMetrics;
	}

	/**
	 * Return created element for metric relationship
	 * 
	 * @param doc
	 * @return
	 */
	private Element createMetricRelationshipElement(Document doc) {
		Element eleRelationship = doc.createElement(METRIC_CONFIG_ELENAME_METRIC_RELATIONSHIP);

		for (MetricRelationshipModel model : topLevelRelationshipModels) {
			Element eleMetric = createMetricRelationshipElement(doc, model);
			eleRelationship.appendChild(eleMetric);
		}

		return eleRelationship;
	}

	/**
	 * Return created element for metric relationship
	 * 
	 * @param doc
	 * @param model
	 * @return
	 */
	private Element createMetricRelationshipElement(Document doc, MetricRelationshipModel model) {
		Element eleMetric = doc.createElement(model.getMetric().getId());
		eleMetric.setAttribute(METRIC_CONFIG_ATTRNAME_WEIGHT, Float.toString(model.getWeight()));

		for (MetricRelationshipModel childModel : model.getChildren()) {
			eleMetric.appendChild(createMetricRelationshipElement(doc, childModel));
		}

		return eleMetric;
	}

	/**
	 * Return metric setting file.
	 * 
	 * @return
	 */
	private File getMetricConfigFile() {
		return metricConfigFile;
	}

	/**
	 * 
	 * 
	 * @param name
	 * @return
	 * @throws MetricDefinitionInvalidException
	 */
	public Metric getMetric(String id) {
		for (Metric metric : metrics) {
			if (metric.getId().equals(id)) {
				return metric;
			}
		}

		return null;
	}

	/**
	 * Return the relationship model for metirc
	 * 
	 * @param metric
	 * @return
	 */
	public MetricRelationshipModel getMetricRelationshipModel(Metric metric) {
		MetricRelationshipModelSearchRunnable searchRunnable = new MetricRelationshipModelSearchRunnable(metric);
		trace(searchRunnable);
		return searchRunnable.getResult();
	}

	/**
	 * Return metrics by level
	 * 
	 * @return
	 */
	public Map<Integer, List<Metric>> getMetricsMapByLevel() {
		Map<Integer, List<Metric>> result = new HashMap<>();
		trace(new AbMetricTraceRunnable() {
			@Override
			public boolean run(MetricRelationshipModel model) {
				int level = getTraceLevel();

				if (!result.containsKey(level)) {
					result.put(level, new ArrayList<>());
				}

				result.get(level).add(model.getMetric());
				return true;
			}
		});
		return result;
	}

	/**
	 * Return metrics by specific level
	 * 
	 * @param level
	 * @return
	 */
	public List<Metric> getMetricsByLevel(int level) {
		List<Metric> result = new ArrayList<>();
		trace(new AbMetricTraceRunnable() {
			@Override
			public boolean run(MetricRelationshipModel model) {
				if (level == getTraceLevel()) {
					result.add(model.getMetric());
				}

				return true;
			}
		});
		return result;
	}

	/**
	 * Return metrics by type
	 * 
	 * @param type
	 * @return
	 */
	public List<Metric> getMetricsByType(MetricType type) {
		String suffix = null;
		switch (type) {
		case STRATEGIC:
			suffix = MetricType.ID_SUFFIX_STRATEGIC;
			break;
		case TACTICAL:
			suffix = MetricType.ID_SUFFIX_TACTICAL;
			break;
		case OPERATIONAL:
			suffix = MetricType.ID_SUFFIX_OPERATIONAL;
			break;
		default:
		}

		List<Metric> result = new ArrayList<>();
		for (Metric metric : getMetrics()) {
			if (metric.getId().startsWith(suffix)) {
				result.add(metric);
			}
		}

		return result;
		// return getMetricsByLevel(type.getValue());
	}

	/**
	 * Return sub-elements of parent node
	 * 
	 * @param parent
	 * @return
	 */
	private List<Element> getChildrenElements(Node parent) {
		NodeList childNodes = parent.getChildNodes();
		return getOnlyElements(childNodes);
	}

	private List<Element> getChildrenElements(Element parentElement, String childEleName) {
		return getOnlyElements(parentElement.getElementsByTagName(childEleName));
	}

	private Element getChildElement(Element parentElement, String childEleName) {
		List<Element> children = getChildrenElements(parentElement, childEleName);

		if (!children.isEmpty()) {
			return children.get(0);
		}

		return null;
	}

	/**
	 * Return only instance of Element
	 * 
	 * @param nodeList
	 * @return
	 */
	private List<Element> getOnlyElements(NodeList nodeList) {
		List<Element> result = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				result.add((Element) node);
			}
		}

		return result;
	}

	/**
	 * Return all metrics
	 * 
	 * @return
	 */
	public List<Metric> getMetrics() {
		return metrics;
	}
	
	public Map<String, Metric> getMetricsMap() {
		Map<String, Metric> metricMap = new HashMap<String, Metric>();
		for ( Metric metric : metrics ) {
			metricMap.put(metric.getId(), metric);
		}
		
		return metricMap;
	}

	public List<MetricRelationshipModel> getTopLevelMetricRelationshipModel() {
		return topLevelRelationshipModels;
	}

	public void trace(AbMetricTraceRunnable runnable) {
		for (MetricRelationshipModel model : topLevelRelationshipModels) {
			if (!trace(model, runnable)) {
				return;
			}
		}
	}

	public boolean trace(MetricRelationshipModel model, AbMetricTraceRunnable runnable) {
		// Run. If returned false, finish tracing
		if (!runnable.run(model)) {
			return false;
		}

		// Increase trace level
		runnable.setTraceLevel(runnable.getTraceLevel() + 1);

		// Run for children
		for (MetricRelationshipModel childModel : model.getChildren()) {
			if (!trace(childModel, runnable)) {
				return false;
			}
		}

		// Decrease trace level
		runnable.setTraceLevel(runnable.getTraceLevel() - 1);
		return true;
	}

	public Image getImage(MetricType type) {
		Image result = null;

		switch (type) {
		case STRATEGIC:
			result = IconFactory.METRIC_TREE_STRATEGIC;
			break;
		case TACTICAL:
			result = IconFactory.METRIC_TREE_TACTICAL;
			break;
		case OPERATIONAL:
			result = IconFactory.METRIC_TREE_OPERATIONAL;
			break;
		default:
		}

		return result;
	}
}
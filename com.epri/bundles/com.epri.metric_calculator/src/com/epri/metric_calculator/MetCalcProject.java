package com.epri.metric_calculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.epri.metric_calculator.actions.viewaction.datapoint.ImportDataPointJob;
import com.epri.metric_calculator.event.MetCalcEvent;
import com.epri.metric_calculator.event.MetCalcEventManager;
import com.epri.metric_calculator.event.MetCalcEventType;
import com.epri.metric_calculator.util.ReflectionUtil;
import com.epri.metric_calculator.util.ZipUtil;
import com.github.davidmoten.security.PPK;
import com.sst.sstat.ModelDescriptionLoadFailException;
import com.sst.sstat.ModelDescriptionLoader;
import com.sst.sstat.metric.AbMetricTraceRunnable;
import com.sst.sstat.metric.Factor;
import com.sst.sstat.metric.Metric;
import com.sst.sstat.metric.MetricDefinitionInvalidException;
import com.sst.sstat.metric.MetricManager;
import com.sst.sstat.metric.MetricRelationshipModel;
import com.sst.sstat.metric.MetricSaveFailException;
import com.sst.sstat.model.Asset;
import com.sst.sstat.model.Database;
import com.sst.sstat.model.EmailServerFilter;
import com.sst.sstat.model.EndUserDevice;
import com.sst.sstat.model.Event;
import com.sst.sstat.model.IDataPointScope;
import com.sst.sstat.model.Incident;
import com.sst.sstat.model.NetworkAccessPoint;
import com.sst.sstat.model.Personnel;
import com.sst.sstat.model.ThreadWarningAlert;
import com.sst.sstat.model.Vulnerability;
import com.sst.sstat.model.WebProxy;
import com.sst.sstat.model.WirelessNetworkAccess;

/**
 * The class for MetCalc project
 * 
 * @author JoWookJae
 *
 */
public class MetCalcProject implements Serializable, Cloneable {
	private static final long serialVersionUID = 5928603931912684121L;

	/**
	 * Extensions used when exporting text
	 */
	public static final String TEXT_EXTENSION = "txt"; //$NON-NLS-1$

	/**
	 * Array of model classes
	 */
	public static final Class<?>[] MODEL_CLASSES = { Asset.class, Database.class,
			EmailServerFilter.class, EndUserDevice.class, Event.class, Incident.class, NetworkAccessPoint.class,
			Personnel.class, Vulnerability.class, ThreadWarningAlert.class, WebProxy.class, WirelessNetworkAccess.class };

	/**
	 * Currently open projects
	 */
	private static MetCalcProject currentProject;

	/**
	 * Version of project
	 */
	private String version;

	/**
	 * Data point contents(key : model class, value : data list of model)
	 */
	private LinkedHashMap<Class<?>, List<Object>> contents;

	private transient MetricManager metricManager;

	/**
	 * Project file
	 */
	private transient File file;

	/**
	 * dirty flag
	 */
	private transient boolean isDirty;

	/**
	 * @return current project
	 */
	public static MetCalcProject getCurrent() {
		return currentProject;
	}

	/**
	 * Set current project to
	 */
	private static void setCurrent(MetCalcProject project) {
		if (project == null)
			return;

		currentProject = project;

		Activator.getDefault().getPreferenceStore().setValue(Const.PREFERENCE_STORE_LAST_USED_PROJECT,
				currentProject.getFile().getAbsolutePath());

		// Set program title text
		Application.setTitle(currentProject.makeTitle());

		// Fire project open event.
		MetCalcEventManager.getInstance().fire(new MetCalcEvent(MetCalcEventType.PROJECT_OPEN));
	}

	/**
	 * Create new MetCalc project
	 * 
	 * @param file
	 *            File location of the project to be created If failed to load
	 *            metric file
	 * @throws IOException
	 *             If failed to load metric file or save project file
	 * 
	 */
	public MetCalcProject(File file) throws MetricDefinitionInvalidException, IOException {
		this.version = ToolSetting.getInstance().getVersion();
		this.file = file;

		contents = new LinkedHashMap<Class<?>, List<Object>>();
		for (Class<?> modelClass : MODEL_CLASSES) {
			contents.put(modelClass, new ArrayList<Object>());
		}

		reloadMetricConfig();
	}

	public MetricManager getMetricManager() {
		return metricManager;
	}

	/**
	 * 
	 * 
	 * @param isDirty
	 */
	public void setDirty(boolean isDirty) {
		if (this.isDirty == isDirty)
			return;

		this.isDirty = isDirty;
		Application.setTitle(makeTitle());
	}

	/**
	 * Get dirty flag
	 * 
	 * @return if project is dirty, return true
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * @return project file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Append data point contents
	 * 
	 * @param key
	 *            model class of data point
	 * @param value
	 *            contents
	 */
	public void append(Class<?> key, List<Object> value) {
		Map<String, Object> map = convertToLinkedMap(contents.get(key));
		map.putAll(convertToLinkedMap(value));
		contents.get(key).clear();
		contents.get(key).addAll(new TreeMap<String, Object>(map).values());
	}

	/**
	 * This function is used temporarily for the identity uniqueness of the data
	 * point. To change to more systematic code, we have to implement it temporarily
	 * because of the increased number of tasks.
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, Object> convertToLinkedMap(List<Object> list) {
		@SuppressWarnings("serial")
		Map<String, Object> result = new LinkedHashMap<String, Object>() {
			@Override
			public Object put(String key, Object value) {
				for (String key2 : keySet()) {
					if (key2.compareTo(key) == 0) {
						replace(key2, value);
						return value;
					}
				}

				return super.put(key, value);
			}
		};

		for (Object o : list) {
			IDataPointScope dps = (IDataPointScope) o;
			result.put(dps.getId(), o);
		}

		return result;
	}

	public void append(Class<?> dpsType, Object dp) {
		List<Object> dpList = contents.get(dpsType);
		dpList.add(dp);
	}

	/**
	 * 
	 * 
	 * @param dpsType
	 * @param dp
	 * @return
	 */
	public int contains(Class<?> dpsType, IDataPointScope dps) {
		List<Object> dpList = contents.get(dpsType);
		for (int i = 0; i < dpList.size(); i++) {
			IDataPointScope dps1 = (IDataPointScope) dpList.get(i);
			if (dps.getId() == dps1.getId()) {
				return i;
			}
		}

		return -1;
	}

	public void replace(Class<?> dpsType, int index, Object dp) {
		List<Object> dpList = contents.get(dpsType);
		dpList.set(index, dp);
	}

	/**
	 * @return Data points
	 */
	public Set<Class<?>> keySet() {
		return contents.keySet();
	}

	/**
	 * Get data point contents
	 * 
	 * @param key
	 *            data point model class
	 * @return contents
	 */
	public List<Object> get(Class<?> key) {
		return contents.get(key);
	}

	/**
	 * @return All contents of data points
	 */
	public LinkedHashMap<Class<?>, List<Object>> getContents() {
		return contents;
	}

	/**
	 * @return Version of project
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Make program title
	 * 
	 * @return
	 */
	private String makeTitle() {
		String title = Messages.Project_PLATFORM_TITLE_PREFIX;

		if (isDirty()) {
			title += "*"; //$NON-NLS-1$
		}

		title += file.getName();
		title += "(" + file.getParent() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		return title;
	}

	/**
	 * Save project
	 * 
	 * @throws ProjectFileSaveFailException
	 *             If failed to write Project file
	 * @throws MetricSaveFailException
	 */
	public boolean save() throws ProjectFileSaveFailException, MetricSaveFailException {
		try {
			return save(false);
		} catch (IOException e) {
			throw new ProjectFileSaveFailException(file.getAbsolutePath());
		}
	}

	/**
	 * Save project
	 * 
	 * @param force
	 *            if true, save even if the dirty flag is false
	 * @throws IOException
	 *             If failed to write project file
	 * @throws MetricSaveFailException
	 */
	public boolean save(boolean force) throws IOException, MetricSaveFailException {
		if (!force && !isDirty)
			return false;

		// Instance -> Byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);

		// Byte array -> Base64 -> File
		Files.write(file.toPath(), Base64.getEncoder().encode(bos.toByteArray()));
		bos.close();
		oos.close();

		if (isDirty)
			setDirty(false);

		metricManager.save();
		return true;
	}

	/**
	 * Save as..
	 * 
	 * @param filePath
	 *            destination to save as
	 * @return Clone project from current project
	 * @throws ProjectFileSaveFailException
	 *             If failed to write project file
	 * @throws MetricSaveFailException
	 */
	public MetCalcProject saveAs(String filePath) throws ProjectFileSaveFailException, MetricSaveFailException {
		MetCalcProject newProject = null;
		try {
			newProject = (MetCalcProject) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		newProject.file = new File(filePath);
		try {
			newProject.save(true);
			open(newProject);
		} catch (IOException e) {
			throw new ProjectFileSaveFailException(filePath);
		}

		return newProject;
	}

	public static void open(MetCalcProject project) throws IOException {
		setCurrent(project);
	}

	/**
	 * @param file
	 *            source file to open(.project)
	 * 
	 * @throws IOException
	 *             If failed to read project file
	 * @throws ClassNotFoundException
	 *             If the project to open is created from another tool-version
	 * @throws VersionDifferentException
	 *             If the project you want to open is in a different version
	 * @throws MetricDefinitionInvalidException
	 * 
	 */
	public static void open(File file)
			throws IOException, ClassNotFoundException, VersionDifferentException, MetricDefinitionInvalidException {
		// File -> Base64
		byte[] bytes = Files.readAllBytes(file.toPath());
		// Base64 -> Byte array
		byte[] decoded = Base64.getDecoder().decode(bytes);
		// Byte array -> Instance

		ObjectInputStream ois = null;
		MetCalcProject project = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(decoded));
			project = (MetCalcProject) ois.readObject();
		} catch (IOException e) {
			throw e;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}

		// Version check
		if (!project.getVersion().equals(ToolSetting.getInstance().getVersion()))
			throw new VersionDifferentException(project.getVersion());

		project.file = file;
		project.reloadMetricConfig();

		setCurrent(project);
	}

	public boolean exportProject(String outputFile) throws Throwable {
		FileOutputStream fos = null;
		try {
			File publicKeyFile = new File(getProjectDir(), Const.PUBLIC_FILENAME);
			if (!publicKeyFile.exists()) {
				throw new Throwable(Messages.MetCalcProject_3);
			}

			File tempDir = Files.createTempDirectory(Long.toString(System.currentTimeMillis())).toFile();

			// When MetCalc exit, delete temporary directory.
			FileUtils.forceDeleteOnExit(tempDir);

			// Copy the files required by the project to temporary directory.
			FileUtils.copyDirectory(new File(getProjectDir()), tempDir, new FileFilter() {

				final String[] REQUIRED_FILE_ENDS_WITH = { Const.HISTORIC_DATA_STORE_DIRNAME,
						Const.HISTORIC_DATA_EXTENSION, Const.METCALC_PROJECT_EXTENSION, Const.METRIC_CONFIG_FILENAME,
						Const.DATA_POINT_LATEST_DIR, Const.DATA_POINT_LATEST_EXTENSION };

				@Override
				public boolean accept(File pathname) {
					for (String endsWith : REQUIRED_FILE_ENDS_WITH) {
						if (pathname.getName().endsWith(endsWith)) {
							return true;
						}
					}

					return false;
				}
			});

			// Compress project through ZIP
			File source = ZipUtil.zip(tempDir.getAbsolutePath());
			PPK.publicKey(publicKeyFile).encrypt(new FileInputStream(source), new FileOutputStream(outputFile));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

		return false;
	}

	/**
	 * Export metric to file
	 * 
	 * @param dest
	 *            destination to export metric
	 * @return true is success
	 * @throws MetricSaveFailException
	 * @throws MetricDefinitionInvalidException
	 */
	public void exportMetric(File dest) throws MetricSaveFailException {
		metricManager.saveAs(dest);
	}

	/**
	 * Save data point in project directory.
	 * 
	 * @throws IOException
	 */
	private void saveDataPoint() throws IOException {
		saveDataPoint(getDataPointDir());
	}

	/**
	 * Export data point
	 * 
	 * @param csvFilePath
	 * @throws IOException
	 */
	public void saveDataPoint(String path) throws IOException {
		File dataPointDir = new File(path);
		if (!dataPointDir.isDirectory()) {
			dataPointDir.mkdirs();
		}

		for (Entry<Class<?>, List<Object>> entry : contents.entrySet()) {
			Class<?> modelClass = entry.getKey();
			CsvBeanWriter writer = null;
			try {
				File file = new File(path,
						modelClass.getSimpleName() + "." + Const.DATA_POINT_LATEST_EXTENSION);
				writer = new CsvBeanWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE);

				List<String> names = new ArrayList<>();
				List<String> descriptions = new ArrayList<>();
				ModelDescriptionLoader loader = null;
				try {
					loader = ModelDescriptionLoader.getInstance();

					for (Field field : modelClass.getDeclaredFields()) {
						names.add(field.getName());
						descriptions.add(loader.get(field.getName()));
					}
				} catch (ModelDescriptionLoadFailException e) {
					e.printStackTrace();
				}

				String[] arrNames = names.toArray(new String[names.size()]);
				String[] arrDescriptions = descriptions.toArray(new String[descriptions.size()]);

				writer.writeHeader(arrNames);
				writer.writeHeader(arrDescriptions);

				for (Object obj : entry.getValue()) {
					writer.write(obj, arrNames);
				}
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
	}

	public void saveDataPointOnBack() {
		Job saveDataPointJob = new Job("Save data point") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Save data point", IProgressMonitor.UNKNOWN);
					saveDataPoint();
					return Status.OK_STATUS;
				} catch (IOException e) {
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();
				}
			}
		};

		saveDataPointJob.setUser(true);
		saveDataPointJob.schedule();
	}

	public String getDataPointDir() {
		return getProjectDir() + File.separator + Const.DATA_POINT_LATEST_DIR;
	}

	/**
	 * Import metric file
	 * 
	 * @param filePath
	 *            metric file path to import
	 * @throws MetricDefinitionInvalidException
	 */
	public void importMetric(String filePath) throws MetricDefinitionInvalidException {
		metricManager = MetricManager.newInstance(filePath);
		for (Metric metric : metricManager.getMetrics()) {
			metric.setValue(Metric.RESULT_NONE);
		}
		setDirty(true);
	}

	public void reloadDataPoint() {
		File dataPointDir = new File(file.getParent(), Const.DATA_POINT_LATEST_DIR);

		if (dataPointDir.isDirectory() && dataPointDir.list().length != 0) {
			ImportDataPointJob importDataPointJob = new ImportDataPointJob(dataPointDir.list());
			importDataPointJob.setUser(true);
			importDataPointJob.schedule();
		}
	}

	/**
	 * Reload metric configuration.
	 * 
	 * @throws MetricDefinitionInvalidException
	 * @throws IOException
	 *             if can not copy the metric config template file
	 */
	public void reloadMetricConfig() throws MetricDefinitionInvalidException, IOException {
		File metricConfigFile = new File(file.getParent(), Const.METRIC_CONFIG_FILENAME);

		if (!metricConfigFile.exists()) {
			copyMetricConfigTemplateTo(metricConfigFile);
		}

		metricManager = MetricManager.newInstance(metricConfigFile.getAbsolutePath());
	}

	/**
	 * Copy metric config template to the destination.
	 * 
	 * @param dest
	 * @throws IOException
	 */
	private void copyMetricConfigTemplateTo(File dest) throws IOException {
		FileInputStream fis = new FileInputStream(getMetricConfigTemplateFile());
		FileOutputStream fos = new FileOutputStream(dest);

		try {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} finally {
			fis.close();
			fos.close();
		}
	}

	/**
	 * Retrun metric config template file.
	 * 
	 * @return
	 */
	private File getMetricConfigTemplateFile() {
		File templateFile = Const.getUserResource(Const.METRIC_CONFIG_FILENAME);
		if (!templateFile.exists()) {
			// If file does not exist, copy from program config.
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = Activator.openResourceStream(Const.PRODUCT_NAME + File.separator + Const.METRIC_CONFIG_FILENAME);
				fos = new FileOutputStream(templateFile);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return templateFile;
	}

	/**
	 * @param sourceFile
	 * @param privateKey
	 * @return
	 * @throws IOException
	 */
	public static boolean importProject(File sourceFile) throws Throwable {
		// Private key file
		File privateKeyFile = new File(sourceFile.getParent(), Const.PRIVATE_FILENAME);
		if (!privateKeyFile.exists()) {
			throw new Exception(Messages.MetCalcProject_4);
		}

		// Temp file
		File tempFile = File.createTempFile("temp", sourceFile.getName());
		tempFile.deleteOnExit();
		try {
			// Encrypt
			PPK.privateKey(privateKeyFile).decrypt(new FileInputStream(sourceFile), new FileOutputStream(tempFile));
		} catch (RuntimeException e) {
			String message = NLS.bind(Messages.MetCalcProject_6, privateKeyFile.getAbsolutePath(),
					sourceFile.getAbsoluteFile());
			throw new Exception(message);
		}

		String withExt = sourceFile.getName();
		String name = withExt.substring(0, withExt.lastIndexOf('.'));
		File projectDir = new File(sourceFile.getParent(), name);

		if (projectDir.exists()) {
			int i = 0;
			do {
				projectDir = new File(sourceFile.getParent(), name + Messages.MetCalcProject_7 + i);
				i++;
			} while (projectDir.exists() && projectDir.list() != null);
		}

		// Unzip to project dir
		ZipUtil.unzip(new FileInputStream(tempFile), projectDir);

		// Search .project file
		File projectFile = null;
		for (File file : projectDir.listFiles()) {
			if (file.getName().endsWith(Const.METCALC_PROJECT_EXTENSION)) {
				projectFile = file;
				break;
			}
		}

		if (projectFile == null) {
			throw new Exception(Messages.MetCalcProject_9);
		}

		// Open project
		if (projectFile != null) {
			try {
				open(projectFile);
				return true;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Export MetCalc project to text
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void exportToText(String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath);
		writer.write(toString());
		writer.close();
	}

	public String getProjectDir() {
		return file.getParent();
	}

	/**
	 * Get only project name without extension
	 * 
	 * @return
	 */
	public String getProjectName() {
		String withExt = file.getName();
		int pos = withExt.lastIndexOf('.');
		if (pos > 0)
			return withExt.substring(0, pos);

		return withExt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String LINE_SEPERATOR = "\r\n"; //$NON-NLS-1$

		StringBuilder result = new StringBuilder();

		// Add factors of metric to string
		metricManager.trace(new AbMetricTraceRunnable() {

			@Override
			public boolean run(MetricRelationshipModel metricModel) {
				result.append("[" + metricModel.getMetric().getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				result.append(LINE_SEPERATOR);

				List<Factor> factors = metricModel.getMetric().getFactors();
				if (!factors.isEmpty()) {
					result.append(factors.get(0).getValue());
					for (int i = 1; i < factors.size(); i++) {
						result.append(", "); //$NON-NLS-1$
						result.append(factors.get(i).getValue());
					}
				}
				result.append(LINE_SEPERATOR);
				result.append(LINE_SEPERATOR);
				return true;
			}
		});

		// Add contents of data points to string
		for (Class<?> clazz : contents.keySet()) {
			result.append("["); //$NON-NLS-1$
			result.append(clazz.getSimpleName());
			result.append("]"); //$NON-NLS-1$
			result.append(LINE_SEPERATOR);

			Field[] fields = clazz.getDeclaredFields();
			result.append(fields[0].getName());
			for (int i = 1; i < fields.length; i++) {
				result.append(", "); //$NON-NLS-1$
				result.append(fields[i].getName());
			}
			result.append(LINE_SEPERATOR);

			for (Object obj : contents.get(clazz)) {
				result.append(fieldToText(obj, fields[0]));
				for (int i = 1; i < fields.length; i++) {
					result.append(", "); //$NON-NLS-1$
					result.append(fieldToText(obj, fields[i]));
				}
				result.append(LINE_SEPERATOR);
			}
			result.append(LINE_SEPERATOR);
		}

		return result.toString();
	}

	/**
	 * Get field value of object
	 * 
	 * @param obj
	 *            instance
	 * @param field
	 *            field of instance
	 * @return
	 */
	private String fieldToText(Object obj, Field field) {
		Object getValue = ReflectionUtil.invokeGetter(obj, field.getType(), field.getName());
		if (getValue == null) {
			return ""; //$NON-NLS-1$
		} else if (getValue instanceof Date) {
			return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format((Date) getValue);
		} else {
			return getValue.toString();
		}
	}
}

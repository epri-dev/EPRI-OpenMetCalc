package com.epri.metric_calculator.actions.recalculation.request;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.epri.metric_calculator.MetCalcProject;
import com.google.gson.Gson;
import com.sst.sstat.model.WirelessNetworkAccess;
import com.sst.sstat.model.WirelessNetworkAccessData;

public class RecalculationDataPointPost extends HttpPost {

	/**
	 * End-point path for uploading data
	 */
	public static final String END_POINT_PATH = "/api/calculate";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param selectedModels
	 * @throws MalformedURLException
	 */
	public RecalculationDataPointPost(URL baseUrl, List<Class<?>> selectedModels) throws MalformedURLException {
		super(new URL(baseUrl, END_POINT_PATH).toString());

		// set header
		setHeaders(makeHeaders());

		// set body
		setEntity(makeBody(selectedModels));
	}

	/**
	 * Make headers
	 * 
	 * @param accessToken
	 * @return
	 */
	private Header[] makeHeaders() {
		List<Header> headers = new ArrayList<>();

		// Content-Type : application/json
		headers.add(new BasicHeader("Content-Type", "application/json"));

		Header[] result = new Header[headers.size()];
		headers.toArray(result);
		
		return result;
	}

	/**
	 * Return body
	 * 
	 * @param selectedModels
	 * @return
	 */
	private HttpEntity makeBody(List<Class<?>> selectedModels) {
		// Put selected data points in map
		MetCalcProject currentProject = MetCalcProject.getCurrent();
		Map<String, List<Object>> dataPoints = new HashMap<>();
		for (Class<?> modelClass : selectedModels) {
			if (modelClass.getSimpleName().equals("WirelessNetworkAccess") ) {
				List<Object> replacedModels = new ArrayList<Object>();
				List<Object> models = currentProject.get(modelClass);
				
				for ( Object model : models ) {
					replacedModels.add(new WirelessNetworkAccessData((WirelessNetworkAccess) model));
				}
				dataPoints.put(modelClass.getSimpleName(), replacedModels);	
			} else {
				dataPoints.put(modelClass.getSimpleName(), currentProject.get(modelClass));	
			}
		}
		
		// jsonize
		String json = new Gson().toJson(dataPoints);
		HttpEntity result = null;
		
		try {
			result = new StringEntity(json);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

package com.epri.metric_calculator.metricmodel.request;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;

public class MetricModelVersionGet extends HttpGet {

	public static final String END_POINT_PATH = "/api/metricmodelversion";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @throws MalformedURLException
	 */
	public MetricModelVersionGet(URL baseUrl) throws MalformedURLException {
		super(new URL(baseUrl, END_POINT_PATH).toString());
	}
}

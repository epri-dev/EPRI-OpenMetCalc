package com.epri.metric_calculator.metricmodel.request;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;

public class MetricModelGet extends HttpGet {

	public static final String END_POINT_PATH = "/api/download/metricmodel";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @throws MalformedURLException
	 */
	public MetricModelGet(URL baseUrl) throws MalformedURLException {
		super(new URL(baseUrl, END_POINT_PATH).toString());
	}
}

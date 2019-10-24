package com.epri.metric_calculator.checkurl.request;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;

public class CheckURLGet extends HttpGet {

	public static final String END_POINT_PATH = "/api/checkurl?url=%s";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @throws MalformedURLException
	 */
	public CheckURLGet(URL baseUrl) throws MalformedURLException {
		super(new URL(baseUrl, String.format(END_POINT_PATH, baseUrl)).toString());
		
	}
}

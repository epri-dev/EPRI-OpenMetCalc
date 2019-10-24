package com.epri.metric_calculator.actions.recalculation.request;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;

public class RecalculationPost extends HttpPost {
	/**
	 * End-point path for authentication
	 */
	public static final String END_POINT_PATH = "/api/login";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @throws MalformedURLException
	 */
	public RecalculationPost(URL baseUrl) throws MalformedURLException {
		super(new URL(baseUrl, END_POINT_PATH).toString());
		setHeaders(makeHeaders());
	}

	/**
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
}

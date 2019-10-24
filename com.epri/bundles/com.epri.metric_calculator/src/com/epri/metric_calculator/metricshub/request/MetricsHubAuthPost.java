package com.epri.metric_calculator.metricshub.request;

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

import com.google.gson.Gson;

public class MetricsHubAuthPost extends HttpPost {

	/**
	 * End-point path for authentication
	 */
	public static final String END_POINT_PATH = "/api/login";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param userName
	 * @param password
	 * @throws MalformedURLException
	 */
	public MetricsHubAuthPost(URL baseUrl, String userName, String password) throws MalformedURLException {
		super(new URL(baseUrl, END_POINT_PATH).toString());

		setHeaders(makeHeaders());
		setEntity(makeBody(userName, password));
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

	/**
	 * @param userName
	 * @param password
	 * @return
	 */
	private HttpEntity makeBody(String userName, String password) {
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("username", userName);
		bodyMap.put("password", password);

		Gson gson = new Gson();
		String body = gson.toJson(bodyMap);

		HttpEntity result = null;
		try {
			result = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
}

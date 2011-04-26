package com.flomio.api.android;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

public class FlomioApiHelper {
	private enum RequestMethod {
		GET, POST, DELETE, PUT
	}
	private static class PathBuilder{
		private StringBuilder path = new StringBuilder();
		public PathBuilder addPart(String part) {
			String base = path.toString();
			if (part.startsWith("/")) {
				part = part.substring(1);
			}
			if (!base.endsWith("/") && base.length()>0){
				path.append("/");
			}
			path.append(part);

			return this;
		}
		public String toString() {
			return path.toString();
		}
	}

	private String user;
	private String password;
	private String baseUrl;
	private DefaultHttpClient client = new DefaultHttpClient();

	public FlomioApiHelper(String user, String password, String baseUrl) {
		this.user = user;
		this.password = password;
		this.baseUrl = baseUrl;
	}

	public void request(String path, String method, Map<String, String> params,
			IFlomioResponseHandler handler) {
		// create parameters

		Parameters parameters = new Parameters(params);
		// create HttpUriRequest
		HttpUriRequest request = makeRequest(path, method, parameters);
		// add authentication info

		// create new runnable
		FlomioRequestRunnable runnable = new FlomioRequestRunnable(client,
				request, handler);
		// start thread
		Thread thread = new Thread(runnable);

		thread.start();
	}

	private HttpUriRequest makeRequest(String path, String method,
			Parameters parameters) {
		RequestMethod requestMethod = RequestMethod.valueOf(method
				.toUpperCase());

		HttpUriRequest request = null;
		final String url = makeUrl(path);

		try {
			// make correct request
			switch (requestMethod) {
			case POST: {
				HttpPost post = new HttpPost(url);
				post.setEntity(parameters.getPostParams());

				request = post;
				break;
			}
			case GET: {
				HttpGet get = new HttpGet(url + parameters.getURLParams());

				request = get;
				break;
			}
			}
			;
		} catch (UnsupportedEncodingException e) {
			// do nothing
			e.printStackTrace();
		}
		String userpass = this.user + ":" + this.password;
		String encodeuserpass = new String(Base64.encodeBase64(userpass
				.getBytes()));

		request.setHeader(new BasicHeader("Authorization", "Basic "
				+ encodeuserpass));

		return request;
	}
	private String makeUrl(String path) {
		PathBuilder pathBuilder = new PathBuilder();
		return pathBuilder.addPart(baseUrl).addPart(user).addPart(path).toString();
	}
}

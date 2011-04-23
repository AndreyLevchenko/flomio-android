package com.flomio.api.android;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

public class FlomioRequestRunnable implements Runnable {
	private HttpClient client;
	private HttpUriRequest request;
	private IFlomioResponseHandler handler;

	public FlomioRequestRunnable(HttpClient client, HttpUriRequest request, IFlomioResponseHandler handler) {
		this.client = client;
		this.request = request;
		this.handler = handler;
	}

	@Override
	public void run() {
        try {
            BasicResponseHandler responseHandler = new BasicResponseHandler() {
            	public String handleResponse(HttpResponse response)
                        throws HttpResponseException, IOException {
            		
            		final int code = response.getStatusLine().getStatusCode();
            		final String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                	response.getEntity().consumeContent();
            		if (code > 400) {
            			handler.handleError(code, body);
            			return "";
            		}
                	handler.handleResponse(body);
                    return "";
                }
            };
            System.out.println(request.toString());
            
            client.execute(request, responseHandler);
        } catch (Exception e) {
        	handler.handleException(e);
        }

	}

}

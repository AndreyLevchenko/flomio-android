package com.flomio.api.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class Parameters {

	private Map<String, String> data;
	
	public Parameters (Map<String, String> data){
		this.data = data;
	}
	
	
	public void set(String key, String value) {
		data.put(key, value);
	}

	/**
	 * Internal package function to make native http parameters 
	 */
	String getURLParams() throws UnsupportedEncodingException {
		String result = "";
		
		Iterator<Entry<String, String>> iter = data.entrySet().iterator();
		
		while(iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			
			final String p = entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
			result += p;
			
			if (iter.hasNext()) {
				result += "&";
			}
		}
		if (result.length()>0) {
			result = "?" + result;
		}
		
		return result;
	}


	HttpEntity getPostParams() throws UnsupportedEncodingException {
        java.util.List<NameValuePair> nvps = new java.util.ArrayList <NameValuePair>();

        for(Entry<String, String> entry : data.entrySet()) {
        	nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        
		return new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
	}

}

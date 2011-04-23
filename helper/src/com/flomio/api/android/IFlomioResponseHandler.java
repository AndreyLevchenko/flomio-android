package com.flomio.api.android;

public interface IFlomioResponseHandler {
	public void handleResponse(String xml);
	public void handleError(int responseStatus, String responseText);
	public void handleException(Exception e);

}

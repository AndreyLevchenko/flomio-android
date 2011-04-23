package com.flomio.api.android.example;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.flomio.api.android.FlomioApiHelper;
import com.flomio.api.android.IFlomioResponseHandler;

public class HelperTest extends Activity {

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			TextView out = (TextView) findViewById(R.id.textView1);
			Bundle bundle = msg.getData();
			out.setText(bundle.getString(BUNDLE_KEY));
		}
	};

	private final String USER_NAME = "AndreyL";
	private final String USER_ID = "6";
	private final String PASSWORD = "flomioREST";
	private final String BASE_URL = "http://api.flomio.com/1.0/accounts/";
	private final String BUNDLE_KEY = "msg";

	private OnClickListener mRequestListener = new OnClickListener() {
		public void onClick(View v) {
			FlomioApiHelper helper = new FlomioApiHelper(USER_NAME, PASSWORD,
					BASE_URL + USER_ID);
			Map<String, String> params = new HashMap<String, String>();

//			params.put("uid", "333");
//			params.put("description", "andrl test A");

			helper.request("terminals/", "GET", params,
					new IFlomioResponseHandler() {
						private Message msg = handler.obtainMessage();

						@Override
						public void handleResponse(String xml) {
							sendMsg(xml);
						}

						@Override
						public void handleException(Exception e) {
							sendMsg("Exception: " + e.getMessage());
							e.printStackTrace();
						}

						@Override
						public void handleError(int responseStatus, String error) {
							sendMsg("Http error: " + responseStatus + "\n"
									+ error);
						}

						private void sendMsg(String text) {
							Bundle bundle = new Bundle();
							bundle.putString(BUNDLE_KEY, text);
							msg.setData(bundle);
							handler.sendMessage(msg);

						}
					});

		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.button1);

		button.setOnClickListener(mRequestListener);
		TextView out = (TextView) findViewById(R.id.textView1);

		out.setMovementMethod(new ScrollingMovementMethod());
	}
}
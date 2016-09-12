package at.jku.stockticker.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RESTfulHttpHandler {
	
	private static RESTfulHttpHandler _instance;
	
	private DefaultHttpClient client;
	private MyTask task;
	
	private RESTfulHttpHandler() {
		this.client = new DefaultHttpClient();
	}
	
	public static RESTfulHttpHandler getInstance() {
		if(_instance == null)
			_instance = new RESTfulHttpHandler();
		_instance.refresh();
		return _instance;
	}
	
	private void refresh() {
		this.task = new MyTask();
	}
	
	public AsyncTask<HttpUriRequest, Void, HttpResponse> request(HttpUriRequest...params) {
		return this.task.execute(params);
	}
	
	private class MyTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(HttpUriRequest... params) {
			HttpUriRequest req = params[0];
			HttpResponse res = null;

			try {
				res = RESTfulHttpHandler.this.client.execute(req);
			} catch (Exception e) {
				Log.e(this.getClass().toString(), e.getLocalizedMessage());
			}
			return res;
		}
	}
}
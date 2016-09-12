package at.jku.stockticker.services;

import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

public abstract class AbstractService {

	protected JsonReader reader = null;

	protected void initializeReader(String url) throws Exception {
		RESTfulHttpHandler handler = RESTfulHttpHandler.getInstance();
		HttpGet get = new HttpGet(url);
		
		AsyncTask<HttpUriRequest, Void, HttpResponse> exec = handler.request(get);
		HttpResponse res = exec.get();
		Log.i("HTTP", url + "\n" +  res.getStatusLine());
		
		this.reader = new JsonReader(new InputStreamReader(res.getEntity().getContent()));
	}
}

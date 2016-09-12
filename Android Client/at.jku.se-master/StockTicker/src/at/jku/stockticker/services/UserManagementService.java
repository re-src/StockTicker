package at.jku.stockticker.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class UserManagementService {
	private static final String LOGIN_URL = "http://10.0.2.2:1337/login";
	private static final String KEY_NAME = "username";
	private static final String KEY_PWD = "password";

	public String signUp(String name, String password) throws Exception {
		HttpPost post = new HttpPost(LOGIN_URL);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair(KEY_NAME, name));
		nameValuePairs.add(new BasicNameValuePair(KEY_PWD, password));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		RESTfulHttpHandler handler = RESTfulHttpHandler.getInstance();
		AsyncTask<HttpUriRequest, Void, HttpResponse> task = handler.request(post);
		HttpResponse res = task.get();

		if (res.getStatusLine().getStatusCode() == 201) {
			return "User angelegt";
		}
		return Util.toString(res);
	}

	public String signIn(String name, String password) throws Exception {
		String url = LOGIN_URL + '?' + KEY_NAME + "=" + name + "&" + KEY_PWD
				+ "=" + password;
		HttpGet get = new HttpGet(url);

		RESTfulHttpHandler handler = RESTfulHttpHandler.getInstance();
		AsyncTask<HttpUriRequest, Void, HttpResponse> task = handler.request(get);
		HttpResponse res = task.get();
		
		if (res.getStatusLine().getStatusCode() == 200) {
			return "200";
		}
		return Util.toString(res);
	}

}

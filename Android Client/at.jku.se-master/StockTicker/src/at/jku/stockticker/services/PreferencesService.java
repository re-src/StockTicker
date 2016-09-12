package at.jku.stockticker.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import at.jku.stockticker.pojo.Stock;

public class PreferencesService extends AbstractService implements RetrieveDataService<Stock>, SendDataService<Stock> {
	private static String URL = "http://10.0.2.2:1337/preferences";
	
	private static final String TAG_ID = "optionid";
	
	@Override
	public List<Stock> retrieve(Object... object) throws Exception {
		super.initializeReader(URL);
		return readStocks();
	}

	private List<Stock> readStocks() throws IOException {
		List<Stock> stocks = new ArrayList<Stock>();

		reader.beginArray();
		while (reader.hasNext()) {
			stocks.add(readStock());
		}
		reader.endArray();

		return stocks;
	}

	private Stock readStock() throws IOException {
		Stock s = new Stock();

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals(TAG_ID)) {
				s.setId(reader.nextString());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		return s;
	}

	
	public String send(List<Stock> stocks) throws Exception {
		RESTfulHttpHandler handler = RESTfulHttpHandler.getInstance();
		HttpPost post = new HttpPost(URL);
		
		JSONArray ids = new JSONArray();
		
		for(Stock stock : stocks) {
			JSONObject id = new JSONObject();
			id.put(TAG_ID, stock.getId());
			ids.put(id);
		}
		
		post.setEntity(new StringEntity(ids.toString()));
		post.setHeader("Content-type", "application/json");

		AsyncTask<HttpUriRequest, Void, HttpResponse> task = handler.request(post);
		
		HttpResponse res = task.get();
		Log.i("HTTP", URL + "\n" + res.getStatusLine());
		if(res.getStatusLine().getStatusCode() == 201) {
			return "Portfolio gespeichert";
		}
		
		return Util.toString(res);
	}

}

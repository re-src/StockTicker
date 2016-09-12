package at.jku.stockticker.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class Util {
	protected static String toString(HttpResponse res) throws IOException {
		HttpEntity entity = res.getEntity();
		BufferedReader content = new BufferedReader(new InputStreamReader(entity.getContent()));
		
		String line = "";
		String ret = "";
		
		while ((line = content.readLine()) != null) {
			ret += line;
		}
		return ret;
	}
}

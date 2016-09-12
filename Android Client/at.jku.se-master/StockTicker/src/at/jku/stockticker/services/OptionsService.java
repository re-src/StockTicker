package at.jku.stockticker.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.jku.stockticker.pojo.Stock;

public class OptionsService extends AbstractService implements RetrieveDataService<Stock> {

	private static final String URL = "http://10.0.2.2:1337/options";
	private static final String TAG_ID = "id";
	private	static final String TAG_NAME = "name";
	private static final String TAG_SYMBOL = "symbol";

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
			} else if (name.equals(TAG_NAME)) {
				s.setName(reader.nextString());
			} else if (name.equals(TAG_SYMBOL)) {
				s.setSymbol(reader.nextString());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		return s;
	}

}

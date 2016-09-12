package at.jku.stockticker.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.jku.stockticker.pojo.Price;
import at.jku.stockticker.pojo.Stock;

public class PriceService extends AbstractService implements RetrieveDataService<Price> {

	private static final String URL = "http://10.0.2.2:1337/prize";
	private static final String TAG_ID = "id";
	private static final String TAG_PRIZE = "price";
	private static final String TAG_TIME = "time";

	@Override
	/**
	 * @param Param1 - Stock object
	 * @param Param2 - Date object start
	 * @param Param3 - Date object end
	 */
	public List<Price> retrieve(Object... o) throws Exception {
		List<Price> prizes = new ArrayList<Price>();

		String url = URL;
		url += "/" + ((Stock)o[0]).getId() + "?start=" + ((Date)o[1]).getTime() + "&end=" + ((Date)o[2]).getTime();
		super.initializeReader(url);
		
		reader.beginArray();
		while (reader.hasNext()) {
			prizes.add(readStock());
		}
		reader.endArray();

		return prizes;
	}

	private Price readStock() throws IOException {
		Price p = new Price();

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals(TAG_ID)) {
				p.setId(reader.nextString());
			} else if (name.equals(TAG_PRIZE)) {
				p.setPrice(reader.nextDouble());
			} else if (name.equals(TAG_TIME)) {
				p.setTime(new Date(reader.nextLong()));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		return p;
	}

}

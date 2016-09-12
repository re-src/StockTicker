package at.jku.stockticker.services;

import java.util.List;

public interface SendDataService<T> {
	
	public String send(List<T> T) throws Exception ;

}

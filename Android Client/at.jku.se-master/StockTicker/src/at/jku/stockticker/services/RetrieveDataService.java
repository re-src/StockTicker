package at.jku.stockticker.services;

import java.util.List;

public interface RetrieveDataService<T> {
	public List<T> retrieve(Object...params) throws Exception;
}

package at.jku.stockticker.pojo;

public class Stock {
	
	private String id;
	private String name;
	private String symbol;
	
	public Stock() {
	
	}
	public Stock(String id, String name, String symbol) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String sign) {
		this.symbol = sign;
	}
	public String toString() {
		return name;
	}
	public boolean equals(Object o) {
		return ((Stock)o).id.equals(this.id);
	}
}

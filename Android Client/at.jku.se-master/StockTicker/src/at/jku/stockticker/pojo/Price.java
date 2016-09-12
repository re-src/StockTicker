package at.jku.stockticker.pojo;

import java.util.Date;

//[{"id":"AT0000603709","price":88.69,"time":1386102039207}]
public class Price {
	private String id;
	private double price;
	private Date time;
	
	public Price() {
	}
	
	public Price(String id, float price, Date time) {
		super();
		this.id = id;
		this.price = price;
		this.time = time;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}

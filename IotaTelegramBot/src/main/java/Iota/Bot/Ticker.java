package Iota.Bot;

public class Ticker {
	private String base;
	private String target;
	private String price;
	private String volume;
	private String change;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		if (target.equals("EUR")) target = "€";
		
		if (target.equals("USD")) target = "$";
		
		this.target = target;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price.substring(0, 5);
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		double changeDouble = Double.parseDouble(change);
		double changePercentDouble = changeDouble/1000;
		
		String changePercent = Double.toString(changePercentDouble);
		
		this.change = changePercent.substring(0,5);
	}
}

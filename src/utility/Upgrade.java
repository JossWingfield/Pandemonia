package utility;

public class Upgrade {
	
	private String name;
	private String description;
	
	public Upgrade(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String d) {
		this.description = d;
	}
	
}

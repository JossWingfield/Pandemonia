package utility;

import java.awt.image.BufferedImage;

import entity.buildings.TipJar;
import entity.buildings.Turntable;
import main.GamePanel;
import utility.ProgressManager.RewardType;
import map.WallPaper;

public class Upgrade {
	
	public enum Tier {
	    EARLY,
	    MID,
	    LATE
	}
	
	GamePanel gp;
	
	private String name;
	private String description;
	private Tier tier;
    private RewardType category;  
	private BufferedImage img;
	public int xOffset = 0, yOffset = 0;
	
	public Upgrade(GamePanel gp, String name, String description, BufferedImage image, Tier tier, RewardType category) {
		this.gp = gp;
		this.name = name;
	    this.tier = tier;
	    this.category = category;
	    this.description = description;
	    img = image;
	}
	public Upgrade(GamePanel gp, String name, String description, BufferedImage image, Tier tier, RewardType category, int xOffset, int yOffset) {
		this.gp = gp;
		this.name = name;
	    this.tier = tier;
	    this.category = category;
	    this.description = description;
	    this.xOffset = xOffset;
	    this.yOffset = yOffset;
	    img = image;
	}
	
	public void activateUpgrade() {
		switch(name) {
		case "Turntable":
			gp.customiser.addToInventory(new Turntable(gp, 0, 0));
			break;
		case "Tip Jar":
			gp.customiser.addToInventory(new TipJar(gp, 0, 0));
			break;
		case "Coloured Walls":
			for(int i = 21; i < 30; i++) {
				gp.catalogue.addToInventory(new WallPaper(gp, i));
			}
			break;
		}
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public BufferedImage getImage() {
		return img;
	}
	public Tier getTier() {
		return tier;
	}
	public RewardType getCategory() {
		return category;
	}
	
}

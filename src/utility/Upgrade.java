package utility;

import entity.buildings.FloorDecor_Building;
import entity.buildings.Shelf;
import entity.buildings.TipJar;
import entity.buildings.Turntable;
import entity.buildings.WallDecor_Building;
import main.GamePanel;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import map.WallPaper;
import utility.ProgressManager.RewardType;

public class Upgrade {
	
	GamePanel gp;
	
	private String name;
	private String description;
	private int tier;
    private RewardType category;  
	private TextureRegion img;
	public int xOffset = 0, yOffset = 0;
	
	public Upgrade(GamePanel gp, String name, String description, TextureRegion image, int tier, RewardType category) {
		this.gp = gp;
		this.name = name;
	    this.tier = tier;
	    this.category = category;
	    this.description = description;
	    img = image;
	}
	public Upgrade(GamePanel gp, String name, String description, TextureRegion image, int tier, RewardType category, int xOffset, int yOffset) {
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
			gp.world.customiser.addToInventory(new Turntable(gp, 0, 0));
			break;
		case "Tip Jar":
			gp.world.customiser.addToInventory(new TipJar(gp, 0, 0));
			break;
		case "Faster Customers":
			gp.world.progressM.fasterCustomers = true;
			break;
		case "More Customers":
			gp.world.progressM.moreCustomers = true;
			break;
		case "Coloured Walls":
			for(int i = 21; i < 30; i++) {
				gp.world.catalogue.addToInventory(new WallPaper(gp, i));
			}
			break;
		case "Small Plants":
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 25));

			for(int i = 70; i < 101; i++) {
				gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, i));
			}
			break;
		case "Big Plants":
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 0));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 1));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 2));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 3));
			
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 101));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 102));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 103));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 104));
			break;
		case "Flowers, Hanging Plants":
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 26));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 28));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 48));
			gp.world.catalogue.addToInventory(new FloorDecor_Building(gp, 0, 0, 49));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 4));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 5));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 23));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 24));
			break;
		case "Paintings":
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 2));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 3));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 28));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 29));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 30));
			gp.world.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 31));
			break;
		case "Shelves":
			gp.world.catalogue.addToInventory(new Shelf(gp, 0, 0));
			break;
		case "Fridge Upgrade I":
			gp.world.progressM.fridgeUpgradeI = true;
			break;
		case "Fridge Upgrade II":
			gp.world.progressM.fridgeUpgradeII = true;
			break;
		case "Sink Upgrade I":
			gp.world.progressM.sinkUpgradeI = true;
			break;
		case "Stove Upgrade I":
			gp.world.progressM.stoveUpgradeI = true;
			break;
		case "Chopping Upgrade I":
			gp.world.progressM.choppingBoardUpgradeI = true;
			break;
		case "Oven Upgrade I":
			gp.world.progressM.ovenUpgradeI = true;
			break;
		case "Seasoning!":
			gp.world.progressM.seasoningUnlocked = true;
			break;
		}
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public TextureRegion getImage() {
		return img;
	}
	public int getTier() {
		return tier;
	}
	public RewardType getCategory() {
		return category;
	}
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Upgrade other = (Upgrade) obj;
	    return name.equals(other.name);
	}

	
	public int hashCode() {
	    return name.hashCode();
	}
}

package utility;

import java.util.ArrayList;
import java.util.List;

import entity.buildings.Bed;
import entity.buildings.Bin;
import entity.buildings.Breaker;
import entity.buildings.Building;
import entity.buildings.Calendar;
import entity.buildings.Candle;
import entity.buildings.Cauldron;
import entity.buildings.Chair;
import entity.buildings.ChoppingBoard;
import entity.buildings.CornerTable;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Fridge;
import entity.buildings.Gate;
import entity.buildings.Lantern;
import entity.buildings.MenuSign;
import entity.buildings.Oven;
import entity.buildings.Sink;
import entity.buildings.SoulLantern;
import entity.buildings.StorageFridge;
import entity.buildings.Stove;
import entity.buildings.Table;
import entity.buildings.Table2;
import entity.buildings.Toilet;
import entity.buildings.ToiletDoor;
import entity.buildings.Trapdoor;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import entity.items.Food;
import main.GamePanel;
import utility.save.BuildingSaveData;

public class BuildingRegistry {
	
	GamePanel gp;
	
	List<String> presetBuildingNames;
	
	public BuildingRegistry(GamePanel gp) {
		this.gp = gp;
		
		presetBuildingNames = new ArrayList<>();
		presetBuildingNames.add("Chair 1");
		presetBuildingNames.add("Table Corner 1");
		presetBuildingNames.add("Food Store");
		presetBuildingNames.add("Toilet 1");
		presetBuildingNames.add("Toilet Door 1");
		presetBuildingNames.add("Trapdoor 1");
	}
	
	public List<BuildingSaveData> saveBuildings(List<Building> buildings) {
		
		List<BuildingSaveData> savedBuildings = new ArrayList<>();
		
		for(Building b: buildings) {
			if(b != null) {
				BuildingSaveData data = new BuildingSaveData();
				data.x = (int)b.hitbox.x;
				data.y = (int)b.hitbox.y;
				data.name = b.getName();
				if(isPresetBuilding(b)) {
					if(b instanceof Chair c) {
						data.preset = c.facing;
					} else if(b instanceof CornerTable c) {
						data.preset = c.presetNum;
					} else if(b instanceof FoodStore c) {
						data.preset = c.foodType;
					} else if(b instanceof Toilet c) {
						data.preset = c.facing;
					} else if(b instanceof ToiletDoor c) {
						data.preset = c.preset;
					} else if(b instanceof Trapdoor c) {
						data.preset = c.type;
					} else if(b instanceof FloorDecor_Building c) {
						data.preset = c.type;
						data.decorType = 0;
					}  else if(b instanceof WallDecor_Building c) {
						data.preset = c.type;
						data.decorType = 1;
					}
				} else if(b instanceof Door d) {
					data.preset = d.preset;
					data.attribute1 = d.facing;
					data.attribute2 = d.roomNum;
				} else if(b instanceof Fridge d) {
					data.fridgeType = 0;
					for(Food f: d.getContents()) {
						data.fridgeContents.add(f.getName());
						data.fridgeContentStates.add(f.getState());
					}
				} else if(b instanceof StorageFridge d) {
					data.fridgeType = 1;
					for(Food f: d.contents) {
						data.fridgeContents.add(f.getName());
						data.fridgeContentStates.add(f.getState());
					}
				}
				savedBuildings.add(data);
			}
		}
		
		return savedBuildings;
	}
	public List<Building> unpackSavedBuildings(List<BuildingSaveData> savedBuildings) {
		
		List<Building> buildings = new ArrayList<>();
		
		for(BuildingSaveData b: savedBuildings) {
			if(b.preset != -1) {
				if(b.decorType != -1) {
					if(b.decorType == 0) {
						Building build = new FloorDecor_Building(gp, b.x, b.y, b.preset);
						buildings.add(build);
					} else if(b.decorType == 1) {
						Building build = new WallDecor_Building(gp, b.x, b.y, b.preset);
						buildings.add(build);
					}
				} else {
					if(b.name.equals("Door 1")) {
						Door build = new Door(gp, b.x, b.y, b.attribute1, b.preset);
						build.setDoorNum(b.attribute2);
						buildings.add(build);
					} else {
						Building build = getBuildingFromName(b.name, b.x, b.y, b.preset);
						buildings.add(build);
					}
				}
			} else if(b.fridgeType != -1) {
				if(b.fridgeType == 0) {
					Fridge build = new Fridge(gp, b.x, b.y);
					build.setContents(b.fridgeContents, b.fridgeContentStates);
					buildings.add(build);
				} else if(b.fridgeType == 1) {
					StorageFridge build = new StorageFridge(gp, b.x, b.y);
					build.setContents(b.fridgeContents, b.fridgeContentStates);
					buildings.add(build);
				}
			} else {
				Building build = getBuildingFromName(b.name, b.x, b.y);
				buildings.add(build);
			}
		}
		
		return buildings;
	}
	public boolean isPresetBuilding(Building b) {
		String n = b.getName();
		if(presetBuildingNames.contains(n)) {
			return true;
		}
		if(b instanceof FloorDecor_Building || b instanceof WallDecor_Building) {
			return true;
		}
		return false;
	}
	public Building getBuildingFromName(String name, int x, int y) {
		Building i = null;
		switch(name) {
			case "Bed" -> i = new Bed(gp, x, y);
			case "Bin 1" -> i = new Bin(gp, x, y);
			case "Breaker" -> i = new Breaker(gp, x, y);
			case "Calendar" -> i = new Calendar(gp, x, y);
			case "Candle 1" -> i = new Candle(gp, x, y, 0);
			case "Candle 2" -> i = new Candle(gp, x, y, 1);
			case "Cauldron" -> i = new Cauldron(gp, x, y);
			case "Escape Hole" -> i = new EscapeHole(gp, x, y);
			case "Gate 1" -> i = new Gate(gp, x, y);
			case "Lantern" -> i = new Lantern(gp, x, y);
			case "Menu Sign" -> i = new MenuSign(gp, x, y);
			case "Oven" -> i = new Oven(gp, x, y);
			case "Kitchen Sink 1" -> i = new Sink(gp, x, y);
			case "Soul Lantern" -> i = new SoulLantern(gp, x, y);
			case "Stove" -> i = new Stove(gp, x, y);
			case "Table 1" -> i = new Table(gp, x, y);
			case "Table 2" -> i = new Table2(gp, x, y);
			case "Window" -> i = new Window(gp, x, y);
			case "Chopping Board 1" -> i = new ChoppingBoard(gp, x, y);
		}

		return i;
	}
	public Building getBuildingFromName(String name, int x, int y, int preset) {
		Building i = null;
		switch(name) {
			case "Chair 1" -> i = new Chair(gp, x, y, preset);
			case "Table Corner 1" -> i = new CornerTable(gp, x, y, preset);
			case "Food Store" -> i = new FoodStore(gp, x, y, preset);
			case "Toilet 1" -> i = new Toilet(gp, x, y, preset);
			case "Toilet Door 1" -> i = new ToiletDoor(gp, x, y, preset);
			case "Trapdoor 1" -> i = new Trapdoor(gp, x, y, preset);
		}

		return i;
	}
	public Building getBuildingFromName(String name) {
		return getBuildingFromName(name, 0, 0);
	}
	
	
}

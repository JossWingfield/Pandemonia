package map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import entity.buildings.ChefPortrait;
import entity.buildings.ChoppingBoard;
import entity.buildings.ClothesRail;
import entity.buildings.Computer;
import entity.buildings.CornerTable;
import entity.buildings.CursedDecor;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Freezer;
import entity.buildings.FreezerLight;
import entity.buildings.Fridge;
import entity.buildings.Gate;
import entity.buildings.Lantern;
import entity.buildings.Leak;
import entity.buildings.MenuBook;
import entity.buildings.MenuSign;
import entity.buildings.Oven;
import entity.buildings.RoomSpawn;
import entity.buildings.Rubble;
import entity.buildings.Sink;
import entity.buildings.SoulLantern;
import entity.buildings.SpiceTable;
import entity.buildings.Spill;
import entity.buildings.Stairs;
import entity.buildings.StorageFridge;
import entity.buildings.Stove;
import entity.buildings.Table;
import entity.buildings.TablePlate;
import entity.buildings.Toilet;
import entity.buildings.ToiletDoor;
import entity.buildings.Torch;
import entity.buildings.Trapdoor;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import entity.buildings.outdoor.OutdoorDecor;
import entity.buildings.outdoor.OutdoorWindow;
import entity.buildings.outdoor.SeasonalDecoration;
import entity.items.Item;
import entity.npc.Cook;
import entity.npc.Customer;
import entity.npc.GroupCustomer;
import entity.npc.NPC;
import entity.npc.Pet;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import utility.recipe.Recipe;
import utility.save.RoomSaveData;

public class Room {
	
	GamePanel gp;
	
	public int[][][] mapGrid; //The 3D array which stores the map
	public int mapWidth, mapHeight; //The width and height of the map
	public String filePath;
	private String roomID, roomIDTag, roomType;
	private Building[] buildings;
	private List<NPC> npcs = new ArrayList<>();
    private List<Item> items = new ArrayList<>(); 
    private List<LightSource> lights = new ArrayList<>(); 
	public int buildingArrayCounter = 0;
	//public boolean shouldUpdate = false;
	public int preset;
	public WallPaper wallpaper = null;
	public FloorPaper floorpaper = null;
	public Beam beam = null;
	public ChairSkin chairSkin = null;
	public CounterSkin counterSkin = null;
	public TableSkin tableSkin = null;
	public PanSkin panSkin = null;
	public DoorSkin doorSkin = null;
	
	public boolean canBeEdited = false;
	
	public boolean darkerRoom = false;
	
	public RoomSpawn roomSpawn;
	
	public Room(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		mapWidth = gp.tilesInWidth;
	    mapHeight = gp.tilesInHeight;
        mapGrid = new int[4][mapWidth][mapHeight];
	    setUpRoom(preset);
	}
	private void setUpRoom(int preset) {
		int phase = 1;
		if(gp.world.progressM != null) {
			phase = gp.world.progressM.currentPhase;
		}
		buildings = new Building[250];
		setTableSkin(0);
		setPanSkin(0);
		setDoorSkin(0);
		switch(preset) {
		case 0:
			roomType = "Main";
	        filePath = "/maps/main/phase" + Integer.toString(phase) + "/Room";
	        importMap(filePath, mapWidth, mapHeight);
	        roomID = "/main/phase" + Integer.toString(phase);
			roomIDTag = "Room";
			setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 1:
			roomType = "Stores";
	        filePath = "/maps/stores/phase" + Integer.toString(phase) + "/Room";
	        importMap(filePath, mapWidth, mapHeight);
	        roomID = "/stores/phase" + Integer.toString(phase);
			roomIDTag = "Room";
			setWallpaper(1);
			setFloorpaper(1);
			setBeam(1);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 2:
			roomType = "Outdoors";
			filePath = "/maps/outdoors/Layer";
			mapWidth = 150;
			mapHeight = 150;
			mapGrid = new int[4][mapWidth][mapHeight];
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/outdoors";
		    roomIDTag = "Layer";
		    setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			buildings = new Building[2000];
			break;
		case 3:
			roomType = "Electrics";
			filePath = "/maps/electrics/phase" + Integer.toString(phase) + "/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/electrics/phase" + Integer.toString(phase);
		    roomIDTag = "Layer";
		    setWallpaper(2);
			setFloorpaper(2);
			setBeam(2);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 4:
			roomType = "Toilets";
			filePath = "/maps/toilets/phase" + Integer.toString(phase) + "/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/toilets/phase" + Integer.toString(phase);
		    roomIDTag = "Layer";
		    setWallpaper(3);
			setFloorpaper(3);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 5:
			roomType = "Bedroom";
			filePath = "/maps/bedroom/phase" + Integer.toString(phase) + "/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/bedroom/phase" + Integer.toString(phase);
		    roomIDTag = "Layer";
		    setWallpaper(4);
			setFloorpaper(0);
			setBeam(4);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 6:
			roomType = "Basement";
			filePath = "/maps/basement/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/basement/phase" + Integer.toString(phase);
		    roomIDTag = "Layer";
		    setWallpaper(5);
			setFloorpaper(0);
			setBeam(4);
			setChairSkin(0);
			setCounterSkin(0);
			break;
		case 7:
			roomType = "Abandonded Corridor";
			filePath = "/maps/corridor2/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/corridor2";
		    roomIDTag = "Layer";
		    setWallpaper(1);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 8:
			roomType = "Corridor 1";
			filePath = "/maps/corridor1/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/corridor1";
		    roomIDTag = "Layer";
		    setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 9:
			roomType = "Old Kitchen";
			filePath = "/maps/oldkitchen/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/oldkitchen";
		    roomIDTag = "Layer";
		    setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 10:
			roomType = "Freezer";
			filePath = "/maps/freezer/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/freezer";
		    roomIDTag = "Layer";
		    setWallpaper(34);
			setFloorpaper(9);
			setBeam(7);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 11:
			roomType = "Downstairs";
			filePath = "/maps/downstairs/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/downstairs";
		    roomIDTag = "Layer";
		    setWallpaper(5);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 12:
			roomType = "Spice Cellar";
			filePath = "/maps/spicecellar/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/spicecellar";
		    roomIDTag = "Layer";
		    setWallpaper(35);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			canBeEdited = true;
			break;
		case 13:
			roomType = "Butcher";
			filePath = "/maps/butcher/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/butcher";
		    roomIDTag = "Layer";
		    setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			setChairSkin(0);
			setCounterSkin(0);
			break;
		}
		setBuildings();
		for(Building b: buildings) {
			if(b != null) {
				b.roomNum = preset;
			}
		}
	}
	public void setDefaultValues() {
		switch(preset) {
		case 7:
			if(!gp.world.progressM.unlockedKitchen) {
			    setWallpaper(1);
			    darkerRoom = true;
			    setFloorpaper(4);
			    setBeam(5);
			}
			break;
		case 6:
			darkerRoom = true;
			break;
		case 9:
			setFloorpaper(8);
			setWallpaper(1);
			break;
		}
		
	}
	
	public void setDestroyed() {
		if(preset == 0) {
			setCounterSkin(3);
			setWallpaper(30);
			setFloorpaper(12);
			setBeam(5);
			gp.world.buildingM.addSpill(0);
			gp.world.buildingM.addSpill(1);
			
			gp.world.buildingM.addBuilding(new Rubble(gp, 408, 288+48, 1));
			gp.world.buildingM.addBuilding(new Rubble(gp, 564, 300+48, 2));
			gp.world.buildingM.addBuilding(new Rubble(gp, 696, 324, 0));
			gp.world.buildingM.addBuilding(new Rubble(gp, 648, 432, 0));
			
			Lantern lantern = (Lantern)gp.world.buildingM.findBuildingWithName("Lantern");
			lantern.turnOff();
		} else if(preset == 9) {
			setCounterSkin(3);
			darkerRoom = true;
			//setWallpaper(30);
			//setFloorpaper(12);
			List<Building> torches = gp.world.buildingM.findBuildingsWithName("Lantern");
    	    for (Building b: torches) {
    	    	Lantern t = (Lantern)b;
    	    	t.turnOff();
    	    }
			Oven oven = (Oven)gp.world.buildingM.findBuildingWithName("Oven");
			oven.setDestroyed(true);
			Stove stove = (Stove)gp.world.buildingM.findBuildingWithName("Stove");
			stove.setDestroyed(true);
			
			setBeam(5);
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 696, 456, 25));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 492, 348, 24));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 552, 396, 22));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 612, 504, 21));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 756, 396, 16));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 492, 396, 14));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 336, 504, 20));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 588, 408, 18));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 708, 372, 19));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 636, 348, 44));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 384, 300, 45));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 372, 348, 26));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 540, 396, 33));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 372, 504, 34));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 684, 504, 35));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 756, 432, 32));
			gp.world.buildingM.addBuilding(new FloorDecor_Building(gp, 348, 312, 34));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 408, 348, 15));
			gp.world.buildingM.addBuilding(new CursedDecor(gp, 612, 348, 23));
		}
		gp.world.buildingM.refreshBuildings();
	}
	public void setRestored() {
		if(preset == 0) {
			setCounterSkin(0);
			setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			
			Lantern lantern = (Lantern)gp.world.buildingM.findBuildingWithName("Lantern");
			lantern.turnOn();
			gp.world.buildingM.refreshBuildings();
		} else if(preset == 9) {
			setBeam(0);
			setCounterSkin(0);
			darkerRoom = false;
			List<Building> torches = gp.world.buildingM.findBuildingsWithName("Lantern");
    	    for (Building b: torches) {
    	    	Lantern t = (Lantern)b;
    	    	t.turnOn();
    	    }
    		Oven oven = (Oven)gp.world.buildingM.findBuildingWithName("Oven");
			oven.setDestroyed(false);
			Stove stove = (Stove)gp.world.buildingM.findBuildingWithName("Stove");
			stove.setDestroyed(false);
    	    List<Building> mess = gp.world.buildingM.findBuildingsWithName("Mess");
    	    for (Building b: mess) {
    	    	gp.world.buildingM.removeBuilding(b);
    	    }
    		gp.world.buildingM.refreshBuildings();
		} else if(preset == 7) {
			setBeam(0);
			//setCounterSkin(0);
			darkerRoom = false;
    	    List<Building> mess = findBuildingsWithName("Mess");
    	    for (Building b: mess) {
    	    	removeBuilding(b);
    	    }
    	    
    	    addBuilding(new FloorDecor_Building(gp, 300, 384, 117));
	 	    addBuilding(new FloorDecor_Building(gp, 480, 336, 118));
	 	    addBuilding(new FloorDecor_Building(gp, 660+48, 336, 118));
	 	    addBuilding(new CursedDecor(gp, 804, 348, 31));
    	    List<Building> torches = findBuildingsWithName("Torch");
    	    for (Building b: torches) {
    	    	Torch t = (Torch)b;
    	    	t.turnOn();
    	    }
    	    
    	    List<Building> doors = findBuildingsWithName("Door 1");
    	    for (Building b: doors) {
    	    	Door d = (Door)b;
    	    	if(d.preset == 2) {

    	    	}
    	    }
		}
	}
	private void setBuildings() {
		int arrayCounter = 0;
		switch(preset) {
		case 0:
			roomSpawn = new RoomSpawn(gp, 480, 504);
			Door door = new Door(gp, 684, 144+48, 0, 0);
			door.setDoorNum(8);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 456, 504+48, 1, 0);
			door.setDoorNum(2);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 336, 344+48, 2, 0);
			door.setDoorNum(4);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 792, 420, 3, 0);
			door.setDoorNum(7);
			buildings[arrayCounter] = door;
			arrayCounter++;
			//Rubble barricade = new Rubble(gp, 732, 432+16, 3);
			//barricade.setBarricade();
			//buildings[arrayCounter] = barricade;
			//arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 384, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 312, 8);
			arrayCounter++;
			buildings[arrayCounter] = new StorageFridge(gp, 588, 204,true);
			arrayCounter++;
			buildings[arrayCounter] = new Stove(gp, 492, 252);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 252, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 252, 6);
			arrayCounter++;
			buildings[arrayCounter] = new CornerTable(gp, 612, 384, 3);
			arrayCounter++;
			buildings[arrayCounter] = new CornerTable(gp, 348, 252, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 312, 8);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 348, 468, "Down", false);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 264, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 252, 7);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 240, 17);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 384, 4);
			arrayCounter++;
			buildings[arrayCounter] = new ChoppingBoard(gp, 588, 372);
			arrayCounter++;
			buildings[arrayCounter] = new Gate(gp, 516, 384);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 468, 384, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 384, 384, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 480, 492, 19);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 756, 276, "Right", true);
			arrayCounter++;
			buildings[arrayCounter] = new Sink(gp, 348, 300);
			arrayCounter++;
			buildings[arrayCounter] = new MenuSign(gp, 636, 372);
			arrayCounter++;
			buildings[arrayCounter] = new Bin(gp, 384, 240, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 636, 144);
			arrayCounter++;
			buildings[arrayCounter] = new CornerTable(gp, 348, 384, 2);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 612, 468, "Down", false);
			arrayCounter++;
			buildings[arrayCounter] = new MenuBook(gp, 636, 324);
			arrayCounter++;
			break;
		case 1:
			door = new Door(gp, 600, 456+48, 1, 0);
			door.setDoorNum(9);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 648, 348, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 612, 348, 2);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 576, 348, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 540, 348, 6);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 504, 348, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 444, 456, 34);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 504, 456, 35);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 648, 348, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 612, 348, 2);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 576, 348, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 540, 348, 6);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 504, 348, 8);
			arrayCounter++;
			buildings[arrayCounter] = new StorageFridge(gp, 564, 192,false);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 444, 240, 1);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 480, 240, 4);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 516, 240, 3);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 612, 240, 4);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 624, 240, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 240, 6);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 648, 384, 7);
			arrayCounter++;
			buildings[arrayCounter] = new EscapeHole(gp, 420, 400);
			arrayCounter++;
			buildings[arrayCounter] = new Bin(gp, 612, 228, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 660, 156);
			arrayCounter++;
			//door = new Door(gp, 672+24, 216+48, 3, 0);
			//door.setDoorNum(3);
			//buildings[arrayCounter] = door;
			//arrayCounter++;
			break;
		case 2:
			buildings = new Building[1500];
			
			door = new Door(gp, 3612, 3528, 0, 5);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door =  new Door(gp, 3978, 4809, 3, 6);
			door.setDoorNum(13);
			buildings[arrayCounter] = door;
			arrayCounter++;
			
			buildings[arrayCounter] = new OutdoorDecor(gp, 3276, 3396, 161);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3288, 3540, 162);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3579, 3396, 164);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3813, 3561, 41);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3813, 3636, 41);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3231, 3564, 39);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3231, 3624, 39);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3240, 3684, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3438, 3681, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3474, 3396, 163);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3912, 3366, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3171, 3420, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4059, 3339, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4020, 3432, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3102, 3450, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3171, 3498, 79);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4089, 3447, 80);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3936, 3432, 78);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3678, 3627, 166);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3678, 3627, 166);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3765, 3630, 166);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3765, 3630, 166);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3699, 3576, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4194, 3204, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4353, 3258, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4257, 3324, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4386, 3330, 81);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4335, 3456, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3828, 3420, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3129, 3570, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3825, 4155, 152);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3303, 4158, 153);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3582, 4809, 69);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3672, 4809, 64);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3492, 4809, 64);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3987, 4719, 170);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4014, 4791, 171);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4044, 4893, 176);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4002, 4908, 173);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4002, 4908, 173);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4152, 4908, 172);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3897, 4773, 174);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3939, 4875, 175);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4032, 4830, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4101, 4830, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3714, 3621, 165);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3720, 3618, 159);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3744, 3654, 160);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3717, 3627, 159);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3516, 3396, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3681, 3363, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3354, 3363, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3354, 3363, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3270, 3369, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3270, 3369, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3426, 3366, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3426, 3366, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3597, 3366, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3753, 3366, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3753, 3366, 149);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3471, 3405, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3576, 3405, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3246, 3381, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3801, 3384, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3507, 3555, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3507, 3555, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3513, 3495, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3513, 3495, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3435, 4305, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3759, 4308, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3363, 4323, 20);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3519, 4323, 20);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3843, 4326, 20);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3687, 4326, 20);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3525, 4509, 39);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3678, 4509, 39);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3420, 4512, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3780, 4512, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3354, 4509, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4398, 4740, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4494, 4740, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4395, 4848, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4203, 4860, 9);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4284, 4860, 9);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4776, 3513, 45);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5913, 2844, 74);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6165, 2997, 60);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6285, 2997, 59);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5805, 2877, 174);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6318, 2961, 173);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5973, 2985, 31);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5922, 2985, 31);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5859, 2937, 30);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5859, 2991, 40);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5772, 2943, 41);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 2778, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5658, 2853, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 2799, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5553, 2880, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6486, 2808, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6651, 2919, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6846, 2778, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6861, 2892, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6819, 2985, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6894, 3075, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6723, 3057, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6729, 2787, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6381, 2853, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6243, 2829, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5643, 2937, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6573, 2826, 79);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6819, 3057, 80);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6249, 2760, 78);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5727, 2829, 78);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6039, 3618, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5220, 3087, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6531, 3261, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6573, 3264, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6621, 3264, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6666, 3261, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6531, 3300, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6573, 3297, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6621, 3297, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6669, 3297, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6708, 3264, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6708, 3300, 100);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6531, 3339, 101);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6570, 3339, 101);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6621, 3339, 101);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3402, 6183, 177);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3435, 6414, 55);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3114, 6039, 54);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2754, 5904, 178);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2946, 5904, 178);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3267, 5904, 178);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3528, 5904, 178);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2733, 6201, 179);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5979, 849, 154);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5763, 1842, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5499, 1119, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6543, 621, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4740, 4803, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4860, 4803, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5178, 4800, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5307, 4800, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5307, 4800, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4722, 4893, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5298, 4893, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5166, 4893, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4848, 4896, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4692, 4452, 180);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4794, 4917, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5244, 4917, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4647, 4821, 175);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4647, 4731, 175);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4650, 4647, 175);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4617, 4521, 174);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4662, 4587, 30);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4659, 4641, 32);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3147, 4572, 56);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3042, 4572, 57);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2937, 4572, 58);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3285, 4563, 40);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3354, 4392, 167);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3552, 4437, 168);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3657, 4389, 169);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3591, 4326, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3162, 4353, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2979, 4353, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2796, 4353, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2265, 4410, 182);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2832, 4572, 59);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2301, 4308, 171);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2628, 4308, 171);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2352, 4347, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2679, 4347, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2703, 4506, 150);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2304, 4431, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2406, 4431, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2730, 4434, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2646, 4434, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 2517, 4344, 9);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2508, 4428, 15);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2289, 4515, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2391, 4515, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2595, 4521, 38);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3876, 4359, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4041, 4398, 182);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4575, 4371, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4758, 4371, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4944, 4371, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5130, 4371, 181);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5130, 4371, 181);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4725, 4044, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4635, 3735, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4638, 3852, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3798, 3864, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3879, 3930, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3786, 4002, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2883, 4245, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2961, 4134, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2838, 4107, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2784, 4152, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3138, 4185, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3339, 4095, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4317, 4095, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4188, 4152, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4818, 4266, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4818, 4266, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4566, 3921, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3969, 3909, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4002, 4140, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5043, 4140, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4920, 4128, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3900, 4050, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3072, 3903, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4323, 3774, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 4578, 4158, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5220, 4236, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3006, 4194, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2850, 3906, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3243, 4149, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3351, 3804, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4230, 4332, 44);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4077, 4305, 171);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4404, 4308, 171);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4128, 4344, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4455, 4347, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4329, 4149, 153);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3984, 4383, 151);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4365, 4512, 29);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4287, 4413, 16);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4275, 4491, 31);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4170, 4488, 174);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4101, 4428, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4509, 4422, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4413, 4422, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4044, 4326, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 4560, 4332, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 4296, 4329, 9);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3399, 4431, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3807, 4434, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 3807, 4434, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3777, 3399, 44);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5295, 4404, 183);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5496, 4317, 148);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5586, 4317, 148);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5454, 4323, 19);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5658, 4326, 19);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5346, 4284, 10);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5352, 4311, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5748, 4305, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5700, 4434, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5793, 4437, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWindow(gp, 5400, 4434, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5541, 4923, 45);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3015, 6540, 177);
			arrayCounter++;
			
			break;
		case 3:
			door = new Door(gp, 432, 204+48, 2, 0);
			door.setDoorNum(8);
			buildings[arrayCounter] = door;
			arrayCounter++;
			//door = new Door(gp, 468, 144+48, 0, 0);
			//door.setDoorNum(5);
			//buildings[arrayCounter] = door;
			//arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 372, 45);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 408, 44);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 360, 34);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 564, 324, 21);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 468, 324, 21);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 372, 324, 21);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 564, 408, 20);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 456, 120, 19);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 444, 42);
			arrayCounter++;
			buildings[arrayCounter] = new Breaker(gp, 552, 216);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 576, 156, 18);
			arrayCounter++;
			buildings[arrayCounter] = new Trapdoor(gp, 516, 420, 0);
			arrayCounter++;
			break;
		case 4:
			door = new Door(gp, 720+24, 264+16+48, 3, 0);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 576, 288, "Down", false);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 672, 288, "Down", false);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 588, 468, "Down", false);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 624, 492, 25);
			arrayCounter++;
			buildings[arrayCounter] = new Toilet(gp, 600, 528, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 708, 576, 46);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 660, 432, 56);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 288, 47);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 696, 276, 1);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 468, 49);
			arrayCounter++;
			buildings[arrayCounter] = new ToiletDoor(gp, 672, 396, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 636, 468, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 468, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 684, 180);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 300, 42);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 516, 300, 43);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 276, 50);
			arrayCounter++;
			break;
		case 5:
			door = new Door(gp, 420, 504+48, 1, 0);
			door.setDoorNum(8);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new Bed(gp, 648, 276);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 300, 57);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 708, 516, 1);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 672, 228, 2);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 408, 61);
			arrayCounter++;
			buildings[arrayCounter] = new ClothesRail(gp, 696, 372);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 384, 240, 62);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 276, 48);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 444, 168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 660, 168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 588, 168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 516, 168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Calendar(gp, 552, 240);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 492, 288, 46);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 324, 3);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 384, 58);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 576, 408, 60);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 396, 25);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 612, 432, 20);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 576, 432, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Computer(gp, 480, 312);
			arrayCounter++;
			break;
		case 6:
			buildings[arrayCounter] = new FloorDecor_Building(gp, 720, 288, 35);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 492, 35);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 744, 492, 31);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 708, 192, 26);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 432, 192, 27);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 444, 420, 64);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 300, 65);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 732, 348, 64);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 444, 66);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 528, 276, 67);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 468, 312, 69);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 708, 444, 68);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 480, 444, 68);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 612, 276, 69);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 492, 69);
			arrayCounter++;
			buildings[arrayCounter] = new SoulLantern(gp, 480, 180);
			arrayCounter++;
			buildings[arrayCounter] = new SoulLantern(gp, 672, 180);
			arrayCounter++;
			buildings[arrayCounter] = new SoulLantern(gp, 432, 456);
			arrayCounter++;
			buildings[arrayCounter] = new SoulLantern(gp, 744, 492);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 420, 44);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 504, 528, 45);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 576, 312, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 672, 372, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 648, 468, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 516, 468, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 480, 384, 1);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 744, 396, 30);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 384, 42);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 336, 42);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 684, 264, 38);
			arrayCounter++;
			buildings[arrayCounter] = new Cauldron(gp, 576, 384);
			arrayCounter++;
			buildings[arrayCounter] = new Trapdoor(gp, 552, 156, 1);
			arrayCounter++;
			break;
		case 7: // Abandoned corridor
			roomSpawn = new RoomSpawn(gp, 264, 420);
			door = new Door(gp, 240, 384, 2, 0);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 360, 288, 0, 2);
			door.setDoorNum(9);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 576, 288, 0, 4);
			door.setDoorNum(11);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 360, 504, 1, 0);
			door.setDoorNum(12);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 312-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 420-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 528-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 636-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 744-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new ChefPortrait(gp, 852-32, 216-32);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 288, 324, 0);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 300, 384, 2);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 360, 480, 3);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 480, 336, 4);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 600, 360, 5);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 828, 456, 6);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 312, 480, 8);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 576, 372, 12);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 732, 480, 13);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 468, 480, 14);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 672, 468, 15);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 540, 468, 16);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 492, 408, 19);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 468, 444, 18);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 600, 444, 24);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 348, 408, 26);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 780, 444, 27);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 804, 348, 31);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 744, 360, 30);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 648, 384, 25);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 660, 336, 4);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 408, 456, 23);
			arrayCounter++;
			buildings[arrayCounter] = new CursedDecor(gp, 300, 360, 23);
			arrayCounter++;
			buildings[arrayCounter] = new Torch(gp, 324, 288);
			arrayCounter++;
			buildings[arrayCounter] = new Torch(gp, 432, 288);
			arrayCounter++;
			buildings[arrayCounter] = new Torch(gp, 624+48, 288);
			arrayCounter++;
			break;
		case 8: //Corridor 1
			door = new Door(gp, 528, 552, 1, 0);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 648, 372, 3, 0);
			door.setDoorNum(3);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 528, 240, 0, 0);
			door.setDoorNum(5);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 612, 228);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 504, 40);
			arrayCounter++;
			break;
		case 9: //Old Kitchen
			roomSpawn = new RoomSpawn(gp, 480-24, 552-48);
			door =  new Door(gp, 456, 600, 1, 0);
			door.setDoorNum(7);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 348, 192+48, 0, 0);
			door.setDoorNum(1);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 696-24, 192+48, 0, 3);
			door.setDoorNum(10);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 756, 384, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 756, 432, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 756, 480, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 732, 516, 16);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 684, 516, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 516, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 516, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 564, 516, 4);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 756, 372, 7);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 516, 15);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 372, 516, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 516, 6);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 348, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 444, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 396, 8);
			arrayCounter++;
			buildings[arrayCounter] = new Sink(gp, 300, 432);
			arrayCounter++;
			buildings[arrayCounter] = new Sink(gp, 300, 336);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 312, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 300, 300, 7);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 408, 4);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 408, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 480, 408, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 528, 408, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 576, 408, 5);
			arrayCounter++;
			buildings[arrayCounter] = new ChoppingBoard(gp, 408, 396);
			arrayCounter++;
			buildings[arrayCounter] = new ChoppingBoard(gp, 468, 396);
			arrayCounter++;
			buildings[arrayCounter] = new Stove(gp, 456, 300);
			arrayCounter++;
			buildings[arrayCounter] = new Oven(gp, 564, 300);
			arrayCounter++;
			buildings[arrayCounter] = new Fridge(gp, 624, 252);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 756, 180);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 312, 180);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 720, 300, 29);
			arrayCounter++;
			buildings[arrayCounter] = new CornerTable(gp, 300, 516, 2);
			arrayCounter++;
			buildings[arrayCounter] = new CornerTable(gp, 732, 516, 3);
			arrayCounter++;
			buildings[arrayCounter] = new Bin(gp, 756, 372, 2);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 624, 408, 6);
			arrayCounter++;
			break;
		case 10: //Freezer
			door = new Door(gp, 456, 600, 1, 0);
			door.setDoorNum(9);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 252, 110);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 324, 113);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 384, 115);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 444, 112);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 672, 348, 114);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 672, 408, 116);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 672, 468, 116);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 624, 252, 111);
			arrayCounter++;
			buildings[arrayCounter] = new Freezer(gp, 588, 348);
			arrayCounter++;
			buildings[arrayCounter] = new Freezer(gp, 516, 348);
			arrayCounter++;
			buildings[arrayCounter] = new Freezer(gp, 612, 576);
			arrayCounter++;
			buildings[arrayCounter] = new Freezer(gp, 540, 576);
			arrayCounter++;
			buildings[arrayCounter] = new FreezerLight(gp, 648, 180);
			arrayCounter++;
			buildings[arrayCounter] = new FreezerLight(gp, 456, 180);
			arrayCounter++;
			break;
		case 11: //WINE CELLAR
			door = new Door(gp, 456, 600, 1, 0);
			door.setDoorNum(7);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 360, 119);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 360, 119);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 696, 360, 119);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 732, 336, 120);
			arrayCounter++;
			buildings[arrayCounter] = new Stairs(gp, 732, 384);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 780, 336, 122);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 828, 336, 122);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 348, 336);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 612, 144);
			arrayCounter++;
			break;
		case 12:
			door = new Door(gp, 528, 240, 0, 0);
			door.setDoorNum(7);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 756, 216);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 348, 216);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 252, 124);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 312, 42);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 336, 312, 125);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 336, 360, 126);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 336, 432, 128);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 768, 312, 129);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 768, 372, 127);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 768, 432, 129);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 396, 504, 10);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 396, 456, 11);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 396, 408, 12);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 396, 372, 13);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 432, 540, 12);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 480, 540, 14);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 456, 312, 15);
			arrayCounter++;
			buildings[arrayCounter] = new SpiceTable(gp, 528, 420);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 396, 552, 42);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 708, 360, 16);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 708, 504, 19);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 708, 456, 18);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 708, 408, 17);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 528, 540, 130);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 708, 564, 34);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 528, 540, 20);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 576, 540, 21);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 624, 540, 22);
			arrayCounter++;
			buildings[arrayCounter] = new FoodStore(gp, 672, 540, 23);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 504, 312, 131);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 372, 132);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 480, 133);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 660, 492, 134);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 372, 135);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 672, 336, 136);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 672, 432, 133);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 312, 133);
			arrayCounter++;
			break;
		case 13:
			
			break;
		}
		buildingArrayCounter = arrayCounter;
	}
	public void addBuilding(Building building) {
		building.roomNum = preset;
		buildings[buildingArrayCounter] = building;
		buildingArrayCounter++;
	}
	public RoomSaveData saveRoomData() {
		RoomSaveData data = new RoomSaveData();
		data.roomNum = preset;
		data.wallpaper = wallpaper.preset;
		data.floorpaper = floorpaper.preset;
		data.beam = beam.preset;
		data.chairSkin = chairSkin.preset;
		data.counterSkin = counterSkin.preset;
		data.tableSkin = tableSkin.preset;
		data.panSkin = panSkin.preset;
		data.doorSkin = doorSkin.preset;
		List<Building> buildList = new ArrayList<>();
		for(Building b: buildings) {
			if(b != null) {
				buildList.add(b);
			}
		}
		data.buildings = gp.world.buildingRegistry.saveBuildings(buildList);
		data.buildingArrayCounter = buildingArrayCounter;
		
		return data;
	}
	public void applySaveData(RoomSaveData data) {
		preset = data.roomNum;
		lights.clear();
		wallpaper = new WallPaper(gp, data.wallpaper);
		floorpaper = new FloorPaper(gp, data.floorpaper);
		beam = new Beam(gp, data.beam);
		chairSkin = new ChairSkin(gp, data.chairSkin);
		counterSkin = new CounterSkin(gp, data.counterSkin);
		tableSkin = new TableSkin(gp, data.tableSkin);
		panSkin = new PanSkin(gp, data.panSkin);
		doorSkin = new DoorSkin(gp, data.doorSkin);
		List<Building> buildList = gp.world.buildingRegistry.unpackSavedBuildings(data.buildings);
		Building[] newBuilds = new Building[250];
		int counter = 0;
		buildings = null;
		for(Building b: buildList) {
			if(b != null) {
				b.roomNum = preset;
			}
			newBuilds[counter] = b;
			counter++;
		}
		buildingArrayCounter = data.buildingArrayCounter;
		buildings = newBuilds;
		if(preset == gp.player.currentRoomIndex) {
			gp.world.lightingM.setLights(lights);
			gp.world.buildingM.setBuildings(newBuilds);
			gp.world.buildingM.setArrayCounter(buildingArrayCounter);
		}
	}
    public void setWallpaper(int preset) {
    	this.wallpaper = new WallPaper(gp, preset);
    }
    public void setWallpaper(WallPaper wallpaper) {
    	gp.world.customiser.addToInventory(this.wallpaper);
    	this.wallpaper = wallpaper;
    }
    public void setChairSkin(int preset) {
    	this.chairSkin = new ChairSkin(gp, preset);
    }
    public void setChairSkin(ChairSkin chairSkin) {
    	gp.world.customiser.addToInventory(this.chairSkin);
    	this.chairSkin = chairSkin;
    	
    }
    public void setCounterSkin(int preset) {
    	this.counterSkin = new CounterSkin(gp, preset);
    }
    public void setCounterSkin(CounterSkin counterSkin) {
    	gp.world.customiser.addToInventory(this.counterSkin);
    	this.counterSkin = counterSkin;
    }
    public void setTableSkin(int preset) {
    	this.tableSkin = new TableSkin(gp, preset);
    }
    public void setTableSkin(TableSkin tableSkin) {
    	gp.world.customiser.addToInventory(this.tableSkin);
    	this.tableSkin = tableSkin;
    }
    public void setPanSkin(int preset) {
    	this.panSkin = new PanSkin(gp, preset);
    }
    public void setPanSkin(PanSkin panSkin) {
    	gp.world.customiser.addToInventory(this.panSkin);
    	this.panSkin = panSkin;
    }
    public void setDoorSkin(int preset) {
    	this.doorSkin = new DoorSkin(gp, preset);
    }
    public void setDoorSkin(DoorSkin doorSkin) {
    	gp.world.customiser.addToInventory(this.doorSkin);
    	this.doorSkin = doorSkin;
    }
    public void setFloorpaper(int preset) {
    	this.floorpaper = new FloorPaper(gp, preset);
    }
    public void setFloorpaper(FloorPaper floorpaper) {
    	gp.world.customiser.addToInventory(this.floorpaper);
    	this.floorpaper = floorpaper;
    }
    public void setBeam(Beam beam) {
    	gp.world.customiser.addToInventory(this.beam);
    	this.beam = beam;
    }
    public void setBeam(int preset) {
    	this.beam = new Beam(gp, preset);
    }
    public WallPaper getWallpaper() {
    	return wallpaper;
    }
    public FloorPaper getFloorpaper() {
    	return floorpaper;
    }
    public Beam getBeam() {
    	return beam;
    }
    public ChairSkin getChairSkin() {
    	return chairSkin;
    }
    public CounterSkin getCounterSkin() {
    	return counterSkin;
    }
    public TableSkin getTableSkin() {
    	return tableSkin;
    }
    public PanSkin getPanSkin() {
    	return panSkin;
    }
    public DoorSkin getDoorSkin() {
    	return doorSkin;
    }
	private void importMap(String fileName, int mapWidth, int mapHeight) {
    	
    	String foregroundName = fileName + "3.txt";
    	String midgroundName = fileName + "1.txt";
    	String backgroundName = fileName + "0.txt";
    	String midground2Name = fileName + "2.txt";

        try{
        	

            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(midgroundName)));

            String currentLine; //Stores the current line of the text file

            for(int i = 0; i < mapHeight; i++) {

                currentLine = br.readLine(); //Reads the current line on the text file

                for(int j = 0; j < mapWidth; j++) {

                    String[] rowNumbers = currentLine.split(" "); //Splits the current line at each space, and stores to an array

                    mapGrid[1][j][i] = Integer.parseInt(rowNumbers[j]); //Stores the numbers in the array to the grid

                }

            }
            
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(foregroundName)));

            currentLine = null; //Stores the current line of the text file

            for(int i = 0; i < mapHeight; i++) {

                currentLine = br.readLine(); //Reads the current line on the text file

                for(int j = 0; j < mapWidth; j++) {

                    String[] rowNumbers = currentLine.split(" "); //Splits the current line at each space, and stores to an array

                    mapGrid[3][j][i] = Integer.parseInt(rowNumbers[j]); //Stores the numbers in the array to the grid

                }

            }
            
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(backgroundName)));

            currentLine = null; //Stores the current line of the text file

            for(int i = 0; i < mapHeight; i++) {

                currentLine = br.readLine(); //Reads the current line on the text file

                for(int j = 0; j < mapWidth; j++) {

                    String[] rowNumbers = currentLine.split(" "); //Splits the current line at each space, and stores to an array

                    mapGrid[0][j][i] = Integer.parseInt(rowNumbers[j]); //Stores the numbers in the array to the grid

                }

            }
            
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(midground2Name)));

            currentLine = null; //Stores the current line of the text file

            for(int i = 0; i < mapHeight; i++) {

                currentLine = br.readLine(); //Reads the current line on the text file

                for(int j = 0; j < mapWidth; j++) {

                    String[] rowNumbers = currentLine.split(" "); //Splits the current line at each space, and stores to an array

                    mapGrid[2][j][i] = Integer.parseInt(rowNumbers[j]); //Stores the numbers in the array to the grid

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getRoomID() {
    	return roomID;
    }
    public String getRoomIDTag() {
    	return roomIDTag;
    }
    public String getRoomType() {
    	return roomType;
    }
    public Building[] getBuildings() {
    	return buildings;
    }
    public List<NPC> getNPCs() {
    	return npcs;
    }
    public List<Item> getItems() {
    	return items;
    } 
    public List<LightSource> getLights() {
    	return lights;
    } 
    public void editBuildings(Building[] buildings, int index) {
    	this.buildings = buildings;
    	this.buildingArrayCounter = index;
    }
    public void editNPCs(List<NPC> npcs) {
    	this.npcs = npcs;
    }
    public void editItems(List<Item> items) {
    	this.items = items;
    }
    public void editLights(List<LightSource> lights) {
    	this.lights = lights;
    }
    public void addNPC(NPC npc) {
     	npcs.add(npc);
    }
    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }
    public boolean containsNPC(NPC npc) {
        return npcs.contains(npc);
    }
    public boolean containsAnyNPC() {
        return npcs.size() > 0;
    }
	public Chair isFreeChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate) {
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Building findBuildingWithName(String name) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals(name)) {
					return b;
				}
			}
		}
		return null;
	}
	public Customer findCustomerWithOrder(Recipe recipe) {
		List<NPC> npcCopy = new ArrayList<NPC>(npcs);
		for(NPC npc : npcCopy) {
			if(npc.getNPCType().equals("Customer")) {
				Customer c = (Customer)npc;
				if(c.foodOrder != null) {
					if(c.foodOrder.equals(recipe)) {
						return c;
					}
				}
			}
		}
		return null;
	}
    public void addCustomer() {
    	Customer customer = new Customer(gp, (int)getSpawnX(), (int)getSpawnY());
    	customer.currentRoomNum = preset;
    	npcs.add(customer);
    }
    public void addCook() {
    	Cook cook = new Cook(gp, (int)getSpawnX(), (int)getSpawnY());
    	cook.currentRoomNum = preset;
    	npcs.add(cook);
    }
	public Chair findFreeChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && !chair.groupChair) {
						chair.available = false;
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Toilet findFreeToilet() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Toilet 1")) {
					Toilet toilet = (Toilet)b;
					if(toilet.available) {
						toilet.available = false;
						return toilet;
					}
				}	
			}
		}
		return null;
	}
	public void removeBuilding(Building building) {
		int counter = 0;
		for(Building b: buildings) {
			if(b != null) {
				if(building.equals(b)) {
					buildings[counter] = null;
				}
			}
			counter++;
		}
	}
	public void addGroup(int num) {
		GroupCustomer customer = new GroupCustomer(gp, (int)getSpawnX(), (int)getSpawnY());
	     for(int i = 0; i < num; i++) {
	    	 customer = new GroupCustomer(gp, (int)getSpawnX(), (int)getSpawnY());
	    	 customer.currentRoomNum = preset;
	         npcs.add(customer);
	     }
	}
	public void addSpecialCustomer() {
		SpecialCustomer customer = new SpecialCustomer(gp, (int)getSpawnX(), (int)getSpawnY());
		customer.currentRoomNum = preset;
		npcs.add(customer);
	}
	public boolean hasBuildingWithName(String name) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	public Door findDoor(int roomNum) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.doorRoomNum == roomNum) {
						return door;
					}
				}	
			}
		}
		return null;
	}
	public void addLeak(int a) {
		if(a == 0) {
			buildings[buildingArrayCounter] = new Leak(gp, 9*gp.tileSize, 10*gp.tileSize + 24);
			buildingArrayCounter++;
		} else if(a == 1) {
			buildings[buildingArrayCounter] = new Leak(gp, 13*gp.tileSize, 10*gp.tileSize + 24);
			buildingArrayCounter++;
		}
	}
	public void addSpill(int a) {
		if(gp.world.progressM.currentPhase == 0) {
			buildings[buildingArrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
			buildingArrayCounter++;
		} else if(gp.world.progressM.currentPhase == 1) {
			if(a == 0) {
				buildings[buildingArrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
				buildingArrayCounter++;
			} else if(a == 1) {
				buildings[buildingArrayCounter] = new Spill(gp, 9*gp.tileSize - 24, 9*gp.tileSize);
				buildingArrayCounter++;
			}
		}
	}
	public void addLight(LightSource light) {
		lights.add(light);
	}
	public float getSpawnX() {
		return roomSpawn.hitbox.x + roomSpawn.hitbox.width/2;
	}
	public float getSpawnY() {
		return roomSpawn.hitbox.y + roomSpawn.hitbox.height/2;
	}
    public int getCatNum() {
    	int catNum = 0;
    	for(NPC b: npcs) {
			if(b != null) {
				if(b instanceof Pet pet) {
					if(pet.isCat) {
						catNum++;
					}
				}
			}
		}
    	return catNum;
    }
	public TablePlate findDirtyPlate() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Table Plate")) {
					TablePlate table = (TablePlate)b;
					if(table.showDirtyPlate && table.plate != null) {
						return table;
					}
				}	
			}
		}
		return null;
	}
	public Chair isGroupTable() {

	    for (Building b : buildings) {
	        if (b != null && b.getName().equals("Chair 1")) {

	            Chair chair = (Chair) b;

	            // Must be a group chair
	            if (!chair.groupChair) continue;

	            boolean tableFree = true;

	            // Check ALL chairs on this table
	            for (Chair c : chair.table.getChairs()) {

	                // If any chair is occupied OR has a dirty plate, the table isn't free
	                if (!c.available || c.tablePlate.showDirtyPlate) {
	                    tableFree = false;
	                    break;
	                }
	            }

	            // Only return the chair if the whole table is free & clean
	            if (tableFree) {
	                return chair;
	            }
	        }
	    }

	    return null;
	}
	public Chair takeGroupChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && chair.groupChair) {
						chair.available = false;
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Chair isFreeSingleChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && !chair.groupChair) {
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public List<Building> findBuildingsWithName(String name) {
	    List<Building> result = new ArrayList<>();
	    for (Building b : buildings) {
	        if (b != null && b.getName().equals(name)) {
	            result.add(b);
	        }
	    }
	    return result;
	}
	 public void removeAllWithName(String name) {
	        for (int i = 0; i < buildings.length; i++) {
	            Building b = buildings[i];
	            if (b != null && b.getName().equals(name)) {

	                // Clean up building state
	                b.destroy();

	                // Remove reference
	                buildings[i] = null;
	            }
	        }
	    }
	   public void customersLeave(double dt) {
	        for (NPC b : npcs) {
	            if (b != null) {
	                if (b instanceof Customer customer) {
	                    customer.leave(dt);
	                }
	            }
	        }
	    }
    public void updateState(double dt) {
    	if(buildings == null) {
    		return;
    	}
    	for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.updateState(dt);
            }
        }
    	for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
            	i.updateState(dt);
            }
        }
    	
    	for (NPC i : new ArrayList<>(npcs)) {
    	    if (i != null) {
    	        i.updateState(dt);
    	    }
    	}
    }
    public void inputUpdate(double dt) {
    	if(gp.world.mapM.isInRoom(preset)) {
	    	for(Building i: buildings) { //Loops through the items on the current map
	            if(i != null) {
	            		i.inputUpdate(dt);
	            }
	        }
	    	for(Item i: items) { //Loops through the items on the current map
	            if(i != null) {
	            	i.inputUpdate(dt);
	            }
	        }
	    	
	    	for (NPC i : new ArrayList<>(npcs)) {
	    	    if (i != null) {
	    	        i.inputUpdate(dt);
	    	    }
	    	}
    	}
    }
}

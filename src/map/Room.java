package map;

import java.awt.image.BufferedImage;
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
import entity.buildings.ChoppingBoard;
import entity.buildings.CornerTable;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Fridge;
import entity.buildings.StorageFridge;
import entity.buildings.Gate;
import entity.buildings.Lantern;
import entity.buildings.Leak;
import entity.buildings.MenuSign;
import entity.buildings.Sink;
import entity.buildings.SoulLantern;
import entity.buildings.Spill;
import entity.buildings.Stove;
import entity.buildings.Table;
import entity.buildings.Table2;
import entity.buildings.TablePlate;
import entity.buildings.Toilet;
import entity.buildings.ToiletDoor;
import entity.buildings.Trapdoor;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import entity.buildings.outdoor.OutdoorDecor;
import entity.buildings.outdoor.OutdoorWallDecor;
import entity.buildings.outdoor.SeasonalDecoration;
import entity.items.Item;
import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import utility.Recipe;
import utility.RoomHelperMethods;
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
	public boolean shouldUpdate = false;
	private int preset;
	public WallPaper wallpaper = null;
	public FloorPaper floorpaper = null;
	public Beam beam = null;
	
	public Room(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		mapWidth = gp.tilesInWidth;
	    mapHeight = gp.tilesInHeight;
        mapGrid = new int[4][mapWidth][mapHeight];
	    setUpRoom(preset);
	}
	private void setUpRoom(int preset) {
		switch(preset) {
		case 0:
			roomType = "Main";
	        filePath = "/maps/main/Room";
	        importMap(filePath, mapWidth, mapHeight);
	        roomID = "/main";
			roomIDTag = "Room";
			shouldUpdate = true;
			setWallpaper(0);
			setFloorpaper(0);
			setBeam(0);
			break;
		case 1:
			roomType = "Stores";
	        filePath = "/maps/stores/Room";
	        importMap(filePath, mapWidth, mapHeight);
	        roomID = "/stores";
			roomIDTag = "Room";
			shouldUpdate = true;
			setWallpaper(1);
			setFloorpaper(1);
			setBeam(1);
			break;
		case 2:
			roomType = "Outdoors";
			filePath = "/maps/outdoors/Layer";
			mapWidth = 250;
			mapHeight = 250;
			mapGrid = new int[4][mapWidth][mapHeight];
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/outdoors";
		    roomIDTag = "Layer";
			break;
		case 3:
			roomType = "Electrics";
			filePath = "/maps/electrics/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/electrics";
		    roomIDTag = "Layer";
		    setWallpaper(2);
			setFloorpaper(2);
			setBeam(2);
			break;
		case 4:
			roomType = "Toilets";
			filePath = "/maps/toilets/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/toilets";
		    roomIDTag = "Layer";
		    setWallpaper(3);
			setFloorpaper(3);
			setBeam(0);
			break;
		case 5:
			roomType = "Bedroom";
			filePath = "/maps/bedroom/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/bedroom";
		    roomIDTag = "Layer";
		    setWallpaper(4);
			setFloorpaper(0);
			setBeam(4);
			break;
		case 6:
			roomType = "Basement";
			filePath = "/maps/basement/Layer";
		    importMap(filePath, mapWidth, mapHeight);
		    roomID = "/basement";
		    roomIDTag = "Layer";
		    setWallpaper(5);
			setFloorpaper(0);
			setBeam(4);
			break;
		}
		setBuildings(preset);
		setNPCs(preset);
		setItems(preset);
	}
	private void setBuildings(int preset) {
		buildings = new Building[250];
		int arrayCounter = 0;
		switch(preset) {
		case 0:
			buildings[arrayCounter] = new Door(gp, 684, 144+48, 0, 0);
			arrayCounter++;
			Door door = new Door(gp, 456, 504+48, 1, 0);
			door.setDoorNum(2);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 336, 344+48, 2, 0);
			door.setDoorNum(4);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new Chair(gp, 708, 444, 2);
			arrayCounter++;
			buildings[arrayCounter] = new Chair(gp, 708, 276, 2);
			arrayCounter++;
			buildings[arrayCounter] = new Chair(gp, 708, 324, 2);
			arrayCounter++;
			buildings[arrayCounter] = new Chair(gp, 372, 432, 3);
			arrayCounter++;
			
			buildings[arrayCounter] = new WallDecor_Building(gp, 336, 156, 9);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 384, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 432, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 480, 156, 8);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 336, 204, 12);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 336, 252, 12);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 204, 22);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 396, 144, 21);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 276, 23);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 336, 228, 13);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 336, 288, 13);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 240, 20);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 156, 24);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 444, 144, 25);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 756, 408);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 384, 5);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 312, 8);
			arrayCounter++;
			buildings[arrayCounter] = new Fridge(gp, 588, 204);
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
			buildings[arrayCounter] = new Table2(gp, 348, 468);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 384, 15);
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
			buildings[arrayCounter] = new FloorDecor_Building(gp, 348, 468, 28);
			arrayCounter++;
			buildings[arrayCounter] = new Table(gp, 756, 276);
			arrayCounter++;
			buildings[arrayCounter] = new Sink(gp, 348, 300);
			arrayCounter++;
			buildings[arrayCounter] = new MenuSign(gp, 636, 372);
			arrayCounter++;
			buildings[arrayCounter] = new Bin(gp, 384, 240);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 756, 372, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 636, 144);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 756, 408, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 756, 336, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 348, 372, 1);
			arrayCounter++;
			break;
		case 1:
			door = new Door(gp, 600, 456+48, 1, 0);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 432, 156, 6);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 480, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 528, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 576, 156, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 564, 132, 25);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 492, 144, 20);
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
			buildings[arrayCounter] = new WallDecor_Building(gp, 432, 156, 6);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 480, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 528, 156, 7);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 576, 156, 8);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 564, 132, 25);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 492, 144, 20);
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
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 324, 17);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 420, 348, 17);
			arrayCounter++;
			buildings[arrayCounter] = new StorageFridge(gp, 564, 192);
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
			buildings[arrayCounter] = new FloorDecor_Building(gp, 684, 420, 39);
			arrayCounter++;
			buildings[arrayCounter] = new EscapeHole(gp, 420, 400);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 144, 33);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 528, 144, 26);
			arrayCounter++;
			buildings[arrayCounter] = new Bin(gp, 612, 228);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 624, 144, 17);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 624, 456, 40);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 660, 156);
			arrayCounter++;
			door = new Door(gp, 672+24, 216+48, 3, 0);
			door.setDoorNum(3);
			buildings[arrayCounter] = door;
			arrayCounter++;
			break;
		case 2:
			buildings = new Building[1500];
			door = new Door(gp, 6096, 5940+48, 0, 1);
			door.setDoorNum(0);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6072, 5880, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6096, 5964, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6180, 5964, 0);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6096, 5916, 2);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 6204, 6024, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6264, 5664, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6072, 5712, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 5796, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6192, 5736, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6324, 5784, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6300, 5844, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5916, 5820, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 5664, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6588, 5664, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6492, 5748, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6624, 5808, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 5844, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6408, 5868, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6408, 5760, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6192, 5796, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6108, 5760, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 5880, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5952, 5940, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6348, 6024, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6300, 5976, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 6048, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6084, 5808, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 5688, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6348, 5916, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6540, 5916, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6660, 5724, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6528, 5664, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 5676, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6048, 5772, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 5892, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 5760, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6564, 5712, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 5724, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6576, 5784, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6648, 5676, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 5892, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6540, 5652, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6660, 5796, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 5820, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 5916, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 6060, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 5712, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 5664, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5868, 5892, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5832, 5940, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 5760, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6384, 5724, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 5664, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6720, 5832, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6672, 5892, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 5904, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 5868, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6540, 5952, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6468, 5964, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 5940, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 5676, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 5724, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 5748, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 5664, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 5976, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5676, 5904, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 5808, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5664, 5772, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5664, 5676, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 5580, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 5616, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5712, 5580, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 5484, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 5412, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 5532, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5952, 5448, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 5496, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6096, 5436, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 5532, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 5400, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5940, 5316, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5988, 5280, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5820, 5328, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6108, 5256, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6084, 5328, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 5400, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 5304, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6324, 5388, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 5340, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6216, 5472, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 5508, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6216, 5412, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6084, 5604, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6156, 5628, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6240, 5580, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6324, 5640, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 5568, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6336, 5556, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6276, 5532, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 5592, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6300, 5448, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 5328, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 5412, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6384, 5460, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 5496, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 5580, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 5508, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6048, 5448, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5940, 5376, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6540, 5460, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6432, 5364, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6744, 5652, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6576, 5520, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6456, 5436, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6252, 5376, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6144, 5472, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6156, 5304, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6036, 5268, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5904, 5280, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5880, 5400, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5760, 5412, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5712, 5532, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5880, 5628, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5880, 5496, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 5652, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5676, 5712, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5688, 5436, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 5292, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6096, 5376, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6276, 5484, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6672, 5592, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 5448, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 5520, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6084, 5568, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 5544, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 5316, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 5568, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6492, 5544, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5796, 5544, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5652, 5544, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5628, 5628, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 5376, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6072, 5316, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5976, 5232, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6216, 5256, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 5280, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6108, 5208, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6264, 5244, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 5340, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6060, 5520, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5976, 5568, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 5436, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 5328, 9);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 5484, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6348, 5292, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 5544, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 5364, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 5256, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6876, 5532, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7068, 5508, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6912, 5484, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7056, 5412, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6948, 5556, 10);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7092, 5352, 11);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7032, 5952, 11);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6876, 5952, 13);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6828, 5700, 17);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7020, 5700, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7068, 5700, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7104, 5700, 19);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6828, 5832, 20);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6924, 5832, 21);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6972, 5832, 21);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7068, 5832, 21);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7104, 5832, 22);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6972, 5700, 23);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6876, 5832, 27);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 7020, 5832, 28);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6876, 5700, 18);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6924, 5700, 24);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5928, 6216, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5856, 6180, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 6300, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5940, 6096, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6384, 6192, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 6360, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6468, 6492, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6276, 6528, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 6540, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 6168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 6312, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 6252, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 6432, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 6408, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 6432, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 6372, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5736, 6132, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 6048, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5688, 6204, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 6168, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6396, 6072, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 6132, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 6204, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6516, 6300, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6660, 6228, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 6144, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6576, 6204, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 6288, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 6216, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6600, 6072, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 6372, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6432, 6420, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6240, 6444, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5904, 6276, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 6264, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5892, 6180, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 6096, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5904, 6048, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 6108, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 6204, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 6300, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6168, 6492, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6348, 6528, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6528, 6444, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6600, 6324, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 6276, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6456, 6168, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 6336, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5748, 6408, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5988, 6228, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 6180, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5916, 6096, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 6132, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6204, 6300, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5916, 6384, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6048, 6288, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6048, 6456, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5904, 6456, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6120, 6384, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5964, 6348, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6192, 6216, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6528, 6060, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 6000, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5940, 6000, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 6036, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5640, 5952, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 6564, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5748, 6504, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6024, 6564, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 6216, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5976, 6252, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 6396, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5904, 6336, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6000, 6000, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 6492, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 6504, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6360, 6324, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6480, 6180, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 6060, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 6252, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 6396, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 6516, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 6348, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 6252, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6624, 6528, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6492, 6540, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5760, 6396, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 6240, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5868, 6108, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5796, 6120, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5880, 6000, 3);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6216, 5844, 5);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6384, 7296, 29);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6744, 7464, 36);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6924, 7524, 6);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorWallDecor(gp, 6756, 7500, 4);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5628, 7464, 33);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 7296, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 7296, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 7236, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 7368, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6252, 7164, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6204, 7236, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 7248, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 7176, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6096, 7128, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6096, 7092, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5868, 7080, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6012, 7200, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5880, 7272, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5868, 7392, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 7332, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6084, 7308, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6444, 7164, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 7164, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5820, 7200, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5796, 7056, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 7236, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5760, 7332, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 7320, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6288, 7188, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 7188, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6252, 7248, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6348, 7236, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6432, 7176, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6564, 7176, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 7200, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6732, 7272, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 7212, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 7176, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6624, 7164, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6660, 7248, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6768, 7296, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6768, 7224, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6300, 7140, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6720, 7320, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6432, 7236, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6492, 7164, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5808, 7260, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 7152, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5988, 7272, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6060, 7032, 7);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6204, 7296, 32);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5904, 7272, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5904, 7320, 12);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5904, 7224, 4);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6108, 7140, 12);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6108, 7092, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6108, 7044, 3);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6060, 7044, 13);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6252, 6204, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6252, 6252, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6252, 6300, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6252, 6348, 12);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6252, 6156, 4);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6120, 6228, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6156, 6300, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5928, 6468, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5880, 5700, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5916, 7008, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 6036, 7128, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 5964, 7272, 37);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6732, 5928, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 6000, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6732, 6084, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7212, 5988, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6768, 6204, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6768, 6024, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7140, 5904, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7140, 6060, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7236, 6168, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7164, 6204, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6660, 6144, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6600, 6012, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6744, 6384, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6756, 6516, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6840, 6348, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 6348, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6396, 6600, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6504, 6648, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6396, 6708, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 6684, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6228, 6540, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6708, 6612, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 6720, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6624, 6672, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6852, 6576, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 7104, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6396, 6972, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6492, 7044, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 6960, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6876, 7104, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6624, 7032, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6732, 7080, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 6972, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6780, 6972, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6984, 6996, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6816, 7140, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6948, 7200, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6876, 6972, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6708, 7020, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6588, 7080, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6420, 7056, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6312, 6984, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6372, 7116, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6480, 7116, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6612, 7116, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6552, 7032, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6600, 6972, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6744, 7140, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6792, 7068, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6972, 7092, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6864, 7224, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7032, 7200, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6900, 7296, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7128, 7236, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7008, 7368, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7200, 7452, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7188, 7308, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6996, 7260, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7080, 7140, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6948, 7056, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6888, 7176, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6984, 7116, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7068, 7248, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6948, 7320, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7140, 7332, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 7092, 7356, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6456, 6900, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6192, 6936, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6120, 6960, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 7008, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 6996, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5712, 7116, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 7188, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5820, 7128, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 7044, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5832, 6996, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6072, 6900, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5844, 6696, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5748, 6696, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6168, 6672, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6180, 6756, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 6756, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5724, 6672, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5700, 6756, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6264, 6744, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5832, 6600, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 6576, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5748, 6588, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6120, 6744, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5796, 6648, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5784, 6456, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6168, 6600, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5820, 6924, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5760, 6924, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5712, 6972, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5688, 6912, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 5772, 6960, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6132, 6900, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6072, 6996, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6588, 6588, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6480, 6708, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6876, 6636, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6720, 6672, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6780, 6588, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6852, 6492, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 6432, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6804, 6396, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 6684, 6060, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3000, 8412, 31);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3144, 8784, 35);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2760, 8940, 37);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2700, 9000, 37);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2520, 9132, 12);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3192, 9228, 10);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2880, 9288, 11);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2580, 9072, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3324, 9216, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3192, 9288, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2964, 9204, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2868, 9228, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2760, 9276, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2628, 9240, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2652, 9144, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3384, 9300, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3456, 9216, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3396, 9180, 13);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2652, 8856, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2628, 8772, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2976, 8592, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2820, 8628, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2748, 8700, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2844, 8724, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2772, 8580, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3072, 8628, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2988, 8688, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3216, 8628, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3336, 8784, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3216, 8880, 2);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2868, 8448, 34);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8016, 1);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2760, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2808, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2856, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2904, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2952, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3000, 8016, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8064, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8112, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8160, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8016, 3);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2568, 7992, 32);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8064, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8112, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8160, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8208, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3048, 8256, 11);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8208, 8);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2712, 8256, 9);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2760, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2808, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 3000, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2952, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2904, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2856, 8256, 14);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2532, 8232, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2472, 8364, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2472, 8208, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2376, 8304, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2424, 8448, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2616, 8496, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2604, 8424, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2568, 8376, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2472, 8292, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2400, 8208, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2520, 8172, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2772, 8184, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2952, 8100, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2820, 8088, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2916, 8172, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2856, 8148, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2784, 8076, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2988, 8064, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3000, 8184, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2892, 8076, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2832, 8232, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2496, 8460, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2424, 8400, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2724, 8628, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2760, 8472, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2688, 8400, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2568, 8316, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2604, 8268, 6);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2808, 8412, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2604, 8364, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2436, 8172, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2484, 8088, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2520, 8136, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2580, 8220, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2640, 8184, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2664, 8304, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3036, 8976, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3012, 8916, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3168, 9072, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3144, 9000, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3060, 9072, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3132, 8964, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3264, 8988, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3324, 9084, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3204, 9060, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3060, 8796, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2784, 8520, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2724, 8520, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3228, 8724, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3300, 8700, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3084, 8712, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3024, 8736, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3228, 8928, 1);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3072, 9012, 0);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3108, 8028, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3168, 8028, 4);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3444, 8028, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3336, 8040, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3228, 8052, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3156, 8124, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3192, 8196, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3108, 8100, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3096, 8208, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3288, 8100, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3444, 8112, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3408, 8184, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3516, 8160, 2);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3288, 8040, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3204, 8148, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3168, 8088, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3360, 8112, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3492, 8076, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3324, 8172, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3216, 8364, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3168, 8316, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2928, 8316, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2784, 8304, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2784, 8364, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3264, 8460, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3144, 8592, 5);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3228, 8544, 5);
			arrayCounter++;
			buildings[arrayCounter] = new OutdoorDecor(gp, 2916, 9072, 37);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 3084, 8928, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2556, 8448, 8);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2832, 8592, 7);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2952, 8040, 3);
			arrayCounter++;
			buildings[arrayCounter] = new SeasonalDecoration(gp, 2712, 8580, 4);
			arrayCounter++;
			break;
		case 3:
			door = new Door(gp, 432, 204+48, 2, 0);
			door.setDoorNum(1);
			buildings[arrayCounter] = door;
			arrayCounter++;
			door = new Door(gp, 468, 144+48, 0, 0);
			door.setDoorNum(5);
			buildings[arrayCounter] = door;
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 372, 45);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 408, 44);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 444, 46);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 432, 360, 34);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 468, 312, 25);
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
			buildings[arrayCounter] = new Table2(gp, 576, 288);
			arrayCounter++;
			buildings[arrayCounter] = new Table2(gp, 372, 288);
			arrayCounter++;
			buildings[arrayCounter] = new Table2(gp, 672, 288);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 480, 216, 2);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 516, 180, 23);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 588, 180, 24);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 624, 216, 3);
			arrayCounter++;
			buildings[arrayCounter] = new Table2(gp, 480, 288);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 504, 53);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 444, 53);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 744, 432, 51);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 324, 420, 51);
			arrayCounter++;
			buildings[arrayCounter] = new Table2(gp, 468, 468);
			arrayCounter++;
			buildings[arrayCounter] = new Table2(gp, 588, 468);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 624, 492, 25);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 480, 492, 25);
			arrayCounter++;
			buildings[arrayCounter] = new Toilet(gp, 492, 528, 1);
			arrayCounter++;
			buildings[arrayCounter] = new Toilet(gp, 600, 528, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 396, 576, 1);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 708, 576, 46);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 468, 432, 55);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 660, 432, 56);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 492, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 540, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 588, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 636, 432, 54);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 492, 432, 22);
			arrayCounter++;
			buildings[arrayCounter] = new WallDecor_Building(gp, 624, 432, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 564, 53);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 456, 288, 47);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 552, 288, 47);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 648, 288, 47);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 408, 276, 0);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 696, 276, 1);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 504, 276, 50);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 480, 468, 48);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 468, 49);
			arrayCounter++;
			buildings[arrayCounter] = new ToiletDoor(gp, 384, 396, 0);
			arrayCounter++;
			buildings[arrayCounter] = new ToiletDoor(gp, 672, 396, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 516, 468, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Candle(gp, 636, 468, 0);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 432, 180);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 672, 180);
			arrayCounter++;
			break;
		case 5:
			door = new Door(gp, 420, 504+48, 1, 0);
			door.setDoorNum(3);
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
			buildings[arrayCounter] = new FloorDecor_Building(gp, 696, 372, 63);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 384, 240, 62);
			arrayCounter++;
			buildings[arrayCounter] = new FloorDecor_Building(gp, 600, 276, 48);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 444, 168);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 660, 168);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 588, 168);
			arrayCounter++;
			buildings[arrayCounter] = new Window(gp, 516, 168);
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
			buildings[arrayCounter] = new Lantern(gp, 480, 132);
			arrayCounter++;
			buildings[arrayCounter] = new Lantern(gp, 624, 132);
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
		}
		buildingArrayCounter = arrayCounter;
	}
	private void setNPCs(int preset) {
		int arrayCounter = 0;
		switch(preset) {
		case 0: 
			
			break;
		}
	}
	public void addBuilding(Building building) {
		buildings[buildingArrayCounter] = building;
		buildingArrayCounter++;
	}
	public RoomSaveData saveRoomData() {
		RoomSaveData data = new RoomSaveData();
		data.roomNum = preset;
		data.wallpaper = wallpaper.preset;
		data.floorpaper = floorpaper.preset;
		data.beam = beam.preset;
		List<Building> buildList = new ArrayList<>();
		for(Building b: buildings) {
			if(b != null) {
				buildList.add(b);
			}
		}
		data.buildings = gp.buildingRegistry.saveBuildings(buildList);
		data.buildingArrayCounter = buildingArrayCounter;
		
		return data;
	}
	public void applySaveData(RoomSaveData data) {
		preset = data.roomNum;
		lights.clear();
		wallpaper = new WallPaper(gp, data.wallpaper);
		floorpaper = new FloorPaper(gp, data.floorpaper);
		beam = new Beam(gp, data.beam);
		List<Building> buildList = gp.buildingRegistry.unpackSavedBuildings(data.buildings);
		Building[] newBuilds = new Building[250];
		int counter = 0;
		buildings = null;
		for(Building b: buildList) {
			newBuilds[counter] = b;
			counter++;
		}
		buildingArrayCounter = data.buildingArrayCounter;
		buildings = newBuilds;
		if(preset == gp.player.currentRoomIndex) {
			gp.lightingM.setLights(lights);
			gp.buildingM.setBuildings(newBuilds);
			gp.buildingM.setArrayCounter(buildingArrayCounter);
		}
	}
	private void setItems(int preset) {
		
	}
    public void setWallpaper(int preset) {
    	this.wallpaper = new WallPaper(gp, preset);
    }
    public void setWallpaper(WallPaper wallpaper) {
    	gp.customiser.addToInventory(this.wallpaper);
    	this.wallpaper = wallpaper;
    	
    }
    public void setFloorpaper(int preset) {
    	this.floorpaper = new FloorPaper(gp, preset);
    }
    public void setFloorpaper(FloorPaper floorpaper) {
    	gp.customiser.addToInventory(this.floorpaper);
    	this.floorpaper = floorpaper;
    }
    public void setBeam(Beam beam) {
    	gp.customiser.addToInventory(this.beam);
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
					if(chair.available) { //NEED TO ADD CHECK IF TABLE IS CLEAR
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
    	Customer customer = new Customer(gp, 10*48, 11*48);
    	npcs.add(customer);
    }
	public Chair findFreeChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate) {
						chair.available = false;
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Door findToiletDoor() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.roomNum == 4) {
						return door;
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
	public Door findExitDoor() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.roomNum == 0) {
						return door;
					}
				}	
			}
		}
		return null;
	}
	public void addSpecialCustomer() {
		npcs.add(new SpecialCustomer(gp, 10*48, 9*48));
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
		if(a == 0) {
			buildings[buildingArrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
			buildingArrayCounter++;
		} else if(a == 1) {
			buildings[buildingArrayCounter] = new Spill(gp, 9*gp.tileSize - 24, 9*gp.tileSize);
			buildingArrayCounter++;
		}
	}
	public void addLight(LightSource light) {
		lights.add(light);
	}
    
    public void update() {
    	for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.update(); //Draws the item
            }
        }
    	for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
            	i.update(); //Draws the item
            }
        }
    	
    	for (NPC i : new ArrayList<>(npcs)) {
    	    if (i != null) {
    	        i.update();
    	    }
    	}
    }
}

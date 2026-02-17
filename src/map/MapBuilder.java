package map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

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
import entity.buildings.Computer;
import entity.buildings.CornerTable;
import entity.buildings.CursedDecor;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Freezer;
import entity.buildings.Fridge;
import entity.buildings.Gate;
import entity.buildings.Lantern;
import entity.buildings.MenuSign;
import entity.buildings.Oven;
import entity.buildings.RoomSpawn;
import entity.buildings.Sink;
import entity.buildings.SoulLantern;
import entity.buildings.StorageFridge;
import entity.buildings.Stove;
import entity.buildings.Table;
import entity.buildings.Toilet;
import entity.buildings.ToiletDoor;
import entity.buildings.Torch;
import entity.buildings.Trapdoor;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import entity.buildings.outdoor.OutdoorDecor;
import entity.buildings.outdoor.OutdoorWallDecor;
import entity.buildings.outdoor.SeasonalDecoration;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class MapBuilder {
	
	GamePanel gp;
	Random r;
	
	Colour c = new Colour(40, 40, 40, 200);
	BitmapFont font;
	
	public int selectedTile = -1;
	private int ySize = 600;
	int yStart;
	private int currentLayer = 1;
	private int totalBuildingCount = 0;
	private double clickCounter = 0;
	
	public boolean showTiles = false;
	private float scrollOffset = 0;
	private float scrollSpeed = 25; // pixels per scroll tick
	
	public int pageNum = 1;
	private Building[] buildings, outdoorBuildings;
	public Building selectedBuilding;
	
	public MapBuilder(GamePanel gp) {
		this.gp = gp;
		r = new Random();
		yStart = gp.frameHeight-ySize+4;
		buildings = new Building[500];
		outdoorBuildings = new Building[500];
		addBuildings();
		font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
	}
	
	private void addBuildings() {
		for(int i = 0; i < 117; i++) {
			buildings[totalBuildingCount] = new FloorDecor_Building(gp, 0, 0, i);
			totalBuildingCount++;
		}
		for(int i = 0; i < 36; i++) {
			buildings[totalBuildingCount] = new CursedDecor(gp, 0, 0, i);
			totalBuildingCount++;
		}
		for(int i = 0; i < 32; i++) {
			buildings[totalBuildingCount] = new WallDecor_Building(gp, 0, 0, i);
			totalBuildingCount++;
		}

		buildings[totalBuildingCount] = new Freezer(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Bin(gp, 0, 0, 2);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Bin(gp, 0, 0, 1);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Computer(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new ChefPortrait(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Torch(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Door(gp, 0, 0, 0, 2);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new RoomSpawn(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Gate(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new SoulLantern(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Cauldron(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Chair(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Table(gp, 0, 0, "Left", false);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new StorageFridge(gp, 0, 0, false);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Stove(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Sink(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new ChoppingBoard(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new CornerTable(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new CornerTable(gp, 0, 0, 1);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new CornerTable(gp, 0, 0, 2);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new CornerTable(gp, 0, 0, 3);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Bin(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Door(gp, 0, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Door(gp, 0, 0, 1, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Door(gp, 0, 0, 2, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Door(gp, 0, 0, 3, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Fridge(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new MenuSign(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Oven(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Breaker(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new ToiletDoor(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Toilet(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Toilet(gp, 0, 0, 1);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Bed(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Calendar(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Window(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new EscapeHole(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Lantern(gp, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Candle(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Candle(gp, 0, 0, 1);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Trapdoor(gp, 0, 0, 0);
		totalBuildingCount++;
		buildings[totalBuildingCount] = new Trapdoor(gp, 0, 0, 1);
		totalBuildingCount++;
		
		for(int i = 0; i < 10; i++) {
			buildings[totalBuildingCount] = new FoodStore(gp, 0, 0, i);
			totalBuildingCount++;
		}
		for(int i = 0; i < 38; i++) {
			outdoorBuildings[totalBuildingCount] = new OutdoorDecor(gp, 0, 0, i);
			totalBuildingCount++;
		}
		for(int i = 0; i < 7; i++) {
			outdoorBuildings[totalBuildingCount] = new OutdoorWallDecor(gp, 0, 0, i);
			totalBuildingCount++;
		}
		for(int i = 0; i < 14; i++) {
			outdoorBuildings[totalBuildingCount] = new SeasonalDecoration(gp, 0, 0, i);
			totalBuildingCount++;
		}
		outdoorBuildings[totalBuildingCount] = new Door(gp, 0, 0, 0, 1);
		totalBuildingCount++;
	}
	
	public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
	
	private boolean containsMouse(int x, int y, int w, int h) {
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		
		if(mouseX > x && mouseX < x+w && mouseY>y && mouseY <y+h) {
			return true;
		}
		return false;
	}
	
	public void switchSides() {
		if(yStart == gp.frameHeight-ySize+4) {
			yStart = 0;
		} else {
			yStart = gp.frameHeight-ySize+4;
		}
	}
	public void upLayer() {
		currentLayer++;
		if(currentLayer == 4) {
			currentLayer = 3;
		}
	}
	
	public void downLayer() {
		currentLayer--;
		if(currentLayer == -1) {
			currentLayer = 0;
		}
	}
	
	private void writeToMap(int xTile, int yTile) {
		if(selectedTile != -1) {
			
			int originalTile = selectedTile;
		
			
			switch(currentLayer) {
			case 0:
				gp.world.mapM.currentRoom.mapGrid[0][xTile][yTile] = selectedTile;
				break;
			case 1:
				gp.world.mapM.currentRoom.mapGrid[1][xTile][yTile] = selectedTile;
				break;
			case 2:
				gp.world.mapM.currentRoom.mapGrid[2][xTile][yTile] = selectedTile;
				break;
			case 3:
				gp.world.mapM.currentRoom.mapGrid[3][xTile][yTile] = selectedTile;
				break;
			}
			
			selectedTile = originalTile;
		}
		
		//SAVE TO MAP
		try {
			File[] files = new File("/Users/josswingfield/Documents/Programming/Java/Pandemonia/res/maps" + gp.world.mapM.currentRoom.getRoomID()).listFiles();
			File f = null;
		    
		    // Traverse through the files array
		    for (File file : files) {
		      // If a subdirectory is found,
		      // print the name of the subdirectory
		    	  if (!file.isDirectory()) {
		    		  String tag = gp.world.mapM.currentRoom.getRoomIDTag();
		    		  if(file.getName().contains(tag)) {
		    			  String num = Integer.toString(currentLayer);
		    			  if(file.getName().contains(num)) {
		    				  f = file;
		    			  }
		    		  }
			    	 }
		    }
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			
			int col=0; 
			int row=0; 
			int maxWorldCol = gp.world.mapM.currentRoom.mapWidth;
			int maxWorldRow = gp.world.mapM.currentRoom.mapHeight;
			
			while(col < maxWorldCol && row < maxWorldRow){
				
				switch(currentLayer) {
				case 0:
					bw.write(gp.world.mapM.currentRoom.mapGrid[0][col][row]+" ");
					break;
				case 1:
					bw.write(gp.world.mapM.currentRoom.mapGrid[1][col][row]+" ");
					break;
				case 2:
					bw.write(gp.world.mapM.currentRoom.mapGrid[2][col][row]+" ");
					break;
				case 3:
					bw.write(gp.world.mapM.currentRoom.mapGrid[3][col][row]+" ");
					break;
				}
				col++;
			
				if(col == maxWorldCol) {						
					col = 0;
					row++;
					bw.newLine();
				}								
			}
			bw.close();
			
		} catch (Exception e) {			
			//System.out.println(e.getMessage());
		}
	}
	
	public void updateState(double dt) {
		
		
	}
	public void inputUpdate(double dt) {
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		
		if(gp.mouseL.mouseButtonDown(0) && (mouseY < gp.frameHeight-ySize || !showTiles)) {
			int xTile = (int)((mouseX)/gp.tileSize);
			int yTile = (int)((mouseY)/gp.tileSize);
			
			writeToMap(xTile, yTile);
		}
		
		if(clickCounter > 0) {
			clickCounter-=dt;
			if(clickCounter < 0) {
				clickCounter = 0;
			}
		}
		
	}
	
	public void draw(Renderer renderer) {
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		int yOffset = 32;
		
		if(showTiles) {
			renderer.fillRect(0, yStart, gp.frameWidth, ySize, c);
			int xStart = 20;
			yStart = gp.frameHeight-ySize+4 - 20 + yOffset;

			TextureRegion img = importImage("/tiles/flooring/Floor1.png").toTextureRegion();
				
			if(pageNum == 1) {
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				int counter = 0;
				for(int i = 0; i < 8; i++) {
			    	for(int j = 0; j < 7; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			renderer.fillRect(xStart + j*32, yStart + i*32, 32, 32, Colour.YELLOW);
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter+1;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				img = importImage("/tiles/flooring/Floor5.png").toTextureRegion();
				int num = 250;
				xStart+=num;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				counter = 182;
				for(int i = 0; i < 8; i++) {
			    	for(int j = 0; j < 7; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter+1;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				xStart -= num;
				
				img = importImage("/tiles/flooring/Floor12.png").toTextureRegion();
				num = 500;
				xStart+=num;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				counter = 1054;
				for(int i = 0; i < 8; i++) {
			    	for(int j = 0; j < 7; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				xStart -= num;
				
				img = importImage("/tiles/flooring/Floor8.png").toTextureRegion();
				num = 800;
				xStart+=num;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				counter = 1134;
				for(int i = 0; i < 8; i++) {
			    	for(int j = 0; j < 7; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				xStart -= num;
				
			} else if (pageNum == 2) {
				img = importImage("/tiles/walls/Wall18.png").toTextureRegion();
				
				int counter = 57;
				xStart = 20;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/beams/Beam 1.png").toTextureRegion();
				
				xStart = 200;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				for(int i = 0; i < 3; i++) {
			    	for(int j = 0; j < 6; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/beams/Column.png").toTextureRegion();
				
				xStart = 20;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 250;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 14; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/walls/Wall19.png").toTextureRegion();
				
				xStart = 600;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				counter = 239;
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/beams/Beam 2.png").toTextureRegion();
				
				xStart = 800;
				//yStart = gp.frameHeight-ySize+4 - 40 + yOffset+250;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				counter = 263;
				for(int i = 0; i < 3; i++) {
			    	for(int j = 0; j < 6; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/walls/Wall20.png").toTextureRegion();
				
				xStart = 400;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				counter = 1110;
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/walls/Wall2.png").toTextureRegion();
				
				xStart = 600;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 200;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				counter = 1190;
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				img = importImage("/tiles/walls/Wall10.png").toTextureRegion();
				
				xStart = 800;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 200;
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
					
				counter = 1214;
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			    }
				
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				
			} else if (pageNum == 3) {
			    // --- Scroll state ---
			    if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_P)) scrollOffset -= 40;
			    if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_O)) scrollOffset += 40;	    

			    // Clamp scroll
			    int totalBuildings = buildings.length;
			    int columns = 17; // since you reset xStart after 16 columns (0..16)
			    int rows = (int) Math.ceil(totalBuildings / (float) columns);
			    int visibleRows = 5; // how many rows fit in panel
			    int rowHeight = 32 * 2;
			    float maxScroll = Math.max(0, (rows - visibleRows) * rowHeight);
			    scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

			    // --- Draw buildings with scroll offset ---
			    int counter = 0;
			    int originalXStart = xStart;
			    int yPos = yStart + 32 - (int) scrollOffset; // apply scroll here
			    int startDraw = 0;

			    int index = 0;
			    for (Building b : buildings) {
			        if (index >= startDraw) {
			            if (b != null) {
			                // only draw if visible (for performance)
			                if (yPos + rowHeight > yStart && yPos < gp.frameHeight) {
			                    img = b.animations[0][0][0];
			                    renderer.draw(b.animations[0][0][0], xStart, yPos, img.getPixelWidth()/2 * 3, img.getPixelHeight()/2 * 3);
			                    if (containsMouse(xStart, yPos, 32 * 2, 32 * 2)) {
			                        renderer.fillRect(xStart, yPos, 32 * 2, 32 * 2, Colour.YELLOW);
			                        if (gp.mouseL.mouseButtonDown(0)) {
			                            selectedBuilding = b;
			                        }
			                    }
			                }

			                xStart += 32 * 2;
			                if (counter >= 16) {
			                    xStart = originalXStart;
			                    yPos += 32 * 2;
			                    counter = 0;
			                }
			            }
			        } else {
			            counter = 0;
			        }
			        counter++;
			        index++;
			    }
			} else if(pageNum == 4) {
				img = importImage("/tiles/spring/Grass1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				int counter = 281;
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 400;
				img = importImage("/tiles/spring/Grass2.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 800;
				img = importImage("/tiles/dirt/Dirt1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 0;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 250;
				img = importImage("/tiles/dirt/Dirt2.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 400;
				img = importImage("/tiles/spring/oldwater1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 10; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 770;
				img = importImage("/tiles/spring/Water.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 1; i++) {
			    	for(int j = 0; j < 1; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 806;
				img = importImage("/tiles/spring/altwater1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
			}  else if(pageNum == 5) {
				int counter = 0;
				xStart = 0;
				int originalXStart = xStart;
				int yPos = yStart+32;
				
				int startDraw = 0;
				
				int index = 0;
				for(Building b: outdoorBuildings) {
					if(index >= startDraw) {
						if(b != null) {
							renderer.draw(b.animations[0][0][0], xStart, yPos, img.getPixelWidth()/2, img.getPixelHeight()/2);
			    			if(containsMouse(xStart, yPos, 32*2, 32*2)) {
			    				
			    				
			    				renderer.fillRect(xStart, yPos, 32*2, 32*2, Colour.YELLOW);
			    				if(gp.mouseL.mouseButtonDown(0)) {
			    					selectedBuilding = b;
			    				}
			    			}
			    			xStart+= 32*2;
			    			if(counter >= 14) {
			    				xStart = originalXStart;
			    				yPos += 32*2;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 6) {
				xStart = 0;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
				int counter = 667;
				img = importImage("/tiles/dirt/Dirt Wall.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 4; i++) {
			    	for(int j = 0; j < 4; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 200;
				img = importImage("/tiles/spring/Grass3.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 600;
				img = importImage("/tiles/spring/Grass4.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 0;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 200;

				img = importImage("/environment/Pavement.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 7; i++) {
			    	for(int j = 0; j < 9; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 300;

				img = importImage("/tiles/summer/water1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 3; i++) {
			    	for(int j = 0; j < 5; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				xStart = 500;

				img = importImage("/tiles/dirt/DirtWall2.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 6; i++) {
			    	for(int j = 0; j < 8; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 750;
				img = importImage("/buildings/Stairs.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 12; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 0;
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset + 450;
				img = importImage("/tiles/dirt/SimpleDirt1.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 4; i++) {
			    	for(int j = 0; j < 5; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				xStart = 250;
				img = importImage("/tiles/dirt/SimpleDirt3.png").toTextureRegion();
				renderer.draw(img, xStart, yStart, img.getPixelWidth()*2, img.getPixelHeight()*2);
				
				for(int i = 0; i < 5; i++) {
			    	for(int j = 0; j < 11; j++) {
			    			
			    		if(containsMouse(xStart + j*32, yStart + i*32, 32, 32)) {
			    			
			    			if(gp.mouseL.mouseButtonDown(0)) {
			    				selectedTile = counter;
			    			}
			    		}
			    		counter++;
			    	}
			   }
				
				yStart = gp.frameHeight-ySize+4 - 40 + yOffset;
			}
			
			String s = Integer.toString(currentLayer);
			renderer.drawString(font, s, gp.frameWidth - 100, gp.frameHeight - 100, 1.0f, Colour.WHITE);
			
			s = Integer.toString(selectedTile);
			renderer.drawString(font, s, gp.frameWidth - 100, gp.frameHeight - 100 + 40, 1.0f, Colour.WHITE);
		}
		
		if(selectedTile != -1) {
			
			if(mouseY < gp.frameHeight-ySize || !showTiles) {
				int xPos = (int)((mouseX)/gp.tileSize) * gp.tileSize;
				int yPos = (int)((mouseY)/gp.tileSize) * gp.tileSize;
				renderer.draw(gp.world.mapM.tiles[selectedTile].image, xPos, yPos, 48, 48);
			}
		} 
		
		if(selectedBuilding != null) {
			selectedTile = -1;
			if(mouseY < gp.frameHeight-ySize || !showTiles) {
				int size = 4*3;
				int xPos = (int)((mouseX)/size) * size;
				int yPos = (int)((mouseY)/size) * size;
				renderer.draw(selectedBuilding.animations[0][0][0], xPos - selectedBuilding.xDrawOffset, yPos - selectedBuilding.yDrawOffset, selectedBuilding.animations[0][0][0].getPixelWidth()*3, selectedBuilding.animations[0][0][0].getPixelHeight()*3);
				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
					gp.world.buildingM.addBuilding((Building)selectedBuilding.clone(), xPos, yPos);
					clickCounter = 0.1;
				}
			}
		}
		
		for(Building b: gp.world.buildingM.getBuildings()) {
			if(b != null) {
				if(b.hitbox.contains(mouseX, mouseY)) {
					if(gp.mouseL.mouseButtonDown(1)) {
						b.destroy();
						gp.world.buildingM.removeBuilding(b);
					}
				}
			}
		}
		
	
	}
}

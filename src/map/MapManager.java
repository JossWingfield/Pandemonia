package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.imageio.ImageIO;

import entity.buildings.Building;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.Fridge;
import entity.buildings.Sink;
import entity.buildings.StorageFridge;
import entity.buildings.TablePlate;
import entity.buildings.Trapdoor;
import entity.npc.Customer;
import entity.npc.NPC;
import main.GamePanel;
import net.packets.Packet02Move;
import net.packets.Packet05ChangeRoom;
import utility.RoomHelperMethods;
import utility.Season;

public class MapManager {

	  GamePanel gp;

	  int screenWidth, screenHeight; //The size of the screen
      public Tile[] tiles; //The array of tiles, the index corresponding to the number in the map text file
	  public int currentMapWidth, currentMapHeight; //The height of the current map
	  public boolean drawPath = false;
	  public Room currentRoom;
	  private Room[] rooms;
	  
	  private boolean changingPhase = false;
	  
	  private int arrayIndex = 0;
	  public int renderDistance = 3;
	  public int chunkSize = 8;

	    public MapManager(GamePanel gp) { //Sets default variables
	        this.gp = gp;

	        tiles = new Tile[2500];

	        loadRooms();

	        screenWidth = gp.tilesInWidth;
	        screenHeight = gp.tilesInHeight;

	        importTiles();
	    }
	    private void loadRooms() {
	        currentRoom = new Room(gp, 0);
	        gp.buildingM.setBuildings(currentRoom.getBuildings());
	        gp.buildingM.setArrayCounter(currentRoom.buildingArrayCounter);
	        
	        rooms = new Room[10];
	        rooms[0] = currentRoom; //Main
	        rooms[1] = new Room(gp, 1); //Stores
	        //rooms[2] = new Room(gp, 2); //Outdoors
	        rooms[3] = new Room(gp, 3); //Electrics
	        rooms[4] = new Room(gp, 4); //Toilet
	        rooms[5] = new Room(gp, 5); //Bedroom
	        rooms[6] = new Room(gp, 6); //Basement
	        
	        currentMapHeight = currentRoom.mapHeight;
	        currentMapWidth = currentRoom.mapWidth;
	    }
	    //Imports each individual tile
	    public void importTiles() {
	        //Sets the booleans for each tile    
	    	tiles[arrayIndex] = new Tile(gp, "/tiles/Air.png", importImage("/tiles/Air.png"));
	    	tiles[arrayIndex].solid = true;
	        arrayIndex++;
	        
	        importFloorFromSpriteSheet("/tiles/flooring/Floor1", 8, 7, false);
	        importWallFromSpriteSheet("/tiles/walls/Wall18", 6, 4, true);
	        importBeamFromSpriteSheet("/tiles/beams/Beam 1", 3, 6, true);
	        importTilesFromSpriteSheet("/tiles/beams/Column", 6, 14, false);
	        importFloorFromSpriteSheet("/tiles/flooring/Floor5", 8, 7, false);
	        importWallFromSpriteSheet("/tiles/walls/Wall19", 6, 4, true);
	        importBeamFromSpriteSheet("/tiles/beams/Beam 2", 3, 6, true);
	        importTilesFromSpriteSheet("Grass1", 5, 11, false, true);
	        importTilesFromSpriteSheet("Grass2", 5, 11, false, true);
	        importTilesFromSpriteSheet("/tiles/dirt/Dirt1", 5, 11, false);
	        importTilesFromSpriteSheet("/tiles/dirt/Dirt2", 5, 11, false);
	        importTilesFromSpriteSheet("oldwater1", 10, 11, true, true);
	        importTilesFromSpriteSheet("Water", 1, 1, true, true);
	        importTilesFromSpriteSheet("altwater1", 5, 11, true, true);
	        importTilesFromSpriteSheet("/tiles/dirt/Dirt Wall", 4, 4, true);
	        importTilesFromSpriteSheet("Grass3", 5, 11, false, true);
	        importTilesFromSpriteSheet("Grass4", 5, 11, false, true);
	        importTilesFromSpriteSheet("/environment/Pavement", 7, 9, false);
	        importTilesFromSpriteSheet("water1", 3, 5, true, true);
	        importTilesFromSpriteSheet("/tiles/dirt/DirtWall2", 6, 8, true);
	        importTilesFromSpriteSheet("/buildings/Stairs", 5, 12, false);
	        importTilesFromSpriteSheet("/tiles/dirt/SimpleDirt1", 4, 5, false);
	        importTilesFromSpriteSheet("/tiles/dirt/SimpleDirt3", 5, 11, false);
	        importFloorFromSpriteSheet("/tiles/flooring/Floor12", 8, 7, false);
	        
	    	tiles[arrayIndex] = new Tile(gp, "/tiles/Air.png", importImage("/tiles/Air.png"));
	        arrayIndex++;

	    }
	    public Room[] getRooms() {
	    	return rooms;
	    }
	    public Room getRoom(int i) {
	    	return rooms[i];
	    }
	    private void importTilesFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
	        BufferedImage img = importImage(filePath+".png");
	        int tileSize = 16;
	        for(int j = 0; j < rows; j++) {
	            for(int i = 0; i < columns; i++) {
	                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
	                tiles[arrayIndex].solid = solid;
	                arrayIndex++;
	            }
	        }
	    }
	    private void importWallFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
	        BufferedImage img = importImage(filePath+".png");
	        int tileSize = 16;
	        for(int j = 0; j < rows; j++) {
	            for(int i = 0; i < columns; i++) {
	                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
	                tiles[arrayIndex].solid = solid;
	                tiles[arrayIndex].isWall = true;
	                arrayIndex++;
	            }
	        }
	    }
	    private void importFloorFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
	        BufferedImage img = importImage(filePath+".png");
	        int tileSize = 16;
	        for(int j = 0; j < rows; j++) {
	            for(int i = 0; i < columns; i++) {
	                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
	                tiles[arrayIndex].solid = solid;
	                tiles[arrayIndex].isFloor = true;
	                arrayIndex++;
	            }
	        }
	    }
	    private void importBeamFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
	        BufferedImage img = importImage(filePath+".png");
	        int tileSize = 16;
	        for(int j = 0; j < rows; j++) {
	            for(int i = 0; i < columns; i++) {
	                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
	                tiles[arrayIndex].solid = solid;
	                tiles[arrayIndex].isBeam = true;
	                arrayIndex++;
	            }
	        }
	    }
	    private void importTilesFromSpriteSheet(String filePath, int rows, int columns, boolean solid, boolean isSeasonal) {
	    	for(int j = 0; j < rows; j++) {
	            for(int i = 0; i < columns; i++) {
	                tiles[arrayIndex] =  new Tile(gp, filePath, i, j, isSeasonal);
	                tiles[arrayIndex].solid = solid;
	                arrayIndex++;
	            }
	        }
	    }
	    private BufferedImage importImage(String filePath) { //Imports and stores the image
	        BufferedImage importedImage = null;
	        try {
	            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
	        } catch(IOException e) {
	            e.printStackTrace();
	        }
	        return importedImage;
	    }
	    public void update() {
	    		    	
	    	for(Room room: rooms) {
	    		if(room != null) {
	    			if(currentRoom != room) {
	    				if(room.containsAnyNPC() || room.getRoomType().equals("Main")) {
	    					room.update();
	    				}
	    			}
	    		}
	    	}
	    	
	    }
	    public void addNPCToRoom(NPC npc, int roomCounter) {
	        Room room = rooms[roomCounter];
	        if (!room.getNPCs().contains(npc)) {
	            room.addNPC(npc);
	        }
	        if (room == currentRoom && !gp.npcM.getNPCs().contains(npc)) {
	            gp.npcM.addNPC(npc);
	        }
	    }
	    public boolean isInRoom(int index) {
	    	if(changingPhase) {
	    		return false;
	    	}
	    	return rooms[index].equals(currentRoom);
	    }
	    public boolean isInRoom(String roomType) {
	    	return currentRoom.getRoomType().equals(roomType);
	    }
	    public void removeNPCFromRoom(NPC npc, int roomCounter) {
	        Room room = rooms[roomCounter];
	        room.removeNPC(npc); // always remove from the room
	        if (room == currentRoom) {
	            gp.npcM.removeNPC(npc); // also mirror to global if it's the active room
	        }
	    }
	    public void enterNewPhase() {
	    	changingPhase = true;
    		gp.customiser.clear();
	    	for(Object o: gp.world.boughtItems) {
	    		if(o instanceof FloorPaper f) {
	    			gp.customiser.addToInventory(f);
    			} else if(o instanceof WallPaper f) {
    				gp.customiser.addToInventory(f);
    			} else if(o instanceof Beam f) {
    				gp.customiser.addToInventory(f);
    			} else if(o instanceof ChairSkin f) {
    				gp.customiser.addToInventory(f);
    			} else if(o instanceof TableSkin f) {
    				gp.customiser.addToInventory(f);
    			} else if(o instanceof Building f) {
    				gp.customiser.addToInventory(f);
    			}
	    	}
	    	gp.lightingM.clearLights();
	    	loadRooms();
	    	gp.lightingM.clearRoomOcclusionCache();
	    	gp.lightingM.getRoomOcclusion();
	    	gp.player.currentRoomIndex = 0;
	    	gp.player.hitbox.x = 8*48;
	    	gp.player.hitbox.y = 12*48;
	    	changingPhase = false;
	    }
	    public void setRoom(int roomNum) {
	    	gp.lightingM.removeLight(gp.player.playerLight);
	    	gp.world.removeLightning();
	    	currentRoom.editBuildings(gp.buildingM.getBuildings(), gp.buildingM.getArrayIndex());
	    	currentRoom.editNPCs(gp.npcM.getNPCs());
	    	currentRoom.editItems(gp.itemM.getItems());
	    	currentRoom.editLights(gp.lightingM.getLights());
	    	currentRoom = rooms[roomNum];
	    	gp.buildingM.setBuildings(currentRoom.getBuildings());
	    	gp.npcM.setNPCs(currentRoom.getNPCs());
	    	gp.itemM.setItems(currentRoom.getItems());
	    	gp.lightingM.setLights(currentRoom.getLights());
	    	gp.buildingM.setArrayCounter(currentRoom.buildingArrayCounter);
	    	if(!gp.world.isPowerOn()) {
	    		gp.lightingM.addLight(gp.player.playerLight);
	    	}

	    	currentMapWidth = currentRoom.mapWidth;
	    	currentMapHeight = currentRoom.mapHeight;
	    	gp.lightingM.getRoomOcclusion();
	    }
	    public void changeRoom(int roomNum, Door previousDoor) {
	    	gp.lightingM.removeLight(gp.player.playerLight);
	    	gp.world.removeLightning();
	    	currentRoom.editBuildings(gp.buildingM.getBuildings(), gp.buildingM.getArrayIndex());
	    	currentRoom.editNPCs(gp.npcM.getNPCs());
	    	currentRoom.editItems(gp.itemM.getItems());
	    	currentRoom.editLights(gp.lightingM.getLights());
	    	currentRoom = rooms[roomNum];
	    	gp.buildingM.setBuildings(currentRoom.getBuildings());
	    	gp.npcM.setNPCs(currentRoom.getNPCs());
	    	gp.itemM.setItems(currentRoom.getItems());
	    	gp.lightingM.setLights(currentRoom.getLights());
	    	gp.buildingM.setArrayCounter(currentRoom.buildingArrayCounter);
	    	if(!gp.world.isPowerOn()) {
	    		gp.lightingM.addLight(gp.player.playerLight);
	    	}
	    	
	    	Door door = (Door)gp.buildingM.findCorrectDoor(previousDoor.facing);
	    	if(door != null) {
    			gp.player.hitbox.x = door.hitbox.x + door.hitbox.width/2 - gp.player.hitbox.width/2;
	    		if(door.facing == 0) {
		    		gp.player.hitbox.y = door.hitbox.y+door.hitbox.height-48;
	    		} else {
	    			gp.player.hitbox.y = door.hitbox.y+16-48;
	    		}
	    		if(door.facing == 2) {
	    			gp.player.hitbox.x = door.hitbox.x;
	    			gp.player.hitbox.y = door.hitbox.y+80-48;
	    		} else if(door.facing == 3) {
	    			gp.player.hitbox.x = door.hitbox.x;
	    			gp.player.hitbox.y = door.hitbox.y+80-48;
	    		}
	    	}
	    	gp.buildingM.setDoorCooldowns();
	    	currentMapWidth = currentRoom.mapWidth;
	    	currentMapHeight = currentRoom.mapHeight;
	    	gp.player.currentRoomIndex = roomNum;
	    	if(gp.multiplayer) {
	            Packet05ChangeRoom packet = new Packet05ChangeRoom(gp.player.getUsername(), roomNum);
	            packet.writeData(gp.socketClient);
            }
	    	gp.lightingM.getRoomOcclusion();
	    }
	    public void changeRoom(int roomNum, Trapdoor trapdoor) {
	    	gp.lightingM.removeLight(gp.player.playerLight);
	    	gp.world.removeLightning();
	    	currentRoom.editBuildings(gp.buildingM.getBuildings(), gp.buildingM.getArrayIndex());
	    	currentRoom.editNPCs(gp.npcM.getNPCs());
	    	currentRoom.editItems(gp.itemM.getItems());
	    	currentRoom.editLights(gp.lightingM.getLights());
	    	currentRoom = rooms[roomNum];
	    	gp.buildingM.setBuildings(currentRoom.getBuildings());
	    	gp.npcM.setNPCs(currentRoom.getNPCs());
	    	gp.itemM.setItems(currentRoom.getItems());
	    	gp.lightingM.setLights(currentRoom.getLights());
	    	gp.buildingM.setArrayCounter(currentRoom.buildingArrayCounter);
	    	if(!gp.world.isPowerOn()) {
	    		gp.lightingM.addLight(gp.player.playerLight);
	    	}
	    	
	    	Trapdoor newTrapdoor = (Trapdoor)gp.buildingM.findTrapdoor();
	    	if(newTrapdoor != null) {
    			gp.player.hitbox.x = newTrapdoor.hitbox.x + newTrapdoor.hitbox.width/2 - gp.player.hitbox.width/2;
	    		if(newTrapdoor.type == 0) {
	    			gp.player.hitbox.y = newTrapdoor.hitbox.y;
	    			gp.player.hitbox.x -= 24;
	    		} else {
	    			gp.player.hitbox.y = newTrapdoor.hitbox.y + newTrapdoor.hitbox.height;
	    		}
	    	}
	    	
	    	gp.buildingM.setDoorCooldowns();
	    	currentMapWidth = currentRoom.mapWidth;
	    	currentMapHeight = currentRoom.mapHeight;
	    	gp.player.currentRoomIndex = roomNum;
	    	if(gp.multiplayer) {
	            Packet05ChangeRoom packet = new Packet05ChangeRoom(gp.player.getUsername(), roomNum);
	            packet.writeData(gp.socketClient);
            }
	    	gp.lightingM.getRoomOcclusion();
	    }
	    public Room getCurrentRoom(NPC npc) {
	    	for(Room room: rooms) {
	    		if(room != null) {
		    		if(room.containsNPC(npc)) {
		    			return room;
		    		}
	    		}
	    	}
	    	if(gp.npcM.containsNPC(npc)) {
	    		return currentRoom;
	    	}
	    	return null;
	    }
	    public Building findCorrectDoorInRoom(int roomNum, int facing) {
	    	Room room = rooms[roomNum];
	    	Building[] buildings = room.getBuildings();
			for(Building b: buildings) {
				if(b != null) {
					if(b.getName().equals("Door 1")) {
						Door door = (Door)b;
						switch(facing) {
						case 0:
							if(door.facing == 1) {
								return b;
							}
							break;
						case 1:
							if(door.facing == 0) {
								return b;
							}
							break;
						case 2:
							if(door.facing == 3) {
								return b;
							}
							break;
						case 3:
							if(door.facing == 2) {
								return b;
							}
							break;
						}
					}
				}
			}
			return null;
		}

		public Customer findCustomerWaiting(int roomNum) {
			Customer c = null;
			if(isInRoom(roomNum)) {
				c = gp.npcM.findWaitingCustomer();
				return c;
			}
			Room room = rooms[roomNum];
			List<NPC> npcs = room.getNPCs();
			for(NPC b: npcs) {
				if(b != null) {
					if(b instanceof Customer customer) {
						if(customer.waitingToOrder) {
							if(customer.atTable) {
								return customer;
							}
						}
					}
				}
			}
			return null;
		}
		public boolean isRoomEmpty(int currentRoom) {
			if(isInRoom(currentRoom)) {
				return !gp.npcM.containsAnyNPC();
			} else {
				return !rooms[currentRoom].containsAnyNPC();
			}
		}
		public EscapeHole findEscapeHole(int roomNum) {
			Room room = rooms[roomNum];
			Building[] buildings = room.getBuildings();
			for(Building b: buildings) {
				if(b != null) {
					if(b.getName().equals("Escape Hole")) {
						EscapeHole escapeHole = (EscapeHole)b;
						return escapeHole;
					}	
				}
			}
			return null;
		}
	    public void setSeason(Season season) {
	        for (Tile tile : tiles) {
	            if (tile != null && tile.seasonal) {
	                switch (season) {
	                    case SPRING:
	                        tile.image = tile.springImage;
	                        break;
	                    case SUMMER:
	                        tile.image = tile.summerImage;
	                        break;
	                    case AUTUMN:
	                        tile.image = tile.autumnImage;
	                        break;
	                    case WINTER:
	                        tile.image = tile.winterImage;
	                        break;
	                }
	            }
	        }
	    }
	    public void draw(Graphics2D g2, float xDiff, float yDiff) {
	        drawBackground(g2, xDiff, yDiff);

	        drawLayer(g2, xDiff, yDiff, 1); // main floor layer
	        drawMidLayer(g2, xDiff, yDiff);

	        if (drawPath) {
	            g2.setColor(new Color(255, 220, 100, 80));
	            gp.npcM.drawPaths(g2);
	        }
	    }
	    //Draws the selected level
	    private void drawLayer(Graphics2D g, float xDiff, float yDiff, int layer) {
	        int tileSize = gp.tileSize;

	        // Convert camera offset to tile indices
	        int startCol = Math.max(0, (int)(xDiff / tileSize));
	        int startRow = Math.max(0, (int)(yDiff / tileSize));

	        int endCol = Math.min(currentMapWidth, (int)((xDiff + gp.screenWidth) / tileSize) + 1);
	        int endRow = Math.min(currentMapHeight, (int)((yDiff + gp.screenHeight) / tileSize) + 1);

	        for (int i = startCol; i < endCol; i++) {
	            for (int j = startRow; j < endRow; j++) {
	                int tileIndex = currentRoom.mapGrid[layer][i][j];
	                if(tileIndex == 0) {
	                	g.drawImage(tiles[tileIndex].image,i * tileSize - (int)xDiff,j * tileSize - (int)yDiff,tileSize,tileSize,null);
	                } else {
		                if (tileIndex < 0 || tileIndex >= tiles.length) continue; // safety check
		                
		                if(!tiles[tileIndex].isWall && !tiles[tileIndex].isFloor && !tiles[tileIndex].isBeam) {
		                	g.drawImage(tiles[tileIndex].image, i * tileSize - (int)xDiff,j * tileSize - (int)yDiff,tileSize,tileSize,null);
		                } else if(tiles[tileIndex].isWall){
		                	g.drawImage(currentRoom.getWallpaper().getImage(tileIndex), i * tileSize - (int)xDiff,j * tileSize - (int)yDiff,tileSize,tileSize, null);
		                } else if(tiles[tileIndex].isFloor){
		                	g.drawImage(currentRoom.getFloorpaper().getImage(tileIndex), i * tileSize - (int)xDiff,j * tileSize - (int)yDiff,tileSize,tileSize, null);
		                } else {
		                	g.drawImage(currentRoom.getBeam().getImage(tileIndex), i * tileSize - (int)xDiff,j * tileSize - (int)yDiff,tileSize,tileSize, null);
		                }
	                }
	            }
	        }
	    }
	    public void drawBackground(Graphics2D g, float xDiff, float yDiff) {
	        drawLayer(g, xDiff, yDiff, 0);
	    }
	    public void drawMidLayer(Graphics2D g, float xDiff, float yDiff) {
	        drawLayer(g, xDiff, yDiff, 2);
	    }
	    public void drawForeground(Graphics2D g, float xDiff, float yDiff) {
	    	drawLayer(g, xDiff, yDiff, 3);
	    }
}

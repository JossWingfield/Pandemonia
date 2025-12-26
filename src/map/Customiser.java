package map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import entity.buildings.Building;
import entity.buildings.Shelf;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;
import utility.Statistics;
import utility.save.CustomiserSaveData;

public class Customiser {
	
	GamePanel gp;
	
	Colour c = new Colour(51, 60, 58);
	BitmapFont font;
	
	private TextureRegion frame, buildFrame, buildFrameHighlight, buildFrameHighlight2, border;
	private TextureRegion decorTab, kitchenTab, floorTab, wallTab, storeTab, bathroomTab, beamTab, chairTab, counterTab, tableTab, panTab, doorTab;
	private TextureRegion decorTab2, kitchenTab2, floorTab2, wallTab2, storeTab2, bathroomTab2, beamTab2, chairTab2, counterTab2, tableTab2, panTab2, doorTab2;
	
	private int ySize = 126*3;
	int yStart;
	private double clickCounter = 0;
	public boolean showBuildings = true;
	
	private Colour noBuild = new Colour(239, 4, 4, 150);
	private Colour yesBuild = new Colour(48, 227, 13, 150);
	
	public int pageNum = 1;
	private List<Building> decorBuildingInventory = new ArrayList<Building>();
	private List<Building> kitchenBuildingInventory = new ArrayList<Building>();
	private List<FloorPaper> floorpaperInventory = new ArrayList<FloorPaper>();
	private List<WallPaper> wallpaperInventory = new ArrayList<WallPaper>();
	private List<Beam> beamInventory = new ArrayList<Beam>();
	private List<Building> storeBuildingInventory = new ArrayList<Building>();
	private List<Building> bathroomBuildingInventory = new ArrayList<Building>();
	private List<ChairSkin> chairSkinInventory = new ArrayList<ChairSkin>();
	private List<CounterSkin> counterSkinInventory = new ArrayList<CounterSkin>();
	private List<TableSkin> tableSkinInventory = new ArrayList<TableSkin>();
	private List<PanSkin> panSkinInventory = new ArrayList<PanSkin>();
	private List<DoorSkin> doorSkinInventory = new ArrayList<DoorSkin>();
	public Building selectedBuilding;
	
	private int prevX, prevY;
	private int tabDisplayNum = 0;
	
	public Customiser(GamePanel gp) {
		this.gp = gp;
		yStart = gp.frameHeight-ySize+4;
		frame = importImage("/UI/customise/CustomiseFrame.png").toTextureRegion();
		buildFrame = importImage("/UI/customise/BuildFrame.png").getSubimage(0, 0, 37, 37);
		buildFrameHighlight = importImage("/UI/customise/BuildFrame.png").getSubimage(37, 0, 37, 37);
		buildFrameHighlight2 = importImage("/UI/customise/BuildFrame.png").getSubimage(37*2, 0, 37, 37);
		decorTab = importImage("/UI/customise/BuildTab.png").getSubimage(34, 0, 34, 25);
		kitchenTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*2, 0, 34, 25);
		floorTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*3, 0, 34, 25);
		wallTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*4, 0, 34, 25);
		beamTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*5, 0, 34, 25);
		storeTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*6, 0, 34, 25);
		bathroomTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*7, 0, 34, 25);
		chairTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*8, 0, 34, 25);
		counterTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*9, 0, 34, 25);
		tableTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*10, 0, 34, 25);
		panTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*11, 0, 34, 25);
		doorTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*12, 0, 34, 25);
		
		decorTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34, 25, 34, 25);
		kitchenTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*2, 25, 34, 25);
		floorTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*3, 25, 34, 25);
		wallTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*4, 25, 34, 25);
		beamTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*5, 25, 34, 25);
		storeTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*6, 25, 34, 25);
		bathroomTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*7, 25, 34, 25);
		chairTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*8, 25, 34, 25);
		counterTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*9, 25, 34, 25);
		tableTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*10, 25, 34, 25);
		panTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*11, 25, 34, 25);
		doorTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*12, 25, 34, 25);
		
		border = importImage("/UI/customise/WallBorder.png").toTextureRegion();
		
		font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
	}
	public void clear() {
		decorBuildingInventory.clear();
		kitchenBuildingInventory.clear();
		floorpaperInventory.clear();
		wallpaperInventory.clear();
		beamInventory.clear();
		storeBuildingInventory.clear();
		bathroomBuildingInventory.clear();
		chairSkinInventory.clear();
		counterSkinInventory.clear();
		tableSkinInventory.clear();
		doorSkinInventory.clear();
		panSkinInventory.clear();
		
		selectedBuilding = null;
	}
	public CustomiserSaveData saveCustomiserData() {
		CustomiserSaveData data = new CustomiserSaveData();
		data.decorBuildingInventory = gp.buildingRegistry.saveBuildings(decorBuildingInventory);
		data.bathroomBuildingInventory = gp.buildingRegistry.saveBuildings(bathroomBuildingInventory);
		data.kitchenBuildingInventory = gp.buildingRegistry.saveBuildings(kitchenBuildingInventory);
		data.storeBuildingInventory = gp.buildingRegistry.saveBuildings(storeBuildingInventory);	
		
		List<Integer> beams = new ArrayList<>();
		for(Beam b: beamInventory) {
			beams.add(b.preset);
		}
		data.beamInventory = beams;
		List<Integer> floors = new ArrayList<>();
		for(FloorPaper b: floorpaperInventory) {
			floors.add(b.preset);
		}
		data.floorpaperInventory = floors;
		List<Integer> walls = new ArrayList<>();
		for(WallPaper b: wallpaperInventory) {
			walls.add(b.preset);
		}
		data.wallpaperInventory = walls;
		List<Integer> chairs = new ArrayList<>();
		for(ChairSkin b: chairSkinInventory) {
			chairs.add(b.preset);
		}
		data.chairSkinInventory = chairs;
		
		List<Integer> counters = new ArrayList<>();
		for(CounterSkin b: counterSkinInventory) {
			counters.add(b.preset);
		}
		data.counterSkinInventory = counters;
		
		List<Integer> tables = new ArrayList<>();
		for(TableSkin b: tableSkinInventory) {
			tables.add(b.preset);
		}
		data.tableSkinInventory = tables;
		
		List<Integer> pans = new ArrayList<>();
		for(PanSkin b: panSkinInventory) {
			pans.add(b.preset);
		}
		data.panSkinInventory = pans;
		
		List<Integer> doors = new ArrayList<>();
		for(DoorSkin b: doorSkinInventory) {
			doors.add(b.preset);
		}
		data.doorSkinInventory = doors;
		
		
		return data;
	}
	public void applySaveData(CustomiserSaveData data) {
		
		decorBuildingInventory = gp.buildingRegistry.unpackSavedBuildings(data.decorBuildingInventory);
		bathroomBuildingInventory = gp.buildingRegistry.unpackSavedBuildings(data.bathroomBuildingInventory);
		kitchenBuildingInventory = gp.buildingRegistry.unpackSavedBuildings(data.kitchenBuildingInventory);
		storeBuildingInventory = gp.buildingRegistry.unpackSavedBuildings(data.storeBuildingInventory);
		
		beamInventory.clear();
		for(Integer i: data.beamInventory) {
			beamInventory.add(new Beam(gp, i));
		}
		floorpaperInventory.clear();
		for(Integer i: data.floorpaperInventory) {
			floorpaperInventory.add(new FloorPaper(gp, i));
		}
		wallpaperInventory.clear();
		for(Integer i: data.wallpaperInventory) {
			wallpaperInventory.add(new WallPaper(gp, i));
		}
		chairSkinInventory.clear();
		for(Integer i: data.chairSkinInventory) {
			chairSkinInventory.add(new ChairSkin(gp, i));
		}
		
		counterSkinInventory.clear();
		for(Integer i: data.counterSkinInventory) {
			counterSkinInventory.add(new CounterSkin(gp, i));
		}
		
		tableSkinInventory.clear();
		for(Integer i: data.tableSkinInventory) {
			tableSkinInventory.add(new TableSkin(gp, i));
		}
		
		panSkinInventory.clear();
		for(Integer i: data.panSkinInventory) {
			panSkinInventory.add(new PanSkin(gp, i));
		}
		
		doorSkinInventory.clear();
		for(Integer i: data.doorSkinInventory) {
			doorSkinInventory.add(new DoorSkin(gp, i));
		}
	}
	private Texture importImage(String filePath) {
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
	public void addOrderToInventory(List<Object> order) {
		for(Object o: order) {
			if(o instanceof Building b) {
				addToInventory(b);
			} else if(o instanceof WallPaper w) {
				addToInventory(w);
			} else if(o instanceof FloorPaper w) {
				addToInventory(w);
			} else if(o instanceof Beam w) {
				addToInventory(w);
			} else if(o instanceof ChairSkin w) {
				addToInventory(w);
			} else if(o instanceof CounterSkin w) {
				addToInventory(w);
			} else if(o instanceof TableSkin w) {
				addToInventory(w);
			} else if(o instanceof PanSkin w) {
				addToInventory(w);
			} else if(o instanceof DoorSkin w) {
				addToInventory(w);
			}
		}
	}
	public void addToInventory(Building b) {
		if(b.isDecor) {
			decorBuildingInventory.add(b);
		} else if(b.isKitchenBuilding) {
			kitchenBuildingInventory.add(b);
		} else if(b.isStoreBuilding) {
			storeBuildingInventory.add(b);
		} else if(b.isBathroomBuilding) {
			bathroomBuildingInventory.add(b);
		}
	}
	public void addToInventory(WallPaper w) {
		wallpaperInventory.add(w);
	}
	public void addToInventory(FloorPaper f) {
		floorpaperInventory.add(f);
	}
	public void addToInventory(Beam b) {
		beamInventory.add(b);
	}
	public void addToInventory(ChairSkin b) {
		chairSkinInventory.add(b);
	}
	public void addToInventory(CounterSkin b) {
		counterSkinInventory.add(b);
	}
	public void addToInventory(TableSkin b) {
		tableSkinInventory.add(b);
	}
	public void addToInventory(PanSkin b) {
		panSkinInventory.add(b);
	}
	public void addToInventory(DoorSkin b) {
		doorSkinInventory.add(b);
	}
	public void update(double dt) {
		if (clickCounter > 0) {
			clickCounter -= dt;        // subtract elapsed time in seconds
		    if (clickCounter < 0) {
		    	clickCounter = 0;      // clamp to zero
		    }
		}
		if(clickCounter == 0) {
			if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
				tabDisplayNum--;
				clickCounter = 0.2;
				if(tabDisplayNum <= 0) {
					tabDisplayNum = 0;
				}
			} else if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
				tabDisplayNum++;
				clickCounter = 0.2;
				if(tabDisplayNum >= 1) {
					tabDisplayNum = 1;
				}
			}
		}
		/*
		if(gp.currentState == gp.customiseRestaurantState) {
        	if(!showBuildings) {
        		showBuildings = true;
        		selectedBuilding = null;
        	}
        }
        */
	}
	public void draw(Renderer renderer) {
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		int yOffset = 32;

		if(showBuildings) {
			renderer.draw(frame, 0, yStart, gp.frameWidth, ySize);
			int xStart = 20;
			yStart = gp.frameHeight-ySize+4 - 20 + yOffset;
			
			
			int xTabStart = -34*3 * tabDisplayNum;
			float fontScale = 1.0f;
			
			if(pageNum == 1) {
				renderer.draw(decorTab2, xTabStart, yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart, yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 1;
					}
				}
				renderer.draw(decorTab, xTabStart, yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 2) {
				renderer.draw(kitchenTab2, xTabStart + (34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + (34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 2;
					}
				}
				renderer.draw(kitchenTab, xTabStart + (34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 3) {
				renderer.draw(floorTab2, xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 3;
					}
				}
				renderer.draw(floorTab, xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 4) {
				renderer.draw(wallTab2, xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 4;
					}
				}
				renderer.draw(wallTab, xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3);
			}
			if(pageNum == 5) {
				renderer.draw(beamTab2, xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 5;
					}
				}
				renderer.draw(beamTab, xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3);
			}
			if(pageNum == 6) {
				renderer.draw(storeTab2, xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 6;
					}
				}
				renderer.draw(storeTab, xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 7) {
				renderer.draw(bathroomTab2, xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 7;
					}
				}
				renderer.draw(bathroomTab, xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 8) {
				renderer.draw(chairTab2, xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 8;
					}
				}
				renderer.draw(chairTab, xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3);
			}
			if(pageNum == 9) {
				renderer.draw(counterTab2, xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 9;
					}
				}
				renderer.draw(counterTab, xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 10) {
				renderer.draw(tableTab2, xTabStart + 9*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 9*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 10;
					}
				}
				renderer.draw(tableTab, xTabStart + 9*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 11) {
				renderer.draw(panTab2, xTabStart + 10*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 10*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 11;
					}
				}
				renderer.draw(panTab, xTabStart + 10*(34*3), yStart-25*3, 34*3, 25*3);
			}
			
			if(pageNum == 12) {
				renderer.draw(doorTab2, xTabStart + 11*(34*3), yStart-25*3, 34*3, 25*3);
			} else {
				if(containsMouse(xTabStart + 11*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
						pageNum = 12;
					}
				}
				renderer.draw(doorTab, xTabStart + 11*(34*3), yStart-25*3, 34*3, 25*3);
			}
				
			if(pageNum == 1) {
				
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(decorBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Decorations!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(Building b: decorBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					selectedBuilding = b;
			    					clickCounter = 0.33;
			    					showBuildings = false;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);


			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 2) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(kitchenBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Kitchen Items!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(Building b: kitchenBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					selectedBuilding = b;
			    					clickCounter = 0.33;
			    					showBuildings = false;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
				
			} else if(pageNum == 3) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(floorpaperInventory.size() == 0) {
					renderer.drawString(font, "No Flooring!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(FloorPaper b: new ArrayList<FloorPaper>(floorpaperInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setFloorpaper(b);
			    					floorpaperInventory.remove(b);
			    					clickCounter = 0.33;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 4) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(wallpaperInventory.size() == 0) {
					renderer.drawString(font, "No Wallpapers!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(WallPaper b: new ArrayList<WallPaper>(wallpaperInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setWallpaper(b);
			    					wallpaperInventory.remove(b);
			    					clickCounter = 0.33;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 5) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(beamInventory.size() == 0) {
					renderer.drawString(font, "No Beams!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(Beam b: new ArrayList<Beam>(beamInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight2, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setBeam(b);
			    					beamInventory.remove(b);
			    					clickCounter = 0.33;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			
							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 6) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(storeBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Store Items!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(Building b: storeBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					selectedBuilding = b;
			    					clickCounter = 0.33;
			    					showBuildings = false;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			}  else if(pageNum == 7) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(bathroomBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Bathroom Items!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(Building b: bathroomBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					selectedBuilding = b;
			    					clickCounter = 0.33;
			    					showBuildings = false;
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 8) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(chairSkinInventory.size() == 0) {
					renderer.drawString(font, "No Chairs!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(ChairSkin b: new ArrayList<ChairSkin>(chairSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setChairSkin(b);
			    					chairSkinInventory.remove(b);
			    					clickCounter = 0.33;
			    					gp.buildingM.refreshBuildings();
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			renderer.draw(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 9) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(counterSkinInventory.size() == 0) {
					renderer.drawString(font, "No Counters!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(CounterSkin b: new ArrayList<CounterSkin>(counterSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setCounterSkin(b);
			    					counterSkinInventory.remove(b);
			    					clickCounter = 0.33;
			    					gp.buildingM.refreshBuildings();
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			renderer.draw(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 10) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(tableSkinInventory.size() == 0) {
					renderer.drawString(font, "No Tables!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(TableSkin b: new ArrayList<TableSkin>(tableSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setTableSkin(b);
			    					tableSkinInventory.remove(b);
			    					clickCounter = 0.33;
			    					gp.buildingM.refreshBuildings();
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			renderer.draw(b.getImage2(), xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 11) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(panSkinInventory.size() == 0) {
					renderer.drawString(font, "No Pans!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(PanSkin b: new ArrayList<PanSkin>(panSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setPanSkin(b);
			    					panSkinInventory.remove(b);
			    					clickCounter = 0.33;
			    					gp.buildingM.refreshStove();
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			renderer.draw(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			} else if(pageNum == 12) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(doorSkinInventory.size() == 0) {
					renderer.drawString(font, "No Doors!", xStart + 50, yPos+50, fontScale, c);
				}
				
				int index = 0;
				for(DoorSkin b: new ArrayList<DoorSkin>(doorSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					gp.mapM.currentRoom.setDoorSkin(b);
			    					doorSkinInventory.remove(b);
			    					clickCounter = 0.33;
			    					gp.buildingM.refreshBuildings();
			    				}
			    			} else {
								renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
			    			renderer.draw(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48);

			    			xStart+= 37*3;
			    			counter++;
			    			if(counter >= 10) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			index++;
				}
			}
			
		}
		
		
		if(selectedBuilding != null) {
			if(mouseY < gp.frameHeight-ySize || !showBuildings) {
				int size = 4*3;
				if(selectedBuilding.tileGrid) {
					size = 16*3;
				}
				int xPos = (int)((mouseX)/size) * size;
				int yPos = (int)((mouseY)/size) * size;
			    if(prevX != xPos || prevY != yPos) {
			    	selectedBuilding.hitbox.x = xPos;
					selectedBuilding.hitbox.y = yPos;
					selectedBuilding.onPlaced();
			    }
			    //TODO only works with hitbox for shelves
				Rectangle2D.Float buildHitbox = selectedBuilding.buildHitbox;
				//boolean canPlace = CollisionMethods.canPlaceBuilding(gp, selectedBuilding, xPos, yPos, selectedBuilding.hitbox.width, selectedBuilding.hitbox.height);
				boolean canPlace = CollisionMethods.canPlaceBuilding(gp, selectedBuilding, buildHitbox.x, buildHitbox.y, buildHitbox.width, buildHitbox.height);

				if(selectedBuilding.getName().equals("Shelf")) {
			        selectedBuilding.hitbox.x = xPos;
			        selectedBuilding.hitbox.y = yPos;
			        selectedBuilding.onPlaced();
		    	    if(prevX != xPos || prevY != yPos) {
				        List<Building> shelves = gp.buildingM.findBuildingsWithName("Shelf");
			    	    for (Building b: shelves) {
			    	    	Shelf t = (Shelf)b;
			    	    	t.updateConnections();
			    	    }
		    	    }
				}
				TextureRegion img = selectedBuilding.animations[0][0][0];
				Colour c;
				if(canPlace) {
					c = yesBuild;
				} else {
					c = noBuild;
				}
				renderer.draw(img, xPos - selectedBuilding.xDrawOffset, yPos - selectedBuilding.yDrawOffset, selectedBuilding.animations[0][0][0].getPixelWidth()*3, selectedBuilding.animations[0][0][0].getPixelHeight()*3, c.toVec4());
				prevX = xPos;
				prevY = yPos;
				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
					if(canPlace) {
						
						boolean onShelf = false;
						boolean onTable = false;

						for (Building b : gp.buildingM.getBuildings()) {
						    if (b == null) continue;

						    String name = b.getName();

						    if ("Shelf".equals(name) && b.buildHitbox.intersects(buildHitbox)) {
						        onShelf = true;
						    } else if (
						        ("Table Piece".equals(name) ||
						        "Table Corner 1".equals(name) ||
						        "Table 1".equals(name) ||
						        "Table 2".equals(name)) &&
						        b.buildHitbox.intersects(buildHitbox)
						    ) {
						        onTable = true;
						    }
						}

					        // --- Clone and set layer flag ---
					        Building placed = (Building) selectedBuilding.clone();
					        if (onShelf && selectedBuilding.canBePlacedOnShelf) {
					            placed.isFifthLayer = true;  // 🟣 Item on shelf
					            //placed.isThirdLayer = false;
					        } else if (onTable && selectedBuilding.canBePlacedOnTable) {
					            placed.isThirdLayer = true;  // 🟢 Item on table
					            //placed.isFifthLayer = false;
					        } else {
					            // Floor or other placement
					            //placed.isThirdLayer = false;
					            //placed.isFifthLayer = false;
					        }
						
						gp.buildingM.addBuilding(placed, xPos, yPos);
						
						Statistics.decorationsPlaced++;
						gp.progressM.checkDecorationsPlaced();
						
						clickCounter = 0.66;
						if(decorBuildingInventory.contains(selectedBuilding)) {
							decorBuildingInventory.remove(selectedBuilding);
						}
						if(kitchenBuildingInventory.contains(selectedBuilding)) {
							kitchenBuildingInventory.remove(selectedBuilding);
						}
						if(storeBuildingInventory.contains(selectedBuilding)) {
							storeBuildingInventory.remove(selectedBuilding);
						} 
						if(bathroomBuildingInventory.contains(selectedBuilding)) {
							bathroomBuildingInventory.remove(selectedBuilding);
						} 
						showBuildings = true;
						selectedBuilding = null;
					}
				}
			}
		}
		
	
	}

}

package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import entity.buildings.Building;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Sink;
import entity.buildings.TipJar;
import entity.buildings.Toilet;
import entity.buildings.Turntable;
import entity.buildings.WallDecor_Building;
import main.GamePanel;
import utility.CollisionMethods;
import utility.save.CatalogueSaveData;
import utility.save.CustomiserSaveData;

public class Customiser {
	
	GamePanel gp;
	
	Color c = new Color(51, 60, 58);
	BasicStroke s = new BasicStroke(3);
	Font font = new Font("monogram", Font.BOLD, 100);
	
	private BufferedImage frame, buildFrame, buildFrameHighlight, buildFrameHighlight2, border;
	private BufferedImage decorTab, kitchenTab, floorTab, wallTab, storeTab, bathroomTab, beamTab, chairTab, tableTab;
	private BufferedImage decorTab2, kitchenTab2, floorTab2, wallTab2, storeTab2, bathroomTab2, beamTab2, chairTab2, tableTab2;
	
	private int ySize = 126*3;
	int yStart;
	private int clickCounter = 0;
	public boolean showBuildings = true;
	
	private Color noBuild = new Color(239, 4, 4, 150);
	private Color yesBuild = new Color(48, 227, 13, 150);
	
	public int pageNum = 1;
	private List<Building> decorBuildingInventory = new ArrayList<Building>();
	private List<Building> kitchenBuildingInventory = new ArrayList<Building>();
	private List<FloorPaper> floorpaperInventory = new ArrayList<FloorPaper>();
	private List<WallPaper> wallpaperInventory = new ArrayList<WallPaper>();
	private List<Beam> beamInventory = new ArrayList<Beam>();
	private List<Building> storeBuildingInventory = new ArrayList<Building>();
	private List<Building> bathroomBuildingInventory = new ArrayList<Building>();
	private List<ChairSkin> chairSkinInventory = new ArrayList<ChairSkin>();
	private List<TableSkin> tableSkinInventory = new ArrayList<TableSkin>();
	public Building selectedBuilding;
	
	public Customiser(GamePanel gp) {
		this.gp = gp;
		yStart = gp.frameHeight-ySize+4;
		frame = importImage("/UI/customise/CustomiseFrame.png");
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
		tableTab = importImage("/UI/customise/BuildTab.png").getSubimage(34*9, 0, 34, 25);
		
		decorTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34, 25, 34, 25);
		kitchenTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*2, 25, 34, 25);
		floorTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*3, 25, 34, 25);
		wallTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*4, 25, 34, 25);
		beamTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*5, 25, 34, 25);
		storeTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*6, 25, 34, 25);
		bathroomTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*7, 25, 34, 25);
		chairTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*8, 25, 34, 25);
		tableTab2 = importImage("/UI/customise/BuildTab.png").getSubimage(34*9, 25, 34, 25);
		
		border = importImage("/UI/customise/WallBorder.png");
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
		
		List<Integer> tables = new ArrayList<>();
		for(TableSkin b: tableSkinInventory) {
			tables.add(b.preset);
		}
		data.tableSkinInventory = tables;
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
		
		tableSkinInventory.clear();
		for(Integer i: data.tableSkinInventory) {
			tableSkinInventory.add(new TableSkin(gp, i));
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
	private boolean containsMouse(int x, int y, int w, int h) {
		
		int mouseX = gp.mouseI.mouseX;
		int mouseY = gp.mouseI.mouseY;
		
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
			} else if(o instanceof TableSkin w) {
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
	public void addToInventory(TableSkin b) {
		tableSkinInventory.add(b);
	}
	public void update() {
		
	}
	public void draw(Graphics2D g2) {
		
		int mouseX = gp.mouseI.mouseX;
		int mouseY = gp.mouseI.mouseY;
		int yOffset = 32;

		if(showBuildings) {
			g2.setColor(c);
			g2.drawImage(frame, 0, yStart, gp.frameWidth, ySize, null);
			int xStart = 20;
			yStart = gp.frameHeight-ySize+4 - 20 + yOffset;
			
			
			int xTabStart = 40;
			
			if(pageNum == 1) {
				g2.drawImage(decorTab2, xTabStart, yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart, yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 1;
					}
				}
				g2.drawImage(decorTab, xTabStart, yStart-25*3, 34*3, 25*3, null);
			}
			
			if(pageNum == 2) {
				g2.drawImage(kitchenTab2, xTabStart + (34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + (34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 2;
					}
				}
				g2.drawImage(kitchenTab, xTabStart + (34*3), yStart-25*3, 34*3, 25*3, null);
			}
			
			if(pageNum == 3) {
				g2.drawImage(floorTab2, xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 3;
					}
				}
				g2.drawImage(floorTab, xTabStart + 2*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			
			if(pageNum == 4) {
				g2.drawImage(wallTab2, xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 4;
					}
				}
				g2.drawImage(wallTab, xTabStart + 3*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			if(pageNum == 5) {
				g2.drawImage(beamTab2, xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 5;
					}
				}
				g2.drawImage(beamTab, xTabStart + 4*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			if(pageNum == 6) {
				g2.drawImage(storeTab2, xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 6;
					}
				}
				g2.drawImage(storeTab, xTabStart + 5*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			
			if(pageNum == 7) {
				g2.drawImage(bathroomTab2, xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 7;
					}
				}
				g2.drawImage(bathroomTab, xTabStart + 6*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			
			if(pageNum == 8) {
				g2.drawImage(chairTab2, xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 8;
					}
				}
				g2.drawImage(chairTab, xTabStart + 7*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
			if(pageNum == 9) {
				g2.drawImage(tableTab2, xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3, null);
			} else {
				if(containsMouse(xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3)) {
					if(gp.mouseI.leftClickPressed && clickCounter == 0) {
						pageNum = 9;
					}
				}
				g2.drawImage(tableTab, xTabStart + 8*(34*3), yStart-25*3, 34*3, 25*3, null);
			}
				
			if(pageNum == 1) {
				
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(decorBuildingInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Decorations!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(Building b: decorBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed) {
			    					selectedBuilding = b;
			    					clickCounter = 10;
			    					showBuildings = false;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);

			    			xStart+= 37*3;
			    			if(counter >= 16) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 2) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(kitchenBuildingInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Kitchen Items!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(Building b: kitchenBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed) {
			    					selectedBuilding = b;
			    					clickCounter = 10;
			    					showBuildings = false;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);

			    			xStart+= 37*3;
			    			if(counter >= 16) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
				
			} else if(pageNum == 3) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(floorpaperInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Flooring!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(FloorPaper b: new ArrayList<FloorPaper>(floorpaperInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					gp.mapM.currentRoom.setFloorpaper(b);
			    					floorpaperInventory.remove(b);
			    					clickCounter = 10;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
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
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(wallpaperInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Wallpapers!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(WallPaper b: new ArrayList<WallPaper>(wallpaperInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					gp.mapM.currentRoom.setWallpaper(b);
			    					wallpaperInventory.remove(b);
			    					clickCounter = 10;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 5) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(beamInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Beams!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(Beam b: new ArrayList<Beam>(beamInventory)) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight2, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					gp.mapM.currentRoom.setBeam(b);
			    					beamInventory.remove(b);
			    					clickCounter = 10;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
			    			
							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
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
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(storeBuildingInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Store Items!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(Building b: storeBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed) {
			    					selectedBuilding = b;
			    					clickCounter = 10;
			    					showBuildings = false;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			}  else if(pageNum == 7) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(bathroomBuildingInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Bathroom Items!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(Building b: bathroomBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed) {
			    					selectedBuilding = b;
			    					clickCounter = 10;
			    					showBuildings = false;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 8) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(chairSkinInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Chairs!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(ChairSkin b: new ArrayList<ChairSkin>(chairSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					gp.mapM.currentRoom.setChairSkin(b);
			    					chairSkinInventory.remove(b);
			    					clickCounter = 10;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
			    			g2.drawImage(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 9) {
				int counter = 0;
				xStart = 8*3;
				int originalXStart = xStart;
				int yPos = yStart+(30);
				
				int startDraw = 0;
				
				if(tableSkinInventory.size() == 0) {
					g2.setFont(font);
					g2.setColor(c);
					g2.drawString("No Tables!", xStart + 50, yPos+50);
				}
				
				int index = 0;
				for(TableSkin b: new ArrayList<TableSkin>(tableSkinInventory)) {
					if(index >= startDraw) {
						if(b != null) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					gp.mapM.currentRoom.setTableSkin(b);
			    					tableSkinInventory.remove(b);
			    					clickCounter = 10;
			    				}
			    			} else {
								g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
			    			g2.drawImage(b.getImage(), xStart+(55) - 24, yPos+28, 48, 48, null);

			    			xStart+= 37*3;
			    			if(counter >= 9) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = 0;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			}
			
		}
		
		if(selectedBuilding != null) {
			if(mouseY < gp.frameHeight-ySize || !showBuildings) {
				int size = 4*3;
				int xPos = (int)((mouseX+gp.player.xDiff)/size) * size;
				int yPos = (int)((mouseY+gp.player.yDiff)/size) * size;
				boolean canPlace = CollisionMethods.canPlaceBuilding(gp, selectedBuilding, xPos, yPos, selectedBuilding.hitbox.width, selectedBuilding.hitbox.height);
				BufferedImage img = selectedBuilding.animations[0][0][0];
				if(canPlace) {
					img = CollisionMethods.getMaskedImage(yesBuild, img);
				} else {
					img = CollisionMethods.getMaskedImage(noBuild, img);
				}
				g2.drawImage(img, xPos-gp.player.xDiff - selectedBuilding.xDrawOffset, yPos-gp.player.yDiff - selectedBuilding.yDrawOffset, selectedBuilding.animations[0][0][0].getWidth()*3, selectedBuilding.animations[0][0][0].getHeight()*3, null);

				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
					if(canPlace) {
						gp.buildingM.addBuilding((Building)selectedBuilding.clone(), xPos, yPos);
						clickCounter = 20;
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
		
		if(clickCounter > 0) {
			clickCounter--;
		}
		
	
	}

}

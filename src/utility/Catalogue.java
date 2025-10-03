package utility;

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
import entity.buildings.Lantern;
import entity.buildings.Sink;
import entity.buildings.Toilet;
import entity.buildings.WallDecor_Building;
import main.GamePanel;
import map.Beam;
import map.ChairSkin;
import map.FloorPaper;
import map.WallPaper;
import utility.save.CatalogueSaveData;

public class Catalogue {
	
	GamePanel gp;
	
	public int pageNum = 1;
	private boolean showDescription = false;
	private int yStart;
	private int clickCounter;
	public int layer = 0;
	public int basketCost = 0;
	private boolean canPay = false;
	public Object selectedItem;
	
	private int selectedRow = 0;
	public boolean checkingOut = false;
	private BufferedImage border, buildFrame, descriptionFrame, overlay, overlay2, coinImage;
	private Font nameFont = new Font("pix M 8pt", Font.PLAIN, 20);
	private Font costFont = new Font("pix M 8pt", Font.PLAIN, 32);
	private Font descriptionFont = new Font("pix M 8pt", Font.PLAIN, 14);
	
	private List<Building> decorBuildingInventory = new ArrayList<Building>();
	private List<Building> kitchenBuildingInventory = new ArrayList<Building>();
	private List<FloorPaper> floorpaperInventory = new ArrayList<FloorPaper>();
	private List<WallPaper> wallpaperInventory = new ArrayList<WallPaper>();
	private List<Beam> beamInventory = new ArrayList<Beam>();
	private List<ChairSkin> chairSkinInventory = new ArrayList<ChairSkin>();
	private List<Building> storeBuildingInventory = new ArrayList<Building>();
	private List<Building> bathroomBuildingInventory = new ArrayList<Building>();
	
	public List<Object> basket = new ArrayList<Object>();

	public Catalogue(GamePanel gp) {
		this.gp = gp;
		border = importImage("/UI/customise/WallBorder.png");
		buildFrame = importImage("/UI/catalogue/ShopFrame.png");
		descriptionFrame = importImage("/UI/catalogue/Description.png");
		overlay = importImage("/UI/catalogue/OverLay.png");
		overlay2 = importImage("/UI/catalogue/Overlay2.png");
		coinImage = importImage("/UI/Coin.png");

		addBuildings();
	}
	
	public CatalogueSaveData saveCatalogueData() {
		CatalogueSaveData data = new CatalogueSaveData();
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
		return data;
	}
	public void applySaveData(CatalogueSaveData data) {
		
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
	}
	private void addBuildings() {
		addToInventory(new Sink(gp, 0, 0));
		addToInventory(new Toilet(gp, 0, 0, 0));
		addToInventory(new FoodStore(gp, 0, 0, 1));
		addToInventory(new WallPaper(gp, 0));
		addToInventory(new WallPaper(gp, 1));
		addToInventory(new WallPaper(gp, 2));
		addToInventory(new WallPaper(gp, 3));
		addToInventory(new WallPaper(gp, 4));
		addToInventory(new WallPaper(gp, 5));
		addToInventory(new WallPaper(gp, 6));
		addToInventory(new WallPaper(gp, 7));
		addToInventory(new WallPaper(gp, 8));
		addToInventory(new WallPaper(gp, 9));
		addToInventory(new WallPaper(gp, 10));
		addToInventory(new WallPaper(gp, 11));
		addToInventory(new WallPaper(gp, 12));
		addToInventory(new WallPaper(gp, 13));
		addToInventory(new WallPaper(gp, 14));
		addToInventory(new WallPaper(gp, 15));
		addToInventory(new WallPaper(gp, 16));
		addToInventory(new WallPaper(gp, 17));
		addToInventory(new WallPaper(gp, 18));
		addToInventory(new WallPaper(gp, 19));
		addToInventory(new WallPaper(gp, 20));
		addToInventory(new FloorPaper(gp, 0));
		addToInventory(new FloorPaper(gp, 1));
		addToInventory(new FloorPaper(gp, 2));
		addToInventory(new FloorPaper(gp, 3));
		addToInventory(new FloorPaper(gp, 4));
		addToInventory(new FloorPaper(gp, 5));
		addToInventory(new FloorPaper(gp, 6));
		addToInventory(new FloorPaper(gp, 7));
		addToInventory(new FloorPaper(gp, 8));
		addToInventory(new FloorPaper(gp, 9));
		addToInventory(new FloorPaper(gp, 10));
		addToInventory(new FloorPaper(gp, 11));
		addToInventory(new Beam(gp, 0));
		addToInventory(new Beam(gp, 1));
		addToInventory(new Beam(gp, 2));
		addToInventory(new Beam(gp, 3));
		addToInventory(new Beam(gp, 4));
		addToInventory(new Lantern(gp, 0, 0));
		addToInventory(new ChairSkin(gp, 9));
		addToInventory(new ChairSkin(gp, 4));
		addToInventory(new ChairSkin(gp, 3));
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
	private void addToBasket(Object o, int cost) {
		basket.add(o);
		basketCost += cost;
	}
	private void removeFromBasket(Object o, int cost) {
		basket.remove(o);
		basketCost -= cost;
	}
	public void leftPage() {
		pageNum--;
		if(pageNum <= 0) {
			pageNum = 1;
		}
		layer = 0;
	}
	public void rightPage() {
		pageNum++;
		if(pageNum >= 9) {
			pageNum = 8;
		}
		layer = 0;
	}
	public int getXforCenteredText(String text, Graphics2D g2) {
        int x;
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.frameWidth/2 - length/2;
        return x;
    }
	public void upLayer() {
		layer--;
		if(layer < 0) {
			layer = 0;
		}
	}
	public void downLayer() {
		layer++;
		int max = 1;
		int remainder = 0;
		boolean large = false;
		if(!checkingOut) {
			switch(pageNum) {
			case 1:
				max = decorBuildingInventory.size() / 4;
				remainder = decorBuildingInventory.size() % 4;
				large = decorBuildingInventory.size() > 4;
				break;
			case 2:
				max = kitchenBuildingInventory.size() / 4;
				remainder = kitchenBuildingInventory.size() % 4;
				large = kitchenBuildingInventory.size() > 4;
				break;
			case 3:
				max = floorpaperInventory.size() / 4;
				remainder = floorpaperInventory.size() % 4;
				large = floorpaperInventory.size() > 4;
				break;
			case 4:
				max = wallpaperInventory.size() / 4;
				remainder = wallpaperInventory.size() % 4;
				large = wallpaperInventory.size() > 4;
				break;
			case 5:
				max = beamInventory.size() / 4;
				remainder = beamInventory.size() % 4;
				large = beamInventory.size() > 4;
				break;
			case 6:
				max = storeBuildingInventory.size() / 4;
				remainder = storeBuildingInventory.size() % 4;
				large = storeBuildingInventory.size() > 4;
				break;
			case 7:
				max = kitchenBuildingInventory.size() / 4;
				remainder = kitchenBuildingInventory.size() % 4;
				large = kitchenBuildingInventory.size() > 4;
				break;
			case 8:
				max = chairSkinInventory.size() / 4;
				remainder = chairSkinInventory.size() % 4;
				large = chairSkinInventory.size() > 4;
				break;
			}
		} else {
			max = basket.size() / 4;
			remainder = basket.size() % 4;
			large = basket.size() > 4;
		}
		if(remainder != 0 && large) {
			max--;
		}
		
		if(layer > max) {
			layer = max;
		}
	}
	public void drawCatalogue(Graphics2D g2) {
		
		int xStart = (int)(83*4.5);
		yStart = (int)(47*4.5);

		selectedItem = null;
		showDescription = false;
		if(pageNum == 1) {
				
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(Building b: decorBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
							
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);
							}
			    			xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
			    			}
							}
					}
	    			counter++;
	    			index++;
				}
			} else if(pageNum == 2) {
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(Building b: kitchenBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				showDescription = true;
			    				selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);
							}
							xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(FloorPaper b: floorpaperInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);
							}
			    			xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(WallPaper b: wallpaperInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);
							}
			    			xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(Beam b: beamInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight2, xStart, yPos, 37*3, 37*3, null);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48, null);
			    			g2.drawImage(border, xStart+(55) - 24, yPos+28, 48, 48, null);
							}
			    			xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(Building b: storeBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
			    				showDescription = true;
			    				selectedItem = b;
			  					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);
							}
							xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(Building b: bathroomBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight, null);
							}
							xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
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
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				int index = 0;
				for(ChairSkin b: chairSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseI.leftClickPressed && clickCounter == 0) {
			    					clickCounter = 10;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);

							g2.drawImage(b.getImage(), xStart+(55) - 24, yPos+30 - 24, 48, 48, null);
							}
							xStart+= 40*3;
			    			if(counter >= 3) {
			    				xStart = originalXStart;
			    				yPos += 37*3;
			    				counter = -1;
			    			}
						}
					} else {
						counter = 0;
					}
	    			counter++;
	    			index++;
				}
			}
		
		g2.drawImage(overlay, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
		
		canPay = gp.player.wealth >= basketCost;
		if(canPay) {
			g2.setColor(Color.GREEN);
		} else {
			g2.setColor(Color.RED);
		}
		g2.setFont(costFont);
		g2.drawString(Integer.toString(basketCost), (int)(153*4.5), 484);
		//g2.drawImage(coinImage,  (int)(153*4.5) + 28, 450, 32, 32, null);
		
		
		if(showDescription) {
			drawBuildingDescription(g2, selectedItem, xStart);
		}
			
		if(clickCounter > 0) {
			clickCounter--;
		}
	
	}
	public void drawBuildingDescription(Graphics2D g2, Object b, int xStart) {
		BufferedImage img;
		String name = "";
		String description = "";
		int cost = 0;
		int drawWidth = 48;
		int drawHeight = 48;
		int yDrawOffset = 0;
		
		if(b instanceof Building build) {
			img = build.animations[0][0][0];
			name = build.getName();
			description = build.getDescription();
			cost = build.cost;
			drawWidth = build.drawWidth;
			drawHeight = build.drawHeight;
			yDrawOffset = build.yDrawOffset;
		} else if(b instanceof WallPaper wall) {
			img = wall.getBaseImage();
			name = wall.name;
			description = wall.description;
			cost = wall.cost;
		} else if(b instanceof FloorPaper wall) {
			img = wall.getBaseImage();
			name = wall.name;
			description = wall.description;
			cost = wall.cost;
		} else if(b instanceof Beam wall) {
			img = wall.getBaseImage();
			name = wall.name;
			description = wall.description;
			cost = wall.cost;
		} else if(b instanceof ChairSkin chair) {
			img = chair.getImage();
			name = chair.name;
			description = chair.description;
			cost = chair.cost;
		} else {
			return;
		}
		
		if(showDescription) {
			int xPos = xStart - 140;
			int yPos = yStart + 70;
			if(selectedRow == 1) {
				yPos = yStart - 126;
			}
			g2.drawImage(descriptionFrame, xPos, yPos, (int)(124*3), (int)(94*3), null);
			
			g2.setColor(Color.WHITE);
			g2.setFont(nameFont);
			String text =  name;
			g2.drawString(text, xPos + 32, yPos + 40);
			
			g2.drawImage(img, xPos+(186/2) - drawWidth/2, yPos+130 - yDrawOffset, drawWidth, drawHeight, null);
			
			g2.setColor(Color.BLACK);
			g2.drawString(Integer.toString(cost), xPos + 32 + 32 + 40, yPos + 200 + 38 + 10);
			g2.drawImage(coinImage, xPos+(186/2) - 10 + 32+16, yPos+200-24 + 46, 24, 24, null);
			
			g2.setFont(descriptionFont);
			text = description;
			for(String line: text.split("\n")) {
				g2.drawString(line, xPos + 64*3, yPos + 24*3+ 12);
				yPos += 30;
			}
			
		}
	}
	public void drawCheckout(Graphics2D g2) {
		int xStart = (int)(83*4.5);
		yStart = (int)(47*4.5);

		selectedItem = null;
		showDescription = false;
		int counter = 0;
		int originalXStart = xStart;
		int yPos = yStart - 37*3 *layer;
		
		int startDraw = 0;
		
		int index = 0;
		ArrayList<Object> copy = new ArrayList<Object>(basket);
		for(Object b: copy) {
			if(index >= startDraw) {
				if(b != null) {
					BufferedImage img;
					String name = "";
					String description = "";
					int cost = 0;
					int drawWidth = 48;
					int drawHeight = 48;
					int yDrawOffset = 0;
					int xDrawOffset = 0;
					
					if(b instanceof Building build) {
						img = build.animations[0][0][0];
						name = build.getName();
						description = build.getDescription();
						cost = build.cost;
						drawWidth = build.drawWidth;
						drawHeight = build.drawHeight;
						yDrawOffset = build.yDrawOffset;
					} else if(b instanceof WallPaper wall) {
						img = wall.getBaseImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						xDrawOffset = 24;
					} else if(b instanceof FloorPaper wall) {
						img = wall.getBaseImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						xDrawOffset = 24;
					} else if(b instanceof Beam wall) {
						img = wall.getBaseImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						xDrawOffset = 24;
					} else if(b instanceof ChairSkin wall) {
						img = wall.getImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						//xDrawOffset = 24;
					} else {
						return;
					}
					if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
	    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
						//g2.drawImage(buildFrameHighlight, xStart, yPos, 37*3, 37*3, null);
    					showDescription = true;
    					selectedItem = b;
    					if(yPos >= yStart +37*3) {
    						selectedRow = 1;
    					} else {
    						selectedRow = 0;
    					}
	    				if(gp.mouseI.rightClickPressed && clickCounter == 0) {
	    					clickCounter = 10;
	    					removeFromBasket(b, cost);
	    				}
	    			}
					g2.drawImage(buildFrame, xStart, yPos, 37*3, 37*3, null);
					if(b instanceof WallPaper || b instanceof FloorPaper || b instanceof Beam) {
						g2.drawImage(border, xStart+(55) - drawWidth/2, yPos+30 - yDrawOffset, 48, 48, null);
					}
					g2.drawImage(img, xStart+(55) - drawWidth/2, yPos+30 - yDrawOffset, drawWidth, drawHeight, null);
					}
					xStart+= 40*3;
	    			if(counter >= 3) {
	    				xStart = originalXStart;
	    				yPos += 37*3;
	    				counter = -1;
	    			}
				}
			} else {
				counter = 0;
			}
			counter++;
			index++;
		}
		
		g2.drawImage(overlay2, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
		
		canPay = gp.player.wealth >= basketCost;
		if(canPay) {
			g2.setColor(Color.GREEN);
		} else {
			g2.setColor(Color.RED);
		}
		g2.setFont(costFont);
		g2.drawString(Integer.toString(basketCost), (int)(128*4.5), 484);
		
		if(showDescription) {
			drawBuildingDescription(g2, selectedItem, xStart);
		}
			
		if(clickCounter > 0) {
			clickCounter--;
		}
	}
	public void tryPay() {
		if(canPay) {
			gp.catalogue.checkingOut = false;
			gp.catalogue.layer = 0;
			gp.currentState = gp.playState;
			orderItems();
		}
	}
	private void orderItems() {
		gp.world.orderItems(new ArrayList<>(basket));
		gp.player.wealth -= basketCost;
		basketCost = 0;
		basket.clear();
	}
}


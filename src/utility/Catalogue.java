package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import entity.buildings.Building;
import entity.buildings.Candle;
import entity.buildings.Fireplace;
import entity.buildings.FloorDecor_Building;
import entity.buildings.Lantern;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import map.Beam;
import map.ChairSkin;
import map.CounterSkin;
import map.DoorSkin;
import map.FloorPaper;
import map.PanSkin;
import map.TableSkin;
import map.WallPaper;
import utility.save.CatalogueSaveData;

public class Catalogue {
	
	GamePanel gp;
	Random r;
	
	public int pageNum = 1;
	private boolean showDescription = false;
	private int yStart;
	private double clickCounter;
	public int layer = 0;
	public int basketCost = 0;
	private boolean canPay = false;
	public Object selectedItem;
	private int mysteryCrateCost = 100;
	
	private int selectedRow = 0;
	public boolean checkingOut = false;
	public boolean onMysteryScreen = false;
	public boolean onCatalogueScreen = false;
	private TextureRegion border, buildFrame, descriptionFrame, overlay, overlay2, coinImage, mysteryIcon;
	BitmapFont font;
	
	private List<Building> decorBuildingInventory = new ArrayList<Building>();
	private List<Building> kitchenBuildingInventory = new ArrayList<Building>();
	private List<FloorPaper> floorpaperInventory = new ArrayList<FloorPaper>();
	private List<WallPaper> wallpaperInventory = new ArrayList<WallPaper>();
	private List<Beam> beamInventory = new ArrayList<Beam>();
	private List<ChairSkin> chairSkinInventory = new ArrayList<ChairSkin>();
	private List<CounterSkin> counterSkinInventory = new ArrayList<CounterSkin>();
	private List<TableSkin> tableSkinInventory = new ArrayList<TableSkin>();
	private List<PanSkin> panSkinInventory = new ArrayList<PanSkin>();
	private List<DoorSkin> doorSkinInventory = new ArrayList<DoorSkin>();
	private List<Building> storeBuildingInventory = new ArrayList<Building>();
	private List<Building> bathroomBuildingInventory = new ArrayList<Building>();
	
	public List<Object> basket = new ArrayList<Object>();
	
	//CATALOGUES
    public final List<ShopCatalogue> allCatalogues = new ArrayList<>();
    private final Set<Integer> unlockedIds = new HashSet<>();
    
    public TextureRegion cabinImage, fishingImage, farmImage;
    
    private int maxPages = 13;

	public Catalogue(GamePanel gp) {
		this.gp = gp;
		r = new Random();
		border = importImage("/UI/customise/WallBorder.png").toTextureRegion();
		buildFrame = importImage("/UI/catalogue/ShopFrame.png").toTextureRegion();
		descriptionFrame = importImage("/UI/catalogue/Description.png").toTextureRegion();
		overlay = importImage("/UI/catalogue/OverLay.png").toTextureRegion();
		overlay2 = importImage("/UI/catalogue/Overlay2.png").toTextureRegion();
		mysteryIcon = importImage("/UI/catalogue/MysteryCrateUI.png").toTextureRegion();
		coinImage = importImage("/UI/Coin.png").toTextureRegion();
		cabinImage = importImage("/UI/catalogue/CabinIcon.png").toTextureRegion();
		fishingImage = importImage("/UI/catalogue/FishingIcon.png").toTextureRegion();
		farmImage = importImage("/UI/catalogue/FarmIcon.png").toTextureRegion();
		
		ShopCatalogue catalogue1 = new ShopCatalogue(0, "Cabin");
		List<Object> contents = new ArrayList<Object>();
		contents.add(new WallDecor_Building(gp, 0, 0, 17));
		contents.add(new WallPaper(gp, 31));
		contents.add(new Fireplace(gp, 0, 0));
		contents.add(new FloorPaper(gp, 4));
		contents.add(new CounterSkin(gp, 4));
		contents.add(new ChairSkin(gp, 13));
		contents.add(new PanSkin(gp, 1));
		contents.add(new TableSkin(gp, 1));
		contents.add(new DoorSkin(gp, 1));
		contents.add(new WallDecor_Building(gp, 0, 0, 32));
		contents.add(new WallDecor_Building(gp, 0, 0, 33));
		contents.add(new WallDecor_Building(gp, 0, 0, 34));
		contents.add(new WallDecor_Building(gp, 0, 0, 35));
		contents.add(new WallDecor_Building(gp, 0, 0, 22));
		catalogue1.setContents(contents);
		catalogue1.setIcon(cabinImage);
		addCatalogue(catalogue1);
		
		ShopCatalogue catalogue2 = new ShopCatalogue(1, "Fishing Shack");
		contents = new ArrayList<Object>();
		contents.add(new WallPaper(gp, 32));
		contents.add(new FloorPaper(gp, 5));
		contents.add(new Window(gp, 0, 0, 1));
		contents.add(new FloorDecor_Building(gp, 0, 0, 105));
		contents.add(new FloorDecor_Building(gp, 0, 0, 106));
		contents.add(new DoorSkin(gp, 2));
		contents.add(new PanSkin(gp, 2));
		contents.add(new TableSkin(gp, 2));
		contents.add(new CounterSkin(gp, 5));
		contents.add(new WallDecor_Building(gp, 0, 0, 22));
		contents.add(new WallDecor_Building(gp, 0, 0, 36));
		contents.add(new WallDecor_Building(gp, 0, 0, 37));
		contents.add(new FloorDecor_Building(gp, 0, 0, 107));
		contents.add(new FloorDecor_Building(gp, 0, 0, 17));
		catalogue2.setContents(contents);
		catalogue2.setIcon(fishingImage);
		addCatalogue(catalogue2);
		
		ShopCatalogue catalogue3 = new ShopCatalogue(2, "Farm");
		contents = new ArrayList<Object>();
		contents.add(new Beam(gp, 6));
		contents.add(new WallPaper(gp, 33));
		contents.add(new FloorPaper(gp, 7));
		contents.add(new DoorSkin(gp, 3));
		contents.add(new ChairSkin(gp, 6));
		contents.add(new TableSkin(gp, 3));
		contents.add(new Window(gp, 0, 0, 2));
		contents.add(new FloorDecor_Building(gp, 0, 0, 108));
		contents.add(new FloorDecor_Building(gp, 0, 0, 109));
		contents.add(new WallDecor_Building(gp, 0, 0, 38));
		catalogue3.setContents(contents);
		catalogue3.setIcon(farmImage);
		addCatalogue(catalogue3);
		
		unlockById(0);
		unlockById(1);
		unlockById(2);
		
		font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
		addBuildings();
	}
    public int getRandomCatalogue() {

        // Get all locked catalogue IDs
        List<ShopCatalogue> locked = new ArrayList<>();
        for (ShopCatalogue c : allCatalogues) {
            if (!unlockedIds.contains(c.getId())) {
                locked.add(c);
            }
        }

        // None left to unlock
        if (locked.isEmpty()) {
            return -1;
        }

        // Pick a random locked catalogue
        ShopCatalogue chosen = locked.get(r.nextInt(locked.size()));
        return chosen.getId();
    }
    public ShopCatalogue getUnlockedByIndex(int index) {
        List<Integer> list = new ArrayList<>(unlockedIds);
        Collections.sort(list);

        if (index < 0 || index >= list.size()) {
            return null;
        }

        int id = list.get(index);

        // Catalogue IDs match their index positions in allCatalogues
        if (id < 0 || id >= allCatalogues.size()) {
            return null;
        }

        return allCatalogues.get(id);
    }
    public ShopCatalogue getCatalogueByID(int id) {
        if (id < 0 || id >= allCatalogues.size()) return null;
        return allCatalogues.get(id);
    }
    public void addCatalogue(ShopCatalogue catalogue) {
        allCatalogues.add(catalogue);
    }
    public TextureRegion getCatalogueIconFor(Object target) {
        if (target == null) return null;
        
        String targetName = "";
    	if(target instanceof Building build) {
    		targetName = build.getName();
		} else if(target instanceof WallPaper wall) {
			targetName = wall.name;
		} else if(target instanceof FloorPaper wall) {
			targetName = wall.name;
		} else if(target instanceof Beam wall) {
			targetName = wall.name;
		} else if(target instanceof ChairSkin chair) {
			targetName = chair.name;
		} else if(target instanceof CounterSkin chair) {
			targetName = chair.name;
		} else if(target instanceof TableSkin chair) {
			targetName = chair.name;
		} else if(target instanceof PanSkin chair) {
			targetName = chair.name;
		} else if(target instanceof DoorSkin chair) {
			targetName = chair.name;
		}

        for (ShopCatalogue catalogue : allCatalogues) {
            for (Object b : catalogue.getContents()) {
            	String name = "";
            	if(b instanceof Building build) {
        			name = build.getName();
        		} else if(b instanceof WallPaper wall) {
        			name = wall.name;
        		} else if(b instanceof FloorPaper wall) {
        			name = wall.name;
        		} else if(b instanceof Beam wall) {
        			name = wall.name;
        		} else if(b instanceof ChairSkin chair) {
        			name = chair.name;
        		} else if(b instanceof CounterSkin chair) {
        			name = chair.name;
        		} else if(b instanceof TableSkin chair) {
        			name = chair.name;
        		} else if(b instanceof PanSkin chair) {
        			name = chair.name;
        		} else if(b instanceof DoorSkin chair) {
        			name = chair.name;
        		}
                if (name.equals(targetName)) {
                    return catalogue.getIcon();
                }
            }
        }

        return null; // Not found in any catalogue
    }
    public void unlockById(int id) {
        unlockedIds.add(id);
    }
    public boolean isUnlocked(int id) {
        return unlockedIds.contains(id);
    }
	
	public CatalogueSaveData saveCatalogueData() {
		CatalogueSaveData data = new CatalogueSaveData();
		data.decorBuildingInventory = gp.world.buildingRegistry.saveBuildings(decorBuildingInventory);
		data.bathroomBuildingInventory = gp.world.buildingRegistry.saveBuildings(bathroomBuildingInventory);
		data.kitchenBuildingInventory = gp.world.buildingRegistry.saveBuildings(kitchenBuildingInventory);
		data.storeBuildingInventory = gp.world.buildingRegistry.saveBuildings(storeBuildingInventory);		
		
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
	public void applySaveData(CatalogueSaveData data) {
		
		decorBuildingInventory = gp.world.buildingRegistry.unpackSavedBuildings(data.decorBuildingInventory);
		bathroomBuildingInventory = gp.world.buildingRegistry.unpackSavedBuildings(data.bathroomBuildingInventory);
		kitchenBuildingInventory = gp.world.buildingRegistry.unpackSavedBuildings(data.kitchenBuildingInventory);
		storeBuildingInventory = gp.world.buildingRegistry.unpackSavedBuildings(data.storeBuildingInventory);
		
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
	private void addBuildings() {
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
		addToInventory(new FloorPaper(gp, 6));
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
		addToInventory(new Candle(gp, 0, 0, 0));
		addToInventory(new Candle(gp, 0, 0, 1));
		addToInventory(new ChairSkin(gp, 1));
		addToInventory(new ChairSkin(gp, 2));
		addToInventory(new ChairSkin(gp, 5));
		addToInventory(new ChairSkin(gp, 7));
		addToInventory(new ChairSkin(gp, 10));
		addToInventory(new ChairSkin(gp, 11));
		addToInventory(new ChairSkin(gp, 14));
		addToInventory(new CounterSkin(gp, 1));
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
	public void resetBasket() {
		basket.clear();
		layer = 0;
		pageNum = 0;
		basketCost = 0;
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
		int max = maxPages;
		if(onCatalogueScreen) {
			max = unlockedIds.size()+1;
		}
		if(pageNum >= max) {
			pageNum = max-1;
		}
		layer = 0;
	}
	public int getXforCenteredText(String text, BitmapFont font) {
	    float textWidth = font.getTextWidth(text);
	    return (int)(gp.frameWidth / 2f - textWidth / 2f);
	}
	public void upLayer() {
		if(clickCounter == 0) {
			clickCounter = 0.2;
			layer--;
			if(layer < 0) {
				layer = 0;
			}
		}
	}
	public void downLayer() {
		if(clickCounter == 0) {
			clickCounter = 0.2;
		layer++;
		int max = 1;
		int remainder = 0;
		boolean large = false;
		if(!checkingOut && !onCatalogueScreen) {
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
			case 9:
				max = counterSkinInventory.size() / 4;
				remainder = counterSkinInventory.size() % 4;
				large = counterSkinInventory.size() > 4;
				break;
			case 10:
				max = tableSkinInventory.size() / 4;
				remainder = tableSkinInventory.size() % 4;
				large = tableSkinInventory.size() > 4;
				break;
			case 11:
				max = panSkinInventory.size() / 4;
				remainder = panSkinInventory.size() % 4;
				large = panSkinInventory.size() > 4;
				break;
			case 12:
				max = doorSkinInventory.size() / 4;
				remainder = doorSkinInventory.size() % 4;
				large = doorSkinInventory.size() > 4;
				break;
			}
		} else if(onCatalogueScreen) {
			ShopCatalogue cat = getUnlockedByIndex(pageNum-1);
			max = cat.getContents().size() / 4;
			remainder = cat.getContents().size() % 4;
			large = cat.getContents().size() > 4;
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
	}
	public void drawCatalogue(Renderer renderer) {
		
		int xStart = (int)(83*4.5);
		yStart = (int)(47*4.5);

		selectedItem = null;
		showDescription = false;
		if(pageNum == 1) {
				
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				
				if(decorBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Decorations.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
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
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);
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
				
				if(kitchenBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Kitchen installations.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(Building b: kitchenBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {

			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				showDescription = true;
			    				selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);
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
				
				if(floorpaperInventory.size() == 0) {
					renderer.drawString(font, "No Flooring.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(FloorPaper b: floorpaperInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);
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
				
				if(wallpaperInventory.size() == 0) {
					renderer.drawString(font, "No Wallpapers.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(WallPaper b: wallpaperInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);
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
				
				if(beamInventory.size() == 0) {
					renderer.drawString(font, "No Beams.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(Beam b: beamInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight2, xStart, yPos, 37*3, 37*3);
			    				showDescription = true;
			    				selectedItem = b;
			    				if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getBaseImage(), xStart+(55) - 24, yPos+28, 48, 48);
			    			renderer.draw(border, xStart+(55) - 24, yPos+28, 48, 48);
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

				if(storeBuildingInventory.size() == 0) {
					renderer.drawString(font, "No Store room items.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(Building b: storeBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
			    				showDescription = true;
			    				selectedItem = b;
			  					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			} else {
								//renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);
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
				
				if(bathroomBuildingInventory.size() == 0) {
					renderer.drawString(font, "No bathroom items.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(Building b: bathroomBuildingInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.animations[0][0][0], xStart+(55) - b.drawWidth/2, yPos+30 - b.yDrawOffset, b.drawWidth, b.drawHeight);
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
				
				if(chairSkinInventory.size() == 0) {
					renderer.drawString(font, "No chairs.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(ChairSkin b: chairSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getImage(), xStart+(55) - 24, yPos+30 - 24, 48, 48);
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
			} else if(pageNum == 9) {
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				if(counterSkinInventory.size() == 0) {
					renderer.drawString(font, "No kitchen counters.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(CounterSkin b: counterSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getImage(), xStart+(55) - 24, yPos+30 - 24, 48, 48);
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
			} else if(pageNum == 10) {
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				if(tableSkinInventory.size() == 0) {
					renderer.drawString(font, "No tables.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(TableSkin b: tableSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getImage2(), xStart+(55) - 24, yPos+30 - 24, 48, 48);
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
			} else if(pageNum == 11) {
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				if(panSkinInventory.size() == 0) {
					renderer.drawString(font, "No pans.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(PanSkin b: panSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getImage(), xStart+(55) - 24, yPos+30 - 24, 48, 48);
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
			} else if(pageNum == 12) {
				int counter = 0;
				int originalXStart = xStart;
				int yPos = yStart - 37*3 *layer;
				
				int startDraw = 0;
				
				if(doorSkinInventory.size() == 0) {
					renderer.drawString(font, "No doors.", xStart + 50, yPos+50, 1.0f , Colour.BLACK);
				}
				int index = 0;
				for(DoorSkin b: doorSkinInventory) {
					if(index >= startDraw) {
						if(b != null) {
							if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
			    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
								//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    					showDescription = true;
		    					selectedItem = b;
		    					if(yPos >= yStart +37*3) {
		    						selectedRow = 1;
		    					} else {
		    						selectedRow = 0;
		    					}
			    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
			    					clickCounter = 0.17;
			    					addToBasket(b, b.cost);
			    				}
			    			}
							renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

							renderer.draw(b.getImage(), xStart+(55) - 24, yPos+30 - 24, 48, 48);
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
		
		renderer.draw(overlay, 0, 0, (int)(260*4.5), (int)(190*4.5));
		
		canPay = gp.player.wealth >= basketCost;
		Colour c;
		if(canPay) {
			c = Colour.GREEN;
		} else {
			c = Colour.RED;
		}
		renderer.drawString(font, Integer.toString(basketCost), (int)(153*4.5), 484, 1.0f, c);
		//renderer.draw(coinImage,  (int)(153*4.5) + 28, 450, 32, 32);
		
		if(showDescription) {
			drawBuildingDescription(renderer, selectedItem, xStart);
		}
	
	}
	public void drawShopCatalogueScreen(Renderer renderer) {
		
		int xStart = (int)(83*4.5);
		yStart = (int)(47*4.5);

		selectedItem = null;
		showDescription = false;
			int counter = 0;
			int originalXStart = xStart;
			int yPos = yStart - 37*3 *layer;
			
			int startDraw = 0;
			
			ShopCatalogue cat = getUnlockedByIndex(pageNum-1);
						
			if(cat != null) {
			int index = 0;
			for(Object o: cat.getContents()) {
				if(index >= startDraw) {
					if(o != null) {
						TextureRegion img = null;
						int cost = 0;
						int drawWidth = 48;
						int drawHeight = 48;
						int xDrawOffset = 0;
						int yDrawOffset = 0;
						if(o instanceof Building build) {
							img = build.animations[0][0][0];
							cost = build.cost;
							drawWidth = build.drawWidth;
							drawHeight = build.drawHeight;
							yDrawOffset = build.yDrawOffset;
						} else if(o instanceof WallPaper wall) {
							img = wall.getBaseImage();
							cost = wall.cost;
							xDrawOffset = 24;
						} else if(o instanceof FloorPaper wall) {
							img = wall.getBaseImage();
							cost = wall.cost;
							xDrawOffset = 24;
						} else if(o instanceof Beam wall) {
							img = wall.getBaseImage();
							cost = wall.cost;
							xDrawOffset = 24;
						} else if(o instanceof ChairSkin wall) {
							img = wall.getImage();
							cost = wall.cost;
							//xDrawOffset = 24;
						} else if(o instanceof CounterSkin wall) {
							img = wall.getImage();
							cost = wall.cost;
							//xDrawOffset = 24;
						} else if(o instanceof TableSkin wall) {
							img = wall.getImage2();
							cost = wall.cost;
							//xDrawOffset = 24;
						} else if(o instanceof PanSkin wall) {
							img = wall.getImage();
							cost = wall.cost;
							//xDrawOffset = 24;
						} else if(o instanceof DoorSkin wall) {
							img = wall.getImage();
							cost = wall.cost;
							//xDrawOffset = 24;
						}
						if(yPos ==  yStart || yPos ==  yStart + 37*3 *1) {
		    			if(containsMouse(xStart, yPos, 37*3, 37*3)) {
							//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
		    				showDescription = true;
		    				selectedItem = o;
		  					if(yPos >= yStart +37*3) {
	    						selectedRow = 1;
	    					} else {
	    						selectedRow = 0;
	    					}
		    				if(gp.mouseL.mouseButtonDown(0) && clickCounter == 0) {
		    					clickCounter = 0.17;
		    					addToBasket(o, cost);
		    				}
		    			}
						renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);

						renderer.draw(img, xStart+(55) - drawWidth/2 - xDrawOffset, yPos+30 - yDrawOffset, drawWidth, drawHeight);
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
			String text = cat.getName();
			int x =getXforCenteredText(text, font);
			renderer.drawString(font, text, x, (int)(38*4.5), 1.0f, Colour.WHITE);
			
			renderer.draw(cat.getIcon(), xStart+180, (int)(38*4.5) - 30, 16*3, 16*3);
		} 
		
		renderer.draw(overlay, 0, 0, (int)(260*4.5), (int)(190*4.5));
		
		canPay = gp.player.wealth >= basketCost;
		Colour c;
		if(canPay) {
			c = Colour.GREEN;
		} else {
			c = Colour.RED;
		}
		renderer.drawString(font, Integer.toString(basketCost), (int)(153*4.5), 484, 1.0f, c);
		//renderer.draw(coinImage,  (int)(153*4.5) + 28, 450, 32, 32);
		
		if(showDescription) {
			drawBuildingDescription(renderer, selectedItem, xStart);
		}
	
	}
	public void drawBuildingDescription(Renderer renderer, Object b, int xStart) {
		TextureRegion img;
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
		} else if(b instanceof CounterSkin chair) {
			img = chair.getImage();
			name = chair.name;
			description = chair.description;
			cost = chair.cost;
		} else if(b instanceof TableSkin chair) {
			img = chair.getImage2();
			name = chair.name;
			description = chair.description;
			cost = chair.cost;
		} else if(b instanceof PanSkin chair) {
			img = chair.getImage();
			name = chair.name;
			description = chair.description;
			cost = chair.cost;
		} else if(b instanceof DoorSkin chair) {
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
			renderer.draw(descriptionFrame, xPos, yPos, (int)(124*3), (int)(94*3));
			
			String text =  name;
			renderer.drawString(font, text, xPos + 32, yPos + 40, 1.0f, Colour.WHITE);
			
			renderer.draw(img, xPos+(186/2) - drawWidth/2, yPos+130 - yDrawOffset, drawWidth, drawHeight);
			
			renderer.drawString(font, Integer.toString(cost), xPos + 32 + 32 + 40, yPos + 200 + 38 + 10, 1.0f, Colour.BLACK);
			renderer.draw(coinImage, xPos+(186/2) - 10 + 32+16, yPos+200-24 + 46, 24, 24);
			
			text = description;
			for(String line: gp.gui.wrapText(description, font, 49*3)) {
				renderer.drawString(font, line, xPos + 64*3, yPos + 24*3+ 12, 1.0f, Colour.BLACK);
				yPos += 30;
			}
			
		}
	}
	public void drawCheckout(Renderer renderer) {
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
					TextureRegion img;
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
					} else if(b instanceof CounterSkin wall) {
						img = wall.getImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						//xDrawOffset = 24;
					} else if(b instanceof TableSkin wall) {
						img = wall.getImage2();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						//xDrawOffset = 24;
					} else if(b instanceof PanSkin wall) {
						img = wall.getImage();
						name = wall.name;
						description = wall.description;
						cost = wall.cost;
						//xDrawOffset = 24;
					} else if(b instanceof DoorSkin wall) {
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
						//renderer.draw(buildFrameHighlight, xStart, yPos, 37*3, 37*3);
    					showDescription = true;
    					selectedItem = b;
    					if(yPos >= yStart +37*3) {
    						selectedRow = 1;
    					} else {
    						selectedRow = 0;
    					}
	    				if(gp.mouseL.mouseButtonDown(1) && clickCounter == 0) {
	    					clickCounter = 0.17;
	    					removeFromBasket(b, cost);
	    				}
	    			}
					renderer.draw(buildFrame, xStart, yPos, 37*3, 37*3);
					if(b instanceof WallPaper || b instanceof FloorPaper || b instanceof Beam) {
						renderer.draw(border, xStart+(55) - drawWidth/2, yPos+30 - yDrawOffset, 48, 48);
					}
					renderer.draw(img, xStart+(55) - drawWidth/2, yPos+30 - yDrawOffset, drawWidth, drawHeight);
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
		
		renderer.draw(overlay2, 0, 0, (int)(260*4.5), (int)(190*4.5));
		
		canPay = gp.player.wealth >= basketCost;
		Colour c;
		if(canPay) {
			c = Colour.GREEN;
		} else {
			c = Colour.RED;
		}
		renderer.drawString(font, Integer.toString(basketCost), (int)(128*4.5), 484, 1.0f, c);
		
		if(showDescription) {
			drawBuildingDescription(renderer, selectedItem, xStart);
		}
			
	}
	public void drawMysteryScreen(Renderer renderer) {
		renderer.drawString(font, Integer.toString(mysteryCrateCost), (int)(173*4.5), (int)(117*4.5), 1.0f, Colour.BLACK);
	}
	public void updateState(double dt) {
		
	}
	public void inputUpdate(double dt) {
		if (clickCounter > 0) {
			clickCounter -= dt;        // subtract elapsed time in seconds
		    if (clickCounter < 0) {
		    	clickCounter = 0;      // clamp to zero
		    }
		}
	}
	public void buyMysteryCrate() {
		boolean hasMoney = gp.player.wealth >= mysteryCrateCost;
		if(hasMoney) {
			gp.currentState = gp.playState;
			orderCrate();
		}
	}
	public void tryPay() {
		if(canPay) {
			gp.world.catalogue.checkingOut = false;
			gp.world.catalogue.layer = 0;
			gp.currentState = gp.playState;
			orderItems();
		}
	}
	private void orderItems() {
		gp.world.gameM.orderItems(new ArrayList<>(basket));
		gp.player.wealth -= basketCost;
		basketCost = 0;
		basket.clear();
	}
	private void orderCrate() {
		gp.world.gameM.orderCrate();
		gp.player.wealth -= mysteryCrateCost;
		basketCost = 0;
		basket.clear();
	}
}


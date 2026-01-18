package utility;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import entity.buildings.Breaker;
import entity.buildings.Building;
import entity.buildings.Parcel;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import map.Beam;
import map.ChairSkin;
import map.FloorPaper;
import map.LightSource;
import map.Room;
import map.CounterSkin;
import map.WallPaper;
import utility.save.OrderSaveData;
import utility.save.SettingsSaveData;
import utility.save.WorldSaveData;

public class World {

    GamePanel gp;
    Random random;

    private float time;
    private boolean paused = false;

    public Season currentSeason = Season.SPRING;
    public int day = 1;
    private static final int MAX_DAYS_PER_SEASON = 28;
    private int dayStart = 6, openingTime = 6, closingTime = 19;
    private double timeSpeed = 30.0;

    private double spawnTimer = 0;
    private int customerSpawnTimer = 24;
    public int previousSoulsCollected = 0;
    private boolean waitingForLevelUp = false;

    // === Menu system ===
    private List<Recipe> todaysMenu = new ArrayList<>();     // player-selected menu
    private List<Recipe> todaysSpecials = new ArrayList<>(); // generated specials
    private boolean menuChosen = false;
    private int maxMenuSize = 5;
    private int lastTimePeriod = 0;
    
    // === Event system ===
    private boolean eventsOn = false;
    private int eventTimer = 0;
    private int nextEventTime; // in frames
    private int minEventInterval = 45;  // 10 in-game minutes
    private int maxEventInterval = 60*3;  // 3 in-game hour
    private int serviceEventGracePeriod = 60; 
    private DayPhase lastPhase = DayPhase.AFTER_HOURS;
    
    // === Weather system ===
    private boolean weatherOn = true;
    public Weather currentWeather = Weather.SUNNY;
    private int weatherTimer = 0;        // frames until weather changes
    private int nextWeatherTime;        // random tick to trigger next weather change
    private int minWeatherDuration = 60 * 1;
    private int maxWeatherDuration = 60 * 3;
    private boolean lightningSpawned = false;
    private int lightningCounter = 0;
    private int lightningTime = 5;
    private LightSource lightningLight;
    
    //Event Stuff
    private Colour darkColour;
    private Breaker breaker;
    private boolean queueSpecialCustomer = false;
    private boolean spawnRats = false;
    private int ratCount = 0;
    private int ratSpawnTimer = 0;
    private int maxRatSpawnTime = 0;
    private int ratsSpawned = 0;
    public boolean animalPresent = false;
    
    //ORDERS
    private List<Object> orderList;
    public List<Object> boughtItems;
    private boolean crateOrdered = false;
    private int crateNum;
    
    // Sleep
    private boolean sleeping = false;
    public boolean fadingOut = false;
    private float fadeAlpha = 0f;
    public boolean fadingIn = false;
    private float fadeSpeed = 0.7f; // tweak for faster/slower fade
    
    private int groupChance = 100;

    public World(GamePanel gp) {
        this.gp = gp;
        this.time = dayStart;
        spawnTimer = customerSpawnTimer;
        random = new Random();
        currentSeason = Season.SUMMER;
        gp.mapM.setSeason(currentSeason);
        darkColour = new Colour(51, 60, 58, 200);
        
        boughtItems = new ArrayList<Object>();
        
        currentWeather = Weather.SUNNY;
        resetWeatherTimer();
        
        //gp.npcM.addDishWasher();
    }

    // === Specials generation ===
    public void generateDailySpecials(List<Recipe> unlockedRecipes) {
        todaysSpecials.clear();
        Collections.shuffle(unlockedRecipes);

        // Pick up to 2 specials
        for (int i = 0; i < 2 && i < unlockedRecipes.size(); i++) {
            todaysSpecials.add(unlockedRecipes.get(i));
        }
    }
    public void removeRecipeFromMenu(Recipe recipe) {
        if (recipe == null) return;

        todaysMenu.remove(recipe);

        // If menu becomes empty again, mark as not chosen
        if (todaysMenu.isEmpty()) {
            menuChosen = false;
        }
    }
    public List<Recipe> getTodaysSpecials() {
    	if(todaysSpecials.size() != 0) {
    		return todaysSpecials;
    	}
    	generateDailySpecials(RecipeManager.getUnlockedRecipes());
        return Collections.unmodifiableList(todaysSpecials);
    }
    public boolean isRecipeSpecial(Recipe recipe) {
        if (recipe == null) return false;
        return todaysSpecials.contains(recipe);
    }
    // === Menu management ===
    public void setTodaysMenu(List<Recipe> menu) {
        todaysMenu.clear();
        todaysMenu.addAll(menu);
        menuChosen = true;
    }
    public void addRecipeToMenu(Recipe recipe) {
        if (recipe == null) return;
        if(todaysMenu.size() == maxMenuSize) {
        	return;
        }

        // Only add if not already in menu
        if (!todaysMenu.contains(recipe)) {
            todaysMenu.add(recipe);
        }

        // Once player has added at least 1 recipe, mark menu as chosen
        if (todaysMenu.size() == maxMenuSize) {
            menuChosen = true;
        }
    }
    public List<Recipe> getTodaysMenu() {
        return Collections.unmodifiableList(todaysMenu);
    }
    public Recipe getRandomMenuRecipe() {
        if (todaysMenu.isEmpty()) return null;
        return todaysMenu.get(random.nextInt(todaysMenu.size()));
    }
    public boolean isMenuChosen() {
        return menuChosen;
    }
    private void resetMenu() {
        todaysMenu.clear();
        todaysSpecials.clear();
        menuChosen = false;
    }
    private void resetEventTimer() {
        eventTimer = 0;
        nextEventTime = random.nextInt(maxEventInterval - minEventInterval) + minEventInterval;
    }
    public void powerCut() {
    	Room electrics = gp.mapM.getRoom(3);
    	if(breaker == null) {
        	breaker = (Breaker)electrics.findBuildingWithName("Breaker");
    	}
    	breaker.cutPower();
    }
    private void spill() {
    	if(gp.mapM.isInRoom(0)) {
    		gp.buildingM.addSpill(random.nextInt(2));
    	} else {
    		gp.mapM.getRoom(0).addSpill(random.nextInt(2));
    	}
    }
    private void clogToilet() {
    	if(gp.mapM.isInRoom(4)) {
    		gp.buildingM.addLeak(random.nextInt(2));
    	} else {
    		gp.mapM.getRoom(4).addLeak(random.nextInt(2));
    	}
    }
    public void setSeason(String season) {
        if (season == null || season.isEmpty()) return;

        try {
            this.currentSeason = Season.valueOf(season.toUpperCase());
            gp.mapM.setSeason(this.currentSeason); // make sure map/tiles update visually
        } catch (IllegalArgumentException e) {
            System.err.println("World.setSeason(): Unknown season: " + season);
        }
    }
    private void spawnDuck() {
    	gp.npcM.addDuck();
    }
    private void spawnRats() {
    	spawnRats = true;
    	ratCount = random.nextInt(4) + 3;
    }
    private void triggerRandomEvent() {
        // Placeholder: plug in your event system here
        int eventId = random.nextInt(8);
        
        switch (eventId) {
            case 0: //Powercut
            	powerCut();
                gp.gui.addMessage("PowerCut!", Colour.RED);
                break;
            case 1, 2, 3: // Special customer
                queueSpecialCustomer = true;
                break;
            case 4:
            	spill();
                gp.gui.addMessage("Spillage!", Colour.RED);
            	break;
            case 5:
            	clogToilet();
                gp.gui.addMessage("The Toilet is Clogged!", Colour.RED);
            	break;
            case 6:
            	spawnRats();
                gp.gui.addMessage("Rats!!!!", Colour.RED);
            	break;
            case 7:
            	spawnDuck();
                gp.gui.addMessage("A wild duck just walked in!", Colour.RED);
            	break;
        }
    }
    // === Game loop ===
    public void update(double dt) {
        if(gp.cutsceneM.cutsceneActive) {
            updateCutsceneEffects(dt);
        	return;
        }
        
        if (paused) return;

        updateSleep(dt);
        
        time += dt/timeSpeed;

        if (time >= 24.0f) {
        	time -= 24.0f;
            advanceDay();
        }

        DayPhase currentPhase = getCurrentPhase();
        
        int currentPeriod = getTimeOfDay();
        if (currentPeriod != lastTimePeriod) {
            lastTimePeriod = currentPeriod;
            gp.gui.changeTimeState(currentPeriod);
        }
        
        if(gp.progressM.moreCustomers) {
        	customerSpawnTimer = 12;
        }

        // Customer spawning only during service, and only if menu chosen
        if (currentPhase == DayPhase.SERVICE && menuChosen) {

        	if(!gp.mapM.isInRoom(0)) {
	            if (gp.mapM.getRoom(0).isFreeChair() != null) {
	                spawnTimer+=dt;
	                if (spawnTimer >= customerSpawnTimer) {
	                    spawnTimer = 0;
	                    
	                    if (queueSpecialCustomer) {
	                        queueSpecialCustomer = false;
	                        gp.mapM.getRoom(0).addSpecialCustomer();
	                        gp.gui.addMessage("A special customer has arrived!", Colour.MAGENTA);
	                    } else {
		                    // Decide WHAT to spawn first
	                      	boolean tryGroup = random.nextInt(100) < groupChance;
	                      	if (tryGroup && gp.mapM.getRoom(0).isGroupTable() != null) {
		                        // Spawn a group if we got lucky AND a group table exists
		                    	gp.mapM.getRoom(0).addGroup(random.nextInt(2) + 3);
		                    } else {
		                    	if(gp.mapM.getRoom(0).isFreeSingleChair() != null) {
		                    		gp.mapM.getRoom(0).addCustomer();
		                    	}
		                    }
	                    }
	                    
	                  
	                }
	            }
	        } else {
	        	if (gp.buildingM.isFreeChair() != null) {
	                spawnTimer+=dt;
	                if (spawnTimer >= customerSpawnTimer) {
	                    spawnTimer = 0;
	                    if(queueSpecialCustomer) {
	                    	queueSpecialCustomer = false;
	                     	gp.npcM.addSpecialCustomer();
	                     	gp.gui.addMessage("A special customer has arrived!", Colour.MAGENTA);
	                    } else {
	                    	boolean tryGroup = random.nextInt(100) < groupChance;
	                    	if (tryGroup && gp.buildingM.isGroupTable() != null) {
			                    // Spawn a group if we got lucky AND a group table exists
                    			gp.npcM.addGroup(random.nextInt(2) + 3);
			                } else {
			                	if(gp.buildingM.isFreeSingleChair() != null) {
			                		gp.npcM.addCustomer();
			                	}
			                }
	                    }
	                    
	                    
	                }
	            }
	        }
        	
        }
        if (eventsOn) {
            if (currentPhase == DayPhase.SERVICE && menuChosen) {
                // If we just entered SERVICE phase, give a grace period before first event
                if (lastPhase != DayPhase.SERVICE && eventTimer == 0) {
                    eventTimer = -serviceEventGracePeriod; // start below zero so timer "waits" to reach 0
                }

                eventTimer+=dt;
                if (eventTimer >= nextEventTime) {
                    triggerRandomEvent();
                    resetEventTimer();
                }
            }
	          
	        if(spawnRats) {
	        	ratSpawnTimer+=dt;
	        	if(ratSpawnTimer >= maxRatSpawnTime) {
		        	gp.npcM.addRat();
		        	ratsSpawned++;
		        	ratSpawnTimer = 0;
		        	maxRatSpawnTime = random.nextInt(140) + 30;
		        	if(ratsSpawned >= ratCount) {
		        		ratCount = 0;
		        		ratSpawnTimer = 0;
		        		maxRatSpawnTime = 0;
		        		spawnRats = false;
		        	}
	        	}
	        }
	        
        }
        
     updateWeather(dt);
        
     currentPhase = getCurrentPhase();

     // Phase change check
     if (currentPhase != lastPhase) {
         if (currentPhase == DayPhase.SERVICE) {
             gp.gui.addMessage("The restaurant is now OPEN!", Colour.YELLOW);
         } else if (lastPhase == DayPhase.SERVICE) {
             gp.gui.addMessage("The restaurant is now CLOSED.", Colour.YELLOW);
             
             // Either trigger instantly OR wait
             if (gp.mapM.isRoomEmpty(0)) {
                 gp.gui.startLevelUpScreen();
             } else {
                 waitingForLevelUp = true;
             }
             
             if(day == 2) {
        		 gp.cutsceneM.cutsceneQueued = true;
        		 gp.cutsceneM.cutsceneName = "Ghosts talking";
        	 }
             if(day == 7) {
        		 gp.cutsceneM.cutsceneQueued = true;
        		 gp.cutsceneM.cutsceneName = "Ignis II";
        		 gp.cutsceneM.checkCutsceneTrigger();
        	 }
         }
         lastPhase = currentPhase;
     }

     // Outside that, in update():
     if (waitingForLevelUp && gp.mapM.isRoomEmpty(0)) {
         gp.gui.startLevelUpScreen();
         waitingForLevelUp = false; // reset
     }
     
    }
    public boolean isPowerOn() {
    	if(breaker != null) {
    		return !breaker.isPowerOff();
    	}
    	return true;
    }
    private void advanceDay() {
        day++;
        if (day > MAX_DAYS_PER_SEASON) {
            day = 1;
            currentSeason = currentSeason.next();
            gp.mapM.setSeason(currentSeason);
        }
        
        previousSoulsCollected = gp.player.soulsServed;
        
        // If there was an order waiting, deliver a package
        if (orderList != null && !orderList.isEmpty()) {
        	addParcel();
        }
        if(crateOrdered) {
        	addCrate();
        }
        
        if(day == 2) {
        	gp.cutsceneM.cutsceneQueued = true;
        	gp.cutsceneM.cutsceneName = "Customiser Tutorial";
        }

        // Reset menu for new day
        resetMenu();
        resetEventTimer();

        // Generate new specials at start of new day
        generateDailySpecials(gp.recipeM.getUnlockedRecipes());
        gp.saveM.saveGame();
    }

    // === Time helpers ===
    public String getTime24h() {
        int hours = (int) time;
        int minutes = (int) ((time - hours) * 60);
        return String.format("%02d:%02d", hours, minutes);
    }

    public String getDate() {
        return String.format("%s Day %d", currentSeason.toString(), day);
    }

    // === Getters/Setters ===
    public void setTime(float newTime) {
        this.time = newTime % 24.0f;
    }

    public float getRawTime() {
        return time;
    }
    public void pauseTime() {
        this.paused = true;
    }

    public void resumeTime() {
        this.paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public int getDay() {
        return day;
    }
    public int getTimeOfDay() {
        int hours = (int) time;

        if (hours >= 6 && hours < 12) {
            return 0; // Morning
        } else if (hours >= 12 && hours < 18) {
            return 1; // Afternoon
        } else if (hours >= 18 && hours < 21) {
            return 4; // Evening
        } else {
            return 5; // Night
        }
    }
    public DayPhase getCurrentPhase() {
        int hours = (int) time;

        if (hours >= dayStart && hours < openingTime) {
            return DayPhase.PREPARATION;
        } else if (hours >= openingTime && hours < closingTime) {
            return DayPhase.SERVICE;
        } else {
            return DayPhase.AFTER_HOURS;
        }
    }
    public void orderItems(List<Object> order) {
    	orderList = order;
    }
    public void orderCrate() {
    	crateOrdered = true;
    	crateNum = gp.catalogue.getRandomCatalogue();
    }
    public void sleep() {
        // Advance the calendar to the next day
        
        sleeping = true;
        fadingOut = true;
        fadeAlpha = 0f;
    }
    public void updateSleep(double dt) {
        if (!sleeping) return;

        if (fadingOut) {
            fadeAlpha += fadeSpeed*dt;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                fadingOut = false;

                // Switch the day here, once it's fully black
                advanceDay();
                this.time = dayStart;
                gp.gui.addMessage("A new day begins!", Colour.YELLOW);
            }
        } else {
            fadeAlpha -= fadeSpeed*dt;
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                sleeping = false; // done fading
            }
        }
    }
    public void startFadeOut() {
        fadingOut = true;
    }
    public void startFadeIn() {
        fadingIn = true;
    }
    private void updateCutsceneEffects(double dt) {
    	if (fadingOut) {
            fadeAlpha += fadeSpeed*dt;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                fadingOut = false;
            }
        }
    	if(fadingIn) {
    		fadeAlpha -= fadeSpeed*dt;
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                fadingIn = false;
            }
    	}
    }
    private void updateWeather(double dt) {
        if (!weatherOn) return; // optional if you only want weather during events

        Weather oldWeather = currentWeather;
        weatherTimer+=dt;
        if (weatherTimer >= nextWeatherTime) {
            // pick new weather avoiding repetition
            Weather newWeather;
            do {
                newWeather = Weather.getRandom(random);
            } while (newWeather == currentWeather);

            currentWeather = newWeather;
            resetWeatherTimer(); // schedule next change

            if(currentWeather != oldWeather) {
	            // Display message
	            switch (currentWeather) {
	                case SUNNY -> gp.gui.addMessage("The sun is shining!", Colour.YELLOW);
	                case RAIN -> gp.gui.addMessage("It's raining!", Colour.CYAN);
	                case THUNDERSTORM -> {
	                    gp.gui.addMessage("A thunderstorm begins!", Colour.BLUE);
	                }
	            }
            }
        }
    }
    public void drawWeather(Renderer renderer) {
        switch (currentWeather) {
            case RAIN -> {
                
            }
            case THUNDERSTORM -> {
                if(!lightningSpawned) {
                    if (random.nextFloat() < 0.01f) { // 1% chance per frame
                    	addLightning();
                    }
                }
            }
            default -> {
                // Sunny = no effect
            }
        }
        if(lightningSpawned) {
        	lightningCounter++;
        	if(lightningCounter >= lightningTime) {
        		lightningCounter = 0;
        		lightningSpawned = false;
        		gp.lightingM.removeLight(lightningLight);
        	   	gp.screenShake(10, 5);
        	}
        }
    }
    private void resetWeatherTimer() {
        weatherTimer = 0;
        nextWeatherTime = random.nextInt(maxWeatherDuration - minWeatherDuration) + minWeatherDuration;
    }
    public void addLightning() {
    	lightningSpawned = true;
    	lightningLight = new LightSource(0, gp.frameHeight/2, Colour.WHITE, 48*8*4);
    	gp.lightingM.addLight(lightningLight);
    }
    public void removeLightning() {
    	if(lightningSpawned) {
    		lightningCounter = 0;
    		lightningSpawned = false;
    		gp.lightingM.removeLight(lightningLight);
    	}
    }
    public SettingsSaveData saveSettingsData() {
    	SettingsSaveData data = new SettingsSaveData();
        data.fullScreen = Settings.fullScreen;
        data.bloomEnabled = Settings.bloomEnabled;
        data.fancyLighting = Settings.fancyLighting;
        data.showUsernames = Settings.showUsernames;
        
        return data;
    }
    public void applySettingsData(SettingsSaveData data) {
    	Settings.showUsernames = data.showUsernames;
    	Settings.fancyLighting = data.fancyLighting;
    	Settings.fullScreen = data.fullScreen;
    	Settings.bloomEnabled = data.bloomEnabled;
    }
    private void addParcel() {
		Parcel parcel = new Parcel(gp, 10*48, 9*48, new ArrayList<>(orderList));
        gp.buildingM.addBuilding(parcel);
        gp.gui.addMessage("A Parcel has arrived!", Colour.MAGENTA);
        
        boughtItems.addAll(orderList);
    	
        orderList.clear();
    }
    private void addCrate() {
		Parcel parcel = new Parcel(gp, 9*48, 9*48, crateNum);
        gp.buildingM.addBuilding(parcel);
        gp.gui.addMessage("A Mystery Crate has arrived!", Colour.MAGENTA);
        
        crateOrdered = false;
        crateNum = -1;
    }
    public void setOrderData(OrderSaveData data) {
    	if(!data.orderEmpty) {
    		orderList = new ArrayList<Object>();
    		List<Building> buildings = gp.buildingRegistry.unpackSavedBuildings(data.buildingInventory);
    		orderList.addAll(buildings);

			for(Integer i: data.beamInventory) {
				orderList.add(new Beam(gp, i));
			}
			for(Integer i: data.floorpaperInventory) {
				orderList.add(new FloorPaper(gp, i));
			}
			for(Integer i: data.wallpaperInventory) {
				orderList.add(new WallPaper(gp, i));
			}
			for(Integer i: data.chairSkinInventory) {
				orderList.add(new ChairSkin(gp, i));
			}
			for(Integer i: data.tableSkinInventory) {
				orderList.add(new CounterSkin(gp, i));
			}
			if (orderList != null && !orderList.isEmpty()) {
		        addParcel();
		    }
    	}
    }
    public OrderSaveData saveOrderData() { 
    	OrderSaveData data = new OrderSaveData();
    	if(orderList != null) {	
	    	if(orderList.size() > 0) {
	    		
	    		data.orderEmpty = false;
	    		List<FloorPaper> floorpaperInventory = new ArrayList<>();
	    		List<WallPaper> wallpaperInventory = new ArrayList<>();
	    		List<Beam> beamInventory = new ArrayList<>();
	    		List<ChairSkin> chairSkinInventory = new ArrayList<>();
	      		List<CounterSkin> tableSkinInventory = new ArrayList<>();
	    		List<Building> buildings = new ArrayList<>();
	    		
	    		for(Object o: orderList) {
	    			if(o instanceof FloorPaper f) {
	    				floorpaperInventory.add(f);
	    			} else if(o instanceof WallPaper f) {
	    				wallpaperInventory.add(f);
	    			} else if(o instanceof Beam f) {
	    				beamInventory.add(f);
	    			} else if(o instanceof ChairSkin f) {
	    				chairSkinInventory.add(f);
	    			} else if(o instanceof CounterSkin f) {
	    				tableSkinInventory.add(f);
	    			} else if(o instanceof Building f) {
	    				buildings.add(f);
	    			}
	    		}
	    		
		    	data.buildingInventory = gp.buildingRegistry.saveBuildings(buildings);	
				
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
				for(CounterSkin b: tableSkinInventory) {
					tables.add(b.preset);
				}
				data.tableSkinInventory = tables;
	    	}
    	}
    	return data;
    }
    public WorldSaveData saveWorldData() {
    	WorldSaveData data = new WorldSaveData();
    	data.day = day;
    	data.previousSoulsCollected = previousSoulsCollected;
    	data.currentSeason = currentSeason;
    	data.crateOrdered = crateOrdered;
    	data.crateNum = crateNum;
  
    	return data;
    }
    public void setSaveData(WorldSaveData data) {
    	time = dayStart;
    	day = data.day;
    	previousSoulsCollected = data.previousSoulsCollected;
    	currentSeason = data.currentSeason;
    	crateNum = data.crateNum;
    	crateOrdered = data.crateOrdered;
    	if(crateOrdered) {
            addCrate();
    	}
    }
    public void drawFilters(Renderer renderer) {
    	if(breaker != null) {
	    	if(breaker.isPowerOff()) {
	    		//p.lightingM.setPowerOff();
	    	}
    	}
    	  if (fadeAlpha > 0f) {
    	        Colour c = new Colour(0, 0, 0, Math.min(1f, fadeAlpha));
    	        renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, c);
    	    }
    	
    }
}
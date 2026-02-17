package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import entity.buildings.Building;
import entity.buildings.BuildingManager;
import entity.items.Item;
import entity.items.ItemManager;
import entity.npc.NPC;
import entity.npc.NPCManager;
import main.renderer.Renderer;
import map.Customiser;
import map.LightingManager;
import map.MapBuilder;
import map.MapManager;
import map.particles.ParticleSystem;
import utility.BuildingRegistry;
import utility.Catalogue;
import utility.GameManager;
import utility.ItemRegistry;
import utility.ProgressManager;
import utility.RecipeManager;
import utility.RoomHelperMethods;
import utility.UpgradeManager;
import utility.cutscene.CutsceneManager;
import utility.minigame.MiniGameManager;

public class World {
	
	GamePanel gp;

    public boolean isServer;

    // ===== AUTHORITATIVE STATE =====
    public BuildingManager buildingM;
    public ItemManager itemM;
    public NPCManager npcM;
    public MapManager mapM;
    public RecipeManager recipeM;
    public UpgradeManager upgradeM;
    public ProgressManager progressM;
    public MiniGameManager minigameM;
    public ArrayList<Entity> updateEntityList;
    public ArrayList<Entity> entityList;
    public Catalogue catalogue;
    public GameManager gameM;
    public Customiser customiser;
    public PathFinder pathF;

    // ===== VISUAL / CLIENT SYSTEMS =====
    public LightingManager lightingM;
    public ParticleSystem particleM;
    public CutsceneManager cutsceneM;
    public RoomHelperMethods roomH;
    public MapBuilder mapB;

    // ===== SHARED / IMMUTABLE =====
    public ItemRegistry itemRegistry;
    public BuildingRegistry buildingRegistry;
    
    public World(GamePanel gp, boolean isServer) {
    	this.gp = gp;
        this.isServer = isServer;
    }
    public void initialiseManagers() {
        buildingM = new BuildingManager(gp);
        itemM = new ItemManager(gp);
        npcM = new NPCManager(gp);
        mapM = new MapManager(gp);
        updateEntityList = new ArrayList<>();
        entityList = new ArrayList<>();
        itemRegistry = new ItemRegistry(gp);
        buildingRegistry = new BuildingRegistry(gp);
        catalogue = new Catalogue(gp);
        gameM = new GameManager(gp);
        particleM = new ParticleSystem(gp);
        customiser = new Customiser(gp);
        pathF = new PathFinder(gp);
        recipeM = new RecipeManager();
        upgradeM = new UpgradeManager(gp);
        progressM = new ProgressManager(gp);
        minigameM = new MiniGameManager(gp);
        roomH = new RoomHelperMethods();
        cutsceneM = new CutsceneManager(gp);
        mapB = new MapBuilder(gp);
        lightingM = new LightingManager(gp, gp.camera);
    }
    public void startGame() {
    	  buildingM = new BuildingManager(gp);
          itemM = new ItemManager(gp);
          npcM = new NPCManager(gp);
          mapM = new MapManager(gp);
          updateEntityList = new ArrayList<>();
          entityList = new ArrayList<>();
          gameM = new GameManager(gp);
          lightingM = new LightingManager(gp, gp.camera);
          customiser = new Customiser(gp);
          cutsceneM = new CutsceneManager(gp);
          catalogue = new Catalogue(gp);
          recipeM = new RecipeManager();
          upgradeM = new UpgradeManager(gp);
          progressM = new ProgressManager(gp);
          minigameM = new MiniGameManager(gp);
          particleM = new ParticleSystem(gp);
          itemRegistry = new ItemRegistry(gp);
          buildingRegistry = new BuildingRegistry(gp);
          mapB = new MapBuilder(gp);
    }
    public void serverUpdate(double dt) {
        if (!isServer) return;

        if(gp.currentState == gp.playState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.catalogueState || gp.currentState == gp.xpState || gp.currentState == gp.chatState) {
	    		    	
	    	mapM.updateState(dt);
	    	buildingM.updateState(dt);
	    	npcM.updateState(dt);
	    	itemM.updateState(dt);
	    	gameM.update(dt);
	    	minigameM.updateState(dt);
	    	if(gp.currentState == gp.customiseRestaurantState) {
	    		customiser.updateState(dt);
	    	}
    	} else if(gp.currentState == gp.mapBuildState) {
    		buildingM.updateState(dt);
    	}
		catalogue.updateState(dt);
    }
    public void clientUpdate(double dt) {
    	particleM.update(dt);
        if(gp.currentState == gp.playState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.catalogueState || gp.currentState == gp.xpState || gp.currentState == gp.chatState) {
        	gp.player.update(dt);
        	lightingM.update(dt);
			cutsceneM.update(dt);
			
			if(gp.multiplayer) {
	    		for(Player p: gp.playerList) {
	    			if(p.getUsername() != gp.player.getUsername()) {
	    				if(p.currentRoomIndex == gp.player.currentRoomIndex) {
	    					p.updateInteractHitbox();
	    					p.updateAnimations(dt);
	    				}
	    			}
	    		}
	    	}
			
			mapM.inputUpdate(dt);
	    	buildingM.inputUpdate(dt);
	    	npcM.inputUpdate(dt);
	    	itemM.inputUpdate(dt);
	    	minigameM.inputUpdate(dt);
	    	if(gp.currentState == gp.customiseRestaurantState) {
	    		customiser.inputUpdate(dt);
	    	}
        }
    	
	    if(gp.currentState == gp.mapBuildState) {
	    	mapB.inputUpdate(dt);
	    }
		catalogue.updateState(dt);
    }
    
    public void draw(Renderer renderer) {
        if(gp.currentState == gp.playState || gp.currentState == gp.pauseState || gp.currentState == gp.achievementState || gp.currentState == gp.settingsState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.xpState || gp.currentState == gp.dialogueState || gp.currentState == gp.chatState) {
    		mapM.draw(renderer);
    		
    		Building[] bottomLayer = buildingM.getBottomLayer();
	        for(int i = 0; i < bottomLayer.length-1; i++) {
	        	if(bottomLayer[i] != null) {
	        		bottomLayer[i].draw(renderer);
	        	}
	        }
	        
	        Building[] secondLayer = buildingM.getSecondLayer();
	        for(int i = 0; i < secondLayer.length-1; i++) {
	        	if(secondLayer[i] != null) {
	        		secondLayer[i].draw(renderer);
	        	}
	        }
    		
	        if(gp.multiplayer) {
        		for (int i = 0; i < gp.getPlayerList().size(); i++) {
    	            if (gp.getPlayerList().get(i) != null) {
    	            	Player p = gp.getPlayerList().get(i);
    	            	if(!p.getUsername().equals(gp.player.getUsername())) {
	    	            	if(p.currentRoomIndex == gp.player.currentRoomIndex) {
		    	            	entityList.add(p);
	    	            	}
    	            	}
    	            }
    	        }
               	entityList.add(gp.player);
        	} else {
	        	entityList.add(gp.player);
        	}
        	
        	Building[] builds = buildingM.getBuildingsToDraw();
	        for(int i = 0; i < builds.length-1; i++) {
	        	if(builds[i] != null) {
	        		entityList.add(builds[i]);
	        		builds[i].drawEmissive(renderer);
	        	}
	        }
	        Item[] itemsInBuildings = buildingM.getBuildingItems();
	        for(int i = 0; i < itemsInBuildings.length-1; i++) {
	        	if(itemsInBuildings[i] != null) {
	        		entityList.add(itemsInBuildings[i]);
	        	}
	        }
	        List<NPC> copy = new ArrayList<NPC>(npcM.getNPCs());
	        
	        for(NPC npc: copy) {
	        	if(npc != null) {
	        		entityList.add(npc);
	        	}
	        }
        	
        	//SORT
    		ArrayList<Entity> listCopy = new ArrayList<Entity>(entityList);

            Collections.sort(listCopy, new Comparator<Entity>() {
                //@Override
                public int compare(Entity e1, Entity e2) {
 
                    int result = Integer.compare((int)e1.hitbox.y, (int)e2.hitbox.y);
                    return result;
                }
            });
            
            entityList = listCopy;
            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++) {
            	Entity a = entityList.get(i);
            	if(a != null) {
	            	if(a.isOnScreen(gp.camera, gp.frameWidth, gp.frameHeight)) {
	            		a.draw(renderer);
	            	}
            	}
            }
            //EMPTY ENTITY LIST
            entityList.clear();
            
            Building[] thirdLayer = buildingM.getThirdLayer();
	        for(int i = 0; i < thirdLayer.length-1; i++) {
	        	if(thirdLayer[i] != null) {
	        		entityList.add(thirdLayer[i]);
	        	}
	        }
	        
		       listCopy = new ArrayList<Entity>(entityList);

	            Collections.sort(listCopy, new Comparator<Entity>() {
	                //@Override
	                public int compare(Entity e1, Entity e2) {
	
	                    int result = Integer.compare((int)e1.hitbox.y, (int)e2.hitbox.y);
	                    return result;
	                }
	            });
	            
	            entityList = listCopy;
	            //DRAW ENTITIES
	            for(int i = 0; i < entityList.size(); i++) {
	            	Entity a = entityList.get(i);
	            	if(a != null) {
	            		if(a.isOnScreen(gp.camera, gp.frameWidth, gp.frameHeight)) {
		            		a.draw(renderer);
		            	}
	            	}
	            }
	            //EMPTY ENTITY LIST
	            entityList.clear();
	        
	        List<Item> items = new ArrayList<Item>(itemM.getItems());
	        for(Item item: items) {
	        	if(item != null) {
	        		item.draw(renderer);
	        	}
	        }
	        
	        if(gp.multiplayer) {
        		for (int i = 0; i < gp.getPlayerList().size(); i++) {
    	            Player p = gp.getPlayerList().get(i);
    	            if(p.currentRoomIndex == gp.player.currentRoomIndex) {
	    	            p.drawOverlay(renderer);
    	            }
    	        }
        	} else {
        		gp.player.drawOverlay(renderer);
        	}
	        
	        for(NPC npc: copy) {
	        	if(npc != null) {
	        		npc.drawOverlay(renderer);
	        	}
	        }
	    
	        Building[] fourthLayer = buildingM.getFourthLayer();
	        for(int i = 0; i < fourthLayer.length-1; i++) {
	        	if(fourthLayer[i] != null) {
	        		fourthLayer[i].draw(renderer);
	        	}
	        }
	        
	        
	        Building[] fifthLayer = buildingM.getFifthLayer();
	        for(int i = 0; i < fifthLayer.length-1; i++) {
	        	if(fifthLayer[i] != null) {
	        		entityList.add(fifthLayer[i]);
	        	}
	        }
	        
	        
	       listCopy = new ArrayList<Entity>(entityList);

            Collections.sort(listCopy, new Comparator<Entity>() {
                //@Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare((int)e1.hitbox.y, (int)e2.hitbox.y);
                    return result;
                }
            });
            
            entityList = listCopy;
            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++) {
            	Entity a = entityList.get(i);
            	if(a != null) {
	            	if(a.isOnScreen(gp.camera, gp.frameWidth, gp.frameHeight)) {
	            		a.draw(renderer);
	            	}
            	}
            }
            //EMPTY ENTITY LIST
            entityList.clear();
	        
            mapM.drawForeground(renderer);
	        
            
            particleM.draw(renderer);
    	}


    }
    public void drawEmissiveBuffers(Renderer renderer) {
        if(gp.currentState == gp.playState || gp.currentState == gp.pauseState || gp.currentState == gp.achievementState || gp.currentState == gp.settingsState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.xpState || gp.currentState == gp.dialogueState) {
	    	Building[] builds = buildingM.getBuildingsToDraw();
	        for(int i = 0; i < builds.length-1; i++) {
	        	if(builds[i] != null) {
	        		entityList.add(builds[i]);
	        		builds[i].drawEmissive(renderer);
	        	}
	        }
    	
	        particleM.drawEmissive(renderer);
        }
    }
    public void drawGUI(Renderer renderer) {
    	 if(gp.currentState == gp.playState || gp.currentState == gp.pauseState || gp.currentState == gp.achievementState || gp.currentState == gp.settingsState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.xpState || gp.currentState == gp.dialogueState) {
	            Building[] thirdLayer = buildingM.getThirdLayer();
	        	for (int i = 0; i < thirdLayer.length; i++) {
	            if (thirdLayer[i] != null) {
	                thirdLayer[i].drawOverlayUI(renderer);
	            }
	        }
	        	
		    Building[] fourthLayer = buildingM.getFourthLayer();
	        for (int i = 0; i < fourthLayer.length; i++) {
	            if (fourthLayer[i] != null) {
	                fourthLayer[i].drawOverlayUI(renderer);
	            }
	        }
	
	        Building[] fifthLayer = buildingM.getFifthLayer();
	        for (int i = 0; i < fifthLayer.length; i++) {
	            if (fifthLayer[i] != null) {
	                fifthLayer[i].drawOverlayUI(renderer);
	            }
	        }

	        Building[] builds = buildingM.getBuildings();
	        for (int i = 0; i < builds.length; i++) {
	            if (builds[i] != null) {
	                builds[i].drawOverlayUI(renderer);
	            }
	        }
	        
	        Item[] itemsInBuildings = buildingM.getBuildingItems();
	        for(int i = 0; i < itemsInBuildings.length-1; i++) {
	        	if(itemsInBuildings[i] != null) {
	        		itemsInBuildings[i].drawOverlay(renderer);
	        	}
	        }
	        
	        gameM.drawFilters(renderer);
	        gameM.drawWeather(renderer); 
	        cutsceneM.draw(renderer);
	}
    	 
			if(gp.currentState == gp.mapBuildState) {
				mapM.draw(renderer);
				mapM.drawForeground(renderer);
				
				Building[] bottomLayer = buildingM.getBottomLayer();
			    for(int i = 0; i < bottomLayer.length-1; i++) {
			    	if(bottomLayer[i] != null) {
			    		bottomLayer[i].draw(renderer);
			    	}
			    }
			    Building[] middleLayer = buildingM.getSecondLayer();
			    for(int i = 0; i < middleLayer.length-1; i++) {
			    	if(middleLayer[i] != null) {
			    		middleLayer[i].draw(renderer);
			    	}
			    }
				
				Building[] main = buildingM.getBuildingsToDraw();
			    for(int i = 0; i < main.length-1; i++) {
			    	if(main[i] != null) {
			    		main[i].draw(renderer);
			    	}
			    }
				
				Building[] secondLayer = buildingM.getThirdLayer();
			    for(int i = 0; i < secondLayer.length-1; i++) {
			    	if(secondLayer[i] != null) {
			    		secondLayer[i].draw(renderer);
			    	}
			    }
			    Building[] thirdLayer = buildingM.getFourthLayer();
			    for(int i = 0; i < thirdLayer.length-1; i++) {
			    	if(thirdLayer[i] != null) {
			    		thirdLayer[i].draw(renderer);
			    	}
			    }
			    Building[] fourthLayer = buildingM.getFifthLayer();
			    for(int i = 0; i < fourthLayer.length-1; i++) {
			    	if(fourthLayer[i] != null) {
			    		fourthLayer[i].draw(renderer);
			    	}
			    }
				mapB.draw(renderer);
			} 
			if(gp.currentState == gp.customiseRestaurantState) {
				customiser.draw(renderer);
			}
			minigameM.draw(renderer);
    	 
    }
    public void drawGodRays(Renderer renderer) {
    	Building[] main = buildingM.getBuildingsToDraw();
	    for(int i = 0; i < main.length-1; i++) {
	    	if(main[i] != null) {
	    		main[i].drawGodRay(renderer);
	    	}
	    }
    }
	
}

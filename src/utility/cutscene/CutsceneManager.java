package utility.cutscene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import entity.buildings.Building;
import entity.buildings.Chair;
import entity.buildings.Door;
import entity.buildings.FloorDecor_Building;
import entity.buildings.Lantern;
import entity.buildings.Rubble;
import entity.buildings.Stove;
import entity.buildings.Torch;
import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.Pet;
import entity.npc.SpecialCustomer;
import entity.npc.StoryCharacter;
import main.GamePanel;

public class CutsceneManager {

    private GamePanel gp;
    public boolean cutsceneActive = false;
    private List<CutsceneEvent> events;
    private int currentEventIndex = 0;
    public String cutsceneName = "";
    
    public boolean cutsceneQueued = false;
    public List<String> cutscenePlayed;
    
    private boolean drawHighlight = false;
    private Rectangle2D.Float highlightArea;
    private Stroke highlightStroke; 
    //TODO make it save which cutscenes have played

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
        events = new ArrayList<>();
        cutscenePlayed = new ArrayList<String>();
        highlightStroke = new BasicStroke(3);
    }

    public void startCutscene(List<CutsceneEvent> events) {
        this.events = events;
        this.cutsceneActive = true;
        this.currentEventIndex = 0;
        gp.player.setControlEnabled(false); // freeze player input
        gp.world.pauseTime();
    }

    public void update() {
        if (!cutsceneActive) return;

        if (currentEventIndex < events.size()) {
            CutsceneEvent current = events.get(currentEventIndex);
            current.update();

            if (current.isFinished()) {
                currentEventIndex++;
            }
        } else {
            endCutscene();
        }
    }

    public void endCutscene() {
    	cutsceneActive = false;
        gp.player.setControlEnabled(true); // re-enable player input
        gp.world.resumeTime();
    }

    public boolean isActive() {
        return cutsceneActive;
    }
    public void checkCutsceneTrigger() {
    	if(isActive()) {
    		return;
    	}
    	//FIRST ROOM ENTRY CUTSCENES
    	if(gp.player.currentRoomIndex == 7) {
    		if(!cutscenePlayed.contains("Enter Corridor")) {
    			enterCorridor();
    		}
    	}
    	
    	if(!cutsceneQueued) {
    		return;
    	}

    	boolean played = false;
    	
    	if(gp.player.currentRoomIndex == 0) {
    		if(!cutscenePlayed.contains("Ghosts talking") && cutsceneName.equals("Ghosts talking")) {
    			seeGhostsTalking();
    			played = true;
    		}
    		else if(!cutscenePlayed.contains("Ignis I") && cutscenePlayed.contains("Ghosts talking") && cutsceneName.equals("Ignis I")) {
    			ignisI();
    			played = true;
    		} else if(!cutscenePlayed.contains("Ignis II") && cutscenePlayed.contains("Ignis I") && cutsceneName.equals("Ignis II")) {
    			ignisII();
    			played = true;
    		} else if(!cutscenePlayed.contains("Customiser Tutorial") && cutsceneName.equals("Customiser Tutorial")) {
    			customiseTutorial();
    			played = true;
    		} 
    	} 
    	
    	if(played) {
    		cutsceneName = "";
	    	cutsceneQueued = false;
    	}
    }
    public void setCutscenesWatched(List<String> cutscenePlayed) {
		this.cutscenePlayed = cutscenePlayed;
	}
    public List<String> getCutscenesWatched() {
		return cutscenePlayed;
	}
    
    public void enterCorridor() {
    	cutscenePlayed.add("Enter Corridor");

        List<CutsceneEvent> events = new ArrayList<>();    	
        
        
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = true;
        }));
        NPC playerNPC = new StoryCharacter(gp, gp.player.hitbox.x, gp.player.hitbox.y, 2);
        events.add(new AddNPCEvent(gp, playerNPC));
        events.add(new WaitEvent(20));
        
        events.add(new NPCMoveEvent(gp, playerNPC, 12, 9));
        
        events.add(new WaitEvent(120)); 
        
        events.add(new DialogueEvent(gp, playerNPC, "This place feels... wrong."));
        events.add(new WaitEvent(60)); 
        

    	events.add(new ActionEvent(() -> {
    		List<Building> torches = gp.buildingM.findBuildingsWithName("Torch");
    	    for (Building b: torches) {
    	    	Torch t = (Torch)b;
    	    	t.turnOn();
    	    }
    	}));
        events.add(new WaitEvent(20)); 
        events.add(new ActionEvent(() -> {
    		gp.world.addLightning();
    	}));
        
        events.add(new DialogueEvent(gp, playerNPC, "Did those lights just turn themselves on?"));

        events.add(new WaitEvent(20)); 
        
        
        events.add(new ActionEvent(() -> {
    		gp.world.addLightning();
    	}));
        
        events.add(new DialogueEvent(gp, playerNPC, "It looks as if they left everything behind. I wonder why this place closed down all those years ago."));
        events.add(new WaitEvent(20)); 
        
    	events.add(new ActionEvent(() -> {
    		List<Building> torches = gp.buildingM.findBuildingsWithName("Torch");
    	    for (Building b: torches) {
    	    	Torch t = (Torch)b;
    	    	t.turnOff();
    	    }
    	}));
    	
        events.add(new WaitEvent(40)); 
        events.add(new DialogueEvent(gp, playerNPC, "Maybe it was just a mind trick. I should get some rest."));

        
        events.add(new ResetZoomEvent(gp));
        
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = false;
        	gp.player.hitbox.x = playerNPC.hitbox.x;
        	gp.player.hitbox.y = playerNPC.hitbox.y;
        	gp.player.setDirection(playerNPC.getDirection());
        }));
        events.add(new RemoveNPCEvent(gp, playerNPC));
        
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    
    public void ignisI() {

    	cutscenePlayed.add("Ignis I");
    	

        List<CutsceneEvent> events = new ArrayList<>();
        
    	Lantern lantern = (Lantern)gp.buildingM.findBuildingWithName("Lantern");

        
        events.add(new ActionEvent(() -> {
        	Stove stove = (Stove)gp.buildingM.findBuildingWithName("Stove");
        	stove.lightFlame();
        	highlightArea = new Rectangle2D.Float(10*48, 6*48, 48*2, 48*2);
        	lantern.setFlicker(true);
        }));
        
        events.add(new ConditionalWaitEvent(gp, () -> {
        	if(gp.player.hitbox.intersects(highlightArea)) {
        		return true;
        	}
        	return false;
        }));
        
        NPC ignis = new StoryCharacter(gp, 0, 0, 5);
        NPC player = new StoryCharacter(gp, 0, 0, 2);
        
        events.add(new AddNPCEvent(gp, ignis, 11, 6));
        events.add(new ActionEvent(() -> {
        	lantern.setFlicker(false);
          	lantern.turnOff();
        }));
        
        events.add(new WaitEvent(60));
        events.add(new CameraFollowEvent(gp, ignis, 1.6f));
        
        events.add(new WaitEvent(20));
        events.add(new DialogueEvent(gp, ignis, "...I told them... the heat must never go out..."));
        events.add(new WaitEvent(20));
        events.add(new DialogueEvent(gp, ignis, "We must fix that damned breaker."));
        
        events.add(new ActionEvent(() -> {
        	Stove stove = (Stove)gp.buildingM.findBuildingWithName("Stove");
        	stove.stopFlame();
        	highlightArea = null;
        }));
        
        events.add(new ActionEvent(() -> {
            gp.world.addLightning();
            
        }));
        
        events.add(new RemoveNPCEvent(gp, ignis));
        events.add(new ActionEvent(() -> {
        	Rubble b = (Rubble)gp.buildingM.findBuildingWithName("Barricade");
        	if(b != null) {
        		b.explode();
        		highlightArea = null;
        	}
        }));
        
        events.add(new ActionEvent(() -> {
        	lantern.turnOn();
        }));
        
        events.add(new DialogueEvent(gp, player, "It seems that rubble in front of the door has cleared."));
        events.add(new WaitEvent(20)); 
        
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    public void ignisII() {

    	cutscenePlayed.add("Ignis II");
    	
        events.add(new WaitEvent(40)); 
        
        Door door = gp.buildingM.findDoor(7);
        
        float x = door.hitbox.x + door.hitbox.width/2 - 100;
        float y = door.hitbox.y + door.hitbox.height/2;
    	  
    	   events.add(new ConditionalWaitEvent(gp, () -> {
    		   if(gp.player.currentRoomIndex == 0) {
    			   gp.particleM.spawnEmberAlongPath(10*48, 5*48, x+16, y, 20);
    		   }
               if(gp.player.currentRoomIndex == 7) {
               	return true;
               } else {
               	return false;
               }
           }));
        
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = true;
        }));
        
        events.add(new ActionEvent(() -> {
        	gp.particleM.setSpawnEmbers(5*48, 9*48, 8*48, 7*48, 20);
        }));
        NPC playerNPC = new StoryCharacter(gp, gp.player.hitbox.x, gp.player.hitbox.y, 2);
        events.add(new AddNPCEvent(gp, playerNPC));
        events.add(new WaitEvent(20));
        
        events.add(new NPCMoveEvent(gp, playerNPC, 11, 8));
        events.add(new ActionEvent(() -> {
        	playerNPC.setDirection("Left");
        }));
        
        events.add(new WaitEvent(80)); 
        
        events.add(new DialogueEvent(gp, playerNPC, "What is that?"));
        events.add(new WaitEvent(20));
        events.add(new DialogueEvent(gp, playerNPC, "It seems to be leading in there..."));
        events.add(new NPCMoveEvent(gp, playerNPC, 7, 8));
        
        events.add(new ActionEvent(() -> {
        	gp.particleM.stopEmbers();
         	playerNPC.setDirection("Up");
         	Door door1 = gp.buildingM.findDoor(9);
         	door1.setDoorLight(true);
         	gp.particleM.setRandomShaking(true);
        }));
        
        events.add(new WaitEvent(20)); 
        NPC ignis = new StoryCharacter(gp, 0, 0, 5);
        
        events.add(new DialogueEvent(gp, ignis, "You dare tell me to lower the heat? You’ll burn with mediocrity!"));
        events.add(new WaitEvent(20)); 
        
        events.add(new ActionEvent(() -> {
        	Door door1 = gp.buildingM.findDoor(9);
         	door1.setDoorLight(false);
         	gp.particleM.setRandomShaking(false);
        	door1.unlock();
        }));
        events.add(new WaitEvent(20)); 
        events.add(new DialogueEvent(gp, ignis, "Did that door just unlock itself?!"));
        events.add(new WaitEvent(20)); 
        
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = false;
        	gp.player.hitbox.x = playerNPC.hitbox.x;
        	gp.player.hitbox.y = playerNPC.hitbox.y;
        	gp.player.setDirection(playerNPC.getDirection());
        }));
        events.add(new RemoveNPCEvent(gp, playerNPC));
        events.add(new WaitEvent(20)); 
        
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    public void seeGhostsTalking() {
    	
    	cutscenePlayed.add("Ghosts talking");

        List<CutsceneEvent> events = new ArrayList<>();
        
    	Lantern lantern = (Lantern)gp.buildingM.findBuildingWithName("Lantern");
        
        events.add(new ActionEvent(() -> {
        	lantern.setFlicker(true);
        	gp.player.resetAnimation(0);
        }));
        
        // 1.
        NPC ghost1 = new StoryCharacter(gp, 0, 0, 3);
        ghost1.setDirection("Left");
        NPC ghost2 = new StoryCharacter(gp, 0, 0, 3);
        events.add(new AddNPCEvent(gp, ghost1, 15, 9));
        events.add(new AddNPCEvent(gp, ghost2, 14, 9));

        events.add(new WaitEvent(120));
        
        events.add(new DialogueEvent(gp, ghost1, "...have you got your invitation to the banquet? I've heard its going to be the best night of the year!"));
        events.add(new WaitEvent(60));
        events.add(new DialogueEvent(gp, ghost2, "Yes I've just got mine last week, I better prepare my dress."));
        
        events.add(new WaitEvent(60));
        
        events.add(new ActionEvent(() -> {
            ghost1.setDirection("Up");
            ghost2.setDirection("Up");
        }));
        
        
        events.add(new DialogueEvent(gp, ghost2, "You shouldn't be here."));
        
        events.add(new ActionEvent(() -> {
            gp.world.addLightning();
        }));
        events.add(new RemoveNPCEvent(gp, ghost1));
        events.add(new RemoveNPCEvent(gp, ghost2));
        events.add(new ActionEvent(() -> {
        	lantern.setFlicker(false);
        }));
        
        events.add(new WaitEvent(60));
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    public void customiseTutorial() {
    	
    	cutscenePlayed.add("Customiser Tutorial");

        List<CutsceneEvent> events = new ArrayList<>();
        
        // 1.
        NPC deliveryMan = new StoryCharacter(gp, 0, 0, 4);
        events.add(new AddNPCEvent(gp, deliveryMan));

        // 2. Camera follows ghost while zooming in
        events.add(new CameraFollowEvent(gp, deliveryMan, 1.6f));
        
        // 3. Move ghost into scene
        events.add(new NPCMoveEvent(gp, deliveryMan, 12, 9));
        // 4. Show dialogue above ghost for 3 seconds (assuming 60 FPS → 180 frames)
        events.add(new DialogueEvent(gp, deliveryMan, "Hi, I'm your local delivery driver for pascal deliveries. You can order furniture and cooking equipment and I'll deliver it straight to your door."));

        // 5. Pause for a moment (can be a WaitEvent)
        events.add(new WaitEvent(20)); // wait 1 second
        
        events.add(new DialogueEvent(gp, deliveryMan, "To start, use the computer in your room to shop on the online catalogue. Once you make an order it'll be delivered the next day."));
        events.add(new WaitEvent(20)); // wait 1 second

        events.add(new DialogueEvent(gp, deliveryMan, "Then press C to customise the restaurant, although this can only be done when it's not open to customers."));        
        events.add(new WaitEvent(20)); // wait 1 second

        events.add(new DialogueEvent(gp, deliveryMan, "Here's our complementary house plant, why don't you place it down in here."));
        events.add(new WaitEvent(20)); // wait 1 second
        
        events.add(new ActionEvent(() -> {
        	gp.customiser.addToInventory(new FloorDecor_Building(gp, 0, 0, 0));
        }));
        
        events.add(new ResetZoomEvent(gp)); // wait 1 second
        
        events.add(new ActionEvent(() -> {
        	drawHighlight = true;
        	highlightArea = new Rectangle2D.Float(756, 371, 48, 48);
        }));
        
        events.add(new ConditionalWaitEvent(gp, () -> {
        	Building plant = gp.buildingM.findBuildingWithName("Plant 1");
            if(plant != null && plant.hitbox.x == 756 && plant.hitbox.y == 372) {
            	return true;
            } else {
            	return false;
            }
        }));
        
        events.add(new ActionEvent(() -> {
        	drawHighlight = false;
        	highlightArea = null;
        }));

        // 8. After boxes are moved, show dialogue
        events.add(new DialogueEvent(gp, deliveryMan, "Nice one, you can also press shift and click it to pick it up, if you feel like moving it."));
        events.add(new WaitEvent(20)); // wait 1 second

        events.add(new DialogueEvent(gp, deliveryMan, "Anyway, I'll leave you to it."));
        events.add(new WaitEvent(20)); // wait 1 second
        
        events.add(new NPCMoveEvent(gp, deliveryMan, 9, 11));
        
        events.add(new RemoveNPCEvent(gp, deliveryMan));
        events.add(new ResetZoomEvent(gp));
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    public void enterDestroyedRestaurant() {
    	
    	cutscenePlayed.add("Destroyed Restaurant");
    	
        gp.mapM.getRoom(0).setDestroyed();

        List<CutsceneEvent> events = new ArrayList<>();
        
        // 1.
        NPC owner = new StoryCharacter(gp, 0, 0, 0);
        events.add(new AddNPCEvent(gp, owner));
        
        events.add(new NPCMoveEvent(gp, owner, 7, 9));

        // 2. Camera follows ghost while zooming in
        events.add(new CameraFollowEvent(gp, owner, 1.6f));
        
        // 3. Move ghost into scene
        events.add(new NPCMoveEvent(gp, owner, 12, 9));
        // 4. Show dialogue above ghost for 3 seconds (assuming 60 FPS → 180 frames)
        events.add(new DialogueEvent(gp, owner, "This place is a dump. Lets first clear some of this mess up."));

        // 5. Pause for a moment (can be a WaitEvent)
        events.add(new WaitEvent(40)); // wait 1 second
        
        events.add(new ResetZoomEvent(gp)); // wait 1 second
        
        events.add(new ConditionalWaitEvent(gp, () -> {
            if(!gp.buildingM.hasBuildingWithName("Rubble") && !gp.buildingM.hasBuildingWithName("Spill")) {
            	return true;
            } else {
            	return false;
            }
        }));

        // 8. After boxes are moved, show dialogue
        events.add(new DialogueEvent(gp, owner, "Great! That mess is cleared. The builders can handle the rest. They'll get this place customer ready in no time. "));
        
        
        NPC builder1 = new StoryCharacter(gp, 0, 0, 1);
        NPC builder2 = new StoryCharacter(gp, 0, 0, 1);
        events.add(new AddNPCEvent(gp, builder1));
        events.add(new AddNPCEvent(gp, builder2));
        
        events.add(new NPCMoveEvent(gp, builder1, 14, 9));
        events.add(new NPCMoveEvent(gp, builder2, 9, 9));
        
        events.add(new StartFadeOutEvent(gp));
        events.add(new ActionEvent(() -> gp.mapM.currentRoom.setRestored()));
        events.add(new WaitEvent(20)); 
        events.add(new RemoveNPCEvent(gp, builder1));
        events.add(new RemoveNPCEvent(gp, builder2));
        events.add(new ActionEvent(() -> gp.mapM.currentRoom.setRestored()));
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = true;
        }));
        NPC playerNPC = new StoryCharacter(gp, 0, 0, 2);
        events.add(new AddNPCEvent(gp, playerNPC));
        events.add(new WaitEvent(20)); 
        events.add(new StartFadeInEvent(gp));
        
        events.add(new DialogueEvent(gp, owner, "Good as new! Let me show you around your new restaurant."));
        events.add(new CameraFollowEvent(gp, owner, 1.2f));
        
        events.add(new ActionEvent(() -> {
        	playerNPC.setFollowNPC(owner);
        }));
        
        events.add(new NPCMoveEvent(gp, owner, 15, 8));
        events.add(new DialogueEvent(gp, owner, "Here in the dining room, customers will enter and you'll need to serve them. You also need to choose your menu for each day."));
        events.add(new WaitEvent(20)); 
        events.add(new DialogueEvent(gp, owner, "Behind this door leads to some older parts of the restaurant, its a bit creepy back there, but don't worry! We've blocked it up for you."));

        
        events.add(new NPCMoveEvent(gp, owner, 10, 6));
        events.add(new DialogueEvent(gp, owner, "This is the kitchen, where you'll be cutting, cooking and washing dishes."));

        events.add(new NPCMoveEvent(gp, owner, "Toilet 1", 4));
        events.add(new DialogueEvent(gp, owner, "If customers need the bathroom, this is where they'll go."));
        
        //events.add(new NPCMoveEvent(gp, owner, "Storage Fridge", 1));
        //events.add(new DialogueEvent(gp, owner, "This is the storage room, you can take food from here and move it into the kitchen fridge, make sure to do this before the customers arrive though!"));
        
        events.add(new NPCMoveEvent(gp, owner, "Breaker", 3));
        events.add(new DialogueEvent(gp, owner, "Here are the electrics for the restaurant, you'll only need to come here when things go wrong."));

        events.add(new NPCMoveEvent(gp, owner, "Bed", 5));
        events.add(new DialogueEvent(gp, owner, "And finally here is your room, where you can sleep through the night."));

        events.add(new ActionEvent(() -> {
        	playerNPC.stopFollowingNPC();
        }));
        
        events.add(new NPCMoveEvent(gp, owner, 10, 8));
        events.add(new DialogueEvent(gp, owner, "Right, I think I'll leave you to it."));
        
        events.add(new NPCMoveEvent(gp, owner, 10, 10));

        events.add(new RemoveNPCEvent(gp, owner));
        
        events.add(new ActionEvent(() -> {
        	gp.player.isInvisible = false;
        	gp.player.hitbox.x = playerNPC.hitbox.x;
        	gp.player.hitbox.y = playerNPC.hitbox.y;
        	gp.player.setDirection(playerNPC.getDirection());
        }));
        
        events.add(new RemoveNPCEvent(gp, playerNPC));
        
        events.add(new ResetZoomEvent(gp)); // wait 1 second
        // 6. Camera returns to player and zooms back to normal
        events.add(new EndCutscene(gp, owner));

        startCutscene(events);
    }
    public void draw(Graphics2D g2, int xDiff, int yDiff) {
    	if(drawHighlight) {
    		g2.setStroke(highlightStroke);
    		g2.setColor(Color.RED);
    		g2.drawRect((int)highlightArea.x - xDiff, (int)highlightArea.y - yDiff, (int)highlightArea.width, (int)highlightArea.height);
    	}
    }
    
}

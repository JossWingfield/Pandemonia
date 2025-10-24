package utility.cutscene;

import java.util.ArrayList;
import java.util.List;

import entity.buildings.Chair;
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

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
        events = new ArrayList<>();
    }

    public void startCutscene(List<CutsceneEvent> events) {
        this.events = events;
        this.cutsceneActive = true;
        this.currentEventIndex = 0;
        gp.player.setControlEnabled(false); // freeze player input
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
    }

    public boolean isActive() {
        return cutsceneActive;
    }
    public void enterDestroyedRestaurant() {
    	
        gp.mapM.getRoom(0).setDestroyed();

        List<CutsceneEvent> events = new ArrayList<>();
        
        // 1.
        NPC owner = new StoryCharacter(gp, 0, 0, 0);
        events.add(new AddNPCEvent(gp, owner));

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
        
        events.add(new NPCMoveEvent(gp, owner, 10, 6));
        events.add(new DialogueEvent(gp, owner, "This is the kitchen, where you'll be cutting, cooking and washing dishes."));

        events.add(new NPCMoveEvent(gp, owner, "Toilet 1", 4));
        events.add(new DialogueEvent(gp, owner, "If customers need the bathroom, this is where they'll go."));
        
        events.add(new NPCMoveEvent(gp, owner, "Storage Fridge", 1));
        events.add(new DialogueEvent(gp, owner, "This is the storage room, you can take food from here and move it into the kitchen fridge, make sure to do this before the customers arrive though!"));

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
    
}

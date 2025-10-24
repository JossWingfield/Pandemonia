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

        List<CutsceneEvent> events = new ArrayList<>();
        
        // 1.
        NPC owner = new StoryCharacter(gp, 0, 0, 0);
        events.add(new AddNPCEvent(gp, owner));

        // 2. Camera follows ghost while zooming in
        events.add(new CameraFollowEvent(gp, owner, 1.6f));
        
        // 3. Move ghost into scene
        events.add(new NPCMoveEvent(owner, 12, 9));

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
        
        events.add(new NPCMoveEvent(builder1, 14, 9));
        events.add(new NPCMoveEvent(builder2, 9, 9));
        
        events.add(new StartFadeOutEvent(gp));
        events.add(new WaitEvent(20)); 
        //events.add(new removeNPCEvent(builder1));
        //events.add(new removeNPCEvent(builder2));
        events.add(new ActionEvent(() -> gp.mapM.currentRoom.setRestored()));
        events.add(new WaitEvent(20)); 
        events.add(new StartFadeInEvent(gp));
        
        
        
 
        
        
        
        events.add(new ResetZoomEvent(gp)); // wait 1 second
        // 6. Camera returns to player and zooms back to normal
        events.add(new EndCutscene(gp, owner));

        startCutscene(events);
    }
    
}

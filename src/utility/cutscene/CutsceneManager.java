package utility.cutscene;

import java.util.ArrayList;
import java.util.List;

import entity.buildings.Chair;
import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.Pet;
import entity.npc.SpecialCustomer;
import main.GamePanel;

public class CutsceneManager {

    private GamePanel gp;
    private boolean active = false;
    private List<CutsceneEvent> events;
    private int currentEventIndex = 0;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
        events = new ArrayList<>();
    }

    public void startCutscene(List<CutsceneEvent> events) {
        this.events = events;
        this.active = true;
        this.currentEventIndex = 0;
        gp.player.setControlEnabled(false); // freeze player input
    }

    public void update() {
        if (!active) return;

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
        active = false;
        gp.player.setControlEnabled(true); // re-enable player input
    }

    public boolean isActive() {
        return active;
    }
    public void startGhostEntranceCutscene() {

        // 1. Spawn ghost offscreen
        NPC ghost = new SpecialCustomer(gp, 48*9, 48*9); 
        gp.npcM.addNPC(ghost);

        List<CutsceneEvent> events = new ArrayList<>();

        // 2. Camera follows ghost while zooming in
        events.add(new CameraFollowEvent(gp, ghost, 1.6f));
        
        // 3. Move ghost into scene
        Chair chair = gp.buildingM.findFreeChair();
        events.add(new NPCMoveEvent(ghost, chair, gp));


        // 4. Show dialogue above ghost for 3 seconds (assuming 60 FPS → 180 frames)
        events.add(new DialogueEvent(gp, "Boo! Welcome to Pandemonia..."));

        // 5. Pause for a moment (can be a WaitEvent)
        events.add(new WaitEvent(60)); // wait 1 second

        // 6. Camera returns to player and zooms back to normal
        events.add(new EndCutscene(gp));

        startCutscene(events);
    }
    
}

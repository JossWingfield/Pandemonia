package utility.cutscene;

import entity.npc.NPC;
import main.GamePanel;

public class DialogueEvent extends CutsceneEvent {

    private String message;
    private GamePanel gp;
    private NPC npc;

    public DialogueEvent(GamePanel gp, NPC npc, String message) {
        this.gp = gp;
        this.npc = npc;
        this.message = message;
    }

    
    public void update() {
    	gp.gui.setDialogue(message, npc);
        finished = true;
    }
}

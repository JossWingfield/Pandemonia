package utility.cutscene;


import entity.npc.NPC;
import main.GamePanel;

public class DialogueEvent extends CutsceneEvent {

    private String message;
    private GamePanel gp;

    public DialogueEvent(GamePanel gp, String message) {
        this.gp = gp;
        this.message = message;
    }

    @Override
    public void update() {
        // Draw dialogue above NPC
        gp.gui.addMessage(message); // assume you have a method in UI to draw dialogue
        finished = true;
    }
}

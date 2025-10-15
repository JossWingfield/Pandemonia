package utility.cutscene;


import entity.npc.NPC;
import main.GamePanel;

public class DialogueEvent extends CutsceneEvent {

    private NPC npc;
    private String message;
    private GamePanel gp;
    private int timer = 0;
    private int duration; // in frames

    public DialogueEvent(GamePanel gp, NPC npc, String message, int duration) {
        this.gp = gp;
        this.npc = npc;
        this.message = message;
        this.duration = duration;
    }

    @Override
    public void update() {
        // Draw dialogue above NPC
        gp.gui.addMessage(message); // assume you have a method in UI to draw dialogue
        timer++;
        if (timer >= duration) finished = true;
    }
}

package utility.cutscene;

public class ActionEvent extends CutsceneEvent {
    private Runnable action;

    public ActionEvent(Runnable action) {
        this.action = action;
    }

    public void update() {
        action.run();
    	finished = true;
    }

}

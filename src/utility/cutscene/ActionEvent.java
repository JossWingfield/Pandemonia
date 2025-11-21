package utility.cutscene;

public class ActionEvent extends CutsceneEvent {
    private Runnable action;

    public ActionEvent(Runnable action) {
        this.action = action;
    }

    public void update(double dt) {
        action.run();
    	finished = true;
    }

}

package utility.cutscene;

public abstract class CutsceneEvent {
    protected boolean finished = false;

    public abstract void update();  // called every frame

    public boolean isFinished() {
        return finished;
    }
}

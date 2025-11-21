package utility.cutscene;

public class WaitEvent extends CutsceneEvent {
    private double timer = 0;
    private double duration;

    public WaitEvent(double duration) {
        this.duration = duration;
    }

    @Override
    public void update(double dt) {
        timer+=dt;
        if (timer >= duration) finished = true;
    }
}

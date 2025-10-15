package utility.cutscene;

public class WaitEvent extends CutsceneEvent {
    private int timer = 0;
    private int duration;

    public WaitEvent(int duration) {
        this.duration = duration;
    }

    @Override
    public void update() {
        timer++;
        if (timer >= duration) finished = true;
    }
}

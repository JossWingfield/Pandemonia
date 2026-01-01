package utility.cutscene;

import java.util.ArrayList;
import java.util.List;

public class ParallelEvent extends CutsceneEvent {

    private final List<CutsceneEvent> events = new ArrayList<>();

    public ParallelEvent(CutsceneEvent... events) {
        for (CutsceneEvent e : events) {
            this.events.add(e);
        }
    }

    public void add(CutsceneEvent event) {
        events.add(event);
    }

    @Override
    public void update(double dt) {
        boolean allFinished = true;

        for (CutsceneEvent event : events) {
            if (!event.isFinished()) {
                event.update(dt);
            }

            if (!event.isFinished()) {
                allFinished = false;
            }
        }

        finished = allFinished;
    }
}

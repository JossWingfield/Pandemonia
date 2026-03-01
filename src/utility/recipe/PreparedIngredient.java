package utility.recipe;

import java.util.ArrayList;
import java.util.List;

public class PreparedIngredient {

    private String name;
    private List<CookStep> performedSteps;

    // Main constructor
    public PreparedIngredient(String name, List<CookStep> performedSteps) {
        this.name = name;
        this.performedSteps = new ArrayList<>(performedSteps); // defensive copy
    }

    // Convenience constructor (used by Plate)
    public PreparedIngredient(String name) {
        this.name = name;
        this.performedSteps = new ArrayList<>();
    }

    public void addStep(CookStep step) {
        performedSteps.add(step);
    }

    public String getName() { return name; }
    public List<CookStep> getPerformedSteps() { return performedSteps; }
}
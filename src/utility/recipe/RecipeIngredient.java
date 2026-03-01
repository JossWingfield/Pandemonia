package utility.recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredient {

    private String name;
    private List<CookStep> requiredSteps = new ArrayList<>();

    public RecipeIngredient(String name) {
        this.name = name;
    }

    public RecipeIngredient addStep(String station) {
        requiredSteps.add(new CookStep(station));
        return this;
    }

    public RecipeIngredient addOptionalStep(String station) {
        requiredSteps.add(new CookStep(station, true));
        return this;
    }

    public String getName() { return name; }
    public List<CookStep> getRequiredSteps() { return requiredSteps; }
}

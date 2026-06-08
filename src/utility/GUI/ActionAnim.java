package utility.GUI;

import utility.recipe.IngredientScore;

public class ActionAnim {
    IngredientScore.ActionScore action;
    double displayedQuality = 0;
    boolean finished = false;
    public double animationTime = 0;

    ActionAnim(IngredientScore.ActionScore action) {
        this.action = action;
    }
}

package utility.GUI;

import java.util.ArrayList;
import java.util.List;

import main.renderer.TextureRegion;
import utility.recipe.IngredientScore;
import utility.recipe.Recipe;

public class RecipeGrading {

	private Recipe recipe;
    private String recipeName;
    private TextureRegion recipeImage;

    private double finalScore;
    private String finalGrade;

    private List<IngredientScore> ingredientScores = new ArrayList<>();

    public float displayTimer;

    public RecipeGrading(Recipe recipe, String recipeName,
                         TextureRegion recipeImage,
                         double finalScore,
                         String finalGrade,
                         List<IngredientScore> ingredientScores) {

    	this.recipe = recipe;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.finalScore = finalScore;
        this.finalGrade = finalGrade;

        this.ingredientScores.addAll(ingredientScores);
    }
    public List<IngredientScore> getIngredientScores() {
		return ingredientScores;
	}
    public Recipe getRecipe() {
		return recipe;
	}
    public double getFinalScore() {
		return finalScore;
	}
}

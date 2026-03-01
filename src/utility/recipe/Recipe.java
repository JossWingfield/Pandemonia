package utility.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Recipe {
    private String name;
    public TextureRegion finishedPlate, dirtyPlate;
    private List<RecipeIngredient> ingredients;
    private int baseCost;
    public boolean isCursed = false;
    public boolean seasoned = false;
    public int phase;
    
    // --- STAR SYSTEM ---
    private int timesCooked = 0;   // how many times player cooked this
    private int starLevel = 1;     // 0-3
    private boolean mastered = false;

    // thresholds (can be tweaked globally or per recipe)
    private int star1Threshold = 1;
    private int star2Threshold = 50;
    private int star3Threshold = 100;
    private int masteryThreshold = 500;

    public Recipe(String name,List<RecipeIngredient> ingredients,TextureRegion finishedImage,TextureRegion dirtyImage,int cost,int phase) {
    	this.name = name;
		this.ingredients = new ArrayList<>(ingredients);
		this.finishedPlate = finishedImage;
		this.dirtyPlate = dirtyImage;
		this.baseCost = cost;
		this.phase = phase;
		  
    }
    public boolean matches(List<PreparedIngredient> plate) {

        if (plate.size() != ingredients.size())
            return false;

        // Count ingredients (order doesn't matter)
        Map<String, Integer> requiredCount = new HashMap<>();
        Map<String, Integer> plateCount = new HashMap<>();

        for (RecipeIngredient ri : ingredients) {
            requiredCount.put(ri.getName(),
                    requiredCount.getOrDefault(ri.getName(), 0) + 1);
        }

        for (PreparedIngredient pi : plate) {
            plateCount.put(pi.getName(),
                    plateCount.getOrDefault(pi.getName(), 0) + 1);
        }

        if (!requiredCount.equals(plateCount))
            return false;

        // Now validate each ingredient's steps
        for (RecipeIngredient required : ingredients) {

            boolean matched = false;

            for (PreparedIngredient actual : plate) {

                if (!required.getName().equals(actual.getName()))
                    continue;

                if (matchSteps(required, actual)) {
                    matched = true;
                    break;
                }
            }

            if (!matched)
                return false;
        }

        return true;
    }
    private boolean matchSteps(RecipeIngredient expected,PreparedIngredient actual) {
		List<CookStep> requiredSteps = expected.getRequiredSteps();
		List<CookStep> performedSteps = actual.getPerformedSteps();
		
		if (performedSteps.size() < requiredSteps.size())
		return false;
		
		for (int i = 0; i < requiredSteps.size(); i++) {
		
		CookStep expectedStep = requiredSteps.get(i);
		CookStep actualStep = performedSteps.get(i);
		
		if (!expectedStep.matches(actualStep))
		return false;
		}
		
		return true;
	}
    public void setCursed() {
    	isCursed = true;
    }
    public String getName() {
        return name;
    }
    public int getCost(boolean isSpecial) {
        float value = baseCost;

        // apply player star bonus
        value *= getMoneyMultiplier();

        // apply dish special flag
        if (isSpecial) value *= 1.2f;

        return (int)Math.ceil(value);
    }
    public int getCost(boolean isSpecial, float multiplier) {
        return (int)(getCost(isSpecial) * multiplier);
    }
    public void setSeasoned(String seasoningName) {
        if (seasoningName == null || seasoningName.isEmpty()) return;
        this.seasoned = true;
    }
    public List<RecipeIngredient> getRequiredIngredients() {
		return ingredients;
	}
    public int getTimesCooked() { return timesCooked; }
    public int getStarLevel() { return starLevel; }
    public boolean isMastered() { return mastered; }

    private void updateStarLevel() {
        if (starLevel == 0 && timesCooked >= star1Threshold) {
            starLevel = 1;
        }
        if (starLevel == 1 && timesCooked >= star2Threshold) {
            starLevel = 2;
        }
        if (starLevel == 2 && timesCooked >= star3Threshold) {
            starLevel = 3;
        }
        if (!mastered && starLevel == 3 && timesCooked >= masteryThreshold) {
            mastered = true;
            // unlock statue/decor reward here if needed
        }
    }
    public float getMoneyMultiplier() {
        switch (starLevel) {
            case 1: return 1.0f;
            case 2: return 1.5f;
            case 3: return 2.5f;
            default: return 1.0f;
        }
    }
    public void incrementCookCount() {
        timesCooked++;
        updateStarLevel();
    }
    public void setCookCount(int count) { this.timesCooked = count; }
    public void setStarLevel(int level) { this.starLevel = level; }
    public void setMastered(boolean value) { this.mastered = value; }
    //@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe r = (Recipe) o;
        return this.name.equals(r.name);
    }

    //@Override
    public int hashCode() {
        return name.hashCode();
    }
}
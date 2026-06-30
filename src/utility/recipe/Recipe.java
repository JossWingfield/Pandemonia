package utility.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public boolean seasoned = false;
    public int phase;

    // --- TAG SYSTEM ---
    private final List<RecipeTag> tags;   // max 3; enforced in constructor

    // --- STAR SYSTEM ---
    private int timesCooked = 0;
    private int starLevel = 1;
    private boolean mastered = false;
    
    private int star1Threshold  = 1;
    private int star2Threshold  = 50;
    private int star3Threshold  = 100;
    private int masteryThreshold = 500;

    // ── Constructors ─────────────────────────────────────────────────────────

    /** No-tag constructor (common recipes). */
    public Recipe(String name, List<RecipeIngredient> ingredients,
                  TextureRegion finishedImage, TextureRegion dirtyImage,
                  int cost, int phase) {
        this(name, ingredients, finishedImage, dirtyImage, cost, phase,
                new RecipeTag[0]);
    }

    /** Full constructor with tags (1–3 allowed). */
    public Recipe(String name, List<RecipeIngredient> ingredients,
                  TextureRegion finishedImage, TextureRegion dirtyImage,
                  int cost, int phase, RecipeTag... tags) {
        if (tags.length > 3)
            throw new IllegalArgumentException(
                    "A recipe may have at most 3 tags (got " + tags.length + ")");

        this.name         = name;
        this.ingredients  = new ArrayList<>(ingredients);
        this.finishedPlate = finishedImage;
        this.dirtyPlate   = dirtyImage;
        this.baseCost     = cost;
        this.phase        = phase;
        this.tags         = new ArrayList<>(Arrays.asList(tags));
    }

    // ── Tag helpers ──────────────────────────────────────────────────────────

    /** Returns an unmodifiable view of this recipe's tags. */
    public List<RecipeTag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public boolean hasTag(RecipeTag tag) {
        return tags.contains(tag);
    }

    /** True if the recipe shares at least one tag with the given list. */
    public boolean hasAnyTag(List<RecipeTag> query) {
        for (RecipeTag t : query) {
            if (tags.contains(t)) return true;
        }
        return false;
    }

    // ── Matching ─────────────────────────────────────────────────────────────

    public boolean matches(List<PreparedIngredient> plate) {
        if (plate.size() != ingredients.size()) return false;

        Map<String, Integer> requiredCount = new HashMap<>();
        Map<String, Integer> plateCount    = new HashMap<>();

        for (RecipeIngredient ri : ingredients)
            requiredCount.put(ri.getName(),
                    requiredCount.getOrDefault(ri.getName(), 0) + 1);

        for (PreparedIngredient pi : plate)
            plateCount.put(pi.getName(),
                    plateCount.getOrDefault(pi.getName(), 0) + 1);

        if (!requiredCount.equals(plateCount)) return false;

        for (RecipeIngredient required : ingredients) {
            boolean matched = false;
            for (PreparedIngredient actual : plate) {
                if (!required.getName().equals(actual.getName())) continue;
                if (matchSteps(required, actual)) { matched = true; break; }
            }
            if (!matched) return false;
        }
        return true;
    }

    private boolean matchSteps(RecipeIngredient expected, PreparedIngredient actual) {
        List<CookStep> requiredSteps  = expected.getRequiredSteps();
        List<CookStep> performedSteps = actual.getPerformedSteps();

        if (performedSteps.size() < requiredSteps.size()) return false;

        for (int i = 0; i < requiredSteps.size(); i++) {
            if (!requiredSteps.get(i).matches(performedSteps.get(i))) return false;
        }
        return true;
    }

    // ── Cost / money ─────────────────────────────────────────────────────────

    public int getCost(boolean isSpecial) {
        float value = baseCost * getMoneyMultiplier();
        if (isSpecial) value *= 1.2f;
        return (int) Math.ceil(value);
    }

    public int getCost(boolean isSpecial, float multiplier) {
        return (int) (getCost(isSpecial) * multiplier);
    }

    // ── Seasoning ────────────────────────────────────────────────────────────

    public void setSeasoned(String seasoningName) {
        if (seasoningName == null || seasoningName.isEmpty()) return;
        this.seasoned = true;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName()                        { return name; }
    public List<RecipeIngredient> getRequiredIngredients() { return ingredients; }
    public int  getTimesCooked()                   { return timesCooked; }
    public int  getStarLevel()                     { return starLevel; }
    public boolean isMastered()                    { return mastered; }

    // ── Star system ──────────────────────────────────────────────────────────

    private void updateStarLevel() {
        if (starLevel == 0 && timesCooked >= star1Threshold)  starLevel = 1;
        if (starLevel == 1 && timesCooked >= star2Threshold)  starLevel = 2;
        if (starLevel == 2 && timesCooked >= star3Threshold)  starLevel = 3;
        if (!mastered && starLevel == 3 && timesCooked >= masteryThreshold) {
            mastered = true;
        }
    }

    public float getMoneyMultiplier() {
        switch (starLevel) {
            case 2:  return 1.5f;
            case 3:  return 2.5f;
            default: return 1.0f;
        }
    }

    public void incrementCookCount() { timesCooked++; updateStarLevel(); }
    public void setCookCount(int count) { this.timesCooked = count; }
    public void setStarLevel(int level) { this.starLevel  = level; }
    public void setMastered(boolean value) { this.mastered = value; }

    // ── Equality (by name) ───────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        return this.name.equals(((Recipe) o).name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }
}
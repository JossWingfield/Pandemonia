package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Recipe {
    private String name;
    private List<String> requiredIngredients;
    private boolean orderMatters;
    public TextureRegion finishedPlate, dirtyPlate;
    private List<String> cookingStates, secondaryCookingStates;
    private int baseCost;
    public boolean isCursed = false;
    public boolean seasoned = false;
    public int phase;
    private String seasoningName = null;
    
    // --- STAR SYSTEM ---
    private int timesCooked = 0;   // how many times player cooked this
    private int starLevel = 1;     // 0-3
    private boolean mastered = false;

    // thresholds (can be tweaked globally or per recipe)
    private int star1Threshold = 1;
    private int star2Threshold = 50;
    private int star3Threshold = 100;
    private int masteryThreshold = 500;

    public Recipe(String name, List<String> requiredIngredients, List<String> cookingStates, List<String> secondaryCookingStates, boolean orderMatters, TextureRegion finishedImage, TextureRegion dirtyImage, int cost, int phase) {
        this.name = name;
        this.phase = phase;
        
        this.requiredIngredients = new ArrayList<>(requiredIngredients);
        this.cookingStates = new ArrayList<>(cookingStates);
        this.secondaryCookingStates = new ArrayList<>(secondaryCookingStates);
        
        
        this.orderMatters = orderMatters;
        this.finishedPlate = finishedImage;
        this.dirtyPlate = dirtyImage;
        this.baseCost = cost;
    }
    /*
    public boolean matches(List<String> plateIngredients, List<String> cookMethod) {
        if (plateIngredients.size() != requiredIngredients.size()) return false;

        if (orderMatters) {
            for (int i = 0; i < requiredIngredients.size(); i++) {
                if (!plateIngredients.get(i).equals(requiredIngredients.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            // Use frequency count to check unordered match
            Map<String, Integer> requiredCount = new HashMap<>();
            Map<String, Integer> plateCount = new HashMap<>();

            for (String ing : requiredIngredients) {
                requiredCount.put(ing, requiredCount.getOrDefault(ing, 0) + 1);
            }
            for (String ing : plateIngredients) {
                plateCount.put(ing, plateCount.getOrDefault(ing, 0) + 1);
            }

            return requiredCount.equals(plateCount);
        }
    }
    */
    public boolean matches(List<String> plateIngredients, List<String> cookMethod) {

        // First check ingredient count
        if (plateIngredients.size() != requiredIngredients.size()) {
            return false;
        }

        // ----- INGREDIENT MATCHING -----

        if (orderMatters) {
            for (int i = 0; i < requiredIngredients.size(); i++) {
                if (!plateIngredients.get(i).equals(requiredIngredients.get(i))) {
                    return false;
                }
            }
        } else {
            // Unordered ingredient match using frequency count
            Map<String, Integer> requiredCount = new HashMap<>();
            Map<String, Integer> plateCount = new HashMap<>();

            for (String ing : requiredIngredients) {
                requiredCount.put(ing, requiredCount.getOrDefault(ing, 0) + 1);
            }
            for (String ing : plateIngredients) {
                plateCount.put(ing, plateCount.getOrDefault(ing, 0) + 1);
            }

            if (!requiredCount.equals(plateCount)) {
                return false;
            }
        }

        // ----- COOK METHOD MATCHING -----

        if (cookMethod.size() != cookingStates.size()) {
            return false;
        }

        for (int i = 0; i < cookingStates.size(); i++) {
            String expected = cookingStates.get(i);
            String actual = cookMethod.get(i);

            // If either side is chopping board, ignore this slot completely
            if ("Chopping Board".equals(expected) || "Chopping Board".equals(actual)) {
                continue;
            }

            // Empty expected = don't care
            if (expected == null || expected.isEmpty()) {
                continue;
            }

            if (!expected.equals(actual)) {
                return false;
            }
        }

        return true;
    }
    public void setCursed() {
    	isCursed = true;
    }
    public List<String> getIngredients() {
    	return requiredIngredients;
    }
    public List<String> getCookingStates() {
        return cookingStates;
    }
    public List<String> getSecondaryCookingStates() {
        return secondaryCookingStates;
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
        this.seasoningName = seasoningName;

        // Optionally add seasoning ingredient so it visually matches
        if (!requiredIngredients.contains(seasoningName)) {
            requiredIngredients.add(seasoningName);
            cookingStates.add(""); // no specific cooking method for seasoning
            secondaryCookingStates.add(""); // same here
        }
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
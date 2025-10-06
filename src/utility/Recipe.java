package utility;

import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Recipe {
    private String name;
    private List<String> requiredIngredients;
    private boolean orderMatters;
    public BufferedImage finishedPlate, dirtyPlate;
    private List<String> cookingStates, secondaryCookingStates;
    private int baseCost;
    public boolean isCursed = false;
    public boolean seasoned = false;
    public int phase;

    public Recipe(String name, List<String> requiredIngredients, List<String> cookingStates, List<String> secondaryCookingStates, boolean orderMatters, BufferedImage finishedImage, BufferedImage dirtyImage, int cost, int phase) {
        this.name = name;
        this.phase = phase;
        this.requiredIngredients = requiredIngredients;
        this.cookingStates = cookingStates;
        this.secondaryCookingStates = secondaryCookingStates;
        this.orderMatters = orderMatters;
        this.finishedPlate = finishedImage;
        this.dirtyPlate = dirtyImage;
        this.baseCost = cost;
    }
    public boolean matches(List<String> plateIngredients) {
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
        if (isSpecial) {
            return (int)Math.ceil(baseCost * 1.2); 
        }
        return baseCost;
    }
    public int getCost(boolean isSpecial, float multiplier) {
        if (isSpecial) {
            return (int)Math.ceil(baseCost * 1.2 * multiplier); 
        }
        return (int)(baseCost * multiplier);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe r = (Recipe) o;
        return this.name.equals(r.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
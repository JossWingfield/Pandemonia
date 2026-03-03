package entity.items;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.recipe.CookStep;

public class Food extends Item {

    public FoodState foodState;
    protected int foodLayer = 0;

    // Images for different states
    public TextureRegion rawImage, choppedImage, frozenImage, burntImage;
    public TextureRegion panPlated, potPlated, ovenPlated, friedPlated, generalPlated;

    protected List<CookStep> performedSteps = new ArrayList<>();

    public boolean cutIntoNewItem = false;
    public boolean notRawItem = false;

    protected int chopCount = 12;
    protected int cookTime = 24;

    // Constructor
    public Food(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos);
        foodState = FoodState.RAW;
        burntImage = importImage("/food/BurntFood.png").toTextureRegion();
    }

    // --- Step Handling ---
    public void addStep(String station) {
        performedSteps.add(new CookStep(station));
    }

    public List<CookStep> getPerformedSteps() {
        return performedSteps;
    }

    // --- State Handling ---
    public void setState(FoodState state) {
        this.foodState = state;
    }
    public void setState(int state) {
        FoodState[] values = FoodState.values();

        if (state < 0 || state >= values.length) {
            this.foodState = FoodState.RAW; // safe fallback
        } else {
            this.foodState = values[state];
        }
    }
    public FoodState getStateEnum() {
        return foodState;
    }

    // --- Cook Method Accessors (linked to last performed step) ---
    public String getCookMethod() {
        if (performedSteps.isEmpty()) return "";
        return performedSteps.get(performedSteps.size() - 1).getStation(); 
    }

    public String getSecondaryCookMethod() {
        if (performedSteps.size() < 2) return "";
        return performedSteps.get(performedSteps.size() - 2).getStation(); 
    }

    // --- Draw / Image ---
    public TextureRegion getImage() {
        switch (foodState) {
            case RAW: return rawImage;
            case CHOPPED: return choppedImage;
            case FROZEN: return frozenImage;
            case BURNT: return burntImage;
            case COOKED:
            case PLATED: return getPlatedImage();
            default: return rawImage;
        }
    }

    private TextureRegion getPlatedImage() {
        if (performedSteps.isEmpty()) return generalPlated;

        String primary = getCookMethod();
        String secondary = getSecondaryCookMethod();
        
        // Prioritize primary station images
        switch (primary) {
            case "Frying Pan": return panPlated;
            case "Small Pot": return potPlated;
            case "Oven": case "Oven Tray": return ovenPlated;
            case "Fryer": return friedPlated;
        }

        // Fallback to secondary
        switch (secondary) {
            case "Fryer": return friedPlated;
            case "Oven": case "Oven Tray": return ovenPlated;
        }

        return generalPlated;
    }

    public void draw(Renderer renderer) {
        TextureRegion img = getImage();
        renderer.draw(img, (int) hitbox.x - xDrawOffset, (int) hitbox.y - yDrawOffset, drawWidth, drawHeight);
    }

    // --- Upgrade Logic ---
    public int getChopCount() {
        if (gp.world.progressM.choppingBoardUpgradeI) return (int) (chopCount * 0.75);
        return chopCount;
    }

    public int getMaxCookTime() {
        if (gp.world.progressM.stoveUpgradeI) return (int)(cookTime*0.75);
        if (gp.world.progressM.stoveUpgradeII) return (int)(cookTime*0.5);
        if (gp.world.progressM.stoveUpgradeIII) return (int)(cookTime*0.25);
        return cookTime;
    }

    public int getFoodLayer() { return foodLayer; }

}

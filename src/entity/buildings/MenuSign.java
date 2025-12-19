package entity.buildings;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.DayPhase;
import utility.Recipe;
import utility.RecipeManager;
import utility.RecipeRenderData;

public class MenuSign extends Building {

    private Rectangle2D.Float interactHitbox;
    private boolean firstUpdate = true;
    private double clickCooldown = 0;
    private boolean uiOpen = false;
    private TextureRegion ui1;
    private TextureRegion recipeBorder, coinImage, starLevel;

    private Map<Recipe, RecipeRenderData> renderCache = new HashMap<>();
    private List<Recipe> selectedRecipes = new ArrayList<>();
    
    private Colour hoverColor = new Colour(172, 172, 170);
    private Colour orderTextColour = new Colour(145, 102, 91);
    private Colour specialColour = new Colour(137, 163, 80);
    private Colour specialHover = new Colour(169, 187, 102);
    
    BitmapFont font;

    public MenuSign(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos, 48, 48);

        font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
        
        isSolid = false;
        
        drawWidth = 16*3;
        drawHeight = 32*3;
        yDrawOffset = 24;
        importImages();
    	isKitchenBuilding = true;
    	mustBePlacedOnTable = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*4);
    }
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*4);
	}
    public Building clone() {
		MenuSign building = new MenuSign(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new MenuSign(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
    private void importImages() {
        animations = new TextureRegion[1][1][2];

        name = "Menu Sign";
        animations[0][0][0] = importImage("/decor/chalkboard.png").getSubimage(0, 32, 16, 32);
        animations[0][0][1] = importImage("/decor/HighlightedMenu.png").toTextureRegion();
        ui1 = importImage("/UI/menu/MenuFrame.png").toTextureRegion();
		recipeBorder = importImage("/UI/recipe/orderBorder.png").toTextureRegion();
		coinImage = importImage("/UI/Coin.png").toTextureRegion();
		starLevel = importImage("/UI/recipe/Star.png").toTextureRegion();
    }
	public void update(double dt) {
		super.update(dt);
		if (clickCooldown > 0) {
	    	clickCooldown -= dt;        // subtract elapsed time in seconds
			if (clickCooldown < 0) {
				clickCooldown = 0;      // clamp to zero
			}
		}
	}
    //@Override
    public void draw(Renderer renderer) {

        if(firstUpdate) {
            firstUpdate = false;
            interactHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y+hitbox.height, 14, 16);
        }

        // Interact highlight
        boolean canInteract = gp.world.getCurrentPhase() == DayPhase.PREPARATION
                || (gp.world.getCurrentPhase() == DayPhase.SERVICE && !gp.world.isMenuChosen());
        if(interactHitbox.intersects(gp.player.interactHitbox) && canInteract) {

            renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , 
                    (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

            // Open UI on key press
            if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && clickCooldown == 0 && !gp.cutsceneM.cutsceneActive) {
                uiOpen = !uiOpen;
                clickCooldown = 0.1;
                if(uiOpen) {
                    //gp.pauseTime(); // freeze world time while selecting menu
                } else {
                    //gp.resumeTime();
                }
            }
        } else {
  		    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
         
        	uiOpen = false;
        }

    }
	public void addOrder(Recipe recipe, Renderer renderer) {
	    RecipeRenderData data = buildRenderData(recipe, font, renderer);
	    renderCache.put(recipe, data);
	}
	public RecipeRenderData buildRenderData(Recipe recipe, BitmapFont nameFont, Renderer renderer) {
	    RecipeRenderData data = new RecipeRenderData();
	    data.recipe = recipe;

	    // Cache base images
	    data.borderImage = recipeBorder;
	    data.starLevel = recipe.getStarLevel();

	    data.coinImage = coinImage;
	    data.plateImage = recipe.finishedPlate;

	    // Cache ingredient + states
	    List<String> ingredients = recipe.getIngredients();
	    List<String> cookingState = recipe.getCookingStates();
	    List<String> secondaryCookingState = recipe.getSecondaryCookingStates();

	    for (int j = 0; j < ingredients.size(); j++) {
	        String ingredientName = ingredients.get(j);
	        Food ingredient = (Food) gp.itemRegistry.getItemFromName(ingredientName, 0);

	        TextureRegion ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
	        if (ingredient.notRawItem) {
	            ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
	        }

	        data.ingredientImages.add(ingredientImage);
	        data.cookingStateIcons.add(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed));
	        data.secondaryCookingStateIcons.add(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed));
	    }
	    
	    for (String line : recipe.getName().split(" ")) {
	        data.nameLines.add(line);
	        int offset = (int)((48 - gp.renderer.measureStringWidth(font, line, 1.0f) / 2));
	        data.nameLineOffsets.add(offset);
	    }

	    // Cache cost
        data.cost = Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe)));

	    return data;
	}
    // Draw the menu UI
	public void drawOverlayUI(Renderer renderer) {
	    if (!uiOpen) return;

	    // Background
	    renderer.draw(ui1, 50, 50, 275*3, 187*3);

	    // Ensure unlocked recipes are cached
	    List<Recipe> unlocked = RecipeManager.getUnlockedRecipes();
	    for (Recipe r : unlocked) {
	        if (!renderCache.containsKey(r)) {
	            addOrder(r, renderer);
	        }
	    }

	    // Remove today’s specials from unlocked list
	    List<Recipe> specials = gp.world.getTodaysSpecials();
	    unlocked.removeAll(specials);
	    
	    float fontScale = 1.0f;

	    int startX = 120;
	    int startY = 256;
	    int lineHeight = (int)((fontScale*32) + 4);

	    for (int i = 0; i < unlocked.size(); i++) {
	        Recipe recipe = unlocked.get(i);
	        RecipeRenderData data = renderCache.get(recipe); // cached render data

	        int textX = startX;
	        int textY = startY + i * lineHeight;
	        int maxNumInColumn = 8;
	        if (i > maxNumInColumn) {
	            textX = startX + 160;
	            textY = startY + (i - maxNumInColumn - 1) * lineHeight;
	        }

	        int textWidth = (int)gp.renderer.measureStringWidth(font, recipe.getName(), fontScale);
	        int textHeight = (int)(fontScale*32);
	        Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

	        boolean hovering = nameHitbox.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY());
	        Colour c = Colour.BLACK;
	        if (hovering) {
	            drawRecipe(renderer, data, 8 + 2 * (36*3), 581);
	            c = Colour.WHITE;
	            if (gp.mouseL.mouseButtonDown(0)) {
	                gp.world.addRecipeToMenu(recipe);
	            }
	        } else {
	            c = hoverColor;
	        }
	        String text = recipe.getName();
	        for (int a = 0; a < recipe.getStarLevel(); a++) {
	            text += " *";
	        }
	        renderer.drawString(font, text, textX, textY, 1.0f, c);
	    }

	    // Specials list – use cached data too
	    int specialsStartY = 486;
	    for (int i = 0; i < specials.size(); i++) {
	        Recipe recipe = specials.get(i);
	        RecipeRenderData data = renderCache.get(recipe);

	        int textX = startX;
	        int textY = specialsStartY + (i + 1) * lineHeight;

	        int textWidth = (int)gp.renderer.measureStringWidth(font, recipe.getName(), fontScale);
	        int textHeight = (int)(fontScale*32);
	        Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

	        boolean hovering = nameHitbox.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY());
	        
	        Colour c2 = Colour.BLACK;
	        if (hovering) {
	            drawRecipe(renderer, data, 8 + 2 * (36*3), 581); // preview
	            c2 = specialHover;
	            if (gp.mouseL.mouseButtonDown(0)) {
	                gp.world.addRecipeToMenu(recipe);
	            }
	        } else {
	            c2 = specialColour;
	        }

	        String text = recipe.getName();
	        for (int a = 0; a < recipe.getStarLevel(); a++) {
	            text += " *";
	        }
	        renderer.drawString(font, text, textX, textY, 1.0f, c2);
	    }

	    // Draw chosen menu slots with cached data
	    List<Recipe> chosen = gp.world.getTodaysMenu();
	    for (int i = 0; i < chosen.size(); i++) {
	        Recipe recipe = chosen.get(i);
	        RecipeRenderData data = renderCache.get(recipe);

	        switch (i) {
	            case 0, 1, 2 -> {
	                int x = 426 + i*(36*3) + i*5 + 11;
	                int y = 209;
	                drawRecipe(renderer, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY()) &&
	                    gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	            case 3 -> {
	                int x = 491, y = 398;
	                drawRecipe(renderer, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY()) &&
	                    gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	            case 4 -> {
	                int x = 605, y = 398;
	                drawRecipe(renderer, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY()) &&
	                    gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	        }
	    }

	    // Done button
	    Colour newCol = Colour.WHITE;
	    String text = "Done";
	    int textX = 730, textY = 544;
	    int textWidth = (int)gp.renderer.measureStringWidth(font, text, fontScale);
        int textHeight = (int)(fontScale*32);
	    Rectangle2D.Float doneHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);
	    if (doneHitbox.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY())) {
	        newCol = specialColour;
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            uiOpen = false;
	            clickCooldown = 0.1;
	        }
	    }
	    renderer.drawString(font, text, textX, textY, 1.0f, newCol);
	}
	private void drawRecipe(Renderer renderer, RecipeRenderData data, int x, int y) {
	    // Base
	    renderer.draw(data.borderImage, x, y, 32*3, 48*3);

	    // Ingredients
	    for (int j = 0; j < data.ingredientImages.size(); j++) {
	        renderer.draw(data.ingredientImages.get(j), x + j*(10*3) + 4, y + 4, 10*3, 10*3);
	        renderer.draw(data.cookingStateIcons.get(j), x + j*(10*3) + 4, y + 20, 10*3, 10*3);
	        renderer.draw(data.secondaryCookingStateIcons.get(j), x + j*(10*3) + 4, y + 44, 10*3, 10*3);
	    }
	    
	    // Name
	    Colour c = orderTextColour;
	    for (int i = 0; i < data.nameLines.size(); i++) {
	        renderer.drawString(font, data.nameLines.get(i), x + data.nameLineOffsets.get(i), y + 84 + i*15, 1.0f, c);
	    }
	    
	    for (int i = 0; i < data.starLevel; i++) {
	        renderer.draw(starLevel, x +10 + i * 36, y + 50, 8*3, 8*3);
	    }

	    // Plate
	    renderer.draw(data.plateImage, x + 24, y + 94, 48, 48);

	    // Coin + cost
	    renderer.draw(data.coinImage, x, y + 142, 48, 48);
	    renderer.drawString(font, data.cost, x + 56, y + 174, 1.0f, Colour.WHITE);
	}
    // Called from input handling to select/deselect recipes or confirm
    public void toggleRecipeSelection(Recipe r) {
        if(selectedRecipes.contains(r)) {
            selectedRecipes.remove(r);
        } else {
            selectedRecipes.add(r);
        }
    }
    
    public void confirmMenuSelection() {
        gp.world.setTodaysMenu(selectedRecipes);
        selectedRecipes.clear();
        uiOpen = false;
        //gp.resumeTime();
    }
    
}
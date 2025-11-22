package entity.buildings;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.items.Bread;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import entity.npc.Customer;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import utility.DayPhase;
import utility.Recipe;
import utility.RecipeManager;
import utility.RecipeRenderData;

public class MenuSign extends Building {

    private Rectangle2D.Float interactHitbox;
    private boolean firstUpdate = true;
    private double clickCooldown = 0;
    private boolean uiOpen = false;
    private BufferedImage ui1, ui2, ui3;
    private BufferedImage recipeBorder, coinImage, starLevel;

    private Map<Recipe, RecipeRenderData> renderCache = new HashMap<>();
    private List<Recipe> selectedRecipes = new ArrayList<>();
    
    private Font nameFont = new Font("monogram", Font.ITALIC, 20);
    private Font menuFont = new Font("monogram", Font.ITALIC, 24);
    private Color hoverColor = new Color(172, 172, 170);
    private Color orderTextColour = new Color(145, 102, 91);
    private Color specialColour = new Color(137, 163, 80);
    private Color specialHover = new Color(169, 187, 102);
    
    private Font timeFont = new Font("monogram", Font.PLAIN, 32);

    public MenuSign(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos, 48, 48);

        isSolid = false;
        blueprint = false;
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
        animations = new BufferedImage[1][1][2];

        name = "Menu Sign";
        animations[0][0][0] = importImage("/decor/chalkboard.png").getSubimage(0, 32, 16, 32);
        animations[0][0][1] = importImage("/decor/HighlightedMenu.png");
        ui1 = importImage("/UI/menu/MenuFrame.png");
        ui2 = importImage("/UI/fridge/5.png");
        ui3 = importImage("/UI/fridge/Hover.png");
		recipeBorder = importImage("/UI/recipe/orderBorder.png");
		coinImage = importImage("/UI/Coin.png");
		starLevel = importImage("/UI/recipe/Star.png");
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
    public void draw(Graphics2D g2, int xDiff, int yDiff) {

        if(firstUpdate) {
            firstUpdate = false;
            interactHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y+hitbox.height, 14, 16);
        }

        // Interact highlight
        boolean canInteract = gp.world.getCurrentPhase() == DayPhase.PREPARATION
                || (gp.world.getCurrentPhase() == DayPhase.SERVICE && !gp.world.isMenuChosen());
        if(interactHitbox.intersects(gp.player.interactHitbox) && canInteract) {

            g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, 
                    (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);

            // Open UI on key press
            if(gp.keyI.ePressed && clickCooldown == 0 && !gp.cutsceneM.cutsceneActive) {
                uiOpen = !uiOpen;
                clickCooldown = 0.1;
                if(uiOpen) {
                    //gp.pauseTime(); // freeze world time while selecting menu
                } else {
                    //gp.resumeTime();
                }
            }
        } else {
  		    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
         
        	uiOpen = false;
        }

    }
	public void addOrder(Recipe recipe, Graphics2D g2) {
	    RecipeRenderData data = buildRenderData(recipe, nameFont, g2);
	    renderCache.put(recipe, data);
	}
	public RecipeRenderData buildRenderData(Recipe recipe, Font nameFont, Graphics2D g2) {
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

	        BufferedImage ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
	        if (ingredient.notRawItem) {
	            ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
	        }

	        data.ingredientImages.add(ingredientImage);
	        data.cookingStateIcons.add(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed));
	        data.secondaryCookingStateIcons.add(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed));
	    }
	    
	    // Cache text layout
	    g2.setFont(nameFont);
	    for (String line : recipe.getName().split(" ")) {
	        data.nameLines.add(line);
	        int offset = (48 - getTextWidth(line, g2) / 2);
	        data.nameLineOffsets.add(offset);
	    }

	    // Cache cost
        data.cost = Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe)));

	    return data;
	}
    // Draw the menu UI
	public void drawOverlayUI(Graphics2D g2, int xDiff, int yDiff) {
	    if (!uiOpen) return;

	    // Background
	    g2.drawImage(ui1, 50, 50, 275*3, 187*3, null);

	    // Ensure unlocked recipes are cached
	    List<Recipe> unlocked = RecipeManager.getUnlockedRecipes();
	    for (Recipe r : unlocked) {
	        if (!renderCache.containsKey(r)) {
	            addOrder(r, g2);
	        }
	    }

	    // Remove today’s specials from unlocked list
	    List<Recipe> specials = gp.world.getTodaysSpecials();
	    unlocked.removeAll(specials);

	    g2.setFont(menuFont);
	    FontMetrics fm = g2.getFontMetrics();

	    int startX = 120;
	    int startY = 256;
	    int lineHeight = fm.getHeight() + 4;

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

	        int textWidth = fm.stringWidth(recipe.getName());
	        int textHeight = fm.getAscent();
	        Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

	        boolean hovering = nameHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY);
	        g2.setFont(menuFont);  
	        
	        if (hovering) {
	            g2.setFont(timeFont);
	            drawRecipe(g2, data, 8 + 2 * (36*3), 581);
	            g2.setColor(Color.WHITE);
	            if (gp.mouseI.leftClickPressed) {
	                gp.world.addRecipeToMenu(recipe);
	            }
	        } else {
	            g2.setColor(hoverColor);
	        }
	        String text = recipe.getName();
	        for (int a = 0; a < recipe.getStarLevel(); a++) {
	            text += " *";
	        }
	        g2.drawString(text, textX, textY);
	    }

	    // Specials list – use cached data too
	    int specialsStartY = 486;
	    for (int i = 0; i < specials.size(); i++) {
	        Recipe recipe = specials.get(i);
	        RecipeRenderData data = renderCache.get(recipe);

	        int textX = startX;
	        int textY = specialsStartY + (i + 1) * lineHeight;

	        int textWidth = fm.stringWidth(recipe.getName());
	        int textHeight = fm.getAscent();
	        Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

	        boolean hovering = nameHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY);
	        g2.setFont(menuFont);  
	        
	        if (hovering) {
	            g2.setFont(timeFont);
	            drawRecipe(g2, data, 8 + 2 * (36*3), 581); // preview
	            g2.setColor(specialHover);

	            if (gp.mouseI.leftClickPressed) {
	                gp.world.addRecipeToMenu(recipe);
	            }
	        } else {
	            g2.setColor(specialColour);
	        }

	        String text = recipe.getName();
	        for (int a = 0; a < recipe.getStarLevel(); a++) {
	            text += " *";
	        }
	        g2.drawString(text, textX, textY);
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
	                drawRecipe(g2, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY) &&
	                    gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	            case 3 -> {
	                int x = 491, y = 398;
	                drawRecipe(g2, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY) &&
	                    gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	            case 4 -> {
	                int x = 605, y = 398;
	                drawRecipe(g2, data, x, y);
	                Rectangle2D.Float border = new Rectangle2D.Float(x, y, 32*3, 48*3);
	                if (border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY) &&
	                    gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                    gp.world.removeRecipeFromMenu(recipe);
	                    clickCooldown = 0.1;
	                }
	            }
	        }
	    }

	    // Done button
	    g2.setColor(Color.WHITE);
	    g2.setFont(timeFont);
	    String text = "Done";
	    int textX = 730, textY = 544;
	    int textWidth = fm.stringWidth(text);
	    int textHeight = fm.getAscent();
	    Rectangle2D.Float doneHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);
	    if (doneHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY)) {
	        g2.setColor(specialColour);
	        if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
	            uiOpen = false;
	            clickCooldown = 0.1;
	        }
	    }
	    g2.drawString(text, textX, textY);
	}
	private void drawRecipe(Graphics2D g2, RecipeRenderData data, int x, int y) {
	    // Base
	    g2.drawImage(data.borderImage, x, y, 32*3, 48*3, null);

	    // Ingredients
	    for (int j = 0; j < data.ingredientImages.size(); j++) {
	        g2.drawImage(data.ingredientImages.get(j), x + j*(10*3) + 4, y + 4, 10*3, 10*3, null);
	        g2.drawImage(data.cookingStateIcons.get(j), x + j*(10*3) + 4, y + 20, 10*3, 10*3, null);
	        g2.drawImage(data.secondaryCookingStateIcons.get(j), x + j*(10*3) + 4, y + 44, 10*3, 10*3, null);
	    }
	    
	    // Name
	    g2.setColor(orderTextColour);
	    g2.setFont(nameFont);
	    for (int i = 0; i < data.nameLines.size(); i++) {
	        g2.drawString(data.nameLines.get(i), x + data.nameLineOffsets.get(i), y + 84 + i*15);
	    }
	    
	    for (int i = 0; i < data.starLevel; i++) {
	        g2.drawImage(starLevel, x +10 + i * 36, y + 50, 8*3, 8*3, null);
	    }

	    // Plate
	    g2.drawImage(data.plateImage, x + 24, y + 94, 48, 48, null);

	    // Coin + cost
	    g2.drawImage(data.coinImage, x, y + 142, 48, 48, null);
	    g2.setColor(Color.WHITE);
	    g2.setFont(timeFont);
	    g2.drawString(data.cost, x + 56, y + 174);
	}
    private int getTextWidth(String text, Graphics2D g2) {
        return (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
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
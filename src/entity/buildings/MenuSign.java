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
import java.util.List;

import entity.items.Bread;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import utility.DayPhase;
import utility.Recipe;
import utility.RecipeManager;

public class MenuSign extends Building {

    private Rectangle2D.Float interactHitbox;
    private boolean firstUpdate = true;
    private int clickCooldown = 0;
    private boolean uiOpen = false;
    private BufferedImage ui1, ui2, ui3;
    private BufferedImage recipeBorder, coinImage;

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
    }

    @Override
    public void draw(Graphics2D g2) {

        if(firstUpdate) {
            firstUpdate = false;
            interactHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y+hitbox.height, 14, 16);
        }

        // Interact highlight
        boolean canInteract = gp.world.getCurrentPhase() == DayPhase.PREPARATION
                || (gp.world.getCurrentPhase() == DayPhase.SERVICE && !gp.world.isMenuChosen());
        if(interactHitbox.intersects(gp.player.interactHitbox) && canInteract) {

            g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, 
                    (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);

            // Open UI on key press
            if(gp.keyI.ePressed && clickCooldown == 0) {
                uiOpen = !uiOpen;
                clickCooldown = 10;
                if(uiOpen) {
                    //gp.pauseTime(); // freeze world time while selecting menu
                } else {
                    //gp.resumeTime();
                }
            }
        } else {
            g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
            uiOpen = false;
        }

        if(clickCooldown>0) clickCooldown--;
    }
    
    // Draw the menu UI
    public void drawOverlayUI(Graphics2D g2) {
        if(!uiOpen) return;

        // Background
        g2.drawImage(ui1, 50, 50, 275*3, 187*3, null);

        // Get unlocked recipes
        List<Recipe> unlocked = new ArrayList<>(RecipeManager.getUnlockedRecipes());

        // Remove today’s specials from unlocked list
        List<Recipe> specials = gp.world.getTodaysSpecials();
        unlocked.removeAll(specials);

        // Setup font
        g2.setFont(menuFont);
        FontMetrics fm = g2.getFontMetrics();

        int startX = 120;
        int startY = 256;
        int lineHeight = fm.getHeight() + 4; // spacing between lines

        // Get mouse position

        for (int i = 0; i < unlocked.size(); i++) {
            g2.setFont(menuFont);
            Recipe recipe = unlocked.get(i);
            String name = recipe.getName();

            
            int textX = startX;
            int textY = startY + i * lineHeight;
            int maxNumInColumn = 8;
            if(i > maxNumInColumn) {
            	textX = startX + 160;
            	textY = startY + (i-maxNumInColumn-1)* lineHeight;
            }

            // Compute text bounds
            int textWidth = fm.stringWidth(name);
            int textHeight = fm.getAscent();
            Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

            // Check if hovering
            boolean hovering = nameHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY);

            // Change color when hovering
            if (hovering) {
    			g2.setFont(timeFont);
                drawRecipe(g2, recipe);
                g2.setColor(Color.WHITE);
                if (gp.mouseI.leftClickPressed) {
                    gp.world.addRecipeToMenu(recipe);
                }
            } else {
                g2.setColor(hoverColor);
            }

            // Draw the text
            g2.drawString(name, textX, textY);
            
        }
        
        // --- Draw specials list, below unlocked recipes ---
        int specialsStartY = 486; // 30px gap
        for (int i = 0; i < specials.size(); i++) {
            g2.setFont(menuFont);
            Recipe recipe = specials.get(i);
            String name = recipe.getName();

            int textX = startX;
            int textY = specialsStartY + (i+1) * lineHeight;

         // Compute text bounds
            int textWidth = fm.stringWidth(name);
            int textHeight = fm.getAscent();
            Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);

            // Check if hovering
            boolean hovering = nameHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY);

            // Change color when hovering
            if (hovering) {
    			g2.setFont(timeFont);
                drawRecipe(g2, recipe);
                g2.setColor(specialHover);
                if (gp.mouseI.leftClickPressed) {
                    gp.world.addRecipeToMenu(recipe);
                }
            } else {
                g2.setColor(specialColour);
            }

            // Just show specials (no click-to-add since they’re auto-included)
            g2.drawString(name, textX, textY);
        }
        
        List<Recipe> chosen = gp.world.getTodaysMenu();
        for (int i = 0; i < chosen.size(); i++) {
            g2.setFont(nameFont);
            Recipe recipe = chosen.get(i);
            switch(i) {
            case 0, 1, 2:
                drawRecipe(g2, recipe, 426 + i*(36*3) +i*5 + 11, 209);
            	Rectangle2D.Float border = new Rectangle2D.Float((float)(426 + i*(36*3) +i*5 + 11), (float)209, (float)32*3, (float)48*3);
            	if(border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY)) {
            		if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
            			gp.world.removeRecipeFromMenu(recipe);
            			clickCooldown = 16;
            		}
            	}
            	break;
            case 3:
            	drawRecipe(g2, recipe, 491, 398);
            	border = new Rectangle2D.Float((float)(491), (float)398, (float)32*3, (float)48*3);
            	if(border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY)) {
            		if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
            			gp.world.removeRecipeFromMenu(recipe);
            			clickCooldown = 16;
            		}
            	}
            	break;
            case 4:
            	drawRecipe(g2, recipe, 605, 398);
            	border = new Rectangle2D.Float((float)(605), (float)398, (float)32*3, (float)48*3);
            	if(border.contains(gp.mouseI.mouseX, gp.mouseI.mouseY)) {
            		if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
            			gp.world.removeRecipeFromMenu(recipe);
            			clickCooldown = 16;
            		}
            	}
            	break;
            }
        }
        
        g2.setColor(Color.WHITE);
		g2.setFont(timeFont);
		String text = "Done";
		int textX = 730;
		int textY = 544;
		int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        Rectangle2D.Float nameHitbox = new Rectangle2D.Float(textX, (textY - textHeight), textWidth, textHeight);
    	if(nameHitbox.contains(gp.mouseI.mouseX, gp.mouseI.mouseY)) {
			g2.setColor(specialColour);
    		if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
    			uiOpen = false;
    			clickCooldown = 16;
    		}
    	}
		g2.drawString(text, textX, textY);
		
        
    }
    private void drawRecipe(Graphics2D g2, Recipe recipe) {
    		int i = 2;
			int x = 8 + i * (36*3);
			int y = 581;

			// BASE
			g2.drawImage(recipeBorder, x, y, 32 * 3, 48 * 3, null);

			// INGREDIENT IMAGES
			List<String> ingredients = recipe.getIngredients();
			List<String> cookingState = recipe.getCookingStates();
			List<String> secondaryCookingState = recipe.getSecondaryCookingStates();
			for (int j = 0; j < ingredients.size(); j++) {
				String ingredientName = ingredients.get(j);
				BufferedImage ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
				Food ingredient = (Food)gp.itemRegistry.getItemFromName(ingredientName, 0);
				if(ingredient.notRawItem) {
					ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
				}
				if (ingredientImage != null) {
					// Draw each ingredient image 32px apart above the order box
					g2.drawImage(ingredientImage, x + j * (10*3) + 4, y + 4, 10*3, 10*3, null);
					g2.drawImage(gp.recipeM.getIconFromName(cookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3, null);
					g2.drawImage(gp.recipeM.getIconFromName(secondaryCookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3, null);
				}
			}
			
			// NAME
			g2.setColor(orderTextColour);
			g2.setFont(nameFont);
			int counter = 0;
			for(String line: recipe.getName().split(" ")) {
	            g2.drawString(line, x + (48 - getTextWidth(line, g2) / 2.0f), y + 84 + counter);
	            counter += 15;
	        }
			// PLATE IMAGE
			g2.drawImage(recipe.finishedPlate, x + 24, y + 94, 48, 48, null);
			
			g2.drawImage(coinImage, x, y + 94 + 48, 48, 48, null);
			
			g2.setColor(Color.WHITE);
			g2.setFont(timeFont);
			g2.drawString(Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe))), x + 48+8, y + 94 + 48+32);

    }
    private void drawRecipe(Graphics2D g2, Recipe recipe, int x, int y) {
		// BASE
		g2.drawImage(recipeBorder, x, y, 32 * 3, 48 * 3, null);

		// INGREDIENT IMAGES
		List<String> ingredients = recipe.getIngredients();
		List<String> cookingState = recipe.getCookingStates();
		List<String> secondaryCookingState = recipe.getSecondaryCookingStates();
		for (int j = 0; j < ingredients.size(); j++) {
			String ingredientName = ingredients.get(j);
			BufferedImage ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
			Food ingredient = (Food)gp.itemRegistry.getItemFromName(ingredientName, 0);
			if(ingredient.notRawItem) {
				ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
			}
			if (ingredientImage != null) {
				// Draw each ingredient image 32px apart above the order box
				g2.drawImage(ingredientImage, x + j * (10*3) + 4, y + 4, 10*3, 10*3, null);
				g2.drawImage(gp.recipeM.getIconFromName(cookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3, null);
				g2.drawImage(gp.recipeM.getIconFromName(secondaryCookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3, null);
			}
		}
		
		// NAME
		g2.setColor(orderTextColour);
		g2.setFont(nameFont);
		int counter = 0;
		for(String line: recipe.getName().split(" ")) {
            g2.drawString(line, x + (48 - getTextWidth(line, g2) / 2.0f), y + 84 + counter);
            counter += 15;
        }
		// PLATE IMAGE
		g2.drawImage(recipe.finishedPlate, x + 24, y + 94, 48, 48, null);
		
		g2.drawImage(coinImage, x, y + 94 + 48, 48, 48, null);
		
		g2.setColor(Color.WHITE);
		g2.setFont(timeFont);
		g2.drawString(Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe))), x + 48+8, y + 94 + 48+32);

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
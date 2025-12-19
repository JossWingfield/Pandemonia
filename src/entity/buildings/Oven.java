package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Oven extends Building {
	
	public Food currentItem;
    private List<String> rawIngredients;
    private List<String> cookedResults;
	
	public Rectangle2D.Float ovenHitbox;
	
	private Texture ovenOn;
	private double clickCooldown = 0;
	private boolean cooking = false;
    private double cookCount = 0;
    private double maxCookCount = 20;
    
	private boolean destroyed = false;
	
	public Oven(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 48*3;
		xDrawOffset = 24;
		yDrawOffset = 24 + 48;
		importImages();
		setupRecipes();
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public void onPlaced() {
		buildHitbox = hitbox;
	}
	public Building clone() {
		Oven building = new Oven(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Oven(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");
	}
	private void importImages() {
		animations = new TextureRegion[1][1][6];
		
		name = "Oven";
    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(80, 128, 32, 48);
    	animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][3] = importImage("/decor/OvenHighlight.png").getSubimage(0, 0, 32, 48);
       	animations[0][0][4] = importImage("/decor/OvenHighlight.png").getSubimage(32, 0, 32, 48);
       	animations[0][0][5] = importImage("/decor/OldKitchenProps.png").getSubimage(0, 0, 32, 48);
	
       	ovenOn = importImage("/decor/OvenOn.png");
	}
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	public void update(double dt) {
		super.update(dt);
		if (clickCooldown > 0) {
	    	clickCooldown -= dt;        // subtract elapsed time in seconds
			if (clickCooldown < 0) {
				clickCooldown = 0;      // clamp to zero
			}
		}
		 if(hitbox.intersects(gp.player.interactHitbox)) {
			    if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
			    	if(clickCooldown == 0) {
				    	if(gp.player.currentItem != null) {
				    		if(currentItem == null) { //IF EMTPY
					    		if(canCook(gp.player.currentItem.getName())) {
					    			Food f = (Food)gp.player.currentItem;
					    			if(f.foodState == FoodState.RAW) {
							    		currentItem = (Food)gp.player.currentItem;
							    		cooking = true;
							    		gp.player.currentItem = null;
							    		clickCooldown = 0.1;
					    			}
					    		}
				    		} else {
				    			if(gp.player.currentItem.getName().equals("Plate")) {
				    				if(currentItem.foodState == FoodState.COOKED) {
				    					currentItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(currentItem);
										currentItem = null;
						    		}
				    		    }
				    		}
				    	}
			    	}
			    }
		    }
			if(gp.progressM.ovenUpgradeI) {
				maxCookCount = 16;
			}
		    if(cooking) {
		    	if(gp.world.isPowerOn()) {
		    		updateCooking(dt);
		    	}
		    }
	}
	public void draw(Renderer renderer) {
		
		if(destroyed) {
		    renderer.draw(animations[0][0][5], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
	    	if(currentItem == null) {
			    renderer.draw(animations[0][0][4], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			    renderer.draw(animations[0][0][3], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
	    } else {
			if(currentItem == null) {
			    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
	    }
	    
	    
	    if(cooking) {
		    renderer.draw(ovenOn, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		if(currentItem != null) {
			if(currentItem.foodState == FoodState.RAW) {
				drawCookingBar(renderer, (int) hitbox.x  + 24, (int) (hitbox.y ) + 24+48, (int)cookCount, (int)maxCookCount);
			}
		}
	}
	private void updateCooking(double dt) {
		if(currentItem.foodState == FoodState.RAW) {
			cookCount+=dt;
			if(cookCount >= maxCookCount) {
				cookCount = 0;
				currentItem.foodState = FoodState.COOKED;
				cooking = false;
			}
		}
	}
	private void drawCookingBar(Renderer renderer, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset ;
	    float screenY = worldY - yDrawOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	 private void setupRecipes() {
		 rawIngredients = new ArrayList<>();
		 cookedResults = new ArrayList<>();

	     rawIngredients.add("Potato");
	     cookedResults.add("Jacket Potato");
	     
		 rawIngredients.add("Chicken");
		 cookedResults.add("Cooked Chicken");
	}
	public boolean canCook(String itemName) {
		return rawIngredients.contains(itemName);
	}
	public String getCookedResult(String rawItemName) {
	    int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return cookedResults.get(index);
	    }
	    return null;
	}
	public void setCooking() {
		//animations[0][0][0] = animations[0][0][2];
	    cooking = true;
	}
	public boolean isCooking() {
		return cooking;
	}
}
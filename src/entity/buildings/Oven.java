package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.Item;
import entity.items.OvenTray;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Oven extends Building {
	
	public Item currentItem;
    private List<String> rawIngredients;
    private List<String> cookedResults;
	
	public Rectangle2D.Float ovenHitbox;
	
	private Texture ovenOn;
	private double clickCooldown = 0;
	private boolean cooking = false;
    private double cookCount = 0;
    private double maxCookCount = 20;
    private TextureRegion completeSign;
    
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
		completeSign = importImage("/UI/Tick.png").getSubimage(0, 0, 16, 16);
		OvenTray ot = new OvenTray(gp, 0, 0);
		currentItem = ot;
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
		animations = new TextureRegion[1][1][8];
		
		name = "Oven";
    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(80, 128, 32, 48);
    	animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][3] = importImage("/decor/OvenHighlight.png").getSubimage(0, 0, 32, 48);
       	animations[0][0][4] = importImage("/decor/OvenHighlight.png").getSubimage(32, 0, 32, 48);
       	animations[0][0][5] = importImage("/decor/OldKitchenProps.png").getSubimage(0, 0, 32, 48);
       	
	
     	animations[0][0][6] = importImage("/decor/OvenHighlight.png").getSubimage(64, 0, 32, 48);
     	animations[0][0][7] = importImage("/decor/OvenHighlight.png").getSubimage(96, 0, 32, 48);

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
			    if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
			    	if(clickCooldown == 0) {
				    	if(gp.player.currentItem != null) {
				    		if(currentItem == null) { //IF EMTPY
				    			Item held = gp.player.currentItem;
				    			// ===== NORMAL FOOD =====
				    			if (held instanceof Food food) {
				    				if (canCook(food.getName()) && food.foodState == FoodState.RAW) {
				    					food.addCookMethod(name);
				    			        currentItem = food;
				    			        cooking = true;
				    			        cookCount = 0;
				    			        gp.player.currentItem = null;
				    			        clickCooldown = 0.1;
				    			    }
				    			} else if (held instanceof OvenTray tray) {
				    				if(tray.getFoodState() == FoodState.RAW) {
				    					if(!tray.isEmpty()) {
					    					currentItem = tray;
						    			    cooking = true;
						    			    cookCount = 0;
						    			    gp.player.currentItem = null;
						    			    clickCooldown = 0.1;
				    					}
				    				}
				    			}
				    		} else {
				    			if(gp.player.currentItem.getName().equals("Plate")) {
				    				if(currentItem instanceof Food f) {
					    				if(f.foodState == FoodState.COOKED) {
					    					f.foodState = FoodState.PLATED;
											Plate p = (Plate)gp.player.currentItem;
											p.addIngredient(f);
											currentItem = null;
							    		}
				    				} else if(currentItem instanceof OvenTray ovenTray){
				    					if(ovenTray.getFoodState().equals(FoodState.COOKED)) {
				    						Plate p = (Plate)gp.player.currentItem;
				    						ovenTray.addToPlate(p);
				    					}
				    				}
				    		    }
				    		}
				    	} else if (gp.player.currentItem == null && currentItem != null && !cooking && currentItem instanceof OvenTray) {
				    	    gp.player.currentItem = currentItem;
				    	    currentItem = null;
				    	    clickCooldown = 0.1;
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
				if(currentItem instanceof OvenTray ot && ot.isEmpty()) {
				    renderer.draw(animations[0][0][7], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				} else {
					renderer.draw(animations[0][0][3], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				}
			}
	    } else {
			if(currentItem == null) {
			    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
				if(currentItem instanceof OvenTray ot && ot.isEmpty()) {
				    renderer.draw(animations[0][0][6], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				} else {
				    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				}
			}
	    }
	    
	    
	    if(cooking) {
		    renderer.draw(ovenOn, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		if (currentItem != null) {

		    // Skip empty oven trays entirely
		    if (currentItem instanceof OvenTray tray && tray.getIngredients().isEmpty()) {
		        return;
		    }

		    if (getFoodState() == FoodState.RAW) {
		    	drawCookingBar(renderer,(int) hitbox.x + 24,(int) hitbox.y + 24 + 48,(int) cookCount,(int) maxCookCount);
		    } else {
		        drawCookingWarning(renderer, (int) hitbox.x, (int) hitbox.y);
		    }
		}
	}
	private FoodState getFoodState() {
		if(currentItem instanceof Food f) {
			return f.foodState;
		} else if(currentItem instanceof OvenTray ot) {
			return ot.getFoodState();
		} else {
			return null;
		}
	}
	private void updateCooking(double dt) {

	    cookCount += dt;

	    if (cookCount < maxCookCount) return;

	    cookCount = 0;
	    cooking = false;

	    // ===== SINGLE FOOD =====
	    if (currentItem instanceof Food food) {
	        if (food.foodState == FoodState.RAW) {
	            food.foodState = FoodState.COOKED;
	        }
	    } else if (currentItem instanceof OvenTray tray) {
	        tray.finishCooking();
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
	public void drawCookingWarning(Renderer renderer, int x, int y) {
		if(getFoodState().equals(FoodState.COOKED)) {
			renderer.draw(completeSign,(int)(x ),(int)(y  - 48),48, 48);
		}
	}
	 private void setupRecipes() {
		 rawIngredients = new ArrayList<>();
		 cookedResults = new ArrayList<>();

	     rawIngredients.add("Potato");
	     cookedResults.add("Jacket Potato");
	     
		 rawIngredients.add("Chicken");
		 cookedResults.add("Cooked Chicken");
		 
		 rawIngredients.add("Bread Slice");
		 cookedResults.add("Toast");
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
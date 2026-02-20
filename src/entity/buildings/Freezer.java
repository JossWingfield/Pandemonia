package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.Item;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class Freezer extends Building {
	
	private Rectangle2D.Float interactHitbox;
    private TextureRegion foodBorder;
	private boolean firstUpdate = true;
	private LightSource light;
	
	public Food currentFood;
	
	private Map<String, Set<FoodState>> canBeFrozen = new HashMap<>();
	
	private double clickCounter = 0;
	
	public Freezer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 80, 40);
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 8;
		yDrawOffset = 48;
		castsShadow = false;
		light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + 25),Colour.BLUE, 100);
		isStoreBuilding = true;
		mustBePlacedOnFloor = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
		setUp();
	}
	private void setUp() {
		allowInFreezer("Potato", FoodState.CHOPPED);
	}
	private void allowInFreezer(String ingredientName, FoodState... allowedStates) {
		canBeFrozen.putIfAbsent(ingredientName, new HashSet<>());
	    for (FoodState state : allowedStates) {
	    	canBeFrozen.get(ingredientName).add(state);
	    }
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
	}
	public Building clone() {
		Freezer building = new Freezer(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Freezer(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][2][3];
				
		name = "Freezer";
		animations[0][0][0] = importImage("/decor/FreezerBox.png").getSubimage(0, 0, 32, 32);
		animations[0][0][1] = importImage("/decor/FreezerBoxHighlight.png").getSubimage(0, 0, 32, 32);
		animations[0][0][2] = importImage("/decor/FreezerBoxEmissive.png").getSubimage(0, 0, 32, 32);
		
		animations[0][1][0] = importImage("/decor/FreezerBox.png").getSubimage(32, 0, 32, 32);
		animations[0][1][1] = importImage("/decor/FreezerBoxHighlight.png").getSubimage(32, 0, 32, 32);
		animations[0][1][2] = importImage("/decor/FreezerBoxEmissive.png").getSubimage(32, 0, 32, 32);
		foodBorder = importImage("/food/FoodBorder.png").toTextureRegion();
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x+4, hitbox.y+hitbox.height - 32, hitbox.width-8, 32);
			gp.world.lightingM.addLight(light);
			importImages();
		}
		
		if(clickCounter > 0) {
			clickCounter-= dt;
			if(clickCounter < 0) {
				clickCounter = 0;
			}
		}
		
		if(interactHitbox != null) {
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && clickCounter == 0) {
				if (gp.player.currentItem == null) {
				    String itemName = null;
				    //PICK UP
				    clickCounter = 0.33;
				    if (itemName != null) {
				        //gp.player.currentItem = (Food) gp.world.itemRegistry.getItemFromName(itemName, 0);
				    	//gp.player.resetAnimation(4);
				    }
				} else {
					if(canFreezeItem(gp.player.currentItem)) {
						currentFood = (Food)gp.player.currentItem;
						clickCounter = 0.33;
						gp.player.currentItem = null;
					}
				}
			}
		}
		}
		
	}
	public boolean canFreezeItem(Item item) {
		if(item instanceof Food food) {

		    // Ingredient not supported at all
		    if (!canBeFrozen.containsKey(food.getName())) {
		        return false;
		    }

		    // Food is in the wrong state
		    Set<FoodState> allowedStates = canBeFrozen.get(food.getName());
		    if (!allowedStates.contains(food.foodState)) {
		        return false;
		    }
		    
		    return true;
		}
		return false;
	}
	public void draw(Renderer renderer) {
		if(interactHitbox == null) {
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
		}
		
		gp.world.particleM.startFreezerMist(453-10, 173, 698-453 - 20, 595-173 - 20);
		
		int currentAni = 0;
		if(currentFood != null) {
			currentAni = 1;
		}
		
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			renderer.draw(animations[0][currentAni][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
		     renderer.draw(animations[0][currentAni][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
	public void drawOverlayUI(Renderer renderer) {
		if(currentFood == null) {
			return;
		}
	    if(gp.player.interactHitbox.intersects(hitbox)) {
		        
	    	float startX = (hitbox.x - xDrawOffset+24);
		    float x = startX;
				
            renderer.draw(foodBorder, x - 4, hitbox.y - yDrawOffset - 24, 32, 32);

            TextureRegion img = currentFood.getImage();
            renderer.draw(img, x-currentFood.xDrawOffset, hitbox.y - yDrawOffset - 24, 32, 32);
	    }
	}
	public void drawEmissive(Renderer renderer) {
		if(animations[0][0][2] != null) {
			int currentAni = 0;
			if(currentFood != null) {
				currentAni = 1;
			}
			renderer.draw(animations[0][currentAni][2], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);		
		}
	}
}


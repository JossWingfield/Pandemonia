package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.IceBlock;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import net.packets.Packet12PlaceOnChoppingBoard;
import net.packets.Packet13PickUpFromChoppingBoard;
import net.packets.Packet14Chop;
import utility.Statistics;

public class ChoppingBoard extends Building {
	
    private List<String> rawIngredients;
    private List<String> choppedResults;
	
	private Food currentItem = null;
	private double clickCooldown = 0;
	private boolean chopping = false;
    private int chopCount = 0;
    private int currentChopCount = 12;
	
	public ChoppingBoard(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		setupRecipes();
		canBePlaced = false;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		//isSecondLayer = true;
		//isThirdLayer = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*7);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*7);
	}
	public Building clone() {
		ChoppingBoard building = new ChoppingBoard(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ChoppingBoard(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");
	}
	private void importImages() {
		animations = new TextureRegion[1][1][3];
		
		name = "Chopping Board";
    	animations[0][0][0] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 0, 16, 16);
    	animations[0][0][1] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 16, 16, 16);
    	animations[0][0][2] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 32, 16, 16);
	}
	public void setCurrentItem(Food item) {
	    this.currentItem = item;
	    this.chopCount = 0;
	}
	public void removeItem() {
		currentItem = null;
		chopping = false;
		chopCount = 0;
	}
	public void updateState(double dt) {
		super.updateState(dt);
	}
	public void addItem(Food f) {
		currentItem = f;
		currentItem.addCookMethod(name);
 		currentChopCount = f.getChopCount();
 		chopCount = 0;
	}
	public void inputUpdate(double dt) {
		
		if (clickCooldown > 0) {
			clickCooldown -= dt;        // subtract elapsed time in seconds
			if (clickCooldown < 0) {
				clickCooldown = 0;      // clamp to zero
			}
		}
		
		   if(hitbox.intersects(gp.player.hitbox)) {
			    if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
			    	if(clickCooldown == 0) {
				    	if(gp.player.currentItem != null) {
				    		if(canChop(gp.player.currentItem.getName())) {
						    		if(currentItem == null) {
					    			Food f = (Food)gp.player.currentItem;
					    			if(f.foodState == FoodState.RAW) {
					    				if(gp.multiplayer) {
					    					gp.socketClient.send(new Packet12PlaceOnChoppingBoard(gp.player.getUsername(), getArrayCounter(), gp.player.currentItem));
					    					clickCooldown = 0.08;
					    				} else {
								    		currentItem = (Food)gp.player.currentItem;
								    		gp.player.currentItem = null;
								    		currentItem.addCookMethod(name);
								    		clickCooldown = 0.08;
								    		currentChopCount = f.getChopCount();
					    				}
							    		
					    			}
					    		}
				    		} else if(gp.player.currentItem instanceof Plate p) {
				    			if(currentItem != null) {
					    			if(p.canBePlated(currentItem.getName(), currentItem.foodState)) {
					    				currentItem.foodState = FoodState.PLATED;
										p.addIngredient(currentItem);
										currentItem = null;
										clickCooldown = 0.333;
					    			}
				    			}
				    		}
				    	} else {
				    		if(currentItem != null) {
					    		if(currentItem.foodState == FoodState.RAW && canChop(currentItem.getName())) {
					    			if(gp.multiplayer) {
					    				clickCooldown = 0.08;
				    					gp.socketClient.send(new Packet14Chop(gp.player.getUsername(), getArrayCounter(), chopCount));
					    			} else {
							    		clickCooldown = 0.08;
							    		chopCount++;
							    		if(chopCount == currentChopCount) {
							    			finishChopItem();
							    		}
					    			}
					    		} else {
					    			pickUpItem();
					    		}
				    		}else {
				    			pickUpItem();
				    		}
				    	}
			    	}
			    }
		    }
	}
	public boolean canContinueChopping() {
        return currentItem.foodState == FoodState.RAW && canChop(currentItem.getName());
	}
	private void pickUpItem() {
		if(gp.multiplayer) {
			gp.socketClient.send(new Packet13PickUpFromChoppingBoard(gp.player.getUsername(), getArrayCounter(), currentItem));
			clickCooldown = 0.08;
		} else {
			if(currentItem != null) {
				gp.player.resetAnimation(2);
			}
			gp.player.currentItem = currentItem;
			currentItem = null;
			clickCooldown = 0.333;
		}
	}
	public void draw(Renderer renderer) {
		renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
      		 
	    if(hitbox.intersects(gp.player.hitbox)) {
		    renderer.draw(animations[0][0][1], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
	    if(currentItem != null) {
		    renderer.draw(animations[0][0][2], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    	renderer.draw(currentItem.getImage(), (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		if(currentItem != null) {
			if(currentItem.foodState == FoodState.RAW && canChop(currentItem.getName())) {
				drawChoppingBar(renderer, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, chopCount, currentChopCount);
			}
		}
	}
	public void clearCurrentItem() {
	    this.currentItem = null;
	    this.chopCount = 0;
	    this.chopping = false;
	}
	public void setChopCount(int chopCount) {
	    this.chopCount = chopCount;
	    if(chopCount == currentChopCount) {
			finishChopItem();
		}
	}
	public void finishChopItem() {
		chopCount = 0;
		Statistics.ingredientsChopped++;
		if(Statistics.ingredientsChopped == 100) {
    		gp.world.progressM.achievements.get("100_chopped").unlock();
		}
		currentItem.foodState = FoodState.CHOPPED;
		if(currentItem.cutIntoNewItem) {
			Food newItem = (Food)gp.world.itemRegistry.getItemFromName(getChoppedResult(currentItem.getName()), 0);
			currentItem = newItem;
    		currentItem.addCookMethod(name);
		}
		if(currentItem instanceof IceBlock ice) {
			currentItem = ice.enclosedItem;
		}
	}
	private void drawChoppingBar(Renderer renderer , float worldX, float worldY, double cookTime, double maxCookTime) {
	    float screenX = worldX - xDrawOffset ;
	    float screenY = worldY - yDrawOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, (float)(cookTime / maxCookTime));

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset-3, (int) screenY + yOffset-3, barWidth+6, barHeight+6, Colour.BASE_COLOUR);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	 private void setupRecipes() {
		 rawIngredients = new ArrayList<>();
	     choppedResults = new ArrayList<>();

	     rawIngredients.add("Cheese");
	     choppedResults.add("Chopped Cheese");
	     
	     rawIngredients.add("Bread");
	     choppedResults.add("Bread Slice");
	     
	     rawIngredients.add("Greens");
	     choppedResults.add("Chopped Greens");
	     
	     rawIngredients.add("Steak");
	     choppedResults.add("Meatball");
	     
	     rawIngredients.add("Tomato");
	     choppedResults.add("Chopped Tomatoes");
	     
	     rawIngredients.add("Chicken");
	     choppedResults.add("Chicken Pieces");
	     
	     rawIngredients.add("Garlic");
	     choppedResults.add("Chopped Garlic");
	     
	     rawIngredients.add("Aubergine");
	     choppedResults.add("Chopped Aubergine");
	     
	     rawIngredients.add("Carrot");
	     choppedResults.add("Chopped Carrot");
	     
	     rawIngredients.add("Aubergine");
	     choppedResults.add("Chopped Aubergine");
	     
	     rawIngredients.add("Cursed Greens");
	     choppedResults.add("Chopped Cursed Greens");
	     
	     rawIngredients.add("Potato");
	     choppedResults.add("Fries");
	     
	     rawIngredients.add("Ice Block");
	     choppedResults.add("???");
	}
	public boolean canChop(String itemName) {
		return rawIngredients.contains(itemName);
	}
	public boolean isEmpty() {
		return currentItem == null;
	}
	public String getChoppedResult(String rawItemName) {
	    int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return choppedResults.get(index);
	    }
	    return null;
	}
	public void setChopping() {
		//animations[0][0][0] = animations[0][0][1];
	    chopping = true;
	}
	public boolean isChopping() {
		return chopping;
	}
}
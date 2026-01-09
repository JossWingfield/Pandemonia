package entity.npc;

import java.util.Random;

import entity.buildings.CookStation;
import entity.buildings.Stove;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.Item;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.RoomHelperMethods;

public class Cook extends Employee {

	enum CookState {
	    FETCHING_INGREDIENT,
	    WALKING_TO_STOVE,
	    START_COOKING,
	    WAITING_FOR_COOK,
	    PICKING_UP_FOOD,
	    RETURNING_TO_STATION
	}
	

	private CookStation station;
	private Stove stove;

	private CookingItem pan;
	private Item carriedItem;
	
	private int stoveSlot = 0; // 0 = left, 1 = right
	private CookState state = CookState.FETCHING_INGREDIENT;
	
	public Cook(GamePanel gp, int xPos, int yPos) {
		super(gp, xPos, yPos);
		drawWidth = 48;
		drawHeight = 48*2;
		speed = 2*60;
		
		animationSpeedFactor = 0.1;
				
		npcType = "Cook";
		
		r = new Random();
				
		importImages();
	}
	
	private void importImages() {
		 animations = new TextureRegion[5][20][15];
		 importPlayerSpriteSheet("/npcs/employees/server/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
	     importPlayerSpriteSheet("/npcs/employees/server/walk", 8, 1, 1, 0, 0, 80, 80); //RUN
	        
	     //importPlayerSpriteSheet("/player/holdingIdle", 4, 1, 2, 0, 0, 80, 80); //HOLDING IDLE
	     //importPlayerSpriteSheet("/player/holdingRun", 8, 1, 3, 0, 0, 80, 80); //HOLDING RUN

	     //importPlayerSpriteSheet("/player/pickup", 6, 1, 4, 0, 0, 80, 80); //Pick Up
	     drawWidth = 80*drawScale;
	     drawHeight = 80*drawScale;
	     xDrawOffset = 34*drawScale;
	     yDrawOffset = 36*drawScale;
		 name = "Ignis";
	}
	private void findCookStation() {
	    station = (CookStation) findBuildingInRoom("Cook Station", RoomHelperMethods.KITCHEN);
	}

	private void findStove() {
	    stove = (Stove) findBuildingInRoom("Stove", RoomHelperMethods.KITCHEN);
	}
	private boolean isCookable(Food food) {
	    if (food == null) return false;
	    if (stove == null) findStove();
	    if (stove == null) return false;

	    if (stove.leftSlot != null && stove.leftSlot.canCook(food.getName())) {
	        return true;
	    }
	    if (stove.rightSlot != null && stove.rightSlot.canCook(food.getName())) {
	        return true;
	    }
	    return false;
	}
	public void update(double dt) {
		super.update(dt);
		
		switch (state) {

		case FETCHING_INGREDIENT -> {
		    if (station == null) findCookStation();
		    if (stove == null) findStove();

		    if (station == null || stove == null) return;

		    // Do not move unless there's a valid cookable item
		    if (!station.hasItem()) return;
		    if (!(station.peekItem() instanceof Food food)) return;
		    if (!isCookable(food)) return;

		    // Only now do we walk
		    if (walkToBuilding(dt, station)) {
		    	carriedItem = station.takeItem();
		        state = CookState.WALKING_TO_STOVE;
		    }
		}

		case WALKING_TO_STOVE -> {
		    if (stove == null) findStove();

		    if (walkToBuilding(dt, stove)) {
		        if (stove.leftSlot != null && stove.leftSlot.canCook(carriedItem.getName())) {
		            pan = stove.leftSlot;
		            stoveSlot = 0;
		        } else if (stove.rightSlot != null && stove.rightSlot.canCook(carriedItem.getName())) {
		            pan = stove.rightSlot;
		            stoveSlot = 1;
		        } else {
		            // No valid pan anymore → abort and return item
		            state = CookState.RETURNING_TO_STATION;
		            return;
		        }
		        state = CookState.START_COOKING;
		    }
		}

        case START_COOKING -> {
        	if (pan != null && pan.cookingItem == null && carriedItem instanceof Food food) {
        	    pan.setCooking(food);
        	    carriedItem = null;
        	    state = CookState.WAITING_FOR_COOK;
        	}
        }

        case WAITING_FOR_COOK -> {
            if (pan.cookingItem != null &&
                pan.cookingItem.foodState == FoodState.COOKED) {

                // Detach pan from stove
                if (stoveSlot == 0) {
                    stove.leftSlot = null;
                } else {
                    stove.rightSlot = null;
                }

                pan.stopCooking();

                // Carry the pan instead of the food
                carriedItem = pan;
                pan = null;

                state = CookState.RETURNING_TO_STATION;
            }
        }

        case RETURNING_TO_STATION -> {
            if (walkToBuilding(dt, station)) {
                station.placeItem(carriedItem);
                carriedItem = null;

                // Reset loop
                stove = null;
                pan = null;
                state = CookState.FETCHING_INGREDIENT;
            }
        }
    }
		
        animationSpeed+=animationUpdateSpeed*dt; //Update the animation frame
        if(animationSpeed >= animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }
        if(walking) {
    		currentAnimation = 1;
    	} else {
    		currentAnimation = 0;
    	}
        if(animations != null) {
            if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
                animationCounter = 0; //Loops the animation
                if(currentAnimation == 4) {
                	currentAnimation = 2;
                }
            }
        }
	}	
	public void drawCurrentItem(Renderer renderer) {
    	if(carriedItem == null) {
    		return;
    	}
    	
    	int xOffset = 120 - 24;
    	int yOffset = 132 - 24;
    	
    	xOffset = 120-24;
    	yOffset = 132-(48+8);
    	int finalOffset = yOffset;
    	int baseOffset = 132-(16); 

    	if(currentAnimation == 4) {
	    	int currentStage = animationCounter; // goes 0 → 6
	    	int totalStages = 2;  
	    	
	    	// Clamp to avoid going out of range
	    	if (currentStage < 0) currentStage = 0;
	    	if (currentStage > totalStages) currentStage = totalStages;
	    	yOffset = baseOffset + (finalOffset - baseOffset) * currentStage / totalStages;
    	}
    	
    	TextureRegion img = carriedItem.animations[0][0][0];
    	if(carriedItem instanceof Food) {
    		Food f = (Food)carriedItem;
    		img = f.getImage();
    	}
		renderer.draw(img, (int)(hitbox.x - xDrawOffset + xOffset), (int)(hitbox.y  - yDrawOffset + yOffset), (int)(48), (int)(48));

    }
    public void draw(Renderer renderer) {
    	
    	if(direction == "Up") {
        	drawCurrentItem(renderer);
        }

        if(animations != null) {
            TextureRegion img = animations[0][currentAnimation][animationCounter];
	    	  int a = 0;
	    	  if(direction != null) {
	    	  switch(direction) {
	    	  case "Left":
	    		  a = 1;
	    		  break;
	    	  case "Right":
	    		  a = 0;
	    		  break;
	    	  case "Up":
	    		  a = 3;
	    	  	  break;
	    	  case "Down":
	    		  a = 2;
	    	  	  break;
	    	  }
	    	  img = animations[a][currentAnimation][animationCounter];
		    	  if(direction.equals("Left") && img != null) {
		          	img = createHorizontalFlipped(img);
		          }
	    	  }   
	    	  renderer.draw(img, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y - yDrawOffset ), (int)(drawWidth), (int)(drawHeight));
        }
        if(talking) {
        	//gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- gp.player.xDiff, (int)hitbox.y - 48*3- gp.player.yDiff, dialogues[dialogueIndex], this);
        }
        if(direction != "Up") {
        	drawCurrentItem(renderer);
        }
    }
}


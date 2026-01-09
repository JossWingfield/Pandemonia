package entity;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.buildings.Building;
import entity.buildings.FloorDecor_Building;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.Item;
import entity.items.Plate;
import main.GamePanel;
import main.KeyListener;
import main.MouseListener;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import map.LightSource;
import map.particles.PlayerDustParticle;
import net.packets.Packet;
import net.packets.Packet02Move;
import net.packets.Packet04PlaceItem;
import net.packets.Packet07PickUpItemFromTable;
import net.packets.Packet19AddFoodToPlateInHand;
import net.packets.Packet20AddFoodToPlateOnTable;
import net.packets.Packet21PickupPlate;
import net.packets.Packet22PlacePlate;
import utility.CollisionMethods;
import utility.Settings;
import utility.save.PlayerSaveData;

public class Player extends Entity{
	
    //INPUTS
    public KeyListener keyI;
    public MouseListener mouseI;
    //USERNAME
    private String username;
    private BitmapFont usernameFont;
    private Colour usernameColor;
    
    //GENERAL VARIABLES
    private boolean firstUpdate = true;
    private float speed; //How many pixels per update the player moves at
    private final float initialSpeed;
    private float currentSpeed; //How many pixels per update the player moves at
    public float reachDistance; //Distance the player can reach
    public float talkDistance; //Distance the player can talk
    public float buildRange;
    public boolean facingLeft = false;
    public Item currentItem = null;
    public double clickCounter = 0;
    public Rectangle2D.Float interactHitbox;
    
    //ATTRIBUTES
    public int level = 1;
    public int soulsServed = 0;
    public int nextLevelAmount = 10;
    public int wealth = 300;
    //HEALTH
    public boolean alive = true;
    public int maxHealth = 3;
    public int currentHealth = maxHealth;
    private double invincibilityCounter = 0;

    //SCREEN SCROLL SETTINGS
    private boolean controlEnabled = true;
    
    public int currentRoomIndex = 0;
    
    public LightSource playerLight;
    public boolean isInvisible = false;
    private double dustCooldown = 0;

    public Player(GamePanel gp, int xPos, int yPos, KeyListener keyL, MouseListener mouseL, String username) { //Setting default variables
        super(gp, (xPos), (yPos), 32, 32);
        
        this.keyI = keyL;
        this.mouseI = mouseL;
        this.username = username;
        
        usernameFont = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
        usernameColor = new Colour(213, 213, 213);

        initialSpeed = 300;
        speed = initialSpeed;
        currentSpeed = speed;
        
        interactHitbox = new Rectangle2D.Float(0, 0, 1, 1);
        
        
        setUp();
    }
    
    private void setUp() {
    	//Point2D spawnPoint = gp.mapM.findSpawnPosition();

        //hitbox.x = (float) spawnPoint.getX();
        //hitbox.y = (float) spawnPoint.getY();
        
        //animationSpeedFactor = 2;
    	animationSpeedFactor = 0.09;
        
        reachDistance = (3*gp.tileSize);
        talkDistance = (5*gp.tileSize);
        buildRange = (5f*gp.tileSize);
        
        drawWidth = 80*drawScale;
        drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
        
        playerLight = new LightSource((int)hitbox.x, (int)hitbox.y, Colour.WHITE, 100);
        
        importImages();
    }
    
    public PlayerSaveData toSaveData() {
        PlayerSaveData data = new PlayerSaveData();
        data.username = username;
        data.level = level;
        data.soulsServed = soulsServed;
        data.nextLevelAmount = nextLevelAmount;
        data.wealth = wealth;
        data.x = hitbox.x;
        data.y = hitbox.y;
        data.currentRoomIndex = currentRoomIndex;

        if (currentItem != null) {
            data.currentItemName = currentItem.getName();
            if (currentItem instanceof Food f) {
                data.currentItemState = f.getState();
            }
        }
        return data;
    }
    public void applySaveData(PlayerSaveData data) {
        this.username = data.username;
        this.level = data.level;
        this.soulsServed = data.soulsServed;
        this.nextLevelAmount = data.nextLevelAmount;
        this.wealth = data.wealth;
        this.hitbox.x = data.x;
        this.hitbox.y = data.y;
        this.currentRoomIndex = data.currentRoomIndex;
        gp.mapM.setRoom(currentRoomIndex);

        // Recreate item if needed
        if (data.currentItemName != null) {
            this.currentItem = gp.itemRegistry.getItemFromName(data.currentItemName, data.currentItemState);
            if (this.currentItem instanceof Food f) {
                f.setState(data.currentItemState);
            }
        }
    }
    
    public void setSpawnPoint(int xPos, int yPos) {
    	hitbox.x = xPos;
    	hitbox.y = yPos;
    }
    
    public int getCurrentAnimation() {
    	return currentAnimation;
    }
    public int getAnimationCounter() {
    	return animationCounter;
    }
    public void slowSpeed() {
    	this.speed = 60;
    }
    public void setNormalSpeed() {
    	this.speed = initialSpeed;
    }
    public void importImages() {

        animations = new TextureRegion[4][20][15];
        
        importPlayerSpriteSheet("/player/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
        importPlayerSpriteSheet("/player/run", 8, 1, 1, 0, 0, 80, 80); //RUN
        
        importPlayerSpriteSheet("/player/holdingIdle", 4, 1, 2, 0, 0, 80, 80); //HOLDING IDLE
        importPlayerSpriteSheet("/player/holdingRun", 8, 1, 3, 0, 0, 80, 80); //HOLDING RUN

        importPlayerSpriteSheet("/player/pickup", 6, 1, 4, 0, 0, 80, 80); //Pick Up
        

        currentAnimation = 0;
    }
    
    protected void importPlayerSpriteSheet(String filePath, int columnNumber, int rowNumber, int animationIndex,int startX, int startY, int width, int height) {
	
		Texture sheetTexture = AssetPool.getTexture(filePath + ".png");
		int sheetWidth = sheetTexture.getWidth();
		int sheetHeight = sheetTexture.getHeight();
		
		int originalStartY = startY; // save the original startY
		
		for (int k = 0; k < 4; k++) {
		int arrayIndex = 0;
		
		for (int i = 0; i < columnNumber; i++) {     // column-first
		for (int j = 0; j < rowNumber; j++) {   // row-second
		int px = i * width + startX;
		int py = j * height + startY;
		
		float u0 = px / (float) sheetWidth;
		float v0 = 1f - (py + height) / (float) sheetHeight; // flip V
		float u1 = (px + width) / (float) sheetWidth;
		float v1 = 1f - py / (float) sheetHeight;
		
		animations[k][animationIndex][arrayIndex] =
		new TextureRegion(sheetTexture, u0, v0, u1, v1);
		
		arrayIndex++;
		}
		}
		
		if (k > 0) {
		startY += height;  // increment just like old version
		}
		}
		
		startY = originalStartY; // restore startY in case needed elsewhere
	}
    
    public String getUsername() {
        return username;
    }
    private void normaliseSpeed() {
    	boolean left = keyI.isKeyPressed(GLFW.GLFW_KEY_A);
    	boolean right = keyI.isKeyPressed(GLFW.GLFW_KEY_D);
    	boolean up = keyI.isKeyPressed(GLFW.GLFW_KEY_W);
    	boolean down = keyI.isKeyPressed(GLFW.GLFW_KEY_S);
    	if(direction == 0 || direction == 1) {
    		if(up || down) { //NORMALISING SPEED
        		currentSpeed = (float)(speed/(Math.sqrt(2)));
        	} else {
        		currentSpeed = speed;
        	}
    	} else if(direction == 2 || direction == 3) {
    		if(left || right) { //NORMALISING SPEED
        		currentSpeed = (float)(speed/(Math.sqrt(2)));
        	} else {
        		currentSpeed = speed;
        	}
    	}
    }

    private void handleMovement(double dt) {

    	boolean left = keyI.isKeyPressed(GLFW.GLFW_KEY_A);
    	boolean right = keyI.isKeyPressed(GLFW.GLFW_KEY_D);
    	boolean up = keyI.isKeyPressed(GLFW.GLFW_KEY_W);
    	boolean down = keyI.isKeyPressed(GLFW.GLFW_KEY_S);
    		if (!(left || right || up || down) && currentAnimation != 4) {
                currentAnimation = 0;
                if(currentItem != null) {
                	currentAnimation = 2;
                }
            }
    		

            if (left) { //Moving left
            	direction = 1;
            	facingLeft = true;
            	normaliseSpeed();
            	float moveAmount = (float) (currentSpeed * dt);
                if(CollisionMethods.tileCheck(hitbox.x-moveAmount, hitbox.y, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x-moveAmount, hitbox.y, hitbox.width, hitbox.height)) {
                    hitbox.x -= moveAmount;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x-moveAmount, hitbox.y, hitbox.width, hitbox.height)) {

                } else {
                	hitbox.x = CollisionMethods.getWallPos(hitbox.x, gp);
                }
            } else if (right) { //Moving right
                direction = 0;
                facingLeft = false;
                normaliseSpeed();
             	float moveAmount = (float) (currentSpeed * dt);
                if(CollisionMethods.tileCheck(hitbox.x+moveAmount, hitbox.y, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x+moveAmount, hitbox.y, hitbox.width, hitbox.height)) {
                    hitbox.x += moveAmount;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x+moveAmount, hitbox.y, hitbox.width, hitbox.height)) {
                	
                } else {
                    hitbox.x = CollisionMethods.getWallPos(hitbox.x + hitbox.width - 1, gp) - (hitbox.width- gp.tileSize);
                }
            }
            if (up) {
            	direction = 3;
            	normaliseSpeed();
             	float moveAmount = (float) (currentSpeed * dt);
                if(CollisionMethods.tileCheck(hitbox.x, hitbox.y-moveAmount, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x, hitbox.y-moveAmount, hitbox.width, hitbox.height)) {
                    hitbox.y -= moveAmount;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x, hitbox.y-moveAmount, hitbox.width, hitbox.height)) {

                } else {
                    hitbox.y = CollisionMethods.getFloorPos(hitbox.y, gp);
                }
            } else if (down) {
            	direction = 2;
            	normaliseSpeed();
            	float moveAmount = (float) (currentSpeed * dt);
                if(CollisionMethods.tileCheck(hitbox.x, hitbox.y + moveAmount, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x, hitbox.y + moveAmount, hitbox.width, hitbox.height)) {
                    hitbox.y += moveAmount;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x, hitbox.y+moveAmount, hitbox.width, hitbox.height)) {
                	
                } else {
                    hitbox.y = CollisionMethods.getFloorPos(hitbox.y + hitbox.height - 1, gp) - (hitbox.height- gp.tileSize);
                }
            }
            
            if(!gp.world.isPowerOn()) {
            	gp.lightingM.moveLight(playerLight, (int)(hitbox.x + hitbox.width/2), (int)(hitbox.y +  hitbox.height/2));
            }

            	
            if(gp.multiplayer) {
	            Packet02Move packet = new Packet02Move(getUsername(), (int)hitbox.x, (int)hitbox.y, currentAnimation, direction);
	            packet.writeData(gp.socketClient);
            }
    }
    
    public void sendPacket(Packet packet) {
    	packet.writeData(gp.socketClient);
    }
    private void handleDebugMode() {
    	if(keyI.isKeyPressed(GLFW.GLFW_KEY_EQUAL)) {
    		int mouseX = (int)gp.mouseL.getWorldX();
    		int mouseY = (int)gp.mouseL.getWorldY();
    		int tileX = (int) ((mouseX) / gp.tileSize);
    		int tileY = (int) ((mouseY) / gp.tileSize);

    		System.out.println(tileX + "   " + tileY);    	
    	}
    }
    public void takeDamage(int damage) {
    	
    	if(invincibilityCounter > 0) {
    		return;
    	}
    	
    	invincibilityCounter = 10;
    	currentHealth -= damage;
    	if(currentHealth <= 0) {
    		death();
    	}
    	
    }
    private void death() {
        gp.currentState = gp.gameOverState;
        alive = false;
    }
    private void updateCounters(double dt) {
        if (invincibilityCounter > 0) {
            invincibilityCounter -= dt;  // subtract elapsed time
            if (invincibilityCounter < 0) invincibilityCounter = 0; // clamp to 0
        }
        if (clickCounter > 0) {
            clickCounter -= dt;
            if (clickCounter < 0) clickCounter = 0;
        }
    }
    public void leftClick(int x, int y) {
	    
    }
    public void rightClick(int x, int y) {
    	
    }
    private void handleItems(double dt) {
    	if(currentItem != null) {
    		currentItem.update(dt);
	    	if(keyI.isKeyPressed(GLFW.GLFW_KEY_E) && clickCounter == 0) {
	    		FloorDecor_Building b = gp.buildingM.findTable(interactHitbox.x, interactHitbox.y, interactHitbox.width, interactHitbox.height);
	    		if(b != null) {
		    		if(gp.buildingM.intersectsKitchenBuilding(gp, b, b.interactHitbox)) {
			    		FloorDecor_Building table = b;
		    			if(table.currentItem == null) {
					    	if(gp.multiplayer) {
			    				if(currentItem instanceof Plate p) {
			    					Packet22PlacePlate packet = new Packet22PlacePlate(getUsername(), p, b.getArrayCounter());
								    packet.writeData(gp.socketClient);
			    				} else {
				    				int state = 0;
					    			if(currentItem instanceof Food f) {
					    				state = f.getState();
					    			}
							        Packet04PlaceItem packet = new Packet04PlaceItem((int)currentItem.hitbox.x, (int)currentItem.hitbox.y, currentItem.getName(), username, state, table.getArrayCounter());
							        packet.writeData(gp.socketClient);
							    }
		    				}
				    		table.currentItem = currentItem;
				    		currentItem = null;
					    	clickCounter = 0.1;
					    	return;
		    			} else {
		    				if(currentItem.getName().equals("Plate")) {
		    					if(table.currentItem instanceof Food food) {
			    					Plate plate = (Plate)currentItem;
			    					if(plate.canBePlated(food.getName(), food.foodState)) {
			    						food.foodState = FoodState.PLATED;
			    						plate.addIngredient(food);
					    				table.currentItem = null;
					    				currentItem = plate;
					    				clickCounter = 0.1;
					    				int num = table.getArrayCounter();
					    				if(gp.multiplayer) {
					    	                Packet19AddFoodToPlateInHand packet = new Packet19AddFoodToPlateInHand(getUsername(),food.getName(),food.getState(), num);
					    	                packet.writeData(gp.socketClient);
					    	            }
			    					}
		    					}
		    				} else if(currentItem instanceof Food food) {
		    					if(table.currentItem instanceof Plate plate) {
			    					if(plate.canBePlated(food.getName(), food.foodState)) {
			    						food.foodState = FoodState.PLATED;
			    						plate.addIngredient(food);
					    				currentItem = null;
					    				table.currentItem = plate;
					    				clickCounter = 0.1;
					    				if(gp.multiplayer) {
					    	                Packet20AddFoodToPlateOnTable packet = new Packet20AddFoodToPlateOnTable(
						    	                	getUsername(),
						    	                    table.getArrayCounter(),
						    	                    food.getName(),
						    	                    food.getState());
					    	                packet.writeData(gp.socketClient);
					    	            }
			    					}
		    					} else if(table.currentItem instanceof CookingItem cookingItem) {
		    						if(cookingItem.canCook(gp.player.currentItem.getName()) && cookingItem.cookingItem == null) {
		    							cookingItem.setCooking(gp.player.currentItem);
		    							gp.player.currentItem = null;
		    							clickCounter = 0.1;
		    						}
		    					}
		    				}
		    			}
		    		}
	    		}
	    	}
    	} else if(currentItem == null){
    		if(keyI.isKeyPressed(GLFW.GLFW_KEY_E) && clickCounter == 0) {
	    		FloorDecor_Building b = gp.buildingM.findTable(interactHitbox.x, interactHitbox.y, interactHitbox.width, interactHitbox.height);
	    		if(b != null) {
		    		if(gp.buildingM.intersectsKitchenBuilding(gp, b, interactHitbox)) {
		    			if(b.currentItem != null) {
			    			if(b.currentItem instanceof Plate plate) {
				    			if(plate.getCurrentStackCount() > 1) {
					    			plate.decreasePlateStack();
					    			Plate p = new Plate(gp, 0, 0);
					    			p.setCurrentStackCount(1);
					    			currentItem = p;
					    			clickCounter = 0.1;
					    			resetAnimation(4);
				    			} else {
				    				currentItem = b.currentItem;
						    		clickCounter = 0.1;
						    		resetAnimation(4);
						    		sendPickupPacket(b);
						    		b.currentItem = null;
				    			}
			    			} else {
			    				currentItem = b.currentItem;
					    		clickCounter = 0.1;
					    		resetAnimation(4);
					    		sendPickupPacket(b);
					    		b.currentItem = null;
			    			}
		    			} 
		    		}
		    	}
    		}
    	}
    }
    private void sendPickupPacket(Building b) {
    	if(gp.multiplayer) {
			if(currentItem instanceof Plate p) {
				Packet21PickupPlate packet = new Packet21PickupPlate(getUsername(), p, b.getArrayCounter());
	            packet.writeData(gp.socketClient);
			} else {
    			int state = 0;
    			if(currentItem instanceof Food f) {
    				state = f.getState();
    			}
	            Packet07PickUpItemFromTable packet = new Packet07PickUpItemFromTable(currentItem.getName(), username, state, b.getArrayCounter());
	            packet.writeData(gp.socketClient);
			}
        }
    }
    
    public void updateInteractHitbox() {
        float baseX = hitbox.x;
        float baseY = hitbox.y;

        float horizontalWidth = 32;
        float horizontalHeight = 16;

        float verticalWidth = 16;
        float verticalHeight = 32;

        switch (direction) {
            case 0: // RIGHT
                interactHitbox = new Rectangle2D.Float(
                    baseX + hitbox.width,
                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
                    horizontalWidth,
                    horizontalHeight
                );
                break;
            case 1: // LEFT
                interactHitbox = new Rectangle2D.Float(
                    baseX - horizontalWidth,
                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
                    horizontalWidth,
                    horizontalHeight
                );
                break;
            case 2: // DOWN
                interactHitbox = new Rectangle2D.Float(
                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
                    baseY + hitbox.height,
                    verticalWidth,
                    verticalHeight
                );
                break;
            case 3: // UP
                interactHitbox = new Rectangle2D.Float(
                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
                    baseY - verticalHeight,
                    verticalWidth,
                    verticalHeight
                );
                break;
        }
    }
    public void setControlEnabled(boolean isEnabled) {
    	controlEnabled = isEnabled;
    }
    public void update(double dt) {
    	if(!gp.minigameM.miniGameActive && controlEnabled) {
    		handleMovement(dt);
    		updateCounters(dt);
    		updateInteractHitbox();
    		handleItems(dt);
    		
    		if (gp.mapM.currentRoom.darkerRoom) {
    		    if (currentAnimation == 1) {
    		        dustCooldown -= dt; // subtract elapsed time
    		        if (dustCooldown <= 0) {
    		            spawnTrailDust();
    		            dustCooldown = 0.033; // reset cooldown to ~2 frames at 60FPS
    		        }
    		    }
    		}
    	}
    	
    	handleDebugMode();
    	
    	animationSpeed+=dt; //Updating animation frame
        if (animationSpeed >= animationSpeedFactor) {
        	animationSpeed = 0;
            animationCounter++;
        }
        if (animations[direction][currentAnimation][animationCounter] == null) { //If the next frame is empty
            animationCounter = 0;
            if(currentAnimation == 4) {
            	currentAnimation = 2;
            }
        }
    }
    private void spawnTrailDust() {
        float spawnX = hitbox.x + hitbox.width / 2f;
        float spawnY = hitbox.y + hitbox.height; // near the player’s feet

        gp.particleM.addParticle(new PlayerDustParticle(gp, spawnX, spawnY));
    }
    
    public void setCurrentAnimation(int currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public void drawCurrentItem(Renderer renderer) {
    	if(currentItem == null) {
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
    	
        TextureRegion frame = currentItem.animations[0][0][0];
    	if(currentItem instanceof Food) {
    		Food f = (Food)currentItem;
    		frame = f.getImage();
    	}
    	switch(direction) {
    	case 0:
    		//xOffset += 14;
    		break;
    	case 1:
    		//xOffset -= 14;
    		frame = createHorizontalFlipped(frame);
    		break;
    	case 2:
    		//yOffset +=8;
    		break;
    	case 3:
    		//yOffset -= 20;
    		break;
    	}
    	if(currentItem instanceof Plate plate) {
    		boolean flip = direction == 1;
    		plate.drawInHand(renderer, (int)(hitbox.x - xDrawOffset + xOffset), (int)(hitbox.y - yDrawOffset + yOffset), flip);
    	} else {
		    renderer.draw(frame, (int)(hitbox.x - xDrawOffset + xOffset), (int)(hitbox.y - yDrawOffset + yOffset), (int)(48), (int)(48));
    	}
    }
    public void setUsername(String newName) {
    	username = newName;
    }
    
    public void draw(Renderer renderer) {
    	if(isInvisible) {
    		return;
    	}
    	if(direction == 3) {
    		drawCurrentItem(renderer);
    	}

        TextureRegion frame = animations[direction][currentAnimation][animationCounter];
        //The image is flipped
        if(direction == 1) {
        	frame = createHorizontalFlipped(frame);
        }	    
     

		if (frame != null) {
		    renderer.draw(frame, hitbox.x - xDrawOffset,hitbox.y - yDrawOffset,drawWidth, drawHeight);
		}     
        if(direction != 3) {
    		drawCurrentItem(renderer);
    	}	 
        
        
        //Draw username
        if(gp.multiplayer) {
        	if(Settings.showUsernames) {
		        if(username != null) {
		            renderer.setColour(usernameColor);
		            renderer.setFont(usernameFont);
		
		            //Get centered text
		            int x;
		            int length = (int)usernameFont.getTextWidth(username);
		            x = (int)(hitbox.x + (hitbox.width/2)) - length/2;
		
		            renderer.drawString(username, x , hitbox.y  - 20);
		        }
        	}
        }
        
        
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
        
        if(gp.keyL.showHitboxes) {
            renderer.setColour(Colour.RED);
            renderer.fillRect((int) hitbox.x , (int) (hitbox.y) , (int) hitbox.width, (int) hitbox.height);
            renderer.setColour(Colour.BLUE);
            gp.buildingM.drawHitboxes(renderer);
            renderer.setColour(Colour.YELLOW);
            gp.itemM.drawItemHitboxes(renderer);
            renderer.setColour(Colour.GREEN);
            gp.npcM.drawNPCHitboxes(renderer);
        }
        
        
        if (keyI.debugMode) {
        	//handleDebugMode(xDiff, yDiff);
        }
    }

}

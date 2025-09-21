package entity;

import main.GamePanel;
import map.LightSource;
import net.packets.Packet;
import net.packets.Packet02Move;
import net.packets.Packet03PickupItem;
import net.packets.Packet04PlaceItem;
import net.packets.Packet07PickUpItemFromTable;
import net.packets.Packet19AddFoodToPlateInHand;
import net.packets.Packet20AddFoodToPlateOnTable;
import net.packets.Packet21PickupPlate;
import net.packets.Packet22PlacePlate;
import utility.Animator;
import utility.CollisionMethods;
import utility.KeyboardInput;
import utility.MouseInput;
import utility.Settings;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Building;
import entity.buildings.CornerTable;
import entity.buildings.FloorDecor_Building;
import entity.items.Cheese;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.Item;
import entity.items.Plate;

public class Player extends Entity{
	
    //INPUTS
    public KeyboardInput keyI;
    public MouseInput mouseI;
    //USERNAME
    private String username;
    private Font usernameFont;
    private Color usernameColor;
    
    //ARMOUR
    private Animator headgearAnimator;
    private Animator chestplateAnimator;
    private Animator trouserAnimator;
    private BufferedImage[][][] headgearImages;
    private BufferedImage[][][] chestplateImages;
    private BufferedImage[][][] trouserImages;
    private BufferedImage[][][] handImages;
    private BufferedImage litHandImage, litHeadgearImage, litChestplateImage, litTrouserImage;

    //GENERAL VARIABLES
    private BufferedImage[][][] normalImages; 
    private boolean firstUpdate = true;
    private float speed; //How many pixels per update the player moves at
    private final float initialSpeed;
    private float currentSpeed; //How many pixels per update the player moves at
    public float reachDistance; //Distance the player can reach
    public float talkDistance; //Distance the player can talk
    public float buildRange;
    public boolean facingLeft = false;
    public Item currentItem = null;
    private int clickCounter = 0;
    public Rectangle2D.Float interactHitbox;
    public int wealth = 0;
    //HEALTH
    public boolean alive = true;
    public int maxHealth = 3;
    public int currentHealth = maxHealth;
    private int invincibilityCounter = 0;

    //SCREEN SCROLL SETTINGS
    public int xDiff; //The difference in the drawn background and the actual position
    public int yDiff; //The difference in the drawn background and the actual position
    private final double topBorder; //A border at the top of the screen, when the player moves past this the screen scrolls
    private final double bottomBorder; //A border at the bottom of the screen, when the player moves past this the screen scrolls
    private final double leftBorder; //A border on the left of the screen, when the player moves past this the screen scrolls
    private final double rightBorder; //A border on the right of the screen, when the player moves past this the screen scrolls
    private int maxXDiffValue;
    private int maxYDiffValue;
    public int currentLayer = 0;
    
    public int currentRoomIndex = 0;
    
    private LightSource playerLight;
    
    //INVENTORY
    //public Headgear currentHeadgear = null;
    //public Chestplate currentChestplate = null;
    //public Trousers currentTrousers = null;

    public Player(GamePanel gp, int xPos, int yPos, KeyboardInput keyI, MouseInput mouseI, String username) { //Setting default variables
        super(gp, (xPos), (yPos), 32, 32);
        
        this.keyI = keyI;
        this.mouseI = mouseI;
        this.username = username;
        
        usernameFont = new Font("monogram", Font.PLAIN, 30);
        usernameColor = new Color(213, 213, 213);

        initialSpeed = 5;
        speed = initialSpeed;
        currentSpeed = speed;
       
        maxXDiffValue = (gp.mapM.currentRoom.mapWidth * gp.tileSize) - gp.frameWidth; //change to worldsize
        maxYDiffValue = (gp.mapM.currentRoom.mapHeight * gp.tileSize) - gp.frameHeight;

        topBorder = gp.frameHeight * 0.45;
        bottomBorder = gp.frameHeight * 0.55;
        leftBorder = gp.frameWidth * 0.45;
        rightBorder = gp.frameWidth * 0.55;
        
        interactHitbox = new Rectangle2D.Float(0, 0, 1, 1);
        
        setUp();
    }
    
    private void setUp() {
    	//Point2D spawnPoint = gp.mapM.findSpawnPosition();

        //hitbox.x = (float) spawnPoint.getX();
        //hitbox.y = (float) spawnPoint.getY();
        
        animationSpeedFactor = 3;
        
        reachDistance = (3*gp.tileSize);
        talkDistance = (5*gp.tileSize);
        buildRange = (5f*gp.tileSize);
        
        drawWidth = 80*drawScale;
        drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
        
        xDiff = 0;
        yDiff = 0;
        
        playerLight = new LightSource((int)hitbox.x, (int)hitbox.y, Color.GREEN, 100);
        gp.lightingM.addLight(playerLight);
        
        importImages();
    }
    
    public void setSpawnPoint(int xPos, int yPos) {
    	hitbox.x = xPos;
    	hitbox.y = yPos;
    	xDiff = xPos;
    	yDiff = yPos;
    }
    
    public int getCurrentAnimation() {
    	return currentAnimation;
    }
    public int getAnimationCounter() {
    	return animationCounter;
    }
    public void slowSpeed() {
    	this.speed = 1;
    }
    public void setNormalSpeed() {
    	this.speed = initialSpeed;
    }
    public void reCalculateMaxDiffVals() {
    	maxXDiffValue = (gp.mapM.currentRoom.mapWidth * gp.tileSize) - gp.frameWidth;
        maxYDiffValue = (gp.mapM.currentRoom.mapHeight * gp.tileSize) - gp.frameHeight;
    }
    public void setDiffValues() {
        // Center camera on player
    	int playerX = (int)hitbox.x;
    	int playerY = (int)hitbox.y;
        xDiff = playerX - gp.frameWidth / 2;
        yDiff = playerY - gp.frameHeight / 2;

        // Clamp so we don't scroll past map edges
        int maxX = gp.mapM.currentMapWidth * gp.tileSize - gp.frameWidth;
        int maxY = gp.mapM.currentMapHeight * gp.tileSize - gp.frameHeight;

        if (xDiff < 0) xDiff = 0;
        if (yDiff < 0) yDiff = 0;
        if (xDiff > maxX) xDiff = maxX;
        if (yDiff > maxY) yDiff = maxY;
    }
    public void importImages() {

        animations = new BufferedImage[4][20][15];
        normalImages = new BufferedImage[4][20][15];
        handImages = new BufferedImage[4][20][15];
        headgearImages = new BufferedImage[4][20][15];
        chestplateImages = new BufferedImage[4][20][15];
        trouserImages = new BufferedImage[4][20][15];
        
        importPlayerSpriteSheet("/player/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
        importPlayerSpriteSheet("/player/run", 8, 1, 1, 0, 0, 80, 80); //RUN
        
        importPlayerSpriteSheet("/player/holdingIdle", 4, 1, 2, 0, 0, 80, 80); //HOLDING IDLE
        importPlayerSpriteSheet("/player/holdingRun", 8, 1, 3, 0, 0, 80, 80); //HOLDING RUN

        importPlayerSpriteSheet("/player/pickup", 6, 1, 4, 0, 0, 80, 80); //Pick Up

        
        //headgearAnimator = new Animator(0);
        //chestplateAnimator = new Animator(1);
        //trouserAnimator = new Animator(2);
        
        currentAnimation = 0;
    }
    
    protected void importPlayerSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {

    	for(int k = 0; k < 4; k++) {
	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	        BufferedImage normalImage = importImage(filePath + "Normal.png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[k][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                normalImages[k][currentAnimation][arrayIndex] = normalImage.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }
	        if(k > 0) {
	        	startY += height;
	        }
    	}

    }
    private void importSingleRowSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {
    	for(int k = 0; k < 4; k++) {
	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	        BufferedImage handImage = importImage(filePath + "Hand.png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[k][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                handImages[k][currentAnimation][arrayIndex] = handImage.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }
    	}
    }
    
    public String getUsername() {
        return username;
    }
    
    private void normaliseSpeed() {
    	if(direction == 0 || direction == 1) {
    		if(gp.keyI.up || gp.keyI.down) { //NORMALISING SPEED
        		currentSpeed = (float)(speed/(Math.sqrt(2)));
        	} else {
        		currentSpeed = speed;
        	}
    	} else if(direction == 2 || direction == 3) {
    		if(gp.keyI.left || gp.keyI.right) { //NORMALISING SPEED
        		currentSpeed = (float)(speed/(Math.sqrt(2)));
        	} else {
        		currentSpeed = speed;
        	}
    	}
    }

    private void handleMovement() {

    		if (!(keyI.left || keyI.right || keyI.up || keyI.down) && currentAnimation != 4) {
                currentAnimation = 0;
                if(currentItem != null) {
                	currentAnimation = 2;
                }
            }

            if (keyI.left) { //Moving left
            	direction = 1;
            	facingLeft = true;
            	normaliseSpeed();
                if(CollisionMethods.tileCheck(hitbox.x-currentSpeed, hitbox.y, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x-currentSpeed, hitbox.y, hitbox.width, hitbox.height)) {
                    hitbox.x -= currentSpeed;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x-currentSpeed, hitbox.y, hitbox.width, hitbox.height)) {

                } else {
                    hitbox.x = CollisionMethods.getWallPos(hitbox.x, gp);
                }
            } else if (keyI.right) { //Moving right
                direction = 0;
                facingLeft = false;
                normaliseSpeed();
                if(CollisionMethods.tileCheck(hitbox.x+currentSpeed, hitbox.y, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x+currentSpeed, hitbox.y, hitbox.width, hitbox.height)) {
                    hitbox.x += currentSpeed;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x+currentSpeed, hitbox.y, hitbox.width, hitbox.height)) {
                	
                } else {
                    hitbox.x = CollisionMethods.getWallPos(hitbox.x + hitbox.width - 1, gp) - (hitbox.width- gp.tileSize);
                }
            }
            if (keyI.up) {
            	direction = 3;
            	normaliseSpeed();
                if(CollisionMethods.tileCheck(hitbox.x, hitbox.y-currentSpeed, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x, hitbox.y-currentSpeed, hitbox.width, hitbox.height)) {
                    hitbox.y -= currentSpeed;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x, hitbox.y-currentSpeed, hitbox.width, hitbox.height)) {

                } else {
                    hitbox.y = CollisionMethods.getFloorPos(hitbox.y, gp);
                }
            } else if (keyI.down) {
            	direction = 2;
            	normaliseSpeed();
                if(CollisionMethods.tileCheck(hitbox.x, hitbox.y + currentSpeed, hitbox.width, hitbox.height, gp) && gp.buildingM.entityCheck(hitbox.x, hitbox.y + currentSpeed, hitbox.width, hitbox.height)) {
                    hitbox.y += currentSpeed;
                    if(currentAnimation != 4) {
	                    currentAnimation = 1;
	                    if(currentItem != null) {
	                    	currentAnimation = 3;
	                    }
                    }
                } else if(!gp.buildingM.entityCheck(hitbox.x, hitbox.y+currentSpeed, hitbox.width, hitbox.height)) {

                } else {
                    hitbox.y = CollisionMethods.getFloorPos(hitbox.y + hitbox.height - 1, gp) - (hitbox.height- gp.tileSize);
                }
            }
            
            gp.lightingM.moveLight(playerLight, (int)(hitbox.x + hitbox.width/2), (int)(hitbox.y +  hitbox.height/2));
            
            if(gp.multiplayer) {
	            Packet02Move packet = new Packet02Move(getUsername(), (int)hitbox.x, (int)hitbox.y, currentAnimation, direction);
	            packet.writeData(gp.socketClient);
            }

    }
    
    public void sendPacket(Packet packet) {
    	packet.writeData(gp.socketClient);
    }
    private void handleDebugMode() {
        System.out.println((int)((mouseI.mouseX + xDiff) / gp.tileSize) + "   " + (int)((mouseI.mouseY + yDiff) / gp.tileSize));
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
    private void checkBorders() {
        float scrollSpeed = currentSpeed+0.1f; //Number of pixels for which the diff value is changed by each update
        //CHECKS IF PLAYER IS CLOSE ENOUGH TO TOP FOR SCREEN TO MOVE
        if ((hitbox.y - yDiff) <= topBorder) {
            yDiff -= scrollSpeed;
        }
        //TOP OF THE MAP
        if (yDiff <= 0) {
            yDiff = 0;
        }
        //CHECKS IF PLAYER IS CLOSE ENOUGH TO BOTTOM FOR SCREEN TO MOVE
        if ((hitbox.y - yDiff) >= bottomBorder) {
            yDiff += scrollSpeed;
        }
        //BOTTOM OF THE MAP
        if (yDiff >= maxYDiffValue) {
            yDiff = maxYDiffValue;
        }

        if ((hitbox.x - xDiff) <= leftBorder) {
            xDiff -= scrollSpeed;
        }
        //TOP OF THE MAP
        if (xDiff <= 0) {
            xDiff = 0;
        }
        //CHECKS IF PLAYER IS CLOSE ENOUGH TO BOTTOM FOR SCREEN TO MOVE
        if ((hitbox.x - xDiff) >= rightBorder) {
            xDiff += scrollSpeed;
        }
        //BOTTOM OF THE MAP
        if (xDiff >= maxXDiffValue) {
            xDiff = maxXDiffValue;
        }

    }
    private void updateCounters() {
        if(invincibilityCounter>0) {
        	invincibilityCounter--;
        }
        if(clickCounter>0) {
        	clickCounter--;
        }
    }
    public void leftClick(int x, int y) {
	    
    }
    public void rightClick(int x, int y) {
    	
    }
  
    private void handleItems() {
    	if(currentItem != null) {
	    	if(gp.keyI.ePressed && clickCounter == 0) {
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
					    	clickCounter = 8;
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
					    				clickCounter = 8;
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
					    				clickCounter = 8;
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
		    							clickCounter = 8;
		    						}
		    					}
		    				}
		    			}
		    		}
	    		}
	    	}
    	} else if(currentItem == null){
    		if(gp.keyI.ePressed && clickCounter == 0) {
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
					    			clickCounter = 8;
					    			resetAnimation(4);
				    			} else {
				    				currentItem = b.currentItem;
						    		clickCounter = 8;
						    		resetAnimation(4);
						    		sendPickupPacket(b);
						    		b.currentItem = null;
				    			}
			    			} else {
			    				currentItem = b.currentItem;
					    		clickCounter = 8;
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
    public void update() {

        handleMovement();
        checkBorders();
        updateCounters();
        updateInteractHitbox();
        handleItems();
        
        if (keyI.debugMode) {
        	handleDebugMode();
        }
    }
    
    public void setCurrentAnimation(int currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public void drawCurrentItem(Graphics2D g2) {
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
    	
    	BufferedImage img = currentItem.animations[0][0][0];
    	if(currentItem instanceof Food) {
    		Food f = (Food)currentItem;
    		img = f.getImage();
    	}
    	switch(direction) {
    	case 0:
    		//xOffset += 14;
    		break;
    	case 1:
    		//xOffset -= 14;
    		img = createHorizontalFlipped(img);
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
    		plate.drawInHand(g2, (int)(hitbox.x - xDiff - xDrawOffset + xOffset), (int)(hitbox.y - yDiff - yDrawOffset + yOffset), flip);
    	} else {
    		g2.drawImage(img, (int)(hitbox.x - xDiff - xDrawOffset + xOffset), (int)(hitbox.y - yDiff - yDrawOffset + yOffset), (int)(48), (int)(48), null);
    	}
    }
    public void setUsername(String newName) {
    	username = newName;
    }
    public void draw(Graphics2D g2) {
    	if(direction == 3) {
    		drawCurrentItem(g2);
    	}

        animationSpeed++; //Updating animation frame
        if (animationSpeed == animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
            litHandImage = null;
            litHeadgearImage = null;
            litChestplateImage = null;
            litTrouserImage = null;
        }

        if (animations[direction][currentAnimation][animationCounter] == null) { //If the next frame is empty
            animationCounter = 0;
            if(currentAnimation == 4) {
            	currentAnimation = 2;
            }
        }
        //The image is flipped
        BufferedImage img = animations[direction][currentAnimation][animationCounter];
        //BufferedImage handImg = handImages[direction][currentAnimation][animationCounter];
        if(direction == 1) {
        	img = createHorizontalFlipped(img);
        	//handImg = createHorizontalFlipped(handImg);
        }
    	g2.drawImage(img, (int)(hitbox.x - xDiff - xDrawOffset), (int)(hitbox.y - yDiff - yDrawOffset), (int)(drawWidth), (int)(drawHeight), null);
	    
        if(direction != 3) {
    		drawCurrentItem(g2);
    	}	 
	    //g2.drawImage(handImg, (int)(hitbox.x - xDiff - xDrawOffset), (int)(hitbox.y - yDiff - yDrawOffset), (int)(drawWidth), (int)(drawHeight), null);    
        //Draw username
        if(gp.multiplayer) {
        	if(Settings.showUsernames) {
		        if(username != null) {
		            g2.setColor(usernameColor);
		            g2.setFont(usernameFont);
		
		            //Get centered text
		            int x;
		            int length = (int)g2.getFontMetrics().getStringBounds(username, g2).getWidth();
		            x = (int)(hitbox.x + (hitbox.width/2)) - length/2;
		
		            g2.drawString(username, x - xDiff, hitbox.y - yDiff - 20);
		        }
        	}
        }
        
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
        
        if(gp.keyI.showHitboxes) {
            g2.setColor(Color.RED);
            g2.drawRect((int) hitbox.x - xDiff, (int) (hitbox.y) - yDiff, (int) hitbox.width, (int) hitbox.height);
            g2.setColor(Color.BLUE);
            gp.buildingM.drawHitboxes(g2, xDiff, yDiff);
            g2.setColor(Color.YELLOW);
            gp.itemM.drawItemHitboxes(g2, xDiff, yDiff);
            g2.setColor(Color.GREEN);
            gp.npcM.drawNPCHitboxes(g2);
        }
    }

}

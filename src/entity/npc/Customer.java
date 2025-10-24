package entity.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Chair;
import entity.buildings.Door;
import entity.buildings.Toilet;
import entity.items.FoodState;
import entity.items.Plate;
import main.GamePanel;
import utility.Recipe;
import utility.RecipeManager;
import utility.RoomHelperMethods;
import utility.RoomHelperMethods.*;

public class Customer extends NPC {
	
	private int type;
	public boolean atTable = false;
	private boolean ordered = false;
	protected boolean eating = false;
	private boolean leaving = false;
	public boolean waitingToOrder = false;
	private boolean goingToToilet = false;
	private boolean inToilet = false;
	private boolean onToilet = false;
	private Chair currentChair = null;
	private Toilet toilet = null;
	public Recipe foodOrder = null;
	private int eatTime = 60*5;
	private int eatCounter = 0;
	protected BufferedImage orderSign, warningOrderSign;
	private int orderTime = 0;
	private int maxOrderTime = 210;
	private int toiletTime = 0;
	private int maxToiletTime = 60*9;
	
	protected int patienceCounter = 0;
	protected int baseMaxPatienceTime = 60*120; // 2mins
	protected int maxPatienceTime = baseMaxPatienceTime;
	protected int extendedMaxPatienceTime = 60*240; //4mins
	protected int patienceFactor = 1;
	private boolean unhappy = false;
	private int flickerCounter = 0;
	private int flickerSpeed = 30; // frames per toggle
	protected boolean celebrityPresent = false;
	public boolean hideOrder = false;
	protected boolean isGhost = false;

	// Tip multipliers
	protected float greenTipMultiplier = 1.0f;
	protected float orangeTipMultiplier = 0.5f;
	protected float redTipMultiplier = 0f;
	
	public BufferedImage faceIcon;
	private Graphics2D g2;
	
	private Pet pet;

	public Customer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 5;
		drawScale = 3;
		drawWidth = 80*drawScale;
	    drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
		speed = 1;
		npcType = "Customer";
		
		type = r.nextInt(7);
		
		talkHitbox = new Rectangle2D.Float(hitbox.x - 16, hitbox.y - 16, hitbox.width + 32, hitbox.height + 32);
		
		if(RoomHelperMethods.isCelebrityPresent(gp.mapM.getRoom(0).getNPCs())) {
			celebrityPresent = true;
		}
		
		importImages();
	}
	
	private void importImages() {
		animations = new BufferedImage[5][10][10];
		animations[0][0][0] = importImage("/npcs/mannequin.png").getSubimage(16, 0, 16, 32);
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		name = "Customer";
		
		switch(type) {
		case 0:
	        importPlayerSpriteSheet("/npcs/angler/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/angler/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 1:
	        importPlayerSpriteSheet("/npcs/blacksmith/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/blacksmith/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 2:
	        importPlayerSpriteSheet("/npcs/farmer/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/farmer/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 3:
	        importPlayerSpriteSheet("/npcs/florist/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/florist/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 4:
	        importPlayerSpriteSheet("/npcs/innKeeper/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/innKeeper/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 5:
	        importPlayerSpriteSheet("/npcs/merchant/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/merchant/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 6:
	        importPlayerSpriteSheet("/npcs/miner/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/miner/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		}
		
		//PET
		boolean addPet = r.nextInt(10) == 0;
		//addPet = true;
		if(addPet) {
			int petType = r.nextInt(5);
			pet = new Pet(gp, hitbox.x, hitbox.y, this, petType);
			if(gp.player.currentRoomIndex == currentRoomNum) {
				gp.npcM.addNPC(pet);
			} else {
				gp.mapM.getRoom(currentRoomNum).addNPC(pet);
			}
		}
		
	}
	
	protected void findTable() {
		if(gp.mapM.currentRoom.equals(gp.mapM.getRoom(currentRoomNum))) {
			currentChair = gp.buildingM.findFreeChair();
		} else {
			currentChair = gp.mapM.getRoom(currentRoomNum).findFreeChair();
		}
		if(currentChair != null) {
			currentChair.setCustomer(this);
			walking = true;
		}
    }
	protected void findToilet() {
		if(gp.mapM.currentRoom.equals(gp.mapM.getRoom(currentRoomNum))) {
			toilet = gp.buildingM.findFreeToilet();
		} else {
			toilet = gp.mapM.getRoom(currentRoomNum).findFreeToilet();
		}
    }
	protected void removeLights() {
		
	}
	private void makeOrder() {
		if(isGhost) {
			foodOrder = RecipeManager.getRandomCursedRecipe();
		} else {
			foodOrder = gp.world.getRandomMenuRecipe();
		}
		
		if (gp.progressM.seasoningUnlocked) {
			if (r.nextFloat() < 0.2f) {
				String seasoning = "Basil";
				int num = r.nextInt(4);
				switch(num) {
				case 0:
					seasoning = "Basil";
					break;
				case 1:
					seasoning = "Sage";
					break;
				case 2:
					seasoning = "Rosemary";
					break;
				case 3:
					seasoning = "Thyme";
					break;
				}
				foodOrder.setSeasoned(seasoning);
		    }
		}
		
		RecipeManager.addOrder(foodOrder);
		ordered = true;
		gp.gui.addOrder(foodOrder, this, g2);
	}
	private void waitForOrder() {
		waitingToOrder = true;
	}
	public void completeOrder(Plate p) {
	    if(foodOrder == null) return;

	    int baseCost = foodOrder.getCost(gp.world.isRecipeSpecial(foodOrder));
	    // base payment
	    gp.player.wealth += baseCost;

	    // tip logic based on patience
	    float progress = patienceCounter / (float) maxPatienceTime;
	    int tip = 0;

	    if(progress <= 0.33f) { // green zone
	        tip = (int)(foodOrder.getCost(gp.world.isRecipeSpecial(foodOrder)) * greenTipMultiplier);
	    } else if(progress <= 0.66f) { // orange zone
	        tip = (int)(foodOrder.getCost(gp.world.isRecipeSpecial(foodOrder)) * orangeTipMultiplier);
	    } else { // red zone
	        tip = (int)(foodOrder.getCost(gp.world.isRecipeSpecial(foodOrder)) * redTipMultiplier);
	    }
	    
	    boolean addTip = true;
	    if(gp.world.animalPresent) {
	    	addTip = false;
	    }
	    if(gp.mapM.isInRoom(4)) {
    		if(gp.buildingM.hasBuildingWithName("Leak")) {
    			addTip = false;
    		}
    	} else {
       		if(gp.mapM.getRoom(4).hasBuildingWithName("Leak")) {
    			addTip = false;
    		}
    	}
	    
	    if(addTip && (gp.progressM.tipJarPresent || gp.progressM.bigTipsPresent)) {
	    	if(gp.progressM.bigTipsPresent) {
		    	gp.player.wealth += tip;
	    	} else {
	    		gp.player.wealth += (tip*0.5);
	    	}
	    }
	    
	    if (p.seasoningQuality != -1) {
	    	if(addTip) {
	    		gp.player.wealth += baseCost * 0.50f * p.seasoningQuality;
	    	}
	    }
	    
	    gp.player.soulsServed++;

	    // clean up
	    RecipeManager.removeOrder(foodOrder);
	    gp.player.currentItem = null;
	    eating = true;
	    waitingToOrder = false;
	}
	public boolean isEating() {
		return eating;
	}
	public void takeOrder() {
		orderTime++;
	    if (orderTime >= maxOrderTime) {
	    	makeOrder();
	        orderTime = 0;
	        waitingToOrder = false;
	    }
	}
	protected void leave() {
		super.leave();
		if(pet != null) {
			removeOtherNPC(pet);
		}
	}
	public void update() {
		if(gp.progressM.fasterCustomers) {
			speed = 2;
		}
	    if(!atTable) {
	        if(leaving) {
	        	if(inToilet) {
	        		if(walkToDoorWithDoorNum(RoomHelperMethods.MAIN)) {
				    	inToilet = false;
	        		}
	        	} else {
	        		leave();
	        	}
	        } else if(goingToToilet && !inToilet) {
	        	if(walkToDoorWithDoorNum(RoomHelperMethods.BATHROOM)) {
	            	inToilet = true;
	        	}
	        } else if(inToilet && !onToilet) {
	        	if(toilet == null) {
	        		findToilet();
	        	}
	        	if(walkToBuilding(toilet)) {
	        		onToilet = true;
			    	hitbox.x = toilet.hitbox.x;
			    	hitbox.y = toilet.hitbox.y;
	        	}
	        } else if(onToilet) {
	        	  toiletTime++;
		            if(toiletTime >= maxToiletTime) {
		            	toiletTime = 0;
		                onToilet = false;
		                toilet.available = true;
		                leaving = true;
		            }
	        } else {
	            if(!walking) {
	                findTable();
	            } else {
	            	if(walkToBuilding(currentChair)) {
	            		 walking = false;
		                 atTable = true;
		                 hitbox.x = currentChair.hitbox.x+16;
		                 hitbox.y = currentChair.hitbox.y;
		                 direction = "Right";
	            	}
	            }
	        }
	    } else {
	        // Waiting states
	        if(!ordered) {
	            if(!waitingToOrder) {
	                waitForOrder();
	            } else {
	                if(hitbox.intersects(gp.player.interactHitbox)) {
	                    if(gp.keyI.ePressed) {
	                        takeOrder();
	                    }
	                }
	            }
	        } 

	        // If they have ordered but haven’t been served yet
	        if((waitingToOrder && !ordered) || (ordered && !eating)) {
	            float patienceIncrement = patienceFactor;
	            if(gp.progressM.turntablePresent) {
	                patienceIncrement *= 0.8f; // 20% slower patience decrease
	            }
	            patienceCounter += patienceIncrement;
	        }

	        if(eating) {
	            eatCounter++;
	            if(eatCounter >= eatTime) {
	                eatCounter = 0;
	                atTable = false;
	                currentChair.available = true;
	                ordered = false;
	                eating = false;
	                goingToToilet = true;
	            }
	        }
	    }
	    
	    // Global patience timeout check
	    if(!eating && patienceCounter >= maxPatienceTime) {
	        // ran out of patience before getting food
	        leave();
	        RecipeManager.removeOrder(foodOrder);
	        currentChair.available = true;
	        unhappy = true;
	        waitingToOrder = false;
	    }
	    flickerCounter++;
	    if (flickerCounter >= flickerSpeed) {
	        flickerCounter = 0;
	    }
	      talkHitbox.x = hitbox.x - 16;
	      talkHitbox.y = hitbox.y - 16;
	}
	public void setCelebrityPresent(boolean isPresent) {
		celebrityPresent = isPresent;
		if(celebrityPresent) {
			maxPatienceTime = extendedMaxPatienceTime;
		} else {
			float fraction = (patienceCounter / maxPatienceTime);
			maxPatienceTime = baseMaxPatienceTime;
			patienceCounter = (int)(fraction * maxPatienceTime);
		}
	}
	public int getPatienceCounter() {
		return patienceCounter;
	}
	public int getMaxPatienceTime() {
		return maxPatienceTime;
	}
	private void drawOrderBar(Graphics2D g2, float worldX, float worldY, int orderTime, int maxOrderTime, int xDiff, int yDiff) {
	    float screenX = worldX - xOffset - xDiff;
	    float screenY = worldY - yOffset - yDiff;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, orderTime / (float) maxOrderTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    g2.setColor(Color.BLACK);
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight);
	    
	    g2.setColor(new Color(r, g, 0));
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight);

	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		this.g2 = g2;
		
	    if(walking && !atTable) {
	    	currentAnimation = 1;
	    } else {
	    	currentAnimation = 0;
	    }
	    
		  animationSpeed+=animationUpdateSpeed; //Update the animation frame
	      if(animationSpeed == animationSpeedFactor) {
	    	  animationSpeed = 0;
	          animationCounter++;
	      }
	      
	      if(animations != null) {
	    	  if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
	    		  animationCounter = 0; //Loops the animation
	    	  }
	    	  BufferedImage img = animations[0][currentAnimation][animationCounter];
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
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDrawOffset - yDiff), (int)(drawWidth), (int)(drawHeight), null);
	      }
	      
	      if(orderTime > 0) {
	    	  drawOrderBar(g2, hitbox.x, hitbox.y+8, orderTime, maxOrderTime, xDiff, yDiff);
	      }
  	      float patienceRatio = patienceCounter / (float) maxPatienceTime;
	      if(patienceRatio >= 0.5f) {
	    	  hideOrder = false;
	      }
	      
	      if (waitingToOrder) {

	    	    boolean lowPatience = patienceRatio >= 0.5f && !ordered;
	    	    BufferedImage currentSign = orderSign;

	    	    if (lowPatience) {
	    	        // alternate every flickerSpeed frames
	    	        if (flickerCounter < flickerSpeed / 2) {
	    	            currentSign = orderSign;
	    	        } else {
	    	            currentSign = warningOrderSign;
	    	        }
	    	    }

	    	    g2.drawImage(currentSign,(int)(hitbox.x - xOffset - xDiff),(int)(hitbox.y - yOffset - yDiff - 48), 48, 48, null);
	    	}
	      if(talking) {
	    	  //gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- xDiff, (int)hitbox.y - 48*3- yDiff, dialogues[dialogueIndex], this);
	      }
	      
	  }
	
}

package entity.npc;

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import entity.buildings.Chair;
import entity.buildings.Toilet;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.Recipe;
import utility.RecipeManager;
import utility.RoomHelperMethods;
import utility.Statistics;

public class Customer extends NPC {
	
	private int type;
	public boolean atTable = false;
	private boolean ordered = false;
	protected boolean eating = false;
	private boolean leaving = false;
	public boolean waitingToOrder = false;
	private boolean goingToToilet = false;
	private boolean finishedMeal = false;
	private boolean inToilet = false;
	private boolean onToilet = false;
	protected Chair currentChair = null;
	private Toilet toilet = null;
	public Recipe foodOrder = null;
	private double eatTime = 5;
	private double eatCounter = 0;
	protected TextureRegion orderSign, warningOrderSign;
	private double orderTime = 0;
	private double maxOrderTime = 3.6;
	private double toiletTime = 0;
	private double maxToiletTime = 9;
	
	protected double patienceCounter = 0;
	protected double baseMaxPatienceTime = 120; // 2mins
	protected double maxPatienceTime = baseMaxPatienceTime;
	protected double extendedMaxPatienceTime = 240; //4mins
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
	
	public TextureRegion faceIcon;
	Renderer renderer;
	
	private TextureRegion[][] eatingAnimations;
	
	private Pet pet;

	public Customer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 0.1;
		drawScale = 3;
		drawWidth = 80*drawScale;
	    drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
		speed = 60;
		npcType = "Customer";
		
		type = r.nextInt(11);
		
		talkHitbox = new Rectangle2D.Float(hitbox.x - 16, hitbox.y - 16, hitbox.width + 32, hitbox.height + 32);
		
		if(RoomHelperMethods.isCelebrityPresent(gp.world.mapM.getRoom(0).getNPCs())) {
			celebrityPresent = true;
		}
		
		importImages();
	}
	
    protected void importEatingAnimation(String filePath, int columnNumber, int rowNumber, int startX, int startY, int width, int height, int direction) {

	        int arrayIndex = 0;
	
	        Texture img = importImage(filePath + ".png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	            	eatingAnimations[direction][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }

    }
    protected void importDirectionalSpriteSheet(
            String basePath,
            int startX,
            int startY,
            int width,
            int height
    ) {
        // Load textures once
        Texture sideSheet = importImage(basePath + "/Side.png");
        Texture downSheet = importImage(basePath + "/Down.png");
        Texture upSheet   = importImage(basePath + "/Up.png");

        // ---- SIDE → directions 0 & 1 ----
        for (int dir = 0; dir <= 1; dir++) {

            // IDLE
            for (int frame = 0; frame < 4; frame++) {
                animations[dir][0][frame] =
                    sideSheet.getSubimage(
                        startX + frame * width,
                        startY,
                        width,
                        height
                    );
            }

            // WALK
            for (int frame = 0; frame < 8; frame++) {
                animations[dir][1][frame] =
                    sideSheet.getSubimage(
                        startX + frame * width,
                        startY + height,
                        width,
                        height
                    );
            }
        }

        // ---- DOWN → direction 2 ----
        for (int frame = 0; frame < 4; frame++) {
            animations[2][0][frame] =
                downSheet.getSubimage(
                    startX + frame * width,
                    startY,
                    width,
                    height
                );
        }
        for (int frame = 0; frame < 8; frame++) {
            animations[2][1][frame] =
                downSheet.getSubimage(
                    startX + frame * width,
                    startY + height,
                    width,
                    height
                );
        }

        // ---- UP → direction 3 ----
        for (int frame = 0; frame < 4; frame++) {
            animations[3][0][frame] =
                upSheet.getSubimage(
                    startX + frame * width,
                    startY,
                    width,
                    height
                );
        }
        for (int frame = 0; frame < 8; frame++) {
            animations[3][1][frame] =
                upSheet.getSubimage(
                    startX + frame * width,
                    startY + height,
                    width,
                    height
                );
        }
    }
	private void importImages() {
		animations = new TextureRegion[5][10][10];
		animations[0][0][0] = importImage("/npcs/mannequin.png").getSubimage(16, 0, 16, 32);
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		name = "Customer";
		
		eatingAnimations = new TextureRegion[5][5];
		importEatingAnimation("/npcs/Eating", 4, 1, 0, 80, 80, 80, 0);
		importEatingAnimation("/npcs/Eating", 4, 1, 0, 0, 80, 80, 1);
		importEatingAnimation("/npcs/Eating", 4, 1, 0, 0, 80, 80, 2);
		importEatingAnimation("/npcs/Eating", 4, 1, 0, 160, 80, 80, 3);
		
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
		case 7:
	        importPlayerSpriteSheet("/npcs/customers/customer1/idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/customers/customer1/walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/customers/customer1/FaceIcon.png").toTextureRegion();
			break;
		case 8:
	        importDirectionalSpriteSheet("/npcs/customers/customer2", 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/customers/customer2/FaceIcon.png").toTextureRegion();
			break;
		case 9:
	        importDirectionalSpriteSheet("/npcs/customers/customer3", 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/customers/customer3/FaceIcon.png").toTextureRegion();
			break;
		case 10:
	        importDirectionalSpriteSheet("/npcs/customers/customer4", 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/customers/customer4/FaceIcon.png").toTextureRegion();
			break;
		}
		
		//PET
		boolean addPet = r.nextInt(10) == 0;
		//addPet = true;
		if(addPet) {
			int petType = r.nextInt(5);
			pet = new Pet(gp, hitbox.x, hitbox.y, this, petType);
			if(gp.player.currentRoomIndex == currentRoomNum) {
				gp.world.npcM.addNPC(pet);
			} else {
				gp.world.mapM.getRoom(currentRoomNum).addNPC(pet);
			}
		}
		
	}
	public List<Customer> getCustomersAtSameTable() {
	    if (currentChair == null || currentChair.table == null) {
	        return List.of(this); // fallback
	    }

	    return currentChair.table.getSeatedCustomers();
	}
	protected void findTable() {
		if(gp.world.mapM.currentRoom.equals(gp.world.mapM.getRoom(currentRoomNum))) {
			currentChair = gp.world.buildingM.findFreeChair();
		} else {
			currentChair = gp.world.mapM.getRoom(currentRoomNum).findFreeChair();
		}
		if(currentChair != null) {
			currentChair.setCustomer(this);
			walking = true;
		}
    }
	private boolean isWholeTableFinishedEating() {
	    for (Customer c : getCustomersAtSameTable()) {

	        // Someone still hasn’t ordered
	        if (!c.ordered) {
	            return false;
	        }

	        // Someone is still actively eating
	        if (c.eating) {
	            return false;
	        }

	        // Someone hasn’t finished their meal yet
	        if (!c.finishedMeal) {
	            return false;
	        }
	    }
	    return true;
	}
	protected void findToilet() {
		if(gp.world.mapM.currentRoom.equals(gp.world.mapM.getRoom(currentRoomNum))) {
			toilet = gp.world.buildingM.findFreeToilet();
		} else {
			toilet = gp.world.mapM.getRoom(currentRoomNum).findFreeToilet();
		}
    }
	public void removeLights() {
		
	}
	private void makeOrder() {
		if(isGhost) {
			foodOrder = RecipeManager.getRandomCursedRecipe();
		} else {
			foodOrder = gp.world.gameM.getRandomMenuRecipe();
		}
		
		if (gp.world.progressM.seasoningUnlocked) {
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
		gp.gui.addOrder(foodOrder, this, renderer);
	}
	private void waitForOrder() {
		waitingToOrder = true;
	}
	public void completeOrder(Plate p) {
	    if(foodOrder == null) return;

	    int baseCost = foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder));
	    // base payment
	    gp.player.wealth += baseCost;

	    int additionalCost = 0;
	    // tip logic based on patience
	    float progress = (float)(patienceCounter / maxPatienceTime);
	    int tip = 0;

	    if(progress <= 0.33f) { // green zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder)) * greenTipMultiplier);
	    } else if(progress <= 0.66f) { // orange zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder)) * orangeTipMultiplier);
	    } else { // red zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder)) * redTipMultiplier);
	    }
	    
	    boolean addTip = true;
	    if(gp.world.gameM.animalPresent) {
	    	addTip = false;
	    }
	    if(gp.world.mapM.isInRoom(4)) {
    		if(gp.world.buildingM.hasBuildingWithName("Leak")) {
    			addTip = false;
    		}
    	} else {
       		if(gp.world.mapM.getRoom(4).hasBuildingWithName("Leak")) {
    			addTip = false;
    		}
    	}
	    
	    if(addTip && (gp.world.progressM.tipJarPresent || gp.world.progressM.bigTipsPresent)) {
    		gp.world.progressM.achievements.get("free_tip").unlock();
	    	if(gp.world.progressM.bigTipsPresent) {
		    	gp.player.wealth += tip;
	    		additionalCost += (tip);
	    	} else {
	    		gp.player.wealth += (tip*0.5);
	    		additionalCost += (tip*0.5);
	    	}
	    }
	    
	    if (p.seasoningQuality != -1) {
	    	if(addTip) {
	    		gp.player.wealth += baseCost * 0.50f * p.seasoningQuality;
	     		additionalCost += (baseCost * 0.50f * p.seasoningQuality);
	    	}
	    }
	    
	    if(additionalCost >= baseCost*2) {
    		gp.world.progressM.achievements.get("sweet_talk").unlock();
	    }
	    
	    gp.player.soulsServed++;
	    
	    Statistics.servedCustomers++;
	    if(Statistics.servedCustomers == 50) {
    		gp.world.progressM.achievements.get("50_served").unlock();
	    } else if(Statistics.servedCustomers == 100) {
    		gp.world.progressM.achievements.get("100_served").unlock();
	    } else if(Statistics.servedCustomers == 500) {
    		gp.world.progressM.achievements.get("500_served").unlock();
	    } else if(Statistics.servedCustomers == 1000) {
    		gp.world.progressM.achievements.get("1000_served").unlock();
	    }

	    // clean up
	    RecipeManager.removeOrder(foodOrder);
	    gp.player.currentItem = null;
	    eating = true;
	    waitingToOrder = false;
	}
	public boolean isEating() {
		return eating;
	}
	public void takeOrder(double dt) {

	    orderTime += dt;

	    if (orderTime >= maxOrderTime) {
	        makeOrder();
	        orderTime = 0;
	        waitingToOrder = false;
	    }
	}
	protected void leave(double dt) {
		super.leave(dt);
		if(pet != null) {
			removeOtherNPC(pet);
		}
	}
	public void updateState(double dt) {
		if(gp.world.progressM.fasterCustomers) {
			speed = 2;
		}
	    if(!atTable) {
	        if(leaving) {
	        	if(inToilet) {
	        		if(walkToDoorWithDoorNum(dt, RoomHelperMethods.MAIN)) {
				    	inToilet = false;
	        		}
	        	} else {
	        		leave(dt);
	        	}
	        } else if(goingToToilet && !inToilet) {
	        	if(walkToDoorWithDoorNum(dt, RoomHelperMethods.BATHROOM)) {
	            	inToilet = true;
	        	}
	        } else if(inToilet && !onToilet) {
	        	if(toilet == null) {
	        		findToilet();
	        	}
	        	if(walkToBuilding(dt, toilet)) {
	        		onToilet = true;
			    	hitbox.x = toilet.hitbox.x;
			    	hitbox.y = toilet.hitbox.y;
	        	}
	        } else if(onToilet) {
	        	  toiletTime+=dt;
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
	            	if(walkToBuilding(dt, currentChair)) {
	            		 walking = false;
		                 atTable = true;
		                 hitbox.x = currentChair.hitbox.x+16;
		                 hitbox.y = currentChair.hitbox.y;
		                 
		                 switch(currentChair.facing) {
		                 case 0:
		                	 direction =  "Up";
		                	 break;
		                 case 1:
		                	 direction =  "Left";
		                	 hitbox.x -= 6;
		                	 break;
		                 case 2:
		                	 hitbox.x -= 6;
		                	 direction =  "Right";
		                	 break;
		                 case 3:
		                	 hitbox.x -= 6;
		                	 hitbox.y += 6;
		                	 direction =  "Down";
		                	 break;
		                 }
	            	}
	            }
	        }
	    } else {
	        // Waiting states
	        if(!ordered && !finishedMeal) {
	            if(!waitingToOrder) {
	                waitForOrder();
	            } else {
	            	if (hitbox.intersects(gp.player.interactHitbox)) {
	            	    if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {

	            	        // Take orders for everyone at this table
	            	        for (Customer c : getCustomersAtSameTable()) {
	            	            if (!c.ordered) {
	            	                c.takeOrder(dt);
	            	            }
	            	        }
	            	    }
	            	}
	            }
	        } 

	        // If they have ordered but haven’t been served yet
	        if((waitingToOrder && !ordered) || (ordered && !eating)) {
	            float patienceIncrement = patienceFactor;
	            if(this instanceof GroupCustomer) {
	            	patienceIncrement *= 0.6f;
	            } else if(gp.world.progressM.turntablePresent) {
	                patienceIncrement *= 0.8f; // 20% slower patience decrease
	            }
	         
	            patienceCounter += patienceIncrement * dt;
	        }

	        if (eating) {
	            eatCounter += dt;
	            currentAnimation = 1;

	            if (eatCounter >= eatTime) {

	                eatCounter = 0;
	                eating = false;        // this customer is done
	                finishedMeal = true;

	                // For groups, only trigger toilet when everyone is finished
	                List<Customer> tableGroup = getCustomersAtSameTable();

	                if (tableGroup.size() > 1) {

	                    if (isWholeTableFinishedEating()) {
	                        // Now the whole group can leave together
	                        for (Customer c : tableGroup) {
	                            c.atTable = false;
	                            if (c.currentChair != null) {
	                                c.currentChair.available = true;
	                            }
	                            c.goingToToilet = true;
	                        }
	                    }

	                } else {
	                    // Original single-customer behaviour
	                    atTable = false;
	                    if (currentChair != null) {
	                        currentChair.available = true;
	                    }
	                    goingToToilet = true;
	                }
	            }
	        }
	    }
	    
	    // Global patience timeout check
	    if (!eating && patienceCounter >= maxPatienceTime) {

	        List<Customer> tableGroup = getCustomersAtSameTable();

	        // If more than one person is at this table → group walkout
	        if (tableGroup.size() > 1) {
	            makeWholeTableLeave();
	        } else {
	            // Original single-customer behaviour
	            leave(dt);
	            RecipeManager.removeOrder(foodOrder);

	            if (currentChair != null) {
	                currentChair.available = true;
	            }

	            unhappy = true;
	            waitingToOrder = false;
	        }
	    }

	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
	    flickerCounter+=dt;
	    if (flickerCounter >= flickerSpeed) {
	        flickerCounter = 0;
	    }
	      talkHitbox.x = hitbox.x - 16;
	      talkHitbox.y = hitbox.y - 16;
	      
	      if(!eating) {
			    if(walking && !atTable) {
			    	currentAnimation = 1;
			    } else {
			    	currentAnimation = 0;
			    }
	      }
	      
		  animationSpeed+=animationUpdateSpeed*dt; //Update the animation frame
	      if(animationSpeed >= animationSpeedFactor) {
	    	  animationSpeed = 0;
	          animationCounter++;
	      }
	      if(animations != null) {
	    	  if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
	    		  animationCounter = 0; //Loops the animation
	    	  }
	      }
	}
	private void makeWholeTableLeave() {
	    for (Customer c : getCustomersAtSameTable()) {

	        // Cancel any active orders
	        if (c.foodOrder != null) {
	            RecipeManager.removeOrder(c.foodOrder);
	        }

	        if (c.currentChair != null) {
	            c.currentChair.available = true;
	        }

	        c.unhappy = true;
	        c.waitingToOrder = false;
	        c.leaving = true;
	    }
	}
	public void setCelebrityPresent(boolean isPresent) {
		celebrityPresent = isPresent;
		if(celebrityPresent) {
			maxPatienceTime = extendedMaxPatienceTime;
		} else {
			float fraction = (float)(patienceCounter / maxPatienceTime);
			maxPatienceTime = baseMaxPatienceTime;
			patienceCounter = (int)(fraction * maxPatienceTime);
		}
	}
	public double getPatienceCounter() {
		return patienceCounter;
	}
	public double getMaxPatienceTime() {
		return maxPatienceTime;
	}
	private void drawOrderBar(Renderer renderer, float worldX, float worldY, int orderTime, int maxOrderTime) {
	    float screenX = worldX - xOffset ;
	    float screenY = worldY - yOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, orderTime / (float) maxOrderTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	public void draw(Renderer renderer) {
	    this.renderer = renderer;  
	    
	    if(eating && direction.equals("Up")) {
	    	  drawEating(renderer);
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
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  
	    	  renderer.draw(img, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y - yDrawOffset ), (int)(drawWidth), (int)(drawHeight));
	      }
	      
	      if(orderTime > 0) {
	    	  drawOrderBar(renderer, hitbox.x, hitbox.y+8, (int)orderTime, (int)maxOrderTime);
	      }
  	      float patienceRatio = (float)(patienceCounter / maxPatienceTime);
	      if(patienceRatio >= 0.5f) {
	    	  hideOrder = false;
	      }
	      
	      if (waitingToOrder) {

	    	    boolean lowPatience = patienceRatio >= 0.5f && !ordered;
	    	    TextureRegion currentSign = orderSign;

	    	    if (lowPatience) {
	    	        // alternate every flickerSpeed frames
	    	        if (flickerCounter < flickerSpeed / 2) {
	    	            currentSign = orderSign;
	    	        } else {
	    	            currentSign = warningOrderSign;
	    	        }
	    	    }

	    	    renderer.draw(currentSign,(int)(hitbox.x - xOffset ),(int)(hitbox.y - yOffset  - 48), 48, 48);
	    	}
	      if(talking) {
	    	  //gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2, (int)hitbox.y - 48*3, dialogues[dialogueIndex], this);
	      }
	      
	  }
	public void drawOverlay(Renderer renderer) {
		 if(eating && !direction.equals("Up")) {
	    	  drawEating(renderer);
	     }
	}
	private void drawEating(Renderer renderer) {
		if(animations != null) {
	    	  int eatCounter = animationCounter;
	    	  if(animationCounter > 3) {
	    		  eatCounter-=4;
	    	  }
	    	  TextureRegion img = eatingAnimations[0][eatCounter];
	    	  int a = 0;
	    	  if(direction != null) {
	    	  switch(direction) {
	    	  case "Left":
	    		  a = 1;
	    		  break;
	    	  case "Right":
	    		  a = 2;
	    		  break;
	    	  case "Up":
	    		  a = 3;
	    	  	  break;
	    	  case "Down":
	    		  a = 0;
	    	  	  break;
	    	  }
	    	  	    	      	  
	    	  img = eatingAnimations[a][eatCounter];
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  
	    	  renderer.draw(img, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y - yDrawOffset ), (int)(drawWidth), (int)(drawHeight));
	      }
	}
	
}

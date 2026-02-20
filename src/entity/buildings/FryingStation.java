package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.Fryer;
import entity.items.Item;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class FryingStation extends Building {
	
	public Fryer leftSlot;
	public Fryer rightSlot;
	
	private LightSource leftLight;
	private LightSource rightLight;
	
	public Rectangle2D.Float leftHitbox;
	public Rectangle2D.Float rightHitbox;
	
	private TextureRegion leftCooking, rightCooking;
	
	private boolean firstUpdate = true;
	
	public FryingStation(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 96, 48);
		
		isSolid = true;
		
		drawWidth = 48*3;
		drawHeight = 48*3;
		xDrawOffset = 24;
		yDrawOffset = 72;
		importImages();
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		
		leftLight = new LightSource((int)(hitbox.x + 13*3 - xDrawOffset), (int)(hitbox.y + 36*3 - yDrawOffset), Colour.RED, 10);
		leftLight.setIntensity(0.2f);
		rightLight = new LightSource((int)(hitbox.x + 33*3 - xDrawOffset), (int)(hitbox.y + 36*3 - yDrawOffset), Colour.RED, 10);
		rightLight.setIntensity(0.2f);
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*6);
		npcHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+48, 48, 32);
		
		
		leftSlot = new Fryer(gp, hitbox.x, hitbox.y);
		rightSlot = new Fryer(gp, hitbox.x, hitbox.y);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*6);
	}
	public void removeBuildingsInWay() {
		    	
		BuildingManager bm = gp.world.buildingM;
		        
		Building[] buildings = bm.getBuildings();
		        
		if(!gp.world.mapM.isInRoom(roomNum)) {
			buildings = gp.world.mapM.getRoom(roomNum).getBuildings();
		}
		        
		        for (int i = 0; i < buildings.length; i++) {
		            Building b = buildings[i];

		            if (b == null || b == this) continue;

		                if (this.hitbox.intersects(b.hitbox)) {
		                	
		                    // Return to customiser inventory
		                	if(b.canBePlaced) {
		                		gp.world.customiser.addToInventory(b);
		                	}
		                    
		                    // Destroy & remove it
		                    b.destroy();
		                    if(gp.world.mapM.isInRoom(roomNum)) {
			                    bm.getBuildings()[i] = null;
		                    } else {
		                    	gp.world.mapM.getRoom(roomNum).getBuildings()[i] = null;
		                    }
		                }
		        }
	}
	public Building clone() {
		FryingStation building = new FryingStation(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new FryingStation(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][13];
		
		name = "Frying Station";
    	animations[0][0][0] = importImage("/decor/Fryer.png").getSubimage(0, 0, 48, 48);
    	leftCooking = importImage("/decor/Fryer.png").getSubimage(48*3, 0, 48, 48);
    	rightCooking = importImage("/decor/Fryer.png").getSubimage(48*4, 0, 48, 48);
    	
    	animations[0][0][1] = importImage("/decor/Fryer.png").getSubimage(48, 0, 48, 48);
    	animations[0][0][2] = importImage("/decor/Fryer.png").getSubimage(48*2, 0, 48, 48);
      	animations[0][0][3] = importImage("/decor/Fryer.png").getSubimage(48*5, 0, 48, 48);
      	animations[0][0][4] = importImage("/decor/Fryer.png").getSubimage(48*6, 0, 48, 48);
      	animations[0][0][5] = importImage("/decor/Fryer.png").getSubimage(48*7, 0, 48, 48);
      	animations[0][0][6] = importImage("/decor/Fryer.png").getSubimage(48*8, 0, 48, 48);
    	animations[0][0][7] = importImage("/decor/Fryer.png").getSubimage(48*10, 0, 48, 48);
      	animations[0][0][8] = importImage("/decor/Fryer.png").getSubimage(48*9, 0, 48, 48);
      	animations[0][0][9] = importImage("/decor/Fryer.png").getSubimage(48*11, 0, 48, 48);
      	animations[0][0][10] = importImage("/decor/Fryer.png").getSubimage(48*12, 0, 48, 48);
     	animations[0][0][11] = importImage("/decor/Fryer.png").getSubimage(48*13, 0, 48, 48);
      	animations[0][0][12] = importImage("/decor/Fryer.png").getSubimage(48*14, 0, 48, 48);
	}
	private void checkBurnAndDisableLight(Fryer fryer, LightSource light) {
	    if (fryer == null || fryer.cookingItem == null) return;

	    Food food = fryer.cookingItem;

	    if (food.foodState == FoodState.BURNT) {
	    	fryer.stopCooking();
	        gp.world.lightingM.removeLight(light);
	    }
	}
	public void updateState(double dt) {
		super.updateState(dt);
		
		if(gp.world.gameM.isPowerOn()) {
			if (leftSlot instanceof Fryer fryer) {
				fryer.updateCooking(dt);
			    checkBurnAndDisableLight(fryer, leftLight);
			}
			if (rightSlot instanceof Fryer fryer) {
				fryer.updateCooking(dt);
			    checkBurnAndDisableLight(fryer, rightLight);
			}
		}
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(firstUpdate) {
			int hitboxWidth = 20;
			leftHitbox = new Rectangle2D.Float(hitbox.x - xDrawOffset + 24 + hitboxWidth/2, (int) (hitbox.y)-yDrawOffset+70, hitboxWidth, 24);
			rightHitbox = new Rectangle2D.Float((int) hitbox.x - xDrawOffset + 48 + 30 + hitboxWidth/2, (int) (hitbox.y)-yDrawOffset+70, hitboxWidth, 24);
			firstUpdate = false;
			removeBuildingsInWay();
		}
		
		if(gp.world.mapM.isInRoom(roomNum)) { 
		if(leftSlot != null) {
			if(leftHitbox.intersects(gp.player.interactHitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
					if(gp.player.currentItem == null) {
							gp.player.currentItem = leftSlot;
							stopParticles(true);
							gp.player.resetAnimation(4);
							gp.player.clickCounter = 0.1;
							gp.world.lightingM.removeLight(leftLight);
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(leftSlot != null) {
								if(leftSlot.cookingItem != null) {
									foodName = leftSlot.cookingItem.getName();
									foodState = leftSlot.cookingItem.getState();
									cookTime = (int)leftSlot.getCookTime();
								}
								itemName = leftSlot.getName();
							}
							leftSlot = null;
					} else {
						if(leftSlot.getName().equals("Fryer")) {
							Fryer pan = (Fryer)leftSlot;
							if(pan.canCook(gp.player.currentItem) && pan.cookingItem == null) {
								pan.setCooking(gp.player.currentItem);
								startParticles(true);
								gp.world.lightingM.addLight(leftLight);
								gp.player.currentItem = null;
								gp.player.clickCounter = 0.1;
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										stopParticles(true);
										gp.world.lightingM.removeLight(leftLight);
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.world.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										stopParticles(true);
										gp.world.lightingM.removeLight(leftLight);
										pan.resetImages();
									}
								}
							}
						}
					}
				}
			}
		} else {
			if(gp.player.currentItem != null) {
				if(gp.player.clickCounter == 0 && gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
					if(leftHitbox.intersects(gp.player.interactHitbox)) {
						if(gp.player.currentItem.getName().equals("Fryer")) {
							leftSlot = (Fryer)gp.player.currentItem;
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(leftSlot != null) {
								if(leftSlot.cookingItem != null) {
									foodName = leftSlot.cookingItem.getName();
									foodState = leftSlot.cookingItem.getState();
									cookTime = (int)leftSlot.getCookTime();
									gp.world.lightingM.addLight(leftLight);
									startParticles(true);
								}
								itemName = leftSlot.getName();
							}
							gp.player.currentItem = null;
							gp.player.clickCounter = 0.1;
						}
					}
				}
			}
		}
		if(rightSlot != null) {
			if(rightHitbox.intersects(gp.player.interactHitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
					if(gp.player.currentItem == null) {
						gp.player.resetAnimation(4);
						gp.player.currentItem = rightSlot;
						gp.player.clickCounter = 0.1;
						stopParticles(false);
						String foodName = null;
						String itemName = null;
						int foodState = 0;
						int cookTime = 0;
						gp.world.lightingM.removeLight(rightLight);
						if(rightSlot != null) {
							if(rightSlot.cookingItem != null) {
								foodName = rightSlot.cookingItem.getName();
								foodState = rightSlot.cookingItem.getState();
								cookTime = (int)rightSlot.getCookTime();
							}
							itemName = rightSlot.getName();
						}
						rightSlot = null;
					} else {
						if(rightSlot.getName().equals("Fryer")) {
							Fryer pan = (Fryer)rightSlot;
							if(pan.canCook(gp.player.currentItem) && pan.cookingItem == null) {
								pan.setCooking(gp.player.currentItem);
								startParticles(false);
								gp.world.lightingM.addLight(rightLight);
								gp.player.currentItem = null;
								gp.player.clickCounter = 0.1;
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										gp.world.lightingM.removeLight(rightLight);
										pan.stopCooking();
										stopParticles(false);
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.world.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										gp.world.lightingM.removeLight(rightLight);
										pan.stopCooking();
										stopParticles(false);
										pan.resetImages();
									}
								}
							}
						}
					}
				}
			}
		} else {
			if(gp.player.currentItem != null) {
				if(gp.player.clickCounter == 0 && gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
					if(rightHitbox.intersects(gp.player.interactHitbox)) {
						if(gp.player.currentItem.getName().equals("Fryer")) {
							rightSlot = (Fryer)gp.player.currentItem;
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(rightSlot != null) {
								if(rightSlot.cookingItem != null) {
									foodName = rightSlot.cookingItem.getName();
									foodState = rightSlot.cookingItem.getState();
									cookTime = (int)rightSlot.getCookTime();
									startParticles(false);
									gp.world.lightingM.addLight(rightLight);
								}
								itemName = rightSlot.getName();
							}
							gp.player.currentItem = null;
							gp.player.clickCounter = 0.1;
						}
					}
				}
			}
		}
		}
	}
	public void removeItem(int slot) {
		if(slot == 0) {
			leftSlot = null;
		} else {
			rightSlot = null;
		}
	}
	public void addItem(int slot, Item fryer) {
		if(slot == 0) {
			leftSlot = (Fryer)fryer;
		} else {
			rightSlot = (Fryer)fryer;
		}
	}
	public void destroy() {
		gp.world.lightingM.removeLight(leftLight);
		gp.world.lightingM.removeLight(rightLight);
	}
	public void draw(Renderer renderer) {
		if(leftHitbox == null) {
			return;
		}
		
		renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		
		if(leftSlot != null) {
				renderer.draw(animations[0][0][4], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				Fryer pan = (Fryer)leftSlot;
				if(leftHitbox.intersects(gp.player.interactHitbox)) {
					renderer.draw(animations[0][0][6], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
				if(pan.isCooking()) {
					renderer.draw(animations[0][0][7], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
				}
				if(pan.cookingItem != null) {
					if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
						renderer.draw(animations[0][0][12], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
					}
				}
			} else {
				if(pan.isCooking()) {
					renderer.draw(animations[0][0][2], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
				}
				if(pan.cookingItem != null) {
					if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
						renderer.draw(animations[0][0][10], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
					}
				}
			}
		}
		if(rightSlot != null) {
			renderer.draw(animations[0][0][3], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			Fryer pan = (Fryer)rightSlot;
			if(rightHitbox.intersects(gp.player.interactHitbox)) {
				renderer.draw(animations[0][0][5], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
				if(rightSlot.getName().equals("Fryer")) {
					if(pan.isCooking()) {
						renderer.draw(animations[0][0][8], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
					}
					if(pan.cookingItem != null) {
						if(pan.cookingItem != null) {
							if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
								renderer.draw(animations[0][0][11], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
							}
						}
					}
				}
			} else {
				if(pan.isCooking()) {
					renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
				}
				if(pan.cookingItem != null) {
					if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
						renderer.draw(animations[0][0][9], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset,  drawWidth, drawHeight);
					}
				}
			}
		}
		
		// Left slot cooking bar
		if (leftSlot instanceof Fryer fryer && fryer.isCooking()) {
			renderer.draw(leftCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}

		// Right slot cooking bar
		if (rightSlot instanceof Fryer fryer && fryer.isCooking()) {
			//renderer.draw(rightSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
			renderer.draw(rightCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(leftSlot != null) {
			leftSlot.drawCookingUI(renderer, (int)hitbox.x+8, (int)hitbox.y, true);
		}
		if(rightSlot != null) {
			rightSlot.drawCookingUI(renderer, (int)hitbox.x-8, (int)hitbox.y, false);
		}
		
	}
	private void stopParticles(boolean isLeft) {
		if(isLeft) {
			gp.world.particleM.removePanEmber(2);
		} else {
			gp.world.particleM.removePanEmber(3);
		}
	}
	private void startParticles(boolean isLeft) {
		if(isLeft) {
			gp.world.particleM.addPanEmber(2, (int) hitbox.x - xDrawOffset  + 40, (int) (hitbox.y )-yDrawOffset+48+44, 32, 32);
		} else {
			gp.world.particleM.addPanEmber(3, (int) hitbox.x - xDrawOffset  + 48 + 48, (int) (hitbox.y )-yDrawOffset+48+44, 32, 32);
		}
	}
	
}
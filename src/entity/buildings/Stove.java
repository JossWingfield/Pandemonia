package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.CookStyle;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.FryingPan;
import entity.items.Item;
import entity.items.Plate;
import entity.items.SmallPan;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import map.LightSource;

public class Stove extends Building {
	
	public CookingItem leftSlot;
	public CookingItem rightSlot;
	
	private LightSource leftLight;
	private LightSource rightLight;
	
	public Rectangle2D.Float leftHitbox;
	public Rectangle2D.Float rightHitbox;
	
	private boolean drawCooking = false;
	private boolean firstUpdate = true;
	
	private boolean destroyed = false;
	
	private TextureRegion leftCooking, rightCooking;
	
	public Stove(GamePanel gp, float xPos, float yPos) {
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
		
		
		leftSlot = new SmallPan(gp, hitbox.x, hitbox.y);
		rightSlot = new FryingPan(gp, hitbox.x, hitbox.y);
		int hitboxWidth = 20;
		leftHitbox = new Rectangle2D.Float(hitbox.x - xDrawOffset + 24 + hitboxWidth/2, (int) (hitbox.y)-yDrawOffset+48+16, hitboxWidth, 64);
		rightHitbox = new Rectangle2D.Float((int) hitbox.x - xDrawOffset + 48 + 30 + hitboxWidth/2, (int) (hitbox.y)-yDrawOffset+48+16, hitboxWidth, 64);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*6);
		//addCookStation();
	}
	public void addCookStation() {
		if(gp.world.progressM.unlockedKitchen) {
			CookStation c = new CookStation(gp, hitbox.x-48, hitbox.y+4);
			if(gp.world.mapM.isInRoom(9)) {
				if(!gp.world.buildingM.hasBuildingWithName("Cook Station")) {
					gp.world.buildingM.addBuilding(c);
				}
			} else {
				if(!gp.world.mapM.getRoom(9).hasBuildingWithName("Cook Station")) {
					gp.world.mapM.getRoom(9).addBuilding(c);
				}
			}
		}
	}
	public Building clone() {
		Stove building = new Stove(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Stove(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Stove";
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 128, 48, 48);
    	animations[0][0][1] = importImage("/decor/OldKitchenProps.png").getSubimage(32, 0, 48, 48);
    	leftCooking = importImage("/decor/StoveOn.png").getSubimage(0, 0, 48, 48);
    	rightCooking = importImage("/decor/StoveOn.png").getSubimage(48, 0, 48, 48);
	}
	public void lightFlame() {
		drawCooking = true;
	}
	public void stopFlame() {
		drawCooking = false;
	}
	private void checkBurnAndDisableLight(CookingItem item, LightSource light) {
	    if (item == null || item.cookingItem == null) return;

	    Food food = item.cookingItem;

	    if (food.foodState == FoodState.BURNT) {
	        item.stopCooking();
	        gp.world.lightingM.removeLight(light);
	    }
	}
	public void updateState(double dt) {
		super.updateState(dt);
		
		if(gp.world.gameM.isPowerOn()) {
			if (leftSlot instanceof CookingItem pan) {
			    pan.updateCooking(dt);
			    checkBurnAndDisableLight(pan, leftLight);
			}
			if (rightSlot instanceof CookingItem pan) {
			    pan.updateCooking(dt);
			    checkBurnAndDisableLight(pan, rightLight);
			}
		}
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(gp.world.mapM.isInRoom(roomNum)) { 
		if(leftSlot != null) {
			if(leftHitbox.intersects(gp.player.hitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
					if(gp.player.currentItem == null) {
						if(leftSlot.getCookStyle().equals(CookStyle.FLIP) && leftSlot.isCooking() && leftSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							
							if (leftSlot.isCooking() && !leftSlot.flipped) {
								leftSlot.tryFlip();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else if(leftSlot.getCookStyle().equals(CookStyle.STIR) && leftSlot.isCooking() && leftSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							
							if (leftSlot.isCooking()) {
								leftSlot.stir();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else if(leftSlot.getCookStyle().equals(CookStyle.SAUTE) && leftSlot.isCooking() && leftSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							
							if (leftSlot.isCooking()) {
								leftSlot.addHeat();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else {
							gp.player.currentItem = leftSlot;
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
						}
					} else {
						if(leftSlot.getName().equals("Small Pot")) {
							SmallPan pan = (SmallPan)leftSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									startParticles(true);
									gp.world.lightingM.addLight(leftLight);
									gp.player.currentItem = null;
									gp.player.clickCounter = 0.1;
								}
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
						} else if(leftSlot.getName().equals("Frying Pan")) {
							FryingPan pan = (FryingPan)leftSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									startParticles(true);
									gp.world.lightingM.addLight(leftLight);
									gp.player.currentItem = null;
									gp.player.clickCounter = 0.1;
								}
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
					if(leftHitbox.intersects(gp.player.hitbox)) {
						if(gp.player.currentItem.getName().equals("Small Pot") || gp.player.currentItem.getName().equals("Frying Pan")) {
							leftSlot = (CookingItem)gp.player.currentItem;
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
			if(rightHitbox.intersects(gp.player.hitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
					if(gp.player.currentItem == null) {
						if(rightSlot.getCookStyle().equals(CookStyle.FLIP) && rightSlot.isCooking() && rightSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							if (rightSlot.isCooking() && !rightSlot.flipped) {
								rightSlot.tryFlip();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else if(rightSlot.getCookStyle().equals(CookStyle.STIR) && rightSlot.isCooking() && rightSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							
							if (rightSlot.isCooking()) {
								rightSlot.stir();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else if(rightSlot.getCookStyle().equals(CookStyle.SAUTE) && rightSlot.isCooking() && rightSlot.cookingItem.foodState.equals(FoodState.RAW)) {
							
							if (rightSlot.isCooking()) {
								rightSlot.addHeat();
							    gp.player.clickCounter = 0.1;
							    return;
							}
						} else {
							gp.player.resetAnimation(4);
							gp.player.currentItem = rightSlot;
							gp.player.clickCounter = 0.1;
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
						}
					} else {
						if(rightSlot.getName().equals("Small Pot")) {
							SmallPan pan = (SmallPan)rightSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									startParticles(false);
									gp.world.lightingM.addLight(rightLight);
									gp.player.currentItem = null;
									gp.player.clickCounter = 0.1;
								}
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
						} else if(rightSlot.getName().equals("Frying Pan")) {
							FryingPan pan = (FryingPan)rightSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									startParticles(false);
									gp.world.lightingM.addLight(rightLight);
									gp.player.currentItem = null;
									gp.player.clickCounter = 0.1;
								}
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
					if(rightHitbox.intersects(gp.player.hitbox)) {
						if(gp.player.currentItem.getName().equals("Small Pot") || gp.player.currentItem.getName().equals("Frying Pan")) {
							rightSlot = (CookingItem)gp.player.currentItem;
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(rightSlot != null) {
								if(rightSlot.cookingItem != null) {
									foodName = rightSlot.cookingItem.getName();
									foodState = rightSlot.cookingItem.getState();
									cookTime = (int)rightSlot.getCookTime();
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
	public void addItem(int slot, Item cookingItem) {
		if(slot == 0) {
			leftSlot = (CookingItem)cookingItem;
		} else {
			rightSlot = (CookingItem)cookingItem;
		}
	}
	public void destroy() {
		gp.world.lightingM.removeLight(leftLight);
		gp.world.lightingM.removeLight(rightLight);
	}
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	public void draw(Renderer renderer) {
		
		if(destroyed) {
			renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} 
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		
		if(leftSlot != null) {
			if(leftSlot.getName().equals("Small Pot")) {
				renderer.draw(leftSlot.animations[0][0][0], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+10, 48, 48);
				if(leftHitbox.intersects(gp.player.hitbox)) {
					renderer.draw(leftSlot.animations[0][0][2], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+10, 48, 48);
					SmallPan pan = (SmallPan)leftSlot;
					if(pan.cookingItem != null) {
						renderer.draw(leftSlot.animations[0][0][4], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+10, 48, 48);
					}
				}
			} else if(leftSlot.getName().equals("Frying Pan")) {
					renderer.draw(leftSlot.animations[0][0][0], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
					FryingPan pan = (FryingPan)leftSlot;
					if(leftHitbox.intersects(gp.player.hitbox)) {
						renderer.draw(leftSlot.animations[0][0][2], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
					if(pan.isCooking()) {
						renderer.draw(leftSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
					}
					if(pan.cookingItem != null) {
						renderer.draw(leftSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
						if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
							renderer.draw(leftSlot.animations[0][0][11], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
						}
					}
					}
				}
		}
		if(rightSlot != null) {
			renderer.draw(rightSlot.animations[0][0][0], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
			if(rightHitbox.intersects(gp.player.hitbox)) {
				renderer.draw(rightSlot.animations[0][0][2], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
				if(rightSlot.getName().equals("Small Pot")) {
					SmallPan pan = (SmallPan)rightSlot;
					if(pan.cookingItem != null) {
						renderer.draw(rightSlot.animations[0][0][4], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
					}
				}
				if(rightSlot.getName().equals("Frying Pan")) {
					FryingPan pan = (FryingPan)rightSlot;
					if(pan.isCooking() || drawCooking) {
						renderer.draw(rightSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
					}
					if(pan.cookingItem != null) {
						renderer.draw(rightSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
						if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
							renderer.draw(rightSlot.animations[0][0][11], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
						}
					}
				}
			}
		}
		
		// Left slot cooking bar
		if (leftSlot instanceof SmallPan pan && pan.isCooking()) {
			renderer.draw(leftCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		if (leftSlot instanceof FryingPan pan && pan.isCooking()) {
			renderer.draw(leftSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
			renderer.draw(leftCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

		}

		// Right slot cooking bar
		if (rightSlot instanceof SmallPan pan && pan.isCooking()) {
			renderer.draw(rightCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

		}
		if (rightSlot instanceof FryingPan pan && pan.isCooking()) {
			renderer.draw(rightSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
			renderer.draw(rightCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(leftSlot != null) {
			leftSlot.drawCookingUI(renderer, (int)hitbox.x, (int)hitbox.y, true);
		}
		if(rightSlot != null) {
			rightSlot.drawCookingUI(renderer, (int)hitbox.x, (int)hitbox.y, false);
		}
		
		if(drawCooking) {
			if(rightSlot instanceof SmallPan pan) {
				renderer.draw(rightCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else if(rightSlot instanceof FryingPan pan) {
				renderer.draw(rightSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 48, 48);
				renderer.draw(rightCooking, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
		}
		
	}
	private void stopParticles(boolean isLeft) {
		if(isLeft) {
			gp.world.particleM.removePanEmber(0);
		} else {
			gp.world.particleM.removePanEmber(1);
		}
	}
	private void startParticles(boolean isLeft) {
		if(isLeft) {
			gp.world.particleM.addPanEmber(0, (int) hitbox.x - xDrawOffset  + 18, (int) (hitbox.y )-yDrawOffset+48+16, 32, 32);
		} else {
			gp.world.particleM.addPanEmber(1, (int) hitbox.x - xDrawOffset  + 48 + 30, (int) (hitbox.y )-yDrawOffset+48+16, 32, 32);
		}
	}
	
}
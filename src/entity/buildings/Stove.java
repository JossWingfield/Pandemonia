package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Taskbar.State;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.FryingPan;
import entity.items.Item;
import entity.items.Plate;
import entity.items.SmallPan;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import net.packets.Packet08PickUpFromStove;
import net.packets.Packet09PlaceItemOnStove;
import net.packets.Packet13ClearPlayerHand;
import net.packets.Packet16StartCookingOnStove;
import net.packets.Packet20AddFoodToPlateOnTable;

public class Stove extends Building {
	
	public CookingItem leftSlot;
	public CookingItem rightSlot;
	
	public Rectangle2D.Float leftHitbox;
	public Rectangle2D.Float rightHitbox;
	
	private boolean firstUpdate = true;
	private int clickCooldown = 0;
	
	public Stove(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 96, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 48*3;
		drawHeight = 48*3;
		xDrawOffset = 24;
		yDrawOffset = 72;
		importImages();
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
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
		animations = new BufferedImage[1][1][1];
		
		name = "Stove";
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 128, 48, 48);
	}
	public void update() {
		if(gp.world.isPowerOn()) {
			if (leftSlot instanceof SmallPan pan) {
			    pan.updateCooking();
			}
			if (leftSlot instanceof FryingPan pan) {
			    pan.updateCooking();
			}
			if (rightSlot instanceof FryingPan pan) {
			    pan.updateCooking();
			}
			if (rightSlot instanceof SmallPan pan) {
			    pan.updateCooking();
			}
		}
		if(canBePlaced) {
			if(hitbox.contains(gp.mouseI.mouseX + gp.player.xDiff, gp.mouseI.mouseY + gp.player.yDiff)) {
				if(gp.keyI.shiftPressed) {
					openDestructionUI();
				}
			}
			
			if(!hitbox.contains(gp.mouseI.mouseX + gp.player.xDiff, gp.mouseI.mouseY + gp.player.yDiff)) {
				closeDestructionUI();
			}
			if(destructionUIOpen) {
				if(gp.mouseI.rightClickPressed) {
					gp.customiser.addToInventory(this);
					gp.buildingM.destroyBuilding(this);
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
	public void draw(Graphics2D g2) {
		
		if(firstUpdate) {
			firstUpdate = false;
			leftSlot = new SmallPan(gp, hitbox.x, hitbox.y);
			rightSlot = new FryingPan(gp, hitbox.x, hitbox.y);
			int hitboxWidth = 20;
			leftHitbox = new Rectangle2D.Float(hitbox.x - xDrawOffset - gp.player.xDiff + 24 + hitboxWidth/2, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, hitboxWidth, 64);
			rightHitbox = new Rectangle2D.Float((int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30 + hitboxWidth/2, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, hitboxWidth, 64);
		}
		
		//g2.setColor(Color.YELLOW);
		//g2.drawRect((int)leftHitbox.x, (int)leftHitbox.y, (int)leftHitbox.width, (int)leftHitbox.height);
		//g2.drawRect((int)rightHitbox.x, (int)rightHitbox.y, (int)rightHitbox.width, (int)rightHitbox.height);
		 
		g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
      		 
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
		
		if(clickCooldown > 0) {
			clickCooldown--;
		}
		
		if(leftSlot != null) {
			g2.drawImage(leftSlot.animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 18, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+10, 48, 48, null);
			if(leftHitbox.intersects(gp.player.hitbox)) {
				g2.drawImage(leftSlot.animations[0][0][2], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 18, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+10, 48, 48, null);
				if(leftSlot.getName().equals("Small Pan")) {
					SmallPan pan = (SmallPan)leftSlot;
					if(pan.cookingItem != null) {
						g2.drawImage(leftSlot.animations[0][0][4], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 18, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+10, 48, 48, null);
					}
				}
				if(leftSlot.getName().equals("Frying Pan")) {
					FryingPan pan = (FryingPan)leftSlot;
					if(pan.isCooking()) {
						g2.drawImage(leftSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 24, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
					}
					if(pan.cookingItem != null) {
						g2.drawImage(leftSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 24, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
						if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
							g2.drawImage(leftSlot.animations[0][0][11], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 24, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
						}
					}
				}
				if(gp.keyI.ePressed && clickCooldown == 0) {
					if(gp.player.currentItem == null) {
						gp.player.currentItem = leftSlot;
						gp.player.resetAnimation(4);
						clickCooldown = 12;
						String foodName = null;
						String itemName = null;
						int foodState = 0;
						int cookTime = 0;
						if(leftSlot != null) {
							if(leftSlot.cookingItem != null) {
								foodName = leftSlot.cookingItem.getName();
								foodState = leftSlot.cookingItem.getState();
								cookTime = leftSlot.getCookTime();
							}
							itemName = leftSlot.getName();
						}
						if(gp.multiplayer) {
							Packet08PickUpFromStove packet = new Packet08PickUpFromStove(
		    	                	gp.player.getUsername(),
		    	                    foodName,
		    	                    itemName,
		    	                    0,
		    	                    getArrayCounter(),
		    	                    foodState,
		    	                    cookTime);
	    	                packet.writeData(gp.socketClient);
	    	            }
						leftSlot = null;
					} else {
						if(leftSlot.getName().equals("Small Pan")) {
							SmallPan pan = (SmallPan)leftSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									if(gp.multiplayer) {
										 // send packet to server
									    Packet16StartCookingOnStove packet = new Packet16StartCookingOnStove(
									        gp.player.getUsername(),  // or player ID
									        getArrayCounter(),       // you'll need a unique ID for each stove
									        0, // 0 = left slot, 1 = right slot
									        gp.player.currentItem.getName()
									    );
									    packet.writeData(gp.socketClient);
									    Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
						    			packet2.writeData(gp.socketClient); 
									}
									gp.player.currentItem = null;
									clickCooldown = 12;
								}
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
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
									if(gp.multiplayer) {
										 // send packet to server
									    Packet16StartCookingOnStove packet = new Packet16StartCookingOnStove(
									        gp.player.getUsername(),  // or player ID
									        getArrayCounter(),       // you'll need a unique ID for each stove
									        0, // 0 = left slot, 1 = right slot
									        gp.player.currentItem.getName()
									    );
									    packet.writeData(gp.socketClient);
									    Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
						    			packet2.writeData(gp.socketClient); 
									}
									gp.player.currentItem = null;
									clickCooldown = 12;
								}
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
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
				if(clickCooldown == 0 && gp.keyI.ePressed) {
					if(leftHitbox.intersects(gp.player.hitbox)) {
						if(gp.player.currentItem.getName().equals("Small Pan") || gp.player.currentItem.getName().equals("Frying Pan")) {
							leftSlot = (CookingItem)gp.player.currentItem;
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(leftSlot != null) {
								if(leftSlot.cookingItem != null) {
									foodName = leftSlot.cookingItem.getName();
									foodState = leftSlot.cookingItem.getState();
									cookTime = leftSlot.getCookTime();
								}
								itemName = leftSlot.getName();
							}
							if(gp.multiplayer) {
								Packet09PlaceItemOnStove packet = new Packet09PlaceItemOnStove(
			    	                	gp.player.getUsername(),
			    	                    foodName,
			    	                    itemName,
			    	                    0,
			    	                    getArrayCounter(),
			    	                    foodState,
			    	                    cookTime);
		    	                packet.writeData(gp.socketClient);
		    	            }
							gp.player.currentItem = null;
							clickCooldown = 12;
						}
					}
				}
			}
		}
		if(rightSlot != null) {
			g2.drawImage(rightSlot.animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
			if(rightHitbox.intersects(gp.player.hitbox)) {
				g2.drawImage(rightSlot.animations[0][0][2], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
				if(rightSlot.getName().equals("Small Pan")) {
					SmallPan pan = (SmallPan)rightSlot;
					if(pan.cookingItem != null) {
						g2.drawImage(rightSlot.animations[0][0][4], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
					}
				}
				if(rightSlot.getName().equals("Frying Pan")) {
					FryingPan pan = (FryingPan)rightSlot;
					if(pan.isCooking()) {
						g2.drawImage(rightSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
					}
					if(pan.cookingItem != null) {
						g2.drawImage(rightSlot.animations[0][0][9], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
						if(pan.cookingItem.foodState.equals(FoodState.BURNT)) {
							g2.drawImage(rightSlot.animations[0][0][11], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
						}
					}
				}
				if(gp.keyI.ePressed && clickCooldown == 0) {
					if(gp.player.currentItem == null) {
						gp.player.resetAnimation(4);
						gp.player.currentItem = rightSlot;
						clickCooldown = 12;
						String foodName = null;
						String itemName = null;
						int foodState = 0;
						int cookTime = 0;
						if(rightSlot != null) {
							if(rightSlot.cookingItem != null) {
								foodName = rightSlot.cookingItem.getName();
								foodState = rightSlot.cookingItem.getState();
								cookTime = rightSlot.getCookTime();
							}
							itemName = rightSlot.getName();
						}
						if(gp.multiplayer) {
							Packet08PickUpFromStove packet = new Packet08PickUpFromStove(
		    	                	gp.player.getUsername(),
		    	                    foodName,
		    	                    itemName,
		    	                    1,
		    	                    getArrayCounter(),
		    	                    foodState,
		    	                    cookTime);
	    	                packet.writeData(gp.socketClient);
	    	            }
						rightSlot = null;
					} else {
						if(rightSlot.getName().equals("Small Pan")) {
							SmallPan pan = (SmallPan)rightSlot;
							if(pan.canCook(gp.player.currentItem.getName()) && pan.cookingItem == null) {
								Food f = (Food)gp.player.currentItem;
								if(f.foodState == FoodState.RAW) {
									pan.setCooking(gp.player.currentItem);
									if(gp.multiplayer) {
										 // send packet to server
									    Packet16StartCookingOnStove packet = new Packet16StartCookingOnStove(
									        gp.player.getUsername(),  // or player ID
									        getArrayCounter(),       // you'll need a unique ID for each stove
									        1, // 0 = left slot, 1 = right slot
									        gp.player.currentItem.getName()
									    );
									    packet.writeData(gp.socketClient);
									    Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
						    			packet2.writeData(gp.socketClient); 
									}
									gp.player.currentItem = null;
									clickCooldown = 12;
								}
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
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
									if(gp.multiplayer) {
										 // send packet to server
									    Packet16StartCookingOnStove packet = new Packet16StartCookingOnStove(
									        gp.player.getUsername(),  // or player ID
									        getArrayCounter(),       // you'll need a unique ID for each stove
									        1, // 0 = left slot, 1 = right slot
									        gp.player.currentItem.getName()
									    );
									    packet.writeData(gp.socketClient);
									    Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
						    			packet2.writeData(gp.socketClient); 
									}
									gp.player.currentItem = null;
									clickCooldown = 12;
								}
							} else if (gp.player.currentItem.getName().equals("Plate")) {
								if(pan.cookingItem != null) {
									if(pan.cookingItem.foodState == FoodState.COOKED) {
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
										pan.resetImages();
									} else if(pan.cookingItem.foodState == FoodState.BURNT) {
										pan.cookingItem = null;
										pan.cookingItem = (Food)gp.itemRegistry.getItemFromName("Burnt Food", 0);
										pan.cookingItem.foodState = FoodState.PLATED;
										Plate p = (Plate)gp.player.currentItem;
										p.addIngredient(pan.cookingItem);
										pan.cookingItem = null;
										pan.stopCooking();
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
				if(clickCooldown == 0 && gp.keyI.ePressed) {
					if(rightHitbox.intersects(gp.player.hitbox)) {
						if(gp.player.currentItem.getName().equals("Small Pan") || gp.player.currentItem.getName().equals("Frying Pan")) {
							rightSlot = (CookingItem)gp.player.currentItem;
							String foodName = null;
							String itemName = null;
							int foodState = 0;
							int cookTime = 0;
							if(rightSlot != null) {
								if(rightSlot.cookingItem != null) {
									foodName = rightSlot.cookingItem.getName();
									foodState = rightSlot.cookingItem.getState();
									cookTime = rightSlot.getCookTime();
								}
								itemName = rightSlot.getName();
							}
							if(gp.multiplayer) {
								Packet09PlaceItemOnStove packet = new Packet09PlaceItemOnStove(
			    	                	gp.player.getUsername(),
			    	                    foodName,
			    	                    itemName,
			    	                    1,
			    	                    getArrayCounter(),
			    	                    foodState,
			    	                	cookTime);
		    	                packet.writeData(gp.socketClient);
		    	            }
							gp.player.currentItem = null;
							clickCooldown = 12;
						}
					}
				}
			}
		}
		// Left slot cooking bar
		if (leftSlot instanceof SmallPan pan && pan.isCooking()) {
			if(pan.cookingItem != null) {
				if(pan.cookingItem.foodState.equals(FoodState.RAW)) {
					drawCookingBar(g2, hitbox.x + 16, hitbox.y + 48 + 16, pan.getCookTime(), pan.getMaxCookTime());
				} else {
					pan.drawCookingWarning(g2, (int)(hitbox.x));
				}
			}
		}
		if (leftSlot instanceof FryingPan pan && pan.isCooking()) {
			if(pan.cookingItem != null) {
				if(pan.cookingItem.foodState.equals(FoodState.RAW)) {
					drawCookingBar(g2, hitbox.x + 16, hitbox.y + 48 + 16, pan.getCookTime(), pan.getMaxCookTime());
				} else {
					pan.drawCookingWarning(g2, (int)(hitbox.x));
				}
			}
			g2.drawImage(leftSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 24, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
		}

		// Right slot cooking bar
		if (rightSlot instanceof SmallPan pan && pan.isCooking()) {
			if(pan.cookingItem != null) {
				if(pan.cookingItem.foodState.equals(FoodState.RAW)) {
					drawCookingBar(g2, hitbox.x + 48 + 30, hitbox.y + 48 + 16, pan.getCookTime(), pan.getMaxCookTime());
				} else {
					pan.drawCookingWarning(g2, (int)(hitbox.x + 56));
				}
			}
		}
		if (rightSlot instanceof FryingPan pan && pan.isCooking()) {
			if(pan.cookingItem != null) {
				if(pan.cookingItem.foodState.equals(FoodState.RAW)) {
					drawCookingBar(g2, hitbox.x + 48 + 30, hitbox.y + 48 + 16, pan.getCookTime(), pan.getMaxCookTime());
				} else {
					pan.drawCookingWarning(g2, (int)(hitbox.x + 56));
				}
			}
			g2.drawImage(rightSlot.animations[0][0][3], (int) hitbox.x - xDrawOffset - gp.player.xDiff + 48 + 30, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+48+16, 48, 48, null);
		}
	}
	
	private void drawCookingBar(Graphics2D g2, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset - gp.player.xDiff;
	    float screenY = worldY - yDrawOffset - gp.player.yDiff;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    g2.setColor(Color.BLACK);
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight);
	    
	    g2.setColor(new Color(r, g, 0));
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight);

	}
}
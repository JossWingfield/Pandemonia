package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import net.packets.Packet03PickupItem;
import net.packets.Packet10RemoveSinkPlate;

public class Sink extends Building {
	
	private Rectangle2D.Float sinkHitbox;
	private boolean firstUpdate = true;
	private Plate currentPlate = null;
	private double clickCooldown = 0;
	private double chopCount = 0;
	private double maxWashCount = 8;
	private int cleanedPlateCount = 3;
	private Rectangle2D.Float cleanedPlateHitbox;
	
	public Sink(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y, 24, hitbox.height);
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*5, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*5, hitbox.height-3*4);
	}
	public Building clone() {
		Sink building = new Sink(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Sink(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][11];
		
        name = "Kitchen Sink 1";
    	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(0, 192, 32, 32);
    	animations[0][0][1] = importImage("/decor/Sink.png").getSubimage(0, 0, 32, 32);
    	//PLATES
    	animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(0, 64, 16, 16);
    	animations[0][0][3] = importImage("/decor/kitchen props.png").getSubimage(16, 64, 16, 16);
    	animations[0][0][4] = importImage("/decor/kitchen props.png").getSubimage(32, 64, 16, 16);
    	//Sink Overlays
    	animations[0][0][5] = importImage("/decor/Sink.png").getSubimage(32, 0, 32, 32);
    	animations[0][0][6] = importImage("/decor/Sink.png").getSubimage(64, 0, 32, 32);
    	//Plate Collection
    	animations[0][0][7] = importImage("/decor/kitchen props.png").getSubimage(48, 48, 16, 16);
    	animations[0][0][8] = importImage("/decor/kitchen props.png").getSubimage(48+16, 48, 16, 16);
    	animations[0][0][9] = importImage("/decor/kitchen props.png").getSubimage(48, 48+16, 16, 16);
    	animations[0][0][10] = importImage("/decor/kitchen props.png").getSubimage(48+16, 48+16, 16, 16);
    	
    	isSolid = false;
    	hitbox.height = 80;
    	drawHeight = 48*2;
    	drawWidth = 48*2;
    	xDrawOffset = 20;
    	yDrawOffset = 0;
    	isThirdLayer = true;
	}
	public void addPlate(Plate p) {
		if(currentPlate == null) {
	    	currentPlate = p;
    	} else {
	    	currentPlate.increasePlateStack();
    	}
	}
	public boolean hasDirtyPlates() {
		return currentPlate != null;
	}
	public void washPlates(double dt) {
		chopCount+=dt;
    	if(chopCount == maxWashCount) {
    		chopCount = 0;
    		cleanedPlateCount++;
    		if(currentPlate.getCurrentStackCount() > 1) {
    			currentPlate.decreasePlateStack();
    		} else {
    			currentPlate = null;
    		}
    	}
	}
	public void decreasePlateCount() {
		if(cleanedPlateCount > 0) {
			cleanedPlateCount--;
		}
	}
	private void drawWashingBar(Renderer renderer, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset ;
	    float screenY = worldY - yDrawOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	public void update(double dt) {
		super.update(dt);
		if (clickCooldown > 0) {
	    	clickCooldown -= dt;        // subtract elapsed time in seconds
			if (clickCooldown < 0) {
				clickCooldown = 0;      // clamp to zero
			}
		}
		
		if(gp.progressM.sinkUpgradeI) {
			maxWashCount = 6;
		}
		
		if(sinkHitbox != null) {
			if(gp.player.currentItem != null) {
				if(gp.player.currentItem instanceof Plate plate) {
					if(plate.isDirty()) {
						if(sinkHitbox.intersects(gp.player.hitbox)) {
						    if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
						    	if(currentPlate == null) {
							    	clickCooldown = 0.1;
							    	currentPlate = (Plate)gp.player.currentItem;
							    	gp.player.currentItem = null;
						    	} else {
						    		clickCooldown = 0.1;
							    	currentPlate.increasePlateStack();
							    	gp.player.currentItem = null;
						    	}
						    }
						}
					}
				}
			} else if(currentPlate != null) {
				if(sinkHitbox.intersects(gp.player.hitbox)) {
				    if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
				    	chopCount+=dt;
				    	if(chopCount >= maxWashCount) {
				    		chopCount = 0;
				    		cleanedPlateCount++;
				    		if(currentPlate.getCurrentStackCount() > 1) {
				    			currentPlate.decreasePlateStack();
				    		} else {
				    			currentPlate = null;
				    		}
				    	}
				    }
				}
			}
		}
		if(cleanedPlateHitbox != null) {
			if(cleanedPlateHitbox.intersects(gp.player.hitbox) && !sinkHitbox.intersects(gp.player.hitbox)) {
				if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
					if(cleanedPlateCount > 0 && gp.player.currentItem == null) {
						cleanedPlateCount--;
						Plate plate = new Plate(gp, 0, 0);
						plate.setClean();
						plate.setCurrentStackCount(1);
						gp.player.currentItem = plate;
						clickCooldown = 0.1;
						gp.player.resetAnimation(4);
						 if (gp.multiplayer) {
							 int state = gp.player.currentItem instanceof Food f ? f.getState() : 0;
					         Packet03PickupItem packet = new Packet03PickupItem(
					                gp.player.currentItem.getName(),
					                gp.player.getUsername(),
					                state
					            );
					         packet.writeData(gp.socketClient);
					         Packet10RemoveSinkPlate packet2 = new Packet10RemoveSinkPlate(gp.player.getUsername(), getArrayCounter());
						     packet2.writeData(gp.socketClient);
						 }
					}
				}
			}
		}
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			sinkHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+48+24, 48+16, 24);
			cleanedPlateHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 48+16, 24);
		}
		
		if(cleanedPlateCount == 0) {
		     renderer.draw(animations[0][0][7], (int) hitbox.x - xDrawOffset +18, (int) (hitbox.y )-yDrawOffset, 48, 48);
		} else {
		     renderer.draw(animations[0][0][7+cleanedPlateCount], (int) hitbox.x - xDrawOffset +18, (int) (hitbox.y )-yDrawOffset, 48, 48);
		}
		
	     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		 
		if(sinkHitbox != null) {
			if(gp.player.currentItem != null) {
				if(gp.player.currentItem instanceof Plate plate) {
					if(plate.isDirty()) {
						if(sinkHitbox.intersects(gp.player.hitbox)) {
						    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
						}
					}
				}
			} else if(currentPlate != null) {
				if(sinkHitbox.intersects(gp.player.hitbox)) {
				    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
				}
			}
		}
		
		
		if(currentPlate != null) {
			TextureRegion img = animations[0][0][2];
			switch(currentPlate.getCurrentStackCount()) {
			case 2:
				img = animations[0][0][3];
				break;
			case 3:
				img = animations[0][0][4];
				break;
			}
			 renderer.draw(img, (int) hitbox.x - xDrawOffset  + 19, (int) (hitbox.y )-yDrawOffset+30, 48, 48);

			
		    if(sinkHitbox.intersects(gp.player.hitbox)) {
		    	renderer.draw(animations[0][0][5], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		    } else {
				renderer.draw(animations[0][0][6], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		    }
		}
		
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	}
	public void drawOverlayUI(Renderer renderer) {
		if(currentPlate != null) {
			drawWashingBar(renderer, hitbox.x+30, hitbox.y+32, (int)chopCount, (int)maxWashCount);
		}
	}
}

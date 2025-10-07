package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.items.Food;
import entity.items.Plate;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import net.packets.Packet10RemoveSinkPlate;

public class Sink extends Building {
	
	private Rectangle2D.Float sinkHitbox;
	private boolean firstUpdate = true;
	private Plate currentPlate = null;
	private int clickCooldown = 0;
	private int chopCount = 0;
	private  int maxWashCount = 60*8;
	private int cleanedPlateCount = 3;
	private Rectangle2D.Float cleanedPlateHitbox;
	private BufferedImage[] normalImages;
	private BufferedImage litPlateImage;
	
	public Sink(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y, 24, hitbox.height);
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
		animations = new BufferedImage[1][1][11];
		normalImages = new BufferedImage[11];
		
        name = "Kitchen Sink 1";
    	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(0, 192, 32, 32);
    	normalImages[0] = importImage("/decor/bathroom propsNormal.png").getSubimage(0, 192, 32, 32);
    	animations[0][0][1] = importImage("/decor/Sink.png").getSubimage(0, 0, 32, 32);
    	normalImages[1] = importImage("/decor/bathroom propsNormal.png").getSubimage(0, 0, 32, 32);
    	//PLATES
    	animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(0, 64, 16, 16);
    	normalImages[2] = importImage("/decor/bathroom propsNormal.png").getSubimage(0, 64, 16, 16);
    	animations[0][0][3] = importImage("/decor/kitchen props.png").getSubimage(16, 64, 16, 16);
    	normalImages[3] = importImage("/decor/bathroom propsNormal.png").getSubimage(16, 64, 16, 16);
    	animations[0][0][4] = importImage("/decor/kitchen props.png").getSubimage(32, 64, 16, 16);
    	normalImages[4] = importImage("/decor/bathroom propsNormal.png").getSubimage(32, 64, 16, 16);
    	//Sink Overlays
    	animations[0][0][5] = importImage("/decor/Sink.png").getSubimage(32, 0, 32, 32);
    	normalImages[5] = importImage("/decor/SinkNormal.png").getSubimage(32, 0, 32, 32);
    	animations[0][0][6] = importImage("/decor/Sink.png").getSubimage(64, 0, 32, 32);
    	normalImages[6] = importImage("/decor/SinkNormal.png").getSubimage(64, 0, 32, 32);
    	//Plate Collection
    	animations[0][0][7] = importImage("/decor/kitchen props.png").getSubimage(48, 48, 16, 16);
    	normalImages[7] = importImage("/decor/kitchen propsNormal.png").getSubimage(48, 48, 16, 16);
    	animations[0][0][8] = importImage("/decor/kitchen props.png").getSubimage(48+16, 48, 16, 16);
    	normalImages[8] = importImage("/decor/kitchen propsNormal.png").getSubimage(48+16, 48, 16, 16);
    	animations[0][0][9] = importImage("/decor/kitchen props.png").getSubimage(48, 48+16, 16, 16);
    	normalImages[9] = importImage("/decor/kitchen propsNormal.png").getSubimage(48, 48+16, 16, 16);
    	animations[0][0][10] = importImage("/decor/kitchen props.png").getSubimage(48+16, 48+16, 16, 16);
    	normalImages[10] = importImage("/decor/kitchen propsNormal.png").getSubimage(48+16, 48+16, 16, 16);
    	
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
	public void washPlates() {
		chopCount++;
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
		litPlateImage = null;
	}
	private void drawWashingBar(Graphics2D g2, float worldX, float worldY, int cookTime, int maxCookTime) {
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
	public void draw(Graphics2D g2) {
		
		if(gp.progressM.sinkUpgradeI) {
			maxWashCount = 60*6;
		}
		
		if(firstUpdate) {
			firstUpdate = false;
			sinkHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+48+24, 48+16, 24);
			cleanedPlateHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 48+16, 24);
		}
		
		if(cleanedPlateCount == 0) {
		     g2.drawImage(animations[0][0][7], (int) hitbox.x - xDrawOffset - gp.player.xDiff+18, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, 48, 48, null);
		} else {
		     g2.drawImage(animations[0][0][7+cleanedPlateCount], (int) hitbox.x - xDrawOffset - gp.player.xDiff+18, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, 48, 48, null);
		}
		
	     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		 
		if(sinkHitbox != null) {
			if(gp.player.currentItem != null) {
				if(gp.player.currentItem instanceof Plate plate) {
					if(plate.isDirty()) {
						if(sinkHitbox.intersects(gp.player.hitbox)) {
						    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
						    if(gp.keyI.ePressed && clickCooldown == 0) {
						    	if(currentPlate == null) {
							    	clickCooldown = 8;
							    	currentPlate = (Plate)gp.player.currentItem;
							    	gp.player.currentItem = null;
						    	} else {
						    		clickCooldown = 8;
							    	currentPlate.increasePlateStack();
							    	gp.player.currentItem = null;
						    	}
						    }
						}
					}
				}
			} else if(currentPlate != null) {
				if(sinkHitbox.intersects(gp.player.hitbox)) {
				    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
				    if(gp.keyI.ePressed) {
				    	chopCount++;
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
				}
			}
		}
		
		if(clickCooldown > 0) {
			clickCooldown--;
		}
		
		if(currentPlate != null) {
			BufferedImage img = animations[0][0][2];
			switch(currentPlate.getCurrentStackCount()) {
			case 2:
				img = animations[0][0][3];
				break;
			case 3:
				img = animations[0][0][4];
				break;
			}
			 g2.drawImage(img, (int) hitbox.x - xDrawOffset - gp.player.xDiff + 19, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset+30, 48, 48, null);

			
		    if(sinkHitbox.intersects(gp.player.hitbox)) {
		    	g2.drawImage(animations[0][0][5], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    } else {
				g2.drawImage(animations[0][0][6], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    }
		}
		
		if(cleanedPlateHitbox.intersects(gp.player.hitbox) && !sinkHitbox.intersects(gp.player.hitbox)) {
			if(gp.keyI.ePressed && clickCooldown == 0) {
				if(cleanedPlateCount > 0 && gp.player.currentItem == null) {
					cleanedPlateCount--;
					Plate plate = new Plate(gp, 0, 0);
					plate.setClean();
					plate.setCurrentStackCount(1);
					gp.player.currentItem = plate;
					litPlateImage = null;
					clickCooldown = 8;
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
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	}
	public void drawOverlayUI(Graphics2D g2) {
		if(currentPlate != null) {
			drawWashingBar(g2, hitbox.x+30, hitbox.y+32, chopCount, maxWashCount);
		}
	}
}

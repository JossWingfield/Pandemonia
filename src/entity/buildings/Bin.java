package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.Player;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.Plate;
import main.GamePanel;
import net.packets.Packet02Move;
import net.packets.Packet03PickupItem;
import net.packets.Packet06BinItem;

public class Bin extends Building {
	
	private Rectangle2D.Float binHitbox;
	private boolean firstUpdate = true;
	private int clickCooldown = 0;
	private boolean open = false;
	
	public Bin(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
	}
	public Building clone() {
		Bin building = new Bin(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Bin(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
        name = "Bin 1";
    	animations[0][0][0] = importImage("/decor/trapdoor.png").getSubimage(0, 32, 32, 32);
    	animations[0][0][1] = importImage("/decor/trapdoor.png").getSubimage(32, 32, 32, 32);
    	
    	isSolid = false;
    	drawHeight = 48*2;
    	drawWidth = 48*2;
    	xDrawOffset = 24;
    	yDrawOffset = 24;
	}
	public void draw(Graphics2D g2) {
		
		if(firstUpdate) {
			firstUpdate = false;
			binHitbox = new Rectangle2D.Float(hitbox.x + 22, hitbox.y+10, 10, 10);
		}
		
		if(!binHitbox.intersects(gp.player.interactHitbox)) {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			if(clickCooldown == 0 && gp.keyI.ePressed) {
				if(gp.player.currentItem != null) {
					if(!(gp.player.currentItem instanceof CookingItem) && !(gp.player.currentItem instanceof Plate)) {
						gp.player.currentItem = null;
						if(gp.multiplayer) {
				            Packet06BinItem packet = new Packet06BinItem(gp.player.getUsername());
				            packet.writeData(gp.socketClient);
			            }
					} else if(gp.player.currentItem instanceof Plate plate) {
						if(!plate.isDirty()) {
							plate.clearIngredients();
							if(gp.multiplayer) {
						        Packet03PickupItem packet = new Packet03PickupItem(
						        new Plate(gp, 0, 0).getName(),
						        gp.player.getUsername(),
						        0
						        );
						        packet.writeData(gp.socketClient);
				            }
						}
					}
				}
			}
		}
		
		if(gp.multiplayer) {
			for(Player p: gp.getPlayerList()) {
				if(p.getUsername() != gp.player.getUsername()) {
					if(p.currentRoomIndex == gp.player.currentRoomIndex) {
						if(binHitbox.intersects(p.interactHitbox)) {
							g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
						}
					}
				}
			}
		}
		
		if(clickCooldown > 0) {
			clickCooldown--;
		}
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}

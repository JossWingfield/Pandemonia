package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import utility.RecipeManager;

public class Trapdoor extends Building {
	
	private Rectangle2D.Float doorHitbox, entryHitbox, npcVisualHitbox;
	public Rectangle2D.Float npcHitbox;
	private boolean firstUpdate = true;
	public int roomNum = 6;
	public int cooldown = 0;
	public int type;
	
	public Trapdoor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.type = type;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		yDrawOffset = 24;
		xDrawOffset = 24;
		importImages();
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48 - 48, 48, 80);

	}
	public Building clone() {
		Trapdoor building = new Trapdoor(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Trapdoor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");
	}
	public void setDoorNum(int num) {
		roomNum = num;
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Trapdoor 1";
		if(type == 0) {
			animations[0][0][0] = importImage("/decor/trapdoor.png").getSubimage(64, 32, 32, 32);
    		animations[0][0][1] = importImage("/decor/trapdoor.png").getSubimage(64+32, 32, 32, 32);
    		isBottomLayer = true;
		} else {
			drawWidth = 32*3;
			drawHeight = 48*3;
			hitbox.width = 32*3;
			hitbox.height = 48*3;
			xDrawOffset = 0;
			yDrawOffset = 0;
			animations[0][0][0] = importImage("/tiles/beams/Multilevel.png").getSubimage(96, 0, 32, 48);
    		animations[0][0][1] = animations[0][0][0];
    		isBottomLayer = true;
    		roomNum = 3;
		}
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			entryHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 48, 48);
			if(type == 1) {
				entryHitbox.width = 96;
				entryHitbox.height = 48*3;
			}
			doorHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 48, 48);		
			npcVisualHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28-48, 48, 80);
			importImages();
		}  
		if(RecipeManager.areHauntedRecipesPresent() || type == 1) {
			
			if(cooldown > 0) {
				cooldown--;
			}
			
	        //g2.setColor(Color.YELLOW);
	      	//g2.drawRect((int)entryHitbox.x, (int)entryHitbox.y, (int)entryHitbox.width, (int)entryHitbox.height);
			 
			if(!gp.player.hitbox.intersects(doorHitbox) && !gp.npcM.stockerCheck(npcVisualHitbox)) {
			    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);		
			} else {
				g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
			
		   //g2.setColor(Color.YELLOW);
		   //g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
			
			if(gp.player.hitbox.intersects(entryHitbox)) {
				if(cooldown == 0) {
					if(gp.keyI.ePressed) {
						gp.keyI.ePressed = false;
						gp.mapM.changeRoom(roomNum, this);
					}
				}
			}
			
			if(destructionUIOpen) {
			    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
			}
			
		}
	        
	}
}

package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Door extends Building {
	
	private Rectangle2D.Float doorHitbox, entryHitbox, npcVisualHitbox;
	public Rectangle2D.Float npcHitbox;
	private boolean firstUpdate = true;
	public int roomNum = 1;
	public int facing;
	private int preset = 0;
	public int cooldown = 0;
	private boolean open = false;
	
	public Door(GamePanel gp, float xPos, float yPos, int facing, int presetNum) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.facing = facing;
		this.preset = presetNum;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 48*3;
		yDrawOffset = 48;
		importImages();
		if(facing == 0) {
			npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48 - 48, 48, 80);
		} else if(facing == 1) {
			npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y - 48 , 48, 48);
		} else if(facing == 2){ //LEFT
			isThirdLayer = true;
			hitbox.width = 24;
			npcHitbox = new Rectangle2D.Float(hitbox.x - 16, hitbox.y+80-48, 48, 48);
		} else if(facing == 3) { //RIGHT
			hitbox.width = 24;
			xDrawOffset += 24;
			npcHitbox = new Rectangle2D.Float(hitbox.x - 16-24, hitbox.y+64-48, 48, 64);
		}
	}
	public Building clone() {
		Door building = new Door(gp, hitbox.x, hitbox.y, facing, preset);
		return building;
    }
	public void printOutput() {
		//System.out.println("buildings[arrayCounter] = new Door(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + facing + ", " + preset + ");");
		//System.out.println("arrayCounter++;");	
	}
	public void setDoorNum(int num) {
		roomNum = num;
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Door 1";
		switch(preset) {
		case 0:
	    	if(facing == 1) { 
	        	animations[0][0][0] = importImage("/decor/DownDoor.png");
	        	animations[0][0][1] = animations[0][0][0];
	        	drawHeight = 48;
	        	isThirdLayer = true;
	    	} else if(facing == 0){ //0
	        	animations[0][0][0] = importImage("/decor/door.png").getSubimage(0, 48, 32, 48);
	        	animations[0][0][1] = importImage("/decor/door.png").getSubimage(32, 0, 32, 48);
	    	} else if(facing == 2 || facing == 3) {
	    		animations[0][0][0] = importImage("/decor/door.png").getSubimage(128, 48, 16, 48);
	        	animations[0][0][1] = animations[0][0][0];
	           	isFourthLayer = true;
	          	drawHeight = 48*3;
	          	drawWidth = 48;
	          	if(facing == 2) {
	          		animations[0][0][0] = createHorizontalFlipped(animations[0][0][0]);
	            	animations[0][0][1] = animations[0][0][0];
	          	}
	    	}
	    	break;
		case 1:
			animations[0][0][0] = importImage("/buildings/House.png").getSubimage(0, 160, 32, 32);
        	animations[0][0][1] = importImage("/buildings/House.png").getSubimage(32, 160, 32, 32);
        	drawHeight = 32*3;
			break;
		}
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			doorHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28-48, 48, 32);
			entryHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48-48, 48, 32);
			npcVisualHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28-48, 48, 80);
			
			if(facing == 2) {
				entryHitbox = new Rectangle2D.Float(hitbox.x-48, hitbox.y+32, 48, 48*2);
			} else if(facing == 3) {
				entryHitbox = new Rectangle2D.Float(hitbox.x+48, hitbox.y+32, 48, 48*2);
			}
			
			if(preset == 1) {
				entryHitbox.y -= 20;
				doorHitbox.y -= 20;
			}
			importImages();
		}  
		
		if(cooldown > 0) {
			cooldown--;
		}
		
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)entryHitbox.x, (int)entryHitbox.y, (int)entryHitbox.width, (int)entryHitbox.height);
      	
		 
		if(!gp.player.interactHitbox.intersects(doorHitbox) && !gp.npcM.stockerCheck(npcVisualHitbox)) {
			if(!open) {
			}
			open = true;
		    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);		
		} else {
			if(open) {
			}
			open = false;
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
		
	   //g2.setColor(Color.YELLOW);
	   //g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
		
		if(gp.player.interactHitbox.intersects(entryHitbox)) {
			if(cooldown == 0) {
				gp.mapM.changeRoom(roomNum, this);
			}
		}
		
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}

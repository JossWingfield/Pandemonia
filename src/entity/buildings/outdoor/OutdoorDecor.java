package entity.buildings.outdoor;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Building;
import entity.items.Item;
import main.GamePanel;
import utility.CollisionMethods;

public class OutdoorDecor extends Building {
	
	Random r;

	private int type;
	
	public Item currentItem = null;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	public OutdoorDecor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		
		r = new Random();
		
		importImages();
	}
	public Building clone() {
		OutdoorDecor building = new OutdoorDecor(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new OutdoorDecor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
        switch(type) {
        case 0:
        	name = "Restaurant 1";
         	animations[0][0][0] = importImage("/buildings/House.png").getSubimage(64, 0, 64, 80);
         	hitbox.width = 16*3*4;
         	hitbox.height = 16*3*3;
         	drawWidth = 64*3;
         	drawHeight=  80*3;
         	yDrawOffset = 28*3;
         	isSolid = true;
        	break;
        case 1:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(0, 0, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 2:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(16, 0, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 3:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(32, 0, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 4:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(48, 0, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 5:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(0, 16, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 6:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(16, 16, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 7:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(32, 16, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 8:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(48, 16, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 9:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(0, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 10:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(16, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 11:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(32, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 12:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(48, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 13:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(0, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 14:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(16, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 15:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(32, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 16:
        	name = "Fence";
         	animations[0][0][0] = importImage("/buildings/Fence.png").getSubimage(48, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 48;
         	isSolid = true;
        	break;
        case 17:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(16, 0, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 18:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(32, 0, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 19:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(48, 0, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 20:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(16, 32, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 21:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(32, 32, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 22:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(48, 32, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 23:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(16, 64, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 24:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(32, 64, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 25:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(48, 64, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 26:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(16, 96, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 27:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(32, 96, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 28:
        	name = "Bridge";
         	animations[0][0][0] = importImage("/buildings/Bridge - stone.png").getSubimage(48, 96, 16, 32);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 48;
         	drawHeight = 96;
         	yDrawOffset = 48;
         	isSolid = true;
        	break;
        case 29:
        	name = "Veggie Market";
         	animations[0][0][0] = importImage("/buildings/Greenhouse.png").getSubimage(64, 144, 96, 80);
         	hitbox.width = 16*3*5;
         	hitbox.height = 36*3;
         	drawWidth = 96*3;
         	drawHeight=  80*3;
         	xDrawOffset = 3*8;
         	yDrawOffset = 28*3;
         	isSolid = true;
        	break;
        case 30:
        	name = "Barn 2";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(80, 80, 128, 80);
         	hitbox.width = 48*7;
         	hitbox.height = 36*3;
         	drawWidth = 128*3;
         	drawHeight=  80*3;
         	xDrawOffset = 3*8;
         	yDrawOffset = 28*3;
         	isSolid = true;
        	break;
        case 31:
        	name = "Barn 1";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(0, 80, 80, 80);
         	hitbox.width = 48*4;
         	hitbox.height = 36*3;
         	drawWidth = 80*3;
         	drawHeight=  80*3;
         	xDrawOffset = 3*8;
         	yDrawOffset = 28*3;
         	isSolid = true;
        	break;
        case 32:
        	name = "House 1";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(0, 0, 64, 80);
         	hitbox.width = 48*3;
         	hitbox.height = 48*3;
         	drawWidth = 48*4;
         	drawHeight=  48*5;
         	xDrawOffset = 3*8;
         	yDrawOffset = 28*3;
         	isSolid = true;
        	break;
        case 33:
        	name = "House 2";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(64, 0, 96, 80);
         	hitbox.width = 80*3;
         	hitbox.height = 48*2;
         	drawWidth = 96*3;
         	drawHeight=  48*5;
         	xDrawOffset = 3*8;
         	yDrawOffset = (32)*3;
         	isSolid = true;
        	break;
        case 34:
        	name = "Farm Silo";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(160, 0, 48, 80);
         	hitbox.width = 48*2;
         	hitbox.height = 24*3;
         	drawWidth = 48*3;
         	drawHeight=  80*3;
         	xDrawOffset = 3*8;
         	yDrawOffset = (48)*3;
         	isSolid = true;
        	break;
        case 35:
        	name = "Farm Well";
         	animations[0][0][0] = importImage("/buildings/Farm.png").getSubimage(32, 160, 32, 48);
         	hitbox.width = 64;
         	hitbox.height = 48;
         	drawWidth = 48*2;
         	drawHeight=  48*3;
         	xDrawOffset = 4*3;
         	yDrawOffset = (28)*3;
         	isSolid = true;
        	break;
        case 36:
        	name = "House 3";
         	animations[0][0][0] = importImage("/buildings/House1.png").getSubimage(64, 0, 96, 80);
         	hitbox.width = 48*5;
         	hitbox.height = 48*3;
         	drawWidth = 96*3;
         	drawHeight=  80*3;
         	xDrawOffset = 4*3;
         	yDrawOffset = (4 + 24)*3;
         	isSolid = true;
        	break;
        case 37:
         	name = "Dirt Texture";
        	int variant = r.nextInt(12);
        	int cols = 4;
            int col = variant % cols;
            int row = variant / cols;
            int tileSize = 16;
            int sx = col * tileSize;
            int sy = row * tileSize;
            BufferedImage sheet = importImage("/environment/Texture.png");
            animations[0][0][0] = sheet.getSubimage(sx, sy, tileSize, tileSize);
            hitbox.width = tileSize * 3;
            hitbox.height = tileSize * 3;
            drawWidth = tileSize * 3;
            drawHeight = tileSize * 3;
            isSolid = false;
            isBottomLayer = true;
            break;
        }
		
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		if(invisHitbox == null) {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				BufferedImage img = animations[0][0][0];
				img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				g2.drawImage(img, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			} else {
			    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
		}
	     
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}
	
}

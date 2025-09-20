package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.items.Item;
import main.GamePanel;

public class CornerTable extends Building {
	
	private int presetNum;
	
	public Rectangle2D.Float interactHitbox1, interactHitbox2;
	private boolean firstUpdate = true;
	public Item slot1, slot2;
	
	public CornerTable(GamePanel gp, float xPos, float yPos, int presetNum) {
		super(gp, xPos, yPos, 48, 48);
		
		this.presetNum = presetNum;
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		isBottomLayer = true;
		importImages();
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		CornerTable building = new CornerTable(gp, hitbox.x, hitbox.y, presetNum);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new CornerTable(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + presetNum +  ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
	    animations = new BufferedImage[1][1][1];

	    name = "Table Corner 1";
	    interactHitbox1 = new Rectangle2D.Float(0, 0, 1, 1);
	    interactHitbox2 = new Rectangle2D.Float(0, 0, 1, 1);

	    switch(presetNum) {
	        case 0: // Top Left
	            animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 0, 32, 32);
	            hitbox.width = 48 + 24;
	            hitbox.height = 28;
	            drawWidth = 48 * 2;
	            drawHeight = 48 * 2;
	            xDrawOffset = 18;
	            yDrawOffset = 12 + 18;
	            break;

	        case 1: // Top Right
	            animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(96, 0, 32, 32);
	            hitbox.width = 48 + 24;
	            hitbox.height = 28;
	            drawWidth = 48 * 2;
	            drawHeight = 48 * 2;
	            xDrawOffset = 6;
	            yDrawOffset = 12 + 18;
	            break;

	        case 2: // Bottom Left
	            animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 32, 32, 32);
	            hitbox.width = 48 + 24;
	            hitbox.height = 38;
	            drawWidth = 48 * 2;
	            drawHeight = 48 * 2;
	            xDrawOffset = 18;
	            yDrawOffset = 12 + 18;

	            // Horizontal arm (rightward)
	            interactHitbox1 = new Rectangle2D.Float(hitbox.x + 24, hitbox.y + 0, 24, 12);
	            // Vertical arm (upward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x + 8, hitbox.y + 16, 12, 24);
	            break;

	        case 3: // Bottom Right
	            animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(96, 32, 32, 32);
	            hitbox.width = 48 + 24;
	            hitbox.height = 38;
	            drawWidth = 48 * 2;
	            drawHeight = 48 * 2;
	            xDrawOffset = 6;
	            yDrawOffset = 12 + 18;

	            // Horizontal arm (leftward)
	            interactHitbox1 = new Rectangle2D.Float(hitbox.x + 0, hitbox.y + 0, 24, 12);
	            // Vertical arm (upward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x + 32, hitbox.y + 16, 12, 24);
	            break;
	    }
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			int hitboxWidth = 24;
			int hitboxHeight = 40;
			switch(presetNum) {
			case 0:
	            // Horizontal arm (rightward)
	            interactHitbox1 = new Rectangle2D.Float(hitbox.x, hitbox.y + 40, hitboxHeight, hitboxWidth);
	            // Vertical arm (downward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x + 48, hitbox.y, hitboxWidth, hitboxHeight);
				break;
			case 1:
	            // Horizontal arm (leftward)
	            interactHitbox1 = new Rectangle2D.Float(hitbox.x + hitbox.width - 40, hitbox.y + 40, hitboxHeight, hitboxWidth);
	            // Vertical arm (downward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x, hitbox.y, hitboxWidth, hitboxHeight);
				break;
			case 2:
		        // Horizontal arm (rightward)
	            interactHitbox1 = new Rectangle2D.Float(hitbox.x+4, hitbox.y-32, hitboxHeight, hitboxWidth);
	            // Vertical arm (upward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x + 48, hitbox.y, hitboxWidth, hitboxHeight);
				break;
			case 3:
	            // Horizontal arm (leftward)
				interactHitbox1 = new Rectangle2D.Float(hitbox.x+28, hitbox.y-32, hitboxHeight, hitboxWidth);
	            // Vertical arm (upward)
	            interactHitbox2 = new Rectangle2D.Float(hitbox.x, hitbox.y, hitboxWidth, hitboxHeight);
				break;
			}
		}
		 
	     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
 
	    //g2.setColor(Color.YELLOW);
	    //g2.drawRect((int)interactHitbox1.x, (int)interactHitbox1.y, (int)interactHitbox1.width, (int)interactHitbox1.height);
	    //g2.drawRect((int)interactHitbox2.x, (int)interactHitbox2.y, (int)interactHitbox2.width, (int)interactHitbox2.height);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
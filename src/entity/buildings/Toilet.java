package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.npc.Customer;
import main.GamePanel;

public class Toilet extends Building {

	public boolean available = true;
	public Customer currentCustomer;
	
	public int facing;
	
	public Toilet(GamePanel gp, float xPos, float yPos, int facing) {
		super(gp, xPos, yPos, 48, 48);
		this.facing = facing;
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 24;
		yDrawOffset = 24;
		importImages();
		isSolid = true;
		npcHitbox = new Rectangle2D.Float(hitbox.x -16, hitbox.y - 16, 48 + 32, 48+32);
		if(facing == 0) {
			npcHitbox = new Rectangle2D.Float(hitbox.x +16, hitbox.y - 16, 48 + 32+32, 48+32);
		}
		isBathroomBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Toilet building = new Toilet(gp, hitbox.x, hitbox.y, facing);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Toilet(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + facing + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Toilet 1";
		if(facing == 0) { //RIGHT
	    	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(96, 160, 32, 32);
	    	animations[0][0][1] = importImage("/decor/bathroom props.png").getSubimage(96, 160+32, 32, 32);
		} else { //LEFT
	    	animations[0][0][0] = importImage("/decor/Toilet.png").getSubimage(0, 0, 32, 32);
	    	animations[0][0][1] = importImage("/decor/Toilet.png").getSubimage(32, 0, 32, 32);
		}
	}
	public void setCustomer(Customer customer) {
		currentCustomer = customer;
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		 		 
	    if(!available) {
		    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    } else {
		    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
        
	    
	}
	
}

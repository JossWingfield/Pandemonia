package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.npc.Customer;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Toilet extends Building {

	public boolean available = true;
	public Customer currentCustomer;
	
	public int facing;
	
	public Toilet(GamePanel gp, float xPos, float yPos, int facing) {
		super(gp, xPos, yPos, 48, 48);
		this.facing = facing;
		
		isSolid = true;
		
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
		if(facing == 0) {
			buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*5, hitbox.width-3*4, hitbox.height-3*5);
		} else {
			buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*5, hitbox.width-3*4, hitbox.height-3*5);
		}
	}
	public void onPlaced() {
		if(facing == 0) {
			buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*5, hitbox.width-3*4, hitbox.height-3*5);
		} else {
			buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*5, hitbox.width-3*4, hitbox.height-3*5);
		}
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
		animations = new TextureRegion[1][1][2];
		
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
	public void draw(Renderer renderer) {
		 		 
	    if(!available) {
		    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    } else {
		    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
        
	    
	}
	
}

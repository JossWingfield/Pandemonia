package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.items.Plate;
import entity.npc.Customer;
import main.GamePanel;

public class TablePlate extends Building {
	
	private int direction = -1;
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	private Chair chair;
	private Customer currentCustomer;
	private boolean orderCompleted = false;
	public boolean showDirtyPlate = false;
	public Plate plate;
	
	public TablePlate(GamePanel gp, float xPos, float yPos, int direction, Chair chair) {
		super(gp, xPos, yPos, 48, 48);
		this.direction = direction;
		
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		canBePlaced = false;
		//isSecondLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		this.chair = chair;
		switch(direction) {
		case 0:
			hitbox.y = chair.hitbox.y - 48;
			break;
		case 1:
			hitbox.x = chair.hitbox.x - 48;
			break;
		case 2:
			hitbox.x = chair.hitbox.x + 48;
			break;
		case 3:
			hitbox.y = chair.hitbox.y + 48;
			break;
		}
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*4, hitbox.width-3*8, hitbox.height-3*8);
	}
	public Building clone() {
		TablePlate building = new TablePlate(gp, hitbox.x, hitbox.y, direction, chair);
		return building;
    }
	public void printOutput() {
		//System.out.println("buildings[arrayCounter] = new TablePlate(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + direction + ");");
		//System.out.println("arrayCounter++;");	
	}
	private void initInteractHitbox() {
	        float baseX = hitbox.x;
	        float baseY = hitbox.y;

	        float horizontalWidth = 24;
	        float horizontalHeight = 16;

	        float verticalWidth = 16;
	        float verticalHeight = 24;

	        switch (direction) {
	            case 1: // RIGHT
	                interactHitbox = new Rectangle2D.Float(
	                    baseX + hitbox.width,
	                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
	                    horizontalWidth,
	                    horizontalHeight
	                );
	                break;
	            case 2: // LEFT
	                interactHitbox = new Rectangle2D.Float(
	                    baseX - horizontalWidth,
	                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
	                    horizontalWidth,
	                    horizontalHeight
	                );
	                break;
	            case 0: // DOWN
	                interactHitbox = new Rectangle2D.Float(
	                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
	                    baseY + hitbox.height,
	                    verticalWidth,
	                    verticalHeight
	                );
	        		//npcHitbox = new Rectangle2D.Float(hitbox.x - 32, hitbox.y - 32, hitbox.width+ 64, hitbox.height+64);
	                break;
	                
	            case 3: // UP
	                interactHitbox = new Rectangle2D.Float(
	                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
	                    baseY - verticalHeight,
	                    verticalWidth,
	                    verticalHeight
	                );
	                break;
	        }
	        npcHitbox = interactHitbox;
	    }
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Table Plate";
		switch(direction) {
		case 0: //UP
	    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(144, 80, 16, 16);
	    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(144+16, 80, 16, 16);
	    	break;
		case 1: //LEFT
	    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(144, 128, 16, 16);
	    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(144+16, 128, 16, 16);
			break;
		case 2: //RIGHT
	    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(144, 128+16, 16, 16);
	    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(144+16, 128+16, 16, 16);
			break;
		case 3: //DOWN
	    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(144, 128+32, 16, 16);
	    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(144+16, 128+32, 16, 16);
			break;
		}
	}
	public void update(double dt) {
		super.update(dt);
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		if(firstUpdate) {
			initInteractHitbox();
			firstUpdate = false;
		}
		if(!chair.available) {
			if(currentCustomer == null) {
				currentCustomer = chair.currentCustomer;
			}
		}
	     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);		
	     
		if(currentCustomer != null) {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);		
		}
		
	    //g2.setColor(Color.RED);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
	    
		if(currentCustomer != null) {
		    if(interactHitbox.intersects(gp.player.interactHitbox)) {
			    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			    if(gp.keyI.ePressed) {
				    if(currentCustomer != null) {
				    	if(gp.player.currentItem != null) {
				    		if(gp.player.currentItem instanceof Plate plate) {
				    			if(currentCustomer.foodOrder != null) {
					    			if(currentCustomer.foodOrder.matches(plate.getIngredients())) {
					    				currentCustomer.completeOrder(plate);
					    				orderCompleted = true;
					    			}
				    			}
				    		}
				    	}
				    }
			    }
		    }
		}
	    if(orderCompleted) {
	    	if(currentCustomer.isEating()) {
	    		g2.drawImage(currentCustomer.foodOrder.finishedPlate, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    	} else {
	    		orderCompleted = false;
	    		showDirtyPlate = true;
	       		plate = new Plate(gp, hitbox.x, hitbox.y);
	    		plate.setDirty(currentCustomer.foodOrder.dirtyPlate);
	    		plate.setCurrentStackCount(1);
	    	}
	    }
	    
	    if(showDirtyPlate) {
    		g2.drawImage(plate.dirtyImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    	if(gp.keyI.ePressed) {
		    	if(interactHitbox.intersects(gp.player.interactHitbox)) {
		    		if(gp.player.currentItem == null) {
		    			gp.player.currentItem = plate;
		    			showDirtyPlate = false;
			    		currentCustomer = null;
			    		plate = null;
		    		}
		    	}
	    	}
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
        
	        
	}
}

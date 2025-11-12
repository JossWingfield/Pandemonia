package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.items.Asparagus;
import entity.items.Aubergine;
import entity.items.Carrot;
import entity.items.Corn;
import entity.items.Food;
import entity.items.Greens;
import entity.items.Leek;
import entity.items.Potato;
import entity.items.RedOnion;
import entity.items.Tomato;
import main.GamePanel;
import net.packets.Packet03PickupItem;

public class FoodStore extends Building {
	
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	public int foodType;
	private BufferedImage highlightedImage;
	
	public FoodStore(GamePanel gp, float xPos, float yPos, int foodType) {
		super(gp, xPos, yPos, 48, 48);
		this.foodType = foodType;
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 32*3;
		isStoreBuilding = true;
		mustBePlacedOnFloor = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
	}
	public Building clone() {
		FoodStore building = new FoodStore(gp, hitbox.x, hitbox.y, foodType);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new FoodStore(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + this.foodType + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		highlightedImage = importImage("/decor/HighlightedStoreProps.png");
		
		name = "Food Store";
		switch(foodType) {
		case 0:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128, 0, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128, 0, 16, 32);
    		break;
		case 1:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+16, 0, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+16, 0, 16, 32);
			break;
		case 2:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+32, 0, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+32, 0, 16, 32);
			break;
		case 3:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+48, 0, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+48, 0, 16, 32);
			break;
		case 4:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+64, 0, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+64, 0, 16, 32);
			break;
		case 5:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128, 32, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128, 32, 16, 32);
    		break;
		case 6:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+16, 32, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+16, 32, 16, 32);
			break;
		case 7:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+32, 32, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+32, 32, 16, 32);
			break;
		case 8:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+48, 32, 16, 32);
    		animations[0][0][1] = highlightedImage.getSubimage(128+48, 32, 16, 32);
			break;
		case 9:
    		animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(128+64, 32, 16, 32);
    		animations[0][0][1] = importImage("/decor/general store props.png").getSubimage(128+64, 32, 16, 32);
			break;
		}
		yDrawOffset = 48;
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
			importImages();
		}
				
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			if(gp.keyI.ePressed) {
				if (gp.player.currentItem == null) {
				    String itemName = null;
				    switch(foodType) {
				        case 0 -> itemName = "Carrot";
				        case 1 -> itemName = "Red Onion";
				        case 2 -> itemName = "Potato";
				        case 3 -> itemName = "Corn";
				        case 4 -> itemName = "Tomato";
				        case 5 -> itemName = "Aubergine";
				        case 6 -> itemName = "Greens";
				        case 7 -> itemName = "Leek";
				        case 8 -> itemName = "Asparagus";
				    }

				    if (itemName != null) {
				        gp.player.currentItem = (Food) gp.itemRegistry.getItemFromName(itemName, 0);
				    	gp.player.resetAnimation(4);

				        if (gp.multiplayer) {
				            int state = gp.player.currentItem instanceof Food f ? f.getState() : 0;
				            Packet03PickupItem packet = new Packet03PickupItem(
				                gp.player.currentItem.getName(),
				                gp.player.getUsername(),
				                state
				            );
				            packet.writeData(gp.socketClient);
				        }
				    }
				}
			}
		} else {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
      		
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}


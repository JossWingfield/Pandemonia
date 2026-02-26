package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class FoodStore extends Building {
	
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	public int foodType;
	private Texture highlightedImage;
	
	public FoodStore(GamePanel gp, float xPos, float yPos, int foodType) {
		super(gp, xPos, yPos, 48, 48);
		this.foodType = foodType;
		
		isSolid = true;
		
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
		animations = new TextureRegion[1][1][2];
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
		case 10:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(16, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(16, 32, 16, 32);
			break;
		case 11:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(32, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(32, 32, 16, 32);
			break;
		case 12:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(48, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(48, 32, 16, 32);
			break;
		case 13:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(64, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(64, 32, 16, 32);
			break;
		case 14:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(80, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(80, 32, 16, 32);
			break;
		case 15:
    		animations[0][0][0] = importImage("/decor/HerbJars.png").getSubimage(96, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/HerbJars.png").getSubimage(96, 32, 16, 32);
			break;
		case 16:
    		animations[0][0][0] = importImage("/decor/SpiceBarrels.png").getSubimage(16, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/SpiceBarrels.png").getSubimage(16, 32, 16, 32);
			break;
		case 17:
    		animations[0][0][0] = importImage("/decor/SpiceBarrels.png").getSubimage(32, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/SpiceBarrels.png").getSubimage(32, 32, 16, 32);
			break;
		case 18:
    		animations[0][0][0] = importImage("/decor/SpiceBarrels.png").getSubimage(48, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/SpiceBarrels.png").getSubimage(48, 32, 16, 32);
			break;
		case 19:
    		animations[0][0][0] = importImage("/decor/SpiceBarrels.png").getSubimage(64, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/SpiceBarrels.png").getSubimage(64, 32, 16, 32);
			break;
		case 20:
    		animations[0][0][0] = importImage("/decor/AromaticSpices.png").getSubimage(0, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/AromaticSpices.png").getSubimage(0, 32, 16, 32);
			break;
		case 21:
    		animations[0][0][0] = importImage("/decor/AromaticSpices.png").getSubimage(16, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/AromaticSpices.png").getSubimage(16, 32, 16, 32);
			break;
		case 22:
    		animations[0][0][0] = importImage("/decor/AromaticSpices.png").getSubimage(32, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/AromaticSpices.png").getSubimage(32, 32, 16, 32);
			break;
		case 23:
    		animations[0][0][0] = importImage("/decor/AromaticSpices.png").getSubimage(48, 0, 16, 32);
    		animations[0][0][1] = importImage("/decor/AromaticSpices.png").getSubimage(48, 32, 16, 32);
			break;
		}
		yDrawOffset = 48;
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
			importImages();
		}
		
		if(interactHitbox != null) {
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
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
				        case 10 -> itemName = "Chamomile";
				        case 11 -> itemName = "Basil";
				        case 12 -> itemName = "Mint";
				        case 13 -> itemName = "Thyme";
				        case 14 -> itemName = "Rosemary";
				        case 15 -> itemName = "Sage";
				        case 16 -> itemName = "Paprika";
				        case 17 -> itemName = "Turmeric";
				        case 18 -> itemName = "Cumin";
				        case 19 -> itemName = "Chilli Flakes";
				        case 20 -> itemName = "Star Anise";
				        case 21 -> itemName = "Fennel Seeds";
				        case 22 -> itemName = "Coriander Seeds";
				        case 23 -> itemName = "Garlic Powder";
				    }

				    if (itemName != null) {
				        gp.player.currentItem = (Food) gp.world.itemRegistry.getItemFromName(itemName, 0);
				    	gp.player.resetAnimation(4);
				    }
				}
			}
		}
		}
		
	}
	public void draw(Renderer renderer) {
		if(interactHitbox == null) {
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
		}
		
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
		     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}


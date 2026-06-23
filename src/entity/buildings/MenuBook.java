package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Menu;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class MenuBook extends Building {
	
	private Rectangle2D.Float menuHitbox;
	private boolean firstUpdate = true;
	private int menuCount = 3;
	private int maxMenuCount = 3;
	
	public MenuBook(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y, 24, hitbox.height);
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*5, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*5, hitbox.height-3*4);
	}
	public Building clone() {
		MenuBook building = new MenuBook(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new MenuBook(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][3];
		
		menuCount = maxMenuCount;
		
        name = "Menu Book";
    	animations[0][0][0] = importImage("/decor/MenuBooks.png").getSubimage(0, 0, 16, 16);
    	animations[0][0][1] = importImage("/decor/MenuBooks.png").getSubimage(16, 0, 16, 16);
    	animations[0][0][2] = importImage("/decor/MenuBooks.png").getSubimage(32, 0, 16, 16);
    	
    	isThirdLayer = true;
	}
	public void addBook() {
		menuCount++;
	}
	public void takeBook() {
		menuCount--;
		gp.player.currentItem = new Menu(gp);
		gp.player.clickCounter = 0.1;
		gp.player.resetAnimation(4);
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(menuHitbox != null) {
			if(menuHitbox.intersects(gp.player.interactHitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
					if(menuCount > 0 && gp.player.currentItem == null) {
						takeBook();
					} else if(gp.player.currentItem != null && gp.player.currentItem instanceof Menu menu) {
						addBook();
						gp.player.clickCounter = 0.1;
						gp.player.currentItem = null;
					}
				}
			}
		}
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			menuHitbox = new Rectangle2D.Float(hitbox.x+16, hitbox.y+8, 16, 48-16);
		}
		
		if(menuCount == 0) {
			
		} else {
			int drawCount = menuCount-1;
			if(drawCount > 2) {
				drawCount = 2;
			}
		    renderer.draw(animations[0][0][drawCount], (int) hitbox.x - xDrawOffset, (int) (hitbox.y )-yDrawOffset, 48, 48);
		}
	}
}

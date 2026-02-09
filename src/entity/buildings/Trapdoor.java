package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.RecipeManager;

public class Trapdoor extends Building {
	
	private Rectangle2D.Float doorHitbox, entryHitbox, npcVisualHitbox;
	public Rectangle2D.Float npcHitbox;
	private boolean firstUpdate = true;
	public int roomNum = 6;
	public double cooldown = 0;
	public int type;
	
	public Trapdoor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.type = type;
		
		isSolid = false;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		yDrawOffset = 24;
		xDrawOffset = 24;
		canBePlaced = false;
		importImages();
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48 - 48, 48, 80);
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-9);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-9);
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
		animations = new TextureRegion[1][1][2];
		
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
	public void update(double dt) {
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
		if (cooldown > 0) {
		    cooldown -= dt;        // subtract elapsed time in seconds
		    if (cooldown < 0) {
		        cooldown = 0;      // clamp to zero
		    }
		}
		if(entryHitbox != null) {
			if(gp.player.hitbox.intersects(entryHitbox)) {
				if(cooldown == 0) {
					if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
						//gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) = false;
						gp.world.mapM.changeRoom(roomNum, this);
					}
				}
			}
		}
	}
	public void draw(Renderer renderer) { 
		if(RecipeManager.areHauntedRecipesPresent() || type == 1) {
			
	        //g2.setColor(Color.YELLOW);
	      	//g2.drawRect((int)entryHitbox.x, (int)entryHitbox.y, (int)entryHitbox.width, (int)entryHitbox.height);
			 
			if(!gp.player.hitbox.intersects(doorHitbox) && !gp.world.npcM.stockerCheck(npcVisualHitbox)) {
			    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);		
			} else {
				renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
			
		   //g2.setColor(Color.YELLOW);
		   //g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
			
			if(destructionUIOpen) {
			    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
			}
			
		}
	        
	}
}

package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Spill extends Building {
	
	private double spillCount = 0;
	private int maxSpillTime = 8;
	private Rectangle2D.Float effectArea;
	private boolean removedSpill = false;
	
	public Spill(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		isBottomLayer = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 24;
		yDrawOffset = 24;
		importImages();
		canBePlaced = false;
		effectArea = new Rectangle2D.Float(hitbox.x - 32, hitbox.y - 32, hitbox.width + 64, hitbox.height + 64);
	}
	public Building clone() {
		Spill building = new Spill(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Spill(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Spill";
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(96, 96, 32, 32);
    	animations[0][0][1] = importImage("/decor/SpillHighlight.png").toTextureRegion();
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(hitbox.intersects(gp.player.interactHitbox)) {
		    if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
		    	spillCount+=dt;
		    	if(spillCount >= maxSpillTime) {
		    		spillCount = 0;
		    		gp.player.setNormalSpeed();
		    		removedSpill = true;
		    		gp.world.buildingM.removeBuilding(this);
		    	}
		    }
		}
	}
	public void draw(Renderer renderer) {
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
		    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    } else {
		    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
	    if(spillCount > 0) {
	    	drawChoppingBar(renderer, hitbox.x+24, hitbox.y+24, (int)spillCount, (int)maxSpillTime);
	    }
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	private void drawChoppingBar(Renderer renderer, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset ;
	    float screenY = worldY - yDrawOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	
}

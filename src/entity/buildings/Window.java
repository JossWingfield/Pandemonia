package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class Window extends Building {
	
	TextureRegion glowImage;
	
	private boolean firstDraw = true;
	private boolean lightsOn = true;
	private LightSource light;
	
	public Window(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.presetNum = preset;
		
		isSolid = false;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 24;
		yDrawOffset = 24;
		importImages();
		isDecor = true;
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public Building clone() {
		Window calendar = new Window(gp, hitbox.x, hitbox.y, presetNum);
		return calendar;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Window(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + this.presetNum + ");");
		System.out.println("arrayCounter++;");	
	}
	public void destroy() {
		if(lightsOn) {
			gp.lightingM.removeLight(light);
		}
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
     	
     	switch(presetNum) {
     	case 0:
    		name = "Window 1";
         	animations[0][0][0] = importImage("/decor/window.png").getSubimage(32, 32, 32, 32);
         	animations[0][0][1] = importImage("/decor/window.png").getSubimage(0, 32, 32, 32);
         	glowImage = importImage("/decor/WindowGlow.png").getSubimage(32, 32, 32, 32);
     		break;
     	case 1:
     		name = "Porthole";
         	animations[0][0][0] = importImage("/decor/catalogue/fishingshack/FishShackWindow.png").getSubimage(32, 0, 32, 32);
         	animations[0][0][1] = importImage("/decor/catalogue/fishingshack/FishShackWindow.png").getSubimage(0, 0, 32, 32);
         	glowImage = importImage("/decor/catalogue/fishingshack/FishShackWindow.png").getSubimage(64, 0, 32, 32);
     		break;
    	case 2:
     		name = "Farm Window";
         	animations[0][0][0] = importImage("/decor/window.png").getSubimage(96, 32, 32, 32);
         	animations[0][0][1] = importImage("/decor/window.png").getSubimage(64, 32, 32, 32);
         	glowImage = importImage("/decor/WindowGlow.png").getSubimage(96, 32, 32, 32);
     		break;
     	}
     	
	}
	public void draw(Renderer renderer) {
		if(firstDraw) {
			firstDraw = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),
					Colour.WHITE, 50);
			light.setIntensity(0.2f);
			gp.lightingM.addLight(light);
		}
		int state = 0;
		if(gp.world.getTimeOfDay() == 5) {
			state = 1;
			if(lightsOn) {
				gp.lightingM.removeLight(light);
				lightsOn = false;
			}
		} else {
			if(!lightsOn) {
				gp.lightingM.addLight(light);
				lightsOn = true;
			}
		}
		
	     renderer.draw(animations[0][0][state], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}
	public void drawGodRay(Renderer renderer) {
		if(gp.world.getTimeOfDay() != 5) {
		     renderer.draw(glowImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
	}
	
}

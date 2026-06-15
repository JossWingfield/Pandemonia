package entity.buildings.outdoor;

import java.awt.geom.Rectangle2D;

import entity.buildings.Building;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class OutdoorWindow extends Building {
	
	TextureRegion glowImage;
	
	private boolean firstDraw = true;
	private boolean lightsOn = false;
	private LightSource light;
	
	public OutdoorWindow(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.presetNum = preset;
		
		isSolid = false;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 24;
		yDrawOffset = 24;
		importImages();
		isDecor = true;
		castsShadow = false;
		canBePlaced = false;
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public Building clone() {
		OutdoorWindow calendar = new OutdoorWindow(gp, hitbox.x, hitbox.y, presetNum);
		return calendar;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new OutdoorWindow(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + this.presetNum + ");");
		System.out.println("arrayCounter++;");	
	}
	public void destroy() {
		if(lightsOn) {
			gp.world.lightingM.removeLight(light);
		}
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
     	
     	switch(presetNum) {
     	case 0:
         	animations[0][0][0] = importImage("/buildings/ButcherWindow.png").getSubimage(0, 0, 16, 16);
        	animations[0][0][1] = importImage("/buildings/ButcherWindow.png").getSubimage(32, 0, 16, 16);
         	glowImage = importImage("/buildings/ButcherWindow.png").getSubimage(16, 0, 16, 16);
        	drawWidth = 16*3;
    		drawHeight = 16*3;
    		xDrawOffset = 0;
    		yDrawOffset = 0;
     		break;
      	case 1:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(0, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(0, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(0, 32, 32, 32);
     		break;
    	case 2:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32, 32, 32, 32);
     		break;
    	case 3:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*2, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*2, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*2, 32, 32, 32);
     		break;
    	case 4:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*3, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*3, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*3, 32, 32, 32);
     		break;
    	case 5:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*4, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*4, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*4, 32, 32, 32);
     		break;
    	case 6:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*5, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*5, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*5, 32, 32, 32);
     		break;
    	case 7:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*6, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*6, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*6, 32, 32, 32);
     		break;
    	case 8:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*7, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*7, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*7, 32, 32, 32);
     		break;
    	case 9:
         	animations[0][0][0] = importImage("/buildings/windows.png").getSubimage(32*8, 0, 32, 32);
        	animations[0][0][1] = importImage("/buildings/windows.png").getSubimage(32*8, 64, 32, 32);
         	glowImage = importImage("/buildings/windows.png").getSubimage(32*8, 32, 32, 32);
     		break;
     	}
     	isThirdLayer = true;
     	name = "Outdoor Window";
     	
	}
	public void draw(Renderer renderer) {
		if(firstDraw) {
			firstDraw = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),
					new Colour("#fae188"), 50);
			light.setIntensity(0.2f);
		}
		int state = 0;
		if(gp.world.gameM.getTimeOfDay() == 0 || gp.world.gameM.getTimeOfDay() == 1) {
			if(lightsOn) {
				gp.world.lightingM.removeLight(light);
				lightsOn = false;
			}
		} else {
			state = 1;
			if(!lightsOn) {
				gp.world.lightingM.addLight(light);
				lightsOn = true;
			}
		}
		
	    renderer.draw(animations[0][0][state], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	}
	public void drawEmissive(Renderer renderer) {
		if(!(gp.world.gameM.getTimeOfDay() == 0 || gp.world.gameM.getTimeOfDay() == 1)) {
		     renderer.draw(glowImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
	}
	public void drawGodRay(Renderer renderer) {
		if(!(gp.world.gameM.getTimeOfDay() == 0 || gp.world.gameM.getTimeOfDay() == 1)) {
		     renderer.draw(glowImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
	}
	
}

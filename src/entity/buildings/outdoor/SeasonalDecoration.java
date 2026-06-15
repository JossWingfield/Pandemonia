package entity.buildings.outdoor;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import entity.buildings.Building;
import entity.items.Item;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class SeasonalDecoration extends Building {

	Random r;
	
	private int type;
	public Item currentItem = null;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	private int variant = 0;
	
	public SeasonalDecoration(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		r = new Random();
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
	}
	public Building clone() {
		SeasonalDecoration building = new SeasonalDecoration(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new SeasonalDecoration(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[4][10][10];
		
        switch(type) {
        case 0:
        	name = "Small Bush";
        	importSeasonalImages("Bush", 0, 0, 32, 32);
         	hitbox.width = 23*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 4*3;
          	yDrawOffset = 16*3;
          	isSolid = false;
        	break;
        case 1:
        	name = "Small Bush";
        	importSeasonalImages("PineTree", 0, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 2:
        	name = "Small Bush";
        	importSeasonalImages("BirchTree", 0, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 3:
        	name = "Small Bush";
        	importSeasonalImages("OakTree", 0, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 4:
        	name = "Small Bush";
        	importSeasonalImages("PineTree", 48, 0, 48, 48);
          	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 5:
        	name = "Small Bush";
        	importSeasonalImages("BirchTree", 48, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 6:
        	name = "Small Bush";
        	importSeasonalImages("OakTree", 48, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 7:
        	name = "Small Bush";
        	importSeasonalImages("PineTree", 48*2, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 8:
        	name = "Small Bush";
        	importSeasonalImages("BirchTree", 48*2, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 9:
        	name = "Small Bush";
        	importSeasonalImages("OakTree", 48*2, 0, 48, 48);
         	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 10:
        	importSeasonalAnimation("WaterRocks", 5, 1, 0, 0, 32, 32);
         	hitbox.width = 11*3;
           	hitbox.height = 9*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 11*3;
        	break;
        case 11:
        	importSeasonalAnimation("WaterRocks", 5, 1, 0, 32, 32, 32);
         	hitbox.width = 11*3;
           	hitbox.height = 9*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 11*3;
        	break;
        case 12:
        	importSeasonalAnimation("WaterRocks", 5, 1, 0, 64, 32, 32);
         	hitbox.width = 11*3;
           	hitbox.height = 9*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 11*3;
        	break;
        case 13:
        	importSeasonalAnimation("Lilypad", 5, 1, 0, 64, 32, 32);
         	hitbox.width = 11*3;
           	hitbox.height = 9*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 11*3;
        	break;
        }
        
       	name = "Small Bush";
	}
	private void importSeasonalImages(String imagePath, int x, int y, int w, int h) {
     	drawWidth = w*3;
       	drawHeight = h*3;
       	
    	animations[0][0][0] = importImage("/itch/environment/spring/" + imagePath + ".png").getSubimage(x, y, w, h);
    	animations[1][0][0] = importImage("/itch/environment/summer/" + imagePath + ".png").getSubimage(x, y, w, h);
    	animations[2][0][0] = importImage("/itch/environment/autumn/" + imagePath + ".png").getSubimage(x, y, w, h);
    	animations[3][0][0] = importImage("/itch/environment/winter/" + imagePath + ".png").getSubimage(x, y, w, h);
	}
	private void importSeasonalAnimation(String imagePath, int columnNumber,int rowNumber, int startX, int startY, int width, int height) {
     	isSolid = false;
     	drawWidth = width*3;
       	drawHeight = height*3;
       	
		importFromSpriteSheet("/itch/environment/spring/" + imagePath + ".png", columnNumber, rowNumber, 0, startX, startY, width, height, 0);
		importFromSpriteSheet("/itch/environment/summer/" + imagePath + ".png", columnNumber, rowNumber, 0, startX, startY, width, height, 1);
		importFromSpriteSheet("/itch/environment/autumn/" + imagePath + ".png", columnNumber, rowNumber, 0, startX, startY, width, height, 2);
    	animations[3][0][0] = importImage("/itch/environment/winter/" + imagePath + ".png").getSubimage(startX, startY, width, height);
	}
	public void updateState(double dt) {
		animationSpeed+=dt; //Updating animation frame
        if (animationSpeed >= animationSpeedFactor) {
        	animationSpeed = 0;
            animationCounter++;
        }
        int season = -1;
		switch(gp.world.gameM.getCurrentSeason()) {
		case SPRING:
			season = 0;
			break;
		case SUMMER:
			season = 1;
			break;
		case AUTUMN:
			season = 2;
			break;
		case WINTER:
			season = 3;
			break;
		}
        if (animations[season][currentAnimation][animationCounter] == null) { //If the next frame is empty
        	animationCounter = 0;
        }
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		int season = -1;
		switch(gp.world.gameM.getCurrentSeason()) {
		case SPRING:
			season = 0;
			break;
		case SUMMER:
			season = 1;
			break;
		case AUTUMN:
			season = 2;
			break;
		case WINTER:
			season = 3;
			break;
		}
		
	    renderer.draw(animations[season][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}
	
}
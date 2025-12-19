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
		animations = new TextureRegion[1][1][10];
		
        switch(type) {
        case 0:
        	name = "Small Bush";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Bush.png").getSubimage(variant*16, 0, 16, 16);
         	animations[0][0][1] = importImage("/environment/Bush.png").getSubimage(64 + variant*16, 0, 16, 16);
         	animations[0][0][2] = importImage("/environment/Bush.png").getSubimage(variant*16, 32, 16, 16);
         	animations[0][0][3] = importImage("/environment/Bush.png").getSubimage(64 + variant*16, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = true;
        	break;
        case 1:
        	name = "Large Bush";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Bush.png").getSubimage(variant*16, 16, 16, 16);
         	animations[0][0][1] = importImage("/environment/Bush.png").getSubimage(64 + variant*16, 16, 16, 16);
         	animations[0][0][2] = importImage("/environment/Bush.png").getSubimage(variant*16, 48, 16, 16);
         	animations[0][0][3] = importImage("/environment/Bush.png").getSubimage(64 + variant*16, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = true;
        	break;
        case 2:
            name = "Grass";
            variant = r.nextInt(24);

            int cols = 4;
            int tileSize = 16;
            int col = variant % cols;
            int row = variant / cols;
            int sx = col * tileSize;
            int sy = row * tileSize;
            Texture sheet = importImage("/environment/Grass.png");
            animations[0][0][0] = sheet.getSubimage(sx, sy, tileSize, tileSize);
            animations[0][0][1] = sheet.getSubimage(sx + 64, sy, tileSize, tileSize);
            animations[0][0][2] = sheet.getSubimage(sx+128, sy, tileSize, tileSize);
            animations[0][0][3] = sheet.getSubimage(sx, sy, tileSize, tileSize);
            hitbox.width = tileSize * 3;
            hitbox.height = tileSize * 3;
            drawWidth = tileSize * 3;
            drawHeight = tileSize * 3;
            isSolid = false;
            break;
        case 3:
        	name = "Big Tree";
        	variant = r.nextInt(4);
        	switch(variant) {
        	case 0, 2, 3:
        	 	animations[0][0][0] = importImage("/environment/Tree.png").getSubimage(0, variant*48, 32, 48);
         		animations[0][0][1] = importImage("/environment/Tree.png").getSubimage(64, variant*48, 32, 48);
         		animations[0][0][2] = importImage("/environment/Tree.png").getSubimage(160, variant*48, 32, 48);
         		animations[0][0][3] = importImage("/environment/Tree.png").getSubimage(192, variant*48, 32, 48);
         		animations[0][0][4] = importImage("/environment/Tree.png").getSubimage(32, variant*48, 32, 48);
         		animations[0][0][5] = importImage("/environment/Tree.png").getSubimage(128, variant*48, 32, 48);
         		break;
        	case 1:
        	 	animations[0][0][0] = importImage("/environment/Tree.png").getSubimage(0, 1*48, 32, 48);
           	 	animations[0][0][1] = importImage("/environment/Tree.png").getSubimage(32, 1*48, 32, 48);
           	 	animations[0][0][2] = importImage("/environment/Tree.png").getSubimage(32, 1*48, 32, 48);
           	 	animations[0][0][3] = importImage("/environment/Tree.png").getSubimage(48, 1*48, 32, 48);
        		break;
        	}
        	
         	hitbox.width = 12*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3*2;
         	drawHeight=  16*3*3;
         	xDrawOffset = 32;
        	yDrawOffset = 44*2;
         	isSolid = true;
        	break;
        case 4:
        	name = "Small Tree";
        	variant = r.nextInt(4);
        	switch(variant) {
        	case 0, 2, 3:
        	 	animations[0][0][0] = importImage("/environment/Tree.png").getSubimage(256, variant*48, 32, 48);
         		animations[0][0][1] = importImage("/environment/Tree.png").getSubimage(64 + 256, variant*48, 32, 48);
         		animations[0][0][2] = importImage("/environment/Tree.png").getSubimage(160 + 256, variant*48, 32, 48);
         		animations[0][0][3] = importImage("/environment/Tree.png").getSubimage(192 + 256, variant*48, 32, 48);
         		animations[0][0][4] = importImage("/environment/Tree.png").getSubimage(32 + 256, variant*48, 32, 48);
         		animations[0][0][5] = importImage("/environment/Tree.png").getSubimage(128 + 256, variant*48, 32, 48);
         		break;
        	case 1:
        	 	animations[0][0][0] = importImage("/environment/Tree.png").getSubimage(0 + 256, 1*48, 32, 48);
           	 	animations[0][0][1] = importImage("/environment/Tree.png").getSubimage(32 + 256, 1*48, 32, 48);
           	 	animations[0][0][2] = importImage("/environment/Tree.png").getSubimage(32 + 256, 1*48, 32, 48);
           	 	animations[0][0][3] = importImage("/environment/Tree.png").getSubimage(48 + 256, 1*48, 32, 48);
        		break;
        	}
        	hitbox.width = 12*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3*2;
         	drawHeight=  16*3*3;
         	xDrawOffset = 32;
        	yDrawOffset = 44*2;
         	isSolid = true;
        	break;
        case 5:
        	name = "Grass Tuft 1";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Tufts and Lumps.png").getSubimage(variant*16, 0, 16, 16);
         	animations[0][0][1] = importImage("/environment/Tufts and Lumps.png").getSubimage(variant*16, 32, 16, 16);
         	animations[0][0][2] = importImage("/environment/Tufts and Lumps.png").getSubimage(64+variant*16, 0, 16, 16);
         	animations[0][0][3] = importImage("/environment/Tufts and Lumps.png").getSubimage(64+variant*16, 32, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = false;
        	isBottomLayer = true;
        	break;
        case 6:
        	name = "Grass Tuft 2";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Tufts and Lumps.png").getSubimage(variant*16, 16, 16, 16);
         	animations[0][0][1] = importImage("/environment/Tufts and Lumps.png").getSubimage(variant*16, 48, 16, 16);
         	animations[0][0][2] = importImage("/environment/Tufts and Lumps.png").getSubimage(64+variant*16, 16, 16, 16);
         	animations[0][0][3] = importImage("/environment/Tufts and Lumps.png").getSubimage(64+variant*16, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = false;
         	isBottomLayer = true;
        	break;
        case 7:
        	name = "Small Ground Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 0, 16, 16);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 0, 16, 16);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 0, 16, 16);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(128+variant*16, 0, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = false;
        	break;
        case 8:
        	name = "Medium Ground Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 0, 16, 16);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 0, 16, 16);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 0, 16, 16);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(64+128+variant*16, 0, 16, 16);
         	hitbox.width = 12*3;
         	hitbox.height = 12*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	xDrawOffset = 2*3;
        	yDrawOffset = 2*3;
         	isSolid = true;
        	break;
        case 9:
        	name = "Big Ground Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 16, 32, 32);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 16, 32, 32);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 16, 32, 32);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(128+variant*32, 16, 32, 32);
         	hitbox.width = 24*3;
         	hitbox.height = 16*3;
         	drawWidth = 32*3;
         	drawHeight=  32*3;
         	xDrawOffset = 4*3;
        	yDrawOffset = 8*3;
         	isSolid = true;
        	break;
        case 10:
        	name = "Small Water Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 48, 16, 16);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 96, 16, 16);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(variant*16, 144, 16, 16);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(128+variant*16, 48, 16, 16);
         	hitbox.width = 16*3;
         	hitbox.height = 16*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	isSolid = false;
        	break;
        case 11:
        	name = "Medium Water Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 48, 16, 16);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 96, 16, 16);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(64 + variant*16, 144, 16, 16);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(64+128+variant*16, 48, 16, 16);
         	hitbox.width = 12*3;
         	hitbox.height = 12*3;
         	drawWidth = 16*3;
         	drawHeight=  16*3;
         	xDrawOffset = 2*3;
        	yDrawOffset = 2*3;
         	isSolid = true;
        	break;
        case 12:
        	name = "Big Ground Stone";
        	variant = r.nextInt(4);
         	animations[0][0][0] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 64, 32, 32);
           	animations[0][0][1] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 112, 32, 32);
           	animations[0][0][2] = importImage("/environment/Stone and Rock.png").getSubimage(variant*32, 160, 32, 32);
         	animations[0][0][3] = importImage("/environment/Stone and Rock.png").getSubimage(128+variant*32, 64, 32, 32);
         	hitbox.width = 24*3;
         	hitbox.height = 16*3;
         	drawWidth = 32*3;
         	drawHeight=  32*3;
         	xDrawOffset = 4*3;
        	yDrawOffset = 8*3;
         	isSolid = true;
        	break;
        case 13:
         	name = "Water Plants";
        	variant = r.nextInt(16);
        	cols = 8;
            col = variant % cols;
            row = variant / cols;
            tileSize = 16;
            sx = col * tileSize;
            sy = row * tileSize;
            sheet = importImage("/environment/Water Plants.png");
            animations[0][0][0] = sheet.getSubimage(sx, sy, tileSize, tileSize);
            animations[0][0][1] = sheet.getSubimage(sx, sy+32, tileSize, tileSize);
            animations[0][0][2] = sheet.getSubimage(sx, sy+64, tileSize, tileSize);
            animations[0][0][3] = sheet.getSubimage(sx, sy, tileSize, tileSize);
            hitbox.width = tileSize * 3;
            hitbox.height = tileSize * 3;
            drawWidth = tileSize * 3;
            drawHeight = tileSize * 3;
            isSolid = false;
            break;
        }
		
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		int season = -1;
		switch(gp.world.getCurrentSeason()) {
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
		if(invisHitbox == null) {
		     renderer.draw(animations[0][0][season], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				TextureRegion img = animations[0][0][season];
				//img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				renderer.draw(img, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			    renderer.draw(animations[0][0][season], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
		}
	     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}
	
}
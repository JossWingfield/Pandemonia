package entity.buildings;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class CursedDecor extends Building {
	
	public int preset;
	
	public CursedDecor(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.preset = preset;
		
		isSolid = true;
		
		hitbox.width = 48;
		hitbox.height = 48*2;
		drawWidth = 48;
		drawHeight = 48*2;
		animationSpeedFactor = 6;
		mustBePlacedOnFloor = true;
		isDecor = true;
		canBePlaced = false;
		importImages();
	}
	public Building clone() {
		CursedDecor building = new CursedDecor(gp, hitbox.x, hitbox.y, preset);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new CursedDecor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + preset + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][10];
		
		switch(preset) {
		case 0:
			mustBePlacedOnFloor = true;
			name = "Grandfather Clock";
			importFromSpriteSheet("/decor/grandfather clock.png", 8, 1, 0, 0, 0, 16, 32, 0);
			hitbox.width = 32;
			hitbox.height = 48 + 32;
			animationSpeedFactor = 3;
			drawWidth = 48;
			drawHeight = 48*2;
			xDrawOffset = 8;
			yDrawOffset = 24;
			break;
		case 1:
			mustBePlacedOnFloor = true;
			name = "Banner";
			isSolid = false;
			animations[0][0][0] = importImage("/decor/banner.png").getSubimage(0, 32, 16, 32);
			hitbox.width = 48;
			hitbox.height = 48+32;
			drawWidth = 48;
			drawHeight = 48*2;
			yDrawOffset = 24;
			break;
		case 2:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			animations[0][0][0] = importImage("/decor/Ruined Carpet.png").toTextureRegion();
			hitbox.width = 208*3;
			hitbox.height = 48*3;
			drawWidth = 208*3;
			drawHeight = 48*3;
        	isBottomLayer = true;
			break;
		case 3:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(0, 0, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 4:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(16, 0, 32, 32);;
			hitbox.width = 32*3;
			hitbox.height = 16*3;
			drawWidth = 32*3;
			drawHeight = 32*3;
			yDrawOffset = 24;
			break;
		case 5:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isBottomLayer = true;
			isSolid = false;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(48, 0, 16, 32);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 32*3;
			break;
		case 6:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isBottomLayer = true;
			isSolid = false;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(64, 0, 16, 32);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 32*3;
			break;
		case 7:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(80, 0, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 8:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isBottomLayer = true;
			isSolid = false;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96, 0, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 9:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96+16, 0, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 10:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(80, 16, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 11:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96, 16, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 12:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(112, 16, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 13:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(80, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 14:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 15:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(112, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 16:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(0, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 17:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(16, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 18:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(32, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 19:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(48, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 20:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(64, 32, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 21:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(0, 48, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 22:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(16, 48, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 23:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96, 48, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 24:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(112, 48, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 25:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(32, 48, 32, 32);
			hitbox.width = 32*3;
			hitbox.height = 32*3;
			drawWidth = 32*3;
			drawHeight = 32*3;
			xDrawOffset = 24;
			yDrawOffset = 24;
			break;
		case 26:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(64, 48, 32, 32);
			hitbox.width = 32*3;
			hitbox.height = 32*3;
			drawWidth = 32*3;
			drawHeight = 32*3;
			xDrawOffset = 24;
			yDrawOffset = 24;
			break;
		case 27:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(96, 64, 16, 32);
			hitbox.width = 16*3;
			hitbox.height = 32*3;
			drawWidth = 16*3;
			drawHeight = 32*3;
			yDrawOffset = 24;
			break;
		case 28:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(112, 64, 16, 32);
			hitbox.width = 16*3;
			hitbox.height = 32*3;
			drawWidth = 16*3;
			drawHeight = 32*3;
			yDrawOffset = 24;
			break;
		case 29:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			isBottomLayer = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(16, 80, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 30:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(0, 64, 32, 32);
			hitbox.width = 32*3;
			hitbox.height = 16*3;
			drawWidth = 32*3;
			drawHeight = 32*3;
			yDrawOffset = 24;
			break;
		case 31:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = true;
			animations[0][0][0] = importImage("/decor/CorridorProps.png").getSubimage(128, 0, 48, 64);
			hitbox.width = 48*3;
			hitbox.height = 64*3;
			drawWidth = 48*3;
			drawHeight = 64*3;
			xDrawOffset = 24;
			yDrawOffset = 24;
			break;
		case 32:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			animations[0][0][0] = importImage("/food/food.png").getSubimage(0, 112, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 33:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			animations[0][0][0] = importImage("/food/food.png").getSubimage(16, 112, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 34:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			animations[0][0][0] = importImage("/food/food.png").getSubimage(32, 112, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		case 35:
			mustBePlacedOnFloor = true;
			name = "Mess";
			isSolid = false;
			animations[0][0][0] = importImage("/food/food.png").getSubimage(48, 112, 16, 16);
			hitbox.width = 16*3;
			hitbox.height = 16*3;
			drawWidth = 16*3;
			drawHeight = 16*3;
			break;
		}


	}
	public void draw(Renderer renderer) {
		
		animationSpeed++; //Updating animation frame
        if (animationSpeed == animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }

        if (animations[direction][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }		
        
	    renderer.draw(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
        
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x , (int) (hitbox.y ), gp.tileSize, gp.tileSize);
		}
	        
	}
}

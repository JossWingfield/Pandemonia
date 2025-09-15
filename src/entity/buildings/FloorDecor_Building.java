package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Building;
import entity.items.Food;
import entity.items.Item;
import entity.items.Plate;
import main.GamePanel;
import utility.CollisionMethods;

public class FloorDecor_Building extends Building {

	private int type;
	
	public Item currentItem = null;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	public Rectangle2D.Float interactHitbox;
	private int interactSize = 32;
	
	public FloorDecor_Building(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
	}
	public Building clone() {
		FloorDecor_Building building = new FloorDecor_Building(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new FloorDecor_Building(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
        switch(type) {
        case 0:
            name = "Plant 1";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
        	break;
        case 1:
            name = "Plant 2";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
        	break;
        case 2:
            name = "Plant 3";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
        	break;
        case 3:
            name = "Plant 4";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
        	break;
        case 4:
            name = "Table Piece"; //EDGE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(0, 0, 16, 32);
        	hitbox.height = 38;
        	hitbox.width = 24;
        	drawHeight = 48*2;
        	yDrawOffset = 30;
        	xDrawOffset = 24;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 5:
            name = "Table Piece"; //MIDDLE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(16, 0, 16, 32);
        	hitbox.height = 38;
        	yDrawOffset = 30;
        	drawHeight = 48*2;
    		isBottomLayer = true;
    		interactHitbox = new Rectangle2D.Float(hitbox.x + interactSize / 2, hitbox.y, interactSize, hitbox.height);
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
    		break;
        case 6:
            name = "Table Piece"; //EDGE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(32, 0, 16, 32);
        	hitbox.height = 38;
        	hitbox.width = 24;
        	drawHeight = 48*2;
          	yDrawOffset = 30;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 7:
            name = "Table Piece"; //EDGE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(0, 32, 32, 16);
        	hitbox.width = 48;
        	hitbox.height = 28;
        	drawWidth = 48*2;
        	xDrawOffset = 18;
        	yDrawOffset = 30;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 8:
            name = "Table Piece"; //MIDDLE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(0, 48, 32, 16);
        	hitbox.width = 48;
        	xDrawOffset = 18;
        	drawWidth = 48*2;
    		isBottomLayer = true;
    		interactHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+interactSize / 2, hitbox.width, interactSize);
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
    		break;
        case 9:
            name = "Table Piece"; //EDGE
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(0, 64, 32, 16);
        	hitbox.width = 48;
        	hitbox.height = 20;
        	drawWidth = 48*2;
        	xDrawOffset = 18;
        	yDrawOffset = 12;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 10: //EDGE
            name = "Table Piece";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(32, 32, 32, 16);
        	hitbox.width = 48;
        	hitbox.height = 28;
        	drawWidth = 48*2;
        	xDrawOffset = 30;
        	yDrawOffset = 30;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 11: //MIDDLE
            name = "Table Piece";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(32, 48, 32, 16);
        	hitbox.width = 48;
        	xDrawOffset = 30;
        	drawWidth = 48*2;
    		isBottomLayer = true;
    		interactHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+interactSize / 2, hitbox.width, interactSize);
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
    		break;
        case 12: //EDGE
            name = "Table Piece";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(32, 64, 32, 16);
        	hitbox.width = 48;
        	hitbox.height = 20;
        	drawWidth = 48*2;
        	xDrawOffset = 30;
        	yDrawOffset = 12;
    		isBottomLayer = true;
    		isKitchenBuilding = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 13: //CORNERS
            name = "Table Corner";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 0, 32, 32);
        	hitbox.width = 48+24;
        	hitbox.height = 28;
        	drawWidth = 48*2;
        	drawHeight = 48*2;
        	xDrawOffset = 18;
        	yDrawOffset = 12+18;
          	isBottomLayer = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 14:
            name = "Table Corner";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(96, 0, 32, 32);
        	hitbox.width = 48+24;
        	hitbox.height = 28;
        	drawWidth = 48*2;
        	drawHeight = 48*2;
        	xDrawOffset = 6;
        	yDrawOffset = 12+18;
          	isBottomLayer = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 15:
            name = "Table Corner";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 32, 32, 32);
        	hitbox.width = 48+24;
        	hitbox.height = 38;
        	drawWidth = 48*2;
        	drawHeight = 48*2;
        	xDrawOffset = 18;
        	yDrawOffset = 12+18;
          	isBottomLayer = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 16:
            name = "Table Corner";
        	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(96, 32, 32, 32);
        	hitbox.width = 48+24;
        	hitbox.height = 38;
        	drawWidth = 48*2;
        	drawHeight = 48*2;
        	xDrawOffset = 6;
        	yDrawOffset = 12+18;
        	isBottomLayer = true;
    		mustBePlacedOnFloor = true;
        	break;
        case 17:
            name = "Barrel";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(16, 32, 16, 32);
        	drawHeight = 48*2;
        	isThirdLayer = true;
        	yDrawOffset = 24;
        	isDecor = true;
    		canBePlacedOnTable = true;
        	break;
        case 18:
            name = "Small Cup";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(80, 16, 16, 16);
        	isSolid = false;
          	isFourthLayer = true;
            isDecor = true;
            mustBePlacedOnTable = true;
        	break;
        case 19:
            name = "Small Carpet 1";
        	animations[0][0][0] = importImage("/decor/carpet/carpet 1.png").getSubimage(0, 0, 32, 16);
        	isSolid = false;
        	drawWidth = 48*2;
        	isBottomLayer = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	canBuildingBePlacedOn = true;
        	xDrawOffset = 24;
        	break;
        case 20:
            name = "Bowl 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 80, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
        	break;
        case 21:
            name = "Jug 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 96, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
        	break;
        case 22:
            name = "Teapot";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(112, 48, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
        	canBePlacedOnShelf = true;
        	break;
        case 23:
            name = "Bottle 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
          	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	break;
        case 24:
            name = "Plate Stack 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(64, 64, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	break;
        case 25:
            name = "Small Plant 1";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80, 0, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	break;
        case 26:
            name = "Flowers 1";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
          	yDrawOffset = 48;
        	break;
        case 27:
            name = "Kitchen Sink 1";
        	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(0, 192, 32, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	drawWidth = 48*2;
        	isThirdLayer = true;
        	mustBePlacedOnTable = true;
        	break;
        case 28:
            name = "Flowers 2";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80+48, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	break;
        case 29:
            name = "Packages 1";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(0, 256, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	xDrawOffset = 24;
        	yDrawOffset = 24;
        	break;
        case 30:
            name = "Packages 2";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(32, 256, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	isDecor = true;
         	mustBePlacedOnFloor = true;
         	xDrawOffset = 24;
        	yDrawOffset = 24;
        	break;
        case 31:
            name = "Packages 3";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(64, 256, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	isDecor = true;
         	mustBePlacedOnFloor = true;
         	xDrawOffset = 24;
        	yDrawOffset = 24;
        	break;
        case 32:
            name = "Packages 4";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(96, 256, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	isDecor = true;
         	mustBePlacedOnFloor = true;
         	xDrawOffset = 24;
        	yDrawOffset = 24;
        	break;
        case 33:
            name = "Wooden Ship";
        	animations[0][0][0] = importImage("/decor/ship decor.png").getSubimage(16, 0, 16, 32);
        	isSolid = true;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
          	yDrawOffset = 48;
        	break;
        case 34:
            name = "Boxes 1";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(0, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
        	hitbox.height = 16*3;
        	yDrawOffset = 48;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 35:
            name = "Boxes 2";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(32, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 48;
           	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 36:
            name = "Boxes 3";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(64, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 48;
           	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 37:
            name = "Boxes 4";
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(96, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 48;
           	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 38:
            name = "Small Parcel 1";
        	animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(64, 0, 16, 16);
        	isSolid = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 39:
            name = "Small Parcel 2";
        	animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(80, 0, 16, 16);
        	hitbox.width = 8*3;
        	hitbox.height = 8*3;
        	xDrawOffset = 4*3;
        	isSolid = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	yDrawOffset = 8;
        	break;
        case 40:
            name = "Small Carpet 2";
        	animations[0][0][0] = importImage("/decor/carpet/GreenCarpet.png").getSubimage(0, 0, 32, 16);
        	isSolid = false;
        	drawWidth = 48*2;
        	isBottomLayer = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	canBuildingBePlacedOn = true;
        	xDrawOffset = 24;
        	break;
        case 41:
            name = "Floor Barrel";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(16, 32, 16, 32);
        	drawHeight = 48*2;
        	yDrawOffset = 24;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 42:
        	 name = "Beer Barrel 1";
         	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(32, 0, 16, 32);
         	drawHeight = 48*2;
        	yDrawOffset = 48;
        	isSolid = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 43:
	       	name = "Beer Barrel 2";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(48, 0, 16, 32);
        	drawHeight = 48*2;
	       	yDrawOffset = 48;
	       	isSolid = true;
	       	isDecor = true;
        	mustBePlacedOnFloor = true;
	       	break;
        case 44:
	       	name = "Timber";
	       	animations[0][0][0] = importImage("/decor/firewood.png").getSubimage(48, 0, 16, 16);
	       	isSolid = true;
	       	isDecor = true;
        	mustBePlacedOnFloor = true;
	       	break;
        case 45:
	       	name = "Sandbags";
	       	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(96, 192, 32, 32);
	       	isSolid = true;
	       	hitbox.width = 48;
	       	hitbox.height = 48;
	       	drawHeight = 48*2;
	       	yDrawOffset = 24;
	       	drawWidth = 48*2;
	       	xDrawOffset = 24;
	       	isDecor = true;
        	mustBePlacedOnFloor = true;
	       	break;
        case 46:
	       	name = "Large Floor Plant 1";
	       	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96, 64, 16, 32);
	       	isSolid = true;
	        yDrawOffset = 24;
            drawHeight = 96;
            isDecor = true;
        	mustBePlacedOnFloor = true;
	       	break;
        case 47:
	       	name = "Bathroom Sink";
	       	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(64, 96, 32, 48);
	       	isSolid = true;
	      	drawWidth = 32*3;
	       	drawHeight = 48*3;
	       	xDrawOffset = 24;
	       	yDrawOffset = 48+24;
	       	isBathroomBuilding = true;
        	mustBePlacedOnFloor = true;
	       	break;
        case 48:
            name = "Flowers 3";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	break;
        case 49:
            name = "Flowers 4";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96+16, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	break;
        case 50:
            name = "Towels";
        	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(224, 176, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isBathroomBuilding = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
          	yDrawOffset = 32;
        	break;
        case 51:
            name = "Bathroom Wall 1";
        	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(208, 48, 32, 32);
        	isSolid = true;
        	hitbox.width = 80;
           	hitbox.height = 48;
        	drawWidth = 48*2;
        	drawHeight = 48*2;
        	yDrawOffset = 24;
          	isThirdLayer = true;
          	isBathroomBuilding = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 52:
            name = "Bathroom Wall 2";
        	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(240, 48, 48, 32);
        	isSolid = true;
        	hitbox.width = 96;
           	hitbox.height = 48;
        	drawWidth = 48*3;
        	drawHeight = 48*2;
        	yDrawOffset = 24;
        	isThirdLayer = true;
        	isBathroomBuilding = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 53:
            name = "Bathroom Wall 3";
        	animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(224, 80, 16, 48);
        	isSolid = true;
        	hitbox.width = 48;
           	hitbox.height = 48*2;
        	drawWidth = 48;
        	drawHeight = 48*3;
        	yDrawOffset = 24;
        	isBathroomBuilding = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 54:
            name = "Bathroom Wall 4";
        	animations[0][0][0] = importImage("/tiles/walls/Wall19.png").getSubimage(16, 16, 16, 16);
        	isSolid = true;
        	hitbox.width = 48;
           	hitbox.height = 48;
        	drawWidth = 48;
        	drawHeight = 48;
          	isBottomLayer = true;
        	break;
        case 55:
            name = "Bathroom Wall 5";
        	animations[0][0][0] = importImage("/tiles/walls/Wall19.png").getSubimage(0, 16, 16, 16);
        	isSolid = true;
        	hitbox.width = 24;
           	hitbox.height = 48;
           	xDrawOffset = 24;
        	drawWidth = 48;
        	drawHeight = 48;
          	isBottomLayer = true;
        	break;
        case 56:
            name = "Bathroom Wall 6";
        	animations[0][0][0] = importImage("/tiles/walls/Wall19.png").getSubimage(32, 16, 16, 16);
        	isSolid = true;
        	hitbox.width = 24;
           	hitbox.height = 48;
           	xDrawOffset = 0;
        	drawWidth = 48;
        	drawHeight = 48;
        	isBottomLayer = true;
        	break;
        case 57:
            name = "Cabinet 1";
        	animations[0][0][0] = importImage("/decor/cabinet.png").getSubimage(16, 32, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 58:
        	name = "Large Carpet 1";
        	animations[0][0][0] = importImage("/decor/carpet/GreenCarpet.png").getSubimage(0, 32, 64, 64);
        	drawWidth = 64*3;
        	drawHeight = 64*3;
        	hitbox.width = 48*3;
        	hitbox.height = 48*3;
        	isBottomLayer = true;
        	isSolid = false;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	canBuildingBePlacedOn = true;
        	xDrawOffset = 24;
        	yDrawOffset = 24;
        	break;
        case 59:
        	name = "Large Shelf";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(64, 96, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 32*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	mustBePlacedOnWall = true;
        	break;
        case 60:
        	name = "Circle Table";
        	animations[0][0][0] = importImage("/decor/table.png").getSubimage(160, 64, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 32*3;
        	hitbox.height = 32*3;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	yDrawOffset = 12;
        	break;
        case 61:
        	name = "Sofa 1";
        	animations[0][0][0] = importImage("/decor/PurpleSofa.png").getSubimage(0, 96, 16, 48);
          	drawWidth = 16*3;
        	drawHeight = 48*3;
        	hitbox.width = 16*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        case 62:
        	name = "Large Corner Shelf 1";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(272, 64, 48, 64);
        	drawWidth = 48*3;
        	drawHeight = 64*3;
        	hitbox.width = 16*3;
        	hitbox.height = 48*3;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	xDrawOffset = 12;
        	yDrawOffset = 24;
        	break;
        case 63:
        	name = "Clothes Rail 1";
        	animations[0][0][0] = importImage("/decor/clothes.png").getSubimage(32, 128, 16, 40);
        	drawWidth = 16*3;
        	drawHeight = 40*3;
        	hitbox.width = 16*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	break;
        }
		
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			switch(type) {
			case 23:
				invisHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 68, hitbox.height);
				break;
			}
		} 
		if(currentItem != null) {
			int newSize = 16;
			currentItem.hitbox.x = hitbox.x;
			currentItem.hitbox.y = hitbox.y - newSize/2;
		}
		
		if(invisHitbox == null) {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				BufferedImage img = animations[0][0][0];
				img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				g2.drawImage(img, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			} else {
			    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
		}
        
	     
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}
	
}

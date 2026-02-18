package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.items.Item;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class FloorDecor_Building extends Building {

	public int type;
	
	public Item currentItem = null;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	public Rectangle2D.Float interactHitbox;
	private int interactSize = 32;
	
	public FloorDecor_Building(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		
		cost = 8;
		importImages();
	}
	public void onPlaced() {
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
		animations = new TextureRegion[1][1][1];
		buildHitbox = hitbox;
		
        switch(type) {
        case 0:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 64, 16, 32);
        	yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 1:
            name = "Plant 2";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 2:
            name = "Plant 3";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 3:
            name = "Plant 4";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
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
    		canBePlaced = false;
        	break;
        case 17:
            name = "Barrel";
        	cost = 20;
        	description = "A decorative barrel to be placed on the floor or on tables.";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(16, 32, 16, 32);
        	drawHeight = 48*2;
        	isThirdLayer = true;
        	yDrawOffset = 24;
        	isDecor = true;
    		canBePlacedOnTable = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*3);
        	break;
        case 18:
            name = "Small Cup";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(80, 16, 16, 16);
        	isSolid = false;
          	isFourthLayer = true;
            isDecor = true;
            mustBePlacedOnTable = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*8);
        	break;
        case 19:
        	cost = 16;
        	description = "A small rug to keep your feet warm.";
            name = "Small Carpet 1";
        	animations[0][0][0] = importImage("/decor/carpet/carpet 1.png").getSubimage(0, 0, 32, 16);
        	isSolid = false;
        	drawWidth = 48*2;
        	isBottomLayer = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	canBuildingBePlacedOn = true;
        	xDrawOffset = 24;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*1, hitbox.width-3*3, hitbox.height-3*3);
        	break;
        case 20:
            name = "Bowl 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 80, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
        	break;
        case 21:
            name = "Jug 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 96, 16, 16);
        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);

        	break;
        case 22:
            name = "Teapot";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(112, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
        	canBePlacedOnShelf = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
        	break;
        case 23:
            name = "Bottle 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
          	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);

        	break;
        case 24:
            name = "Plate Stack 1";
        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(64, 64, 16, 16);        	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(64, 64, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
            canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

        	break;
        case 25:
            name = "Small Plant 1";
        	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 26:
            name = "Flowers 1";
        	cost = 30;
        	description = "Decorative flowers to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
          	yDrawOffset = 48;
          	cost = 12;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*8);

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
            cost = 30;
        	description = "Decorative flowers to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80+48, 96, 16, 32);
        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	cost = 12;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*8);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*4, hitbox.width, hitbox.height);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*4, hitbox.width, hitbox.height);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*4, hitbox.width, hitbox.height);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*4, hitbox.width, hitbox.height);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*6, hitbox.height-3*3);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*6, hitbox.height-3*3);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*6, hitbox.height-3*3);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*6, hitbox.height-3*3);

        	break;
        case 38:
            name = "Small Parcel 1";
        	animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(64, 0, 16, 16);
        	isSolid = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*1, hitbox.width-3*3, hitbox.height-3*3);

        	break;
        case 41:
            name = "Floor Barrel";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(16, 32, 16, 32);
        	drawHeight = 48*2;
        	yDrawOffset = 24;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*3);

        	break;
        case 42:
        	 name = "Beer Barrel 1";
         	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(32, 0, 16, 32);
         	drawHeight = 48*2;
        	yDrawOffset = 48;
        	isSolid = true;
        	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*3);

        	break;
        case 43:
	       	name = "Beer Barrel 2";
        	animations[0][0][0] = importImage("/decor/barrel.png").getSubimage(48, 0, 16, 32);
        	drawHeight = 48*2;
	       	yDrawOffset = 48;
	       	isSolid = true;
	       	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*3);

	       	break;
        case 44:
	       	name = "Timber";
	       	animations[0][0][0] = importImage("/decor/firewood.png").getSubimage(48, 0, 16, 16);
	       	isSolid = true;
	       	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
	     	cost = 30;
        	description = "Add some greenery to the area.";
	       	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96, 64, 16, 32);
	       	isSolid = true;
	        yDrawOffset = 24;
            drawHeight = 96;
            isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);

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
        	cost = 30;
        	description = "Decorative flowers to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96, 96, 16, 32);

        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	cost = 12;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*8);

        	break;
        case 49:
            name = "Flowers 4";
        	cost = 30;
        	description = "Decorative flowers to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96+16, 96, 16, 32);

        	isSolid = false;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
        	yDrawOffset = 48;
        	cost = 12;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*8);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
        	canBePlaced = false;
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
        	canBePlaced = false;
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
        	canBePlaced = false;
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
        	canBePlaced = false;
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
        	canBePlaced = false;
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
        	canBePlaced = false;
        	break;
        case 57:
            name = "Cabinet 1";
        	animations[0][0][0] = importImage("/decor/cabinet.png").getSubimage(16, 32, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

        	break;
        case 58:
        	name = "Large Carpet 1";
        	cost = 50;
        	description = "A large cosy carpet.";
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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*2);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width+3*8, hitbox.height-3*8);

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
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
        	break;
        case 64:
            name = "Book 1";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(64, 400, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 65:
            name = "Book 2";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(80, 400, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 66:
            name = "Book 3";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(96, 400, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 67:
            name = "Book 4";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(112, 400, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 68:
            name = "Book 5";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(64, 368, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 69:
            name = "Book 6";
        	animations[0][0][0] = importImage("/decor/shelf.png").getSubimage(64+16, 368+16, 16, 16);
        	isDecor = true;
        	mustBePlacedOnFloor = true;
        	isSolid = false;
        	break;
        case 70:
            name = "Small Plant 2";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 71:
            name = "Small Plant 3";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 72:
            name = "Small Plant 4";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 73:
            name = "Small Plant 5";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 74:
            name = "Small Plant 6";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 75:
            name = "Small Plant 7";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 76:
            name = "Small Plant 8";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 77:
            name = "Small Plant 9";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 78:
            name = "Small Plant 10";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 79:
            name = "Small Plant 11";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 80:
            name = "Small Plant 12";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 81:
            name = "Small Plant 13";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 82:
            name = "Small Plant 14";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 83:
            name = "Small Plant 15";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 84:
            name = "Small Plant 16";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 85:
            name = "Small Plant 17";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 86:
            name = "Small Plant 18";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32+64, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 87:
            name = "Small Plant 19";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48+64, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 88:
            name = "Small Plant 20";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64+64, 0, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 89:
            name = "Small Plant 21";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16+64, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 90:
            name = "Small Plant 22";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32+64, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 91:
            name = "Small Plant 23";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48+64, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 92:
            name = "Small Plant 24";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64+64, 16, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 93:
            name = "Small Plant 25";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16+64, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 94:
            name = "Small Plant 26";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32+64, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 95:
            name = "Small Plant 27";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48+64, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 96:
            name = "Small Plant 28";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64+64, 32, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 97:
            name = "Small Plant 29";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16+64, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 98:
            name = "Small Plant 30";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32+64, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 99:
            name = "Small Plant 31";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48+64, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 100:
            name = "Small Plant 32";
          	cost = 20;
        	description = "A small plant to be placed on a table or a shelf.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64+64, 48, 16, 16);

        	isSolid = false;
        	isFifthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*5, hitbox.width-3*6, hitbox.height-3*8);

        	break;
        case 101:
            name = "Plant 5";
         	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(80, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 102:
            name = "Plant 6";
         	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(96, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 103:
            name = "Plant 7";
         	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(112, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 104:
            name = "Plant 8";
         	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/decor/plants.png").getSubimage(112+16, 64, 16, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            canBePlacedOnTable = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 105:
            name = "Fish Net";
         	cost = 10;
        	description = "A shark was once caught with this.";
        	animations[0][0][0] = importImage("/decor/catalogue/fishingshack/Netting.png").getSubimage(0, 0, 32, 32);
            yDrawOffset = 32;
            drawHeight = 96;
            drawWidth = 96;
            xDrawOffset = 24;
            hitbox.width = 48;
            hitbox.height = 32;
            mustBePlacedOnFloor = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        	break;
        case 106:
            name = "Sailing Boat";
        	animations[0][0][0] = importImage("/decor/ship decor.png").getSubimage(48, 0, 16, 32);
        	isSolid = true;
        	drawHeight = 48*2;
        	isFourthLayer = true;
        	isDecor = true;
        	mustBePlacedOnTable = true;
          	canBePlacedOnShelf = true;
          	yDrawOffset = 48;
          	cost = 20;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
        	break;
        case 107:
            name = "Lobster Cage";
        	cost = 20;
        	description = "Catch some lobsters.";
        	animations[0][0][0] = importImage("/decor/catalogue/fishingshack/LobsterCage.png").getSubimage(0, 0, 16, 32);
        	drawHeight = 48*2;
        	isThirdLayer = true;
        	yDrawOffset = 24;
        	isDecor = true;
    		canBePlacedOnTable = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*2, hitbox.width-3*3, hitbox.height-3*3);
        	break;
        case 108:
            name = "Hay Bale 1";
        	cost = 15;
        	description = "Feed the animals.";
        	animations[0][0][0] = importImage("/decor/catalogue/farm/FarmHouses.png").getSubimage(96, 160, 16, 32);
        	drawHeight = 48*2;
        	yDrawOffset = 48;
        	isDecor = true;
        	isSolid = false;
        	mustBePlacedOnTable = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
        	break;
        case 109:
            name = "Hay Bale 2";
        	animations[0][0][0] = importImage("/decor/catalogue/farm/FarmHouses.png").getSubimage(96+16, 160+16, 16, 16);
        	isSolid = false;
        	isDecor = true;
    		mustBePlacedOnTable = true;
          	cost = 15;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
        	break;
        case 110:
        	name = "Freezer Shelf 1";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(0, 0, 64, 48);
        	drawWidth = 64*3;
        	drawHeight = 48*3;
        	hitbox.width = 48*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
         	xDrawOffset = 24;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 111:
        	name = "Freezer Shelf 2";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(64, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 24*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
         	xDrawOffset = 12;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 112:
        	name = "Freezer Shelf 3";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(64+32, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 24*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 113:
        	name = "Freezer Shelf 4";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(64+64, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 24*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 114:
        	name = "Freezer Shelf 5";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(64+64+32, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 12*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	xDrawOffset = 40;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 115:
        	name = "Freezer Shelf 6";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(128+64, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 24*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        case 116:
        	name = "Freezer Shelf 7";
        	animations[0][0][0] = importImage("/decor/Shelves.png").getSubimage(128+64+32, 0, 32, 48);
        	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width = 12*3;
        	hitbox.height = 32*3;
        	yDrawOffset = 24;
        	xDrawOffset = 40;
        	isDecor = true;
        	canBePlaced = false;
        	mustBePlacedOnFloor = true;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
    		castsShadow = false;
        	break;
        }
		
	}
	public void refreshImages() {
		switch(type) {
		case 4:
           	animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(0, 0, 16, 32);
			break;
		case 5:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(16, 0, 16, 32);
			break;
		case 6:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(32, 0, 16, 32);
			break;
		case 7:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(0, 32, 32, 16);
			break;
		case 8:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(0, 48, 32, 16);
			break;
		case 9:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(0, 64, 32, 16);
			break;
		case 10:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(32, 32, 32, 16);
			break;
		case 11:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(32, 48, 32, 16);
			break;
		case 12:
			animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(32, 64, 32, 16);
			break;
		}
	}
	public void destroy() {

	    // Only tables clean up table-placed buildings
	    if (name.equals("Table Piece")) {
	    	
	        BuildingManager bm = gp.world.buildingM;
	        
	        
	        Building[] buildings = bm.getBuildings();
	        
	        if(!gp.world.mapM.isInRoom(roomNum)) {
	        	buildings = gp.world.mapM.getRoom(roomNum).getBuildings();
	        }
	        
	        for (int i = 0; i < buildings.length; i++) {
	            Building b = buildings[i];

	            if (b == null || b == this) continue;

	            // If it was placed on a table
	            if (b.mustBePlacedOnTable || b.canBePlacedOnTable) {

	                // Optional: ensure it's actually on THIS table
	                if (this.hitbox.intersects(b.hitbox)) {

	                    // Return to customiser inventory
	                	if(b.canBePlaced) {
	                		gp.world.customiser.addToInventory(b);
	                	}
	                    
	                    // Destroy & remove it
	                    b.destroy();
	                    if(gp.world.mapM.isInRoom(roomNum)) {
		                    bm.getBuildings()[i] = null;
	                    } else {
	                    	gp.world.mapM.getRoom(roomNum).getBuildings()[i] = null;
	                    }
	                }
	            }
	        }
	    }

	    // Any other per-building cleanup here
	}
	public void draw(Renderer renderer) {
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
			renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				TextureRegion img = animations[0][0][0];
				//img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				renderer.draw(img, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
		}
	     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}
	
}

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

public class OutdoorDecor extends Building {
	
	Random r;

	private int type;
	
	public Item currentItem = null;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	private boolean animated = false;
	
	public OutdoorDecor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		
		r = new Random();
		
		importImages();
	}
	public Building clone() {
		OutdoorDecor building = new OutdoorDecor(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new OutdoorDecor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][20];
		
		isSolid = true;
		
        switch(type) {
        case 0:
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building1/Blue.png").toTextureRegion();
        	drawHeight = 112*3;
        	drawWidth = 112*3;
        	hitbox.width=70*3;
        	hitbox.height=48*3;
        	xDrawOffset = 21*3;
        	yDrawOffset = 64*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            isSolid = false;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 1:
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building2/Blue.png").toTextureRegion();
        	drawWidth = 256*3;
        	drawHeight = 128*3;
        	hitbox.width=150*3;
        	hitbox.height=90*3;
        	xDrawOffset = 61*3;
        	yDrawOffset = 32*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            isSolid = false;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 2:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building3/Blue.png").toTextureRegion();
         	drawWidth = 256*3;
        	drawHeight = 128*3;
        	hitbox.width=150*3;
        	hitbox.height=84*3;
        	xDrawOffset = 14*3;
          	yDrawOffset = 25*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            isSolid = false;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 3:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building4/Blue.png").toTextureRegion();
         	drawWidth = 128*3;
        	drawHeight = 160*3;
        	hitbox.width=68*3;
        	hitbox.height=59*3;
        	xDrawOffset = 30*3;
          	yDrawOffset = 96*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            isSolid = false;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 4:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building5/Blue.png").toTextureRegion();
         	drawWidth = 160*3;
        	drawHeight = 160*3;
        	hitbox.width=110*3;
        	hitbox.height=54*3;
        	xDrawOffset = 28*3;
          	yDrawOffset = 100*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 5:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Building6/Blue.png").toTextureRegion();
         	drawWidth = 256*3;
        	drawHeight = 128*3;
        	hitbox.width=231*3;
        	hitbox.height=64*3;
        	xDrawOffset = 12*3;
          	yDrawOffset = 49*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            isSolid = false;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 6:
            name = "Plant 1";
        	cost = 30;
        	description = "Add some greenery to the area.";
        	animations[0][0][0] = importImage("/itch/buildings/bases/Tower/Wood.png").toTextureRegion();
         	drawWidth = 80*3;
        	drawHeight = 160*3;
        	hitbox.width=56*3;
        	hitbox.height=50*3;
        	xDrawOffset = 11*3;
          	yDrawOffset = 107*3;
        	canBePlacedOnTable = true;
            canBePlacedOnShelf = true;
            isDecor = true;
            cost = 10;
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*7, hitbox.height-3*6);
        	break;
        case 7:
        	animations[0][0][0] = importImage("/itch/buildings/Planters.png").getSubimage(0, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 24*3;
        	hitbox.height=8*3;
        	xDrawOffset = 3*3;
          	yDrawOffset = 16*3;
        	break;
        case 8:
        	animations[0][0][0] = importImage("/itch/buildings/Planters.png").getSubimage(32, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 24*3;
        	hitbox.height=8*3;
        	xDrawOffset = 3*3;
          	yDrawOffset = 16*3;
        	break;
        case 9:
        	animations[0][0][0] = importImage("/itch/buildings/Planters.png").getSubimage(64, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 24*3;
        	hitbox.height=8*3;
        	xDrawOffset = 3*3;
          	yDrawOffset = 16*3;
        	break;
        case 10:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(0, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
        	break;
        case 11:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =20*3;
        	hitbox.height =16*3;
        	xDrawOffset =6*3;
          	yDrawOffset =12*3;
        	break;
        case 12:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =13*3;
        	hitbox.height =13*3;
        	xDrawOffset =9*3;
          	yDrawOffset =9*3;
        	break;
        case 13:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*3, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =26*3;
        	hitbox.height =13*3;
        	xDrawOffset =3*3;
          	yDrawOffset =6*3;
        	break;
        case 14:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*4, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =17*3;
        	hitbox.height =17*3;
        	xDrawOffset =7*3;
          	yDrawOffset =8*3;
        	break;
        case 15:
        	animations[0][0][0] = importImage("/itch/buildings/Door.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =20*3;
        	hitbox.height =27*3;
        	xDrawOffset =5*3;
          	yDrawOffset =2*3;
        	break;
        case 16:
        	animations[0][0][0] = importImage("/itch/buildings/Door.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =20*3;
        	hitbox.height =27*3;
        	xDrawOffset =5*3;
          	yDrawOffset =2*3;
        	break;
        case 17:
        	animations[0][0][0] = importImage("/itch/buildings/Door.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =20*3;
        	hitbox.height =27*3;
        	xDrawOffset =5*3;
          	yDrawOffset =2*3;
        	break;
        case 18:
        	animations[0][0][0] = importImage("/itch/buildings/HangingPlants.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =12*3;
        	hitbox.height =29*3;
        	xDrawOffset =10*3;
          	yDrawOffset =1*3;
          	isThirdLayer = true;
          	isSolid = false;
        	break;
        case 19:
        	animations[0][0][0] = importImage("/itch/buildings/HangingPlants.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =12*3;
        	hitbox.height =29*3;
        	xDrawOffset =10*3;
          	yDrawOffset =1*3;
        	isThirdLayer = true;
        	isSolid = false;
        	break;
        case 20:
        	animations[0][0][0] = importImage("/itch/buildings/HangingPlants.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =12*3;
        	hitbox.height =29*3;
        	xDrawOffset =10*3;
          	yDrawOffset =1*3;
        	isThirdLayer = true;
        	isSolid = false;
        	break;
        case 21:
        	animations[0][0][0] = importImage("/itch/buildings/HayBale.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =29*3;
        	hitbox.height =15*3;
        	xDrawOffset =1*3;
          	yDrawOffset =7*3;
          	
        	break;
        case 22:
        	animations[0][0][0] = importImage("/itch/buildings/HayBale.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =17*3;
        	hitbox.height =13*3;
        	xDrawOffset =6*3;
          	yDrawOffset =8*3;
        	break;
        case 23:
        	animations[0][0][0] = importImage("/itch/buildings/HayBale.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =17*3;
        	hitbox.height =13*3;
        	xDrawOffset =6*3;
          	yDrawOffset =8*3;
        	break;
        case 24:
        	animations[0][0][0] = importImage("/itch/buildings/Sign.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =18*3;
        	hitbox.height =24*3;
        	xDrawOffset =6*3;
          	yDrawOffset =5*3;
        	break;
        case 25:
        	animations[0][0][0] = importImage("/itch/buildings/Sign.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =18*3;
        	hitbox.height =24*3;
        	xDrawOffset =6*3;
          	yDrawOffset =5*3;
        	break;
        case 26:
        	animations[0][0][0] = importImage("/itch/buildings/Sign.png").getSubimage(32*2, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =18*3;
        	hitbox.height =24*3;
        	xDrawOffset =6*3;
          	yDrawOffset =5*3;
        	break;
        case 27:
        	animations[0][0][0] = importImage("/itch/buildings/Sign.png").getSubimage(32*3, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =18*3;
        	hitbox.height =24*3;
        	xDrawOffset =6*3;
          	yDrawOffset =5*3;
        	break;
        case 28:
        	animations[0][0][0] = importImage("/itch/buildings/Sign.png").getSubimage(32*4, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =18*3;
        	hitbox.height =24*3;
        	xDrawOffset =6*3;
          	yDrawOffset =5*3;
        	break;
        case 29:
        	animations[0][0][0] = importImage("/itch/buildings/Pot.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =16*3;
        	hitbox.height =10*3;
        	xDrawOffset =7*3;
          	yDrawOffset =13*3;
        	break;
        case 30:
        	animations[0][0][0] = importImage("/itch/buildings/Pot.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =14*3;
        	hitbox.height =11*3;
        	xDrawOffset =8*3;
          	yDrawOffset =12*3;
        	break;
        case 31:
        	animations[0][0][0] = importImage("/itch/buildings/Pot.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =14*3;
        	hitbox.height =11*3;
        	xDrawOffset =8*3;
          	yDrawOffset =12*3;
        	break;
        case 32:
        	animations[0][0][0] = importImage("/itch/buildings/Parcel&Presents.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =14*3;
        	hitbox.height =11*3;
        	xDrawOffset =8*3;
          	yDrawOffset =12*3;
        	break;
        case 33:
        	animations[0][0][0] = importImage("/itch/buildings/Parcel&Presents.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =14*3;
        	hitbox.height =11*3;
        	xDrawOffset =8*3;
          	yDrawOffset =12*3;
        	break;
        case 34:
        	animations[0][0][0] = importImage("/itch/buildings/Parcel&Presents.png").getSubimage(64, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =14*3;
        	hitbox.height =11*3;
        	xDrawOffset =8*3;
          	yDrawOffset =12*3;
        	break;
        case 35:
        	animations[0][0][0] = importImage("/itch/buildings/Scarecrow.png").getSubimage(0, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =15*3;
        	hitbox.height =14*3;
        	xDrawOffset =16*3;
          	yDrawOffset =26*3;
        	break;
        case 36:
        	animations[0][0][0] = importImage("/itch/buildings/PlantPot.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =9*3;
        	hitbox.height =10*3;
        	xDrawOffset =10*3;
          	yDrawOffset =12*3;
        	break;
        case 37:
        	animations[0][0][0] = importImage("/itch/buildings/LargePlants.png").getSubimage(0, 0, 32, 64);
        	drawWidth =32*3;
        	drawHeight =64*3;
        	hitbox.width =14*3;
        	hitbox.height =14*3;
        	xDrawOffset =8*3;
          	yDrawOffset =40*3;
        	break;
        case 38:
        	animations[0][0][0] = importImage("/itch/buildings/LargePlants.png").getSubimage(32, 0, 32, 64);
        	drawWidth =32*3;
        	drawHeight =64*3;
        	hitbox.width =14*3;
        	hitbox.height =14*3;
        	xDrawOffset =8*3;
          	yDrawOffset =40*3;
        	break;
        case 39:
        	animations[0][0][0] = importImage("/itch/buildings/LargePlants.png").getSubimage(32*2, 0, 32, 64);
        	drawWidth =32*3;
        	drawHeight =64*3;
        	hitbox.width =14*3;
        	hitbox.height =14*3;
        	xDrawOffset =8*3;
          	yDrawOffset =40*3;
        	break;
        case 40:
        	animations[0][0][0] = importImage("/itch/buildings/LargePlants.png").getSubimage(32*3, 0, 32, 64);
        	drawWidth =32*3;
        	drawHeight =64*3;
        	hitbox.width =14*3;
        	hitbox.height =14*3;
        	xDrawOffset =8*3;
          	yDrawOffset =40*3;
        	break;
        case 41:
        	animations[0][0][0] = importImage("/itch/buildings/LargePlants.png").getSubimage(32*4, 0, 32, 64);
        	drawWidth =32*3;
        	drawHeight =64*3;
        	hitbox.width =14*3;
        	hitbox.height =14*3;
        	xDrawOffset =8*3;
          	yDrawOffset =40*3;
        	break;
        case 42:
        	animations[0][0][0] = importImage("/itch/buildings/Bridge.png").getSubimage(0, 0, 64, 64);
        	drawWidth =64*3;
        	drawHeight =64*3;
        	hitbox.width =19*3;
        	hitbox.height =4*3;
        	xDrawOffset =22*3;
          	yDrawOffset =0*3;
        	break;
        case 43:
        	animations[0][0][0] = importImage("/itch/buildings/Bridge.png").getSubimage(64, 0, 64, 64);
        	drawWidth =64*3;
        	drawHeight =64*3;
        	hitbox.width =19*3;
        	hitbox.height =4*3;
        	xDrawOffset =22*3;
          	yDrawOffset =0*3;
        	break;
        case 44:
    		importFromSpriteSheet("/itch/buildings/Chimney.png", 8, 1, 0, 0, 0, 16, 48, 0);
        	drawWidth =16*3;
        	drawHeight =48*3;
        	hitbox.width =12*3;
        	hitbox.height =13*3;
        	xDrawOffset =1*3;
          	yDrawOffset =34*3 + 44*3;
          	animated = true;
          	isThirdLayer = true;
        	break;
        case 45:
        	animations[0][0][0] = importImage("/itch/buildings/StoneBridge2.png").getSubimage(0, 0, 64, 64);
        	drawWidth =64*3;
        	drawHeight =64*3;
        	hitbox.width =19*3;
        	hitbox.height =4*3;
        	xDrawOffset =22*3;
          	yDrawOffset =0*3;
        	break;
        case 46:
        	animations[0][0][0] = importImage("/itch/buildings/StoneBridge2.png").getSubimage(64, 0, 64, 64);
        	drawWidth =64*3;
        	drawHeight =64*3;
        	hitbox.width =19*3;
        	hitbox.height =4*3;
        	xDrawOffset =22*3;
          	yDrawOffset =0*3;
        	break;
        case 47:
        	animations[0][0][0] = importImage("/itch/buildings/Lantern.png").getSubimage(0, 0, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width =9*3;
        	hitbox.height =8*3;
        	xDrawOffset =3*3;
          	yDrawOffset =5*3;
        	break;
        case 48:
        	animations[0][0][0] = importImage("/itch/buildings/Candles.png").getSubimage(0, 0, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width =9*3;
        	hitbox.height =8*3;
        	xDrawOffset =3*3;
          	yDrawOffset =5*3;
        	break;
        case 49:
        	animations[0][0][0] = importImage("/itch/buildings/Candles.png").getSubimage(16, 0, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width =9*3;
        	hitbox.height =8*3;
        	xDrawOffset =3*3;
          	yDrawOffset =5*3;
        	break;
        case 50:
        	animations[0][0][0] = importImage("/itch/buildings/Candles.png").getSubimage(32, 0, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width =9*3;
        	hitbox.height =8*3;
        	xDrawOffset =3*3;
          	yDrawOffset =5*3;
        	break;
        case 51:
        	animations[0][0][0] = importImage("/itch/buildings/Trough.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =21*3;
        	hitbox.height =11*3;
        	xDrawOffset =4*3;
          	yDrawOffset =11*3;
        	break;
        case 52:
        	animations[0][0][0] = importImage("/itch/buildings/Trough.png").getSubimage(32, 32, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =12*3;
        	hitbox.height =18*3;
        	xDrawOffset =8*3;
          	yDrawOffset =8*3;
        	break;
        case 53:
        	animations[0][0][0] = importImage("/itch/buildings/Anvil.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =26*3;
        	hitbox.height =10*3;
        	xDrawOffset =3*3;
          	yDrawOffset =16*3;
        	break;
        case 54:
        	animations[0][0][0] = importImage("/itch/buildings/Barrel.png").getSubimage(0, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =17*3;
        	hitbox.height =10*3;
        	xDrawOffset =6*3;
          	yDrawOffset =14*3;
        	break;
        case 55:
        	animations[0][0][0] = importImage("/itch/buildings/Barrel.png").getSubimage(32, 0, 32, 32);
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width =17*3;
        	hitbox.height =10*3;
        	xDrawOffset =6*3;
          	yDrawOffset =14*3;
        	break;
        case 56:
        	animations[0][0][0] = importImage("/itch/buildings/village/Market.png").getSubimage(0, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =35*3;
        	hitbox.height =10*3;
        	xDrawOffset =5*3;
          	yDrawOffset =34*3;
        	break;
        case 57:
        	animations[0][0][0] = importImage("/itch/buildings/village/Market.png").getSubimage(48, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =35*3;
        	hitbox.height =10*3;
        	xDrawOffset =5*3;
          	yDrawOffset =34*3;
        	break;
        case 58:
        	animations[0][0][0] = importImage("/itch/buildings/village/Market.png").getSubimage(48*2, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =35*3;
        	hitbox.height =10*3;
        	xDrawOffset =5*3;
          	yDrawOffset =34*3;
        	break;
        case 59:
        	animations[0][0][0] = importImage("/itch/buildings/village/Market.png").getSubimage(48*3, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =35*3;
        	hitbox.height =10*3;
        	xDrawOffset =5*3;
          	yDrawOffset =34*3;
        	break;
        case 60:
        	animations[0][0][0] = importImage("/itch/buildings/village/Market.png").getSubimage(48*4, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =35*3;
        	hitbox.height =10*3;
        	xDrawOffset =5*3;
          	yDrawOffset =34*3;
        	break;
        case 61:
        	animations[0][0][0] = importImage("/itch/buildings/Well.png").getSubimage(0, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width =23*3;
        	hitbox.height =16*3;
        	xDrawOffset =11*3;
          	yDrawOffset =19*3;
        	break;
        case 62:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare.png").getSubimage(0, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 63:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare.png").getSubimage(64, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 64:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare.png").getSubimage(64*2, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 65:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare2.png").getSubimage(0, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 66:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare2.png").getSubimage(64, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 67:
        	animations[0][0][0] = importImage("/itch/buildings/TownSquare2.png").getSubimage(64*2, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 68:
        	animations[0][0][0] = importImage("/itch/buildings/TownStatue.png").getSubimage(64*0, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 69:
        	animations[0][0][0] = importImage("/itch/buildings/TownStatue.png").getSubimage(64*1, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 70:
        	animations[0][0][0] = importImage("/itch/buildings/TownStatue2.png").getSubimage(64*0, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 71:
        	animations[0][0][0] = importImage("/itch/buildings/TownStatue2.png").getSubimage(64*1, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 72:
        	animations[0][0][0] = importImage("/itch/buildings/TownStatue2.png").getSubimage(64*1, 0, 64, 80);
        	drawWidth =64*3;
        	drawHeight =80*3;
        	hitbox.width =26*3;
        	hitbox.height =23*3;
        	xDrawOffset =18*3;
          	yDrawOffset =47*3;
        	break;
        case 73:
    		importFromSpriteSheet("/itch/buildings/village/Blacksmith/Red.png", 14, 1, 0, 0, 0, 192, 144, 0);
        	drawWidth =192*3;
        	drawHeight =144*3;
        	hitbox.width =150*3;
        	hitbox.height =53*3;
        	xDrawOffset =13*3;
          	yDrawOffset =66*3;
          	animated = true;
        	break;
        case 74:
        	animations[0][0][0] = importImage("/itch/buildings/village/Barn.png").getSubimage(0, 0, 112, 112);
        	drawWidth =112*3;
        	drawHeight =112*3;
        	hitbox.width =104*3;
        	hitbox.height =45*3;
        	xDrawOffset =2*3;
          	yDrawOffset =62*3;
        	break;
        case 75:
        	animations[0][0][0] = importImage("/itch/buildings/village/Greenhouse.png").getSubimage(0, 0, 112, 112);
        	drawWidth =112*3;
        	drawHeight =112*3;
        	hitbox.width =65*3;
        	hitbox.height =43*3;
        	xDrawOffset =22*3;
          	yDrawOffset =64*3;
        	break;
        case 76:
        	animations[0][0][0] = importImage("/itch/buildings/village/Tavern/Blue.png").getSubimage(0, 0, 128, 160);
          	drawWidth = 128*3;
        	drawHeight = 160*3;
        	hitbox.width=68*3;
        	hitbox.height=59*3;
        	xDrawOffset = 30*3;
          	yDrawOffset = 96*3;
        	break;
        case 77:
        	animations[0][0][0] = importImage("/itch/buildings/GrainSilo.png").getSubimage(0, 0, 48, 128);
          	drawWidth = 48*3;
        	drawHeight = 128*3;
        	hitbox.width=40*3;
        	hitbox.height=37*3;
        	xDrawOffset = 1*3;
          	yDrawOffset = 86*3;
        	break;
        case 78:
        	animations[0][0][0] = importImage("/itch/environment/Rocks.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=11*3;
        	hitbox.height=6*3;
        	xDrawOffset = 10*3;
          	yDrawOffset = 13*3;
        	break;
        case 79:
        	animations[0][0][0] = importImage("/itch/environment/Rocks.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=11*3;
        	hitbox.height=6*3;
        	xDrawOffset = 10*3;
          	yDrawOffset = 13*3;
        	break;
        case 80:
        	animations[0][0][0] = importImage("/itch/environment/Rocks.png").getSubimage(64, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=11*3;
        	hitbox.height=6*3;
        	xDrawOffset = 10*3;
          	yDrawOffset = 13*3;
        	break;
        case 81:
        	animations[0][0][0] = importImage("/itch/environment/Rocks.png").getSubimage(96, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=27*3;
        	hitbox.height=10*3;
        	xDrawOffset = 2*3;
          	yDrawOffset = 14*3;
        	break;
        case 82:
        	animations[0][0][0] = importImage("/itch/environment/Mushrooms.png").getSubimage(0, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 83:
        	animations[0][0][0] = importImage("/itch/environment/Mushrooms.png").getSubimage(32, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 84:
        	animations[0][0][0] = importImage("/itch/environment/Mushrooms.png").getSubimage(96, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 85:
        	animations[0][0][0] = importImage("/itch/environment/Mushrooms.png").getSubimage(96+16, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 86:
        	animations[0][0][0] = importImage("/itch/environment/Mushrooms.png").getSubimage(96+16+16, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 87:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(0, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 88:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(16, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 89:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(64, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 90:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(48, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 91:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112, 16, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 92:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*2, 32, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 93:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*3, 32, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 94:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*4, 16, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 95:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*5, 16, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 96:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*6, 32, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 97:
        	animations[0][0][0] = importImage("/itch/environment/Flowers.png").getSubimage(112+16*7, 16, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 98:
        	animations[0][0][0] = importImage("/itch/crops/Wheat.png").getSubimage(64, 0, 16, 32);
          	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 99:
        	animations[0][0][0] = importImage("/itch/crops/RedPepper.png").getSubimage(64, 0, 16, 32);
          	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 100:
        	animations[0][0][0] = importImage("/itch/crops/Carrot.png").getSubimage(64, 0, 16, 32);
          	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
          	isSolid = false;
        	break;
        case 101:
        	animations[0][0][0] = importImage("/itch/crops/Carrot.png").getSubimage(16, 0, 16, 32);
          	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	isSolid = false;
        	break;
        case 102:
    		importFromSpriteSheet("/itch/animals/farm//pig/Pig.png", 5, 1, 0, 0, 0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 103:
    		importFromSpriteSheet("/itch/animals/farm/Duck/Duck.png", 4, 1, 0, 0, 0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 104:
    		importFromSpriteSheet("/itch/animals/farm/Duck/LeftDuck.png", 4, 1, 0, 0, 48*3, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 105:
    		importFromSpriteSheet("/itch/animals/farm/chicken/Chicken.png", 4, 1, 0, 0, 48*6, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 106:
    		importFromSpriteSheet("/itch/animals/forest/bunny/Bunny.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 107:
        	animations[0][0][0] = importImage("/itch/animals/farm/Chicken/ChickenCoop.png").getSubimage(0, 0, 96, 96);
          	drawWidth = 96*3;
        	drawHeight = 98*3;
        	hitbox.width=48*3;
        	hitbox.height=32*3;
        	xDrawOffset = 22*3;
          	yDrawOffset = 46*3;
        	break;
        case 108:
        	animations[0][0][0] = importImage("/itch/animals/farm/Chicken/Nest.png").getSubimage(32, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 109:
    		importFromSpriteSheet("/itch/animals/farm/Chicken/Chicken.png", 5, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 110:
    		importFromSpriteSheet("/itch/animals/farm/Chicken/Chick.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 111:
    		importFromSpriteSheet("/itch/animals/farm/bee/Beehive.png", 7, 1, 0, 0, 0, 32, 32, 0);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 112:
    		importFromSpriteSheet("/itch/animals/farm/bee/Bee.png", 4, 1, 0, 0, 0, 16, 16, 0);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 113:
    		importFromSpriteSheet("/itch/animals/farm/bee/Bee.png", 4, 1, 0, 0, 16, 16, 16, 0);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 114:
    		importFromSpriteSheet("/itch/animals/pet/cat/Cat.png", 6, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 115:
    		importFromSpriteSheet("/itch/animals/forest/fox/Fox.png", 6, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 116:
    		importFromSpriteSheet("/itch/animals/farm/bull/Bull.png", 4, 1, 0, 0, 48*3, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 117:
    		importFromSpriteSheet("/itch/animals/farm/goat/Goat.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 118:
    		importFromSpriteSheet("/itch/animals/farm/highlandcow/HighlandCow.png", 5, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 119:
    		importFromSpriteSheet("/itch/animals/farm/horse/Horse.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 120:
    		importFromSpriteSheet("/itch/animals/farm/highlandcow/HighlandCalf.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 121:
    		importFromSpriteSheet("/itch/animals/farm/highlandcow/HighlandCalf.png", 4, 1, 0, 0, 48*1, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 122:
    		importFromSpriteSheet("/itch/animals/forest/badger/Badger.png", 5, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 123:
    		importFromSpriteSheet("/itch/skeleton/Idle.png", 4, 1, 0, 0, 80*1, 80, 80, 0);
          	drawWidth = 80*3;
        	drawHeight = 80*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 124:
    		importFromSpriteSheet("/itch/skeleton/Idle.png", 4, 1, 0, 0, 80*0, 80, 80, 0);
          	drawWidth = 80*3;
        	drawHeight = 80*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 125:
    		importFromSpriteSheet("/itch/skeleton/Idle.png", 4, 1, 0, 0, 80*2, 80, 80, 0);
          	drawWidth = 80*3;
        	drawHeight = 80*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 126:
        	animations[0][0][0] = importImage("/itch/buildings/cave/MineEntrance.png").getSubimage(0, 0, 32, 48);
          	drawWidth = 32*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 127:
    		importFromSpriteSheet("/itch/animals/farm/pig/Pig.png", 4, 1, 0, 0, 48*6, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 128:
    		importFromSpriteSheet("/itch/animals/farm/pig/Piglet.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 129:
    		importFromSpriteSheet("/itch/animals/farm/pig/Piglet.png", 4, 1, 0, 0, 48*1, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 130:
    		importFromSpriteSheet("/itch/animals/farm/cow/Cow.png", 5, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 131:
        	animations[0][0][0] = importImage("/itch/buildings/cave/Ladder.png").getSubimage(0, 0, 16, 16);
          	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 132:
        	animations[0][0][0] = importImage("/itch/buildings/cave/Minecart.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 133:
        	animations[0][0][0] = importImage("/itch/buildings/cave/Minecart.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 134:
        	animations[0][0][0] = importImage("/itch/environment/cave/Amethyst.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 135:
        	animations[0][0][0] = importImage("/itch/environment/cave/Amethyst.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 136:
        	animations[0][0][0] = importImage("/itch/environment/cave/Emerald.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 137:
        	animations[0][0][0] = importImage("/itch/environment/cave/Emerald.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 138:
        	animations[0][0][0] = importImage("/itch/environment/cave/Ruby.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 139:
        	animations[0][0][0] = importImage("/itch/environment/cave/Ruby.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 140:
        	animations[0][0][0] = importImage("/itch/environment/cave/IronOre.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 141:
        	animations[0][0][0] = importImage("/itch/environment/cave/IronOre.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 142:
        	animations[0][0][0] = importImage("/itch/environment/cave/GoldOre.png").getSubimage(0, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 143:
        	animations[0][0][0] = importImage("/itch/environment/cave/GoldOre.png").getSubimage(32, 0, 32, 32);
          	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 144:
    		importFromSpriteSheet("/itch/animals/pet/dog/Dog.png", 5, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 145:
    		importFromSpriteSheet("/itch/animals/pet/dog/Puppy.png", 4, 1, 0, 0, 48*0, 48, 48, 0);
          	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width=4*3;
        	hitbox.height=6*3;
        	xDrawOffset = 5*3;
          	yDrawOffset = 4*3;
        	break;
        case 146:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*5, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
         	isFourthLayer = true;
        	break;
        case 147:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*6, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
         	isFourthLayer = true;
        	break;
        case 148:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*7, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
         	isFourthLayer = true;
        	break;
        case 149:
        	animations[0][0][0] = importImage("/itch/buildings/Windows.png").getSubimage(32*8, 0, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
         	isFourthLayer = true;
        	break;
        case 150:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(32, 32, 32, 16);
        	drawWidth = 32*3;
        	drawHeight = 16*3;
        	hitbox.width= 20*3;
        	hitbox.height=9*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
        	break;
        case 151:
        	animations[0][0][0] = importImage("/itch/buildings/bases/buildingdecor/patios/Blue.png").getSubimage(0, 0, 48, 48);
        	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
         	isFourthLayer = true;
        	break;
        case 152:
        	animations[0][0][0] = importImage("/itch/buildings/bases/buildingdecor/roofdecor/Blue.png").getSubimage(0, 0, 48, 48);
        	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
          	isSolid = false;
         	isFourthLayer = true;
        	break;
        case 153:
        	animations[0][0][0] = importImage("/itch/buildings/bases/buildingdecor/roofdecor/Blue.png").getSubimage(0, 48, 48, 48);
        	drawWidth = 48*3;
        	drawHeight = 48*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
          	isSolid = false;
          	isFourthLayer = true;
        	break;
        case 154:
    		importFromSpriteSheet("/itch/buildings/bases/Windmill/Wood.png", 10, 1, 0, 0, 0, 128, 192, 0);
          	drawWidth = 128*3;
        	drawHeight = 192*3;
        	hitbox.width=55*3;
        	hitbox.height=46*3;
        	xDrawOffset = 36*3;
          	yDrawOffset = 143*3;
          	animated = true;
        	break;
        case 155:
        	animations[0][0][0] = importImage("/itch/buildings/village/Restaurant/Blue.png").toTextureRegion();
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width =155*3;
        	hitbox.height =43*3;
        	xDrawOffset =47*3;
          	yDrawOffset =93*3;
        	break;
        case 156:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(80, 64, 48, 32);
        	drawWidth = 48*3;
        	drawHeight = 32*3;
        	hitbox.width= 25*3;
        	hitbox.height=14*3;
        	xDrawOffset = 4*3;
          	yDrawOffset = 17*3;
        	break;
        case 157:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(32, 48, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
        	break;
        case 158:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(0, 48, 32, 32);
        	drawWidth = 32*3;
        	drawHeight = 32*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
          	isSolid = false;
        	break;
        case 159:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(64, 0, 16, 16);
        	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
          	isSolid = false;
        	break;
        case 160:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(48, 16, 16, 16);
        	drawWidth = 16*3;
        	drawHeight = 16*3;
        	hitbox.width= 20*3;
        	hitbox.height=27*3;
        	xDrawOffset = 6*3;
          	yDrawOffset = 1*3;
          	isSolid = false;
        	break;
        case 161:
        	animations[0][0][0] = importImage("/buildings/Restaurant.png").getSubimage(0, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 69*3;
        	hitbox.height = 57*3;
        	xDrawOffset = 34*3;
          	yDrawOffset = 81*3;
        	break;
        case 162:
        	animations[0][0][0] = importImage("/buildings/Restaurant.png").getSubimage(240, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 60*3;
        	hitbox.height = 53*3;
        	xDrawOffset = 39*3;
          	yDrawOffset = 128*3;
        	break;
        case 163:
        	animations[0][0][0] = importImage("/buildings/Restaurant.png").getSubimage(240*2, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 42*3;
        	hitbox.height = 57*3;
        	xDrawOffset = 100*3;
        	yDrawOffset = 81*3;
        	break;
        case 164:
        	animations[0][0][0] = importImage("/buildings/Restaurant.png").getSubimage(240*3, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 75*3;
        	hitbox.height = 57*3;
        	xDrawOffset = 135*3;
         	yDrawOffset = 81*3;
        	break;
        case 165:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(16, 0, 16, 32);
        	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width= 16*3;
        	hitbox.height=22*3;
        	xDrawOffset = 0*3;
          	yDrawOffset = 2*3;
        	break;
        case 166:
        	animations[0][0][0] = importImage("/itch/buildings/OutdoorDecor.png").getSubimage(0, 0, 16, 32);
        	drawWidth = 16*3;
        	drawHeight = 32*3;
        	hitbox.width = 9*3;
        	hitbox.height = 18*3;
        	xDrawOffset = 2*3;
          	yDrawOffset = 4*3;
        	break;
        case 167:
        	animations[0][0][0] = importImage("/buildings/ArchBuilding.png").getSubimage(0, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 69*3;
        	hitbox.height = 37*3;
        	xDrawOffset = 34*3;
          	yDrawOffset = 101*3;
        	break;
        case 168:
        	animations[0][0][0] = importImage("/buildings/ArchBuilding.png").getSubimage(240*2, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 42*3;
        	hitbox.height = 20*3;
        	xDrawOffset = 100*3;
        	yDrawOffset = 117*3;
        	isSolid = false;
        	break;
        case 169:
        	animations[0][0][0] = importImage("/buildings/ArchBuilding.png").getSubimage(240*3, 0, 240, 208);
        	drawWidth =240*3;
        	drawHeight =208*3;
        	hitbox.width = 75*3;
        	hitbox.height = 37*3;
        	xDrawOffset = 135*3;
         	yDrawOffset = 101*3;
        	break;
        case 170:
        	animations[0][0][0] = importImage("/buildings/Butcher.png").getSubimage(0, 0, 256, 144);
        	drawWidth =256*3;
        	drawHeight =144*3;
        	hitbox.width = 190*3;
        	hitbox.height = 54*3;
        	xDrawOffset = 45*3;
         	yDrawOffset = 59*3;
        	break;
        case 171:
        	animations[0][0][0] = importImage("/buildings/Shade1.png").toTextureRegion();
        	drawWidth =64*3;
        	drawHeight =48*3;
        	hitbox.width = 50*3;
        	hitbox.height = 17*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 14*3;
         	isSolid = false;
         	isThirdLayer = true;
        	break;
        case 172:
        	animations[0][0][0] = importImage("/buildings/MeatCrate.png").toTextureRegion();
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width = 14*3;
        	hitbox.height = 12*3;
        	xDrawOffset = 8*3;
         	yDrawOffset = 13*3;
        	break;
        case 173:
        	animations[0][0][0] = importImage("/buildings/Stool.png").toTextureRegion();
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width = 9*3;
        	hitbox.height = 9*3;
        	xDrawOffset = 3*3;
         	yDrawOffset = 2*3;
        	break;
        case 174:
        	animations[0][0][0] = importImage("/buildings/CrateTower.png").toTextureRegion();
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width = 27*3;
        	hitbox.height = 14*3;
        	xDrawOffset = 1*3;
         	yDrawOffset = 14*3;
        	break;
        case 175:
        	animations[0][0][0] = importImage("/buildings/Shade2.png").toTextureRegion();
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width = 3*3;
        	hitbox.height = 2*3;
        	xDrawOffset = 7*3;
         	yDrawOffset = 29*3;
        	break;
        case 176:
        	animations[0][0][0] = importImage("/buildings/ButcherTable.png").toTextureRegion();
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width = 32*3;
        	hitbox.height = 16*3;
        	xDrawOffset = 0*3;
         	yDrawOffset = 7*3;
        	break;
        case 177:
    		importFromSpriteSheet("/buildings/Boat.png", 7, 1, 0, 0, 0, 96, 80, 0);
          	drawWidth = 96*3;
        	drawHeight = 80*3;
        	hitbox.width=80*3;
        	hitbox.height=7*3;
        	xDrawOffset = 8*3;
          	yDrawOffset = 46*3;
          	animated = true;
        	break;
        case 178:
        	animations[0][0][0] = importImage("/buildings/Sewer.png").toTextureRegion();
        	drawWidth =32*3;
        	drawHeight =32*3;
        	hitbox.width = 32*3;
        	hitbox.height = 32*3;
        	xDrawOffset = 0;
         	yDrawOffset = 0;
         	isSolid = false;
        	break;
        case 179:
    		importFromSpriteSheet("/buildings/FishingBoat.png", 7, 1, 0, 0, 0, 192, 112, 0);
          	drawWidth = 192*3;
        	drawHeight = 112*3;
        	hitbox.width= 139*3;
        	hitbox.height=51*3;
        	xDrawOffset = 39*3;
          	yDrawOffset = 38*3;
          	animated = true;
        	break;
        case 180:
        	animations[0][0][0] = importImage("/buildings/Supermarket.png").toTextureRegion();
        	drawWidth =256*3;
        	drawHeight =256*3;
        	hitbox.width = 236*3;
        	hitbox.height = 165*3;
        	xDrawOffset = 9*3;
         	yDrawOffset = 76*3;
        	break;
        case 181:
        	animations[0][0][0] = importImage("/buildings/CastleWall.png").toTextureRegion();
        	drawWidth =96*3;
        	drawHeight =128*3;
        	hitbox.width = 62*3;
        	hitbox.height = 33*3;
        	xDrawOffset = 15*3;
         	yDrawOffset = 78*3;
        	break;
        case 182:
        	animations[0][0][0] = importImage("/buildings/WallBuilding.png").toTextureRegion();
        	drawWidth =240*3;
        	drawHeight =160*3;
        	hitbox.width = 177*3;
        	hitbox.height = 33*3;
        	xDrawOffset = 28*3;
         	yDrawOffset = 103*3;
        	break;
        case 183:
        	animations[0][0][0] = importImage("/buildings/RiverEntrance.png").toTextureRegion();
        	drawWidth =240*3;
        	drawHeight =160*3;
        	hitbox.width = 201*3;
        	hitbox.height = 33*3;
        	xDrawOffset = 28*3;
         	yDrawOffset = 103*3;
        	break;
        case 184:
    		importFromSpriteSheet("/buildings/Lighthouse.png", 6, 1, 0, 0, 0, 160, 304, 0);
          	drawWidth = 160*3;
        	drawHeight = 304*3;
        	hitbox.width= 142*3;
        	hitbox.height=58*3;
        	xDrawOffset = 8*3;
          	yDrawOffset = 217*3;
          	animated = true;
          	break;
        case 185:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(0, 0, 48, 48);
        	drawWidth =48*3;
        	drawHeight =48*3;
        	hitbox.width = 14*3;
        	hitbox.height = 14*3;
        	xDrawOffset = 17*3;
         	yDrawOffset = 32*3;
        	break;
        case 186:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(48, 16, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 14*3;
        	hitbox.height = 14*3;
        	xDrawOffset = 0*3;
         	yDrawOffset = 0*3;
         	isSolid = false;
        	break;
        case 187:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(64, 16, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 4*3;
        	hitbox.height = 6*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 7*3;
        	break;
        case 188:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(80, 16, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 4*3;
        	hitbox.height = 6*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 7*3;
        	break;
        case 189:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(96, 16, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width = 4*3;
        	hitbox.height = 6*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 7*3;
        	break;
        case 190:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(96+16, 16, 16, 16);
        	drawWidth =16*3;
        	drawHeight =16*3;
        	hitbox.width = 4*3;
        	hitbox.height = 6*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 7*3;
        	break;
        case 191:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(96+32, 16, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 12*3;
        	hitbox.height = 8*3;
        	xDrawOffset = 2*3;
         	yDrawOffset = 21*3;
        	break;
        case 192:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(96+48, 16, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 12*3;
        	hitbox.height = 8*3;
        	xDrawOffset = 2*3;
         	yDrawOffset = 21*3;
        	break;
        case 193:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(0, 48, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 16*3;
        	hitbox.height = 16*3;
        	xDrawOffset = 0*3;
         	yDrawOffset = 16*3;
        	break;
        case 194:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(16, 48, 16, 32);
        	drawWidth =16*3;
        	drawHeight =32*3;
        	hitbox.width = 16*3;
        	hitbox.height = 16*3;
        	xDrawOffset = 0*3;
         	yDrawOffset = 16*3;
        	break;
        case 195:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(32, 64, 16, 16);
        	drawWidth =16*3;
        	drawHeight = 16*3;
        	hitbox.width = 14*3;
        	hitbox.height = 8*3;
        	xDrawOffset = 0*3;
         	yDrawOffset = 6*3;
        	break;
        case 196:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(48, 48, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 20*3;
        	hitbox.height = 26*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 5*3;
         	isBottomLayer = true;
        	break;
        case 197:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(80, 48, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 20*3;
        	hitbox.height = 26*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 5*3;
        	isBottomLayer = true;
        	break;
        case 198:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(80+32, 48, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 20*3;
        	hitbox.height = 26*3;
        	xDrawOffset = 6*3;
         	yDrawOffset = 5*3;
        	isBottomLayer = true;
        	break;
        case 199:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(0, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 14*3;
        	hitbox.height = 19*3;
        	xDrawOffset = 8*3;
         	yDrawOffset = 10*3;
        	break;
        case 200:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(32, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 14*3;
        	hitbox.height = 19*3;
        	xDrawOffset = 8*3;
         	yDrawOffset = 10*3;
        	break;
        case 201:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(32, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 14*3;
        	hitbox.height = 19*3;
        	xDrawOffset = 8*3;
         	yDrawOffset = 10*3;
        	break;
        case 202:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(64, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 12*3;
        	hitbox.height = 12*3;
        	xDrawOffset = 10*3;
         	yDrawOffset = 12*3;
        	break;
        case 203:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(64+32, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 18*3;
        	hitbox.height = 21*3;
        	xDrawOffset = 7*3;
         	yDrawOffset = 4*3;
        	break;
        case 204:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(64+32+32, 80, 32, 32);
        	drawWidth =32*3;
        	drawHeight = 32*3;
        	hitbox.width = 18*3;
        	hitbox.height = 21*3;
        	xDrawOffset = 7*3;
         	yDrawOffset = 4*3;
        	break;
        case 205:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(0, 112, 48, 48);
        	drawWidth =48*3;
        	drawHeight = 48*3;
          	hitbox.width = 16*3;
           	hitbox.height = 16*3;
           	xDrawOffset = 16*3;
          	yDrawOffset = 32*3;
        	break;
        case 206:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(48, 112, 16, 16);
        	drawWidth = 16*3;
        	drawHeight = 16*3;
          	hitbox.width = 4*3;
           	hitbox.height = 7*3;
           	xDrawOffset = 6*3;
          	yDrawOffset = 6*3;
        	break;
        case 207:
        	animations[0][0][0] = importImage("/buildings/GraveyardProps.png").getSubimage(64, 112, 16, 16);
        	drawWidth = 16*3;
        	drawHeight = 16*3;
          	hitbox.width = 4*3;
           	hitbox.height = 7*3;
           	xDrawOffset = 6*3;
          	yDrawOffset = 6*3;
        	break;
        case 208:
        	animations[0][0][0] = importImage("/buildings/ClothesShop.png").toTextureRegion();
        	drawWidth = 176*3;
        	drawHeight = 128*3;
          	hitbox.width = 124*3;
           	hitbox.height = 30*3;
           	xDrawOffset = 29*3;
          	yDrawOffset = 66*3;
        	break;
        case 209:
        	animations[0][0][0] = importImage("/buildings/FurnitureShop.png").toTextureRegion();
        	drawWidth = 192*3;
        	drawHeight = 144*3;
          	hitbox.width = 149*3;
           	hitbox.height = 44*3;
           	xDrawOffset = 13*3;
          	yDrawOffset = 89*3;
        	break;
        case 210:
        	animations[0][0][0] = importImage("/buildings/FabricCrate.png").toTextureRegion();
        	drawWidth = 32*3;
        	drawHeight = 32*3;
          	hitbox.width = 14*3;
           	hitbox.height = 11*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 13*3;
        	break;
        case 211:
        	animations[0][0][0] = importImage("/buildings/FurnitureSign.png").toTextureRegion();
        	drawWidth = 32*3;
        	drawHeight = 32*3;
          	hitbox.width = 14*3;
           	hitbox.height = 11*3;
           	xDrawOffset = 9*3;
          	yDrawOffset = 13*3;
          	isSolid = false;
          	isThirdLayer = true;
        	break;
        case 212:
        	animations[0][0][0] = importImage("/buildings/SaleSign.png").toTextureRegion();
        	drawWidth = 32*3;
        	drawHeight = 32*3;
          	hitbox.width = 20*3;
           	hitbox.height = 13*3;
           	xDrawOffset = 5*3;
          	yDrawOffset = 16*3;
        	break;
        case 213:
        	animations[0][0][0] = importImage("/buildings/Bakery.png").toTextureRegion();
        	drawWidth = 128*3;
        	drawHeight = 160*3;
          	hitbox.width = 68*3;
           	hitbox.height = 40*3;
           	xDrawOffset = 30*3;
          	yDrawOffset = 114*3;
        	break;
        }
        name = "Outdoor Decor";
        canBePlaced = false;
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(!animated) {
			return;
		}
		
		animationSpeed+=dt; //Updating animation frame
        if (animationSpeed >= animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }

        if (animations[0][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }	
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		if(invisHitbox == null) {
		     renderer.draw(animations[0][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
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

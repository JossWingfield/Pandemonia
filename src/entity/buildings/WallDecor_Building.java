package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class WallDecor_Building extends Building {

	public int type;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	public WallDecor_Building(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		mustBePlacedOnWall = true;
		cost = 10;
	}
	public void onPlaced() {
		importImages();
	}
	public Building clone() {
		WallDecor_Building building = new WallDecor_Building(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new WallDecor_Building(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		buildHitbox = hitbox;
		
		switch(type) {
		 case 0:
			 name = "Painting 1";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(64, 0, 16, 16);
	         isDecor = true;
	      	 cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*4, hitbox.height-3*4);
	         break;
		 case 1:
			 name = "Painting 2";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(80, 0, 16, 16);
	         isDecor = true;
	      	 cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*5, hitbox.height-3*4);

	         break;
		 case 2:
			 name = "Painting 3";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(0, 0, 16, 16);
	         isDecor = true;
	      	 cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*4, hitbox.height-3*5);

	         break;
		 case 3:
			 name = "Painting 4";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(16, 0, 16, 16);
	         isDecor = true;
	      	 cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*5, hitbox.height-3*5);

	         break;
		 case 4:
			 name = "Hanging Plant 1";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 96, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Plants to be hung on the wall.";
	 		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 5:
			 name = "Hanging Plant 2";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 96, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Plants to be hung on the wall.";
	 		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);

	         break;
		 case 6:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 7:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(16, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 8:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 9:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 10:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(64, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 11:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 16, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 12:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 32, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 13:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 48, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 14:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 16, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 15:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 32, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 16:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 48, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 17:
	         name = "Deer Head";
	         animations[0][0][0] = importImage("/decor/animal wall decor.png").getSubimage(96, 0, 32, 32);
	         isSolid = true;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
			 buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height-3*2);
			 cost = 32;
			 description = "A nice trophy for the wall.";
	         break;
		 case 18:
	         name = "Notice Board";
	         animations[0][0][0] = importImage("/decor/chalkboard.png").getSubimage(96, 0, 32, 32);
	         isSolid = false;
	         isThirdLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
			 buildHitbox = new Rectangle2D.Float(hitbox.x-3*1, hitbox.y-3*2, hitbox.width+3*1, hitbox.height-3*2);

	         break;
		 case 19:
	         name = "Piping 1";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(0, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			canBePlaced = false;
			buildHitbox = new Rectangle2D.Float(0, 0, 1, 1);

	         break;
		 case 20:
	         name = "Piping 2";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(32, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			canBePlaced = false;
			buildHitbox = new Rectangle2D.Float(0, 0, 1, 1);
	         break;
		 case 21:
	         name = "Piping 3";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(64, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			canBePlaced = false;
			buildHitbox = new Rectangle2D.Float(0, 0, 1, 1);
	         break;
		 case 22:
	         name = "Mountain View";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(0, 16, 32, 16);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
	 		 xDrawOffset = 12;
	 		 yDrawOffset = -12;
	 	   	 cost = 40;
	      	 description = "A scenic landscape for all to see.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*4, hitbox.width, hitbox.height-3*5);
	         break;
		 case 23:
			 name = "Hanging Plant 3";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 96, 16, 32);
	         drawHeight = 96;
	         isDecor = true;
	         hitbox.height = 80;
	         cost = 12;
	 		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 24:
			 name = "Hanging Plant 4";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 96, 16, 32);
	         drawHeight = 96;
	         isDecor = true;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Plants to be hung on the wall.";
	 		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);

	         break;
		 case 25:
			 name = "Toilet Paper";
			 isSolid = false;
	         animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(208, 160, 16, 16);
	         isBathroomBuilding = true;
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*5, hitbox.width-3*3, hitbox.height-3*8);

	         break;
		 case 26:
			 name = "Wolf Head";
	         animations[0][0][0] = importImage("/decor/animal wall decor.png").getSubimage(64, 32, 32, 32);
	         isSolid = true;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
			 buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height-3*2);

	         break;
		 case 27:
			 name = "Ram Skull";
	         animations[0][0][0] = importImage("/decor/animal wall decor.png").getSubimage(0, 0, 32, 32);
	         isSolid = true;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
			 buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height-3*2);

	         break;
		 case 28:
	         name = "Painting 6";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(32, 0, 16, 16);
	         isDecor = true;
	         cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*4, hitbox.height-3*5);

	         break;
		 case 29:
	         name = "Painting 7";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(48, 0, 16, 16);
	         isDecor = true;
	         cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*4, hitbox.height-3*4);

	         break;
		 case 30:
	         name = "Painting 8";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(32, 16, 16, 16);
	         isDecor = true;
	         cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y, hitbox.width-3*4, hitbox.height-3*5);

	         break;
		 case 31:
	         name = "Painting 9";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(48, 16, 16, 16);
	         isDecor = true;
	         cost = 30;
	      	 description = "Artwork to be hung on the wall.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y, hitbox.width-3*3, hitbox.height-3*2);

	         break;
		 case 32:
			 name = "Cabin Hanging Plant 1";
	         animations[0][0][0] = importImage("/decor/catalogue/cabin/CabinPlants.png").getSubimage(0, 0, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Rustic cabin plants.";
	 		 buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 33:
			 name = "Cabin Hanging Plant 2";
	         animations[0][0][0] = importImage("/decor/catalogue/cabin/CabinPlants.png").getSubimage(16, 0, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Rustic cabin plants.";
	 		 buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 34:
			 name = "Cabin Hanging Plant 3";
	         animations[0][0][0] = importImage("/decor/catalogue/cabin/CabinPlants.png").getSubimage(32, 0, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Rustic cabin plants.";
	 		 buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 35:
			 name = "Cabin Hanging Plant 4";
	         animations[0][0][0] = importImage("/decor/catalogue/cabin/CabinPlants.png").getSubimage(48, 0, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         cost = 20;
	    	 description = "Rustic cabin plants.";
	 		 buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	         break;
		 case 36:
			 name = "Life Ring";
	         animations[0][0][0] = importImage("/decor/catalogue/fishingshack/LifeRing.png").toTextureRegion();
	         isDecor = true;
	      	 cost = 20;
	      	 description = "Could save a life potentially.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
	         break;
		 case 37:
			 name = "Wall Netting";
	         animations[0][0][0] = importImage("/decor/catalogue/fishingshack/WallNetting.png").toTextureRegion();
	         isDecor = true;
	      	 cost = 20;
	      	 drawWidth = 48*3;
	     	 drawHeight = 48*2;
	     	 hitbox.width = 48*3;
	     	 hitbox.height = 48*2;
	      	 description = "Completely false, never used.";
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
	         break;
		 case 38:
			 name = "Tool Rack";
	         animations[0][0][0] = importImage("/decor/catalogue/farm/ToolRack.png").toTextureRegion();
	         isDecor = true;
	      	 cost = 20;
	      	 drawWidth = 48*3;
	     	 drawHeight = 32*3;
	      	 description = "Never been used.";
	      	 hitbox.width = 96;
	      	 hitbox.height = 48;
	      	 xDrawOffset = 20;
	       	 yDrawOffset = 24;
	    	 buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
	         break;
        }
		
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
			switch(type) {
			case 11, 12, 13:
				invisHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 80, hitbox.height);
				break;
			}
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

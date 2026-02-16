package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class Door extends Building {
	
	private Rectangle2D.Float doorHitbox, entryHitbox, npcVisualHitbox;
	private boolean firstUpdate = true;
	public int doorRoomNum = 1;
	public int facing;
	public int preset = 0;
	public double doorCooldown = 0;
	private boolean open = false;
	private boolean locked = false;
	private boolean drawLight = false;
	private LightSource light;
	private boolean justUnlocked = false;
	
	public Door(GamePanel gp, float xPos, float yPos, int facing, int presetNum) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.facing = facing;
		this.preset = presetNum;
		
		isSolid = false;
		
		drawWidth = 32*3;
		drawHeight = 48*3;
		canBePlaced = false;
		yDrawOffset = 48;
		castsShadow = false;
		importImages();
		if(facing == 0) {
			npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height-48, 48, 48);
		} else if(facing == 1) {
			npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y - 48 , 48, 48);
		} else if(facing == 2){ //LEFT
			isThirdLayer = true;
			hitbox.width = 24;
			npcHitbox = new Rectangle2D.Float(hitbox.x - 16, hitbox.y+80-48, 48, 48);
		} else if(facing == 3) { //RIGHT
			hitbox.width = 24;
			xDrawOffset += 24;
			npcHitbox = new Rectangle2D.Float(hitbox.x - 16-24, hitbox.y+64-48, 48, 64);
		}
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*7, hitbox.height-3*9);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y, hitbox.width-3*7, hitbox.height-3*9);
	}
	public Building clone() {
		Door building = new Door(gp, hitbox.x, hitbox.y, facing, preset);
		return building;
    }
	public void printOutput() {
		//System.out.println("buildings[arrayCounter] = new Door(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + facing + ", " + preset + ");");
		//System.out.println("arrayCounter++;");
	}
	public void setDoorNum(int num) {
		doorRoomNum = num;
	}
	public void setDoorLight(boolean doorLight) {
		this.drawLight = doorLight;
		if(doorLight) {
			gp.world.lightingM.addLight(light);
		} else {
			gp.world.lightingM.removeLight(light);
		}
	}
	public void unlock() {
		locked = false;
		justUnlocked = true;
	}
	private void importImages() {
		animations = new TextureRegion[1][1][3];
		
		name = "Door 1";
		switch(preset) {
		case 0:
	    	if(facing == 1) { 
	        	animations[0][0][0] = importImage("/decor/DownDoor.png").toTextureRegion();
	        	animations[0][0][1] = animations[0][0][0];
	        	drawHeight = 48;
	        	isThirdLayer = true;
	    	} else if(facing == 0){ //0
	        	animations[0][0][0] = importImage("/decor/door.png").getSubimage(0, 48, 32, 48);
	        	animations[0][0][1] = importImage("/decor/door.png").getSubimage(32, 0, 32, 48);
	        	if(gp.world.mapM != null) {
					animations[0][0][0] = gp.world.mapM.getRooms()[doorRoomNum].getDoorSkin().getImage();
					animations[0][0][1] = gp.world.mapM.getRooms()[doorRoomNum].getDoorSkin().getOpenImage();
	        	}
	    	} else if(facing == 2 || facing == 3) {
	    		animations[0][0][0] = importImage("/decor/door.png").getSubimage(128, 48, 16, 48);
	        	animations[0][0][1] = animations[0][0][0];
	           	isFourthLayer = true;
	          	drawHeight = 48*3;
	          	drawWidth = 48;
	          	if(facing == 2) {
	          		animations[0][0][0] = createHorizontalFlipped(animations[0][0][0]);
	            	animations[0][0][1] = animations[0][0][0];
	          	}
	    	}
	    	break;
		case 1:
			animations[0][0][0] = importImage("/buildings/House.png").getSubimage(0, 160, 32, 32);
        	animations[0][0][1] = importImage("/buildings/House.png").getSubimage(32, 160, 32, 32);
        	drawHeight = 32*3;
			break;
		case 2:
			animations[0][0][0] = importImage("/decor/Chefdoor.png").getSubimage(0, 0, 32, 48);
        	animations[0][0][1] = importImage("/decor/door.png").getSubimage(32, 0, 32, 48);
        	animations[0][0][2] = importImage("/decor/Chefdoor.png").getSubimage(64, 0, 32, 48);
        	locked = true;
    		locked = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + 25),Colour.RED, 16);
			break;
		}
	}
	public void refreshImages() {
		if(preset == 0) {
			if(facing == 0) {
				animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getDoorSkin().getImage();
				animations[0][0][1] = gp.world.mapM.getRooms()[roomNum].getDoorSkin().getOpenImage();
			}
		}
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(entryHitbox != null) {
			if(gp.player.interactHitbox.intersects(entryHitbox) && !locked) {
				if(doorCooldown == 0) {
					if(doorRoomNum != 2) {
						gp.world.mapM.changeRoom(doorRoomNum, this);
					}
				}
			}
		}
		
	}
	public void draw(Renderer renderer) {
		if (doorCooldown > 0) {
			doorCooldown -= 0.05;
		    if (doorCooldown < 0) {
		    	doorCooldown = 0;
		    }
		}
		if(firstUpdate) {
			firstUpdate = false;
			doorHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28-48, 48, 32);
			entryHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48-48, 48, 32);
			npcVisualHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28-48, 48, 80);
			
			if(facing == 2) {
				entryHitbox = new Rectangle2D.Float(hitbox.x-48, hitbox.y+32, 48, 48*2);
			} else if(facing == 3) {
				entryHitbox = new Rectangle2D.Float(hitbox.x+48, hitbox.y+32, 48, 48*2);
			}
			
			if(preset == 1) {
				entryHitbox.y -= 20;
				doorHitbox.y -= 20;
			}
			importImages();
		}  
		
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)entryHitbox.x, (int)entryHitbox.y, (int)entryHitbox.width, (int)entryHitbox.height);
		 
		if((!gp.player.interactHitbox.intersects(doorHitbox) && !gp.world.npcM.stockerCheck(npcVisualHitbox)) || locked) {
			if(!open) {
			}
			open = true;
			if(!justUnlocked) {
				renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);	
			} else {
				renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);	
			}
		} else {
			if(open) {
			}
			open = false;
			if(justUnlocked) {
				justUnlocked = false;
			}
			renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
	    //g2.setColor(Color.YELLOW);
	    //renderer.fillRect((int)entryHitbox.x, (int)entryHitbox.y, (int)entryHitbox.width, (int)entryHitbox.height);
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)gp.player.interactHitbox.x, (int)gp.player.interactHitbox.y, (int)gp.player.interactHitbox.width, (int)gp.player.interactHitbox.height);
		
		
		if(drawLight && animations[0][0][2] != null) {
			renderer.draw(animations[0][0][2], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);		
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	}
	public void drawEmissive(Renderer renderer) {
		if(drawLight && animations[0][0][2] != null) {
			renderer.draw(animations[0][0][2], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);		
		}
	}
}

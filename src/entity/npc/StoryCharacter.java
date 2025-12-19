package entity.npc;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class StoryCharacter extends NPC {
	
	protected int type;
	
	public TextureRegion faceIcon;
	private Graphics2D g2;
	private boolean firstDraw = true;
	
	private LightSource ghostLight;
	private boolean hasLight = false;

	public StoryCharacter(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 3;
		drawScale = 3;
		drawWidth = 80*drawScale;
	    drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
		speed = 1*60;
		npcType = "Story Character";
		this.type = type;
		
		talkHitbox = new Rectangle2D.Float(hitbox.x - 16, hitbox.y - 16, hitbox.width + 32, hitbox.height + 32);
		
		importImages();
	}
	private void importImages() {
		animations = new TextureRegion[5][10][10];
		animations[0][0][0] = importImage("/npcs/mannequin.png").getSubimage(16, 0, 16, 32);
		name = "";
		
		switch(type) {
		case 0:
			name = "Owner";
	        importPlayerSpriteSheet("/npcs/angler/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/angler/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
	        break;
		case 1:
	        importPlayerSpriteSheet("/npcs/blacksmith/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/blacksmith/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 2:
			name = "Player";
	        importPlayerSpriteSheet("/player/idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/player/walk", 8, 1, 1, 0, 0, 80, 80);
	        
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 3: //GHOSTS
			name = "???";
			hasLight = true;
			switch(r.nextInt(1)) {
			case 0:
				importPlayerSpriteSheet("/npcs/ghosts/variant1/idle", 4, 1, 0, 0, 0, 80, 80);
			    importPlayerSpriteSheet("/npcs/ghosts/variant1/walk", 4, 1, 1, 0, 0, 80, 80);
			    faceIcon = importImage("/npcs/ghosts/variant1/BasicFaceIcon.png").toTextureRegion();
				break;
			}
			break;
		case 4:
			name = "Pete";
		    importPlayerSpriteSheet("/npcs/innKeeper/Idle", 4, 1, 0, 0, 0, 80, 80);
	        importPlayerSpriteSheet("/npcs/innKeeper/Walk", 8, 1, 1, 0, 0, 80, 80);
	        faceIcon = importImage("/npcs/FaceIcons.png").getSubimage(type*32, 0, 32, 32);
			break;
		case 5:
			name = "Ignis";
			hasLight = true;
			importPlayerSpriteSheet("/npcs/ghosts/variant1/idle", 4, 1, 0, 0, 0, 80, 80);
		    importPlayerSpriteSheet("/npcs/ghosts/variant1/walk", 4, 1, 1, 0, 0, 80, 80);
		    faceIcon = importImage("/npcs/ghosts/variant1/BasicFaceIcon.png").toTextureRegion();
			break;
		}
		
		portrait = faceIcon;
		
	}
	public void removeLights() {
		if(ghostLight != null) {
			gp.lightingM.removeLight(ghostLight);
		}
		ghostLight = null;
	}
	protected void leave(double dt) {
		super.leave(dt);
	}
	public void update(double dt) {
		talkHitbox.x = hitbox.x - 16;
	    talkHitbox.y = hitbox.y - 16;
	    if(npcToFollow != null) {
			followNPC(dt, npcToFollow);
		}
	    if(walking) {
	    	currentAnimation = 1;
	    } else {
	    	currentAnimation = 0;
	    }
		
		  animationSpeed+=animationUpdateSpeed*dt; //Update the animation frame
	      if(animationSpeed >= animationSpeedFactor) {
	    	  animationSpeed = 0;
	          animationCounter++;
	      }
	      
	      if(animations != null) {
	    	  if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
	    		  animationCounter = 0; //Loops the animation
	    	  }
	      }
	}
	public void draw(Renderer renderer) {
		this.g2 = g2;
		if(firstDraw) {
			if(hasLight) {
				ghostLight = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Colour.BLUE, 48);
				gp.lightingM.addLight(ghostLight);
			}
			firstDraw = false;
		}
		
		if(hasLight) {
			ghostLight.x = (int)(hitbox.x + hitbox.width / 2) - 8;
	    	ghostLight.y = (int)(hitbox.y + hitbox.height / 2) - 8;
		}
	      
	      if(animations != null) {
	    	  TextureRegion img = animations[0][currentAnimation][animationCounter];
	    	  int a = 0;
	    	  if(direction != null) {
	    	  switch(direction) {
	    	  case "Left":
	    		  a = 1;
	    		  break;
	    	  case "Right":
	    		  a = 0;
	    		  break;
	    	  case "Up":
	    		  a = 3;
	    	  	  break;
	    	  case "Down":
	    		  a = 2;
	    	  	  break;
	    	  }
	    	  img = animations[a][currentAnimation][animationCounter];
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  
	    	  renderer.draw(img, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y - yDrawOffset ), (int)(drawWidth), (int)(drawHeight));
	      }
	      if(talking) {
	    	  //gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2, (int)hitbox.y - 48*3, dialogues[dialogueIndex], this);
	      }
	      
	  }
	
}

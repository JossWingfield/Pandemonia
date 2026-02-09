package entity.npc;

import java.util.Random;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;
import utility.RecipeManager;
import utility.RoomHelperMethods;

public class SpecialCustomer extends Customer {

	GamePanel gp;
	Random r;
	
	private int specialType;
	private float moneyMultiplier = 1.0f;
	
	private LightSource ghostLight;
	
	private boolean isCelebrity = false;
	
	public SpecialCustomer(GamePanel gp, int x, int y) {
		super(gp, x, y);
		this.gp = gp;
		
		r = new Random();
		
		if(RoomHelperMethods.isCelebrityPresent(gp.world.mapM.getRoom(0).getNPCs())) {
			celebrityPresent = true;
		}
		
		getImages();
	}
	
	private void getImages() {
		animations = new TextureRegion[4][10][10];
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		name = "Special Customer";
		
		animationSpeedFactor = 5;
		drawWidth = 80*drawScale;
	    drawHeight = 80*drawScale;
        xDrawOffset = 34*drawScale;
        yDrawOffset = 36*drawScale;
		
		specialType = r.nextInt(4);

		switch(specialType) {
		case 0: //Rich, pays double
			importPlayerSpriteSheet("/npcs/special/Idle", 4, 1, 0, 0, 0, 80, 80);
		    importPlayerSpriteSheet("/npcs/special/Run", 8, 1, 1, 0, 0, 80, 80);
		    faceIcon = importImage("/npcs/special/BasicFaceIcon.png").toTextureRegion();
			moneyMultiplier = 2.0f;
			ghostLight = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Colour.BLUE, 48);
			gp.world.lightingM.addLight(ghostLight);
			break;
		case 1: //Impatient but pays slightly more
			moneyMultiplier = 1.5f;
			patienceFactor = 2;
			importPlayerSpriteSheet("/npcs/special/Idle", 4, 1, 0, 0, 0, 80, 80);
		    importPlayerSpriteSheet("/npcs/special/Run", 8, 1, 1, 0, 0, 80, 80);
		    faceIcon = importImage("/npcs/special/BasicFaceIcon.png").toTextureRegion();
			break;
		case 2: //Celebrity, slows down others patience, but pays more
			moneyMultiplier = 1.8f;
			isCelebrity = true;
			
			 importPlayerSpriteSheet("/npcs/special/Idle", 4, 1, 0, 0, 0, 80, 80);
		     importPlayerSpriteSheet("/npcs/special/Run", 8, 1, 1, 0, 0, 80, 80);
		     faceIcon = importImage("/npcs/special/BasicFaceIcon.png").toTextureRegion();
			
			if(gp.world.mapM.currentRoom.equals(gp.world.mapM.getRoom(0))) {
				RoomHelperMethods.setCelebrityPresent(gp.world.npcM.npcs, true);
			} else {
				RoomHelperMethods.setCelebrityPresent(gp.world.mapM.getRoom(0).getNPCs(), true);
			}
			break;
		case 3: //Mystery Order
			moneyMultiplier = 1.75f;
			hideOrder = true;
			
			 importPlayerSpriteSheet("/npcs/special/Idle", 4, 1, 0, 0, 0, 80, 80);
		     importPlayerSpriteSheet("/npcs/special/Run", 8, 1, 1, 0, 0, 80, 80);
		     faceIcon = importImage("/npcs/special/BasicFaceIcon.png").toTextureRegion();
			break;
		case 4: //Rich, pays double
			importPlayerSpriteSheet("/npcs/ghosts/variant1/idle", 4, 1, 0, 0, 0, 80, 80);
		    importPlayerSpriteSheet("/npcs/ghosts/variant1/walk", 4, 1, 1, 0, 0, 80, 80);
		    faceIcon = importImage("/npcs/ghosts/variant1/BasicFaceIcon.png").toTextureRegion();
			ghostLight = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Colour.BLUE, 48);
			gp.world.lightingM.addLight(ghostLight);
			isGhost = true;
			break;
		}
		
	}
	public void removeLights() {
		gp.world.lightingM.removeLight(ghostLight);
	}
	public void completeOrder() {
	    if(foodOrder == null) return;

	    // base payment
	    gp.player.wealth += (foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder), moneyMultiplier));

	    // tip logic based on patience
	    float progress = (float)(patienceCounter / maxPatienceTime);
	    int tip = 0;

	    if(progress <= 0.33f) { // green zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder), moneyMultiplier) * greenTipMultiplier);
	    } else if(progress <= 0.66f) { // orange zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder), moneyMultiplier) * orangeTipMultiplier);
	    } else { // red zone
	        tip = (int)(foodOrder.getCost(gp.world.gameM.isRecipeSpecial(foodOrder), moneyMultiplier) * redTipMultiplier);
	    }
	    gp.player.wealth += tip;

	    // clean up
	    RecipeManager.removeOrder(foodOrder);
	    gp.player.currentItem = null;
	    eating = true;
	    waitingToOrder = false;
		if(isCelebrity) {
			if(gp.world.mapM.currentRoom.equals(gp.world.mapM.getRoom(0))) {
				RoomHelperMethods.setCelebrityPresent(gp.world.npcM.npcs, false);
			} else {
				RoomHelperMethods.setCelebrityPresent(gp.world.mapM.getRoom(0).getNPCs(), false);
			}
		}
	}
	public float getMoneyMultiplier() {
		return moneyMultiplier;
	}
	public boolean isCelebrity() {
		return isCelebrity;
	}
	public void setCelebrityPresent(boolean isPresent) {
		if(isCelebrity) {
			return;
		}
		super.setCelebrityPresent(isPresent);
	}
	public void draw(Renderer renderer) {
		if(isGhost) {
			gp.world.lightingM.moveLight(ghostLight, (int)(hitbox.x + hitbox.width/2 - 8), (int)(hitbox.y +  hitbox.height/2));
		}
		super.draw(renderer);
        
	}
	
	
}

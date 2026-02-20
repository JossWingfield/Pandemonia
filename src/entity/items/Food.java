package entity.items;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Food extends Item {
	
	public FoodState foodState;
	
	protected int foodLayer = 0;
	protected TextureRegion burntImage;
	public TextureRegion potPlated, panPlated, rawImage, frozenImage, choppedImage, generalPlated, ovenPlated;
	private String cookedBy = "";
	private String secondaryCookedBy = "";
	public boolean cutIntoNewItem = false;
	public boolean notRawItem = false;
	protected int chopCount = 12;
	protected int cookTime = 24;

	public Food(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		foodState = FoodState.RAW;
		burntImage = importImage("/food/BurntFood.png").toTextureRegion();
	}
	public void addCookMethod(String method) {
		if(cookedBy == null || cookedBy.equals("")) {
			cookedBy = method;
		} else {
			secondaryCookedBy = method;
		}
	}
	public void setState(int state) {
		switch(state) {
		case 0:
			this.foodState = FoodState.RAW;
			break;
		case 1:
			this.foodState = FoodState.CHOPPED;
			break;
		case 2:
			this.foodState = FoodState.COOKED;
			break;
		case 3:
			this.foodState = FoodState.PLATED;
			break;
		case 5:
			this.foodState = FoodState.FROZEN;
			break;
		}
	}
	public void setState(FoodState state) {
		this.foodState = state;
	}
	public int getState() {
		switch(foodState) {
		case RAW:
			return 0;
		case CHOPPED:
			return 1;
		case COOKED:
			return 2;
		case PLATED:
			return 3;
		case FROZEN:
			return 5;
		}
		return -1;
	}
	public int getFoodLayer() {
		return foodLayer;
	}
	 public void draw(Renderer renderer) {
		 TextureRegion img = animations[0][0][0];
		 switch(foodState) {
		 case RAW:
			 img = rawImage;
			 break;
		 case COOKED:
			 if(cookedBy.equals("Frying Pan")) {
				 img = panPlated;
			 } else if(cookedBy.equals("Small Pot")){
				 img = potPlated;
			 } else if(cookedBy.equals("Oven") || cookedBy.equals("Oven Tray")){
				 img = ovenPlated;
			 }
			 break;
		 case PLATED:
			 if(cookedBy.equals("Frying Pan")) {
				 img = panPlated;
			 } else if(cookedBy.equals("Small Pot")){
				 img = potPlated;
			 } else if(cookedBy.equals("Oven") || cookedBy.equals("Oven Tray")){
				 img = ovenPlated;
			 } else {
				 img = generalPlated;
			 }
			 break;
		 case CHOPPED:
			 img = choppedImage;
			 break;
		 case FROZEN:
			 img = frozenImage;
			 break;
		 }
		 
		 renderer.draw(img, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

	 }
	 public TextureRegion getImage() {
		 TextureRegion img = animations[0][0][0];
		 switch(foodState) {
		 case RAW:
			 //0
			 img = rawImage;
			 break;
		 case COOKED:
			 //1
			 if(cookedBy.equals("Frying Pan")) {
				 img = panPlated;
			 } else if(cookedBy.equals("Small Pot")){
				 img = potPlated;
			 } else if(cookedBy.equals("Oven") || cookedBy.equals("Oven Tray")){
				 img = ovenPlated;
			 }
			 break;
		 case PLATED:
			 //2
			 if(cookedBy.equals("Frying Pan")) {
				 img = panPlated;
			 } else if(cookedBy.equals("Small Pot")){
				 img = potPlated;
			 } else if(cookedBy.equals("Oven") || cookedBy.equals("Oven Tray")){
				 img = ovenPlated;
			 } else {
				 img = generalPlated;
			 }
			 break;
		 case CHOPPED:
			 //4
			 img = choppedImage;
			 break;
		 case BURNT:
			 img = burntImage;
			 break;
		 case FROZEN:
			 img = frozenImage;
			 break;
		 }
		 return img;
	 }
	 public String getCookMethod() {
			return cookedBy;
		 }
	 public String getSecondaryCookMethod() {
		return secondaryCookedBy;
	 }
	 public void setCookMethod(String cookedBy) {
		this.cookedBy = cookedBy;
	 }
	 public void setSecondaryCookMethod(String secondaryCookedBy) {
		this.secondaryCookedBy = secondaryCookedBy;
	 }
	 public int getChopCount() {
		if(gp.world.progressM.choppingBoardUpgradeI) {
			return (int)(chopCount * 0.75);
		}
		return chopCount;
	}
	 public int getMaxCookTime() {
		 if(gp.world.progressM.stoveUpgradeI) {
			return (int)(cookTime*0.75);
		 } else if(gp.world.progressM.stoveUpgradeII) {
			return (int)(cookTime*0.5);
		 } else if(gp.world.progressM.stoveUpgradeIII) {
			return (int)(cookTime*0.25);
		 }
		 return cookTime;
	 } 
	
}

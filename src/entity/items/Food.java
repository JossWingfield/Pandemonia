package entity.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Food extends Item {
	
	public FoodState foodState;
	
	protected int foodLayer = 0;
	private BufferedImage burntImage;
	public boolean cutIntoNewItem = false;
	public boolean notRawItem = false;

	public Food(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		foodState = FoodState.RAW;
		burntImage = importImage("/food/BurntFood.png");
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
		case 4:
			this.foodState = FoodState.FINISHED;
			break;
		}
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
		case FINISHED:
			return 4;
		}
		return -1;
	}
	public int getFoodLayer() {
		return foodLayer;
	}
	 public void draw(Graphics2D g, int xDiff, int yDiff) {
		 BufferedImage img = animations[0][0][0];
		 switch(foodState) {
		 case RAW:
			 img = animations[0][0][0];
			 break;
		 case COOKED:
			 img = animations[0][0][1];
			 break;
		 case PLATED:
			 img = animations[0][0][2];
			 break;
		 case FINISHED:
			 img = animations[0][0][3];
			 break;
		 case CHOPPED:
			 img = animations[0][0][4];
			 break;
		 }
		 
		 g.drawImage(img, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);

	 }
	 public BufferedImage getImage() {
		 BufferedImage img = animations[0][0][0];
		 switch(foodState) {
		 case RAW:
			 img = animations[0][0][0];
			 break;
		 case COOKED:
			 img = animations[0][0][1];
			 break;
		 case PLATED:
			 img = animations[0][0][2];
			 break;
		 case FINISHED:
			 img = animations[0][0][3];
			 break;
		 case CHOPPED:
			 img = animations[0][0][4];
			 break;
		 case BURNT:
			 img = burntImage;
			 break;
		 }
		 return img;
	 }
	
}

package utility;

import java.awt.image.BufferedImage;

import entity.items.Asparagus;
import entity.items.Aubergine;
import entity.items.Bread;
import entity.items.BurntFood;
import entity.items.Carrot;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.ChickenPieces;
import entity.items.ChoppedTomatoes;
import entity.items.Corn;
import entity.items.CursedGreens;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import entity.items.FryingPan;
import entity.items.Greens;
import entity.items.Item;
import entity.items.Leek;
import entity.items.Meatball;
import entity.items.Penne;
import entity.items.Plate;
import entity.items.Potato;
import entity.items.RedOnion;
import entity.items.SmallPan;
import entity.items.Spaghetti;
import entity.items.Steak;
import entity.items.Tomato;
import main.GamePanel;

public class ItemRegistry {
	
	GamePanel gp;
	
	public ItemRegistry(GamePanel gp) {
		this.gp = gp;
	}
	
	public Item getItemFromName(String name, int state) {
		Item i = null;
		switch(name) {
			case "Cheese" -> i = new Cheese(gp, 0, 0);
			case "Chicken" -> i = new Chicken(gp, 0, 0);
			case "Fish" -> i = new Fish(gp, 0, 0);
			case "Bread" -> i = new Bread(gp, 0, 0);
			case "Greens" -> i = new Greens(gp, 0, 0);
			case "Egg" -> i = new Egg(gp, 0, 0);
			case "Carrot" -> i = new Carrot(gp, 0, 0);
			case "Red Onion" -> i = new RedOnion(gp, 0, 0);
			case "Potato" -> i = new Potato(gp, 0, 0);
			case "Steak" -> i = new Steak(gp, 0, 0);
			case "Corn" -> i = new Corn(gp, 0, 0);
			case "Tomato" -> i = new Tomato(gp, 0, 0);
			case "Aubergine" -> i = new Aubergine(gp, 0, 0);
			case "Asparagus" -> i = new Asparagus(gp, 0, 0);
			case "Leek" -> i = new Leek(gp, 0, 0);
			case "Meatball" -> i = new Meatball(gp, 0, 0);
			case "Chicken Pieces" -> i = new ChickenPieces(gp, 0, 0);
			case "Chopped Tomatoes" -> i = new ChoppedTomatoes(gp, 0, 0);
			case "Spaghetti" -> i = new Spaghetti(gp, 0, 0);
			case "Penne" -> i = new Penne(gp, 0, 0);
			case "Cursed Greens" -> i = new CursedGreens(gp, 0, 0);
			case "Burnt Food" -> i = new BurntFood(gp, 0, 0);
			case "Plate" -> i = new Plate(gp, 0, 0);
			case "Frying Pan" -> i = new FryingPan(gp, 0, 0);
			case "Small Pan" -> i = new SmallPan(gp, 0, 0);
		}
		if(i instanceof Food food) {
			food.setState(state);
		}
		return i;
	}
	public Item getRawIngredientFromName(String name, int state) {
		Item i = null;
		switch(name) {
			case "Meatball" -> i = new Steak(gp, 0, 0);
			case "Chopped Tomatoes" -> i = new Tomato(gp, 0, 0);
			case "Chicken Pieces" -> i = new Chicken(gp, 0, 0);
		}
		if(i instanceof Food food) {
			food.setState(state);
		}
		
		return i;
	}
	public Item getItemFromName(int x, int y, String name, int state) {
		Item i = getItemFromName(name, state);
		i.hitbox.x = x;
		i.hitbox.y = y;
		return i;
	}
	public BufferedImage getRawIngredientImage(String name) {
		Item item = getRawIngredientFromName(name, 0);
		if (item != null && item.animations != null && item.animations.length > 0 && item.animations[0].length > 0 && item.animations[0][0].length > 0) {
			return item.animations[0][0][0]; // Adjust if your image indexing is different
		}
		return null;
	}
	public BufferedImage getImageFromName(String name) {
		Item item = getItemFromName(name, 0);
		if (item != null && item.animations != null && item.animations.length > 0 && item.animations[0].length > 0 && item.animations[0][0].length > 0) {
			return item.animations[0][0][0]; // Adjust if your image indexing is different
		}
		return null;
	}
}

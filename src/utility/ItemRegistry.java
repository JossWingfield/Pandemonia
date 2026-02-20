package utility;

import entity.items.Asparagus;
import entity.items.Aubergine;
import entity.items.Basil;
import entity.items.Bread;
import entity.items.BreadSlice;
import entity.items.BurntFood;
import entity.items.Carrot;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.ChickenPieces;
import entity.items.ChoppedGarlic;
import entity.items.ChoppedTomatoes;
import entity.items.CookingItem;
import entity.items.Corn;
import entity.items.CursedGreens;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import entity.items.Fryer;
import entity.items.FryingPan;
import entity.items.Garlic;
import entity.items.Greens;
import entity.items.IceBlock;
import entity.items.Item;
import entity.items.Lasagna;
import entity.items.Leek;
import entity.items.Meatball;
import entity.items.Onion;
import entity.items.OvenTray;
import entity.items.Penne;
import entity.items.Plate;
import entity.items.Potato;
import entity.items.RedOnion;
import entity.items.Rosemary;
import entity.items.Sage;
import entity.items.SmallPan;
import entity.items.Spaghetti;
import entity.items.Steak;
import entity.items.Thyme;
import entity.items.Tomato;
import main.GamePanel;
import main.renderer.TextureRegion;
import net.data.ItemData;

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
			case "Bread Slice" -> i = new BreadSlice(gp, 0, 0);
			case "Greens" -> i = new Greens(gp, 0, 0);
			case "Egg" -> i = new Egg(gp, 0, 0);
			case "Carrot" -> i = new Carrot(gp, 0, 0);
			case "Red Onion" -> i = new RedOnion(gp, 0, 0);
			case "Onion" -> i = new Onion(gp, 0, 0);
			case "Potato" -> i = new Potato(gp, 0, 0);
			case "Garlic" -> i = new Garlic(gp, 0, 0);
			case "Chopped Garlic" -> i = new ChoppedGarlic(gp, 0, 0);
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
			case "Lasagna" -> i = new Lasagna(gp, 0, 0);
			case "Thyme" -> i = new Thyme(gp, 0, 0);
			case "Basil" -> i = new Basil(gp, 0, 0);
			case "Rosemary" -> i = new Rosemary(gp, 0, 0);
			case "Sage" -> i = new Sage(gp, 0, 0);
			case "Ice Block" -> i = new IceBlock(gp, null);
			case "Cursed Greens" -> i = new CursedGreens(gp, 0, 0);
			case "Burnt Food" -> i = new BurntFood(gp, 0, 0);
			case "Plate" -> i = new Plate(gp, 0, 0);
			case "Frying Pan" -> i = new FryingPan(gp, 0, 0);
			case "Small Pot" -> i = new SmallPan(gp, 0, 0);
			case "Oven Tray" -> i = new OvenTray(gp, 0, 0);
			case "Fryer" -> i = new Fryer(gp, 0, 0);
		}
		if(i instanceof Food food) {
			food.setState(state);
			if(state == 1) {
				food.addCookMethod("Chopping Board");
			}
		}
		return i;
	}
	public Item getRawIngredientFromName(String name, int state) {
		Item i = null;
		switch(name) {
			case "Meatball" -> i = new Steak(gp, 0, 0);
			case "Chopped Tomatoes" -> i = new Tomato(gp, 0, 0);
			case "Chicken Pieces" -> i = new Chicken(gp, 0, 0);
			case "Bread Slice" -> i = new Bread(gp, 0, 0);
			case "Chopped Garlic" -> i = new Garlic(gp, 0, 0);
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
	public TextureRegion getRawIngredientImage(String name) {
		Item item = getRawIngredientFromName(name, 0);
		if (item != null && item.animations != null && item.animations.length > 0 && item.animations[0].length > 0 && item.animations[0][0].length > 0) {
			if(item instanceof Food f) {
				return f.rawImage;
			} else {
				return item.animations[0][0][0]; 
			}
		}
		return null;
	}
	public TextureRegion getImageFromName(String name) {
		Item item = getItemFromName(name, 0);
		if(item instanceof Food f) {
			if(f.notRawItem) {
				item = getRawIngredientFromName(name, 0);
			}
		}
		if (item != null && item.animations != null && item.animations.length > 0 && item.animations[0].length > 0 && item.animations[0][0].length > 0) {
			if(item instanceof Food f) {
				return f.rawImage;
			} else {
				return item.animations[0][0][0]; 
			}
		}
		return null;
	}
	public Item getItemFromItemData(ItemData data) {

	    // 1️⃣ Create base item from name + food state
	    Item item = getItemFromName(data.itemName, data.hasFoodData ? data.foodState : 0);
	    if (item == null) return null;

	    // ================= FOOD =================
	    if (data.hasFoodData && item instanceof Food food) {
	        food.setState(data.foodState);

	        if (data.cookedBy != null && !data.cookedBy.isEmpty()) {
	            food.setCookMethod(data.cookedBy);
	        }

	        if (data.secondaryCookedBy != null && !data.secondaryCookedBy.isEmpty()) {
	            food.setSecondaryCookMethod(data.secondaryCookedBy);
	        }
	    }

	    // ================= PLATE =================
	    if (data.isPlate && item instanceof Plate plate) {

	        plate.clearIngredients();

	        if (data.plateIngredients != null) {
	            for (ItemData ingredientData : data.plateIngredients) {
	                Item ingredient = getItemFromItemData(ingredientData);
	                if (ingredient instanceof Food food) {
	                    plate.addIngredient(food);
	                }
	            }
	        }

	        plate.setSeasoningQuality(data.seasoningQuality);
	    }

	    // ================= PAN / COOKING ITEM =================
	    if (data.isPan && item instanceof CookingItem cookingItem) {

	        cookingItem.setCookTime((int) data.cookProgress);

	        if (data.cookingFood != null) {
	            Item foodItem = getItemFromItemData(data.cookingFood);
	            if (foodItem instanceof Food f) {
	                cookingItem.cookingItem = f;
	            }
	        }
	    }

	    // ================= OVEN TRAY =================
	    if (data.isOvenTray && item instanceof OvenTray ovenTray) {

	        ovenTray.clearIngredients();

	        if (data.ovenIngredients != null) {
	            for (ItemData ingData : data.ovenIngredients) {
	                Item ingItem = getItemFromItemData(ingData);
	                if (ingItem instanceof Food f) {
	                    ovenTray.addIngredient(f);
	                }
	            }
	        }
	    }

	    return item;
	}
}

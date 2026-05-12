package utility;

import entity.items.Asparagus;
import entity.items.Aubergine;
import entity.items.Basil;
import entity.items.Bread;
import entity.items.BreadCrumbs;
import entity.items.BreadSlice;
import entity.items.BurntFood;
import entity.items.Carrot;
import entity.items.Chamomile;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.ChickenPieces;
import entity.items.ChilliFlakes;
import entity.items.ChoppedGarlic;
import entity.items.ChoppedTomatoes;
import entity.items.CookingItem;
import entity.items.CorianderSeeds;
import entity.items.Corn;
import entity.items.Cumin;
import entity.items.CursedGreens;
import entity.items.Egg;
import entity.items.FennelSeeds;
import entity.items.Fish;
import entity.items.Food;
import entity.items.Fryer;
import entity.items.FryingPan;
import entity.items.Garlic;
import entity.items.GarlicPowder;
import entity.items.Greens;
import entity.items.IceBlock;
import entity.items.Item;
import entity.items.Lasagna;
import entity.items.Leek;
import entity.items.Meatball;
import entity.items.Menu;
import entity.items.Mint;
import entity.items.Onion;
import entity.items.OvenTray;
import entity.items.Paprika;
import entity.items.Penne;
import entity.items.Plate;
import entity.items.Potato;
import entity.items.RedOnion;
import entity.items.Rosemary;
import entity.items.Sage;
import entity.items.SeasoningBlend;
import entity.items.SmallPan;
import entity.items.Spaghetti;
import entity.items.StarAnise;
import entity.items.Steak;
import entity.items.Thyme;
import entity.items.Tomato;
import entity.items.Turmeric;
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
			case "Cheese" -> i = new Cheese(gp);
			case "Chicken" -> i = new Chicken(gp);
			case "Fish" -> i = new Fish(gp);
			case "Bread" -> i = new Bread(gp);
			case "Bread Slice" -> i = new BreadSlice(gp);
			case "Greens" -> i = new Greens(gp);
			case "Egg" -> i = new Egg(gp);
			case "Carrot" -> i = new Carrot(gp);
			case "Red Onion" -> i = new RedOnion(gp);
			case "Onion" -> i = new Onion(gp);
			case "Potato" -> i = new Potato(gp);
			case "Garlic" -> i = new Garlic(gp);
			case "Chopped Garlic" -> i = new ChoppedGarlic(gp);
			case "Steak" -> i = new Steak(gp);
			case "Corn" -> i = new Corn(gp);
			case "Tomato" -> i = new Tomato(gp);
			case "Aubergine" -> i = new Aubergine(gp);
			case "Asparagus" -> i = new Asparagus(gp);
			case "Leek" -> i = new Leek(gp);
			case "Meatball" -> i = new Meatball(gp);
			case "Chicken Pieces" -> i = new ChickenPieces(gp);
			case "Chopped Tomatoes" -> i = new ChoppedTomatoes(gp);
			case "Spaghetti" -> i = new Spaghetti(gp);
			case "Penne" -> i = new Penne(gp);
			case "Lasagna" -> i = new Lasagna(gp);
			case "Thyme" -> i = new Thyme(gp);
			case "Basil" -> i = new Basil(gp);
			case "Rosemary" -> i = new Rosemary(gp);
			case "Sage" -> i = new Sage(gp);
			case "Chamomile" -> i = new Chamomile(gp);
			case "Mint" -> i = new Mint(gp);
			case "Paprika" -> i = new Paprika(gp);
			case "Turmeric" -> i = new Turmeric(gp);
			case "Cumin" -> i = new Cumin(gp);
			case "Chilli Flakes" -> i = new ChilliFlakes(gp);
			case "Star Anise" -> i = new StarAnise(gp);
			case "Coriander Seeds" -> i = new CorianderSeeds(gp);
			case "Fennel Seeds" -> i = new FennelSeeds(gp);
			case "Garlic Powder" -> i = new GarlicPowder(gp);
			case "Seasoning Blend" -> i = new SeasoningBlend(gp);
			case "Bread Crumbs" -> i = new BreadCrumbs(gp);
			case "Ice Block" -> i = new IceBlock(gp, null);
			case "Cursed Greens" -> i = new CursedGreens(gp);
			case "Burnt Food" -> i = new BurntFood(gp);
			case "Plate" -> i = new Plate(gp);
			case "Frying Pan" -> i = new FryingPan(gp);
			case "Small Pot" -> i = new SmallPan(gp);
			case "Oven Tray" -> i = new OvenTray(gp);
			case "Fryer" -> i = new Fryer(gp);
			case "Menu" -> i = new Menu(gp);
		}
		if(i instanceof Food food) {
			food.setState(state);
			if(state == 1) {
				food.addStep("Chopping Board");
			}
		}
		return i;
	}
	public Item getRawIngredientFromName(String name, int state) {
		Item i = null;
		switch(name) {
			case "Meatball" -> i = new Steak(gp);
			case "Chopped Tomatoes" -> i = new Tomato(gp);
			case "Chicken Pieces" -> i = new Chicken(gp);
			case "Bread Slice" -> i = new Bread(gp);
			case "Chopped Garlic" -> i = new Garlic(gp);
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

	        if (data.steps != null) {
	            for (String station : data.steps) {
	                food.addStep(station);
	            }
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

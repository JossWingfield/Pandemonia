package entity.items;

import main.GamePanel;

public class CookStyleResolver {

	  public static CookStyle resolve(Food food, CookingItem pan, GamePanel gp) {

	        String name = food.getName();
	        
	        if(pan instanceof FryingPan) {
	        	return switch(name) {
	        		case "Egg" -> CookStyle.FLIP;
	           		case "Steak" -> CookStyle.FLIP;
	           		case "Meatball" -> CookStyle.SAUTE;
	           		case "Chicken Pieces" -> CookStyle.SAUTE;
	        		case "Chopped Tomatoes" -> CookStyle.SAUTE;
	        		default -> CookStyle.PASSIVE;
	        	};
	        } else if(pan instanceof SmallPan) {
	        	return switch(name) {
        		case "Spaghetti" -> CookStyle.STIR;
        		case "Penne" -> CookStyle.STIR;
           		case "Fish" -> CookStyle.PASSIVE;

        		default -> CookStyle.PASSIVE;
        	};
	        }

	        // Default
	        return CookStyle.PASSIVE;
	    }
	
	
}

package utility.recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientScore {
	
	 private String ingredientName;

	 private List<String> cookMethods = new ArrayList<>();
	 private List<ActionScore> actions = new ArrayList<>();


	 public static class ActionScore {
	        public String action;     // "Chop", "Cook", "Flip", etc
	        public double quality;
	        public String grade;

	        public ActionScore(String action, double quality, String grade) {
	            this.action = action;
	            this.quality = quality;
	            this.grade = grade;
	        }
	 }
	 
	 public IngredientScore(String ingredientName) {
		 this.ingredientName = ingredientName;
	 }
	 public void addAction(String action, double quality, String grade) {
		 actions.add(new ActionScore(action, quality, grade));
	 }
	 public List<ActionScore> getActions() {
		 return actions;
	 }
	 public List<String> getCookMethods() {
		return cookMethods;
	 }
	 public String getIngredientName() {
		return ingredientName;
	 }
}

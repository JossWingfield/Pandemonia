package utility.save;

import java.util.List;

import main.GamePanel;
import utility.Recipe;
import utility.RecipeManager;

public class RecipeSaveData extends SaveData {
	
    public List<String> unlockedRecipesNames;
	
	public void applySaveData(GamePanel gp) {
		RecipeManager.applySaveData(this);
	}
    
}

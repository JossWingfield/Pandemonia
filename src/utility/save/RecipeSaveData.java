package utility.save;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import utility.RecipeManager;

public class RecipeSaveData extends SaveData {
	
    public List<String> unlockedRecipesNames;
    public List<Integer> starLevels = new ArrayList<>();
    public List<Integer> cookCounts = new ArrayList<>();
    public List<Boolean> masteredFlags = new ArrayList<>();
	
	public void applySaveData(GamePanel gp) {
		RecipeManager.applySaveData(this);
	}
    
}

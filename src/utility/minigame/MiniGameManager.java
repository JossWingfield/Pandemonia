package utility.minigame;

import entity.buildings.SpiceTable;
import entity.items.Plate;
import entity.items.Seasoning;
import main.GamePanel;
import main.renderer.Renderer;

public class MiniGameManager {
	
    public SeasoningMiniGame seasoningMiniGame;
    public GrindingMiniGame grindingMiniGame;
    
    public boolean miniGameActive = false;

    public MiniGameManager(GamePanel gp) {
        seasoningMiniGame = new SeasoningMiniGame(gp);
        grindingMiniGame = new GrindingMiniGame(gp);
    }

    public void startSeasoningMiniGame(Plate plate, Seasoning seasoning) {
        seasoningMiniGame.start(plate, seasoning);
        miniGameActive = true;
    }
    public void startGrindingMiniGame(SpiceTable table, Seasoning seasoning) {
        grindingMiniGame.start(table, seasoning);
        miniGameActive = true;
    }
    public void updateState(double dt) {
        seasoningMiniGame.updateState(dt);
        grindingMiniGame.updateState(dt);
    }
    public void inputUpdate(double dt) {
        seasoningMiniGame.inputUpdate(dt);
        grindingMiniGame.inputUpdate(dt);
    }

    public void draw(Renderer renderer) {
        seasoningMiniGame.draw(renderer);
        grindingMiniGame.draw(renderer);
    }
}

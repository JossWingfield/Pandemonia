package utility.minigame;

import entity.items.Plate;
import entity.items.Seasoning;
import main.GamePanel;
import main.renderer.Renderer;

public class MiniGameManager {
    public SeasoningMiniGame seasoningMiniGame;
    
    public boolean miniGameActive = false;

    public MiniGameManager(GamePanel gp) {
        seasoningMiniGame = new SeasoningMiniGame(gp);
    }

    public void startSeasoningMiniGame(Plate plate, Seasoning seasoning) {
        seasoningMiniGame.start(plate, seasoning);
        miniGameActive = true;
    }

    public void updateState(double dt) {
        seasoningMiniGame.updateState(dt);
    }
    public void inputUpdate(double dt) {
        seasoningMiniGame.inputUpdate(dt);
    }

    public void draw(Renderer renderer) {
        // other UI elements...
        seasoningMiniGame.draw(renderer);
    }
}

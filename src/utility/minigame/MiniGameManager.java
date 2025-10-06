package utility.minigame;

import java.awt.Graphics2D;

import entity.items.Plate;
import entity.items.Seasoning;
import main.GamePanel;

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

    public void update() {
        seasoningMiniGame.update();
        // other UI updates...
    }

    public void draw(Graphics2D g2) {
        // other UI elements...
        seasoningMiniGame.draw(g2);
    }
}

package main;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import entity.buildings.FloorDecor_Building;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import map.Beam;
import map.ChairSkin;
import map.DoorSkin;
import map.FloorPaper;
import map.TableSkin;
import map.WallPaper;

public class KeyListener {

    GamePanel gp;

    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];
    
    public boolean showHitboxes = false;
    public boolean debugMode = false;

    public KeyListener(GamePanel gp) {
        this.gp = gp;
    }

    public void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (key < 0 || key >= 350) return;

        if (action == GLFW.GLFW_PRESS) {
            keyPressed[key] = true;
            keyBeginPress[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            keyPressed[key] = false;
            keyBeginPress[key] = false;
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public boolean keyBeginPress(int keyCode) {
        return keyBeginPress[keyCode];
    }

    public void endFrame() {
        Arrays.fill(keyBeginPress, false);
    }

    // -------------------------
    // Process input per frame
    // -------------------------
    public void update() {
        // ----- PLAY STATE -----
        if (gp.currentState == gp.playState) {

            if (keyBeginPress(GLFW.GLFW_KEY_4)) gp.saveM.saveGame();
            if (keyBeginPress(GLFW.GLFW_KEY_5)) gp.saveM.loadGame(gp.saveM.currentSave);
            if (keyBeginPress(GLFW.GLFW_KEY_F)) gp.gui.startLevelUpScreen();
            if (keyBeginPress(GLFW.GLFW_KEY_Q)) gp.player.soulsServed = gp.player.nextLevelAmount;
            if (keyBeginPress(GLFW.GLFW_KEY_C)) gp.currentState = gp.customiseRestaurantState;
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) gp.currentState = gp.pauseState;

            // Movement
            boolean left = isKeyPressed(GLFW.GLFW_KEY_A) || isKeyPressed(GLFW.GLFW_KEY_LEFT);
            boolean right = isKeyPressed(GLFW.GLFW_KEY_D) || isKeyPressed(GLFW.GLFW_KEY_RIGHT);
            boolean up = isKeyPressed(GLFW.GLFW_KEY_W) || isKeyPressed(GLFW.GLFW_KEY_UP);
            boolean down = isKeyPressed(GLFW.GLFW_KEY_S) || isKeyPressed(GLFW.GLFW_KEY_DOWN);
            // Other actions
            boolean shiftPressed = isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT);
            boolean ePressed = isKeyPressed(GLFW.GLFW_KEY_E);

            // Debug
            if (keyBeginPress(GLFW.GLFW_KEY_KP_EQUAL)) debugMode = !debugMode;
            if (keyBeginPress(GLFW.GLFW_KEY_B)) showHitboxes = !showHitboxes;
            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) gp.currentState = gp.mapBuildState;
            if (keyBeginPress(GLFW.GLFW_KEY_MINUS)) gp.mapM.drawPath = !gp.mapM.drawPath;

        }
        // ----- MAP BUILD STATE -----
        else if (gp.currentState == gp.mapBuildState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) gp.currentState = gp.playState;

            if (keyBeginPress(GLFW.GLFW_KEY_E)) gp.mapB.showTiles = !gp.mapB.showTiles;
            if (keyBeginPress(GLFW.GLFW_KEY_BACKSPACE)) gp.mapB.switchSides();
            if (keyBeginPress(GLFW.GLFW_KEY_UP)) gp.mapB.upLayer();
            if (keyBeginPress(GLFW.GLFW_KEY_DOWN)) gp.mapB.downLayer();
            if (keyBeginPress(GLFW.GLFW_KEY_P)) gp.buildingM.outputMap();

            // Page selection
            if (keyBeginPress(GLFW.GLFW_KEY_1)) gp.mapB.pageNum = 1;
            if (keyBeginPress(GLFW.GLFW_KEY_2)) gp.mapB.pageNum = 2;
            if (keyBeginPress(GLFW.GLFW_KEY_3)) gp.mapB.pageNum = 3;
            if (keyBeginPress(GLFW.GLFW_KEY_4)) gp.mapB.pageNum = 4;
            if (keyBeginPress(GLFW.GLFW_KEY_5)) gp.mapB.pageNum = 5;
            if (keyBeginPress(GLFW.GLFW_KEY_6)) gp.mapB.pageNum = 6;

            // Tile selection
            if (keyBeginPress(GLFW.GLFW_KEY_0)) {
                gp.mapB.selectedTile = 0;
                gp.mapB.selectedBuilding = null;
            }
            if (keyBeginPress(GLFW.GLFW_KEY_L)) {
                gp.mapB.selectedTile = 1110;
                gp.mapB.selectedBuilding = null;
            }
        }
        // ----- PAUSE STATE -----
        else if (gp.currentState == gp.pauseState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) gp.currentState = gp.playState;
        }
        // ----- CUSTOMISE RESTAURANT -----
        else if (gp.currentState == gp.customiseRestaurantState) {
            if (keyBeginPress(GLFW.GLFW_KEY_C)) {
                gp.customiser.selectedBuilding = null;
                gp.customiser.showBuildings = true;
                gp.currentState = gp.playState;
            }
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                if (!gp.customiser.showBuildings) {
                    gp.customiser.showBuildings = true;
                    gp.customiser.selectedBuilding = null;
                } else {
                    gp.currentState = gp.playState;
                }
            }
            if(keyBeginPress(GLFW.GLFW_KEY_L)) {
         		gp.customiser.addToInventory(new Beam(gp, 6));
         		gp.customiser.addToInventory(new WallPaper(gp, 33));
         		gp.customiser.addToInventory(new FloorPaper(gp, 7));
         		gp.customiser.addToInventory(new DoorSkin(gp, 3));
         		gp.customiser.addToInventory(new ChairSkin(gp, 6));
         		gp.customiser.addToInventory(new TableSkin(gp, 3));
        		gp.customiser.addToInventory(new Window(gp, 0, 0, 2));
        		gp.customiser.addToInventory(new FloorDecor_Building(gp, 0, 0, 108));
        		gp.customiser.addToInventory(new FloorDecor_Building(gp, 0, 0, 109));
        		gp.customiser.addToInventory(new WallDecor_Building(gp, 0, 0, 38));
         		
            }
        }
        // ----- CATALOGUE STATE -----
        else if (gp.currentState == gp.catalogueState) {
            if (keyBeginPress(GLFW.GLFW_KEY_E)) {
                gp.currentState = gp.playState;
                gp.catalogue.resetBasket();
            }
            if (gp.catalogue.checkingOut && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                gp.catalogue.checkingOut = false;
            } else if (!gp.catalogue.checkingOut && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                gp.currentState = gp.playState;
                gp.catalogue.resetBasket();
            }
            if (isKeyPressed(GLFW.GLFW_KEY_S) || isKeyPressed(GLFW.GLFW_KEY_DOWN)) gp.catalogue.downLayer();
            if (isKeyPressed(GLFW.GLFW_KEY_W) || isKeyPressed(GLFW.GLFW_KEY_UP)) gp.catalogue.upLayer();
        }

        // ----- ACHIEVEMENT STATE -----
        if (gp.currentState == gp.achievementState && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            gp.currentState = gp.pauseState;
        }
    }
	
}

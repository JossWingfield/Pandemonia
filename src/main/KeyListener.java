package main;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import entity.buildings.Gate;
import entity.buildings.KitchenCounter;
import entity.buildings.Shelf;

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

            // ---- BACKSPACE ----
            if (key == GLFW.GLFW_KEY_BACKSPACE && gp.gui != null) {

                // TextBoxes handle their own deletion
                if (gp.currentState == gp.createWorldScreen) {
                    gp.gui.playerNameBox.onCharTyped('\b');
                    gp.gui.worldNameBox.onCharTyped('\b');
                    return;
                } 
                
                if (gp.currentState == gp.createJoinPlayerScreen) {
                    gp.gui.playerNameBox.onCharTyped('\b');
                    return;
                } 
                
                if (gp.currentState == gp.writeUsernameState) {
                    gp.gui.usernameBox.onCharTyped('\b');
                    return;
                }

                // Chat input (if you haven't converted it yet)
                if (gp.currentState == gp.chatState) {
                    String msg = gp.gui.chatBox.getText();
                    gp.gui.chatBox.onCharTyped('\b');

                    if (!msg.isBlank()) {
                        //gp.gui.sendChatMessage(msg);
                        //gp.gui.chatBox.setText("");
                    }
                    return;
                }
            }

            // ---- NORMAL KEY TRACKING ----
            if (!keyPressed[key]) {
                keyBeginPress[key] = true;
            }
            keyPressed[key] = true;
        }
        else if (action == GLFW.GLFW_RELEASE) {
            keyPressed[key] = false;
            keyBeginPress[key] = false;
        }
    }
    
    public void charCallback(long window, int codepoint) {

        if (gp.gui == null) return;

        char c = (char) codepoint;

        // ---- Create World Screen ----
        if (gp.currentState == gp.createWorldScreen) {
            gp.gui.playerNameBox.onCharTyped(c);
            gp.gui.worldNameBox.onCharTyped(c);
            return;
        }
        if (gp.currentState == gp.createJoinPlayerScreen) {
            gp.gui.playerNameBox.onCharTyped(c);
            return;
        }
        
        if (gp.currentState == gp.writeUsernameState) {
            gp.gui.usernameBox.onCharTyped(c);
            return;
        }

        // ---- Chat ----
        if (gp.currentState == gp.chatState) {
            gp.gui.chatBox.onCharTyped(c);
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
        	
            /*
            if (isKeyPressed(GLFW.GLFW_KEY_1)) {
            	gp.renderer.intensity+=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_2)) {
            	gp.renderer.intensity-=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_3)) {
            	gp.renderer.decay+=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_4)) {
            	gp.renderer.decay-=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_5)) {
            	gp.renderer.density+=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_6)) {
            	gp.renderer.density-=0.01f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_7)) {
            	gp.renderer.samples +=1;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_8)) {
            	gp.renderer.samples -= 1;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_9)) {
            	gp.renderer.verticalBias +=0.1f;
            }
            if (isKeyPressed(GLFW.GLFW_KEY_0)) {
            	gp.renderer.verticalBias -= 0.1f;
            }*/
            
            /*if (keyBeginPress(GLFW.GLFW_KEY_2)) {
            	Plate plate = new Plate(gp);
            	Greens g = new Greens(gp);
            	g.setState(FoodState.PLATED);
            	g.addStep("Chopping Board");
            	plate.addIngredient(g);
            	Tomato t = new Tomato(gp);
              	t.setState(FoodState.PLATED);
        		t.addStep("Chopping Board");
            	plate.addIngredient(t);
            	gp.gui.addRecipeGrading(plate);
            }*/
        	
            //if (keyBeginPress(GLFW.GLFW_KEY_P)) gp.saveM.startRecording();
            //if (keyBeginPress(GLFW.GLFW_KEY_O)) gp.saveM.stopRecording();
        	
            if (keyBeginPress(GLFW.GLFW_KEY_6)) {
            	gp.world.gameM.setSeason("Spring");
            }
            if (keyBeginPress(GLFW.GLFW_KEY_7)) {
            	gp.world.gameM.setSeason("Summer");
            }
            if (keyBeginPress(GLFW.GLFW_KEY_8)) {
            	gp.world.gameM.setSeason("Autumn");
            }
            if (keyBeginPress(GLFW.GLFW_KEY_9)) {
            	gp.world.gameM.setSeason("Winter");
            }
            if (keyBeginPress(GLFW.GLFW_KEY_3)) {
            	//gp.world.catalogue.obtainAllItemsWithinCatalogue(0);
            }
            if (keyBeginPress(GLFW.GLFW_KEY_4)) gp.saveM.saveGame();
            if (keyBeginPress(GLFW.GLFW_KEY_5)) gp.saveM.loadGame(gp.saveM.currentSave);
            if (keyBeginPress(GLFW.GLFW_KEY_F)) gp.gui.startLevelUpScreen();
            if (keyBeginPress(GLFW.GLFW_KEY_Q)) gp.player.soulsServed = gp.player.nextLevelAmount;
            
            
            if (keyBeginPress(GLFW.GLFW_KEY_C)) 
            	if(gp.world.mapM.getRoom(gp.player.currentRoomIndex).canBeEdited) {
            		gp.currentState = gp.customiseRestaurantState;
            	}
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) gp.currentState = gp.pauseState;

            // Movement

            // Debug
            if (keyBeginPress(GLFW.GLFW_KEY_KP_EQUAL)) debugMode = !debugMode;
            if (keyBeginPress(GLFW.GLFW_KEY_B)) showHitboxes = !showHitboxes;
            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) gp.currentState = gp.mapBuildState;
            if (keyBeginPress(GLFW.GLFW_KEY_MINUS)) gp.world.mapM.drawPath = !gp.world.mapM.drawPath;
            if (keyBeginPress(GLFW.GLFW_KEY_T)) {
            	gp.currentState = gp.chatState;
              	gp.gui.chatBox.setActive(true);
            }
        }
        // ----- MAP BUILD STATE -----
        else if (gp.currentState == gp.mapBuildState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) gp.currentState = gp.playState;

            if (keyBeginPress(GLFW.GLFW_KEY_E)) gp.world.mapB.showTiles = !gp.world.mapB.showTiles;
            if (keyBeginPress(GLFW.GLFW_KEY_BACKSPACE)) gp.world.mapB.switchSides();
            if (keyBeginPress(GLFW.GLFW_KEY_UP)) gp.world.mapB.upLayer();
            if (keyBeginPress(GLFW.GLFW_KEY_DOWN)) gp.world.mapB.downLayer();
            if (keyBeginPress(GLFW.GLFW_KEY_P)) {
            	gp.world.buildingM.outputMap();
            }

            // Page selection
            if (keyBeginPress(GLFW.GLFW_KEY_1)) gp.world.mapB.pageNum = 1;
            if (keyBeginPress(GLFW.GLFW_KEY_2)) gp.world.mapB.pageNum = 2;
            if (keyBeginPress(GLFW.GLFW_KEY_3)) gp.world.mapB.pageNum = 3;
            if (keyBeginPress(GLFW.GLFW_KEY_4)) gp.world.mapB.pageNum = 4;
            if (keyBeginPress(GLFW.GLFW_KEY_5)) gp.world.mapB.pageNum = 5;
            if (keyBeginPress(GLFW.GLFW_KEY_6)) gp.world.mapB.pageNum = 6;

            // Tile selection
            if (keyBeginPress(GLFW.GLFW_KEY_0)) {
                gp.world.mapB.selectedTile = 0;
                gp.world.mapB.selectedBuilding = null;
            }
            if (keyBeginPress(GLFW.GLFW_KEY_L)) {
                gp.world.mapB.selectedTile = 1110;
                gp.world.mapB.selectedBuilding = null;
            }
        }
        // ----- PAUSE STATE -----
        else if (gp.currentState == gp.pauseState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) gp.currentState = gp.playState;
        } else if (gp.currentState == gp.chatState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            	gp.currentState = gp.playState;
             	gp.gui.chatActive = false;
            }
            if(keyBeginPress(GLFW.GLFW_KEY_ENTER)) {
            	gp.gui.sendChatMessage(gp.gui.chatBox.getText());
            	gp.gui.chatActive = false;
            	gp.gui.chatBox.setText("");
            }
        }
        // ----- CUSTOMISE RESTAURANT -----
        else if (gp.currentState == gp.customiseRestaurantState) {
            if (keyBeginPress(GLFW.GLFW_KEY_C)) {
            	if(gp.world.customiser.selectedBuilding != null) {
            		gp.world.customiser.selectedBuilding.resetForCustomiser();
            	}
                gp.world.customiser.selectedBuilding = null;
                gp.world.customiser.showBuildings = true;
                gp.currentState = gp.playState;
                gp.world.buildingM.checkBuildingConnections();
            }
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                if (!gp.world.customiser.showBuildings) {
                   	if(gp.world.customiser.selectedBuilding != null) {
                    	gp.world.customiser.selectedBuilding.resetForCustomiser();
                   	}
                    gp.world.customiser.showBuildings = true;
                    gp.world.customiser.selectedBuilding = null;
                    gp.world.buildingM.checkBuildingConnections();
                } else {
                    gp.currentState = gp.playState;
                    gp.world.buildingM.checkBuildingConnections();
                }
            }
        }
        // ----- CATALOGUE STATE -----
        else if (gp.currentState == gp.catalogueState) {
            if (keyBeginPress(GLFW.GLFW_KEY_E)) {
                gp.currentState = gp.playState;
                gp.world.catalogue.resetBasket();
            }
            if (gp.world.catalogue.checkingOut && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                gp.world.catalogue.checkingOut = false;
            } else if (!gp.world.catalogue.checkingOut && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
                gp.currentState = gp.playState;
                gp.world.catalogue.resetBasket();
            }
            if (isKeyPressed(GLFW.GLFW_KEY_S) || isKeyPressed(GLFW.GLFW_KEY_DOWN)) gp.world.catalogue.downLayer();
            if (isKeyPressed(GLFW.GLFW_KEY_W) || isKeyPressed(GLFW.GLFW_KEY_UP)) gp.world.catalogue.upLayer();
        }

        // ----- ACHIEVEMENT STATE -----
        if (gp.currentState == gp.achievementState && keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            gp.currentState = gp.pauseState;
        }
    }
	
}

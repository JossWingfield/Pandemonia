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
    
    // Add these fields at the top of the class
    private double backspaceHeldTimer = 0;
    private static final double BACKSPACE_INITIAL_DELAY = 0.4; // seconds before repeat starts
    private static final double BACKSPACE_REPEAT_RATE = 0.05;  // seconds between repeats
    
    public boolean shiftHeldForMouse = false;

    public KeyListener(GamePanel gp) {
        this.gp = gp;
    }
    
    public void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (key < 0 || key >= 350) return;

        if (action == GLFW.GLFW_PRESS) {

            // ---- NORMAL KEY TRACKING (always runs first) ----
            if (!keyPressed[key]) {
                keyBeginPress[key] = true;
            }
            keyPressed[key] = true;  // <-- moved up here, before any early returns

            // ---- BACKSPACE ----
            if (key == GLFW.GLFW_KEY_BACKSPACE && gp.gui != null) {
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
                if (gp.currentState == gp.chatState) {
                    gp.gui.chatBox.onCharTyped('\b');
                    return;
                }
            }
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
    public void update(double dt) {
    	
    	// ---- HELD BACKSPACE ----
    	// In update(), replace the held backspace block with this:
    	if (isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) && gp.gui != null) {
    	    if (keyBeginPress(GLFW.GLFW_KEY_BACKSPACE)) {
    	        sendBackspace(); // immediate delete on first press
    	        backspaceHeldTimer = 0;
    	    } else {
    	        backspaceHeldTimer += dt;
    	        if (backspaceHeldTimer >= BACKSPACE_INITIAL_DELAY) {
    	            double repeatTime = backspaceHeldTimer - BACKSPACE_INITIAL_DELAY;
    	            double prevRepeatTime = repeatTime - dt;
    	            int prevTicks = (int)(prevRepeatTime < 0 ? 0 : prevRepeatTime / BACKSPACE_REPEAT_RATE);
    	            int currTicks = (int)(repeatTime / BACKSPACE_REPEAT_RATE);
    	            int fires = currTicks - prevTicks;
    	            for (int i = 0; i < fires; i++) sendBackspace();
    	        }
    	    }
    	} else {
    	    backspaceHeldTimer = 0;
    	}
    	
    	
        // ----- PLAY STATE -----
        if (gp.currentState == gp.playState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            	gp.currentState = gp.pauseState;
            	gp.mouseCursor.showCursor();
            }
            
        	if(gp.player.controlEnabled) {
        		
	            if (keyBeginPress(GLFW.GLFW_KEY_T)) {
	            	gp.currentState = gp.chatState;
	            	gp.mouseCursor.showCursor();
	              	gp.gui.chatBox.setActive(true);
	            }
        		
	            if (keyBeginPress(GLFW.GLFW_KEY_C)) {
	            	if(gp.world.mapM.getRoom(gp.player.currentRoomIndex).canBeEdited) {
	            		gp.currentState = gp.customiseRestaurantState;
	            		gp.mouseCursor.showCursor();
	            	}
	            }
	            if (isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
	            	gp.mouseCursor.showCursor();
	            	shiftHeldForMouse = true;
	            } else if(shiftHeldForMouse) {
	            	gp.mouseCursor.hideCursor();
	            	shiftHeldForMouse = false;
	            }
	            
	            
	        	//DEBUG
	        	if (keyBeginPress(GLFW.GLFW_KEY_1)) {
	             	//gp.world.cutsceneM.enterDestroyedRestaurant();
	            }
	            if (keyBeginPress(GLFW.GLFW_KEY_9)) {
	            	gp.world.progressM.unlockOldKitchen();
	            }
	            
	            if (keyBeginPress(GLFW.GLFW_KEY_2)) {
	            	gp.world.customiser.addToInventory(new KitchenCounter(gp, 0, 0, 0));
	            }
	            if (keyBeginPress(GLFW.GLFW_KEY_4)) gp.saveM.saveGame();
	            if (keyBeginPress(GLFW.GLFW_KEY_5)) gp.saveM.loadGame(gp.saveM.currentSave);
	            if (keyBeginPress(GLFW.GLFW_KEY_KP_EQUAL)) debugMode = !debugMode;
	            if (keyBeginPress(GLFW.GLFW_KEY_B)) showHitboxes = !showHitboxes;
	            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) {
	            	gp.currentState = gp.mapBuildState;
	            	gp.mouseCursor.showCursor();
	            }
	            if (keyBeginPress(GLFW.GLFW_KEY_MINUS)) gp.world.mapM.drawPath = !gp.world.mapM.drawPath;
        	}
        }
        // ----- MAP BUILD STATE -----
        else if (gp.currentState == gp.mapBuildState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ENTER)) {
            	gp.currentState = gp.playState;
            	gp.mouseCursor.hideCursor();
            }

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
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            	gp.currentState = gp.playState;
            	gp.mouseCursor.hideCursor();
            }
        } else if (gp.currentState == gp.chatState) {
            if (keyBeginPress(GLFW.GLFW_KEY_ESCAPE)) {
            	gp.currentState = gp.playState;
             	gp.gui.chatActive = false;
             	gp.mouseCursor.hideCursor();
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
                gp.mouseCursor.hideCursor();
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
                	gp.mouseCursor.hideCursor();
                    gp.currentState = gp.playState;
                    gp.world.buildingM.checkBuildingConnections();
                }
            }
        }
        // ----- CATALOGUE STATE -----
        else if (gp.currentState == gp.catalogueState) {
            if (keyBeginPress(GLFW.GLFW_KEY_E)) {
            	gp.player.clickCounter = 0.5;
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
    private void sendBackspace() {
        if (gp.currentState == gp.createWorldScreen) {
            gp.gui.playerNameBox.onCharTyped('\b');
            gp.gui.worldNameBox.onCharTyped('\b');
        } else if (gp.currentState == gp.createJoinPlayerScreen) {
            gp.gui.playerNameBox.onCharTyped('\b');
        } else if (gp.currentState == gp.writeUsernameState) {
            gp.gui.usernameBox.onCharTyped('\b');
        } else if (gp.currentState == gp.chatState) {
            gp.gui.chatBox.onCharTyped('\b');
        }
    }
	
}

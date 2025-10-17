package utility;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.buildings.HerbBasket;

public class KeyboardInput implements KeyListener {
    //HANDLES ALL THE KEYBOARD INPUTS

    //Store the basic booleans, to determine which keys are pressed
    public boolean left, right, up, down, shiftPressed, ePressed, qPressed, showHitboxes, debugMode;

    GamePanel gp;

    public KeyboardInput(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(gp.gui.usernameActive) {
            char c = e.getKeyChar();

            // Allow letters, digits, underscore, and limit length
            if(Character.isLetterOrDigit(c) || c == '_' || c == '-') {
                if(gp.gui.username.length() < 16) {
                    gp.gui.username += c;
                }
            } else if(c == '\b') { // Backspace
                if(gp.gui.username.length() > 0) {
                    gp.gui.username = gp.gui.username.substring(0, gp.gui.username.length() - 1);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { //Sets each boolean to true if the corresponding key is pressed
        int num = e.getKeyCode();
        if(gp.currentState == gp.playState) {
        	
        	if (num == KeyEvent.VK_4) {
                gp.saveM.saveGame();
            }
        	if (num == KeyEvent.VK_5) {
                gp.saveM.loadGame(gp.saveM.currentSave);
            }
            if(num == KeyEvent.VK_F) {
            	gp.gui.startLevelUpScreen();
            }
            if(num == KeyEvent.VK_G) {
            	gp.progressM.moveToNextPhase();
            }
            if(num == KeyEvent.VK_H) {
            	//gp.camera.setZoom(1.5f);
            }
            if(num == KeyEvent.VK_J) {
            	//gp.camera.resetToDefaultZoom();
            }
            if(num == KeyEvent.VK_Q) {
            	gp.player.soulsServed = gp.player.nextLevelAmount;
            }
        	
            //MAIN INPUTS
            if (num == KeyEvent.VK_A || num == KeyEvent.VK_LEFT) {
                left = true;
            }
            if (num == KeyEvent.VK_D || num == KeyEvent.VK_RIGHT) {
                right = true;
            }
            if (num == KeyEvent.VK_W || num == KeyEvent.VK_UP) {
                up = true;
            }
            if (num == KeyEvent.VK_S || num == KeyEvent.VK_DOWN) {
                down = true;
            }
            if (num == KeyEvent.VK_Q) {
                qPressed = true;
            }
            if (num == KeyEvent.VK_C) {
            	gp.currentState = gp.customiseRestaurantState;
            }
            if (num == KeyEvent.VK_P) {
            	gp.currentState = gp.catalogueState;
            	gp.gui.resetComputerAnimations();
            }
            
            if(num == KeyEvent.VK_SHIFT) {
            	shiftPressed = true;
            }
            if(num == KeyEvent.VK_E) {
            	ePressed = true;
            }
            if(num == KeyEvent.VK_ESCAPE) {
            	gp.currentState = gp.pauseState;
            }
            
            //DEBUG
            if (num == KeyEvent.VK_EQUALS) {
                debugMode = !debugMode;
            }
            if (num == KeyEvent.VK_B) {
                //SHOWS HITBOX
                showHitboxes = !showHitboxes;
            }
            if (num == KeyEvent.VK_ENTER) {
                gp.currentState = gp.mapBuildState;
            } 
            if(num == KeyEvent.VK_MINUS) {
            	gp.mapM.drawPath = !gp.mapM.drawPath;
            }


        } else if(gp.currentState == gp.mapBuildState) {
        	if (num == KeyEvent.VK_ENTER) {
                gp.currentState = gp.playState;
            }
        	if(num == KeyEvent.VK_E) {
        		gp.mapB.showTiles = !gp.mapB.showTiles;
        	}
        	if(num == KeyEvent.VK_BACK_SPACE) {
        		gp.mapB.switchSides();
        	}
        	if(num == KeyEvent.VK_UP) {
        		gp.mapB.upLayer();
        	}
        	if(num == KeyEvent.VK_DOWN) {
        		gp.mapB.downLayer();
        	}
        	if(num == KeyEvent.VK_2) {
        		gp.mapB.pageNum = 2;
        	}
        	if(num == KeyEvent.VK_1) {
        		gp.mapB.pageNum = 1;
        	}
        	if(num == KeyEvent.VK_3) {
        		gp.mapB.pageNum = 3;
        	}
        	if(num == KeyEvent.VK_4) {
        		gp.mapB.pageNum = 4;
        	}
        	if(num == KeyEvent.VK_5) {
        		gp.mapB.pageNum = 5;
        	}
        	if(num == KeyEvent.VK_6) {
        		gp.mapB.pageNum = 6;
        	}
        	if(num == KeyEvent.VK_P) {
            	gp.buildingM.outputMap();
            }
        	if(num == KeyEvent.VK_0) {
        		gp.mapB.selectedTile = 0;
        		gp.mapB.selectedBuilding = null;
        	}
         	if(num == KeyEvent.VK_L) {
        		gp.mapB.selectedTile = 1110;
        		gp.mapB.selectedBuilding = null;
        	}
        } else if(gp.currentState == gp.pauseState) {
        	if(num == KeyEvent.VK_ESCAPE) {
            	gp.currentState = gp.playState;
            }
        } else if(gp.currentState == gp.customiseRestaurantState) {
        	if(num == KeyEvent.VK_C) {
            	gp.customiser.selectedBuilding = null;
            	gp.customiser.showBuildings = true;
            	gp.currentState = gp.playState;
            }
        	if(num == KeyEvent.VK_ESCAPE) {
        		if(!gp.customiser.showBuildings) {
            		gp.customiser.showBuildings = true;
            		gp.customiser.selectedBuilding = null;
            	} else {
                	gp.currentState = gp.playState;
            	}
        	}
        } else if(gp.currentState == gp.catalogueState) {
        	if(num == KeyEvent.VK_P) {
        		gp.currentState = gp.playState;
        		gp.catalogue.basket.clear();
        		gp.catalogue.layer = 0;
        		gp.catalogue.pageNum = 1;
        		gp.catalogue.basketCost = 0;
        	}
        	if(gp.catalogue.checkingOut) {
        		if(num == KeyEvent.VK_ESCAPE) {
                	gp.catalogue.checkingOut = false;
                }
        	} else {
            	if(num == KeyEvent.VK_ESCAPE) {
                	gp.currentState = gp.playState;
             		gp.catalogue.basket.clear();
             		gp.catalogue.layer = 0;
             		gp.catalogue.pageNum = 1;
             		gp.catalogue.basketCost = 0;
                }
        	}
        	if(num == KeyEvent.VK_S || num == KeyEvent.VK_DOWN) {
            	gp.catalogue.downLayer();;
            }
        	if(num == KeyEvent.VK_W || num == KeyEvent.VK_UP) {
            	gp.catalogue.upLayer();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) { //Sets each boolean to false if the corresponding key is released
        int num = e.getKeyCode();

            if (num == KeyEvent.VK_A || num == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (num == KeyEvent.VK_D || num == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (num == KeyEvent.VK_W || num == KeyEvent.VK_UP) {
                up = false;
            }
            if (num == KeyEvent.VK_S || num == KeyEvent.VK_DOWN) {
                down = false;
            }
            if (num == KeyEvent.VK_Q) {
                qPressed = false;
            }
            if(num == KeyEvent.VK_E) {
            	ePressed = false;
            }
            
            if (num == KeyEvent.VK_SHIFT) {
                shiftPressed = false;
            }

    }
    

}

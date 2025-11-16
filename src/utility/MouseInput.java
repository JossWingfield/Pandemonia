package utility;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    //HANDLES ALL THE MOUSE INPUTS
    GamePanel gp;

    public int mouseX, mouseY; //Stores the current position of the mouse on the panel
    public boolean leftClickPressed = false; //Determines if the mouse is pressed or not
    public boolean rightClickPressed = false;
    public MouseInput(GamePanel gp) {
        this.gp = gp;
    }

    
    public void mouseClicked(MouseEvent e) {

    }
    
    public void mousePressed(MouseEvent e) { //If the mouse is pressed, the boolean is set to true
        int code = e.getButton();
        if(code == MouseEvent.BUTTON1) {
            leftClickPressed = true;
            if(gp.player != null) {
            	gp.player.leftClick(mouseX, mouseY);
            }
        }
        if(code ==  MouseEvent.BUTTON3) {
            rightClickPressed = true;
            if(gp.player != null) {
            	gp.player.rightClick(mouseX, mouseY);
            }
            if(gp.currentState == gp.customiseRestaurantState) {
            	if(!gp.customiser.showBuildings) {
            		gp.customiser.showBuildings = true;
            		gp.customiser.selectedBuilding = null;
            	}
            }
        }
    }
    
    public void mouseReleased(MouseEvent e) { //If the mouse is released, the boolean is set to false
        int code = e.getButton();
        if(code == MouseEvent.BUTTON1) {
            leftClickPressed = false;
        }
        if(code ==  MouseEvent.BUTTON3) {
            rightClickPressed = false;
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) { //Updates the mouse position co-ordinates, when the mouse is dragged
    	  // Get the raw mouse position
        int rawMouseX = e.getX();
        int rawMouseY = e.getY();

        if (Settings.fullScreen) {
            // Calculate scale factors for fullscreen mode
            double scaleX = (double) gp.frameWidth / gp.screenWidth;
            double scaleY = (double) gp.frameHeight / gp.screenHeight;

            // Adjust mouse position based on scaling
            mouseX = (int) (rawMouseX * scaleX);
            mouseY = (int) (rawMouseY * scaleY);
        } else {
            // Use raw mouse position for windowed mode
            mouseX = rawMouseX;
            mouseY = rawMouseY;
        }
    }
    
    public void mouseMoved(MouseEvent e) { //Updates the mouse position co-ordinates, when the mouse is moved
    	  // Get the raw mouse position
        int rawMouseX = e.getX();
        int rawMouseY = e.getY();

        if (Settings.fullScreen) {
            // Calculate scale factors for fullscreen mode
            double scaleX = (double) gp.frameWidth / gp.screenWidth;
            double scaleY = (double) gp.frameHeight / gp.screenHeight;

            // Adjust mouse position based on scaling
            mouseX = (int) (rawMouseX * scaleX);
            mouseY = (int) (rawMouseY * scaleY);
        } else {
            // Use raw mouse position for windowed mode
            mouseX = rawMouseX;
            mouseY = rawMouseY;
        }
    }
}

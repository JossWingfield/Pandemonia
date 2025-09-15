package main;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Main {
	
	public static JFrame window;

	public static void main(String[] args) { //TEST PUSH
        //Creates the window which the game will be run on
        window = new JFrame();
        //Sets default settings
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Pandemonia");
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Transparent 16 x 16 pixel cursor image.
        //BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        //Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        //window.getContentPane().setCursor(blankCursor);
        
        gp.start();
	}
	
}

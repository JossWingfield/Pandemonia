package entity.items;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;

public class Seasoning extends Food {
	
	public boolean isHerb = false;
	public boolean isSpice = false;
	public boolean isAromaticSpice = false;
	
	protected ArrayList<Colour> particleColours;
	
	public Seasoning(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		foodLayer = 5;
		particleColours = new ArrayList<Colour>();
	}
	
	public ArrayList<Colour> getColours() {
		return particleColours;
	}
}

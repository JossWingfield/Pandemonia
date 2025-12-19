package utility;

import main.renderer.Colour;

public class GUIMessage {

	   String text;
	   int lifetime;
	   int maxLifetime;
	   Colour color;

	   public GUIMessage(String text, int lifetime, Colour color) {
	        this.text = text;
	        this.lifetime = lifetime;
	        this.maxLifetime = lifetime;
	        this.color = color;
	    }
	
}

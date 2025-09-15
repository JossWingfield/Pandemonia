package utility;

import java.awt.Color;

public class GUIMessage {

	   String text;
	   int lifetime;
	   int maxLifetime;
	   Color color;

	   public GUIMessage(String text, int lifetime, Color color) {
	        this.text = text;
	        this.lifetime = lifetime;
	        this.maxLifetime = lifetime;
	        this.color = color;
	    }
	
}

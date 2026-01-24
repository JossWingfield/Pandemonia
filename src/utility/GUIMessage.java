package utility;

import main.renderer.Colour;

public class GUIMessage {

	   String text;
	   int lifetime;
	   int maxLifetime;
	   Colour color;
	   String username = "";

	   public GUIMessage(String text, int lifetime, Colour color) {
	        this.text = text;
	        this.lifetime = lifetime;
	        this.maxLifetime = lifetime;
	        this.color = color;
	    }
	   
	   public GUIMessage(String text, String username) {
	        this.text = text;
	        this.username = username;
	        this.lifetime = 320;
	        this.maxLifetime = 320;
	        this.color = Colour.WHITE;
	    }
	
}

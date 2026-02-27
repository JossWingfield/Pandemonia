package entity.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class SeasoningBlend extends Food {
	
    private List<String> ingredients = new ArrayList<>();
    private List<TextureRegion> ingredientImages = new ArrayList<>();
    
    private int baseX, baseY;
    
	private boolean miniGameStarted = false;
	
	  private TextureRegion foodBorder;
	
	public SeasoningBlend(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Seasoning Blend";
		importImages();
		foodLayer = 5;
		drawWidth = 48;
		drawHeight = 48;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][4];
		
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(0, 0, 16, 16);
		animations[0][0][0] = rawImage;
		foodBorder = importImage("/food/FoodBorder.png").toTextureRegion();
	}
	public List<String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	public void setIngredientImages(List<TextureRegion> ingredientImages) {
		this.ingredientImages = ingredientImages;
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
            // Check for nearby table or plate
            var table = gp.world.buildingM.findTable(
                gp.player.interactHitbox.x, 
                gp.player.interactHitbox.y, 
                gp.player.interactHitbox.width, 
                gp.player.interactHitbox.height
            );

            if (table != null && table.currentItem instanceof Plate plate) {
                // If player presses E to apply seasoning
                if (gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && !miniGameStarted && gp.player.clickCounter == 0) {
                    miniGameStarted = true;
                    gp.world.minigameM.startSeasoningMiniGame(plate, this);
                    gp.player.currentItem = null;
                    gp.player.clickCounter = 0.1;
                }
            }
	}
	public void draw(Renderer renderer) {
		this.baseX = (int) hitbox.x - xDrawOffset ;
		this.baseY = (int) hitbox.y - yDrawOffset ;

        renderer.draw(animations[0][0][0], baseX, baseY, drawWidth, drawHeight);
	        
	    // Draw food layers from bottom to top
	       
        for (int i = 0; i < ingredientImages.size(); i++) {
        	TextureRegion img = ingredientImages.get(i);
            if (img != null) {
            	renderer.draw(img, baseX, baseY, drawWidth, drawHeight);
            }
        }

	 }
	public void drawInHand(Renderer renderer, int x, int y, boolean flip) {
		this.baseX = (int) x;
		this.baseY = (int) y;
		
        renderer.draw(animations[0][0][0], baseX, baseY, drawWidth, drawHeight);
        
        // Draw food layers from bottom to top
        for (int i = 0; i < ingredientImages.size(); i++) {
        	TextureRegion img = ingredientImages.get(i);
            if (img != null) {
                renderer.draw(img, baseX, baseY, drawWidth, drawHeight);
            }
        }
            
    }
    public void drawOverlay(Renderer renderer) {
    	if(gp.player.interactHitbox.intersects(hitbox)) {
	        int spacing  = 32;
	
	        // Count non-null ingredients
	        int count = 0;
	        for (String i : ingredients) {
	            if (i != null) count++;
	        }
	
	        // Total width of all items
	        int totalWidth = count * spacing;
	
	        // Starting X so the row is centered on baseX
	        float startX = (baseX+24) - totalWidth / 2f;
	
	        int index = 0;
	        for (String i : ingredients) {
	            if (i != null) {
	                float x = startX + index * spacing;
	
	                renderer.draw(foodBorder, x - 4, baseY - 24, 32, 32);
	
	                TextureRegion img = gp.world.itemRegistry.getImageFromName(i);
	                Food f = (Food)gp.world.itemRegistry.getItemFromName(i, 0);
	                renderer.draw(img, x-f.xDrawOffset, baseY - 24, 32, 32);
	
	                index++;
	            }
	        }
        }
    }
	
}

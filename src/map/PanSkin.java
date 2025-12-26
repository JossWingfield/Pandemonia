package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class PanSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion smallPan, fryingPan;
	private TextureRegion[][][] fryAnimations, smallPanAnimations;
    public int cost = 0;
    public String name = "Pan";
    public String description = ""; 

	public PanSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 20;
		description = "New Cooking untensils for your restaurant.";
		importImages();
	}
	public void importImages() {
		
		fryAnimations = new TextureRegion[1][1][12];
		smallPanAnimations = new TextureRegion[1][1][12];
		
		switch(preset) {
		case 0:
			importPanAnimations("/decor/CookingPots.png");
			break;
		case 1:
			importPanAnimations("/decor/catalogue/cabin/CabinPots.png");
			break;
		}
		
	}
	private void importPanAnimations(String fileName) {
		
		fryingPan = importImage(fileName).getSubimage(32, 0, 16, 16);
		smallPan = importImage(fileName).getSubimage(16, 0, 16, 16);
		
		fryAnimations[0][0][0] = importImage(fileName).getSubimage(32, 0, 16, 16);
		fryAnimations[0][0][2] = importImage(fileName).getSubimage(16, 32, 16, 16);
		fryAnimations[0][0][3] = importImage(fileName).getSubimage(0, 64, 16, 16);
		fryAnimations[0][0][4] = importImage(fileName).getSubimage(0, 64, 16, 16);
		fryAnimations[0][0][5] = importImage(fileName).getSubimage(16, 64, 16, 16);
		fryAnimations[0][0][6] = importImage(fileName).getSubimage(32, 64, 16, 16);
		fryAnimations[0][0][8] = importImage(fileName).getSubimage(32, 0, 16, 16);
		fryAnimations[0][0][9] = importImage(fileName).getSubimage(16, 48, 16, 16);
		fryAnimations[0][0][10] = importImage(fileName).getSubimage(32, 32 ,16, 16);
		fryAnimations[0][0][11] = importImage(fileName).getSubimage(48, 0 ,16, 16);
		
		smallPanAnimations[0][0][0] = importImage(fileName).getSubimage(16, 0, 16, 16);
		smallPanAnimations[0][0][1] = importImage(fileName).getSubimage(16, 16, 16, 16);
		smallPanAnimations[0][0][2] = importImage(fileName).getSubimage(0, 32, 16, 16);
		smallPanAnimations[0][0][3] = importImage(fileName).getSubimage(16, 0, 16, 16);
		smallPanAnimations[0][0][4] = importImage(fileName).getSubimage(0, 48, 16, 16);
	}
	public TextureRegion getImage() {
		return smallPan;
	}
	public TextureRegion[][][] getFryingPanAnimations() {
		return fryAnimations;
	}
	public TextureRegion[][][] getSmallPanAnimations() {
		return smallPanAnimations;
	}
	 public Texture importImage(String filePath) {
			Texture texture = AssetPool.getTexture(filePath);
		    return texture;
		}
	
}

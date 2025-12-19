package utility;

import java.util.ArrayList;
import java.util.List;

import entity.npc.Customer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class RecipeRenderData {
    public Recipe recipe;
    public Customer customer;

    // Cached text rendering
    public List<String> nameLines = new ArrayList<>();
    public List<Integer> nameLineOffsets = new ArrayList<>();

    // Cached ingredient + state images
    public List<TextureRegion> ingredientImages = new ArrayList<>();
    public List<TextureRegion> cookingStateIcons = new ArrayList<>();
    public List<TextureRegion> secondaryCookingStateIcons = new ArrayList<>();
    
    public int starLevel;

    // Other cached images
    public TextureRegion plateImage;
    public TextureRegion borderImage;
    public TextureRegion mysteryOrderImage;
    public TextureRegion coinImage;
    public TextureRegion faceIcon;

    // Cached cost string
    public String cost;
}

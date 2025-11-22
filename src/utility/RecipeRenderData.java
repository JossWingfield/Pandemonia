package utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.npc.Customer;

public class RecipeRenderData {
    public Recipe recipe;
    public Customer customer;

    // Cached text rendering
    public List<String> nameLines = new ArrayList<>();
    public List<Integer> nameLineOffsets = new ArrayList<>();

    // Cached ingredient + state images
    public List<BufferedImage> ingredientImages = new ArrayList<>();
    public List<BufferedImage> cookingStateIcons = new ArrayList<>();
    public List<BufferedImage> secondaryCookingStateIcons = new ArrayList<>();
    
    public int starLevel;

    // Other cached images
    public BufferedImage plateImage;
    public BufferedImage borderImage;
    public BufferedImage mysteryOrderImage;
    public BufferedImage coinImage;
    public BufferedImage faceIcon;

    // Cached cost string
    public String cost;
}

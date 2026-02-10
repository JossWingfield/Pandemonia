package net.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.items.CookingItem;
import entity.items.Food;
import entity.items.Item;
import entity.items.OvenTray;
import entity.items.Plate;

public class ItemData {

    // ===== BASE =====
    public String itemName;

    // ===== FOOD =====
    public boolean hasFoodData;
    public int foodState;
    public String cookedBy;
    public String secondaryCookedBy;

    // ===== PLATE =====
    public boolean isPlate;
    public List<ItemData> plateIngredients;
    public float seasoningQuality;

    // ===== PAN =====
    public boolean isPan;
    public ItemData cookingFood;
    public double cookProgress;

    // ===== OVEN TRAY =====
    public boolean isOvenTray;
    public List<ItemData> ovenIngredients;

    // ================== READ ==================
    public ItemData(DataInputStream in) throws IOException {

        // Base
        itemName = in.readUTF();

        // Food
        hasFoodData = in.readBoolean();
        if (hasFoodData) {
            foodState = in.readInt();
            cookedBy = in.readUTF();
            secondaryCookedBy = in.readUTF();
        }

        // Plate
        isPlate = in.readBoolean();
        if (isPlate) {
            int count = in.readInt();
            plateIngredients = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                plateIngredients.add(new ItemData(in));
            }
            seasoningQuality = in.readFloat();
        }

        // Pan
        isPan = in.readBoolean();
        if (isPan) {
            cookProgress = in.readDouble();
            boolean hasFood = in.readBoolean();
            if (hasFood) {
                cookingFood = new ItemData(in);
            }
        }

        // Oven Tray
        isOvenTray = in.readBoolean();
        if (isOvenTray) {
            int count = in.readInt();
            ovenIngredients = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                ovenIngredients.add(new ItemData(in));
            }
        }
    }

    // ================== WRITE ==================
    public void write(DataOutputStream out) throws IOException {

        // Base
        out.writeUTF(itemName);

        // Food
        out.writeBoolean(hasFoodData);
        if (hasFoodData) {
            out.writeInt(foodState);
            out.writeUTF(cookedBy != null ? cookedBy : "");
            out.writeUTF(secondaryCookedBy != null ? secondaryCookedBy : "");
        }

        // Plate
        out.writeBoolean(isPlate);
        if (isPlate) {
            out.writeInt(plateIngredients != null ? plateIngredients.size() : 0);
            if (plateIngredients != null) {
                for (ItemData data : plateIngredients) {
                    data.write(out);
                }
            }
            out.writeFloat(seasoningQuality);
        }

        // Pan
        out.writeBoolean(isPan);
        if (isPan) {
            out.writeDouble(cookProgress);
            out.writeBoolean(cookingFood != null);
            if (cookingFood != null) {
                cookingFood.write(out);
            }
        }

        // Oven Tray
        out.writeBoolean(isOvenTray);
        if (isOvenTray) {
            out.writeInt(ovenIngredients != null ? ovenIngredients.size() : 0);
            if (ovenIngredients != null) {
                for (ItemData data : ovenIngredients) {
                    data.write(out);
                }
            }
        }
    }

    // ================== FROM ITEM ==================
    public ItemData(Item item) {
        itemName = item.getName();

        // Food
        if (item instanceof Food food) {
            hasFoodData = true;
            foodState = food.getState();
            cookedBy = food.getCookMethod();
            secondaryCookedBy = food.getSecondaryCookMethod();
        }

        // Plate
        if (item instanceof Plate plate) {
            isPlate = true;
            plateIngredients = new ArrayList<>();
            for (Food food : plate.getFoodItems()) {
                plateIngredients.add(new ItemData(food));
            }
            seasoningQuality = plate.getSeasoningQuality();
        }

        // Pan
        if (item instanceof CookingItem pan) {
            isPan = true;
            cookProgress = pan.getCookTime();
            Food food = pan.cookingItem;
            if (food != null) {
                cookingFood = new ItemData(food);
            }
        }

        // Oven Tray
        if (item instanceof OvenTray tray) {
            isOvenTray = true;
            ovenIngredients = new ArrayList<>();
            for (Food food : tray.getFoodItems()) {
                ovenIngredients.add(new ItemData(food));
            }
        }
    }
}
package utility.recipe;

import entity.npc.Customer;

public class Order {

    private static int NEXT_ID = 0;

    private final int id;              // Unique per order
    private final Recipe recipe;       // Template reference
    private final Customer customer;   // Who ordered it

    private String seasoning;          // Optional
    public boolean isCursed;    // If needed later

    public Order(Recipe recipe, Customer customer) {
        this.id = NEXT_ID++;
        this.recipe = recipe;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setSeasoning(String seasoning) {
        this.seasoning = seasoning;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public boolean hasSeasoning() {
        return seasoning != null;
    }
}

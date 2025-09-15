package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet09PlaceItemOnStove extends Packet {

    private String username, foodName, itemName;
    private int stoveSlot, stoveArrayIndex, foodState, cookTime;

    // Constructor for sending the packet
    public Packet09PlaceItemOnStove(String username, String foodName, String itemName, int stoveSlot, int stoveArrayIndex, int foodState, int cookTime) {
        super(9);
        this.username = username;
        this.foodName = foodName;
        this.itemName = itemName;
        this.stoveSlot = stoveSlot;
        this.stoveArrayIndex = stoveArrayIndex;
        this.foodState = foodState;
        this.cookTime = cookTime;
    }

    // Constructor for receiving the packet
    public Packet09PlaceItemOnStove(byte[] data) {
        super(9);
        String[] dataArray = readData(data).split(",");
        this.foodState = Integer.parseInt(dataArray[0]);
        this.username = dataArray[1];
        this.foodName = dataArray[2];
        this.itemName = dataArray[3];
        this.stoveSlot = Integer.parseInt(dataArray[4]);
        this.stoveArrayIndex = Integer.parseInt(dataArray[5]);
        this.cookTime = Integer.parseInt(dataArray[6]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("09" + this.foodState + "," + this.username + "," + this.foodName + "," + this.itemName + "," + this.stoveSlot + "," + this.stoveArrayIndex + "," + this.cookTime).getBytes();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getStoveSlot() {
        return stoveSlot;
    }

    public int getStoveArrayIndex() {
        return stoveArrayIndex;
    }
    public int getCookTime() {
        return cookTime;
    }

    public int getFoodState() {
        return foodState;
    }
}

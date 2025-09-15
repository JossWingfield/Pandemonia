package net.packets;

import net.GameClient;
import net.GameServer;
import main.GamePanel;
import entity.items.Food;
import entity.buildings.Fridge;

public class Packet17AddItemToFridge extends Packet {

    private String username;
    private int fridgeId;
    private String itemName;
    private int foodState;

    // Constructor from received data
    public Packet17AddItemToFridge(byte[] data) {
        super(17);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.fridgeId = Integer.parseInt(dataArray[1]);
        this.itemName = dataArray[2];
        this.foodState = Integer.parseInt(dataArray[3]);
    }

    // Constructor for sending
    public Packet17AddItemToFridge(String username, int fridgeId, String itemName, int foodState) {
        super(17);
        this.username = username;
        this.fridgeId = fridgeId;
        this.itemName = itemName;
        this.foodState = foodState;
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
        return ("17" + username + "," + fridgeId + "," + itemName + "," + foodState).getBytes();
    }

    // Getters
    public String getUsername() { return username; }
    public int getFridgeId() { return fridgeId; }
    public String getItemName() { return itemName; }
    public int getFoodState() { return foodState; }
}
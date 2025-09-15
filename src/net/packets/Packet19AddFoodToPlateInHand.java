package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet19AddFoodToPlateInHand extends Packet {

    private String username;     // who did the action
    private String foodName;     // name of the food added
    private int foodState;       // cooked / raw / plated etc
    private int tableIndex;

    public Packet19AddFoodToPlateInHand(byte[] data) {
        super(19);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.foodName = dataArray[1];
        this.foodState = Integer.parseInt(dataArray[2]);
        this.tableIndex = Integer.parseInt(dataArray[3]);
    }

    public Packet19AddFoodToPlateInHand(String username, String foodName, int foodState, int tableIndex) {
        super(19);
        this.username = username;
        this.foodName = foodName;
        this.foodState = foodState;
        this.tableIndex = tableIndex;
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
        return ("19" + this.username + "," + this.foodName + "," + this.foodState + "," + this.tableIndex).getBytes();
    }

    public String getUsername() {
        return username;
    }
    public int getTableIndex() {
        return tableIndex;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodState() {
        return foodState;
    }
}

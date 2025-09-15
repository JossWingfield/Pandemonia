package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet20AddFoodToPlateOnTable extends Packet {

    private String username;     // who did the action
    private int tableIndex;         // ID or index of the plate entity
    private String foodName;     // name of the food added
    private int foodState;       // cooked / raw / plated etc

    public Packet20AddFoodToPlateOnTable(byte[] data) {
        super(20);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.tableIndex = Integer.parseInt(dataArray[1]);
        this.foodName = dataArray[2];
        this.foodState = Integer.parseInt(dataArray[3]);
    }

    public Packet20AddFoodToPlateOnTable(String username, int tableIndex, String foodName, int foodState) {
        super(20);
        this.username = username;
        this.tableIndex = tableIndex;
        this.foodName = foodName;
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
        return ("20" + this.username + "," + this.tableIndex + "," + this.foodName + "," + this.foodState).getBytes();
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

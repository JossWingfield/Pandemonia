package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet12AddItemToChoppingBoard extends Packet {

    private String itemName;
    private int boardIndex; // the array index of the chopping board
    private int foodState; // RAW, CHOPPED, etc.

    public Packet12AddItemToChoppingBoard(byte[] data) {
        super(12);
        String[] dataArray = readData(data).split(",");
        this.itemName = dataArray[0];
        this.boardIndex = Integer.parseInt(dataArray[1]);
        this.foodState = Integer.parseInt(dataArray[2]);
    }

    public Packet12AddItemToChoppingBoard(String itemName, int boardIndex, int foodState) {
        super(12);
        this.itemName = itemName;
        this.boardIndex = boardIndex;
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
        return ("12" + itemName + "," + boardIndex + "," + foodState).getBytes();
    }

    // Getters
    public String getItemName() { return itemName; }
    public int getBoardIndex() { return boardIndex; }
    public int getFoodState() { return foodState; }
}

package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet18RemoveItemFromFridge extends Packet {

    private String username;
    private int fridgeId;
    private int itemIndex; // which slot is being removed

    public Packet18RemoveItemFromFridge(byte[] data) {
        super(18);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.fridgeId = Integer.parseInt(dataArray[1]);
        this.itemIndex = Integer.parseInt(dataArray[2]);
    }

    public Packet18RemoveItemFromFridge(String username, int fridgeId, int itemIndex) {
        super(18);
        this.username = username;
        this.fridgeId = fridgeId;
        this.itemIndex = itemIndex;
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
        return ("18" + username + "," + fridgeId + "," + itemIndex).getBytes();
    }

    // Getters
    public String getUsername() { return username; }
    public int getFridgeId() { return fridgeId; }
    public int getItemIndex() { return itemIndex; }
}
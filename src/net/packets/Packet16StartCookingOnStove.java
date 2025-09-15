package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet16StartCookingOnStove extends Packet {

    private String username;
    private int stoveIndex;
    private int slot; // 0 = left, 1 = right
    private String itemName;

    public Packet16StartCookingOnStove(byte[] data) {
        super(16);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.stoveIndex = Integer.parseInt(dataArray[1]);
        this.slot = Integer.parseInt(dataArray[2]);
        this.itemName = dataArray[3];
    }

    public Packet16StartCookingOnStove(String username, int stoveIndex, int slot, String itemName) {
        super(16);
        this.username = username;
        this.stoveIndex = stoveIndex;
        this.slot = slot;
        this.itemName = itemName;
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
        return ("16" + username + "," + stoveIndex + "," + slot + "," + itemName).getBytes();
    }

    // Getters
    public String getUsername() { return username; }
    public int getStoveIndex() { return stoveIndex; }
    public int getSlot() { return slot; }
    public String getItemName() { return itemName; }
}
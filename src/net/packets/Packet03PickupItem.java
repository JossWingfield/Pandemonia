package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet03PickupItem extends Packet {
	
	private String name, username;
	private int foodState;

    public Packet03PickupItem(String name, String username, int foodState) {
        super(3);
        this.name = name;
        this.username = username;
        this.foodState = foodState;
    }

    public Packet03PickupItem(byte[] data) {
        super(3);
        String[] dataArray = readData(data).split(",");
        this.foodState = Integer.parseInt(dataArray[0]);
        this.name = dataArray[1];
        this.username = dataArray[2];
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
        return ("03" + this.foodState + "," + this.name + "," + this.username).getBytes();
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public int getState() {
    	return foodState;
    }
}
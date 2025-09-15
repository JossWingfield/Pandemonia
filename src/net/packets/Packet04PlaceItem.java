package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet04PlaceItem extends Packet {
	
	private String name, username;
	private int foodState, x, y, arrayCounter;

    public Packet04PlaceItem(int x, int y, String name, String username, int foodState, int arrayCounter) {
        super(4);
        this.x = x;
        this.y = y;
        this.name = name;
        this.username = username;
        this.foodState = foodState;
        this.arrayCounter = arrayCounter;
    }

    public Packet04PlaceItem(byte[] data) {
        super(4);
        String[] dataArray = readData(data).split(",");
        this.x = Integer.parseInt(dataArray[0]);
        this.y = Integer.parseInt(dataArray[1]);
        this.foodState = Integer.parseInt(dataArray[2]);
        this.arrayCounter = Integer.parseInt(dataArray[3]);
        this.name = dataArray[4];
        this.username = dataArray[5];
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
        return ("04" + this.x + "," + this.y + "," + this.foodState + "," + this.arrayCounter + "," + this.name + "," + this.username).getBytes();
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
    public int getX() {
    	return x;
    }
    public int getY() {
    	return y;
    }
    public int getArrayCounter() {
    	return arrayCounter;
    }
}
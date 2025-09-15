package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet07PickUpItemFromTable extends Packet {
	
	private String name, username;
	private int foodState, arrayCounter;

    public Packet07PickUpItemFromTable(String name, String username, int foodState, int arrayCounter) {
        super(7);
        this.name = name;
        this.username = username;
        this.foodState = foodState;
        this.arrayCounter = arrayCounter;
    }

    public Packet07PickUpItemFromTable(byte[] data) {
        super(7);
        String[] dataArray = readData(data).split(",");
        this.foodState = Integer.parseInt(dataArray[0]);
        this.arrayCounter = Integer.parseInt(dataArray[1]);
        this.name = dataArray[2];
        this.username = dataArray[3];
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
        return ("07" + this.foodState + "," + this.arrayCounter  + "," + this.name + "," + this.username).getBytes();
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
    public int getArrayCounter() {
    	return arrayCounter;
    }
}

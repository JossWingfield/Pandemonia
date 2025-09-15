package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet10RemoveSinkPlate extends Packet{

    private int arrayIndex;
    private String username;

    public Packet10RemoveSinkPlate(byte[] data) {
        super(10);
        String[] dataArray = readData(data).split(",");
        this.arrayIndex = Integer.parseInt(dataArray[0]);
        this.username = dataArray[1];
    }

    public Packet10RemoveSinkPlate(String username, int arrayIndex) {
        super(10);
        this.arrayIndex = arrayIndex;
        this.username = username;
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
        return ("10" + this.arrayIndex + "," + this.username).getBytes();
    }
    public int getArrayIndex() {
        return arrayIndex;
    }
    public String getUsername() {
    	return username;
    }

}

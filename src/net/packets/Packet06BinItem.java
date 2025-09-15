package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet06BinItem extends Packet {

	private String username;

    public Packet06BinItem(String username) {
        super(6);
        this.username = username;
    }

    public Packet06BinItem(byte[] data) {
        super(6);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
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
        return ("06" + this.username).getBytes();
    }
    public String getUsername() {
        return username;
    }
}

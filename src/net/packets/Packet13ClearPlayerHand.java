package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet13ClearPlayerHand extends Packet {

    private String username;

    // Constructor to parse incoming data
    public Packet13ClearPlayerHand(byte[] data) {
        super(13);
        this.username = readData(data); // data is just the username
    }

    // Constructor to create packet to send
    public Packet13ClearPlayerHand(String username) {
        super(13);
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
        return ("13" + this.username).getBytes();
    }

    // Getter
    public String getUsername() {
        return username;
    }
}
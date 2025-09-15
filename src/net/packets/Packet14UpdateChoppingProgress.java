package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet14UpdateChoppingProgress extends Packet {

    private String username;
    private int boardIndex;
    private int chopCount;

    // Constructor from received data
    public Packet14UpdateChoppingProgress(byte[] data) {
        super(14);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.boardIndex = Integer.parseInt(dataArray[1]);
        this.chopCount = Integer.parseInt(dataArray[2]);
    }

    // Constructor for sending
    public Packet14UpdateChoppingProgress(String username, int boardIndex, int chopCount) {
        super(14);
        this.username = username;
        this.boardIndex = boardIndex;
        this.chopCount = chopCount;
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
        return ("14" + username + "," + boardIndex + "," + chopCount).getBytes();
    }

    // Getters
    public String getUsername() { return username; }
    public int getBoardIndex() { return boardIndex; }
    public int getChopCount() { return chopCount; }
}
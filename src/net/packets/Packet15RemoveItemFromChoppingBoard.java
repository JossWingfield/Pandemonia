package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet15RemoveItemFromChoppingBoard extends Packet {

    private int boardIndex;

    public Packet15RemoveItemFromChoppingBoard(byte[] data) {
        super(15);
        this.boardIndex = Integer.parseInt(readData(data));
    }

    public Packet15RemoveItemFromChoppingBoard(int boardIndex) {
        super(15);
        this.boardIndex = boardIndex;
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
        return ("15" + boardIndex).getBytes();
    }

    public int getBoardIndex() { return boardIndex; }
}

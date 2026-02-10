package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import entity.items.Item;
import net.ConnectionState;
import net.data.ItemData;

public class Packet14Chop extends Packet {

    private String username;
    private int tableIndex, chopCount;

    // Client → Server
    public Packet14Chop(String username, int tableIndex, int chopCount) {
        super(PacketType.CHOP);
        this.username = username;
        this.tableIndex = tableIndex;
        this.chopCount = chopCount;
    }

    // Server / Client receive
    public Packet14Chop(DataInputStream in) throws IOException {
        super(PacketType.CHOP);
        this.username = in.readUTF();
        this.tableIndex = in.readInt();
        this.chopCount = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(tableIndex);
        out.writeInt(chopCount);
    }

    public String getUsername() {
        return username;
    }

    public int getTableIndex() {
        return tableIndex;
    }
    public int getChopCount() {
		return chopCount;
	}

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }
}
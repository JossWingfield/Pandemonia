package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import entity.items.Item;
import net.ConnectionState;
import net.data.ItemData;

public class Packet13PickUpFromChoppingBoard extends Packet {

    private String username;
    private int tableIndex;
    private ItemData itemData;

    // Client → Server
    public Packet13PickUpFromChoppingBoard(String username, int tableIndex, Item item) {
        super(PacketType.PICK_UP_FROM_CHOPPING_BOARD);
        this.username = username;
        this.tableIndex = tableIndex;
        this.itemData = new ItemData(item);
    }

    // Server / Client receive
    public Packet13PickUpFromChoppingBoard(DataInputStream in) throws IOException {
        super(PacketType.PICK_UP_FROM_CHOPPING_BOARD);
        this.username = in.readUTF();
        this.tableIndex = in.readInt();
        this.itemData = new ItemData(in);
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(tableIndex);
        itemData.write(out);
    }

    public String getUsername() {
        return username;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public ItemData getItemData() {
        return itemData;
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }
}
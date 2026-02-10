package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import entity.items.Item;
import net.ConnectionState;
import net.data.ItemData;

public class Packet11PickUpFromTable extends Packet {

    private String username;
    private int tableIndex;
    private ItemData itemData;

    // Client → Server
    public Packet11PickUpFromTable(String username, int tableIndex, Item item) {
        super(PacketType.PICK_UP_FROM_TABLE);
        this.username = username;
        this.tableIndex = tableIndex;
        this.itemData = new ItemData(item);
    }

    // Server / Client receive
    public Packet11PickUpFromTable(DataInputStream in) throws IOException {
        super(PacketType.PICK_UP_FROM_TABLE);
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
package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet09PickFridgeItem extends Packet {

    private String username, itemName;
    private int foodState;

    // Client → Server
    public Packet09PickFridgeItem(String username, String itemName, int foodState) {
        super(PacketType.PICK_FRIDGE_ITEM);
        this.username = username;
        this.itemName = itemName;
        this.foodState = foodState;
    }

    // Server / Client receive
    public Packet09PickFridgeItem(DataInputStream in) throws IOException {
        super(PacketType.PICK_FRIDGE_ITEM);
        this.username = in.readUTF();
        this.itemName = in.readUTF();
        this.foodState = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    	out.writeUTF(username);
        out.writeUTF(itemName);
        out.writeInt(foodState);
    }

    public String getItemName() {
        return itemName;
    }
    public String getUsername() {
    	return username;
    }
    public int getFoodState() {
        return foodState;
    }

	@Override
	public ConnectionState requiredState() {
		return ConnectionState.IN_GAME;
	}
}
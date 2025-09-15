package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet05ChangeRoom extends Packet {
	
	   private String username;
	   private int roomIndex;

	   public Packet05ChangeRoom(byte[] data) {
	       super(05);
	       String[] dataArray = readData(data).split(",");
	       this.username = dataArray[0];
	       this.roomIndex = Integer.parseInt(dataArray[1]);
	   }

	    public Packet05ChangeRoom(String username, int roomIndex) {
	        super(05);
	        this.username = username;
	        this.roomIndex = roomIndex;
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
	        return ("05" + this.username+","+this.roomIndex).getBytes();
	    }
	    public String getUsername() {
	        return username;
	    }

	    public int getRoomIndex() {
	        return this.roomIndex;
	    }

}

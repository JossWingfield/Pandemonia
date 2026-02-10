package net.packets;

public enum PacketType {
    LOGIN(0),
    DISCONNECT(1),
    MOVE(2),
    SNAPSHOT(3),
    CHAT(4),
    LOGIN_ACK(5),
    PING(6),
    SERVERSHUTDOWN(7),
    SPAWN_INFO(8),
    PICK_FRIDGE_ITEM(9),
    PLACE_ON_TABLE(10),
    PICK_UP_FROM_TABLE(11),
    PLACE_ON_CHOPPING_BOARD(12),
    PICK_UP_FROM_CHOPPING_BOARD(13),
    CHOP(14);

	 private final int id;

	 PacketType(int id) {
		 this.id = id;
	 }

	    public int getId() {
	        return id;
	    }

	    public static PacketType fromId(int id) {
	        for (PacketType t : values()) {
	            if (t.id == id) return t;
	        }
	        throw new IllegalArgumentException("Unknown packet id: " + id);
	    }
	}


package net.packets;

public enum PacketType {
    LOGIN(0),
    DISCONNECT(1),
    MOVE(2),
    SNAPSHOT(3);

    public final int id;

    PacketType(int id) {
        this.id = id;
    }

    public static PacketType fromId(int id) {
        for (PacketType t : values()) {
            if (t.id == id) return t;
        }
        return null;
    }
}


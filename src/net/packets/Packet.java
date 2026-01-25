package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public abstract class Packet {

    protected final PacketType type;

    protected Packet(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }

    /** Which connection state is required to receive this packet */
    public abstract ConnectionState requiredState();

    /** Write packet payload (NOT the type) */
    public abstract void write(DataOutputStream out) throws IOException;

}
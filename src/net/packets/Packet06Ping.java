package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet06Ping extends Packet {

    // Sent by client
    public Packet06Ping() {
        super(PacketType.PING);
    }

    // Received by server
    public Packet06Ping(DataInputStream in) throws IOException {
        this();
        // no payload to read
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        // no payload to write
    }
}

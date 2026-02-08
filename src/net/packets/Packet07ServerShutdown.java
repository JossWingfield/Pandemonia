package net.packets;

import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet07ServerShutdown extends Packet {

    public Packet07ServerShutdown() {
        super(PacketType.SERVERSHUTDOWN);
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME; // or CONNECTED if you prefer
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        // No payload
    }
}
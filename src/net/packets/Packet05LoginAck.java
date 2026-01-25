package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet05LoginAck extends Packet {

    public Packet05LoginAck() {
        super(PacketType.LOGIN_ACK);
    }

    public Packet05LoginAck(DataInputStream in) throws IOException {
        super(PacketType.LOGIN_ACK);
        // no payload
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.LOGGING_IN;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        // nothing to write
    }
}

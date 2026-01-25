package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet04Chat extends Packet {

    private final String username;
    private final String message;

    // Send
    public Packet04Chat(String username, String message) {
        super(PacketType.CHAT);
        this.username = username;
        this.message = message;
    }

    // Receive
    public Packet04Chat(DataInputStream in) throws IOException {
        super(PacketType.CHAT);
        this.username = in.readUTF();
        this.message = in.readUTF();
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(message);
    }

    public String getUsername() { return username; }
    public String getMessage() { return message; }
}

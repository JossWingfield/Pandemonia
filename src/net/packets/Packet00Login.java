package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet00Login extends Packet {

    private final String username;
    private final int x, y;

    // Client → Server constructor
    public Packet00Login(String username, int x, int y) {
        super(PacketType.LOGIN);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    // Server receives
    public Packet00Login(DataInputStream in) throws IOException {
        super(PacketType.LOGIN);
        this.username = in.readUTF();
        this.x = in.readInt();
        this.y = in.readInt();
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.CONNECTING;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(x);
        out.writeInt(y);
    }

    public String getUsername() { return username; }
    public int getX() { return x; }
    public int getY() { return y; }
}
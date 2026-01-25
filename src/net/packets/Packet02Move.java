package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet02Move extends Packet {

    private final String username;
    private final int x, y;
    private final int direction;
    private final int currentAnimation;
    private final int currentRoomIndex;

    // Client → Server
    public Packet02Move(
            String username,
            int x,
            int y,
            int direction,
            int currentAnimation,
            int currentRoomIndex
    ) {
        super(PacketType.MOVE);
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.currentAnimation = currentAnimation;
        this.currentRoomIndex = currentRoomIndex;
    }

    // Server receives
    public Packet02Move(DataInputStream in) throws IOException {
        super(PacketType.MOVE);
        this.username = in.readUTF();
        this.x = in.readInt();
        this.y = in.readInt();
        this.direction = in.readInt();
        this.currentAnimation = in.readInt();
        this.currentRoomIndex = in.readInt();
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(direction);
        out.writeInt(currentAnimation);
        out.writeInt(currentRoomIndex);
    }

    public String getUsername() { return username; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDirection() { return direction; }
    public int getCurrentAnimation() { return currentAnimation; }
    public int getCurrentRoomIndex() { return currentRoomIndex; }
}

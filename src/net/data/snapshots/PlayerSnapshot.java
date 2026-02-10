package net.data.snapshots;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerSnapshot {

    public final String username;
    public final int x, y;
    public final int direction;
    public final int animation;
    public final int roomIndex;

    public PlayerSnapshot(
            String username,
            int x,
            int y,
            int direction,
            int animation,
            int roomIndex
    ) {
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.animation = animation;
        this.roomIndex = roomIndex;
    }

    public PlayerSnapshot(DataInputStream in) throws IOException {
        this.username = in.readUTF();
        this.x = in.readInt();
        this.y = in.readInt();
        this.direction = in.readInt();
        this.animation = in.readInt();
        this.roomIndex = in.readInt();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(direction);
        out.writeInt(animation);
        out.writeInt(roomIndex);
    }
}

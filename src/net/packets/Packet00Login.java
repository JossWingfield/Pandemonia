package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet00Login extends Packet {

    private final String username;
    private final int x, y, roomNum;
    private final int skinId;
    private final int hairId;
    private final int hairColour;

        // Client → Server constructor
        public Packet00Login(String username, int x, int y, int roomNum, int skinId, int hairId, int hairColour) {
            super(PacketType.LOGIN);
            this.username = username;
            this.x = x;
            this.y = y;
            this.roomNum = roomNum;
            this.skinId = skinId;
            this.hairId = hairId;
            this.hairColour = hairColour;
        }

        // Server receives
        public Packet00Login(DataInputStream in) throws IOException {
            super(PacketType.LOGIN);
            this.username = in.readUTF();
            this.x = in.readInt();
            this.y = in.readInt();
            this.roomNum = in.readInt();
            this.skinId = in.readInt();
            this.hairId = in.readInt();
            this.hairColour = in.readInt();
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
            out.writeInt(roomNum);
            out.writeInt(skinId);
            out.writeInt(hairId);
            out.writeInt(hairColour);
        }

        public String getUsername() { return username; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getHairColour() {
			return hairColour;
		}
        public int getSkinId() {
			return skinId;
		}
        public int getRoomNum() {
			return roomNum;
		}
        public int getHairId() {
			return hairId;
		}
}
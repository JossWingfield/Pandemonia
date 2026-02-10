package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet08SpawnInfo extends Packet {

    private final String username;

    // Player spawn
    private final int x;
    private final int y;

    // Appearance
    private final int skinId;
    private final int hairId;
    private final int hairColour;

    // World
    private final int timeOfDay;     // 0–23999 style or whatever you use
    private final int weatherType;   // enum ordinal (CLEAR, RAIN, STORM)

    // Server → Client constructor
    public Packet08SpawnInfo(
            String username,
            int x, int y,
            int skinId,
            int hairId,
            int hairColour,
            int timeOfDay,
            int weatherType
    ) {
        super(PacketType.SPAWN_INFO);
        this.username = username;
        this.x = x;
        this.y = y;
        this.skinId = skinId;
        this.hairId = hairId;
        this.hairColour = hairColour;
        this.timeOfDay = timeOfDay;
        this.weatherType = weatherType;
    }

    // Client-side read constructor
    public Packet08SpawnInfo(DataInputStream in) throws IOException {
        super(PacketType.SPAWN_INFO);
        this.username = in.readUTF();
        this.x = in.readInt();
        this.y = in.readInt();
        this.skinId = in.readInt();
        this.hairId = in.readInt();
        this.hairColour = in.readInt();
        this.timeOfDay = in.readInt();
        this.weatherType = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(skinId);
        out.writeInt(hairId);
        out.writeInt(hairColour);
        out.writeInt(timeOfDay);
        out.writeInt(weatherType);
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.LOGGING_IN;
    }

    public int getHairColour() {
		return hairColour;
	}
    public int getY() {
		return y;
	}
    public int getX() {
		return x;
	}
    public int getHairId() {
		return hairId;
	}
    public int getSkinId() {
		return skinId;
	}
    public int getTimeOfDay() {
		return timeOfDay;
	}
    public String getUsername() {
		return username;
	}
    public int getWeatherType() {
		return weatherType;
	}

    // Getters ↓
}
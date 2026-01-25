package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ConnectionState;
import net.snapshots.PlayerSnapshot;

public class Packet03Snapshot extends Packet {

    private final List<PlayerSnapshot> players;

    // Server → Client
    public Packet03Snapshot(List<PlayerSnapshot> players) {
        super(PacketType.SNAPSHOT);
        this.players = players;
    }

    // Client receives
    public Packet03Snapshot(DataInputStream in) throws IOException {
        super(PacketType.SNAPSHOT);

        int count = in.readInt();
        players = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            players.add(new PlayerSnapshot(in));
        }
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(players.size());
        for (PlayerSnapshot ps : players) {
            ps.write(out);
        }
    }

    public List<PlayerSnapshot> getPlayers() {
        return players;
    }
}

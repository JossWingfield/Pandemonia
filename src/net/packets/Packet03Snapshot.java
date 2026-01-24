package net.packets;

import java.io.Serializable;
import java.util.List;

import net.snapshots.PlayerSnapshot;

public class Packet03Snapshot extends Packet implements Serializable {

	private static final long serialVersionUID = 1L;

    private List<PlayerSnapshot> players;

    public Packet03Snapshot(List<PlayerSnapshot> players) {
        super(PacketType.SNAPSHOT);
        this.players = players;
    }

    public List<PlayerSnapshot> getPlayers() {
        return players;
    }
}

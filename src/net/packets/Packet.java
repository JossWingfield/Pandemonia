package net.packets;

import java.io.Serializable;

import net.ClientHandler;
import net.GameClient;
import net.GameServer;

public abstract class Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    protected PacketType type;

    public Packet(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }
    /* -------- TCP helpers (object streams) -------- */

    public final void send(GameClient client) {
        client.send(this); // send the Packet object directly
    }

    public final void send(GameServer server) {
        server.broadcast(this);
    }

    public final void sendExcept(GameServer server, ClientHandler excluded) {
        server.sendToAllExcept(this, excluded);
    }
}
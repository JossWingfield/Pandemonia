package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import entity.PlayerMP;
import main.GamePanel;
import net.packets.Packet;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import net.packets.Packet02Move;

public class ClientHandler extends Thread {

    private final GamePanel gp;
    private final GameServer server;
    private final Socket socket;
    private PlayerMP player;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean running = true;

    public ClientHandler(GamePanel gp, GameServer server, Socket socket) {
        this.gp = gp;
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            while (running) {
                Object obj = in.readObject();
                if (obj instanceof Packet packet) {
                    handlePacket(packet);
                }
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    private void handlePacket(Packet packet) {
        switch (packet.getType()) {
            case LOGIN -> {
            	server.handleLogin((Packet00Login) packet, this);
            }
            case DISCONNECT -> {
            	server.handleDisconnect((Packet01Disconnect) packet, this);
            }
            case MOVE -> {
                Packet02Move move = (Packet02Move) packet;

                // Update server-authoritative position of the player that sent the packet
                PlayerMP p = getPlayer(); // might be null for host client
                if (p != null) {
                    p.hitbox.x = move.getX();
                    p.hitbox.y = move.getY();
                    player.setDirection(move.getDirection());
                    player.setCurrentAnimation(move.getCurrentAnimation());
                    player.setCurrentRoomIndex(move.getCurrentRoomIndex());
                }

                // Broadcast move to everyone else
                server.sendToAllExcept(move, this);
            }
        }
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            shutdown();
        }
    }

    public void setPlayer(PlayerMP player) {
        this.player = player;
        player.setOwner(this);
    }

    public PlayerMP getPlayer() {
        return player;
    }

    public void shutdown() {
        if (!running) return; // 🔒 prevent double shutdown
        running = false;

        if (player != null) {
            server.handleDisconnect(
                new Packet01Disconnect(player.getUsername()),
                this
            );
        }

        try {
            socket.close();
        } catch (IOException ignored) {}

        server.removeClient(this);
    }
}

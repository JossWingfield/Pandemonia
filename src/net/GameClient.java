package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Colour;
import net.packets.Packet;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import net.packets.Packet02Move;
import net.packets.Packet03Snapshot;
import net.packets.Packet04Chat;
import net.snapshots.PlayerSnapshot;

public class GameClient extends Thread {

    private final GamePanel gp;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public GameClient(GamePanel gp, String ip, int port) {
        this.gp = gp;
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Packet packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Object obj = in.readObject();
                if (obj instanceof Packet packet) {
                    handlePacket(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePacket(Packet packet) {
        switch (packet.getType()) {
            case LOGIN -> {
                Packet00Login p = (Packet00Login) packet;

                // Ignore self
                if (gp.player != null && p.getUsername().equals(gp.player.getUsername())) return;

                // Already exists
                for (PlayerMP player : gp.playerList) {
                    if (player.getUsername().equals(p.getUsername())) return;
                }

                // Add remote player
                PlayerMP newPlayer = new PlayerMP(gp, p.getX(), p.getY(), p.getUsername());
                gp.playerList.add(newPlayer);
                
                gp.gui.addMessage(
                        p.getUsername() + " has joined the game.",
                        Colour.WHITE
                );
            }
            case DISCONNECT -> {
                Packet01Disconnect p = (Packet01Disconnect) packet;
                
                // Remove the player from local list
                PlayerMP toRemove = null;
                for (PlayerMP player : gp.playerList) {
                    if (player.getUsername().equals(p.getUsername())) {
                        toRemove = player;
                        break;
                    }
                }
                if (toRemove != null) {
                	gp.playerList.remove(toRemove);
                    gp.gui.addMessage(
                            p.getUsername() + " has left the game.",
                            Colour.WHITE
                    );
                }
            } case MOVE -> {
                Packet02Move p = (Packet02Move) packet;

                for (PlayerMP player : gp.playerList) {
                    if (player.getUsername().equals(p.getUsername())) {
                        player.hitbox.x = p.getX();
                        player.hitbox.y = p.getY();
                        player.setDirection(p.getDirection());
                        player.setCurrentAnimation(p.getCurrentAnimation());
                        player.setCurrentRoomIndex(p.getCurrentRoomIndex());
                        break;
                    }
                }
            }
            case SNAPSHOT -> {
                Packet03Snapshot snap = (Packet03Snapshot) packet;

                for (PlayerSnapshot ps : snap.getPlayers()) {

                    // Skip local player
                    if (ps.username.equals(gp.player.getUsername())) continue;

                    PlayerMP player = gp.findPlayer(ps.username);
                    if (player == null) continue;

                    // Hard correction OR soft correction
                    player.hitbox.x = ps.x;
                    player.hitbox.y = ps.y;
                    player.setDirection(ps.direction);
                    player.currentAnimation = ps.animation;
                }
            }
            case CHAT -> {
            	Packet04Chat chatPacket = (Packet04Chat)packet;
            	gp.gui.addMessageFromPacket(chatPacket.getUsername(), chatPacket.getMessage());
            }

        }
    }
}
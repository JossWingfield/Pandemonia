package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Colour;
import net.packets.Packet;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import net.packets.Packet03Snapshot;
import net.packets.Packet05LoginAck;
import net.packets.Packet07ServerShutdown;
import net.packets.Packet08SpawnInfo;
import net.snapshots.PlayerSnapshot;

public class GameServer extends Thread {

    public static final int GAME_PORT = 1331;

    private final GamePanel gp;
    private ServerSocket serverSocket;
    private boolean running = true;

    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private DiscoveryManager discoveryManager;
    long lastSnapshot = 0;

    public GameServer(GamePanel gp) throws IOException {
        this.gp = gp;
        this.serverSocket = new ServerSocket(GAME_PORT);

        discoveryManager = new DiscoveryManager(
                true,
                gp.player != null ? gp.player.getUsername() : "Host",
                gp.world.progressM.worldName,
                GAME_PORT
        );
        discoveryManager.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(gp, this, socket);
                clients.add(handler);
                handler.start();
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastSnapshot > 1000) {
            sendSnapshot();
            lastSnapshot = now;
        }
        
        // ⏱ Timeout detection
        for (ClientHandler client : clients) {
            if (now - client.getLastHeardTime() > 6000) { // 6 seconds
            	/*
            	boolean isHost = client.getName().equals(gp.player.getUsername());
            	if(isHost) {
            		gp.serverHost = false;
				    gp.socketServer.shutdown();
            	}
            	*/
                client.shutdown();
            }
        }
    }


    public void sendToAll(Packet packet) {
        for (ClientHandler client : clients) {
            if (!client.isAlive()) continue;
            client.send(packet);
        }
    }

    public void sendToAllExcept(Packet packet, ClientHandler excludedHandler) {
        for (ClientHandler client : clients) {
            if (client == excludedHandler) continue;
            client.send(packet);
        }
    }

    public void broadcast(Packet packet) {
        for (ClientHandler client : clients) client.send(packet);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void shutdown() {
        // Tell all clients the host is gone
        Packet07ServerShutdown packet = new Packet07ServerShutdown();
        sendToAll(packet);

        // Close all client connections
        for (ClientHandler client : clients) {
            client.shutdown();
        }
        running = false;

        try {
            serverSocket.close();
        } catch (IOException ignored) {}
        if (discoveryManager != null) discoveryManager.shutdown();
    }

    public void handleLogin(Packet00Login packet, ClientHandler senderHandler) {
    	for (PlayerMP p : gp.playerList) {
    	    if (p.getUsername().equals(packet.getUsername())) {
    	        senderHandler.setPlayer(p);
    	        senderHandler.send(new Packet05LoginAck());
    	        senderHandler.setState(ConnectionState.IN_GAME);
    	        return;
    	    }
    	}
    	
        senderHandler.setState(ConnectionState.LOGGING_IN);
        

        boolean isHost = packet.getUsername().equals(gp.player.getUsername());

        // Add new remote player
        if (!isHost) {
            PlayerMP newPlayer = new PlayerMP(
                    gp,
                    packet.getX(),
                    packet.getY(),
                    packet.getUsername()
            );
            newPlayer.currentRoomIndex = packet.getRoomNum();
            newPlayer.setSkin(packet.getSkinId());
            newPlayer.setHairStyle(packet.getHairId());
            newPlayer.setHair(packet.getHairColour());
            gp.playerList.add(newPlayer);

            if (senderHandler != null)
                senderHandler.setPlayer(newPlayer);

            // 🔔 HOST GUI MESSAGE
            gp.gui.addMessage(
                    packet.getUsername() + " has joined the game.",
                    Colour.WHITE
            );
        } else {
            senderHandler.setPlayer((PlayerMP) gp.player);
        }

        // Send existing players to the new client
        if (senderHandler != null) {
            for (PlayerMP p : gp.playerList) {
                if (p.getUsername().equals(packet.getUsername())) continue;
                Packet00Login existingPlayer = new Packet00Login(
                        p.getUsername(), 
                        (int) p.hitbox.x, 
                        (int) p.hitbox.y,
                        p.currentRoomIndex,
                        p.getSkinColour(),
                        p.getHairStyle(),
                        p.getHairColour()
                );
                senderHandler.send(existingPlayer);
            }
        }

        // Broadcast new player to everyone else
        if (!isHost) {
            for (ClientHandler c : clients) {
                if (c == senderHandler) continue;
                c.send(new Packet00Login(
                    packet.getUsername(),
                    packet.getX(),
                    packet.getY(),
                    packet.getRoomNum(),
                    packet.getSkinId(),
                    packet.getHairId(),
                    packet.getHairColour()
                ));
            }
        }
        
        PlayerMP p = senderHandler.getPlayer();

        Packet08SpawnInfo spawn = new Packet08SpawnInfo(
            packet.getUsername(),
            (int)p.hitbox.x,
            (int)p.hitbox.y,
            p.getSkinColour(),
            p.getHairStyle(),
            p.getHairColour(),
            gp.world.gameM.getTime(),
            gp.world.gameM.currentWeather.ordinal()
        );

        if (senderHandler.getPlayer().getUsername().equals(packet.getUsername())) {
            senderHandler.send(spawn);
        }

        senderHandler.send(new Packet05LoginAck());
        senderHandler.setState(ConnectionState.IN_GAME);
    }

    public void sendSnapshot() {
        for (ClientHandler client : clients) {
            if (client.getConnectionState() != ConnectionState.IN_GAME) continue;

            List<PlayerSnapshot> snaps = new ArrayList<>();
            for (PlayerMP p : gp.playerList) {
                if (p.isChangingRoom) continue;

                // IMPORTANT: Skip sending this player's own snapshot to their client
                if (client.getPlayer() == p) continue;

                snaps.add(new PlayerSnapshot(
                    p.getUsername(),
                    (int)p.hitbox.x,
                    (int)p.hitbox.y,
                    p.getDirection(),
                    p.currentAnimation,
                    p.currentRoomIndex
                ));
            }

            client.send(new Packet03Snapshot(snaps));
        }
    }


    public void sendSnapshotForPlayer(PlayerMP targetPlayer) {
        List<PlayerSnapshot> snaps = new ArrayList<>();
        
        for (PlayerMP p : gp.playerList) {
            if (p.isChangingRoom) continue;
            if (p == targetPlayer) continue; // Don't send own snapshot
            snaps.add(new PlayerSnapshot(
                p.getUsername(),
                (int)p.hitbox.x,
                (int)p.hitbox.y,
                p.getDirection(),
                p.currentAnimation,
                p.currentRoomIndex
            ));
        }

        for (ClientHandler client : clients) {
            if (client.getConnectionState() != ConnectionState.IN_GAME) continue;
            client.send(new Packet03Snapshot(snaps));
        }
    }

    public void handleDisconnect(Packet01Disconnect packet, ClientHandler sender) {
        PlayerMP removed = sender.getPlayer();
        if (removed == null) return;

        gp.playerList.remove(removed);

        gp.gui.addMessage(
                packet.getUsername() + " has left the game.",
                Colour.WHITE
        );

        for (ClientHandler c : clients) {
            if (c == sender) continue;
            c.send(packet);
        }
    }
}
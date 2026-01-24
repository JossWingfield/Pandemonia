package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Colour;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;

public class GameServer extends Thread {

    public static final int GAME_PORT = 1331;

    private final GamePanel gp;
    private ServerSocket serverSocket;
    private boolean running = true;

    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    
    private DiscoveryManager discoveryManager;

    public GameServer(GamePanel gp) throws IOException {
        this.gp = gp;
        this.serverSocket = new ServerSocket(GAME_PORT);
        System.out.println("TCP Server started on port " + GAME_PORT);
        
        discoveryManager = new DiscoveryManager(
                true, // server
                gp.player != null ? gp.player.getUsername() : "Host",
                "World1",
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

    public void sendToAll(Object obj) {
        for (ClientHandler client : clients) client.send(obj);
    }

    public void sendToAllExcept(Object obj, ClientHandler excludedHandler) {
        for (ClientHandler client : clients) {
            if (client == excludedHandler) continue;
            client.send(obj);
        }
    }

    public void broadcast(Object obj) {
        for (ClientHandler client : clients) client.send(obj);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void shutdown() {
        running = false;
        try {
            for (ClientHandler c : clients) c.shutdown();
            serverSocket.close();
        } catch (IOException ignored) {}
        System.out.println("TCP Server shut down.");
        if (discoveryManager != null) discoveryManager.shutdown();
    }
    
    public void handleLogin(Packet00Login packet, ClientHandler senderHandler) {

        boolean isHost = packet.getUsername().equals(gp.player.getUsername());

        // Add new remote player
        if (!isHost) {
            PlayerMP newPlayer = new PlayerMP(
                    gp,
                    packet.getX(),
                    packet.getY(),
                    packet.getUsername()
            );
            gp.playerList.add(newPlayer);

            if (senderHandler != null)
                senderHandler.setPlayer(newPlayer);

            // 🔔 HOST GUI MESSAGE
            gp.gui.addMessage(
                    packet.getUsername() + " has joined the game.",
                    Colour.WHITE
            );
        }

        // Send existing players to the new client
        if (senderHandler != null) {
            for (PlayerMP p : gp.playerList) {
                if (p.getUsername().equals(packet.getUsername())) continue;

                // 👇 Send directly through the handler
                Packet00Login existingPlayer = new Packet00Login(p.getUsername(), (int)p.hitbox.x, (int)p.hitbox.y);
                senderHandler.send(existingPlayer);
            }
        }

        // Broadcast new player to everyone else
        for (ClientHandler c : clients) {
            if (c == senderHandler) continue;
            c.send(new Packet00Login(
                    packet.getUsername(),
                    packet.getX(),
                    packet.getY()
            ));
        }
    }
    public void handleDisconnect(Packet01Disconnect packet, ClientHandler sender) {

        PlayerMP removed = sender.getPlayer();
        if (removed == null) return;

        // Remove player
        gp.playerList.remove(removed);

        // 🔔 HOST MESSAGE (ONCE)
        gp.gui.addMessage(
            packet.getUsername() + " has left the game.",
            Colour.WHITE
        );

        // Notify other clients (NOT sender)
        for (ClientHandler c : clients) {
            if (c == sender) continue;
            c.send(packet);
        }
    }

}
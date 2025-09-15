package net;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DiscoveryManager extends Thread {

    private static final int DISCOVERY_PORT = 1332;
    private static final int BROADCAST_INTERVAL_MS = 1000;

    private volatile boolean running = true;
    private final boolean isServer;
    private DatagramSocket socket;

    private final List<DiscoveredServer> discoveredServers = new ArrayList<>();

    private final String serverUsername;
    private final int serverGamePort;
    private final String worldName;

    /**
     * Client mode: isServer = false
     * Server mode: isServer = true (broadcasts every second)
     */
    public DiscoveryManager(boolean isServer, String serverUsername, String worldName, int serverGamePort) {
        this.isServer = isServer;
        this.serverUsername = serverUsername;
        this.worldName = worldName;
        this.serverGamePort = serverGamePort;

        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.setBroadcast(true);

            // Both server and client listen on the same DISCOVERY_PORT
            socket.bind(new InetSocketAddress(DISCOVERY_PORT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (isServer) {
            new Thread(this::broadcastLoop, "Discovery-Broadcast").start();
        }
        listenLoop();
    }

    private void broadcastLoop() {
        while (running) {
            try {
                String message = "GAME_DISCOVERY|" + serverUsername + "|" + worldName + "|" + serverGamePort;
                byte[] data = message.getBytes();

                // Broadcast to all interfaces
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface ni = interfaces.nextElement();
                    if (!ni.isUp() || ni.isLoopback()) continue;

                    for (InterfaceAddress addr : ni.getInterfaceAddresses()) {
                        InetAddress broadcast = addr.getBroadcast();
                        if (broadcast != null) {
                            DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, DISCOVERY_PORT);
                            socket.send(packet);
                        }
                    }
                }

                // Global broadcast
                DatagramPacket global = new DatagramPacket(data, data.length,
                        InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT);
                socket.send(global);

                // Explicit loopback (for same-machine clients)
                DatagramPacket loopback = new DatagramPacket(data, data.length,
                        InetAddress.getByName("127.0.0.1"), DISCOVERY_PORT);
                socket.send(loopback);

                Thread.sleep(BROADCAST_INTERVAL_MS);

            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private void listenLoop() {
        while (running) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength()).trim();
                if (msg.startsWith("GAME_DISCOVERY|")) {
                    String[] parts = msg.split("\\|");
                    if (parts.length >= 4) {
                        String hostUser = parts[1];
                        String world = parts[2];
                        int port = Integer.parseInt(parts[3]);
                        String ip = packet.getAddress().getHostAddress();

                        // Deduplicate by ip+port
                        boolean exists = discoveredServers.stream()
                                .anyMatch(s -> s.ip.equals(ip) && s.port == port);
                        if (!exists) {
                            discoveredServers.add(new DiscoveredServer(ip, port, hostUser, world));
                            //System.out.println("Discovered server: " + hostUser + " - " + world + " @ " + ip + ":" + port);
                        }
                    }
                }
            } catch (SocketException se) {
                if (running) se.printStackTrace();
            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

    public synchronized List<DiscoveredServer> getDiscoveredServers() {
        return new ArrayList<>(discoveredServers);
    }

    public void shutdown() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }

    public static class DiscoveredServer {
        public final String ip;
        public final int port;
        public final String hostUser;
        public final String worldName;

        public DiscoveredServer(String ip, int port, String hostUser, String worldName) {
            this.ip = ip;
            this.port = port;
            this.hostUser = hostUser;
            this.worldName = worldName;
        }
    }
}
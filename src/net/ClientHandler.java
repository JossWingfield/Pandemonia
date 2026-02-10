package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import entity.PlayerMP;
import main.GamePanel;
import net.packets.Packet;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import net.packets.Packet02Move;
import net.packets.Packet03Snapshot;
import net.packets.Packet04Chat;
import net.packets.Packet05LoginAck;
import net.packets.Packet06Ping;
import net.packets.Packet07ServerShutdown;
import net.packets.Packet08SpawnInfo;
import net.packets.Packet09PickFridgeItem;
import net.packets.Packet10PlaceOnTable;
import net.packets.Packet11PickUpFromTable;
import net.packets.Packet12PlaceOnChoppingBoard;
import net.packets.Packet13PickUpFromChoppingBoard;
import net.packets.Packet14Chop;
import net.packets.PacketType;

public class ClientHandler extends Thread {

	    private final GamePanel gp;
	    private final GameServer server;
	    private final Socket socket;
	    private PlayerMP player;

	    private DataInputStream in;
	    private DataOutputStream out;
	    private volatile boolean running = true;

	    private final BlockingQueue<Packet> sendQueue = new LinkedBlockingQueue<>();
	    private Thread writerThread;
	    
	    ConnectionState state = ConnectionState.CONNECTING;

	    private volatile long lastHeardTime = System.currentTimeMillis();

	    public ClientHandler(GamePanel gp, GameServer server, Socket socket) {
	        this.gp = gp;
	        this.server = server;
	        this.socket = socket;
	    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startWriter();

            while (running) {
                int typeCode = in.readInt();
                PacketType type = PacketType.values()[typeCode];
                Packet packet = switch (type) {
                    case LOGIN -> new Packet00Login(in);
                    case DISCONNECT -> new Packet01Disconnect(in);
                    case MOVE -> new Packet02Move(in);
                    case SNAPSHOT -> new Packet03Snapshot(in);
                    case CHAT -> new Packet04Chat(in);
                    case LOGIN_ACK -> new Packet05LoginAck();
                    case PING -> new Packet06Ping(in);
                    case SERVERSHUTDOWN -> new Packet07ServerShutdown();
                    case SPAWN_INFO -> new Packet08SpawnInfo(in);
                    case PICK_FRIDGE_ITEM -> new Packet09PickFridgeItem(in);
                    case PLACE_ON_TABLE -> new Packet10PlaceOnTable(in);
                    case PICK_UP_FROM_TABLE-> new Packet11PickUpFromTable(in);
                    case PLACE_ON_CHOPPING_BOARD-> new Packet12PlaceOnChoppingBoard(in);
                    case PICK_UP_FROM_CHOPPING_BOARD-> new Packet13PickUpFromChoppingBoard(in);
                    case CHOP-> new Packet14Chop(in);
                };
                handlePacket(packet);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    private void handlePacket(Packet packet) {
    	
        lastHeardTime = System.currentTimeMillis();
    	
    	 if (packet.requiredState() != null &&
    		        packet.requiredState() != state) {
    		        return;
    	 }
        switch (packet.getType()) {
            case LOGIN -> {
            	server.handleLogin((Packet00Login) packet, this);
            }
            case DISCONNECT -> {
            	server.handleDisconnect((Packet01Disconnect) packet, this);
                shutdown(); 
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
                    
                    // Broadcast move to everyone else
                    server.sendToAllExcept(move, this);
                }
                
            }
            case CHAT -> {
            	 Packet04Chat chatPacket = (Packet04Chat)packet;
            	 // Relay to all clients
                 server.sendToAllExcept(chatPacket, this);
            }
            case PING -> {
                // nothing to do, just counts as "I'm alive"
            }
            case PICK_FRIDGE_ITEM -> {
                Packet09PickFridgeItem p = (Packet09PickFridgeItem)packet;
                server.handlePickFridgeItem(p, this);
            }
            case PLACE_ON_TABLE -> {
            	Packet10PlaceOnTable p = (Packet10PlaceOnTable)packet;
                server.handlePlaceOnTable(p, this);
            }
            case PICK_UP_FROM_TABLE -> {
            	Packet11PickUpFromTable p = (Packet11PickUpFromTable)packet;
                server.handlePickUpFromTable(p, this);
            }
            case PLACE_ON_CHOPPING_BOARD -> {
            	Packet12PlaceOnChoppingBoard p = (Packet12PlaceOnChoppingBoard)packet;
                server.handlePlaceOnChoppingBoard(p, this);
            }
            case PICK_UP_FROM_CHOPPING_BOARD -> {
            	Packet13PickUpFromChoppingBoard p = (Packet13PickUpFromChoppingBoard)packet;
                server.handlePickUpFromChoppingBoard(p, this);
            }
            case CHOP -> {
            	Packet14Chop p = (Packet14Chop)packet;
                server.handleChop(p, this);
            }
        }
    }
    public long getLastHeardTime() {
        return lastHeardTime;
    }
    private void startWriter() {
        writerThread = new Thread(() -> {
            try {
                while (running && !socket.isClosed()) {
                    Packet packet = sendQueue.take();
                    out.writeInt(packet.getType().ordinal());
                    packet.write(out);
                    out.flush();
                }
            } catch (Exception e) {
                shutdown();
            }
        }, "ClientHandler-Writer");
        writerThread.start();
    }

    public void send(Packet packet) {
        if (!running || socket.isClosed()) return;
        sendQueue.offer(packet);
    }

    public ConnectionState getConnectionState() {
        return state;
    }
    public void setState(ConnectionState state) {
        this.state = state;
    }
    public void setPlayer(PlayerMP player) {
        this.player = player;
        player.setOwner(this);
    }

    public PlayerMP getPlayer() {
        return player;
    }

    public void shutdown() {
        if (!running) return;
        running = false;

        if (player != null) {
            server.handleDisconnect(
                new Packet01Disconnect(player.getUsername()),
                this
            );
        }

        try { socket.close(); } catch (IOException ignored) {}
        server.removeClient(this);
    }
}
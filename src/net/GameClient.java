package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import entity.Player;
import entity.PlayerMP;
import entity.buildings.ChoppingBoard;
import entity.buildings.FloorDecor_Building;
import entity.items.Food;
import entity.items.Item;
import main.GamePanel;
import main.renderer.Colour;
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
import net.snapshots.PlayerSnapshot;

public class GameClient extends Thread {

    private final GamePanel gp;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ConnectionState state = ConnectionState.CONNECTING;
    private final BlockingQueue<Packet> sendQueue = new LinkedBlockingQueue<>();
    private Thread writerThread;
    private volatile boolean running = true;
    
    private long lastPingSent = 0;

    public GameClient(GamePanel gp, String ip, int port) {
        this.gp = gp;
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startWriter() {
        writerThread = new Thread(() -> {
            try {
                while (running && !socket.isClosed()) {
                    Packet packet = sendQueue.take();
                    out.writeInt(packet.getType().ordinal()); // write type first
                    packet.write(out);                        // write packet data
                    out.flush();
                }
            } catch (Exception e) {
                shutdown();
            }
        }, "GameClient-Writer");
        writerThread.start();
    }

    public void send(Packet packet) {
        if (socket.isClosed()) return;
        sendQueue.offer(packet);
    }

    @Override
    public void run() {
        try {
            while (running && !socket.isClosed()) {
                int typeCode = in.readInt(); // blocks
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
                    case PLACE_ON_CHOPPING_BOARD -> new Packet12PlaceOnChoppingBoard(in);
                    case PICK_UP_FROM_CHOPPING_BOARD -> new Packet13PickUpFromChoppingBoard(in);
                    case CHOP -> new Packet14Chop(in);
                };

                handlePacket(packet);
            }
        }
        catch (EOFException | SocketException e) {
            // Normal disconnect (server crashed or closed)
            handleRemoteDisconnect();
        }
        catch (Exception e) {
            // Unexpected bug
            e.printStackTrace();
            handleRemoteDisconnect();
        }
    }
    public void flush() throws IOException {
        out.flush();
    }
    public void close() throws IOException {
    	out.close();
    }
    private void handleRemoteDisconnect() {
        if (!running) return;

        running = false;

        gp.gui.addMessage(
            "Disconnected from server.",
            Colour.WHITE
        );

        shutdown();
    }
    public void sendImmediately(Packet packet) {
        try {
            out.writeInt(packet.getType().ordinal());
            packet.write(out);
            out.flush();
        } catch (IOException ignored) {
        }
    }
    public void shutdown() {
        running = false;
        try { socket.close(); } catch (IOException ignored) {}
    }
    public void update() {
        if (state != ConnectionState.IN_GAME) return;

        long now = System.currentTimeMillis();
        if (now - lastPingSent > 2000) {
            send(new Packet06Ping());
            lastPingSent = now;
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
                newPlayer.currentRoomIndex = p.getRoomNum();
                newPlayer.setSkin(p.getSkinId());
                newPlayer.setHairStyle(p.getHairId());
                newPlayer.setHair(p.getHairColour());
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
                    player.currentRoomIndex = ps.roomIndex;
                }
            }
            case CHAT -> {
            	Packet04Chat chatPacket = (Packet04Chat)packet;
            	gp.gui.addMessageFromPacket(chatPacket.getUsername(), chatPacket.getMessage());
            }
            case LOGIN_ACK -> {
                state = ConnectionState.IN_GAME;

            }
            case SERVERSHUTDOWN -> {
                shutdown();                // close socket
                gp.multiplayer = false;
                gp.currentState = gp.titleState;
            }
            case SPAWN_INFO -> {
                Packet08SpawnInfo p = (Packet08SpawnInfo) packet;   

                if(!p.getUsername().equals(gp.player.getUsername())) break;
                gp.player.hitbox.x = p.getX();
                gp.player.hitbox.y = p.getY();

                gp.player.setSkin(p.getSkinId());
                gp.player.setHairStyle(p.getHairId());
                gp.player.setHair(p.getHairColour());

                // Sync world
                gp.world.gameM.setTime(p.getTimeOfDay());
                gp.world.gameM.setWeather(p.getWeatherType());
            }
            case PICK_FRIDGE_ITEM -> {
                Packet09PickFridgeItem p = (Packet09PickFridgeItem) packet;

                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                targetPlayer.resetAnimation(4);
                targetPlayer.currentItem =
                    gp.world.itemRegistry.getItemFromName(
                        p.getItemName(),
                        p.getFoodState()
                    );
            }
            case PLACE_ON_TABLE -> {
            	Packet10PlaceOnTable p = (Packet10PlaceOnTable)packet;

                // Place item on table
                FloorDecor_Building table = (FloorDecor_Building)gp.world.buildingM.getBuilding(p.getTableIndex());
                if (table != null) {
                    Item item = gp.world.itemRegistry.getItemFromItemData(p.getItemData());
                    table.currentItem = item;
                }

                
                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                targetPlayer.currentItem = null;
            }
            case PICK_UP_FROM_TABLE -> {
            	Packet11PickUpFromTable p = (Packet11PickUpFromTable)packet;

                
                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                // Place item on table
                FloorDecor_Building table = (FloorDecor_Building)gp.world.buildingM.getBuilding(p.getTableIndex());
                if (table != null) {
                    table.currentItem = null;
                    Item item = gp.world.itemRegistry.getItemFromItemData(p.getItemData());
                    targetPlayer.currentItem = item;
                    targetPlayer.resetAnimation(2);
                }

            }
            case PLACE_ON_CHOPPING_BOARD -> {
            	Packet12PlaceOnChoppingBoard p = (Packet12PlaceOnChoppingBoard)packet;

                // Place item on table
                ChoppingBoard cb = (ChoppingBoard)gp.world.buildingM.getBuilding(p.getTableIndex());
                if (cb != null) {
                    Food item = (Food)gp.world.itemRegistry.getItemFromItemData(p.getItemData());
                    cb.addItem(item);
                }

                
                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                targetPlayer.currentItem = null;
            }
            case PICK_UP_FROM_CHOPPING_BOARD -> {
            	Packet13PickUpFromChoppingBoard p = (Packet13PickUpFromChoppingBoard)packet;

                
                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                // Place item on table
                ChoppingBoard cb = (ChoppingBoard)gp.world.buildingM.getBuilding(p.getTableIndex());
                if (cb != null) {
                	cb.removeItem();
                    Item item = gp.world.itemRegistry.getItemFromItemData(p.getItemData());
                    targetPlayer.currentItem = item;
                    targetPlayer.resetAnimation(2);
                }

            }
            case CHOP -> {
            	Packet14Chop p = (Packet14Chop)packet;
            	
                Player targetPlayer = gp.getPlayer(p.getUsername());
                if (targetPlayer == null) return;

                // Place item on table
                ChoppingBoard cb = (ChoppingBoard)gp.world.buildingM.getBuilding(p.getTableIndex());
                cb.setChopCount(p.getChopCount()+1);
            }

        }
    }
}
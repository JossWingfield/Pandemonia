package net;

import main.GamePanel;
import net.packets.Packet;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import net.packets.Packet02Move;
import net.packets.Packet03PickupItem;
import net.packets.Packet04PlaceItem;
import net.packets.Packet05ChangeRoom;
import net.packets.Packet06BinItem;
import net.packets.Packet07PickUpItemFromTable;
import net.packets.Packet08PickUpFromStove;
import net.packets.Packet09PlaceItemOnStove;
import net.packets.Packet10RemoveSinkPlate;
import net.packets.Packet11SpawnInfo;
import net.packets.Packet12AddItemToChoppingBoard;
import net.packets.Packet13ClearPlayerHand;
import net.packets.Packet14UpdateChoppingProgress;
import net.packets.Packet15RemoveItemFromChoppingBoard;
import net.packets.Packet16StartCookingOnStove;
import net.packets.Packet17AddItemToFridge;
import net.packets.Packet18RemoveItemFromFridge;
import net.packets.Packet19AddFoodToPlateInHand;
import net.packets.Packet20AddFoodToPlateOnTable;
import net.packets.Packet21PickupPlate;
import net.packets.Packet22PlacePlate;

import java.awt.Color;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import entity.PlayerMP;
import entity.buildings.Building;
import entity.buildings.ChoppingBoard;
import entity.buildings.FloorDecor_Building;
import entity.buildings.Fridge;
import entity.buildings.Sink;
import entity.buildings.Stove;
import entity.items.Food;
import entity.items.FryingPan;
import entity.items.Item;
import entity.items.Plate;
import entity.items.SmallPan;


public class GameServer extends Thread{

	private final DatagramSocket socket;
    private final GamePanel gp;
    private final List<PlayerMP> connectedPlayers = new ArrayList<>();
    private volatile boolean running = true;

    private DiscoveryManager discoveryManager; // replaces old broadcaster

    public static final int GAME_PORT = 1331;

    public GameServer(GamePanel gp) throws SocketException {
        this.gp = gp;
        this.socket = new DatagramSocket(GAME_PORT);

        // Start DiscoveryManager for server
        discoveryManager = new DiscoveryManager(
                true, // this is the server
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
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
        if (discoveryManager != null) discoveryManager.shutdown();
        System.out.println("GameServer shut down.");
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        
        // Ensure the message is not empty and that we have enough data
        if (message.length() < 2) {
            System.out.println("Received an invalid packet with insufficient data.");
            return;
        }

        // Look up the packet type based on the first two bytes of the message
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                gp.gui.addMessage(((Packet00Login) packet).getUsername() + " has joined the game...", Color.WHITE);

                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has connected...");
                PlayerMP player = new PlayerMP(gp, 200, 200, ((Packet00Login)packet).getUsername(), address, port);
                this.addConnection(player, (Packet00Login)packet);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);

                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left...");
                this.removeConnection((Packet01Disconnect)packet);
                break;
            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove((Packet02Move)packet);
                break;
            case PICKUPITEM:
                packet = new Packet03PickupItem(data);
                this.handlePickupItem((Packet03PickupItem)packet);
                break;
            case PLACEITEM:
            	packet = new Packet04PlaceItem(data);
                this.handlePlaceItem((Packet04PlaceItem)packet);
            	break;
            case CHANGEROOM:
            	packet = new Packet05ChangeRoom(data);
                handleChangeRoom((Packet05ChangeRoom)packet);
            	break;
            case BINITEM:
                packet = new Packet06BinItem(data);
                this.handleBinItem((Packet06BinItem)packet);
                break;
            case PICKUPFROMTABLE:
                packet = new Packet07PickUpItemFromTable(data);
                this.handlePickupItemFromTable((Packet07PickUpItemFromTable)packet);
                break;
            case REMOVESINKPLATE:
            	packet = new Packet10RemoveSinkPlate(data);
                this.handleRemoveSinkPlate((Packet10RemoveSinkPlate)packet);
                break;
            case ADDTOCHOPPINGBOARD:
                packet = new Packet12AddItemToChoppingBoard(data);
                handleAddItemToChoppingBoard((Packet12AddItemToChoppingBoard) packet, address, port);
                break;
            case CLEARPLAYERHAND:
                packet = new Packet13ClearPlayerHand(data);
                handleClearPlayerHand((Packet13ClearPlayerHand) packet);
                break;
            case UPDATECHOPPINGPROGRESS:
                packet = new Packet14UpdateChoppingProgress(data);
                handleUpdateChoppingProgress((Packet14UpdateChoppingProgress) packet);
                break;
            case REMOVEITEMFROMCHOPPINGBOARD:
                packet = new Packet15RemoveItemFromChoppingBoard(data);
                handleRemoveItemFromChoppingBoard((Packet15RemoveItemFromChoppingBoard) packet);
                break;
            case STARTCOOKINGONSTOVE:
                packet = new Packet16StartCookingOnStove(data);
                handleStartCookingOnStove((Packet16StartCookingOnStove) packet);
                break;
            case ADDTOFRIDGE:
                packet = new Packet17AddItemToFridge(data);
                handleAddItemToFridge((Packet17AddItemToFridge) packet);
                break;
            case REMOVEFROMFRIDGE:
                packet = new Packet18RemoveItemFromFridge(data);
                handleRemoveItemFromFridge((Packet18RemoveItemFromFridge) packet);
                break;
            case ADDFOODTOPLATEINHAND:
                packet = new Packet19AddFoodToPlateInHand(data);
                handleAddFoodToPlateInHand((Packet19AddFoodToPlateInHand) packet);
                break;
            case ADDFOODTOPLATEONTABLE:
                packet = new Packet20AddFoodToPlateOnTable(data);
                handleAddFoodToPlateOnTable((Packet20AddFoodToPlateOnTable) packet);
                break;
            case PICKUPFROMSTOVE:
            	 packet = new Packet08PickUpFromStove(data);
            	 handlePickupFromStove((Packet08PickUpFromStove) packet);
            	break;
            case PLACEONSTOVE:
           	 	packet = new Packet09PlaceItemOnStove(data);
           	 	handlePlaceonStove((Packet09PlaceItemOnStove) packet);
           	 	break;
            case PICKUPPLATE:
           	 	packet = new Packet21PickupPlate(data);
           	 	handlePickupPlate((Packet21PickupPlate) packet);
           	 	break;
            case PLACEPLATE:
           	 	packet = new Packet22PlacePlate(data);
           	 	handlePlacePlate((Packet22PlacePlate) packet);
           	 	break;
        }
    }
    public void handlePlacePlate(Packet22PlacePlate packet) {
   	   PlayerMP player = getPlayerMP(packet.getUsername());
          if(player == null) return;
          
          // Broadcast to all clients
          sendDataToAllClients(packet.getData());
      }
    public void handlePickupPlate(Packet21PickupPlate packet) {
  	   PlayerMP player = getPlayerMP(packet.getUsername());
         if(player == null) return;
         
         // Broadcast to all clients
         sendDataToAllClients(packet.getData());
     }
    public void handlePlaceonStove(Packet09PlaceItemOnStove packet) {
 	   PlayerMP player = getPlayerMP(packet.getUsername());
        if(player == null) return;
        
        // Broadcast to all clients
        sendDataToAllClients(packet.getData());
    }
    public void handlePickupFromStove(Packet08PickUpFromStove packet) {
    	   PlayerMP player = getPlayerMP(packet.getUsername());
           if(player == null) return;
           
           // Broadcast to all clients
           sendDataToAllClients(packet.getData());
    }
    private void handleAddFoodToPlateInHand(Packet19AddFoodToPlateInHand p) {
        // Find the player who did it
        PlayerMP player = getPlayerMP(p.getUsername());
        if(player == null) return;
        
        // Broadcast to all clients
        sendDataToAllClients(p.getData());
    }

    private void handleAddFoodToPlateOnTable(Packet20AddFoodToPlateOnTable p) {
        PlayerMP player = getPlayerMP(p.getUsername());
        if(player == null) return;

        // Broadcast to all clients
        sendDataToAllClients(p.getData());
    }
    private void handleRemoveItemFromFridge(Packet18RemoveItemFromFridge packet) {
        PlayerMP player = getPlayerMP(packet.getUsername());
        if (player == null) return;

        // Broadcast to all clients (host included)
        packet.writeData(this);
    }
    private void handleAddItemToFridge(Packet17AddItemToFridge packet) {
        PlayerMP player = getPlayerMP(packet.getUsername());
        if (player == null) return;

        // Only clear the player hand on the server side
        player.currentItem = null;

        // Broadcast to all clients (including host)
        packet.writeData(this);
    }
    private void handleStartCookingOnStove(Packet16StartCookingOnStove packet) {
        // Find the player who sent this packet
        PlayerMP player = getPlayerMP(packet.getUsername());
        if (player == null) return;

        // Get the stove
        Building building = gp.buildingM.getBuilding(packet.getStoveIndex());
        if (!(building instanceof Stove stove)) return;

        // Determine which slot
        Item slotItem = (packet.getSlot() == 0) ? stove.leftSlot : stove.rightSlot;

        // Only allow cooking if the slot is a pan and empty
        if (slotItem instanceof SmallPan pan && pan.cookingItem == null && player.currentItem != null) {
            pan.setCooking(player.currentItem);
            player.currentItem = null;
        } else if (slotItem instanceof FryingPan fpan && fpan.cookingItem == null && player.currentItem != null) {
            fpan.setCooking(player.currentItem);
            player.currentItem = null;
        }

        // Broadcast to all other clients
        sendDataToOtherClients(player, packet.getData());
    }
    private void handleClearPlayerHand(Packet13ClearPlayerHand packet) {
        PlayerMP player = getPlayerMP(packet.getUsername());
        if (player != null) {
            player.currentItem = null;

            // Broadcast to all clients so they clear the hand
            packet.writeData(this);
        }
    }
    public PlayerMP getPlayerMP(InetAddress address, int port) {
        for (PlayerMP p : connectedPlayers) {
            if (p.ipAddress.equals(address) && p.port == port) {
                return p;
            }
        }
        return null;
    }
    private void handleRemoveItemFromChoppingBoard(Packet15RemoveItemFromChoppingBoard packet) {
        Building building = gp.buildingM.getBuilding(packet.getBoardIndex());
        if (!(building instanceof ChoppingBoard)) return;

        ChoppingBoard board = (ChoppingBoard) building;
        board.clearCurrentItem();

        // Broadcast to all clients
        packet.writeData(this);
    }
    private void handleUpdateChoppingProgress(Packet14UpdateChoppingProgress packet) {
        Building building = gp.buildingM.getBuilding(packet.getBoardIndex());
        if (!(building instanceof ChoppingBoard)) return;

        ChoppingBoard board = (ChoppingBoard) building;
        board.setChopCount(packet.getChopCount());

        // Broadcast to everyone else (excluding sender)
        PlayerMP sender = getPlayerMP(packet.getUsername());
        if (sender != null) {
            sendDataToOtherClients(sender, packet.getData());
        }
    }
    private void handleAddItemToChoppingBoard(Packet12AddItemToChoppingBoard packet, InetAddress address, int port) {
        // Find the player based on the source IP/port
        PlayerMP player = getPlayerMP(address, port);
        if (player == null) return;

        // Get the chopping board
        Building building = gp.buildingM.getBuildings()[packet.getBoardIndex()];
        if (!(building instanceof ChoppingBoard)) return;

        ChoppingBoard board = (ChoppingBoard) building;

        // Create the food item from the registry
        Item item = gp.itemRegistry.getItemFromName(packet.getItemName(), packet.getFoodState());
        if (item instanceof Food) {
            board.setCurrentItem((Food) item);
            player.currentItem = null;

            // Broadcast to all clients
            packet.writeData(this);
        }
    }
    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;

        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                // Update IP/port if needed
                if (p.ipAddress == null) p.ipAddress = player.ipAddress;
                if (p.port == -1) p.port = player.port;
                alreadyConnected = true;
            } else {
                // Tell existing players about the new one
                Packet00Login newPlayerPacket =
                        new Packet00Login(player.getUsername(), (int) player.hitbox.x, (int) player.hitbox.y);
                sendData(newPlayerPacket.getData(), p.ipAddress, p.port);

                // Tell the new player about existing players
                Packet00Login existingPlayerPacket =
                        new Packet00Login(p.getUsername(), (int) p.hitbox.x, (int) p.hitbox.y);
                sendData(existingPlayerPacket.getData(), player.ipAddress, player.port);
            }
        }

        if (!alreadyConnected) {
            this.connectedPlayers.add(player);

            // Broadcast the new player to everyone
            sendDataToAllClients(packet.getData());

            // Only send world state if we know their IP + port
            if (player.ipAddress != null && player.port > 0) {
                Packet11SpawnInfo spawnPacket = new Packet11SpawnInfo(
                    gp.world.getCurrentSeason(),
                    gp.world.getDay(),
                    gp.world.getRawTime()
                );
                sendData(spawnPacket.getData(), player.ipAddress, player.port);
            } else {
                //System.out.println("⚠ Tried to send SpawnInfo but player IP/port not set yet: " + player.getUsername());
            }
        }
    }
    public void removeConnection(Packet01Disconnect packet) {
    	int index = getPlayerMPIndex(packet.getUsername());
		if (index == -1) return; // not found, ignore
        this.connectedPlayers.remove(index);
        packet.writeData(this);
    }
    private void handleMove(Packet02Move packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
        	int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            player.hitbox.x = packet.getX();
            player.hitbox.y = packet.getY();
            player.setCurrentAnimation(packet.getCurrentAnimation());
            player.setDirection(packet.getDirection());
            packet.writeData(this);
        }
    }
    private void handleChangeRoom(Packet05ChangeRoom packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
        	int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            player.currentRoomIndex = packet.getRoomIndex();
            packet.writeData(this);
        }
    }
    private void handlePickupItem(Packet03PickupItem packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
        	int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            player.currentItem = gp.itemRegistry.getItemFromName(packet.getName(), packet.getState());
            packet.writeData(this);
        }
    }
    private void handlePickupItemFromTable(Packet07PickUpItemFromTable packet) {
    	if(getPlayerMP(packet.getUsername()) != null) {
    		PlayerMP player = getPlayerMP(packet.getUsername()); 
            Item item = gp.buildingM.getBuildingItem(packet.getArrayCounter());

            if (item != null) {
                // Clear the slot on the table
                gp.buildingM.setBuildingItem(packet.getArrayCounter(), null);


                // Give to player
                player.currentItem = item;
            }

            // Broadcast so other clients update
            packet.writeData(this);
        }
    }
    private void handleBinItem(Packet06BinItem packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
        	int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            player.currentItem = null;
            packet.writeData(this);
        }
    }
    private void handlePlaceItem(Packet04PlaceItem packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
        	int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            if (player != null) {
                Item item = player.currentItem;

                if (item != null) {
                    // Position it where the packet says
                    item.hitbox.x = packet.getX();
                    item.hitbox.y = packet.getY();

                    // Place into table
                    gp.buildingM.setBuildingItem(packet.getArrayCounter(), item);

                    // Clear player hand
                    player.currentItem = null;
                }

                // Broadcast placement
                packet.writeData(this);
            }
        }
    }
    private void handleRemoveSinkPlate(Packet10RemoveSinkPlate packet) {
    	if(getPlayerMP(packet.getUsername()) != null) {
    		int index = getPlayerMPIndex(packet.getUsername());
    		if (index == -1) return; // not found, ignore
    		PlayerMP player = this.connectedPlayers.get(index);
            for(Building b: gp.buildingM.getBuildings()) {
            	if(b != null) {
	            	if(b.getName().equals("Kitchen Sink 1")) {
	            		Sink sink = (Sink)b;
	            		if(!player.getUsername().equals(packet.getUsername())) {
	            			sink.decreasePlateCount();
	            		}
	            	}
            	}
            }
            packet.writeData(this);
        }
    }
    public void sendDataToPlayer(byte[] data, PlayerMP player) {
        DatagramPacket packet = new DatagramPacket(data, data.length, player.ipAddress, player.port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PlayerMP getPlayerMP(String username) {
        for(PlayerMP player : this.connectedPlayers) {
            if(username.equals(player.getUsername())) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerMPIndex(String username) {
        for (int i = 0; i < connectedPlayers.size(); i++) {
            if (username.equals(connectedPlayers.get(i).getUsername())) {
                return i;
            }
        }
        return -1; // not found
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        try {
            socket.send(new DatagramPacket(data, data.length, ipAddress, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeAllPlayers() {
        // Create a copy of the list to safely iterate
        List<PlayerMP> playersCopy = new ArrayList<>(connectedPlayers);

        for (PlayerMP player : playersCopy) {
            if (player != null) {
                // Send a disconnect packet to this player
                Packet01Disconnect disconnectPacket = new Packet01Disconnect(player.getUsername());
                disconnectPacket.writeData(this);

                // Remove from the list
                connectedPlayers.remove(player);
            }
        }

        // At this point, the list should be empty
        connectedPlayers.clear();
        
        System.out.println("All players have been removed from the server.");
    }
    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            if (p.ipAddress == null || p.port < 0) {
                continue; // skip invalid connections
            }
            sendData(data, p.ipAddress, p.port);
        }
    }
    public void sendDataToOtherClients(PlayerMP player, byte[] data) {
        for (PlayerMP p : connectedPlayers) {
        	if(p != player)  {
        		sendData(data, p.ipAddress, p.port);
        	}
        }
    }

}

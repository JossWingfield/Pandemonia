package net;

import main.GamePanel;
import net.packets.*;

import java.awt.Color;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import entity.PlayerMP;
import entity.buildings.Building;
import entity.buildings.ChoppingBoard;
import entity.buildings.FloorDecor_Building;
import entity.buildings.Fridge;
import entity.buildings.Sink;
import entity.buildings.Stove;
import entity.items.CookingItem;
import entity.items.Food;
import entity.items.FoodState;
import entity.items.FryingPan;
import entity.items.Item;
import entity.items.Plate;
import entity.items.SmallPan;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private int port;
    private GamePanel gp;

    public GameClient(GamePanel gp, String ip, int port) {
    	 this.gp = gp;
         this.port = port;
         try {
             this.ipAddress = InetAddress.getByName(ip);
             socket = new DatagramSocket(); // UDP socket for sending/receiving
             socket.setReuseAddress(true);
             socket.setBroadcast(true);
             socket.setSoTimeout(0);       // no timeout
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = new byte[1024]; // buffer
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet); // wait for incoming UDP packet
                parsePacket(Arrays.copyOf(packet.getData(), packet.getLength()), packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
    	
    	 if (data == null || data.length < 2) {
    	        System.out.println("Received empty or invalid data from: " + address.getHostAddress());
    	        return;  // Skip processing if data is invalid
    	    }
    	 
        String message = new String(data).trim();
        if (message.length() < 2) {
	        System.out.println("Received empty or invalid data from: " + address.getHostAddress());
	        return;  // Skip processing if data is invalid
	    }
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                //gp.gui.addMessage(((Packet00Login) packet).getUsername() + " has joined the game...", Color.WHITE);
                handleLogin((Packet00Login) packet, address, port);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                Packet01Disconnect p = (Packet01Disconnect)packet;
                if(p.getUsername().equals(gp.player.getUsername())) {
                	gp.currentState = gp.titleState;
                }
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left the game...");
                gp.gui.addMessage(((Packet01Disconnect) packet).getUsername() + " has left the game...", Color.YELLOW);
                gp.removePlayerMP(((Packet01Disconnect) packet).getUsername());
                break;
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move) packet);
                break;
            case PICKUPITEM:
                packet = new Packet03PickupItem(data);
                handlePickupItem((Packet03PickupItem) packet);
                break;
            case PLACEITEM:
                packet = new Packet04PlaceItem(data);
                handlePlaceItem((Packet04PlaceItem) packet);
                break;
            case CHANGEROOM:
            	packet = new Packet05ChangeRoom(data);
                handleChangeRoom((Packet05ChangeRoom)packet);
            	break;
            case BINITEM:
                packet = new Packet06BinItem(data);
                handleBinItem((Packet06BinItem) packet);
                break;
            case PICKUPFROMTABLE:
                packet = new Packet07PickUpItemFromTable(data);
                handlePickupItemFromTable((Packet07PickUpItemFromTable) packet);
                break;
            case REMOVESINKPLATE:
                packet = new Packet10RemoveSinkPlate(data);
                handleRemoveSinkPlate((Packet10RemoveSinkPlate) packet);
                break;
            case SPAWNINFO:
                packet = new Packet11SpawnInfo(data);
                handleSpawnInfo((Packet11SpawnInfo) packet);
                break;
            case ADDTOCHOPPINGBOARD:
                packet = new Packet12AddItemToChoppingBoard(data);
                handleAddItemToChoppingBoard((Packet12AddItemToChoppingBoard) packet);
                break;
            case CLEARPLAYERHAND:
                packet = new Packet13ClearPlayerHand(data);
                handleClearPlayerHand((Packet13ClearPlayerHand) packet);
                break;
            case UPDATECHOPPINGPROGRESS:
            	packet = new Packet14UpdateChoppingProgress(data);
                handleUpdateChoppingProgress((Packet14UpdateChoppingProgress)packet);
                break;
            case REMOVEITEMFROMCHOPPINGBOARD:
            	packet = new Packet15RemoveItemFromChoppingBoard(data);
            	handleRemoveItemFromChoppingBoard((Packet15RemoveItemFromChoppingBoard)packet);
                break;
            case STARTCOOKINGONSTOVE:
                packet = new Packet16StartCookingOnStove(data);
                handleStartCookingOnStove((Packet16StartCookingOnStove)packet);
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
           	 	handlePlaceOnStove((Packet09PlaceItemOnStove) packet);
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
    private void handlePlacePlate(Packet22PlacePlate packet) {
	    // Ignore local player
	    if (packet.getUsername().equals(gp.player.getUsername())) return;
	
	    int index = gp.getPlayerMPIndex(packet.getUsername());
	    if (index == -1) return;
	
	    PlayerMP player = gp.getPlayerList().get(index);
	
	    // Create a Plate for this player
	    Plate plate = new Plate(gp, 0, 0);
	    plate.clearIngredients();
	    plate.setDirty(packet.isDirty());
	    for (String ing : packet.getIngredients()) {
	        if (ing != null && !ing.isEmpty()) {
	            Food food = (Food) gp.itemRegistry.getItemFromName(ing, 0); // adjust state if needed
	            food.foodState = FoodState.PLATED;
	            plate.addIngredient(food);
	        }
	    }
	    
        FloorDecor_Building table = (FloorDecor_Building)gp.buildingM.getBuilding(packet.getArrayIndex());
        table.currentItem = plate;
	
	    // Assign to player's current item
	    player.currentItem = null;
    }
    private void handlePickupPlate(Packet21PickupPlate packet) {
        // Ignore local player
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        int index = gp.getPlayerMPIndex(packet.getUsername());
        if (index == -1) return;

        PlayerMP player = gp.getPlayerList().get(index);
        
        FloorDecor_Building table = (FloorDecor_Building)gp.buildingM.getBuilding(packet.getArrayIndex());
        table.currentItem = null;
        
        
        // Create a Plate for this player
        Plate plate = new Plate(gp, 0, 0);
        plate.clearIngredients();
        plate.setDirty(packet.isDirty());
        for (String ing : packet.getIngredients()) {
            if (ing != null && !ing.isEmpty()) {
                Food food = (Food) gp.itemRegistry.getItemFromName(ing, 0); // adjust state if needed
                food.foodState = FoodState.PLATED;
                plate.addIngredient(food);
            }
        }

        // Assign to player's current item
        player.currentItem = plate;
    }
    private void handlePlaceOnStove(Packet09PlaceItemOnStove packet) {
        // Ignore if this action was performed by the local player
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        // Find the remote player who did the action
        int index = gp.getPlayerMPIndex(packet.getUsername());
        if (index == -1) return; // Player not found
        PlayerMP player = gp.getPlayerList().get(index);

        // Get the stove from which the item was picked up
        Stove stove = (Stove)gp.buildingM.getBuilding(packet.getStoveArrayIndex());
        if (stove == null) return;
        
        // Give the food/item to the remote player's hand (or current item)
        CookingItem item = (CookingItem)gp.itemRegistry.getItemFromName(packet.getItemName(), 0);
        
        Food food= (Food)gp.itemRegistry.getItemFromName(packet.getFoodName(), packet.getFoodState());
        if(food != null) {
        	item.setCooking(food);
        }
            item.setCookTime(packet.getCookTime());

        // Remove the item from the stove slot
        stove.addItem(packet.getStoveSlot(), item);

        // If the player is holding something already, you might need custom logic
        player.currentItem = null;
    }
    private void handlePickupFromStove(Packet08PickUpFromStove packet) {
        // Ignore if this action was performed by the local player
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        // Find the remote player who did the action
        int index = gp.getPlayerMPIndex(packet.getUsername());
        if (index == -1) return; // Player not found
        PlayerMP player = gp.getPlayerList().get(index);

        // Get the stove from which the item was picked up
        Stove stove = (Stove)gp.buildingM.getBuilding(packet.getStoveArrayIndex());
        if (stove == null) return;

        // Remove the item from the stove slot
        stove.removeItem(packet.getStoveSlot());

        // Give the food/item to the remote player's hand (or current item)
        CookingItem item = (CookingItem)gp.itemRegistry.getItemFromName(packet.getItemName(), 0);

        Food food= (Food)gp.itemRegistry.getItemFromName(packet.getFoodName(), packet.getFoodState());
        if(food != null) {
        	item.setCooking(food);
        	item.setCookTime(packet.getCookTime());
        }
        
        // If the player is holding something already, you might need custom logic
        player.currentItem = item;
    }
    private void handleAddFoodToPlateInHand(Packet19AddFoodToPlateInHand packet) {
       if (packet.getUsername().equals(gp.player.getUsername())) return;
       
       int index = gp.getPlayerMPIndex(packet.getUsername());
       PlayerMP player = gp.getPlayerList().get(index);
      
        Plate plate = (Plate)player.currentItem;
        if(plate == null) return;

        Food food = (Food)gp.itemRegistry.getItemFromName(packet.getFoodName(), packet.getFoodState());
        food.setState(packet.getFoodState());
        plate.addIngredient(food);

        gp.buildingM.setBuildingItem(packet.getTableIndex(), null);
        
    }

    private void handleAddFoodToPlateOnTable(Packet20AddFoodToPlateOnTable p) {
        FloorDecor_Building table = (FloorDecor_Building)gp.buildingM.getBuilding(p.getTableIndex());
        if(table == null) return;

        if(!(table.currentItem instanceof Plate plate)) return;

        Food food = (Food)gp.itemRegistry.getItemFromName(p.getFoodName(), p.getFoodState());

        food.setState(p.getFoodState());
        plate.addIngredient(food);
        int index = gp.getPlayerMPIndex(p.getUsername());
        PlayerMP player = gp.getPlayerList().get(index);
        if (player != null) {
            player.currentItem = null;
        }

        table.currentItem = plate;
    }
    private void handleRemoveItemFromFridge(Packet18RemoveItemFromFridge packet) {
        // Ignore if this update is from the local player
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        // Get the fridge
        Building building = gp.buildingM.getBuilding(packet.getFridgeId());
        if (!(building instanceof Fridge fridge)) return;

        // Validate index
        int index = packet.getItemIndex();
        if (index >= 0 && index < fridge.getContents().size()) {
            fridge.getContents().remove(index);
        }
    }
    private void handleAddItemToFridge(Packet17AddItemToFridge packet) {
        // Ignore if this update is from the local player
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        // Get the fridge building
        Building building = gp.buildingM.getBuilding(packet.getFridgeId());
        if (!(building instanceof Fridge fridge)) return;

        // Create the food item
        Food food = (Food) gp.itemRegistry.getItemFromName(packet.getItemName(), packet.getFoodState());

        // Add it to the fridge contents
        fridge.addItem(food);
    }
    public void sendData(byte[] data) {
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleSpawnInfo(Packet11SpawnInfo packet) {
        if (gp == null || gp.world == null) return;

        // Sync world season
        gp.world.setSeason(packet.getSeason());
        // Sync world day
        gp.world.day = (packet.getDay());

        // Sync world time (raw float, e.g. 12.5f = 12:30)
        gp.world.setTime(packet.getTime());
    }
    private void handleAddItemToChoppingBoard(Packet12AddItemToChoppingBoard packet) {
        ChoppingBoard board = (ChoppingBoard)gp.buildingM.getBuildings()[packet.getBoardIndex()];
        if (board == null) return;

        Food item = (Food) gp.itemRegistry.getItemFromName(packet.getItemName(), 0);
        item.foodState = FoodState.values()[packet.getFoodState()];

        board.setCurrentItem(item); // you'll need a setter in ChoppingBoard
    }
    public void handleRemoveItemFromChoppingBoard(Packet15RemoveItemFromChoppingBoard packet) {
        Building building = gp.buildingM.getBuilding(packet.getBoardIndex());
        if (!(building instanceof ChoppingBoard)) return;

        ChoppingBoard board = (ChoppingBoard) building;
        board.clearCurrentItem();
    }
    public void handleUpdateChoppingProgress(Packet14UpdateChoppingProgress packet) {
        // Ignore if this update came from this client
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        Building building = gp.buildingM.getBuilding(packet.getBoardIndex());
        if (!(building instanceof ChoppingBoard)) return;

        ChoppingBoard board = (ChoppingBoard) building;
        board.setChopCount(packet.getChopCount());
    }
    private void handleClearPlayerHand(Packet13ClearPlayerHand packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
        int index = gp.getPlayerMPIndex(packet.getUsername());
        PlayerMP player = gp.getPlayerList().get(index);
        if (player != null) {
            player.currentItem = null;
        }
    }
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        for (PlayerMP p : gp.playerList) {
            if (p.getUsername().equals(packet.getUsername())) {
                return; // already exists
            }
        }
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " 
                           + packet.getUsername() + " has joined the game...");
        PlayerMP player = new PlayerMP(gp, packet.getX(), packet.getY(), packet.getUsername(), address, port);
        gp.playerList.add(player);
    }
    private void handleMove(Packet02Move packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
        gp.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getCurrentAnimation(), packet.getDirection());
    }
    private void handleRemoveSinkPlate(Packet10RemoveSinkPlate packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
    	for(Building b: gp.buildingM.getBuildings()) {
    		if(b != null) {
	        	if(b.getName().equals("Kitchen Sink 1")) {
	        		Sink sink = (Sink)b;
	        		sink.decreasePlateCount();
	        	}
    		}
        }
    }
    public void handleStartCookingOnStove(Packet16StartCookingOnStove packet) {
        // Ignore if this client is the sender
        if (packet.getUsername().equals(gp.player.getUsername())) return;

        Building building = gp.buildingM.getBuilding(packet.getStoveIndex());
        if (!(building instanceof Stove stove)) return;

        Item slotItem = (packet.getSlot() == 0) ? stove.leftSlot : stove.rightSlot;

        // Only allow if slot is a pan and empty
        if (slotItem instanceof SmallPan pan && pan.cookingItem == null) {
            pan.setCooking(gp.itemRegistry.getItemFromName(packet.getItemName(), 0));
        } else if (slotItem instanceof FryingPan fpan && fpan.cookingItem == null) {
            fpan.setCooking(gp.itemRegistry.getItemFromName(packet.getItemName(), 0));
        }
    }
    
    private void handleChangeRoom(Packet05ChangeRoom packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
        int index = gp.getPlayerMPIndex(packet.getUsername());
        PlayerMP player = gp.getPlayerList().get(index);
        player.currentRoomIndex = packet.getRoomIndex();
    }
    private void handleBinItem(Packet06BinItem packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
    	for(PlayerMP player: gp.playerList) {
    		if(player.getUsername().equals(packet.getUsername())) {
    			player.currentItem = null;
    		}
    	}
    }
    private void handlePickupItem(Packet03PickupItem packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
    	for(PlayerMP player: gp.playerList) {
    		if(player.getUsername().equals(packet.getUsername())) {
    			player.currentItem = gp.itemRegistry.getItemFromName(packet.getName(), packet.getState());
    		}
    	}
    }
    private void handlePickupItemFromTable(Packet07PickUpItemFromTable packet) {
    	gp.buildingM.setBuildingItem(packet.getArrayCounter(), null);

    	// Assign item to remote player if needed
    	for (PlayerMP player : gp.playerList) {
    	    if (player.getUsername().equals(packet.getUsername()) &&
    	        !packet.getUsername().equals(gp.player.getUsername())) {

    	        Item i = gp.itemRegistry.getItemFromName(0, 0, packet.getName(), packet.getState());
    	        player.currentItem = i;
    	    }
    	}
    }
    private void handlePlaceItem(Packet04PlaceItem packet) {
    	if(packet.getUsername().equals(gp.player.getUsername())) {
    		return;
    	}
    	for(PlayerMP player: gp.playerList) {
    		if(player.getUsername().equals(packet.getUsername())) {
    			Item item = player.currentItem;

    			if (item != null) {
    			    // Position it where the packet says
    			    item.hitbox.x = packet.getX();
    			    item.hitbox.y = packet.getY();

    			    // Place into table slot
    			    gp.buildingM.setBuildingItem(packet.getArrayCounter(), item);


    			    // Clear player hand
    			    player.currentItem = null;
    			}
    		}
    	}
    }
}
package net.packets;

import net.GameClient;
import net.GameServer;
import entity.items.Plate;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class Packet22PlacePlate extends Packet {

    private String username;
    private boolean isDirty;
    private List<String> ingredients;
    private int arrayIndex;

    // Constructor for sending
    public Packet22PlacePlate(String username, Plate plate, int arrayIndex) {
        super(22);
        this.username = username;
        this.isDirty = plate.isDirty();
        this.ingredients = plate.getIngredients();
        this.arrayIndex = arrayIndex;
    }

    // Constructor for receiving
    public Packet22PlacePlate(byte[] data) {
        super(22);
        String[] dataArray = readData(data).split(",", 4);
        // Format: isDirty,ingredientsString,username,arrayIndex
        this.isDirty = Boolean.parseBoolean(dataArray[0]);
        this.ingredients = dataArray[1].isEmpty() ? List.of() : Arrays.asList(dataArray[1].split(";"));
        this.username = dataArray[2];
        this.arrayIndex = Integer.parseInt(dataArray[3]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        // Serialize ingredients as semicolon-separated string
        StringJoiner joiner = new StringJoiner(";");
        for (String ing : ingredients) {
            joiner.add(ing);
        }
        String ingredientsString = joiner.toString();

        // Format: packetID,isDirty,ingredientsString,username,arrayIndex
        return ("22" + isDirty + "," + ingredientsString + "," + username + "," + arrayIndex).getBytes();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}

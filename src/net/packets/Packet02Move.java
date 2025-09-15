package net.packets;

import net.GameClient;
import net.GameServer;

public class Packet02Move extends Packet{

    private String username;
    private int x, y;
    private int currentAnimation, direction;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.currentAnimation = Integer.parseInt(dataArray[3]);
        this.direction = Integer.parseInt(dataArray[4]);
    }

    public Packet02Move(String username, int x, int y, int currentAnimation, int direction) {
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.currentAnimation = currentAnimation;
        this.direction = direction;
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
        return ("02" + this.username+","+this.x+","+this.y+","+this.currentAnimation+","+this.direction).getBytes();
    }
    public String getUsername() {
        return username;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getCurrentAnimation() {
        return currentAnimation;
    }
    public int getDirection() {
        return direction;
    }

}

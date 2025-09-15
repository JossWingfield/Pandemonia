package net.packets;

import net.GameClient;
import net.GameServer;
import utility.Season;

public class Packet11SpawnInfo extends Packet {

    private String season;
    private int day;
    private float time;

    public Packet11SpawnInfo(byte[] data) {
        super(11);
        String[] dataArray = readData(data).split(",");
        this.season = dataArray[0];
        this.day = Integer.parseInt(dataArray[1]);
        this.time = Float.parseFloat(dataArray[2]);
    }

    public Packet11SpawnInfo(Season season, int day, float time) {
        super(11);
        this.season = season.toString();
        this.day = day;
        this.time = time;
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
        return ("11" + this.season + "," + this.day + "," + this.time).getBytes();
    }

    public String getSeason() {
        return season;
    }

    public int getDay() {
        return day;
    }

    public float getTime() {
        return time;
    }
}

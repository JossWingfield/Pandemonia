package net.packets;

import net.GameClient;
import net.GameServer;

public abstract class Packet  {

    public static enum PacketTypes {
        INVALID(-1), LOGIN(0), DISCONNECT(1), MOVE(2), PICKUPITEM(3), PLACEITEM(4), CHANGEROOM(5), BINITEM(6), PICKUPFROMTABLE(7),
        PICKUPFROMSTOVE(8), PLACEONSTOVE(9), REMOVESINKPLATE(10), SPAWNINFO(11), ADDTOCHOPPINGBOARD(12), CLEARPLAYERHAND(13),
        UPDATECHOPPINGPROGRESS(14), REMOVEITEMFROMCHOPPINGBOARD(15), STARTCOOKINGONSTOVE(16), ADDTOFRIDGE(17), REMOVEFROMFRIDGE(18),
        ADDFOODTOPLATEINHAND(19), ADDFOODTOPLATEONTABLE(20), PICKUPPLATE(21), PLACEPLATE(22);

        private int packetId;
        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte)packetId;
    }

    public abstract void writeData(GameClient client);
    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();

    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }

    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes p : PacketTypes.values()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

}

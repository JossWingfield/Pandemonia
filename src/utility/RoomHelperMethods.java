package utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;

public class RoomHelperMethods {
	
	public static int MAIN = 0;
	public static int KITCHEN = 0;
	public static int STORES = 1;
	public static int OUTDOORS = 2;
	public static int ELECTRICS = 3;
	public static int BATHROOM = 4;
	public static int BEDROOM = 5;
	public static int BASEMENT = 6;
	public static int ABANDONEDCORRIDOR = 7;
	public static int CORRIDOR1 = 8;
	public static int OLDKITCHEN = 9;
	public static int FREEZER = 10;
	
    public static Map<Integer, int[]> roomGraph = new HashMap<>();

    
    public RoomHelperMethods() {
        roomGraph.put(RoomHelperMethods.MAIN, new int[]{RoomHelperMethods.CORRIDOR1, RoomHelperMethods.OUTDOORS, RoomHelperMethods.BATHROOM});
        roomGraph.put(RoomHelperMethods.STORES, new int[]{RoomHelperMethods.OLDKITCHEN});
        roomGraph.put(RoomHelperMethods.OUTDOORS, new int[]{RoomHelperMethods.MAIN});
        roomGraph.put(RoomHelperMethods.BATHROOM, new int[]{RoomHelperMethods.MAIN});
        roomGraph.put(RoomHelperMethods.ELECTRICS, new int[]{RoomHelperMethods.CORRIDOR1});
        roomGraph.put(RoomHelperMethods.BEDROOM, new int[]{RoomHelperMethods.CORRIDOR1});
        roomGraph.put(RoomHelperMethods.CORRIDOR1, new int[]{RoomHelperMethods.MAIN, RoomHelperMethods.BEDROOM, RoomHelperMethods.ELECTRICS});
        roomGraph.put(RoomHelperMethods.ABANDONEDCORRIDOR, new int[]{RoomHelperMethods.MAIN, RoomHelperMethods.OLDKITCHEN});
        roomGraph.put(RoomHelperMethods.OLDKITCHEN, new int[]{RoomHelperMethods.STORES, RoomHelperMethods.ABANDONEDCORRIDOR, RoomHelperMethods.FREEZER});
        roomGraph.put(RoomHelperMethods.FREEZER, new int[]{RoomHelperMethods.OLDKITCHEN});
    }
	
	public static List<NPC> setCelebrityPresent(List<NPC> npcs, boolean isPresent) {

		for(NPC npc: npcs) {
			if(npc instanceof Customer customer) {
				customer.setCelebrityPresent(isPresent);
			}
		}
		
		return npcs;
	}
	
	public static boolean isCelebrityPresent(List<NPC> npcs) {
		
		for(NPC npc: npcs) {
			if(npc instanceof SpecialCustomer customer) {
				if(customer.isCelebrity()) {
					return true;
				}
			}
		}
		return false;
	}
	
}

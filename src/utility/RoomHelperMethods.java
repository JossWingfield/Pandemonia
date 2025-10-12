package utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;

public class RoomHelperMethods {
	
	public static int MAIN = 0;
	public static int STORES = 1;
	public static int OUTDOORS = 2;
	public static int ELECTRICS = 3;
	public static int BATHROOM = 4;
	public static int BEDROOM = 5;
	
    public static Map<Integer, int[]> roomGraph = new HashMap<>();

    
    public RoomHelperMethods() {
        roomGraph.put(RoomHelperMethods.MAIN, new int[]{RoomHelperMethods.STORES, RoomHelperMethods.OUTDOORS, RoomHelperMethods.BATHROOM});
        roomGraph.put(RoomHelperMethods.STORES, new int[]{RoomHelperMethods.MAIN, RoomHelperMethods.ELECTRICS});
        roomGraph.put(RoomHelperMethods.OUTDOORS, new int[]{RoomHelperMethods.MAIN});
        roomGraph.put(RoomHelperMethods.BATHROOM, new int[]{RoomHelperMethods.MAIN});
        roomGraph.put(RoomHelperMethods.ELECTRICS, new int[]{RoomHelperMethods.STORES, RoomHelperMethods.BEDROOM});
        roomGraph.put(RoomHelperMethods.BEDROOM, new int[]{RoomHelperMethods.ELECTRICS});
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

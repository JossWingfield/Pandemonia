package utility;

import java.util.List;

import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;

public class RoomHelperMethods {
	
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

package entity.npc;

import main.GamePanel;
import utility.CollisionMethods;
import utility.Recipe;
import utility.RoomHelperMethods;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ArrayList;

import ai.Node;
import entity.PlayerMP;
import entity.buildings.Building;
import entity.buildings.EscapeHole;
import entity.items.Item;

public class NPCManager {

    GamePanel gp;

    public List<NPC> npcs = new ArrayList<>(); //The array which stores all the items

    public NPCManager(GamePanel gp) {
        this.gp = gp;
    }
    public void addCustomer() {
    	Customer customer = new Customer(gp, 10*48, 11*48);
    	npcs.add(customer);
    	//gp.mapM.addNPCToRoom(customer, 0);
    }
    public void addStocker() {
    	Stocker stocker = new Stocker(gp, 10*48, 9*48);
    	npcs.add(stocker);
    	//gp.mapM.addNPCToRoom(stocker, 0);
    }
    public void addServer() {
    	Server server = new Server(gp, 10*48, 9*48);
    	npcs.add(server);
    }
    public void addDishWasher() {
    	DishWasher server = new DishWasher(gp, 10*48, 9*48);
    	npcs.add(server);
    }
    public void addRat() {
    	int x = 0;
    	int y = 0;
    	EscapeHole hole = gp.mapM.findEscapeHole(1);
    	x = (int)hole.hitbox.x;
    	y = (int)hole.hitbox.y;
    	Rat rat = new Rat(gp, 10*48, 9*48);
    	rat.hitbox.x = x+32;
    	rat.hitbox.y = y;
    	if(gp.mapM.isInRoom(1)) {
    		npcs.add(rat);
    	} else {
    		gp.mapM.addNPCToRoom(rat, 1);
    	}
    }
    public boolean containsNPC(NPC npc) {
        return npcs.contains(npc);
    }
    public void addNPC(NPC npc) {
        npcs.add(npc);
    }
    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }
    public void setNPCs(List<NPC> npcs) {
    	this.npcs = npcs;
    }
    public List<NPC> getNPCs() {
    	return npcs;
    }
    public void checkInteractions(int simulationDistance, float xDiff, float yDiff) {
   	 for (NPC i: npcs) { //Loops through the items on the current map
            if (i != null) {
            	if(i.hitbox.contains(gp.mouseI.mouseX+xDiff, gp.mouseI.mouseY+yDiff)) {
            		if(CollisionMethods.getDistance(i.hitbox.x+i.hitbox.width/2, i.hitbox.y+i.hitbox.height/2, gp.player.hitbox.x + gp.player.hitbox.width/2, gp.player.hitbox.y + gp.player.hitbox.height/2) < gp.player.reachDistance) {
            			i.interact();
		            }
		        }
            }
        }
   }
	public void addSpecialCustomer() {
		Customer customer = new SpecialCustomer(gp, 10*48, 9*48);
    	npcs.add(customer);
	}
	public Customer findWaitingCustomer() {
		for(NPC b: npcs) {
			if(b != null) {
				if(b instanceof Customer customer) {
					if(customer.waitingToOrder) {
						if(customer.atTable) {
							return customer;
						}
					}
				}
			}
		}
		return null;
	}
    public boolean entityCheck(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(NPC b: npcs) {
			if(b != null) {
				if(b.isSolid) {
					if(b.hitbox.intersects(hitbox)) {
						return false;
					}	
				}
			}
		}
		return true;
	}
    public boolean entityCheck(Rectangle2D.Float hitbox) {
		for(NPC b: npcs) {
			if(b != null) {
				if(b.hitbox.intersects(hitbox)) {
					return false;
				}	
			}
		}
		return true;
	}
    public boolean stockerCheck(Rectangle2D.Float hitbox) {
        for (NPC b : npcs) {
            if (b != null && b.hitbox.intersects(hitbox)) {
                if ("Stocker".equals(b.npcType)) {
                    return true; // found a Stocker in the way
                }
            }
        }
        return false; // no Stocker intersecting
    }
    public void drawPaths(Graphics2D g2) {
        if (npcs == null) return;

        for (NPC i : npcs) { // Loop through all NPCs on the current map
            if (i == null) continue;
            if (i.pathF.pathList.isEmpty()) continue;

            int cellSize = gp.tileSize / i.pathF.nodesPerTile;

            for (Node node : i.pathF.pathList) {
                int x = node.col * cellSize;
                int y = node.row * cellSize;
                g2.fillRect(x, y, cellSize, cellSize);
            }
        }
    }
    public boolean containsAnyNPC() {
        return npcs.size() > 0;
    }
    //Draw the item hitboxes
    public void drawNPCHitboxes(Graphics2D g) {
        for(NPC i: npcs) { //Loops through the items on the current map
            if(i != null) {
                i.drawHitbox(g);
            }
        }
    }
	public Customer findCustomerWithOrder(Recipe recipe) {
		List<NPC> npcCopy = new ArrayList<NPC>(npcs);
		for(NPC npc : npcCopy) {
			if(npc.getNPCType().equals("Customer")) {
				Customer c = (Customer)npc;
				if(c.foodOrder != null) {
					if(c.foodOrder.equals(recipe)) {
						return c;
					}
				}
			}
		}
		return null;
	}
    public void update() {
    	for (NPC i : new ArrayList<>(npcs)) {
    	    if (i != null) {
    	        i.update();
    	    }
    	}
        
    }

}


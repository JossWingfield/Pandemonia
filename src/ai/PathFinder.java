package ai;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import entity.buildings.Building;
import main.GamePanel;
import map.Room;

/*
public class PathFinder { //TODO fix this all
	
    GamePanel gp;

    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;
    public int nodesPerTile = 1;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes(gp.mapM.getRoom(0));
    }
    public void instantiateNodes(Room room) { //TODO this will break when in different sized rooms
    	
        node = new Node[room.mapWidth][room.mapHeight];

        for (int i = 0; i < room.mapHeight; i++) {

            for (int j = 0; j < room.mapWidth; j++) {

                node[j][i] = new Node(j, i);
            }
        }

    }
    public void resetNodes(Room room) {

        for (int i = 0; i < room.mapHeight; i++) {

            for (int j = 0; j < room.mapWidth; j++) {

                node[j][i].open = false;
                node[j][i].checked = false;
                node[j][i].solid = false;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Room room) {
    	
        resetNodes(room);

        //Set start and goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        for (int i = 0; i < room.mapHeight; i++) {

            for (int j = 0; j <room.mapWidth; j++) {

                //Set solid nodes
                int tileNum = room.mapGrid[1][j][i];
                if(gp.mapM.tiles[tileNum].solid) {
                    node[j][i].solid = true;
                }
                //Set costs
                getCost(node[j][i]);

            }
        }
        for (Building b : room.getBuildings()) {
            if (b != null && b.isSolid) {
                // Convert building hitbox to tile coordinates
                int tileX = (int) ((b.hitbox.x + b.hitbox.width / 2) / gp.tileSize);
                int tileY = (int) ((b.hitbox.y + b.hitbox.height / 2) / gp.tileSize);

                if (tileX >= 0 && tileX < room.mapWidth && tileY >= 0 && tileY < room.mapHeight) {
                    node[tileX][tileY].solid = true;
                }
            }
        }
    }

    public void getCost(Node node) {
        //calculates MANHATTAN values (heuristic)

        //Get G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);

        node.gCost = xDistance + yDistance;

        //Get H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //Get F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search(Room room) {
        while(!goalReached && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            //Check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //Open the Up node
            if(row - 1 >= 0) {
                openNode(node[col][row-1]);
            }
            //Open the Left node
            if(col - 1 >= 0) {
                openNode(node[col-1][row]);
            }
            //Open the Down node
            if(row + 1 < room.mapHeight) {
                openNode(node[col][row+1]);
            }
            //Open the Right node
            if(col + 1 < room.mapWidth) {
                openNode(node[col+1][row]);
            }
            //TODO testing diagonal pathfinding
            if(row - 1 >= 0 && col - 1 >= 0) { //UP LEFT
                openNode(node[col-1][row-1]);
            }
            if(row - 1 >= 0 && col + 1 < room.mapWidth) { //UP RIGHT
                openNode(node[col+1][row-1]);
            }
            if(row + 1 < room.mapHeight && col - 1 >= 0) { //DOWN LEFT
                openNode(node[col-1][row+1]);
            }
            if(row + 1 < room.mapHeight && col + 1 < room.mapWidth) { //DOWN RIGHT
                openNode(node[col+1][row+1]);
            }


            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {

                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    public void openNode(Node node) {

        if(!node.open && !node.checked && !node.solid) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);

        }

    }

    public void trackThePath() {

        Node current = goalNode;

        while(current != startNode) {
            pathList.add(0, currentNode);
            current = current.parent;
        }

    }

    public void drawNodes(Graphics2D g2) {

        for (int i = 0; i < gp.mapM.currentRoom.mapHeight; i++) {
            for (int j = 0; j < gp.mapM.currentRoom.mapWidth; j++) {

                g2.setColor(Color.GREEN);
                if(node[j][i].solid) {
                    g2.setColor(Color.RED);
                } else if(node[j][i] == goalNode) {
                    g2.setColor(Color.ORANGE);
                } else if(node[j][i] == startNode) {
                    g2.setColor(Color.BLUE);
                } else if(node[j][i] == currentNode) {
                    g2.setColor(Color.PINK);
                }
                g2.fillRect(j*48, i*48, 48, 48);
            }
        }

        if(goalReached) {
            Node node = pathList.get(0);

            for (int i = 0; i < pathList.size(); i++) {

                g2.setColor(Color.white);
                int x = node.col * gp.tileSize;
                int y = node.row * gp.tileSize;

                g2.fillRect(x, y, 48, 48);

                node = node.parent;

            }
        }

    }

}
*/
public class PathFinder {

    GamePanel gp;

    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;
    public int nodesPerTile = 2;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes(gp.mapM.getRoom(0));
    }

    public void instantiateNodes(Room room) {
        int width = room.mapWidth * nodesPerTile;
        int height = room.mapHeight * nodesPerTile;

        node = new Node[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                node[j][i] = new Node(j, i);
            }
        }
    }

    public void resetNodes(Room room) {
        int width = room.mapWidth * nodesPerTile;
        int height = room.mapHeight * nodesPerTile;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                node[j][i].open = false;
                node[j][i].checked = false;
                node[j][i].solid = false;
                node[j][i].parent = null;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Room room) {

        resetNodes(room);

        int width = room.mapWidth * nodesPerTile;
        int height = room.mapHeight * nodesPerTile;

        // Scale tile coordinates to node coordinates
        startNode = node[startCol * nodesPerTile + nodesPerTile / 2][startRow * nodesPerTile + nodesPerTile / 2];
        goalNode = node[goalCol * nodesPerTile + nodesPerTile / 2][goalRow * nodesPerTile + nodesPerTile / 2];
        currentNode = startNode;
        openList.add(currentNode);

        // Set solid nodes from tiles
        for (int i = 0; i < room.mapHeight; i++) {
            for (int j = 0; j < room.mapWidth; j++) {
                int tileNum = room.mapGrid[1][j][i];
                if (gp.mapM.tiles[tileNum].solid) {
                    // Mark all sub-nodes in this tile as solid
                    for (int dy = 0; dy < nodesPerTile; dy++) {
                        for (int dx = 0; dx < nodesPerTile; dx++) {
                            node[j * nodesPerTile + dx][i * nodesPerTile + dy].solid = true;
                        }
                    }
                }
            }
        }

        // Mark buildings as solid
        for (Building b : room.getBuildings()) {
            if (b != null && b.isSolid) {
            	if(!b.getName().equals("Gate 1")) {
	                int tileX = (int) ((b.hitbox.x + b.hitbox.width / 2) / gp.tileSize);
	                int tileY = (int) ((b.hitbox.y + b.hitbox.height / 2) / gp.tileSize);
	
	                for (int dy = 0; dy < nodesPerTile; dy++) {
	                    for (int dx = 0; dx < nodesPerTile; dx++) {
	                        int nx = tileX * nodesPerTile + dx;
	                        int ny = tileY * nodesPerTile + dy;
	                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
	                            node[nx][ny].solid = true;
	                        }
	                    }
	                }
            	}
            }
        }

        // Set costs
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getCost(node[j][i]);
            }
        }
    }

    public void getCost(Node node) {
        // calculates MANHATTAN values (heuristic)

        // G cost: distance from start
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost: distance to goal
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost = G + H
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search(Room room) {
        int width = node.length;
        int height = node[0].length;
        int maxSteps = width * height;   // instead of 500

        while (!goalReached && step < maxSteps) {

            int col = currentNode.col;
            int row = currentNode.row;

            // Check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // Neighbors (4-dir + diagonals)
            openNode(node[col][row - 1], false); // Up
            openNode(node[col - 1][row], false); // Left
            openNode(node[col + 1][row], false); // Right
            openNode(node[col][row + 1], false); // Down

            openNode(node[col - 1][row - 1], true);  // Up-Left
            openNode(node[col + 1][row - 1], true);  // Up-Right
            openNode(node[col - 1][row + 1], true);  // Down-Left
            openNode(node[col + 1][row + 1], true);  // Down-Right

            // Pick best node
            int bestNodeIndex = 0;
            int bestNodefCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                Node n = openList.get(i);
                if (n.fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = n.fCost;
                } else if (n.fCost == bestNodefCost) {
                    if (n.gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if (openList.isEmpty()) {
                break; // no path
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }
    public void openNode(Node neighbor, boolean diagonal) {
        if (!neighbor.open && !neighbor.checked && !neighbor.solid) {
            neighbor.open = true;
            neighbor.parent = currentNode;
            // Increment G cost from current node
            neighbor.gCost = currentNode.gCost + (diagonal ? 14 : 10); // scale to int
            // H cost stays the Manhattan distance (or you could use Euclidean)
            int xDistance = Math.abs(neighbor.col - goalNode.col);
            int yDistance = Math.abs(neighbor.row - goalNode.row);
            neighbor.hCost = (xDistance + yDistance) * 10;
            neighbor.fCost = neighbor.gCost + neighbor.hCost;
            openList.add(neighbor);
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while (current != null && current != startNode) {
            pathList.add(0, current); // ✅ fixed: add "current"
            current = current.parent;
        }
    }
}
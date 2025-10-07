package ai;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import entity.buildings.Building;
import main.GamePanel;
import map.Room;

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
            pathList.add(0, current);
            current = current.parent;
        }
    }
    public int[] getRandomReachableNode(Room room, float avoidX, float avoidY, float minDistance) {
        Random random = new Random();
        int width = room.mapWidth * nodesPerTile;
        int height = room.mapHeight * nodesPerTile;

        int maxAttempts = 200;
        for (int i = 0; i < maxAttempts; i++) {
            int randCol = random.nextInt(width);
            int randRow = random.nextInt(height);
            Node candidate = node[randCol][randRow];

            if (candidate != null && !candidate.solid) {
                int tileCol = randCol / nodesPerTile;
                int tileRow = randRow / nodesPerTile;

                // Check it's a floor tile (mapGrid tile #9)
                if (room.mapGrid[1][tileCol][tileRow] != 9) continue;

                // Check distance from player
                float nodeX = tileCol * gp.tileSize + gp.tileSize/2f;
                float nodeY = tileRow * gp.tileSize + gp.tileSize/2f;
                float dx = nodeX - avoidX;
                float dy = nodeY - avoidY;
                if (dx*dx + dy*dy < minDistance*minDistance) continue;

                return new int[]{tileCol, tileRow};
            }
        }

        // fallback: move opposite direction of player if no random node found
        int fallbackCol = (int)((avoidX - (avoidX - gp.player.hitbox.x)) / gp.tileSize);
        int fallbackRow = (int)((avoidY - (avoidY - gp.player.hitbox.y)) / gp.tileSize);
        return new int[]{fallbackCol, fallbackRow};
    }
}
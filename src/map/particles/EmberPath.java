package map.particles;

import java.util.List;

import ai.Node;
import main.GamePanel;

public class EmberPath {
    List<Node> path;
    int currentIndex = 0;
    boolean withLight;

    public EmberPath(List<Node> path, boolean withLight) {
        this.path = path;
        this.withLight = withLight;
    }

    public boolean isFinished() {
        return path == null || currentIndex >= path.size();
    }

    public float[] getNextPosition(float speed, GamePanel gp) {
        if (isFinished()) return null;

        Node node = path.get(currentIndex);
        float cell = gp.tileSize / (float) gp.pathF.nodesPerTile;
        float targetX = node.col * cell + cell * 0.5f;
        float targetY = node.row * cell + cell * 0.5f;

        float x = targetX;
        float y = targetY;

        currentIndex++;
        return new float[]{x, y};
    }
}
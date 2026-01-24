package entity;

import main.GamePanel;
import main.KeyListener;
import main.MouseListener;
import net.ClientHandler;

public class PlayerMP extends Player {

    // Server-only: which client owns this player
    private transient ClientHandler owner;

    public PlayerMP(GamePanel gp,
                    int x,
                    int y,
                    KeyListener keyI,
                    MouseListener mouseI,
                    String username) {

        super(gp, x, y, keyI, mouseI, username);
    }

    // Convenience constructor for remote players
    public PlayerMP(GamePanel gp,
                    int x,
                    int y,
                    String username) {

        super(gp, x, y, null, null, username);
    }

    /* -------- Server-only helpers -------- */

    public void setOwner(ClientHandler owner) {
        this.owner = owner;
    }

    public ClientHandler getOwner() {
        return owner;
    }

    public boolean isLocal() {
        return keyI != null;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
    }
}

package entity;

import java.net.InetAddress;

import main.GamePanel;
import main.KeyListener;
import main.MouseListener;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;
    
    public PlayerMP(GamePanel gp, int x, int y, KeyListener keyI, MouseListener mouseI, String username, InetAddress ipAddress, int port) {
        super(gp, x, y, keyI, mouseI, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    public PlayerMP(GamePanel gp, int x, int y, String username, InetAddress ipAddress, int port) {
        super(gp, x,y , null, null, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    //@Override
    public void update(double dt) {
        super.update(dt);
    }

}

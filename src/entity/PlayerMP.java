package entity;

import main.GamePanel;
import utility.KeyboardInput;
import utility.MouseInput;

import java.net.InetAddress;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(GamePanel gp, int x, int y, KeyboardInput keyI, MouseInput mouseI, String username, InetAddress ipAddress, int port) {
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
    public void update() {
        super.update();
    }

}

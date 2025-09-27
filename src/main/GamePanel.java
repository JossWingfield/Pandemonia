package main;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import entity.PlayerMP;
import entity.buildings.Building;
import entity.buildings.BuildingManager;
import entity.items.Item;
import entity.items.ItemManager;
import entity.npc.NPC;
import entity.npc.NPCManager;
import map.Customiser;
import map.LightingManager;
import map.MapBuilder;
import map.MapManager;
import net.DiscoveryManager;
import net.GameClient;
import net.GameServer;
import net.packets.Packet00Login;
import utility.Catalogue;
import utility.GUI;
import utility.ItemRegistry;
import utility.KeyboardInput;
import utility.MouseInput;
import utility.RecipeManager;
import utility.Settings;
import utility.World;
import utility.save.SaveManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
	
	// NETWORKING
    public GameClient socketClient;
    public GameServer socketServer;
    public boolean multiplayer = false;
    public boolean serverHost = false;
    public boolean joiningServer = false;

    // LAN DISCOVERY (single manager)
    public DiscoveryManager discovery;
    public DiscoveryManager.DiscoveredServer selectedServer;
    private boolean hostError = false;
    private int errorCounter = 0;

    public ArrayList<PlayerMP> playerList;
    
    // SETTINGS
    private final int FPS = 60; //The frames per second the game is updated and drawn at
    private final double optimalLoopTime = 1000000000 / FPS; //The optimal time for the game loop
    private final int scale = 3; //The scale factor the tiles will be multiplied by, to make the game more visible
    private final int baseTileSize = 16; // The base size of each tile
    public final int tileSize = baseTileSize * scale; //The drawn size of each tile

    // SCREEN SIZE
    public int tilesInWidth = 24; //The number of tiles in width and in height
    public int tilesInHeight = 16;
    public int frameWidth = tilesInWidth * tileSize; //The number of pixels in width and in height
    public int frameHeight = tilesInHeight * tileSize;

    // SYSTEM initialising all classes and managers
    public Player player;
    public BuildingManager buildingM = new BuildingManager(this);
    public ItemManager itemM = new ItemManager(this);
    public NPCManager npcM = new NPCManager(this);
    public MapManager mapM = new MapManager(this);
    public KeyboardInput keyI = new KeyboardInput(this);
    public MouseInput mouseI = new MouseInput(this);
    public GUI gui = new GUI(this);
    public ArrayList<Entity> updateEntityList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ItemRegistry itemRegistry = new ItemRegistry(this);
    public World world = new World(this);
    public LightingManager lightingM = new LightingManager(this);
    public Customiser customiser = new Customiser(this);
    public Catalogue catalogue = new Catalogue(this);
    public RecipeManager recipeM = new RecipeManager();
    public SaveManager saveM = new SaveManager(this);
    public MapBuilder mapB = new MapBuilder(this);
    //THREAD initialising the thread which the game loop is run off
    public Thread thread;

    // GAME STATES The different states which represent which, screens, gui and gameplay should be allowed in each state
    public int currentState = 0; //Determines which 'state' the game is in
    public final int titleState = 0;
    public final int playState = 1;
    public final int mapBuildState = 2;
    public final int pauseState = 3;
    public final int startGameSettingsState = 4;
    public final int settingsState = 5;
    public final int multiplayerSettingsState = 6;
    public final int gameOverState = 7;
    public final int writeUsernameState = 8;
    public final int lanJoinMenuState = 9;
    public final int customiseRestaurantState = 10;
    public final int catalogueState = 11;
    public final int xpState = 12;
    public final int chooseRecipeState = 13;

    // AESTHETICS
    private Color backgroundColour = new Color(51, 60, 58);
    
    //SCREEN SETTINGS AND FREEZE
    private int freezeCounter = 0;
    public int screenWidth, screenHeight;
    private BufferedImage frozenImage;
    private boolean getFrozenImage = false;
    
    // SCREEN SHAKE VARIABLES
    private int shakeDuration = 0;  // Duration of the shake in frames
    private int shakeIntensity = 0; // Intensity of the shake (max offset in pixels)
    private final Random random = new Random(); // Random generator for shake offsets
    private int shakeOffsetX = 0; // Current horizontal shake offset
    private int shakeOffsetY = 0; // Current vertical shake offset
    
    private boolean firstUpdate = true;
    
    //LIGHTING
    private BufferedImage colorBuffer = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D gColor;


    // INITIATES PANEL SETTINGS
    public GamePanel() {
        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(backgroundColour);
        //Adds keyboard and mouse inputs
        this.addKeyListener(keyI);
        this.addMouseListener(mouseI);
        this.addMouseMotionListener(mouseI);
        screenWidth = (int)(frameWidth);
        screenHeight = (int)(frameHeight);
        Graphics2D warm = colorBuffer.createGraphics();
        warm.setComposite(AlphaComposite.SrcOver);
        warm.setColor(Color.BLACK);
        warm.fillRect(0, 0, frameWidth, frameHeight);
        warm.dispose();
        gColor = colorBuffer.createGraphics();
    	gColor.setComposite(AlphaComposite.SrcOver);
    	gColor.setColor(backgroundColour);
    	gColor.fillRect(0, 0, frameWidth, frameHeight);
        gColor.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));

    }
    
    public void playSinglePlayer() {
    	player = new Player(this, 48*10, 48*10, keyI, mouseI, "");
    	currentState = playState;
    }
    public void hostServer(String username) {
        if (serverHost) return;
        if(hostError) {
            playerList = null;
            player = null;
        	return;
        }

        playerList = new ArrayList<PlayerMP>();
        // 1) Create host player
        player = new PlayerMP(this, 48 * 10, 48 * 10, keyI, mouseI, username, null, -1);
        if (!playerList.contains(player)) playerList.add((PlayerMP) player);

        // 2) Start server
        try {
            socketServer = new GameServer(this);
            socketServer.start();
            serverHost = true;
        } catch (BindException e) {
            // Another server is already hosting
        	hostError = true;
        	errorCounter = 60;
            JOptionPane.showMessageDialog(null, 
                "A LAN world is already being hosted on this network.\nYou can only join it.",
                "LAN Host Error",
                JOptionPane.ERROR_MESSAGE);
            serverHost = false;
            currentState = multiplayerSettingsState;
            mouseI.leftClickPressed = false;
            gui.startLoading = false;
            return;
        } catch (Exception e) {
            e.printStackTrace();
            serverHost = false;
            return;
        }

        // 3) Start local client (connects to localhost)
        socketClient = new GameClient(this, "127.0.0.1", GameServer.GAME_PORT);
        socketClient.start();

        // 4) Send login packet from local client
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), (int) player.hitbox.x, (int) player.hitbox.y);
        if (socketServer != null) socketServer.addConnection((PlayerMP) player, loginPacket);
        loginPacket.writeData(socketClient);

        // Ensure discovery is running for LAN browsing
        if (discovery == null) {
            discovery = new DiscoveryManager(false, null, null, 0); // client mode
            discovery.start();
        }

        player.mouseI.leftClickPressed = false;
        multiplayer = true;
        currentState = playState;
    }

    public void joinServer(String username, String ip, int port) {
        if (joiningServer) return;
        joiningServer = true;
        
        if (playerList == null) {
            playerList = new ArrayList<>();
        }

        // 1) Create local player
        player = new PlayerMP(this, 48 * 10, 48 * 10, keyI, mouseI, username, null, -1);
        if (!playerList.contains(player)) playerList.add((PlayerMP) player);

        // 2) Ensure discovery is running
        if (discovery == null) {
            discovery = new DiscoveryManager(false, null, null, 0); // client mode
            discovery.start();
        }

        // 3) Start client
        socketClient = new GameClient(this, ip, port);
        socketClient.start();

        // 4) Send login packet
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), (int) player.hitbox.x, (int) player.hitbox.y);
        loginPacket.writeData(socketClient);

        multiplayer = true;
        player.mouseI.leftClickPressed = false;
        currentState = playState;
    }
    public void startDiscovery() {
        // Stop old discovery if already running
        if (discovery != null) {
            discovery.shutdown();
        }

        // Start a new discovery manager in client mode
        discovery = new DiscoveryManager(
            false,      // isServer = false (we are searching)
            null,       // username not needed for client
            null,       // world name not needed
            0           // game port not needed
        );
        discovery.start();
    }
    public void stopHosting() {
        if (!serverHost) return;
        serverHost = false;
        multiplayer = false;

        // Shutdown GameServer and its discovery
        if (socketServer != null) {
            socketServer.shutdown();
            socketServer = null;
        }

        // Stop local client
        if (socketClient != null) {
            socketClient.interrupt();
            socketClient = null;
        }

        // Optionally clear players
        if (playerList != null) playerList.clear();
    }
    //Starts the thread, called from the Main class
    public void start() {
        startThread();
    }
    public void stopDiscovery() {
        if (discovery != null) {
            discovery.shutdown();
            discovery = null;
        }
    }
    //Initialises and starts the thread
    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }
    
    protected BufferedImage importImage(String filePath) { //Imports and stores the image
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
            //BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }
    //THE GAME LOOP
    @Override
    public void run() {
        //Initialising variables
        long previousUpdateTime = System.nanoTime();
        long interval = 0;
        double delta = 0;

        //CHECKS IF THREAD IS ACTIVE
        while(thread != null) {
            long current = System.nanoTime();
            long updateTime = current - previousUpdateTime;
            delta += updateTime / optimalLoopTime;
            interval += updateTime;
            previousUpdateTime = current;

            if(delta >= 1) {
                update();
                repaint();
                delta = 0;
            }
            //Resetting variables
            if(interval >= 1000000000) {
                interval = 0;
            }

        }
    }
    public synchronized ArrayList<PlayerMP> getPlayerList() {
        return playerList;
    }
    
    public void removePlayerMP(String username) {
        int index = 0;
        for(PlayerMP p : getPlayerList()) {
            if(p.getUsername().equals(username)) {
                break;
            }
            index++;
        }
        getPlayerList().remove(index);
    }

    public int getPlayerMPIndex(String username) {
        for (int i = 0; i < getPlayerList().size(); i++) {
            if (username.equals(getPlayerList().get(i).getUsername())) {
                return i;
            }
        }
        return -1; // not found
    }
    
    public void movePlayer(String username, int x, int y, int currentAnimation, int direction) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = getPlayerList().get(index);
        player.hitbox.x = x;
        player.hitbox.y = y;
        player.setCurrentAnimation(currentAnimation);
        player.setDirection(direction);
    }
    
    public void setFreezeCounter(int freezeCounter) {
    	this.freezeCounter = freezeCounter;
    	getFrozenImage = true;
    }
    
    // SCREEN SHAKE METHOD
    public void screenShake(int duration, int intensity) {
        this.shakeDuration = duration; // Set shake duration (frames)
        this.shakeIntensity = intensity; // Set shake intensity (max offset)
    }
    
    public void setFullScreen() {
    	
    	Settings.fullScreen = true;
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice gd = ge.getDefaultScreenDevice();
    	
    	screenWidth = gd.getDisplayMode().getWidth();
    	screenHeight = gd.getDisplayMode().getHeight();
    	
    	//Center the game panel within the fullscreen window
        int xOffset = (screenWidth - frameWidth) / 2;
        int yOffset = (screenHeight - frameHeight) / 2;

        // Apply centering offsets in the paintComponent method
        this.setLocation(xOffset, yOffset);
    	
    	Main.window.setSize(screenWidth, screenHeight);
    }
    public void stopFullScreen() {
    	Settings.fullScreen = false;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        screenWidth = frameWidth;
        screenHeight = frameHeight;

        // Center the window on screen
        int xOffset = (gd.getDisplayMode().getWidth() - frameWidth) / 2;
        int yOffset = (gd.getDisplayMode().getHeight() - frameHeight) / 2;

        this.setLocation(0, 0); // reset GamePanel position
        Main.window.setSize(frameWidth, frameHeight);
        Main.window.setLocation(xOffset, yOffset);
    }
    //UPDATES THE GAME
    public void update() {    	
    	
    	if(errorCounter > 0) {
    		errorCounter--;
    		if(errorCounter == 0) {
    			hostError = false;
    		}
    	}
    	
    	if (shakeDuration > 0) {
            // Randomly generate shake offsets within the intensity range
            shakeOffsetX = random.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            shakeOffsetY = random.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            shakeDuration--; // Decrease shake duration
        } else {
            // Reset shake offsets when shake duration ends
            shakeOffsetX = 0;
            shakeOffsetY = 0;
        }

    	if(freezeCounter == 0) {
	    	if(currentState == playState || currentState == customiseRestaurantState || currentState == catalogueState || currentState == xpState) {
		    	if(firstUpdate) {
		    		firstUpdate = false;
		    	}
		    	
		    	if(multiplayer) {
		    		for(Player p: getPlayerList()) {
		    			if(p.getUsername() != player.getUsername()) {
		    				if(p.currentRoomIndex == player.currentRoomIndex) {
		    					p.updateInteractHitbox();
		    				}
		    			}
		    		}
		    	}
		    		
		    	player.update();
		    	mapM.update();
		    	buildingM.update();
		    	npcM.update();
		    	itemM.update();
		    	world.update();
		    	lightingM.update();
		    	if(currentState == customiseRestaurantState) {
		    		customiser.update();
		    	}
	    	} else if(currentState == mapBuildState) {
	    		mapB.update();
	    		buildingM.update();
	    	}
    		gui.update();
    	} else {
    		freezeCounter--;
    		if(freezeCounter<= 0) {
    			freezeCounter = 0;
    		}
    	}

    }
    //DRAWS THE GAME
    public void paintComponent(Graphics g1) {

        super.paintComponent(g1);
        Graphics2D g2 = (Graphics2D) g1;
        
        double scaleX = (double) screenWidth / frameWidth;
        double scaleY = (double) screenHeight / frameHeight;

        g2.scale(scaleX, scaleY);
        
        // Apply shake offsets
        g2.translate(shakeOffsetX, shakeOffsetY);
        
        if(getFrozenImage) {
        	getFrozenImage = false;
        	frozenImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
            // Get the Graphics2D object from the BufferedImage
            Graphics2D g3 = frozenImage.createGraphics();
            draw(g3);
            g3.dispose();
        }

        // Draw the game content directly with scaling
        if(freezeCounter == 0) {
        	draw(g2);
        } else {
        	g2.drawImage(frozenImage, 0, 0, screenWidth, screenHeight, null);
        }
        
        g2.dispose();
    }
    
    public void draw(Graphics2D g2) {
        
        if(currentState == playState || currentState == pauseState || currentState == settingsState || currentState == catalogueState || currentState == customiseRestaurantState || currentState == xpState) {
        	
        	gColor = colorBuffer.createGraphics();
        	gColor.setComposite(AlphaComposite.SrcOver);
        	gColor.setColor(backgroundColour);
        	gColor.fillRect(0, 0, frameWidth, frameHeight);
            gColor.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));

        	
        		int xDiff = player.xDiff;
        		int yDiff = player.yDiff;
        		mapM.draw(gColor, xDiff, yDiff);	 
        		
        		Building[] bottomLayer = buildingM.getBottomLayer();
    	        for(int i = 0; i < bottomLayer.length-1; i++) {
    	        	if(bottomLayer[i] != null) {
    	        		bottomLayer[i].draw(gColor);
    	        	}
    	        }
    	        
    	        Building[] secondLayer = buildingM.getSecondLayer();
    	        for(int i = 0; i < secondLayer.length-1; i++) {
    	        	if(secondLayer[i] != null) {
    	        		secondLayer[i].draw(gColor);
    	        	}
    	        }
        		
    	        if(multiplayer) {
	        		for (int i = 0; i < getPlayerList().size(); i++) {
	    	            if (getPlayerList().get(i) != null) {
	    	            	Player p = getPlayerList().get(i);
	    	            	if(p != player) {
	    	            		p.xDiff = player.xDiff;
	    	            		p.yDiff = player.yDiff;
	    	            	}
	    	            	if(p.currentRoomIndex == player.currentRoomIndex) {
		    	            	entityList.add(p);
	    	            	}
	    	            }
	    	        }
	        	} else {
		        	entityList.add(player);
	        	}
	        	
	        	Building[] builds = buildingM.getBuildingsToDraw();
		        for(int i = 0; i < builds.length-1; i++) {
		        	if(builds[i] != null) {
		        		entityList.add(builds[i]);
		        	}
		        }
		        Item[] itemsInBuildings = buildingM.getBuildingItems();
		        for(int i = 0; i < itemsInBuildings.length-1; i++) {
		        	if(itemsInBuildings[i] != null) {
		        		entityList.add(itemsInBuildings[i]);
		        	}
		        }
		        List<NPC> npcs = npcM.getNPCs();
		        for(NPC npc: npcs) {
		        	if(npc != null) {
		        		entityList.add(npc);
		        	}
		        }
	        	
	        	//SORT
	    		ArrayList<Entity> listCopy = new ArrayList<Entity>(entityList);

	            Collections.sort(listCopy, new Comparator<Entity>() {
	                @Override
	                public int compare(Entity e1, Entity e2) {
	
	                    int result = Integer.compare((int)e1.hitbox.y, (int)e2.hitbox.y);
	                    return result;
	                }
	            });
	            
	            entityList = listCopy;
	            //DRAW ENTITIES
	            for(int i = 0; i < entityList.size(); i++) {
	            	Entity a = entityList.get(i);
	            	if(a != null) {
		            	if(a.isOnScreen(xDiff, yDiff, frameWidth, frameHeight)) {
		            		a.draw(gColor);
		            	}
	            	}
	            }
	            //EMPTY ENTITY LIST
	            entityList.clear();
	            
	            Building[] thirdLayer = buildingM.getThirdLayer();
		        for(int i = 0; i < thirdLayer.length-1; i++) {
		        	if(thirdLayer[i] != null) {
		        		thirdLayer[i].draw(gColor);
		        	}
		        }
		        
		        List<Item> items = new ArrayList<Item>(itemM.getItems());
		        for(Item item: items) {
		        	if(item != null) {
		        		item.draw(gColor);
		        	}
		        }
		    
		        Building[] fourthLayer = buildingM.getFourthLayer();
		        for(int i = 0; i < fourthLayer.length-1; i++) {
		        	if(fourthLayer[i] != null) {
		        		fourthLayer[i].draw(gColor);
		        	}
		        }
		        
		        Building[] fifthLayer = buildingM.getFifthLayer();
		        for(int i = 0; i < fifthLayer.length-1; i++) {
		        	if(fifthLayer[i] != null) {
		        		fifthLayer[i].draw(gColor);
		        	}
		        }
		        
	            mapM.drawForeground(gColor, xDiff, yDiff);
	            
		        gColor.dispose();

		        if(Settings.fancyLighting) {
		        	// === 2. Apply lighting ===
		        	lightingM.applyLighting(colorBuffer, g2);
        		} else {
        			g2.drawImage(colorBuffer, 0, 0, frameWidth, frameHeight, null);
        		}
	            
	            for (int i = 0; i < thirdLayer.length; i++) {
	                if (thirdLayer[i] != null) {
	                    thirdLayer[i].drawOverlayUI(g2);
	                }
	            }

	            for (int i = 0; i < fourthLayer.length; i++) {
	                if (fourthLayer[i] != null) {
	                    fourthLayer[i].drawOverlayUI(g2);
	                }
	            }

	            for (int i = 0; i < fifthLayer.length; i++) {
	                if (fifthLayer[i] != null) {
	                    fifthLayer[i].drawOverlayUI(g2);
	                }
	            }
	            builds = buildingM.getBuildings();
	            for (int i = 0; i < builds.length; i++) {
	                if (builds[i] != null) {
	                    builds[i].drawOverlayUI(g2);
	                }
	            }
		        
		        world.drawFilters(g2);
		        world.drawWeather(g2);
        }
        
        
        if(currentState == mapBuildState) {
        	mapM.draw(g2, player.xDiff, player.yDiff);
        	mapM.drawForeground(g2, player.xDiff, player.yDiff);
        	
        	Building[] bottomLayer = buildingM.getBottomLayer();
	        for(int i = 0; i < bottomLayer.length-1; i++) {
	        	if(bottomLayer[i] != null) {
	        		bottomLayer[i].draw(g2);
	        	}
	        }
	        Building[] middleLayer = buildingM.getSecondLayer();
	        for(int i = 0; i < middleLayer.length-1; i++) {
	        	if(middleLayer[i] != null) {
	        		middleLayer[i].draw(g2);
	        	}
	        }
        	
        	Building[] main = buildingM.getBuildingsToDraw();
	        for(int i = 0; i < main.length-1; i++) {
	        	if(main[i] != null) {
	        		main[i].draw(g2);
	        	}
	        }
        	
        	Building[] secondLayer = buildingM.getThirdLayer();
	        for(int i = 0; i < secondLayer.length-1; i++) {
	        	if(secondLayer[i] != null) {
	        		secondLayer[i].draw(g2);
	        	}
	        }
	        Building[] thirdLayer = buildingM.getFourthLayer();
	        for(int i = 0; i < thirdLayer.length-1; i++) {
	        	if(thirdLayer[i] != null) {
	        		thirdLayer[i].draw(g2);
	        	}
	        }
	        Building[] fourthLayer = buildingM.getFifthLayer();
	        for(int i = 0; i < fourthLayer.length-1; i++) {
	        	if(fourthLayer[i] != null) {
	        		fourthLayer[i].draw(g2);
	        	}
	        }
        	
        	mapB.draw(g2);
        } 
        if(currentState == customiseRestaurantState) {
        	customiser.draw(g2);
        }
        
        gui.draw(g2);
        
        g2.dispose();
    }


}

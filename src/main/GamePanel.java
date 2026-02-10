package main;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import entity.PlayerMP;
import entity.buildings.Building;
import entity.buildings.BuildingManager;
import entity.items.Item;
import entity.items.ItemManager;
import entity.npc.NPC;
import entity.npc.NPCManager;
import main.renderer.AssetPool;
import main.renderer.DebugDraw;
import main.renderer.GLSLCamera;
import main.renderer.Renderer;
import map.Customiser;
import map.LightingManager;
import map.MapBuilder;
import map.MapManager;
import map.particles.ParticleSystem;
import net.ClientHandler;
import net.DiscoveryManager;
import net.GameClient;
import net.GameServer;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import utility.BuildingRegistry;
import utility.Catalogue;
import utility.Debug;
import utility.GUI;
import utility.ItemRegistry;
import utility.ProgressManager;
import utility.RecipeManager;
import utility.RoomHelperMethods;
import utility.Settings;
import utility.UpgradeManager;
import utility.GameManager;
import utility.cutscene.CutsceneManager;
import utility.minigame.MiniGameManager;
import utility.save.SaveManager;


public class GamePanel {

    private long window;
	private String title;
	public GLSLCamera camera;
	private DebugDraw debugDraw;
	public Renderer renderer;
    
	private long audioContext;
	private long audioDevice;
	
    private final int scale = 3; //The scale factor the tiles will be multiplied by, to make the game more visible
    private final int baseTileSize = 16; // The base size of each tile
    public final int tileSize = baseTileSize * scale; //The drawn size of each tile

    // SCREEN SIZE
    public int tilesInWidth = 24; //The number of tiles in width and in height
    public int tilesInHeight = 16;
    public int frameWidth = tilesInWidth * tileSize; //The number of pixels in width and in height
    public int frameHeight = tilesInHeight * tileSize;
    
    public KeyListener keyL = new KeyListener(this);
    public MouseListener mouseL = new MouseListener(this);
    
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


    // SYSTEM initialising all classes and managers
    public List<PlayerMP> playerList;
    public Player player;
    public World world;
    public SaveManager saveM;
    public GUI gui;
    public Debug debug;

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
    public final int chooseSaveState = 14;
    public final int chooseUpgradeState = 15;
    public final int dialogueState = 16;
    public final int achievementState = 17;
    public final int recipeState = 18;
    public final int chatState = 19;
    public final int createWorldScreen = 20;
    public final int createJoinPlayerScreen = 21;
    
    Queue<String> textureQueue = new LinkedList<>();
    Queue<String> fontQueue = new LinkedList<>();
    private boolean texturesLoaded = false;
    private boolean fontsLoaded = false;
    private boolean managersInitialised = false;
    
    public int emissiveFbo;
    public int emissiveTextureId;
    
    public int sizeX, sizeY;
    
    public int sceneFbo, litFbo;
    public int sceneTextureId, litTextureId;
    public int bloomFbo1, bloomFbo2;
    public int bloomTex1, bloomTex2;
    public int godrayFbo, godrayTextureId;
    public int godrayProcessedFbo, godrayProcessedTextureId;
    
    private float shakeDuration = 0f;   // Remaining time for shake
    private float shakeIntensity = 0f; 
    
    public GamePanel() {
    	this.camera = new GLSLCamera(new Vector2f(0, 0), frameWidth, frameHeight);
    }
    public void startGame() {
    	if(!multiplayer) {
    		player = new Player(this, 48*10, 48*10, keyL, mouseL, "");
    	} else {
            player = new PlayerMP(this, 48*10, 48*10, keyL, mouseL, "");
    	}
        world = new World(this, false);
        world.startGame();
      	
        //cutsceneM.enterDestroyedRestaurant();
    }
    public void playSinglePlayer(int saveSlot, String playerName, String worldName, int selectedSkinNum, int selectedHairNum, int selectedHairStyleNum) {
    	saveM.currentSave = saveSlot;
    	player = new Player(this, 48*10, 48*10, keyL, mouseL, playerName);
    	currentState = playState;
    	saveM.loadGame(saveSlot);
    	
    	//SET STUFF DOWN HERE AFTER CREATING CLASSES
    	world.isServer = true;
    	world.progressM.worldName = worldName;
    	player.setUsername(playerName);
    	player.setSkin(selectedSkinNum);
      	player.setHair(selectedHairNum);
      	player.setHairStyle(selectedHairStyleNum);
    }
    public void hostServer(int saveSlot, String playerName, String worldName, int selectedSkinNum, int selectedHairNum, int selectedHairStyleNum) {

        if (serverHost) return;
        
        serverHost = true;

        playerList = new CopyOnWriteArrayList<>();
        
        player = new PlayerMP(this, 48*10, 48*10, keyL, mouseL, playerName);
        playerList.add((PlayerMP)player); // only local player

        // start server
        try {
			socketServer = new GameServer(this, worldName);
	        socketServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // connect client to self
        socketClient = new GameClient(this, "127.0.0.1", GameServer.GAME_PORT);
        socketClient.start();  
        
    	world.isServer = true;
    	player.setUsername(playerName);
    	world.progressM.worldName = worldName;
    	player.setSkin(selectedSkinNum);
      	player.setHair(selectedHairNum);
      	player.setHairStyle(selectedHairStyleNum);
      	
        socketClient.send(new Packet00Login(playerName,
            (int) player.hitbox.x,
            (int) player.hitbox.y,
            player.currentRoomIndex,
            selectedSkinNum, selectedHairStyleNum, selectedHairNum
        ));
        stopDiscovery();

        multiplayer = true;
        currentState = playState;
        
    	world.progressM.worldName = worldName;
    	saveM.loadGame(saveSlot);
        
     	world.isServer = true;
    	player.setUsername(playerName);
    	world.progressM.worldName = worldName;
    	player.setSkin(selectedSkinNum);
      	player.setHair(selectedHairNum);
      	player.setHairStyle(selectedHairStyleNum);
    }
    public void joinServer(String username, String ip, int port, int selectedSkinNum, int selectedHairNum, int selectedHairStyleNum) {

        if (joiningServer) return;
        joiningServer = true;

        playerList = new CopyOnWriteArrayList<>();

        player = new PlayerMP(this, 48 * 10, 48 * 10, keyL, mouseL, username);
        playerList.add((PlayerMP)player);

        socketClient = new GameClient(this, ip, port);
        socketClient.start();
        
    	player.setSkin(selectedSkinNum);
      	player.setHair(selectedHairNum);
      	player.setHairStyle(selectedHairStyleNum);

        socketClient.send(new Packet00Login(username,
            (int) player.hitbox.x,
            (int) player.hitbox.y,
            player.currentRoomIndex,
            selectedSkinNum, selectedHairStyleNum, selectedHairNum
        ));
        
        stopDiscovery();

        multiplayer = true;
        currentState = playState;
    }
    public PlayerMP findPlayer(String username) {
        for (PlayerMP player : playerList) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
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
    public void stopDiscovery() {
        if (discovery != null) {
            discovery.shutdown();
            discovery = null;
        }
    }
    public void disconnect() {
        try {
            if (socketClient != null) {
                socketClient.sendImmediately(
                    new Packet01Disconnect(player.getUsername())
                );

                socketClient.flush();   
                Thread.sleep(10);       
                socketClient.close();   // close AFTER send
            }
        } catch (Exception ignored) {}
    }
    public synchronized List<PlayerMP> getPlayerList() {
        return playerList;
    }
    public Player getPlayer(String username) {
        if (player != null && player.getUsername().equals(username)) {
            return player;
        }

        for (PlayerMP p : playerList) {
            if (p.getUsername().equals(username)) return p;
        }

        return null;
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
    public void createEmissiveBuffer(int width, int height) {
        emissiveFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, emissiveFbo);

        emissiveTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, emissiveTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                               GL_TEXTURE_2D, emissiveTextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("ERROR: Emissive FBO is incomplete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void createSceneBuffer(int width, int height) {
        sceneFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, sceneFbo);

        sceneTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, sceneTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                     width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                               GL_TEXTURE_2D, sceneTextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("ERROR: Scene FBO incomplete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void createLitBuffer(int width, int height) {
        litFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, litFbo);

        litTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, litTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                     width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                               GL_TEXTURE_2D, litTextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("ERROR: Lit FBO incomplete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void createBloomBuffers(int width, int height) {
        bloomFbo1 = glGenFramebuffers();
        bloomTex1 = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, bloomTex1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindFramebuffer(GL_FRAMEBUFFER, bloomFbo1);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, bloomTex1, 0);

        bloomFbo2 = glGenFramebuffers();
        bloomTex2 = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, bloomTex2);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindFramebuffer(GL_FRAMEBUFFER, bloomFbo2);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, bloomTex2, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void createGodrayBuffer(int width, int height) {
        godrayFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, godrayFbo);

        godrayTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, godrayTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                               GL_TEXTURE_2D, godrayTextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("ERROR: Godray FBO incomplete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void createGodrayProcessedBuffer(int width, int height) {
    	godrayProcessedFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, godrayProcessedFbo);

        godrayProcessedTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, godrayProcessedTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                               GL_TEXTURE_2D, godrayProcessedTextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("ERROR: Godray FBO incomplete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public int getPlayerMPIndex(String username) {
        for (int i = 0; i < getPlayerList().size(); i++) {
            if (username.equals(getPlayerList().get(i).getUsername())) {
                return i;
            }
        }
        return -1; // not found
    }
    private void createTextureList() {
        Path resFolder = Paths.get("res");

        try (Stream<Path> paths = Files.walk(resFolder)) {
            paths
                .filter(Files::isRegularFile)               // only files
                .filter(p -> p.toString().endsWith(".png")) // only PNGs
                .forEach(p -> {
                    // Make path relative to resFolder and prepend "/"
                    Path relativePath = resFolder.relativize(p);
                    textureQueue.add("/" + relativePath.toString().replace("\\", "/"));
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createFontList() {
        Path resFolder = Paths.get("res");

        try (Stream<Path> paths = Files.walk(resFolder)) {
            paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".ttf"))
                .forEach(p -> {
                    Path relative = resFolder.relativize(p); // remove leading "res/"
                    String clean = "/" + relative.toString().replace("\\", "/"); // ensure leading /
                    fontQueue.add(clean);
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        init();
        createTextureList();
        createFontList();
        loop();
        
		//Destroy audio context
		alcDestroyContext(audioContext);
		alcCloseDevice(audioDevice);
		
		//Free the memory
		glfwSetKeyCallback(window, null);
		glfwDestroyWindow(window);
		
		//Terminate GLFW
		glfwTerminate();
		glfwSetErrorCallback(null).free();
    }
    private void initialiseManagers() {            
            world = new World(this, false);
            world.initialiseManagers();
            saveM = new SaveManager(this);
            debug = new Debug(this);
            gui = new GUI(this);
            // -----------------------
            // Game Setup
            // -----------------------
            //playSinglePlayer(0);
    }

    public void screenShake(float duration, float intensity) {
        this.shakeDuration = duration;
        this.shakeIntensity = intensity;
        camera.shake(shakeIntensity, shakeDuration);
    }
    private void applyScreenShake(double dt) {
        // Reduce remaining shake time
        if (shakeDuration > 0) {
            shakeDuration -= dt;
            // Camera handles the actual shake offsets internally
        }
    }
    public void init() {
        // -----------------------
        // GLFW Setup
        // -----------------------
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_DEPTH_BITS, 0);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        this.title = "Pandemonia";
        // Create the window
        window = glfwCreateWindow(frameWidth, frameHeight, title, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
        
        glfwSetCursorPosCallback(window, mouseL::mousePosCallback);
        glfwSetMouseButtonCallback(window, mouseL::mouseButtonCallback);
        glfwSetScrollCallback(window, mouseL::mouseScrollCallback);
        glfwSetKeyCallback(window, keyL::keyCallback);
        GLFW.glfwSetCharCallback(window, keyL::charCallback);
        
        GLFW.glfwSetFramebufferSizeCallback(window, (win, fbWidth, fbHeight) -> {
            if (fbWidth == 0 || fbHeight == 0) return;

            // 1. OpenGL renders in physical pixels
            glViewport(0, 0, fbWidth, fbHeight);
            
            sizeX = fbWidth;
            sizeY = fbHeight;
            
            // 3. FBOs must match framebuffer resolution
            recreateBuffers(fbWidth, fbHeight);
        });

        // -----------------------
        // Make OpenGL context current
        // -----------------------
        glfwMakeContextCurrent(window);
        GL.createCapabilities(); // MUST be called after context is current

        // Enable V-sync
        glfwSwapInterval(1);
        
        // Show window
        glfwShowWindow(window);

        // -----------------------
        // OpenGL State
        // -----------------------
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST); // Now safe to call

        // Use the framebuffer size for the viewport, NOT the logical frameWidth/frameHeight
        int[] px = new int[1];
        int[] py = new int[1];

        glfwGetFramebufferSize(window, px, py);

        sizeX = px[0];
        sizeY = py[0];
        
        // correct viewport (physical pixels)
        glViewport(0, 0, px[0], py[0]);

        // correct camera / projection (logical pixels)
        camera.setSize(frameWidth, frameHeight);
        
        
         

        // -----------------------
        // FrameBuffer and Renderers
        // -----------------------
        this.renderer = new Renderer(this, camera);
        this.debugDraw = new DebugDraw(this);

        // -----------------------
        // Audio Initialization
        // -----------------------
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            throw new IllegalStateException("Audio library not supported.");
        }
        
        createEmissiveBuffer(px[0], py[0]);
        createSceneBuffer(px[0], py[0]);
        createBloomBuffers(px[0], py[0]);
        createLitBuffer(px[0], py[0]);
        createGodrayBuffer(px[0], py[0]);
        createGodrayProcessedBuffer(px[0], py[0]);
    }
    public void applyGameViewport(int fbWidth, int fbHeight) {
        float gameAspect = frameWidth / (float) frameHeight;
        float screenAspect = fbWidth / (float) fbHeight;

        int vpW, vpH, vpX, vpY;

        if (screenAspect > gameAspect) {
            // Pillarbox
            vpH = fbHeight;
            vpW = Math.round(fbHeight * gameAspect);
            vpX = (fbWidth - vpW) / 2;
            vpY = 0;
        } else {
            // Letterbox
            vpW = fbWidth;
            vpH = Math.round(fbWidth / gameAspect);
            vpX = 0;
            vpY = (fbHeight - vpH) / 2;
        }

        glViewport(vpX, vpY, vpW, vpH);
    }
    public void loop() {
    	double beginTime = glfwGetTime();
    	double endTime;
    	double dt = 0;
    	
		while(!glfwWindowShouldClose(window)) {
			//Poll Events
			glfwPollEvents();
			
			debugDraw.beginFrame();
			
			
			  int loadsPerFrame = 2;
		        // Load some textures
		        for (int i = 0; i < loadsPerFrame && !textureQueue.isEmpty(); i++) {
		            String file = textureQueue.poll();
		            AssetPool.getTexture(file);
		        }

		        
		        // Load some fonts
		        for (int i = 0; i < loadsPerFrame && !fontQueue.isEmpty(); i++) {
		            String font = fontQueue.poll();
		            AssetPool.getBitmapFont(font, 32);
		        }

		        // Check if done
		        if (!texturesLoaded && textureQueue.isEmpty()) {
		            texturesLoaded = true;
		        }
		        if (!fontsLoaded && fontQueue.isEmpty()) {
		        	fontsLoaded = true;
		        }

		        
		        if (!managersInitialised && texturesLoaded && fontsLoaded) {
		            managersInitialised = true;
		            initialiseManagers();
		        }

		        // -------------------------------
		        // 2. Game update & render
		        // -------------------------------
		        if (managersInitialised) {
		        	
					if(dt >= 0) {
						update(dt);
						
						int[] fbw = new int[1];
						int[] fbh = new int[1];
						glfwGetFramebufferSize(window, fbw, fbh);

						int w = fbw[0];
						int h = fbh[0];
						
						glBindFramebuffer(GL_FRAMEBUFFER, sceneFbo);
						applyGameViewport(w, h);
						glClearColor(0, 0, 0, 0);
						glClear(GL_COLOR_BUFFER_BIT);
				    	renderer.beginFrame();
						draw();
						renderer.endFrame();
						
			        	glBindFramebuffer(GL_FRAMEBUFFER, emissiveFbo);
			        	applyGameViewport(w, h);
			        	glClearColor(0, 0, 0, 0);
			        	glClear(GL_COLOR_BUFFER_BIT);
			        	renderer.beginFrame();
			        	drawEmissiveBuffers();
			        	renderer.endFrame();
			        	
			        	int finalTexture = sceneTextureId;
			        	if (Settings.fancyLighting) {
			        		glBindFramebuffer(GL_FRAMEBUFFER, litFbo);
			        		applyGameViewport(w, h);
			        		glClearColor(0, 0, 0, 0);
			        		glClear(GL_COLOR_BUFFER_BIT);
							renderer.updateLightsOncePerFrame();
							renderer.renderLightingPass();
							finalTexture = litTextureId;   
							if(Settings.bloomEnabled) {
							    renderer.applyAndDrawBloom(finalTexture, bloomFbo1, bloomFbo2, bloomTex1, bloomTex2);
							} else {
							    // Just draw the finalTexture to screen without bloom
							    glBindFramebuffer(GL_FRAMEBUFFER, 0);
							    applyGameViewport(w, h);
							    glClear(GL_COLOR_BUFFER_BIT);
							    
							    // Draw fullscreen quad with finalTexture
							    renderer.drawFullscreenTexture(litTextureId);
							}
							
							if(Settings.godraysEnabled) {
					        	glBindFramebuffer(GL_FRAMEBUFFER, godrayFbo);
					        	applyGameViewport(w, h);
					        	glClearColor(0, 0, 0, 0);
					        	glClear(GL_COLOR_BUFFER_BIT);
	
					        	renderer.beginFrame();
					        	drawGodRays();
					        	renderer.endFrame();
					        	
					        	renderer.renderGodRays(godrayTextureId, godrayProcessedFbo, 0.4f, 0.95f, 1.3f);
					        	
					        	glBindFramebuffer(GL_FRAMEBUFFER, 0);
					        	applyGameViewport(w, h);
					        	//renderer.drawFullscreenTexture(finalTexture); // scene
					        	glEnable(GL_BLEND);
					        	glBlendFunc(GL_ONE, GL_ONE);
					        	renderer.drawFullscreenTexture(godrayProcessedTextureId); // add god rays
					        	glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
							}
			        	} else {
			        		glBindFramebuffer(GL_FRAMEBUFFER, 0);
			        		applyGameViewport(w, h);
			        		glClear(GL_COLOR_BUFFER_BIT);
			        		renderer.drawFullscreenTexture(sceneTextureId);
			        	}
			        	
			        	//GUI
			        	glBindFramebuffer(GL_FRAMEBUFFER, 0);
			        	applyGameViewport(w, h);
		    	    	renderer.beginFrame();
			        	drawGUI();
				    	renderer.endFrame();
						debugDraw.draw();
					}
		        } else {
		            //drawLoadingScreen();
		        }
			
			keyL.endFrame();
			mouseL.endFrame();
			glfwSwapBuffers(window);
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		
	}
    public void setFullScreen() {


        // Get the primary monitor's resolution
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);

        // Resize window to match monitor size (windowed fullscreen)
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW_TRUE); // Keep borders
        GLFW.glfwSetWindowMonitor(window, 0, 0, 0, vidMode.width(), vidMode.height(), GLFW.GLFW_DONT_CARE);

        // Update your stored viewport
        sizeX = vidMode.width();
        sizeY = vidMode.height();
        mouseL.setGameViewportPos(new Vector2f(0, 0));
        mouseL.setGameViewportSize(new Vector2f(sizeX, sizeY));

        recreateBuffers(sizeX, sizeY);
        Settings.fullScreen = true;
    }

    public void stopFullScreen() {
        int windowedWidth = frameWidth;   // your saved windowed size
        int windowedHeight = frameHeight;
        int windowPosX = 100;             // your saved windowed position
        int windowPosY = 100;

        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW_TRUE);
        GLFW.glfwSetWindowPos(window, windowPosX, windowPosY);
        GLFW.glfwSetWindowSize(window, windowedWidth, windowedHeight);
        Settings.fullScreen = false;
    }

    // Helper method to recreate FBOs at the new resolution
    private void recreateBuffers(int width, int height) {
        createEmissiveBuffer(width, height);
        createSceneBuffer(width, height);
        createLitBuffer(width, height);
        createBloomBuffers(width, height);
        createGodrayBuffer(width, height);
        createGodrayProcessedBuffer(width, height);
    }
    public void resize(int width, int height) {
        // Update OpenGL viewport
        glViewport(0, 0, width, height);

        // Update camera projection
        //camera.resize(width, height);

        // If you have a lightmap / FBO:
        //renderer.resize(width, height);
    }
    private void update(double dt) {
    	
    	if(errorCounter > 0) {
    		errorCounter--;
    		if(errorCounter == 0) {
    			hostError = false;
    		}
    	}
        if(multiplayer) {
        	if(socketServer != null) {
        		socketServer.update();
        	}
        	if (socketClient != null) {
        		socketClient.update();
            }
        }
    	
        applyScreenShake(dt);
    	keyL.update();
    	camera.update(dt);
    	gui.update(dt);

	    	
	    world.serverUpdate(dt);
    	world.clientUpdate(dt);

    }

    // --------------------------
    // DRAW USING OPENGL
    // --------------------------
    private void draw() {
    	world.draw(renderer);
    }
    private void drawEmissiveBuffers() {
        world.drawEmissiveBuffers(renderer);
    }
    private void drawGUI() {
    	renderer.setGUI();
		        
		world.drawGUI(renderer);
		
    	renderer.setGUI();
		gui.draw(renderer);
    }
    private void drawGodRays() {
    	world.drawGodRays(renderer);
    }

}
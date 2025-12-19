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
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
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
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
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
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.net.BindException;
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
import java.util.Random;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
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
import net.DiscoveryManager;
import net.GameClient;
import net.GameServer;
import net.packets.Packet00Login;
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
import utility.World;
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

    public ArrayList<PlayerMP> playerList;

    // SYSTEM initialising all classes and managers
    public Player player;
    public BuildingManager buildingM;
    public ItemManager itemM;
    public NPCManager npcM;
    public MapManager mapM;
    public GUI gui;
    public ArrayList<Entity> updateEntityList;
    public ArrayList<Entity> entityList;
    public ItemRegistry itemRegistry;
    public BuildingRegistry buildingRegistry;
    public Catalogue catalogue;
    public World world;
    public LightingManager lightingM = new LightingManager(this, camera);
    public ParticleSystem particleM;
    public Customiser customiser;
    public PathFinder pathF;
    public RecipeManager recipeM;;
    public UpgradeManager upgradeM;
    public ProgressManager progressM;
    public MiniGameManager minigameM;
    public RoomHelperMethods roomH;
    public CutsceneManager cutsceneM;
    public SaveManager saveM;
    public MapBuilder mapB;
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

    private final Random random = new Random(); // Random generator for shake offsets
    
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
    
    public GamePanel() {
    	this.camera = new GLSLCamera(new Vector2f(0, 0), frameWidth, frameHeight);
    }
    public void startGame() {
    	player = new Player(this, 48*10, 48*10, keyL, mouseL, "");
        buildingM = new BuildingManager(this);
        itemM = new ItemManager(this);
        npcM = new NPCManager(this);
        mapM = new MapManager(this);
        updateEntityList = new ArrayList<>();
        entityList = new ArrayList<>();
        world = new World(this);
        lightingM = new LightingManager(this, camera);
        customiser = new Customiser(this);
        cutsceneM = new CutsceneManager(this);
        catalogue = new Catalogue(this);
        recipeM = new RecipeManager();
        upgradeM = new UpgradeManager(this);
        progressM = new ProgressManager(this);
      	
        //cutsceneM.enterDestroyedRestaurant();
    }
    public void playSinglePlayer(int saveSlot) {
    	saveM.currentSave = saveSlot;
    	player = new Player(this, 48*10, 48*10, keyL, mouseL, "");
    	currentState = playState;
    	saveM.loadGame(saveSlot);
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
        player = new PlayerMP(this, 48 * 10, 48 * 10, keyL, mouseL, username, null, -1);
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
            //mouseL.leftClickPressed = false;
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

        //player.mouseI.leftClickPressed = false;
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
        player = new PlayerMP(this, 48 * 10, 48 * 10, keyL, mouseL, username, null, -1);
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
        //player.mouseI.leftClickPressed = false;
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
    public void stopDiscovery() {
        if (discovery != null) {
            discovery.shutdown();
            discovery = null;
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
    
    public void movePlayer(String username, int x, int y, int currentAnimation, int direction) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = getPlayerList().get(index);
        player.hitbox.x = x;
        player.hitbox.y = y;
        player.setCurrentAnimation(currentAnimation);
        player.setDirection(direction);
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
            buildingM = new BuildingManager(this);
            itemM = new ItemManager(this);
            npcM = new NPCManager(this);
            mapM = new MapManager(this);
            gui = new GUI(this);
            updateEntityList = new ArrayList<>();
            entityList = new ArrayList<>();
            itemRegistry = new ItemRegistry(this);
            buildingRegistry = new BuildingRegistry(this);
            catalogue = new Catalogue(this);
            world = new World(this);
            particleM = new ParticleSystem(this);
            customiser = new Customiser(this);
            pathF = new PathFinder(this);
            recipeM = new RecipeManager();
            upgradeM = new UpgradeManager(this);
            progressM = new ProgressManager(this);
            minigameM = new MiniGameManager(this);
            roomH = new RoomHelperMethods();
            cutsceneM = new CutsceneManager(this);
            saveM = new SaveManager(this);
            mapB = new MapBuilder(this);
            debug = new Debug(this);
            // -----------------------
            // Game Setup
            // -----------------------
            playSinglePlayer(0);
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
        
        glfwSetWindowSizeCallback(window, (win, newWidth, newHeight) -> {
            try {
                setWidth(newWidth);
                setHeight(newHeight);

                // Update viewport
                int[] fbW = new int[1];
                int[] fbH = new int[1];
                glfwGetFramebufferSize(window, fbW, fbH);
                glViewport(0, 0, fbW[0], fbH[0]);

                // Update camera logical size
                camera.setSize(newWidth, newHeight);
            } catch (Throwable t) {
                t.printStackTrace();
            }
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
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
						glViewport(0, 0, w, h);
						glClearColor(0, 0, 0, 1);
						glClear(GL_COLOR_BUFFER_BIT);
				    	renderer.beginFrame();
						draw();
						renderer.endFrame();
						
			        	glBindFramebuffer(GL_FRAMEBUFFER, emissiveFbo);
			        	glViewport(0, 0, w, h);
			        	glClearColor(0, 0, 0, 0);
			        	glClear(GL_COLOR_BUFFER_BIT);
			        	renderer.beginFrame();
			        	drawEmissiveBuffers();
			        	renderer.endFrame();
			        	
			        	int finalTexture = sceneTextureId;
			        	if (Settings.fancyLighting) {
			        		glBindFramebuffer(GL_FRAMEBUFFER, litFbo);
			        		glClear(GL_COLOR_BUFFER_BIT);
							renderer.updateLightsOncePerFrame();
							renderer.renderLightingPass();
							finalTexture = litTextureId;   
							if(Settings.bloomEnabled) {
							    renderer.applyAndDrawBloom(finalTexture, bloomFbo1, bloomFbo2, bloomTex1, bloomTex2);
							} else {
							    // Just draw the finalTexture to screen without bloom
							    glBindFramebuffer(GL_FRAMEBUFFER, 0);
							    glViewport(0, 0, w, h);
							    glClear(GL_COLOR_BUFFER_BIT);
							    
							    // Draw fullscreen quad with finalTexture
							    renderer.drawFullscreenTexture(litTextureId);
							}
							
							if(Settings.godraysEnabled) {
					        	glBindFramebuffer(GL_FRAMEBUFFER, godrayFbo);
					        	glViewport(0, 0, sizeX, sizeY);
					        	glClearColor(0, 0, 0, 0);
					        	glClear(GL_COLOR_BUFFER_BIT);
	
					        	renderer.beginFrame();
					        	drawGodRays();
					        	renderer.endFrame();
					        	
					        	renderer.renderGodRays(godrayTextureId, godrayProcessedFbo, 0.4f, 0.95f, 1.3f);
					        	
					        	glBindFramebuffer(GL_FRAMEBUFFER, 0);
					        	glViewport(0, 0, w, h);
					        	//renderer.drawFullscreenTexture(finalTexture); // scene
					        	glEnable(GL_BLEND);
					        	glBlendFunc(GL_ONE, GL_ONE);
					        	renderer.drawFullscreenTexture(godrayProcessedTextureId); // add god rays
					        	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
							}
			        	} else {
			        		glBindFramebuffer(GL_FRAMEBUFFER, 0);
			        		glViewport(0, 0, w, h);
			        		glClear(GL_COLOR_BUFFER_BIT);
			        		renderer.drawFullscreenTexture(sceneTextureId);
			        	}
	
			        	//GUI
			        	glBindFramebuffer(GL_FRAMEBUFFER, 0);
		        		glViewport(0, 0, w, h);
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
	public int getWidth() {
		return frameWidth;
	}
	public int getHeight() {
		return frameHeight;
	}
	public int getFullScreenWidth() {
		return 2560;
	}
	public int getFullScreenHeight() {
		return 1600;
	}
	public void setWidth(int newWidth) {
		frameWidth = newWidth;
	}
	public void setHeight(int newHeight) {
		frameHeight = newHeight;
	}
    private void update(double dt) {
    	
    	if(errorCounter > 0) {
    		errorCounter--;
    		if(errorCounter == 0) {
    			hostError = false;
    		}
    	}
    	
    	keyL.update();
    	

	    	if(currentState == playState || currentState == customiseRestaurantState || currentState == catalogueState || currentState == xpState) {
		    	
		    	if(multiplayer) {
		    		for(Player p: getPlayerList()) {
		    			if(p.getUsername() != player.getUsername()) {
		    				if(p.currentRoomIndex == player.currentRoomIndex) {
		    					p.updateInteractHitbox();
		    				}
		    			}
		    		}
		    	}
		    		
		    	player.update(dt);
		    	mapM.update(dt);
		    	buildingM.update(dt);
		    	npcM.update(dt);
		    	itemM.update(dt);
		    	world.update(dt);
    			cutsceneM.update(dt);
		    	lightingM.update(dt);
		    	minigameM.update(dt);
		        //if(keyI.debugMode) {
		        	//debug.update(dt);
		        //}
		    	if(currentState == customiseRestaurantState) {
		    		customiser.update(dt);
		    	}
	    	} else if(currentState == mapBuildState) {
	    		//mapB.update(dt);
	    		buildingM.update(dt);
	    	}
    		catalogue.update(dt);
    		gui.update(dt);
	    	particleM.update(dt);

    }

    // --------------------------
    // DRAW USING OPENGL
    // --------------------------
    private void draw() {
        
        if(currentState == playState || currentState == pauseState || currentState == achievementState || currentState == settingsState || currentState == customiseRestaurantState || currentState == xpState || currentState == dialogueState) {
        		mapM.draw(renderer);	 
        		
        		Building[] bottomLayer = buildingM.getBottomLayer();
    	        for(int i = 0; i < bottomLayer.length-1; i++) {
    	        	if(bottomLayer[i] != null) {
    	        		bottomLayer[i].draw(renderer);
    	        	}
    	        }
    	        
    	        Building[] secondLayer = buildingM.getSecondLayer();
    	        for(int i = 0; i < secondLayer.length-1; i++) {
    	        	if(secondLayer[i] != null) {
    	        		secondLayer[i].draw(renderer);
    	        	}
    	        }
        		
    	        if(multiplayer) {
	        		for (int i = 0; i < getPlayerList().size(); i++) {
	    	            if (getPlayerList().get(i) != null) {
	    	            	Player p = getPlayerList().get(i);
	    	            	if(p != player) {
	    	            		//p.xDiff = player.xDiff;
	    	            		//p.yDiff = player.yDiff;
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
		        		builds[i].drawEmissive(renderer);
		        	}
		        }
		        Item[] itemsInBuildings = buildingM.getBuildingItems();
		        for(int i = 0; i < itemsInBuildings.length-1; i++) {
		        	if(itemsInBuildings[i] != null) {
		        		entityList.add(itemsInBuildings[i]);
		        	}
		        }
		        List<NPC> copy = new ArrayList<NPC>(npcM.getNPCs());
		        
		        for(NPC npc: copy) {
		        	if(npc != null) {
		        		entityList.add(npc);
		        	}
		        }
	        	
	        	//SORT
	    		ArrayList<Entity> listCopy = new ArrayList<Entity>(entityList);

	            Collections.sort(listCopy, new Comparator<Entity>() {
	                //@Override
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
		            	if(a.isOnScreen(camera, frameWidth, frameHeight)) {
		            		a.draw(renderer);
		            	}
	            	}
	            }
	            //EMPTY ENTITY LIST
	            entityList.clear();
	            
	            Building[] thirdLayer = buildingM.getThirdLayer();
		        for(int i = 0; i < thirdLayer.length-1; i++) {
		        	if(thirdLayer[i] != null) {
		        		entityList.add(thirdLayer[i]);
		        	}
		        }
		        
			       listCopy = new ArrayList<Entity>(entityList);

		            Collections.sort(listCopy, new Comparator<Entity>() {
		                //@Override
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
		            		if(a.isOnScreen(camera, frameWidth, frameHeight)) {
			            		a.draw(renderer);
			            	}
		            	}
		            }
		            //EMPTY ENTITY LIST
		            entityList.clear();
		        
		        List<Item> items = new ArrayList<Item>(itemM.getItems());
		        for(Item item: items) {
		        	if(item != null) {
		        		item.draw(renderer);
		        	}
		        }
		    
		        Building[] fourthLayer = buildingM.getFourthLayer();
		        for(int i = 0; i < fourthLayer.length-1; i++) {
		        	if(fourthLayer[i] != null) {
		        		fourthLayer[i].draw(renderer);
		        	}
		        }
		        
		        
		        Building[] fifthLayer = buildingM.getFifthLayer();
		        for(int i = 0; i < fifthLayer.length-1; i++) {
		        	if(fifthLayer[i] != null) {
		        		entityList.add(fifthLayer[i]);
		        	}
		        }
		        
		        
		       listCopy = new ArrayList<Entity>(entityList);

	            Collections.sort(listCopy, new Comparator<Entity>() {
	                //@Override
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
		            	if(a.isOnScreen(camera, frameWidth, frameHeight)) {
		            		a.draw(renderer);
		            	}
	            	}
	            }
	            //EMPTY ENTITY LIST
	            entityList.clear();
		        
	            mapM.drawForeground(renderer);
		        
	            
	            particleM.draw(renderer);
        		}
	            /*
		        if (Settings.fancyLighting) {
		            Texture litFull = lightingM.applyLighting(colorBuffer, emissiveBuffer, xDiff, yDiff);
		            
		            int bufferCamX = Math.round(camera.x );
		            int bufferCamY = Math.round(camera.y );
		            int srcW = Math.max(1, (int) Math.round(frameWidth / camera.zoom));
		            int srcH = Math.max(1, (int) Math.round(frameHeight / camera.zoom));

		            int srcX = bufferCamX - srcW / 2;
		            int srcY = bufferCamY - srcH / 2;
		            
		            int lightingScale = 3;

		            // snap to lighting buffer's pixel grid
		            srcX = Math.round(srcX / (float) lightingScale) * lightingScale;
		            srcY = Math.round(srcY / (float) lightingScale) * lightingScale;

		            // clamp to litFull edges
		            srcX = Math.max(0, Math.min(srcX, litFull.getWidth() - srcW));
		            srcY = Math.max(0, Math.min(srcY, litFull.getHeight() - srcH));

		            Texture viewSub = litFull.getSubimage(srcX, srcY, srcW, srcH);
		            renderer.draw(viewSub, 0, 0, frameWidth, frameHeight, null);
		        } else {
		        	int bufferCamX = Math.round(camera.x );
		        	int bufferCamY = Math.round(camera.y );
		        	int srcW = Math.max(1, (int) Math.round(frameWidth / camera.zoom));
		        	int srcH = Math.max(1, (int) Math.round(frameHeight / camera.zoom));

		        	int srcX = bufferCamX - srcW / 2;
		        	int srcY = bufferCamY - srcH / 2;

		        	// clamp to buffer edges
		        	if (srcX < 0) srcX = 0;
		        	if (srcY < 0) srcY = 0;
		        	if (srcX + srcW > colorBuffer.getWidth()) srcX = colorBuffer.getWidth() - srcW;
		        	if (srcY + srcH > colorBuffer.getHeight()) srcY = colorBuffer.getHeight() - srcH;

		        	Texture viewSub = colorBuffer.getSubimage(srcX, srcY, srcW, srcH);
		        	renderer.draw(viewSub, 0, 0, frameWidth, frameHeight, null);
		        }
		        */

    }
    private void drawEmissiveBuffers() {
        if(currentState == playState || currentState == pauseState || currentState == achievementState || currentState == settingsState || currentState == customiseRestaurantState || currentState == xpState || currentState == dialogueState) {
	    	Building[] builds = buildingM.getBuildingsToDraw();
	        for(int i = 0; i < builds.length-1; i++) {
	        	if(builds[i] != null) {
	        		entityList.add(builds[i]);
	        		builds[i].drawEmissive(renderer);
	        	}
	        }
    	
	        particleM.drawEmissive(renderer);
        }
    }
    private void drawGUI() {
		        //renderer.setGUI();
		        
		        if(currentState == playState || currentState == pauseState || currentState == achievementState || currentState == settingsState || currentState == customiseRestaurantState || currentState == xpState || currentState == dialogueState) {
		            Building[] thirdLayer = buildingM.getThirdLayer();
		        	for (int i = 0; i < thirdLayer.length; i++) {
		            if (thirdLayer[i] != null) {
		                thirdLayer[i].drawOverlayUI(renderer);
		            }
		        }
		        	
			    Building[] fourthLayer = buildingM.getFourthLayer();
		        for (int i = 0; i < fourthLayer.length; i++) {
		            if (fourthLayer[i] != null) {
		                fourthLayer[i].drawOverlayUI(renderer);
		            }
		        }
		
		        Building[] fifthLayer = buildingM.getFifthLayer();
		        for (int i = 0; i < fifthLayer.length; i++) {
		            if (fifthLayer[i] != null) {
		                fifthLayer[i].drawOverlayUI(renderer);
		            }
		        }

		        Building[] builds = buildingM.getBuildings();
		        for (int i = 0; i < builds.length; i++) {
		            if (builds[i] != null) {
		                builds[i].drawOverlayUI(renderer);
		            }
		        }
		        
		        world.drawFilters(renderer);
		        world.drawWeather(renderer); 
		        cutsceneM.draw(renderer);
		}
		
		
		if(currentState == mapBuildState) {
			mapM.draw(renderer);
			mapM.drawForeground(renderer);
			
			Building[] bottomLayer = buildingM.getBottomLayer();
		    for(int i = 0; i < bottomLayer.length-1; i++) {
		    	if(bottomLayer[i] != null) {
		    		bottomLayer[i].draw(renderer);
		    	}
		    }
		    Building[] middleLayer = buildingM.getSecondLayer();
		    for(int i = 0; i < middleLayer.length-1; i++) {
		    	if(middleLayer[i] != null) {
		    		middleLayer[i].draw(renderer);
		    	}
		    }
			
			Building[] main = buildingM.getBuildingsToDraw();
		    for(int i = 0; i < main.length-1; i++) {
		    	if(main[i] != null) {
		    		main[i].draw(renderer);
		    	}
		    }
			
			Building[] secondLayer = buildingM.getThirdLayer();
		    for(int i = 0; i < secondLayer.length-1; i++) {
		    	if(secondLayer[i] != null) {
		    		secondLayer[i].draw(renderer);
		    	}
		    }
		    Building[] thirdLayer = buildingM.getFourthLayer();
		    for(int i = 0; i < thirdLayer.length-1; i++) {
		    	if(thirdLayer[i] != null) {
		    		thirdLayer[i].draw(renderer);
		    	}
		    }
		    Building[] fourthLayer = buildingM.getFifthLayer();
		    for(int i = 0; i < fourthLayer.length-1; i++) {
		    	if(fourthLayer[i] != null) {
		    		fourthLayer[i].draw(renderer);
		    	}
		    }
			
			mapB.draw(renderer);
		} 
		if(currentState == customiseRestaurantState) {
			customiser.draw(renderer);
		}
		//renderer.setGUI();
		
		minigameM.draw(renderer);
		gui.draw(renderer);
    }
    private void drawGodRays() {
    	Building[] main = buildingM.getBuildingsToDraw();
	    for(int i = 0; i < main.length-1; i++) {
	    	if(main[i] != null) {
	    		main[i].drawGodRay(renderer);
	    	}
	    }
    }

}
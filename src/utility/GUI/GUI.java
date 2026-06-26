package utility.GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

import org.lwjgl.glfw.GLFW;

import entity.Player;
import entity.items.Food;
import entity.items.Plate;
import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import net.DiscoveryManager.DiscoveredServer;
import net.packets.Packet04Chat;
import utility.Achievement;
import utility.Constants;
import utility.ProgressManager.RewardType;
import utility.Settings;
import utility.Statistics;
import utility.Upgrade;
import utility.UpgradeManager;
import utility.Weather;
import utility.recipe.CookStep;
import utility.recipe.IngredientScore;
import utility.recipe.Order;
import utility.recipe.PreparedIngredient;
import utility.recipe.Recipe;
import utility.recipe.RecipeIngredient;
import utility.recipe.RecipeManager;
import utility.recipe.RecipeRenderData;

public class GUI {
	
	GamePanel gp;
	
	//IMAGES
    private TextureRegion recipeBorder, recipeBorder2, foodStepBorder, timeHeader, timeFrame, coinImage, mysteryOrder, cursedRecipeBorder, starLevel, emptyStar;
    private TextureRegion morning, day, evening, night, rain, thunder, cloudy;
    private TextureRegion[][] titleBookAnimations;
    private TextureRegion titleBackground;
    private TextureRegion highlight1, highlight2, highlight3, highlight4, underline, uncheckedBox, checkedBox, uncheckedBox2, checkedBox2;
    private TextureRegion settingsFrame, generalSettingsFrame, videoSettingsFrame, audioSettingsFrame, multiplayerSettingsFrame;
    private TextureRegion[] computerAnimations;
    private TextureRegion shoppingUI, shoppingButtonUI, leftArrow, rightArrow, basketUI, basketButtons;
    private TextureRegion leftProgress1, leftProgress2, middleProgress1, middleProgress2, rightProgress1, rightProgress2;
    private TextureRegion saveBorder, deleteSave, leftTitleArrow1, leftTitleArrow2, rightTitleArrow1, rightTitleArrow2, leftArrow3, rightArrow3, leftArrow3Pressed, rightArrow3Pressed;
    private TextureRegion dialogueFrame;
    private TextureRegion bookLine1, bookLine1Start, bookLine1End, bookLine2, previewFrame, createCharacterFrame, previewCharacterFrame;
	private TextureRegion bookIcons[], bookOpen, bookClosed, recipeBookBorder, lockedRecipeBorder;
	private TextureRegion achievementBorder, achievement, lockedAchievement, achievementPopup, mysteryIcon, mysteryCrateUI, catalogueButton, catalogueMenu;
    private TextureRegion  PanIcon, choppedIcon, PotIcon, ovenIcon, fryerIcon, freezerIcon, seasoningIcon, ovenTrayIcon, coatedIcon;
    private TextureRegion  sFrame, aFrame, bFrame, cFrame, dFrame, fFrame, gradingFrameOverlay;
	
	//COLOURS
	private Colour darkened;
	private Colour craftColour1;
    private Colour titleColour1, titleColour2;
    private Colour orderTextColour;
	
	//FONTS
	BitmapFont font;
	
	//COUNTERS
	public double clickCooldown = 0;
	private int titleAnimationCounter, titleAnimationSpeed, currentTitleAnimation;
	private double titleAnimationSpeedFactor;
	private int titlePageNum = -1;
	private int computerAnimationCounter;
	private double computerAnimationSpeed;
	
	//MESSAGES
	private List<GUIMessage> messages = new ArrayList<>();
	private List<GUIMessage> chatMessages = new ArrayList<>();
	private float chatScrollOffset = 0; // how far up the user has scrolled
	private final int maxMessages = 1000; // optional cap to avoid memory issues
	
	private boolean firstDraw = true;
	public boolean startLoading = false;
	private boolean singleplayerSelected = false;
	private boolean hostSelected = false;
	private boolean joinSelected = false;
	
	//RECIPE GRADING
	private Queue<RecipeGrading> gradingQueue = new LinkedList<>();
	
	//XP
	public int displayedSouls;     // what we currently draw
	public int targetSouls;        // where to animate to
	public boolean animatingSouls; // true while animating
	private int soulCounter = 0;
	
	//USERNAME
	public String username = "";
	public boolean usernameActive = false; // whether user is typing
	private boolean levelUp = false;
	
	private Map<Order, RecipeRenderData> renderCache = new HashMap<>();
	public Recipe[] recipeChoices;
	public Upgrade[] upgradeChoices;
	
	//SETTINGS STATES
	private int settingsState = 0;
	private final int baseSettings = 0;
	private final int generalState = 1;
	private final int videoState = 2;
	private final int audioState = 3;
	private final int multiplayerState = 4;
	//VIDEO SETTINGS
	private List<CheckBox> videoCheckBoxes;
	private int videoScrollIndex = 0;
	private final int VIDEO_PAGE_SIZE = 4;
	private final int spacing = 32; 
	
	//DIALOGUE
	private NPC currentTalkingNPC;
	private String currentDialogue;
	private int charIndex = 0;
	private long lastCharTime = 0;
	private String displayedText = "";
	private boolean finishedTyping = false;

	// Typing speed (milliseconds per character)
	private static final int CHAR_DELAY = 25;
	
	//SAVE
	private boolean doDestroySave;
	private int destroySaveNum;
	private int saveChosen;
	
	//ACHIEVEMENT
	private int currentPage = 0;  
	private int achievementStartIndex = 0; // which achievement is currently at the top
	private Achievement notificationAchievement = null;
	private float notificationTimer = 0f; // seconds
	private final float notificationDuration = 3f; // how long to show (3 seconds)
	private int notificationX, notificationY; // position to draw
	private int achievementPage = 0;
	private final int achievementsPerPage = 12;
	
	public boolean chatActive = true;
	
	public TextBox playerNameBox;
	public TextBox worldNameBox;
	public TextBox chatBox;
	public TextBox usernameBox;
	
	private int selectedSkinNum = 0;
	private int selectedHairNum = 0;
	private int selectedHairStyleNum = 0;
	private int selectedHatNum = 0;
	private int selectedCostumeNum = 0;
	
	private int titleUIScale = 5;
	
	//Recipe Grading
	private List<ActionAnim> animQueue = new ArrayList<>();
	private int animIndex = 0;
	private double animSpeed = 3.2; // tweak feel
	private boolean animating = true;
	private double finalGradeDisplay = 0;
	private boolean finalGradeShown = false;
	private RecipeGrading currentRecipe;
	private double duration = 2.25;
	private static final double[] SCORE_THRESHOLDS = {
		    0, 65, 75, 83, 90, 95, 100
		};
		private static final Colour[] SCORE_COLOURS = {
		    new Colour("#891956"), // F
		    new Colour("#d34461"), // D
		    new Colour("#ed7b6a"), // C
		    new Colour("#fbc17e"), // B
		    new Colour("#61ab6a"), // A
		    new Colour("#c960d4")  // S
		};
	
	public GUI(GamePanel gp) {
		this.gp = gp;
		
		darkened = new Colour(30, 30, 30, 200);
		craftColour1 = new Colour(232, 193, 112);
		craftColour1 = Colour.BLACK;
		titleColour1 = new Colour(29, 36, 39);
		//titleColour1 = Colour.BLACK;
	    titleColour2 = new Colour(87, 87, 87);
		titleColour2 = Colour.WHITE;
	    orderTextColour = Colour.BLACK;
        titleAnimationSpeedFactor = 4.0;
        
        font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
        
        int boxWidth  = 220;
        int boxHeight = 40;
        int centerX   = 110 + 60;

        playerNameBox = new TextBox(
            gp,
            centerX,
            200+40,
            boxWidth,
            boxHeight,
            font,
            titleColour1,
            20
        );

        worldNameBox = new TextBox(
            gp,
            centerX,
            280+40,
            boxWidth,
            boxHeight,
            font,
            titleColour1,
            20
        );
        
        boxWidth  = gp.frameWidth - 40;
        boxHeight = 50;
        int boxX = 20;
        int boxY = gp.frameHeight - boxHeight - 20;

        chatBox = new TextBox(
            gp,
            boxX,
            boxY,
            boxWidth,
            boxHeight,
            font,
            Colour.WHITE,
            1000
        );
        
        boxWidth  = 400;
        boxHeight = 50;
        boxX = gp.frameWidth / 2 - boxWidth / 2;
        boxY = 300;

        usernameBox = new TextBox(
            gp,
            boxX,
            boxY,
            boxWidth,
            boxHeight,
            font,
            titleColour1,
            20
        );
        
		importImages();
		initVideoSettings();
	}

	private void initVideoSettings() {
	    videoCheckBoxes = new ArrayList<>();
	    
		int x = gp.frameWidth/2 - ((112*3)/2) + 40;
		int y = gp.frameHeight/2 - ((112*3)/2) + 120+200;
	
	    videoCheckBoxes.add(new CheckBox(gp, x, y, 9*3, "FullScreen",
	        () -> Settings.fullScreen,
	        () -> {
	            Settings.fullScreen = !Settings.fullScreen;
	            if (Settings.fullScreen) gp.setFullScreen();
	            else gp.stopFullScreen();
	        }));
	    
	    
	    videoCheckBoxes.add(new CheckBox(gp, x, y+60, 9*3, "Fancy Lighting",
	        () -> Settings.fancyLighting,
	        () -> Settings.fancyLighting = !Settings.fancyLighting));
	
	    videoCheckBoxes.add(new CheckBox(gp, x, y+120, 9*3, "Bloom",
	        () -> Settings.bloomEnabled,
	        () -> Settings.bloomEnabled = !Settings.bloomEnabled));
	
	    videoCheckBoxes.add(new CheckBox(gp, x, y+180, 9*3, "God Rays",
	        () -> Settings.godraysEnabled,
	        () -> Settings.godraysEnabled = !Settings.godraysEnabled));
	    
	    videoCheckBoxes.add(new CheckBox(gp, x, y+240, 9*3, "Occlusion",
		        () -> Settings.occlusionEnabled,
		        () -> Settings.occlusionEnabled = !Settings.occlusionEnabled));
	    
	    videoCheckBoxes.add(new CheckBox(gp, x, y+300, 9*3, "Shadows",
		        () -> Settings.shadowsEnabled,
		        () -> Settings.shadowsEnabled = !Settings.shadowsEnabled));
	    videoCheckBoxes.add(new CheckBox(gp, x, y+360, 9*3, "Particles",
		        () -> Settings.particlesEnabled,
		        () -> Settings.particlesEnabled = !Settings.particlesEnabled));
	}
	protected Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
	
	private void importImages() {
		titleBackground = importImage("/UI/titlescreen/Backdrop.png").toTextureRegion();
		recipeBorder = importImage("/UI/recipe/orderBorder.png").toTextureRegion();
		recipeBorder2 = importImage("/UI/recipe/OrderBorder2.png").toTextureRegion();
		foodStepBorder = importImage("/UI/recipe/FoodStepBorder.png").toTextureRegion();
		timeHeader = importImage("/UI/weather/TimeBorder.png").toTextureRegion();
		timeFrame = importImage("/UI/weather/TimeBorder2.png").toTextureRegion();
		coinImage = importImage("/UI/Coin.png").toTextureRegion();
		mysteryOrder = importImage("/UI/recipe/MysteryOrder.png").toTextureRegion();
		
		morning = importImage("/UI/weather/Weather.png").getSubimage(0, 0, 48, 48); 
		day = importImage("/UI/weather/Weather.png").getSubimage(48, 0, 48, 48); 
		evening = importImage("/UI/weather/Weather.png").getSubimage(48*2, 0, 48, 48); 
		night = importImage("/UI/weather/Weather.png").getSubimage(48*3, 0, 48, 48); 
		cloudy = importImage("/UI/weather/Weather.png").getSubimage(48*4, 0, 48, 48); 
		rain = importImage("/UI/weather/Weather.png").getSubimage(48*5, 0, 48, 48); 
		thunder = importImage("/UI/weather/Weather.png").getSubimage(48*6, 0, 48, 48); 
		
		
		titleBookAnimations = new TextureRegion[10][10];
		titleBookAnimations[0] = importFromSpriteSheet("/UI/titlescreen/Opening.png", 7, 1, 0, 0, 240, 160); 
		titleBookAnimations[1] = importFromSpriteSheet("/UI/titlescreen/FlipPageRight.png", 7, 1, 0, 0, 240, 160); 
		titleBookAnimations[2] = importFromSpriteSheet("/UI/titlescreen/FlipPageLeft.png", 7, 1, 0, 0, 240, 160); 
		leftTitleArrow1 = importImage("/UI/titlescreen/Arrows.png").getSubimage(0, 0, 16, 16); 
		leftTitleArrow2 = importImage("/UI/titlescreen/Arrows.png").getSubimage(0, 16, 16, 16); 
		rightTitleArrow1 = importImage("/UI/titlescreen/Arrows.png").getSubimage(16, 0, 16, 16); 
		rightTitleArrow2 = importImage("/UI/titlescreen/Arrows.png").getSubimage(16, 16, 16, 16); 
		
		computerAnimations = importFromSpriteSheet("/UI/catalogue/Computer.png", 10, 1, 0, 0, 260, 190); 
		shoppingUI = importImage("/UI/catalogue/Shopping.png").getSubimage(0, 190, 260, 190);
		shoppingButtonUI = importImage("/UI/catalogue/Shopping.png").getSubimage(0, 190*2, 260, 190);
		basketUI = importImage("/UI/catalogue/Checkout.png").getSubimage(0, 190, 260, 190);
		basketButtons = importImage("/UI/catalogue/Checkout.png").getSubimage(0, 190*2, 260, 190);
		leftArrow = importImage("/UI/catalogue/ArrowButton.png").getSubimage(11, 0, 11, 11);
		rightArrow = importImage("/UI/catalogue/ArrowButton.png").getSubimage(0, 0, 11, 11);
		
		settingsFrame = importImage("/UI/settings/SettingsFrame.png").getSubimage(0, 0, 112, 112);
		generalSettingsFrame = importImage("/UI/settings/SettingsFrame.png").getSubimage(112, 0, 112, 112);
		videoSettingsFrame = importImage("/UI/settings/SettingsFrame.png").getSubimage(112*2, 0, 112, 112);
		audioSettingsFrame = importImage("/UI/settings/SettingsFrame.png").getSubimage(112*3, 0, 112, 112);
		multiplayerSettingsFrame = importImage("/UI/settings/SettingsFrame.png").getSubimage(112*4, 0, 112, 112);
		highlight1 = importImage("/UI/settings/Highlight1.png").toTextureRegion();
		highlight2 = importImage("/UI/settings/Highlight2.png").toTextureRegion();
		highlight3 = importImage("/UI/settings/Highlight3.png").toTextureRegion();
		highlight4 = importImage("/UI/settings/Highlight4.png").toTextureRegion();
		uncheckedBox = importImage("/UI/settings/CheckBox.png").getSubimage(0, 0, 9, 9);
		checkedBox = importImage("/UI/settings/CheckBox.png").getSubimage(9, 0, 9, 9);
		uncheckedBox2 = importImage("/UI/settings/CheckBox2.png").getSubimage(0, 0, 9, 9);
		checkedBox2 = importImage("/UI/settings/CheckBox2.png").getSubimage(9, 0, 9, 9);
		underline = importImage("/UI/settings/Underline.png").toTextureRegion();
		
		leftProgress1 = importImage("/UI/levels/LeftProgress.png").getSubimage(0, 0, 46/2, 20);	
		leftProgress2 = importImage("/UI/levels/LeftProgress.png").getSubimage(46/2, 0, 46/2, 20);	
		middleProgress1 = importImage("/UI/levels/MiddleProgress.png").getSubimage(0, 0, 6, 20);	
		middleProgress2 = importImage("/UI/levels/MiddleProgress.png").getSubimage(6, 0, 6, 20);	
		rightProgress1 = importImage("/UI/levels/RightProgress.png").getSubimage(0, 0, 12, 20);	
		rightProgress2 = importImage("/UI/levels/RightProgress.png").getSubimage(12, 0, 12, 20);	

		cursedRecipeBorder = importImage("/UI/recipe/HauntedOrderBorder.png").toTextureRegion();
		starLevel = importImage("/UI/recipe/Star.png").getSubimage(0, 0, 16, 16);	
		emptyStar = importImage("/UI/recipe/Star.png").getSubimage(32, 0, 16, 16);	
		saveBorder = importImage("/UI/saves/SaveUI.png").toTextureRegion();
		deleteSave = importImage("/UI/saves/DeleteSave.png").toTextureRegion();
		
		dialogueFrame = importImage("/UI/customise/CustomiseFrame.png").toTextureRegion();
		bookIcons = new TextureRegion[4];
		bookIcons[0] = importImage("/UI/menu/Icons.png").getSubimage(0, 48, 16, 16);
		bookIcons[1] = importImage("/UI/menu/Icons.png").getSubimage(16, 48, 16, 16);
		bookIcons[2] = importImage("/UI/menu/Icons.png").getSubimage(32, 48, 16, 16);
		bookIcons[3] = importImage("/UI/menu/Icons.png").getSubimage(48, 48, 16, 16);
		bookOpen = importImage("/UI/titlescreen/Opening.png").getSubimage(240*6, 0, 240, 160);
		bookClosed = importImage("/UI/Book_Closed.png").toTextureRegion();
		recipeBookBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(0, 0, 20, 20);
		lockedRecipeBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(20, 0, 20, 20);
		
		achievementBorder = importImage("/UI/achievement/Achievements.png").toTextureRegion();
		achievement = importImage("/UI/achievement/AchievementUI.png").toTextureRegion();
		lockedAchievement = importImage("/UI/achievement/LockedAchievement.png").toTextureRegion();
		achievementPopup = importImage("/UI/achievement/AchievementPopup.png").toTextureRegion();
		mysteryIcon = importImage("/UI/catalogue/MysteryCrate.png").toTextureRegion();
		mysteryCrateUI = importImage("/UI/catalogue/MysteryCrateUI.png").toTextureRegion();
		catalogueButton = importImage("/UI/catalogue/CatalogueButton.png").toTextureRegion();
		catalogueMenu = importImage("/UI/catalogue/CatalogueMenu.png").toTextureRegion();
		
		bookLine1 = importImage("/UI/titlescreen/Lines.png").getSubimage(16, 16, 16, 16);
		bookLine1Start = importImage("/UI/titlescreen/Lines.png").getSubimage(0, 16, 16, 16);
		bookLine1End = importImage("/UI/titlescreen/Lines.png").getSubimage(32, 16, 16, 16);
		
		bookLine2 = importImage("/UI/titlescreen/Lines.png").getSubimage(0, 0, 48, 16);
		previewFrame = importImage("/UI/PreviewFrame.png").toTextureRegion();
		createCharacterFrame= importImage("/UI/saves/CharacterCreateFrame.png").toTextureRegion();
		leftArrow3 = importImage("/UI/saves/ArrowButton1.png").getSubimage(0, 16, 16, 16);
		rightArrow3 = importImage("/UI/saves/ArrowButton1.png").getSubimage(32, 16, 16, 16);
		leftArrow3Pressed = importImage("/UI/saves/ArrowButton1.png").getSubimage(0+48, 16, 16, 16);
		rightArrow3Pressed = importImage("/UI/saves/ArrowButton1.png").getSubimage(32+48, 16, 16, 16);
		
		previewCharacterFrame = importImage("/UI/saves/PreviewCharacterFrame.png").toTextureRegion();
		
	    PanIcon = importImage("/UI/recipe/Icons.png").getSubimage(0, 0, 16, 16);
	    choppedIcon = importImage("/UI/recipe/Icons.png").getSubimage(32, 0, 16, 16);
	        PotIcon = importImage("/UI/recipe/Icons.png").getSubimage(16, 0, 16, 16);
	        ovenIcon = importImage("/UI/recipe/Icons.png").getSubimage(48, 0, 16, 16);
	        fryerIcon = importImage("/UI/recipe/Icons.png").getSubimage(64, 0, 16, 16);
	        freezerIcon = importImage("/UI/recipe/Icons.png").getSubimage(80, 0, 16, 16);
	        seasoningIcon = importImage("/UI/recipe/Icons.png").getSubimage(96, 0, 16, 16);
	        ovenTrayIcon = importImage("/UI/recipe/Icons.png").getSubimage(112, 0, 16, 16);
	        coatedIcon = importImage("/food/coating/BreadCrumbs.png").getSubimage(0, 0, 16, 16);
	    sFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(64*5, 0, 64, 16);
	    aFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(64*4, 0, 64, 16);
	    bFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(64*3, 0, 64, 16);
	    cFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(64*2, 0, 64, 16);
	    dFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(64, 0, 64, 16);
	    fFrame = importImage("/UI/recipe/grading/Frame.png").getSubimage(0, 0, 64, 16);
	    gradingFrameOverlay = importImage("/UI/recipe/grading/Frame.png").getSubimage(64*6, 0, 64, 16);
	}
	private TextureRegion createHorizontalFlipped(TextureRegion original) {
	        // Swap U coordinates (flip horizontally)
	        float u0 = original.u1;
	        float u1 = original.u0;

	        // Keep V coordinates the same
	        float v0 = original.v0;
	        float v1 = original.v1;

	        return new TextureRegion(original.texture, u0, v0, u1, v1);
	}
	protected TextureRegion[] importFromSpriteSheet(String filePath, int columnNumber, int rowNumber, int startX, int startY, int width, int height) {
	    TextureRegion animations[] = new TextureRegion[20];
	    int arrayIndex = 0;

	    Texture sheetTexture = AssetPool.getTexture(filePath);
	    int sheetWidth = sheetTexture.getWidth();
	    int sheetHeight = sheetTexture.getHeight();

	    // LOOP ORDER IDENTICAL TO BufferedImage
	    for (int i = 0; i < columnNumber; i++) {      // columns outer
	        for (int j = 0; j < rowNumber; j++) {     // rows inner
	            int px = i * width + startX;
	            int py = j * height + startY;

	            float u0 = px / (float) sheetWidth;
	            float u1 = (px + width) / (float) sheetWidth;

	            // Flip V-axis to match BufferedImage top-left origin
	            float v0 = 1.0f - (py + height) / (float) sheetHeight;
	            float v1 = 1.0f - py / (float) sheetHeight;

	            animations[arrayIndex] = new TextureRegion(sheetTexture, u0, v0, u1, v1);
	            arrayIndex++;
	        }
	    }

	    return animations;
	}
    public int getXforCenteredText(String text, BitmapFont font) {
	    float textWidth = font.getTextWidth(text);
	    return (int)(gp.frameWidth / 2f - textWidth / 2f);
	}
	private int getTextWidth(String text, BitmapFont font) {
        return (int)font.getTextWidth(text);
    }
	private int getTextHeight(BitmapFont font) {
        return (int)font.getLineHeight();
    }
	private boolean isHovering(String text, int x1, int y1, BitmapFont font) {
		
		int x = (int)gp.mouseL.getScreenX();
		int y = (int)gp.mouseL.getScreenY();
		
		if(x > x1 && x < x1 + getTextWidth(text, font) && y > y1 && y < y1 + getTextHeight(font)) {
			return true;
		}
		return false;
	}
	private boolean isHovering(int x1, int y1, int w, int h) {
		
		int x = (int)gp.mouseL.getScreenX();
		int y = (int)gp.mouseL.getScreenY();
		
		if(x > x1 && x < x1 + w && y > y1 && y < y1 + h) {
			return true;
		}
		return false;
	}
	
	private void drawTitleScreen(Renderer renderer) {
		
		renderer.draw(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5));
		
		titleAnimationSpeed++;
		if(titleAnimationSpeed >= titleAnimationSpeedFactor) {
			titleAnimationSpeed = 0;
			titleAnimationCounter++;
		}
		
		if(titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
			titleAnimationCounter--;
			if(currentTitleAnimation == 0) {
				titlePageNum = 0;
			} else if(currentTitleAnimation == 2) {
				titlePageNum = 0;
			}
		}
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));

		
		if(titlePageNum == 0 || titleAnimationCounter >= 6) {
			
			String text = "Pandemonia";
			int x = 180;
			int y = 140;
			drawUnderlinedText(renderer, text, x, y, 2.0f);
			
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			text = "Singleplayer";
				
			x = 180;
			y = 360;
				
			if(isHovering(text,x, y-24, font)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
						singleplayerSelected = true;
						gp.currentState = gp.chooseSaveState;
					}
				}
			}
				
				renderer.drawString(text, x, y);
				renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
				
				//MULTIPLAYER
				renderer.setColour(titleColour1);
				text = "Multiplayer";

				x = 180;
				y = 450;
				
				if(isHovering(text,x, y-24, font)) {
					renderer.setColour(titleColour2);
					if(gp.mouseL.mouseButtonDown(0)) {
						//ENTER MULTIPLAYER
						gp.currentState = gp.multiplayerSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				
				renderer.drawString(text, x, y);
				renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);

			
			
			//SETTINGS
			text = "Settings";
	
			y = 450+90;
			renderer.setColour(titleColour1);
			if(isHovering(text,x, y-24, font)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//ENTER SETTINGS
						//gp.currentState = gp.settingsState;
						clickCooldown = 0.33;
					}
				}
			}
			
			renderer.drawString(font, text, x, y);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
	
			//QUIT
			text = "Quit";
			
			y = 450+90+90;
			Colour c = titleColour1;
			if(isHovering(text,x, y-24, font)) {
				c  = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//QUIT GAME
						System.exit(0);
					}
				}
			}
			
			renderer.drawString(font, text, x, y, 1.0f, c);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
		}
		
	}
	private void drawRecipesScreen(Renderer renderer) {
		
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		TextureRegion img = bookOpen;
		renderer.draw(img, (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));
		
		
		int mouseX = (int)gp.mouseL.getScreenX();
		int mouseY = (int)gp.mouseL.getScreenY();
		
		int x = gp.frameWidth - 74;
		int y = 20;
		int i = 0;
		if(gp.currentState == gp.recipeState) {
			i = 1;
		}
		
		if(mouseX > x && mouseX < x+64 && mouseY > y && mouseY < y+64) {
			if(gp.currentState == gp.recipeState) {
				i = 3;
			} else {
				i = 2;
			}
			if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
				if(gp.currentState == gp.recipeState) {
					gp.currentState = gp.pauseState;
				} else {
					gp.currentState = gp.recipeState;
				}
				clickCooldown = 0.33;
			}
		}
		
		renderer.draw(bookIcons[i], x, y, 64, 64);
		
		int recipesPerSide = 12;  // 4 rows * 4 columns
		int recipesPerPage = 24;  // left + right page

		int startIndex = currentPage * recipesPerPage;
		int endIndex = startIndex + recipesPerPage;

		int leftX = 140;
		int rightX = 630;
		int startY = 120;

		int spacing = 32;
		int borderSize = 20*titleUIScale;
		int starSpacing = 16;
		int rowHeight = borderSize + 60;


		List<Recipe> all = RecipeManager.getAllRecipes();
		List<Recipe> unlocked = RecipeManager.getUnlockedRecipes();
		List<Recipe> ordered = new ArrayList<>();

		// Add unlocked first
		for (Recipe r : all) {
		    if (unlocked.contains(r))
		        ordered.add(r);
		}

		// Add locked (only those NOT unlocked)
		for (Recipe r : all) {
		    if (!unlocked.contains(r))
		        ordered.add(r);
		}

		// ------------------------------------
		// PAGE CALCULATION
		// ------------------------------------
		int maxPage = (ordered.size() - 1) / recipesPerPage;
		if (currentPage > maxPage) currentPage = maxPage;
		if (currentPage < 0) currentPage = 0;

		startIndex = currentPage * recipesPerPage;
		endIndex = startIndex + recipesPerPage;

		// ------------------------------------
		// DRAW CURRENT SPREAD
		// ------------------------------------
		x = leftX;
		y = startY;

		int printed = 0;  // how many drawn on this page

		for (int j = startIndex; j < ordered.size(); j++) {

		    // Stop when page is full
		    if (j >= endIndex) break;

		    Recipe recipe = ordered.get(j);
		    boolean isUnlocked = unlocked.contains(recipe);

		    // ------------------------------------
		    // DRAW RECIPE NAME
		    // ------------------------------------
		    if(isUnlocked) {
			    String name = recipe.getName();
			    int textWidth = (int)font.getTextWidth(name);
			    int textX = x + (borderSize - textWidth) / 2;
			    int textY = y - 6;
			    renderer.drawString(font, name, textX, textY, 1.0f, titleColour1);
		    }

		    // ------------------------------------
		    // BORDER
		    // ------------------------------------
		    if (isUnlocked)
		        renderer.draw(recipeBookBorder, x, y, borderSize, borderSize);
		    else
		        renderer.draw(lockedRecipeBorder, x, y, borderSize, borderSize);

		    // ------------------------------------
		    // PLATED IMAGE
		    // ------------------------------------
		    if (isUnlocked && recipe.finishedPlate != null) {
		        TextureRegion plate = recipe.finishedPlate;
		        int plateX = x + (borderSize - plate.getPixelWidth()) / 2;
		        int plateY = y + (borderSize - plate.getPixelHeight()) / 2;
		        renderer.draw(plate, plateX - 6*titleUIScale, plateY - 6*titleUIScale, 16*titleUIScale, 16*titleUIScale);
		    }

		    // ------------------------------------
		    // STARS
		    // ------------------------------------
		    if (isUnlocked) {
		        int stars = recipe.getStarLevel();
		        int starsY = y + borderSize + 4;

		        for (int k = 0; k < stars; k++) {
		            int starX = x + k * starSpacing;
		            //renderer.draw(starLevel, starX, starsY, 16, 16);
		        }
		    }

		    // ------------------------------------
		    // MOVE TO NEXT SLOT
		    // ------------------------------------
		    printed++;
		    x += borderSize + spacing;

		    // After 4 columns → new row
		    if (printed % 3 == 0) {
		        y += rowHeight;

		        // After 4 rows → switch to right page
		        if (printed == recipesPerSide) {
		            x = rightX;   // RIGHT PAGE START
		            y = startY;
		        } else {
		            // Reset X to correct side depending on count
		            x = (printed < recipesPerSide ? leftX : rightX);
		        }
		    }
		}
		
	}
	public void drawMultiplayerGameSettings(Renderer renderer) {
		
		renderer.draw(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5));
		
		titleAnimationSpeed++;
		if(titleAnimationSpeed >= titleAnimationSpeedFactor) {
			titleAnimationSpeed = 0;
			titleAnimationCounter++;
		}
		
		if(titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
			titleAnimationCounter--;
			if(currentTitleAnimation == 1) {
				titlePageNum = 2;
			}
		}
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));

		if(titleAnimationCounter >= 6) {
			String text = "Multiplayer";
			
			int x = 170;
			int y = 120;
			drawUnderlinedText(renderer, text, x, y, 2.0f);
		//HOST
		Colour c = (titleColour1);
		text = "Host";
			
		x = 180;
		y = 390;
			
		if(isHovering(text,x, y-24, font)) {
			c = (titleColour2);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					//ENTER GAME
					hostSelected = true;
					gp.currentState = gp.chooseSaveState;
					clickCooldown = 0.16;
					
					//usernameActive = true;
					//usernameBox.setActive(true);
				}
			}
		}
			
			renderer.drawString(font, text, x, y, 1.0f, c);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
			
			//MULTIPLAYER
			c = titleColour1;
			text = "Join";

			y = 480;
			
			if(isHovering(text,x, y-24, font)) {
				c = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//ENTER MULTIPLAYER
					if(clickCooldown == 0) {
					    gp.currentState = gp.lanJoinMenuState;
					    gp.startDiscovery();
						clickCooldown = 0.16;
					}
				}
			}
			
			renderer.drawString(font, text, x, y, 1.0f, c);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);

			//QUIT
			c = (titleColour1);
			text = "Back";

			y = 660;
			
			if(isHovering(text,x, y-24, font)) {
				c = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//QUIT GAME
					if(clickCooldown == 0) {
						gp.currentState = gp.titleState;
						clickCooldown = 0.33;
						currentTitleAnimation = 2;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}

			}
			renderer.drawString(font, text, x, y, 1.0f, c);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);

		}
	}
	public void drawLanJoinMenu(Renderer renderer) {

	    // Background
	    renderer.draw(titleBackground, 
	        (gp.frameWidth/2) - (int)((768*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((560*1.5)/2), 
	        (int)(768*1.5), (int)(560*1.5));

	    // Book animation
	    titleAnimationSpeed++;
	    if (titleAnimationSpeed >= titleAnimationSpeedFactor) {
	        titleAnimationSpeed = 0;
	        titleAnimationCounter++;
	    }
	    if (titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
	        titleAnimationCounter--;
	    }
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));


	    if (titleAnimationCounter >= 6) {
	        // Title
	        String text = "Available LAN Worlds";
	        int x = 190;
	        int y = 140;
			drawUnderlinedText(renderer, text, x, y, 2.0f);

	        // Server list
	        int startY = 320;
	        int index = 0;

	        if (gp.discovery != null) {
	            startY = 320;
	            index = 0;
	            for (DiscoveredServer server : gp.discovery.getDiscoveredServers()) {
	                text = server.worldName + " (Host: " + server.hostUser + ")";
	                x = 180;
	                y = startY + index * 60;

	                Colour c = titleColour1;
	                if (isHovering(text, x, y-24, font)) {
	                    c = (titleColour2);
	                    if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                        // Select this server
	                        gp.selectedServer = server;
	                        gp.currentState = gp.createJoinPlayerScreen;
	                        usernameActive = true;
	                        joinSelected = true;
	                        clickCooldown = 0.25;
	                		selectedSkinNum = 0;
	    		    		selectedHairNum = 0;
	                    }
	                }
	                renderer.drawString(font, text, x, y, 1.0f, c);
	    			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
	                index++;
	            }
	        } else {
	            renderer.drawString(font, "Searching for games on LAN...", 140, 320, 1.0f, Colour.BLACK);
	        }

	        // Back button
	        text = "Back";
	        x = 180;
	        y = 660;
	        Colour c = titleColour1;
	        if (isHovering(text, x, y-24, font)) {
	            c = (titleColour2);
	            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                gp.currentState = gp.multiplayerSettingsState;
	                clickCooldown = 0.33;
	                currentTitleAnimation = 2;
	                titleAnimationCounter = 0;
	                titleAnimationSpeed = 0;
	                gp.stopDiscovery();
	                joinSelected = false;
	            }
	        }
	        renderer.drawString(font, text, x, y, 1.0f, c);
			renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
	    }
	}
	public void drawGameSettingsScreen(Renderer renderer) {

		renderer.draw(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5));
		
		titleAnimationSpeed++;
		if(titleAnimationSpeed >= titleAnimationSpeedFactor) {
			titleAnimationSpeed = 0;
			titleAnimationCounter++;
		}
		
		if(titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
			titleAnimationCounter--;
			if(currentTitleAnimation == 1) {
				titlePageNum = 1;
			}
		}
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));
		
		if(titleAnimationCounter >= 6) {
		renderer.setFont(font);
		renderer.setColour(titleColour1);
		String text = "Play";
		
		int x = 270;
		int y = 140;
		drawUnderlinedText(renderer, text, x, y, 2.0f);

		//SinglePlayer
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			text = "Singleplayer";
				
			x = 180;
			y = 360;
				
			if(isHovering(text,x, y-24, font)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
						singleplayerSelected = true;
						gp.currentState = gp.chooseSaveState;
					}
				}
			}
				
				renderer.drawString(text, x, y);
				renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
				
				//MULTIPLAYER
				renderer.setColour(titleColour1);
				text = "Multiplayer";

				x = 180;
				y = 450;
				
				if(isHovering(text,x, y-24, font)) {
					renderer.setColour(titleColour2);
					if(gp.mouseL.mouseButtonDown(0)) {
						//ENTER MULTIPLAYER
						gp.currentState = gp.multiplayerSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				
				renderer.drawString(text, x, y);
				renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);

				//QUIT
				renderer.setColour(titleColour1);
				text = "Back";

				x = 180;
				y = 680;
				
				if(isHovering(text,x, y-24, font)) {
					renderer.setColour(titleColour2);
					if(gp.mouseL.mouseButtonDown(0)) {
						//QUIT GAME
						if(clickCooldown == 0) {
							gp.currentState = gp.titleState;
							clickCooldown = 0.33;
							currentTitleAnimation = 2;
							titleAnimationCounter = 0;
							titleAnimationSpeed = 0;
						}
					}
				}
				
				renderer.drawString(text, x, y);
				renderer.draw(bookLine2, x-titleUIScale*10, y-titleUIScale*4, titleUIScale*48, titleUIScale*16);
		}
	}
	private void drawChooseSaveState(Renderer renderer) {
		
		renderer.draw(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5));
		
		titleAnimationSpeed++;
		if(titleAnimationSpeed >= titleAnimationSpeedFactor) {
			titleAnimationSpeed = 0;
			titleAnimationCounter++;
		}
		
		if(titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
			titleAnimationCounter--;
			if(currentTitleAnimation == 1) {
				titlePageNum = 1;
			}
		}
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));
		
		if(titleAnimationCounter >= 6) {
			
			int x = 170;
			int y = 140;
			String text = "Choose Save";
			
			saveChosen = -1;
			
			drawUnderlinedText(renderer, text, x, y, 2.0f);
			
			text = "Save 1";
			
			x = 135;
			y = 260 - 60;
			
			renderer.setColour(titleColour1);
			int result = drawSave(renderer, 1, x, y, text);
			if (result != -1) saveChosen = result;
			
			renderer.setColour(titleColour1);
			text = "Save 2";
			y = 260 + 48*3 + 10 - 60;
			
			result = drawSave(renderer, 2, x, y, text);
			if (result != -1) saveChosen = result;
			
			renderer.setColour(titleColour1);
			text = "Save 3";
			y = 260 + 48*6 + 20 - 60;
			
			result = drawSave(renderer, 3, x, y, text);
			if (result != -1) saveChosen = result;
			
			//QUIT
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			text = "Back";

			x = 180;
			y = 680;
			
			if(isHovering(text,x, y-24, font)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//QUIT GAME
					if(clickCooldown == 0) {
						gp.currentState = gp.titleState;
						clickCooldown = 0.33;
						currentTitleAnimation = 2;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
						singleplayerSelected = false;
						hostSelected = false;
					}
				}
				renderer.fillRect(x, y+ 6, getTextWidth(text, font), 4);
			}
			renderer.drawString(text, x, y);
			
			if(doDestroySave) {
				renderer.setFont(font);
				renderer.setColour(titleColour1);
				renderer.drawString("Are you sure you want", 620, 220);
				renderer.drawString("  to delete this save", 620, 240);
				
				y+= 40;
				text = "Yes";

				x = 660;
				y = 400;
				
				if(isHovering(text,x, y-24, font)) {
					renderer.setColour(titleColour2);
					if(gp.mouseL.mouseButtonDown(0)) {
						if(clickCooldown == 0) {
							clickCooldown = 0.33;
							gp.saveM.clearSaveSlot(destroySaveNum);
							destroySaveNum = 0;
							doDestroySave = false;
						}
					}
					renderer.fillRect(x, y+ 6, getTextWidth(text, font), 4);
				}
				renderer.drawString(text, x, y);
				
				renderer.setColour(titleColour1);
				text = "No";
				
				x += 200;
				y = 400;
				
				if(isHovering(text,x, y-24, font)) {
					renderer.setColour(titleColour2);
					if(gp.mouseL.mouseButtonDown(0)) {
						if(clickCooldown == 0) {
							clickCooldown = 0.33;
							destroySaveNum = 0;
							doDestroySave = false;
						}
					}
					renderer.fillRect(x, y+ 6, getTextWidth(text, font), 4);
				}
				renderer.drawString(text, x, y);
			}
			
			if(saveChosen != -1 && !doDestroySave) {
				if(gp.saveM.isSlotEmpty(saveChosen)) {
					gp.currentState = gp.createWorldScreen;
					currentTitleAnimation = 1;
					titleAnimationCounter = 0;
					titleAnimationSpeed = 0;
		    		gp.player = new Player(gp, 0, 0, null, null, "");
		    		gp.player.setDirection(2);
		    		selectedSkinNum = 0;
		    		selectedHairNum = 0;
				} else {
					if(!hostSelected) {
						gp.playSinglePlayer(saveChosen, "", "", 0, 0, 0);
					} else {
						gp.hostServer(saveChosen, "", "", 0, 0, 0);
						hostSelected = false;
					}
				}
			}
		}
	}
	private void drawCustomiseOutfitScreen(Renderer renderer) {
		
	    gp.player.drawPreview(renderer, 420, -20);
	    
		int x = 540+200;
	    int y = 300;
	    //DIRECTION ARROWS 
	    int imageSize = 16*titleUIScale;
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateLeft();
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    if (isHovering(x + imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateRight();
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize, y, imageSize, imageSize);
	    }
	    
	    //HAT
	    String text = "Hat " + (selectedHatNum + 1);
	    int centerX  = 20 + 90;
	    x = centerX + 50;
	    y = 360;
	    
	    renderer.setColour(titleColour1);
	    renderer.drawString(text, x, y);
	    
	    y-= 50;
	    
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.1;
	            selectedHatNum--;
	            
	            if(selectedHatNum < 0) {
	            	selectedHatNum = Constants.MAXHATNUM;
	            }
	            gp.player.setHat(selectedHatNum);
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    if (isHovering(x + imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.1;
	            selectedHatNum++;
	            
	            if(selectedHatNum > Constants.MAXHATNUM) {
	            	selectedHatNum = 0;
	            }
	            gp.player.setHat(selectedHatNum);
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize, y, imageSize, imageSize);
	    }
	    
	    //Hair Style
	    text = "Costume " + (selectedCostumeNum + 1);
	    x = centerX + 50;
	    y = 460;
	    
	    renderer.setColour(titleColour1);
	    renderer.drawString(text, x, y);
	    
	    y-= 50;
	    
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedCostumeNum--;
	            
	            if(selectedCostumeNum < 0) {
	            	selectedCostumeNum = Constants.MAXCOSTUMENUM;
	            }
	            gp.player.setCostume(selectedCostumeNum);
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    int rightArrowOffset = 80;
	    if (isHovering(x + imageSize+rightArrowOffset, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedCostumeNum++;
	            
	            if(selectedCostumeNum > Constants.MAXCOSTUMENUM) {
	            	selectedCostumeNum = 0;
	            }
	            gp.player.setCostume(selectedCostumeNum);
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	    }
	    
	    //EXIT
	    if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
	    	clickCooldown = 0.33;
        	//ENTER CUSTOMISATION STATE
        	gp.currentState = gp.playState;
    	}
	    
	    
	}
	private void drawCreateWorldScreen(Renderer renderer) {

	    renderer.draw(titleBackground,(gp.frameWidth / 2) - (int) ((768 * 1.5) / 2),(gp.frameHeight / 2) - (int) ((560 * 1.5) / 2),(int) (768 * 1.5),(int) (560 * 1.5));

	    // ---- Book animation ----
	    titleAnimationSpeed++;
	    if (titleAnimationSpeed >= titleAnimationSpeedFactor) {
	        titleAnimationSpeed = 0;
	        titleAnimationCounter++;
	    }

	    if (titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
	        titleAnimationCounter--;
	    }

		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));

	    if (titleAnimationCounter <= 5) return;
	    
	    //TITLE
		String text = "Create Save";
		
		int x = 170;
		int y = 140;
		drawUnderlinedText(renderer, text, x, y, 2.0f);
		
		text = "Preview";
		
		x = 700;
		y = 140;
		drawUnderlinedText(renderer, text, x, y, 2.0f);
	    
	    // ---- Labels ----
	    renderer.setFont(font);
	    renderer.setColour(titleColour1);
	    int centerX   = 110 + 60;
	    renderer.drawString("Player Name", centerX, 190+40);

	    renderer.drawString("World Name", centerX, 270+40);

	    // ---- TextBoxes ----
	    playerNameBox.draw(renderer);
	    worldNameBox.draw(renderer);
	    
	    //DRAW PLAYER PREVIEW
    	renderer.draw(previewCharacterFrame, 660-30, 190, 365,  360);

	    renderer.draw(previewFrame, 660+5, 200, 300, 240);
	    gp.player.drawPreview(renderer, 450+10, -20);
	    
	    x = 540+200+24+10;
	    y = 300;
	    //DIRECTION ARROWS 
	    int imageSize = 16*titleUIScale;
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateLeft();
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    if (isHovering(x + imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateRight();
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize, y, imageSize, imageSize);
	    }
	    
	    //CUSTOMISATION
	    // Shared layout
	    
	    
	    int selectorCenterX = centerX + 140+20;
	    int leftArrowX = selectorCenterX - 200;
	    int rightArrowX = selectorCenterX + 120;

	    float fontScale = 1.0f;
	    
    	renderer.draw(createCharacterFrame, leftArrowX + 8, 390, (rightArrowX-leftArrowX-10)+imageSize, 250);

	    // ======================================================
	    // SKIN
	    // ======================================================

	    text = "Skin " + (selectedSkinNum + 1);

	    y = 445;

	    // Center text
	    float textWidth = font.getTextWidth(text, fontScale);
	    float textX = selectorCenterX - (textWidth / 2);

	    renderer.setColour(titleColour1);
	    renderer.drawString(font, text, textX, y, fontScale, titleColour1);

	    // Arrow Y
	    int arrowY = y - 50;

	    // Left Arrow
	    if (isHovering(leftArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(leftArrow3Pressed, leftArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.1;

	            selectedSkinNum--;

	            if (selectedSkinNum < 0) {
	                selectedSkinNum = Constants.MAXSKINNUM;
	            }

	            gp.player.setSkin(selectedSkinNum);
	        }

	    } else {
	        renderer.draw(leftArrow3, leftArrowX, arrowY, imageSize, imageSize);
	    }

	    // Right Arrow
	    if (isHovering(rightArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(rightArrow3Pressed, rightArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.1;

	            selectedSkinNum++;

	            if (selectedSkinNum > Constants.MAXSKINNUM) {
	                selectedSkinNum = 0;
	            }

	            gp.player.setSkin(selectedSkinNum);
	        }

	    } else {

	        renderer.draw(rightArrow3, rightArrowX, arrowY, imageSize, imageSize);
	    }


	    // ======================================================
	    // HAIR STYLE
	    // ======================================================

	    text = "Hair Style " + (selectedHairStyleNum + 1);

	    y = 525;

	    // Center text
	    textWidth = font.getTextWidth(text, fontScale);
	    textX = selectorCenterX - (textWidth / 2);

	    renderer.setColour(titleColour1);
	    renderer.drawString(font, text, textX, y, fontScale, titleColour1);

	    // Arrow Y
	    arrowY = y - 50;

	    // Left Arrow
	    if (isHovering(leftArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(leftArrow3Pressed, leftArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.25;

	            selectedHairStyleNum--;

	            if (selectedHairStyleNum < 0) {
	                selectedHairStyleNum = Constants.MAXHAIRNUM;
	            }

	            gp.player.setHairStyle(selectedHairStyleNum);
	        }

	    } else {
	        renderer.draw(leftArrow3, leftArrowX, arrowY, imageSize, imageSize);
	    }

	    // Right Arrow
	    if (isHovering(rightArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(rightArrow3Pressed, rightArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.25;

	            selectedHairStyleNum++;

	            if (selectedHairStyleNum > Constants.MAXHAIRNUM) {
	                selectedHairStyleNum = 0;
	            }

	            gp.player.setHairStyle(selectedHairStyleNum);
	        }

	    } else {

	        renderer.draw(rightArrow3, rightArrowX, arrowY, imageSize, imageSize);
	    }


	    // ======================================================
	    // HAIR COLOUR
	    // ======================================================

	    text = "Hair Colour " + (selectedHairNum + 1);

	    y = 605;

	    // Center text
	    textWidth = font.getTextWidth(text, fontScale);
	    textX = selectorCenterX - (textWidth / 2);

	    renderer.setColour(titleColour1);
	    renderer.drawString(font, text, textX, y, fontScale, titleColour1);

	    // Arrow Y
	    arrowY = y - 50;

	    // Left Arrow
	    if (isHovering(leftArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(leftArrow3Pressed, leftArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.25;

	            selectedHairNum--;

	            if (selectedHairNum < 0) {
	                selectedHairNum = Constants.MAXHAIRCOLOURNUM;
	            }

	            gp.player.setHair(selectedHairNum);
	        }

	    } else {
	        renderer.draw(leftArrow3, leftArrowX, arrowY, imageSize, imageSize);
	    }

	    // Right Arrow
	    if (isHovering(rightArrowX, arrowY, imageSize, imageSize)) {

	        renderer.draw(rightArrow3Pressed, rightArrowX, arrowY, imageSize, imageSize);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {

	            clickCooldown = 0.25;

	            selectedHairNum++;

	            if (selectedHairNum > Constants.MAXHAIRCOLOURNUM) {
	                selectedHairNum = 0;
	            }

	            gp.player.setHair(selectedHairNum);
	        }

	    } else {
	        renderer.draw(rightArrow3, rightArrowX, arrowY, imageSize, imageSize);
	    }
	    

	    //RIGHT SIDE
	    x = 120+gp.frameWidth / 2;
	    y-=100;
	    //PREVIEW HAT
		renderer.setColour(titleColour1);
		text = "Show Hat";
		int boxOffset = 170;
		renderer.drawString(text, x, y);
		y-= 22;
		if(gp.player.previewHat) {
			renderer.draw(checkedBox2, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox2, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	gp.player.previewHat = !gp.player.previewHat;
                clickCooldown = 0.33;
            }
        }
	    // ---- Confirm ----
	    text = "Confirm";
	    y = 640;

	    renderer.setColour(titleColour1);

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.33;

	            String playerName = playerNameBox.getText();
	            String worldName  = worldNameBox.getText();
	            
	            if(!hostSelected) {
		            gp.playSinglePlayer(saveChosen, playerName, worldName, selectedSkinNum, selectedHairNum, selectedHairStyleNum);
	            } else {
	            	hostSelected = false;
		            gp.hostServer(saveChosen, playerName, worldName, selectedSkinNum, selectedHairNum, selectedHairStyleNum);
	            }
	            
	            playerNameBox.setText("");
	            worldNameBox.setText("");
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }
	    
	    renderer.drawString(text, x, y);

	    // ---- Back ----
	    renderer.setColour(titleColour1);
	    text = "Back";
	    x = 180;
	    y = 680;

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            gp.currentState = gp.chooseSaveState;
	            clickCooldown = 0.33;
	            currentTitleAnimation = 2;
	            titleAnimationCounter = 0;
	            titleAnimationSpeed = 0;
	            playerNameBox.setText("");
	            worldNameBox.setText("");
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }

	    renderer.drawString(text, x, y);
	}
	private void drawJoinWorldScreen(Renderer renderer) {

	    renderer.draw(
	        titleBackground,
	        (gp.frameWidth / 2) - (int) ((768 * 1.5) / 2),
	        (gp.frameHeight / 2) - (int) ((560 * 1.5) / 2),
	        (int) (768 * 1.5),
	        (int) (560 * 1.5)
	    );

	    // ---- Book animation ----
	    titleAnimationSpeed++;
	    if (titleAnimationSpeed >= titleAnimationSpeedFactor) {
	        titleAnimationSpeed = 0;
	        titleAnimationCounter++;
	    }

	    if (titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
	        titleAnimationCounter--;
	    }
	    
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((240*titleUIScale) / 2), (gp.frameHeight/2) - (int)((160*titleUIScale)/2), (int)(240*titleUIScale), (int)(160*titleUIScale));


	    if (titleAnimationCounter < 6) return;
	    
	    //TITLE
	    String text = "Create Player";
		
		int x = 250;
		int y = 140;
		drawUnderlinedText(renderer, text, x, y, 2.0f);

	    
	    // ---- Labels ----
	    renderer.setFont(font);
	    renderer.setColour(titleColour1);
	    int centerX   = 110 + 60;
	    renderer.drawString("Player Name", centerX, 190);

	    // ---- TextBoxes ----
	    playerNameBox.draw(renderer);
	    
	    //DRAW PLAYER PREVIEW
	    if(gp.player == null) {
			gp.player = new Player(gp, 0, 0, null, null, "");
    		gp.player.setDirection(2);
    		selectedSkinNum = 0;
	    }
	    gp.player.drawPreview(renderer, 420, -20);
	    
	    x = 540+200;
	    y = 300;
	    //DIRECTION ARROWS 
	    int imageSize = 16*titleUIScale;
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateLeft();
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    if (isHovering(x + imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            gp.player.rotateRight();
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize, y, imageSize, imageSize);
	    }
	    
	    //Skin
	    text = "Skin " + (selectedSkinNum + 1);
	    x = centerX + 50;
	    y = 360;
	    
	    renderer.setColour(titleColour1);
	    renderer.drawString(text, x, y);
	    
	    y-= 50;
	    
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.1;
	            selectedSkinNum--;
	            
	            if(selectedSkinNum < 0) {
	            	selectedSkinNum = Constants.MAXSKINNUM;
	            }
	            gp.player.setSkin(selectedSkinNum);
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    if (isHovering(x + imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.1;
	            selectedSkinNum++;
	            
	            if(selectedSkinNum > Constants.MAXSKINNUM) {
	            	selectedSkinNum = 0;
	            }
	            gp.player.setSkin(selectedSkinNum);
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize, y, imageSize, imageSize);
	    }
	    
	    
	    //Hair Style
	    text = "Hair Style " + (selectedHairStyleNum + 1);
	    x = centerX + 50;
	    y = 460;
	    
	    renderer.setColour(titleColour1);
	    renderer.drawString(text, x, y);
	    
	    y-= 50;
	    
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedHairStyleNum--;
	            
	            if(selectedHairStyleNum < 0) {
	            	selectedHairStyleNum = Constants.MAXHAIRNUM;
	            }
	            gp.player.setHairStyle(selectedHairStyleNum);
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    int rightArrowOffset = 80;
	    if (isHovering(x + imageSize+rightArrowOffset, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedHairStyleNum++;
	            
	            if(selectedHairStyleNum > Constants.MAXHAIRNUM) {
	            	selectedHairStyleNum = 0;
	            }
	            gp.player.setHairStyle(selectedHairStyleNum);
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	    }
	    
	    //Hair
	    text = "Hair Colour " + (selectedHairNum + 1);
	    x = centerX + 50;
	    y = 560;
	    
	    renderer.setColour(titleColour1);
	    renderer.drawString(text, x, y);
	    
	    y-= 50;
	    
	    if (isHovering(x-imageSize, y, imageSize, imageSize)) {
	    	renderer.draw(leftTitleArrow2, x - imageSize, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedHairNum--;
	            
	            if(selectedHairNum < 0) {
	            	selectedHairNum = Constants.MAXHAIRCOLOURNUM;
	            }
	            gp.player.setHair(selectedHairNum);
	        }
	    } else {
	    	renderer.draw(leftTitleArrow1, x - imageSize, y, imageSize, imageSize);
	    }
	    
	    rightArrowOffset = 80;
	    if (isHovering(x + imageSize+rightArrowOffset, y, imageSize, imageSize)) {
	    	renderer.draw(rightTitleArrow2, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.25;
	            selectedHairNum++;
	            
	            if(selectedHairNum > Constants.MAXHAIRCOLOURNUM) {
	            	selectedHairNum = 0;
	            }
	            gp.player.setHair(selectedHairNum);
	        }
	    } else {
		    renderer.draw(rightTitleArrow1, x + imageSize+rightArrowOffset, y, imageSize, imageSize);
	    }
	    
	    //RIGHT SIDE
	    x = 200+gp.frameWidth / 2 - getTextWidth(text, font) / 2;
	    //PREVIEW HAT
		renderer.setColour(titleColour1);
		text = "Show Hat";
		int boxOffset = 170;
		renderer.drawString(text, x, y);
		y-= 20;
		if(gp.player.previewHat) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	gp.player.previewHat = !gp.player.previewHat;
                clickCooldown = 0.33;
            }
        }

	    // ---- Confirm ----
	    text = "Confirm";
	    y = 640;

	    renderer.setColour(titleColour1);

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            clickCooldown = 0.33;

	            String playerName = playerNameBox.getText();
	            
	            gp.joinServer(playerName, gp.selectedServer.ip, gp.selectedServer.port, selectedSkinNum, selectedHairNum, selectedHairStyleNum);
	            joinSelected = false;
	            
	            playerNameBox.setText("");
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }
	    
	    renderer.drawString(text, x, y);

	    // ---- Back ----
	    renderer.setColour(titleColour1);
	    text = "Back";
	    x = 180;
	    y = 680;

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            gp.currentState = gp.lanJoinMenuState;
	            clickCooldown = 0.33;
	            currentTitleAnimation = 2;
	            titleAnimationCounter = 0;
	            titleAnimationSpeed = 0;
	            playerNameBox.setText("");
	            worldNameBox.setText("");
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }

	    renderer.drawString(text, x, y);
	}
	private int drawSave(Renderer renderer, int saveSlot, int x, int y, String text) {
		int saveChosen = -1;
		if(!gp.saveM.isSlotEmpty(saveSlot)) {
			renderer.setFont(font);
			renderer.draw(saveBorder, x, y, 130*3, 48*3);
			
			if(isHovering(x, y, 130*3, 48*3)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					saveChosen = saveSlot;
				}
			}
			if(!gp.saveM.isSlotEmpty(saveSlot)) {
				text = gp.saveM.getSavedWorldName(saveSlot);
			}
			int num = 30;
			renderer.drawString(text, 162+num, y+50);
			
			//renderer.setColour(titleColour1);
			renderer.setFont(font);
			renderer.drawString(gp.saveM.getSavedSeason(saveSlot) + " Day " + Integer.toString(gp.saveM.getSavedDay(saveSlot)), 120+num, y+80);
			
			renderer.draw(coinImage, 120+num, y+84, 16*3, 16*3);
			renderer.drawString(Integer.toString(gp.saveM.getSavedMoney(saveSlot)), 176+num, y+114);
			
			renderer.fillRect(x + 176-4, y + 5+12-4, 180+8, 110+8);
			
			renderer.draw(deleteSave, x + 130*3, y+84, 16*3, 18*3);
			if(isHovering(x + 130*3, y+84, 16*3, 18*3)) {
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						clickCooldown = 0.33;
						doDestroySave = true;
						destroySaveNum = saveSlot;
					}
				}
			}
			
			//Draw image
			try {
			    File previewFile = new File("save/preview" + saveSlot + ".png");
			    if (previewFile.exists()) {
			        BufferedImage image = ImageIO.read(previewFile);
			        Texture preview = new Texture();
			        preview.init(image);
			        renderer.draw(preview, x + 176, y + 5+12, 180, 110); 
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
		} else {
			text = "New Save";
			renderer.setFont(font);
			renderer.draw(saveBorder, x, y, 130*3, 48*3);
			
			if(isHovering(x, y, 130*3, 48*3)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
					saveChosen = saveSlot;
				}
			}
			renderer.drawString(text, 162, y+50);
		}
		
		return saveChosen;
	}
	public void drawChatScreen(Renderer renderer) {

	    updateMessages();
	    // Chat feed stays exactly the same
	    drawChatFeed(renderer);
	    // Chat input box
	    chatBox.draw(renderer);
	}
	public void drawChatFeed(Renderer renderer) {
	    int padding = 20;
	    int inputBoxHeight = 50;
	    int boxY = gp.frameHeight - inputBoxHeight - 20; // same as chat input
	    int chatHeight = boxY - padding; // chat area height
	    int chatX = 20;
	    int chatWidth = gp.frameWidth - 40;

	    renderer.setColour(new Colour(0, 0, 0, 150));
	    renderer.fillRect(chatX, padding, chatWidth, chatHeight);

	    renderer.setFont(font);
	    renderer.setColour(Colour.WHITE);

	    // Draw messages from bottom up
	    int lineHeight = 30;
	    float y = boxY - 10 - chatScrollOffset; // start just above input box

	    for(int i = chatMessages.size() - 1; i >= 0; i--) {
	        GUIMessage msg = chatMessages.get(i);
	        String text;
	    	msg.lifetime = 100;
	        if(msg.username.equals("")) {
	        	text = msg.text;
	        } else {
		        text = msg.username + ": " + msg.text;
	        }

	        renderer.drawString(text, chatX + 10, (int)y, msg.color);
	        y -= lineHeight;
	        if(y < padding) break; // stop drawing if we reach top
	    }
	}
	public void scrollChat(float delta) {
	    chatScrollOffset -= delta * 20; // delta from mouse wheel
	    if(chatScrollOffset < 0) chatScrollOffset = 0;

	    // Optional: limit max scroll to avoid empty space
	    int lineHeight = 30;
	    int visibleLines = (gp.frameHeight - 50 - 20 - 20) / lineHeight; // total visible lines
	    int extraLines = chatMessages.size() - visibleLines;
	    if(extraLines > 0) {
	        float maxScroll = extraLines * lineHeight;
	        if(chatScrollOffset > maxScroll) chatScrollOffset = maxScroll;
	    }
	}
	public void sendChatMessage(String message) {
		 addChatMessage(gp.player.getUsername(), message);
		 
		 if(gp.multiplayer) {
			 if (gp.socketClient != null) {
				 gp.socketClient.send(new Packet04Chat(gp.player.getUsername(), message));
			 }
		 }
	}
	public void drawUsernameInput(Renderer renderer) {

	    renderer.draw(
	        titleBackground,
	        (gp.frameWidth / 2) - (int) ((768 * 1.5) / 2),
	        (gp.frameHeight / 2) - (int) ((560 * 1.5) / 2),
	        (int) (768 * 1.5),
	        (int) (560 * 1.5)
	    );

	    boolean drawLoadingScreen = false;

	    // ---- Title ----
	    renderer.setFont(font);
	    renderer.setColour(titleColour1);

	    String text = "Enter Username";
	    int x = gp.frameWidth / 2 - getTextWidth(text, font) / 2;
	    int y = 200;
	    renderer.drawString(text, x, y);

	    // ---- TextBox ----
	    usernameBox.draw(renderer);

	    // ---- Confirm ----
	    text = "Confirm";
	    x = gp.frameWidth / 2 - getTextWidth(text, font) / 2;
	    y = 420;

	    renderer.setColour(titleColour1);

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            drawLoadingScreen = true;
	            startLoading = true;
	            clickCooldown = 0.33;
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }

	    renderer.drawString(text, x, y);

	    // ---- Back ----
	    renderer.setColour(titleColour1);
	    text = "Back";

	    x = 100;
	    y = 660;

	    if (isHovering(text, x, y - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            gp.currentState = gp.multiplayerSettingsState;
	            clickCooldown = 0.33;

	            currentTitleAnimation = 2;
	            titleAnimationCounter = 0;
	            titleAnimationSpeed = 0;

	            joinSelected = false;
	            hostSelected = false;
	        }

	        renderer.fillRect(x, y + 14, getTextWidth(text, font), 6);
	    }

	    renderer.drawString(text, x, y);

	    // ---- Loading ----
	    if (drawLoadingScreen) {

	        renderer.setFont(font);
	        renderer.setColour(Colour.WHITE);

	        text = "LOADING...";
	        renderer.drawString(text, 30, 720);

	        if (startLoading) {
	            String username = usernameBox.getText();

	            if (hostSelected) {
	                //gp.hostServer(username);
	                hostSelected = false;
	            } 
	            else if (joinSelected) {
	                //gp.joinServer(
	                    //username,
	                    //gp.selectedServer.ip,
	                    //gp.selectedServer.port
	                //);
	                joinSelected = false;
	            }

	            startLoading = false;
	        }

	        startLoading = true;
	    }
	}
	private void drawAchievementsScreen(Renderer renderer) {

		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);

		int scale = 3;
		TextureRegion img = achievementBorder;

		int bgX = gp.frameWidth / 2 - img.getPixelWidth() / 2 * scale;
		int bgY = gp.frameHeight / 2 - img.getPixelHeight() / 2 * scale;

		renderer.draw(img, bgX, bgY,
				img.getPixelWidth() * scale,
				img.getPixelHeight() * scale);

		// =========================
		// BACK BUTTON
		// =========================

		renderer.setFont(font);

		String text = "Back";
		int textX = getXforCenteredText(text, font);

		if (isHovering(text, textX, 720 - 24, font)) {

			renderer.setColour(titleColour1);

			if (gp.mouseL.mouseButtonDown(0)) {
				if (clickCooldown == 0) {
					gp.currentState = gp.pauseState;
					clickCooldown = 0.16;
				}
			}

		} else {
			renderer.setColour(Colour.WHITE);
		}

		renderer.drawString(text, textX, 720);
		
		// =========================
		// PAGE BUTTONS
		// =========================
		
		List<Achievement> achievementList =
				new ArrayList<>(gp.world.progressM.achievements.values());

		// unlocked first
		achievementList.sort((a, b) -> {
			if (a.isUnlocked() == b.isUnlocked()) return 0;
			return a.isUnlocked() ? -1 : 1;
		});

		int totalPages = (int) Math.ceil(
				achievementList.size() / (float) achievementsPerPage);

		String left = "<";
		String right = ">";

		int arrowY = bgY + 530;//610;

		// LEFT
		int leftX = bgX + 80;

		boolean hoverLeft =
				isHovering(left, leftX, arrowY - 24, font);

		renderer.setColour(
				hoverLeft ? titleColour1 : Colour.WHITE);

		renderer.drawString(left, leftX, arrowY);

		if (hoverLeft &&
				gp.mouseL.mouseButtonDown(0) &&
				clickCooldown == 0) {

			achievementPage--;

			if (achievementPage < 0)
				achievementPage = 0;

			clickCooldown = 0.16;
		}

		// RIGHT
		int rightX = bgX + img.getPixelWidth() * scale - 100;

		boolean hoverRight =
				isHovering(right, rightX, arrowY - 24, font);

		renderer.setColour(
				hoverRight ? titleColour1 : Colour.WHITE);

		renderer.drawString(right, rightX, arrowY);

		if (hoverRight &&
				gp.mouseL.mouseButtonDown(0) &&
				clickCooldown == 0) {

			achievementPage++;

			if (achievementPage >= totalPages)
				achievementPage = totalPages - 1;

			clickCooldown = 0.16;
		}

		// Page text
		renderer.setColour(Colour.WHITE);

		String pageText =
				(achievementPage + 1) + " / " + Math.max(1, totalPages);

		renderer.drawString(
				pageText,
				getXforCenteredText(pageText, font),
				arrowY);
		

		// =========================
		// ACHIEVEMENT LIST
		// =========================

		if (achievementPage < 0) achievementPage = 0;
		if (achievementPage >= totalPages)
			achievementPage = Math.max(0, totalPages - 1);

		// =========================
		// GRID SETTINGS
		// =========================

		int columns = 3;

		int iconScale = 3;

		int slotSize = 24 * 3;
		int spacingX = 100;
		int spacingY = 86;

		int gridWidth = spacingX * (columns - 1);
		int startX = gp.frameWidth / 2 - gridWidth / 2 - slotSize/2;

		int startY = bgY + 100;

		int pageStartIndex = achievementPage * achievementsPerPage;

		Achievement hoveredAchievement = null;

		// =========================
		// DRAW ICON GRID
		// =========================

		for (int i = 0; i < achievementsPerPage; i++) {

			int achievementIndex = pageStartIndex + i;

			if (achievementIndex >= achievementList.size())
				break;

			Achievement a = achievementList.get(achievementIndex);

			int row = i / columns;
			int col = i % columns;

			int iconX = startX + col * spacingX;
			int iconY = startY + row * spacingY;

			boolean hovering =
					gp.mouseL.getScreenX() >= iconX &&
					gp.mouseL.getScreenX() <= iconX + slotSize &&
					gp.mouseL.getScreenY() >= iconY &&
					gp.mouseL.getScreenY() <= iconY + slotSize;

			// Draw slot
			renderer.draw(achievement, iconX, iconY, slotSize, slotSize);

			// Locked overlay
			if (!a.isUnlocked()) {
				renderer.draw(lockedAchievement,
						iconX,
						iconY,
						slotSize,
						slotSize);
			}

			// Draw icon
			if (a.getIcon() != null) {

				TextureRegion icon = a.getIcon();

				int drawW = icon.getPixelWidth() * iconScale;
				int drawH = icon.getPixelHeight() * iconScale;

				int drawX = iconX + slotSize / 2 - drawW / 2;
				int drawY = iconY + slotSize / 2 - drawH / 2;

				if(a.isUnlocked()) {
					renderer.draw(icon,
							drawX,
							drawY,
							drawW,
							drawH);
				} else {
					renderer.draw(icon,
							drawX,
							drawY,
							drawW,
							drawH,
							titleColour1.toVec4());
				}
			}

			// Hover highlight
			if (hovering) {

				hoveredAchievement = a;

				renderer.drawRect(
						iconX - 2,
						iconY - 2,
						slotSize + 4,
						slotSize + 4,
						Colour.WHITE);
			}
		}

		// =========================
		// TOOLTIP
		// =========================

		if (hoveredAchievement != null) {

			int tooltipW = 420;
			int tooltipH = 180;

			int tooltipX = (int)(gp.mouseL.getScreenX() + 24);
			int tooltipY = (int)(gp.mouseL.getScreenY()+ 24);

			// Keep onscreen
			if (tooltipX + tooltipW > gp.frameWidth)
				tooltipX = gp.frameWidth - tooltipW - 20;

			if (tooltipY + tooltipH > gp.frameHeight)
				tooltipY = gp.frameHeight - tooltipH - 20;

			renderer.setColour(new Colour(titleColour1.r, titleColour1.g, titleColour1.b, 240));
			renderer.fillRect(tooltipX, tooltipY, tooltipW, tooltipH);

			renderer.drawRect(
					tooltipX,
					tooltipY,
					tooltipW,
					tooltipH,
					titleColour1);

			// Icon
			if (hoveredAchievement.getIcon() != null) {

				TextureRegion icon = hoveredAchievement.getIcon();

				renderer.draw(icon,
						tooltipX + 20,
						tooltipY + 20,
						icon.getPixelWidth() * 4,
						icon.getPixelHeight() * 4);
			}

			// Name
			renderer.setFont(font);
			renderer.setColour(Colour.WHITE);

			renderer.drawString(
					hoveredAchievement.getName(),
					tooltipX + 120,
					tooltipY + 40);

			// Description
			renderer.setColour(Colour.WHITE);

			int descY = tooltipY + 75;

			for (String line : gp.gui.wrapText(
					hoveredAchievement.getDescription(),
					font,
					240)) {

				renderer.drawString(line,
						tooltipX + 120,
						descY);

				descY += 28;
			}

			// Status
			String status = hoveredAchievement.isUnlocked()
					? "Unlocked"
					: "Locked";

			renderer.setColour(
					hoveredAchievement.isUnlocked()
							? Colour.WHITE
							: Colour.WHITE);

			renderer.drawString(
					status,
					tooltipX + 120,
					tooltipY + 140);
		}
	}
	public void showAchievementNotification(Achievement a) {
	    notificationAchievement = a;
	    notificationTimer = notificationDuration;

	    // Example position: top-right of screen
	    notificationX = gp.frameWidth - 200;
	    notificationY = 50;
	}
	public void drawAchievementNotification(Renderer renderer) {

		if (notificationAchievement == null)
			return;

		// =========================
		// SETTINGS
		// =========================

		int popupW = 420;
		int popupH = 180;

		int x = gp.frameWidth - popupW - 20;
		int y = gp.frameHeight - popupH - 20;

		// =========================
		// BACKGROUND
		// =========================
		renderer.draw(achievementPopup, x, y, popupW, popupH);


		// =========================
		// ICON
		// =========================

		TextureRegion icon = notificationAchievement.getIcon();

		if (icon != null) {

			int iconScale = 3;

			int drawW = icon.getPixelWidth() * iconScale;
			int drawH = icon.getPixelHeight() * iconScale;

			renderer.draw(
					icon,
					x + 20,
					y + 20,
					drawW,
					drawH);
		}

		// =========================
		// TITLE
		// =========================

		renderer.setFont(font);

		renderer.setColour(Colour.WHITE);

		renderer.drawString(
				"Achievement Unlocked",
				x + 100,
				y + 28);

		// Achievement name
		renderer.drawString(
				notificationAchievement.getName(),
				x + 120,
				y + 58);

		// =========================
		// DESCRIPTION
		// =========================

		int descY = y + 95;

		renderer.setColour(titleColour1);
		for (String line : gp.gui.wrapText(
				notificationAchievement.getDescription(),
				font,
				240)) {

			renderer.drawString(
					line,
					x + 120,
					descY);

			descY += 26;
		}
	}
	public void draw(Renderer renderer) {
		
		switch(gp.currentState) {
		case 0:
			drawTitleScreen(renderer);
			break;
		case 1: //PLAY
			drawPlayScreen(renderer);
			break;
		case 3: //PAUSE
			drawPauseScreen(renderer);
			break;
		case 4: //GAME SETTINGS
			drawGameSettingsScreen(renderer);
			break;
		case 5: 
			switch(settingsState) {
			case baseSettings:
				drawBaseSettingsScreen(renderer);
				break;
			case generalState:
				drawGeneralSettings(renderer);
				break;
			case videoState:
				drawVideoSettings(renderer);
				break;
			case audioState:
				drawAudioSettings(renderer);
				break;
			case multiplayerState:
				drawMultiplayerSettings(renderer);
				break;
			}
			break;
		case 6: //MULTIPLAYER GAME SETTINGS
			drawMultiplayerGameSettings(renderer);
			break;
		case 8:
			drawUsernameInput(renderer);
			break;
		case 9:
			drawLanJoinMenu(renderer);
			break;
		case 11:
			drawCatalogueState(renderer);
			break;
		case 12:
			drawXPScreen(renderer);
			break;
		case 13:
			drawRecipeSelectScreen(renderer);
			break;
		case 14:
			drawChooseSaveState(renderer);
			break;
		case 15:
			drawChooseUpgradeScreen(renderer);
			break;
		case 16:
			drawDialogueState(renderer);
			break;
		case 17:
			drawAchievementsScreen(renderer);
			break;
		case 18:
			drawRecipesScreen(renderer);
			break;
		case 19:
			drawChatScreen(renderer);
			break;
		case 20:
			drawCreateWorldScreen(renderer);
			break;
		case 21:
			drawJoinWorldScreen(renderer);
			break;
		case 22:
			drawCustomiseOutfitScreen(renderer);
			break;
		case 23:
			drawStatisticsScreen(renderer);
			break;
		}
		
		drawAchievementNotification(renderer);
		
		if(firstDraw) {
			renderer.setFont(font);
			renderer.drawString("", -1000, -1000);
			firstDraw = false;
		}

		
	}
	private void drawPlayRecipe(Renderer renderer, Order order, int x, int y, int recipeScale) {
		RecipeRenderData data = renderCache.get(order);
	    if (data == null) return;
	    
    	float textScale = 0.7f;
	    
    	boolean isHovering = isHovering(x, y, 64*recipeScale, 73*recipeScale);
    	
	    // BASE
	    renderer.draw(data.borderImage, x, y, 64*recipeScale, 73*recipeScale);
	    if (data.customer.hideOrder) {
	        // Overlay the "mystery order" image
	        renderer.draw(data.mysteryOrderImage, x, y, 64*recipeScale, 80*recipeScale);
	    } else {
	        // Normal ingredient + text drawing
	    	for (int j = 0; j < data.ingredientImages.size(); j++) {

	    	    int ingredientX = x + j * (19 * recipeScale) + 5 * recipeScale;
	    	    int ingredientY = y + 52 * recipeScale;
	    	    
	    	    // Draw ingredient
	    	    renderer.draw(data.ingredientImages.get(j),
	    	            ingredientX,
	    	            ingredientY,
	    	            16 * recipeScale,
	    	            16 * recipeScale
	    	    );

	    	    // Draw dynamic step icons
	    	    List<TextureRegion> icons = data.stepIcons.get(j);

	    	    int iconSize = 16 * recipeScale;
	    	    int spacing = 20*recipeScale;
	    	    int borderSize = 20*recipeScale;

	    	    int startX = ingredientX - 2*recipeScale;
	    	    int iconY = ingredientY + 20 * recipeScale;

	    	    if(isHovering) {
	    	    	for (int s = 0; s < icons.size(); s++) {

	    	    	    int stepY = iconY + s * spacing;

	    	    	    renderer.draw(foodStepBorder,
	    	    	            startX,
	    	    	            stepY,
	    	    	            borderSize,
	    	    	            borderSize);

	    	    	    renderer.draw(icons.get(s),
	    	    	            startX+ 2*recipeScale,
	    	    	            stepY,
	    	    	            iconSize,
	    	    	            iconSize);
	    	    	}
	    	    }
	    	}
	    	 

	        if(!order.isCursed) {
		        renderer.setColour(orderTextColour);
	        } else {
	        	renderer.setColour(Colour.WHITE);
	        }

	        renderer.setFont(font);
		    for (int j = 0; j < data.nameLines.size(); j++) {
		        renderer.drawString(font, data.nameLines.get(j), x + data.nameLineOffsets.get(j), y + recipeScale*16 -6*recipeScale + j*15, textScale);
		    }
	        
		    //renderer.draw(starLevel, x +7*recipeScale, y + recipeScale*16 + 36*recipeScale, 16*recipeScale, 16*recipeScale);
	        if(data.starLevel > 2) {
	            //renderer.draw(starLevel, x +7*recipeScale + 1 * 16*recipeScale, y + recipeScale*16+ 36*recipeScale, 16*recipeScale, 16*recipeScale);
	        } else {
	            //renderer.draw(emptyStar, x +7*recipeScale + 1 * 16*recipeScale, y+ recipeScale*16 + 36*recipeScale, 16*recipeScale, 16*recipeScale);
	        }
	        
	        if(data.starLevel > 3) {
	            //renderer.draw(starLevel, x +7*recipeScale + 2 * 16*recipeScale, y + recipeScale*16 + 36*recipeScale, 16*recipeScale, 16*recipeScale);
	        } else {
	            //renderer.draw(emptyStar, x +7*recipeScale + 2 * 16*recipeScale, y + recipeScale*16 + 36*recipeScale, 16*recipeScale, 16*recipeScale);
	        }

		    renderer.draw(data.plateImage, x + 24*recipeScale, y + 25*recipeScale, 16*recipeScale, 16*recipeScale);
	    }

	    // COIN + FACE
	    renderer.draw(data.coinImage, x+5*recipeScale, y + 29*recipeScale - 2, 16*recipeScale, 16*recipeScale);
	    renderer.draw(data.faceIcon, x + 70, y + 16*recipeScale - 2, 32*recipeScale, 32*recipeScale);

	    // COST
	    renderer.drawString(font, data.cost, x + 9*recipeScale, y + 27*recipeScale - 2 + 8, textScale, orderTextColour);

	    // PATIENCE
	    drawPatienceBar(renderer, x + 5*recipeScale, y + 43*recipeScale, (int)data.customer.getPatienceCounter(), (int)data.customer.getMaxPatienceTime());

	}
	private void drawPlayScreen(Renderer renderer) {
		updateMessages();
		List<Order> currentOrders = new ArrayList<>(RecipeManager.getCurrentOrders());
		
		int i = 0;
		int recipeScale = 2;
		
		for (Order order : currentOrders) {
		    int x = 8 + i * (64*recipeScale);
		    int y = 0;
		    drawPlayRecipe(renderer, order, x, y, recipeScale);
		    i++;
		}

		//DRAW TIME AND WEATHER		
		TextureRegion currentImage = day;
		
		switch(gp.world.gameM.getTimeOfDay()) {
		case 1:
			currentImage = day;
			break;
		case 0:
			currentImage = morning;
			break;
		case 4:
			currentImage = evening;
			break;
		case 5:
			currentImage = night;
			break;
		}
		
		if(gp.world.gameM.currentWeather.equals(Weather.RAIN)) {
			currentImage = rain;
		} else if(gp.world.gameM.currentWeather.equals(Weather.THUNDERSTORM)) {
			currentImage = thunder;
		} else if(gp.world.gameM.currentWeather.equals(Weather.CLOUDY)) {
			currentImage = cloudy;
		}
		renderer.draw(currentImage, gp.frameWidth - (48*4) - 4, 4, 48*4, 48*4);
		renderer.draw(timeHeader, gp.frameWidth - (54*4), 4 + (30*4) - 2 + 70, 54*4, 20*4);
		renderer.draw(timeFrame, gp.frameWidth - (45*3), 4 + (73*3) - 2 + (15*3), 38*3, 15*3);
		
		renderer.setFont(font);
		renderer.setColour(Colour.WHITE);
		renderer.drawString(gp.world.gameM.getDate(), gp.frameWidth - (75*3) + 28, 4 + (69*3) + 28);
		renderer.drawString(gp.world.gameM.getTime24h(), gp.frameWidth - (75*3) + 112, 4 + (71*3) + 26 + (16*3));
		
		renderer.setColour(Colour.WHITE);
		renderer.setFont(font);
		renderer.draw(coinImage, 20,  gp.frameHeight - 48 - 20, 48, 48);
		renderer.drawString(Integer.toString(gp.player.wealth), 20 + 48+8, gp.frameHeight - 48 - 20 +32);
		
		// === Messages ===
		int msgX = 20+20;
		int msgY = gp.frameHeight - 120; // bottom margin
		int lineHeight = 28;

		renderer.setFont(font);

		for (int k = 0; k < messages.size(); k++) {
		    GUIMessage msg = messages.get(k);

		    // Calculate fade alpha
		    float lifeRatio = msg.lifetime / (float) msg.maxLifetime;
		    //int alpha = (int)(255 * lifeRatio);
		    float alpha = lifeRatio;
		    if (alpha < 0) alpha = 0;
		    
		    String text = msg.text;
		    if(msg.username != "") {
		    	text = msg.username + ": " + msg.text;
		    }
		    
		    // Shadow
		    renderer.setColour(new Colour(0, 0, 0, alpha));
		    renderer.drawString(text, msgX, msgY - k * lineHeight + 3);

		    // Main text
		    renderer.setColour(new Colour(msg.color.r, msg.color.g, msg.color.b, alpha));
		    renderer.drawString(text, msgX, msgY - k * lineHeight);
		}
		
		if(!gradingQueue.isEmpty()) {

		    RecipeGrading currentGrading = gradingQueue.peek();

		    drawRecipeGrading(renderer, currentGrading);
		}
		
	}
	public void addOrder(Order order, Customer customer, Renderer renderer) {
	    RecipeRenderData data = buildRenderData(order, customer, font, renderer);
	    renderCache.put(order, data);
	}
	
	public RecipeRenderData buildRenderData(Order order, Customer customer, BitmapFont font, Renderer renderer) {
		RecipeRenderData data = new RecipeRenderData();
		
		Recipe recipe = order.getRecipe();
		data.recipe = recipe;
		data.customer = customer;
		
		// Base images
		data.borderImage = recipe.isCursed ? cursedRecipeBorder : recipeBorder2;
		data.starLevel = recipe.getStarLevel();
		data.mysteryOrderImage = mysteryOrder;
		data.coinImage = coinImage;
		data.plateImage = recipe.finishedPlate;
		data.faceIcon = customer.faceIcon;
		
		float textScale = 0.7f;
		int scale = 2;
		
		// NEW STEP-BASED INGREDIENT SYSTEM
		List<RecipeIngredient> required = recipe.getRequiredIngredients();
		
		for (RecipeIngredient req : required) {
		
		String ingredientName = req.getName();
		
		Food ingredient = (Food) gp.world.itemRegistry
		.getItemFromName(ingredientName, 0);
		
		TextureRegion ingredientImage =
		gp.world.itemRegistry.getImageFromName(ingredientName);
		
		if (ingredient.notRawItem) {
		ingredientImage =
		gp.world.itemRegistry.getRawIngredientImage(ingredientName);
		}
		
		data.ingredientImages.add(ingredientImage);
		
		// Build step icon list
		List<TextureRegion> ingredientStepIcons = new ArrayList<>();
		
		for (CookStep step : req.getRequiredSteps()) {
		TextureRegion icon =
		gp.world.recipeM.getIconFromName(
		step.getStation(),
		recipe.isCursed
		);
		ingredientStepIcons.add(icon);
		}
		
		data.stepIcons.add(ingredientStepIcons);
		}
		
		// Text layout
		renderer.setFont(font);
		for (String line : recipe.getName().split(" ")) {
		
		int offset = (int) ((((64 * scale) / 2)
		- gp.renderer.measureStringWidth(font, line, textScale) / 2));
		
		data.nameLineOffsets.add(offset);
		}
		data.nameLines = wrapText(recipe.getName(), font, textScale);
		
		// Cost
		if (customer instanceof SpecialCustomer specialCustomer) {
		
		if (specialCustomer.hideOrder) {
		data.cost = "???";
		} else {
		data.cost = Integer.toString(
		recipe.getCost(
		gp.world.gameM.isRecipeSpecial(recipe),
		specialCustomer.getMoneyMultiplier()
		)
		);
		}
		
		} else {
		data.cost = Integer.toString(
		recipe.getCost(gp.world.gameM.isRecipeSpecial(recipe))
		);
		}
		
		return data;
	}
	private void drawPatienceBar(Renderer renderer, float worldX, float worldY, int patienceCounter, int maxPatienceTime) {

	    int barWidth = 54 * 2;
	    int barHeight = 6*2;
	    int yOffset = 0;

	    float progress = 1f - (patienceCounter / (float) maxPatienceTime);
	    progress = Math.max(0, Math.min(1, progress));

	    // Green → Orange → Red zones
	    Colour c;
	    if(progress > 0.66f) c = Colour.GREEN;
	    else if(progress > 0.33f) c = Colour.YELLOW;
	    else c = Colour.RED;

	    renderer.setColour(Colour.BLACK);
	    renderer.fillRect((int)worldX, (int)(worldY + yOffset), barWidth, barHeight);

	    renderer.setColour(c);
	    renderer.fillRect((int)worldX, (int)(worldY + yOffset), (int)(barWidth * progress), barHeight);
	}
	private void updateMessages() {
	    for (int i = 0; i < messages.size(); i++) {
	        GUIMessage msg = messages.get(i);
	        msg.lifetime--;
	        if (msg.lifetime <= 0) {
	            messages.remove(i);
	            i--;
	        }
	    }
	}
	private void drawStatisticsScreen(Renderer renderer) {
		
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		
		renderer.setColour(Colour.WHITE);
		renderer.setFont(font);
		String text = "Statistics";
		renderer.drawString(text, getXforCenteredText(text, font), 200);
		
		int y = 200;
		int spacing = 40;
		
		text = "Ingredients Chopped:" + Integer.toString(Statistics.ingredientsChopped);
		y+=spacing;
		renderer.drawString(text, getXforCenteredText(text, font), y);
		
		text = "Customers Served:" + Integer.toString(Statistics.servedCustomers);
		y+=spacing;
		renderer.drawString(text, getXforCenteredText(text, font), y);
		
		text = "Kitchen Upgrades:" + Integer.toString(Statistics.kitchenUpgradeCount);
		y+=spacing;
		renderer.drawString(text, getXforCenteredText(text, font), y);
		
		text = "Decorations Placed:" + Integer.toString(Statistics.decorationsPlaced);
		y+=spacing;
		renderer.drawString(text, getXforCenteredText(text, font), y);
		
		
		
		text = "Back";
		int x =getXforCenteredText(text, font);
		if(isHovering(text, x, 640-24, font)) {
			renderer.setColour(craftColour1);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					//QUIT
					gp.currentState = gp.pauseState;
					clickCooldown = 0.33;
				}
			}
	}else {
		renderer.setColour(Colour.WHITE);
	}
	renderer.drawString(text, getXforCenteredText(text, font), 640);
	
	}
	private void drawPauseScreen(Renderer renderer) {
		
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		renderer.setColour(Colour.WHITE);
		renderer.setFont(font);
		String text = "PAUSED";
		renderer.drawString(text, getXforCenteredText(text, font), 200);
		
		text = "Resume";
		int x =getXforCenteredText(text, font);
		if(isHovering(text, x, 400-24, font)) {
			renderer.setColour(craftColour1);
			if(gp.mouseL.mouseButtonDown(0)) {
				//RESUME
				gp.currentState = 1;
			}
		}
		renderer.setFont(font);
		renderer.drawString(text, getXforCenteredText(text, font), 400);
		
		text = "Settings";
		x =getXforCenteredText(text, font);
		if(isHovering(text, x, 460-24, font)) {
			renderer.setColour(craftColour1);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					//SETTINGS
					gp.currentState = gp.settingsState;
					settingsState = 0;
					clickCooldown = 0.33;
				}
			}
		}else {
			renderer.setColour(Colour.WHITE);
		}
		renderer.drawString(text, getXforCenteredText(text, font), 460);
		
		text = "Achievements";
		x =getXforCenteredText(text, font);
		if(isHovering(text, x, 520-24, font)) {
			renderer.setColour(craftColour1);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					gp.currentState = gp.achievementState;
					clickCooldown = 0.33;
					currentTitleAnimation = 0;
				}
			}
		} else { 
			renderer.setColour(Colour.WHITE);
		}
			renderer.drawString(text, getXforCenteredText(text, font), 520);
			
			text = "Statistics";
			x =getXforCenteredText(text, font);
			if(isHovering(text, x, 580-24, font)) {
				renderer.setColour(craftColour1);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						gp.currentState = gp.statisticsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 0;
					}
				}
			} else {
				renderer.setColour(Colour.WHITE);
			}
				renderer.drawString(text, getXforCenteredText(text, font), 580);
			
			text = "Quit";
			x =getXforCenteredText(text, font);
			if(isHovering(text, x, 640-24, font)) {
				renderer.setColour(craftColour1);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//QUIT
						gp.currentState = gp.titleState;
						currentTitleAnimation = 0;
						if(gp.multiplayer) {
							if(gp.discovery != null) {
								gp.discovery.shutdown();
							}
							if (gp.serverHost) {
								gp.serverHost = false;
							    gp.socketServer.shutdown();
							}
							gp.joiningServer = false;
							gp.disconnect();
							gp.multiplayer = false;
						}
						/*
						if(gp.multiplayer) {
							gp.discovery.shutdown();
							Packet01Disconnect loginPacket = new Packet01Disconnect(gp.player.getUsername());
					        if(gp.socketServer != null) {
					            gp.socketServer.removeConnection(loginPacket);
					        }
					        if(gp.serverHost) {
					        	gp.stopHosting();
					        }
					        loginPacket.writeData(gp.socketClient);
						}
						*/
					}
				}
		}else {
			renderer.setColour(Colour.WHITE);
		}
		renderer.drawString(text, getXforCenteredText(text, font), 640);
		
		
		int mouseX = (int)gp.mouseL.getScreenX();
		int mouseY = (int)gp.mouseL.getScreenY();
		
		x = gp.frameWidth - 74;
		int y = 20;
		int i = 0;
		if(gp.currentState == gp.recipeState) {
			i = 1;
		}
		
		if(mouseX > x && mouseX < x+64 && mouseY > y && mouseY < y+64) {
			if(gp.currentState == gp.recipeState) {
				i = 3;
			} else {
				i = 2;
			}
			if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
				if(gp.currentState == gp.recipeState) {
					gp.currentState = gp.pauseState;
				} else {
					gp.currentState = gp.recipeState;
				}
				clickCooldown = 0.33;
			}
		}
		
		renderer.draw(bookIcons[i], x, y, 64, 64);
		
		
	}	
	private void drawBaseSettingsScreen(Renderer renderer) {
		
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		renderer.draw(settingsFrame, x, y, 112*3, 112*3);
		
		//Categories
		renderer.setFont(font);
		x += 40;
		y += 140;
		
		renderer.setColour(titleColour1);
		String text = "General";
		if (isHovering(text, x, y-24, font)) {
            renderer.setColour(titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = generalState;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, x-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(text, x, y);
		
		renderer.setColour(titleColour1);
		text = "Video";
		y+= 40;
		if (isHovering(text, x, y-24, font)) {
            renderer.setColour(titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = videoState;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, x-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(text, x, y);
		
		renderer.setColour(titleColour1);
		text = "Audio";
		y+= 40;
		if (isHovering(text, x, y-24, font)) {
            renderer.setColour(titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = audioState;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, x-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(text, x, y);
		
		if(gp.multiplayer) {
			renderer.setColour(titleColour1);
			text = "Multiplayer";
			y+= 40;
			if (isHovering(text, x, y-24, font)) {
	            renderer.setColour(titleColour2);
	            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                settingsState = multiplayerState;
	                clickCooldown = 0.33;
	            }
	    		renderer.draw(underline, x-60, y-16, 80*3, 16*3);
	        }
			renderer.drawString(text, x, y);
		}
		
		renderer.setColour(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, font)) {
            renderer.setColour(titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                gp.currentState = gp.pauseState;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, 512-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(text, x, y);
		
	}	
	private void drawGeneralSettings(Renderer renderer) {
		
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		renderer.draw(generalSettingsFrame, x, y, 112*3, 112*3);
		
		String text;
		renderer.setFont(font);
		x += 40;
		y += 140;
		
		renderer.setColour(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, font)) {
            renderer.setColour(titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, 512-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(text, x, y);
		
	}
	private void drawVideoSettings(Renderer renderer) {
	    renderer.setColour(darkened);
	    renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);

	    renderer.draw(videoSettingsFrame,
	            gp.frameWidth / 2 - ((112 * 3) / 2),
	            gp.frameHeight / 2 - ((112 * 3) / 2),
	            112 * 3,
	            112 * 3);

	    renderer.setFont(font);

	    renderer.setColour(titleColour1);

	    // Draw up to 4 checkboxes per page
	    int start = videoScrollIndex;
	    int end = Math.min(start + VIDEO_PAGE_SIZE, videoCheckBoxes.size());

	    int y = 320;

	    for (int i = start; i < end; i++) {
	        CheckBox cb = videoCheckBoxes.get(i);

	        cb.y = y+spacing;
	        cb.draw(renderer);

	        y += 40; // spacing between options
	    }

	    String backText = "Back";
	    int backX = 542;
	    int backY = 510;

	    renderer.setColour(titleColour1);

	    if (isHovering(backText, backX, backY - 24, font)) {
	        renderer.setColour(titleColour2);

	        if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	            settingsState = baseSettings;
	            clickCooldown = 0.33;
	        }

	        renderer.draw(underline, 512 - 60, backY - 16, 80 * 3, 16 * 3);
	    }

	    renderer.drawString(backText, backX, backY);
	}
	private void drawAudioSettings(Renderer renderer) {
		
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		renderer.draw(audioSettingsFrame, x, y, 112*3, 112*3);
		
		String text;
		x += 40;
		y += 140;
		
		
		text = "Back";
		x = 542;
		y = 510;
		Colour c = titleColour1;
		if (isHovering(text, x, y-24, font)) {
            c = (titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, 512-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(font, text, x, y, 1.0f, c);
	}
	private void drawMultiplayerSettings(Renderer renderer) {
		int boxOffset = 220;
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		renderer.draw(multiplayerSettingsFrame, x, y, 112*3, 112*3);
		
		String text;
		x += 40;
		y += 140;
		
		
		text = "Show Usernames";
		renderer.drawString(font, text, x, y, 1.0f, titleColour1);
		y-= 20;
		if(Settings.showUsernames) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	Settings.showUsernames = !Settings.showUsernames;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		text = "Back";
		x = 542;
		y = 510;
		Colour c = titleColour1;
		if (isHovering(text, x, y-24, font)) {
            c = (titleColour2);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		renderer.draw(underline, 512-60, y-16, 80*3, 16*3);
        }
		renderer.drawString(font, text, x, y, 1.0f, c);
	}
	public void drawCatalogueState(Renderer renderer) {
		
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		renderer.draw(computerAnimations[computerAnimationCounter], 0, 0, (int)(260*4.5), (int)(190*4.5));
		
		if(computerAnimationCounter >= 9) {
			if(!gp.world.catalogue.checkingOut && !gp.world.catalogue.onMysteryScreen && !gp.world.catalogue.onCatalogueScreen) {
				renderer.draw(shoppingUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				renderer.draw(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.leftPage();
					}
				}
				renderer.draw(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.checkingOut = true;
						gp.world.catalogue.layer = 0;
					}
				}
				
				renderer.draw(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.onMysteryScreen = true;
						gp.world.catalogue.layer = 0;
						gp.world.catalogue.pageNum = 1;
					}
				}
				
				renderer.draw(catalogueButton, (int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.onCatalogueScreen = true;
						gp.world.catalogue.layer = 0;
						gp.world.catalogue.pageNum = 1;
					}
				}
				
			} else if(gp.world.catalogue.onMysteryScreen) {
				renderer.draw(mysteryCrateUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				if(isHovering((int)(87*4.5), (int)(108*4.5), (int)(61*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.buyMysteryCrate();
						gp.world.catalogue.layer = 0;
					}
				}
				
				renderer.draw(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.onMysteryScreen = false;
						gp.world.catalogue.layer = 0;
					}
				}
			} else if(gp.world.catalogue.onCatalogueScreen) {
				
				renderer.draw(catalogueMenu, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				renderer.draw(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.leftPage();
					}
				}
				renderer.draw(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.checkingOut = true;
						gp.world.catalogue.layer = 0;
					}
				}
				
				renderer.draw(catalogueButton, (int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.onCatalogueScreen = false;
						gp.world.catalogue.layer = 0;
						gp.world.catalogue.pageNum = 1;
					}
				}
				
			} else {
				renderer.draw(basketUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(basketButtons, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				//CHECKOUT
				if(isHovering((int)(146*4.5), (int)(96*4.5), (int)(42*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.world.catalogue.tryPay();
					}
				}
			}
			
			if(!gp.world.catalogue.checkingOut && !gp.world.catalogue.onMysteryScreen && !gp.world.catalogue.onCatalogueScreen) {
				gp.world.catalogue.drawCatalogue(renderer);
			} else if(gp.world.catalogue.onMysteryScreen) {
				gp.world.catalogue.drawMysteryScreen(renderer);
			} else if(gp.world.catalogue.onCatalogueScreen) {
				gp.world.catalogue.drawShopCatalogueScreen(renderer);
			} else {
				gp.world.catalogue.drawCheckout(renderer);
			}
			
		}
		
	}
	public void drawXPScreen(Renderer renderer) {
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		if (animatingSouls) {
		    if (displayedSouls < targetSouls) {
		        soulCounter++;
		        if(soulCounter >= 6) {
			        displayedSouls ++;
			        soulCounter = 0;
		        }
		    } else {
		        displayedSouls = targetSouls;
		        animatingSouls = false;
		    }
		}
		if (displayedSouls >= gp.player.nextLevelAmount) {
			levelUp = true;
		}
		
		if(displayedSouls == gp.player.soulsServed) {
			if(!levelUp) {
				if(gp.mouseL.mouseButtonDown(0)) {
					gp.currentState = gp.playState;
				}
			}
		}
		
		if(levelUp) {
			if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
				levelUp = false;
				clickCooldown = 0.33;
				gp.player.level++;
				gp.world.progressM.handleLevelUp(gp.player.level);
			}
		}
		
		drawLevelRoadmap(renderer);
		
	}
	private void drawLevelRoadmap(Renderer renderer) {
	    int centerX = gp.frameWidth / 2;
	    int lineY = gp.frameHeight / 2; 
	    int soulWidth = 6 * 3;
	    int soulHeight = 20 * 3;
	    int nodeSize = 32 * 3;
	    int nodeYOffset = 0; // adjust to lift nodes above the bar if needed

	    int totalLevels = gp.world.progressM.totalLevels;
	    int[] soulsPerLevel = new int[totalLevels];
	    for (int i = 0; i < totalLevels; i++) {
	        soulsPerLevel[i] = gp.player.nextLevelAmount; // replace with per-level array if available
	    }

	    // Player's current soul index
	    int accumulatedSouls = 0;
	    for (int i = 1; i < gp.player.level; i++) { // start at 1, stop before current level
	        accumulatedSouls += soulsPerLevel[i - 1];
	    }
	    int playerSoulIndex = accumulatedSouls + displayedSouls;
	    // Smooth scroll
	    float targetOffsetX = centerX - playerSoulIndex * soulWidth;
	    gp.world.progressM.roadmapOffsetX += (targetOffsetX - gp.world.progressM.roadmapOffsetX) * 0.1f;

	    // Draw souls as roadmap line
	    int soulCounter = 0;
	    for (int level = 0; level < totalLevels; level++) {
	        int soulsInThisLevel = soulsPerLevel[level];
	        for (int s = 0; s < soulsInThisLevel; s++, soulCounter++) {
	            int x = (int)(soulCounter * soulWidth + gp.world.progressM.roadmapOffsetX);
	            if (x + soulWidth < 0 || x > gp.frameWidth) continue;

	            TextureRegion img;

	            boolean isFirstSoulOverall = (soulCounter == 0);
	            boolean isLastSoulInLevel = (s == soulsInThisLevel - 1);

	            if (isFirstSoulOverall) img = (soulCounter < playerSoulIndex) ? leftProgress2 : leftProgress1;
	            else if (isLastSoulInLevel) img = (soulCounter < playerSoulIndex) ? rightProgress2 : rightProgress1;
	            else img = (soulCounter < playerSoulIndex) ? middleProgress2 : middleProgress1;

	            renderer.draw(img, x, lineY - soulHeight / 2, img.getPixelWidth() * 3, img.getPixelHeight() * 3);
	        }
	    }

	    // Draw nodes aligned with last soul of each level
	    accumulatedSouls = 0;
	    
	    for (int level = 1; level <= totalLevels; level++) {
	        int nodeSoulIndex = accumulatedSouls + soulsPerLevel[level - 1] - 1;
	        int x = (int)(nodeSoulIndex * soulWidth + gp.world.progressM.roadmapOffsetX - nodeSize / 2);
	        int y = lineY - nodeSize / 2;

	        if (!(x + nodeSize < 0 || x > gp.frameWidth)) {
	        	TextureRegion icon;
	        	if (level+1 <= gp.player.level) {
	        	    icon = gp.world.progressM.levelRewards[level - 1][1]; // passed/completed
	        	} else {
	        	    icon = gp.world.progressM.levelRewards[level - 1][0]; // normal/unreached
	        	}
	        	renderer.draw(icon, x, y, nodeSize, nodeSize);
	        }
	                
	        String lvlText = "" + level;
	        Colour c;
	        int textWidth = (int)font.getTextWidth(lvlText);
	        if (level+1 == gp.player.level) {
	            c = (Colour.YELLOW);
	        } else {
	        	c = (Colour.WHITE);
	        }
	        renderer.drawString(font, lvlText, x + nodeSize / 2 - textWidth / 2, y + nodeSize + 16, 1.0f, c);

	        accumulatedSouls += soulsPerLevel[level - 1];
	    }
	}
	private void drawRecipeSelectScreen(Renderer renderer) {
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);

		if (gp.world.progressM.getRecipeChoices() == null) {
		    // fallback in case something went wrong
		    recipeChoices = RecipeManager.getTwoRandomLocked(gp);
		} else {
		    recipeChoices = gp.world.progressM.getRecipeChoices();
		}
		
		String text = "Choose New Recipe!";
		int x = getXforCenteredText(text, font);
		int y = 100;
		renderer.drawString(font, text, x, y, 1.0f, Colour.WHITE);
		
	    x = 400;
	    y = gp.frameHeight/2 - 100;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	    		RecipeManager.unlockRecipe(recipeChoices[0]);
	    		gp.currentState = gp.playState;
	    		gp.world.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(renderer, recipeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	       		RecipeManager.unlockRecipe(recipeChoices[1]);
	    		gp.currentState = gp.playState;
	    		gp.world.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(renderer, recipeChoices[1], x, y);
	}
	private void drawChooseUpgradeScreen(Renderer renderer) {
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		upgradeChoices = gp.world.progressM.getUpgradeChoices();
		
		String text = "Choose Upgrade!";
		switch(upgradeChoices[0].getCategory()) {
			case RECIPE, BASIC:
				break;
			case COSMETIC:
				text = "Choose New Decorations";
				break;
			case KITCHEN:
				text = "Choose Kitchen Upgrade";
				break;
		}
		int x = getXforCenteredText(text, font);
		int y = 100;
		renderer.drawString(font, text, x, y, 1.0f, Colour.WHITE);

	    x = 200;
	    y = gp.frameHeight/2 - 100;
	    if(isHovering(x, y, 130*3, 48*3)) {
	    	if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	    		UpgradeManager.unlockUpgrade(upgradeChoices[0]);
	    		if(upgradeChoices[0].getCategory() == RewardType.KITCHEN) {
	    			gp.world.progressM.checkKitchenUpgrade();
	    		}
	    		clickCooldown = 0.33;
	    		gp.currentState = gp.chooseRecipeState;
	    	}
	    }
	    drawUpgrade(renderer, upgradeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 130*3, 48*3)) {
	    	if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	    		UpgradeManager.unlockUpgrade(upgradeChoices[1]);
	    		clickCooldown = 0.33;
	    		if(upgradeChoices[1].getCategory() == RewardType.KITCHEN) {
	    			gp.world.progressM.checkKitchenUpgrade();
	    		}
	    		gp.currentState = gp.chooseRecipeState;
	    	}
	    }
	    drawUpgrade(renderer, upgradeChoices[1], x, y);
	}
    private void drawUpgrade(Renderer renderer, Upgrade upgrade, int x, int y) {
		// BASE
		renderer.draw(saveBorder, x, y, 130 * 3, 48 * 3);

		renderer.drawString(font, upgrade.getName(), x + 30, y + 40, 1.0f, titleColour1);
		
		int counter = 0;
		for(String line: upgrade.getDescription().split("/n")) {
            renderer.drawString(font, line, x + 30, y + 70 + counter, 1.0f, orderTextColour);
            counter += 18;
        }
		
		TextureRegion img = upgrade.getImage();
		renderer.draw(img, x+ 130*2+60 - upgrade.xOffset, y+40+24 - upgrade.yOffset, img.getPixelWidth()*3, img.getPixelHeight()*3);
		
    }
    private void drawRecipe(Renderer renderer, Recipe recipe, int x, int y) {
		// BASE
		renderer.draw(recipeBorder, x, y, 32 * 3, 48 * 3);
		/*
		// INGREDIENT IMAGES
		List<String> ingredients = recipe.getIngredients();
		List<String> cookingState = recipe.getCookingStates();
		List<String> secondaryCookingState = recipe.getSecondaryCookingStates();
		for (int j = 0; j < ingredients.size(); j++) {
			String ingredientName = ingredients.get(j);
			TextureRegion ingredientImage = gp.world.itemRegistry.getImageFromName(ingredientName);
			Food ingredient = (Food)gp.world.itemRegistry.getItemFromName(ingredientName, 0);
			if(ingredient.notRawItem) {
				ingredientImage = gp.world.itemRegistry.getRawIngredientImage(ingredientName);
			}
			if (ingredientImage != null) {
				// Draw each ingredient image 32px apart above the order box
				renderer.draw(ingredientImage, x + j * (10*3) + 4, y + 4, 10*3, 10*3);
				renderer.draw(gp.world.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3);
				renderer.draw(gp.world.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3);
			}
		}
		*/
		
		// NAME
		int counter = 0;
		for(String line: recipe.getName().split(" ")) {
            renderer.drawString(font, line, x + (48 - getTextWidth(line, font) / 2.0f), y + 84 + counter, 1.0f, orderTextColour);
            counter += 15;
        }
		// PLATE IMAGE
		renderer.draw(recipe.finishedPlate, x + 24, y + 94, 48, 48);
		
		renderer.draw(coinImage, x, y + 94 + 48, 48, 48);
		
		renderer.drawString(font, Integer.toString(recipe.getCost(gp.world.gameM.isRecipeSpecial(recipe))), x + 48+8, y + 94 + 48+32, 1.0f, Colour.WHITE);

    }
	public void startLevelUpScreen() {
		gp.currentState = gp.xpState;
		displayedSouls = gp.world.gameM.previousSoulsCollected;
		targetSouls = gp.player.soulsServed;
		animatingSouls = true;
		soulCounter = 0;
	}
	public void resetComputerAnimations() {
		computerAnimationSpeed = 0;
		computerAnimationCounter = 0;
	}
	public void addChatMessage(String username, String message) {
	    if(messages.size() >= maxMessages) {
	    	messages.remove(0); // remove oldest message
	    }
	    GUIMessage msg = new GUIMessage(message, username);
	    chatMessages.add(msg);
	    //messages.add(msg);
	}
	public void addMessageFromPacket(String username, String message) {
	    if(messages.size() >= maxMessages) {
	    	messages.remove(0); // remove oldest message
	    }
	    GUIMessage msg = new GUIMessage(message, username);
	    chatMessages.add(msg);
	    messages.add(msg);
	}
	public void addMessage(String text) {
	    addMessage(text, 320, Colour.YELLOW);
	}
	public void addMessage(String text, Colour Colour) {
	    addMessage(text, 320, Colour);
	}
	public void addMessage(String text, int lifetime, Colour colour) {
		GUIMessage msg = new GUIMessage(text, lifetime, colour);
	    messages.add(msg);
	    chatMessages.add(msg);
	}
	public void update(double dt) {
		
		if (clickCooldown > 0) {
			clickCooldown -= dt;        // subtract elapsed time in seconds
		    if (clickCooldown <= 0) {
		    	clickCooldown = 0;      // clamp to zero
		    }
		}
		
		if(gp.currentState == gp.playState) {
			if(!gradingQueue.isEmpty()) {

			    RecipeGrading grading = gradingQueue.peek();

			    grading.displayTimer += dt;

			    if(grading.displayTimer >= 15.0f) {
			        gradingQueue.poll();
			    }
			}
			updateGradingAnimation(dt);
		}
		
		if(gp.currentState == gp.writeUsernameState) {
		    usernameBox.update(dt);
		} 
		if(gp.currentState == gp.chatState) {
			chatBox.update(dt);
		}
		if(gp.currentState == gp.createWorldScreen) {
			playerNameBox.update(dt);
			worldNameBox.update(dt);
		}
		if(gp.currentState == gp.createJoinPlayerScreen) {
			playerNameBox.update(dt);
		}
		if (gp.currentState == gp.settingsState && settingsState == videoState) {

		    if (gp.keyL.keyBeginPress(GLFW.GLFW_KEY_S)) {

		        videoScrollIndex += VIDEO_PAGE_SIZE;

		        int maxIndex = Math.max(0, videoCheckBoxes.size() - VIDEO_PAGE_SIZE);

		        if (videoScrollIndex > maxIndex) {
		            videoScrollIndex = maxIndex;
		        }
		    }

		    if (gp.keyL.keyBeginPress(GLFW.GLFW_KEY_W)) {

		        videoScrollIndex -= VIDEO_PAGE_SIZE;

		        if (videoScrollIndex < 0) {
		            videoScrollIndex = 0;
		        }
		    }
		}
		
		if(gp.currentState == gp.catalogueState) {
			computerAnimationSpeed+=dt;
			if(computerAnimationSpeed >= 0.1) {
				computerAnimationSpeed = 0;
				computerAnimationCounter++;
			}
			
			if(computerAnimations[computerAnimationCounter] == null) {
				computerAnimationCounter--;
			}
		} else if(gp.currentState == gp.recipeState) {
			if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_D)) {
			    currentPage++;
			}

			if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_A)) {
			    currentPage--;
			}

			// Clamp page
			if (currentPage < 0) currentPage = 0;
			if (currentPage > 1) currentPage = 1;
		} else if(gp.currentState == gp.achievementState) {
		    // --- Handle scrolling ---
		    if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_S) && clickCooldown == 0) {
		        if (achievementStartIndex + 2 < gp.world.progressM.achievements.size()) {
		            achievementStartIndex++;
		            clickCooldown = 0.1;
		        }
		    }
		    if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_W) && clickCooldown == 0) {
		        if (achievementStartIndex > 0) {
		            achievementStartIndex--;
		            clickCooldown = 0.1;
		        }
		    }
		}
		   if (notificationAchievement != null) {
		        notificationTimer -= dt;
		        if (notificationTimer <= 0) {
		            notificationAchievement = null; // hide notification
		        }
		    }
		
	}
	public List<String> wrapText(String text, BitmapFont font, float maxWidth) {
	    List<String> lines = new ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder currentLine = new StringBuilder();

	    for (String word : words) {
	        String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

	        float lineWidth = font.getTextWidth(testLine);

	        if (lineWidth > maxWidth) {
	            // Commit the current line
	            if (currentLine.length() > 0) {
	                lines.add(currentLine.toString());
	            }
	            // Start new line with the long word
	            currentLine = new StringBuilder(word);
	        } else {
	            currentLine = new StringBuilder(testLine);
	        }
	    }

	    // Add final line
	    if (currentLine.length() > 0) {
	        lines.add(currentLine.toString());
	    }

	    return lines;
	}
	public List<String> wrapText(String text, BitmapFont font, float maxWidth, float textScale) {
	    List<String> lines = new ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder currentLine = new StringBuilder();

	    for (String word : words) {
	        String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

	        float lineWidth = font.getTextWidth(testLine, textScale);

	        if (lineWidth > maxWidth) {
	            // Commit the current line
	            if (currentLine.length() > 0) {
	                lines.add(currentLine.toString());
	            }
	            // Start new line with the long word
	            currentLine = new StringBuilder(word);
	        } else {
	            currentLine = new StringBuilder(testLine);
	        }
	    }

	    // Add final line
	    if (currentLine.length() > 0) {
	        lines.add(currentLine.toString());
	    }

	    return lines;
	}
	public void addRecipeGrading(Plate plate) {
		
	    Recipe recipe =
	            RecipeManager.getMatchingRecipe(
	                    plate.getPreparedIngredients());

	    if (recipe == null)
	        return;
	    
	    double finalScore = plate.getFinalScore();

	    String finalGrade = getFinalGrade(finalScore);

	    RecipeGrading grading =
	            new RecipeGrading(recipe,
	                    recipe.getName(),
	                    recipe.finishedPlate,
	                    finalScore,
	                    finalGrade,
	                    plate.getIngredientScores());

	    gradingQueue.add(grading);
	    
	    animQueue.clear();
	    animIndex = 0;

	    for (IngredientScore ingredient : grading.getIngredientScores()) {
	        for (IngredientScore.ActionScore action : ingredient.getActions()) {
	            animQueue.add(new ActionAnim(action));
	        }
	    }

	    animating = true;
	}
	public void updateGradingAnimation(double dt) {
		if(currentRecipe == null) {
			return;
		}
		
	    if (!animating && !finalGradeShown) {
	        double target = currentRecipe.getFinalScore();
	        double diff = target - finalGradeDisplay;

	        finalGradeDisplay += diff * Math.min(1.0, dt * 3.5);
	        
	        if (Math.abs(diff) < 0.5) {
	            finalGradeDisplay = target;
	            finalGradeShown = true;
	        }
	    }

	    if (!animating) return;
	    

	    if (animIndex >= animQueue.size()) {
	        animating = false;
	        return;
	    }

	    ActionAnim current = animQueue.get(animIndex);

	    double target = current.action.quality;

	    double t = current.animationTime / duration;
	    t = Math.min(1.0, t);

	    double eased = 1.0 - Math.pow(1.0 - t, 5);

	    current.displayedQuality = target * eased;

	    current.animationTime += dt;

	    // completion threshold
	    if(t >= 1.0) {
	    	current.displayedQuality = target;
	    	current.finished = true;
	    	animIndex++;
	    }
	}
	public Colour getScoreColourSmooth(double score) {

	    for (int i = 0; i < SCORE_THRESHOLDS.length - 1; i++) {

	        double a = SCORE_THRESHOLDS[i];
	        double b = SCORE_THRESHOLDS[i + 1];

	        if (score <= b) {

	            double t = (score - a) / (b - a);

	            return lerpColour(
	                SCORE_COLOURS[i],
	                SCORE_COLOURS[Math.min(i + 1, SCORE_COLOURS.length - 1)],
	                t
	            );
	        }
	    }

	    return SCORE_COLOURS[SCORE_COLOURS.length - 1];
	}
	public Colour lerpColour(Colour c1, Colour c2, double t) {

	    t = Math.max(0.0, Math.min(1.0, t));

	    return new Colour(
	        (float)(c1.r + (c2.r - c1.r) * t),
	        (float)(c1.g + (c2.g - c1.g) * t),
	        (float)(c1.b + (c2.b - c1.b) * t),
	        (float)(c1.a + (c2.a - c1.a) * t)
	    );
	}
	public void drawRecipeGrading(Renderer renderer, RecipeGrading recipeGrading) {

	    if (recipeGrading == null)
	        return;
	    
	    currentRecipe = recipeGrading;

	    int panelX = 0;
	    int panelY = 100;
	    int scale = 3;

	    int panelWidth = 340;

	    int panelHeight = 100 + (recipeGrading.getIngredientScores().size() * 44);

	    //renderer.setColour(new Colour(0, 0, 0, 180));
	    //renderer.fillRect(panelX, panelY, panelWidth, panelHeight);

	    renderer.setColour(Colour.WHITE);

	    // Recipe image
	    renderer.draw(
	        recipeGrading.getRecipe().finishedPlate,
	        panelX + 8,
	        panelY + 8,
	        16 * scale,
	        16 * scale
	    );

	    // Recipe name
	    renderer.drawString(
	        recipeGrading.getRecipe().getName(),
	        panelX + 64,
	        panelY + 18 + 8 * scale
	    );


	    int rowY = panelY + 70;

	    for (IngredientScore ingredient : recipeGrading.getIngredientScores()) {

	        int x = panelX + 10;

	        // Ingredient name
	        renderer.drawString(
	            ingredient.getIngredientName(),
	            x,
	            rowY + 12
	        );

	        x += 110;

	        // =========================
	        // ACTIONS (FULLY GENERIC)
	        // =========================
	        for (IngredientScore.ActionScore action : ingredient.getActions()) {

	            // icon per action type (future-proof)
	            renderer.draw(
	                getCookMethodIcon(action.action),
	                x,
	                rowY - 8 * scale,
	                16 * scale,
	                16 * scale
	            );

	            // grade per action
	            renderer.draw(
	                getGradeFrame(action.grade),
	                x+24*scale,
	                rowY -8*scale,
	                64 * scale,
	                16 * scale
	            );
		        
		        int barX = x+24*scale + 3;
		        int barY = rowY-8*scale + 15;
		        int barWidth = 40*scale;
		        int barHeight = 6*scale;
		        ActionAnim anim = getAnim(action);

		        double visual = toVisualScore(anim.displayedQuality);
		        double percent = visual / 100.0;
		        
		        renderer.setColour(getScoreColourSmooth(visual));
		        renderer.fillRect(barX, barY, (int)(barWidth * percent), barHeight);
		        
		        renderer.draw(
		                gradingFrameOverlay,
		                x+24*scale,
		                rowY -8*scale,
		                64 * scale,
		                16 * scale
		            );
		        

		        if (anim.finished) {
		        	renderer.setColour(Colour.WHITE);
		            renderer.drawString(action.grade,
		            		x+24*scale + 41*scale,
			                rowY -8*scale + 10*scale
		            );
		        }

	            x += 40;
	        }

	        rowY += 42;
	    }
	    if (finalGradeShown) {
	        renderer.drawString(
	            getFinalGrade(finalGradeDisplay),
	            panelX + panelWidth - 48,
	            panelY + 18 +scale*40
	        );
	    }
	    
	}
	private ActionAnim getAnim(IngredientScore.ActionScore action) {
	    for (ActionAnim a : animQueue) {
	        if (a.action == action) return a;
	    }
	    return null;
	}
	public TextureRegion getCookMethodIcon(String method) {

	    switch(method) {

	        case "Chopping Board":
	            return choppedIcon;

	        case "Frying Pan":
	            return PanIcon;

	        case "Fryer":
	            return fryerIcon;

	        case "Oven":
	            return ovenIcon;

	        case "Small Pot":
	            return PotIcon;
	    }
	    
	    return choppedIcon;
	}
	public TextureRegion getGradeFrame(String grade) {
		
	    switch(grade) {
	        case "S": return sFrame;
	        case "A": return aFrame;
	        case "B": return bFrame;
	        case "C": return cFrame;
	        case "D": return dFrame;
	        default: return fFrame;
	    }
	}
	public String getFinalGrade(double score) {
		System.out.println("Final Score: " + score);

	    if (score >= 98) return "S+";
	    if (score >= 95) return "S";
	    if (score >= 93) return "S-";
	    if (score >= 92) return "A+";
	    if (score >= 90) return "A";
	    if (score >= 88) return "A-";
	    if (score >= 86) return "B+";
	    if (score >= 83) return "B";
	    if (score >= 80) return "B-";
	    if (score >= 78) return "C+";
	    if (score >= 75) return "C";
	    if (score >= 70) return "C-";
	    if (score >= 68) return "D+";
	    if (score >= 65) return "D";
	    if (score >= 62) return "D-";

	    return "F";
	}
	public void setDialogue(String message, NPC npc) {
		this.currentDialogue = message;
		this.currentTalkingNPC = npc;
        gp.currentState = gp.dialogueState;
        this.charIndex = 0;
        this.displayedText = "";
        this.finishedTyping = false;
        this.lastCharTime = System.currentTimeMillis();
	}
	public void drawDialogueState(Renderer renderer) {
	    int width = 384 * 3;
	    int height = 126 * 3;
	    int x = (gp.frameWidth - width) / 2;
	    int y = gp.frameHeight - height + 200;

	    // Draw dialogue frame
	    renderer.draw(dialogueFrame, x, y, width, height);

	    // Font setup

	    // Typing effect update
	    long now = System.currentTimeMillis();
	    if (!finishedTyping && currentDialogue != null) {
	        if (now - lastCharTime > CHAR_DELAY && charIndex < currentDialogue.length()) {
	            charIndex++;
	            displayedText = currentDialogue.substring(0, charIndex);
	            lastCharTime = now;
	        }

	        if (charIndex >= currentDialogue.length()) {
	            finishedTyping = true;
	        }
	    }

	    // Draw dialogue text
	    int textX = x + 30;
	    int textY = y + 50;

	    if (displayedText != null) {
	        List<String> lines = wrapText(displayedText, font, width - 60);
	        for (String line : lines) {
	            renderer.drawString(font, line, textX, textY, 1.0f, titleColour1);
	            textY += font.getLineHeight() + 2;
	        }
	    }

	    // Draw NPC name
	    if (currentTalkingNPC != null && currentTalkingNPC.getName() != null && !currentTalkingNPC.getName().isEmpty()) {
	        renderer.drawString(font, currentTalkingNPC.getName(), x + 20, y - 10, 1.0f, Colour.WHITE);
	        
	        renderer.draw(currentTalkingNPC.portrait, x + 20 - 24, y + 110, 32*3, 32*3);
	    }

	    // Handle click: skip or finish
	    if (gp.mouseL.mouseButtonDown(0)) {
	        if (!finishedTyping) {
	            // Finish instantly
	            displayedText = currentDialogue;
	            charIndex = currentDialogue.length();
	            finishedTyping = true;
	        } else {
	            // Move to next state / close dialogue
	            gp.currentState = gp.playState;
	        }
	    }
	}
	public double toVisualScore(double score) {

	    for (int i = 0; i < SCORE_THRESHOLDS.length - 1; i++) {

	        double a = SCORE_THRESHOLDS[i];
	        double b = SCORE_THRESHOLDS[i + 1];

	        if (score <= b) {

	            double t = (score - a) / (b - a);

	            double segmentSize = 100.0 / (SCORE_THRESHOLDS.length - 1);

	            double base = i * segmentSize;

	            return base + t * segmentSize;
	        }
	    }

	    return 100.0;
	}
	public void drawCheckBoxHover(Renderer renderer, int x, int y, int w, int h) {
	    int size = 16 * 3;

	    // Top-left
	    renderer.draw(highlight3, x, y, size, size);

	    // Top-right
	    renderer.draw(highlight4, x + w - size, y, size, size);

	    // Bottom-left
	    renderer.draw(highlight2, x, y + h - size, size, size);

	    // Bottom-right
	    renderer.draw(highlight1, x + w - size, y + h - size, size, size);
	}
	public void drawUnderlinedText(Renderer renderer, String text,int x,int y,float fontScale) {
	
	// Draw text
	renderer.drawString(font, text, x, y, fontScale, titleColour1);
	
	// Get text width
	float textWidth = font.getTextWidth(text, fontScale);
	
	// Padding around text
	float paddingLeft = titleUIScale * 16;
	float paddingRight = titleUIScale * 16;
	
	// Total underline width
	float totalWidth = textWidth + paddingLeft + paddingRight;
	
	// Underline position
	float lineX = x - paddingLeft;
	float lineY = y - titleUIScale * 9;
	
	// Draw left cap
	renderer.draw(bookLine1Start,
	lineX,
	lineY,
	titleUIScale * 16,
	titleUIScale * 16);
	
	// Draw stretched middle
	renderer.draw(bookLine1,
	lineX + titleUIScale * 16,
	lineY,
	totalWidth - (titleUIScale * 32),
	titleUIScale * 16);
	
	// Draw right cap
	renderer.draw(bookLine1End,
	lineX + totalWidth - titleUIScale * 16,
	lineY,
	titleUIScale * 16,
	titleUIScale * 16);
	}
}

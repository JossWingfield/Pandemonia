package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
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
import net.packets.Packet01Disconnect;
import utility.ProgressManager.RewardType;

public class GUI {
	
	GamePanel gp;
	
	//IMAGES
    private TextureRegion recipeBorder, timeBorder, timeHeader, timeFrame, coinImage, mysteryOrder, cursedRecipeBorder, starLevel;
    private TextureRegion[][] timeAnimations;
    private TextureRegion[][] titleBookAnimations;
    private TextureRegion titleBackground;
    private TextureRegion highlight1, highlight2, highlight3, highlight4, underline, uncheckedBox, checkedBox;
    private TextureRegion settingsFrame, generalSettingsFrame, videoSettingsFrame, audioSettingsFrame, multiplayerSettingsFrame;
    private TextureRegion[] computerAnimations;
    private TextureRegion shoppingUI, shoppingButtonUI, leftArrow, rightArrow, basketUI, basketButtons;
    private TextureRegion leftProgress1, leftProgress2, middleProgress1, middleProgress2, rightProgress1, rightProgress2;
    private TextureRegion saveBorder, deleteSave;
    private TextureRegion dialogueFrame;
	private TextureRegion bookIcons[], bookOpen, bookClosed, recipeBookBorder, lockedRecipeBorder;
	private TextureRegion achievementBorder, achievement, lockedAchievement, mysteryIcon, mysteryCrateUI, catalogueButton, catalogueMenu;
    
	//COLOURS
	private Colour darkened;
	private Colour craftColour1;
    private Colour titleColour1, titleColour2;
    private Colour orderTextColour;
	
	//FONTS
	BitmapFont font, fancyFont;
	
	//COUNTERS
	private int timeAnimationCounter, timeAnimationSpeed, currentTimeAnimation;
	private double clickCooldown = 0;
	private int titleAnimationCounter, titleAnimationSpeed, currentTitleAnimation;
	private double titleAnimationSpeedFactor;
	private int titlePageNum = -1;
	private int computerAnimationCounter;
	private double computerAnimationSpeed;
	
	//MESSAGES
	private List<GUIMessage> messages = new ArrayList<>();
	
	private boolean firstDraw = true;
	public boolean startLoading = false;
	private boolean singleplayerSelected = false;
	private boolean hostSelected = false;
	private boolean joinSelected = false;
	
	//XP
	public int displayedSouls;     // what we currently draw
	public int targetSouls;        // where to animate to
	public boolean animatingSouls; // true while animating
	private int soulCounter = 0;
	
	//USERNAME
	public String username = "";
	public boolean usernameActive = false; // whether user is typing
	private int caretBlinkCounter = 0; // blinking cursor effect
	private boolean levelUp = false;
	
	private Map<Recipe, RecipeRenderData> renderCache = new HashMap<>();
	public Recipe[] recipeChoices;
	public Upgrade[] upgradeChoices;
	
	//SETTINGS STATES
	private int settingsState = 0;
	private final int baseSettings = 0;
	private final int generalState = 1;
	private final int videoState = 2;
	private final int audioState = 3;
	private final int multiplayerState = 4;
	
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
	
	private int currentPage = 0;  
	private int achievementStartIndex = 0; // which achievement is currently at the top
	private Achievement notificationAchievement = null;
	private float notificationTimer = 0f; // seconds
	private final float notificationDuration = 7f; // how long to show (3 seconds)
	private int notificationX, notificationY; // position to draw

	public GUI(GamePanel gp) {
		this.gp = gp;
		
		darkened = new Colour(30, 30, 30, 200);
		craftColour1 = new Colour(232, 193, 112);
		craftColour1 = Colour.BLACK;
		titleColour1 = new Colour(51, 60, 58);
		titleColour1 = Colour.BLACK;
	    titleColour2 = new Colour(87, 87, 87);
		titleColour2 = Colour.WHITE;
        //orderTextColour = new Colour(145, 102, 91);
	    orderTextColour = Colour.BLACK;
        //titleAnimationSpeedFactor = 4;
        titleAnimationSpeedFactor = 3.0;
        
        font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
        fancyFont = AssetPool.getBitmapFont("/UI/monogram-extended-italic.ttf", 32);
        
		importImages();
	}
	
	protected Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
	
	private void importImages() {
		titleBackground = importImage("/UI/titlescreen/DeskBase.png").toTextureRegion();
		recipeBorder = importImage("/UI/recipe/orderBorder.png").toTextureRegion();
		timeBorder = importImage("/UI/weather/TimeBorder.png").toTextureRegion();
		timeHeader = importImage("/UI/weather/3.png").toTextureRegion();
		timeFrame = importImage("/UI/weather/TimeFrame.png").toTextureRegion();
		coinImage = importImage("/UI/Coin.png").toTextureRegion();
		mysteryOrder = importImage("/UI/recipe/MysteryOrder.png").toTextureRegion();
		
		timeAnimations = new TextureRegion[20][20];
		currentTimeAnimation = 0;
		timeAnimations[0] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77, 78, 77);   //MORNING
		timeAnimations[1] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*2, 78, 77); //AFTERNOON
		timeAnimations[2] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*3, 78, 77); //LIGHTNING
		timeAnimations[3] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*5, 78, 77); //RAIN
		timeAnimations[4] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*6, 78, 77); //EVENING
		timeAnimations[5] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*7, 78, 77); //NIGHT
		//TRANSITIONS
		timeAnimations[6] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*10, 78, 77); //MORNING -> AFTERNOON
		timeAnimations[7] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*11, 78, 77); //AFTERNOON -> LIGHTNING
		timeAnimations[8] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*12, 78, 77); //LIGHTNING -> RAIN
		timeAnimations[9] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*13, 78, 77); //RAIN -> AFTERNOON
		timeAnimations[10] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*14, 78, 77); //AFTERNOON -> EVENING
		timeAnimations[11] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*15, 78, 77); //EVENING -> NIGHT
		timeAnimations[12] = importFromSpriteSheet("/UI/weather/Day & Night Cycle.png", 8, 1, 0, 77*17, 78, 77); //NIGHT -> MORNING
		
		titleBookAnimations = new TextureRegion[10][10];
		titleBookAnimations[0] = importFromSpriteSheet("/UI/titlescreen/BookOpen.png", 5, 1, 0, 0, 848, 640); 
		titleBookAnimations[1] = importFromSpriteSheet("/UI/titlescreen/PageLeft.png", 8, 1, 0, 0, 848, 640); 
		titleBookAnimations[2] = importFromSpriteSheet("/UI/titlescreen/PageRight.png", 8, 1, 0, 0, 848, 640); 

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
		underline = importImage("/UI/settings/Underline.png").toTextureRegion();
		
		leftProgress1 = importImage("/UI/levels/LeftProgress.png").getSubimage(0, 0, 46/2, 20);	
		leftProgress2 = importImage("/UI/levels/LeftProgress.png").getSubimage(46/2, 0, 46/2, 20);	
		middleProgress1 = importImage("/UI/levels/MiddleProgress.png").getSubimage(0, 0, 6, 20);	
		middleProgress2 = importImage("/UI/levels/MiddleProgress.png").getSubimage(6, 0, 6, 20);	
		rightProgress1 = importImage("/UI/levels/RightProgress.png").getSubimage(0, 0, 12, 20);	
		rightProgress2 = importImage("/UI/levels/RightProgress.png").getSubimage(12, 0, 12, 20);	

		cursedRecipeBorder = importImage("/UI/recipe/HauntedOrderBorder.png").toTextureRegion();
		starLevel = importImage("/UI/recipe/Star.png").toTextureRegion();
		saveBorder = importImage("/UI/saves/SaveUI.png").toTextureRegion();
		deleteSave = importImage("/UI/saves/DeleteSave.png").toTextureRegion();
		
		dialogueFrame = importImage("/UI/customise/CustomiseFrame.png").toTextureRegion();
		bookIcons = new TextureRegion[4];
		bookIcons[0] = importImage("/UI/BookIcons.png").getSubimage(0, 0, 16, 16);
		bookIcons[1] = importImage("/UI/BookIcons.png").getSubimage(16, 0, 16, 16);
		bookIcons[2] = importImage("/UI/BookIcons.png").getSubimage(0, 16, 16, 16);
		bookIcons[3] = importImage("/UI/BookIcons.png").getSubimage(16, 16, 16, 16);
		bookOpen = importImage("/UI/Book_Open.png").toTextureRegion();
		bookClosed = importImage("/UI/Book_Closed.png").toTextureRegion();
		recipeBookBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(0, 0, 16, 16);
		lockedRecipeBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(16, 0, 16, 16);
		
		achievementBorder = importImage("/UI/achievement/AchievementBorder.png").toTextureRegion();
		achievement = importImage("/UI/achievement/AchievementUI.png").toTextureRegion();
		lockedAchievement = importImage("/UI/achievement/LockedAchievement.png").toTextureRegion();
		mysteryIcon = importImage("/UI/catalogue/MysteryCrate.png").toTextureRegion();
		mysteryCrateUI = importImage("/UI/catalogue/MysteryCrateUI.png").toTextureRegion();
		catalogueButton = importImage("/UI/catalogue/CatalogueButton.png").toTextureRegion();
		catalogueMenu = importImage("/UI/catalogue/CatalogueMenu.png").toTextureRegion();
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
		
		int x = (int)gp.mouseL.getWorldX();
		int y = (int)gp.mouseL.getWorldY();
		
		if(x > x1 && x < x1 + getTextWidth(text, font) && y > y1 && y < y1 + getTextHeight(font)) {
			return true;
		}
		return false;
	}
	private boolean isHovering(int x1, int y1, int w, int h) {
		
		int x = (int)gp.mouseL.getWorldX();
		int y = (int)gp.mouseL.getWorldY();
		
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
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5));

		
		if(titlePageNum == 0 || titleAnimationCounter > 6) {
			
			String text = "Pandemonia";
			
			int x = 110;
			int y = 240;
			renderer.drawString(fancyFont, text, x, y, 1.0f, titleColour1);
			
			renderer.fillRect(x, y+ 20, getTextWidth(text, fancyFont), 10, titleColour1);
			
			//PLAY

			text = "Play";
			
			int mouseX = (int)gp.mouseL.getWorldX();
			int mouseY = (int)gp.mouseL.getWorldY();
			
			x = 120;
		    y = 360;
			Colour c = titleColour1;
			if(isHovering(text, x, y-24, fancyFont)) {
				c = titleColour2;
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						gp.currentState = gp.startGameSettingsState;
						clickCooldown = 0.5;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);
			}
			
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);
		
			//SETTINGS
			text = "Settings";
	
			x = 120;
			y = 450;
			c = titleColour1;
			if(isHovering(text,x, y-24, fancyFont)) {
				c = titleColour1;
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//ENTER SETTINGS
						//gp.currentState = gp.settingsState;
						clickCooldown = 0.33;
					}
				}
				renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);
			}
			
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);
	
			//QUIT
			text = "Quit";
	
			x = 120;
			y = 450+90;
			c = titleColour1;
			if(isHovering(text,x, y-24, fancyFont)) {
				c  = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//QUIT GAME
						System.exit(0);
					}
				}
				renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);
			}
			
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);
		}
		
	}
	private void drawRecipesScreen(Renderer renderer) {
		
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		TextureRegion img = bookOpen;
		renderer.draw(img, gp.frameWidth/2-img.getPixelWidth()/2*6, gp.frameHeight/2-img.getPixelHeight()/2*6, img.getPixelWidth()*6, img.getPixelHeight()*6);
		
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		
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
		
		int recipesPerSide = 16;  // 4 rows * 4 columns
		int recipesPerPage = 32;  // left + right page

		int startIndex = currentPage * recipesPerPage;
		int endIndex = startIndex + recipesPerPage;

		int leftX = 180;
		int rightX = 665;
		int startY = 180;

		int spacing = 32;
		int borderSize = 48;
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
			    int textWidth = (int)fancyFont.getTextWidth(name);
			    int textX = x + (borderSize - textWidth) / 2;
			    int textY = y - 6;
			    renderer.drawString(fancyFont, name, textX, textY, 1.0f, titleColour1);
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
		        renderer.draw(plate, plateX - 16, plateY - 14, 48, 48);
		    }

		    // ------------------------------------
		    // STARS
		    // ------------------------------------
		    if (isUnlocked) {
		        int stars = recipe.getStarLevel();
		        int starsY = y + borderSize + 4;

		        for (int k = 0; k < stars; k++) {
		            int starX = x + k * starSpacing;
		            renderer.draw(starLevel, starX, starsY, 16, 16);
		        }
		    }

		    // ------------------------------------
		    // MOVE TO NEXT SLOT
		    // ------------------------------------
		    printed++;
		    x += borderSize + spacing;

		    // After 4 columns → new row
		    if (printed % 4 == 0) {
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
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5));

		if(titleAnimationCounter > 6) {
			String text = "Multiplayer";
			
			int x = 110;
			int y = 220;
			renderer.drawString(fancyFont, text, x, y, 1.0f, titleColour1);
			
			renderer.fillRect(x, y+ 20, getTextWidth(text, fancyFont), 10, titleColour1);
			text = "Settings";
					
		    x = 110;
		    y = 230+80;
			renderer.drawString(fancyFont, text, x, y, 1.0f, titleColour1);
					
			renderer.fillRect(x, y+20, getTextWidth(text, fancyFont), 10, titleColour1);
		
		//HOST
		Colour c = (titleColour1);
		text = "Host";
			
		x = 120;
		y = 390;
			
		if(isHovering(text,x, y-24, fancyFont)) {
			c = (titleColour2);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					//ENTER GAME
					hostSelected = true;
					usernameActive = true;
					gp.currentState = gp.writeUsernameState;
					clickCooldown = 0.16;
				}
			}
			renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);

		}
			
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);
			
			//MULTIPLAYER
			c = titleColour1;
			text = "Join";

			x = 120;
			y = 480;
			
			if(isHovering(text,x, y-24, fancyFont)) {
				c = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//ENTER MULTIPLAYER
					if(clickCooldown == 0) {
					    gp.currentState = gp.lanJoinMenuState;
					    gp.startDiscovery();
						clickCooldown = 0.16;
					}
				}
				renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);

			}
			
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);

			//QUIT
			c = (titleColour1);
			text = "Back";

			x = 100;
			y = 660;
			
			if(isHovering(text,x, y-24, fancyFont)) {
				c = (titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//QUIT GAME
					if(clickCooldown == 0) {
						gp.currentState = gp.startGameSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 2;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				renderer.fillRect(x, y+ 14, getTextWidth(text, fancyFont), 6, c);

			}
			renderer.drawString(fancyFont, text, x, y, 1.0f, c);

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
	    renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], 
	        (gp.frameWidth/2) - (int)((848*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((640*1.5)/2), 
	        (int)(848*1.5), (int)(640*1.5));

	    if (titleAnimationCounter > 6) {
	        // Title
	        String text = "Available LAN Worlds";
	        int x = gp.frameWidth/2 - getTextWidth(text, fancyFont)/2;
	        int y = 200;
	        renderer.drawString(fancyFont, text, x, y, 1.0f, titleColour1);
	        renderer.fillRect(x, y+20, getTextWidth(text, font), 10, titleColour1);

	        // Server list
	        int startY = 320;
	        int index = 0;

	        if (gp.discovery != null) {
	            startY = 320;
	            index = 0;
	            for (DiscoveredServer server : gp.discovery.getDiscoveredServers()) {
	                text = server.worldName + " (Host: " + server.hostUser + ")";
	                x = 140;
	                y = startY + index * 60;

	                Colour c = titleColour1;
	                if (isHovering(text, x, y-24, fancyFont)) {
	                    c = (titleColour2);
	                    if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                        // Select this server
	                        gp.selectedServer = server;
	                        gp.currentState = gp.writeUsernameState;
	                        usernameActive = true;
	                        joinSelected = true;
	                        clickCooldown = 0.25;
	                    }
	                    renderer.fillRect(x, y+14, getTextWidth(text, fancyFont), 6, c);
	                }
	                renderer.drawString(fancyFont, text, x, y, 1.0f, c);
	                index++;
	            }
	        } else {
	            renderer.drawString(fancyFont, "Searching for games on LAN...", 140, 320, 1.0f, Colour.BLACK);
	        }

	        // Back button
	        text = "Back";
	        x = 100;
	        y = 660;
	        Colour c = titleColour1;
	        if (isHovering(text, x, y-24, fancyFont)) {
	            c = (titleColour2);
	            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                gp.currentState = gp.multiplayerSettingsState;
	                clickCooldown = 0.33;
	                currentTitleAnimation = 2;
	                titleAnimationCounter = 0;
	                titleAnimationSpeed = 0;
	                gp.stopDiscovery();
	            }
	            renderer.fillRect(x, y+14, getTextWidth(text, fancyFont), 6, c);
	        }
	        renderer.drawString(fancyFont, text, x, y, 1.0f, c);
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
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5));
		
		if(titleAnimationCounter > 6) {
		renderer.setFont(font);
		renderer.setColour(titleColour1);
		String text = "Play";
		
		int x = 110;
		int y = 240;
		renderer.drawString(text, x, y);
		
		renderer.fillRect(x, y+ 20, getTextWidth(text, font), 10);
		
		//SinglePlayer
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			text = "Singleplayer";
				
			x = 120;
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
				renderer.fillRect(x, y+ 14, getTextWidth(text, font), 6);

			}
				
				renderer.drawString(text, x, y);
				
				//MULTIPLAYER
				renderer.setColour(titleColour1);
				text = "Multiplayer";

				x = 120;
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
					renderer.fillRect(x, y+ 14, getTextWidth(text, font), 6);

				}
				
				renderer.drawString(text, x, y);

				//QUIT
				renderer.setColour(titleColour1);
				text = "Back";

				x = 100;
				y = 660;
				
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
					renderer.fillRect(x, y+ 14, getTextWidth(text, font), 6);

				}
				
				renderer.drawString(text, x, y);
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
		
		renderer.draw(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5));
		
		if(titleAnimationCounter > 6) {
			
			int x = 100;
			int y = 200;
			String text = "Choose Save";
			
			int saveChosen = -1;
			
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			renderer.drawString(text, x, y);
			
			text = "Save 1";
			
			x = 100;
			y = 260 - 40;
			
			int result = drawSave(renderer, 1, x, y, text);
			if (result != -1) saveChosen = result;
			
			renderer.setColour(titleColour1);
			text = "Save 2";
			x = 100;
			y = 260 + 48*3 + 10 - 40;
			
			result = drawSave(renderer, 2, x, y, text);
			if (result != -1) saveChosen = result;
			
			renderer.setColour(titleColour1);
			text = "Save 3";
			x = 100;
			y = 260 + 48*6 + 20 - 40;
			
			result = drawSave(renderer, 3, x, y, text);
			if (result != -1) saveChosen = result;
			
			//QUIT
			renderer.setFont(font);
			renderer.setColour(titleColour1);
			text = "Back";

			x = 100;
			y = 696;
			
			if(isHovering(text,x, y-24, font)) {
				renderer.setColour(titleColour2);
				if(gp.mouseL.mouseButtonDown(0)) {
					//QUIT GAME
					if(clickCooldown == 0) {
						gp.currentState = gp.startGameSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 2;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
						singleplayerSelected = false;
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
				renderer.setFont(font);
				renderer.setColour(Colour.WHITE);
				text = "LOADING";
					
				x = 20;
				y = 800;
				
				renderer.drawString(text, x, y);
				if(singleplayerSelected) {
					gp.playSinglePlayer(saveChosen);
					singleplayerSelected = false;
				}
			}
		}
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
			renderer.drawString(text, 120, y+50);
			
			//renderer.setColour(titleColour1);
			renderer.setFont(font);
			renderer.drawString(gp.saveM.getSavedSeason(saveSlot) + " Day " + Integer.toString(gp.saveM.getSavedDay(saveSlot)), 120, y+80);
			
			renderer.draw(coinImage, 120, y+84, 16*3, 16*3);
			renderer.drawString(Integer.toString(gp.saveM.getSavedMoney(saveSlot)), 176, y+114);
			
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
			renderer.drawString(text, 120, y+50);
		}
		
		return saveChosen;
	}
	public void drawUsernameInput(Renderer renderer) {
	    
	    // Background (reuse your title background style if you want)
	    renderer.draw(titleBackground, 
	        (gp.frameWidth/2) - (int)((768*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((560*1.5)/2), 
	        (int)(768*1.5), 
	        (int)(560*1.5));

		
	    boolean drawLoadingScreen = false;
	    // Title
	    renderer.setFont(font);
	    renderer.setColour(titleColour1);
	    String text = "Enter Username";
	    int x = gp.frameWidth/2 - getTextWidth(text, font)/2;
	    int y = 200;
	    renderer.drawString(text, x, y);

	    // Username box
	    int boxWidth = 400;
	    int boxHeight = 50;
	    int boxX = gp.frameWidth/2 - boxWidth/2;
	    int boxY = 300;

	    renderer.setColour(new Colour(0, 0, 0, 150));
	    renderer.fillRect(boxX, boxY, boxWidth, boxHeight);
	    renderer.setColour(Colour.WHITE);
	    renderer.fillRect(boxX, boxY, boxWidth, boxHeight);

	    // Username text
	    renderer.setFont(font);
	    renderer.setColour(Colour.WHITE);
	    renderer.drawString(username, boxX + 10, boxY + 35);

	    // Blinking caret
	    caretBlinkCounter++;
	    if(caretBlinkCounter > 120) caretBlinkCounter = 0;
	    if(caretBlinkCounter < 60 && usernameActive) {
	        int caretX = boxX + 10 + getTextWidth(username, font);
	        renderer.fillRect(caretX, boxY + 10, 2, 30);
	    }

	    // "Confirm" button
	    text = "Confirm";
	    x = gp.frameWidth/2 - getTextWidth(text, font)/2;
	    y = 420;
	    renderer.setColour(titleColour1);

	    if(isHovering(text, x, y-24, font)) {
	        renderer.setColour(titleColour2);
	        if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	        	drawLoadingScreen = true;
	        	startLoading = true; 
	            clickCooldown = 0.33;
	        }
	        renderer.fillRect(x, y+14, getTextWidth(text, font), 6);
	    }
	    renderer.drawString(text, x, y);
	    
		renderer.setColour(titleColour1);
		text = "Back";

		x = 100;
		y = 660;
		
		if(isHovering(text,x, y-24, font)) {
			renderer.setColour(titleColour2);
			if(gp.mouseL.mouseButtonDown(0)) {
				//QUIT GAME
				if(clickCooldown == 0) {
					gp.currentState = gp.multiplayerSettingsState;
					clickCooldown = 0.33;
					currentTitleAnimation = 2;
					titleAnimationCounter = 0;
					titleAnimationSpeed = 0;
					usernameActive = false;
					joinSelected = false;
					hostSelected = false;
				}
			}
			renderer.fillRect(x, y+ 14, getTextWidth(text, font), 6);

		}
		renderer.drawString(text, x, y);
		
		if(drawLoadingScreen) {
			
			renderer.setFont(font);
			renderer.setColour(Colour.WHITE);
			text = "LOADING...";
				
			x = 30;
			y = 720;
			
			renderer.drawString(text, x, y);
			if(startLoading) {
				if(hostSelected) {
					gp.hostServer(username);
					hostSelected = false;
					usernameActive = false;
				} else if(joinSelected) {
					gp.joinServer(username, gp.selectedServer.ip, gp.selectedServer.port);
					joinSelected = false;
					usernameActive = false;
				}
				startLoading = false;
			}
			startLoading = true;
		}
	    
	}
	private void drawAchievementsScreen(Renderer renderer) {
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		TextureRegion img = achievementBorder;
		int x = gp.frameWidth/2-img.getPixelWidth()/2*6;
		int y = gp.frameHeight/2-img.getPixelHeight()/2*6;
		renderer.draw(img, x, y, img.getPixelWidth()*6, img.getPixelHeight()*6);
		
		renderer.setColour(titleColour1);
		renderer.setFont(font);
		renderer.drawString("Achievements", x+36, y+68);
		
		renderer.setFont(font);
		String text = "BACK";
		x =getXforCenteredText(text, font);
		if(isHovering(text, x, 650, font)) {
			renderer.setColour(craftColour1);
			if(gp.mouseL.mouseButtonDown(0)) {
				if(clickCooldown == 0) {
					//QUIT
					gp.currentState = gp.pauseState;
					clickCooldown = 0.16;
				}
			}
		}else {
			renderer.setColour(Colour.WHITE);
		}
		renderer.drawString(text, getXforCenteredText(text, font), 700);
		
		List<Achievement> achievementList =
		        new ArrayList<>(gp.progressM.achievements.values());

		// Sort so unlocked ones appear first, locked ones after
		achievementList.sort((a, b) -> {
		    if (a.isUnlocked() == b.isUnlocked()) return 0;
		    return a.isUnlocked() ? -1 : 1;  // unlocked = top
		});	    
		int startX = x-86;
		int startY = y+90;
	    int ySpacing = 27*6+20; // vertical space per achievement
	    int iconOffsetX = 92; // distance from left border to icon
	    int textOffsetX = 50; // distance from left border to text
	    
	    int maxVisibleAchievements = 2;
	   
	    for (int i = 0; i < maxVisibleAchievements; i++) {
	        int index = achievementStartIndex + i;
	        if (index >= achievementList.size()) break; // no more achievements

	        Achievement a = achievementList.get(index);

	        int yPos = startY + i * ySpacing;

	        // Draw border first
	        renderer.draw(achievement, startX, yPos, 44*6, 27*6);
	        // Optional: gray out if not unlocked
	        if (!a.isUnlocked()) {
		        renderer.draw(lockedAchievement, startX, yPos, 44*6, 27*6);
	        }

	        // Draw achievement icon
	        if (a.getIcon() != null) {
	        	TextureRegion icon = a.getIcon();
	        	if(!a.isUnlocked()) {
	        		//icon = CollisionMethods.getMaskedImage(Colour.BLACK, icon);
	        	}
	            renderer.draw(icon, startX + iconOffsetX+ 80, yPos + 80, icon.getPixelWidth()*3, icon.getPixelHeight()*3, Colour.BLACK.toVec4());
	        }

	        // Draw text: name (bold) and description
	        renderer.setColour(titleColour1);
	        renderer.setFont(font);
	        renderer.drawString(a.getName(), startX + textOffsetX-28, yPos + 45);

	        renderer.setFont(font);
	        
	    	text = a.getDescription();
			for(String line: gp.gui.wrapText(text, font, 49*5)) {
				renderer.drawString(line, startX + textOffsetX-28, yPos + 70);
				yPos += 30;
			}
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
	    if (notificationAchievement == null) return;

	    int popupScale = 6;
	    TextureRegion img = achievement; // same as menu
	    int x = gp.frameWidth - img.getPixelWidth() * popupScale - 20;
	    int y = gp.frameHeight - img.getPixelHeight() * popupScale - 20; // top of screen (or wherever you like)
	    
	    // Draw border
	    renderer.draw(img, x, y, img.getPixelWidth() * popupScale, img.getPixelHeight() * popupScale);
	    
	    // Draw icon
	    TextureRegion icon = notificationAchievement.getIcon();
	    if (icon != null) {
	        int iconX = x + 92;
	        int iconY = y + 80;
	        renderer.draw(icon, iconX+80, iconY, icon.getPixelWidth() * 3, icon.getPixelHeight() * 3);
	    }
	    
	    // Draw name
	    renderer.setColour(titleColour1);
	    renderer.setFont(font);
	    renderer.drawString(notificationAchievement.getName(), x + 50-15, y + 45);

	    // Draw description
	    renderer.setFont(font);
	    renderer.drawString(notificationAchievement.getDescription(), x + 22, y + 70);
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
		}
		
		drawAchievementNotification(renderer);
		
		if(firstDraw) {
			renderer.setFont(font);
			renderer.drawString("", -1000, -1000);
			firstDraw = false;
		}

		
	}
	
	private void drawPlayScreen(Renderer renderer) {
		updateMessages();
		List<Recipe> currentOrders = new ArrayList<>(RecipeManager.getCurrentOrders());

		int i = 0;
		
		for (Recipe recipe : currentOrders) {
		    RecipeRenderData data = renderCache.get(recipe);
		    if (data == null) continue;

		    int x = 8 + i * (36*3);
		    int y = 0;

		    // BASE
		    renderer.draw(data.borderImage, x, y, 96, 144);
		    if (data.customer.hideOrder) {
		        // Overlay the "mystery order" image
		        renderer.draw(data.mysteryOrderImage, x, y, 96, 144);
		    } else {
		        // Normal ingredient + text drawing
		        for (int j = 0; j < data.ingredientImages.size(); j++) {
		            int dx = x + j * (10*3) + 4;
		            int dy = y + 4;
		            renderer.draw(data.ingredientImages.get(j), dx, dy, 30, 30);
		            renderer.draw(data.cookingStateIcons.get(j), dx, dy + 16, 30, 30);
		            renderer.draw(data.secondaryCookingStateIcons.get(j), dx, dy + 40, 30, 30);
		        }

		        if(!recipe.isCursed) {
			        renderer.setColour(orderTextColour);
		        } else {
		        	renderer.setColour(Colour.WHITE);
		        }

		        renderer.setFont(font);
		        int counter = 0;
		        for (int l = 0; l < data.nameLines.size(); l++) {
		            String line = data.nameLines.get(l);
		            int offset = data.nameLineOffsets.get(l);
		            renderer.drawString(line, x + offset, y + 84 + counter);
		            counter += 15;
		        }
		        for (int j = 0; j < data.starLevel; j++) {
			        renderer.draw(starLevel, x +10 + j * 36, y + 50, 8*3, 8*3);
			    }

		        renderer.draw(data.plateImage, x + 24, y + 94, 48, 48);
		    }

		    // COIN + FACE
		    renderer.draw(data.coinImage, x - 8, y + 142, 48, 48);
		    renderer.draw(data.faceIcon, x + 32, y + 118, 96, 96);

		    // COST
		    renderer.setColour(Colour.WHITE);
		    renderer.setFont(font);
		    renderer.drawString(data.cost, x + 36, y + 174);

		    // PATIENCE
		    drawPatienceBar(renderer, x, y + 208, (int)data.customer.getPatienceCounter(), (int)data.customer.getMaxPatienceTime());

		    i++;
		}

		//DRAW TIME AND WEATHER
		renderer.draw(timeBorder, gp.frameWidth - (78*3) - 4, 4, 78*3, 77*3);
		
		timeAnimationSpeed++;
		if(timeAnimationSpeed >= 7) {
			timeAnimationSpeed = 0;
			timeAnimationCounter++;
		}
		
		if(timeAnimations[currentTimeAnimation][timeAnimationCounter] == null) {
			timeAnimationCounter = 0;
			if(currentTimeAnimation == 6) {
				currentTimeAnimation = 1;
			} else if(currentTimeAnimation == 10) {
				currentTimeAnimation = 4;
			} else if(currentTimeAnimation == 11) {
				currentTimeAnimation = 5;
			} else if(currentTimeAnimation == 12) {
				currentTimeAnimation = 0;
			}
		}
		
		if(gp.world.currentWeather.equals(Weather.RAIN)) {
			currentTimeAnimation = 3;
		} else if(gp.world.currentWeather.equals(Weather.THUNDERSTORM)) {
			currentTimeAnimation = 2;
		}
		renderer.draw(timeAnimations[currentTimeAnimation][timeAnimationCounter], gp.frameWidth - (78*3) - 4, 4, 78*3, 77*3);
		renderer.draw(timeHeader, gp.frameWidth - (80*3), 4 + (77*3) - 2, 80*3, 16*3);
		renderer.draw(timeFrame, gp.frameWidth - (80*3), 4 + (77*3) - 2 + (16*3), 80*3, 16*3);
		
		renderer.setFont(font);
		renderer.setColour(orderTextColour);
		renderer.drawString(gp.world.getDate(), gp.frameWidth - (80*3) + 28, 4 + (77*3) + 28);
		renderer.drawString(gp.world.getTime24h(), gp.frameWidth - (80*3) + 112, 4 + (77*3) + 26 + (16*3));
		
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

		    // Shadow
		    renderer.setColour(new Colour(0, 0, 0, alpha));
		    renderer.drawString(msg.text, msgX, msgY - k * lineHeight + 3);

		    // Main text
		    renderer.setColour(new Colour(msg.color.r, msg.color.g, msg.color.b, alpha));
		    renderer.drawString(msg.text, msgX, msgY - k * lineHeight);
		}
		
	}
	public void addOrder(Recipe recipe, Customer customer, Renderer renderer) {
	    RecipeRenderData data = buildRenderData(recipe, customer, font, renderer);
	    renderCache.put(recipe, data);
	}
	public RecipeRenderData buildRenderData(Recipe recipe, Customer customer, BitmapFont font, Renderer renderer) {
	    RecipeRenderData data = new RecipeRenderData();
	    data.recipe = recipe;
	    data.customer = customer;

	    // Cache base images
	    if(recipe.isCursed) {
		    data.borderImage = cursedRecipeBorder;
	    } else {
		    data.borderImage = recipeBorder;
	    }
	    data.starLevel = recipe.getStarLevel();
	    data.mysteryOrderImage = mysteryOrder;
	    data.coinImage = coinImage;
	    data.plateImage = recipe.finishedPlate;
	    data.faceIcon = customer.faceIcon;

	    // Cache ingredient + states
	    List<String> ingredients = recipe.getIngredients();
	    List<String> cookingState = recipe.getCookingStates();
	    List<String> secondaryCookingState = recipe.getSecondaryCookingStates();

	    for (int j = 0; j < ingredients.size(); j++) {
	        String ingredientName = ingredients.get(j);
	        Food ingredient = (Food) gp.itemRegistry.getItemFromName(ingredientName, 0);

	        TextureRegion ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
	        if (ingredient.notRawItem) {
	            ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
	        }

	        data.ingredientImages.add(ingredientImage);
	        data.cookingStateIcons.add(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed));
	        data.secondaryCookingStateIcons.add(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed));
	    }
	    
	    // Cache text layout
	    renderer.setFont(font);
	    for (String line : recipe.getName().split(" ")) {
	        data.nameLines.add(line);
	        int offset = (48 - getTextWidth(line, font) / 2);
	        data.nameLineOffsets.add(offset);
	    }

	    // Cache cost
	    if (customer instanceof SpecialCustomer specialCustomer) {
	        if (specialCustomer.hideOrder) {
	            data.cost = "???";
	        } else {
	            data.cost = Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe),
	                                                        specialCustomer.getMoneyMultiplier()));
	        }
	    } else {
	        data.cost = Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe)));
	    }

	    return data;
	}
	private void drawPatienceBar(Renderer renderer, float worldX, float worldY, int patienceCounter, int maxPatienceTime) {

	    int barWidth = 32 * 3;
	    int barHeight = 12;
	    int yOffset = -20;

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
	public void changeTimeState(int a) {
		switch(a) {
		case 0:
			currentTimeAnimation = 12;
			timeAnimationCounter = 0;
			timeAnimationSpeed = 0;
		case 1:
			if(currentTimeAnimation == 12) {
				return;
			}
			currentTimeAnimation = 6;
			timeAnimationCounter = 0;
			timeAnimationSpeed = 0;
			break;
		case 4:
			currentTimeAnimation = 10;
			timeAnimationCounter = 0;
			timeAnimationSpeed = 0;
			break;
		case 5:
			currentTimeAnimation = 11;
			timeAnimationCounter = 0;
			timeAnimationSpeed = 0;
			break;
		}
	}
	private void drawPauseScreen(Renderer renderer) {
		
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		renderer.setColour(Colour.WHITE);
		renderer.setFont(font);
		String text = "PAUSED";
		renderer.drawString(text, getXforCenteredText(text, font), 200);
		
		text = "RESUME";
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
		
		text = "SETTINGS";
		x =getXforCenteredText(text, font);
		if(isHovering(text, x, 500-24, font)) {
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
		renderer.drawString(text, getXforCenteredText(text, font), 500);
		
		text = "Achievements";
		x =getXforCenteredText(text, font);
		if(isHovering(text, x, 600-24, font)) {
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
			renderer.drawString(text, getXforCenteredText(text, font), 600);
			
			text = "QUIT";
			x =getXforCenteredText(text, font);
			if(isHovering(text, x, 700-24, font)) {
				renderer.setColour(craftColour1);
				if(gp.mouseL.mouseButtonDown(0)) {
					if(clickCooldown == 0) {
						//QUIT
						gp.currentState = gp.titleState;
						currentTitleAnimation = 0;
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
					}
				}
		}else {
			renderer.setColour(Colour.WHITE);
		}
		renderer.drawString(text, getXforCenteredText(text, font), 700);
		
		
		int mouseX = (int)gp.mouseL.getWorldX();
		int mouseY = (int)gp.mouseL.getWorldY();
		
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
		int boxOffset = 220;
		renderer.setColour(darkened);
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		renderer.draw(videoSettingsFrame, x, y, 112*3, 112*3);
		
		String text;
		renderer.setFont(font);
		x += 40;
		y += 140;
		
		renderer.setColour(titleColour1);
		text = "FullScreen";
		renderer.drawString(text, x, y);
		y-= 20;
		if(Settings.fullScreen) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	if(!Settings.fullScreen) {
            		gp.setFullScreen();
            	} else {
            		gp.stopFullScreen();
            	}
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		y+=40;
		renderer.setColour(titleColour1);
		text = "Fancy Lighting";
		renderer.drawString(text, x, y);
		y-= 20;
		if(Settings.fancyLighting) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	Settings.fancyLighting = !Settings.fancyLighting;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		y+=40;
		renderer.setColour(titleColour1);
		text = "Bloom";
		renderer.drawString(text, x, y);
		y-= 20;
		if(Settings.bloomEnabled) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	Settings.bloomEnabled = !Settings.bloomEnabled;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		y+=40;
		renderer.setColour(titleColour1);
		text = "God Rays";
		renderer.drawString(text, x, y);
		y-= 20;
		if(Settings.godraysEnabled) {
			renderer.draw(checkedBox, x+boxOffset, y, 9*3, 9*3);
		} else {
			renderer.draw(uncheckedBox, x+boxOffset, y, 9*3, 9*3);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(renderer, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
            	Settings.godraysEnabled = !Settings.godraysEnabled;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
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
			if(!gp.catalogue.checkingOut && !gp.catalogue.onMysteryScreen && !gp.catalogue.onCatalogueScreen) {
				renderer.draw(shoppingUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				renderer.draw(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.leftPage();
					}
				}
				renderer.draw(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.checkingOut = true;
						gp.catalogue.layer = 0;
					}
				}
				
				renderer.draw(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onMysteryScreen = true;
						gp.catalogue.layer = 0;
						gp.catalogue.pageNum = 1;
					}
				}
				
				renderer.draw(catalogueButton, (int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onCatalogueScreen = true;
						gp.catalogue.layer = 0;
						gp.catalogue.pageNum = 1;
					}
				}
				
			} else if(gp.catalogue.onMysteryScreen) {
				renderer.draw(mysteryCrateUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				if(isHovering((int)(87*4.5), (int)(108*4.5), (int)(61*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.buyMysteryCrate();
						gp.catalogue.layer = 0;
					}
				}
				
				renderer.draw(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onMysteryScreen = false;
						gp.catalogue.layer = 0;
					}
				}
			} else if(gp.catalogue.onCatalogueScreen) {
				
				renderer.draw(catalogueMenu, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				renderer.draw(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.leftPage();
					}
				}
				renderer.draw(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5));
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.checkingOut = true;
						gp.catalogue.layer = 0;
					}
				}
				
				renderer.draw(catalogueButton, (int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5));
				if(isHovering((int)((82)*4.5), (int)(30*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onCatalogueScreen = false;
						gp.catalogue.layer = 0;
						gp.catalogue.pageNum = 1;
					}
				}
				
			} else {
				renderer.draw(basketUI, 0, 0, (int)(260*4.5), (int)(190*4.5));
				renderer.draw(basketButtons, 0, 0, (int)(260*4.5), (int)(190*4.5));
				
				//CHECKOUT
				if(isHovering((int)(146*4.5), (int)(96*4.5), (int)(42*4.5), (int)(16*4.5))) {
					if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.tryPay();
					}
				}
			}
			
			if(!gp.catalogue.checkingOut && !gp.catalogue.onMysteryScreen && !gp.catalogue.onCatalogueScreen) {
				gp.catalogue.drawCatalogue(renderer);
			} else if(gp.catalogue.onMysteryScreen) {
				gp.catalogue.drawMysteryScreen(renderer);
			} else if(gp.catalogue.onCatalogueScreen) {
				gp.catalogue.drawShopCatalogueScreen(renderer);
			} else {
				gp.catalogue.drawCheckout(renderer);
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
				gp.progressM.handleLevelUp(gp.player.level);
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

	    int totalLevels = gp.progressM.totalLevels;
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
	    gp.progressM.roadmapOffsetX += (targetOffsetX - gp.progressM.roadmapOffsetX) * 0.1f;

	    // Draw souls as roadmap line
	    int soulCounter = 0;
	    for (int level = 0; level < totalLevels; level++) {
	        int soulsInThisLevel = soulsPerLevel[level];
	        for (int s = 0; s < soulsInThisLevel; s++, soulCounter++) {
	            int x = (int)(soulCounter * soulWidth + gp.progressM.roadmapOffsetX);
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
	        int x = (int)(nodeSoulIndex * soulWidth + gp.progressM.roadmapOffsetX - nodeSize / 2);
	        int y = lineY - nodeSize / 2;

	        if (!(x + nodeSize < 0 || x > gp.frameWidth)) {
	        	TextureRegion icon;
	        	if (level+1 <= gp.player.level) {
	        	    icon = gp.progressM.levelRewards[level - 1][1]; // passed/completed
	        	} else {
	        	    icon = gp.progressM.levelRewards[level - 1][0]; // normal/unreached
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

		if (gp.progressM.getRecipeChoices() == null) {
		    // fallback in case something went wrong
		    recipeChoices = RecipeManager.getTwoRandomLocked(gp);
		} else {
		    recipeChoices = gp.progressM.getRecipeChoices();
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
	    		gp.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(renderer, recipeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	       		RecipeManager.unlockRecipe(recipeChoices[1]);
	    		gp.currentState = gp.playState;
	    		gp.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(renderer, recipeChoices[1], x, y);
	}
	private void drawChooseUpgradeScreen(Renderer renderer) {
		renderer.fillRect(0, 0, gp.frameWidth, gp.frameHeight, darkened);
		
		upgradeChoices = gp.progressM.getUpgradeChoices();
		
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
	    			gp.progressM.checkKitchenUpgrade();
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
	    			gp.progressM.checkKitchenUpgrade();
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

		// INGREDIENT IMAGES
		List<String> ingredients = recipe.getIngredients();
		List<String> cookingState = recipe.getCookingStates();
		List<String> secondaryCookingState = recipe.getSecondaryCookingStates();
		for (int j = 0; j < ingredients.size(); j++) {
			String ingredientName = ingredients.get(j);
			TextureRegion ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
			Food ingredient = (Food)gp.itemRegistry.getItemFromName(ingredientName, 0);
			if(ingredient.notRawItem) {
				ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
			}
			if (ingredientImage != null) {
				// Draw each ingredient image 32px apart above the order box
				renderer.draw(ingredientImage, x + j * (10*3) + 4, y + 4, 10*3, 10*3);
				renderer.draw(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3);
				renderer.draw(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3);
			}
		}
		
		// NAME
		int counter = 0;
		for(String line: recipe.getName().split(" ")) {
            renderer.drawString(font, line, x + (48 - getTextWidth(line, font) / 2.0f), y + 84 + counter, 1.0f, orderTextColour);
            counter += 15;
        }
		// PLATE IMAGE
		renderer.draw(recipe.finishedPlate, x + 24, y + 94, 48, 48);
		
		renderer.draw(coinImage, x, y + 94 + 48, 48, 48);
		
		renderer.drawString(font, Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe))), x + 48+8, y + 94 + 48+32, 1.0f, Colour.WHITE);

    }
	public void startLevelUpScreen() {
		gp.currentState = gp.xpState;
		displayedSouls = gp.world.previousSoulsCollected;
		targetSouls = gp.player.soulsServed;
		animatingSouls = true;
		soulCounter = 0;
	}
	public void resetComputerAnimations() {
		computerAnimationSpeed = 0;
		computerAnimationCounter = 0;
	}
	public void addMessage(String text) {
	    addMessage(text, 320, Colour.YELLOW);
	}
	public void addMessage(String text, Colour Colour) {
	    addMessage(text, 320, Colour);
	}

	public void addMessage(String text, int lifetime, Colour colour) {
	    messages.add(new GUIMessage(text, lifetime, colour));
	}
	public void update(double dt) {
		
		if (clickCooldown > 0) {
			clickCooldown -= dt;        // subtract elapsed time in seconds
		    if (clickCooldown <= 0) {
		    	clickCooldown = 0;      // clamp to zero
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
		        if (achievementStartIndex + 2 < gp.progressM.achievements.size()) {
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
}

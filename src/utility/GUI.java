package utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import entity.PlayerMP;
import entity.items.Food;
import entity.npc.Customer;
import entity.npc.NPC;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import net.DiscoveryManager;
import net.DiscoveryManager.DiscoveredServer;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;
import utility.ProgressManager.RewardType;

public class GUI {
	
	GamePanel gp;
	
	//IMAGES
    private BufferedImage recipeBorder, timeBorder, timeHeader, timeFrame, coinImage, mysteryOrder, cursedRecipeBorder, starLevel;
    private BufferedImage[][] timeAnimations;
    private BufferedImage[][] titleBookAnimations;
    private BufferedImage titleBackground;
    private BufferedImage highlight1, highlight2, highlight3, highlight4, underline, uncheckedBox, checkedBox;
    private BufferedImage settingsFrame, generalSettingsFrame, videoSettingsFrame, audioSettingsFrame, multiplayerSettingsFrame;
    private BufferedImage[] computerAnimations;
    private BufferedImage shoppingUI, shoppingButtonUI, leftArrow, rightArrow, basketUI, basketButtons;
    private BufferedImage leftProgress1, leftProgress2, middleProgress1, middleProgress2, rightProgress1, rightProgress2;
    private BufferedImage saveBorder, deleteSave;
    private BufferedImage dialogueFrame;
	private BufferedImage bookIcons[], bookOpen, bookClosed, recipeBookBorder, lockedRecipeBorder;
	private BufferedImage achievementBorder, achievement, lockedAchievement, mysteryIcon, mysteryCrateUI;
    
	//COLOURS
	private Color darkened;
	private Color craftColour1;
    private Color titleColour1, titleColour2;
    private Color orderTextColour;
	
	//FONTS
	private Font headingFont = new Font("Orange Kid", Font.BOLD, 70);
	private Font pauseFont = new Font("Orange Kid", Font.BOLD, 50);
	private Font numberFont = new Font("Orange Kid", Font.PLAIN, 20);
	private Font titleFont = new Font("monogram", Font.BOLD, 70);
	private Font saveFont = new Font("monogram", Font.BOLD, 40);
	private Font saveFont2 = new Font("monogram", Font.BOLD, 32);
	private Font fancyTitleFont = new Font("monogram", Font.ITALIC, 100);
    private Font nameFont = new Font("monogram", Font.ITALIC, 20);
    private Font recipeNameFont = new Font("monogram", Font.ITALIC, 16);
    private Font timeFont = new Font("monogram", Font.PLAIN, 40);
    private Font settingsFont = new Font("monogram", Font.PLAIN, 32);
    private Font nameFont2 = new Font("monogram", Font.PLAIN, 50);
	
	//COUNTERS
	private int timeAnimationCounter, timeAnimationSpeed, currentTimeAnimation;
	private double clickCooldown = 0;
	private int titleAnimationCounter, titleAnimationSpeed, currentTitleAnimation, titleAnimationSpeedFactor;
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
		
		darkened = new Color(30, 30, 30, 200);
		craftColour1 = new Color(232, 193, 112);
		titleColour1 = new Color(51, 60, 58);
	    titleColour2 = new Color(87, 87, 87);
        orderTextColour = new Color(145, 102, 91);
        
        //titleAnimationSpeedFactor = 4;
        titleAnimationSpeedFactor = 30;
		
		importImages();
	}
	
	protected BufferedImage importImage(String filePath) { //Imports and stores the image
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }
	
	private void importImages() {
		titleBackground = importImage("/UI/titlescreen/DeskBase.png");
		recipeBorder = importImage("/UI/recipe/orderBorder.png");
		timeBorder = importImage("/UI/weather/TimeBorder.png");
		timeHeader = importImage("/UI/weather/3.png");
		timeFrame = importImage("/UI/weather/TimeFrame.png");
		coinImage = importImage("/UI/Coin.png");
		mysteryOrder = importImage("/UI/recipe/MysteryOrder.png");
		
		timeAnimations = new BufferedImage[20][20];
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
		
		titleBookAnimations = new BufferedImage[10][10];
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
		highlight1 = importImage("/UI/settings/Highlight1.png");
		highlight2 = importImage("/UI/settings/Highlight2.png");
		highlight3 = importImage("/UI/settings/Highlight3.png");
		highlight4 = importImage("/UI/settings/Highlight4.png");
		uncheckedBox = importImage("/UI/settings/CheckBox.png").getSubimage(0, 0, 9, 9);
		checkedBox = importImage("/UI/settings/CheckBox.png").getSubimage(9, 0, 9, 9);
		underline = importImage("/UI/settings/Underline.png");
		
		leftProgress1 = importImage("/UI/levels/LeftProgress.png").getSubimage(0, 0, 46/2, 20);	
		leftProgress2 = importImage("/UI/levels/LeftProgress.png").getSubimage(46/2, 0, 46/2, 20);	
		middleProgress1 = importImage("/UI/levels/MiddleProgress.png").getSubimage(0, 0, 6, 20);	
		middleProgress2 = importImage("/UI/levels/MiddleProgress.png").getSubimage(6, 0, 6, 20);	
		rightProgress1 = importImage("/UI/levels/RightProgress.png").getSubimage(0, 0, 12, 20);	
		rightProgress2 = importImage("/UI/levels/RightProgress.png").getSubimage(12, 0, 12, 20);	

		cursedRecipeBorder = importImage("/UI/recipe/HauntedOrderBorder.png");
		starLevel = importImage("/UI/recipe/Star.png");
		saveBorder = importImage("/UI/saves/SaveUI.png");
		deleteSave = importImage("/UI/saves/DeleteSave.png");
		
		dialogueFrame = importImage("/UI/customise/CustomiseFrame.png");
		bookIcons = new BufferedImage[4];
		bookIcons[0] = importImage("/UI/BookIcons.png").getSubimage(0, 0, 16, 16);
		bookIcons[1] = importImage("/UI/BookIcons.png").getSubimage(16, 0, 16, 16);
		bookIcons[2] = importImage("/UI/BookIcons.png").getSubimage(0, 16, 16, 16);
		bookIcons[3] = importImage("/UI/BookIcons.png").getSubimage(16, 16, 16, 16);
		bookOpen = importImage("/UI/Book_Open.png");
		bookClosed = importImage("/UI/Book_Closed.png");
		recipeBookBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(0, 0, 16, 16);
		lockedRecipeBorder = importImage("/UI/recipe/RecipeBookBorder.png").getSubimage(16, 0, 16, 16);
		
		achievementBorder = importImage("/UI/achievement/AchievementBorder.png");
		achievement = importImage("/UI/achievement/AchievementUI.png");
		lockedAchievement = importImage("/UI/achievement/LockedAchievement.png");
		mysteryIcon = importImage("/UI/catalogue/MysteryCrate.png");
		mysteryCrateUI = importImage("/UI/catalogue/MysteryCrateUI.png");
	}
	protected BufferedImage[] importFromSpriteSheet(String filePath, int columnNumber, int rowNumber, int startX, int startY, int width, int height) {
		BufferedImage animations[] = new BufferedImage[20];
	    int arrayIndex = 0;

	    BufferedImage img = importImage(filePath);

	    for(int i = 0; i < columnNumber; i++) {
	    	for(int j = 0; j < rowNumber; j++) {
	    		animations[arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	            arrayIndex++;
	        }
	    } 
	    return animations;
	}
	public Font getNumberFont() {
		return numberFont;
	}
	public int getXforCenteredText(String text, Graphics2D g2) {
        int x;
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.frameWidth/2 - length/2;
        return x;
    }
	private int getTextWidth(String text, Graphics2D g2) {
        return (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }
	private int getTextHeight(String text, Graphics2D g2) {
        return (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
    }
	private boolean isHovering(String text, int x1, int y1, Graphics2D g2) {
		
		int x = gp.mouseI.mouseX;
		int y = gp.mouseI.mouseY;
		
		if(x > x1 && x < x1 + getTextWidth(text, g2) && y > y1 && y < y1 + getTextHeight(text, g2)) {
			return true;
		}
		return false;
	}
	private boolean isHovering(int x1, int y1, int w, int h) {
		
		int x = gp.mouseI.mouseX;
		int y = gp.mouseI.mouseY;
		
		if(x > x1 && x < x1 + w && y > y1 && y < y1 + h) {
			return true;
		}
		return false;
	}
	
	private void drawTitleScreen(Graphics2D g2) {
		
		g2.drawImage(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5), null);
		
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
		
		g2.drawImage(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5), null);

		
		if(titlePageNum == 0 || titleAnimationCounter > 6) {
			
			g2.setFont(fancyTitleFont);
			g2.setColor(titleColour1);
			String text = "Pandemonia";
			
			int x = 110;
			int y = 240;
			g2.drawString(text, x, y);
			
			g2.fillRect(x, y+ 20, getTextWidth(text, g2), 10);
			
			//PLAY
			g2.setFont(titleFont);
			g2.setColor(titleColour1);
			text = "Play";
			
			int mouseX = gp.mouseI.mouseX;
			int mouseY = gp.mouseI.mouseY;
			
			x = 120;
		    y = 360;
			
			if(isHovering(text, x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					if(clickCooldown == 0) {
						gp.currentState = gp.startGameSettingsState;
						clickCooldown = 0.5;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);
			}
			
			g2.drawString(text, x, y);
		
			//SETTINGS
			g2.setColor(titleColour1);
			text = "Settings";
	
			x = 120;
			y = 450;
			
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					if(clickCooldown == 0) {
						//ENTER SETTINGS
						//gp.currentState = gp.settingsState;
						clickCooldown = 0.33;
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);
			}
			
			g2.drawString(text, x, y);
	
			//QUIT
			g2.setColor(titleColour1);
			text = "Quit";
	
			x = 120;
			y = 450+90;
			
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					if(clickCooldown == 0) {
						//QUIT GAME
						System.exit(0);
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);
			}
			
			g2.drawString(text, x, y);
		}
		
	}
	private void drawRecipesScreen(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		BufferedImage img = bookOpen;
		g2.drawImage(img, gp.frameWidth/2-img.getWidth()/2*6, gp.frameHeight/2-img.getHeight()/2*6, img.getWidth()*6, img.getHeight()*6, null);
		
		
		int mouseX = gp.mouseI.mouseX;
		int mouseY = gp.mouseI.mouseY;
		
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
			if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
				if(gp.currentState == gp.recipeState) {
					gp.currentState = gp.pauseState;
				} else {
					gp.currentState = gp.recipeState;
				}
				clickCooldown = 0.33;
			}
		}
		
		g2.drawImage(bookIcons[i], x, y, 64, 64, null);
		
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
			    g2.setColor(titleColour1);
			    g2.setFont(recipeNameFont);
			    FontMetrics fm = g2.getFontMetrics();
			    int textWidth = fm.stringWidth(name);
			    int textX = x + (borderSize - textWidth) / 2;
			    int textY = y - 6;
			    g2.drawString(name, textX, textY);
		    }

		    // ------------------------------------
		    // BORDER
		    // ------------------------------------
		    if (isUnlocked)
		        g2.drawImage(recipeBookBorder, x, y, borderSize, borderSize, null);
		    else
		        g2.drawImage(lockedRecipeBorder, x, y, borderSize, borderSize, null);

		    // ------------------------------------
		    // PLATED IMAGE
		    // ------------------------------------
		    if (isUnlocked && recipe.finishedPlate != null) {
		        BufferedImage plate = recipe.finishedPlate;
		        int plateX = x + (borderSize - plate.getWidth()) / 2;
		        int plateY = y + (borderSize - plate.getHeight()) / 2;
		        g2.drawImage(plate, plateX - 16, plateY - 14, 48, 48, null);
		    }

		    // ------------------------------------
		    // STARS
		    // ------------------------------------
		    if (isUnlocked) {
		        int stars = recipe.getStarLevel();
		        int starsY = y + borderSize + 4;

		        for (int k = 0; k < stars; k++) {
		            int starX = x + k * starSpacing;
		            g2.drawImage(starLevel, starX, starsY, 16, 16, null);
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
	public void drawMultiplayerGameSettings(Graphics2D g2) {
		
		g2.drawImage(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5), null);
		
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
		g2.drawImage(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5), null);

		if(titleAnimationCounter > 6) {
			g2.setFont(fancyTitleFont);
			g2.setColor(titleColour1);
			String text = "Multiplayer";
			
			int x = 110;
			int y = 220;
			g2.drawString(text, x, y);
			
			g2.fillRect(x, y+ 20, getTextWidth(text, g2), 10);
			text = "Settings";
					
		    x = 110;
		    y = 230+80;
			g2.drawString(text, x, y);
					
			g2.fillRect(x, y+20, getTextWidth(text, g2), 10);
		
		//HOST
		g2.setFont(titleFont);
		g2.setColor(titleColour1);
		text = "Host";
			
		x = 120;
		y = 390;
			
		if(isHovering(text,x, y-40, g2)) {
			g2.setColor(titleColour2);
			if(gp.mouseI.leftClickPressed) {
				if(clickCooldown == 0) {
					//ENTER GAME
					hostSelected = true;
					usernameActive = true;
					gp.currentState = gp.writeUsernameState;
					clickCooldown = 0.16;
				}
			}
			g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

		}
			
			g2.drawString(text, x, y);
			
			//MULTIPLAYER
			g2.setColor(titleColour1);
			text = "Join";

			x = 120;
			y = 480;
			
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					//ENTER MULTIPLAYER
					if(clickCooldown == 0) {
					    gp.currentState = gp.lanJoinMenuState;
					    gp.startDiscovery();
						clickCooldown = 0.16;
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

			}
			
			g2.drawString(text, x, y);

			//QUIT
			g2.setColor(titleColour1);
			text = "Back";

			x = 100;
			y = 660;
			
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					//QUIT GAME
					if(clickCooldown == 0) {
						gp.currentState = gp.startGameSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 2;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

			}
			g2.drawString(text, x, y);

		}
	}
	public void drawLanJoinMenu(Graphics2D g2) {

	    // Background
	    g2.drawImage(titleBackground, 
	        (gp.frameWidth/2) - (int)((768*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((560*1.5)/2), 
	        (int)(768*1.5), (int)(560*1.5), null);

	    // Book animation
	    titleAnimationSpeed++;
	    if (titleAnimationSpeed >= titleAnimationSpeedFactor) {
	        titleAnimationSpeed = 0;
	        titleAnimationCounter++;
	    }
	    if (titleBookAnimations[currentTitleAnimation][titleAnimationCounter] == null) {
	        titleAnimationCounter--;
	    }
	    g2.drawImage(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], 
	        (gp.frameWidth/2) - (int)((848*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((640*1.5)/2), 
	        (int)(848*1.5), (int)(640*1.5), null);

	    if (titleAnimationCounter > 6) {
	        // Title
	        g2.setFont(fancyTitleFont);
	        g2.setColor(titleColour1);
	        String text = "Available LAN Worlds";
	        int x = gp.frameWidth/2 - getTextWidth(text, g2)/2;
	        int y = 200;
	        g2.drawString(text, x, y);
	        g2.fillRect(x, y+20, getTextWidth(text, g2), 10);

	        // Server list
	        g2.setFont(titleFont);
	        int startY = 320;
	        int index = 0;

	        if (gp.discovery != null) {
	            startY = 320;
	            index = 0;
	            for (DiscoveredServer server : gp.discovery.getDiscoveredServers()) {
	                text = server.worldName + " (Host: " + server.hostUser + ")";
	                x = 140;
	                y = startY + index * 60;

	                g2.setColor(titleColour1);
	                if (isHovering(text, x, y-40, g2)) {
	                    g2.setColor(titleColour2);
	                    if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                        // Select this server
	                        gp.selectedServer = server;
	                        gp.currentState = gp.writeUsernameState;
	                        usernameActive = true;
	                        joinSelected = true;
	                        clickCooldown = 0.25;
	                    }
	                    g2.fillRect(x, y+14, getTextWidth(text, g2), 6);
	                }
	                g2.drawString(text, x, y);
	                index++;
	            }
	        } else {
	            g2.setColor(Color.GRAY);
	            g2.drawString("Searching for games on LAN...", 140, 320);
	        }

	        // Back button
	        g2.setColor(titleColour1);
	        text = "Back";
	        x = 100;
	        y = 660;
	        if (isHovering(text, x, y-40, g2)) {
	            g2.setColor(titleColour2);
	            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                gp.currentState = gp.multiplayerSettingsState;
	                clickCooldown = 0.33;
	                currentTitleAnimation = 2;
	                titleAnimationCounter = 0;
	                titleAnimationSpeed = 0;
	                gp.stopDiscovery();
	            }
	            g2.fillRect(x, y+14, getTextWidth(text, g2), 6);
	        }
	        g2.drawString(text, x, y);
	    }
	}
	public void drawGameSettingsScreen(Graphics2D g2) {

		g2.drawImage(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5), null);
		
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
		
		g2.drawImage(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5), null);
		
		if(titleAnimationCounter > 6) {
		g2.setFont(fancyTitleFont);
		g2.setColor(titleColour1);
		String text = "Play";
		
		int x = 110;
		int y = 240;
		g2.drawString(text, x, y);
		
		g2.fillRect(x, y+ 20, getTextWidth(text, g2), 10);
		
		//SinglePlayer
			g2.setFont(titleFont);
			g2.setColor(titleColour1);
			text = "Singleplayer";
				
			x = 120;
			y = 360;
				
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					if(clickCooldown == 0) {
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
						singleplayerSelected = true;
						gp.currentState = gp.chooseSaveState;
					}
				}
				g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

			}
				
				g2.drawString(text, x, y);
				
				//MULTIPLAYER
				g2.setColor(titleColour1);
				text = "Multiplayer";

				x = 120;
				y = 450;
				
				if(isHovering(text,x, y-40, g2)) {
					g2.setColor(titleColour2);
					if(gp.mouseI.leftClickPressed) {
						//ENTER MULTIPLAYER
						gp.currentState = gp.multiplayerSettingsState;
						clickCooldown = 0.33;
						currentTitleAnimation = 1;
						titleAnimationCounter = 0;
						titleAnimationSpeed = 0;
					}
					g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

				}
				
				g2.drawString(text, x, y);

				//QUIT
				g2.setColor(titleColour1);
				text = "Back";

				x = 100;
				y = 660;
				
				if(isHovering(text,x, y-40, g2)) {
					g2.setColor(titleColour2);
					if(gp.mouseI.leftClickPressed) {
						//QUIT GAME
						if(clickCooldown == 0) {
							gp.currentState = gp.titleState;
							clickCooldown = 0.33;
							currentTitleAnimation = 2;
							titleAnimationCounter = 0;
							titleAnimationSpeed = 0;
						}
					}
					g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

				}
				
				g2.drawString(text, x, y);
		}
	}
	private void drawChooseSaveState(Graphics2D g2) {
		
		g2.drawImage(titleBackground, (gp.frameWidth/2) - (int)((768*1.5) / 2), (gp.frameHeight/2) - (int)((560*1.5)/2), (int)(768*1.5), (int)(560*1.5), null);
		
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
		
		g2.drawImage(titleBookAnimations[currentTitleAnimation][titleAnimationCounter], (gp.frameWidth/2) - (int)((848*1.5) / 2), (gp.frameHeight/2) - (int)((640*1.5)/2), (int)(848*1.5), (int)(640*1.5), null);
		
		if(titleAnimationCounter > 6) {
			
			int x = 100;
			int y = 200;
			String text = "Choose Save";
			
			int saveChosen = -1;
			
			g2.setFont(fancyTitleFont);
			g2.setColor(titleColour1);
			g2.drawString(text, x, y);
			
			text = "Save 1";
			
			x = 100;
			y = 260 - 40;
			
			int result = drawSave(g2, 1, x, y, text);
			if (result != -1) saveChosen = result;
			
			g2.setColor(titleColour1);
			text = "Save 2";
			x = 100;
			y = 260 + 48*3 + 10 - 40;
			
			result = drawSave(g2, 2, x, y, text);
			if (result != -1) saveChosen = result;
			
			g2.setColor(titleColour1);
			text = "Save 3";
			x = 100;
			y = 260 + 48*6 + 20 - 40;
			
			result = drawSave(g2, 3, x, y, text);
			if (result != -1) saveChosen = result;
			
			//QUIT
			g2.setFont(saveFont);
			g2.setColor(titleColour1);
			text = "Back";

			x = 100;
			y = 696;
			
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
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
				g2.fillRect(x, y+ 6, getTextWidth(text, g2), 4);
			}
			g2.drawString(text, x, y);
			
			if(doDestroySave) {
				g2.setFont(saveFont);
				g2.setColor(titleColour1);
				g2.drawString("Are you sure you want", 620, 220);
				g2.drawString("  to delete this save", 620, 240);
				
				y+= 40;
				text = "Yes";

				x = 660;
				y = 400;
				
				if(isHovering(text,x, y-40, g2)) {
					g2.setColor(titleColour2);
					if(gp.mouseI.leftClickPressed) {
						if(clickCooldown == 0) {
							clickCooldown = 0.33;
							gp.saveM.clearSaveSlot(destroySaveNum);
							destroySaveNum = 0;
							doDestroySave = false;
						}
					}
					g2.fillRect(x, y+ 6, getTextWidth(text, g2), 4);
				}
				g2.drawString(text, x, y);
				
				g2.setColor(titleColour1);
				text = "No";

				x += 200;
				y = 400;
				
				if(isHovering(text,x, y-40, g2)) {
					g2.setColor(titleColour2);
					if(gp.mouseI.leftClickPressed) {
						if(clickCooldown == 0) {
							clickCooldown = 0.33;
							destroySaveNum = 0;
							doDestroySave = false;
						}
					}
					g2.fillRect(x, y+ 6, getTextWidth(text, g2), 4);
				}
				g2.drawString(text, x, y);
			}
			
			if(saveChosen != -1 && !doDestroySave) {					
				g2.setFont(titleFont);
				g2.setColor(Color.WHITE);
				text = "LOADING";
					
				x = 20;
				y = 800;
				
				g2.drawString(text, x, y);
				if(singleplayerSelected) {
					gp.playSinglePlayer(saveChosen);
					singleplayerSelected = false;
				}
			}
		}
	}
	private int drawSave(Graphics2D g2, int saveSlot, int x, int y, String text) {
		int saveChosen = -1;
		if(!gp.saveM.isSlotEmpty(saveSlot)) {
			g2.setFont(saveFont);
			g2.drawImage(saveBorder, x, y, 130*3, 48*3, null);
			
			if(isHovering(x, y, 130*3, 48*3)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					saveChosen = saveSlot;
				}
			}
			g2.drawString(text, 120, y+50);
			
			//g2.setColor(titleColour1);
			g2.setFont(saveFont2);
			g2.drawString(gp.saveM.getSavedSeason(saveSlot) + " Day " + Integer.toString(gp.saveM.getSavedDay(saveSlot)), 120, y+80);
			
			g2.drawImage(coinImage, 120, y+84, 16*3, 16*3, null);
			g2.drawString(Integer.toString(gp.saveM.getSavedMoney(saveSlot)), 176, y+114);
			
			g2.fillRect(x + 176-4, y + 5+12-4, 180+8, 110+8);
			
			g2.drawImage(deleteSave, x + 130*3, y+84, 16*3, 18*3, null);
			if(isHovering(x + 130*3, y+84, 16*3, 18*3)) {
				if(gp.mouseI.leftClickPressed) {
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
			        BufferedImage preview = ImageIO.read(previewFile);
			        g2.drawImage(preview, x + 176, y + 5+12, 180, 110, null); 
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
		} else {
			text = "New Save";
			g2.setFont(saveFont);
			g2.drawImage(saveBorder, x, y, 130*3, 48*3, null);
			
			if(isHovering(x, y, 130*3, 48*3)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
					saveChosen = saveSlot;
				}
			}
			g2.drawString(text, 120, y+50);
		}
		
		return saveChosen;
	}
	public void drawUsernameInput(Graphics2D g2) {
	    
	    // Background (reuse your title background style if you want)
	    g2.drawImage(titleBackground, 
	        (gp.frameWidth/2) - (int)((768*1.5) / 2), 
	        (gp.frameHeight/2) - (int)((560*1.5)/2), 
	        (int)(768*1.5), 
	        (int)(560*1.5), 
	        null);

		
	    boolean drawLoadingScreen = false;
	    // Title
	    g2.setFont(fancyTitleFont);
	    g2.setColor(titleColour1);
	    String text = "Enter Username";
	    int x = gp.frameWidth/2 - getTextWidth(text, g2)/2;
	    int y = 200;
	    g2.drawString(text, x, y);

	    // Username box
	    int boxWidth = 400;
	    int boxHeight = 50;
	    int boxX = gp.frameWidth/2 - boxWidth/2;
	    int boxY = 300;

	    g2.setColor(new Color(0, 0, 0, 150));
	    g2.fillRect(boxX, boxY, boxWidth, boxHeight);
	    g2.setColor(Color.WHITE);
	    g2.drawRect(boxX, boxY, boxWidth, boxHeight);

	    // Username text
	    g2.setFont(titleFont);
	    g2.setColor(Color.WHITE);
	    g2.drawString(username, boxX + 10, boxY + 35);

	    // Blinking caret
	    caretBlinkCounter++;
	    if(caretBlinkCounter > 120) caretBlinkCounter = 0;
	    if(caretBlinkCounter < 60 && usernameActive) {
	        int caretX = boxX + 10 + getTextWidth(username, g2);
	        g2.fillRect(caretX, boxY + 10, 2, 30);
	    }

	    // "Confirm" button
	    text = "Confirm";
	    x = gp.frameWidth/2 - getTextWidth(text, g2)/2;
	    y = 420;
	    g2.setColor(titleColour1);

	    if(isHovering(text, x, y-40, g2)) {
	        g2.setColor(titleColour2);
	        if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	        	drawLoadingScreen = true;
	        	startLoading = true; 
	            clickCooldown = 0.33;
	        }
	        g2.fillRect(x, y+14, getTextWidth(text, g2), 6);
	    }
	    g2.drawString(text, x, y);
	    
		g2.setColor(titleColour1);
		text = "Back";

		x = 100;
		y = 660;
		
		if(isHovering(text,x, y-40, g2)) {
			g2.setColor(titleColour2);
			if(gp.mouseI.leftClickPressed) {
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
			g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

		}
		g2.drawString(text, x, y);
		
		if(drawLoadingScreen) {
			
			g2.setFont(titleFont);
			g2.setColor(Color.WHITE);
			text = "LOADING...";
				
			x = 30;
			y = 720;
			
			g2.drawString(text, x, y);
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
	private void drawAchievementsScreen(Graphics2D g2) {
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		BufferedImage img = achievementBorder;
		int x = gp.frameWidth/2-img.getWidth()/2*6;
		int y = gp.frameHeight/2-img.getHeight()/2*6;
		g2.drawImage(img, x, y, img.getWidth()*6, img.getHeight()*6, null);
		
		g2.setColor(titleColour1);
		g2.setFont(pauseFont);
		g2.drawString("Achievements", x+36, y+68);
		
		g2.setFont(pauseFont);
		String text = "BACK";
		x =getXforCenteredText(text, g2);
		if(isHovering(text, x, 650, g2)) {
			g2.setColor(craftColour1);
			if(gp.mouseI.leftClickPressed) {
				if(clickCooldown == 0) {
					//QUIT
					gp.currentState = gp.pauseState;
					clickCooldown = 0.16;
				}
			}
		}else {
			g2.setColor(Color.WHITE);
		}
		g2.drawString(text, getXforCenteredText(text, g2), 700);
		
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
	        g2.drawImage(achievement, startX, yPos, 44*6, 27*6, null);
	        // Optional: gray out if not unlocked
	        if (!a.isUnlocked()) {
		        g2.drawImage(lockedAchievement, startX, yPos, 44*6, 27*6, null);
	        }

	        // Draw achievement icon
	        if (a.getIcon() != null) {
	        	BufferedImage icon = a.getIcon();
	        	if(!a.isUnlocked()) {
	        		icon = CollisionMethods.getMaskedImage(Color.BLACK, icon);
	        	}
	            g2.drawImage(icon, startX + iconOffsetX+ 80, yPos + 80, icon.getWidth()*3, icon.getHeight()*3, null);
	        }

	        // Draw text: name (bold) and description
	        g2.setColor(titleColour1);
	        g2.setFont(saveFont);
	        g2.drawString(a.getName(), startX + textOffsetX-28, yPos + 45);

	        g2.setFont(saveFont2);
	        
	    	text = a.getDescription();
			for(String line: gp.gui.wrapText(text, g2, 49*5)) {
				g2.drawString(line, startX + textOffsetX-28, yPos + 70);
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
	public void drawAchievementNotification(Graphics2D g2) {
	    if (notificationAchievement == null) return;

	    int popupScale = 6;
	    BufferedImage img = achievement; // same as menu
	    int x = gp.frameWidth - img.getWidth() * popupScale - 20;
	    int y = gp.frameHeight - img.getHeight() * popupScale - 20; // top of screen (or wherever you like)
	    
	    // Draw border
	    g2.drawImage(img, x, y, img.getWidth() * popupScale, img.getHeight() * popupScale, null);
	    
	    // Draw icon
	    BufferedImage icon = notificationAchievement.getIcon();
	    if (icon != null) {
	        int iconX = x + 92;
	        int iconY = y + 80;
	        g2.drawImage(icon, iconX+80, iconY, icon.getWidth() * 3, icon.getHeight() * 3, null);
	    }
	    
	    // Draw name
	    g2.setColor(titleColour1);
	    g2.setFont(saveFont);
	    g2.drawString(notificationAchievement.getName(), x + 50-15, y + 45);

	    // Draw description
	    g2.setFont(saveFont2);
	    g2.drawString(notificationAchievement.getDescription(), x + 22, y + 70);
	}
	public void draw(Graphics2D g2) {
		
		switch(gp.currentState) {
		case 0:
			drawTitleScreen(g2);
			break;
		case 1: //PLAY
			drawPlayScreen(g2);
			break;
		case 3: //PAUSE
			drawPauseScreen(g2);
			break;
		case 4: //GAME SETTINGS
			drawGameSettingsScreen(g2);
			break;
		case 5: 
			switch(settingsState) {
			case baseSettings:
				drawBaseSettingsScreen(g2);
				break;
			case generalState:
				drawGeneralSettings(g2);
				break;
			case videoState:
				drawVideoSettings(g2);
				break;
			case audioState:
				drawAudioSettings(g2);
				break;
			case multiplayerState:
				drawMultiplayerSettings(g2);
				break;
			}
			break;
		case 6: //MULTIPLAYER GAME SETTINGS
			drawMultiplayerGameSettings(g2);
			break;
		case 8:
			drawUsernameInput(g2);
			break;
		case 9:
			drawLanJoinMenu(g2);
			break;
		case 11:
			drawCatalogueState(g2);
			break;
		case 12:
			drawXPScreen(g2);
			break;
		case 13:
			drawRecipeSelectScreen(g2);
			break;
		case 14:
			drawChooseSaveState(g2);
			break;
		case 15:
			drawChooseUpgradeScreen(g2);
			break;
		case 16:
			drawDialogueState(g2);
			break;
		case 17:
			drawAchievementsScreen(g2);
			break;
		case 18:
			drawRecipesScreen(g2);
			break;
		}
		
		drawAchievementNotification(g2);
		
		if(firstDraw) {
			g2.setFont(numberFont);
			g2.drawString("", -1000, -1000);
			firstDraw = false;
		}

		
	}
	
	private void drawPlayScreen(Graphics2D g2) {
		updateMessages();
		List<Recipe> currentOrders = new ArrayList<>(RecipeManager.getCurrentOrders());

		int i = 0;
		
		for (Recipe recipe : currentOrders) {
		    RecipeRenderData data = renderCache.get(recipe);
		    if (data == null) continue;

		    int x = 8 + i * (36*3);
		    int y = 0;

		    // BASE
		    g2.drawImage(data.borderImage, x, y, 96, 144, null);
		    if (data.customer.hideOrder) {
		        // Overlay the "mystery order" image
		        g2.drawImage(data.mysteryOrderImage, x, y, 96, 144, null);
		    } else {
		        // Normal ingredient + text drawing
		        for (int j = 0; j < data.ingredientImages.size(); j++) {
		            int dx = x + j * (10*3) + 4;
		            int dy = y + 4;
		            g2.drawImage(data.ingredientImages.get(j), dx, dy, 30, 30, null);
		            g2.drawImage(data.cookingStateIcons.get(j), dx, dy + 16, 30, 30, null);
		            g2.drawImage(data.secondaryCookingStateIcons.get(j), dx, dy + 40, 30, 30, null);
		        }

		        if(!recipe.isCursed) {
			        g2.setColor(orderTextColour);
		        } else {
		        	g2.setColor(Color.WHITE);
		        }

		        g2.setFont(nameFont);
		        int counter = 0;
		        for (int l = 0; l < data.nameLines.size(); l++) {
		            String line = data.nameLines.get(l);
		            int offset = data.nameLineOffsets.get(l);
		            g2.drawString(line, x + offset, y + 84 + counter);
		            counter += 15;
		        }
		        for (int j = 0; j < data.starLevel; j++) {
			        g2.drawImage(starLevel, x +10 + j * 36, y + 50, 8*3, 8*3, null);
			    }

		        g2.drawImage(data.plateImage, x + 24, y + 94, 48, 48, null);
		    }

		    // COIN + FACE
		    g2.drawImage(data.coinImage, x - 8, y + 142, 48, 48, null);
		    g2.drawImage(data.faceIcon, x + 32, y + 118, 96, 96, null);

		    // COST
		    g2.setColor(Color.WHITE);
		    g2.setFont(timeFont);
		    g2.drawString(data.cost, x + 36, y + 174);

		    // PATIENCE
		    drawPatienceBar(g2, x, y + 208, (int)data.customer.getPatienceCounter(), (int)data.customer.getMaxPatienceTime());

		    i++;
		}

		//DRAW TIME AND WEATHER
		g2.drawImage(timeBorder, gp.frameWidth - (78*3) - 4, 4, 78*3, 77*3, null);
		
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
		g2.drawImage(timeAnimations[currentTimeAnimation][timeAnimationCounter], gp.frameWidth - (78*3) - 4, 4, 78*3, 77*3, null);
		g2.drawImage(timeHeader, gp.frameWidth - (80*3), 4 + (77*3) - 2, 80*3, 16*3, null);
		g2.drawImage(timeFrame, gp.frameWidth - (80*3), 4 + (77*3) - 2 + (16*3), 80*3, 16*3, null);
		
		g2.setFont(timeFont);
		g2.setColor(orderTextColour);
		g2.drawString(gp.world.getDate(), gp.frameWidth - (80*3) + 28, 4 + (77*3) + 28);
		g2.drawString(gp.world.getTime24h(), gp.frameWidth - (80*3) + 112, 4 + (77*3) + 26 + (16*3));
		
		g2.setColor(Color.WHITE);
		g2.setFont(timeFont);
		g2.drawImage(coinImage, 20,  gp.frameHeight - 48 - 20, 48, 48, null);
		g2.drawString(Integer.toString(gp.player.wealth), 20 + 48+8, gp.frameHeight - 48 - 20 +32);
		
		// === Messages ===
		int msgX = 20+20;
		int msgY = gp.frameHeight - 120; // bottom margin
		int lineHeight = 28;

		g2.setFont(timeFont);

		for (int k = 0; k < messages.size(); k++) {
		    GUIMessage msg = messages.get(k);

		    // Calculate fade alpha
		    float lifeRatio = msg.lifetime / (float) msg.maxLifetime;
		    int alpha = (int)(255 * lifeRatio);
		    if (alpha < 0) alpha = 0;

		    // Shadow
		    g2.setColor(new Color(0, 0, 0, alpha));
		    g2.drawString(msg.text, msgX, msgY - k * lineHeight + 3);

		    // Main text
		    g2.setColor(new Color(msg.color.getRed(), msg.color.getGreen(), msg.color.getBlue(), alpha));
		    g2.drawString(msg.text, msgX, msgY - k * lineHeight);
		}
		
	}
	public void addOrder(Recipe recipe, Customer customer, Graphics2D g2) {
	    RecipeRenderData data = buildRenderData(recipe, customer, nameFont, g2);
	    renderCache.put(recipe, data);
	}
	public RecipeRenderData buildRenderData(Recipe recipe, Customer customer, Font nameFont, Graphics2D g2) {
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

	        BufferedImage ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
	        if (ingredient.notRawItem) {
	            ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
	        }

	        data.ingredientImages.add(ingredientImage);
	        data.cookingStateIcons.add(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed));
	        data.secondaryCookingStateIcons.add(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed));
	    }
	    
	    // Cache text layout
	    g2.setFont(nameFont);
	    for (String line : recipe.getName().split(" ")) {
	        data.nameLines.add(line);
	        int offset = (48 - getTextWidth(line, g2) / 2);
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
	private void drawPatienceBar(Graphics2D g2, float worldX, float worldY, int patienceCounter, int maxPatienceTime) {

	    int barWidth = 32 * 3;
	    int barHeight = 12;
	    int yOffset = -20;

	    float progress = 1f - (patienceCounter / (float) maxPatienceTime);
	    progress = Math.max(0, Math.min(1, progress));

	    // Green → Orange → Red zones
	    Color c;
	    if(progress > 0.66f) c = Color.GREEN;
	    else if(progress > 0.33f) c = Color.ORANGE;
	    else c = Color.RED;

	    g2.setColor(Color.BLACK);
	    g2.fillRect((int)worldX, (int)(worldY + yOffset), barWidth, barHeight);

	    g2.setColor(c);
	    g2.fillRect((int)worldX, (int)(worldY + yOffset), (int)(barWidth * progress), barHeight);
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
	private void drawPauseScreen(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		g2.setColor(Color.WHITE);
		g2.setFont(headingFont);
		String text = "PAUSED";
		g2.drawString(text, getXforCenteredText(text, g2), 200);
		
		text = "RESUME";
		int x =getXforCenteredText(text, g2);
		if(isHovering(text, x, 350, g2)) {
			g2.setColor(craftColour1);
			if(gp.mouseI.leftClickPressed) {
				//RESUME
				gp.currentState = 1;
			}
		}
		g2.setFont(pauseFont);
		g2.drawString(text, getXforCenteredText(text, g2), 400);
		
		text = "SETTINGS";
		x =getXforCenteredText(text, g2);
		if(isHovering(text, x, 450, g2)) {
			g2.setColor(craftColour1);
			if(gp.mouseI.leftClickPressed) {
				if(clickCooldown == 0) {
					//SETTINGS
					gp.currentState = gp.settingsState;
					settingsState = 0;
					clickCooldown = 0.33;
				}
			}
		}else {
			g2.setColor(Color.WHITE);
		}
		g2.drawString(text, getXforCenteredText(text, g2), 500);
		
		text = "Achievements";
		x =getXforCenteredText(text, g2);
		if(isHovering(text, x, 550, g2)) {
			g2.setColor(craftColour1);
			if(gp.mouseI.leftClickPressed) {
				if(clickCooldown == 0) {
					gp.currentState = gp.achievementState;
					clickCooldown = 0.33;
					currentTitleAnimation = 0;
				}
			}
		} else {
			g2.setColor(Color.WHITE);
		}
			g2.drawString(text, getXforCenteredText(text, g2), 600);
			
			text = "QUIT";
			x =getXforCenteredText(text, g2);
			if(isHovering(text, x, 650, g2)) {
				g2.setColor(craftColour1);
				if(gp.mouseI.leftClickPressed) {
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
			g2.setColor(Color.WHITE);
		}
		g2.drawString(text, getXforCenteredText(text, g2), 700);
		
		
		int mouseX = gp.mouseI.mouseX;
		int mouseY = gp.mouseI.mouseY;
		
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
			if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
				if(gp.currentState == gp.recipeState) {
					gp.currentState = gp.pauseState;
				} else {
					gp.currentState = gp.recipeState;
				}
				clickCooldown = 0.33;
			}
		}
		
		g2.drawImage(bookIcons[i], x, y, 64, 64, null);
		
		
	}	
	private void drawBaseSettingsScreen(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		g2.drawImage(settingsFrame, x, y, 112*3, 112*3, null);
		
		//Categories
		g2.setFont(timeFont);
		x += 40;
		y += 140;
		
		g2.setColor(titleColour1);
		String text = "General";
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = generalState;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, x-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
		g2.setColor(titleColour1);
		text = "Video";
		y+= 40;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = videoState;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, x-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
		g2.setColor(titleColour1);
		text = "Audio";
		y+= 40;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = audioState;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, x-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
		if(gp.multiplayer) {
			g2.setColor(titleColour1);
			text = "Multiplayer";
			y+= 40;
			if (isHovering(text, x, y-24, g2)) {
	            g2.setColor(titleColour2);
	            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                settingsState = multiplayerState;
	                clickCooldown = 0.33;
	            }
	    		g2.drawImage(underline, x-60, y-16, 80*3, 16*3, null);
	        }
			g2.drawString(text, x, y);
		}
		
		g2.setColor(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                gp.currentState = gp.pauseState;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
	}	
	private void drawGeneralSettings(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		g2.drawImage(generalSettingsFrame, x, y, 112*3, 112*3, null);
		
		String text;
		g2.setFont(timeFont);
		x += 40;
		y += 140;
		
		
		
		g2.setColor(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
	}
	private void drawVideoSettings(Graphics2D g2) {
		int boxOffset = 220;
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		g2.drawImage(videoSettingsFrame, x, y, 112*3, 112*3, null);
		
		String text;
		g2.setFont(settingsFont);
		x += 40;
		y += 140;
		
		g2.setColor(titleColour1);
		text = "FullScreen";
		g2.drawString(text, x, y);
		y-= 20;
		if(Settings.fullScreen) {
			g2.drawImage(checkedBox, x+boxOffset, y, 9*3, 9*3, null);
		} else {
			g2.drawImage(uncheckedBox, x+boxOffset, y, 9*3, 9*3, null);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(g2, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
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
		g2.setColor(titleColour1);
		text = "Fancy Lighting";
		g2.drawString(text, x, y);
		y-= 20;
		if(Settings.fancyLighting) {
			g2.drawImage(checkedBox, x+boxOffset, y, 9*3, 9*3, null);
		} else {
			g2.drawImage(uncheckedBox, x+boxOffset, y, 9*3, 9*3, null);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(g2, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
            	Settings.fancyLighting = !Settings.fancyLighting;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		y+=40;
		g2.setColor(titleColour1);
		text = "Bloom";
		g2.drawString(text, x, y);
		y-= 20;
		if(Settings.bloomEnabled) {
			g2.drawImage(checkedBox, x+boxOffset, y, 9*3, 9*3, null);
		} else {
			g2.drawImage(uncheckedBox, x+boxOffset, y, 9*3, 9*3, null);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(g2, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
            	Settings.bloomEnabled = !Settings.bloomEnabled;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		y+=40;
		g2.setColor(titleColour1);
		text = "Light Occlusion";
		g2.drawString(text, x, y);
		y-= 20;
		if(Settings.lightOcclusionEnabled) {
			g2.drawImage(checkedBox, x+boxOffset, y, 9*3, 9*3, null);
		} else {
			g2.drawImage(uncheckedBox, x+boxOffset, y, 9*3, 9*3, null);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(g2, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
            	Settings.lightOcclusionEnabled = !Settings.lightOcclusionEnabled;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		g2.setColor(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
		
	}
	private void drawAudioSettings(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		g2.drawImage(audioSettingsFrame, x, y, 112*3, 112*3, null);
		
		String text;
		g2.setFont(timeFont);
		x += 40;
		y += 140;
		
		
		g2.setColor(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
	}
	private void drawMultiplayerSettings(Graphics2D g2) {
		int boxOffset = 220;
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		int x = gp.frameWidth/2 - ((112*3)/2);
		int y = gp.frameHeight/2 - ((112*3)/2);
		
		g2.drawImage(multiplayerSettingsFrame, x, y, 112*3, 112*3, null);
		
		String text;
		g2.setFont(timeFont);
		x += 40;
		y += 140;
		
		
		g2.setColor(titleColour1);
		text = "Show Usernames";
		g2.drawString(text, x, y);
		y-= 20;
		if(Settings.showUsernames) {
			g2.drawImage(checkedBox, x+boxOffset, y, 9*3, 9*3, null);
		} else {
			g2.drawImage(uncheckedBox, x+boxOffset, y, 9*3, 9*3, null);
		}
		if (isHovering(x+boxOffset, y, 9*3, 9*3)) {
			drawCheckBoxHover(g2, x+boxOffset, y, 9*3, 9*3);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
            	Settings.showUsernames = !Settings.showUsernames;
                clickCooldown = 0.33;
            }
        }
		y += 20;
		
		g2.setColor(titleColour1);
		text = "Back";
		x = 542;
		y = 510;
		if (isHovering(text, x, y-24, g2)) {
            g2.setColor(titleColour2);
            if (gp.mouseI.leftClickPressed && clickCooldown == 0) {
                settingsState = baseSettings;
                clickCooldown = 0.33;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
	}
	public void drawCatalogueState(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		g2.drawImage(computerAnimations[computerAnimationCounter], 0, 0, (int)(260*4.5), (int)(190*4.5), null);
		
		if(computerAnimationCounter >= 9) {
			if(!gp.catalogue.checkingOut && !gp.catalogue.onMysteryScreen) {
				g2.drawImage(shoppingUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				g2.drawImage(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				
				g2.drawImage(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5), null);
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.leftPage();
					}
				}
				g2.drawImage(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5), null);
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.checkingOut = true;
						gp.catalogue.layer = 0;
					}
				}
				
				g2.drawImage(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5), null);
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onMysteryScreen = true;
						gp.catalogue.layer = 0;
					}
				}
				
			} else if(gp.catalogue.onMysteryScreen) {
				g2.drawImage(mysteryCrateUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				
				if(isHovering((int)(87*4.5), (int)(108*4.5), (int)(61*4.5), (int)(11*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.buyMysteryCrate();
						gp.catalogue.layer = 0;
					}
				}
				
				g2.drawImage(mysteryIcon, (int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5), null);
				if(isHovering((int)((171)*4.5), (int)(29*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.onMysteryScreen = false;
						gp.catalogue.layer = 0;
					}
				}
			} else {
				g2.drawImage(basketUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				g2.drawImage(basketButtons, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				
				//CHECKOUT
				if(isHovering((int)(146*4.5), (int)(96*4.5), (int)(42*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 0.5;
						gp.catalogue.tryPay();
					}
				}
			}
			
			if(!gp.catalogue.checkingOut && !gp.catalogue.onMysteryScreen) {
				gp.catalogue.drawCatalogue(g2);
			} else if(gp.catalogue.onMysteryScreen) {
				gp.catalogue.drawMysteryScreen(g2);
			} else {
				gp.catalogue.drawCheckout(g2);
			}
			
		}
		
	}
	public void drawXPScreen(Graphics2D g2) {
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
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
				if(gp.mouseI.leftClickPressed) {
					gp.currentState = gp.playState;
				}
			}
		}
		
		if(levelUp) {
			if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
				levelUp = false;
				clickCooldown = 0.33;
				gp.player.level++;
				gp.progressM.handleLevelUp(gp.player.level);
			}
		}
		
		drawLevelRoadmap(g2);
		
	}
	private void drawLevelRoadmap(Graphics2D g2) {
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

	            BufferedImage img;

	            boolean isFirstSoulOverall = (soulCounter == 0);
	            boolean isLastSoulInLevel = (s == soulsInThisLevel - 1);

	            if (isFirstSoulOverall) img = (soulCounter < playerSoulIndex) ? leftProgress2 : leftProgress1;
	            else if (isLastSoulInLevel) img = (soulCounter < playerSoulIndex) ? rightProgress2 : rightProgress1;
	            else img = (soulCounter < playerSoulIndex) ? middleProgress2 : middleProgress1;

	            g2.drawImage(img, x, lineY - soulHeight / 2, img.getWidth() * 3, img.getHeight() * 3, null);
	        }
	    }

	    // Draw nodes aligned with last soul of each level
	    accumulatedSouls = 0;
	    
	    for (int level = 1; level <= totalLevels; level++) {
	        int nodeSoulIndex = accumulatedSouls + soulsPerLevel[level - 1] - 1;
	        int x = (int)(nodeSoulIndex * soulWidth + gp.progressM.roadmapOffsetX - nodeSize / 2);
	        int y = lineY - nodeSize / 2;

	        if (!(x + nodeSize < 0 || x > gp.frameWidth)) {
	        	BufferedImage icon;
	        	if (level+1 <= gp.player.level) {
	        	    icon = gp.progressM.levelRewards[level - 1][1]; // passed/completed
	        	} else {
	        	    icon = gp.progressM.levelRewards[level - 1][0]; // normal/unreached
	        	}
	        	g2.drawImage(icon, x, y, nodeSize, nodeSize, null);
	        }
	                
	        String lvlText = "" + level;
	        int textWidth = g2.getFontMetrics().stringWidth(lvlText);
	        if (level+1 == gp.player.level) {
	            g2.setColor(Color.YELLOW);
	        } else {
	        	g2.setColor(Color.WHITE);
	        }
	        g2.setFont(timeFont);
	        g2.drawString(lvlText, x + nodeSize / 2 - textWidth / 2, y + nodeSize + 16);

	        accumulatedSouls += soulsPerLevel[level - 1];
	    }
	}
	private void drawRecipeSelectScreen(Graphics2D g2) {
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);

		if (gp.progressM.getRecipeChoices() == null) {
		    // fallback in case something went wrong
		    recipeChoices = RecipeManager.getTwoRandomLocked(gp);
		} else {
		    recipeChoices = gp.progressM.getRecipeChoices();
		}
		
		String text = "Choose New Recipe!";
		g2.setFont(fancyTitleFont);
		int x = getXforCenteredText(text, g2);
		int y = 100;
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		
	    x = 400;
	    y = gp.frameHeight/2 - 100;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	    		RecipeManager.unlockRecipe(recipeChoices[0]);
	    		gp.currentState = gp.playState;
	    		gp.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(g2, recipeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	       		RecipeManager.unlockRecipe(recipeChoices[1]);
	    		gp.currentState = gp.playState;
	    		gp.progressM.checkRecipeCollect();
	    	}
	    }
	    drawRecipe(g2, recipeChoices[1], x, y);
	}
	private void drawChooseUpgradeScreen(Graphics2D g2) {
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
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
		g2.setFont(fancyTitleFont);
		int x = getXforCenteredText(text, g2);
		int y = 100;
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);

	    x = 200;
	    y = gp.frameHeight/2 - 100;
	    if(isHovering(x, y, 130*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	    		UpgradeManager.unlockUpgrade(upgradeChoices[0]);
	    		if(upgradeChoices[0].getCategory() == RewardType.KITCHEN) {
	    			gp.progressM.checkKitchenUpgrade();
	    		}
	    		clickCooldown = 0.33;
	    		gp.currentState = gp.chooseRecipeState;
	    	}
	    }
	    drawUpgrade(g2, upgradeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 130*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	    		UpgradeManager.unlockUpgrade(upgradeChoices[1]);
	    		clickCooldown = 0.33;
	    		if(upgradeChoices[1].getCategory() == RewardType.KITCHEN) {
	    			gp.progressM.checkKitchenUpgrade();
	    		}
	    		gp.currentState = gp.chooseRecipeState;
	    	}
	    }
	    drawUpgrade(g2, upgradeChoices[1], x, y);
	}
    private void drawUpgrade(Graphics2D g2, Upgrade upgrade, int x, int y) {
		// BASE
		g2.drawImage(saveBorder, x, y, 130 * 3, 48 * 3, null);

		g2.setColor(titleColour1);
		g2.setFont(saveFont);
		g2.drawString(upgrade.getName(), x + 30, y + 40);
		
		g2.setColor(orderTextColour);
		g2.setFont(saveFont2);
		
		int counter = 0;
		for(String line: upgrade.getDescription().split("/n")) {
            g2.drawString(line, x + 30, y + 70 + counter);
            counter += 18;
        }
		
		BufferedImage img = upgrade.getImage();
		g2.drawImage(img, x+ 130*2+60 - upgrade.xOffset, y+40+24 - upgrade.yOffset, img.getWidth()*3, img.getHeight()*3, null);
		
    }
    private void drawRecipe(Graphics2D g2, Recipe recipe, int x, int y) {
		// BASE
		g2.drawImage(recipeBorder, x, y, 32 * 3, 48 * 3, null);

		// INGREDIENT IMAGES
		List<String> ingredients = recipe.getIngredients();
		List<String> cookingState = recipe.getCookingStates();
		List<String> secondaryCookingState = recipe.getSecondaryCookingStates();
		for (int j = 0; j < ingredients.size(); j++) {
			String ingredientName = ingredients.get(j);
			BufferedImage ingredientImage = gp.itemRegistry.getImageFromName(ingredientName);
			Food ingredient = (Food)gp.itemRegistry.getItemFromName(ingredientName, 0);
			if(ingredient.notRawItem) {
				ingredientImage = gp.itemRegistry.getRawIngredientImage(ingredientName);
			}
			if (ingredientImage != null) {
				// Draw each ingredient image 32px apart above the order box
				g2.drawImage(ingredientImage, x + j * (10*3) + 4, y + 4, 10*3, 10*3, null);
				g2.drawImage(gp.recipeM.getIconFromName(cookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3, null);
				g2.drawImage(gp.recipeM.getIconFromName(secondaryCookingState.get(j), recipe.isCursed), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3, null);
			}
		}
		
		// NAME
		g2.setColor(orderTextColour);
		g2.setFont(nameFont);
		int counter = 0;
		for(String line: recipe.getName().split(" ")) {
            g2.drawString(line, x + (48 - getTextWidth(line, g2) / 2.0f), y + 84 + counter);
            counter += 15;
        }
		// PLATE IMAGE
		g2.drawImage(recipe.finishedPlate, x + 24, y + 94, 48, 48, null);
		
		g2.drawImage(coinImage, x, y + 94 + 48, 48, 48, null);
		
		g2.setColor(Color.WHITE);
		g2.setFont(timeFont);
		g2.drawString(Integer.toString(recipe.getCost(gp.world.isRecipeSpecial(recipe))), x + 48+8, y + 94 + 48+32);

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
	    addMessage(text, 320, Color.YELLOW);
	}
	public void addMessage(String text, Color color) {
	    addMessage(text, 320, color);
	}

	public void addMessage(String text, int lifetime, Color color) {
	    messages.add(new GUIMessage(text, lifetime, color));
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
			if (gp.keyI.right) {
			    currentPage++;
			    gp.keyI.right = false;   // prevent holding key
			}

			if (gp.keyI.left) {
			    currentPage--;
			    gp.keyI.left = false;
			}

			// Clamp page
			if (currentPage < 0) currentPage = 0;
			if (currentPage > 1) currentPage = 1;
		} else if(gp.currentState == gp.achievementState) {
		    // --- Handle scrolling ---
		    if (gp.keyI.down && clickCooldown == 0) {
		        if (achievementStartIndex + 2 < gp.progressM.achievements.size()) {
		            achievementStartIndex++;
		            clickCooldown = 0.1;
		        }
		    }
		    if (gp.keyI.up && clickCooldown == 0) {
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
	public List<String> wrapText(String text, Graphics2D g2, int maxWidth) {
	    List<String> lines = new ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder currentLine = new StringBuilder();

	    for (String word : words) {
	        String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
	        int lineWidth = (int) g2.getFontMetrics().getStringBounds(testLine, g2).getWidth();
	        if (lineWidth > maxWidth) {
	            if (currentLine.length() > 0) {
	                lines.add(currentLine.toString());
	            }
	            currentLine = new StringBuilder(word);
	        } else {
	            currentLine = new StringBuilder(testLine);
	        }
	    }

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
	public void drawDialogueState(Graphics2D g2) {
	    int width = 384 * 3;
	    int height = 126 * 3;
	    int x = (gp.screenWidth - width) / 2;
	    int y = gp.screenHeight - height + 200;

	    // Draw dialogue frame
	    g2.drawImage(dialogueFrame, x, y, width, height, null);

	    // Font setup
	    g2.setFont(saveFont);
	    g2.setColor(titleColour2);

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
	        List<String> lines = wrapText(displayedText, g2, width - 60);
	        for (String line : lines) {
	            g2.drawString(line, textX, textY);
	            textY += g2.getFontMetrics().getHeight() + 2;
	        }
	    }

	    // Draw NPC name
	    if (currentTalkingNPC != null && currentTalkingNPC.getName() != null && !currentTalkingNPC.getName().isEmpty()) {
	        g2.setFont(headingFont);
	        g2.setColor(Color.WHITE);
	        g2.drawString(currentTalkingNPC.getName(), x + 20, y - 10);
	        
	        g2.drawImage(currentTalkingNPC.portrait, x + 20 - 24, y + 110, 32*3, 32*3, null);
	    }

	    // Handle click: skip or finish
	    if (gp.mouseI.leftClickPressed) {
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
	public void drawCheckBoxHover(Graphics2D g2, int x, int y, int w, int h) {
	    int size = 16 * 3;

	    // Top-left
	    g2.drawImage(highlight3, x, y, size, size, null);

	    // Top-right
	    g2.drawImage(highlight4, x + w - size, y, size, size, null);

	    // Bottom-left
	    g2.drawImage(highlight2, x, y + h - size, size, size, null);

	    // Bottom-right
	    g2.drawImage(highlight1, x + w - size, y + h - size, size, size, null);
	}
}

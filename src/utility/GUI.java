package utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import entity.PlayerMP;
import entity.items.Food;
import entity.npc.Customer;
import entity.npc.SpecialCustomer;
import main.GamePanel;
import net.DiscoveryManager;
import net.DiscoveryManager.DiscoveredServer;
import net.packets.Packet00Login;
import net.packets.Packet01Disconnect;

public class GUI {
	
	GamePanel gp;
	
	//IMAGES
    private BufferedImage recipeBorder, timeBorder, timeHeader, timeFrame, coinImage, mysteryOrder;
    private BufferedImage[][] timeAnimations;
    private BufferedImage[][] titleBookAnimations;
    private BufferedImage titleBackground;
    private BufferedImage highlight1, highlight2, highlight3, highlight4, underline, uncheckedBox, checkedBox;
    private BufferedImage settingsFrame, generalSettingsFrame, videoSettingsFrame, audioSettingsFrame, multiplayerSettingsFrame;
    private BufferedImage[] computerAnimations;
    private BufferedImage shoppingUI, shoppingButtonUI, leftArrow, rightArrow, basketUI, basketButtons;
    private BufferedImage leftProgress1, leftProgress2, middleProgress1, middleProgress2, rightProgress1, rightProgress2;
    
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
	private Font fancyTitleFont = new Font("monogram", Font.ITALIC, 100);
    private Font nameFont = new Font("monogram", Font.ITALIC, 20);
    private Font timeFont = new Font("monogram", Font.PLAIN, 40);
    
	
	//COUNTERS
	private int timeAnimationCounter, timeAnimationSpeed, currentTimeAnimation;
	private int clickCooldown = 0;
	private int titleAnimationCounter, titleAnimationSpeed, currentTitleAnimation, titleAnimationSpeedFactor;
	private int titlePageNum = -1;
	private int computerAnimationCounter, computerAnimationSpeed;
	
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
	
	//SETTINGS STATES
	private int settingsState = 0;
	private final int baseSettings = 0;
	private final int generalState = 1;
	private final int videoState = 2;
	private final int audioState = 3;
	private final int multiplayerState = 4;


	public GUI(GamePanel gp) {
		this.gp = gp;
		
		darkened = new Color(30, 30, 30, 200);
		craftColour1 = new Color(232, 193, 112);
		titleColour1 = new Color(51, 60, 58);
	    titleColour2 = new Color(87, 87, 87);
        orderTextColour = new Color(145, 102, 91);
        
        titleAnimationSpeedFactor = 4;
		
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
						clickCooldown = 60;
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
						clickCooldown = 20;
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
					clickCooldown = 10;
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
						clickCooldown = 10;
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
						clickCooldown = 20;
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
	                        clickCooldown = 15;
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
	                clickCooldown = 20;
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
		
		
		boolean drawLoadingScreen = false;
		//SinglePlayer
			g2.setFont(titleFont);
			g2.setColor(titleColour1);
			text = "Singleplayer";
				
			int mouseX = gp.mouseI.mouseX;
			int mouseY = gp.mouseI.mouseY;
				
			x = 120;
			y = 360;
				
			if(isHovering(text,x, y-40, g2)) {
				g2.setColor(titleColour2);
				if(gp.mouseI.leftClickPressed) {
					if(clickCooldown == 0) {
						//ENTER GAME
						drawLoadingScreen = true;
						singleplayerSelected = true;
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
						clickCooldown = 20;
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
							clickCooldown = 20;
							currentTitleAnimation = 2;
							titleAnimationCounter = 0;
							titleAnimationSpeed = 0;
						}
					}
					g2.fillRect(x, y+ 14, getTextWidth(text, g2), 6);

				}
				
				g2.drawString(text, x, y);
				
				if(drawLoadingScreen) {
					
					//g2.drawImage(titleImage3, 0, 0, gp.frameWidth, gp.frameHeight, null);
					
					g2.setFont(titleFont);
					g2.setColor(Color.WHITE);
					text = "LOADING";
						
					x = 20;
					y = 800;
					
					g2.drawString(text, x, y);
					if(startLoading) {
						if(singleplayerSelected) {
							gp.playSinglePlayer();
							singleplayerSelected = false;
						}
						startLoading = false;
					}
					startLoading = true;
				}
		}
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
	    if(caretBlinkCounter > 30) caretBlinkCounter = 0;
	    if(caretBlinkCounter < 15 && usernameActive) {
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
	            clickCooldown = 20;
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
					clickCooldown = 20;
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
		}
		
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

		        g2.setColor(orderTextColour);
		        g2.setFont(nameFont);
		        int counter = 0;
		        for (int l = 0; l < data.nameLines.size(); l++) {
		            String line = data.nameLines.get(l);
		            int offset = data.nameLineOffsets.get(l);
		            g2.drawString(line, x + offset, y + 84 + counter);
		            counter += 15;
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
		    drawPatienceBar(g2, x, y + 208, data.customer.getPatienceCounter(), data.customer.getMaxPatienceTime());

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
	    data.borderImage = recipeBorder;
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
	        data.cookingStateIcons.add(gp.recipeM.getIconFromName(cookingState.get(j)));
	        data.secondaryCookingStateIcons.add(gp.recipeM.getIconFromName(secondaryCookingState.get(j)));
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
					clickCooldown = 20;
				}
			}
		}else {
			g2.setColor(Color.WHITE);
		}
		g2.drawString(text, getXforCenteredText(text, g2), 500);
		
		text = "QUIT";
		x =getXforCenteredText(text, g2);
		if(isHovering(text, x, 550, g2)) {
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
		g2.drawString(text, getXforCenteredText(text, g2), 600);
		
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
	                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
		g2.setFont(timeFont);
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
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
                clickCooldown = 20;
            }
    		g2.drawImage(underline, 512-60, y-16, 80*3, 16*3, null);
        }
		g2.drawString(text, x, y);
	}
	public void drawCatalogueState(Graphics2D g2) {
		
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);
		
		computerAnimationSpeed++;
		if(computerAnimationSpeed >= 5) {
			computerAnimationSpeed = 0;
			computerAnimationCounter++;
		}
		
		if(computerAnimations[computerAnimationCounter] == null) {
			computerAnimationCounter--;
		}
		g2.drawImage(computerAnimations[computerAnimationCounter], 0, 0, (int)(260*4.5), (int)(190*4.5), null);
		
		if(clickCooldown > 0) {
			clickCooldown--;
		}
		
		if(computerAnimationCounter >= 9) {
			if(!gp.catalogue.checkingOut) {
				g2.drawImage(shoppingUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				g2.drawImage(shoppingButtonUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				
				g2.drawImage(leftArrow, (int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5), null);
				if(isHovering((int)(81*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 30;
						gp.catalogue.leftPage();
					}
				}
				g2.drawImage(rightArrow, (int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5), null);
				if(isHovering((int)((81+11)*4.5), (int)(98*4.5), (int)(11*4.5), (int)(11*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 30;
						gp.catalogue.rightPage();
					}
				}
				
				if(isHovering((int)(170*4.5), (int)(96*4.5), (int)(16*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 30;
						gp.catalogue.checkingOut = true;
						gp.catalogue.layer = 0;
					}
				}
			} else {
				g2.drawImage(basketUI, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				g2.drawImage(basketButtons, 0, 0, (int)(260*4.5), (int)(190*4.5), null);
				
				//CHECKOUT
				if(isHovering((int)(146*4.5), (int)(96*4.5), (int)(42*4.5), (int)(16*4.5))) {
					if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
						clickCooldown = 30;
						gp.catalogue.tryPay();
					}
				}
			}
			
			if(!gp.catalogue.checkingOut) {
				gp.catalogue.drawCatalogue(g2);
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
		if (displayedSouls == 0) {
		    g2.drawImage(leftProgress1, 200, gp.frameHeight/2 - 30, 23*3, 20*3, null);
		} else {
		    g2.drawImage(leftProgress2, 200, gp.frameHeight/2 - 30, 23*3, 20*3, null);
		}

		for (int i = 0; i < gp.player.nextLevelAmount - 2; i++) {
		    if (i + 2 > displayedSouls) {
		        g2.drawImage(middleProgress1, 200 + 23*3 + 6*3*i, gp.frameHeight/2 - 30, 6*3, 20*3, null);
		    } else {
		        g2.drawImage(middleProgress2, 200 + 23*3 + 6*3*i, gp.frameHeight/2 - 30, 6*3, 20*3, null);
		    }
		}

		if (displayedSouls >= gp.player.nextLevelAmount) {
			levelUp = true;
		    g2.drawImage(rightProgress2, 200 + 23*3 + 6*3*(gp.player.nextLevelAmount - 2), gp.frameHeight/2 - 30, 12*3, 20*3, null);
		} else {
		    g2.drawImage(rightProgress1, 200 + 23*3 + 6*3*(gp.player.nextLevelAmount - 2), gp.frameHeight/2 - 30, 12*3, 20*3, null);
		}
		
		if(displayedSouls == gp.player.soulsServed) {
			if(!levelUp) {
				if(gp.mouseI.leftClickPressed) {
					gp.currentState = gp.playState;
				}
			}
		}
		
		if(levelUp) {
			levelUp = false;
			gp.currentState = gp.chooseRecipeState;
		}
		
	}
	private void drawRecipeSelectScreen(Graphics2D g2) {
		g2.setColor(darkened);
		g2.fillRect(0, 0, gp.frameWidth, gp.frameHeight);

	    if (recipeChoices == null) {
	    	recipeChoices = RecipeManager.getTwoRandomLocked();
	    }

	    if(true) {
	    	
	    }
	    int x = 400;
	    int y = gp.frameHeight/2 - 100;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed) {
	    		RecipeManager.unlockRecipe(recipeChoices[0]);
	    		gp.currentState = gp.playState;
	    	}
	    }
	    drawRecipe(g2, recipeChoices[0], x, y);
	    
	    x = 600;
	    if(isHovering(x, y, 32*3, 48*3)) {
	    	if(gp.mouseI.leftClickPressed) {
	       		RecipeManager.unlockRecipe(recipeChoices[1]);
	    		gp.currentState = gp.playState;
	    	}
	    }
	    drawRecipe(g2, recipeChoices[1], x, y);
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
				g2.drawImage(gp.recipeM.getIconFromName(cookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16), 10*3, 10*3, null);
				g2.drawImage(gp.recipeM.getIconFromName(secondaryCookingState.get(j)), x + j * (10*3) + 4, y + 4 + (16) + 24, 10*3, 10*3, null);
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
	public void update() {

		if(clickCooldown > 0) {
			clickCooldown--;
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

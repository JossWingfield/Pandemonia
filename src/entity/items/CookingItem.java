package entity.items;

import java.util.List;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class CookingItem extends Item {
	
    protected boolean cooking = false;
    protected List<String> rawIngredients;
    protected List<String> cookedResults;
    public Food cookingItem = null;
    protected float cookTime = 0;
    protected int baseMaxCookTime = 16;
    protected float maxCookTime;
    protected int flickerThreshold = 30;
    protected int maxBurnTime = 38; //60*28
    
    private int BASE_COOK_TIME = 24;
    private int BASE_FLICKER   = 30;
    private int BASE_BURN      = 38;
    protected float flickerFraction = (float) BASE_FLICKER / BASE_COOK_TIME;
    private float burnFraction    = (float) BASE_BURN / BASE_COOK_TIME;
    
    //BURN WARNING
	private TextureRegion orderSign, warningOrderSign, completeSign, burntSign, stirSign, sauteSign, flipSign;
	private int flickerCounter = 0;
	private int flickerSpeed = 30; // frames per toggle
	
	//COOKSTYLES
    protected CookStyle cookStyle = CookStyle.PASSIVE;
    public boolean flipped = false;
    
    //PASSIVE
    private double perfectWindow = 0.4;
    
    //FLIP
    protected float flipWindowStart; // time (ticks)
    protected float flipWindowEnd;
    protected float flipWindowCenter = maxCookTime / 2;
    protected double flipWindowTolerance = maxCookTime * 0.15; // tighter window
    private double flipAccuracy = 0;
    private boolean flipEvaluated = false;
    
    //STIR
    protected float stirLevel;       // 0 → 1
    protected float stirDecayRate;   // per second / tick
    protected float stirAddAmount;   // per stir
    private double stirScoreTotal = 0;
    private double stirSamples = 0;
    
    // Saute-specific
    private double heatValue = 0.5;
    private double heatMax = 1.0;
    private double heatDecayRate = 0.9; 
    private double heatMin = 0.0;  
 	private double safeZoneMin = 0.32;
 	private double safeZoneMax = 0.68;
	private double burnProgress = 0;
	private double stabilitySum = 0;
	private double stabilitySamples = 0;
 	
 	//GRADING
 	private double finalScore = 0.0;
 	private String finalGrade = "";
	
    public CookingItem(GamePanel gp) {
		super(gp);
		setupRecipes();
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		completeSign = importImage("/UI/Tick.png").getSubimage(0, 0, 16, 16);
		burntSign = importImage("/UI/Tick.png").getSubimage(16, 0, 16, 16);
		flipSign = importImage("/UI/CookingIcons.png").getSubimage(0, 0, 16, 16);
		stirSign = importImage("/UI/CookingIcons.png").getSubimage(16, 0, 16, 16);
		sauteSign = importImage("/UI/CookingIcons.png").getSubimage(32, 0, 16, 16);
	}
	protected void setupRecipes() {
		
	}
	public boolean canCook(String itemName) {
		return rawIngredients.contains(itemName);
	}
	public void bin() {
		cookingItem = null;
		cooking = false;
		resetImages();
	}

	public String getCookedResult(String rawItemName) {
		int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return cookedResults.get(index);
	    }
	    return null;
	} 
	public CookStyle getCookStyle() {
		return cookStyle;
	}
	public List<String> getRawIngredients() {
		return rawIngredients;
	}
	public List<String> getCookedResults() {
		return cookedResults;
	}
	public void setCooking(Item i) {
	    cooking = true;
	    cookTime = 0;
	    cookingItem = (Food)i;
		maxCookTime = baseMaxCookTime* cookingItem.getMaxCookTime();
		flickerThreshold = Math.round(maxCookTime * flickerFraction);
		maxBurnTime = Math.round(maxCookTime * burnFraction);
		cookStyle = CookStyleResolver.resolve(cookingItem, this, gp);
		cookingItem.addStep(name);
		
		boolean requiresFlip = cookStyle == CookStyle.FLIP;

		if (requiresFlip) {
			flipped = false;
			flipWindowStart = maxCookTime / 3;
		    flipWindowEnd   = (maxCookTime * 2) / 3;
		}
		
		boolean requiresStir = cookStyle == CookStyle.STIR;

		if (requiresStir) {
		    stirLevel = 1.0f;
		    stirDecayRate = 0.154f;   // tune this
		    stirAddAmount = 0.35f;
		    stirScoreTotal = 0;
		    stirSamples = 0;
		}
		
		boolean requiresSaute = cookStyle == CookStyle.SAUTE;

		if (requiresSaute) {
		    heatValue = 0.5;          // neutral starting heat
		    burnProgress = 0.0;
		}
	}
	public boolean isCooking() {
		return cooking;
	}
	public void updateCooking(double dt) {
	    if (cooking) {
	    	
	    	switch(cookStyle) {
	    		case PASSIVE -> updatePassive(dt);
	    		case FLIP -> updateFlip(dt);
	    		case STIR -> updateStir(dt);
	    		case SAUTE -> updateSaute(dt);
	    	}
	
	    }
	}
	protected void animatePan(double dt) {}
	private void updatePassive(double dt) {
		animatePan(dt);
        cookTime+= dt;
        if (cookTime >= maxCookTime) {
            //cooking = false;
        	if(cookingItem != null) {
	            cookingItem.foodState = FoodState.COOKED;
	            if(cookTime >= maxBurnTime) {
		            cookTime = maxBurnTime;
		            setBurntImage();
		            cookingItem.foodState = FoodState.BURNT;
		            cooking = false;
	            }
        	}
        }
	}
	private void updateFlip(double dt) {
		animatePan(dt);
		cookTime+=dt;
		if (!flipped) {
			// Missed flip window → auto-fail
		    if (cookTime > flipWindowEnd) {
		    	cookingItem.foodState = FoodState.BURNT;
		        setBurntImage();
		        cooking = false;
		        return;
		    }
		}

		if (cookTime >= maxCookTime) {

		    cookingItem.foodState = FoodState.COOKED;

		    // If flip was part of cooking, apply scoring
		    if (flipEvaluated) {

		        // convert accuracy → score
		        finalScore = flipAccuracy * 100.0;


		        // apply gameplay impact (important part)
		        double quality = 100* (0.4 + flipAccuracy * 0.6);
		        finalGrade = cookingItem.getGrade(quality);
		        //System.out.println("Flip: " + quality + "  " + finalGrade);
		        cookingItem.addActionScore(name, quality, finalGrade);
		    }
		}
	}
	private void updateStir(double dt) {

	    // --- Animate & advance cooking ---
	    animatePan(dt);
	    cookTime += dt;

	    // --- Stir decay ---
	    stirLevel -= stirDecayRate * dt;
	    
	    double ideal = 0.75;
	    
	    double distance = Math.abs(stirLevel - ideal);

	    // 0 = awful, 1 = perfect
	    double accuracy = Math.max(0, 1.0 - (distance / ideal));

	    stirScoreTotal += accuracy;
	    stirSamples++;

	    // --- Failed to stir = instant burn ---
	    if (stirLevel <= 0f) {
	        stirLevel = 0f;
	        cookingItem.foodState = FoodState.BURNT;
	        setBurntImage();
	        stopCooking();
	        return;
	    }

	    
	    // --- Finished cooking ---
	    if (cookTime >= maxCookTime) {

	        cookingItem.foodState = FoodState.COOKED;

	        double averageAccuracy = stirScoreTotal / stirSamples;

	        finalScore = averageAccuracy * 100.0;

	        double quality = 100*(0.4 + averageAccuracy * 0.6);
	        finalGrade = cookingItem.getGrade(quality);
	        cookingItem.addActionScore(name, quality, finalGrade);
	        //System.out.println("Stir: " + quality + "  " + finalGrade);

	        // --- Overcooking still possible ---
	        if (cookTime >= maxBurnTime) {
	            cookTime = maxBurnTime;
	            cookingItem.foodState = FoodState.BURNT;
	            setBurntImage();
	            cooking = false;
	        }
	    }
	}
	private void updateSaute(double dt) {

	    animatePan(dt);

	    // Passive cooling (gentle)
	    heatValue -= heatDecayRate * dt * (1.0 - heatValue);
	    heatValue = Math.max(0.0, heatValue);

	    boolean inSafeZone = heatValue >= safeZoneMin && heatValue <= safeZoneMax;

	    // --- Cooking ---
	    if (inSafeZone) {
	        cookTime += dt;
	        burnProgress -= dt * 0.6; // recover from mistakes
	        stabilitySum += 1.0;
	    } else {
	    	double dist = (heatValue < safeZoneMin)
	    	        ? (safeZoneMin - heatValue)
	    	        : (heatValue - safeZoneMax);

	    	stabilitySum += Math.max(0, 1.0 - dist * 2.0);
	    }
	    stabilitySamples++;

	    // --- Too hot ---
	    if (heatValue > safeZoneMax) {
	        cookTime += dt * 0.5;
	        burnProgress += (heatValue - safeZoneMax) * dt * 5.0;
	    }

	    // --- Too cold ---
	    if (heatValue < safeZoneMin) {
	        burnProgress += dt * 0.3; // oil not hot enough
	    }

	    burnProgress = Math.max(0, burnProgress);

	    // --- Finish cooking ---
	    if (cookTime >= maxCookTime) {

	        cookingItem.foodState = FoodState.COOKED;

	        // --- PERFORMANCE CALCULATION ---

	        double timeAccuracy = Math.min(1.0, cookTime / maxCookTime);
	        double stability = stabilitySum / Math.max(1, stabilitySamples);
	        // reward staying in safe zone indirectly via low burnProgress
	        double burnPenalty = Math.exp(-burnProgress * 2.0);
	        
	        finalScore = (timeAccuracy * 0.3 +stability * 0.5 + burnPenalty * 0.2) * 100.0;


	        double quality = (100*(0.4 + finalScore / 250.0));
	        finalGrade = cookingItem.getGrade(quality);
	        cookingItem.addActionScore(name, quality, finalGrade);
	        //System.out.println("Saute: " + quality + "  " + finalGrade);

	        cooking = false;
	    }
	    // --- Burn ---
	    if (burnProgress >= 1.0) {
	        cookingItem.foodState = FoodState.BURNT;
	        setBurntImage();
	        cooking = false;
	    }
	}
	public void tryFlip() {

	    if (flipped) return;

	    double center = (flipWindowStart + flipWindowEnd) / 2.0;
	    double offset = Math.abs(cookTime - center);
	    double maxOffset = (flipWindowEnd - flipWindowStart) / 2.0;

	    flipAccuracy = 1.0 - (offset / maxOffset);
	    flipAccuracy = Math.max(0, Math.min(1, flipAccuracy));

	    flipped = true;
	    flipEvaluated = true;

	    // HARD FAILURE threshold (makes it more skill based)
	    if (flipAccuracy < 0.15) {
	        cookingItem.foodState = FoodState.BURNT;
	        setBurntImage();
	        stopCooking();
	        return;
	    }
	}
	public void stir() {
	    stirLevel = Math.min(1.0f, stirLevel + stirAddAmount);
	}
	public void addHeat() {
	    heatValue += 0.12;
	    heatValue = Math.max(heatMin, Math.min(heatMax, heatValue));
	}
	public void evaluatePassiveScore() {

	    double offset = Math.abs(cookTime - maxCookTime);
	    double maxOffset = maxCookTime * 0.5;
	    double accuracy = 1.0 - (offset / maxOffset);

	    if(offset <= perfectWindow) {
	        accuracy = 1.0;
	    } else {
	        double adjustedOffset = offset - perfectWindow;
	        maxOffset = (maxBurnTime - maxCookTime);

	        accuracy = 1.0 - (adjustedOffset / maxOffset);
	        accuracy = Math.max(0, accuracy);
	    }


	    accuracy = Math.max(0, Math.min(1, accuracy));

	    finalScore = accuracy * 100.0;

	    double quality = 100*(0.4 + accuracy * 0.6);
	    finalGrade = getPassiveCookGrade(quality);
        cookingItem.addActionScore(name, quality, finalGrade);
        //System.out.println("Passive: " + quality + "  " + finalGrade);
	}
	public String getPassiveCookGrade(double score) {
		
        if(score >= 99) return "S";
        if(score >= 75) return "A";
        if(score >= 60) return "B";
        if(score >= 40) return "C";
        if(score >= 25) return "D";

        return "F";
    }
	protected void setBurntImage() {
		
	}
	public void resetImages() {
		
	}
	public void setCookingImage() {
		
	}
	public void stopCooking() {
		cooking = false;
	}
	public void checkPassiveGrade() {
		if(cookStyle == CookStyle.PASSIVE) {
			if(cookingItem.foodState == FoodState.PLATED) {
		        evaluatePassiveScore();
		    }
		}
	}
	public void setCookTime(int time) {
	    cookTime = time;
	}
	public float getCookTime() {
	    return cookTime;
	}
	public float getMaxCookTime() {
	    return maxCookTime;
	}
	public void drawCookingWarning(Renderer renderer, int x, int y) {
		
		 flickerCounter++;
		 if (flickerCounter >= flickerSpeed) {
			 flickerCounter = 0;
		 }
		    
		 TextureRegion currentSign = orderSign;

		 if(cookTime >= flickerThreshold) {
	 	     if (flickerCounter < flickerSpeed / 2) {
	 	    	 currentSign = orderSign;
	 	     } else {
	 	         currentSign = warningOrderSign;
	 	     }
	
	 	    renderer.draw(currentSign,(int)(x ),(int)(y  - 48),48, 48);
		} else {
			if(cookingItem.foodState.equals(FoodState.COOKED)) {
				renderer.draw(completeSign,(int)(x ),(int)(y  - 48),48, 48);
			} else if(cookingItem.foodState.equals(FoodState.BURNT)){
				renderer.draw(burntSign,(int)(x ),(int)(y  - 48),48, 48);
			}
		}
	}
	public void drawBurntSign(Renderer renderer, int x, int y) {
		renderer.draw(burntSign,(int)(x ),(int)(y  - 48),48, 48);
	}
	public void drawCookingUI(Renderer renderer, int x, int y, boolean isLeft) {
		
		switch(cookStyle) {
		case PASSIVE:
			// Left slot cooking bar
			if(isLeft) {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						System.out.println(getMaxCookTime());
						drawPassiveCookingBar(renderer, x + 16, y + 48 + 16, getCookTime(), getMaxCookTime());
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x), (int)y);
					}
				}
			} else {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawPassiveCookingBar(renderer, x + 48 + 30, y + 48 + 16, getCookTime(), getMaxCookTime());
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x + 56), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x + 56), (int)y);
					}
				}
			}
			break;
		case FLIP:
			
			if(isLeft) {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawFlipCookingBar(renderer, x + 16, y + 48 + 16, getCookTime(), getMaxCookTime());
						renderer.draw(flipSign,(int)(x),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x), (int)y);
					}
				}
			} else {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawFlipCookingBar(renderer, x + 48 + 30, y + 48 + 16, getCookTime(), getMaxCookTime());
						renderer.draw(flipSign,(int)(x +56),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x + 56), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x + 56), (int)y);
					}
				}
			}
			break;
		case STIR:
			
			if(isLeft) {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawStirBar(renderer, x + 16, y + 48 + 16);
						renderer.draw(stirSign,(int)(x),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x), (int)y);
					}
				}
			} else {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawStirBar(renderer, x + 48 + 30, y + 48 + 16);
						renderer.draw(stirSign,(int)(x+56),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x + 56), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x + 56), (int)y);
					}
				}
			}
			break;
		case SAUTE:
			
			if(isLeft) {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawSauteBar(renderer, x + 16, y + 48 + 16);
						renderer.draw(sauteSign,(int)(x),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x), (int)y);
					}
				}
			} else {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawSauteBar(renderer, x + 48 + 30, y + 48 + 16);
						renderer.draw(sauteSign,(int)(x+56),(int)(y  - 48-24),48, 48);
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x + 56), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x + 56), (int)y);
					}
				}
			}
			break;
		}
		
	}
	private void drawStirBar(Renderer renderer,float worldX,float worldY) {
	    float screenX = worldX - 24;
	    float screenY = worldY - 84;

	    int width = 48;
	    int height = 4;

	    int r = (int) ((1 - stirLevel) * 255);
	    int g = (int) (stirLevel * 255);

	    //BACKGROUND
	    renderer.fillRect((int) screenX -3, (int) screenY -3, width+6, height+6, Colour.BASE_COLOUR);


	    renderer.fillRect(
	        (int) screenX,
	        (int) screenY,
	        (int) (width * stirLevel),
	        height,
	        new Colour(r, g, 0)
	    );
	}
	private void drawSauteBar(Renderer renderer, float worldX, float worldY) {

	    float screenX = worldX - 24;
	    float screenY = worldY - 82;

	    int barWidth = 48;
	    int barHeight = 6;

	    // Background
	    renderer.fillRect((int) screenX -3, (int) screenY -3, barWidth+6, barHeight+6, Colour.BASE_COLOUR);

	    // Safe zone
	    int safeX = (int)(barWidth * safeZoneMin);
	    int safeW = (int)(barWidth * (safeZoneMax - safeZoneMin));
	    renderer.fillRect(
	        (int)screenX + safeX,
	        (int)screenY,
	        safeW,
	        barHeight,
	        Colour.GREEN
	    );

	    // Heat marker
	    int markerX = (int)(barWidth * heatValue);
	    renderer.fillRect(
	        (int)screenX + markerX - 1,
	        (int)screenY - 2,
	        2,
	        barHeight + 4,
	        Colour.WHITE
	    );
	}
	private void drawPassiveCookingBar(Renderer renderer, float worldX, float worldY, float cookTime, float maxCookTime) {
	    float screenX = worldX - 24 ;
	    float screenY = worldY - 72 ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = (maxCookTime <= 0f) ? 0f : (cookTime / maxCookTime);
	    //System.out.println(cookTime + "   " + maxCookTime);
	    progress = Math.max(0f, Math.min(1f, progress));

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset-3, (int) screenY + yOffset-3, barWidth+6, barHeight+6, Colour.BASE_COLOUR);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));
	}
	private void drawFlipCookingBar(Renderer renderer,float worldX,float worldY,double cookTime,float maxCookTime) {
	    float screenX = worldX - 24;
	    float screenY = worldY - 72;

	    int barWidth = 48;
	    int barHeight = 6;
	    int yOffset = -10;

	    float progress = Math.min(1f, (float) cookTime / maxCookTime);

	    // --- Background ---
	    renderer.fillRect((int) screenX -3, (int) screenY + yOffset-3, barWidth+6, barHeight+6, Colour.BASE_COLOUR);

	    

	    // --- Base cook progress (dimmed) ---
	    renderer.fillRect(
	        (int) screenX,
	        (int) screenY + yOffset,
	        (int) (barWidth * progress),
	        barHeight,
	        new Colour(120, 120, 120)
	    );

	    // --- Flip window overlay ---
	    float flipStartPct = flipWindowStart / (float) maxCookTime;
	    float flipEndPct   = flipWindowEnd   / (float) maxCookTime;

	    int flipX = (int) (barWidth * flipStartPct);
	    int flipW = (int) (barWidth * (flipEndPct - flipStartPct));

	    Colour flipColour = flipped
	            ? new Colour(0, 200, 0)      // green if flipped
	            : new Colour(255, 200, 0);   // yellow if pending

	    renderer.fillRect(
	        (int) screenX + flipX,
	        (int) screenY + yOffset,
	        flipW,
	        barHeight,
	        flipColour
	    );

	    // --- Current time marker ---
	    int markerX = (int) (barWidth * progress);

	    Colour markerColour;
	    if (flipped) {
	        markerColour = new Colour(0, 255, 0);
	    } else if (cookTime >= flipWindowStart && cookTime <= flipWindowEnd) {
	        markerColour = new Colour(255, 255, 255); // flip NOW
	    } else {
	        markerColour = new Colour(255, 0, 0);     // danger
	    }

	    renderer.fillRect(
	        (int) screenX + markerX - 1,
	        (int) screenY + yOffset - 2,
	        2,
	        barHeight + 4,
	        markerColour
	    );
	}
	
}

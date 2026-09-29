package application.logic.Candies;

import java.util.Random;

/* BASIC Candy
 * Requires only 1 hits to destroy
 * Awards 1 points when destroyed
 */
public class Basic_Candy extends CandySuper {
    private int hitCount = 1;  					// Number of hits required to destroy
    // Visual properties - array of possible candy images
    private static final String[] BASIC_CANDY_IMAGES = {
            "/application/image/BLUE basic.png",   "/application/image/GREEN basic.png", "/application/image/ORANGE basic.png", 
            "/application/image/PURPLE basic.png", "/application/image/RED basic.png",   "/application/image/YELLOW basic.png",
    };
    //Creates a new basic candy with random visual variation
    public Basic_Candy(double x, double y, double width, double height) {
    	super(x, y, width, height, BASIC_CANDY_IMAGES[new Random().nextInt(BASIC_CANDY_IMAGES.length)]);
    }
    
    @Override 
    public boolean isAvailable(){				//Checks if the candy is still available (not destroyed)
    	return !isDestroyed; 					//return true if candy exists, false if destroyed
    }
    
    @Override 
    public void hit() {
        hitCount--;								//Registers a hit on the candy
        if(hitCount == 0) {						//destroys it if hit count reaches zero
			isDestroyed = true;
		}
    }

    @Override public int getScore() { 
    	if(isDestroyed == true) {
			return 1;							//return 1 point if destroyed, 0 otherwise
		}		
		else {
			return 0; 
    	}
    }

    @Override public String blockPower() { 
    	return null; 							//Basic candies have no special powers
    }
}
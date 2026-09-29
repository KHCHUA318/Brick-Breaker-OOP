package application.logic.Candies;

/* Blue Packed CAndy - Gives BallProtection power tools
 * Requires 2 hits to destroy
 * Awards 5 points when destroyed
 */
public class BluePacked_Candy extends CandySuper {
    private int hitCount = 2;					// Number of hits required to destroy
    // create candy with Visual Representation
    public BluePacked_Candy(double x, double y, double width, double height) {
        super(x, y, width, height, "/application/image/BLUE pack.png");
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

    @Override 
    public int getScore() { 
    	if(isDestroyed == true) {
			return 5;							//return 5 point if destroyed, 0 otherwise
		}		
		else {
			return 0; 
    	}
    }

    @Override 
    public String blockPower() { 
    	return "BallProtection"; 				//return power tool type
    }
}
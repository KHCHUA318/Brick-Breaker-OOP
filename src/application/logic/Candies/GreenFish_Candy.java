package application.logic.Candies;

/* Green Fish Candy - Gives BallDouble power tools
 * Requires 3 hits to destroy
 * Awards 10 points when destroyed
 */
public class GreenFish_Candy extends CandySuper {
    private int hitCount = 3;					// Number of hits required to destroy
    // create candy with Visual Representation
    public GreenFish_Candy(double x, double y, double width, double height) {
        super(x, y, width, height, "/application/image/GREEN Fish.png");
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
			return 10;							//return 10 point if destroyed, 0 otherwise
		}		
		else {
			return 0; 
    	}
    }

    @Override public String blockPower() { 		//return power tool type
    	return "BallDouble"; 
    }
}
package application.logic.Candies;

/* Purple Packed Fish Candy - Gives Score Double power tools
 * Requires 1 hits to destroy
 * Awards 20 points when destroyed
 */
public class PurplePackedFish_Candy extends CandySuper {
    private int hitCount = 1;					// Number of hits required to destroy
    // create candy with Visual Representation
    public PurplePackedFish_Candy(double x, double y, double width, double height) {
        super(x, y, width, height, "/application/image/Purple packedFISH.png");
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
			return 20;							//return 20 point if destroyed, 0 otherwise
		}		
		else {
			return 0; 
    	}
    }

    @Override public String blockPower() { 
        return "ScoreDouble"; 					//return power tool type
    }
}
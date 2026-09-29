package application.logic.Candies;

/* Orange Boom Candy - Penalty candy that slows the paddle
 * Breaks in 1 hit
 * No score awarded
 */
public class OrangeBoom_Candy extends CandySuper {
    private int hitCount = 1;					// Number of hits required to destroy
    // create candy with Visual Representation
    public OrangeBoom_Candy(double x, double y, double width, double height) {
        super(x, y, width, height, "/application/image/Orange boom.png");
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
    public int getScore() { 					//not score returned
    	return 0; 
    }

    @Override public String blockPower() { 
        return "PaddleSlow"; 					//return penalty tool type
    }
}

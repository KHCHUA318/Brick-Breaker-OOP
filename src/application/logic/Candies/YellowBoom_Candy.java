package application.logic.Candies;

/* Yellow Boom Candy- Penalty candy that speed up the balls
 * Breaks in 2 hit
 * No score awarded
 */
public class YellowBoom_Candy extends CandySuper {
    private int hitCount = 2;					// Number of hits required to destroy
    // create candy with Visual Representation
    public YellowBoom_Candy(double x, double y, double width, double height) {
        super(x, y, width, height, "/application/image/YELLOW boom.png");
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

    @Override 
    public String blockPower() { 
    	return "BallSpeedIncrease"; 			//return penalty tool type
    }
}
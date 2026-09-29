package application.logic.power_tools;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

/* Power-up that temporarily doubles score points for a limited duration.
 * Activates a 2x score multiplier for 5 seconds before automatically deactivating.
 */
public class ScoreMultiplier implements PowerTool_Interface {  
    private int multiplier; 							//current score multiplier 
    private boolean isActive = false;					//State tracking
    private PauseTransition timer;						//set up timer
    
    //Create a ScoreMultiplier with default 1x multiplier
    public ScoreMultiplier() {
        this.multiplier = 1;
    }
    @Override  
    public void activate() {							//activates the score boost
        if (!isActive) {
            this.multiplier = 2;						//set 2x score multiplier
            isActive = true;
            //set 5 second timer
            timer = new PauseTransition(Duration.seconds(5));
            timer.setOnFinished(e -> deactivate());		//automatic deactivate
            timer.play();
        }
    }

    @Override  
    public void deactivate() {							//deactivate the score boost:
        this.multiplier = 1;							//reset back
        isActive = false;
        if (timer != null) {
            timer.stop();								//stop and clean up the timer
            timer = null;
        }
    }

    @Override  
    public boolean isActive() {							//check if score boost is currently active
        return isActive;
    }

    //get the current score multiplier value
    public int getMultiplier() {
        return this.multiplier;
    }
}
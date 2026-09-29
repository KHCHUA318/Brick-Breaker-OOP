package application.logic.power_tools;

import application.logic.paddle.Paddle;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/* Penalty power-up that temporarily reduces paddle movement speed by 66% for 10 seconds.
 * Decreasing player control responsiveness.
 */
public class PaddleSlow implements PowerTool_Interface {  
    private Paddle paddle;									// Reference to game paddle
    private double originalMoveStep;						// Stores original movement speed
    private boolean isActive = false;						// Effect state tracking
    private PauseTransition timer;							// Set up timer for auto deactivate

    public PaddleSlow(Paddle paddle) {
        this.paddle = paddle;
        this.originalMoveStep = paddle.getMoveStep();		//stores original movement speed
    }

    @Override
    public void activate() {								//activate the slow effect
        if (!isActive) {
            paddle.setMoveStep(originalMoveStep / 3);		//reduce the move speed to 33% of original speed
            isActive = true;
            //set 10second timer
            timer = new PauseTransition(Duration.seconds(10)); 
            timer.setOnFinished(e -> deactivate());			// deactivate after 10 second
            timer.play();
        }
    }

    @Override
    public void deactivate() {								//deactivates the slow effect
        if (isActive) {
            paddle.setMoveStep(originalMoveStep);			//Restores original paddle speed
            isActive = false;
        }
        if (timer != null) {
            timer.stop();									//stop and clean up the timer
            timer = null;
        }
    }

    @Override
    public boolean isActive() {
        return isActive;									//check if slow effect is currently active
    }
}
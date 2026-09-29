package application.logic.power_tools;

import application.logic.Ball.Ball;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/* Power-up that temporarily increases ball speed for 5 second.
 * Doubles the ball's speed for 5 seconds before automatically reverting.
 */
public class BallSpeedIncrease implements PowerTool_Interface {  
	private Ball ball;										// The ball affected by this power-up
    private double originalSpeed;							// The original speed to restore later
    private boolean isActive = false;						// Activation state flag
    private PauseTransition timer;							// Timer for automatic deactivate

    public BallSpeedIncrease(Ball ball) {					// Creates a speed increase power-up for the specified ball
        this.ball = ball;
        this.originalSpeed = ball.getSpeed();
    }

    @Override  
    public void activate() {								//activate the speed boost effect
        if (!isActive) {
        	ball.setSpeed(originalSpeed * 2);				// Double up the speed and set to the ball class
            isActive = true;
            //set timer 5seconds
            timer = new PauseTransition(Duration.seconds(5));
            timer.setOnFinished(e -> deactivate());
            timer.play();
        }
    }

    @Override  
    public void deactivate() {								//deactivate the speed boost effect
        if (isActive) {
        	ball.setSpeed(originalSpeed);					// Restore back to original speed when time is up 
            isActive = false;
        }
        if (timer != null) {
            timer.stop();									// Stop and clean up  the timer
            timer = null;
        }
    }

    @Override
    public boolean isActive() {								//checks if the speed boost is currently active
        return isActive;
    }
}
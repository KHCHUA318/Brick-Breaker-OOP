package application.logic.power_tools;

import application.logic.paddle.Paddle;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/** Penalty power-up that temporarily reduces paddle width by half for 5 seconds.
 * Decreasing the player's hitting area.
 */
public class PaddleShrink implements PowerTool_Interface { 
    private Paddle paddle;										// Reference to game paddle
    private PauseTransition timer;								// set up timer for automatic deactivate
    private boolean isActive = false;							// Effect state tracking	

    public PaddleShrink(Paddle paddle) {						//create a paddle shrink penalty instance
        this.paddle = paddle;
    }

    @Override
    public void activate() {									//activate the shrink effect
        if (!isActive) {
            paddle.setPaddleWidth(paddle.getPaddleWidth() / 2);	//halve the paddle width
            isActive = true;
            //set time 5seconds
            timer = new PauseTransition(Duration.seconds(5));
            timer.setOnFinished(e -> deactivate());				//deactivate when 5second finish
            timer.play();
        }
    }

    @Override
    public void deactivate() {									//deactivate the shrink effect
        if (isActive) {
            paddle.resetPaddleWidth(); 							//reset back to original width
            isActive = false;
        }
        if (timer != null) {
            timer.stop();										// stop and clean up the timer
            timer = null;
        }
    }

    @Override
    public boolean isActive() {									//check if shrink effect is currently active
        return isActive;
    }
}
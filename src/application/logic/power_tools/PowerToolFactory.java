package application.logic.power_tools;

import application.logic.Ball.Ball;
import application.logic.paddle.Paddle;
import javafx.scene.layout.Pane;
import java.util.List;

/* Creates power-up、penalty instances based on type
 * Help to handles all power-up object creation in one place
 */
public class PowerToolFactory {
    public static PowerTool_Interface createTool(String powerType, Pane gamePane, Object... args) {
        switch(powerType) {
            case "BallDouble":
                List<Ball> balls = (List<Ball>) args[0];
                return new BallMultiplier(balls, gamePane); //create new multi balls
            case "BallProtection":
                return new BallProtection(gamePane);		//create protection
            case "ScoreDouble":	
            	return (ScoreMultiplier) args[0]; 			//get change on the multiplier variable that alr exists
            case "PaddleShrink":
                return new PaddleShrink((Paddle)args[0]);	//create penalty instance
            case "PaddleSlow":
                return new PaddleSlow((Paddle)args[0]);		//create penalty instance
            case "BallSpeedIncrease":
                return new BallSpeedIncrease((Ball)args[0]);//create penalty instance
        }
        return null;
    }
}
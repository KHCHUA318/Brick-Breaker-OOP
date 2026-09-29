package application.logic.power_tools;

import application.logic.Ball.Ball;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/* PowerTool implementation that creates duplicate balls when activated.
 * Each new ball starts at the same position as an existing ball but moves in the opposite direction.
 */
public class BallMultiplier implements PowerTool_Interface {
    private List<Ball> balls;									// Reference to game's ball list
    // Path to the image used for cloned balls
    private static final String CLONE_IMAGE_PATH = "/application/image/Rainbow Chocolate BALL.png";

    //Constructs a BallMultiplier tied to the game's ball list
    public BallMultiplier(List<Ball> balls, Pane gamePane) {
        this.balls = balls;
    }
    
    @Override
    public void activate() {
        if (!balls.isEmpty()) {
            List<Ball> newBalls = new ArrayList<>();
            Pane root = (Pane) balls.get(0).getVisual().getParent();//get parent pane from EXISTING balls
            
            for (Ball originalBall : new ArrayList<>(balls)) {	//create a copy to avoid concurrent modification
                Ball newBall = new Ball(root);					// Create new ball in same game pane
                
                // Set clone properties 
                ImageView cloneVisual = (ImageView) newBall.getVisual();
                cloneVisual.setImage(new Image(getClass().getResourceAsStream(CLONE_IMAGE_PATH)));
                newBall.setLoc_X(originalBall.getLoc_X());		//set the new ball at same location with exist ball
                newBall.setLoc_Y(originalBall.getLoc_Y());
                newBall.reverseX(); 							// Make clone go opposite direction
                newBalls.add(newBall);
            }
            balls.addAll(newBalls);								// Add all new balls to game's ball list
        }
    }
    
    //deactivate method does nothing - clones stay until they fall
    @Override
    public void deactivate() {} 
    
    @Override
    public boolean isActive() {						    		//Indicates whether the power effect is currently active
        return true;											// Effect is permanent for created balls
    }
}
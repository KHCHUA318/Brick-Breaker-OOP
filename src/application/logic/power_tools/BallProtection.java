package application.logic.power_tools;

import application.logic.Ball.Ball;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/* PowerTool implementation that creates a temporary protective barrier
 * at the bottom of the game screen to prevent balls from falling.
 * The barrier lasts for 10 seconds before automatically deactivating.
 */
public class BallProtection implements PowerTool_Interface {
    private boolean isActive = false;  							//activation state flag
    private PauseTransition timer;								//timer for automatic deactivate
    private Rectangle protectionBarrier;						//visual representation of the protection barrier
    private Pane gamePane;										// Reference to the game pane for UI operations

    //Constructs a BallProtection power-up tied to the game pane
    public BallProtection(Pane gamePane) { 
        this.gamePane = gamePane;
    }

    @Override
    public void activate() {									//Activates the protective barrier:
        if (!isActive && gamePane != null) {
            this.isActive = true;
            // Create protection barrier
            protectionBarrier = new Rectangle();
            protectionBarrier.setWidth(gamePane.getWidth()); 	//full width of game area
            protectionBarrier.setHeight(10);                 	//height barrier
            protectionBarrier.setLayoutX(0);                  	// Align to left edge
            protectionBarrier.setLayoutY(gamePane.getHeight() - protectionBarrier.getHeight()); // Position at bottom
            protectionBarrier.setFill(javafx.scene.paint.Color.GOLD); // Visual indicator
            protectionBarrier.setOpacity(0.8);                	// Set transparency
            
            // Add to game scene
            gamePane.getChildren().add(protectionBarrier);

            // Set timer for 10 seconds for automatic deactivate
            timer = new PauseTransition(Duration.seconds(10));
            timer.setOnFinished(e -> deactivate());
            timer.play();
        }
    }

    @Override
    public void deactivate() {									 //Deactivates the protective barrier:
        if (isActive) {
            this.isActive = false;
            if (timer != null) {								// Stop the timer
                timer.stop();
                timer = null;									//clean up the timer
            }
            if (protectionBarrier != null && gamePane != null) {
                gamePane.getChildren().remove(protectionBarrier);
                protectionBarrier = null;						// Clean up the barrier visual
            }
        }
    }

    @Override
    public boolean isActive() {									//Checks whether the protection barrier is currently active
        return isActive;
    }

    public boolean checkBallCollision(Ball ball) {				//handle the ball colliding with the protection barrier
        return isActive && 										//return "true" if barrier is active and collision occurred, false otherwise
               protectionBarrier != null && 
               ball.getVisual().getBoundsInParent().intersects(protectionBarrier.getBoundsInParent());
    }
    
    public double getBarrierY() {								//get the vertical position of the protection barrier
        return protectionBarrier != null ? protectionBarrier.getLayoutY() : 0;
    }
}
package application.logic.paddle;

import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/* Represents the player's paddle in the game with movement controls and visual representation.
 * Uses an image for visual display with fallback to a rectangle if image loading fails.
 */
public class Paddle {
    private ImageView paddle; 							// ImageView for displaying paddle image
    private double paddleWidth;							//Current width of paddle
    private double paddleHeight;						//height of the paddle
    private final double originalPaddleWidth;			// Original width for reset functionality		
    private double x, y;               					// Current position coordinates
    private double moveStep;           					// Movement speed in pixels per key press
    private double sceneWidth;         					// Width of the game scene for boundary checking
    private Image paddleImage;         					//image used for the paddle
    
    //Constructs a new paddle with specified game boundaries
    public Paddle(Pane root, double sceneWidth) {
        this.sceneWidth = sceneWidth;
        this.paddleWidth = 120; 						// default width
        this.originalPaddleWidth = paddleWidth;
        this.paddleHeight = 60; 						// height
        this.x = sceneWidth / 2 - paddleWidth / 2; 		// center position
        this.y = 560; 									// fixed Y position (near bottom)
        this.moveStep = 50; 							// paddle default movement speed

        // Load paddle image from resources
        paddleImage = new Image(getClass().getResourceAsStream("/application/image/Chocolate Bar.png"));
        paddle = new ImageView(paddleImage);
        paddle.setFitWidth(paddleWidth);
        paddle.setFitHeight(paddleHeight);
        paddle.setX(x);
        paddle.setY(y);
        
        root.getChildren().add(paddle);					// Add to game scene
        
    }

    public void moveLeft() {							//Moves the paddle left
    	x = Math.max(0, x - moveStep); 					//prevent moving past left edge
        paddle.setX(x);                					// Update visual position
    }

    public void moveRight() {							//Moves the paddle right
        x = Math.min(sceneWidth - paddleWidth, x + moveStep);//prevent moving past right edge
        paddle.setX(x);									// Update visual position
    }
    
/* -----------------SETTER method-----------------
 * -- access to set up private data of paddle
 */
    public void setMoveStep(double step) {
    	this.moveStep = Math.max(10, step);				//enforce MINIMUM movestep
    }

    public void setPaddleWidth(double newWidth) {		//set new width for the paddle while maintaining center position
    	double centerX = x + paddleWidth/2;       		//current center of paddle
        paddleWidth = Math.max(50, newWidth);    		//enforce MINIMUM width
        x = centerX - paddleWidth/2;             		//adjust position to maintain center
        paddle.setFitWidth(paddleWidth);         		//update visual width
        paddle.setX(x);                          		//update visual position
    }

    public void resetPaddleWidth() {
        setPaddleWidth(originalPaddleWidth);			//reset the paddle to original default width
    }
    
/* -----------------GETTER method----------------- 
 * -- access to get private data of paddle
 */ 
    public double getMoveStep() {						//get the current movement step size
    	return moveStep;
    }
    public ImageView getPaddle() { 						//get the visual representation of the paddle
        return paddle;									//return The ImageView containing the paddle visual
    }
    public double getPaddleWidth() {					//get the current width of the paddle
        return paddleWidth;
    }
    public double getLeftBound() {						//get the LEFT boundary position
    	return x;
    }
    public double getRightBound() {						//get the RIGHT boundary position
    	return x + paddleWidth;
    }
    public double getTopBound() {						//get the TOP boundary position
    	return y;
    }
    public double getBottomBound() {					//get the BUTTOM boundary position
    	return y + paddleHeight;
    }
}

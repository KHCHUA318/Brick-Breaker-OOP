package application.logic.Ball;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.Random;

/* Represents the ball in the game with physics, movement, and visual properties.
 * Handles ball movement, collisions, and rendering using an ImageView.
 */
public class Ball {
	private static final double DEFAULT_SPEED = 3;
	private double dx; 							// x-axis velocity
    private double dy; 							// y-axis velocity
    private double x_loc; 						//current X position            
    private double y_loc; 						//current Y position
    private double speed; 						//ball speed
    private double radius; 						//visual radius (ball size)
    private ImageView visual;					//ImageView for displaying the ball's image
    
    public Ball(Pane root){
        this.x_loc = 600; 						// define start position at center
        this.y_loc = 500;
        this.radius = 12;						//ball size
        this.speed = DEFAULT_SPEED;				//set initial speed
        
        // Visual representation
        Image ballImage = new Image(getClass().getResourceAsStream("/application/image/Rainbow Chocolate BALL.png"));
        visual = new ImageView(ballImage);
        visual.setFitWidth(radius * 2); 		// Set width to diameter
        visual.setFitHeight(radius * 2); 		// Set height to diameter
        visual.setX(x_loc - radius); 			//centered position  of image on x_loc
        visual.setY(y_loc - radius); 			//centered position  of image on y_loc
        // Add to game scene
        root.getChildren().add(visual);
        
        //let the start ball go UPWARD in random angle range (from -90 to -30 degrees)
        Random rand = new Random();
        double angle = -Math.PI/2 + (rand.nextDouble() * Math.PI/3);
        this.dx = Math.cos(angle) * this.speed; //horizontal velocity component
        this.dy = Math.sin(angle) * this.speed; //vertical velocity component
    }
    
    public void updatePosition() {
        x_loc += this.dx;						//Updates ball position based on velocity 
        y_loc += this.dy;
        visual.setX(x_loc-radius);				//Update the visual positions
        visual.setY(y_loc-radius);
    }
    public void reverseX() {					//change the HORIZONTAL direction when ball hit something
        this.dx = -this.dx;
    }
    public void reverseY() {					//change the VERTICAL direction when ball hit something
        this.dy = -this.dy;
    }

/* -----------------SETTER method-----------------
 * -- access to set up private data of ball
 */
    public void setSpeed(double newSpeed) {
    	//Sets a new speed while maintaining current direction
        double currentSpeed = Math.sqrt(this.dx*this.dx + this.dy*this.dy);		
        double ratio = newSpeed / currentSpeed;
        this.dx *= ratio;						// Scale x velocity
        this.dy *= ratio;						// Scale y velocity
        this.speed = newSpeed;					//update speed value
    }
    public void setDirection(double angle) {
    	//Sets a new movement direction while maintaining current speed
        double currentSpeed = Math.sqrt(this.dx*this.dx + this.dy*this.dy);
        this.dx = Math.cos(angle) * currentSpeed;
        this.dy = Math.sin(angle) * currentSpeed;
    }
    public void setLoc_X(double x) {			//Set new x-axis position
        this.x_loc = x;
    }
    public void setLoc_Y(double y) {			//Set new y-axis position
        this.y_loc = y;
    } 
    public void setDx(double dx) {				//Set new x-axis velocity
        this.dx = dx;
    }
    public void setDy(double dy) {				//Set new y-axis velocity
        this.dy = dy;
    }
    
/* -----------------GETTER method----------------- 
 * -- access to get private data of ball
 */
    public double getSpeed() {					//Return base speed of ball constantly
    	return DEFAULT_SPEED ; 					//avoid the ball speed update recursively
    }
    public double getLoc_X() {					//return Current x-axis position
        return this.x_loc;
    }
    public double getLoc_Y() {					//return Current y-axis position
        return this.y_loc;
    }
    public double getDx() {						//return Current x-axis velocity
        return this.dx;
    }
    public double getDy() {						//return Current y-axis velocity
        return this.dy;
    }
    public ImageView getVisual() {				//return The ImageView visual representation of the ball
        return visual;
    }
    public double getRadius() {					//return the ball size
        return radius;
    }   
}
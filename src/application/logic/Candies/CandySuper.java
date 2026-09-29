package application.logic.Candies;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;

/* Abstract base class for all candy types in the game.
 * Provides common functionality for candy visuals and state management.
 * All concrete candy types must implement the abstract game behavior methods.
 */
public abstract class CandySuper {
    protected boolean isDestroyed = false;			//flag indicating if candy has been destroyed
    protected ImageView visual; 					// JavaFX node for displaying the candy
    protected Image image;     						//the image used for this candy
    
    public abstract boolean isAvailable();			//method that check if candy is available for interaction
    public abstract void hit();						//method that handle candy being hit
    public abstract int getScore();					//method to get score value when candy is destroyed
    public abstract String blockPower();			//method to get special power associated with candy
    
    //Constructs a new candy with specified properties
    public CandySuper(double x, double y, double width, double height, String imagePath) {
        image = new Image(getClass().getResourceAsStream(imagePath));
        visual = new ImageView(image);
        visual.setFitWidth(width);  				// Set display width
        visual.setFitHeight(height); 				// Set display height
        visual.setX(x);              				// Set x position
        visual.setY(y);              				// Set y position
        visual.setEffect(new Glow(0.7)); 			// Add glow effect
    }
    
    public Node getVisual() {						//get the visual representation of the candy
        return visual;								//return JavaFX Node containing candy visual
    }
    
    public boolean isDestroyed() {					//Checks if candy has been destroyed
        return isDestroyed;							//return true if destroyed, false otherwise
    }		
}
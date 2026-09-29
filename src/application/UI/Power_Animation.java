package application.UI;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.Glow;
import javafx.util.Duration;
import application.logic.paddle.Paddle;

/* Handles the visual animation and behavior of power-up/penalty items
 * as they fall from destroyed blocks and can be collected by the paddle.
 */
public class Power_Animation {
    // Appearance constants
    private static final double POWER_UP_RADIUS = 10.0;
    private static final double GLOW_INTENSITY = 0.8;
    private static final double COLLISION_PADDING = 2.0;
    private static final double COLLECTION_GLOW = 1.5;
    private static final double FALL_DURATION = 3.0; // Seconds
    private static final double FADE_DURATION = 0.2; // Seconds

    // Game components
    private final Circle visual;          // Visual representation
    private final String powerType;      // Type identifier
    private final Pane root;             // Parent game pane
    
    // State tracking
    private Runnable onCollected;        // Callback when collected
    private boolean collected = false;   // Collection flag
    private TranslateTransition fallAnimation; // Falling animation

    //Create a new power-up animation
    public Power_Animation(Pane root, double blockX, double blockY, String powerType, double sceneHeight) {
        this.root = root;
        this.powerType = powerType;
        
        // Calculate spawn position - center of the block
        double spawnX = blockX;
        double spawnY = blockY; 
        this.visual = createVisual(spawnX, spawnY);
        // Calculate fall distance (from spawn to bottom of screen)
        setupFallingAnimation(sceneHeight - spawnY);
    }

    private Circle createVisual(double centerX, double centerY) {
    	//Creates the visual representation of the power-up
    	Circle circle = new Circle(							//power tool represent in a Circle
            centerX,									
            centerY,
            POWER_UP_RADIUS,
            getPowerColor(powerType)
        );
        circle.setEffect(new Glow(GLOW_INTENSITY));			//adding visual effect
        root.getChildren().add(circle);						//add to game scene
        return circle;
    }
    
    //Sets up the falling animation from spawn point to bottom of screen
    private void setupFallingAnimation(double fallDistance) {
        fallAnimation = new TranslateTransition(
            Duration.seconds(FALL_DURATION), 
            visual
        );
        fallAnimation.setByY(fallDistance);
        fallAnimation.setOnFinished(e -> cleanup());		//cleanup if not collected
        fallAnimation.play();
    }

    public void checkCollection(Paddle paddle) {			//Check for collision with paddle
        if (collected || visual.getParent() == null) return;//return null if get nothing

        if (visual.getBoundsInParent().intersects(			// Expanded collision bounds for better gameplay feel
            paddle.getPaddle().getBoundsInParent().getMinX() - COLLISION_PADDING,
            paddle.getPaddle().getBoundsInParent().getMinY() - COLLISION_PADDING,
            paddle.getPaddle().getBoundsInParent().getWidth() + (2 * COLLISION_PADDING),
            paddle.getPaddle().getBoundsInParent().getHeight() + (2 * COLLISION_PADDING))) {
            
            handleCollection();
        }
    }

    private void handleCollection() {						//Handles collection sequence
        collected = true;
        fallAnimation.stop();
        playCollectionEffects();							//Plays visual effects when collected
        
        // Schedule cleanup after effects finish
        PauseTransition delay = new PauseTransition(Duration.seconds(FADE_DURATION));
        delay.setOnFinished(e -> cleanup());
        delay.play();

        // Trigger callback on FX thread
        if (onCollected != null) {
            Platform.runLater(onCollected);
        }
    }

    private void playCollectionEffects() {					//method to play visual effects when collected
        visual.setEffect(new Glow(COLLECTION_GLOW));
        FadeTransition fade = new FadeTransition(Duration.seconds(FADE_DURATION), visual);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }

    public void setOnCollected(Runnable handler) {
        this.onCollected = handler;							//set the callback for when this power-up is collected
    }

    public boolean isActive() {								//check if power-up is still active in the game
        return visual.getParent() != null;					//return true if visible, false otherwise
    }

    public String getPowerType() {							//get the type identifier of this power-up
        return powerType;
    }

    private Color getPowerColor(String type) {
        return switch(type) {								//maps power-up types to visual colors
            case "BallDouble" -> Color.GREEN;
            case "BallProtection" -> Color.BLUE;
            case "ScoreDouble" -> Color.PURPLE;
            case "PaddleShrink" -> Color.RED;
            case "PaddleSlow" -> Color.ORANGE;
            case "BallSpeedIncrease" -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }
    
    private void cleanup() {
        if (visual.getParent() != null) {					//cleans up the power-up visual from the game
            root.getChildren().remove(visual);
        }
    }

    // For emergency cleanup if game ends
    public void forceCleanup() {
        if (fallAnimation != null) {
            fallAnimation.stop();
        }
        cleanup();
    }
}
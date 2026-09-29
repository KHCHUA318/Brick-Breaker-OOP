package application.model;

import application.UI.Power_Animation;
import application.logic.Ball.Ball;
import application.logic.Candies.*;
import application.logic.paddle.Paddle;
import application.logic.power_tools.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {
	// Game configuration constants
	private final int GRID_COLS = 30;
    private final int GRID_ROWS = 10;
    private final double BLOCK_WIDTH = 30;
    private final double BLOCK_HEIGHT = 30;
    private static final int INITIAL_LIVES = 3;
    
    // Game UI components
    private Pane gamePane;     								// Main game container pane
    private Text scoreText;    								// UI element displaying current score
    private Text livesText;    								// UI element displaying remaining lives
    
    // Game OBJECT COLLECTIONS
    private Paddle paddle;									// Player-controlled paddle
    private final List<Ball> balls;							// Immutable reference to balls list (contents can change)
    private final ScoreMultiplier multiplier= new ScoreMultiplier(); //ensure only single ScoreMultiplier instance
    private CandySuper[][] blockGrid;						// Mutable block grid reference (can be reassigned)
    private final List<Power_Animation> activePowerUps;		// Immutable power-ups list reference
    private final PowerUpManager powerUpManager;			// Immutable manager instance
    	
    // Game state variables
    private int score = 0;
    private int lives = INITIAL_LIVES;
    private boolean gameRunning = false;					// Game loop active flag
    private GameOverState gameOverState;					// Callback for game over events
    
    //Initializes a new game manager with UI components.
    public GameManager(Pane gamePane, Text scoreText, Text livesText) {
    	this.gamePane = gamePane;							// Store UI references
        this.scoreText = scoreText;
        this.livesText = livesText;
        
        this.balls = new ArrayList<>();						// Initialize final collections
        this.activePowerUps = new ArrayList<>();
        this.powerUpManager = new PowerUpManager();
        
        initializeGame();									// Initialize game state
    }
    
    private void initializeGame() {
    	// Clear any existing game elements first
        gamePane.getChildren().clear();
        
        // Add UI elements first (they should be on top)
        gamePane.getChildren().addAll(scoreText, livesText);
    	double gridWidth = GRID_COLS * BLOCK_WIDTH;			// Compute total width of grid
        double gridHeight = GRID_ROWS * BLOCK_HEIGHT;
        double offsetX = (gamePane.getWidth()-gridWidth)/2;	// Center grid horizontally in gamePane
        double offsetY = 20;
        
        Rectangle iceBackground = new Rectangle(offsetX, offsetY, gridWidth, gridHeight);
        
        // Candy Crush-style ice appearance
        iceBackground.setFill(Color.rgb(200, 230, 255, 0.5));  // Light blue with 30% opacity
        iceBackground.setStroke(Color.rgb(150, 200, 255, 1)); // Border with 70% opacity
        iceBackground.setStrokeWidth(2);
        iceBackground.setArcWidth(15);  // Slightly rounded corners
        iceBackground.setArcHeight(15);
        gamePane.getChildren().add(iceBackground);//add to game scene
        
        paddle = new Paddle(gamePane, gamePane.getWidth());	// Create paddle
        balls.add(new Ball(gamePane));						// Create initial ball
        createBlockGrid(gridWidth, gridHeight, offsetX, offsetY);									// Create block grid
        updateUI();											// Initialize UI
        
        startGameLoop();									// Start game loop
    }
    
    private void startGameLoop() {							
        gameRunning = true;
        new AnimationTimer() {								//Starts the main game animation loop.
            @Override
            public void handle(long now) {
                if (!gameRunning) return;
                // Core game loop sequence
                updateBalls();								// Update all balls
                updatePowerUps();							// Update power-ups
                checkCollisions();							// Check collisions
                checkGameState();							// Update game state
                powerUpManager.update();					// Update power-up manager
            }
        }.start();
    }
    
    private void updateBalls() {							// Update all ball positions and handles ball loss.
        List<Ball> ballsToRemove = new ArrayList<>();
        for (Ball ball : balls) {
            ball.updatePosition();
            if (ball.getLoc_Y() > gamePane.getHeight()) {	// Check for balls lost below screen
                ballsToRemove.add(ball);
                gamePane.getChildren().remove(ball.getVisual());//clean up them in scene
            }
        }balls.removeAll(ballsToRemove);					//clean up them in object list
       
        if (balls.isEmpty()) {								//handle player life loss when all balls are lost
            lives--;										//reduce 1 lives
            updateUI();										//update the game UI text
            if (lives > 0) {
                balls.add(new Ball(gamePane));				//add a new ball for player
            } else {
                gameOver(false);							//if lives=0, then GAMEOVER
            }
        }
    }
    private void updateUI() {								//update the game UI SCORE/LIVE TEXT
        scoreText.setText("Score: " + score);
        livesText.setText("Lives: " + lives);
    }
    private void updatePowerUps() {							//Updates active power-ups and removes expired ones
        List<Power_Animation> powerUpsToRemove = new ArrayList<>();//add a new list to store expired power tools
        for (Power_Animation powerUp : activePowerUps) {
            powerUp.checkCollection(paddle);
            if (!powerUp.isActive()) {
                powerUpsToRemove.add(powerUp);				
            }
        }activePowerUps.removeAll(powerUpsToRemove);		//removes expired ones
    }
    
    private void createBlockGrid(double gridWidth, double gridHeight, double offsetX, double offsetY) {
        blockGrid = new CandySuper[GRID_ROWS][GRID_COLS];
        Random rand = new Random();
        
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                double x = offsetX + col * BLOCK_WIDTH;
                double y = offsetY + row * BLOCK_HEIGHT;
                
                // Randomly select candy type based on probabilities
                double chance = rand.nextDouble();

                CandySuper block;
                if (chance < 0.8) {							// 80% basic candies
                    block = new Basic_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);// create the candy block 
                } else if (chance < 0.9) {					// 10% power up candies
                    double powerChance = rand.nextDouble();
                    if (powerChance < 0.33) {				//distribute the 3 type power candies evenly
                        block = new BluePacked_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    } else if (powerChance < 0.66) {
                        block = new GreenFish_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    } else {
                        block = new PurplePackedFish_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    }
                } else {									// 10% penalty candies
                    double penaltyChance = rand.nextDouble();
                    if (penaltyChance < 0.33) {				//distribute the 3 type penalty candies evenly
                        block = new RedBoom_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    } else if (penaltyChance < 0.66) {
                        block = new OrangeBoom_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    } else {
                        block = new YellowBoom_Candy(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
                    }
                }
                blockGrid[row][col] = block;				//store the result data
                gamePane.getChildren().add(block.getVisual());//add to game scene
            }
        }
    }
    
    private void checkCollisions() {						//check and handles all collision types in the game.
        for (Ball ball : balls) {							//loop and check for each balls
        	checkBlockCollisions(ball);						//check ball with candy block
            checkPaddleCollision(ball);						//check ball with paddle
            checkWallCollision(ball);						//check ball with wall
            if (powerUpManager.hasActiveProtection()) {		//check only protection activated
            	checkProtectionCollision(ball);
            }
        }
    }
//=================== Check BALL-CANDY collisions =================== 
    private void checkBlockCollisions(Ball ball) {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                CandySuper block = blockGrid[row][col];
                if (block != null && block.isAvailable() &&//candy not destroyed + scene intersects
                    ball.getVisual().getBoundsInParent().intersects(block.getVisual().getBoundsInParent())) {
                    handleBlockCollision(ball, block, row, col); //handle the collision behavior
                }
            }
        }
    }
    private void handleBlockCollision(Ball ball, CandySuper block, int row, int col) {
        // Get bounds of both objects
        Bounds ballBounds = ball.getVisual().getBoundsInParent();
        Bounds blockBounds = block.getVisual().getBoundsInParent();
        // Calculate overlap on all sides
        double ballLeft = ballBounds.getMinX();
        double ballRight = ballBounds.getMaxX();
        double ballTop = ballBounds.getMinY();
        double ballBottom = ballBounds.getMaxY();
        double blockLeft = blockBounds.getMinX();
        double blockRight = blockBounds.getMaxX();
        double blockTop = blockBounds.getMinY();
        double blockBottom = blockBounds.getMaxY();
        
        // Calculate overlap amounts
        double overlapLeft = ballRight - blockLeft;
        double overlapRight = blockRight - ballLeft;
        double overlapTop = ballBottom - blockTop;
        double overlapBottom = blockBottom - ballTop;
        
        // Determine which side has the smallest overlap (primary collision side)
        boolean fromLeft = overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom;
        boolean fromRight = overlapRight < overlapLeft && overlapRight < overlapTop && overlapRight < overlapBottom;
        boolean fromTop = overlapTop < overlapLeft && overlapTop < overlapRight && overlapTop < overlapBottom;
        boolean fromBottom = overlapBottom < overlapLeft && overlapBottom < overlapRight && overlapBottom < overlapTop;
        
        // Apply appropriate bounce based on collision side
        if (fromLeft || fromRight) {
            ball.reverseX(); 									// Horizontal bounce
            if (fromLeft) {										// Adjust position to prevent sticking
                ball.setLoc_X(blockLeft - ball.getRadius() - 1);
            } else {
                ball.setLoc_X(blockRight + ball.getRadius() + 1);
            }
        } else if (fromTop || fromBottom) {
            ball.reverseY(); 									// Vertical bounce
            if (fromTop) {										// Adjust position to prevent sticking
                ball.setLoc_Y(blockTop - ball.getRadius() - 1);
            } else {
                ball.setLoc_Y(blockBottom + ball.getRadius() + 1);
            }
        }
        block.hit();											// Register hit with the block
        
        // Handle candy block destruction
        if (!block.isAvailable()) {
            score += block.getScore() * this.multiplier.getMultiplier();//get candy's score
            updateUI();
            gamePane.getChildren().remove(block.getVisual());	//remove candy from game scene
            blockGrid[row][col] = null;							//remove candy from object matrix
            spawnPowerUp(block);								//spawn a power-up at the center of a destroyed block
        }
    }
    private void spawnPowerUp(CandySuper block) {				//spawn a power-up at the center of a destroyed block
        String powerType = block.blockPower();					// Get the type of power-up from the block
        if (powerType != null) {
            Node blockVisual = block.getVisual();
            // Calculate center position of the block for power-up spawn
            double centerX = blockVisual.getBoundsInParent().getMinX() + 
                           blockVisual.getBoundsInParent().getWidth()/2;
            double centerY = blockVisual.getBoundsInParent().getMinY() + 
                           blockVisual.getBoundsInParent().getHeight()/2;
            // Create new power-up animation at block's center position
            Power_Animation powerUp = new Power_Animation(gamePane, centerX, centerY, 
                                                        powerType, gamePane.getHeight());
            
            powerUp.setOnCollected(() -> activatePowerTool(powerType));
            
            activePowerUps.add(powerUp);						// Add to active power-ups list for tracking
        }
    }
    //Creates and activates a power-up tool when collected by player.
    private void activatePowerTool(String powerType) {
        PowerTool_Interface tool = PowerToolFactory.createTool(
            powerType, 
            gamePane, 
            getToolArguments(powerType)
        );
        powerUpManager.activatePowerUp(tool);					// Activate the power-up through the manager
    }
    private Object[] getToolArguments(String powerType) {		//Provide the required arguments for each power-up type.
        return switch(powerType) {
            case "BallDouble" -> new Object[]{balls};			
            case "PaddleShrink", "PaddleSlow" -> new Object[]{paddle};
            case "BallSpeedIncrease" -> new Object[]{balls.get(0)};
            case "ScoreDouble" -> new Object[]{multiplier};
            default -> new Object[]{};							// Default case for power-ups needing no arguments
        };
    }
    
//=================== Check BALL-PADDLE collisions =================== 
    private void checkPaddleCollision(Ball ball) {
	    if (ball.getVisual().getBoundsInParent().intersects(paddle.getPaddle().getBoundsInParent())) {
	    	handlePaddleCollision(ball);					//call handle function
	    }
	 }
    private void handlePaddleCollision(Ball ball) {
        // Get paddle bounds
        double paddleLeft = paddle.getLeftBound();
        double paddleRight = paddle.getRightBound();
        double paddleTop = paddle.getTopBound();
        double paddleBottom = paddle.getBottomBound();
        // Get ball bounds
        double ballLeft = ball.getLoc_X() - ball.getRadius();
        double ballRight = ball.getLoc_X() + ball.getRadius();
        double ballTop = ball.getLoc_Y() - ball.getRadius();
        double ballBottom = ball.getLoc_Y() + ball.getRadius();
        
        // Determine which side of the paddle was hit
        boolean hitTop = ballBottom >= paddleTop && ballTop < paddleTop;
        boolean hitBottom = ballTop <= paddleBottom && ballBottom > paddleBottom;
        boolean hitLeft = ballRight >= paddleLeft && ballLeft < paddleLeft;
        boolean hitRight = ballLeft <= paddleRight && ballRight > paddleRight;
        
        double paddleCenterX = paddle.getLeftBound() + paddle.getPaddleWidth() / 2;
        double hitPosition = (ball.getLoc_X() - paddleCenterX) / (paddle.getPaddleWidth() / 2);
        double maxAngle = Math.PI/4; // 45 degrees
        double bounceAngle = hitPosition * maxAngle;
        
        double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
        ball.setDx(Math.sin(bounceAngle) * speed);
        
        if (hitTop) {											// Handle top collision (normal case)
            ball.setDy(-Math.abs(Math.cos(bounceAngle)) * speed); //bounce up
            ball.setLoc_Y(paddle.getTopBound() - ball.getRadius() - 1); // Adjust position to prevent sticking
        } else if (hitBottom) {									 // Handle bottom collision (special case)
        	ball.setDy(Math.abs(Math.cos(bounceAngle)) * speed); //bounce down
        	ball.setLoc_Y(paddle.getBottomBound() - ball.getRadius() - 1); // Adjust position to prevent sticking
        }
        // Handle side collisions
        else if (hitLeft || hitRight) {
            ball.reverseX();
            if (hitLeft) {										// Adjust position to prevent sticking
                ball.setLoc_X(paddle.getLeftBound() - ball.getRadius() - 1);
            } else {
                ball.setLoc_X(paddle.getRightBound() + ball.getRadius() + 1);
            }
        }
    }
   
//=================== Check BALL-PROTECTION_BARRIER collisions =================== 
    private void checkProtectionCollision(Ball ball) {
	      BallProtection protection = powerUpManager.getActiveProtection();
	      if (protection.checkBallCollision(ball)) {
	          ball.reverseY();
	          // Adjust position to prevent sticking
	          ball.setLoc_Y(protection.getBarrierY() - ball.getRadius() - 1);
	      }
	  }
    
//=================== Check BALL-WALL collisions =================== 
    private void checkWallCollision(Ball ball) {
        if (ball.getLoc_X() <= ball.getRadius() || 
            ball.getLoc_X() >= gamePane.getWidth() - ball.getRadius()) {
            ball.reverseX();
        }
        if (ball.getLoc_Y() <= ball.getRadius()) {
            ball.reverseY();
        }
    }
    
    
//-------------------GAME OVER Interface-------------------
    public interface GameOverState {						//Interface for game over event handling
        void onGameOver(boolean won);
    }
    public void setGameOverListener(GameOverState listener) {//Set the game over event listener.
        this.gameOverState = listener;
    }
    private void gameOver(boolean won) {					//Handles game over state
        gameRunning = false;
        if (gameOverState != null) {
        	gameOverState.onGameOver(won);
        }
    }
    private void checkGameState() {							//Check if game win/lose conditions are met.
        boolean allBlocksDestroyed = true;
        for (int row = 0; row < GRID_ROWS; row++) {			// Check if all blocks are destroyed
            for (int col = 0; col < GRID_COLS; col++) {
                if (blockGrid[row][col] != null && blockGrid[row][col].isAvailable()) {
                    allBlocksDestroyed = false;				//if have one not destroy then skip loop
                    break;
                }
            }if (!allBlocksDestroyed) break;				//skip outer loop			
        }
        if (allBlocksDestroyed) {							//GAME OVER
            gameOver(true);
        }
    }

/* -------------------PUBLIC METHOD-------------------
 */
    public int getScore() {									//get current player score
        return score;
    }				
    public void movePaddleLeft() {							//Manage to moves paddle left
        paddle.moveLeft();
    }
    public void movePaddleRight() {							//Manage to moves paddle right
        paddle.moveRight();
    }
    public void resetGame() {
        gamePane.getChildren().clear();						// Clear existing game elements
        balls.clear();										//clear balls
        activePowerUps.clear();								//clear powertools
        powerUpManager.clearAll();							//make power manager empty 
        
        score = 0;											// Reset game state
        lives = 3;
        initializeGame();									//Reinitialize game
    }
}
package application.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import java.io.InputStream;
import java.net.URL;

import application.model.GameManager;

public class MainController {
	//FXML UI components linked from MainView.fxml and rule.fxml
    @FXML private StackPane rootPane;
    @FXML private Pane gamePane;
    @FXML private VBox startScreen;
    @FXML private VBox gameOverScreen;
    @FXML private Text gameOverText;
    @FXML private Text finalScoreText;
    @FXML private Button startButton;
    @FXML private Button restartButton;
    @FXML private Button menuButton;
    @FXML private Button ruleButton;
    @FXML private Button musicButton;
    @FXML private ImageView musicIcon;
    //Game Management
    private GameManager gameManager;
    private Stage primaryStage;
    private int finalScore;
    //Background Music
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = false;
    
//------------------SETTER method-----------------------
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
//-------------------Start up initialize----------------------
    @FXML
    public void initialize() {
        // Set background for start screen
        setBackgroundImage(startScreen, "/application/image/MAIN MENU bg.png");
        setupBackgroundMusic();						// Load and setup music
        
        //Button for event handle
        startButton.setOnAction(event -> startGame());
        restartButton.setOnAction(event -> restartGame());
        menuButton.setOnAction(event -> returnToMenu());
        ruleButton.setOnAction(event -> showRulesWindow());
        musicButton.setOnAction(event -> toggleMusic());
        
        //Style the game over text
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
    }
    
    private void initializeGame() {
    	// Create and position the score and lives text
        Text scoreText = createText("Score: 0", Color.BROWN);
        scoreText.setLayoutX(20);
        scoreText.setLayoutY(20);

        Text livesText = createText("Lives: 3", Color.BROWN);
        livesText.setLayoutX(20);
        livesText.setLayoutY(50);

        gamePane.getChildren().addAll(scoreText, livesText);
        //Initializes the GameManager with score/lives UI 
        gameManager = new GameManager(gamePane, scoreText, livesText);
        gameManager.setGameOverListener(won -> {	//set the game over callback
            finalScore = gameManager.getScore();
            showGameOverScreen(won);				// Handle game over scene
        });
    }
    
//--------------------UI BACKGROUND and Element SET UP--------------------
    // Helper method to set up background image 
    private void setBackgroundImage(Pane pane, String imagePath) {
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image bgImage = new Image(imageStream);
            BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                    BackgroundSize.AUTO,
                    BackgroundSize.AUTO,
                    false, false,true,true
                )
            );
            pane.setBackground(new Background(backgroundImage));
        } else {
            System.err.println("Could not load background image: " + imagePath);
        }
    }
    
    private void showRulesWindow() {					//open a modal window to show game rules using rule.fxml.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rule.fxml"));
            Pane rulesPane = loader.load();
            
            Stage rulesStage = new Stage();
            rulesStage.initModality(Modality.APPLICATION_MODAL);// Block input to other windows
            rulesStage.initOwner(primaryStage);
            rulesStage.setTitle("Game Rules");
            
            Scene rulesScene = new Scene(rulesPane);
            rulesStage.setScene(rulesScene);
            rulesStage.showAndWait();				//pause until close window
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    private void showGameOverScreen(boolean won) {
        gamePane.setVisible(false);					// Hide game pane 
        gameOverScreen.setVisible(true);			// Show game over screen
        gameOverScreen.toFront();					// Bring game over screen to front

        // Set final score
        finalScoreText.setText("Final Score: " + finalScore);
        if (won) {									// display background depending on game outcome
            setBackgroundImage(gameOverScreen, "/application/image/WIN bg.png"); //winning background
        } else {
            setBackgroundImage(gameOverScreen, "/application/image/GAMEOVER bg.png"); //losing background
        }
        
    }
    
    private Text createText(String content, Color color) {
        Text text = new Text(content);				//create styled Text nodes for UI (Score, Lives).
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        text.setFill(Color.BROWN);
        return text;
    }
  //--------------------BACKGROUND MUSIC SET UP--------------------
    private void setupBackgroundMusic() {			//Initializes the background music player
        URL musicUrl = getClass().getResource("/application/music/Candy Breaker Background Music.mp3");
        if (musicUrl != null) {
            Media sound = new Media(musicUrl.toString());// Create Media object from the music file
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            mediaPlayer.setVolume(0.5); 			// Set volume to 50%
        }
    }
    private void updateMusicButtonIcon() {
        String imagePath; 
        if (isMusicPlaying) {
            imagePath = "/application/image/MUSIC ON.png"; // Use "MUSIC ON" icon when music is playing
        } else {
            imagePath = "/application/image/MUSIC OFF.png";// Use "MUSIC OFF" icon when music is paused/stopped
        }
        
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {					// Load and set the appropriate icon image
            Image icon = new Image(imageStream);
            musicIcon.setImage(icon);
        }
    }

//--------------------Button and Key control function set up--------------------
    private void startGame() {						//starts the game by switching to the game pane
        startScreen.setVisible(false);				//hide main menu
        gamePane.setVisible(true);					//show game screen 
        gamePane.getChildren().clear();				// Clear any existing nodes(score/lives)
        
        // Initialize new game
        initializeGame();
        // Then set game background
        setBackgroundImage(gamePane, "/application/image/GAME bg.png");
        setupKeyControls();							//Enable paddle control key
    }
    
    private void restartGame() {
        gameOverScreen.setVisible(false);			//hide game over screen
        startGame();								//Restart the game after a game over.
    }
   
    private void returnToMenu() {
        gameOverScreen.setVisible(false);			//hide game over screen
        startScreen.setVisible(true);				//show main menu
    }	
    
    private void toggleMusic() {
        if (mediaPlayer != null) {
            if (isMusicPlaying) {
                mediaPlayer.pause();				//pause music if current playing
            } else {
                mediaPlayer.play();   				//play music if current pausing
            }
            isMusicPlaying = !isMusicPlaying;
            updateMusicButtonIcon();				// Update the button icon to reflect current music state (on/off)
        }
    }
    
    private void setupKeyControls() {				//Configure keyboard input to control paddle movement.
        Scene scene = rootPane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> {
                if (gameManager != null) {
                    switch (e.getCode()) {
                        case LEFT:
                            gameManager.movePaddleLeft();
                            break;
                        case RIGHT:
                            gameManager.movePaddleRight();
                            break;
                    }
                }
            });
        }
    }
}
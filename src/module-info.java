module Brick_Breaker {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.media;
    
    opens application.UI to javafx.fxml;  
    exports application;               
}
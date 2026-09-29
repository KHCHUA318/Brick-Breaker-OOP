# Candy Breaker Game

A JavaFX brick breaker game with Candy Crush Saga themed.

## How to play

- launch the "MainApp.java" inside application folder to start the game.
- Players must hit every candy block using the ball while maintaining the ball with the paddle from falling down. Players will be given 3 lives to destroy all candy completely. Players will declare winner if all candies are destroyed, else will be lose when lives are used up.

## Built with

- JavaFX
- Scene Builder

## 🎮 Features

- Background music
- High score tracking
- Multiple kinds of power-up & penalty tools

### 🍬 Candy Blocks

| Type               | Hits | Score | Effect              | Color  |
| ------------------ | ---- | ----- | ------------------- | ------ |
| Basic              | 1    | 1     | None                | Random |
| Blue Packed        | 2    | 5     | Ball Protection     | Blue   |
| Green Fish         | 3    | 10    | Double Balls        | Green  |
| Purple Packed Fish | 1    | 20    | Double Score        | Purple |
| Red Boom           | 1    | 0     | Paddle Shrink       | Red    |
| Orange Boom        | 1    | 0     | Paddle Slow         | Orange |
| Yellow Boom        | 2    | 0     | Ball Speed Increase | Yellow |

### ✨ Power-ups

- **Ball Protection** (Blue Packed): 10s bottom barrier protection
- **Ball Double** (Green Fish): Duplicates all balls
- **Score Double** (Purple Packed Fish): 2x points for 5s

### ☠ Penalties

- **Paddle Shrink** (Red Boom): reduce 50% of paddle width for 5s
- **Paddle Slow** (Orange Boom): reduce 33% speed of paddle movement for 10s
- **Ball Speed+** (Yellow Boom): 2x chocolate ball speed for 5s

### 🕹️ Controls

***Main Menu Page***
START button ： Start the Game
RULES button :  Show Game Rules Window
MUSIC button :  Pause/Start Music

***Game Screen***
← → : Move Paddle with left/right key

***Game Over Screen***
RESTART button ： Restart the Game
MAIN MENU button :  Back to Main Menu

## 🏗️Project Structure

src/
└── application/
   ├── MainApp.java        #launch the game here!
   ├── logic/
   │   ├── Ball/           		# Ball class
   │   ├── Candies/        		# Candy classes
   │   ├── paddle/        		# Paddle class
   │   └── power_tools/   	# Power-up classes
   ├── model/             		# GameManager
   └── UI/               			# JavaFX interfaces
   └── image/             		# UI image assets
   └── music/             		# UI music assets

## 📝 Credits

* Candy Crush Saga
* Brick Breaker
* Fantasy Dream Music - Dessert Land: https://www.youtube.com/watch?v=iCyVIgvsf2A

## Contributors

- Chua Kian Hoong
- Chua Meng Hong
- Kong Yenly
- Pang Siao Xuan

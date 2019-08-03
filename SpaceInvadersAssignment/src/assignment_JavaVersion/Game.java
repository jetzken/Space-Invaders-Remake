package assignment_JavaVersion;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L; // default serialisation
	// constants
	public final static int SCREEN_WIDTH = 800; // screen BOARDWIDTH
	public static final int SCREEN_HEIGHT = 600; // screen BOARDHEIGHT
	public final String TITLE = "Rise of the Lich";
	private BackgroundGenerator [][] bgFloorArray = new BackgroundGenerator[26][20]; // floor background tiles array
	private BackgroundGenerator [][] bgWallLArray = new BackgroundGenerator[1][20]; // left wall background tiles array
	private BackgroundGenerator [][] bgWallRArray = new BackgroundGenerator[1][20]; // right wall background tiles array
	private final int enemyStartX = 12; // invader start x position
	private final int enemyStartY = 80; // invader start y position
	private final int spacingX = 35; // spacing x between each invader
	private final int spacingY = 40; // spacing y between each invader
	private int xSize = 8; // array x size
	private final int ySize = 5; // array y size
	private int enemyTotal; // number of invaders is equal to the total array size
	private final int playerLives = 3; // total player lives
	private final int dropChance = 10; // enemy drops bomb if random number = drop chance
	private final int right = 2;
	private final int left = -2;
	
	private boolean running = false;
	private Thread thread;
	
	// in game variables
	private boolean inGame = false;
	private boolean bulletFired = false; // flag to see if bullet has been fired
	private Player player; // player variable
	private Bullet bullet; // player bullet variable
	private Textures tex; // used to select the right sprite for the object
	private ArrayList<Enemy> enemyList;
	private Bonus bonus; // bonus enemy
	private int bonusCounter; // bonus enemy wait time counter
	private int bonusRandom; // random number to determine bonus enemy wait time
	private boolean bonusSpawned; // flag for if bonus enemy spawned
	private ArrayList<Shield> shields; // array for shields
	private int currentScore; // current in-game score
	private int currentLives; // lives in game
	private int killed; // number of enemies killed counter
	private int eDirection = right; // enemy move direction
	
	// menu variables
	private enum STATE { MENU, GAME, HELP1, HELP2, PAUSE, GAMEOVER, CHARSELECT;} // game state
	private STATE state = STATE.MENU;
	// variables for mouse position to display feather next to corresponding button
	private boolean overPlay = false;
	private boolean overHelp = false;
	private boolean overQuit = false;
	private boolean overFire = false;
	private boolean overIce = false;
	private boolean overLightning = false;
	// player character picked type
	private enum CHARPICKED { NONE, FIRE, LIGHT, ICE;}
	private CHARPICKED charPicked;
	
	// buffered image loads images in the background to speed up display
	private BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null; // sprite sheet used for player, enemies, and tiles
	private BufferedImage menuScreen = null; // menu screen image
	private BufferedImage helpScreen = null; // help screen image
	private BufferedImage helpScreen2 = null;
	private BufferedImage pauseScreen = null; // pause screen image
	private BufferedImage gameOverScreen = null; // game over screen image
	private BufferedImage charSelectScreen = null; // character select screen image
	private BufferedImage feather = null; // feather used to show which button is hovered over
	private BufferedImage charSelecter = null; // square to highlight which character is hovered over
	private BufferedImage charSelected = null; // square to highlight which character is selected
	private BufferedImage fireSelected = null; // image showing fire wizard selected
	private BufferedImage electricSelected = null; // image showing lightning wizard selected
	private BufferedImage iceSelected = null; // image showing ice wizard selected
	
	// fonts
	private Font inGameFont = null; // font displaying score
	private Font gameOverFont = null; // font to display score at game over screen
	
	// *** INITIALISE *** //
	public void init () {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			// load images
			spriteSheet = loader.loadImage("/game_spritesheet.png");
			menuScreen = loader.loadImage("/menuScreen.png");
			pauseScreen = loader.loadImage("/pausescreen.png");
			charSelectScreen = loader.loadImage("/charSelectScreen.png");
			helpScreen = loader.loadImage("/helpScreen.png");
			helpScreen2 = loader.loadImage("/helpScreen2.png");
			gameOverScreen = loader.loadImage("/gameOverScreen.png");
			feather = loader.loadImage("/featherpointer72dpi.png");
			charSelecter = loader.loadImage("/charSelecter.png");
			charSelected = loader.loadImage("/charSelected.png");
			fireSelected = loader.loadImage("/charSelect_Fire.png");
			electricSelected = loader.loadImage("/charSelect_Electric.png");
			iceSelected = loader.loadImage("/charSelect_Ice.png");
			// load font
			inGameFont = new Font("arial", Font.PLAIN, 20);
			gameOverFont = new Font ("arial", Font.PLAIN, 60);
		} catch (IOException e) { // display error if image file cannot be found
			e.printStackTrace();
		}
		
		addKeyListener(new KeyInput(this)); // keyboard input
		// mouse input
		MouseInput mouseInput = new MouseInput(this);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
		tex = new Textures(this);
		restartGame();
	}
	// *** END INITIALISE *** //
	
	// *** SYNCSTART *** //
	private synchronized void start () {
		if (running) {
			return; // if running, then exit this method
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	// *** END SYNCSTART *** //
	
	// *** SYNCSTOP *** //
	private synchronized void stop () {
		if (!running) {
			return; // if there's no thread, exit this method
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace(); // display error report if threads fail to join
		}
		System.exit(1);
	}
	// *** END SYNCSTOP *** //
	
	// *** RUN *** //
	// game loop
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0; // game will update at a 60fps rate
		double ns = 1000000000 / amountOfTicks;
		double delta = 0; // calculate time passed if frames lag behind ticks
		int ticks = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			// game loop handles updates of the game
			long now = System.nanoTime();
			delta += (now - lastTime) / ns; // calculate time between previous time and time at loop restarting
			lastTime = now;
			if (delta >= 1) { // run tick method every 60 frames
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) { // display once every second
				timer += 1000;
				System.out.println("Ticks: " + ticks + " FPS: " + frames);
				ticks = 0;
				frames = 0;
			}
		}
		stop();
		// change stop() to gameOver() later
	}
	// *** END RUN *** //
	
	// *** TICK ***
	// main game code
	private void tick () {
		if (state == STATE.CHARSELECT) {
			if (charPicked == CHARPICKED.FIRE) {
				player.setWizard(player.getFire());
			}
			if (charPicked == CHARPICKED.LIGHT) {
				player.setWizard(player.getLightning());
			}
			if (charPicked == CHARPICKED.ICE) {
				player.setWizard(player.getIce());
			}
		}
		if (state == STATE.GAME) {
			runGame();
		}
	}
	// *** END TICK ***
	
	// *** RUN GAME ***
	private void runGame () {
		// only run if in game is true
		if (inGame) {
			// *** ENEMY ***
			// check if enemies have hit the screen edge
			Iterator<Enemy> moveEnemyIt = enemyList.iterator();
			while (moveEnemyIt.hasNext()) {
				Enemy e = (Enemy) moveEnemyIt.next();
				// if enemy isn't dead, check if it has hit the edge
				if (!e.isDead()) {
					int x = e.getX();
					int w = e.getW();
					// move enemies down if they hit the edge and change direction
					if ((x >= SCREEN_WIDTH - w - 10) && (eDirection == right)) {
						eDirection = left;
						moveEnemiesDown();
					}
					if ((x <= 16) && (eDirection == left)) {
						eDirection = right;
						moveEnemiesDown();
					}
				}
			}
			// move enemies across screen
			Iterator<Enemy> enemyUpdate = enemyList.iterator();
			while (enemyUpdate.hasNext()) {
				Enemy e = (Enemy) enemyUpdate.next();
				e.tick(eDirection);
			}
			// *** END ENEMY ***
			
			// *** ENEMY BOMB ***
			Iterator <Enemy> enemyBombDrop = enemyList.iterator();
			while (enemyBombDrop.hasNext()) {
				Enemy e = (Enemy) enemyBombDrop.next();
				Enemy.Bomb b = e.getBomb();
				if (!e.isDead()) { // only get bomb and fire it if enemy is alive
					int drop = (int)Math.floor(Math.random() * 800); // randomly choose when to drop bomb
					if (drop == dropChance && !b.bombDropped()) {
						b.setX(e.getX());
						b.setY(e.getY());
						b.dropped();
					}
					if (b.bombDropped()) {
						b.tick();
						if (player.bombCollision(b)) { // remove life and reset bomb on collision with player
							currentLives--;
							b.ready();
						}
						if (b.getY() + b.getH() >= SCREEN_HEIGHT) { // reset bomb if it goes off screen
							b.ready();
						}
						// check if shield has been hit by bomb
						Iterator <Shield> shieldHit = shields.iterator();
						while (shieldHit.hasNext()) {
							Shield s = (Shield) shieldHit.next();
							// subtract health if hit
							if (s.bombCollision(b)) {
								s.hit();
								b.ready();
							}
						}
					}
				}
			}
			// *** END ENEMY BOMB ***
			
			// *** BONUS ENEMY ***
			bonusCounter++; // increase bonus counter
			if (bonusCounter == bonusRandom) { // if bonus counter = randomised wait time, spawn bonus enemy
				bonusSpawned = true;
				bonus = new Bonus (tex);
			}
			
			if (bonusSpawned) { // if bonus enemy spawned, move bonus enemy
				bonus.tick();
				if (bonus.getX() == SCREEN_WIDTH + 30) { // check for if bonus enemy is off screen
					bonus = null;
					bonusSpawned = false;
					bonusSpawner();
				}
			}
			// *** END BONUS ENEMY ***
			
			// *** PLAYER ***
			player.tick();
			// *** END PLAYER ***
			
			// *** SHIELD ***
			// remove shield if shield has 0 health
			Iterator <Shield> shieldIt = shields.iterator();
			while (shieldIt.hasNext()) {
				Shield s = (Shield) shieldIt.next();
				if (s.getHealth() <= 0) {
					shieldIt.remove();
				}
			}
			// *** END SHIELD ***
			
			// *** BULLET ***			
			if (bulletFired == true) {
				bullet.tick();
				if (bullet.getY() <= 0) { // destroy bullet if it hits the top
					bullet = null;
					bulletFired = false;
				}
				// loop through enemies to check if hit
				Iterator<Enemy> enemyHit = enemyList.iterator();
				while (enemyHit.hasNext()) {
					Enemy e = (Enemy) enemyHit.next();
					if (e.bulletCollision(bullet) && !e.isDead()) { // if enemy is hit, destroy bullet, kill enemy and add to score
						bullet = null;
						bulletFired = false;
						e.kill();
						killed++;
						currentScore += e.getScore();
					}
				}
				// check if bonus enemy has been hit
				if (bonusSpawned) {
					if (bonus.bulletCollision(bullet)) {
						bullet = null;
						bulletFired = false;
						currentScore += bonus.getScore(); // add score
						bonus = null;
						bonusSpawned = false;
						bonusSpawner();
					}
				}				
				// check if shields have been hit by player bullet
				Iterator <Shield> shieldHit = shields.iterator();
				while (shieldHit.hasNext()) {
					Shield s = (Shield) shieldHit.next();
					if (s.bulletCollision(bullet)) {
						s.hit();
						bullet = null;
						bulletFired = false;
					}
				}
			}
			// *** END BULLET ***
			
			// *** BOARD INFORMATION ***
			// if lives = 0, end game
			if (currentLives == 0) {
				inGame = false;
			}
			// if all enemies killed, load new level
			if (killed == enemyTotal) {
				if (currentLives < 5) {
					currentLives++; // +1 life if less than 5
				}
				if (xSize < 11) {
					xSize++; // increase x dimension of enemies up to 11 across
				}
				newLevel(); // reload level
			}
			// *** END BOARD INFORMATION ***
		} // end inGame == true
		else {
			state = STATE.GAMEOVER;
		}
		
	}
	// *** END RUN GAME *** //
	
	// *** RENDER *** //
	private void render () {
		// handle buffering in background
		BufferStrategy bs = this.getBufferStrategy(); // "this" refers to Canvas
		if (bs == null) { // getBufferStrategy returns null if there isn't one
			// initialise buffer strategy once if one is not already created
			createBufferStrategy(3); // 3 buffers
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		// *** DRAW IMAGES HERE *** //
		// draw game
		if (state == STATE.GAME) {
			g.drawImage(image, 0, 0,  getWidth(), getHeight(), this); // clear screen
			// draw background
			// draw floor
			for (int i = 0; i < 26; i++) {
				for (int j = 0; j < 20; j++) {
					bgFloorArray[i][j].render(g);
				}
			}
			// draw left wall
			for (int i = 0; i < 20; i++) {
				bgWallLArray[0][i].render(g);
			}
			// draw right wall
			for (int i = 0; i < 20; i++) {
				bgWallRArray[0][i].render(g);
			}
			
			// draw enemy on screen
			Iterator<Enemy> it = enemyList.iterator();
			while (it.hasNext()) {
				Enemy e = (Enemy) it.next();
				if (!e.isDead()) { // only draw if enemy is alive
					e.render(g);
				}
			}
			
			// only render bomb if it has been dropped
			Iterator <Enemy> enemyBombDrop = enemyList.iterator();
			while (enemyBombDrop.hasNext()) {
				Enemy e = (Enemy) enemyBombDrop.next();
				if (!e.isDead()) {
					Enemy.Bomb b = e.getBomb();
					if (b.bombDropped()) {
						b.render(g);
					}
				}
			}
			
			// draw bonus enemy if it has been spawned
			if (bonusSpawned) {
				bonus.render(g);
			}
			
			// draw shields on screen
			Iterator <Shield> shieldIt = shields.iterator();
			while (shieldIt.hasNext()) {
				Shield s = (Shield) shieldIt.next();
				s.render(g);
			}
			
			player.render(g); // draw player on screen
			
			// draw bullet on screen if bullet is fired
			if (bulletFired == true) {
				bullet.render(g);
			}
			
			// display score
			g.setFont(inGameFont);
			g.setColor(Color.white);
			g.drawString("Score: " + currentScore, 10, 20);
			
			// display lives remaining
			if (currentLives == 5) {
				g.drawImage(tex.lifeDisplay, 760, 0, this);
				g.drawImage(tex.lifeDisplay, 723, 0, this);
				g.drawImage(tex.lifeDisplay, 686, 0, this);
				g.drawImage(tex.lifeDisplay, 649, 0, this);
				g.drawImage(tex.lifeDisplay, 612, 0, this);
			}
			else if (currentLives == 4) {
				g.drawImage(tex.lifeDisplay, 760, 0, this);
				g.drawImage(tex.lifeDisplay, 723, 0, this);
				g.drawImage(tex.lifeDisplay, 686, 0, this);
				g.drawImage(tex.lifeDisplay, 649, 0, this);
			}
			else if (currentLives == 3) {
				g.drawImage(tex.lifeDisplay, 760, 0, this);
				g.drawImage(tex.lifeDisplay, 723, 0, this);
				g.drawImage(tex.lifeDisplay, 686, 0, this);
			}
			else if (currentLives == 2) {
				g.drawImage(tex.lifeDisplay, 760, 0, this);
				g.drawImage(tex.lifeDisplay, 723, 0, this);
			}
			else if (currentLives == 1) {
				g.drawImage(tex.lifeDisplay, 760, 0, this);
			}
		}
		// draw menu
		else if (state == STATE.MENU){
			// draw menu and draw feather next to hovered button
			g.drawImage(menuScreen, 0, 0, getWidth(), getHeight(), this);
			if (overPlay) {
				g.drawImage(feather, 490, 276, 56, 52, this);
			}
			if (overHelp) {
				g.drawImage(feather, 490, 356, 56, 52, this);
			}
			if (overQuit) {
				g.drawImage(feather, 490, 436, 56, 52, this);
			}
		}
		// draw pause
		else if (state == STATE.PAUSE) {
			// draw pause menu and feather next to hovered button
			g.drawImage(pauseScreen, 0, 0, getWidth(), getHeight(), this);
			if (overPlay) {
				g.drawImage(feather, 490, 189, 56, 52, this);
			}
			if (overHelp) {
				g.drawImage(feather, 490, 269, 56, 52, this);
			}
			if (overQuit) {
				g.drawImage(feather, 490, 349, 56, 52, this);
			}
		}
		// draw character select
		else if (state == STATE.CHARSELECT) {
			// draw character select and box around hovered character
			g.drawImage(charSelectScreen, 0, 0, getWidth(), getHeight(), this);
			if (overFire) {
				g.drawImage(charSelecter, 244, 315, 96, 96, this);
			}
			if (overLightning) {
				g.drawImage(charSelecter, 358, 315, 96, 96, this);
			}
			if (overIce) {
				g.drawImage(charSelecter, 471, 315, 96, 96, this);
			}
			// use green box for selected character
			if (charPicked == CHARPICKED.FIRE) {
				g.drawImage(charSelected, 244, 315, 96, 96, this);
				g.drawImage(fireSelected, 266, 204, 278, 36, this);
			}
			else if (charPicked == CHARPICKED.LIGHT) {
				g.drawImage(charSelected, 358, 315, 96, 96, this);
				g.drawImage(electricSelected, 266, 204, 278, 36, this);
			}
			else if (charPicked == CHARPICKED.ICE) {
				g.drawImage(charSelected, 471, 315, 96, 96, this);
				g.drawImage(iceSelected, 266, 204, 278, 36, this);
			}
			// display feather over play button
			if (overPlay) {
				g.drawImage(feather, 490, 536, 56, 52, this);
			}
		}
		// draw help
		else if (state == STATE.HELP1) {
			g.drawImage(helpScreen, 0, 0, getWidth(), getHeight(), this);
			if (overHelp) {
				g.drawImage(feather, 490, 467, 56, 52, this);
			}
			if (overPlay) {
				g.drawImage(feather, 490, 536, 56, 52, this);
			}
		}
		else if (state == STATE.HELP2) {
			g.drawImage(helpScreen2, 0, 0, getWidth(), getHeight(), this);
			if (overHelp) {
				g.drawImage(feather, 490, 467, 56, 52, this);
			}
			if (overPlay) {
				g.drawImage(feather, 490, 536, 56, 52, this);
			}
		}
		// draw game over screen
		else if (state == STATE.GAMEOVER) {
			g.drawImage(gameOverScreen, 0, 0, getWidth(), getHeight(), this);
			if (overPlay) {
				g.drawImage(feather, 490, 467, 56, 52, this);
			}
			if (overQuit) {
				g.drawImage(feather, 490, 536, 56, 52, this);
			}
			// game over text
			g.setFont(gameOverFont);
			g.setColor(Color.decode("#efcca4"));
			g.drawString("" + currentScore, SCREEN_WIDTH/2 - 20, SCREEN_HEIGHT/2); // display final score
		}
		
		// *** END DRAW IMAGES *** //
		
		g.dispose();
		bs.show();	
	}
	// *** END RENDER *** //
	
	// *** KEY PRESSED *** //
	public void keyPressed (KeyEvent e) {
		int key = e.getKeyCode();
		if (state == STATE.GAME) { // if state is set to game, allow game controls
			if (key == KeyEvent.VK_RIGHT) { // move right
				player.setVelX(5);
			}
			if (key == KeyEvent.VK_LEFT) { // move left
				player.setVelX(-5);
			}
			if ((key == KeyEvent.VK_SPACE)) { // fire bullet if one is not already shot
				if (!bulletFired) {
					bullet = new Bullet(tex, player);
					bullet.fireBullet(player.getX(), player.getY());
					bulletFired = true;
				}
			}
			if (key == KeyEvent.VK_P) { // if "p" is pressed, pause game
				state = STATE.PAUSE;
			}
		}
		else if (state == STATE.PAUSE) {
			if (key == KeyEvent.VK_P) {
				state = STATE.GAME;
			}
		}
	}
	// *** END KEY PRESSED *** //
	
	// *** KEY RELEASED *** //
	public void keyReleased (KeyEvent e) {
		int key = e.getKeyCode();
		// set player velocity to 0 to stop player moving
		if (key == KeyEvent.VK_RIGHT) {
			player.setVelX(0);
		}
		if (key == KeyEvent.VK_LEFT) {
			player.setVelX(0);
		}
	}
	// *** END KEY RELEASED *** //
	
	// *** MOUSE PRESSED ***
	public void mousePressed(MouseEvent e) {		
		// character select options
		// fire wizard selected
		if (overFire) {
			charPicked = CHARPICKED.FIRE;
		}
		// lightning wizard selected
		if (overLightning) {
			charPicked = CHARPICKED.LIGHT;
		}
		// ice wizard selected
		if (overIce) {
			charPicked = CHARPICKED.ICE;
		}
		
		// change state when button is clicked		
		// if play button clicked, set game state to game 
		if (overPlay) {
			// if no character picked, play button takes player to character select screen
			if (charPicked == CHARPICKED.NONE) {
				if (overPlay) {
					state = STATE.CHARSELECT;
					overPlay = false;
					overHelp = false;
					overQuit = false;
				}
			}
			else {
				state = STATE.GAME;
				inGame = true;
				overPlay = false;
				overHelp = false;
				overQuit = false;
				}
			}
		
		if (state == STATE.HELP1) { // go to second help screen if next button clicked 
			if (overHelp) {
				state = STATE.HELP2;
				overHelp = false;
			}
		}
		
		if (state == STATE.HELP2) { // go to first help screen if back button clicked
			if (overHelp) {
				state = STATE.HELP1;
				overHelp = false;
			}
		}
		
		// if help button clicked, set game state to help
		if (overHelp) {
			state = STATE.HELP1;
			overPlay = false;
			overHelp = false;
			overQuit = false;
		}
		
		if (state == STATE.PAUSE) {
			// return to menu if quit button clicked when on the pause screen
			if (overQuit) {
				overQuit = false;
				restartGame();
				state = STATE.MENU;
			}
		}
		
		// if quit button clicked, quit game
		if (overQuit) {
			System.exit(1);
		}
		
	}
	// *** END MOUSE PRESSED ***
	
	// *** MOUSE MOVED ***
	public void mouseMoved(MouseEvent e) {
		// check mouse position for which button (if any) is hovered over
		int mx = e.getX();
		int my = e.getY();
		// mouse position in main menu
		if (state == STATE.MENU) {
			// play button
			if (mx >= 320 && mx <= 480 && my >= 272 && my <= 332) {
				overPlay = true;
			}
			else {
				overPlay = false;
			}
			// help button
			if (mx >= 320 && mx <= 480 && my >= 352 && my <= 412) {
				overHelp = true;
			}
			else {
				overHelp = false;
			}
			// quit button
			if (mx >= 320 && mx <= 480 && my >= 432 && my <= 492) {
				overQuit = true;
			}
			else {
				overQuit = false;
			}
		}
		
		// mouse position for character select screen
		if (state == STATE.CHARSELECT) {
			// fire wizard
			if (mx >= 247 && mx <= 345 && my >= 343 && my <= 431) {
				overFire = true;
			}
			else {
				overFire = false;
			}
			// lightning wizard
			if (mx >= 359 && mx <= 447 && my >= 343 && my <= 431) {
				overLightning = true;
			}
			else {
				overLightning = false;
			}
			// ice wizard
			if (mx >= 471 && mx <= 559 && my >= 343 && my <= 431) {
				overIce = true;
			}
			else {
				overIce = false;
			}
			// play button
			if (mx >= 320 && mx <= 480 && my >= 527 && my <= 587) {
				overPlay = true;
			}
			else {
				overPlay = false;
			}
		}
		
		// mouse position for help screen
		if (state == STATE.HELP1 || state == STATE.HELP2) {
			if (mx >= 320 && mx <= 480 && my >= 527 && my <= 587) {
				// play/resume
				overPlay = true;
			}
			else {
				overPlay = false;
			}
			if (mx >= 320 && mx <= 480 && my >= 458 && my <= 518) {
				// next/back button on help screen
				overHelp = true;
			}
			else {
				overHelp = false;
			}
		}
		
		// mouse position for pause screen
		if (state == STATE.PAUSE) {
			// play button
			if (mx >= 320 && mx <= 480 && my >= 185 && my <= 245) {
				overPlay = true;
			}
			else {
				overPlay = false;
			}
			// help button
			if (mx >= 320 && mx <= 480 && my >= 265 && my <= 325) {
				overHelp = true;
			}
			else {
				overHelp = false;
			}
			// quit button
			if (mx >= 320 && mx <= 480 && my >= 345 && my <= 405) {
				overQuit = true;
			}
			else {
				overQuit = false;
			}
		}
		
		// mouse position for game over
		if (state == STATE.GAMEOVER) {
			// replay button
			if (mx >= 320 && mx <= 480 && my >= 458 && my <= 518) {
				overPlay = true;
			}
			else {
				overPlay = false;
			}
			// quit button
			if (mx >= 320 && mx <= 480 && my >= 527 && my <= 587) {
				overQuit = true;
			}
			else {
				overQuit = false;
			}
		}
	}
	// *** END MOUSE MOVED ***
	
	// *** GENERATE BACKGROUND *** //
	private void generateBackground () { // fill background array with tiles
		int floor = 0;
		int wallL = 1;
		int wallR = 2;
		// generate floor
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 20; j++) {
				BackgroundGenerator bg = new BackgroundGenerator (0 + i*32, 0 + j*32, tex, floor);
				bgFloorArray[i][j] = bg;
			}
		}
		// generate left wall
		for (int i = 0; i < 20; i++) {
			BackgroundGenerator bg = new BackgroundGenerator (0, 0 + i*32, tex, wallL);
			bgWallLArray[0][i] = bg;
		}
		// generate right wall
		for (int i = 0; i < 20; i++) {
			BackgroundGenerator bg = new BackgroundGenerator (795, 0 + i*32, tex, wallR);
			bgWallRArray[0][i] = bg;
		}
	}
	// *** END GENERATE BACKGROUND *** //
	
	// *** POPULATE ENEMIES *** //
	private void populateEnemies () { // fill enemies array with new enemies
		enemyList = new ArrayList<Enemy> ();
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				Enemy e = new Enemy (enemyStartX + spacingX*j, enemyStartY + spacingY*i, tex); // add spacing between aliens   
				// set enemy types
				if (i == 0) {
					e.setEnemy(e.getSkeleton());
					e.bomb.setBombSprite(e.getSkeleton());
				} else if (i <= 2) {
					e.setEnemy(e.getSpider());
					e.bomb.setBombSprite(e.getSpider());
				} else if (i <= ySize) {
					e.setEnemy(e.getSlime());
					e.bomb.setBombSprite(e.getSlime());
				}
				enemyList.add(e);
			}
		}
	}
	// *** END POPULATE ENEMIES *** //
	
	// *** MOVE ENEMIES DOWN *** //
	private void moveEnemiesDown () { // move enemies down
		// loop through enemies and move each one down by 16 pixels
		Iterator<Enemy> it = enemyList.iterator();
		while (it.hasNext()) {
			Enemy e = (Enemy) it.next();
			e.setY(e.getY() + 16);
			int y = e.getY();
			if ((y > 550 - e.getH()) && !e.isDead()) {
				inGame = false;
			}
		}
	}
	// *** END MOVE ENEMIES DOWN *** //
	
	// *** BONUS SPAWNER ***
	private void bonusSpawner() { // reset bonus enemy spawn
		bonusCounter = 0;
		bonusRandom = 300 + (int)(Math.floor(Math.random()*5)*10); // randomise bonus enemy wait time
	}
	// *** END BONUS SPAWNER ***
	
	// *** CREATE SHIELDS ***
	private void createShields () { // create shields/barriers between player and enemy
		int x = 110;
		shields = new ArrayList<Shield> ();
		for (int i = 0; i <= 3; i++) {
			Shield s = new Shield (tex, x);
			shields.add(s);
			x += 180;
		}
	}
	// *** END CREATE SHIELDS ***
	
	// *** NEW LEVEL ***
	private void newLevel () { // create new level
		// reset game variables required for new levels
		enemyList = null;
		bullet = null;
		bonus = null;
		bonusSpawned = false;
		bulletFired = false;
		populateEnemies ();
		bonusSpawner();
		enemyTotal = xSize * ySize; // reset enemy total
		killed = 0;
	}
	// *** END NEW LEVEL ***
	
	// *** RESTART GAME *** //
	private void restartGame() { // reset game variables
		// clear game objects
		player = null;
		// reset game variables		
		player = new Player (0, 0, tex);
		player.setY(570 - player.getH());
		player.setX(SCREEN_WIDTH/2);
		charPicked = CHARPICKED.NONE;
		generateBackground();
		newLevel();
		createShields();
		currentLives = playerLives;
		currentScore = 0;
	}
	// *** END RESTART GAME *** //
	
	public static void main(String[] args) {
		Game game = new Game(); // create new instance of Game class
		
		// set screen size
		game.setPreferredSize(new Dimension (SCREEN_WIDTH, SCREEN_HEIGHT));
		game.setMaximumSize(new Dimension (SCREEN_WIDTH, SCREEN_HEIGHT));
		game.setMinimumSize(new Dimension (SCREEN_WIDTH, SCREEN_HEIGHT));
		
		JFrame gameFrame = new JFrame (game.TITLE); // create new JFrame with title TITLE
		gameFrame.add(game);
		gameFrame.pack();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false); // prevent frame being resized
		gameFrame.setLocationRelativeTo(null); // prevent set location method being run
		gameFrame.setVisible(true);
		
		game.start();
		
	}
	
	
	// getters and setters	
	public BufferedImage getSpriteSheet () {
		return spriteSheet;
	}

}

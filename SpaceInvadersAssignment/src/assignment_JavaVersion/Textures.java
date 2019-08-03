package assignment_JavaVersion;

import java.awt.image.BufferedImage;

public class Textures {

	public BufferedImage iceWizard, fireWizard, lightningWizard; // player sprites
	public BufferedImage lifeDisplay; // player lives
	public BufferedImage shieldFull, shieldThreeQuarter, shieldHalf, shieldQuarter; // barricade sprites
	public BufferedImage iceAttack, fireAttack, lightningAttack; // player bullets
	public BufferedImage slime, spider, skeleton, ghost; // enemy sprites
	public BufferedImage slimeAttack, spiderAttack, skeletonAttack; // enemy attack sprites
	public BufferedImage mainTile, water1Tile, water2Tile, crackedTile, mossTile, lichenTile; // floor tiles
	public BufferedImage mainWallLeft, mainWallRight, torchWallLeft, torchWallRight; // wall tiles
	private SpriteSheet ss = null;
	private int standardSize = 32; // standard sprite size
	private int halfSize = standardSize/2; // half of standard size
	
	// constructor
	public Textures (Game game) {
		this.ss = new SpriteSheet (game.getSpriteSheet());
		
		getTextures();
	}
	
	private void getTextures () {
		// grabImage (column, row, width, height)
		// player sprites
		fireWizard = ss.grabImage(1, 1, standardSize, standardSize);
		lightningWizard = ss.grabImage(2, 1, standardSize, standardSize);
		iceWizard = ss.grabImage(3, 1, standardSize, standardSize);
		fireAttack = ss.grabImage(1, 2, halfSize, standardSize);
		lightningAttack = ss.grabImage(2, 2, halfSize, standardSize);
		iceAttack = ss.grabImage(3, 2, halfSize, standardSize);
		lifeDisplay = ss.grabImage(5, 3, standardSize, standardSize);
		
		// shield sprites
		shieldFull = ss.grabImage(7, 2, standardSize*2, standardSize);
		shieldThreeQuarter = ss.grabImage(7, 3, standardSize*2, standardSize);
		shieldHalf = ss.grabImage(1, 4, standardSize*2, standardSize);
		shieldQuarter = ss.grabImage(3, 4, standardSize*2, standardSize);
		
		// enemy sprites
		slime = ss.grabImage(1, 3, standardSize, standardSize);
		spider = ss.grabImage(2, 3, standardSize, standardSize);
		skeleton = ss.grabImage(3, 3, standardSize, standardSize);
		slimeAttack = ss.grabImage(4, 3, halfSize, halfSize);
		spiderAttack = ss.grabImage(4.5, 3, halfSize, halfSize);
		skeletonAttack = ss.grabImage(4, 3.5, halfSize, halfSize);
		ghost = ss.grabImage(6, 3, standardSize, standardSize);
		
		// background tile sprites
		mainTile = ss.grabImage(4, 1, standardSize, standardSize);
		crackedTile = ss.grabImage(5, 1, standardSize, standardSize);
		lichenTile = ss.grabImage(6, 1, standardSize, standardSize);
		water1Tile = ss.grabImage(7, 1, standardSize, standardSize);
		water2Tile = ss.grabImage(8, 1, standardSize, standardSize);
		mossTile = ss.grabImage(4, 2, standardSize, standardSize);
		mainWallLeft = ss.grabImage(5, 2, halfSize, standardSize);
		mainWallRight = ss.grabImage(5.5, 2, halfSize, standardSize);
		torchWallLeft = ss.grabImage(6, 2, halfSize, standardSize);
		torchWallRight = ss.grabImage(6.5, 2, halfSize, standardSize);
		
	}
	
}

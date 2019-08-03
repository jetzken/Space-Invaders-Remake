package assignment_JavaVersion;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage image; // store sprite sheet
	private int spriteSize = 32; // standard sprite size
	
	// constructor
	public SpriteSheet (BufferedImage ss) {
		this.image = ss;
	}
	
	// get individual sprites
	public BufferedImage grabImage (double col, double row, int width, int height) { // take in doubles for col and row to allow for small sprites within the same space
		BufferedImage img = image.getSubimage((int)(col * spriteSize) - spriteSize, (int)(row * spriteSize) - spriteSize, width, height);
		return img;
	}
	
}

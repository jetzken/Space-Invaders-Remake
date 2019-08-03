package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bonus {
	private int score; // points bonus enemy is worth
	private int x, y; // bonus enemy position
	private BufferedImage image;
	
	public Bonus (Textures t) {
		this.x = -30;
		this.y = 40;
		this.score = generateScore();
		this.image = t.ghost;
	}
	
	public void tick () {
		x += 2;
	}
	
	public void render (Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	private int generateScore () { // generate what bonus enemy is worth at spawn
		int s = 50;
		s = s * ((int)Math.ceil(Math.random() * 19)+1); // generate a number between 50 - 1000
		return s;
	}
	
	// check if enemy has been hit
	public boolean bulletCollision (Bullet bullet) {
		if ((bullet != null)) {
			if (Math.abs(this.x - bullet.getX()) < 20 && Math.abs(this.y - bullet.getY()) < 20) {
				return true;
			}
		}
		return false;
	}
	
	public int getScore () {
		return score;
	}
	
	public int getX() {
		return x;
	}
	
}

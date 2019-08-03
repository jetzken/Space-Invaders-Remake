package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shield {
	private int health = 10; // health of shield
	private BufferedImage image; // sprite for shield
	private int x; // x location of shield
	private int y = 480;
	private int w = 64;
	private Textures tex;
	
	public Shield (Textures t, int x) {
		this.tex = t;
		this.x = x;
	}
	
	public void render (Graphics g) {
		if (health <= 3) {
			image = tex.shieldQuarter;
		}
		else if (health <= 5) {
			image = tex.shieldHalf;
		}
		else if (health <= 8) {
			image = tex.shieldThreeQuarter;
		}
		else if (health <= 10) {
			image = tex.shieldFull;
		}
		g.drawImage(image, x, y, null);
	}
	
	public void hit () {
		health--;
	}
	
	// check if hit by player bullet
	public boolean bulletCollision (Bullet bullet) {
		if ((bullet != null)) {
			if (x-5 <= bullet.getX() && x+w >= bullet.getX() && Math.abs(this.y - bullet.getY()) < 5) {
				return true;
			}
		}
		return false;
	}
	
	// check for collision with enemy bomb
	public boolean bombCollision (Enemy.Bomb b) {
		if (b.bombDropped()) {
			if (x-5 <= b.getX() && x+w >= b.getX() && Math.abs(this.y - b.getY()) < 10) {
				return true;
			}
		}
		return false;
	}
	
	// getters
	public int getHealth () {
		return health;
	}
	
}

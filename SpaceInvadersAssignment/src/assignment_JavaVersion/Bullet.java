package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet {
	private int x, y; // bullet position
	private int h = 32; // bullet height
	private Textures tex;
	private BufferedImage bullet;
	
	// constructor
	public Bullet (Textures t, Player p) {
		this.tex = t;
		// set bullet image depending on player type
		if (p.getType() == p.getFire()) {
			bullet = tex.fireAttack;
		} else if (p.getType() == p.getLightning()) {
			bullet = tex.lightningAttack;
		}
		else if (p.getType() == p.getIce()) {
			bullet = tex.iceAttack;
		}
	}
	
	// set bullet starting x and y
	public void fireBullet (int x, int y) {
			this.x = x;
			this.y = y - h;
	}
	
	// move bullet up screen
	public void tick () {
		y -= 7;
	}
	
	// render bullet
	public void render (Graphics g) {
		g.drawImage(bullet, x, y, null);
	}
	
	// getters and setters
	public int getH() {
		return h;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}

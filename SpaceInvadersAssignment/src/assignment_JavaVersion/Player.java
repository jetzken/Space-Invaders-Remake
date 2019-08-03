package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {
	private int x, y; // defender position
	private int w = 32; // defender width
	private int h = 32; // defender height
	private int velX = 0;
	private Textures tex;
	private BufferedImage wizard;
	private enum TYPE {FIRE, LIGHTNING, ICE};
	private TYPE type;
	
	// constructor
	public Player (int x, int y, Textures tex) {
		this.x = x;
		this.y = y;
		this.tex = tex;
	}
	
	private void move() {
		x += velX;
		// edge collision
	}
	
	public void tick () {
		move();
		if (x <= 10) {
			x = 10;
		}
		if (x >= 800 - (w/2) - 10) {
			x = 800 - (w/2) - 10;
		}
	}
	
	// check for collision with enemy bomb
	public boolean bombCollision (Enemy.Bomb b) {
		if (b.bombDropped()) {
			if (Math.abs(this.x - b.getX()) < 25 && Math.abs(this.y - b.getY()) < 10) {
				return true;
			}
		}
		return false;
	}
	
	public void render (Graphics g) {
		g.drawImage(wizard, x, y, null);
	}
	
	// getters and setters
	public void setWizard (TYPE t) {
		if (t == TYPE.FIRE) {
			this.type = t;
			this.wizard = tex.fireWizard;
		} else if (t == TYPE.LIGHTNING) {
			this.type = t;
			this.wizard = tex.lightningWizard;
		} else if (t == TYPE.ICE) {
			this.type = t;
			this.wizard = tex.iceWizard;
		}
	}
	
	public TYPE getFire () {
		return TYPE.FIRE;
	}
	
	public TYPE getLightning () {
		return TYPE.LIGHTNING;
	}
	
	public TYPE getIce () {
		return TYPE.ICE;
	}
	
	public TYPE getType () {
		return type;
	}
	
	public int getW() {
		return w;
	}
	public int getH() {
		return h;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setVelX(int velX) {
		this.velX = velX;
	}
}

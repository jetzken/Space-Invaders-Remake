package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Enemy {
	private int x, y; // enemy position
	private int h = 32; // enemy height
	private int w = 32; // enemy width
	private boolean dead = false; // flag to detect if enemy is alive or dead
	Bomb bomb; // bomb class called bomb
	private enum TYPE { SLIME, SPIDER, SKELETON; } // enemy type
	private int score; // score enemy is worth
	private BufferedImage enemy; // enemy sprite
	private Textures tex;
	private TYPE type;
	
	// constructor
	public Enemy (int x, int y, Textures tex) {
		this.x = x;
		this.y = y;
		bomb = new Bomb();
		this.tex = tex;
	}
	
	// move enemy across screen
	public void tick(int speed) {
			x += speed;
	}
	
	// render enemy
	public void render (Graphics g) {
		g.drawImage(enemy, x, y, null);
	}
	
	// check if enemy has been hit
	public boolean bulletCollision (Bullet bullet) {
		if ((bullet != null)) {
			if (Math.abs(this.x - bullet.getX()) < 25 && Math.abs(this.y - bullet.getY()) < 20) {
				return true;
			}
		}
		return false;
	}
	
	// different types of enemies
	// different enemies will be worth different points and use a different sprite
	public void setEnemy (TYPE type) {
		if (type == TYPE.SLIME) {
			score = 10;
			enemy = tex.slime;
			this.type = type;
		} else if (type == TYPE.SPIDER) {
			score = 20;
			enemy = tex.spider;
			this.type = type;
		} else if (type == TYPE.SKELETON) {
			score = 40;
			enemy = tex.skeleton;
			this.type = type;
		}
	}
	
	public void kill() { // set enemy dead to true
		this.dead = true;
	}
	
	// Enemy getters and setters
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isDead() {
		return dead;
	}
	public int getH() {
		return h;
	}
	public int getW() {
		return w;
	}
	public int getScore() {
		return score;
	}
	public TYPE getType () {
		return type;
	}
	public TYPE getSlime() {
		return TYPE.SLIME;
	}
	public TYPE getSpider() {
		return TYPE.SPIDER;
	}
	public TYPE getSkeleton() {
		return TYPE.SKELETON;
	}
	public Bomb getBomb () {
		return bomb;
	}
	
	// Enemy bomb class
	public class Bomb {
		private int x, y; // bomb position
		private int h = 16; // bomb height
		private boolean dropped = false; // flag for if bomb is dropped or not
		private BufferedImage bombSprite;
		
		// move bullet down screen
		public void tick () {
			y += 3;
		}
		
		public void render (Graphics g) {
			g.drawImage(bombSprite, x, y, null);
		}
		
		// set bomb sprite depending on enemy type
		public void setBombSprite (Enemy.TYPE type) {
			if (type == TYPE.SLIME) {
				bombSprite = tex.slimeAttack;
			} else if (type == TYPE.SPIDER) {
				bombSprite = tex.spiderAttack;
			} else if (type == TYPE.SKELETON) {
				bombSprite = tex.skeletonAttack;
			}
	    }
		
		// set bomb dropped to true
	    public void dropped () {
	    	dropped = true;
	    }
	    
	    // set bomb dropped to false
	    public void ready () {
	    	dropped = false;
	    }
		
		
		// Bomb getters and setters
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public void setX (int x) {
			this.x = x;
		}
		public void setY (int y) {
			this.y = y;
		}
		public int getH () {
			return h;
		}
		public boolean bombDropped() {
			return dropped;
		}
		
	} // end bomb class

}
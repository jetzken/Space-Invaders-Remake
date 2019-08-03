package assignment_JavaVersion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BackgroundGenerator {
	private Textures tex;
	private BufferedImage tile; // tile sprite
	private int x, y; // tile position
	
	public BackgroundGenerator (int x, int y, Textures tex, int type) {
		this.tex = tex;
		this.x = x;
		this.y = y;
		if (type == 0) { // if type is floor (0), then generate floor tile
			generateTile();
		} else if (type == 1){ // if type is left wall (1), so generate left wall tile
			generateWallL();
		} else { // else type is right wall (2), so generate right wall tile
			generateWallR();
		}
	}
	
	private void generateTile () {
		int randomNumber = (int) Math.ceil(Math.random()*100); // generate random number between 1 - 100
		// set tile depending on generated number
		if (randomNumber < 2) {
			tile = tex.water1Tile;
		} else if (randomNumber < 4) {
			tile = tex.water2Tile;
		} else if (randomNumber < 7) {
			tile = tex.lichenTile;
		} else if (randomNumber < 15) {
			tile = tex.crackedTile;
		} else if (randomNumber < 20) {
			tile = tex.mossTile;
		} else {
			tile = tex.mainTile;
		}
	}
	
	private void generateWallL () {
		int randomNumber = (int) Math.ceil(Math.random()*50); // generate random number between 1 - 50
		// set tile depending on generated number
		if (randomNumber < 10) {
			tile = tex.torchWallLeft;
		}
		else {
			tile = tex.mainWallLeft;
		}
	}
	
	private void generateWallR () {
		int randomNumber = (int) Math.ceil(Math.random()*50); // generate random number between 1 - 50
		// set tile depending on generated number
		if (randomNumber < 10) {
			tile = tex.torchWallRight;
		}
		else {
			tile = tex.mainWallRight;
		}
	}
	
	public void render (Graphics g) { // render tile
		g.drawImage(tile, x, y, null);
	}
	
}

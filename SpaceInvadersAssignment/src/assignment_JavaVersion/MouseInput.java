package assignment_JavaVersion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	private Game game;
	
	public MouseInput (Game game) {
		this.game = game;
	}
	
	public void mousePressed(MouseEvent e) {
		// run mousePressed method within the game
		game.mousePressed(e);		
	}
	
	public void mouseMoved(MouseEvent e) {
		// run mouseMoved method within the game
		game.mouseMoved(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

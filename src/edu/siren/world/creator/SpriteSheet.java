package edu.siren.world.creator;

import javax.swing.JFrame;

public class SpriteSheet extends JFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
		JFrame window = new SpriteSheet();
		window.setVisible(true);
		

	}
	
	public SpriteSheet(){
		setTitle("Juke Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 310);
		setResizable(false);
		
	}

	public SpriteSheet(WorldCreator worldCreator) {
		setTitle("Juke Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 310);
		setResizable(false);
		
		worldCreator.sprite = "res/tests/img/weeds.png";
	}

}

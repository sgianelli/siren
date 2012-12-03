package edu.siren.world.creator;

import java.awt.Canvas;
import java.io.IOException;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;

public class sampleJFrame extends JFrame{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws LWJGLException 
	 */
	public static void main(String[] args) throws LWJGLException, IOException {

			  new sampleJFrame();


	}
	
	public sampleJFrame() throws LWJGLException, IOException{
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(500, 500);
        setVisible(true);
        
			WorldCreator Creator = new WorldCreator();
			Creator.start();

        Creator.setTile("res/game/sprites/tiles/water-bottom.png");

	}

}

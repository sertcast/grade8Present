package flappyBird;

/**	Main.java
 * 
 * Description:	The flappy bird game with little more!!! Keeps your top five high scores 
 * 				with the dates which were accomplished. Also with a choice of birds!!!
 * 
 * @author M Kaya
 * @version 4.6 (Last updated: June 15, 2018)
 */

import javax.swing.JFrame;

public class Main {
	private static JFrame frame;
	private static Display disp;//the game that will be added to the frame
	private static final int FRAME_WIDTH = 500, FRAME_HEIGHT = 750;

	public static void main(String[] args) {
		disp = new Display(FRAME_WIDTH, FRAME_HEIGHT);
		frame = new JFrame("FLAPPY BIRD");//create the window with the title
		//frame setup
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(disp);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}

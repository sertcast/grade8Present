package flappyBird;

import java.awt.Graphics;
import java.awt.Image;

public class Bird {
	private int xPos, yPos;
	/**
	 * let x equal to fall
	 * y = -0.10(x+3) ^ 2 + 10
	 */
	double fall;
	Image img;
	/**
	 * @param img the image of the bird
	 * @param x the x position of the bird
	 * @param y the y position of the bird
	 */
	public Bird(Image img, int x, int y) {
		this.xPos = x;
		this.yPos = y;
		this.img = img;
		fall = 0;
		
	}
	/**
	 * draws the bird to the screen
	 * @param g the graphics panel of the program
	 */
	public void display(Graphics g) {
		g.drawImage(img, xPos, yPos, null);
	}
	/**
	 * changes the y position of the bird in terms of a quadradic equation
	 */
	public void update() {
		int add = (int)Math.round((-0.10*Math.pow((fall+3), 2) + 10)); 
		if(add < -8) {
			this.yPos += 8;
		}else {
			this.yPos -= add;
		}
		fall+=0.3;
	}
	public void clicked() {
		fall = 0;
	}
	/***********getters and setters*************/
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
	public Image getImg() {
		return img;
	}
}

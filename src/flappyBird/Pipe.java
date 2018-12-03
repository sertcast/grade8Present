package flappyBird;

import java.awt.Color;
import java.awt.Graphics;

public class Pipe {
	private int xPos, w, h, spaceY, spaceW, retX;
	private boolean birdPassed = false; //used to make sure that the score increases only by one
	/** 
	 * 
	 * @param xPos the starting x position of the pipe
	 * @param w the width of the pipe
	 * @param h the height of the pipe
	 * @param spaceY the y position of the gap
	 * @param spaceW the the width of the gap
	 * @param retX the returning x position of the pipe after it travels out of the screen
	 */
	public Pipe(int xPos, int w, int h, int spaceY, int spaceW,int retX) {
		this.xPos = xPos;
		this.w = w;
		this.h = h;
		this.spaceY = spaceY;
		this.spaceW = spaceW;
		this.retX = retX;
	}
	/**
	 * draws the pipe to the frame
	 * @param g the graphics panel of the program
	 */
	public void display(Graphics g) {
		g.setColor(new Color(27, 163, 48));
		g.fillRect(this.xPos, 0, this.w, this.spaceY);
		g.fillRect(xPos, this.spaceW + this.spaceY, this.w, this.h - this.spaceW - this.spaceY);
	}
	/**
	 * checks if the bird colliding with the pipe
	 * @param b the bird
	 * @return true: bird is colliding with the pipe
	 */
	public boolean isColliding(Bird b) {
		//checks if the bird between the gap in terms of x position
		boolean bXin = b.getxPos() + b.getImg().getWidth(null) > this.xPos && b.getxPos()< this.xPos + w;
		//checks if the bird is not in the between the gap in terms of y position
		boolean bYin = b.getyPos() > spaceY && b.getyPos()+ b.getImg().getHeight(null) < spaceY + spaceW; 
		if(bYin ||!bXin) {
			return false;
		}
		return true;
	}
	/**
	 * moves the pipe to the left with given speed
	 * @param speed the speed of the pipe
	 */
	public void update(int speed) {
		this.xPos -= speed;
	}
	/****** getters and setters ****/
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getW() {
		return w;
	}
	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}
	public void setBirdPassed(boolean v) {
		this.birdPassed = v;
	}
	public boolean getBirdPassed() {
		return this.birdPassed;
	}
	public void returnX() {
		this.xPos = this.retX;
	}
	public int getRetX() {
		return this.retX;
	}
	public void setRetX(int retX) {
		this.retX = retX;
	}
}

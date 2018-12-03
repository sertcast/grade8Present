package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Button {

	private int xPos, yPos,	//positions of the rectangle referenced from top left corner 
				width, height, fontSize, roundSize, 
				txtXpos, txtYpos, // positions of the text that will be displayed referenced from left bottom corner
				x ,y;	//positions of the button referenced from the middle of the button
	private String txt;
	private Color colour, txtColour;
	private Font font;
	private boolean mouseIn = false;
	private String job;
	private JPanel panel;
	/**
	 * 
	 * @param panel the panel where the buttons is going to be displayed
	 * @param txt the text that is displayed on the button
	 * @param x the x position of the button referenced from the middle 
	 * @param y the y position of the button referenced from the middle
	 * @param fontSize the font size of the text
	 * @param job the string that it is suppose to return(something other than "")
	 */
	//
	public Button(JPanel panel, String txt, int x, int y, int fontSize, String job) {
		this.colour = new Color(255, 200, 0);
		this.txtColour = Color.WHITE;
		this.panel = panel;
		
		this.x = x;
		this.y = y;
		this.txt = txt;
		this.job = job;
		this.fontSize = fontSize;
		this.font = new Font("Comic Sans MS", Font.PLAIN, this.fontSize);
		
		this.width = panel.getFontMetrics(this.font).stringWidth(txt);
		this.height = panel.getFontMetrics(this.font).getHeight();
		this.roundSize = this.width / 3;
		
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.yPos = y - this.height / 2;
		
		this.txtXpos = x - this.width / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();
	}
	/**
	 * draws the button to the frame
	 * @param g the graphics panel of the program
	 */
	public void display(Graphics g) {
		
		g.setFont(this.font);
		g.setColor(colour);
		g.fillRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);

		g.setColor(txtColour);
		g.drawString(txt, txtXpos, txtYpos);
		//if mouse is on, draw the outline
		if(this.mouseIn) {
			g.setColor(Color.blue);
			g.drawRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);		
		}
	}
	/**
	 * checks if the mouse is on
	 */
	public void mouseOver(MouseEvent e) {
		boolean isMouseXIn = e.getX() > this.x  - (this.width / 2 + this.roundSize / 2)&& e.getX() < this.x + (this.width / 2 + this.roundSize / 2);
		boolean isMouseYIn = e.getY() > this.y - this.height / 2 && e.getY() < this.y + this.height / 2;
		if(isMouseXIn && isMouseYIn) {
			this.mouseIn = true;
		}else {
			this.mouseIn = false;
		}
	}
	/**
	 * does its job if the mouse is clicked on the button
	 * @return if mouse is in, its job, if not, empty string
	 */
	public String clicked(MouseEvent e){
		mouseOver(e);
		if(mouseIn)	{
			mouseIn = false;
			return this.job;
		}
		return "";
	}
	/******* setters and getters *********/
	public void setxPos(int x) {
		this.x = x;
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.txtXpos = x - this.width / 2;	
	}
	public void setyPos(int y) {
		this.y = y;
		this.yPos = y - this.height / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();	
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.yPos = y - this.height / 2;
		this.txtXpos = x - this.width / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();
	}
	
	
}
package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Display extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

    private Timer tm = new Timer(10, this);
    private Bird flappy;
    private Pipe pipes[];
    private Image ground[], background[];
    private int retGroundX, retBackgroundX, randSpaceY, groundX[], backgroundX[];
    private int speed = 3;
    private int score = 0, prevScore = 0, highScore = 0;
    private int frameW, frameH;
    private boolean gameOver = false, isHighScore = false;
    private Font font;
    private String scene = "menu";
    private int scoreX, scoreY;
    private Color clickedColor = Color.white;

    private Button bPlayGame, bInstructions, bMainMenu,
            bTryAgain, bHighScores, bClear;
    private BufferedWriter writer;
    private BufferedReader reader;

    /********************************************************************************/
    public Display(int frameW, int frameH) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.frameW = frameW;
        this.frameH = frameH;
        this.bPlayGame = new Button(this, "Play Game", frameW / 2, frameH / 2 + 50, 35, "gameplay");//change it to skin
        this.bInstructions = new Button(this, "Instructions", frameW / 2, frameH / 2 + 174, 35, "instructions");
        this.bMainMenu = new Button(this, "Main Menu", frameW / 2, frameH / 2 + 112, 35, "menu");
        this.bTryAgain = new Button(this, "Try Again", frameW / 2, frameH / 2 + 50, 35, "false");
        this.bHighScores = new Button(this, "High Scores", frameW / 2, frameH / 2 + 112, 35, "high scores");
        this.bClear = new Button(this, "Clear", frameW / 2, frameH - 78, 35, "clear");
        this.init();
        this.updateHighScore();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scene.equals("menu")) {
            mainMenu(g);
        } else if (scene.equals("instructions")) {
            instructions(g);
        } else if (scene.equals("gameplay")) {
            gamePlay(g);
        } else if (scene.equals("high scores")) {
            highScores(g);
        }

        tm.start();
    }

    /*********	SCENES	********/
    /**
     * the output of main menu
     *
     * @param g the graphics panel of the program
     */
    public void mainMenu(Graphics g) {
        font = new Font("Comic Sans MS", Font.BOLD, 70);
        g.setFont(font);
        g.setColor(Color.WHITE);
        final int BIRD_XPOS = frameW / 2 - this.getFontMetrics(font).stringWidth("FLAPPY BIRD") / 2;
        final int BIRD_YPOS = 267;

        background(g);
        flappy.display(g);
        g.drawString("FLAPPY BIRD", BIRD_XPOS, BIRD_YPOS);
        bPlayGame.display(g);
        bInstructions.display(g);
        bHighScores.display(g);
    }

    /**
     * where the gameplay processing and output occures
     *
     * @param g the graphics panel of the program
     */
    public void gamePlay(Graphics g) {
        //PROCESSING........................................................................................................................
        //increases the speed every 6 points
        flappy.setxPos(100);
        int gameOverX, gameOverY;
        if (!gameOver) {
            if (score % 5 == 0 && score != prevScore) {
                speed++;
                prevScore = score;
            }
            for (int i = 0; i < pipes.length; i++) {
                /*
                 * when pipe travels beyond the screen, makes it return to its "return position" and
                 * randomizes the y position of the space
                 */
                if (pipes[i].getxPos() + pipes[i].getW() < 0) {
                    this.randSpaceY = (int) (Math.random() * 300) + 125;
                    pipes[i].returnX();
                    pipes[i].setSpaceY(randSpaceY);
                    pipes[i].setBirdPassed(false);
                }
                //increments the score if the bird successfully passed the pipe
                if (!pipes[i].getBirdPassed() && flappy.getxPos() > pipes[i].getxPos()) {
                    score++;
                    pipes[i].setBirdPassed(true);
                }
                if (!gameOver) gameOver = pipes[i].isColliding(flappy); //checks if the bird is colliding with the pipe
                pipes[i].update(speed);
            }
            //the animation of the ground moving with pipes
            for (int i = 0; i < ground.length; i++) {
                groundX[i] -= speed;
                if (groundX[i] + ground[i].getWidth(null) <= 0) {
                    groundX[i] = retGroundX + groundX[i] + ground[i].getWidth(null);
                }
            }
            //the animation of the background(sunset) moving with pipes
            for (int i = 0; i < background.length; i++) {
                backgroundX[i] -= speed;
                if (backgroundX[i] + background[i].getWidth(null) <= 0) {
                    backgroundX[i] = retBackgroundX + backgroundX[i] + background[i].getWidth(null);
                }
            }
            //sets the x and y positions of the score to the right top of the frame
            font = new Font("Comic Sans MS", Font.BOLD, 70);
            scoreX = frameW - this.getFontMetrics(font).stringWidth("" + score) - this.getFontMetrics(font).getDescent();
            scoreY = this.getFontMetrics(font).getAscent();

            //checks if the bird is touching the ground
            if (!gameOver)
                gameOver = flappy.getyPos() + flappy.getImg().getHeight(null) >= frameH - ground[0].getHeight(null);
        } else {
            //sets the x and y positions of the score to the right top of the frame
            font = new Font("Comic Sans MS", Font.BOLD, 70);
            scoreX = frameW / 2 - this.getFontMetrics(font).stringWidth("" + score) / 2;
            scoreY = frameH / 2 - this.getFontMetrics(font).getHeight() / 2 + 20;
            if (score > highScore) {
                highScore = score;
                isHighScore = true;
                this.addHighScore(highScore);
            }
        }
        //OUTPUT........................................................................................................................
        //draws the sunset background images
        for (int i = 0; i < background.length; i++) {
            g.drawImage(background[i], backgroundX[i], 0, null);
        }
        //draws the bird
        flappy.display(g);
        //draws the pipes
        for (int i = 0; i < pipes.length; i++) {
            pipes[i].display(g);
        }
        //draws the ground images
        for (int i = 0; i < ground.length; i++) {
            g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
        }
        //set the color of the score to yellow if it is greater than high score
        if (!isHighScore && score <= highScore)
            g.setColor(new Color(0, 0, 0, 100));
        else
            g.setColor(new Color(255, 255, 0, 100));
        //draws the score
        font = new Font("Comic Sans MS", Font.BOLD, 70);
        g.setFont(font);
        g.drawString("" + score, scoreX, scoreY);

        tm.start();//to start the timer

        if (gameOver) {
            font = new Font("Comic Sans MS", Font.BOLD, 70);
            g.setFont(font);
            g.setColor(Color.white);

            gameOverX = frameW / 2 - this.getFontMetrics(font).stringWidth("GAME OVER") / 2;
            gameOverY = frameH / 2 - this.getFontMetrics(font).getHeight() / 2 - 75;

            g.drawString("GAME OVER", gameOverX, gameOverY);
            this.bMainMenu.setPos(frameW / 2, frameH / 2 + 112);
            this.bMainMenu.display(g);
            this.bTryAgain.display(g);
        } else flappy.update();//	updates the birds position until game is over

    }

    /**
     * where the output of the high scores scene occurs
     *
     * @param g the graphics panel of the program
     */
    public void highScores(Graphics g) {
        final int TITLE_XPOS = this.frameW / 2;
        final int TITLE_YPOS = 100;
        final int TITLE_FONT_SIZE = 65;
        background(g);

        //makes the background darker
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, frameW, frameH);

        this.drawTextMiddle(g, "HIGH SCORES", TITLE_XPOS, TITLE_YPOS, TITLE_FONT_SIZE, new Color(255, 100, 100));
        this.displayHighScores(g);
        bMainMenu.setPos(frameW / 2, frameH - 140);
        bMainMenu.display(g);
        bClear.display(g);
    }

    /**
     * where the output of the instructions scene occurs
     *
     * @param g the graphics panel of the program
     */
    public void instructions(Graphics g) {
        background(g);
        //makes the background darker
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, frameW, frameH);
        final String[] instruct = {
                "Try to avoid touching to the",
                "pipes and the ground",
                "The pipes gets faster every",
                "5 scores!!!",
                "Click to jump"
        };

        this.drawTextMiddle(g, "INSTRUCTIONS", this.frameW / 2, 100, 58, new Color(255, 100, 100));
        int i;
        for (i = 0; i < instruct.length; i++) {
            this.drawTextMiddle(g, instruct[i], this.frameW / 2, 200 + 40 * i, 30, new Color(255, 255, 255));
        }
        this.drawTextMiddle(g, "test your mouse!", this.frameW / 2, 325 + 40 * (i - 2), 58, Color.white);
        this.drawTextMiddle(g, "CLICKED", this.frameW / 2, 325 + 40 * i, 58, this.clickedColor);
        bMainMenu.setPos(frameW / 2, frameH - 140);
        bMainMenu.display(g);
    }
    /********	FUNCTIONAL METHODS	*****/

    /**
     * reinitializes all the variables and objects that are used in gameplay
     */
    public void init() {
        gameOver = false;
        isHighScore = false;
        speed = 3;
        score = 0;
        prevScore = 0;
        Image birdImage = new ImageIcon("Grade8s/src/flappyBird/images/flappyBird.png").getImage();
        Image groundImage = new ImageIcon("Grade8s/src/flappyBird/images/ground.png").getImage();
        Image backgroundImage = new ImageIcon("Grade8s/src/flappyBird/images/background.png").getImage();
        flappy = new Bird(birdImage, frameW / 2 - birdImage.getWidth(null) / 2, frameH / 2 - 50);
        pipes = new Pipe[3];
        ground = new Image[3];
        groundX = new int[3];
        background = new Image[3];
        backgroundX = new int[3];

        int i; //defined outside so could use the ending value

        //initializes the images of the ground array
        for (i = 0; i < ground.length; i++) {
            ground[i] = groundImage;
            groundX[i] = i * ground[i].getWidth(null);
        }
        retGroundX = (i - 1) * ground[i - 1].getWidth(null);

        //initializes the images of the background array
        for (i = 0; i < background.length; i++) {
            background[i] = backgroundImage;
            backgroundX[i] = i * background[i].getWidth(null);
        }
        retBackgroundX = (i - 1) * background[i - 1].getWidth(null);


        for (i = 0; i < pipes.length; i++) {
            this.randSpaceY = (int) (Math.random() * 250) + 125;
            pipes[i] = new Pipe(800 + i * 300, 50, this.frameH, this.randSpaceY, 200, 300 * pipes.length - 50);
        }
    }

    /**
     * clears the file that stores the high scores
     */
    public void clearFile() {
        try {
            writer = new BufferedWriter(new FileWriter("Grade8s/src/flappyBird/high_scores.txt", false));
            writer.close();
        } catch (IOException e) {
            System.out.println("file not found");
        }
    }

    /**
     * appends the current high score to the high scores value
     *
     * @param score
     */
    public void addHighScore(int score) {
        addHighScore(score, true);
    }

    /**
     * adds high score to the file with the current date
     *
     * @param score      the high score that will be added
     * @param appendFile the decision about will the file be erased or not
     */
    public void addHighScore(int score, boolean appendFile) {

        String currentTime = "";
        int time;
        time = LocalTime.now().getHour();
        currentTime += (time < 10) ? "0" + time + ":" : time+ ":";
        time = LocalTime.now().getMinute();
        currentTime += (time < 10) ? "0" + time+ ":": time + ":";
        time = LocalTime.now().getSecond();
        currentTime += (time < 10) ? "0" + time : time;
        try {
            writer = new BufferedWriter(new FileWriter("Grade8s/src/flappyBird/high_scores.txt", appendFile));
            writer.append(currentTime + '\t' + score);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("file not found");
        }
    }

    /**
     * updates the current high score from the high scores file
     */
    public void updateHighScore() {
        String in = "";
        int a = 0;
        try {
            reader = new BufferedReader(new FileReader("Grade8s/src/flappyBird/high_scores.txt"));
            in = reader.readLine();
            while (in != null && !in.equals("")) {
                for (int i = 0; i < in.length(); i++) {
                    if (in.charAt(i) == '\t') {
                        highScore = Integer.parseInt(in.substring(i + 1, in.length()));
                    }
                }
                in = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("file not found");
            this.clearFile();
        }
    }

    /**
     * @param str the string
     * @return the two parts of the string divided with the tab character, which is included in either
     */
    public String[] untilTab(String str) {
        String[] ret = null;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\t') {
                ret = new String[2];
                ret[0] = str.substring(0, i);
                ret[1] = str.substring(i + 1, str.length());
                return ret;
            }
        }
        return null;
    }
    /*********	METHODS THAT DISPLAY VISUAL	******/
    /**
     * draws all the high scores to the frame
     *
     * @param g the graphics panel of the program
     */
    public void displayHighScores(Graphics g) {
        String in = "", date = "", scoreO = "", display[] = null;
        int i = 0;
        int counter = 0;
        try {
            //determines how many high scores there are in the file
            reader = new BufferedReader(new FileReader("Grade8s/src/flappyBird/high_scores.txt"));
            in = reader.readLine();
            while (in != null && !in.equals("")) {
                counter++;
                in = reader.readLine();
            }
            reader.close();
            display = new String[counter];

            reader = new BufferedReader(new FileReader("Grade8s/src/flappyBird/high_scores.txt"));
            in = reader.readLine();
            while (in != null && !in.equals("")) {
                date = this.untilTab(in)[0];
                scoreO = this.untilTab(in)[1];
                display[i] = "score: " + scoreO + "     " + date;
                i++;
                in = reader.readLine();
            }
            counter = 1;
            for (i = display.length - 1; i >= 0; i--) {
                if (counter < 5) this.drawTextMiddle(g, display[i], frameW / 2, counter * 40 + 140, 30, Color.white);
                counter++;
            }
        } catch (IOException e) {
            System.out.println("file not found");
        }
    }

    /**
     * draws string referenced from the middle of the text
     *
     * @param g        the graphics panel of the program
     * @param txt      the text that will be displayed
     * @param x        x position of the text referenced from middle
     * @param y        y position of the text referenced from middle
     * @param fontSize the font size
     * @param color    color of the text
     */
    public void drawTextMiddle(Graphics g, String txt, int x, int y, int fontSize, Color color) {
        int xPos, yPos;
        font = new Font("Comic Sans MS", Font.BOLD, fontSize);
        g.setFont(font);
        g.setColor(color);
        xPos = x - this.getFontMetrics(font).stringWidth(txt) / 2;
        yPos = y + this.getFontMetrics(font).getHeight() / 2;
        g.drawString(txt, xPos, yPos);
    }

    /**
     * draws string referenced from left bottom of the text
     *
     * @param g        the graphics panel of the program
     * @param txt      the text that will be displayed
     * @param x        x position of the text referenced from left bottom
     * @param y        y position of the text referenced from left bottom
     * @param fontSize the font size
     * @param color    color of the text
     */
    public void drawText(Graphics g, String txt, int x, int y, int fontSize, Color color) {
        font = new Font("Comic Sans MS", Font.BOLD, fontSize);
        g.setFont(font);
        g.setColor(color);
        g.drawString(txt, x, y);
    }

    /**
     * draws the grass and background images
     *
     * @param g the graphics panel of the program
     */
    public void background(Graphics g) {
        for (int i = 0; i < background.length; i++) {
            g.drawImage(background[i], backgroundX[i], 0, null);
        }
        for (int i = 0; i < ground.length; i++) {
            g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
        }
    }

    public void actionPerformed(ActionEvent event) {
        repaint();
    }

    /********MOUSEMOTIONLISTENER*******/
    /**
     * looks if the mouse is on a button every time the mouse is moved
     */
    public void mouseMoved(MouseEvent e) {
        if (scene.equals("menu")) {
            this.bPlayGame.mouseOver(e);
            this.bInstructions.mouseOver(e);
            this.bHighScores.mouseOver(e);
        } else if (scene.equals("instructions")) {
            this.bMainMenu.mouseOver(e);
        } else if (scene.equals("gameplay")) {
            this.bTryAgain.mouseOver(e);
            this.bMainMenu.mouseOver(e);
        } else if (scene.equals("high scores")) {
            this.bMainMenu.mouseOver(e);
            this.bClear.mouseOver(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    /********MOUSELISTENER*******/
    public void mousePressed(MouseEvent e) {
        String check; //used to reduce usement of memory
        if (scene.equals("menu")) {
            check = bPlayGame.clicked(e);
            if (!check.equals("")) //if mouse is clicked do the job of the button
                scene = check;

            check = bInstructions.clicked(e);//if mouse is clicked do the job of the button
            if (!check.equals("")) scene = check;

            check = bHighScores.clicked(e);//if mouse is clicked do the job of the button
            if (!check.equals("")) scene = check;
        } else if (scene.equals("instructions")) {
            check = bMainMenu.clicked(e);//if mouse is clicked do the job of the button
            if (!check.equals("")) scene = check;
            clickedColor = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        } else if (scene.equals("gameplay")) {
            flappy.clicked();

            if (gameOver) {
                check = bTryAgain.clicked(e);//if mouse is clicked do the job of the button
                if (!check.equals("")) init();

                check = bMainMenu.clicked(e);//if mouse is clicked do the job of the button
                if (!check.equals("")) {
                    init();
                    scene = check;
                }
            }
        } else if (scene.equals("high scores")) {
            check = bMainMenu.clicked(e);
            if (!check.equals("")) scene = check;
            check = bClear.clicked(e);
            if (!check.equals("")) {
                this.clearFile();
                this.highScore = 0;
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    /********KEYLISTENER*******/
    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}

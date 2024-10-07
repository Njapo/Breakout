
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutExtension extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 500;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 15;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 13;

	/** Paddle */
	private static GRect paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);

	/** Random generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/** Moving through X */
	// private static int VEL_X = 0;

	/** Moving through Y */
	private static final int VEL_Y = 5;

	/** PAUSE program with this millisecond */
	private static final int PAUSE = 10;

	public static final int INTRO_RECT_WIDTH = 200;

	public static final int INTRO_RECT_HEIGHT = 50;

	public static final int INTRO_RECT_SEP = 10;

	public static GRect startPlayRect;

	public static GLabel startPlayLabel;

	public static GRect instructionsRect;

	public static GLabel instructionsLabel;

	public static GRect introWindow;

	public static GRect scoreRect;

	public static int score = 0;

	public static GLabel scoreLabel;

	public static double time = 0;

	public static GRect bonusRect;

	public static GLabel bonusLabel;

	public static boolean catchedBonus = false;

	public static boolean checkBonus = false;

	public static GRect rulesRect;

	public static GLabel rulesLabels;

	public static GRect goHomeRect;

	public static GLabel goHomeLabel;

	public static GRect[][] BRICKS = new GRect[NBRICK_ROWS][NBRICKS_PER_ROW];

	public static boolean CheckerOfBallAnimations = false;

	public static int Lives = NTURNS;

	public static GRect livesRect;

	public static GLabel livesLabel;

	public Thread threadPlayGame;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {

		addMouseListeners();
		gameIntro();

	}

	private void setUpGame() {
		setAllBricks();
		setPaddle();
		scoreCounter();
		LivesCounter();
	}

	private void scoreCounter() {
		scoreRect = new GRect(40, 40);
		add(scoreRect, 10, 10);
		scoreRect.setFilled(true);
		scoreRect.setFillColor(Color.pink);
		scoreLabel = new GLabel("" + score);
		add(scoreLabel, scoreRect.getX() + scoreRect.getWidth() / 2 - scoreLabel.getWidth() / 2,
				scoreRect.getY() + scoreRect.getHeight() / 2 + scoreLabel.getHeight() / 2 - 2);
	}

	private void LivesCounter() {
		livesRect = new GRect(80, 40);
		add(livesRect, WIDTH - 110, 10);
		livesRect.setFilled(true);
		livesRect.setFillColor(Color.pink);

		livesLabel = new GLabel("LIVES: " + Lives);
		add(livesLabel, livesRect.getX() + livesRect.getWidth() / 2 - livesLabel.getWidth() / 2,
				livesRect.getY() + livesRect.getHeight() / 2 + livesLabel.getHeight() / 2 - 2);
	}

	private void rules() {
		removeAll();

		GRect rulesWindow = new GRect(WIDTH, HEIGHT);
		rulesWindow.setFilled(true);
		rulesWindow.setFillColor(Color.red);
		add(rulesWindow);
		rulesWindow.sendToBack();

		goHomeRect = new GRect(80, 40);
		add(goHomeRect, 30, 30);
		goHomeRect.setFilled(true);
		goHomeRect.setFillColor(Color.PINK);
		goHomeLabel = new GLabel("GO HOME");
		add(goHomeLabel, goHomeRect.getX() + goHomeRect.getWidth() / 2 - goHomeLabel.getWidth() / 2,
				goHomeRect.getY() + goHomeRect.getHeight() / 2 + goHomeLabel.getHeight() / 2);

		double startY = getHeight() / 4;

		GRect rulesBigRect = new GRect(450, 180);
		rulesBigRect.setFilled(true);
		rulesBigRect.setFillColor(Color.orange);
		add(rulesBigRect, 43, startY - 5);
		rulesBigRect.sendBackward();

		for (int i = 0; i < 5; i++) {
			GOval circle = new GOval(30, 30);
			circle.setFilled(true);
			circle.setFillColor(Color.gray);
			add(circle, 50, startY);
			startY += 35;
			GLabel plus = new GLabel("+");
			add(plus, circle.getX() + circle.getWidth() / 2 - plus.getWidth() / 2,
					circle.getY() + circle.getHeight() / 2 + plus.getHeight() / 2);

			GLabel text = new GLabel(rulesLabels(i));
			add(text, circle.getX() + 36, startY - 15);
		}
	}

	private String rulesLabels(int i) {
		if (i == 0) {
			return "This is game BREAKOUT, To win this game You have to break All Bricks";
		}
		if (i == 1) {
			return "Use the paddle to bounce the ball and direct it toward the bricks";
		}
		if (i == 2) {
			return "You earn points for each brick you break";
		}
		if (i == 3) {
			return "Try to hit the ball at corner points it will help you tryst me :)";
		}
		if (i == 4) {
			return "You can try " + NTURNS + " times";
		} else {
			return null;
		}
	}

	private void resultMessage(int totalBricks) {
		if (totalBricks > 0) {

			GRect gameResultRect = new GRect(INTRO_RECT_WIDTH, INTRO_RECT_HEIGHT);
			gameResultRect.setFilled(true);
			gameResultRect.setFillColor(Color.gray);
			add(gameResultRect, APPLICATION_WIDTH / 2 - INTRO_RECT_WIDTH / 2,
					(APPLICATION_HEIGHT - 2 * INTRO_RECT_HEIGHT - INTRO_RECT_SEP) / 2);
			startPlayRect = gameResultRect;

			GLabel gameResultRectLabel = new GLabel("YOU LOST");
			add(gameResultRectLabel,
					gameResultRect.getX() + gameResultRect.getWidth() / 2 - gameResultRectLabel.getWidth() / 2,
					gameResultRect.getY() + gameResultRect.getHeight() / 2 + gameResultRectLabel.getHeight() / 2);


		} else if (totalBricks == 0) {
			GRect gameResultRect = new GRect(INTRO_RECT_WIDTH, INTRO_RECT_HEIGHT);
			gameResultRect.setFilled(true);
			gameResultRect.setFillColor(Color.blue);
			add(gameResultRect, APPLICATION_WIDTH / 2 - INTRO_RECT_WIDTH / 2,
					(APPLICATION_HEIGHT - 2 * INTRO_RECT_HEIGHT - INTRO_RECT_SEP) / 2);
			startPlayRect = gameResultRect;

			GLabel gameResultRectLabel = new GLabel("YOU WIN");
			add(gameResultRectLabel,
					gameResultRect.getX() + gameResultRect.getWidth() / 2 - gameResultRectLabel.getWidth() / 2,
					gameResultRect.getY() + gameResultRect.getHeight() / 2 + gameResultRectLabel.getHeight() / 2);
			
		}

	}
	

	private void updateScoreAndLives() {
		scoreLabel.setLabel("" + score);
		livesLabel.setLabel("LIVES: " + Lives);
	}

	private void PlayGame() {
		pause(1000);
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
		int totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
		int nBalls = NTURNS;
		while (nBalls > 0) {
			int vY = VEL_Y;
			double vX = rgen.nextDouble(3.0, 4.0);
			if (rgen.nextBoolean(0.5)) {
				vX = -vX;
			}
			GOval ball = initialBall();
			nBalls--;
			while (true) {

				ball.move(vX, vY);
				pause(PAUSE);
				time += PAUSE;
				if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * BALL_RADIUS) {
					vX = -vX;
					bounceClip.play();
				}
				if (ball.getY() <= 0 || ball.getY() >= getHeight() - 2 * BALL_RADIUS) {
					vY = -vY;
					bounceClip.play();
				}
				if (ball.getY() >= getHeight() - 2 * BALL_RADIUS) {

					remove(ball);
					Lives--;
					updateScoreAndLives();
					pause(900);

					break;
				}
				GObject rect = getCollidingObject(ball);
				if (rect != null && rect != paddle && rect.getWidth() == BRICK_WIDTH && rect != scoreRect
						&& rect != scoreLabel && rect!=livesRect && rect!=livesLabel) {
					ball.setColor(rect.getColor());
					bounceClip.play();
					new Thread(() -> {
						brickAnimation((GRect) rect);
						try {
							Thread.sleep(1);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}).start();
					score++;
					updateScoreAndLives();
					remove(rect);
					totalBricks--;
					vY = -vY;
					if (totalBricks == 0) {
						break;
					}
				}
				if (rect == paddle) {
					paddle.setColor(ball.getColor());
					if (ball.getY() + BALL_RADIUS < paddle.getY() + PADDLE_HEIGHT) {

						vY = -module(vY);
						bounceClip.play();

					}

				}
				if (totalBricks == 0) {
					break;
				}
			}

		}
		score = 0;
		Lives = NTURNS;
		resultMessage(totalBricks);
	}

	private GObject getCollidingObject(GOval ball) {

		GObject rect1 = getElementAt(ball.getX()+0.25*BALL_RADIUS, ball.getY()+0.25*BALL_RADIUS);
		if (rect1 != null) {
			return rect1;
		}

		GObject rect2 = getElementAt(ball.getX()+0.25*BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS-0.25*BALL_RADIUS);
		if (rect2 != null) {
			return rect2;
		}

		GObject rect3 = getElementAt(ball.getX() + 2 * BALL_RADIUS-0.25*BALL_RADIUS, ball.getY()+0.25*BALL_RADIUS);
		if (rect3 != null) {
			return rect3;
		}

		GObject rect4 = getElementAt(ball.getX() + 2 * BALL_RADIUS-0.25*BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS-0.25*BALL_RADIUS);
		if (rect4 != null) {
			return rect4;
		}
		GObject rect5 = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY());
		if (rect5 != null) {
			return rect5;
		}
		GObject rect6 = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2 * BALL_RADIUS + 1);
		if (rect6 != null) {
			return rect6;
		}
		GObject rect7 = getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS);
		if (rect7 != null) {
			return rect7;
		}
		GObject rect8 = getElementAt(ball.getX() + 2 * BALL_RADIUS + 1, ball.getY() + BALL_RADIUS);
		if (rect8 != null) {
			return rect8;
		}

		else {
			return null;
		}
	}

	// This func writes module of a number
	private int module(int x) {
		if (x >= 0) {
			return x;
		} else {
			return -x;
		}
	}

	// This func creates another ball
	private GOval initialBall() {
		GOval ball = new GOval(2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.GRAY);
		add(ball, getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS);
		return ball;
	}

	// This function moves paddle as mouse is moving
	public void mouseMoved(MouseEvent e) {
		if (e.getX() >= PADDLE_WIDTH / 2 && e.getX() <= getWidth() - PADDLE_WIDTH / 2) {
			paddle.setLocation(e.getX() - paddle.getWidth() / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		}

	}

	// This func sets paddle at the start position
	private void setPaddle() {
		paddle.setFilled(true);
		paddle.setColor(Color.black);
		add(paddle, getWidth() / 2 - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}

	// This func draws all the bricks on the aplication
	private void setAllBricks() {

		int rectX = (APPLICATION_WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;
		int rectY = BRICK_Y_OFFSET;
		for (int i = 1; i <= NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				GRect rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				add(rect, rectX, rectY);
				rect.setFilled(true);
				rect.setColor(setBrickColor(i));
				BRICKS[j][i - 1] = rect;
				rectX += (BRICK_WIDTH + BRICK_SEP);
			}
			rectX = (APPLICATION_WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;
			rectY += (BRICK_SEP + BRICK_HEIGHT);
		}
	}

	// This function sets colors for each row of bricks
	private Color setBrickColor(int i) {
		for (int j = 1; j <= NBRICK_ROWS; j++) {
			if (j == i && (i / 2 + i % 2) % 5 == 1) {
				return Color.RED;
			}
			if (j == i && (i / 2 + i % 2) % 5 == 2) {
				return Color.orange;
			}
			if (j == i && (i / 2 + i % 2) % 5 == 3) {
				return Color.yellow;
			}
			if (j == i && (i / 2 + i % 2) % 5 == 4) {
				return Color.green;
			}
			if (j == i && (i / 2 + i % 2) % 5 == 5) {
				return Color.blue;
			}
		}
		return null;
	}

	private void gameIntro() {

		GRect window = new GRect(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		window.setFilled(true);
		window.setFillColor(Color.red);
		introWindow = window;
		add(window);
		buttons();
		ballAnimations();
	}

	private void buttons() {
		GRect startPlay = new GRect(INTRO_RECT_WIDTH, INTRO_RECT_HEIGHT);
		startPlay.setFilled(true);
		startPlay.setFillColor(Color.blue);
		add(startPlay, APPLICATION_WIDTH / 2 - INTRO_RECT_WIDTH / 2,
				(APPLICATION_HEIGHT - 2 * INTRO_RECT_HEIGHT - INTRO_RECT_SEP) / 2);
		startPlayRect = startPlay;

		GLabel starPlayLabel = new GLabel("START GAME");
		add(starPlayLabel, startPlay.getX() + startPlay.getWidth() / 2 - starPlayLabel.getWidth() / 2,
				startPlay.getY() + startPlay.getHeight() / 2 + starPlayLabel.getHeight() / 2);
		this.startPlayLabel = starPlayLabel;

		GRect instructions = new GRect(INTRO_RECT_WIDTH, INTRO_RECT_HEIGHT);
		instructions.setFilled(true);
		instructions.setFillColor(Color.blue);
		add(instructions, APPLICATION_WIDTH / 2 - INTRO_RECT_WIDTH / 2,
				(APPLICATION_HEIGHT - 2 * INTRO_RECT_HEIGHT - INTRO_RECT_SEP) / 2 + INTRO_RECT_HEIGHT + INTRO_RECT_SEP);
		instructionsRect = instructions;

		GLabel instructionsLabel = new GLabel("READ THE RULES");
		add(instructionsLabel, instructions.getX() + instructions.getWidth() / 2 - instructionsLabel.getWidth() / 2,
				instructions.getY() + instructions.getHeight() / 2 + instructionsLabel.getHeight() / 2);
		this.instructionsLabel = instructionsLabel;
	
	}

	public void mouseClicked(MouseEvent e) {

		GObject obj = getElementAt(e.getX(), e.getY());
		if (obj == instructionsLabel || obj == instructionsRect) {
			
			removeAll();
			rules();
			// ballAnimations();

		}

		if (obj == goHomeRect || obj == goHomeLabel) {

			removeAll();
			new Thread(() -> {
				gameIntro();
				try {
					Thread.sleep(4000);
				} catch (InterruptedException ev) {
					ev.printStackTrace();
				}

			}).start();

		}

		if (obj == startPlayLabel || obj == startPlayRect) {

			removeAll();
			new Thread(() -> {
				setUpGame();
				addMouseListeners();
				PlayGame();
				try {
					Thread.sleep(1);
				} catch (InterruptedException ev) {
					ev.printStackTrace();
				}

			}).start();
		}

	}

	private void brickAnimation(GRect rect) {
		double litBrickHeight = rect.getHeight() / 4;
		double litBrickWidth = rect.getWidth() / 10;
		double startX = rect.getX();
		double startY = rect.getY();

		GRect[][] arr = new GRect[4][10];
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 10; j++) {
				GRect rect1 = new GRect(litBrickWidth, litBrickHeight);
				rect1.setFilled(true);
				rect1.setColor(rect.getColor());
				add(rect1, startX, startY);
				startX += litBrickWidth;
				arr[i - 1][j - 1] = rect1;
			}
			startX = rect.getX();
			startY += litBrickHeight;
		}

		new Thread(() -> {
			for (int k = 0; k < 1000; k++) {
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 10; j++) {
						arr[i][j].move(rgen.nextDouble(1, 0), rgen.nextDouble(0, 10));
					}
				}
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();  

	}

	public void ballAnimations() {

		GOval[][] arr = new GOval[100][100];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				GOval ball = new GOval(30, 30);
				ball.setFilled(true);
				ball.setColor(rgen.nextColor());
				add(ball, rgen.nextDouble(0, APPLICATION_WIDTH), APPLICATION_HEIGHT);
				arr[i][j] = ball;
			}
		}

		new Thread(() -> {
			for (int k = 0; k < 1000; k++) {
				for (int i = 0; i < 100; i++) {
					for (int j = 0; j < 100; j++) {
						arr[i][j].move(rgen.nextDouble(-4, 4), rgen.nextDouble(-10, 6));
					}
				}

				try {
					Thread.sleep(4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}


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

public class Breakout extends GraphicsProgram {

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
	private static final int NBRICKS_PER_ROW = 1;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 1;

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
	private static final int NTURNS = 3;

	/** Paddle */
	private static GRect paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);

	/** Random generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/** Moving through X */
	private static double vX = 0;

	private static double vY = 0;

	/** Moving through Y */
	private static final int VEL_Y = 5;

	/** PAUSE program with this millisecond */
	private static final int PAUSE = 10;

	private static AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");

	private static int totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setUpGame();
		addMouseListeners();
		PlayGame();

	}

	// This function set ups bricks and paddle
	private void setUpGame() {
		setAllBricks();
		setPaddle();
	}

	// This function starts the game
	private void PlayGame() {

		int nBalls = NTURNS;
		while (nBalls > 0) {
			vY = VEL_Y;
			vX = rgen.nextDouble(1.0, 3.0);
			if (rgen.nextBoolean(0.5)) {
				vX = -vX;
			}
			GOval ball = initialBall();
			nBalls--;
			while (true) {

				ball.move(vX, vY);
				pause(PAUSE);
				checkForWindowBorders(ball, vX, vY);
				if (ball.getY() >= getHeight() - 2 * BALL_RADIUS) {

					remove(ball);
					pause(900);
					break;
				}
				checkForBricksAndPaddle(ball);
				if (totalBricks == 0) {
					break;
				}
			}
		}
		resultMessage(totalBricks);
	}

	/*
	 * This functions checks if ball touches application borders and changes its
	 * directions also
	 */
	private void checkForWindowBorders(GOval ball, double vX, double vY) {
		if (ball.getX() <= 0 || ball.getX() >= getWidth() - 2 * BALL_RADIUS) {
			this.vX = -vX;
			bounceClip.play();
		}
		if (ball.getY() <= 0 || ball.getY() >= getHeight() - 2 * BALL_RADIUS) {
			this.vY = -vY;
			bounceClip.play();
		}
	}
	
	/*
	 * This functions checks if ball touches bricks or if it touches paddle
	 * if it is brick it is deleting brick, if it is paddle, just changing the direction
	 */
	private void checkForBricksAndPaddle(GOval ball) {
		GObject obj = getCollidingObject(ball);
		if (obj != null && obj != paddle) {
			bounceClip.play();
			remove(obj);
			totalBricks--;
			vY = -vY;

		}
		if (obj == paddle) {
			if (ball.getY() + BALL_RADIUS < paddle.getY() + PADDLE_HEIGHT) {
				vY = -module(vY);
				bounceClip.play();
			}
		}
	}
	
	//This function tells us if gamer win or loose game
	private void resultMessage(int totalBricks) {
		if (totalBricks > 0) {
			GLabel lab = new GLabel("YOU LOST");
			add(lab, getWidth() / 2 - lab.getWidth() / 2, getHeight() / 2 - lab.getHeight());
		} else if (totalBricks == 0) {
			GLabel lab = new GLabel("YOU WIN");
			add(lab, getWidth() / 2 - lab.getWidth() / 2, getHeight() / 2 - lab.getHeight());
		}

	}
	
	//This function identifies if ball touches objects
	private GObject getCollidingObject(GOval ball) {

		GObject obj1 = getElementAt(ball.getX(), ball.getY());
		if (obj1 != null) {
			return obj1;
		}

		GObject obj2 = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		if (obj2 != null) {
			return obj2;
		}

		GObject obj3 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		if (obj3 != null) {
			return obj3;
		}

		GObject obj4 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		if (obj4 != null) {
			return obj4;
		}
		GObject obj5 = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY());
		if (obj5 != null) {
			return obj5;
		}
		GObject obj6 = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2 * BALL_RADIUS + 1);
		if (obj6 != null) {
			return obj6;
		}
		GObject obj7 = getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS);
		if (obj7 != null) {
			return obj7;
		}
		GObject obj8 = getElementAt(ball.getX() + 2 * BALL_RADIUS + 1, ball.getY() + BALL_RADIUS);
		if (obj8 != null) {
			return obj8;
		}

		else {
			return null;
		}
	}

	// This func returns module of a number
	private double module(double x) {
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

}

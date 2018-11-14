import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class Game implements ActionListener, KeyListener
{
	// Global Constants
	public static final int FIELD_WIDTH = 600;
	public static final int FIELD_HEIGHT = 400;

	// Local Constants
	private final int TIMER_SPEED = 10;
	private final int TIMER_DELAY = 750;

	private int SHOOTER_SPEED = 2;

	private static int NUM_SMALL_ALIENS = 20;
	private static int NUM_LARGE_ALIENS = 12;

	private JFrame gameFrame;
	private Timer timer;

	private static boolean esc = false;
	private static int powerUpTimer = 0;
	private static boolean hasPowerup = false;
	private static int powerTime = 0;
	private static int time = 0;
	// These Images could be loaded without the use of the 'getClass' and 'getResource'
	// methods, but using those two methods allows all of the files that make up this
	// program (the .CLASS files and the graphics files) to be put into a single .JAR
	// file and then loaded and run directly from that (executable) file; in addition,
	// a benefit of using the 'ImageIcon' class is that, unlike some of the other
	// file-loading classes, the 'ImageIcon' class fully loads the Image when the
	// object is created, making it possible to immediately determine and use the
	// dimensions of the Image
	private ImageIcon imgBackground = new ImageIcon(getClass().getResource("space.gif"));
	private ImageIcon imgShooter = new ImageIcon(getClass().getResource("testshooter270.fw.png"));
	private ImageIcon imgShooter2 = new ImageIcon(getClass().getResource("test2shooter270.fw.png"));
	private ImageIcon imgPowerup = new ImageIcon(getClass().getResource("PowerUpSystem-star-1.png"));

	private JLabel lblShooter = new JLabel(imgShooter);
	private JLabel lblShooter2 = new JLabel(imgShooter2);
	private JLabel lblPowerup = new JLabel(imgPowerup);
	private int shooterX, shooterY, shooter2Y, shooter2X, shooterDir = 270, shooter2Dir = 270;

	private boolean pressedLeft = false, pressedRight = false, pressedSpace = false, pressedUp = false,
				pressedDown = false, pressedQ = false, pressedW = false, pressedA = false, pressedS = false,
				pressedD = false;;
	private boolean controlKeyPressed = false, missileFired = false, missile2Fired = false;

	private JLabel lblGameOver = new JLabel("Game Over!");
	private JLabel pause = new JLabel("Game Paused");
	private Font fontGameOver = new Font("Helvetica", Font.BOLD, 24);
	private int textWidth = lblGameOver.getFontMetrics(fontGameOver).stringWidth(lblGameOver.getText());

	// Create ArrayLists to hold the 'Alien' objects (all types) and the 'Missile'
	// objects that will be used throughout the game
	ArrayList<Alien> aliens = new ArrayList<Alien>();
	ArrayList<Missile> missiles = new ArrayList<Missile>();
	ArrayList<Bomb> bombs = new ArrayList<Bomb>();

	int points = 0;
	JLabel scoreboard = new JLabel("<html><font color='white'>points: " + points + "</font></html>");
	Font fonto = new Font("Arial", Font.BOLD, 32);

	int randMakeBomb = 0;
	boolean makeBomb = false;

	boolean soundOn = true;
	boolean scoreOn = true;

	private static int difficulty = 100;
	int value = 0;

	private static boolean beserker = false;

	JProgressBar progressBar = new JProgressBar();

	static final int MIN_BAR_VALUE = 0;
	static final int MAX_BAR_VALUE = 100;

	public static void main(String[] args)
	{
		new Game();
	}
	// Constructor
	public Game()
	{
		gameFrame = new JFrame();

		// Set the background image and other JFrame properties
		gameFrame.setContentPane(new JLabel(imgBackground));
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(FIELD_WIDTH, FIELD_HEIGHT + 18);
		gameFrame.setTitle("Dave's Simple Shooter Game");
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setFocusable(true);

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");

		JMenuItem newMenuItem = new JMenuItem("Restart");
		newMenuItem.setActionCommand("start");
		newMenuItem.addActionListener(this);

		JMenuItem exitMenuItem = new JMenuItem("Quit");
		exitMenuItem.setActionCommand("quit");
		exitMenuItem.addActionListener(this);

		JMenuItem shootMenuItem = new JMenuItem("Shoot");
		shootMenuItem.setActionCommand("shoot");
		shootMenuItem.addActionListener(this);

		JMenuItem soundMenuItem = new JMenuItem("Toggle Sound");
		soundMenuItem.setActionCommand("sound");
		soundMenuItem.addActionListener(this);

		JMenuItem scoreMenuItem = new JMenuItem("Toggle Score");
		scoreMenuItem.setActionCommand("score");
		scoreMenuItem.addActionListener(this);

		JMenuItem twoPlayerMenuItem = new JMenuItem("Toggle Two Players");
		twoPlayerMenuItem.setActionCommand("players");
		twoPlayerMenuItem.addActionListener(this);

		JMenuItem pauseMenuItem = new JMenuItem("Pause/Unpause");
		pauseMenuItem.setActionCommand("pause");
		pauseMenuItem.addActionListener(this);

		fileMenu.add(twoPlayerMenuItem);
		fileMenu.add(pauseMenuItem);
		fileMenu.add(newMenuItem);
		fileMenu.add(exitMenuItem);
		editMenu.add(shootMenuItem);
		editMenu.add(soundMenuItem);
		editMenu.add(scoreMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		gameFrame.setJMenuBar(menuBar);

		// Set up the "Game Over" JLabel
		lblGameOver.setVisible(false);
		lblGameOver.setSize(400, 100);
		lblGameOver.setLocation((FIELD_WIDTH / 2 - textWidth / 2) - 80, (FIELD_HEIGHT / 2 - 50) - 50);
		lblGameOver.setFont(fontGameOver);
		lblGameOver.setForeground(Color.YELLOW);

		lblPowerup.setVisible(false);
		lblPowerup.setSize(10, 15);
		lblPowerup.setLocation((int) (Math.random() * FIELD_WIDTH - 100), FIELD_HEIGHT - 85);
		gameFrame.add(lblPowerup);

		pause.setVisible(false);
		pause.setSize(textWidth + 50, 50);
		pause.setLocation(FIELD_WIDTH / 2 - (textWidth + 50) / 2, FIELD_HEIGHT / 2 - 50);
		pause.setFont(fontGameOver);
		pause.setForeground(Color.GRAY);
		gameFrame.add(pause);

		scoreboard.setSize(450, 100);
		scoreboard.setLocation(215, 0);
		scoreboard.setFont(fonto);
		gameFrame.add(scoreboard);

		progressBar.setSize(250, 20);
		progressBar.setLocation(5, 5);
		progressBar.setForeground(Color.BLACK);
		progressBar.setBackground(Color.RED);
		//progressBar.setAl

		// Set the point at which the Progress Bar begins moving
		progressBar.setMinimum(MIN_BAR_VALUE);
		progressBar.setMaximum(MAX_BAR_VALUE);

		gameFrame.add(progressBar);
		gameFrame.add(lblGameOver);

		setUpShooter();
		setUpLargeAliens();
		setUpSmallAliens();
		
		gameFrame.addKeyListener(this);
		gameFrame.setVisible(true);

		// Set (and start) a new Swing Timer to fire every 'TIMER_SPEED' milliseconds,
		// after an initial delay of 'TIMER_DELAY' milliseconds; this Timer, along
		// with the distance (number of pixels) that the aliens, missiles, and shooter
		// move with each cycle, controls how fast the objects move on the playing
		// field; note that if adding a "pause/unpause" feature to this game, the
		// value of the 'TIMER_DELAY' constant should probably be set to zero
		timer = new Timer(TIMER_SPEED, this);
		timer.setInitialDelay(TIMER_DELAY);
		timer.setActionCommand("timer");
		timer.start();
	}

	// Set the size and starting position of the player's shooter
	public void setUpShooter()
	{
		// Set the size of the JLabel that contains the shooter image
		lblShooter.setSize(imgShooter.getIconWidth(), imgShooter.getIconHeight());
		lblShooter2.setSize(imgShooter2.getIconWidth(), imgShooter2.getIconHeight());

		// Set the shooter's initial position on the playing field; note
		// that subtracting 30 pixels accounts for the JFrame title bar
		shooterX = (FIELD_WIDTH / 2) - (lblShooter.getWidth() / 2);
		shooterY = FIELD_HEIGHT - lblShooter.getHeight() - 30;
		lblShooter.setLocation(shooterX, shooterY);

		shooter2X = (FIELD_WIDTH / 2) - (lblShooter2.getWidth() / 2) - 15;
		shooter2Y = FIELD_HEIGHT - lblShooter2.getHeight() - 30;
		lblShooter2.setLocation(shooter2X, shooter2Y);
		lblShooter2.setVisible(false);

		// Add the shooter JLabel to the JFrame
		gameFrame.add(lblShooter);
		gameFrame.add(lblShooter2);
	}

	// Create and randomly place the appropriate number of SMALL aliens on the playing field
	public void setUpSmallAliens()
	{
		// Determine the width and height of each alien being placed
		Alien tempAlien = new SmallAlien(0, 0);
		int alienWidth = tempAlien.getWidth();
		int alienHeight = tempAlien.getHeight();

		for (int i = 0; i < NUM_SMALL_ALIENS; i++)
		{
			// Set the starting positions of each of the aliens being placed
			int x = (int) (Math.random() * (FIELD_WIDTH - alienWidth - 7) + 1);
			int y = (int) (Math.random() * (FIELD_HEIGHT - alienHeight - 26 - lblShooter.getHeight() - 60));

			// Create a new 'Alien' object and add it to the 'aliens' ArrayList 
			aliens.add(new SmallAlien(x, y));
		}
	}

	// Create and randomly place the appropriate number of LARGE aliens on the playing field
	public void setUpLargeAliens()
	{
		// Determine the width and height of each alien being placed
		Alien tempAlien = new LargeAlien(0, 0);
		int alienWidth = tempAlien.getWidth();
		int alienHeight = tempAlien.getHeight();

		for (int i = 0; i < NUM_LARGE_ALIENS; i++)
		{
			// Set the starting positions of each of the aliens being placed
			int x = (int) (Math.random() * (FIELD_WIDTH - alienWidth - 7) + 1);
			int y = (int) (Math.random() * (FIELD_HEIGHT - alienHeight - 26 - lblShooter.getHeight() - 60));

			// Create a new 'Alien' object and add it to the 'aliens' ArrayList 
			aliens.add(new LargeAlien(x, y));
		}
	}

	// This method will be called automatically whenever the Timer fires
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("start"))
		{
			gameFrame.dispose();
			time = 0;
			hasPowerup = false;
			new Game();
		}
		else if (event.getActionCommand().equals("quit"))
			gameFrame.dispose();
		else if (event.getActionCommand().equals("shoot"))
			pressedSpace = true;
		else if (event.getActionCommand().equals("sound"))
			System.out.println();
		else if (event.getActionCommand().equals("score"))
		{
			if (scoreOn)
			{
				scoreboard.setVisible(false);
				scoreOn = false;
			}
			else
			{
				scoreboard.setVisible(true);
				scoreOn = true;
			}
		}
		else if (event.getActionCommand().equals("players"))
		{
			lblShooter2.setVisible(!lblShooter2.isVisible());
			if (lblShooter2.isVisible())
			{
				progressBar.setMaximum(MAX_BAR_VALUE * 2);
				progressBar.setValue(progressBar.getValue() * 2);
			}
			else
			{
				progressBar.setMaximum(MAX_BAR_VALUE / 2);
				progressBar.setValue(progressBar.getValue() / 2);
			}
			
			progressBar.setValue(value);
		}
		else if (event.getActionCommand().equals("pause"))
		{
			if (!esc)
			{
				timer.stop();
				pause.setVisible(true);
				gameFrame.repaint();
				esc = true;
			}
			else
			{
				pause.setVisible(false);
				gameFrame.repaint();
				timer.start();
				esc = false;
			}
		}

		randMakeBomb = (int) (Math.random() * difficulty);

		if (randMakeBomb == 1)
			makeBomb = true;
		else
			makeBomb = false;

		time++;
		if (hasPowerup)
			powerTime++;

		if (powerTime > 500)
		{
			hasPowerup = false;
			powerTime = 0;
		}
		powerUpTimer++;

		if (powerUpTimer > 1800)
		{
			lblPowerup.setVisible(true);
			lblPowerup.setLocation((int) (Math.random() * FIELD_WIDTH - 100), FIELD_HEIGHT - 40);
			powerUpTimer = 0;
		}
		// Change the shooter's position if the player is pressing the left
		// or right arrow keys
		double radianShooterDir = Math.toRadians(shooterDir);
		double radianShooter2Dir = Math.toRadians(shooter2Dir);

		if (pressedLeft)
		{
			//shooterX -= SHOOTER_SPEED;
			System.out.println(shooterDir + " - 1");
			shooterDir -= 45;
			System.out.println(shooterDir + " - 2");
			pressedLeft = false;
		}
		if (pressedRight)
		{
			//shooterX += SHOOTER_SPEED;
			shooterDir += 45;
			pressedRight = false;
		}
		if (pressedA)
		{
			shooter2Dir -= 45;
			pressedA = false;
		}
		if (pressedD)
		{
			shooter2Dir += 45;
			pressedD = false;
		}

		shooterDir %= 360;
		shooter2Dir %= 360;

		if (shooterDir < 0)
			shooterDir += 360;
		if (shooter2Dir < 0)
			shooter2Dir += 360;

		if (shooterDir == 0)
		{
			imgShooter = new ImageIcon(getClass().getResource("testshooter.fw.png"));
			lblShooter.setIcon(imgShooter);
		}
		else
		{
			imgShooter = new ImageIcon(getClass().getResource("testshooter" + shooterDir + ".fw.png"));
			lblShooter.setIcon(imgShooter);
		}

		if (shooter2Dir == 0)
		{
			imgShooter2 = new ImageIcon(getClass().getResource("test2shooter.fw.png"));
			lblShooter2.setIcon(imgShooter2);
		}
		else
		{
			imgShooter2 = new ImageIcon(getClass().getResource("test2shooter" + shooter2Dir + ".fw.png"));
			lblShooter2.setIcon(imgShooter2);
		}

		if (pressedUp)
		{
			shooterX += SHOOTER_SPEED * Math.cos(radianShooterDir);
			shooterY += SHOOTER_SPEED * Math.sin(radianShooterDir);
			//gameFrame.repaint();
		}
		if (pressedDown)
		{
			shooterX -= SHOOTER_SPEED * Math.cos(radianShooterDir);
			shooterY -= SHOOTER_SPEED * Math.sin(radianShooterDir);
			//gameFrame.repaint();
		}
		if (pressedW)
		{
			shooter2X += SHOOTER_SPEED * Math.cos(radianShooter2Dir);
			shooter2Y += SHOOTER_SPEED * Math.sin(radianShooter2Dir);
		}
		if (pressedS)
		{
			shooter2X -= SHOOTER_SPEED * Math.cos(radianShooter2Dir);
			shooter2Y -= SHOOTER_SPEED * Math.sin(radianShooter2Dir);
		}

		lblShooter.setLocation(shooterX, shooterY);
		if (shooterX < 0)
			shooterX = FIELD_WIDTH;
		if (shooterX > FIELD_WIDTH)
			shooterX = 0;
		if (shooterY < 0)
			shooterY = FIELD_HEIGHT;
		if (shooterY > FIELD_HEIGHT)
			shooterY = 0;

		lblShooter2.setLocation(shooter2X, shooter2Y);
		if (shooter2X < 0)
			shooter2X = FIELD_WIDTH;
		if (shooter2X > FIELD_WIDTH)
			shooter2X = 0;
		if (shooter2Y < 0)
			shooter2Y = FIELD_HEIGHT;
		if (shooter2Y > FIELD_HEIGHT)
			shooter2Y = 0;

		// Move the remaining aliens across the playing field
		for (int i = 0; i < aliens.size(); i++)
		{
			Alien alien = aliens.get(i);
			alien.moveAlien();
		}

		// Move the existing missiles up the playing field
		for (int l = 0; l < bombs.size(); l++)
		{
			Bomb bomb = bombs.get(l);
			bomb.moveMissile();

			if (bomb.getY() < 0 - bomb.getHeight())
			{
				gameFrame.getContentPane().remove(bomb.getMissileImage());
				bombs.remove(l);
			}
		}

		for (int j = 0; j < missiles.size(); j++)
		{
			Missile missile = missiles.get(j);
			missile.moveMissile();

			// If the missile gets past the top of the playing field, remove it
			if (missile.getY() < 0 - missile.getHeight())
			{
				gameFrame.getContentPane().remove(missile.getMissileImage());
				missiles.remove(j);
			}
		}

		if (makeBomb)
		{
			int x = aliens.get((int) (Math.random() * aliens.size())).getX();
			int y = aliens.get((int) (Math.random() * aliens.size())).getY();
			int rand = (int) (Math.random() * 4);
			int bombSpeed = 0;

			if (rand == 0)
				bombSpeed = 4;
			else if (rand == 1)
				bombSpeed = 3;
			else if (rand == 2)
				bombSpeed = -4;
			else if (rand == 3)
				bombSpeed = -3;

			if (!beserker)
				bombSpeed = 2;

			System.out.println(rand + "         " + bombSpeed);

			bombs.add(new Bomb(x, y, bombSpeed));
		}
		// If the player has pressed the space bar, launch a missile; the variable
		// 'missileFired' prevents the player from holding down the space bar to
		// fire missiles continuously (by forcing the player to release the space
		// bar between firings)
		if (pressedSpace && !missileFired)
		{
			// Determine the width and height of the missile being launched
			Missile tempMissile = new Missile(0, 0, shooterDir);
			int missileWidth = tempMissile.getWidth();
			int missileHeight = tempMissile.getHeight();

			// Set the starting position of the missile being launched 
			int x = shooterX + (lblShooter.getWidth() / 2) - (missileWidth / 2);
			int y = FIELD_HEIGHT - lblShooter.getHeight() - 30 - missileHeight;

			// Create a new 'Missile' object and add it to the 'missiles' ArrayList 
			missiles.add(new Missile(shooterX, shooterY, shooterDir));

			if (hasPowerup)
				missileFired = false;
			else
				missileFired = true;
		}
		if (pressedQ && !missile2Fired)
		{
			// Determine the width and height of the missile being launched
			Missile temp2Missile = new Missile(0, 0, shooter2Dir);
			int missile2Width = temp2Missile.getWidth();
			int missile2Height = temp2Missile.getHeight();

			// Set the starting position of the missile being launched 
			int x2 = shooter2X + (lblShooter2.getWidth() / 2) - (missile2Width / 2);
			int y2 = FIELD_HEIGHT - lblShooter2.getHeight() - 30 - missile2Height;

			// Create a new 'Missile' object and add it to the 'missiles' ArrayList 
			missiles.add(new Missile(shooter2X, shooter2Y, shooter2Dir));

			if (hasPowerup)
				missile2Fired = false;
			else
				missile2Fired = true;
		}

		// Draw the aliens (all types)
		for (int i = 0; i < aliens.size(); i++)
		{
			Alien alien = aliens.get(i);
			JLabel aLabel = alien.getAlienImage();
			aLabel.setLocation(alien.getX(), alien.getY());
			aLabel.setSize(alien.getWidth(), alien.getHeight());
			gameFrame.add(aLabel);
		}

		// Draw the missiles
		for (int i = 0; i < missiles.size(); i++)
		{
			Missile missile = missiles.get(i);
			JLabel mLabel = missile.getMissileImage();
			mLabel.setLocation(missile.getX(), missile.getY());
			mLabel.setSize(missile.getWidth(), missile.getHeight());
			gameFrame.add(mLabel);
		}

		for (int o = 0; o < bombs.size(); o++)
		{
			Bomb bomb = bombs.get(o);
			JLabel bLabel = bomb.getMissileImage();
			bLabel.setLocation(bomb.getX(), bomb.getY());
			bLabel.setSize(bomb.getWidth(), bomb.getHeight());
			gameFrame.add(bLabel);
		}

		// Redraw/Update the playing field
		gameFrame.repaint();

		checkCollisions();

		// This line synchronizes the graphics state by flushing buffers containing
		// graphics events and forcing the frame drawing to happen now; otherwise,
		// it can sometimes take a few extra milliseconds for the drawing to take
		// place, which can result in jerky graphics movement; this line ensures
		// that the display is up-to-date; it is useful for animation, since it can
		// reduce or eliminate flickering
		Toolkit.getDefaultToolkit().sync();
	}

	// For every alien and missile currently on the playing field, create a
	// "rectangle" around both the alien and the missile, and then check to
	// see if the two rectangles intersect each other
	public void checkCollisions()
	{
		// The 'try-catch' exception trapping is needed to prevent an error from
		// occurring when an element is removed from the 'aliens' and 'missiles'
		// ArrayLists, causing the 'for' loops to end prematurely 

		Rectangle rShooter = new Rectangle(lblShooter.getX(), lblShooter.getY(), lblShooter.getWidth(),
					lblShooter.getHeight());
		Rectangle rShooter2 = new Rectangle(lblShooter2.getX(), lblShooter2.getY(), lblShooter2.getWidth(),
					lblShooter2.getHeight());
		Rectangle rPowerup = new Rectangle(lblPowerup.getX(), lblPowerup.getY(), lblPowerup.getWidth(),
					lblPowerup.getHeight());

		if (rShooter.intersects(rPowerup) && lblPowerup.isVisible())
		{
			System.out.println(lblPowerup.getWidth());
			System.out.println(lblShooter.getWidth());
			hasPowerup = true;
			lblPowerup.setVisible(false);
		}

		if (rShooter2.intersects(rPowerup) && lblPowerup.isVisible())
		{
			System.out.println(lblPowerup.getWidth());
			System.out.println(lblShooter.getWidth());
			hasPowerup = true;
			lblPowerup.setVisible(false);
		}

		for (int c = 0; c < bombs.size(); c++)
		{
			try
			{
				//				int spot = (int) (Math.random() * bombs.size());
				Rectangle rBomb = new Rectangle((bombs.get(c)).getX(), (bombs.get(c)).getY(), (bombs.get(c)).getWidth(),
							(bombs.get(c)).getHeight());
				//System.out.println(lblShooter.getY() + "  " + lblShooter.getX() + "  " + rBomb.getX());
				if (rBomb.intersects(rShooter))
				{
					gameFrame.getContentPane().remove(bombs.get(c).getMissileImage());
					bombs.remove(c);
					value += 20;
					progressBar.setValue(value);
					if (value >= 100 && !lblShooter2.isVisible())
					{
						timer.stop();
						double finalScore = Math.round((double) points / time * 1000);
						System.out.println(finalScore);
						gameFrame.getContentPane().removeAll();
						missiles.removeAll(missiles);
						aliens.removeAll(aliens);
						lblGameOver.setText("Game Over! Score is: " + finalScore);
						lblGameOver.setVisible(true);
					}
					else if (value >= 200 && lblShooter2.isVisible())
					{
						timer.stop();
						double finalScore = Math.round((double) points / time * 1000);
						System.out.println(finalScore);
						gameFrame.getContentPane().removeAll();
						missiles.removeAll(missiles);
						aliens.removeAll(aliens);
						lblGameOver.setText("Game Over! Score is: " + finalScore);
						lblGameOver.setVisible(true);
					}
				}
				if (rBomb.intersects(rShooter2))
				{
					gameFrame.getContentPane().remove(bombs.get(c).getMissileImage());
					bombs.remove(c);
					value += 20;
					progressBar.setValue(value);
					if (value >= 100 && !lblShooter2.isVisible())
					{
						timer.stop();
						//btw you can live forever w/ esc
					}
					else if (value >= 200 && lblShooter2.isVisible())
					{
						timer.stop();
					}
				}
			}
			catch (Exception error)
			{
			}
		}

		for (int i = 0; i < aliens.size(); i++)
			for (int j = 0; j < missiles.size(); j++)
			{
				try
				{
					Rectangle rAlien = new Rectangle(aliens.get(i).getX(), aliens.get(i).getY(), aliens.get(i).getWidth(),
								aliens.get(i).getHeight());
					Rectangle rMissile = new Rectangle(missiles.get(j).getX(), missiles.get(j).getY(),
								missiles.get(j).getWidth(), missiles.get(j).getHeight());
					// If an alien and a missile intersect each other, remove both
					// of them from the playing field and the ArrayLists
					if (rAlien.intersects(rMissile))
					{
						if (aliens.get(i) instanceof SmallAlien)
							points += 2;
						if (aliens.get(i) instanceof LargeAlien)
							points += 5;
						gameFrame.getContentPane().remove(aliens.get(i).getAlienImage());
						aliens.remove(i);
						gameFrame.getContentPane().remove(missiles.get(j).getMissileImage());
						missiles.remove(j);
						String color = "white";
						if (points < 50)
							color = "white";
						else if (points < 100)
							color = "red";
						else if (points < 250)
							color = "orange";
						else if (points < 500)
							color = "yellow";
						else if (points < 1000)
							color = "blue";
						scoreboard.setText("<html><font color='" + color + "'>points: " + points + "</font></html>");
					}
				}
				catch (Exception error)
				{
				}
			}

		// If all of the aliens have been destroyed, the game is over, so stop
		// the Timer and remove any remaining missiles from the playing field 
		if (aliens.size() == 0)
		{
			timer.stop();
			gameFrame.getContentPane().removeAll();
			missiles.removeAll(missiles);

			// Display the "Game Over" JLabel
			gameFrame.add(lblGameOver);
			double finalScore = Math.round((double) points / time * 1000);
			System.out.println(finalScore);
			lblGameOver.setText("Game Over! Score is: " + finalScore);
			lblGameOver.setVisible(true);
			
			//saves a file
			String i = "f";
			String name = "shooterGameHighScore";
			double list = finalScore;

			final String FILENAME = "C:\\" + name + ".txt";
			BufferedReader br = null;
			FileReader fr = null;
			String sCurrentLine = null;
			double previousHS = 0;

			try
			{

				fr = new FileReader(FILENAME);
				br = new BufferedReader(fr);

				sCurrentLine = br.readLine();

			}
			catch (IOException e)
			{
				try
				{
					PrintWriter file2 = new PrintWriter("C:\\" + name + ".txt", "UTF-8");
					if (list > previousHS) {
						file2.println(list);
						lblGameOver.setText("<html>" + lblGameOver.getText() + "<br/>You got the high score!</html>");
					}
					else {
						file2.println(previousHS);
						lblGameOver.setText("<html>" + lblGameOver.getText() + "<br/>You didn't get the high score :(</html>");
					}
					
					file2.close();
					fr = new FileReader(FILENAME);
					br = new BufferedReader(fr);

					try
					{
						sCurrentLine = br.readLine();
					}
					catch (IOException error)
					{
						// TODO Auto-generated catch block
						error.printStackTrace();
					}

				}
				catch (FileNotFoundException eroor)
				{
					System.out.println("error");
					// TODO Auto-generated catch block
					eroor.printStackTrace();
				}
				catch (UnsupportedEncodingException eroor)
				{
					System.out.println("error");
					// TODO Auto-generated catch block
					eroor.printStackTrace();
				}
				
			}
			finally
			{

				try
				{

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				}
				catch (IOException ex)
				{

					ex.printStackTrace();

				}

			}
			if (sCurrentLine != null)
				previousHS = Double.parseDouble(sCurrentLine);

			try
			{
				PrintWriter file2 = new PrintWriter("C:\\" + name + ".txt", "UTF-8");
				if (list > previousHS) {
					file2.println(list);
					lblGameOver.setText("<html>" + lblGameOver.getText() + "<br/>You got the high score!</html>");
				}
				else {
					file2.println(previousHS);
					lblGameOver.setText("<html>" + lblGameOver.getText() + "<br/>You didn't get the high score :(</html>");
				}
				
				file2.close();
			}
			catch (FileNotFoundException e)
			{
				System.out.println("error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (UnsupportedEncodingException e)
			{
				System.out.println("error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(previousHS + "   " + list);
		}
		gameFrame.repaint();
	}

	// See if the player has PRESSED a key
	public void keyPressed(KeyEvent event)
	{
		int key = event.getKeyCode();

		if (key == KeyEvent.VK_ESCAPE)
		{
			if (!esc)
			{
				timer.stop();
				pause.setVisible(true);
				gameFrame.repaint();
				esc = true;
			}
			else
			{
				pause.setVisible(false);
				gameFrame.repaint();
				timer.start();
				esc = false;
			}
		}

		if (key == KeyEvent.VK_CONTROL) // CONTROL key
			controlKeyPressed = true;

		if ((key == 88) && (controlKeyPressed)) // CONTROL + X
		{
			gameFrame.dispose();
			System.exit(0);
		}

		if ((key == 78) && (controlKeyPressed))
		{
			gameFrame.dispose();
			time = 0;
			hasPowerup = false;
			new Game();
		}

		if ((key == 66) && (controlKeyPressed))
		{
			gameFrame.dispose();
			time = 0;
			if (!beserker)
			{
				beserker = true;
				NUM_SMALL_ALIENS = 55;
				NUM_LARGE_ALIENS = 38;
				difficulty = 3;
			}
			else
			{
				beserker = false;
				NUM_SMALL_ALIENS = 20;
				NUM_LARGE_ALIENS = 12;
				difficulty = 100;
			}
			new Game();
		}

		if (key == KeyEvent.VK_LEFT) // LEFT arrow
			pressedLeft = true;
		if (key == KeyEvent.VK_RIGHT) // RIGHT arrow
			pressedRight = true;
		if (key == KeyEvent.VK_UP) // UP arrow
			pressedUp = true;
		if (key == KeyEvent.VK_DOWN)
			pressedDown = true;

		if (key == KeyEvent.VK_SPACE) // SPACE bar
			pressedSpace = true;

		if (key == KeyEvent.VK_A) // LEFT key
			if (lblShooter2.isVisible())
				pressedA = true;
		if (key == KeyEvent.VK_D) // RIGHT key
			if (lblShooter2.isVisible())
				pressedD = true;
		if (key == KeyEvent.VK_W) // UP key
			if (lblShooter2.isVisible())
				pressedW = true;
		if (key == KeyEvent.VK_S)
			if (lblShooter2.isVisible())
				pressedS = true;

		if (key == KeyEvent.VK_Q) // SHOOT key
			if (lblShooter2.isVisible())
				pressedQ = true;
	}

	// See if the player has RELEASED a key
	public void keyReleased(KeyEvent event)
	{
		int key = event.getKeyCode();

		if (key == KeyEvent.VK_CONTROL) // CONTROL key
			controlKeyPressed = false;

		if (key == KeyEvent.VK_LEFT) // LEFT arrow
			pressedLeft = false;
		if (key == KeyEvent.VK_RIGHT) // RIGHT arrow
			pressedRight = false;
		if (key == KeyEvent.VK_UP) // UP arrow
			pressedUp = false;
		if (key == KeyEvent.VK_DOWN)
			pressedDown = false;

		if (key == KeyEvent.VK_SPACE) // SPACE bar
		{
			pressedSpace = false;
			missileFired = false;
		}

		if (key == KeyEvent.VK_A) // LEFT key
			pressedA = false;
		if (key == KeyEvent.VK_D) // RIGHT key
			pressedD = false;
		if (key == KeyEvent.VK_W) // UP key
			pressedW = false;
		if (key == KeyEvent.VK_S)
			pressedS = false;

		if (key == KeyEvent.VK_Q) // SHOOT key
		{
			pressedQ = false;
			missile2Fired = false;
		}
	}

	public void keyTyped(KeyEvent event)
	{
	}
}

package pokeClone;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;

public class Game implements ActionListener, KeyListener {
	public static Game myGame;
	
	// Global Constants
	public static final int FIELD_WIDTH = 600;
	public static final int FIELD_HEIGHT = 400;

	// Local Constants
	private final int TIMER_SPEED = 10;
	private final int TIMER_DELAY = 750;
	
	public int mapTopRow = 0;
	public int mapLeftColumn = 0;

	private JFrame gameFrame;
	private Timer timer;

	private static boolean esc = false;
	private static int time = 0;
	// These Images could be loaded without the use of the 'getClass' and
	// 'getResource'
	// methods, but using those two methods allows all of the files that make up
	// this
	// program (the .CLASS files and the graphics files) to be put into a single
	// .JAR
	// file and then loaded and run directly from that (executable) file; in
	// addition,
	// a benefit of using the 'ImageIcon' class is that, unlike some of the
	// other
	// file-loading classes, the 'ImageIcon' class fully loads the Image when
	// the
	// object is created, making it possible to immediately determine and use
	// the
	// dimensions of the Image
	private ArrayList<ImageIcon> playerImages = new ArrayList<ImageIcon>();

	private ImageIcon tile = new ImageIcon(getClass().getResource("testing.png"));
	private ImageIcon player = new ImageIcon(getClass().getResource("testing.png"));

	private JLabel lblPlayer = new JLabel(player);
	private int playerX, playerY, playerDir = 270;

	private boolean pressedLeft = false, pressedRight = false, pressedSpace = false, pressedUp = false,
			pressedDown = false;
	private boolean controlKeyPressed = false;

	private Font aFont = new Font("Helvetica", Font.BOLD, 24);

	// Create ArrayLists to hold the 'Alien' objects (all types) and the
	// 'Missile'
	// objects that will be used throughout the game
	private ArrayList<Tile> tiles = new ArrayList<Tile>();

	private Font fonto = new Font("Arial", Font.BOLD, 32);

	private boolean soundOn = true;

	public static void main(String[] args) {
		myGame = new Game();
	}

	public Game() {
//		 for (int i = 0; i < 10; i++) {
//		 playerImages.add(new ImageIcon(getClass().getResource("player" + i +
//		 ".png")));
//		 }

		System.out.println("testing");

		gameFrame = new JFrame();

		// Set the background image and other JFrame properties
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(FIELD_WIDTH, FIELD_HEIGHT + 18);
		gameFrame.setTitle("Testing");
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setFocusable(true);

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");

		JMenuItem exitMenuItem = new JMenuItem("Quit");
		exitMenuItem.setActionCommand("quit");
		exitMenuItem.addActionListener(this);

		JMenuItem soundMenuItem = new JMenuItem("Toggle Sound");
		soundMenuItem.setActionCommand("sound");
		soundMenuItem.addActionListener(this);

		fileMenu.add(exitMenuItem);
		editMenu.add(soundMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		gameFrame.setJMenuBar(menuBar);

		gameFrame.setVisible(true);
		
		
	}
	
	private void drawBackground() {
		for (Tile i : tiles) {
			i.draw();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("quit")) {
			gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}

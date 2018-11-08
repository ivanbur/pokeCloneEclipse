package pokeClone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game {
	final static int SCREEN_WIDTH = 960;
	final static int SCREEN_HEIGHT = 720;
	final int TILE_SIZE = 32;
	final int CHARACTER_W = 32;
	final int CHARACTER_H = 32;
	final int GAME_SPEED = 1;
	final int MOVEMENT_DISTANCE = 2;

	public static JFrame frame = new JFrame();
	public static JLabel label = new JLabel();

	public static void main(String[] args) {
		frame.setLayout(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setTitle("Testing");
		frame.getContentPane().setBackground(Color.GREEN);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		label.setSize(500, 500);
		frame.add(label);
		try {
			BufferedImage img = ImageIO.read(new File("testing.png"));
			Graphics g = label.getGraphics();
			g.drawImage(img, 0, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

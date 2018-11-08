package pokeClone;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	private int currentRow;
	private int currentFrame;
	private int numRows;
	private int numFrames;
	private int clipSizeWidth;
	private int clipSizeHeight;
	private BufferedImage img;
	
	public Sprite(String filename, int numberRows, int numberFrames, int sizeOfClipWidth, int sizeOfClipHeight) {
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		currentRow = 0;
		currentFrame = 0;
		numRows = numberRows;
		numFrames = numberFrames;
		clipSizeWidth = sizeOfClipWidth;
		clipSizeHeight = sizeOfClipHeight;
	}
	
	public Sprite(String filename, int numberRows, int numberFrames) {
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		currentRow = 0;
		currentFrame = 0;
		numRows = numberRows;
		numFrames = numberFrames;
		clipSizeWidth = 32;
		clipSizeHeight = 32;
	}
}

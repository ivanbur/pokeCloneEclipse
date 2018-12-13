package pokeClone;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Sprite {
	protected int currentRow;
	protected int currentFrame;
	protected int numRows;
	protected int numFrames;
	protected int clipSizeWidth;
	protected int clipSizeHeight;
	protected ImageIcon img;
	private BufferedImage bufferedIMG;

	public Sprite(String filename, int numberRows, int numberFrames, int sizeOfClipWidth, int sizeOfClipHeight) {
		img = new ImageIcon(getClass().getResource(filename));

		try {
			bufferedIMG = ImageIO.read(getClass().getResource(filename));
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
		img = new ImageIcon(getClass().getResource(filename));

		currentRow = 0;
		currentFrame = 0;
		numRows = numberRows;
		numFrames = numberFrames;
		clipSizeWidth = 32;
		clipSizeHeight = 32;
	}

	public Sprite(String filename, int numberRows) {
		img = new ImageIcon(getClass().getResource(filename));

		currentRow = 0;
		currentFrame = 0;
		numRows = numberRows;
		numFrames = 4;
		clipSizeWidth = 32;
		clipSizeHeight = 32;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumFrames() {
		return numFrames;
	}

	public int getWidth() {
		return clipSizeWidth;
	}

	public int getHeight() {
		return clipSizeHeight;
	}

	public void setRow(int row) {
		currentRow = row;
		
		img = new ImageIcon(bufferedIMG.getSubimage(currentFrame*clipSizeWidth, currentRow*clipSizeHeight, clipSizeWidth, clipSizeHeight));
	}

	public void setFrame(int frame) {
		currentFrame = frame;
		
		img = new ImageIcon(bufferedIMG.getSubimage(currentFrame*clipSizeWidth, currentRow*clipSizeHeight, clipSizeWidth, clipSizeHeight));
		
//		bufferedIMG.getSubimage(currentFrame*clipSizeWidth, currentRow*clipSizeHeight, clipSizeWidth, clipSizeHeight);
	}
}

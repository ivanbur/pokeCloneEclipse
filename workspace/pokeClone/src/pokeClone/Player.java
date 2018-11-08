package pokeClone;

public class Player extends Sprite {
	private int x;
	private int y;
	private int mapRow;
	private int mapColumn;
	
	public Player(String filename, int x, int y, int mapRow, int mapColumn, int numberRows, int numberFrames, int sizeOfClipWidth, int sizeOfClipHeight) {
		super(filename, numberRows, numberFrames, sizeOfClipWidth, sizeOfClipHeight);
	}
}

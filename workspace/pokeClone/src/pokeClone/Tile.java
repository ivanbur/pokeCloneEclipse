package pokeClone;

import javax.swing.JLabel;

public class Tile extends Sprite {
	private int mapRow;
	private int mapColumn;
	
	public Tile(String filename, int numberRows, int numberFrames, int sizeOfClipWidth, int sizeOfClipHeight, int mapRow, int mapColumn) {
		super(filename, numberRows, numberFrames, sizeOfClipWidth, sizeOfClipHeight);
		
		this.mapRow = mapRow;
		this.mapColumn = mapColumn;
	}
	
	public void draw() {
		JLabel testing = new JLabel(super.img);
		testing.setLocation(mapColumn - Game.myGame.mapLeftColumn, mapRow - Game.myGame.mapTopRow);
	}
}

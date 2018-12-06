package pokeClone;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends Sprite {
	protected int mapRow;
	protected int mapColumn;
	
	public Tile(String filename, int sizeOfClipWidth, int sizeOfClipHeight, int mapRow, int mapColumn) {
		super(filename, 1, 1, sizeOfClipWidth, sizeOfClipHeight);
		
		this.mapRow = mapRow;
		this.mapColumn = mapColumn;
	}
	
	public void draw() {
		JLabel testing = new JLabel(super.img);
		testing.setLocation(mapColumn - Game.mapLeftColumn, mapRow - Game.mapTopRow);
	}
	
	public void setImg(String filename) {
		super.img = new ImageIcon(getClass().getResource(filename));
	}
	
}

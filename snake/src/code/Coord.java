package code;

import java.util.Objects;

/**
 * Coordinate in a 2D space, y goes from top to bottom and x from left to right.
 */
public record Coord(int x, int y) {
	
	/** Generate an instance of Coord
	 * @param x the higher the x the more on the right you are.
	 * @param y the higher the y the closer to the bottom of the screen you are. (Given you are still in)
	 */
	public Coord {}

	/** Addition of the value x and y of both coordinate.
	 * @param b the coordinate which we add to this.
	 * @return Result of the addition in the form of a new coordinate.
	 */
	public Coord add(Coord b) {
		Objects.requireNonNull(b);
		return new Coord(x + b.x, y + b.y);
	}
}
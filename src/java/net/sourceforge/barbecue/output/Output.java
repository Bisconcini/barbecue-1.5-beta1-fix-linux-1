package net.sourceforge.barbecue.output;

public interface Output {
	/**
	 * "Draws" a bar at the given coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param width the width
	 * @param height the height
	 * @param paintWithForegroundColor if true, use the foreground color, otherwise use the background color
         * @return the width of the bar drawn
	 */
	int drawBar(int x, int y, int width, int height, boolean paintWithForegroundColor) throws OutputException;

	/**
	 * Sets up the drawing environment.  Called before drawing starts.
	 * Matched with call to endDraw() at the end.  This allows for caching as needed.
	 */
	void beginDraw() throws OutputException;

	/**
	 * Balanced with startDraw(), used for caching, output of epilogues.
	 * @param width The output width (in pixels) of the barcode
	 * @param height The output height (in pixels) of the barcode.
	 */
	void endDraw(int width, int height) throws OutputException;

	/**
	 * Draw the specified text.
	 * @param text text to draw
	 * @param layout the text layout calculator
	 * @return the height of this text
	 */
	int drawText(String text, LabelLayout layout) throws OutputException;

	/**
	 * Swaps the foreground and background colours of the output.
	 */
	void toggleDrawingColor();
        
	/**
	 * Paint the background the background colour, based on the height and the width.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param width the width to be painted
	 * @param height the height to be painted
	 */
	void paintBackground(int x, int y, int width, int height);
        
}

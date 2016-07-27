package net.sourceforge.barbecue.output;

public class MarginLabelLayout extends LabelLayout {
	public MarginLabelLayout(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	protected void calculate() {
		textX = x + (width / 2) - ((int)textLayout.getBounds().getWidth() / 2);
		textY = y + (height / 2) + ((int)textLayout.getBounds().getHeight() / 2);
		bgX = x;
		bgY = y;
		bgHeight = height;
		bgWidth = width;
	}
}

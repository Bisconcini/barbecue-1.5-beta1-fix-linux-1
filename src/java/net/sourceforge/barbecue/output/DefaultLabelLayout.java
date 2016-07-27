package net.sourceforge.barbecue.output;

public class DefaultLabelLayout extends LabelLayout {

	public DefaultLabelLayout(int x, int y) {
		super(x, y, NOT_SET, NOT_SET);
	}

	protected void calculate() {
		bgWidth = (int)textLayout.getBounds().getWidth();
		bgHeight = (int)textLayout.getBounds().getHeight();
		bgX = x;
		bgY = y;
		textX = x;
		textY = y;
	}
}

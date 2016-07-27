package net.sourceforge.barbecue.output;

public class CenteredLabelLayout extends LabelLayout {
	/** Pixel gap between the barcode bars and the top of the data text underneath */
    public static final int BARS_TEXT_VGAP = 5;

	public CenteredLabelLayout(int x, int y, int width) {
		super(x, y, width, BARS_TEXT_VGAP);
	}

	protected void calculate() {
		textX = (float) ((((width - x) - textLayout.getBounds().getWidth()) / 2) + x);
		textY = (float) (y + textLayout.getBounds().getHeight() + BARS_TEXT_VGAP);
		int height = (int) (textLayout.getBounds().getHeight() + BARS_TEXT_VGAP + 1);
		bgX = x;
		bgY = y;
		bgWidth = width - x;
		bgHeight = height;
	}
}

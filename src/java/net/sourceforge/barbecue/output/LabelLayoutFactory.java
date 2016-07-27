package net.sourceforge.barbecue.output;

public class LabelLayoutFactory {

	public static LabelLayout createOriginLayout(int x, int y) {
		return new DefaultLabelLayout(x, y);
	}

	public static LabelLayout createCenteredLayout(int x, int y, int width) {
		return new CenteredLabelLayout(x, y, width);
	}

	public static LabelLayout createMarginLayout(int x, int y, int width, int height) {
		return new MarginLabelLayout(x, y, width, height);
	}
}

package net.sourceforge.barbecue.output;

import java.awt.font.TextLayout;

public abstract class LabelLayout {
	static final int NOT_SET = -1;

	protected final int x;
	protected final int y;
	protected final int width;
	protected final int height;
	protected TextLayout textLayout;
	protected float textX;
	protected float textY;
	protected int bgX;
	protected int bgY;
	protected int bgWidth;
	protected int bgHeight;

	protected LabelLayout(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setTextLayout(TextLayout textLayout) {
		this.textLayout = textLayout;
		calculate();
	}

	protected abstract void calculate();

	public float getTextX() {
		return textX;
	}

	public float getTextY() {
		return textY;
	}

	public int getBackgroundX() {
		return bgX;
	}

	public int getBackgroundY() {
		return bgY;
	}

	public int getBackgroundWidth() {
		return bgWidth;
	}

	public int getBackgroundHeight() {
		return bgHeight;
	}
}

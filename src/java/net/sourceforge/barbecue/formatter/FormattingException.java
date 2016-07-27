package net.sourceforge.barbecue.formatter;

public class FormattingException extends Exception {

	public FormattingException(String s) {
		this(s, null);
	}

	public FormattingException(String s, Throwable cause) {
		super(s, cause);
	}

}

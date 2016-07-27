package net.sourceforge.barbecue.output;

public class OutputException extends Exception {

	public OutputException(String msg) {
		super(msg);
	}

	public OutputException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public OutputException(Throwable cause) {
		super(cause);
	}
}

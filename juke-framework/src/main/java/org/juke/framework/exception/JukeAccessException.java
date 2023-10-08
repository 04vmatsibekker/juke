package org.juke.framework.exception;

import java.io.IOException;

public class JukeAccessException extends IOException {
	public JukeAccessException(String text) {
		super(text);
	}
	public JukeAccessException() {
		super();
		}

}

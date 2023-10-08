package org.juke.framework.exception;

public class TunerGeneratedException extends Exception {
 
Exception wrappedException = null;
public Exception getWrappedException() {
	return wrappedException;
}
public void setWrappedException(Exception wrappedException) {
	this.wrappedException = wrappedException;
}
public TunerGeneratedException(Exception ex) {
	  wrappedException = ex;
}
}

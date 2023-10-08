package org.juke.framework.tuner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ServerResponseMock {

	private static Map<String, Exception> exceptionMap = new HashMap<>();
	static {
		
		registerException(NullPointerException.class.getSimpleName(),new NullPointerException());
		registerException(InvocationTargetException.class.getSimpleName(),new InvocationTargetException(null));
		registerException(Exception.class.getSimpleName(),new Exception());
		registerException(IOException.class.getSimpleName(),new IOException());	
		registerException(IllegalAccessException.class.getSimpleName(),new IllegalAccessException());
		registerException(ParseException.class.getSimpleName(),new ParseException("",0));
	}
	
	
	Exception exception;
	
	
	public ServerResponseMock(String exceptionClass, String response) throws Exception {
		Exception e = exceptionMap.get(exceptionClass)==null? new Exception() : exceptionMap.get(exceptionClass);
		this.exception = e;
		throw e;
	}
	
	
	
	
	public static void registerException(String name, Exception e) {
		exceptionMap.put(name,e);
	}
	public static Exception getException(String name) {
		Exception e = exceptionMap.get(name);
		if (e == null)
			e = exceptionMap.get("Exception");
		return e;
	}
	
	
}

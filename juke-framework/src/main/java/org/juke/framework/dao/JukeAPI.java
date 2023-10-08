package org.juke.framework.dao;

import java.util.Set;

import org.juke.framework.exception.JukeAccessException;


public interface JukeAPI {
	

<T> T  readFromFile(Class<T> c,String identifer) throws JukeAccessException;

boolean writeTofile(String identifer, String o) throws Exception;

String write() throws JukeAccessException;
String path();
Set<String> getFileNames();
String asString(String identifier) throws JukeAccessException;
	

}

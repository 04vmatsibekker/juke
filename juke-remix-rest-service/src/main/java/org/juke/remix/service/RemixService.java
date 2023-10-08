package org.juke.remix.service;

import org.juke.framework.exception.JukeAccessException;

public interface RemixService {

	String beginjuke(String testRun) throws JukeAccessException;
	String endJuke() throws JukeAccessException;
	
	String beginReplay(String testRun) throws JukeAccessException;
 
	String enable() throws JukeAccessException;
	String disable() throws JukeAccessException;
	String path();
	
	void remixDelaySchedule(String classAndMethodSequence, long waitTimeInMillis);
	void remixExceptionSchedule(String classAndMethodSequence, String exceptionType, String exeptionMessages) throws Exception;

}

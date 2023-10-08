package org.juke.framework.scheduler;

import java.util.HashMap;

public class ScheduleFactory {
	public static HashMap<String,DataProgramRecording> recordings=new HashMap<>();
	
	public static ScheduleFactory instance = new ScheduleFactory();
	
	public DataProgramRecording get(String recording) {
		return recordings.get(recording);
		
	}
	public DataProgramRecording add(String recording, DataProgramRecording content) {
		return recordings.put(recording, content);
		
	}
	

}

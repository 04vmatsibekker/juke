package org.juke.framework.scheduler;

import java.util.HashMap;

public class DataProgramState {
	public static HashMap<String, DataProgramSchedule > schedules;

	public static DataProgramRecording recording;

	
	public static DataProgramRecording recording() {
		return recording;
	}
	public static void recording(DataProgramRecording recording) {
		DataProgramState.recording = recording;
	}
	public static DataProgramSchedule schedule(String sessionId) {
		return schedules.get(sessionId);
	}
	public static DataProgramSchedule add(String sessionId,DataProgramSchedule schedule) {
		return schedules.put(sessionId, schedule);
	}
	public static HashMap<String, DataProgramSchedule> schedules() {
		return schedules;
	}

	public static void schedules(HashMap<String, DataProgramSchedule> schedules) {
		DataProgramState.schedules = schedules;
	}
	
	

}

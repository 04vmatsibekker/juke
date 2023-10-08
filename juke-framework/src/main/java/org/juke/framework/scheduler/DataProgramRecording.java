package org.juke.framework.scheduler;

import java.util.HashMap;

public class DataProgramRecording {
	private static  HashMap<String, DataProgram> recordings= new HashMap<>();

	public static HashMap<String, DataProgram> getRecordings() {
		return recordings;
	}

	public static void setRecordings(HashMap<String, DataProgram> recordings) {
		DataProgramRecording.recordings = recordings;
	}
	
	public static void add(String name, DataProgram recording) {
		recordings.put(name,recording);
	}
	
	public static void recording(String name) {
		recordings.get(name);
	}
}

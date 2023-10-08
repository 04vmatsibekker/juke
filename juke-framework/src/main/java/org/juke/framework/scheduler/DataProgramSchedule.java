package org.juke.framework.scheduler;

import java.util.HashMap;

public class DataProgramSchedule {
	
	private HashMap<String, DataProgram> requestsIndexMap = new HashMap<>();
		
	public DataProgram getProgram(String entry) {
		if(requestsIndexMap.containsKey(entry))
			return requestsIndexMap.get(entry);
		
		DataProgram dp= new DataProgram();
		requestsIndexMap.put(entry,dp);
		return dp;
	}
	public int add(String entry) {

		DataProgram program=getProgram(entry);
		
		int newindex = program.getIndex() + 1;
		program.setIndex(newindex);
		if (program.getLength() < newindex)
			program.setLength(newindex);
		
		requestsIndexMap.put(entry, program);
		return newindex;
	}
	public int increment(String entry) {

		DataProgram program=getProgram(entry);
		
		int newindex = program.getIndex() + 1;
		if (program.getLength() < newindex)
			newindex=program.getLength();
	
		program.setIndex(newindex);
		
		requestsIndexMap.put(entry, program);
		return newindex;
	}	
	public int current(String entry) {

		DataProgram program=getProgram(entry);
		
		return program.getIndex() ;
			
		
	}	
	public int next(String entry) {

		DataProgram program=getProgram(entry);
		
		int newindex = program.getIndex() + 1;
		
		if (newindex > program.getLength())
			return program.getLength();
		
		return newindex;
	}	
	
	public int size( String entry) {
		DataProgram program=getProgram(entry);
		
		return program.getLength();
		
		
	}
	

	public String getNextAvailable(String unsequencedEntry){
		String result= unsequencedEntry+ "." + (current(unsequencedEntry));
		increment(unsequencedEntry);
		return result;
		
		
	}


}

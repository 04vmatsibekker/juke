package org.juke.framework.tuner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public abstract class TunerTask {
	
	
	public static Map<String,Set<String>> participants = new HashMap<String, Set<String>>();
	
	public Class tuner=null; //must be set during derived class constructor
	
	public static Map<String,Set<String>> getParticipants() {
		  
		return participants;
	}

	public static void setParticipants(Map<String,Set<String>> p) {
		participants = p;
	}
	public static void add(TunerTask tunerTask, String sequencedItem) {
		TunerTaskRegistry.register(tunerTask);
		String className=tunerTask.getClass().getCanonicalName();
		if (participants.containsKey(className)) {
			participants.get(className).add(sequencedItem);
		}else {
			Set<String> participantSet=new HashSet<String>();
			participantSet.add(sequencedItem);
			participants.put(className, participantSet);
		}
		
	}
	 
	public static void clear() {
		getParticipants().forEach((key,value) ->{
			value.clear();
		});
	}
	public final void executeWith(ProcessObject obj) throws Exception {
		if (this.tuner == null) {
			throw new Exception("Failed to find a registered Tuner class. Add this.tuner = this.getClass(); into Tuner constructor");
		}
		if (getParticipants().get(this.tuner.getCanonicalName()).contains(obj.getSignature())) {
			execute(obj);
		}
		
	
	



}
	public abstract void execute(ProcessObject obj) throws Exception;
	
	
}

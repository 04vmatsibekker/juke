package org.juke.framework.reflection;

import org.juke.framework.exception.TunerGeneratedException;
import org.juke.framework.tuner.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

;


public class TunerTest {

	
	
	
	@Test
	void testTune1()  {
		//JukeCommandProcessorChain.getInstance().execute(null)
		String signature1 = ISampleService.class.getCanonicalName()+".$getMyDataMapAsList.1";
		String signature2 = ISampleService.class.getCanonicalName()+".$getMyDataMapAsList.2";
		String signature3 = ISampleService.class.getCanonicalName()+".$getMyDataMapAsList.3";
		
		
		DelayTunerTask tuner1 = new DelayTunerTask.Builder(signature1, 2000).build();
		DelayTunerTask tuner2 = new DelayTunerTask.Builder(signature2, 3000).build();
		ExceptionTunerTask tuner3 = new ExceptionTunerTask.Builder(signature3, IOException.class.getSimpleName()).build();
		assertEquals(TunerTask.getParticipants().size(), 2);
		assertEquals(TunerTask.getParticipants().get(DelayTunerTask.class.getCanonicalName()).size(),2);
		assertEquals(TunerTask.getParticipants().get(ExceptionTunerTask.class.getCanonicalName()).size(),1);
		
		
		JukeCommandProcessorChain chain=new JukeCommandProcessorChain();
		ProcessObject po=new ProcessObject();
		po.signature=signature2;
		po.json= "{}";
		long start = new Date().getTime();
		try {
			JukeCommandProcessorChain.execute(po);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = new Date().getTime();
		assertTrue((end-start)>2000);
		assertTrue((end-start)<3000);
		
		
		po.signature=ISampleService.class.getCanonicalName()+".$getMyDataMapAsList.0";
		po.json= "{}";
		 start = new Date().getTime();
		try {
			JukeCommandProcessorChain.execute(po);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 end = new Date().getTime();
		assertTrue((end-start)<100);

		
		
		IOException ioe=null;
		try {
			ProcessObject expo=new ProcessObject();
			expo.signature=signature3;
			expo.json= "{}";
			JukeCommandProcessorChain.execute(expo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e instanceof TunerGeneratedException) {
				TunerGeneratedException ex=(TunerGeneratedException) e;
				
				ioe=(IOException) ex.getWrappedException();;
			}
			else {
				e.printStackTrace();
			}
		}
		assertNotNull(ioe);
		
		TunerTask.clear();
		assertEquals(TunerTask.getParticipants().get(DelayTunerTask.class.getCanonicalName()).size(),0);
		
				
	}
	
}

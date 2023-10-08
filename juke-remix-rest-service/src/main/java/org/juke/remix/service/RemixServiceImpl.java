package org.juke.remix.service;

import org.springframework.stereotype.Service;
import org.juke.framework.api.ReplayHandler;
import org.juke.framework.api.JukeFactory;
import org.juke.framework.api.JukeState;
import org.juke.framework.configuration.ConfigUtil;
import org.juke.framework.dao.JukeHelper;
import org.juke.framework.dao.JukeZipDAOImpl;
import org.juke.framework.exception.JukeAccessException;
import org.juke.framework.tuner.DelayTunerTask;
import org.juke.framework.tuner.ExceptionTunerTask;

@Service
public class RemixServiceImpl implements RemixService {

	
	public static final String JUKEPATH="juke.path";
	
	public static final String TESTRUN="testRun";
	
	public RemixServiceImpl() {
	//	RemixUtil.copyjukeFromResourceFolder("juke");
	}
	

	
	@Override
	public String beginjuke(String testRun) throws JukeAccessException {
		// TODO Auto-generated method stub
		System.setProperty(JUKEPATH, TESTRUN);
		JukeFactory.setGlobaljuke(JukeState.JUKE);
		JukeHelper.setJukeDao(new JukeZipDAOImpl(ConfigUtil.getJukePath(),TESTRUN));
		return  RemixUtil.OK;
	}

	@Override
	public String endJuke() throws JukeAccessException {
		// TODO Auto-generated method stub
		JukeFactory.setGlobaljuke(JukeState.NONE);
		System.setProperty(JUKEPATH,"");
		JukeHelper.getJukeDAO().write();
		return  RemixUtil.OK;
	}


	@Override
	public String beginReplay(String testRun) throws JukeAccessException {
		// TODO Auto-generated method stub
		String whiteListed = RemixUtil.getWhiteList(testRun);
		if (whiteListed != null) {
			System.setProperty("replayPath", whiteListed);
			System.setProperty("juke.zip", whiteListed);
			
		}else {
			return "NOK- "+testRun+ " is not a valid test";
		}
		JukeFactory.setGlobaljuke(JukeState.REPLAY);
		JukeZipDAOImpl impl = new JukeZipDAOImpl(ConfigUtil.getJukePath(), whiteListed);
		JukeHelper.setJukeDao(impl);
		JukeFactory.resetReplay();
		if (!impl.exists())
			return  RemixUtil.NOK;
		
		return  RemixUtil.OK;
	}

 

	@Override
	public String enable() throws JukeAccessException {
		// TODO Auto-generated method stub
		JukeFactory.setGlobaljuke(JukeState.REPLAY);
		JukeState.GLOBAL_DISABLE=false;
		return RemixUtil.OK;
	}

	@Override
	public String disable() throws JukeAccessException {
		// TODO Auto-generated method stub
		JukeFactory.setGlobaljuke(JukeState.REPLAY);
		JukeState.GLOBAL_DISABLE=true;
		ReplayHandler.resetReplay();
		return  RemixUtil.OK;
	}

	@Override
	public String path() {
		// TODO Auto-generated method stub
		return JukeHelper.getJukeDAO().path();
	}

	@Override
	public void remixDelaySchedule(String classAndMethodSequence, long waitTimeInMillis) {
		// TODO Auto-generated method stub
		new DelayTunerTask.Builder(classAndMethodSequence, waitTimeInMillis).build();
		//ReplayHandler.getTrack().setDelay(classAndMethodSequence,new DelayTunerTask.Builder(classAndMethodSequence, waitTimeInMillis).build());
	}

	@Override
	public void remixExceptionSchedule(String classAndMethodSequence, String exceptionType, String exeptionMessages)
			throws Exception {
		// TODO Auto-generated method stub
		new ExceptionTunerTask.Builder(classAndMethodSequence, exceptionType).build();
		
		
	}



}

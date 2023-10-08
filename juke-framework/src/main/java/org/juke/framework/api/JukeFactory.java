package org.juke.framework.api;

import org.juke.framework.configuration.ConfigUtil;
import org.juke.framework.dao.JukeHelper;
import org.juke.framework.dao.JukeZipDAOImpl;
import org.juke.framework.exception.JukeAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class JukeFactory<T> {
	private static Logger log = LoggerFactory.getLogger(JukeFactory.class);
	
	static {
		final String propVal = System.getProperty(JukeState.JUKE);
		if (!JukeState.IGNORE.equals(JukeState.GLOBAL_JUKE)) {
			
		
			JukeState.GLOBAL_JUKE= (propVal==null || !propVal.equalsIgnoreCase(JukeState.RECORD)
				&& !propVal.equalsIgnoreCase(JukeState.REPLAY))? JukeState.IGNORE:propVal;
		}
			try {
				JukeHelper.setJukeDao(new JukeZipDAOImpl(ConfigUtil.getJukePath(),
					System.getProperty(JukeState.JUKEZIP)!=null? System.getProperty(JukeState.JUKEZIP):"track"));
			} catch (JukeAccessException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
		
	}
	
	private static HashMap<Class,Object> jukeMockCache = new HashMap<>();
	
	public static void setGlobaljuke(String global) {
		JukeState.GLOBAL_JUKE=global;
	}
	public static void resetReplay() {
		ReplayHandler.resetReplay();
	}
	
	public static String getJukeSate(String juke) {
		if (juke==null) return null;
		
		if (JukeState.GLOBAL_JUKE !=null && ( JukeState.GLOBAL_JUKE.equalsIgnoreCase(JukeState.RECORD)
				||JukeState.GLOBAL_JUKE.equalsIgnoreCase(JukeState.REPLAY)) &&
				(!JukeState.NONE.equalsIgnoreCase(juke))) {
			return JukeState.GLOBAL_JUKE;
			
		} else if (JukeState.GLOBAL_JUKE !=null &&  JukeState.GLOBAL_JUKE.equalsIgnoreCase(JukeState.IGNORE)) {
			return JukeState.NONE;
		}else {
			return juke;
		}
	}
	
	public synchronized T newInstance(T wrapped, Class clazz, String jukeInput) {
		
		try{
			String juke = getJukeSate(jukeInput);
			if (juke != null) {
				if (juke.equalsIgnoreCase(JukeState.REPLAY)) {
					T result =null;
					if (jukeMockCache.get(clazz) != null) {
						result = (T) jukeMockCache.get(clazz);
						
					}else {
						ReplayHandler<T> handler = ReplayHandler.getReplayHandlerCache().get(clazz);
						if (handler == null)
							handler= new ReplayHandler<T>(wrapped, clazz);
						result = handler.newInstance(clazz);
					}
					
					jukeMockCache.put(clazz,  result);
					return result == null ? wrapped: result;
					
				}else if (juke.equalsIgnoreCase(JukeState.RECORD)) {
					T result= new RecordHandler<T>().newInstance(wrapped, clazz);
					return result;
				}
				else
					return wrapped;
			} else {
				return wrapped;
			}
			
		}catch (Exception yae) {
			log.error("can not find juke at " + ConfigUtil.setDefauljukePath()+"/"+System.getProperty(JukeState.JUKEZIP));
			return wrapped;
		}
	}
}

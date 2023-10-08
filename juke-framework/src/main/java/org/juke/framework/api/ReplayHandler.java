package org.juke.framework.api;

import org.juke.framework.configuration.ConfigUtil;
import org.juke.framework.dao.JukeHelper;
import org.juke.framework.dao.JukeZipDAOImpl;
import org.juke.framework.exception.JukeAccessException;
import org.juke.framework.exception.TunerGeneratedException;
import org.juke.framework.mapper.pojo.JukeClass;
import org.juke.framework.mapper.pojo.JukeMethod;
import org.juke.framework.mapper.util.JukeConfigBuilder;
import org.juke.framework.mapper.util.JukeParser;
import org.juke.framework.scheduler.DataProgramSchedule;
import org.juke.framework.scheduler.JukeStateBuilder;
import org.juke.framework.tuner.JukeCommandProcessorChain;
import org.juke.framework.tuner.ProcessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
public class ReplayHandler<T> extends BaseHandler<T> {
	private static Logger log = LoggerFactory.getLogger(ReplayHandler.class);
	
	private T proxy;
	private T originalService=null;

	private static HashMap<Class, ReplayHandler> replayHandlerCache = new HashMap<>();
	DataProgramSchedule schedule;
	
	
		
	static {
		final String propVal = System.getProperty(JukeState.JUKE);
		JukeState.GLOBAL_JUKE= (propVal==null || !propVal.equalsIgnoreCase(JukeState.RECORD)
				&& !propVal.equalsIgnoreCase(JukeState.REPLAY))? JukeState.IGNORE:propVal;
		
		try {
			JukeHelper.setJukeDao(new JukeZipDAOImpl(ConfigUtil.getJukePath(),
					System.getProperty(JukeState.JUKEZIP)!=null? System.getProperty(JukeState.JUKEZIP):"track"));
			
			
		} catch (JukeAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public ReplayHandler(T service, Class clazz) throws JukeAccessException{
		
		
		if (!replayHandlerCache.containsKey(clazz)) {
			replayHandlerCache.put(clazz,  this);
			
		}
		this.originalService= service;

		this.setInterfaceClass(clazz);

		this.setup();
	}
	
	
	
	
	

	public static void resetDelays() {
	//	track = new JukTrack();
	}
	public T getProxy() {
		return proxy;
	}

	public void setProxy(T proxy) {
		this.proxy = proxy;
	}

	public T getOriginalService() {
		return originalService;
	}

	public void setOriginalService(T originalService) {
		this.originalService = originalService;
	}
	
	public static void resetReplay() {
		Collection values = replayHandlerCache.values();
		Iterator iter= values.iterator();
		while (iter.hasNext()) {
			ReplayHandler handler= (ReplayHandler) iter.next();
			handler.reset();
		}
	}
	public static HashMap<Class, ReplayHandler> getReplayHandlerCache(){
		return replayHandlerCache;
	}
	public synchronized void reset() {
		
		this.setInitialized(false);
		ReplayHandler handler= getReplayHandlerCache().get(this.getInterfaceClass());
		if (handler != null)
			handler.setInitialized(false);
		resetDelays();
	}
	
	
	public static Class getMockClass(Class instanceClass, Class interfaceClass) {
		
		if (Arrays.asList(instanceClass.getInterfaces()).contains(interfaceClass))
			return instanceClass;
		else if (instanceClass.equals(Object.class))
			return instanceClass;
		else 
			return getMockClass(instanceClass.getSuperclass(),interfaceClass);
	}
	
	public synchronized void setup() throws JukeAccessException{
		
		
		if (this.isInitialized())
			return;
		
		
		System.setProperty("juke.path",ConfigUtil.getJukePath());
		if (System.getProperty("juke.zip") == null) {
			System.setProperty("juke.zip","juke");
		}
		
		JukeHelper.setJukeDao(new JukeZipDAOImpl(ConfigUtil.getJukePath(),System.getProperty("juke.zip")));
	
		//String JukeClassTxt=JukeHelper.getJukeDAO().asString( "juke");
		Class clazz=originalService.getClass();
		
		//builds juke class form juke.json
		
		if (!JukeClass.instance().containsKey(this.getInterfaceClass().getCanonicalName())) {
			JukeClass JukeClass=  JukeConfigBuilder.set(this.getInterfaceClass()).build();
			JukeClass.instance().put(clazz.getCanonicalName(),JukeClass);
			setJukeClass(JukeClass);
		}
		//set schedule
		JukeStateBuilder built= new JukeStateBuilder.Builder(JukeHelper.getJukeDAO().getFileNames()).build();
		this.schedule = built.getSchedule();
		this.setInitialized(true);
	
	}
	
	

	
	public T newInstance(Class clazz) {
		ReplayHandler<T> handler = null;
		if (!replayHandlerCache.containsKey(clazz)){
			replayHandlerCache.put(clazz, this);
		}
	
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[] {clazz},replayHandlerCache.get(clazz));
		
	
	}
	
	
	
	@Override
	public synchronized Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Object serviceInstance = JukeState.GLOBAL_DISABLE ? this.originalService: this.getService();
		Object result = null;
		setup();
		Method[] methods = proxy.getClass().getInterfaces()[0].getMethods();
		List<Method> foundMethods = JukeParser.isInMethods(m.getName(), methods);
		String callerFullName= JukeParser.getFullName(this,m);
		TunerGeneratedException tge=null;
		ProcessObject po=new ProcessObject();
		try {
			if (foundMethods != null & foundMethods.size() >0) {
				log.debug(m.getName() + " needs special handling by juke due to overloading");		
				
			}
			for (int i=0; i < foundMethods.size(); i++) {
				if (JukeParser.isAssignableFromArguments(foundMethods.get(i),args)
						&& (callerFullName.equalsIgnoreCase(JukeParser.getFullName(this,m)))
						) {
					try {
						String fullName= JukeParser.getFullName(this,m);
					
						String sequencedFullName=this.schedule.getNextAvailable(fullName);
						JukeClass instance=JukeClass.instance().get(this.getInterfaceClass().getCanonicalName());
						String methodName=foundMethods.get(i).getName();
						List<JukeMethod> JukeMethods=instance.getMethodsByName(methodName);
					
						//String sequencedName=schedule.getNextAvailable(fullName);
						String json= JukeHelper.getJukeDAO().asString( sequencedFullName);
						
						result= JukeHelper.getJukeDAO().readFromFile( this.getInterfaceClass(),sequencedFullName);
					//	Integer seq=track.getSequnce(fullName);
					//	track.increment(fullName);
						po.setSignature(sequencedFullName);
						po.setJson(json);;
						po.setObject(result);
						JukeCommandProcessorChain.execute(po);
						
						break;
					}
					catch (TunerGeneratedException tgexception) {
						tge = tgexception;
					}
					catch (Exception e) {
						log.info("Exception found" + e.getClass().getSimpleName() + "->"+e.getMessage());
					}
				}
			}
			log.debug("Completed extracting result from file "+m.getName());
		}catch (Exception e) {
			throw new RuntimeException ("Unexpected invocation exception: "+e.getMessage());
		}finally {
			log.info("after method " + m.getName());
		}
		if (tge != null) {
			throw tge.getWrappedException();
		}
		return result;
	}

}

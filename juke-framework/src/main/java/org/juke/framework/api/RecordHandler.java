package org.juke.framework.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.juke.framework.dao.JukeHelper;
import org.juke.framework.exception.JukeAccessException;
import org.juke.framework.mapper.pojo.JukeClass;
import org.juke.framework.mapper.util.JukeConfigBuilder;
import org.juke.framework.mapper.util.JukeParser;


import java.lang.reflect.Proxy;
public class RecordHandler<T> extends BaseHandler<T> {
	private static Logger logger = LoggerFactory.getLogger(RecordHandler.class);
	
	
	
	public T newInstance(T service, Class clazz) {
		
		ArrayList<Class> interfaces = new ArrayList<>();
		interfaces.addAll(Arrays.asList(service.getClass().getInterfaces()));
		if (!Arrays.asList(service.getClass().getInterfaces()).contains(clazz) && clazz.isInterface()) {
			interfaces.add(clazz);
		}
		Class[] classes = interfaces.toArray(new Class[interfaces.size()]);
		
		
		T proxy = (T) Proxy.newProxyInstance(service.getClass().getClassLoader(),classes,
				new RecordHandler<T>(service, clazz));
		
		return proxy;
	}
	
	public RecordHandler() {
		
	}
	public RecordHandler(T service, Class clazz) {
		this.setService(service);
		this.setInterfaceClass(clazz);
		if (!JukeClass.instance().containsKey(clazz.getCanonicalName())) {
			JukeClass JukeClass=  JukeConfigBuilder.set(clazz).build();
			JukeClass.instance().put(clazz.getCanonicalName(),JukeClass);
			setJukeClass(JukeClass);
		}
	
		
	}
	@Override
	public synchronized Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		if (m.getName().equals("toString")) return super.toString();
		
		List<Method> interfaceMethods= JukeParser.isInMethods(m.getName(), this.getInterfaceClass().getMethods());
		boolean isSpecial=false;
		Object result=null;
		String methodName = null;
		try {
			methodName= JukeParser.getMethodName(this, m);
			if (interfaceMethods != null && interfaceMethods.size() >0) {
				isSpecial=true;
				logger.debug(m.getName()+" juke this one need special handling");
			}
			interfaceMethods=JukeParser.isInMethods(m.getName(), this.getService().getClass().getMethods());
			boolean success = false;
			Exception exception =null;
			for (int i=0; i < interfaceMethods.size(); i++) {
				if (JukeParser.isAssignableFromArguments(interfaceMethods.get(i), args)) {
					
					try {
						if (args == null) {
							result = interfaceMethods.get(i).invoke(this.getService());
						}else {
							result = interfaceMethods.get(i).invoke(this.getService(),args);
						}
						success = true;
						break;
					}catch (Exception e) {
						exception = e;
					}
				}
			}
			if (success == false)
				throw new JukeAccessException("Unable to find interface for "+this.getService().getClass()+"."+methodName);
			
		}catch (Exception e) {
			throw new RuntimeException("unexpected invocation exceptione ",e);
		}finally {
			logger.debug("after method "+methodName);
		}
		
		if (result !=null && isSpecial) {
				
			JukeHelper.getInstance().writeToFile(this.getInterfaceClass().getName()+".$"+methodName, result);
		}
		return result;
	}

}

package org.juke.framework.api;

import java.lang.reflect.InvocationHandler;

import org.juke.framework.mapper.pojo.JukeClass;

public abstract class BaseHandler<T> implements InvocationHandler {
	
	boolean isInitialized = false;
	private T service;
	private Class interfaceClass;
	private JukeClass JukeClass;
	
	public JukeClass getJukeClass() {
		return JukeClass;
	}
	public void setJukeClass(JukeClass JukeClass) {
		this.JukeClass = JukeClass;
	}
	public boolean isInitialized() {
		return isInitialized;
	}
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	public T getService() {
		return service;
	}
	public void setService(T service) {
		this.service = service;
	}
	public Class getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	

	 

}

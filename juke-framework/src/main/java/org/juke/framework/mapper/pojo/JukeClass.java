package org.juke.framework.mapper.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JukeClass {
	private static HashMap<String,JukeClass> classes=new HashMap<>();
	

String className = null;
List<JukeMethod> methods=new ArrayList<JukeMethod>();

public static HashMap<String,JukeClass> instance() {
	return classes;
}
public String getClassName() {
	return className;
}

public void setClassName(String className) {
	this.className = className;
}
public List<JukeMethod> getMethods() {
	return methods;
}

public void setMethods(List<JukeMethod> methods) {
	this.methods = methods;
}

public  List<JukeMethod> getMethodsByName(String name) {
	 List<JukeMethod> result= (List<JukeMethod>) this.getMethods().stream()
			.filter(method -> method.getMethod().equals(name)).collect(Collectors.toList());
	 
	 return result;
	
}

}

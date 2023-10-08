package org.juke.framework.mapper.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.juke.framework.mapper.pojo.JukeClass;
import org.juke.framework.mapper.pojo.JukeMethod;
import org.juke.framework.mapper.pojo.JukeParameter;
import org.juke.framework.mapper.pojo.JukeParameterizedType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JukeConfigBuilder {

	Class interfaceClass;
	static ObjectMapper objectMapper ;
	
	static {
		
		objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}


	public static JavaType constructParameterizedType(JukeParameterizedType pt) throws ClassNotFoundException {
		
		return JukeParameterizedTypeBuilder.fromJukeParameterizedType(pt);
		
			
		
	}
	
	public static JavaType constructParameterizedType(ParameterizedType pt) throws ClassNotFoundException {
		List <Class> ptc= new ArrayList<Class> ();
		List <Type> ptj= new ArrayList<Type> ();
		JavaType jtype= null;
		Type[] actualTypeArguments= pt.getActualTypeArguments();
		Type raw=pt.getRawType();
		int arguments = actualTypeArguments.length;
		if (arguments == 0) {
			return objectMapper.getTypeFactory().constructFromCanonical(raw.getTypeName());
					
		}
		if (arguments == 1 && (actualTypeArguments[0] instanceof ParameterizedType)) {
			 jtype=constructParameterizedType((ParameterizedType) actualTypeArguments[0] );
			 return objectMapper.getTypeFactory().constructParametricType(Class.forName(raw.getTypeName()),jtype);
		}else {
			for (Type type:  actualTypeArguments) {
				Class cl= Class.forName(type.getTypeName());
 
				ptc.add(cl);
			}
			Class [] classes= ptc.toArray(new java.lang.Class[ptc.size()]);
			return objectMapper.getTypeFactory().constructParametricType(Class.forName(raw.getTypeName()),classes);
		}
			
		
	}
	public static JavaType constructType(JukeParameter yp ) throws ClassNotFoundException {
		
		TypeFactory factory = objectMapper.getTypeFactory();
		List<JavaType> arguments= new ArrayList<JavaType>();
		if (yp.getList().size() > 0) {
			yp.getList().forEach(item ->{
				
				try {
					JavaType jt = constructType(item);
					arguments.add(jt);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			 
		}
		JavaType full = null;
		JavaType inner = null;
		if (yp.isParameterized())
		{
			inner = factory.constructParametricType(Class.forName(yp.getClassName())
					, arguments.toArray(new JavaType[arguments.size()]));
			full = factory.constructParametricType(Class.forName(yp.getClassName()), inner);
		}else if  (yp.isArray()) {
			full = factory.constructArrayType(Class.forName(yp.getClassName()));
	
			}
		else if (yp.getType() != null) {
			full= constructParameterizedType(yp.getType());
			full= constructParameterizedType(yp.getType());
		}
		else {
			full = factory.constructType(Class.forName(yp.getClassName()));
		}
		 


		

		return full;
	}
	public JukeConfigBuilder(Class interfaceClass) {
		this.interfaceClass=interfaceClass;
	}
	
	
	public JukeClass  build() {
		JukeClass c = new JukeClass();
		c.setClassName(interfaceClass.getCanonicalName());
	
		List<JukeMethod> JukeMethods = setJukeMethods(interfaceClass);
		c.setMethods(JukeMethods);
		JukeClass.instance().put(interfaceClass.getCanonicalName(), c);
		return c;
	}
	
	
	public List<JukeMethod> setJukeMethods(Class interfaceClass) {
		List<JukeMethod> JukeMethods = new ArrayList<JukeMethod>();
		for (Method method: interfaceClass.getDeclaredMethods()) {
		
			JukeMethod ym = new JukeMethod();
			int signature=JukeParser.buildParameterSignature(method);
			ym.setOverloadedSignature(signature);
			ym.setMethod(method.getName());
			List <JukeParameter> JukeParameters = new ArrayList<JukeParameter>();
		 
			for (java.lang.reflect.Type type: method.getParameterTypes()) {
				JukeParameter parameter = new JukeParameter();
				String name = type.getTypeName() ;
				System.out.println(name);
				if (name.endsWith("[]")) {
					parameter.setArray(true);	
					name=cleanArray(name);
				}		
							
				name = JukeConfigBuilder.convertSimpleTypeToObject(name);
				name = cleanArray(name);
				parameter.setClassName(name);
				JukeParameters.add(parameter);
			}
			ym.setInputParameters(JukeParameters);
			JukeParameter  parameter = new JukeParameter();
		 
			Class returnType =  method.getReturnType();
			if (method.getGenericReturnType() instanceof ParameterizedType) {
				ParameterizedType pt=(ParameterizedType) method.getGenericReturnType();
				JukeParameterizedType ypt= JukeParameterizedTypeBuilder.fromParameterizedType(pt);
				parameter.setType(ypt);
				// parameter.setType((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl )pt);
			}else {
				Type pt=(Type) method.getGenericReturnType();
				JukeParameterizedType ypt= JukeParameterizedTypeBuilder.fromParameterizedType(pt);
				
				parameter.setType(ypt);
			}
			
			
			String name =  returnType.getTypeName();
			if (name.endsWith("[]")) {
				
				name = name.substring(0, name.length() - 2);
				
				parameter.setArray(true);	
			}
			name = JukeConfigBuilder.convertSimpleTypeToObject(name);
				
			name = cleanArray(name);
			parameter.setClassName(name);
			ym.setOutputResult(parameter);
			JukeMethods.add(ym);
			 
		}
		 
		
		 
		
		return JukeMethods;
	}
	
	
	public static String toJSON(JukeClass interfaceClass) throws JsonProcessingException {
	   return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(interfaceClass);
		
	}
	public static JukeClass fromJSON(String json) throws JsonMappingException, JsonProcessingException {
		   return objectMapper.readValue(json, JukeClass.class);
			
		}
	public static JukeConfigBuilder set(Class c) {
		 JukeConfigBuilder instance = new JukeConfigBuilder(c);
			 
			return instance;
	}
	private static String cleanArray(String name) {
		if (name.endsWith("[]")) {
			return name.substring(0,name.length() - 2);
			
		}
		return name;
		
	}
	public static String convertSimpleTypeToObject(String simpleType) {
		String post="[]";
		if (simpleType.endsWith(post)) {
			simpleType = cleanArray(simpleType);
			
		}else {
			post="";
		}
		
		switch (simpleType) {
			case "int":
				return "Integer" + post;
				
			case "double":
				return "Double" + post;
			case "boolean":
				return "Boolean" + post;
			
			case "long":
				return "Long" + post;
		
			case "char":
				return "Character" + post;
			case "float":
				return "Float" + post;
			case "short":
				return "Short" + post;
			case "byte":
				return "Byte" + post;
			default:
				return simpleType + post;
			
		} 
		
	}
}

package org.juke.framework.mapper.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.juke.framework.mapper.pojo.JukeParameterizedType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.JavaType;

public class JukeParameterizedTypeBuilder {
	
static ObjectMapper objectMapper ;
	
	static {
		
		objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}
	
	public static final String RAWTYPE="rawType";
	public static final String ACTUALTYPEARGUMENTS="actualTypeArguments";
	
	public static JukeParameterizedType fromParameterizedType(Type type) {
		JukeParameterizedType ptype = new JukeParameterizedType();
		
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType)type;
			ptype.setRawType(parameterizedType.getRawType().getTypeName());		
			Type[] types=parameterizedType.getActualTypeArguments();
			for (Type t: types) {
				if (t instanceof ParameterizedType) {
					ptype.getActualTypeArguments().add(fromParameterizedType((ParameterizedType) t));
					
					
				}else {
					JukeParameterizedType ypt=new JukeParameterizedType();
					ypt.setRawType(t.getTypeName());
					ptype.getActualTypeArguments().add(ypt);	
				}
				
			}	
		}else {
			String name = type.getTypeName();
			if (isArray(name)){
				ptype.setArray(true);
				name = cleanArray(name);			
			}
			
			ptype.setRawType(name);
		
			
		}
		return ptype;		
	}
	public static boolean isArray(String rawType) {
		if (rawType.endsWith("[]")) {
			return true;
		}
		return false;
	}
	public static String cleanArray(String rawType) {
		if (rawType.endsWith("[]")) {
			 return rawType.substring(0,rawType.length() -2 );
					 
		}
		return rawType;
	}
	public static Class<?> parseType(final String className) throws ClassNotFoundException {
	    switch (className) {
	        case "boolean":
	            return boolean.class;
	        case "byte":
	            return byte.class;
	        case "short":
	            return short.class;
	        case "int":
	            return int.class;
	        case "long":
	            return long.class;
	        case "float":
	            return float.class;
	        case "double":
	            return double.class;
	        case "char":
	            return char.class;
	        case "void":
	            return void.class;
	        default:
	            String fqn = className.contains(".") ? className : "java.lang.".concat(className);
	            try {
	                return Class.forName(fqn);
	            } catch (ClassNotFoundException ex) {
	                throw new ClassNotFoundException("Class not found: " + fqn);
	            }
	    }
	}
	public static JavaType fromJukeParameterizedType(JukeParameterizedType type) throws ClassNotFoundException {
		Class rawType= null;
	
		rawType = parseType(cleanArray(type.getRawType()));
			
		
		List<Class> argStringList= new ArrayList<Class>();
		List<JavaType> argJavaTypeList= new ArrayList<JavaType>();
	
		type.getActualTypeArguments().forEach(arg -> {
			if (arg.getActualTypeArguments().size()== 0) {
				String argString=arg.getRawType();
		
				Class c;
				try {
					c = parseType(argString);
					argStringList.add(c);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				 
			}else  {
				
				JavaType jc;
				try {
					jc = fromJukeParameterizedType((JukeParameterizedType) arg);
					argJavaTypeList.add(jc);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			 } 
			
		
		});
		JavaType jt= null;
		if (argJavaTypeList.size() >0) {
			return objectMapper.getTypeFactory().constructParametricType(rawType, argJavaTypeList.toArray(new JavaType[argJavaTypeList.size()]));
			
		}else if (argStringList.size() >0) {
			return objectMapper.getTypeFactory().constructParametricType(rawType, argStringList.toArray(new Class[argStringList.size()]));

		}else {
			if (type.isArray()) {
				return objectMapper.getTypeFactory().constructArrayType(rawType);
			}
			return objectMapper.getTypeFactory().constructFromCanonical(rawType.getCanonicalName());
		}
	 
				
	 	
	}
}

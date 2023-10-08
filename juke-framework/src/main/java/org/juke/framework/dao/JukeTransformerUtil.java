package org.juke.framework.dao;

import java.util.List;

import org.juke.framework.mapper.pojo.JukeClass;
import org.juke.framework.mapper.pojo.JukeMethod;
import org.juke.framework.mapper.pojo.JukeParameter;
import org.juke.framework.mapper.pojo.JukeParameterizedType;
import org.juke.framework.mapper.util.JukeParameterizedTypeBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JukeTransformerUtil {
	
static ObjectMapper objectMapper ;

static {
		
		objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}



   @SuppressWarnings("unchecked")
    public static  <T> T readValue(String content, JukeMethod method)
        throws JsonProcessingException, JsonMappingException, ClassNotFoundException
    {
	   
	   JukeParameter outputParameter = method.getOutputResult();
	   JukeParameterizedType ypt=  outputParameter.getType() ;
	   JavaType jt = JukeParameterizedTypeBuilder.fromJukeParameterizedType(ypt);

	   return objectMapper.readValue(content, jt);
    }

   @SuppressWarnings("unchecked")
    public static  <T> T readValue(String content, JukeClass JukeClass, String method)
        throws JsonProcessingException, JsonMappingException, ClassNotFoundException, Exception
    {
	   List<JukeMethod> methods= JukeClass.getMethodsByName(method);
		
	   //!!replace  with something that handles multiple methods with same name!!
	   if(methods.size()==0) {
		   throw new Exception("No such method "+method+" in interface class "+JukeClass.getClassName() );
	   }
	  return readValue(content, methods.get(0));

    }

   @SuppressWarnings("unchecked")
    public static  <T> T readValue(String content, JukeMethod method, JavaType valueType)
        throws JsonProcessingException, JsonMappingException, ClassNotFoundException
    {
	   
	   JukeParameter outputParameter = method.getOutputResult();
	   JukeParameterizedType ypt=  outputParameter.getType() ;
	   JavaType jt = JukeParameterizedTypeBuilder.fromJukeParameterizedType(ypt);

	   return objectMapper.readValue(content, jt);
    }

   @SuppressWarnings("unchecked")
    public static  <T> T readValue(String content, JukeClass JukeClass, String method, JavaType valueType)
        throws JsonProcessingException, JsonMappingException, ClassNotFoundException, Exception
    {
	   List<JukeMethod> methods= JukeClass.getMethodsByName(method);
	   //!!replace  with something that handles multiple methods with same name!!
	   if(methods.size()==0) {
		   throw new Exception("No such method "+method+" in interface class "+JukeClass.getClassName() );
	   }
	  return readValue(content, methods.get(0), valueType);

    }

   public static String writeValueAsString(Object value)
	        throws JsonProcessingException{
	   
	   return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	   
	   
   }
}

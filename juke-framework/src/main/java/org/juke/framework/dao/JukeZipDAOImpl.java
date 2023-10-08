package org.juke.framework.dao;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.juke.framework.api.JukeState;
import org.juke.framework.exception.JukeAccessException;
import org.juke.framework.mapper.pojo.JukeClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class JukeZipDAOImpl implements JukeAPI {
	
	protected static ObjectMapper objectMapper = new ObjectMapper();
	private static final String JSON = ".json";
	static {
		
		objectMapper.registerModule(new JodaModule());
		objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}
	private static Logger log = LoggerFactory.getLogger(JukeZipDAOImpl.class);
	public static final String juke = "juke";
	private ZipUtil zipper = null;
	HashMap<String,JukeClass> JukeClassMap= null;
	public JukeZipDAOImpl(String path, String zipName) throws JukeAccessException {
		
		// TODO Auto-generated constructor stub
		
		path = FilenameUtils.normalize(path);
		zipName = FilenameUtils.normalize(zipName);
		zipper = new ZipUtil(path, zipName);
		if(JukeState.GLOBAL_JUKE.equals(JukeState.REPLAY)){
			this.getJukeClassMap();
		}
		
	
	}
	
	
	public boolean exists() {
		return this.zipper.exists();
	}


	@Override
	public String asString(String identifier) throws JukeAccessException {
		// TODO Auto-generated method stub

		 try {
			 return zipper.readStringFromZipFile(this.zipper.getZipName(), identifier + JSON);

			 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JukeAccessException("Unable to read " +identifier + " from zip");
			
		}
	 
	}

	
	@Override
	public <T> T readFromFile(Class<T> clazz, String identifier) throws JukeAccessException {
		// TODO Auto-generated method stub
		 if (JukeClassMap ==null)
			 getJukeClassMap();
			
		
		 String text= this.zipper.readFromZipFile(identifier);
		 try {
			return (T) JukeTransformerUtil.readValue(text, JukeClassMap.get(clazz.getCanonicalName()), ZipUtil.getMethodFromZipEntryName(identifier));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JukeAccessException("Unable to read " +identifier + " for " + clazz+ " from zip");
			
		}
		
		
	 
	}
	
	public <TTarget> TTarget readFromFile(TTarget type, Class<TTarget> clazz, String identifier) throws JukeAccessException {
		// TODO Auto-generated method stub
		 if (JukeClassMap ==null)
			 getJukeClassMap();
		
		 String text= this.zipper.readFromZipFile(identifier);
		 try {
			return (TTarget) JukeTransformerUtil.readValue(text, JukeClassMap.get(clazz.getCanonicalName()), ZipUtil.getMethodFromZipEntryName(identifier));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JukeAccessException("Unable to read " +identifier + " for " + clazz+ " from zip");
			
		}
		
		
	 
	}

	public HashMap<String,JukeClass>  getJukeClassMap() throws JukeAccessException {
		// TODO Auto-generated method stub
		
		
		String JukeClassTxt= this.zipper.readFromZipFile(juke);
		
		try {
			HashMap<String,JukeClass> juke2Class=this.objectMapper.readValue(JukeClassTxt, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, JukeClass.class));
			JukeClassMap=juke2Class;
			return juke2Class;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JukeAccessException("Unable to convert juke.json to juke class map");
		}
		
		
	 
	}

	@Override
	public boolean writeTofile(String identifier, String o) throws Exception {
		// TODO Auto-generated method stub
		if (identifier.equals("juke")) {
			zipper.addEntry(identifier, o);
		}else {
			
		
			zipper.addIncrementEntry(identifier, o);
		}
		return false;
	}
	

	@Override
	public String write()  throws JukeAccessException {
		String content = null;

		
		try {

			ObjectMapper objectMapper= new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			
			 content=new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString( JukeClass.instance());

			zipper.addEntry("juke", content);
			zipper.writeToZipFile();
			
		}catch (IOException e ) {
			throw new JukeAccessException ("Unable to close zipfile ");
		}
		return zipper.getZipName();
	}
	@Override
	public String path() {
		return this.zipper.getZipName();
		
	}
	
	@Override
	public Set<String> getFileNames() {
		// TODO Auto-generated method stub
		if (this.zipper.getZipFile()==null) {
			this.zipper.open();
		}
		try {
			return zipper.getFileNamesFromZipFile(this.zipper.getZipName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

package org.juke.framework.dao;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.juke.framework.configuration.ConfigUtil;
import org.juke.framework.exception.JukeAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JukeHelper {
	private static Logger log = LoggerFactory.getLogger(JukeHelper.class);
	private static JukeAPI jukeDAO;
	private static JukeHelper instance =new JukeHelper();
	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		try {
			jukeDAO = new JukeZipDAOImpl(ConfigUtil.getJukePath(), 
					System.getProperty("juke.zip")!=null?System.getProperty("juke.zip"):"track");
		} catch (JukeAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objectMapper.registerModule(new JodaModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,true);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}
	public static JukeHelper getInstance() {
		return instance;
	}
	

	public static JukeAPI getJukeDAO() {
		return jukeDAO;
	}
	public static void setJukeDao(JukeAPI dao) {
		jukeDAO = dao;
	}
	
	
	public void write() throws JukeAccessException{
		jukeDAO.write();
	}
	public boolean writeToFile(String identifier, Object o) throws JukeAccessException {
		String content= null;
		try {
			content = JukeTransformerUtil.writeValueAsString(o);
			//JukeClass JukeClass=  JukeConfigBuilder.set(ISampleService.class).build();
			//content= objectMapper.writeValueAsString(o);
			return jukeDAO.writeTofile(identifier, content);
			
		}catch (Exception e) {
			throw new JukeAccessException ("Could not write "+ identifier);
		}
	}
}

package org.juke.framework.reflection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.juke.framework.api.JukeFactory;
import org.juke.framework.api.JukeState;
import org.juke.framework.configuration.ConfigUtil;
import org.juke.framework.dao.JukeHelper;
import org.juke.framework.dao.JukeTransformerUtil;
import org.juke.framework.dao.JukeZipDAOImpl;
import org.juke.framework.dao.ZipUtil;
import org.juke.framework.mapper.pojo.JukeClass;
import org.juke.framework.mapper.util.JukeConfigBuilder;
import org.juke.framework.scheduler.DataProgramSchedule;
import org.juke.framework.scheduler.JukeStateBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class JukeLoaderTest {
static ObjectMapper objectMapper ;
	
	static {
		
		objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
	}
	ISampleService service = new SampleService();
	@Test
	void loadTest0() throws Exception {
		JukeState.GLOBAL_JUKE=JukeState.juke;
		JukeFactory.setGlobaljuke(JukeState.juke);
		System.setProperty("juke.path",ConfigUtil.getJukePath());
		System.setProperty("juke.zip","juke2");
		JukeHelper.setJukeDao(new JukeZipDAOImpl(ConfigUtil.getJukePath(),System.getProperty("juke.zip")));
		
		ISampleService wrapped=new JukeFactory<ISampleService>().newInstance(this.service,ISampleService.class, JukeState.juke);
	
		
	
		JukeClass JukeClass=  JukeConfigBuilder.set(ISampleService.class).build();
		HashMap<String,JukeClass> juke=new HashMap<>();
		juke.put(ISampleService.class.getCanonicalName(),JukeClass);
		BigDecimal[] biggie= new BigDecimal[2];
		biggie[0]=new BigDecimal("23423423423423423423423423423423.3423423423423423");
		biggie[1]= new BigDecimal("278734384738453485837475384573485345.2342342342342342342342365");
	
		
		List<HashMap<String,BigDecimal>> listHashMapStringBiDecimal2= wrapped.getMyDataMapAsList(biggie, "testit");
		HashMap<String,BigDecimal> biggieMap = wrapped.getMyDataMap(biggie, "testit");
		JukeHelper.getJukeDAO().write();
		assert(listHashMapStringBiDecimal2.size() == 2);
		assert(biggieMap.size() == 2);
		
		new File(JukeHelper.getJukeDAO().path()).renameTo(new File("C:/temp/juke2.zip"));
		
	}
	@Test
	void loadTest1() throws Exception {
		new File("/temp").mkdirs();
		if (new File ("/temp/juke.zip").exists())
			new File ("/temp/juke.zip").delete();

		
		JukeClass JukeClass=  JukeConfigBuilder.set(ISampleService.class).build();
		HashMap<String,JukeClass> juke=new HashMap<>();
		juke.put(ISampleService.class.getCanonicalName(),JukeClass);
		BigDecimal[] biggie= new BigDecimal[2];
		biggie[0]=new BigDecimal("23423423423423423423423423423423.3423423423423423");
		biggie[1]= new BigDecimal("278734384738453485837475384573485345.2342342342342342342342365");
		List<HashMap<String,BigDecimal>> listHashMapStringBiDecimal=service.getMyDataMapAsList(biggie, "testit");
		HashMap<String,BigDecimal> biggieMap = service.getMyDataMap(biggie, "testit");
		

		String listHashMapStringBiDecimalJson = JukeTransformerUtil.writeValueAsString(listHashMapStringBiDecimal);
		String hashMapStringBiDecimalJson = JukeTransformerUtil.writeValueAsString(biggieMap);
		String tmpdir=System.getProperty("java.io.tmpdir");
		String orig=objectMapper.writeValueAsString(juke);
		//File f=new File(new File(tmpdir),ISampleService.class.getName()+".json" );
		JukeZipDAOImpl zip=new JukeZipDAOImpl("C:/temp", "juke");
		zip.writeTofile(ISampleService.class.getCanonicalName()+".$getMyDataMapAsList-1641989625", listHashMapStringBiDecimalJson);
		zip.writeTofile(ISampleService.class.getCanonicalName()+".$getMyDataMapAsList-1641989625", listHashMapStringBiDecimalJson);
		zip.writeTofile(ISampleService.class.getCanonicalName()+".$getMyDataMapAsList-1641989625", listHashMapStringBiDecimalJson);
		zip.writeTofile(ISampleService.class.getCanonicalName()+".$getMyDataMap", hashMapStringBiDecimalJson);
		zip.writeTofile("juke", objectMapper.writeValueAsString(juke));
		zip.write();

		//zip.writeTofile("juke.json", objectMapper.writeValueAsString(JukeClass));
	
		String path =zip.path();
		System.out.println(path);

		new File(path).renameTo(new File("C:/temp/juke.zip"));


		
		//System.out.println(File.createTempFile("test", "test").getAbsolutePath());
		JukeZipDAOImpl zip2=new JukeZipDAOImpl("C:/temp", "juke");
		assert(new File(zip2.path()).exists());
		JukeHelper.setJukeDao(zip2);
		String jukeTxt=JukeHelper.getJukeDAO().asString( "juke");
		//
	//	JukeTransformerUtil.readValue(orig, null)
		HashMap<String,JukeClass> juke2Class= zip2.getJukeClassMap();
		//zip2.readFromFile("getMyDataMapAsList");
	//			objectMapper.readValue(jukeTxt, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, JukeClass.class));
		JukeState.GLOBAL_JUKE=JukeState.REPLAY;
	
		
		System.setProperty("juke.path","C:/temp");
		System.setProperty("juke.zip","juke");
		
		ISampleService wrapped=new JukeFactory<ISampleService>().newInstance(this.service,ISampleService.class, JukeState.REPLAY);
		List<HashMap<String,BigDecimal>> list=wrapped.getMyDataMapAsList(biggie, "testit");
        List<HashMap<String,BigDecimal>> listB=wrapped.getMyDataMapAsList(biggie, "testit");
        List<HashMap<String,BigDecimal>> listV=wrapped.getMyDataMapAsList(biggie, "testit");
        List<HashMap<String,BigDecimal>> listV2=wrapped.getMyDataMapAsList(biggie, "testit");


        ZipUtil zipper = new ZipUtil("C:/temp","juke");
		zipper.open();
		//return new DelayTuner.Builder(sequencedItem, 0).build();
		JukeStateBuilder built= new JukeStateBuilder.Builder(zipper.getZipName()).build();
		DataProgramSchedule sched= built.getSchedule();
		assert (sched != null);
		
		
		System.out.println("done");
	}
	

}

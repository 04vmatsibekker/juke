package org.juke.framework.dao;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.juke.framework.api.JukeState;
import org.juke.framework.exception.JukeAccessException;
import org.juke.framework.scheduler.DataProgramSchedule;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

	private String zipPath = "";
	private String identifier = "";
	private ZipFile file;
	private File f ;
	private HashMap<String, String> entriesMap = new HashMap<>();
	DataProgramSchedule schedule =new DataProgramSchedule();
	
	private final static int BUF_SIZE = 1024;

	private static final String DELIM = "/";
	private static final String JSON = ".json";
	private static final String ZIP = ".zip";
	private static final String BAK = ".bak";
	private static final String DOT = ".";
	public static final String SLASH="/";

	
	
	public ZipUtil(String path, String identifier) {
		this.zipPath = FilenameUtils.normalize(path);
		this.identifier = FilenameUtils.normalize(identifier);
		try {
			final String propVal = System.getProperty(JukeState.JUKE);
			if (propVal != null && propVal.equalsIgnoreCase(JukeState.RECORD)) {

				this.f = File.createTempFile("juke",ZIP);
			} else {
				this.f = new File(path, identifier + ZIP);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getZipPath() {
		return zipPath;
	}

	public void setZipPath(String zipPath) {
		this.zipPath = zipPath;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIDentifier(String identifier) {
		this.identifier = identifier;
	}

	public ZipFile getFile() {
		return file;
	}

	public void setFile(ZipFile file) {
		this.file = file;
	}

	public HashMap<String, String> getEntriesMap() {
		return entriesMap;
	}

	public void setEntriesMap(HashMap<String, String> entriesMap) {
		this.entriesMap = entriesMap;
	}

	public boolean exists() {
		return new File(getZipName()).exists();
	}

	public String getZipName() {
		return zipPath + DELIM + identifier + ZIP;

	}


	public  String readStringFromZipFile(String zipFileName, String fileName) throws IOException {
		ZipFile zipFile = new ZipFile(new File(zipFileName));
		ZipEntry zipEntry= zipFile.getEntry(fileName);
		//open input stream from zip entry
		InputStream is= zipFile.getInputStream(zipEntry);
		//read the input stream and return the content as a string
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String str=null;
		while (( str = reader.readLine())!=null) {
			sb.append(str);
		}
		reader.close();
		isr.close();
		is.close();
		return sb.toString();
	}

	public void close() throws IOException {
		if (file != null) {
			file.close();
			file = null;
		}
	}

	public void removeZipFile() throws IOException {
		if (exists()) {
			close();
		}
		File f = new File(getZipName());
		removeFile(new File(f.getPath() + BAK));

		f.renameTo(new File(f.getPath() + BAK));

	}

	public static void removeFile(File f) {
		if (f.exists()) {
			f.delete();
		}
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		//copy a file from [sourceFile] to [destFile]
		Files.copy(sourceFile.toPath(), destFile.toPath());

	}
	public String insertKey(String entry, int index) {
		
		return entry+DOT+ index + JSON;
	}
	 
	
 	public void addIncrementEntry(String entry, String content) {
 		int length=schedule.add(entry);
 		entriesMap.put(insertKey(entry,length), content);
 		//entriesMap.put(entry + DOT + length + JSON, content);
 	}
	public void addEntry(String entry, String content) {
		entriesMap.put(entry+JSON, content);
	}
	public FileInputStream readFromZipFile() throws IOException{
		return new FileInputStream (new File (this.getZipName()));
	}
	public  void createZipFile(String zipFileName, HashMap<String, String> files) throws IOException {
		//create a zip file
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFileName));
		//for each file in the HashMap
		for (String fileName : files.keySet()) {
			//create a zip entry
			ZipEntry zipEntry = new ZipEntry(fileName);
			//add the zip entry to the zip file
			zipOut.putNextEntry(zipEntry);
			//get the file content from the HashMap
			String fileContent = files.get(fileName);
			//write the file content to the zip file
			zipOut.write(fileContent.getBytes());
		}
		//close the zip file
		zipOut.close();
	}
	public void writeToZipFile() throws IOException{
		removeZipFile();

		this.zipPath = f.getParent();
		this.identifier= f.getName().split(ZIP)[0];
		createZipFile(f.getPath(),entriesMap);

	}
	public static Set<String> getFileNamesFromZipFile(String zipFileName) throws IOException {
		//create a zip file
		ZipFile zipFile = new ZipFile(zipFileName);
		//get the zip file entries
		ZipEntry[] zipEntries = zipFile.stream().toArray(ZipEntry[]::new);
		//create a string array of the zip file entry names
		Set<String> fileNames = new HashSet<>();
		//for each zip file entry
		for (int i = 0; i < zipEntries.length; i++) {
			//get the zip file entry name
			String fileName = zipEntries[i].getName();
			//add the zip file entry name to the string array
			fileNames.add(fileName);

		}
		//return the string array of zip file entry names
		return fileNames;
	}


	
	public ZipFile getZipFile() {
		return file;
	}

	public void open() {
		try {
			if (file != null) {
				file.close();

			}
			 File f=new File(getZipName());
			
			file = new ZipFile(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMethodFromZipEntryName(String zipEntryName) {
		String method  = zipEntryName.split("\\.\\d+\\.json")[0];
		method = method.split("\\.\\$")[1];
		method = method.split("[-?\\.\\d*]")[0];
	 
		return method;
	}
	public String readFromZipFile(String entry) throws JukeAccessException{
		
		ZipEntry zentry= null;
		open();
		InputStream is = null;
		try{
			zentry = file.getEntry(entry+JSON);
			is = file.getInputStream(zentry);
			
		}catch (Exception e) {
			throw new JukeAccessException ("Failed to find " + entry + JSON);
			
		}
		
		InputStreamReader isr=null;
		try{
			isr = new InputStreamReader(is);
			
		}catch (NullPointerException npe) {
			throw new JukeAccessException ("Could not open stream " + entry + JSON);
		}
		BufferedReader reader = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String str=null;
		try {
			while (( str = reader.readLine())!=null) {
				sb.append(str);
			}
		}catch (IOException e) {
			throw new JukeAccessException ("Can not read date for "+entry+" from "+getZipName());
		}
		
		try {
			reader.close();
			isr.close();
			is.close();
			//file.close();
		}catch (IOException e) {
			throw new JukeAccessException ("Can not close file"+getZipName());
		}
		return sb.toString();
	}

	
}

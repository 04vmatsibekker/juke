package org.juke.framework.scheduler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

import org.juke.framework.dao.JukeZipDAOImpl;
import org.juke.framework.dao.ZipUtil;
import org.juke.framework.mapper.pojo.JukeClass;

public class JukeStateBuilder {
	Builder builder;
	JukeStateBuilder(Builder builder) {
	this.builder = builder;
		 
	}
	public DataProgramSchedule getSchedule() {
		return builder.schedule().getSchedule();
		
		
		
	}
	public static void main(String[] args) {

	}

	

	public static class Builder {
		private static final String JukeMethodIDENTIFIER = ".$";
		private static final String JSON = ".json";
		private static final String juke = "juke.json";
		Set<String> fileNames = null;
		Set<String> filtered = new HashSet<>();;
	
		DataProgramSchedule schedule = null;
		public Builder(String zipFile) {

			try {
				this.fileNames =  ZipUtil.getFileNamesFromZipFile(zipFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			this.filtered = filter(fileNames);

		}
		public Builder(Set<String> fileNames) {
			this.fileNames = fileNames;
			this.filtered = filter(fileNames);

		}

		private Builder schedule() {
				
		 
			this.schedule = new DataProgramSchedule();
			for (String file : filtered) {
				String[] cleaned = this.split(file);

				if (cleaned != null) {

					int count = Integer.parseInt(cleaned[1]);
					String classAndMethod = cleaned[0];
					DataProgram dp = schedule.getProgram(classAndMethod);
					if (dp.getLength() < count) {
						dp.setLength(count);
					}

				}

			}

			return this;
		}

		private String[] split(String file) {
			String cleaned = file.substring(0, file.length()- JSON.length());
			
		
			int index = cleaned.lastIndexOf('.');
			if (index > -1 && cleaned.endsWith(".") == false) {
				String numStr = cleaned.substring(index + 1);
				if (numStr.matches("\\d+")) {
					String classAndMethod = cleaned.substring(0, index );
					String count = cleaned.substring(index + 1);
					return new String[] { classAndMethod, count };
				}
			}
			return null;
		}

		private Set<String> filter(Set<String> fileNames) {
			filtered.clear();

			for (String file : fileNames) {
				if (file.contains(JukeMethodIDENTIFIER)) {
				
					String cleaned = file.substring(0, file.length()- JSON.length());
					int index = cleaned.lastIndexOf('.');
					if (index > -1 && cleaned.endsWith(".") == false) {
						String numStr = cleaned.substring(index + 1);
						if (numStr.matches("\\d+")) {
							filtered.add(file);

						}
					}

				}
			}
			return filtered;
		}

		public Builder setFiles(Set<String> fileNames) {
			this.fileNames = this.filter(fileNames);

			return this;

		}
		public DataProgramSchedule getJukeClass() {
			return this.schedule;
		}
		public DataProgramSchedule getSchedule() {
			return this.schedule;
		}
		public JukeStateBuilder build() {
			JukeStateBuilder builder = new JukeStateBuilder(this);
			schedule();
			

			return builder;
		}

	}

}

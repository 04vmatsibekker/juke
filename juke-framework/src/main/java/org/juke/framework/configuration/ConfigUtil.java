package org.juke.framework.configuration;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class ConfigUtil {

	private static final String TMP = FilenameUtils.normalize(System.getProperty("java.io.tmpdir") + "/juke");
	static {
	new File(TMP).mkdirs();
		
	}
	private ConfigUtil() {
		
	}
	public static String getJukePath() {
		String prop = FilenameUtils.normalize(System.getProperty("juke.path"));
		return (prop != null && prop.trim().length() > 0)? prop : getDefauljukePath();
		
	}
	public static String setDefauljukePath() {
		System.setProperty("juke.path", TMP);
		return TMP;
		
	}
	public static String getDefauljukePath() {
		return TMP;
		
	}
}

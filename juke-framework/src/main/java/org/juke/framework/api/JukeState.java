package org.juke.framework.api;

public class JukeState {


	public static final String JUKE= "juke";
	public static final String RECORD = "record";
	public static final String JUKEZIP ="juke.zip";
	public static final String REPLAY = "replay";
	public static final String IGNORE = "ignore";
	public static final String NONE = "";
	public static final String DISABLE = "disable";
	public static  String GLOBAL_JUKE = null;
	public static boolean GLOBAL_DISABLE = false;

public static String getGlobaljuke() {
	return GLOBAL_JUKE;
}

public static void setGlobaljuke(String globaljuke) {
	GLOBAL_JUKE = globaljuke;
}
public static boolean isGlobalisable() {
	return GLOBAL_DISABLE;
}
public static void setGlobalDisable(boolean globalDisable) {
	GLOBAL_DISABLE = globalDisable;
}

}

package io.github.ayechanaungthwin.chat.utils;

import java.io.File;

public class FileUtils {

	public static String getLogoPath() {
		StringBuilder sb = new StringBuilder();
    	sb.append(System.getProperty("user.dir"));
    	sb.append(File.separator);
    	sb.append("src");
    	sb.append(File.separator);
    	sb.append("main");
    	sb.append(File.separator);
    	sb.append("resources");
    	sb.append(File.separator);
    	sb.append("images");
    	sb.append(File.separator);
    	
    	String imgFile = null;
    	File dir = new File(sb.toString());
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		for(File file: files) {
    			String name = file.getName();
    			if (Character.isUpperCase(name.charAt(0))) {
    				imgFile = name;
        			break;
    			}
    		}
    	}
    	sb.append(imgFile);
    	
    	return sb.toString();
	}
}

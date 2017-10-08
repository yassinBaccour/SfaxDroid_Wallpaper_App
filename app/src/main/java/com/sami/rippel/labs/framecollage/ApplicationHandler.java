package com.sami.rippel.labs.framecollage;

import java.io.File;

public class ApplicationHandler {

	public enum IMAGES{
		Cache,
		FrameImages
	}
	private static ApplicationHandler handler;
	private ApplicationHandler() {
	}

	public File getOrCreateFolder(String folder, IMAGES imagePath){

		String strImageFolder = folder + File.separator + imagePath.name();
		File imageFolder = new File(strImageFolder);
		if(!imageFolder.exists()){
			imageFolder.mkdirs();
		}
		return imageFolder;
	}

	public static ApplicationHandler getInstance() {
		if (handler == null)
			handler = new ApplicationHandler();
		return handler;
	}
}

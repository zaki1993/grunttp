package com.zaki.grunttp.constant;

/**
 * File extensions that the server recognises
 */
public enum FileExtension {
	
	JPG(".jpg"),
	JPEG(".jpeg"),
	BMP(".bmp"),
	PNG(".png"),
	GIF(".gif"),
	MP3(".mp3"),
	MP4(".mp4"),
	AVI(".avi"),
	TXT(".txt"),
	CSS(".css"),
	JS(".js"),
	HTML(".html"),
	OTHER("");
	
	FileExtension(String value) {
		this.value = value;
	}
	
	public String value;

    /**
     * @param extension file extension
     * @return returns file extension if the given one is recognised otherwise returns .html
     */
	public static FileExtension getExtensionFromFileName(String extension) {

		for (FileExtension ext : FileExtension.values()) {
			if (ext.value.equals(extension)) {
				return ext;
			}
		}
		return HTML;
	}

    /**
     * @param path file path
     * @return file extension of the path is valid
     */
	public static FileExtension getExtensionFromFilePath(String path) {

		if (!path.contains(".")) {
			return OTHER;
		}
		return getExtensionFromFileName(path.substring(path.lastIndexOf(".")));
	}
}

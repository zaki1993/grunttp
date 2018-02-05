package com.zaki.grunttp.constant;

/**
 * Enum with content types that the server recognises
 */
public enum ContentType {

    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    IMAGE_BMP("image/bmp"),
    AUDIO_MPEG("audio/mpeg"),
    VIDEO_MP4("video/mp4");

    ContentType(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * @param fileExt file extension
     * @return content type for the given file extension,
     * if the file extension is not known text/plain is returned
     */
    public static ContentType getContentTypeFromFileExtension(FileExtension fileExt) {

        ContentType result;
        switch (fileExt) {
            case JPG:
                result = ContentType.IMAGE_JPEG;
                break;
            case PNG:
                result = ContentType.IMAGE_PNG;
                break;
            case BMP:
                result = ContentType.IMAGE_BMP;
                break;
            case GIF:
                result = ContentType.IMAGE_GIF;
                break;
            case HTML:
                result = ContentType.TEXT_HTML;
                break;
            case MP4:
                result = ContentType.VIDEO_MP4;
                break;
            case MP3:
                result = ContentType.AUDIO_MPEG;
                break;
            case OTHER:
            default:
                result = ContentType.TEXT_PLAIN;
                break;
        }
        return result;
    }
}

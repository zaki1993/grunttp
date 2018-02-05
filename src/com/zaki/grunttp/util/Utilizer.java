package com.zaki.grunttp.util;

import com.zaki.grunttp.constant.Global;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Utilizer {

    /**
     * Class that finds the buffer size of given output stream
     */
    private static class BlockSizeFinder extends BufferedOutputStream {

        public BlockSizeFinder(OutputStream out) {
            super(out);
        }

        public int getBlockSize() {
            return buf.length;
        }
    }

    /**
     * @return The perfect block size of the system
     */
    public static int getPerfectBlockSize() {

        return new BlockSizeFinder(System.out).getBlockSize();
    }

    /**
     * @return carriage return as array of bytes depending on the OS type
     */
    public static byte[] getCarriageReturn() {

        String osName = System.getProperty("os.name");
        return osName.startsWith("Windows") ? new byte[] { 13, 10, 13, 10 } :
                                              osName.startsWith("Mac") ? new byte[] { 13 } :
                                                                         new byte[] { 10, 10};
    }

    /**
     * @return carriage line as string depending on the OS type
     */
    public static String getCarriageLine() {

        String osName = System.getProperty("os.name");
        return osName.startsWith("Windows") ? "\r\n" :
                                              osName.startsWith("Mac") ? "\r" :
                                                                         "\n";
    }

    /**
     * @param template http response template
     * @param templateVar template expression
     * @param value expression value
     * @return http response template with replaced variable by given value
     */
    public static String replaceTemplateVariable(String template, String templateVar, String value) {

        String expression = new StringBuilder().append("#{").append(templateVar).append("}").toString();
        return template.replace(expression, value);
    }

    /**
     * Finds carriage return index in given list of bytes
     * @param requestBytes request bytes
     * @return carriage return index
     */
    public static int findCarriageReturnIndex(final List<Byte> requestBytes) {

        byte[] carriageReturn = Global.CARRIAGE_RETURN;
        int result = -1;
        for (int i = 0; i < requestBytes.size(); i++) {
            if (requestBytes.get(i) == carriageReturn[0]) {
                boolean hasCarriage = true;
                for (int j = 1; j < carriageReturn.length; j++) {
                    if (requestBytes.get(i + j) != carriageReturn[j]) {
                        hasCarriage = false;
                    }
                }
                if (hasCarriage) {
                    result = i + 4;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Finds carriage return index in given array of bytes
     * @param requestBytes requestBytes request bytes
     * @return carriage return index
     */
    public static int findCarriageReturnIndex(final byte[] requestBytes) {

        byte[] carriageReturn = Global.CARRIAGE_RETURN;
        int result = -1;
        for (int i = 0; i < requestBytes.length; i++) {
            if (requestBytes[i] == carriageReturn[0]) {
                boolean hasCarriage = true;
                for (int j = 1; j < carriageReturn.length; j++) {
                    if (requestBytes[i + j] != carriageReturn[j]) {
                        hasCarriage = false;
                    }
                }
                if (hasCarriage) {
                    result = i + 4;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Finds the end of request body, i.e. the last valid bytes if uploading file
     * @param arr array of bytes
     * @return end of file index
     */
    public static int findEndOfFileIndex(List<Byte> arr) {

        int result = -1;
        for (int i = arr.size() - 1; i >= 0; i--) {
            if (arr.get(i) == 45) {
                boolean idxFound = true;
                for (int j = 1; j < 28; j++) {
                    if (arr.get(i - j) != 45) {
                        idxFound = false;
                        break;
                    }
                }
                if (idxFound) {
                    result = i - 28;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Transforms array to list
     * @param arr array of bytes
     * @return list of bytes
     */
    public static List<Byte> toByteList(final byte[] arr) {

        List<Byte> result = new ArrayList<>();
        for (byte b : arr) {
            result.add(b);
        }
        return result;
    }

    /**
     * Transforms list to array
     * @param lst list of bytes
     * @return array of bytes
     */
    public static byte[] toByteArray(final List<Byte> lst) {

        byte[] result = new byte[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            result[i] = lst.get(i);
        }
        return result;
    }

    /**
     * Retrieves the stack trace from throwable object and transforms it into html string
     * @param t throwable object
     * @return stacktrace as html string
     */
    public static String displayErrorForWeb(Throwable t) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String stackTrace = sw.toString();
        return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
    }

    /**
     * Retrieves the stack trace from throwable object and transforms it into string good for logging
     * @param t throwable object
     * @return stacktrace as string
     */
    public static String getStackTraceForLogging(Throwable t) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * @param rootBaseName root directory name
     * @param fileAbsolutePath absolute path for file
     * @return className from given className absolute path
     */
    public static String getClassNameFromAbsolutePath(String rootBaseName, String fileAbsolutePath) {

        String fileRelativePath = fileAbsolutePath.substring(fileAbsolutePath.indexOf(rootBaseName) + rootBaseName.length() + 1);
        String result = null;
        if (fileRelativePath.contains(".class")) {
            fileRelativePath = fileRelativePath.substring(0, fileRelativePath.lastIndexOf(".class"));
            if (!fileRelativePath.contains(File.separator)) {
                result = fileRelativePath;
            } else {
                result = fileRelativePath.replace(File.separator, ".");
            }
        }
        return result;
    }
}
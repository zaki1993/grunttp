package com.zaki.grunttp.server.auth.controller;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;

import java.io.*;

/**
 * This class is just for testing purposes do not read
 */

@Handler(url="/upload")
public class Uploader {

    @RequestMapping(method= RequestMethod.GET)
    public String loadPage(ServerRequest req, ServerResponse res) {
        return "upload.html";
    }

    @RequestMapping(method= RequestMethod.POST)
    public String uploadFile(ServerRequest req, ServerResponse res) {

        byte[] fileBytes = req.getRequestBodyBytes();
        File newFile = new File("test.jpg");

        try (OutputStream os = new FileOutputStream(newFile)) {
            newFile.createNewFile();
            os.write(fileBytes);
            os.flush();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload.html";
    }
}

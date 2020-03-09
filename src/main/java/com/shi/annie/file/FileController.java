package com.shi.annie.file;

import com.shi.annie.file.client.FileClient;
import com.shi.annie.file.client.FileClientFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/12/21 11:23 上午
 */

@RestController
public class FileController {

    /**
     * 文件下载
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void downloadFile(@RequestBody ResourceFile file, HttpServletResponse response) {

        FileClient client = FileClientFactory.getFileClient();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
        try (
                InputStream fileInputSteam = client.download(file);
                BufferedInputStream in = new BufferedInputStream(fileInputSteam);
                ServletOutputStream out = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } catch (IOException e) {
            System.out.println("file download stream error");
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void fileUpload(@RequestParam("file") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        //前端没有选择文件，srcFile为空
        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件");
            return;
        }
        FileClient client = FileClientFactory.getFileClient();
        try {
            client.upload(multipartFile.getName(), multipartFile.getBytes());
            //todo init ResourceFile info
        } catch (IOException e) {
            e.printStackTrace();
        } }
}

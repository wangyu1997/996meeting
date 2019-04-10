package com.lab.meet.Controller.Public;

import com.sun.deploy.net.URLEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.processing.FilerException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
@Api("通用-文件下载接口")
@RequestMapping("/public/files")
public class DownloadController {

    @Value("${upload.baseFolder}")
    String UPLOADED_FOLDER;


    @ApiOperation(value = "文件下载", notes = "用户下载文件", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pathName", value = "要下载的文件路径", required = true, dataType = "String", paramType = "form")
    })
    @GetMapping("/{basePath}/**")
    public void download(@PathVariable String basePath, HttpServletRequest request, HttpServletResponse response) throws FilerException {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

        String pathName;
        if (null != arguments && !arguments.isEmpty())
            pathName = basePath + '/' + arguments;
        else
            pathName = basePath;

        System.out.println(UPLOADED_FOLDER);

        try {
            File downloadFile = new File(UPLOADED_FOLDER, pathName);
            System.out.println(downloadFile);
            String fileName = downloadFile.getName();
            InputStream inputStream = new FileInputStream(downloadFile);
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new FilerException("文件不存在");
        }
    }

}

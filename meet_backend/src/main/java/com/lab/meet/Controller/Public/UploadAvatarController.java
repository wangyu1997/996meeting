package com.lab.meet.Controller.Public;

import com.lab.meet.Dao.FileService;
import com.lab.meet.Model.FileEntity;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.Status;
import com.lab.meet.Repository.*;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Api("通用-上传头像接口")
@RequestMapping("/public/upload")
public class UploadAvatarController {

    @Value("${upload.baseFolder}")
    String UPLOADED_FOLDER;

    @Value("${upload.documents}")
    String DOC_FOLDER;

    @Value("${upload.images}")
    String IMAGE_FOLDER;

    @Value("${upload.avatars}")
    String AVATAR_FOLDER;

    @Value("/public/files")
    String downloadRoute;

    @Autowired
    FileService fileService;


    @ApiOperation(value = "用户头像上传", notes = "用户上传头像", httpMethod = "POST")
    @PostMapping("/avatar")
    public ResponseBody<?> uploadAvatar(
            @RequestParam("file") MultipartFile uploadfile,
            HttpServletRequest request) {
        String detail_name;
        if (uploadfile.isEmpty())
            return ResponseBody.failedRep(Status.FILE_NOT_EXIST);
        try {
            detail_name = Utils.saveUploadedImage(uploadfile, UPLOADED_FOLDER + AVATAR_FOLDER);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseBody.failedRep();
        }
        String type = "avatar";
        String name = detail_name;
        String path = UPLOADED_FOLDER + AVATAR_FOLDER + name;
        String url = Utils.getContentPath(request) + downloadRoute + "/" + AVATAR_FOLDER + detail_name;
        FileEntity data = fileService.storeFile(type, name, path, url);
        return ResponseBody.successRep(data);
    }
}
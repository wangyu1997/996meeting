package com.lab.meet.Controller.User;

import com.lab.meet.Dao.FileService;
import com.lab.meet.Model.FileEntity;
import com.lab.meet.Model.PersonMeet;
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
@Api("用户-上传接口")
@RequestMapping("/user/upload")
public class UploadController {

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

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonMeetRepository perMeetRepository;

    @Autowired
    GroupTaskRepository groupTaskRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    GroupSummaryRepository groupSummaryRepository;

    @ApiOperation(value = "文本文件上传", notes = "用户上传附件", httpMethod = "POST")
    @PostMapping("/doc")
    public ResponseBody<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile,
            HttpServletRequest request) {
        String detail_name;
        if (uploadfile.isEmpty())
            return ResponseBody.failedRep(Status.FILE_NOT_EXIST);
        try {
            detail_name = Utils.saveFileByDate(uploadfile, UPLOADED_FOLDER + DOC_FOLDER);
        } catch (IOException e) {
            return ResponseBody.failedRep();
        }
        String path = UPLOADED_FOLDER + DOC_FOLDER + detail_name;
        String type = "doc";
        String name = uploadfile.getOriginalFilename();
        String url = Utils.getContentPath(request) + downloadRoute + "/" + DOC_FOLDER + detail_name;
        FileEntity data = fileService.storeFile(type, name, path, url);
        return ResponseBody.successRep(data);
    }


    @ApiOperation(value = "图片上传", notes = "用户上传图片", httpMethod = "POST")
    @PostMapping("/image")
    public ResponseBody<?> uploadImage(
            @RequestParam("file") MultipartFile uploadfile,
            HttpServletRequest request) {
        String detail_name;
        if (uploadfile.isEmpty())
            return ResponseBody.failedRep(Status.FILE_NOT_EXIST);
        try {
            detail_name = Utils.saveUploadedImage(uploadfile, UPLOADED_FOLDER + IMAGE_FOLDER);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseBody.failedRep();
        }
        String name = detail_name;
        String path = UPLOADED_FOLDER + IMAGE_FOLDER + name;
        String type = "image";
        String url = Utils.getContentPath(request) + downloadRoute + "/" + IMAGE_FOLDER + detail_name;
        FileEntity data = fileService.storeFile(type, name, path, url);
        return ResponseBody.successRep(data);
    }

}
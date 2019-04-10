package com.lab.meet.Controller.Public;

import com.lab.meet.Model.FileEntity;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Api("通用-文件下载接口")
@RequestMapping("/public/file")
public class FileController {

    @Autowired
    FileRepository fileRepository;

    @ApiOperation(value = "根据file_ids获取文件", notes = "根据file_ids获取文件", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file_ids", value = "FileEntity ids，(,分隔)", required = true, dataType = "String", paramType = "form")
    })
    @GetMapping("multi/")
    public ResponseBody<?> download(String file_ids) {
        Set<FileEntity> files = Utils.findDocsByIds(fileRepository, file_ids);
        return ResponseBody.successRep(files);
    }
}

package com.lab.meet.Controller.Admin;


import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupSummaryRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("管理员-组会任务信息管理")
@RequestMapping("/admin/tasks")
public class AdminTaskController {

    @Autowired
    GroupTaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupSummaryRepository summaryRepository;

    @ApiOperation(value = "修改组会任务", notes = "修改组会任务信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务主题", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "start_time", value = "会议开始时间", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "user_ids", value = "参与用户Id (用','隔开)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "file_ids", value = "附件id (用','隔开)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "head_img", value = "task头图", dataType = "String", paramType = "form"),
    })
    @PostMapping("/update/{id}")
    public ResponseBody<?> updateTask(@PathVariable Long id, String title, String description,
                                      String start_time, String user_ids, String file_ids, String head_img) throws Exception {
        GroupTask task = taskRepository.findGroupTaskById(id);
        if (task == null)
            throw new Exception("The Task which id is " + id + " is not exist");
        if (StringUtils.isNoneBlank(title))
            task.setTitle(title);
        if (StringUtils.isNoneBlank(description))
            task.setDescription(description);
        if (StringUtils.isNoneBlank(start_time))
            task.setStart_time(Utils.timeFormat(start_time));
        if (StringUtils.isNoneBlank(user_ids))
            task.setUsers(Utils.findUsersByIds(userRepository, user_ids));
        if (StringUtils.isNoneBlank(file_ids))
            task.setFile_ids(file_ids);
        if (StringUtils.isNoneBlank(head_img))
            task.setHead_img(head_img);
        if ((task = taskRepository.save(task)) != null)
            return ResponseBody.successRep(task);
        else return ResponseBody.failedRep();
    }

    @ApiOperation(value = "管理员发布新的组会任务", notes = "管理员发布组会任务", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "任务描述", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "start_time", value = "会议开始时间", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "user_ids", value = "参与用户Id (用','隔开)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "file_ids", value = "附件id (用','隔开)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "head_img", value = "task头图", dataType = "String", paramType = "form"),
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseBody<?> addTask(String title, String description,
                                   String start_time, String user_ids, String file_ids, String head_img) {
        GroupTask task;
        if ((task = taskService.addTask(title, description, start_time, user_ids, file_ids, head_img)) == null)
            return ResponseBody.failedRep();
        return ResponseBody.successRep(task);
    }

    @ApiOperation(value = "删除组会任务", notes = "删除组会任务信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/delete/{id}")
    public ResponseBody<?> deleteUser(@PathVariable Long id) {
        if (taskService.deleteTaskById(id))
            return ResponseBody.successRep(null);
        else
            return ResponseBody.failedRep();
    }

}

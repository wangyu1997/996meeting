package com.lab.meet.Controller.Public;


import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api("通用-组会任务查询管理")
@RequestMapping("/public/tasks")
public class TaskController {

    @Autowired
    GroupTaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @ApiOperation(value = "查询所有组会任务信息", notes = "查询所有组会任务信息", httpMethod = "GET")
    @GetMapping("")
    public ResponseBody<?> findAllTasks() {
        List<GroupTask> tasks = taskRepository.findAll();
        return ResponseBody.successRep(tasks);
    }

    @ApiOperation(value = "分页查询组会任务", notes = "查询根据过滤条件对组会任务做分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_start", value = "检索task发布时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_end", value = "检索task发布时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time_start", value = "检索会议开始时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time_end", value = "检索会议开始时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query")
    })
    @GetMapping("/page")
    public ResponseBody<?> findTasksByPage(String title, String description,
                                           String publish_time_start, String publish_time_end,
                                           String start_time_start, String start_time_end,
                                           Integer page, Integer pageSize) {
        Page<GroupTask> tasks = taskService.findTaskByPage(title, description,
                publish_time_start, publish_time_end,
                start_time_start, start_time_end,
                page - 1, pageSize);
        Map<String, Object> result = Utils.formatPagable(tasks);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }


    @ApiOperation(value = "根据id查询组会任务信息", notes = "查询组会任务信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组会任务Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public ResponseBody<?> findTaskById(@PathVariable Long id) {
        GroupTask task = taskRepository.findGroupTaskById(id);
        return ResponseBody.successRep(task);
    }

}

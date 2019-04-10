package com.lab.meet.Controller.User;


import com.lab.meet.Dao.AuthService;
import com.lab.meet.Dao.PersonMeetService;
import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.*;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.PersonMeetRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RestController
@Api("用户-组会任务信息查询")
@RequestMapping("/user/tasks")
public class UserTaskController {

    @Autowired
    GroupTaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    AuthService authService;

    @Value("${token.header}")
    private String tokenHeader;

    @ApiOperation(value = "查询用户参与的组会任务", notes = "查询用户组会任务信息", httpMethod = "GET")
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
    @GetMapping("/my")
    public ResponseBody<?> queryUserSummary(String title, String description,
                                            String publish_time_start, String publish_time_end,
                                            String start_time_start, String start_time_end,
                                            Integer page, Integer pageSize, HttpServletRequest request) {
        User user = Utils.getCurrentUser(authService, request, tokenHeader);
        if (user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        Page<GroupTask> tasks = taskService.findMyTaskByPage(user.getId(), title, description, publish_time_start, publish_time_end, start_time_start, start_time_end, page-1, pageSize);
        Map<String, Object> result = Utils.formatPagable(tasks);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }

}

package com.lab.meet.Controller.User;


import com.lab.meet.Dao.AuthService;
import com.lab.meet.Dao.SummaryService;
import com.lab.meet.Model.*;
import com.lab.meet.Model.ResponseBody;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RestController
@Api("用户-组会报告管理")
@RequestMapping("/user/summarys")
public class UserSummaryController {

    @Autowired
    GroupSummaryRepository summaryRepository;

    @Autowired
    SummaryService summaryService;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupTaskRepository taskRepository;

    @Value("${token.header}")
    private String tokenHeader;

    @ApiOperation(value = "修改用户组会报告", notes = "修改用户组会报告信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务主题", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "task_id", value = "组会任务 id", dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "user_id", value = "用户id 管理员才有权利才做", dataType = "Long", paramType = "form")
    })
    @PostMapping("/update/{id}")
    public ResponseBody<?> updateSummary(@PathVariable Long id, String title, String description,
                                         Long task_id, Long user_id,
                                         HttpServletRequest request) throws Exception {
        User curr_user = Utils.getCurrentUser(authService, request, tokenHeader);
        GroupSummary summary = summaryRepository.findGroupSummaryById(id);
        if (curr_user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        else if (!Utils.isAdmin(curr_user.getRoles()) && !summary.getUser().getId().equals(curr_user.getId()))
            return ResponseBody.failedRep(Status.CAN_NOT_ACCESS);
        else {
            if (summary == null)
                throw new Exception("The Summary which id is " + id + " is not exist");
            if (StringUtils.isNoneBlank(title))
                summary.setTitle(title);
            if (StringUtils.isNoneBlank(description))
                summary.setDescription(description);
            if (Utils.isAdmin(curr_user.getRoles()) && StringUtils.isNoneBlank(user_id + "")) {
                User user = userRepository.findSysUserById(user_id);
                if (user != null)
                    summary.setUser(user);
            }
            if (StringUtils.isNoneBlank(task_id + "")) {
                GroupTask task = taskRepository.findGroupTaskById(task_id);
                if (task != null)
                    summary.setTask(task);
            }
            if ((summary = summaryRepository.save(summary)) != null)
                return ResponseBody.successRep(summary);
            else return ResponseBody.failedRep();
        }
    }

    @ApiOperation(value = "用户发布新的用户组会报告", notes = "用户发布用户组会报告", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "任务描述", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "task_id", value = "组会任务id", required = true, dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "user_id", value = "用户id 管理员才有权利才做", dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "isAdmin", value = "是否为管理员操作", dataType = "boolean", paramType = "form")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseBody<?> addSummary(String title, String description,
                                      Long task_id, Long user_id, boolean isAdmin, HttpServletRequest request) throws Exception {
        System.out.println(user_id);
        User curr_user = Utils.getCurrentUser(authService, request, tokenHeader);
        if (curr_user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        if (!(Utils.isAdmin(curr_user.getRoles()) && isAdmin))
            user_id = curr_user.getId();
        GroupSummary summary;
        if ((summary = summaryService.addSummary(title, description, task_id, user_id)) == null)
            return ResponseBody.failedRep();
        return ResponseBody.successRep(summary);
    }

    @ApiOperation(value = "删除用户组会报告", notes = "删除用户组会报告信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/delete/{id}")
    public ResponseBody<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User user = Utils.getCurrentUser(authService, request, tokenHeader);
        GroupSummary summary = summaryRepository.findGroupSummaryById(id);
        if (user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        else if (!Utils.isAdmin(user.getRoles()) && !summary.getUser().getId().equals(user.getId()))
            return ResponseBody.failedRep(Status.CAN_NOT_ACCESS);
        else {
            if (summaryRepository.deleteGroupSummaryById(id) == 1)
                return ResponseBody.successRep(null);
            else
                return ResponseBody.failedRep();
        }
    }

    @ApiOperation(value = "查询用户参与的组会报告", notes = "查询用户组会报告信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_start", value = "检索summary发布时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_end", value = "检索summary发布时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query")
    })
    @GetMapping("/my")
    public ResponseBody<?> queryUserSummary(String title, String description,
                                            String publish_time_start, String publish_time_end,
                                            Integer page, Integer pageSize, HttpServletRequest request) {
        User user = Utils.getCurrentUser(authService, request, tokenHeader);
        if (user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        Page<GroupSummary> summaries = summaryService.findMySummarysByPage(user.getId(), title, description, publish_time_start, publish_time_end, page - 1, pageSize);
        Map<String, Object> result = Utils.formatPagable(summaries);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }

}

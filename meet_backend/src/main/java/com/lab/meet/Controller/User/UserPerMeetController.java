package com.lab.meet.Controller.User;


import com.lab.meet.Dao.AuthService;
import com.lab.meet.Dao.PersonMeetService;
import com.lab.meet.Model.*;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.FileRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Api("用户-个人汇报信息管理")
@RequestMapping("/user/per_meets")
public class UserPerMeetController {

    @Autowired
    PersonMeetRepository meetRepository;

    @Autowired
    PersonMeetService meetService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AuthService authService;

    @Value("${token.header}")
    private String tokenHeader;

    @ApiOperation(value = "修改个人汇报信息", notes = "修改个人汇报", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务主题", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "next_week_plan", value = "下阶段工作计划", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "start_time", value = "汇报时间", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "user_id", value = "参与用户Id (用','隔开)，管理员使用，普通用户无权限指定", dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "file_ids", value = "附件id (用','隔开)", dataType = "String", paramType = "form"),
    })
    @PostMapping("/update/{id}")
    public ResponseBody<?> updatePersonMeet(@PathVariable Long id, String title, String description,
                                            String next_week_plan, String start_time,
                                            Long user_id, String file_ids, HttpServletRequest request) throws Exception {
        User curr_user = Utils.getCurrentUser(authService, request, tokenHeader);
        PersonMeet meet = meetRepository.findPersonMeetById(id);
        if (curr_user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        else if (!Utils.isAdmin(curr_user.getRoles()) && !meet.getUser().getId().equals(curr_user.getId()))
            return ResponseBody.failedRep(Status.CAN_NOT_ACCESS);
        else {
            if (meet == null)
                throw new Exception("The PersonMeet which id is " + id + " is not exist");
            if (StringUtils.isNoneBlank(title))
                meet.setTitle(title);
            if (StringUtils.isNoneBlank(description))
                meet.setDescription(description);
            if (StringUtils.isNoneBlank(next_week_plan))
                meet.setNext_week_plan(next_week_plan);
            if (StringUtils.isNoneBlank(file_ids))
                meet.setFile_ids(file_ids);
            if (Utils.isAdmin(curr_user.getRoles()) && StringUtils.isNoneBlank(user_id + "")) {
                User user = userRepository.findSysUserById(user_id);
                if (user != null)
                    meet.setUser(user);
            }
            if (StringUtils.isNoneBlank(start_time))
                meet.setStart_time(Utils.timeFormat(start_time));
            if ((meet = meetRepository.save(meet)) != null)
                return ResponseBody.successRep(meet);
            else return ResponseBody.failedRep();
        }
    }

    @ApiOperation(value = "发布新的个人汇报", notes = "发布个人汇报,管理员可以指定汇报人id", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "汇报主题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "汇报工作描述", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "next_week_plan", value = "下阶段工作计划", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "start_time", value = "汇报时间", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "user_id", value = "参与用户Id，管理员使用，普通用户无权限指定", dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "file_ids", value = "附件id (用','隔开)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "isAdmin", value = "是否为管理员操作", dataType = "boolean", paramType = "form")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseBody<?> addTask(String title, String description,
                                   String next_week_plan, String start_time,
                                   Long user_id, String file_ids, boolean isAdmin, HttpServletRequest request) {
        User curr_user = Utils.getCurrentUser(authService, request, tokenHeader);
        if (curr_user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        if (!(Utils.isAdmin(curr_user.getRoles()) && isAdmin))
            user_id = curr_user.getId();
        PersonMeet meet;
        if ((meet = meetService.addPersonMeet(title, description, next_week_plan, start_time, user_id, file_ids)) == null)
            return ResponseBody.failedRep();
        return ResponseBody.successRep(meet);
    }

    @ApiOperation(value = "删除个人汇报", notes = "删除个人汇报信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/delete/{id}")
    public ResponseBody<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User curr_user = Utils.getCurrentUser(authService, request, tokenHeader);
        PersonMeet meet = meetRepository.findPersonMeetById(id);
        if (curr_user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        else if (!Utils.isAdmin(curr_user.getRoles()) && !meet.getUser().getId().equals(curr_user.getId()))
            return ResponseBody.failedRep(Status.CAN_NOT_ACCESS);
        else {
            if (meetRepository.deletePersonMeetById(id) == 1)
                return ResponseBody.successRep(null);
            else
                return ResponseBody.failedRep();
        }
    }


    @ApiOperation(value = "查询用户参与的个人汇报", notes = "查询用户个人汇报信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "next_week_plan", value = "下阶段工作计划", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_start", value = "检索个人汇报提交时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_end", value = "检索个人汇报提交时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time_start", value = "检索个人汇报开始时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time_end", value = "检索个人汇报开始时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query")
    })
    @GetMapping("/my")
    public ResponseBody<?> queryUserSummary(String title, String description, String next_week_plan,
                                            String publish_time_start, String publish_time_end,
                                            String start_time_start, String start_time_end,
                                            Integer page, Integer pageSize, HttpServletRequest request) {
        User user = Utils.getCurrentUser(authService, request, tokenHeader);
        if (user == null)
            return ResponseBody.failedRep(Status.USER_NOT_LOGIN);
        Page<PersonMeet> personMeets = meetService.findMyPersonMeetsByPage(user.getId(), title, description, next_week_plan, publish_time_start,
                publish_time_end, start_time_start, start_time_end, page - 1, pageSize);
        Map<String, Object> result = Utils.formatPagable(personMeets);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }

}

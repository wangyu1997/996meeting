package com.lab.meet.Controller.Public;


import com.lab.meet.Dao.PersonMeetService;
import com.lab.meet.Model.PersonMeet;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.User;
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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api("通用-个人汇报信息查询")
@RequestMapping("/public/per_meets")
public class PerMeetController {

    @Autowired
    PersonMeetRepository meetRepository;

    @Autowired
    PersonMeetService meetService;

    @ApiOperation(value = "管理员查询所有个人回报信息", notes = "查询所有个人汇报信息", httpMethod = "GET")
    @GetMapping("")
    public ResponseBody<?> findAllSummarys() {
        List<PersonMeet> personMeets = meetRepository.findAll();
        return ResponseBody.successRep(personMeets);
    }

    @ApiOperation(value = "分页查询个人汇报", notes = "查询根据过滤条件对个人汇报做分页查询", httpMethod = "GET")
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
    @GetMapping("/page")
    public ResponseBody<?> findPersonMeetsByPage(String title, String description, String next_week_plan,
                                                 String publish_time_start, String publish_time_end,
                                                 String start_time_start, String start_time_end,
                                                 Integer page, Integer pageSize) {
        Page<PersonMeet> meets = meetService.findPersonMeetsByPage(title, description, next_week_plan,
                publish_time_start, publish_time_end,
                start_time_start, start_time_end,
                page-1, pageSize);
        Map<String, Object> result = Utils.formatPagable(meets);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }


    @ApiOperation(value = "根据id查询个人汇报信息", notes = "查询个人汇报信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "个人汇报 Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public ResponseBody<?> findPersonMeetById(@PathVariable Long id) {
        PersonMeet meet = meetRepository.findPersonMeetById(id);
        return ResponseBody.successRep(meet);
    }

}

package com.lab.meet.Controller.Public;


import com.lab.meet.Dao.SummaryService;
import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Repository.GroupSummaryRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Api("通用-组会总结查询")
@RequestMapping("/public/summarys")
public class SummaryController {


    @Autowired
    GroupSummaryRepository summaryRepository;

    @Autowired
    SummaryService summaryService;

    @ApiOperation(value = "查询所有组会总结", notes = "查询所有组会总结信息", httpMethod = "GET")
    @GetMapping("")
    public ResponseBody<?> findAllSummarys() {
        List<GroupSummary> summaries = summaryRepository.findAll();
        return ResponseBody.successRep(summaries);
    }

    @ApiOperation(value = "分页查询组会总结", notes = "查询根据过滤条件对组会总结做分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "任务主题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "任务描述", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_start", value = "检索summary发布时间左范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "publish_time_end", value = "检索summary发布时间右范围", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query")
    })
    @GetMapping("/page")
    public ResponseBody<?> findSummarysByPage(String title, String description,
                                              String publish_time_start, String publish_time_end,
                                              Integer page, Integer pageSize) {
        Page<GroupSummary> tasks = summaryService.findSummarysByPage(title, description,
                publish_time_start, publish_time_end,
                page - 1, pageSize);
        Map<String, Object> result = Utils.formatPagable(tasks);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }


    @ApiOperation(value = "根据id查询组会总结信息", notes = "查询组会总结信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "GroupSummary Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public ResponseBody<?> findSummaryById(@PathVariable Long id) {
        GroupSummary summary = summaryRepository.findGroupSummaryById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", summary.getId());
        map.put("task_id", summaryRepository.findTaskIdBySummaryId(id));
        map.put("title", summary.getTitle());
        map.put("user", summary.getUser());
        map.put("description", summary.getDescription());
        map.put("publish_time", summary.getPublish_time());
        return ResponseBody.successRep(map);
    }


    @ApiOperation(value = "根据Task id查询组会总结信息", notes = "根据task查询组会总结信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "GroupTask Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/tasks/{id}")
    public ResponseBody<?> findSummaryByTaskId(@PathVariable Long id) {
        Set<GroupSummary> summaries = summaryRepository.findGroupSummariesByTaskId(id);
        return ResponseBody.successRep(summaries);
    }

}

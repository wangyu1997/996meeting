package com.lab.meet.Controller.User;

import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api("用户-选择器选择结果")
@RequestMapping("/user/search")
public class SearchDataController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupTaskRepository taskRepository;

    @ApiOperation(value = "选择器查询用户信息", notes = "查询所有用户信息", httpMethod = "GET")
    @GetMapping("/users")
    public ResponseBody<?> findAllUsers() {
        List<User> users = userRepository.findAll();
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> map;
        for (User user : users) {
            map = new HashMap<>();
            map.put("key", user.getId());
            map.put("label", user.getUsername());
            res.add(map);
        }
        return ResponseBody.successRep(res);
    }


    @ApiOperation(value = "选择器查询所有组会任务信息", notes = "查询所有组会任务信息", httpMethod = "GET")
    @GetMapping("/tasks")
    public ResponseBody<?> findAllTasks() {
        List<GroupTask> tasks = taskRepository.findAll();
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> map;
        for (GroupTask task : tasks) {
            map = new HashMap<>();
            map.put("key", task.getId());
            map.put("label", task.getTitle());
            res.add(map);
        }
        return ResponseBody.successRep(res);
    }


}

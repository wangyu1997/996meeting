package com.lab.meet.Dao.Impl;

import com.github.wenhao.jpa.Specifications;
import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.GroupTask;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupSummaryRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    GroupTaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    GroupSummaryRepository summaryRepository;

    public Page<GroupTask> findTaskByPage(
            String title, String description,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end, Integer page, Integer pageSize) {
        Specification<GroupTask> specification = Specifications.<GroupTask>and()
                .like(StringUtils.isNotBlank(title), "title", "%" + title + "%")
                .like(StringUtils.isNotBlank(description), "description", "%" + description + "%")
                .between(StringUtils.isNotBlank(publish_time_start) && StringUtils.isNoneBlank(publish_time_end),
                        "publish_time", Utils.timeFormat(publish_time_start), Utils.timeFormat(publish_time_end)) //or eq("company", (Object) null)
                .between(StringUtils.isNotBlank(start_time_start) && StringUtils.isNoneBlank(start_time_end),
                        "start_time", Utils.timeFormat(start_time_start), Utils.timeFormat(start_time_end)) //or eq("company", (Object) null)
                .build();

        return taskRepository.findAll(specification, PageRequest.of(page, pageSize));
    }

    @Override
    public GroupTask addTask(String title, String description, String start_time, String user_ids, String file_ids, String head_img) {
        GroupTask task = new GroupTask();
        task.setTitle(title);
        task.setDescription(description);
        task.setPublish_time(new Date().getTime());
        task.setStart_time(Utils.timeFormat(start_time));
        task.setUsers(Utils.findUsersByIds(userRepository, user_ids));
        task.setFile_ids(file_ids);
        task.setHead_img(head_img);
        task = taskRepository.save(task);
        return task;
    }

    @Override
    public boolean deleteTaskById(Long id) {
        Set<GroupSummary> summarySet = summaryRepository.findGroupSummariesByTaskId(id);
        for (GroupSummary summary : summarySet)
            summaryRepository.delete(summary);
        return taskRepository.deleteGroupTaskById(id) == 1;
    }

    @Override
    public Page<GroupTask> findMyTaskByPage(Long id, String title, String description, String publish_time_start, String publish_time_end, String start_time_start, String start_time_end, Integer page, Integer pageSize) {
        System.out.println("==========");
        if (StringUtils.isBlank(title))
            title = "";
        if (StringUtils.isBlank(description))
            description = "";
        Long publish_start, publish_end, start_start, start_end;
        Long[] publish_array = formatDate(publish_time_start, publish_time_end);
        publish_start = publish_array[0];
        publish_end = publish_array[1];
        Long[] start_array = formatDate(start_time_start, start_time_end);
        start_start = start_array[0];
        start_end = start_array[1];
        title = "%" + title + "%";
        description = "%" + description + "%";
        System.out.println(id + "==" + title + "==" + description + "==" +
                publish_start + "==" + publish_end + "==" +
                start_start + "==" + start_end + "==" + page + "==" + pageSize);

        return taskRepository.findUsersGroupTasks(id, title, description,
                publish_start, publish_end, start_start, start_end, PageRequest.of(page, pageSize));
    }

    private Long[] formatDate(String date1, String date2) {
        Long d1, d2;
        if (StringUtils.isBlank(date1)) {
            d1 = Utils.timeFormat("1900-01-01 00:00");
            d2 = Utils.timeFormat("2900-01-01 00:00");
        } else {
            d1 = Utils.timeFormat(date1);
            d2 = Utils.timeFormat(date2);
        }
        return new Long[]{d1, d2};
    }
}

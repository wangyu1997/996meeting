package com.lab.meet.Dao.Impl;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import com.lab.meet.Dao.SummaryService;
import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupSummaryRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    GroupSummaryRepository summaryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupTaskRepository taskRepository;

    public Page<GroupSummary> findSummarysByPage(
            String title, String description,
            String publish_time_start, String publish_time_end,
            Integer page, Integer pageSize) {
        Specification<GroupSummary> specification = Specifications.<GroupSummary>and()
                .like(StringUtils.isNotBlank(title), "title", "%" + title + "%")
                .like(StringUtils.isNotBlank(description), "description", "%" + description + "%")
                .between(StringUtils.isNotBlank(publish_time_start) && StringUtils.isNoneBlank(publish_time_end),
                        "publish_time", Utils.timeFormat(publish_time_start), Utils.timeFormat(publish_time_end))
                .build();

        return summaryRepository.findAll(specification, PageRequest.of(page, pageSize));
    }

    @Override
    public GroupSummary addSummary(String title, String description, Long task_id, Long user_id) throws Exception {
        GroupSummary summary = new GroupSummary();
        summary.setTitle(title);
        summary.setDescription(description);
        User user = userRepository.findSysUserById(user_id);
        summary.setUser(user);
        GroupTask task = taskRepository.findGroupTaskById(task_id);
        if (task == null)
            throw new Exception("该组会任务不存在");
        summary.setTask(task);
        summary.setPublish_time(new Date().getTime());
        summary = summaryRepository.save(summary);
        return summary;
    }

    @Override
    public Page<GroupSummary> findMySummarysByPage(Long id, String title, String description, String publish_time_start, String publish_time_end, Integer page, Integer pageSize) {
        Specification<GroupSummary> specification = Specifications.<GroupSummary>and()
                .like(StringUtils.isNotBlank(title), "title", "%" + title + "%")
                .like(StringUtils.isNotBlank(description), "description", "%" + description + "%")
                .eq("user.id", id)
                .between(StringUtils.isNotBlank(publish_time_start) && StringUtils.isNoneBlank(publish_time_end),
                        "publish_time", Utils.timeFormat(publish_time_start), Utils.timeFormat(publish_time_end))
                .build();

        return summaryRepository.findAll(specification, PageRequest.of(page, pageSize));
    }
}

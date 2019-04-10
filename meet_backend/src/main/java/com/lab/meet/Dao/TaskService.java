package com.lab.meet.Dao;

import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.User;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<GroupTask> findTaskByPage(
            String title, String description,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end, Integer page, Integer pageSize);

    GroupTask addTask(String title, String description,
                      String start_time, String user_ids, String file_ids,String head_img);

    boolean deleteTaskById(Long id);

    Page<GroupTask> findMyTaskByPage(
            Long id,
            String title, String description,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end, Integer page, Integer pageSize);
}

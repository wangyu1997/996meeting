package com.lab.meet.Dao;

import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.GroupTask;
import org.springframework.data.domain.Page;

public interface SummaryService {
    Page<GroupSummary> findSummarysByPage(
            String title, String description,
            String publish_time_start, String publish_time_end,
            Integer page, Integer pageSize);

    GroupSummary addSummary(String title, String description, Long task_id, Long user_id) throws Exception;

    Page<GroupSummary> findMySummarysByPage(Long id,
                                            String title, String description,
                                            String publish_time_start, String publish_time_end,
                                            Integer page, Integer pageSize);

}

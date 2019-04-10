package com.lab.meet.Dao;

import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.PersonMeet;
import org.springframework.data.domain.Page;

public interface PersonMeetService {

    Page<PersonMeet> findPersonMeetsByPage(
            String title, String description, String next_week_plan,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end,
            Integer page, Integer pageSize);

    PersonMeet addPersonMeet(String title, String description,
                             String next_week_plan, String start_time,
                             Long user_id, String file_ids);

    Page<PersonMeet> findMyPersonMeetsByPage(Long id,
                                             String title, String description, String next_week_plan,
                                             String publish_time_start, String publish_time_end,
                                             String start_time_start, String start_time_end,
                                             Integer page, Integer pageSize);
}

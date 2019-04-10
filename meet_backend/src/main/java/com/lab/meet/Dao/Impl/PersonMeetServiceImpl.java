package com.lab.meet.Dao.Impl;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import com.lab.meet.Dao.PersonMeetService;
import com.lab.meet.Dao.TaskService;
import com.lab.meet.Model.*;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupTaskRepository;
import com.lab.meet.Repository.PersonMeetRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Service
public class PersonMeetServiceImpl implements PersonMeetService {

    @Autowired
    PersonMeetRepository meetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    public Page<PersonMeet> findPersonMeetsByPage(
            String title, String description, String next_week_plan,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end,
            Integer page, Integer pageSize) {
        Specification<PersonMeet> specification = Specifications.<PersonMeet>and()
                .like(StringUtils.isNotBlank(title), "title", "%" + title + "%")
                .like(StringUtils.isNotBlank(description), "description", "%" + description + "%")
                .like(StringUtils.isNotBlank(description), "next_week_plan", "%" + next_week_plan + "%")
                .between(StringUtils.isNotBlank(publish_time_start) && StringUtils.isNoneBlank(publish_time_end),
                        "publish_time", Utils.timeFormat(publish_time_start), Utils.timeFormat(publish_time_end)) //or eq("company", (Object) null)
                .between(StringUtils.isNotBlank(start_time_start) && StringUtils.isNoneBlank(start_time_end),
                        "start_time", Utils.timeFormat(start_time_start), Utils.timeFormat(start_time_end)) //or eq("company", (Object) null)
                .build();

        return meetRepository.findAll(specification, PageRequest.of(page, pageSize));
    }

    @Override
    public PersonMeet addPersonMeet(String title, String description,
                                    String next_week_plan, String start_time,
                                    Long user_id, String file_ids) {
        PersonMeet meet = new PersonMeet();
        meet.setTitle(title);
        meet.setDescription(description);
        meet.setNext_week_plan(next_week_plan);
        meet.setPublish_time(new Date().getTime());
        meet.setStart_time(Utils.timeFormat(start_time));
        meet.setFile_ids(file_ids);
        User user = userRepository.findSysUserById(user_id);
        if (user != null)
            meet.setUser(user);
        meet = meetRepository.save(meet);
        return meet;
    }

    public Page<PersonMeet> findMyPersonMeetsByPage(
            Long id,
            String title, String description, String next_week_plan,
            String publish_time_start, String publish_time_end,
            String start_time_start, String start_time_end,
            Integer page, Integer pageSize) {
        Specification<PersonMeet> specification = Specifications.<PersonMeet>and()
                .like(StringUtils.isNotBlank(title), "title", "%" + title + "%")
                .like(StringUtils.isNotBlank(description), "description", "%" + description + "%")
                .like(StringUtils.isNotBlank(description), "next_week_plan", "%" + next_week_plan + "%")
                .eq("user.id", id)
                .between(StringUtils.isNotBlank(publish_time_start) && StringUtils.isNoneBlank(publish_time_end),
                        "publish_time", Utils.timeFormat(publish_time_start), Utils.timeFormat(publish_time_end)) //or eq("company", (Object) null)
                .between(StringUtils.isNotBlank(start_time_start) && StringUtils.isNoneBlank(start_time_end),
                        "start_time", Utils.timeFormat(start_time_start), Utils.timeFormat(start_time_end)) //or eq("company", (Object) null)
                .build();

        return meetRepository.findAll(specification, PageRequest.of(page, pageSize));
    }
}

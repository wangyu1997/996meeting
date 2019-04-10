package com.lab.meet.Dao.Impl;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import com.lab.meet.Dao.TaskService;
import com.lab.meet.Dao.UserService;
import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    @Autowired
    PersonMeetRepository meetRepository;
    @Autowired
    GroupTaskRepository taskRepository;
    @Autowired
    GroupSummaryRepository summaryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findUserByPage(String username, String email, String contact, Integer page, Integer pageSize) {
        Specification<User> specification = Specifications.<User>and()
                .like(StringUtils.isNotBlank(username), "username", "%" + username + "%")
                .like(StringUtils.isNotBlank(email), "email", "%" + email + "%")
                .like(StringUtils.isNotBlank(contact), "contact", "%" + contact + "%") //or eq("company", (Object) null)
                .build();

        Sort sort = Sorts.builder()
                .asc(StringUtils.isNotBlank(username), "username")
                .asc("email")
                .build();

        return userRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    @Override
    public boolean deleteUserById(Long id) {
        meetRepository.deletePersonMeetByUserId(id);
        taskRepository.deleteTasksRelationByUserID(id);
        summaryRepository.deleteGroupSummaryByUserId(id);
        return userRepository.deleteUserById(id) == 1;
    }
}

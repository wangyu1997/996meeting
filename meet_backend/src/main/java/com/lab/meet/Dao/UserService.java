package com.lab.meet.Dao;

import com.lab.meet.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface UserService {
    Page<User> findUserByPage(String username, String email, String contact, Integer page, Integer pageSize);

    boolean deleteUserById(Long id);
}

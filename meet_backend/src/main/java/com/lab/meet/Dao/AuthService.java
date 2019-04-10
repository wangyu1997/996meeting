package com.lab.meet.Dao;


import com.lab.meet.Model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthService {
    User register(String username, String password, String email, String contact, HttpServletRequest request) throws Exception;

    Map<String, Object> login(String username, String password);

    String refresh(String oldToken);

    User getCurrentUser(String oldToken);
}  
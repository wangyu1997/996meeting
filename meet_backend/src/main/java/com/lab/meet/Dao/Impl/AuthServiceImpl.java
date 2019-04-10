package com.lab.meet.Dao.Impl;

import com.lab.meet.Dao.AuthService;
import com.lab.meet.Model.Role;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.RoleRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.JwtTokenUtil;
import com.lab.meet.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final static String USER_PROPERTY = "ROLE_USER";

    @Value("${token.head}")
    private String tokenHead;

    @Value("${upload.default_avatar}")
    String DEFAULT_AVATAR;

    @Value("${upload.avatars}")
    String AVATAR_FOLDER;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password, String email, String contact, HttpServletRequest request) throws Exception {
        System.out.println(username + "====");
        if (userRepository.findUserByUsername(username) != null) {
            throw new Exception("用户名已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        Role role = roleRepository.findSysRoleByName(AuthServiceImpl.USER_PROPERTY);
        if (role == null) {
            role = Role.GenerateUserRole();
            roleRepository.save(role);
        }
        String default_avatar = Utils.getDefaultAvatarUrl(request, AVATAR_FOLDER, DEFAULT_AVATAR);
        Set<Role> roleList = user.getRoles();
        roleList.add(role);
        user.setRoles(roleList);
        user.setUsername(username);
        user.setAvatar(default_avatar);
        user.setContact(contact);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setLastPasswordResetDate(new Date());
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security  
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token  
        User user = (User) userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(user);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        return map;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = (User) userDetailsService.loadUserByUsername(username);
        System.out.println(token);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public User getCurrentUser(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (User) userDetailsService.loadUserByUsername(username);
    }


}  
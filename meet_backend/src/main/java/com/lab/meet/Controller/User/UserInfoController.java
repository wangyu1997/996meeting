package com.lab.meet.Controller.User;

import com.lab.meet.Dao.AuthService;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.Status;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api("用户-个人信息管理")
@RequestMapping("/user/info")
public class UserInfoController {
    final UserRepository userRepository;
    @Value("${token.header}")
    private String tokenHeader;
    final AuthService authService;
    final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserInfoController(AuthService authService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "修改个人信息", notes = "用户修改个人信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatar", value = "用户头像", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "avatar", value = "用户邮箱", dataType = "String", paramType = "form"),
    })
    @PostMapping("/update")
    public ResponseBody<?> updateUser(String avatar, String password, String email, HttpServletRequest httpServletRequest) throws Exception {
        String token = httpServletRequest.getHeader(tokenHeader);
        User user = authService.getCurrentUser(token);
        if (user == null)
            return ResponseBody.failedRep(Status.USER_NOT_EXIST);
        else {
            if (avatar != null && !avatar.isEmpty())
                user.setAvatar(avatar);
            if (password != null && !password.isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(password));
            }
            if (email != null && !email.isEmpty())
                user.setEmail(email);
            if (userRepository.save(user) != null)
                return ResponseBody.successRep(user);
            else return ResponseBody.failedRep();
        }
    }

    @ApiOperation(value = "获取个人信息", notes = "用户获取个人信息", httpMethod = "GET")
    @GetMapping("")
    public ResponseBody<?> getInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(tokenHeader);
        User user = authService.getCurrentUser(token);
        return ResponseBody.successRep(user);
    }
}

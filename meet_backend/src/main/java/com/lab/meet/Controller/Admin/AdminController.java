package com.lab.meet.Controller.Admin;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api("管理员-个人信息管理")
@RequestMapping("/admin/info")
public class AdminController {

    @Value("${token.header}")
    String tokenHeader;

    final AuthService authService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserRepository userRepository;

    @Autowired
    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "管理员修改密码", notes = "管理员密码修改", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/update")
    public ResponseBody<?> updatePassword(String password, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(tokenHeader);
        User user = authService.getCurrentUser(token);
        if (user == null)
            return ResponseBody.failedRep(Status.LOGIN_FAILED);
        else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (password.isEmpty())
                return ResponseBody.failedRep(Status.PASSWORD_NOT_NULL);
            else {
                user.setPassword(encoder.encode(password));
                if (userRepository.save(user) != null)
                    return ResponseBody.successRep(user);
                else
                    return ResponseBody.failedRep();
            }
        }
    }

}

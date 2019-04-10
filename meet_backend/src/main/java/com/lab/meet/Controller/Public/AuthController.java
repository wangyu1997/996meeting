package com.lab.meet.Controller.Public;

import com.lab.meet.Dao.AuthService;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Api("通用-用户认证")
@RequestMapping("/auth")
public class AuthController {

    @Value("${token.header}")
    String tokenHeader;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "用户登陆",
            notes = "根据用户名密码进行登陆,返回当前用户信息和token",
            httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseBody<?> createAuthenticationToken(
            String username, String password
    ) {
        Map map = authService.login(username, password);
        return ResponseBody.getRep(Status.LOGIN_SUCCESS, map);
    }

    @ApiOperation(value = "用户token刷新",
            notes = "本地保存token，在下次使用时可以获得新的token，延长token时间，免登录",
            httpMethod = "GET")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseBody<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseBody.failedRep(Status.TOKEN_REFRESH_FAILED);
        } else {
            return ResponseBody.getRep(Status.TOKEN_REFRESH_SUCCESS, refreshedToken);
        }
    }

    @ApiOperation(value = "用户注册", notes = "根据用户名密码进行注册,返回用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户昵称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contact", value = "联系方式", required = true, dataType = "String", paramType = "form"),
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseBody<?> register(String username, String password, String email, String contact, HttpServletRequest request) throws Exception {
        return ResponseBody.successRep(authService.register(username, password, email, contact, request));
    }
}  
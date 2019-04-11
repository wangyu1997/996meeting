package com.lab.meet.Controller.Admin;

import com.lab.meet.Dao.AuthService;
import com.lab.meet.Dao.UserService;
import com.lab.meet.Model.ResponseBody;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.RoleRepository;
import com.lab.meet.Repository.UserRepository;
import com.lab.meet.Util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@Api("管理员-用户信息管理")
@RequestMapping("/admin/users")
public class AdminUserController {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final UserService userService;
    final AuthService authService;

    @Autowired
    public AdminUserController(UserRepository userRepository, RoleRepository roleRepository, UserService userService, AuthService authService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.authService = authService;
    }


    @ApiOperation(value = "管理员查询所有用户信息", notes = "查询所有用户信息", httpMethod = "GET")
    @GetMapping("")
    public ResponseBody<?> findAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseBody.successRep(users);
    }

    @ApiOperation(value = "管理员查询根据过滤条件对用户做分页查询", notes = "分页查询用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "contact", value = "联系方式", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query")
    })
    @GetMapping("/page")
    public ResponseBody<?> findUsersByPage(String username, String email, String contact, Integer page, Integer pageSize) {
        Page<User> users = userService.findUserByPage(username, email, contact, page - 1, pageSize);
        Map<String, Object> result = Utils.formatPagable(users);
        result.put("current", page);
        return ResponseBody.successRep(result);
    }

    @ApiOperation(value = "管理员根据id查询用户信息", notes = "查询用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public ResponseBody<?> findUserById(@PathVariable Long id) {
        User user = userRepository.findSysUserById(id);
        return ResponseBody.successRep(user);
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "avatar", value = "用户头像", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contact", value = "联系方式", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "role_ids", value = "权限id (用','隔开)", dataType = "String", paramType = "form"),
    })
    @PostMapping("/update/{id}")
    public ResponseBody<?> updateUser(@PathVariable Long id, String avatar, String password, String email, String contact, String role_ids) throws Exception {
        User user = userRepository.findSysUserById(id);
        if (user == null)
            throw new Exception("The User which id is " + id + " is not exist");
        if (StringUtils.isNoneBlank(avatar))
            user.setAvatar(avatar);
        if (StringUtils.isNoneBlank(password)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
        }
        if (StringUtils.isNoneBlank(email))
            user.setEmail(email);
        if (StringUtils.isNoneBlank(contact))
            user.setContact(contact);
        if (StringUtils.isNoneBlank(role_ids))
            user.setRoles(Utils.findRolesByIds(roleRepository, role_ids));
        if ((user = userRepository.save(user)) != null)
            return ResponseBody.successRep(user);
        else return ResponseBody.failedRep();
    }

    @ApiOperation(value = "管理员新增用户", notes = "管理员新增用户", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatar", value = "用户头像", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "username", value = "用户昵称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contact", value = "联系方式", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "role_ids", value = "权限id (用','隔开)", dataType = "String", paramType = "form"),
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseBody<?> addUser(String avatar, String username, String password, String email, String contact, String role_ids, HttpServletRequest request) throws Exception {
        User user = authService.register(avatar, username, password, email, contact, request);
        if (StringUtils.isNoneBlank(avatar))
            user.setAvatar(avatar);
        user.setRoles(Utils.findRolesByIds(roleRepository, role_ids));
        user = userRepository.save(user);
        if (user == null)
            return ResponseBody.failedRep();
        return ResponseBody.successRep(user);
    }

    @ApiOperation(value = "删除用户", notes = "删除用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/delete/{id}")
    public ResponseBody<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteUserById(id))
            return ResponseBody.successRep(null);
        else
            return ResponseBody.failedRep();
    }
}

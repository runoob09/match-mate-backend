package github.runoob09.controller;

import github.runoob09.common.exception.BusinessException;
import github.runoob09.common.result.BasicResult;
import github.runoob09.common.result.ResultEnum;
import github.runoob09.entity.User;
import github.runoob09.request.UserLoginRequest;
import github.runoob09.request.UserRegisterRequest;
import github.runoob09.request.UserSearchRequest;
import github.runoob09.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author runoob09
 * @date 2024年07月28日 09:28:01
 * @description
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 执行用户登录操作
     *
     * @param loginRequest 用户登录的请求对象
     * @param request      请求对象
     * @return 查询到的用户信息
     */
    @PostMapping("login")
    public BasicResult<User> userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(loginRequest.getUserAccount(), loginRequest.getUserPassword())) {
            throw BusinessException.of(ResultEnum.PARAM_ERROR,"账号或密码不能为空");
        }
        User user = userService.doLogin(loginRequest.getUserAccount(), loginRequest.getUserPassword(), request);
        return BasicResult.success(user);
    }

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册的请求对象
     * @return 用户的新id
     */
    @PostMapping("register")
    public BasicResult<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw BusinessException.of(ResultEnum.PARAM_ERROR,"注册信息不能为空");
        }
        Long id = userService.userRegister(userRegisterRequest);
        return BasicResult.success(id);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("delete/{id}")
    public BasicResult<Boolean> deleteUser(@PathVariable("id") Long id) {
        if (id == null) {
            log.error("user id cannot be null");
            throw BusinessException.of(ResultEnum.PARAM_ERROR,"缺少要删除的用户id");
        }
        return BasicResult.success(userService.deleteUser(id),"删除成功");
    }

    /**
     * 搜索用户
     *
     * @param searchRequest 搜索用户的请求对象
     * @return 搜索到的用户列表
     */
    @GetMapping("search")
    public BasicResult<List<User>> searchUser(UserSearchRequest searchRequest) {
        if (searchRequest == null) {
            log.warn("Search request is null.");
            searchRequest = new UserSearchRequest();
        }
        return BasicResult.success(userService.searchUsers(searchRequest),"搜索成功");
    }

    @GetMapping("currentUser")
    public BasicResult<User> currentUser(HttpServletRequest request) {
        return BasicResult.success(userService.currentUser(request));
    }

    @GetMapping("logout")
    public BasicResult<Boolean> logout(HttpServletRequest request) {
        return BasicResult.success(userService.logout(request));
    }
}

package github.runoob09.service;

import github.runoob09.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import github.runoob09.request.UserRegisterRequest;
import github.runoob09.request.UserSearchRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZJH
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-07-24 13:16:48
 */
public interface UserService extends IService<User> {
    /**
     * 执行用户注册的方法
     * @param userRegisterRequest 用户注册的请求封装类
     * @return
     */
    Long userRegister(UserRegisterRequest userRegisterRequest); // 用户注册

    /**
     * 执行登陆操作
     * @param userAccount 用户账号
     * @param userPassword 用户名
     * @return 用户对象
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏工具类
     * @param user 原用户对象
     * @return 脱敏后的用户对象
     */
    User convertToSafeUser(User user);

    /**
     * 查询用户的服务类
     * @param request 用户请求实体类
     * @return 查询到的用户列表
     */
    List<User> searchUsers(UserSearchRequest request);

    /**
     * 删除指定id的用户
     * @param userId 用户的唯一id
     * @return 执行的状态
     */
    Boolean deleteUser(Long userId);

    /**
     * 获取当前用户
     * @param request 当前的请求对象
     * @return 当前已登录的用户信息
     */
    User currentUser(HttpServletRequest request);

    /**
     * 用户退出登录的方法
     * @param request 当前的请求对象
     * @return 退出登录的状态
     */
    Boolean logout(HttpServletRequest request);
}

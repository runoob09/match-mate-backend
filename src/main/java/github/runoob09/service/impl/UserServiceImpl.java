package github.runoob09.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import github.runoob09.common.annotation.RequireRole;
import github.runoob09.common.exception.BusinessException;
import github.runoob09.common.result.ResultEnum;
import github.runoob09.constant.UserConstant;
import github.runoob09.entity.User;
import github.runoob09.entity.request.UserRegisterRequest;
import github.runoob09.entity.request.UserSearchRequest;
import github.runoob09.service.UserService;
import github.runoob09.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static github.runoob09.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author ZJH
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-07-24 13:16:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 账户名校验
     */
    private static final Pattern USER_ACCOUNT_CHECK = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{4,}$");
    /**
     * 密码校验
     */
    private static final Pattern USER_PASSWORD_CHECK = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,}$");
    /**
     * md5加密混淆
     */
    private final static String SALT = "X8s9D2Z3jK4nM6bR";


    /**
     * 执行用户注册操作
     *
     * @param userRegisterRequest 用户注册的请求封装类
     * @return 成功注册后的用户id
     */
    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        // 检查用户输入的数据
        if (StringUtils.isAllBlank(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(), userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 校验用户名
        if (!USER_ACCOUNT_CHECK.matcher(userRegisterRequest.getUserAccount()).matches()) {
            return -1L;
        }
        // 检查两次密码是否一致
        if (!userRegisterRequest.getUserPassword().equals(userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 校验密码是否合法
        if (!USER_PASSWORD_CHECK.matcher(userRegisterRequest.getUserPassword()).matches()) {
            return -1L;
        }
        // 校验两次密码是否正确
        if (!userRegisterRequest.getUserPassword().equals(userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 查询用户名是否已经存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userRegisterRequest.getUserAccount());
        User user = this.getOne(queryWrapper);
        if (user != null) {
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "用户名已存在");
        }
        // 对密码进行加密
        String newPassword = DigestUtil.md5Hex((userRegisterRequest.getUserPassword() + "_" + SALT).getBytes());
        user = new User(userRegisterRequest.getUserAccount(), newPassword);
        // 存储用户
        save(user);
        log.info("User register successful: {}", user.getUsername());
        return user.getId();
    }

    /**
     * 执行登陆操作
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户对象
     */
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 检查参数不为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "用户名或密码为空");
        }
        // 不能包含特殊字符
        if (!(USER_ACCOUNT_CHECK.matcher(userAccount).matches() && USER_PASSWORD_CHECK.matcher(userPassword).matches())) {
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "用户名或密码不符合要求");
        }
        // 对密码进行加密
        String encryptPassword = DigestUtil.md5Hex((userPassword + "_" + SALT).getBytes());
        // 从数据库中查询是否有对应的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.error("No user found with account: {} with provided password.", userAccount);
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "用户名或密码错误");
        }
        // 存储用户信息
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 返回用户的脱敏信息
        log.info("User login successful: {}", user.getUsername());
        return convertToSafeUser(user);
    }

    /**
     * 用户脱敏方法
     *
     * @param user 未脱敏的用户对象
     * @return 脱敏后的用户对象
     */
    @Override
    public User convertToSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setUserPassword(null);
        safeUser.setPhoneNumber(user.getPhoneNumber());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setIsDelete(user.getIsDelete());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setUserTags(user.getUserTags());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(user.getUpdateTime());
        return safeUser;
    }

    /**
     * 查询用户的服务类
     *
     * @param searchRequest 用户请求实体类
     * @return 查询到的用户列表
     */
    @RequireRole(roles = UserConstant.Role.ADMIN)
    @Override
    public List<User> searchUsers(UserSearchRequest searchRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (searchRequest == null) {
            searchRequest = new UserSearchRequest();
        }
        if (searchRequest.getUsername() != null) {
            queryWrapper = queryWrapper.like(User::getUsername, searchRequest.getUsername());
        }
        if (searchRequest.getUserRole() != null) {
            queryWrapper = queryWrapper.eq(User::getUserRole, searchRequest.getUserRole());
        }
        if (searchRequest.getUserStatus() != null) {
            queryWrapper = queryWrapper.eq(User::getUserStatus, searchRequest.getUserStatus());
        }
        if (searchRequest.getUserAccount() != null) {
            queryWrapper = queryWrapper.like(User::getUserAccount, searchRequest.getUserAccount());
        }
        if (searchRequest.getGender() != null) {
            queryWrapper = queryWrapper.eq(User::getGender, searchRequest.getGender());
        }
        List<User> userList = list(queryWrapper);
        log.info("User search successful, userList length is {}, searchRequest is {} ", userList.size(), searchRequest);
        return userList.stream().map(this::convertToSafeUser).collect(Collectors.toList());
    }

    /**
     * 删除指定id的用户
     *
     * @param userId 用户的唯一id
     * @return 执行的状态
     */
    @RequireRole(roles = UserConstant.Role.ADMIN)
    @Override
    public Boolean deleteUser(Long userId) {
        if (userId == null) {
            log.error("You cannot use a null ID to delete a user, because the user ID cannot be null.");
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "用户ID不能为空");
        }
        log.info("User delete successful: {}", userId);
        return removeById(userId);
    }

    /**
     * 获取当前用户
     *
     * @return 当前已登录的用户信息
     */
    @Override
    public User currentUser(HttpServletRequest request) {
        if (request == null) {
            log.error("Request can not be null");
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "系统不能找到请求对象");
        }
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            log.error("user is not login, cannot find currentUser!");
            throw BusinessException.of(ResultEnum.NOT_LOGIN, "用户未登录或登录已过期");
        }
        // 查询该用户的最新信息
        currentUser = getById(currentUser.getId());
        // 更新用户的信息
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);
        log.info("Current user is update: {}", currentUser.getUsername());
        return currentUser;
    }

    /**
     * 用户退出登录的方法
     *
     * @param request 当前的请求对象
     * @return 退出登录的状态
     */
    @Override
    public Boolean logout(HttpServletRequest request) {
        if (request == null) {
            log.error("The request object used for logging out is null."); //退出登录所使用的request对象为空
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "系统不能找到请求对象");
        }
        HttpSession session = request.getSession();
        if (session == null) {
            log.error("The session used for logging out is null.");
            throw BusinessException.of(ResultEnum.SYSTEM_ERROR, "系统不能找到会话对象");
        }
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            log.error("User session has expired.");// 用户会话已过期
            return true;
        }
        session.removeAttribute(USER_LOGIN_STATE);
        log.info("User logout successful, id is {}.", user.getId());
        return true;
    }

    /**
     * 根据标签搜索对应的用户
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            log.error("Tag name list is empty.");
            throw BusinessException.of(ResultEnum.PARAM_ERROR, "标签列表不能为空");
        }
        List<User> userList = null;
        CompletableFuture<List<User>> task1 = CompletableFuture.supplyAsync(() -> searchUsersByTagsInMysql(tagNameList));
        CompletableFuture<List<User>> task2 = CompletableFuture.supplyAsync(() -> searchUsersByTagsInMemory(tagNameList));
        CompletableFuture<Object> result = CompletableFuture.anyOf(task1, task2);
        try {
            userList = (List<User>) result.get();
        } catch (Exception e) {
            log.error("Search users by tags failed, error message is {}", e.getMessage());
            throw BusinessException.of(ResultEnum.SYSTEM_ERROR, "无法获取到查询结果");
        }
        // 取消对应任务
        task1.cancel(true);
        task2.cancel(true);
        return userList;
    }

    /**
     * 在mysql内利用标签进行查询
     */
    private List<User> searchUsersByTagsInMysql(List<String> tagNameList) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        for (String s : tagNameList) {
            queryWrapper = queryWrapper.like(User::getUserTags, s);
        }
        List<User> userList = list(queryWrapper);
        userList = userList.stream().map(this::convertToSafeUser).toList();
        return userList;
    }

    /**
     * 在内存内利用标签进行查询
     */
    private List<User> searchUsersByTagsInMemory(List<String> tagNameList) {
        // 查询全部的用户
        List<User> userList = list();
        userList = userList.parallelStream().filter(user -> {
            // 获取用户的标签列表
            List<String> tags = user.getUserTags();
            if (CollectionUtils.isEmpty(tags)) {
                return false;
            }
            // 遍历用户的标签列表
            for (String tagName : tagNameList) {
                // 如果用户标签列表不包含该标签，则返回false
                if (!tags.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::convertToSafeUser).toList();
        return userList;
    }
}
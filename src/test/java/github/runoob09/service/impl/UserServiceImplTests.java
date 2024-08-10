package github.runoob09.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import github.runoob09.entity.User;
import github.runoob09.request.UserRegisterRequest;
import github.runoob09.mapper.UserMapper;
import github.runoob09.request.UserSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;

import static github.runoob09.constant.UserConstant.USER_LOGIN_STATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImplTests.class);
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    private UserRegisterRequest userRegisterRequest;
    private MockHttpServletRequest request;

    private User user;
    @BeforeEach
    void setUp() {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserAccount("user123");
        userRegisterRequest.setUserPassword("Password1");
        userRegisterRequest.setCheckPassword("Password1");
        request = new MockHttpServletRequest();
        // 生成一个用户
        user = new User();
        user.setUserAccount("testAccount");
        user.setUserPassword("Password1");
        user.setUserRole(0);
        user.setUserStatus(0);
        user.setUsername("user123");
    }

    @Test
    void testUserRegister_UserAlreadyExists() {
        User existingUser = new User();
        when(userMapper.selectOne(any())).thenReturn(existingUser);

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, times(1)).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_BlankFields() {
        userRegisterRequest.setUserAccount("");
        userRegisterRequest.setUserPassword("");
        userRegisterRequest.setCheckPassword("");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_PasswordsNotMatching() {
        userRegisterRequest.setCheckPassword("DifferentPassword");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_InvalidUsername() {
        userRegisterRequest.setUserAccount("us");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_InvalidPassword() {
        userRegisterRequest.setUserPassword("password");
        userRegisterRequest.setCheckPassword("password");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_Success() {
        when(userMapper.selectOne(any())).thenReturn(null);
        when(userMapper.insert(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(1L, result);

        verify(userMapper, times(1)).selectOne(any());
        verify(userMapper, times(1)).insert(any());
    }

    @Test
    void testDoLogin_Success() {
        // Arrange
        String userAccount = "user123";
        String userPassword = "User1234";
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        when(userMapper.selectOne(any())).thenReturn(user); // 模拟 userMapper 查询
        // Act
        User result = userService.doLogin(userAccount, userPassword, request);
        // Assert
        assertNotNull(result, "The result should not be null");
        assertNull(result.getUserPassword(), "The return user's password should be null");
        HttpSession session = request.getSession();
        assertEquals(user, session.getAttribute(USER_LOGIN_STATE), "The user should be set in session");
        verify(userMapper, times(1)).selectOne(any());
    }

    @Test
    void testDoLogin_UserNotFound() {
        // Arrange
        String userAccount = "user123";
        String userPassword = "User1234";
        // 模拟 userMapper.selectOne 方法返回 null，表示用户不存在
        when(userMapper.selectOne(any())).thenReturn(null);
        // Act
        User result = userService.doLogin(userAccount, userPassword, request);
        // Assert
        assertNull(result, "The result should be null when user is not found");
        verify(userMapper, times(1)).selectOne(any());
    }


    @Test
    void testDoLogin_InvalidParams() {
        // Arrange & Act
        User result1 = userService.doLogin("", "Valid1234", request);
        User result2 = userService.doLogin("validUser", "", request);
        // Assert
        assertNull(result1,"userAccount is an blank string, result should be null");
        assertNull(result2,"userPassword is an blank string, result should be null");
        // 判断查询是否触发
        verify(userMapper, times(0)).selectOne(any());
    }

    @Test
    void testDoLogin_SpecialCharacters() {
        // Arrange
        String userAccount = "invalid!User";
        String userPassword = "Invalid!1234";

        // Act
        User result = userService.doLogin(userAccount, userPassword, request);

        // Assert
        assertNull(result,"userAccount and userPassword are invalid, result should be null");
        verify(userMapper, times(0)).selectOne(any());
    }

    /**
     * 测试正常查询
     */
    @Test
    void testSearchUsersWithNonNullRequest() {
        UserSearchRequest searchRequest = new UserSearchRequest();
        searchRequest.setUsername("test");

        when(userMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(user));

        List<User> users = userService.searchUsers(searchRequest);

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("testAccount", users.get(0).getUserAccount());

        verify(userMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testSearchUsersWithNullRequest() {
        UserSearchRequest searchRequest = null;

        // Verify that the selectList method was never called due to the early return on null check
        verify(userMapper, never()).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testSearchUsersWhenMapperReturnsNoResults() {
        UserSearchRequest searchRequest = new UserSearchRequest();
        searchRequest.setUsername("nonexistentAccount");
        when(userMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        List<User> users = userService.searchUsers(searchRequest);
        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(userMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }
}

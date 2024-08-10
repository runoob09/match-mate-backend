package github.runoob09.request;

import lombok.Data;

/**
 * @author runoob09
 * @date 2024年08月01日 09:56:14
 * @description
 */
@Data
public class UserSearchRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户角色
     */
    private Integer userRole;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户性别
     */
    private Integer gender;
}

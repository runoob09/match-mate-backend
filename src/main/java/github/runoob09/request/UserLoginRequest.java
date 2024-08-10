package github.runoob09.request;

import lombok.Data;

/**
 * @author runoob09
 * @date 2024年07月24日 23:30:40
 * @description
 */
@Data
public class UserLoginRequest {
    private String userAccount; // 账号
    private String userPassword; // 密码
}

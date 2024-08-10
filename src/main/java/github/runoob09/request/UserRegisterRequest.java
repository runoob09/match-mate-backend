package github.runoob09.request;

import github.runoob09.entity.User;
import lombok.Data;

/**
 * @author runoob09
 * @date 2024年07月25日 12:48:05
 * @description
 */
@Data
public class UserRegisterRequest {
    private String userAccount;
    private String userPassword;
    private String checkPassword;

    // 转换方法
    public User convertToUSer(){
        User user = new User(userAccount,userPassword);
        return user;
    }
}

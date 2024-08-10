package github.runoob09.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import github.runoob09.constant.BasicConstant;
import github.runoob09.constant.UserConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * 非空主键
     */
    @TableId
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户性别 0-男性 1-女性 2-未知
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态 0-正常 1-封禁
     */
    private Integer userStatus;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", username=" + username +
                ", userAccount=" + userAccount +
                ", avatarUrl=" + avatarUrl +
                ", gender=" + gender +
                ", userPassword=" + userPassword +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email +
                ", userStatus=" + userStatus +
                ", isDelete=" + isDelete +
                ", userRole=" + userRole +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }

    public User(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        // 设置默认数据
        gender = UserConstant.Gender.UNKNOWN;
        userStatus = UserConstant.Status.ACTIVE;
        isDelete = BasicConstant.Status.ACTIVE;
        userRole = UserConstant.Role.USER;
        // 随机生成一个username
        username = "user_" + userAccount;
        // 设置用户的默认头像
        avatarUrl = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
    }
}
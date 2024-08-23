package github.runoob09.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import github.runoob09.constant.BasicConstant;
import github.runoob09.constant.UserConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * @TableName user
 */
@TableName(value = "user", autoResultMap = true, resultMap = "BaseResultMap")
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
     * 标签列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR, value = "user_tags", javaType = true)
    private List<String> userTags;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String userProfile;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        // 设置空的标签
        userTags = new ArrayList<>();
        // 设置用户的默认头像
        avatarUrl = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
    }
}
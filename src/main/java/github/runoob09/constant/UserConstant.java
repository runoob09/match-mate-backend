package github.runoob09.constant;

/**
 * @author runoob09
 * @date 2024年08月01日 09:24:51
 * @description
 */
public class UserConstant {
    public final static String USER_LOGIN_STATE = "userLoginState";
    /**
     * 用户性别常量
     */
    public static final class Gender {
        /**
         * 男
         */
        public static final int MALE = 0;
        /**
         * 女
         */
        public static final int FEMALE = 1;
        public static final int UNKNOWN = 2;
    }

    /**
     * 用户状态
     */
    public static final class Status {
        /**
         * 账户正常
         */
        public static final int ACTIVE = 0;
        /**
         * 账户被封禁
         */
        public static final int BANNED = 1;
        /**
         * 账户停用
         */
        public static final int DEACTIVATE = 2;
    }

    /**
     * 用户角色
     */
    public static final class Role {
        /**
         * 普通用户
         */
        public static final int USER = 0;
        /**
         * 管理员用户
         */
        public static final int ADMIN = 1;
    }
}

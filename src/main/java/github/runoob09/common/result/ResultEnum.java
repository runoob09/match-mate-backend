package github.runoob09.common.result;

import lombok.Getter;

/**
 * @author runoob09
 * @date 2024年08月08日 23:26:58
 * @description
 */
@Getter
public enum ResultEnum {
    SUCCESS(0, "成功"),
    PARAM_ERROR(40000, "参数错误"),
    NOT_LOGIN(40100, "用户未登录"),
    NO_AUTH(40101, "未授权用户"),
    NOT_FOUND(40400, "资源不存在"),

    SYSTEM_ERROR(50000, "系统错误");
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

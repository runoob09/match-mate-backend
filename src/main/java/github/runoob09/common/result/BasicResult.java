package github.runoob09.common.result;

import lombok.Data;
import lombok.Getter;

/**
 * @author runoob09
 * @date 2024年08月08日 22:45:57
 * @description 基础返回结果包装类
 */
@Getter
public class BasicResult<T> {
    private final Integer code;
    private final String message;
    private T data;
    private String description;

    /**
     * 私有构造方法（可供成功响应的请求使用）
     *
     * @param code
     * @param message
     * @param data
     */
    private BasicResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = "请求成功";
    }
    /**
     * 私有构造方法（可供成功响应的请求使用）
     *
     * @param code
     * @param message
     * @param data
     */
    private BasicResult(Integer code, String message, T data,String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    /**
     * 私有构造方法（可供失败响应的请求使用）
     */
    private BasicResult(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 私有构造方法（失败和成功均可用）
     * 成功表示没有返回数据的情况
     */
    private BasicResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功响应
     *
     * @param data 返回数据
     * @param <T>  响应数据类型
     * @return 对响应数据进行包装后的结果
     */
    public static <T> BasicResult<T> success(T data) {
        return new BasicResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应
     *
     * @param data 返回数据
     * @param <T>  响应数据类型
     * @return 对响应数据进行包装后的结果
     */
    public static <T> BasicResult<T> success(T data,String description) {
        return new BasicResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data,description);
    }
    /**
     * 失败响应
     *
     * @param resultEnum 响应枚举
     * @return 对响应数据进行包装后的结果
     */
    public static BasicResult<Void> fail(ResultEnum resultEnum, String description) {
        return new BasicResult<>(resultEnum.getCode(), resultEnum.getMessage(), description);
    }
}

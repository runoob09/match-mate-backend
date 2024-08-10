package github.runoob09.common.exception;

import github.runoob09.common.result.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author runoob09
 * @date 2024年08月09日 08:33:05
 * @description
 */

@Getter
public class BusinessException extends RuntimeException {
    /**
     * 异常码
     */
    private Integer code;
    /**
     * 异常描述
     */
    private String description;

    private ResultEnum resultEnum;

    public BusinessException(ResultEnum resultEnum, String description) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
        this.code = resultEnum.getCode();
        this.description = description;
    }

    public static BusinessException of(ResultEnum resultEnum, String description) {
        return new BusinessException(resultEnum, description);
    }
}

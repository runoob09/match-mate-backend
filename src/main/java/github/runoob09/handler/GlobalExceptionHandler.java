package github.runoob09.handler;

import github.runoob09.common.exception.BusinessException;
import github.runoob09.common.result.BasicResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author runoob09
 * @date 2024年08月09日 08:59:14
 * @description
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BasicResult<Void> handleBusinessException(BusinessException e) {
        return BasicResult.fail(e.getResultEnum(),e.getDescription());
    }
}

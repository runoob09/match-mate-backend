package github.runoob09.common.aop;

import github.runoob09.common.annotation.RequireRole;
import github.runoob09.common.exception.BusinessException;
import github.runoob09.common.result.ResultEnum;
import github.runoob09.constant.UserConstant;
import github.runoob09.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author runoob09
 * @date 2024年08月02日 12:45:41
 * @description
 */
@Aspect
@Slf4j
@Component
public class RequireRoleAOP {

    public boolean checkRole(int[] roles) {
        // 获取当前的request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            log.error("can not get http request object");
            throw BusinessException.of(ResultEnum.PARAM_ERROR,"查询不到用户的会话信息");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null) {
            log.error("user is not login");
            throw BusinessException.of(ResultEnum.NOT_LOGIN,"用户未登录");
        }
        Integer userRole = user.getUserRole();
        for (int role : roles) {
            if (userRole == role) {
                return true;
            }
        }
        log.error("user is not have permission, user id is{}", user.getId());
        return false;
    }

    @Pointcut("@annotation(github.runoob09.common.annotation.RequireRole)")
    public void point() {
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequireRole annotation = method.getAnnotation(RequireRole.class);
        if (annotation == null) {
            log.error("can not get annotation RequireRole from method:{}", method.getName());
            throw BusinessException.of(ResultEnum.SYSTEM_ERROR,"系统内部错误");
        }
        // 从注解中获取对应的数据信息
        int[] roles = annotation.roles();
        // 检查用户是否具有对应权限
        if (!checkRole(roles)) {
            throw BusinessException.of(ResultEnum.NO_AUTH,"没有权限");
        }
        return joinPoint.proceed();
    }
}

package github.runoob09.task;

import cn.hutool.core.collection.ListUtil;
import github.runoob09.constant.RedisKey;
import github.runoob09.constant.ThreadPoolConstant;
import github.runoob09.entity.User;
import github.runoob09.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author runoob09
 * @date 2024年08月20日 21:10:13
 * @description
 */
@Component
@Slf4j
public class OnceTimeTask {
    // 注入io密集线程池
    @Resource(name = ThreadPoolConstant.IO_INTENSIVE_EXECUTOR)
    private Executor executor;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @PostConstruct
    public void initUserData() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 尝试去设置值
        redisTemplate.opsForValue().setIfAbsent(RedisKey.ONCE_TIME_TASK_INIT_USER, true);
        boolean flag = false;
        // 查询配置文件
        Object o = redisTemplate.opsForValue().get(RedisKey.ONCE_TIME_TASK_INIT_USER);
        if (o instanceof Boolean) {
            flag = (boolean) o;
        }
        if (!flag) {
            log.info("用户数据已初始化，跳过初始化");
            return;
        }
        // 创建用户列表
        LinkedList<User> userList = new LinkedList<>();
        int total = 100000;
        int batchSize = 10000;
        for (int i = 0; i < total; i++) {
            User user = new User();
            user.setUserAccount("runoob" + i);
            user.setUserPassword("12345678");
            user.setUsername("runoob" + i);
            user.setGender(0);
            user.setPhoneNumber("1234567890");
            user.setEmail("runoob" + i + "@gmail.com");
            user.setUserRole(0);
            user.setUserStatus(0);
            user.setUserTags(List.of("java", "python", "c++"));
            user.setUserProfile("https://runoob09.github.io/");
            user.setAvatarUrl("https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
            userList.add(user);
        }
        List<List<User>> splitResult = ListUtil.split(userList, batchSize); // 分割后的结果
        for (List<User> list : splitResult) {
            CompletableFuture.supplyAsync(() -> {
                userService.saveBatch(list);
                return null;
            }, executor);
        }
        stopWatch.stop();
        log.info("初始化用户数据完成，耗时：{} ms", stopWatch.getTotalTimeMillis());
        // 更新redis标志
        redisTemplate.opsForValue().set(RedisKey.ONCE_TIME_TASK_INIT_USER, false);
    }

}

package github.runoob09.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author runoob09
 * @date 2024年07月28日 10:26:35
 * @description
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(); // 使用默认的雪花算法生成器
    }
}

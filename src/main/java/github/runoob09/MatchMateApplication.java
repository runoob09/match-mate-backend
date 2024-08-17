package github.runoob09;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "github.runoob09.mapper")
public class MatchMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchMateApplication.class, args);
    }

}

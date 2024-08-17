package github.runoob09.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author runoob09
 * @date 2024年08月17日 21:08:00
 * @description
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("github.runoob09.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("match-mate")
                .description("伙伴匹配项目接口文档")
                .termsOfServiceUrl("https://github.com/runoob09/match-mate-backend")
                .contact(new Contact("runoob09", "https://github.com/runoob09", "2499469495@qq.com"))
                .version("1.0")
                .build();
    }
}

package com.arrange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * swagger配置类
 * 接口地址：http://localhost:80/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //作者信息
    Contact contact = new Contact("","","");
    //配置Swagger的Docket的bean实例
    @Bean
    public Docket docker(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)//如果为false则不能在浏览器中访问
                .select()
                //指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.arrange.controller"))
                .build();
    }

    //配置Swagger信息 = apiInfo
    private ApiInfo apiInfo(){
        return new ApiInfo
                ("arrange API"
                ,"排班系统接口文档"
                ,"v1.0"
                ,"bit"
                ,contact
                ,"Apache 2.0"
                ,"http://www.apache.org/licenses/LICENSE-2.0"
                ,new ArrayList<>());
    }
}


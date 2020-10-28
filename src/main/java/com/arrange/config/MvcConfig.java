package com.arrange.config;

import com.arrange.interceptor.JwtInterceptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "interceptorconfig")
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 这些请求无条件通过拦截器
     */
    private String[] excludePathPatterns;

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(excludePathPatterns);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toBit1").setViewName("toBit1");
    }
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("wishing."); //允许任何域名
//        return corsConfiguration;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); //注册
//        return new CorsFilter(source);
//    }
}

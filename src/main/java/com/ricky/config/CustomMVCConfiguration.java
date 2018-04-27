package com.ricky.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;


/**
 * Created by baiguantao on 2017/6/28.
 *
 */

@EnableWebMvc
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class CustomMVCConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        //设置默认主页路径
        registry.addViewController( "/" ).setViewName( "/index" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         //设置webjars资源路径
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        super.addResourceHandlers(registry);
    }


}

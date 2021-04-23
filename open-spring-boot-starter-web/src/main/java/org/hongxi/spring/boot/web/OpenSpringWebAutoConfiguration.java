package org.hongxi.spring.boot.web;

import org.hongxi.spring.boot.web.advice.RequestAdvice;
import org.hongxi.spring.boot.web.advice.ResponseAdvice;
import org.hongxi.spring.boot.web.constants.WebConstants;
import org.hongxi.spring.boot.web.controller.OpenbootController;
import org.hongxi.spring.boot.web.exception.DefaultExceptionHandler;
import org.hongxi.spring.boot.web.filter.MonitorFilter;
import org.hongxi.spring.boot.web.filter.RequestResponseWrapperFilter;
import org.hongxi.spring.boot.web.filter.SessionFilter;
import org.hongxi.spring.boot.web.interceptor.SessionInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

import static org.hongxi.spring.boot.common.constants.Constants.*;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenSpringWebAutoConfiguration {

    @Bean
    public OpenbootController openbootController() {
        return new OpenbootController();
    }

    @Configuration
    public static class InterceptorAutoConfiguration implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new SessionInterceptor())
                    .excludePathPatterns(WebConstants.EXCLUDE_PATHS)
                    .order(-100);
        }

        @Override
        public void configurePathMatch(PathMatchConfigurer configurer) {
            configurer.setUseTrailingSlashMatch(true);
        }
    }

    @Configuration
    public static class FilterAutoConfiguration {

        /**
         * ShallowEtagHeaderFilter 对 response 进行了 update
         * 如使用 response.getWriter().println() ，实际输出将不会换行
         */
        @Bean
        @ConditionalOnProperty(prefix = ETAG_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
        public FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean() {
            FilterRegistrationBean<ShallowEtagHeaderFilter> filterBean = new FilterRegistrationBean<>();
            filterBean.setFilter(new ShallowEtagHeaderFilter());
            filterBean.setUrlPatterns(Collections.singletonList("/*"));
            return filterBean;
        }

        @Bean
        @ConditionalOnProperty(prefix = MONITOR_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
        public MonitorFilter monitorFilter() {
            return new MonitorFilter();
        }

        @Bean
        public SessionFilter sessionFilter() {
            return new SessionFilter();
        }

        @Bean
        public RequestResponseWrapperFilter requestResponseWrapperFilter() {
            return new RequestResponseWrapperFilter();
        }
    }

    @Bean
    public DefaultExceptionHandler defaultExceptionHandler() {
        return new DefaultExceptionHandler();
    }

    @Bean
    public RequestAdvice requestAdvice() {
        return new RequestAdvice();
    }

    @Bean
    public ResponseAdvice responseAdvice() {
        return new ResponseAdvice();
    }
}

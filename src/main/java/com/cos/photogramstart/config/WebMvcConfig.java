package com.cos.photogramstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // WEB 설정 파일

    @Value("${file.path}")
    private String sampleFolder;

// 첨부파일을 만들기 위함
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/sample/**") //jsp페이지에서 /sample/**의 주소 패턴이 나오면 발동된다
                .addResourceLocations("file:///" + sampleFolder) //위치 지정
                .setCachePeriod(60 * 10 * 6) //캐시 지속시간 60초*10 => 10분*6 => 1시간
                .resourceChain(true) //리소스 체인 발동
                .addResolver(new PathResourceResolver());
    }
}

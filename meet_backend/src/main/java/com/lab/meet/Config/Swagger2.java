package com.lab.meet.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Value("${token.header}")
    String tokenHeader;

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> params = new ArrayList<>();
        tokenPar.name(tokenHeader).description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        params.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lab.meet.Controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(params);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("组会管理系统 API v1.0")
                .description("关于一个985研究生的996组会管理的系统")
                .termsOfServiceUrl("https://github.com/wangyu1997/njtech_reader_project.git")
                .contact("人不如故")
                .version("1.0")
                .build();
    }

    @Bean
    public UiConfiguration getConfig() {
        String validateUrl = "validateUrl";
        return new UiConfiguration(validateUrl);
    }

}
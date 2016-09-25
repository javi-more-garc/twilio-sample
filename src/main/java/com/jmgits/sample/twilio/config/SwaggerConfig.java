package com.jmgits.sample.twilio.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * Created by javi.more.garc on 09/05/16.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(SWAGGER_2) //
                .select() //
                .apis(RequestHandlerSelectors.any()) //
                .paths(appPaths()) //
                .build() //
                .apiInfo(apiInfo());
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfiguration.DEFAULT;
    }

    //
    // private methods

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Twilio sample's API") //
                .description("Twilio sample's API") //
                .termsOfServiceUrl("http://springfox.io") //
                .license("Apache License Version 2.0") //
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE") //
                .version("2.0") //
                .build();//
    }

    private Predicate<String> appPaths() {

        return Predicates.or(
                regex("/api/.*")//
        );
    }

}

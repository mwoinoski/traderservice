package com.acme.acmetrade.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.swagger.web.*;

/**
 * Swagger configuration.
 * 		Raw JSON docs: http://localhost:8080/v2/api
 * 		Swagger UI:    http://localhost:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2
@ComponentScan("com.acme.acmetrade")
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("ACME Trader REST API")
            .description("ACME Trader REST API")
            .contact(new Contact("API Developer", "www.roitraining.com", "api-info@roitraining.com"))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build();
    }

    @Bean
    public Docket api() {
        ArrayList<ResponseMessage> responseMessages = 
			newArrayList(
				new ResponseMessageBuilder()   
					.code(500)
					.message("Internal server error")
					//.responseModel(new ModelRef("Error"))
					.build(),
				new ResponseMessageBuilder() 
					.code(401)
					.message("Not authorized")
					.build(),
				new ResponseMessageBuilder() 
					.code(403)
					.message("Forbidden")
					.build(),
				new ResponseMessageBuilder() 
					.code(404)
					.message("Not found")
					.build());
        
		return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)                                   
                .globalResponseMessage(RequestMethod.GET,                     
                  responseMessages)                
                .apiInfo(apiInfo())
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.acme.acmetrade.endpoints"))
                    .paths(PathSelectors.any())
        	.build();
    }

	// Swagger UI config
    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

}
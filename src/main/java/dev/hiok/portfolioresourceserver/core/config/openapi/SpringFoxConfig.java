package dev.hiok.portfolioresourceserver.core.config.openapi;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ProblemDetails;
import dev.hiok.portfolioresourceserver.api.openapi.model.PageableModelOpenApi;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
public class SpringFoxConfig {
  
  @Bean
  public Docket apiDocketV1() {
    var typeResolver = new TypeResolver();

    return new Docket(DocumentationType.OAS_30)
      .groupName("Version 1")
      .select()
        .apis(RequestHandlerSelectors.basePackage("dev.hiok.portfolioresourceserver.api"))
        .paths(PathSelectors.any())
        .build()
      .additionalModels(typeResolver.resolve(ProblemDetails.class))
      .ignoredParameterTypes(ServletWebRequest.class)
      .globalResponses(HttpMethod.POST, globalPostPutResponses())
      .globalResponses(HttpMethod.GET, globalGetResponses())
      .globalResponses(HttpMethod.PUT, globalPostPutResponses())
      .globalResponses(HttpMethod.PATCH, globalPostPutResponses())
      .globalResponses(HttpMethod.DELETE, globalDeleteResponses())
      .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
      .apiInfo(apiInfoV1())
      .tags(
        new Tag("Feedbacks", "Feedbacks"),
        new Tag("Status", "API Status")
      )
    ;
  }

  public ApiInfo apiInfoV1() {
    return new ApiInfoBuilder()
      .title("Portfolio Resource Server")
      .description("Portifio Resource Server of HiOk Dev Applications")
      .version("1")
      .contact(new Contact(
        "HiOk Dev",
        "https://github.com/rodolfoHOk",
        "hioktec@gmail.com"))
      .build();
  }

  private List<Response> globalGetResponses() {
    return Arrays.asList(
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .description("Internal Server Error")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
        .description("Not Acceptable")
        .build()
    );
  }

  private List<Response> globalPostPutResponses() {
    return Arrays.asList(
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .description("Internal Server Error")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
        .description("Not Acceptable")
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
        .description("Bad Request")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
        .description("Unsupported Media Type")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build()
    );
  }

  private List<Response> globalDeleteResponses() {
    return Arrays.asList(
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .description("Internal Server Error")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
        .description("Bad Request")
        .representation(MediaType.APPLICATION_JSON)
        .apply(getProblemDetailsReference())
        .build(),
      new ResponseBuilder()
        .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
        .description("Unauthorized")
        .build()
    );
  }

  private Consumer<RepresentationBuilder> getProblemDetailsReference() {
    return representation -> representation.model(model -> model.name("ProblemDetails")
      .referenceModel(ref -> ref.key(key -> key.qualifiedModelName(
        qMName -> qMName.name("ProblemDetails").namespace(
          "dev.hiok.portfolioresourceserver.api.exceptionHandler"
    )))));
  }

  @Bean // Para resolver o problema de serialização de OffsetDateTime
	public JacksonModuleRegistrar springfoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}	
	
	@Bean // bean necessário springfox 3.0.0 issues in springboot 2.6.0+
	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
    return new BeanPostProcessor() {

      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
          customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
        }
        return bean;
      }

      private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
        List<T> copy = mappings.stream()
          .filter(mapping -> mapping.getPatternParser() == null)
          .collect(Collectors.toList());
        mappings.clear();
        mappings.addAll(copy);
      }

      @SuppressWarnings("unchecked")
      private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
          Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
          field.setAccessible(true);
          return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          throw new IllegalStateException(e);
        }
      }
    };
	}
}

package team.waggly.backend.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SpringDocUtils
import org.springdoc.core.SwaggerUiConfigProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.security.core.annotation.AuthenticationPrincipal


@Profile("default", "dev")
@Configuration
class OpenApiConfig {
    @Bean
    fun openApi(): OpenAPI {
        val bearerAuth = SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .`in`(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)

        // Security 요청 설정
        val addSecurityItem = SecurityRequirement().addList("JWT")
        return OpenAPI()
                .info(
                        Info()
                                .title("Dalock Admin API")
                                .description("와글리 API")
                                .version("v1")
                )
                .components(Components().addSecuritySchemes("JWT", bearerAuth))
                .addSecurityItem(addSecurityItem)
    }

    @Bean
    fun groupedOpenApiUser(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/user/**", "/major/**", "/alarm/**")
                .build()
    }

    @Bean
    fun groupedOpenApiBoard(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group("Board")
                .pathsToMatch("/board/**", "/comment/**", "/reply/**", "/like/**")
                .build()
    }

    @Bean
    fun groupedOpenApiChat(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group("Chat")
                .pathsToMatch("/chat/**", "/groupchat/**")
                .build()
    }

    @Bean
    fun swaggerUiConfig(config: SwaggerUiConfigProperties): SwaggerUiConfigProperties {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthenticationPrincipal::class.java)
        config.path = "/docs/swagger-ui.html"
        config.tagsSorter = "alpha"
        config.operationsSorter = "alpha"
        return config
    }
}
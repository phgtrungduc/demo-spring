package vn.vpbanks.bo.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import vn.vpbanks.bo.api.consts.HttpHeaderKey;

import java.util.ArrayList;
import java.util.List;
/**
 * Class: SwaggerConfiguration
 * Description:
 *
 * @author quyetnv
 * @date 11/8/2025
 */
@Configuration
public class SwaggerConfiguration {

    @Autowired
    private AuthenticationConfig authConfig;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("bo-api")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addParametersItem(
                            new HeaderParameter().name(HttpHeaderKey.X_REQUEST_ID).description("Request Id dùng để trace log")
                    );
                    operation.addParametersItem(
                            new HeaderParameter().name(HttpHeaderKey.X_LANGUAGE).description("Ngôn ngữ en/vi")
                    );
                    operation.addParametersItem(
                            new HeaderParameter().name(HttpHeaderKey.X_CHANNEL).description("Kênh thao tác")
                    );
                    operation.addParametersItem(
                            new HeaderParameter().name(HttpHeaderKey.X_DEVICE).description("Device ID")
                    );
                    return operation;
                })
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        Components components = new Components();
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        
        // Cấu hình security scheme theo authMode
        AuthenticationConfig.AuthMode authMode = authConfig.getAuthModeEnum();
        
        if (authMode == AuthenticationConfig.AuthMode.BACK) {
            // Chế độ JWT/API Key
            components.addSecuritySchemes(
                    "service_sec_key",
                    new SecurityScheme()
                            .name(HttpHeaders.AUTHORIZATION)
                            .type(SecurityScheme.Type.APIKEY)
                            .in(SecurityScheme.In.HEADER)
                            .description("JWT token hoặc API Key")
            );
            securityRequirements.add(new SecurityRequirement().addList("service_sec_key"));
            
        } else if (authMode == AuthenticationConfig.AuthMode.INTERNAL) {
            // Chế độ Basic Auth
            StringBuilder userInfo = new StringBuilder("Basic Authentication. Available users:\n");
            if (authConfig.getAuthUsers() != null) {
                authConfig.getAuthUsers().forEach((username, user) -> {
                    userInfo.append("- Username: ").append(username).append("\n");
                });
            }
            
            components.addSecuritySchemes(
                    "basicAuth",
                    new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("basic")
                            .description(userInfo.toString())
            );
            securityRequirements.add(new SecurityRequirement().addList("basicAuth"));
        }
        // authMode = OFF → không thêm security schemes

        return new OpenAPI()
                .info(new Info().title("BO API info")
                        .description("BO Api documentation info")
                        .version("v1.0.0")
                        .license(new License().name("VPBANK SECURITIES CORPORATION").url("http://vpbanks.com.vn")))
                .externalDocs(new ExternalDocumentation()
                        .description("BO Api documentation")
                        .url("https://vpbanks.com.vn"))
                .components(components)
                .security(securityRequirements);

    }
}

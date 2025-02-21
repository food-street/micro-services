package com.imthath.food_street.api_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.*;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    final String USER = "user";
    final String AUTH = "auth";
    final String OTP = "otp";
    final String COURT = "court";
    final String RESTAURANT = "restaurant";
    final String IMAGE = "image";
    final String MENU = "menu";
    final String CART = "cart";
    final String ORDER = "order";
    final String PAYMENT = "payment";

    @Value("${user.service.url}") private String userServiceUrl;
    @Value("${otp.service.url}") private String otpServiceUrl;
    @Value("${court.service.url}") private String courtServiceUrl;
    @Value("${restaurant.service.url}") private String restaurantServiceUrl;
    @Value("${image.service.url}") private String imageServiceUrl;
    @Value("${menu.service.url}") private String menuServiceUrl;
    @Value("${cart.service.url}") private String cartServiceUrl;
    @Value("${order.service.url}") private String orderServiceUrl;
    @Value("${payment.service.url}") private String paymentServiceUrl;

    @Bean RouterFunction<ServerResponse> userRoute() {
        return buildServiceRoute(USER, userServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> authRoute() {
        return buildServiceRoute(AUTH, userServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> otpRoute() {
        return buildServiceRoute(OTP, otpServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> courtRoute() {
        return buildServiceRoute(COURT, courtServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> restaurantRoute() {
        return buildServiceRoute(RESTAURANT, restaurantServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> imageRoute() {
        return buildServiceRoute(IMAGE, imageServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> menuRoute() {
        return buildServiceRoute(MENU, menuServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> cartRoute() {
        return buildServiceRoute(CART, cartServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> orderRoute() {
        return buildServiceRoute(ORDER, orderServiceUrl);
    }

    @Bean RouterFunction<ServerResponse> paymentRoute() {
        return buildServiceRoute(PAYMENT, paymentServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> fallbackResponse("get"))
                .PUT("/fallbackRoute", request -> fallbackResponse("put"))
                .POST("/fallbackRoute", request -> fallbackResponse("post"))
                .DELETE("/fallbackRoute", request -> fallbackResponse("delete"))
                .build();
    }

    // Documentation routes
    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
        return buildServiceSwaggerRoute(USER, userServiceUrl);
    }

    @Bean
    RouterFunction<ServerResponse> otpServiceSwaggerRoute() {
        return buildServiceSwaggerRoute(OTP, otpServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> cartServiceSwaggerRoute() {
        return buildServiceSwaggerRoute(CART, cartServiceUrl);
    }

    private RouterFunction<ServerResponse> buildServiceRoute(String service, String baseURL) {
        return route(service)
                .route(RequestPredicates.path("/" + service + "/**"), HandlerFunctions.http(baseURL))
//                .filter(customHeaderFilter(baseURL))
                .build();
    }

    private RouterFunction<ServerResponse> buildServiceSwaggerRoute(String service, String baseURL) {
        return GatewayRouterFunctions.route(service + "_swagger")
                .route(RequestPredicates.path("/aggregate/" + service + "-service/v3/api-docs"), HandlerFunctions.http(baseURL))
                .filter(setPath("/api-docs"))
                .build();
    }

    private ServerResponse fallbackResponse(String method) {
        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service Unavailable, please try again later to " + method + " value");
    }

//     private final HandlerFilterFunction<ServerResponse, ServerResponse> customHeaderFilter(String baseUrl) {
//         return (request, next) -> {
//             var newRequest = ServerRequest
//                     .from(request)
//                     .header(Authorization.headerKey, Authorization.headerValue(baseUrl))
//                     .build();
//             return next.handle(newRequest);
//         };
//     }
}

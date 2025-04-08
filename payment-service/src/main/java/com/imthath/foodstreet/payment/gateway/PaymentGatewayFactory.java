package com.imthath.foodstreet.payment.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentGatewayFactory {
    private final List<PaymentGateway> paymentGateways;

    private Map<String, PaymentGateway> gatewayMap;

    public PaymentGateway getGateway(String gatewayName) {
        if (gatewayMap == null) {
            gatewayMap = paymentGateways.stream()
                .collect(Collectors.toMap(
                    PaymentGateway::getGatewayName,
                    Function.identity()
                ));
        }

        PaymentGateway gateway = gatewayMap.get(gatewayName.toUpperCase());
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment gateway: " + gatewayName);
        }
        return gateway;
    }
} 
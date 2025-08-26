package com.example.paymentservice.paymentGateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewaySelectorStrategy {

    @Autowired
    private RazorpayPaymentGateway razorpayPaymentGateway;

    public IPaymentGateway getBestPaymentGateway() {
        return razorpayPaymentGateway;
    }
}

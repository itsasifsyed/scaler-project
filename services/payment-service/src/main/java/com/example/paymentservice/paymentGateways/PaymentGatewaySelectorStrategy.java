package com.example.paymentservice.paymentGateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewaySelectorStrategy {

    @Autowired
    private RazorpayPaymentGateway razorpayPaymentGateway;


    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    public IPaymentGateway getBestPaymentGateway() {
//        return razorpayPaymentGateway;
        return stripePaymentGateway;
    }
}
